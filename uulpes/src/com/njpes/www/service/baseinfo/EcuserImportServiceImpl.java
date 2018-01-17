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
import com.njpes.www.dao.baseinfo.AuthMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.DistrictMapper;
import com.njpes.www.dao.baseinfo.EcUserMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.util.SequenceMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.EcUserWithBLOBs;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.service.util.DictionaryServiceI;

import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

@Service("EcUserImportService")
public class EcuserImportServiceImpl implements EcuserImportServiceI {

    Workbook wb;
    FileInputStream stream;
    boolean isRead = false;
    String provinceid = Constants.APPLICATION_PROVINCECODE;
    String cityid = Constants.APPLICATION_CITYCODE;
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
    EcUserMapper ecUserMapper;

    @Autowired
    DictionaryServiceI dicService;

    @Autowired
    RoleServiceI roleService;

    @Autowired
    AuthMapper authMapper;

    @Autowired
    AccountOrgJobMapper aojMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    SequenceMapper sequenceMapper;
    @Autowired
    private PasswordService passwordService;

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
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }

    public void importEcuserinfo(String excelUrl, Organization corg) throws Exception {
        // Map<String, String> ids = accountMapper.getAllIds();
        List<EcUserWithBLOBs> existTeachers = ecUserMapper.selectEcUsersByParentOrgid(corg.getId(), corg);

        Map<String, EcUserWithBLOBs> codeClsMap = new HashMap<String, EcUserWithBLOBs>();
        for (EcUserWithBLOBs teacher : existTeachers) {
            byte[] teacherByte = SerializationUtils.serialize(teacher);
            codeClsMap.put(teacher.getGh(), (EcUserWithBLOBs) SerializationUtils.deserialize(teacherByte));
        }

        List<EcUserWithBLOBs> teachers_to_insert = new ArrayList<EcUserWithBLOBs>();
        List<EcUserWithBLOBs> teachers_to_update = new ArrayList<EcUserWithBLOBs>();

        List<Account> accounts_to_insert = new ArrayList<Account>();
        List<Account> accounts_to_update = new ArrayList<Account>();
        List<Auth> auths = new ArrayList<Auth>();
        List<AccountOrgJob> aojs = new ArrayList<AccountOrgJob>();
        List<Account> acts = null;

        String sheetName = "教委用户基础信息";
        Map<String, Integer> cols = new HashMap<String, Integer>() {
            {
                put("idCol", 1);
                put("ghCol", 2);
                put("xmCol", 3);
                put("ywxmCol", 4);
                put("xmpyCol", 5);
                put("cymCol", 6);
                put("xbmCol", 7);
                put("csrqCol", 8);
                put("csdmCol", 9);
                put("jgCol", 10);
                put("mzmCol", 11);
                put("gjdqmCol", 12);
                put("sfzjlxmCol", 13);
                put("sfzjhCol", 14);
                put("hyzkmCol", 15);
                put("gatqwmCol", 16);
                put("zzmmmCol", 17);
                put("jkzkmCol", 18);
                put("xyzjmCol", 19);
                put("xxmCol", 20);
                put("zpCol", 21);
                put("sfzjyxqCol", 22);
                put("jtzzCol", 23);
                put("xzzCol", 24);
                put("hkszdCol", 25);
                put("hkxzmCol", 26);
                put("xlmCol", 27);
                put("gznyCol", 28);
                put("dabhCol", 29);
                put("txdzCol", 30);
                put("lxdhCol", 31);
                put("yzbmCol", 32);
                put("dzxxCol", 33);
                put("gwzymCol", 34);
                put("bjxxCol", 35);
                put("jsCol", 36);
            }
        };
        // 教师信息
        int idCol = cols.get("idCol");
        int ghCol = cols.get("ghCol");
        int xmCol = cols.get("xmCol");
        int ywxmCol = cols.get("ywxmCol");
        int xmpyCol = cols.get("xmpyCol");
        int cymCol = cols.get("cymCol");
        int xbmCol = cols.get("xbmCol");
        int csrqCol = cols.get("csrqCol");
        int csdmCol = cols.get("csdmCol");
        int jgCol = cols.get("jgCol");
        int mzmCol = cols.get("mzmCol");
        int gjdqmCol = cols.get("gjdqmCol");
        int sfzjlxmCol = cols.get("sfzjlxmCol");
        int sfzjhCol = cols.get("sfzjhCol");
        int hyzkmCol = cols.get("hyzkmCol");
        int gatqwmCol = cols.get("gatqwmCol");
        int zzmmmCol = cols.get("zzmmmCol");
        int jkzkmCol = cols.get("jkzkmCol");
        int xyzjmCol = cols.get("xyzjmCol");
        int xxmCol = cols.get("xxmCol");
        int zpCol = cols.get("zpCol");
        int sfzjyxqCol = cols.get("sfzjyxqCol");
        int jtzzCol = cols.get("jtzzCol");
        int xzzCol = cols.get("xzzCol");
        int hkszdCol = cols.get("hkszdCol");
        int hkxzmCol = cols.get("hkxzmCol");
        int xlmCol = cols.get("xlmCol");
        int gznyCol = cols.get("gznyCol");
        int dabhCol = cols.get("dabhCol");
        int txdzCol = cols.get("txdzCol");
        int lxdhCol = cols.get("lxdhCol");
        int yzbmCol = cols.get("yzbmCol");
        int dzxxCol = cols.get("dzxxCol");
        int gwzymCol = cols.get("gwzymCol");
        int bjxxCol = cols.get("bjxxCol");
        int jsCol = cols.get("jsCol");

        Long nextId = -1l;
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
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            throw new Exception("模板解析出错，请检查模板");
        }
        Sheet sheet = wb.getSheet(sheetName);
        int rowCnt = sheet.getPhysicalNumberOfRows();
        Long accid = accountMapper.getMaxId() + 1;
        sequenceMapper.updateSeqId(accid, "account");

        // 开启事务
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);

        try {

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                System.out.println(i);
                int insert_or_update = 0; // 1-insert,2-update
                Row row = sheet.getRow(i);
                if (null == row || null == row.getCell(idCol - 1)) {
                    continue;
                }

                String id = null;
                if (null != row.getCell(idCol - 1)) {
                    row.getCell(idCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                    id = row.getCell(idCol - 1).getStringCellValue(); // 编号
                    if (StringUtils.isNotEmpty(id)) {

                        String gh = null;
                        if (null != row.getCell(ghCol - 1)) {
                            row.getCell(ghCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            gh = row.getCell(ghCol - 1).getStringCellValue(); // 工号
                            if (null != gh) {
                                gh = gh.trim();
                            } else {
                                continue;
                            }
                        }

                        String xm = null;
                        if (null != row.getCell(xmCol - 1)) {
                            row.getCell(xmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xm = row.getCell(xmCol - 1).getStringCellValue(); // 名称
                            if (null != xm)
                                xm = xm.trim();
                        }

                        String ywxm = null;
                        if (null != row.getCell(ywxmCol - 1)) {
                            row.getCell(ywxmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            ywxm = row.getCell(ywxmCol - 1).getStringCellValue(); // 英文姓名
                        }

                        String cym = null;
                        if (null != row.getCell(cymCol - 1)) {
                            row.getCell(cymCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            cym = row.getCell(cymCol - 1).getStringCellValue(); // 曾用名
                        }

                        String xbm = null;
                        if (null != row.getCell(xbmCol - 1)) {
                            row.getCell(xbmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xbm = row.getCell(xbmCol - 1).getStringCellValue(); // 性别码
                        }

                        String csrq = null;
                        if (null != row.getCell(csrqCol - 1)) {
                            row.getCell(csrqCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            csrq = row.getCell(csrqCol - 1).getStringCellValue(); // 出生日期
                            if (null != csrq)
                                csrq = csrq.trim();
                        }

                        String csdm = null;
                        if (null != row.getCell(csdmCol - 1)) {
                            row.getCell(csdmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            csdm = row.getCell(csdmCol - 1).getStringCellValue(); // 出生地码
                        }

                        String jg = null;
                        if (null != row.getCell(jgCol - 1)) {
                            row.getCell(jgCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            jg = row.getCell(jgCol - 1).getStringCellValue(); // 籍贯
                        }
                        String xmpy = null;
                        if (null != row.getCell(xmpyCol - 1)) {
                            row.getCell(xmpyCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xmpy = row.getCell(xmpyCol - 1).getStringCellValue(); // 姓名拼音
                        }

                        String mzm = null;
                        if (null != row.getCell(mzmCol - 1)) {
                            row.getCell(mzmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            mzm = row.getCell(mzmCol - 1).getStringCellValue(); // 民族码
                        }

                        String gjdqm = null;
                        if (null != row.getCell(gjdqmCol - 1)) {
                            row.getCell(gjdqmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            gjdqm = row.getCell(gjdqmCol - 1).getStringCellValue(); // 国籍、地区码
                        }

                        String sfzjlxm = null;
                        if (null != row.getCell(sfzjlxmCol - 1)) {
                            row.getCell(sfzjlxmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            sfzjlxm = row.getCell(sfzjlxmCol - 1).getStringCellValue(); // 身份证件类型码
                        }

                        String sfzjh = null;
                        if (null != row.getCell(sfzjhCol - 1)) {
                            row.getCell(sfzjhCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            sfzjh = row.getCell(sfzjhCol - 1).getStringCellValue(); // 身份证件号
                        }

                        String hyzkm = null;
                        if (null != row.getCell(hyzkmCol - 1)) {
                            row.getCell(hyzkmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            hyzkm = row.getCell(hyzkmCol - 1).getStringCellValue(); // 婚姻状况码
                        }

                        String gatqwm = null;
                        if (null != row.getCell(gatqwmCol - 1)) {
                            row.getCell(gatqwmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            gatqwm = row.getCell(gatqwmCol - 1).getStringCellValue(); // 港澳台侨外码
                        }

                        String zzmmm = null;
                        if (null != row.getCell(zzmmmCol - 1)) {
                            row.getCell(zzmmmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            zzmmm = row.getCell(zzmmmCol - 1).getStringCellValue(); // 政治面貌码
                        }

                        String jkzkm = null;
                        if (null != row.getCell(jkzkmCol - 1)) {
                            row.getCell(jkzkmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            jkzkm = row.getCell(jkzkmCol - 1).getStringCellValue(); // 健康状况码
                        }

                        String xyzjm = null;
                        if (null != row.getCell(xyzjmCol - 1)) {
                            row.getCell(xyzjmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xyzjm = row.getCell(xyzjmCol - 1).getStringCellValue(); // 信仰宗教码
                        }

                        String xxm = null;
                        if (null != row.getCell(xxmCol - 1)) {
                            row.getCell(xxmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xxm = row.getCell(xxmCol - 1).getStringCellValue(); // 血型码
                        }

                        String zp = null;
                        if (null != row.getCell(zpCol - 1)) {
                            row.getCell(zpCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            zp = row.getCell(zpCol - 1).getStringCellValue(); // 照片
                        }

                        String sfzjyxq = null;
                        if (null != row.getCell(sfzjyxqCol - 1)) {
                            row.getCell(sfzjyxqCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            sfzjyxq = row.getCell(sfzjyxqCol - 1).getStringCellValue(); // 身份证件有效期
                        }

                        String jtzz = null;
                        if (null != row.getCell(jtzzCol - 1)) {
                            row.getCell(jtzzCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            jtzz = row.getCell(jtzzCol - 1).getStringCellValue(); // 家庭住址
                        }

                        String xzz = null;
                        if (null != row.getCell(xzzCol - 1)) {
                            row.getCell(xzzCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xzz = row.getCell(xzzCol - 1).getStringCellValue(); // 现住址
                        }

                        String hkszd = null;
                        if (null != row.getCell(hkszdCol - 1)) {
                            row.getCell(hkszdCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            hkszd = row.getCell(hkszdCol - 1).getStringCellValue(); // 户口所在地
                        }

                        String hkxzm = null;
                        if (null != row.getCell(hkxzmCol - 1)) {
                            row.getCell(hkxzmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            hkxzm = row.getCell(hkxzmCol - 1).getStringCellValue(); // 户口性质码
                        }

                        String xlm = null;
                        if (null != row.getCell(xlmCol - 1)) {
                            row.getCell(xlmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            xlm = row.getCell(xlmCol - 1).getStringCellValue(); // 学历码
                        }

                        String gzny = null;
                        if (null != row.getCell(gznyCol - 1)) {
                            row.getCell(gznyCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            gzny = row.getCell(gznyCol - 1).getStringCellValue(); // 参加工作年月
                        }

                        String dabh = null;
                        if (null != row.getCell(dabhCol - 1)) {
                            row.getCell(dabhCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            dabh = row.getCell(dabhCol - 1).getStringCellValue(); // 档案编号
                        }

                        String txdz = null;
                        if (null != row.getCell(txdzCol - 1)) {
                            row.getCell(txdzCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            txdz = row.getCell(txdzCol - 1).getStringCellValue(); // 通信地址
                        }

                        String lxdh = null;
                        if (null != row.getCell(lxdhCol - 1)) {
                            row.getCell(lxdhCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            lxdh = row.getCell(lxdhCol - 1).getStringCellValue(); // 联系电话
                        }

                        String yzbm = null;
                        if (null != row.getCell(yzbmCol - 1)) {
                            row.getCell(yzbmCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            yzbm = row.getCell(yzbmCol - 1).getStringCellValue(); // 邮政编码
                        }

                        String dzxx = null;
                        if (null != row.getCell(dzxxCol - 1)) {
                            row.getCell(dzxxCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            dzxx = row.getCell(dzxxCol - 1).getStringCellValue(); // 电子信箱
                        }

                        String gwzym = null;
                        if (null != row.getCell(gwzymCol - 1)) {
                            row.getCell(gwzymCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            gwzym = row.getCell(gwzymCol - 1).getStringCellValue(); // 岗位职业码
                        }

                        String bjxx = null;
                        if (null != row.getCell(bjxxCol - 1)) {
                            row.getCell(bjxxCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            bjxx = row.getCell(bjxxCol - 1).getStringCellValue(); // 背景信息
                        }

                        String js = null;
                        if (null != row.getCell(jsCol - 1)) {
                            row.getCell(jsCol - 1).setCellType(Cell.CELL_TYPE_STRING);
                            js = row.getCell(jsCol - 1).getStringCellValue(); // 角色
                        }

                        // 查看是否有相同的教师记录
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("idcard", sfzjh);
                        param.put("gh", gh);
                        param.put("username", gh);
                        acts = accountMapper.selectTeacherByUniqueCol(param);
                        if (null == acts || acts.size() == 0) {
                            insert_or_update = 1; // insert
                        } else if (null != acts && acts.size() == 1) {
                            insert_or_update = 2; // update
                        } else {
                            throw new Exception(acts.get(0).getUsername() + "用户信息冲突，请联系管理员处理");
                        }

                        // 创建用户，写account表，返回id，写入student的account_id
                        Account record = new Account();
                        if (sfzjh.length() <= 18)
                            record.setIdcard(sfzjh); // 身份证
                        // record.setId(Long.parseLong(id)); //编号
                        record.setUsername(gh);
                        // record.setPassword("21232f297a57a5a743894a0e4a801fc3");
                        // //密码，默认admin
                        record.setPassword(passwordService.encryptPassword(null, Constants.DEFAULT_PASSWORD));
                        record.setCreateTime(Calendar.getInstance().getTime()); // 时间
                        record.setUpdateTime(Calendar.getInstance().getTime());
                        record.setTypeFlag(4); // 类型是教委老师
                        record.setState(1); // 正常状态
                        record.setAdmin(0);
                        record.setTheme("theme1");

                        EcUserWithBLOBs teacher = new EcUserWithBLOBs();

                        teacher.setGh(gh);
                        teacher.setXm(xm);
                        teacher.setYwxm(ywxm);
                        teacher.setXmpy(xmpy);
                        teacher.setCym(cym);
                        teacher.setXbm(xbm);
                        teacher.setCsrq(csrq);
                        teacher.setCsdm(csdm);
                        teacher.setJg(jg);
                        teacher.setMzm(mzm);
                        teacher.setGjdqm(gjdqm);
                        teacher.setSfzjlxm(sfzjlxm);
                        teacher.setSfzjh(sfzjh);
                        teacher.setHyzkm(hyzkm);
                        teacher.setGatqwm(gatqwm);
                        teacher.setZzmmm(zzmmm);
                        teacher.setJkzkm(jkzkm);
                        teacher.setXyzjm(xyzjm);
                        teacher.setXxm(xxm);
                        teacher.setZp(zp.getBytes());
                        teacher.setSfzjyxq(sfzjyxq);
                        teacher.setJgh(corg.getCode());
                        teacher.setJtzz(jtzz);
                        teacher.setXzz(xzz);
                        teacher.setHkszd(hkszd);
                        teacher.setHkxzm(hkxzm);
                        teacher.setXlm(xlm);
                        teacher.setGzny(gzny);
                        teacher.setDabh(dabh);
                        teacher.setTxdz(txdz);
                        teacher.setLxdh(lxdh);
                        teacher.setYzbm(yzbm);
                        teacher.setDzxx(dzxx);
                        teacher.setGwzym(gwzym);
                        teacher.setAccountId(accid);
                        teacher.setBjxx(bjxx);

                        // 写auth表，默认教师用户和角色
                        Auth auth = new Auth();
                        auth.setOrgId(corg.getId());
                        auth.setUserId(accid);
                        Role role = new Role();
                        role.setRoleName(js);
                        List<Role> roles = roleService.selectBySelective(role);

                        if (roles.size() > 0 && null != roles.get(0)) {
                            auth.setRoleId(roles.get(0).getId()); // 教师角色
                        }
                        auth.setAuthType("user");

                        // 写user_org_job表，写教委老师和教委映射关系
                        AccountOrgJob aoj = new AccountOrgJob();
                        aoj.setUserId(accid);
                        aoj.setOrgId(corg.getId());

                        if (insert_or_update == 1) {
                            teachers_to_insert.add(teacher);
                            accounts_to_insert.add(record);
                            aojs.add(aoj);
                            auths.add(auth);
                            accid = accid + 1;
                            codeClsMap.put(gh, teacher);
                        } else if (insert_or_update == 2) {
                            record.setId(acts.get(0).getId());
                            accounts_to_update.add(record);
                            teacher.setAccountId(acts.get(0).getId());
                            teachers_to_update.add(teacher);
                            codeClsMap.put(gh, teacher);
                        }
                    }
                }

                if (accounts_to_insert.size() >= 1000 || i == rowCnt - 1) {
                    if (accounts_to_insert.size() > 0) {
                        accountMapper.insertBatch(accounts_to_insert);
                        accounts_to_insert = new ArrayList<Account>();
                    }
                }
                if (accounts_to_update.size() >= 1000 || i == rowCnt - 1) {
                    if (accounts_to_update.size() > 0) {
                        accountMapper.updateBatch(accounts_to_update);
                        accounts_to_update = new ArrayList<Account>();
                    }
                }
                if (teachers_to_insert.size() >= 1000 || i == rowCnt - 1) {
                    if (teachers_to_insert.size() > 0) {
                        ecUserMapper.insertBatch(teachers_to_insert);
                        teachers_to_insert = new ArrayList<EcUserWithBLOBs>();
                    }
                }
                if (teachers_to_update.size() >= 1000 || i == rowCnt - 1) {
                    if (teachers_to_update.size() > 0) {
                        ecUserMapper.updateBatch(teachers_to_update);
                        teachers_to_update = new ArrayList<EcUserWithBLOBs>();
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

            // 如果是最后几行continue跳出循环，处理未提交的数据
            if (accounts_to_insert.size() > 0) {
                accountMapper.insertBatch(accounts_to_insert);
                accounts_to_insert = new ArrayList<Account>();
            }
            if (accounts_to_update.size() > 0) {
                accountMapper.updateBatch(accounts_to_update);
                accounts_to_update = new ArrayList<Account>();
            }
            if (teachers_to_insert.size() > 0) {
                ecUserMapper.insertBatch(teachers_to_insert);
                teachers_to_insert = new ArrayList<EcUserWithBLOBs>();
            }
            if (teachers_to_update.size() > 0) {
                ecUserMapper.updateBatch(teachers_to_update);
                teachers_to_update = new ArrayList<EcUserWithBLOBs>();
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
            e.printStackTrace();
            txManager.rollback(status);

        } finally {
            stream.close();
        }
    }

}
