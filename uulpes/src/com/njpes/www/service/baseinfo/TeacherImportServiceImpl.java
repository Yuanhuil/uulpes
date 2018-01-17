package com.njpes.www.service.baseinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.AccountMapper;
import com.njpes.www.dao.baseinfo.AccountOrgJobMapper;
import com.njpes.www.dao.baseinfo.AttrMapper;
import com.njpes.www.dao.baseinfo.AuthMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.DistrictMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.util.SequenceMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.attr.AttrCategory;
import com.njpes.www.entity.baseinfo.attr.AttrDefine;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.service.util.DictionaryServiceI;

import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

@Service("TeacherImportService")
public class TeacherImportServiceImpl implements TeacherImportServiceI {

    Workbook wb;
    FileInputStream stream;
    boolean isRead = false;
    String provinceid = Constants.APPLICATION_PROVINCECODE;
    String cityid = Constants.APPLICATION_CITYCODE;
    String cateName = "教师信息";
    String sheetName = "teacher";

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ClassSchoolMapper classMapper;

    @Autowired
    OrganizationMapper orgMapper;

    @Autowired
    SchoolMapper schoolMapper;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    DictionaryServiceI dicService;

    @Autowired
    RoleServiceI roleService;

    @Autowired
    AuthMapper authMapper;

    @Autowired
    AccountOrgJobMapper aojMapper;

    @Autowired
    SequenceMapper sequenceMapper;

    @Autowired
    AttrMapper attrMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    AttrDefineManagerImpl attrService;
    @Autowired
    private PasswordService passwordService;

    /**
     * 根据目录查找目录id
     * @param cateName
     *            教师信息目录
     * @return
     */
    public Integer getCateId(String cateName) {
        AttrCategory cate = attrMapper.getAttrCateByTitle(cateName);
        if (null != cate) {
            return cate.getId();
        }
        return null;
    }

    /**
     * 根据目录获得所有定义的背景信息
     * @param cateId
     * @return attrIdName是属性名字为key，id为value的map，返回结果是属性名字为key，如果为select取值范围key，value的map为value的map
     */
    public Map<String, Map<String, String>> getCateAttrs(Integer cateId, Map<String, String> attrIdName) {
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        AttrDefine param = new AttrDefine();
        param.setCateId(cateId);
        param.setType("select");
        List<AttrDefine> attrs = attrMapper.selectByUserParam(param);
        for (AttrDefine attr : attrs) {
            if (null == attrIdName)
                attrIdName = new HashMap<String, String>();
            attrIdName.put(attr.getLabel(), attr.getId());
            if ("select".equals(attr.getType())) {
                String optsStr = attr.getOptDataSource();
                if (StringUtils.isNotEmpty(optsStr)) {
                    String[] optsArray = optsStr.split(";");
                    Map<String, String> opts = new HashMap<String, String>();
                    for (String opt : optsArray) {
                        String[] kv = opt.split("=");
                        opts.put(kv[1].trim(), kv[0].trim());
                    }
                    result.put(attr.getLabel(), opts);
                }
            }
        }
        return result;
    }

    public void read2003Excel(InputStream stream) {
        try {
            wb = WorkbookUtils.openWorkbook(stream);
            isRead = true;
        } catch (ExcelException e) {
            System.out.println(e.getMessage());
        }
    }

    public void read2007Excel(InputStream stream) {

        try {
            wb = new XSSFWorkbook(stream);
            isRead = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 从标题行读取中文，对比获得各个属性对应的模板里的列数
     * @param row
     * @param titlecol
     * @return {"idCol":1,...}
     */
    private Map<String, Integer> find(Row row, Map<String, String> titlecol) {
        Map<String, Integer> colindex = new HashMap<String, Integer>();
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            Cell cell = row.getCell(i);
            if (null != cell) {
                row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                String content = row.getCell(i).getStringCellValue(); // 编号
                if (titlecol.containsKey(content)) {
                    colindex.put(titlecol.get(content), i + 1);
                }
            }
        }
        return colindex;
    }

    public void importTeahinfo(String excelUrl, Organization corg) throws Exception {
        // Map<String, String> ids = accountMapper.getAllIds();
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        Map<String, String> attrIdName = new HashMap<String, String>();
        result = this.getCateAttrs(6, attrIdName); // 教师的属性
        List<TeacherWithBLOBs> existTeachers = teacherMapper.getTeachersInSchool(corg.getCode());

        Map<String, TeacherWithBLOBs> codeClsMap = new HashMap<String, TeacherWithBLOBs>();
        for (TeacherWithBLOBs teacher : existTeachers) {
            byte[] teacherByte = SerializationUtils.serialize(teacher);
            codeClsMap.put(teacher.getGh(), (TeacherWithBLOBs) SerializationUtils.deserialize(teacherByte));
        }

        List<TeacherWithBLOBs> teachers_to_insert = new ArrayList<TeacherWithBLOBs>();

        List<Account> accounts_to_insert = new ArrayList<Account>();
        List<Auth> auths = new ArrayList<Auth>();
        List<AccountOrgJob> aojs = new ArrayList<AccountOrgJob>();
        // 中英文对照
        Map<String, String> cnens = new HashMap<String, String>() {
            {
                // 必填字段
                put("编号*", "idCol");
                put("工号*", "ghCol");
                put("姓名*", "xmCol");
                put("性别*", "xbmCol");
                put("身份证件类型*", "sfzjlxmCol");
                put("身份证件号*", "sfzjhCol");
                put("角色名称*", "jsCol");
                // 非必填字段
                put("籍贯", "jgCol");
                put("民族", "mzmCol");
                put("婚姻状况", "hyzkmCol");
                put("联系电话", "lxdhCol");
                put("主要任课学段", "zyrkxdCol");
            }
        };

        try {
            stream = new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            String fileName = excelUrl.substring(prefix + 1);
            if (StringUtils.endsWith(fileName, ".xls"))
                read2003Excel(stream);
            else if (StringUtils.endsWith(fileName, ".xlsx"))
                read2007Excel(stream);
        } catch (Exception e) {
            try {
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new Exception("模板解析出错，请检查模板");
        }

        Sheet sheet = wb.getSheet(sheetName);

        // 标题行
        Row titlerow = sheet.getRow(1);

        Map<String, Integer> colIndex = find(titlerow, cnens);

        // 教师信息
        int idCol = colIndex.get("idCol");
        int ghCol = colIndex.get("ghCol");
        int xmCol = colIndex.get("xmCol");
        int xbmCol = colIndex.get("xbmCol");
        int sfzjlxmCol = colIndex.get("sfzjlxmCol");
        int sfzjhCol = colIndex.get("sfzjhCol");
        int jsCol = colIndex.get("jsCol");
        int jgCol = colIndex.get("jgCol");
        int mzmCol = colIndex.get("mzmCol");
        int hyzkmCol = colIndex.get("hyzkmCol");
        int lxdhCol = colIndex.get("lxdhCol");
        int zyrkxdCol = colIndex.get("zyrkxdCol");

        int rowCnt = sheet.getPhysicalNumberOfRows();
        Long accid = accountMapper.getMaxId() + 1;
        sequenceMapper.updateSeqId(accid, "account");

        // 开启事务
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);

        try {
            for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
                System.out.println(i);
                Row row = sheet.getRow(i);
                if (null == row || null == row.getCell(idCol - 1)) {
                    continue;
                }

                String id = null;
                row.getCell(idCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                id = row.getCell(idCol - 1).getStringCellValue(); // 编号
                if (null != row.getCell(idCol - 1) && StringUtils.isNotEmpty(id)) {
                    String gh = null;
                    if (null != row.getCell(ghCol - 1)) {
                        row.getCell(ghCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        gh = row.getCell(ghCol - 1).getStringCellValue(); // 编码
                        if (null != gh) {
                            gh = gh.trim();
                        } else {
                            throw new Exception("请填写第" + (i + 1) + "行工号");
                        }
                    }

                    String xm = null;
                    if (null != row.getCell(xmCol - 1)) {
                        row.getCell(xmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        xm = row.getCell(xmCol - 1).getStringCellValue(); // 名称
                        if (null != xm) {
                            xm = xm.trim();
                        } else {
                            throw new Exception("请填写第" + (i + 1) + "行教师姓名");
                        }
                    }

                    String xbm = null;
                    if (null != row.getCell(xbmCol - 1)) {
                        row.getCell(xbmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        xbm = row.getCell(xbmCol - 1).getStringCellValue(); // 性别码
                        if (StringUtils.isNotEmpty(xbm)) {
                            if (xbm.equals("男"))
                                xbm = "1";
                            else
                                xbm = "2";
                        } else {
                            throw new Exception("请填写第" + (i + 1) + "行教师性别");
                        }
                    }

                    String sfzjlxm = null;
                    if (null != row.getCell(sfzjlxmCol - 1)) {
                        row.getCell(sfzjlxmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        sfzjlxm = row.getCell(sfzjlxmCol - 1).getStringCellValue(); // 身份证件类型码
                        if (sfzjlxm.equals("居民身份证") || sfzjlxm.equals("身份证")) {
                            sfzjlxm = "1";
                        } else if (sfzjlxm.equals("军官证")) {
                            sfzjlxm = "2";
                        } else if (sfzjlxm.equals("士兵证")) {
                            sfzjlxm = "3";
                        } else if (sfzjlxm.equals("文职干部证")) {
                            sfzjlxm = "4";
                        } else if (sfzjlxm.equals("部队离退休证")) {
                            sfzjlxm = "5";
                        } else if (sfzjlxm.equals("香港特区护照/身份证明")) {
                            sfzjlxm = "6";
                        } else if (sfzjlxm.equals("澳门特区护照/身份证明")) {
                            sfzjlxm = "7";
                        } else if (sfzjlxm.equals("台湾居民来往大陆通行证")) {
                            sfzjlxm = "8";
                        } else if (sfzjlxm.equals("境外永久居住证")) {
                            sfzjlxm = "9";
                        } else if (sfzjlxm.equals("护照")) {
                            sfzjlxm = "A";
                        } else if (sfzjlxm.equals("户口簿")) {
                            sfzjlxm = "B";
                        } else if (sfzjlxm.equals("其他")) {
                            sfzjlxm = "Z";
                        } else {
                            if (result.get("身份证件类型") == null || !result.get("身份证件类型").containsKey(sfzjlxm))
                                throw new Exception("请根据填写说明正确填写第" + (i + 1) + "行身份证件类型");
                        }
                    }

                    String sfzjh = null;
                    if (null != row.getCell(sfzjhCol - 1)) {
                        row.getCell(sfzjhCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        sfzjh = row.getCell(sfzjhCol - 1).getStringCellValue(); // 身份证件号
                        int count = accountMapper.selectUserByUniqueCol(sfzjh);
                        if (count > 0) {
                            throw new Exception("身份证件号为'" + sfzjh + "'的用户信息冲突，请联系管理员处理");
                        }
                    } else {
                        throw new Exception("请填写第" + (i + 1) + "行身份证件号");
                    }

                    String js = null;
                    if (null != row.getCell(jsCol - 1)) {
                        row.getCell(jsCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        js = row.getCell(jsCol - 1).getStringCellValue(); // 角色名称
                    }

                    String jg = null;
                    if (null != row.getCell(jgCol - 1)) {
                        row.getCell(jgCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        jg = row.getCell(jgCol - 1).getStringCellValue(); // 籍贯
                    }

                    String mzm = null;
                    if (null != row.getCell(mzmCol - 1)) {
                        row.getCell(mzmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        mzm = row.getCell(mzmCol - 1).getStringCellValue(); // 民族码
                        if (result.get("民族") != null && result.get("民族").containsKey(mzm))
                            mzm = result.get("民族").get(mzm);
                        else
                            mzm = null;
                    }

                    String hyzkm = null;
                    if (null != row.getCell(hyzkmCol - 1)) {
                        row.getCell(hyzkmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        hyzkm = row.getCell(hyzkmCol - 1).getStringCellValue(); // 婚姻状况码
                        if (result.get("婚姻状况") != null && result.get("婚姻状况").containsKey(hyzkm))
                            hyzkm = result.get("婚姻状况").get(hyzkm);
                        else
                            hyzkm = null;
                    }

                    String lxdh = null;
                    if (null != row.getCell(lxdhCol - 1)) {
                        row.getCell(lxdhCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        lxdh = row.getCell(lxdhCol - 1).getStringCellValue(); // 联系电话
                    }

                    String zyrkxd = null;
                    if (null != row.getCell(zyrkxdCol - 1)) {
                        row.getCell(zyrkxdCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                        zyrkxd = row.getCell(zyrkxdCol - 1).getStringCellValue(); // 主要任课学段
                    }

                    // 创建用户，写account表，返回id，写入student的account_id
                    Account record = new Account();
                    if (sfzjh.length() <= 18)
                        record.setIdcard(sfzjh); // 身份证
                    // record.setId(Long.parseLong(id)); //编号
                    record.setUsername(gh);
                    record.setRealname(xm);
                    // record.setPassword("21232f297a57a5a743894a0e4a801fc3");
                    // //密码，默认admin
                    record.setPassword(passwordService.encryptPassword(null, Constants.DEFAULT_PASSWORD));
                    record.setCreateTime(Calendar.getInstance().getTime()); // 时间
                    record.setUpdateTime(Calendar.getInstance().getTime());
                    record.setTypeFlag(2); // 类型是老师
                    record.setState(1); // 正常状态
                    record.setAdmin(0);
                    record.setTheme("theme1");

                    Map<String, String> attrVals = new HashMap<String, String>();
                    TeacherWithBLOBs teacher = new TeacherWithBLOBs();
                    teacher.setGh(gh);
                    attrVals.put("工号", gh);
                    teacher.setXm(xm);
                    attrVals.put("姓名", xm);
                    teacher.setXbm(xbm);
                    attrVals.put("性别", xbm);
                    teacher.setJg(jg);
                    attrVals.put("籍贯", jg);
                    teacher.setMzm(mzm);
                    attrVals.put("民族码", mzm);
                    teacher.setSfzjlxm(sfzjlxm);
                    attrVals.put("身份证件类型码", sfzjlxm);
                    teacher.setSfzjh(sfzjh);
                    attrVals.put("身份证件号", sfzjh);
                    teacher.setHyzkm(hyzkm);
                    attrVals.put("婚姻状况码", hyzkm);
                    teacher.setJgh(corg.getCode());
                    teacher.setLxdh(lxdh);
                    attrVals.put("联系电话", lxdh);
                    teacher.setZyrkxd(zyrkxd);
                    attrVals.put("主要任课学段", zyrkxd);
                    teacher.setAccountId(accid);
                    String bjxxObj = "";
                    // 给教师背景字段赋值
                    teacher.setAttrsValsWithLabel(attrVals);
                    List<FieldValue> attrs = teacher.getAttrs();
                    for (FieldValue fv : attrs) {
                        AttrDefine field = (AttrDefine) fv.getField();
                        String label = field.getLabel();
                        if (cnens.containsKey(label) && StringUtils.isNotEmpty(fv.getValue())) {
                            // bjxxObj.put(fv.getId(),fv.getValue());
                            if (!"".equals(bjxxObj)) {
                                bjxxObj = bjxxObj + " ";
                            }
                            bjxxObj = bjxxObj + fv.getId() + "=" + fv.getValue();
                        }
                    }
                    teacher.setBjxx(bjxxObj.toString());
                    // 写auth表，默认教师用户和角色
                    Auth auth = new Auth();
                    auth.setOrgId(corg.getId());
                    auth.setUserId(accid);
                    Role role = new Role();
                    role.setRoleName(js);
                    List<Role> roles = roleService.selectBySelective(role);

                    if (roles.size() > 0 && null != roles.get(0)) {
                        auth.setRoleId(roles.get(0).getId()); // 教师角色
                    } else {
                        throw new Exception("请根据填写说明正确填写第" + (i + 1) + "行角色名称");
                    }
                    // 写完教师用户的角色就可以把岗位职责删除掉了
                    teacher.setGwzym("");
                    auth.setAuthType("user");

                    // 写user_org_job表，写学生学校映射关系
                    AccountOrgJob aoj = new AccountOrgJob();
                    aoj.setUserId(accid);
                    aoj.setOrgId(corg.getId());

                    teachers_to_insert.add(teacher);
                    accounts_to_insert.add(record);
                    aojs.add(aoj);
                    auths.add(auth);
                    accid = accid + 1;
                    codeClsMap.put(gh, teacher);
                }

                if (accounts_to_insert.size() >= 1000 || i == rowCnt - 1) {
                    if (accounts_to_insert.size() > 0) {
                        accountMapper.insertBatch(accounts_to_insert);
                        accounts_to_insert = new ArrayList<Account>();
                    }
                }
                if (teachers_to_insert.size() >= 1000 || i == rowCnt - 1) {
                    if (teachers_to_insert.size() > 0) {
                        teacherMapper.insertBatch(teachers_to_insert);
                        teachers_to_insert = new ArrayList<TeacherWithBLOBs>();
                    }
                }

                if (auths.size() == 1000 || i == rowCnt - 1) {
                    if (auths.size() > 0) {
                        authMapper.insertBatch(auths);
                        auths = new ArrayList<Auth>();
                    }
                }

                if (aojs.size() == 1000 || i == rowCnt - 1) {
                    if (aojs.size() > 0) {
                        aojMapper.insertBatch(aojs);
                        aojs = new ArrayList<AccountOrgJob>();
                    }
                }

            }

            // 如果最后是continue跳出循环的，则处理未提交的数据
            if (accounts_to_insert.size() > 0) {
                accountMapper.insertBatch(accounts_to_insert);
                accounts_to_insert = new ArrayList<Account>();
            }
            if (teachers_to_insert.size() > 0) {
                teacherMapper.insertBatch(teachers_to_insert);
                teachers_to_insert = new ArrayList<TeacherWithBLOBs>();
            }

            if (auths.size() > 0) {
                authMapper.insertBatch(auths);
                auths = new ArrayList<Auth>();
            }

            if (aojs.size() > 0) {
                aojMapper.insertBatch(aojs);
                aojs = new ArrayList<AccountOrgJob>();
            }

            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            throw new Exception(e.getMessage());
        } finally {
            stream.close();
        }
    }

}
