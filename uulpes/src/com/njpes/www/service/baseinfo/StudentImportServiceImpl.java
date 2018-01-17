package com.njpes.www.service.baseinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.ParentMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.util.SequenceMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.attr.AttrDefine;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.service.util.DictionaryServiceI;

import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

@Service("StudentImportService")
public class StudentImportServiceImpl implements StudentImportService {

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
    StudentMapper stuMapper;

    @Autowired
    ParentMapper parMapper;

    @Autowired
    DictionaryServiceI dicService;

    @Autowired
    AuthMapper authMapper;

    @Autowired
    AccountOrgJobMapper aojMapper;

    @Autowired
    RoleServiceI roleService;

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

    private Map<String, Integer> lookupIndex(String[] titles, Row row) throws Exception {
        Map<String, Integer> indexes = new HashMap<String, Integer>();
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            Cell cell = row.getCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String title = cell.getStringCellValue();
            Integer index = search(titles, title);
            if (index == -1)
                throw new Exception("模板错误，缺少\"" + title + "\"列");
            indexes.put(title, index);
        }
        return indexes;
    }

    private Integer search(String[] titles, String title) {
        for (int i = 0; i < titles.length; i++) {
            if (title.trim().equals(titles[i].trim())) {
                return i;
            }
        }
        return -1;
    }

    public byte[] md5encode(byte[] input) {
        byte[] digestedValue = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            digestedValue = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digestedValue;
    }

    public String getBirthFromIDcard(String cardnum) {
        return cardnum.length() >= 14 ? cardnum.substring(6, 14) : "";
    }

    private Map revert(Map map) {
        Iterator it = map.keySet().iterator();
        Map rmap = new HashMap();
        while (it.hasNext()) {
            Object key = it.next();
            rmap.put(map.get(key), key);
        }
        return rmap;
    }

    public String importStuinfo(String excelUrl, Organization org) throws Exception {
        // TODO Auto-generated method stub

        // String excelUrl = "C:/Users/s/Desktop/学生基本信息导入资料.xls";
        String[] titles = new String[] { "编号*", "学号*", "姓名*", "英文姓名", "姓名拼音", "曾用名", "性别*", "出生地", "籍贯", "民族", "国籍/地区",
                "身份证件类型*", "身份证件号*", "婚姻状况", "港澳台侨外", "政治面貌", "健康状况", "信仰宗教", "血型", "照片", "身份证件有效期", "独生子女标志", "入学年月",
                "年级", "班号*", "学生类别", "现住址", "户口所在地", "户口性质", "是否流动人口", "特长", "联系电话", "通信地址", "邮政编码", "电子信箱", "主页地址",
                "学籍号*" };

        // 中英文对照
        Map<String, String> cnens = new HashMap<String, String>() {
            {
                put("编号", "");
                put("学校", "");
                put("学号", "");
                put("姓名", "");
                put("英文姓名", "");
                put("姓名拼音", "");
                put("曾用名", "");
                put("性别", "");
                put("出生地", "");
                put("籍贯", "");
                put("民族", "");
                put("国籍/地区", "");
                put("身份证件类型", "");
                put("身份证件号", "");
                put("婚姻状况", "");
                put("港澳台侨外", "");
                put("政治面貌", "");
                put("健康状况", "");
                put("信仰宗教", "");
                put("血型", "");
                put("照片", "");
                put("身份证件有效期", "");
                put("独生子女标志", "");
                put("入学年月", "");
                put("年级", "");
                put("班号", "");
                put("学生类别", "");
                put("现住址", "");
                put("户口所在地", "");
                put("户口性质", "");
                put("是否流动人口", "");
                put("通信地址", "txdzCol");
                put("联系电话", "lxdhCol");
                put("邮政编码", "yzbmCol");
                put("电子信箱", "dzxxCol");
                put("主页地址", "zydzCol");
                put("特长", "tcCol");
                put("学籍号", "gwzymCol");
            }
        };

        List<Organization> existOrgs = orgMapper.getOrgByCode(org.getCode()); // 已存在组织机构
        if (existOrgs.size() <= 0) {
            throw new Exception("组织机构发生错误");
        }

        List<StudentWithBLOBs> students_to_insert = new ArrayList<StudentWithBLOBs>();
        List<StudentWithBLOBs> students_to_update = new ArrayList<StudentWithBLOBs>();
        List<Parent> parent_to_insert = new ArrayList<Parent>();
        List<Parent> parent_to_update = new ArrayList<Parent>();

        List<Account> accounts_to_insert = new ArrayList<Account>();
        List<Account> accounts_to_update = new ArrayList<Account>();
        List<Auth> auths = new ArrayList<Auth>();
        List<AccountOrgJob> aojs = new ArrayList<AccountOrgJob>();
        Map<String, School> schoolMap = new HashMap<String, School>();
        ClassSchool curClass = new ClassSchool();

        Map<String, String> sexDic = this.revert(dicService.selectAllDicMap("dic_sex"));
        Map<String, String> nationDic = this.revert(dicService.selectAllDicMap("dic_minzu"));
        Map<String, String> cardTypeDic = this.revert(dicService.selectAllDicMap("dic_cardtype"));
        Map<String, String> marryDic = this.revert(dicService.selectAllDicMap("dic_marry"));
        Map<String, String> gatqbDic = this.revert(dicService.selectAllDicMap("dic_gatqb"));
        Map<String, String> zzmmDic = this.revert(dicService.selectAllDicMap("dic_zzmm"));
        Map<String, String> healthDic = this.revert(dicService.selectAllDicMap("dic_health"));
        Map<String, String> religionDic = this.revert(dicService.selectAllDicMap("dic_religion"));
        Map<String, String> stuTypeDic = new HashMap<String, String>();
        stuTypeDic.put("普通初中生", "31100");
        stuTypeDic.put("普通高中学生", "32100");
        Map<String, String> residenceDic = this.revert(dicService.selectAllDicMap("dic_residence"));

        Long accid = accountMapper.getMaxId() + 1;
        sequenceMapper.updateSeqId(accid, "account");

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
        // 轮循第一行，找到各个列所在的列号，记到colIndex里
        Sheet sheet = wb.getSheet("学生基础信息");
        if (sheet == null)
            throw new Exception("模板表单名有错，请检查模板");
        Map<String, Integer> colIndex = new HashMap<String, Integer>();
        colIndex = lookupIndex(titles, sheet.getRow(1));
        int rowCnt = sheet.getPhysicalNumberOfRows();
        StudentWithBLOBs stu = null;
        Parent parent = null;
        Auth auth = null;
        Auth parent_auth = null;
        Account record = null;
        Account parent_record = null;
        AccountOrgJob aoj = null;
        AccountOrgJob parent_aoj = null;
        // 开启事务
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        // 轮循所有行
        for (int i = 2; i < rowCnt; i++) {
            System.out.println(i);
            try {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                // 获得编号
                Cell idCell = row.getCell(colIndex.get("编号*"));
                if (null == idCell) {
                    continue;
                }
                idCell.setCellType(Cell.CELL_TYPE_STRING);
                String id = idCell.getStringCellValue();
                if (StringUtils.isEmpty(id)) {
                    txManager.rollback(status);
                    throw new Exception("第" + i + "行编号不可为空");
                }

                // 拿出学校，获得学校id，留着后面的xxorgid用
                /*
                 * Cell xxCell = row.getCell(colIndex.get("学校*"));
                 * xxCell.setCellType(Cell.CELL_TYPE_STRING); String schName =
                 * xxCell.getStringCellValue();
                 * if(StringUtils.isEmpty(schName)){ txManager.rollback(status);
                 * throw new Exception("第" + i + "行学校不可为空"); }
                 */

                // 获得学生学号
                Cell xhCell = row.getCell(colIndex.get("学号*"));
                xhCell.setCellType(Cell.CELL_TYPE_STRING);
                String stuNo = xhCell.getStringCellValue();
                if (StringUtils.isEmpty(stuNo)) {
                    txManager.rollback(status);
                    throw new Exception("第" + i + "行学号不可为空");
                }

                // 获得学生姓名
                Cell xmCell = row.getCell(colIndex.get("姓名*"));
                String stuName = "";
                if (xmCell != null) {
                    xmCell.setCellType(Cell.CELL_TYPE_STRING);
                    stuName = xmCell.getStringCellValue();
                }
                // 获得学生曾用名
                Cell cymCell = row.getCell(colIndex.get("曾用名"));
                String pastName = "";
                if (cymCell != null) {
                    cymCell.setCellType(Cell.CELL_TYPE_STRING);
                    pastName = cymCell.getStringCellValue();
                    if ("无".equals(pastName))
                        pastName = "";
                }
                // 获得学生性别，查到性别码
                Cell xbCell = row.getCell(colIndex.get("性别*"));
                String sex = "";
                if (xbCell != null) {
                    xbCell.setCellType(Cell.CELL_TYPE_STRING);
                    sex = xbCell.getStringCellValue();
                    sex = sexDic.containsKey(sex) ? sexDic.get(sex) : "";
                }
                if (StringUtils.isEmpty(sex)) {
                    txManager.rollback(status);
                    throw new Exception("第" + i + "行性别不可为空");
                }

                // 获得学生出生地
                Cell csdCell = row.getCell(colIndex.get("出生地"));
                String born = "";
                if (csdCell != null) {
                    csdCell.setCellType(Cell.CELL_TYPE_STRING);
                    born = csdCell.getStringCellValue();
                }

                // 获得学生籍贯
                Cell jgCell = row.getCell(colIndex.get("籍贯"));
                String nativeplace = "";
                if (jgCell != null) {
                    jgCell.setCellType(Cell.CELL_TYPE_STRING);
                    nativeplace = jgCell.getStringCellValue();
                }

                // 获得学生民族，查询民族码
                Cell mzCell = row.getCell(colIndex.get("民族"));
                String nation = "";
                if (mzCell != null) {
                    mzCell.setCellType(Cell.CELL_TYPE_STRING);
                    nation = mzCell.getStringCellValue();

                    // List<Dictionary> mzms =
                    // dicService.selectAllDicWhere("dic_minzu",
                    // "name='"+nation+"'");
                    // nation = mzms.size()>0?mzms.get(0).getId():"";
                    nation = nationDic.containsKey(nation) ? nationDic.get(nation) : "";
                }
                // 获得学生国籍/地区
                Cell gjCell = row.getCell(colIndex.get("国籍/地区"));
                String nationality = "";
                if (gjCell != null) {
                    gjCell.setCellType(Cell.CELL_TYPE_STRING);
                    nationality = gjCell.getStringCellValue();
                }
                // 获得身份证类型，查询身份证类型码
                Cell sfzCell = row.getCell(colIndex.get("身份证件类型*"));
                String sfzlx = "";
                if (sfzCell != null) {
                    sfzCell.setCellType(Cell.CELL_TYPE_STRING);
                    sfzlx = sfzCell.getStringCellValue();
                    // List<Dictionary> cardTypes =
                    // dicService.selectAllDicWhere("dic_cardtype",
                    // "name='"+sfzlx+"'");
                    // sfzlx = cardTypes.size()>0?cardTypes.get(0).getId():"";
                    sfzlx = cardTypeDic.containsKey(sfzlx) ? cardTypeDic.get(sfzlx) : "";
                }
                // 获得证件号
                Cell zjhCell = row.getCell(colIndex.get("身份证件号*"));
                String cardNo = "";
                if (zjhCell != null) {
                    zjhCell.setCellType(Cell.CELL_TYPE_STRING);
                    cardNo = zjhCell.getStringCellValue();
                }
                int count = accountMapper.selectUserByUniqueCol(cardNo);
                if (count > 0) {
                    throw new Exception("身份证件号为'" + cardNo + "'的用户信息冲突，请联系管理员处理");
                }
                // 获得婚姻状况，查询婚姻状况码
                Cell hyzkCell = row.getCell(colIndex.get("婚姻状况"));
                String hyzk = "";
                if (hyzkCell != null) {
                    hyzkCell.setCellType(Cell.CELL_TYPE_STRING);
                    hyzk = hyzkCell.getStringCellValue();
                    // List<Dictionary> marrys =
                    // dicService.selectAllDicWhere("dic_marry",
                    // "name='"+hyzk+"'");
                    // hyzk = marrys.size()>0?marrys.get(0).getId():"";
                    hyzk = marryDic.containsKey(hyzk) ? marryDic.get(hyzk) : "";
                }
                // 获得港澳台侨胞，查询侨胞码
                Cell qbCell = row.getCell(colIndex.get("港澳台侨外"));
                String qb = "";
                if (qbCell != null) {
                    qbCell.setCellType(Cell.CELL_TYPE_STRING);
                    qb = qbCell.getStringCellValue();
                    // List<Dictionary> qbs =
                    // dicService.selectAllDicWhere("dic_gatqb",
                    // "name='"+qb+"'");
                    // qb = qbs.size()>0?qbs.get(0).getId():"";
                    qb = gatqbDic.containsKey(qb) ? gatqbDic.get(qb) : "";
                }
                // 查询政治面貌码
                Cell zzmmCell = row.getCell(colIndex.get("政治面貌"));
                String zzmm = "";
                if (zzmmCell != null) {
                    zzmmCell.setCellType(Cell.CELL_TYPE_STRING);
                    zzmm = zzmmCell.getStringCellValue();
                    // List<Dictionary> zzmms =
                    // dicService.selectAllDicWhere("dic_zzmm",
                    // "name='"+zzmm+"'");
                    // zzmm = zzmms.size()>0?zzmms.get(0).getId():"";
                    zzmm = zzmmDic.containsKey(zzmm) ? zzmmDic.get(zzmm) : "";
                }
                // 查询健康状况码
                Cell jkzkCell = row.getCell(colIndex.get("健康状况"));
                String jkzk = "";
                if (jkzkCell != null) {
                    jkzkCell.setCellType(Cell.CELL_TYPE_STRING);
                    jkzk = jkzkCell.getStringCellValue();
                    // List<Dictionary> healths =
                    // dicService.selectAllDicWhere("dic_health",
                    // "name='"+jkzk+"'");
                    // jkzk = healths.size()>0?healths.get(0).getId():"";
                    jkzk = healthDic.containsKey(jkzk) ? healthDic.get(jkzk) : "";
                }
                // 查询信仰宗教码
                Cell zjxyCell = row.getCell(colIndex.get("信仰宗教"));
                String zjxy = "";
                if (zjxyCell != null) {
                    zjxyCell.setCellType(Cell.CELL_TYPE_STRING);
                    zjxy = zjxyCell.getStringCellValue();
                    // List<Dictionary> religions =
                    // dicService.selectAllDicWhere("dic_religion",
                    // "name='"+zjxy+"'");
                    // zjxy = religions.size()>0?religions.get(0).getId():"";
                    zjxy = religionDic.containsKey(zjxy) ? religionDic.get(zjxy) : "";
                }
                // 查询独生子女标识码
                Cell dsznCell = row.getCell(colIndex.get("独生子女标志"));
                String dsznbs = "";
                if (dsznCell != null) {
                    dsznCell.setCellType(Cell.CELL_TYPE_STRING);
                    dsznbs = dsznCell.getStringCellValue();
                }
                // 获得入学年月
                Cell rxnyCell = row.getCell(colIndex.get("入学年月"));
                String enrollYear = "";
                if (rxnyCell != null) {
                    rxnyCell.setCellType(Cell.CELL_TYPE_STRING);
                    enrollYear = rxnyCell.getStringCellValue();
                }
                // 获得年级
                Cell njCell = row.getCell(colIndex.get("年级"));
                String grade = "";
                if (njCell != null) {
                    njCell.setCellType(Cell.CELL_TYPE_STRING);
                    grade = njCell.getStringCellValue();
                }
                // 获得班号，如果无法对应班级，新增班级，返回id，留着student的class_id用
                Cell bhCell = row.getCell(colIndex.get("班号*"));
                String classNo = "";
                List<ClassSchool> classes = null;
                if (bhCell != null) {
                    bhCell.setCellType(Cell.CELL_TYPE_STRING);
                    classNo = bhCell.getStringCellValue();
                    classes = classMapper.selectByBH(classNo);
                    if (classes.size() <= 0) {
                        throw new Exception("没有找到班号为" + classNo + "的班级，请先导入或新建班级信息");
                    } else if (classes.size() > 1) {
                        throw new Exception(classNo + "班级信息重复");
                    }
                }
                // 查询学生类别码
                Cell xxlbCell = row.getCell(colIndex.get("学生类别"));
                String xxlbm = "";
                String stuType = "";
                if (xxlbCell != null) {
                    xxlbCell.setCellType(Cell.CELL_TYPE_STRING);
                    xxlbm = xxlbCell.getStringCellValue();
                    stuType = stuTypeDic.containsKey(xxlbm) ? stuTypeDic.get(xxlbm) : "";
                }
                // 获取现住址
                Cell xzzCell = row.getCell(colIndex.get("现住址"));
                String xzz = "";
                if (xzzCell != null) {
                    xzzCell.setCellType(Cell.CELL_TYPE_STRING);
                    xzz = xzzCell.getStringCellValue();
                }
                // 获取户口所在地
                Cell hkszdCell = row.getCell(colIndex.get("户口所在地"));
                String hkszd = "";
                if (hkszdCell != null) {
                    hkszdCell.setCellType(Cell.CELL_TYPE_STRING);
                    hkszd = hkszdCell.getStringCellValue();
                }
                // 查询户口性质码
                Cell hkxzCell = row.getCell(colIndex.get("户口性质"));
                String hkxz = "";
                if (hkxzCell != null) {
                    hkxzCell.setCellType(Cell.CELL_TYPE_STRING);
                    hkxz = hkxzCell.getStringCellValue();
                    // List<Dictionary> residenceTypes =
                    // dicService.selectAllDicWhere("dic_residence",
                    // "name='"+hkxz+"'");
                    // hkxz =
                    // residenceTypes.size()>0?residenceTypes.get(0).getId():"";
                    hkxz = residenceDic.containsKey(hkxz) ? residenceDic.get(hkxz) : "";
                }
                // 查询是否流动人口
                Cell sfldrkCell = row.getCell(colIndex.get("是否流动人口"));
                String sfldrk = "";
                if (sfldrkCell != null) {
                    sfldrkCell.setCellType(Cell.CELL_TYPE_STRING);
                    sfldrk = sfldrkCell.getStringCellValue();
                }
                // 获取特长
                Cell tcCell = row.getCell(colIndex.get("特长"));
                String tc = "";
                if (tcCell != null) {
                    tcCell.setCellType(Cell.CELL_TYPE_STRING);
                    tc = tcCell.getStringCellValue();
                }
                // 联系电话
                Cell lxdhCell = row.getCell(colIndex.get("联系电话"));
                String lxdh = "";
                if (lxdhCell != null) {
                    lxdhCell.setCellType(Cell.CELL_TYPE_STRING);
                    lxdh = lxdhCell.getStringCellValue();
                }
                // 获取通信地址
                Cell txdzCell = row.getCell(colIndex.get("通信地址"));
                String txdz = "";
                if (txdzCell != null) {
                    txdzCell.setCellType(Cell.CELL_TYPE_STRING);
                    txdz = txdzCell.getStringCellValue();
                }
                // 邮政编码
                Cell yzbmCell = row.getCell(colIndex.get("邮政编码"));
                String yzbm = "";
                if (yzbmCell != null) {
                    yzbmCell.setCellType(Cell.CELL_TYPE_STRING);
                    yzbm = yzbmCell.getStringCellValue();
                }
                // 电子信箱
                Cell dzxxCell = row.getCell(colIndex.get("电子信箱"));
                String dzxx = "";
                if (dzxxCell != null) {
                    dzxxCell.setCellType(Cell.CELL_TYPE_STRING);
                    dzxx = dzxxCell.getStringCellValue();
                }
                // 主页地址
                Cell zydzCell = row.getCell(colIndex.get("主页地址"));
                String zydz = "";
                if (zydzCell != null) {
                    zydzCell.setCellType(Cell.CELL_TYPE_STRING);
                    zydz = zydzCell.getStringCellValue();
                }
                // 获取学籍号
                Cell xjhCell = row.getCell(colIndex.get("学籍号*"));
                String xjh = "";
                if (xjhCell != null) {
                    xjhCell.setCellType(Cell.CELL_TYPE_STRING);
                    xjh = xjhCell.getStringCellValue();
                    if (StringUtils.isEmpty(xjh)) {
                        throw new Exception("第" + i + "行学籍号不可为空");
                    }
                }
                String xxdm = "2".equals(org.getOrgType()) ? org.getCode() : null;
                String username = cardNo;
                Long orgId = org.getId();

                // 创建用户，写account表，返回id，写入student的account_id
                record = new Account();
                if (cardNo.length() <= 18)
                    record.setIdcard(cardNo); // 身份证
                record.setId(accid); // 编号
                record.setUsername(username);
                record.setRealname(stuName);
                // record.setPassword("21232f297a57a5a743894a0e4a801fc3");
                // //密码，默认admin
                record.setPassword(passwordService.encryptPassword(null, Constants.DEFAULT_PASSWORD));
                record.setCreateTime(Calendar.getInstance().getTime()); // 时间
                record.setUpdateTime(Calendar.getInstance().getTime());
                record.setTypeFlag(1); // 类型是学生
                record.setState(1); // 正常状态
                record.setAdmin(0);
                record.setTheme("theme1");
                record.setIsdoglongin(null); // 是否加密狗登录

                // 创建家长用户，写account表，返回id，写入student的account_id
                parent_record = new Account();
                parent_record.setId(accid + 1); // 编号
                parent_record.setUsername(username + "fm");
                // parent_record.setPassword("21232f297a57a5a743894a0e4a801fc3");
                // //密码，默认admin
                parent_record.setPassword(passwordService.encryptPassword(null, Constants.DEFAULT_PASSWORD)); // 密码，默认admin
                parent_record.setCreateTime(Calendar.getInstance().getTime()); // 时间
                parent_record.setUpdateTime(Calendar.getInstance().getTime());
                parent_record.setTypeFlag(3); // 类型是家长
                parent_record.setState(1); // 正常状态
                parent_record.setAdmin(0);
                parent_record.setRealname(stuName + "家长");
                parent_record.setTheme("theme1");
                parent_record.setIsdoglongin(null); // 是否加密狗登录

                // 写student表
                Map<String, String> attrVals = new HashMap<String, String>();
                stu = new StudentWithBLOBs();
                stu.setXh(stuNo);
                attrVals.put("学号", stuNo);
                stu.setXm(stuName);
                attrVals.put("姓名", stuName);
                stu.setCym(pastName);
                attrVals.put("曾用名", pastName);
                stu.setXbm(sex);
                attrVals.put("性别", sex);
                /*
                 * if(born.contains("江苏省南京市")){ born = born.replace("江苏省南京市",
                 * ""); districtMapper. stu.setCsdm(csdm); //出生地码，查资料 }
                 */

                stu.setJg(nativeplace);
                attrVals.put("籍贯", nativeplace);
                stu.setMzm(nation);
                attrVals.put("民族", nation);
                if ("中国".equals(nationality))
                    stu.setGjdqm("CHN"); // 国籍、地区码 查资料
                stu.setSfzjlxm(sfzlx);
                attrVals.put("身份证件类型", sfzlx);
                if (cardNo.length() <= 18)
                    stu.setSfzjh(cardNo);
                stu.setHyzkm(hyzk);
                attrVals.put("婚姻状况", hyzk);
                stu.setGatqwm(qb);
                attrVals.put("港澳台侨外", qb);
                stu.setZzmmm(zzmm);
                attrVals.put("政治面貌", zzmm);
                stu.setDzxx(dzxx);
                attrVals.put("电子信箱", dzxx);
                stu.setJkzkm(jkzk);
                attrVals.put("健康状况", jkzk);
                stu.setXyzjm(zjxy);
                attrVals.put("信仰宗教", zjxy);
                stu.setDszybz(dsznbs);
                attrVals.put("独生子女标志", dsznbs);
                stu.setRxny(enrollYear);
                attrVals.put("入学年月", enrollYear);
                stu.setNj(grade);
                attrVals.put("年级", grade);
                stu.setBh(classNo);
                attrVals.put("班号", classNo);
                stu.setXslbm(stuType);
                attrVals.put("学生类别", stuType);
                stu.setXzz(xzz);
                attrVals.put("现住址", xzz);
                stu.setHkszd(hkszd);
                attrVals.put("户口所在地", hkszd);
                stu.setHkxzm(hkxz);
                attrVals.put("户口性质", hkxz);
                stu.setSfldrk(sfldrk);
                attrVals.put("是否流动人口", sfldrk);
                stu.setTc(tc);
                attrVals.put("特长", tc);
                stu.setLxdh(lxdh);
                attrVals.put("联系电话", lxdh);
                stu.setTxdz(txdz);
                attrVals.put("通信地址", txdz);
                stu.setYzbm(yzbm);
                attrVals.put("邮政编码", yzbm);
                stu.setZydz(zydz);
                attrVals.put("主页地址", zydz);
                stu.setXjh(xjh);
                attrVals.put("学籍号", xjh);
                stu.setAccountId(accid);
                stu.setCsrq(getBirthFromIDcard(cardNo)); // 根据身份证号算出出生日期
                stu.setBjid(classes.get(0).getId()); // 班级id

                // 给学生背景字段赋值
                // JSONObject bjxxObj = new JSONObject();
                String bjxxObj = "";
                stu.setAttrsValsWithLabel(attrVals);
                List<FieldValue> attrs = stu.getAttrs();
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
                stu.setBjxx(bjxxObj.toString());

                // 写parent表
                parent = new Parent();
                parent.setAccountId(accid + 1);
                parent.setStudentAccountId(accid);
                parent.setCyxm(stuName + "家长");
                parent.setGxm("51");

                // 写auth表，默认学生用户和角色
                /*
                 * auth = new Auth(); auth.setOrgId(orgId);
                 * auth.setUserId(accid); Role role = new Role();
                 * role.setRoleName("学生"); List<Role> roles =
                 * roleService.selectBySelective(role);
                 * 
                 * if(roles.size()>0 && null != roles.get(0)){
                 * auth.setRoleId(roles.get(0).getId()); //学生角色 }else{ throw new
                 * Exception("系统没有学生角色，请联系管理员添加"); } auth.setAuthType("user");
                 * 
                 * //家长写auth表，默认家长角色 parent_auth = new Auth();
                 * parent_auth.setOrgId(orgId); parent_auth.setUserId(accid+1);
                 * Role parent_role = new Role(); parent_role.setRoleName("家长");
                 * List<Role> parent_roles =
                 * roleService.selectBySelective(role);
                 * 
                 * if(parent_roles.size()>0 && null != parent_roles.get(0)){
                 * parent_auth.setRoleId(parent_roles.get(0).getId()); //学生角色
                 * }else{ throw new Exception("系统没有家长角色，请联系管理员添加"); }
                 * parent_auth.setAuthType("user");
                 */

                // 写user_org_job表，写学生学校映射关系
                aoj = new AccountOrgJob();
                aoj.setUserId(accid);
                aoj.setOrgId(orgId);

                // 写user_org_job表，写学生学校映射关系
                parent_aoj = new AccountOrgJob();
                parent_aoj.setUserId(accid + 1);
                parent_aoj.setOrgId(orgId);

            } catch (Exception e1) {
                e1.printStackTrace();
                txManager.rollback(status);
                if (StringUtils.isNotEmpty(e1.getMessage()))
                    throw new Exception(e1.getMessage());
                else
                    throw new Exception("模板第" + i + "行数据有错，请检查后再上传");

            }
            accounts_to_insert.add(record);
            accounts_to_insert.add(parent_record);
            accid = accid + 2;
            students_to_insert.add(stu);
            parent_to_insert.add(parent);
            aojs.add(aoj);
            aojs.add(parent_aoj);

            if (accounts_to_insert.size() >= 1000 || i == rowCnt - 1) {
                if (accounts_to_insert.size() > 0) {
                    accountMapper.insertBatch(accounts_to_insert);
                    accounts_to_insert = new ArrayList<Account>();
                }
            }

            if (students_to_insert.size() >= 1000 || i == rowCnt - 1) {
                if (students_to_insert.size() > 0) {
                    stuMapper.insertBatch(students_to_insert);
                    students_to_insert = new ArrayList<StudentWithBLOBs>();
                }
            }

            if (parent_to_insert.size() >= 1000 || i == rowCnt - 1) {
                if (parent_to_insert.size() > 0) {
                    parMapper.insertBatch(parent_to_insert);
                    parent_to_insert = new ArrayList<Parent>();
                }
            }
            // 统一处理sql
            /*
             * if(auths.size()>=1000 || i==rowCnt-1){ if(auths.size()>0){
             * authMapper.insertBatch(auths); auths = new ArrayList<Auth>(); } }
             * 
             */
            if (aojs.size() >= 1000 || i == rowCnt - 1) {
                if (aojs.size() > 0) {
                    aojMapper.insertBatch(aojs);
                    aojs = new ArrayList<AccountOrgJob>();
                }
            }

            if (accounts_to_update.size() >= 1000 || i == rowCnt - 1) {
                if (accounts_to_update.size() > 0) {
                    accountMapper.updateBatch(accounts_to_update);
                    accounts_to_update = new ArrayList<Account>();
                }
            }

            if (students_to_update.size() == 1000 || i == rowCnt - 1) {
                if (students_to_update.size() > 0) {
                    stuMapper.updateBatch(students_to_update);
                    students_to_update = new ArrayList<StudentWithBLOBs>();
                }
            }

            if (parent_to_update.size() == 1000 || i == rowCnt - 1) {
                if (parent_to_update.size() > 0) {
                    parMapper.updateBatch(parent_to_update);
                    parent_to_update = new ArrayList<Parent>();
                }
            }
            // 提交事务
        }

        // 如果最后几行都continue跳出循环，处理没有提交的数据
        if (accounts_to_insert.size() > 0) {
            accountMapper.insertBatch(accounts_to_insert);
            accounts_to_insert = new ArrayList<Account>();
        }

        if (students_to_insert.size() > 0) {
            stuMapper.insertBatch(students_to_insert);
            students_to_insert = new ArrayList<StudentWithBLOBs>();
        }

        if (parent_to_insert.size() > 0) {
            parMapper.insertBatch(parent_to_insert);
            parent_to_insert = new ArrayList<Parent>();
        }
        if (aojs.size() > 0) {
            aojMapper.insertBatch(aojs);
            aojs = new ArrayList<AccountOrgJob>();
        }

        if (accounts_to_update.size() > 0) {
            accountMapper.updateBatch(accounts_to_update);
            accounts_to_update = new ArrayList<Account>();
        }

        if (students_to_update.size() > 0) {
            stuMapper.updateBatch(students_to_update);
            students_to_update = new ArrayList<StudentWithBLOBs>();
        }

        if (parent_to_update.size() > 0) {
            parMapper.updateBatch(parent_to_update);
            parent_to_update = new ArrayList<Parent>();
        }
        try {
            stream.close();
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            throw new Exception("数据导入失败，请重试");
        }
        return "success";
    }

}
