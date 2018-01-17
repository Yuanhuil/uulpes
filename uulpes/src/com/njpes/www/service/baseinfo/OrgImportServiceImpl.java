package com.njpes.www.service.baseinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.AccountMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.DistrictMapper;
import com.njpes.www.dao.baseinfo.EducommissionMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.util.SequenceMapper;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Educommission;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.service.util.DictionaryServiceI;
import com.njpes.www.utils.PropertiesUtil;
import com.sun.xml.internal.stream.buffer.sax.Properties;

import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

@Service("OrgImportService")
public class OrgImportServiceImpl implements OrgImportServiceI {

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
    EducommissionMapper educommissionMapper;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    DictionaryServiceI dicService;

    @Autowired
    SequenceMapper sequenceMapper;

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

    // 学校还是教委
    public String getOrgType(String code) {
        if (code.length() <= 6)
            return OrganizationType.ec.getId();
        else
            return OrganizationType.school.getId();
    }

    // 市直属学校
    public boolean isCitySubSchool(String code) {
        if ("320100".equals(code))
            return true;
        else
            return false;
    }

    public String getOrgId(String name) {
        Organization org = new Organization();
        org.setName(name);
        List<Organization> orgs = orgMapper.selectAllOrganizations();
        return orgs.size() > 0 ? Long.toString(orgs.get(0).getId()) : null;
    }

    private Map<String, Integer> lookupIndex(String[] titles, Row row) {
        Map<String, Integer> indexes = new HashMap<String, Integer>();
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            Cell cell = row.getCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String title = cell.getStringCellValue();
            Integer index = search(titles, title);
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

    public Integer getOrgLevel(String code) {
        String subSchoolCode = Constants.APPLICATION_CITYCODE + "00";
        if (code.length() == 4)
            return 3;
        if ((code.startsWith(subSchoolCode) && !subSchoolCode.equals(code) && code.length() == 11)
                || code.length() == 6) // 市直属学校、区教委
            return 4;
        if (code.length() == 11 && OrganizationType.school.getId().equals(getOrgType(code))) // 学校
            return 6;
        return null;
    }

    public Long getOrgPrentId(String code) {
        List orgs = new ArrayList<Organization>();
        Organization org = new Organization();
        org.setCode(code);
        orgs.add(org);
        return orgMapper.getOrgByCode(code).get(0).getId();
    }

    private String getParentCode(String code) {
        // TODO Auto-generated method stub
        if (code.length() == 6) {
            return code.substring(0, 4);
        }
        if (code.length() == 11) {
            if (code.startsWith("320100"))
                return cityid;
            return code.substring(0, 6);
        }
        return null;
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

    public void importOrg(String excelUrl, Organization corg) throws Exception {

        String[] titles = new String[] { "编号", "学校代码", "学校名称", "所属区县", "所属乡镇", "学校英文名称", "学校地址", "学校邮政编码", "建校年月",
                "校庆日", "学校办学类型", "学校举办者", "学校主管部门", "法定代表人号", "法人证书号", "校长工号", "校长姓名", "党委负责人", "组织机构码", "联系电话", "传真电话",
                "电子信箱", "主页地址", "历史沿革", "学校办别", "所属主管单位", "所在地城乡类型", "所在地经济属性", "所在地民族属性", "小学学制", "小学入学年龄", "初中学制",
                "初中入学年龄", "高中学制", "主教学语言", "辅教学语言", "招生半径" };

        Map<String, Integer> colIndex = new HashMap<String, Integer>();

        List<Organization> existOrgs = orgMapper.selectAllOrganizations();
        List<School> existSchools = schoolMapper.getSchoolsAccordingEducommission(corg.getId().intValue());
        Long maxId = orgMapper.getMaxId();
        sequenceMapper.updateSeqId(maxId + 1, "organization");
        Map<String, Organization> codeOrgMap = new HashMap<String, Organization>();
        for (Organization org : existOrgs) {
            byte[] orgByte = SerializationUtils.serialize(org);
            codeOrgMap.put(org.getCode(), (Organization) SerializationUtils.deserialize(orgByte));
        }
        Map<String, School> codeSchMap = new HashMap<String, School>();
        for (School sch : existSchools) {
            byte[] schByte = SerializationUtils.serialize(sch);
            // 强制必须有学校代码，并且全局唯一
            codeSchMap.put(sch.getXxdm(), (School) SerializationUtils.deserialize(schByte));
        }

        Map<String, Organization> parentIds = new HashMap<String, Organization>();
        List<Organization> orgs_to_insert = new ArrayList<Organization>();
        List<Organization> orgs_to_update = new ArrayList<Organization>();
        List<School> schs_to_insert = new ArrayList<School>();
        List<School> schs_to_update = new ArrayList<School>();

        Map<String, String> orgtypeMap = revert(dicService.selectAllDicMap("dic_org_type"));
        Map<String, String> orglevelMap = revert(dicService.selectAllDicMap("dic_organizationlevel"));
        final String provinceid2 = corg.getProvinceid();
        final District prov = districtMapper.selectByCode(String.valueOf(corg.getProvinceid()));
        Map<String, String> provinceMap = new HashMap<String, String>() {
            {
                // put("江苏省", Constants.APPLICATION_CITYCODE);
                put(prov.getName(), String.valueOf(provinceid2));
            }
        };
        final District ct = districtMapper.selectByCode(String.valueOf(corg.getCityid()));
        Map<String, String> cityMap = new HashMap<String, String>() {
            {
                put(ct.getName(), String.valueOf(ct.getCode()));
            }
        };
        Map<String, String> countyMap = new HashMap<String, String>();
        Map<String, String> townMap = new HashMap<String, String>();

        List<District> citys = districtMapper.getSubCities(provinceid2);
        for (District city : citys) {
            cityMap.put(city.getName(), city.getCode());
            List<District> counties = districtMapper.getSubCounties(city.getCode());
            for (District county : counties) {
                countyMap.put(county.getName(), county.getCode());
                List<District> towns = districtMapper.getSubTowns(county.getCode());
                for (District town : towns) {
                    townMap.put(town.getName(), town.getCode());
                }
            }
        }

        String sheetName = "school";
        try {
            stream = new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            String fileName = excelUrl.substring(prefix + 1);
            if (StringUtils.endsWith(fileName, ".xls"))
                read2003Excel(stream);
            else if (StringUtils.endsWith(fileName, ".xlsx"))
                read2007Excel(stream);
            Sheet sheet = wb.getSheet(sheetName);
            colIndex = lookupIndex(titles, sheet.getRow(0));

            // 组织机构信息

            int idCol = colIndex.get("编号");
            int schoolcodeCol = colIndex.get("学校代码");
            int codeCol = colIndex.get("组织机构码");
            int nameCol = colIndex.get("学校名称");
            // int parentCol = colIndex.get("编号");
            // int orgTypeCol = colIndex.get("编号");
            // int orgLevelCol = colIndex.get("编号");
            // int sortCol = colIndex.get("编号");
            int countyCol = colIndex.get("所属区县");
            int townCol = colIndex.get("所属乡镇");
            // 学校信息
            int ywmcCol = colIndex.get("学校英文名称");
            int xxdzCol = colIndex.get("学校地址");
            int xxyzbmCol = colIndex.get("学校邮政编码");
            int jxnyCol = colIndex.get("建校年月");
            int xqrCol = colIndex.get("校庆日");
            int xxbxlxmCol = colIndex.get("学校办学类型");
            int xxjbzmCol = colIndex.get("学校举办者");
            int xxzgbmmCol = colIndex.get("学校主管部门");
            int fddbrhCol = colIndex.get("法定代表人号");
            int frzshCol = colIndex.get("法人证书号");
            int xzghCol = colIndex.get("校长工号");
            int xzxmCol = colIndex.get("校长姓名");
            int dwfzrhCol = colIndex.get("党委负责人");
            // int zzjgmCol = colIndex.get("组织机构码");
            int lxdhCol = colIndex.get("联系电话");
            int czdhCol = colIndex.get("传真电话");
            int dzxxCol = colIndex.get("电子信箱");
            int zydzCol = colIndex.get("主页地址");
            int lsygCol = colIndex.get("历史沿革");
            int xxbbmCol = colIndex.get("学校办别");
            int sszgdwmCol = colIndex.get("所属主管单位");
            int ssdcxlbmCol = colIndex.get("所在地城乡类型");
            int ssdjjsxmCol = colIndex.get("所在地经济属性");
            int szdmzsxCol = colIndex.get("所在地民族属性");
            int xxxzCol = colIndex.get("小学学制");
            int xxrxnlCol = colIndex.get("小学入学年龄");
            int czxzCol = colIndex.get("初中学制");
            int czrxnlCol = colIndex.get("初中入学年龄");
            int gzxzCol = colIndex.get("高中学制");
            int zjxyymCol = colIndex.get("主教学语言");
            int fjxyymCol = colIndex.get("辅教学语言");
            int zsbjCol = colIndex.get("招生半径");
            // int bjxxCol = colIndex.get("编号");
            Long nextId = -1l;

            PropertiesUtil p = new PropertiesUtil("resource/resources.properties");
            String string = p.readProperty("school.count");

            for (int i = 1; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                if (i + existSchools.size() > Integer.parseInt(string)) {
                    throw new Exception("已超过限制，限制学校总数为:，"+string);
                }
                System.out.println(i);
                int insert_or_update = 0; // 1-insert,2-update
                Row row = sheet.getRow(i);
                Cell idcell = row.getCell(idCol);
                if (null == idcell) {
                    throw new Exception("学校基础信息第" + (i + 1) + "行有误，请检查模板，重新导入");
                }
                idcell.setCellType(Cell.CELL_TYPE_STRING);
                String id = idcell.getStringCellValue(); // 编号
                if (StringUtils.isEmpty(id))
                    throw new Exception("请填写第" + (i + 1) + "行学校编号");

                Cell codecell = row.getCell(codeCol);
                codecell.setCellType(Cell.CELL_TYPE_STRING);
                String code = codecell.getStringCellValue(); // 编码
                if (null != code) {
                    code = code.trim();
                } else {
                    throw new Exception("请填写第" + (i + 1) + "行组织机构码");
                }
                if (!codeOrgMap.containsKey(code)) {
                    insert_or_update = 1;
                    maxId = maxId + 1;
                    nextId = maxId;
                } else {
                    insert_or_update = 2;
                    nextId = codeOrgMap.get(code).getId();
                }

                Cell namecell = row.getCell(nameCol);
                namecell.setCellType(Cell.CELL_TYPE_STRING);
                String name = namecell.getStringCellValue(); // 名称
                if (null != name) {
                    name = name.trim();
                } else {
                    throw new Exception("请填写第" + (i + 1) + "行学校名称");
                }

                String parentId = null;
                /*
                 * row.getCell(parentCol).setCellType(Cell.CELL_TYPE_STRING);
                 * String parent = row.getCell(parentCol).getStringCellValue();
                 * //父机构名称 if(StringUtils.isNotEmpty(parent)){
                 * if(parentIds.containsKey(parent)){ parentId =
                 * Long.toString(parentIds.get(parent).getId()); }else{ parentId
                 * = null; } }
                 */
                if (null != corg.getId()) {
                    // 只能导入当前组织的子机构
                    parentId = Long.toString(corg.getId());
                } else {
                    throw new Exception("系统错误，请退出重新登录再导入学校信息");
                }
                // String parentids =
                // parentIds.containsKey(parent)?StringUtils.isNotEmpty(parentIds.get(parent).getParentIds())
                // ?parentIds.get(parent).getParentIds()+"/"+parentId:parentId:null;
                String parentids = StringUtils.isNotEmpty(corg.getParentIds()) ? corg.getParentIds() + "/" + parentId
                        : parentId;

                String orgtype = getOrgType(code); // 组织类型

                /*
                 * row.getCell(orgTypeCol).setCellType(Cell.CELL_TYPE_STRING);
                 * String orgtype =
                 * row.getCell(orgTypeCol).getStringCellValue(); //组织类型
                 * if(null!=orgtype){ if(orgtypeMap.containsKey(orgtype))
                 * orgtype = orgtypeMap.get(orgtype.trim()); }
                 */

                String orglevel = Integer.toString(getOrgLevel(code));
                /*
                 * row.getCell(orgLevelCol).setCellType(Cell.CELL_TYPE_STRING);
                 * String orglevel =
                 * row.getCell(orgLevelCol).getStringCellValue(); //组织级别
                 * if(null!=orglevel){ if(orglevelMap.containsKey(orglevel))
                 * orglevel = orglevelMap.get(orglevel.trim()); }
                 * 
                 * row.getCell(sortCol).setCellType(Cell.CELL_TYPE_STRING);
                 * String sort = row.getCell(sortCol).getStringCellValue(); //排序
                 * if(null!=sort) sort = sort.trim();
                 */

                /*
                 * row.getCell(provCol).setCellType(Cell.CELL_TYPE_STRING);
                 * String province = row.getCell(provCol).getStringCellValue();
                 * //省 if(null!=province){ if(provinceMap.containsKey(province))
                 * province = provinceMap.get(province); }
                 */
                // String province = "32";
                String province = Constants.APPLICATION_PROVINCECODE;

                /*
                 * row.getCell(cityCol).setCellType(Cell.CELL_TYPE_STRING);
                 * String city = row.getCell(cityCol).getStringCellValue(); //市
                 * if(null!=city){ if(cityMap.containsKey(city)) city =
                 * cityMap.get(city); }
                 */
                String city = Constants.APPLICATION_CITYCODE;

                Cell countycell = row.getCell(countyCol);
                String county = null;
                if (null != countycell) {
                    countycell.setCellType(Cell.CELL_TYPE_STRING);
                    county = countycell.getStringCellValue(); // 区县
                    if (null != county) {
                        if (countyMap.containsKey(county))
                            county = countyMap.get(county);
                        else
                            throw new Exception("请根据填写说明正确填写第" + (i + 1) + "行所属区县");
                    } else {
                        throw new Exception("请填写第" + (i + 1) + "行所属区县");
                    }
                }
                String town = null;
                Cell towncell = row.getCell(townCol);
                if (null != towncell) {
                    towncell.setCellType(Cell.CELL_TYPE_STRING);
                    town = towncell.getStringCellValue(); // 乡镇
                    if (null != town) {
                        if (townMap.containsKey(town))
                            town = townMap.get(town);
                    }
                }
                Organization org = new Organization();

                org.setCode(code);
                org.setName(name);
                org.setOrgType(orgtype);
                org.setOrgLevel(
                        StringUtils.isNumeric(orglevel) && !"".equals(orglevel) ? Integer.parseInt(orglevel) : -1);
                org.setProvinceid(province);
                org.setCityid(city);
                org.setCountyid(county);
                org.setTownid(town);
                org.setParentId(
                        StringUtils.isNumeric(parentId) && !"".equals(parentId) ? Long.parseLong(parentId) : -1);
                org.setIsshow(1);
                // org.setSort(Integer.parseInt(sort));
                org.setParentIds(parentids);
                org.setLocked("");

                if (insert_or_update == 1) {
                    orgs_to_insert.add(org);
                    org.setId(nextId);
                    codeOrgMap.put(code, org);
                    parentIds.put(name, org);
                } else if (insert_or_update == 2) {
                    orgs_to_update.add(org);
                    org.setId(nextId);
                    codeOrgMap.put(code, org);
                    parentIds.put(name, org);
                }

                // 学校的信息组装
                if (orgtype.equals(orgtypeMap.get("学校"))) {
                    Cell ywmccell = row.getCell(ywmcCol);
                    String ywmc = null;
                    if (ywmccell != null) {
                        ywmccell.setCellType(Cell.CELL_TYPE_STRING);
                        ywmc = ywmccell.getStringCellValue(); // 英文名称
                        if (null != ywmc)
                            ywmc = ywmc.trim();
                    }

                    Cell xxdzcell = row.getCell(xxdzCol);
                    String xxdz = null;
                    if (xxdzcell != null) {
                        xxdzcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxdz = xxdzcell.getStringCellValue(); // 学校地址
                        if (null != xxdz)
                            xxdz = xxdz.trim();
                    }

                    Cell xxyzbmcell = row.getCell(xxyzbmCol);
                    String xxyzbm = null;
                    if (xxdzcell != null) {
                        xxyzbmcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxyzbm = xxyzbmcell.getStringCellValue(); // 学校邮政编码
                        if (null != xxyzbm)
                            xxyzbm = xxyzbm.trim();
                    }

                    Cell jxnycell = row.getCell(jxnyCol);
                    String jxny = null;
                    if (jxnycell != null) {
                        jxnycell.setCellType(Cell.CELL_TYPE_STRING);
                        jxny = jxnycell.getStringCellValue(); // 建校年月
                        if (null != jxny)
                            jxny = jxny.trim();
                    }

                    Cell xqrcell = row.getCell(xqrCol);
                    String xqr = null;
                    if (xqrcell != null) {
                        xqrcell.setCellType(Cell.CELL_TYPE_STRING);
                        xqr = xqrcell.getStringCellValue(); // 校庆日
                        if (null != xqr)
                            xqr = xqr.trim();
                    }

                    Cell xxbxlxmcell = row.getCell(xxbxlxmCol);
                    String xxbxlxm = null;
                    if (xxbxlxmcell != null) {
                        xxbxlxmcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxbxlxm = xxbxlxmcell.getStringCellValue(); // 学校办学类型码
                        if (null != xxbxlxm)
                            xxbxlxm = xxbxlxm.trim();
                    }

                    Cell xxjbzmcell = row.getCell(xxjbzmCol);
                    String xxjbzm = null;
                    if (xxjbzmcell != null) {
                        xxjbzmcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxjbzm = xxjbzmcell.getStringCellValue(); // 学校举办者码
                        if (null != xxjbzm)
                            xxjbzm = xxjbzm.trim();
                    }

                    Cell xxzgbmmcell = row.getCell(xxzgbmmCol);
                    String xxzgbmm = null;
                    if (xxzgbmmcell != null) {
                        xxzgbmmcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxzgbmm = xxzgbmmcell.getStringCellValue(); // 学校主管部门码
                        if (null != xxzgbmm) {
                            xxzgbmm = xxzgbmm.trim();
                        } else {
                            throw new Exception("请填写第" + (i + 1) + "行学校主管部门编码");
                        }
                    }

                    Cell fddbrhcell = row.getCell(fddbrhCol);
                    String fddbrh = null;
                    if (fddbrhcell != null) {
                        fddbrhcell.setCellType(Cell.CELL_TYPE_STRING);
                        fddbrh = fddbrhcell.getStringCellValue(); // 法定代表人号
                        if (null != fddbrh)
                            fddbrh = fddbrh.trim();
                    }

                    Cell frzshcell = row.getCell(frzshCol);
                    String frzsh = null;
                    if (frzshcell != null) {
                        frzshcell.setCellType(Cell.CELL_TYPE_STRING);
                        frzsh = frzshcell.getStringCellValue(); // 法人证书号
                        if (null != frzsh)
                            frzsh = frzsh.trim();
                    }

                    Cell xzghcell = row.getCell(xzghCol);
                    String xzgh = null;
                    if (xzghcell != null) {
                        xzghcell.setCellType(Cell.CELL_TYPE_STRING);
                        xzgh = xzghcell.getStringCellValue(); // 校长工号
                        if (null != xzgh)
                            xzgh = xzgh.trim();
                    }

                    Cell xzxmcell = row.getCell(xzxmCol);
                    String xzxm = null;
                    if (xzxmcell != null) {
                        xzxmcell.setCellType(Cell.CELL_TYPE_STRING);
                        xzxm = xzxmcell.getStringCellValue(); // 校长姓名
                        if (null != xzxm)
                            xzxm = xzxm.trim();
                    }

                    Cell dwfzrhcell = row.getCell(dwfzrhCol);
                    String dwfzrh = null;
                    if (dwfzrhcell != null) {
                        dwfzrhcell.setCellType(Cell.CELL_TYPE_STRING);
                        dwfzrh = dwfzrhcell.getStringCellValue(); // 党委负责人号
                        if (null != dwfzrh)
                            dwfzrh = dwfzrh.trim();
                    }

                    /*
                     * row.getCell(codeCol).setCellType(Cell.CELL_TYPE_STRING);
                     * String zzjgm = row.getCell(codeCol).getStringCellValue();
                     * //组织机构码 if(null!=zzjgm) zzjgm = zzjgm.trim();
                     */

                    Cell lxdhcell = row.getCell(lxdhCol);
                    String lxdh = null;
                    if (lxdhcell != null) {
                        lxdhcell.setCellType(Cell.CELL_TYPE_STRING);
                        lxdh = lxdhcell.getStringCellValue(); // 联系电话
                        if (null != lxdh)
                            lxdh = lxdh.trim();
                    }

                    Cell czdhcell = row.getCell(czdhCol);
                    String czdh = null;
                    if (czdhcell != null) {
                        czdhcell.setCellType(Cell.CELL_TYPE_STRING);
                        czdh = czdhcell.getStringCellValue(); // 传真电话
                        if (null != czdh)
                            czdh = czdh.trim();
                    }

                    Cell dzxxcell = row.getCell(dzxxCol);
                    String dzxx = null;
                    if (dzxxcell != null) {
                        dzxxcell.setCellType(Cell.CELL_TYPE_STRING);
                        dzxx = dzxxcell.getStringCellValue(); // 电子信箱
                        if (null != dzxx)
                            dzxx = dzxx.trim();
                    }

                    Cell zydzcell = row.getCell(zydzCol);
                    String zydz = null;
                    if (zydzcell != null) {
                        zydzcell.setCellType(Cell.CELL_TYPE_STRING);
                        zydz = zydzcell.getStringCellValue(); // 主页地址
                        if (null != zydz)
                            zydz = zydz.trim();
                    }

                    Cell lsygcell = row.getCell(lsygCol);
                    String lsyg = null;
                    if (lsygcell != null) {
                        lsygcell.setCellType(Cell.CELL_TYPE_STRING);
                        lsyg = lsygcell.getStringCellValue(); // 历史沿革
                        if (null != lsyg)
                            lsyg = lsyg.trim();
                    }

                    Cell xxbbmcell = row.getCell(xxbbmCol);
                    String xxbbm = null;
                    if (xxbbmcell != null) {
                        xxbbmcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxbbm = xxbbmcell.getStringCellValue(); // 学校办别码
                        if (null != xxbbm)
                            xxbbm = xxbbm.trim();
                    }

                    Cell sszgdwmcell = row.getCell(sszgdwmCol);
                    String sszgdwm = null;
                    if (sszgdwmcell != null) {
                        sszgdwmcell.setCellType(Cell.CELL_TYPE_STRING);
                        sszgdwm = sszgdwmcell.getStringCellValue(); // 所属主管单位码
                        if (null != sszgdwm)
                            sszgdwm = sszgdwm.trim();
                    }

                    Cell ssdcxlbmcell = row.getCell(ssdcxlbmCol);
                    String ssdcxlbm = null;
                    if (ssdcxlbmcell != null) {
                        ssdcxlbmcell.setCellType(Cell.CELL_TYPE_STRING);
                        ssdcxlbm = ssdcxlbmcell.getStringCellValue(); // 所在地城乡类型码
                        if (null != ssdcxlbm)
                            ssdcxlbm = ssdcxlbm.trim();
                    }

                    Cell ssdjjsxmcell = row.getCell(ssdjjsxmCol);
                    String ssdjjsxm = null;
                    if (ssdjjsxmcell != null) {
                        ssdjjsxmcell.setCellType(Cell.CELL_TYPE_STRING);
                        ssdjjsxm = ssdjjsxmcell.getStringCellValue(); // 所在地经济属性码
                        if (null != ssdjjsxm)
                            ssdjjsxm = ssdjjsxm.trim();
                    }

                    Cell szdmzsxcell = row.getCell(szdmzsxCol);
                    String szdmzsx = null;
                    if (szdmzsxcell != null) {
                        szdmzsxcell.setCellType(Cell.CELL_TYPE_STRING);
                        szdmzsx = szdmzsxcell.getStringCellValue(); // 所在地民族属性
                        if (null != szdmzsx)
                            szdmzsx = szdmzsx.trim();
                    }

                    Cell xxxzcell = row.getCell(xxxzCol);
                    String xxxz = null;
                    if (xxxzcell != null) {
                        xxxzcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxxz = xxxzcell.getStringCellValue(); // 小学学制
                        if (null != xxxz) {
                            xxxz = xxxz.trim();
                        } else {
                            if (StringUtils.isNumeric(xxxz))
                                throw new Exception("请以数字形式正确填写第" + (i + 1) + "行小学学制");
                        }
                    }

                    Cell xxrxnlcell = row.getCell(xxrxnlCol);
                    String xxrxnl = null;
                    if (xxrxnlcell != null) {
                        xxrxnlcell.setCellType(Cell.CELL_TYPE_STRING);
                        xxrxnl = xxrxnlcell.getStringCellValue(); // 小学入学年龄
                        if (null != xxrxnl)
                            xxrxnl = xxrxnl.trim();
                    }

                    Cell czxzcell = row.getCell(czxzCol);
                    String czxz = null;
                    if (czxzcell != null) {
                        czxzcell.setCellType(Cell.CELL_TYPE_STRING);
                        czxz = czxzcell.getStringCellValue(); // 初中学制
                        if (null != czxz) {
                            czxz = czxz.trim();
                        } else {
                            if (StringUtils.isNumeric(xxxz))
                                throw new Exception("请以数字形式正确填写第" + (i + 1) + "行初中学制");
                        }
                    }

                    Cell czrxnlcell = row.getCell(czrxnlCol);
                    String czrxnl = null;
                    if (czrxnlcell != null) {
                        czrxnlcell.setCellType(Cell.CELL_TYPE_STRING);
                        czrxnl = czrxnlcell.getStringCellValue(); // 初中入学年龄
                        if (null != czrxnl)
                            czrxnl = czrxnl.trim();
                    }

                    Cell gzxzcell = row.getCell(gzxzCol);
                    String gzxz = null;
                    if (gzxzcell != null) {
                        gzxzcell.setCellType(Cell.CELL_TYPE_STRING);
                        gzxz = gzxzcell.getStringCellValue(); // 高中学制
                        if (null != gzxz) {
                            gzxz = gzxz.trim();
                        } else {
                            if (StringUtils.isNumeric(xxxz))
                                throw new Exception("请以数字形式正确填写第" + (i + 1) + "行高中学制");
                        }
                    }

                    Cell zjxyymcell = row.getCell(zjxyymCol);
                    String zjxyym = null;
                    if (zjxyymcell != null) {
                        zjxyymcell.setCellType(Cell.CELL_TYPE_STRING);
                        zjxyym = zjxyymcell.getStringCellValue(); // 主教学语言码
                        if (null != zjxyym)
                            zjxyym = zjxyym.trim();
                    }

                    Cell fjxyymcell = row.getCell(fjxyymCol);
                    String fjxyym = null;
                    if (fjxyymcell != null) {
                        fjxyymcell.setCellType(Cell.CELL_TYPE_STRING);
                        fjxyym = fjxyymcell.getStringCellValue(); // 辅教学语言码
                        if (null != fjxyym)
                            fjxyym = fjxyym.trim();
                    }

                    Cell zsbjcell = row.getCell(zsbjCol);
                    String zsbj = null;
                    if (zsbjcell != null) {
                        zsbjcell.setCellType(Cell.CELL_TYPE_STRING);
                        zsbj = zsbjcell.getStringCellValue(); // 招生半径
                        if (null != zsbj)
                            zsbj = zsbj.trim();
                    }

                    /*
                     * row.getCell(bjxxCol).setCellType(Cell.CELL_TYPE_STRING);
                     * String bjxx = row.getCell(bjxxCol).getStringCellValue();
                     * //背景信息 if(null!=bjxx) bjxx = bjxx.trim();
                     */
                    if (StringUtils.isEmpty(gzxz) && StringUtils.isEmpty(czxz) && StringUtils.isEmpty(xxxz))
                        throw new Exception("请至少填写第" + (i + 1) + "行一种学制年份");

                    String xzqhm = null;
                    if (StringUtils.isNotEmpty(town))
                        xzqhm = town;
                    else if (StringUtils.isNotEmpty(county))
                        xzqhm = county;
                    else if (StringUtils.isNotEmpty(city))
                        xzqhm = city;

                    School sch = new School();
                    sch.setOrgid(nextId); // 组织机构id
                    sch.setXxdm(code); // 组织机构编码
                    sch.setXxmc(name); // 学校名称
                    sch.setXxywmc(ywmc); // 英文名称
                    sch.setXxdz(xxdz); // 学校地址
                    sch.setXxyzbm(xxyzbm); // 邮政编码
                    sch.setXzqhm(xzqhm); // 行政区划码
                    sch.setJxny(jxny); // 建校年月
                    sch.setXqr(xqr); // 校庆日
                    sch.setXxbxlxm(xxbxlxm); // 学校办学类型码
                    sch.setXxjbzm(xxjbzm); // 学校举办者码
                    sch.setXxzgbmm(xxzgbmm); // 学校主管部门码
                    sch.setFddbrh(fddbrh); // 法定代表人号
                    sch.setFrzsh(frzsh); // 法人证书号
                    sch.setXzgh(xzgh); // 校长工号
                    sch.setXzxm(xzxm); // 校长姓名
                    sch.setDwfzrh(dwfzrh); // 党委负责人号
                    sch.setZzjgm(code); // 组织机构码
                    sch.setLxdh(lxdh); // 联系电话
                    sch.setCzdh(czdh); // 传真电话
                    sch.setDzxx(dzxx); // 电子信箱
                    sch.setZydz(zydz); // 主页地址
                    sch.setLsyg(lsyg); // 历史沿革
                    sch.setXxbbm(xxbbm); // 学校办别码
                    sch.setSszgdwm(sszgdwm); // 所属主管单位码
                    sch.setSzdcxlxm(ssdcxlbm); // 所属地区类型码
                    sch.setSzdjjsxm(ssdjjsxm); // 所在地经济属性码
                    sch.setSzdmzsx(szdmzsx); // 所在地民族属性
                    sch.setXxxz((StringUtils.isNumeric(xxxz) && StringUtils.isNotEmpty(xxxz)) ? new BigDecimal(xxxz)
                            : null); // 小学学制
                    sch.setXxrxnl((StringUtils.isNumeric(xxrxnl) && StringUtils.isNotEmpty(xxrxnl)) ? new Short(xxrxnl)
                            : null); // 小学入学年龄
                    sch.setCzxz((StringUtils.isNumeric(czxz) && StringUtils.isNotEmpty(czxz)) ? new BigDecimal(czxz)
                            : null); // 初中学制
                    sch.setCzrxnl((StringUtils.isNumeric(czrxnl) && StringUtils.isNotEmpty(czrxnl)) ? new Short(czrxnl)
                            : null); // 小学入学年龄
                    sch.setGzxz((StringUtils.isNumeric(gzxz) && StringUtils.isNotEmpty(gzxz)) ? new BigDecimal(gzxz)
                            : null); // 高中学制
                    sch.setZjxyym(zjxyym); // 主教学语言码
                    sch.setFjxyym(fjxyym); // 辅教学语言码
                    sch.setZsbj(zsbj); // 招生半径
                    // sch.setPx((StringUtils.isNumeric(sort) &&
                    // StringUtils.isNotEmpty(sort))?Long.parseLong(sort):null);
                    // //排序
                    // sch.setBjxx(bjxx); //背景信息

                    // 判断该增加还是更新

                    if (!codeSchMap.containsKey(code)) {
                        schs_to_insert.add(sch);
                    } else {
                        schs_to_update.add(sch);
                    }
                }
            }

            if (orgs_to_insert.size() > 0)
                orgMapper.insertBatch(orgs_to_insert);
            if (orgs_to_update.size() > 0)
                orgMapper.updateBatch(orgs_to_update);
            if (schs_to_insert.size() > 0)
                schoolMapper.insertBatch(schs_to_insert);
            if (schs_to_update.size() > 0)
                schoolMapper.updateBatch(schs_to_update);
        } catch (Exception e) {
            stream.close();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    public void importEdu(String excelUrl, Organization corg) throws Exception {

        String[] titles = new String[] { "编号", "机构代码", "教委名称", "所属省", "所属市", "所属区县", "教委地址", "教委负责人", "传真电话", "联系电话",
                "电子信箱", "主页地址" };

        Map<String, Integer> colIndex = new HashMap<String, Integer>();

        List<Organization> existOrgs = orgMapper.selectAllOrganizations();
        List<Educommission> existEcs = educommissionMapper.getEdusUnderOrgid(corg.getId());
        Long maxId = orgMapper.getMaxId();
        sequenceMapper.updateSeqId(maxId + 1, "organization");
        Map<String, Organization> codeOrgMap = new HashMap<String, Organization>();
        for (Organization org : existOrgs) {
            byte[] orgByte = SerializationUtils.serialize(org);
            codeOrgMap.put(org.getCode(), (Organization) SerializationUtils.deserialize(orgByte));
        }
        Map<String, Educommission> codeEcMap = new HashMap<String, Educommission>();
        for (Educommission ec : existEcs) {
            byte[] ecByte = SerializationUtils.serialize(ec);
            // 强制必须有机构代码，并且全局唯一
            codeEcMap.put(ec.getJwdm(), (Educommission) SerializationUtils.deserialize(ecByte));
        }
        Map<String, Organization> parentIds = new HashMap<String, Organization>();
        List<Organization> orgs_to_insert = new ArrayList<Organization>();
        List<Organization> orgs_to_update = new ArrayList<Organization>();
        List<Educommission> ecs_to_insert = new ArrayList<Educommission>();
        List<Educommission> ecs_to_update = new ArrayList<Educommission>();

        Map<String, String> orgtypeMap = revert(dicService.selectAllDicMap("dic_org_type"));
        Map<String, String> orglevelMap = revert(dicService.selectAllDicMap("dic_organizationlevel"));
        final District prov = districtMapper.selectByCode(Constants.APPLICATION_PROVINCECODE);
        Map<String, String> provinceMap = new HashMap<String, String>() {
            {
                // put("江苏省", Constants.APPLICATION_CITYCODE);
                put(prov.getName(), Constants.APPLICATION_PROVINCECODE);
            }
        };
        final District ct = districtMapper.selectByCode(Constants.APPLICATION_CITYCODE);
        Map<String, String> cityMap = new HashMap<String, String>() {
            {
                put(ct.getName(), Constants.APPLICATION_CITYCODE);
            }
        };
        Map<String, String> countyMap = new HashMap<String, String>();
        Map<String, String> townMap = new HashMap<String, String>();

        List<District> citys = districtMapper.getSubCities(Constants.APPLICATION_PROVINCECODE);
        for (District city : citys) {
            cityMap.put(city.getName(), city.getCode());
            List<District> counties = districtMapper.getSubCounties(city.getCode());
            for (District county : counties) {
                countyMap.put(county.getName(), county.getCode());
                // List<District> towns =
                // districtMapper.getSubTowns(county.getCode());
                // for(District town: towns){
                // townMap.put(town.getName(), town.getCode());
                // }
            }
        }

        String sheetName = "组织机构";
        try {
            stream = new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            String fileName = excelUrl.substring(prefix + 1);
            if (StringUtils.endsWith(fileName, ".xls"))
                read2003Excel(stream);
            else if (StringUtils.endsWith(fileName, ".xlsx"))
                read2007Excel(stream);
            Sheet sheet = wb.getSheet(sheetName);
            colIndex = lookupIndex(titles, sheet.getRow(1));

            // 组织机构信息

            int idCol = colIndex.get("编号");
            int codeCol = colIndex.get("机构代码");
            int nameCol = colIndex.get("教委名称");
            int countyCol = colIndex.get("所属区县");
            int jwfzrCol = colIndex.get("教委负责人");
            int jwdzCol = colIndex.get("教委地址");
            int lxdhCol = colIndex.get("联系电话");
            int czdhCol = colIndex.get("传真电话");
            int dzxxCol = colIndex.get("电子信箱");
            int zydzCol = colIndex.get("主页地址");

            Long nextId = -1l;

            for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
                System.out.println(i);
                int insert_or_update = 0; // 1-insert,2-update
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                Cell idcell = row.getCell(idCol);
                if (null == idcell) {
                    continue;
                }
                idcell.setCellType(Cell.CELL_TYPE_STRING);
                String id = idcell.getStringCellValue(); // 编号
                if (StringUtils.isEmpty(id))
                    // throw new Exception("请填写第"+(i+1)+"行机构编号");
                    continue;

                Cell codecell = row.getCell(codeCol);
                codecell.setCellType(Cell.CELL_TYPE_STRING);
                String code = codecell.getStringCellValue(); // 编码
                if (null != code) {
                    code = code.trim();
                } else {
                    throw new Exception("请填写第" + (i + 1) + "行组织机构码");
                }
                if (!codeOrgMap.containsKey(code)) {
                    insert_or_update = 1;
                    maxId = maxId + 1;
                    nextId = maxId;
                } else {
                    insert_or_update = 2;
                    nextId = codeOrgMap.get(code).getId();
                }

                Cell namecell = row.getCell(nameCol);
                namecell.setCellType(Cell.CELL_TYPE_STRING);
                String name = namecell.getStringCellValue(); // 名称
                if (null != name) {
                    name = name.trim();
                } else {
                    throw new Exception("请填写第" + (i + 1) + "行机构名称");
                }

                String parentId = null;
                if (null != corg.getId()) {
                    // 只能导入当前组织的子机构
                    parentId = Long.toString(corg.getId());
                } else {
                    throw new Exception("系统错误，请退出重新登录再导入机构信息");
                }

                String parentids = StringUtils.isNotEmpty(corg.getParentIds()) ? corg.getParentIds() + "/" + parentId
                        : parentId;
                String orgtype = getOrgType(code); // 组织类型
                String orglevel = Integer.toString(getOrgLevel(code));
                String province = Constants.APPLICATION_PROVINCECODE;

                String city = Constants.APPLICATION_CITYCODE;

                Cell countycell = row.getCell(countyCol);
                String county = null;
                if (null != countycell) {
                    countycell.setCellType(Cell.CELL_TYPE_STRING);
                    county = countycell.getStringCellValue(); // 区县
                    if (null != county) {
                        if (countyMap.containsKey(county))
                            county = countyMap.get(county);
                        else
                            throw new Exception("请根据填写说明正确填写第" + (i + 1) + "行所属区县");
                    } else {
                        throw new Exception("请填写第" + (i + 1) + "行所属区县");
                    }
                }

                Organization org = new Organization();

                org.setCode(code);
                org.setName(name);
                org.setOrgType(orgtype);
                org.setOrgLevel(
                        StringUtils.isNumeric(orglevel) && !"".equals(orglevel) ? Integer.parseInt(orglevel) : -1);
                org.setProvinceid(province);
                org.setCityid(city);
                org.setCountyid(county);

                org.setParentId(
                        StringUtils.isNumeric(parentId) && !"".equals(parentId) ? Long.parseLong(parentId) : -1);
                org.setIsshow(1);
                org.setSort(0);
                org.setParentIds(parentids);
                org.setLocked("");

                if (insert_or_update == 1) {
                    orgs_to_insert.add(org);
                    org.setId(nextId);
                    codeOrgMap.put(code, org);
                    parentIds.put(name, org);
                } else if (insert_or_update == 2) {
                    orgs_to_update.add(org);
                    org.setId(nextId);
                    codeOrgMap.put(code, org);
                    parentIds.put(name, org);
                }

                // 学校的信息组装
                if (orgtype.equals(orgtypeMap.get("教委"))) {
                    Cell jwdzcell = row.getCell(jwdzCol);
                    String jwdz = null;
                    if (jwdzcell != null) {
                        jwdzcell.setCellType(Cell.CELL_TYPE_STRING);
                        jwdz = jwdzcell.getStringCellValue(); // 英文名称
                        if (null != jwdz)
                            jwdz = jwdz.trim();
                    }

                    Cell jwfzrcell = row.getCell(jwfzrCol);
                    String jwfzr = null;
                    if (jwfzrcell != null) {
                        jwfzrcell.setCellType(Cell.CELL_TYPE_STRING);
                        jwfzr = jwfzrcell.getStringCellValue(); // 学校地址
                        if (null != jwfzr)
                            jwfzr = jwfzr.trim();
                    }

                    Cell lxdhcell = row.getCell(lxdhCol);
                    String lxdh = null;
                    if (lxdhcell != null) {
                        lxdhcell.setCellType(Cell.CELL_TYPE_STRING);
                        lxdh = lxdhcell.getStringCellValue(); // 学校邮政编码
                        if (null != lxdh)
                            lxdh = lxdh.trim();
                    }

                    Cell czdhcell = row.getCell(czdhCol);
                    String czdh = null;
                    if (czdhcell != null) {
                        czdhcell.setCellType(Cell.CELL_TYPE_STRING);
                        czdh = czdhcell.getStringCellValue(); // 学校邮政编码
                        if (null != czdh)
                            czdh = czdh.trim();
                    }

                    Cell dzxxcell = row.getCell(dzxxCol);
                    String dzxx = null;
                    if (dzxxcell != null) {
                        dzxxcell.setCellType(Cell.CELL_TYPE_STRING);
                        dzxx = dzxxcell.getStringCellValue(); // 建校年月
                        if (null != dzxx)
                            dzxx = dzxx.trim();
                    }

                    Cell zydzcell = row.getCell(zydzCol);
                    String zydz = null;
                    if (zydzcell != null) {
                        zydzcell.setCellType(Cell.CELL_TYPE_STRING);
                        zydz = zydzcell.getStringCellValue(); // 主页地址
                        if (null != zydz)
                            zydz = zydz.trim();
                    }

                    Educommission ec = new Educommission();
                    ec.setOrgid(nextId); // 组织机构id
                    ec.setJwdm(code); // 组织机构编码
                    ec.setJwmc(name); // 学校名称
                    ec.setJwdz(jwdz); // 学校地址
                    ec.setJwfzr(jwfzr);
                    ec.setLxdh(lxdh);
                    ec.setCzdh(czdh);
                    ec.setDzxx(dzxx); // 电子信箱
                    ec.setZydz(zydz); // 主页地址

                    // 判断该增加还是更新

                    if (!codeEcMap.containsKey(code)) {
                        ecs_to_insert.add(ec);
                    } else {
                        ecs_to_update.add(ec);
                    }
                }
            }

            if (orgs_to_insert.size() > 0)
                orgMapper.insertBatch(orgs_to_insert);
            if (orgs_to_update.size() > 0)
                orgMapper.updateBatch(orgs_to_update);

            if (ecs_to_insert.size() > 0)
                educommissionMapper.insertBatch(ecs_to_insert);
            if (ecs_to_update.size() > 0)
                educommissionMapper.updateBatch(ecs_to_update);
        } catch (Exception e) {
            stream.close();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    public void importOrg1(String excelUrl) throws Exception {
        // TODO Auto-generated method stub
        // String excelUrl = "C:/Users/s/Desktop/nj.xlsx";
        // String excelUrl =
        // "J:/其他文档/项目文档/心理健康教育与咨询管理平台/4.0/需求文档/svn模板/量表上传测试结果/产权量表/可上传/5.小学生学科兴趣问卷0328.xlsx";
        int org_nameCol = 2;
        int org_codeCol = 3;
        int org_idCol = 5;

        List<String> parent_ids = new ArrayList<String>();
        List<String> parent_codes = new ArrayList<String>();
        try {
            stream = new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            String fileName = excelUrl.substring(prefix + 1);
            if (StringUtils.endsWith(fileName, ".xls"))
                read2003Excel(stream);
            else if (StringUtils.endsWith(fileName, ".xlsx"))
                read2007Excel(stream);

            // 处理中学组织机构
            Sheet sheet = wb.getSheet("org");
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String name = row.getCell(org_nameCol - 1).getStringCellValue();
                String code = row.getCell(org_codeCol - 1).getStringCellValue();
                if (null != code)
                    code = code.trim();
                Cell idCell = row.getCell(org_idCol - 1);
                idCell.setCellType(Cell.CELL_TYPE_STRING);
                String id = idCell.getStringCellValue();
                Integer orgLevel = getOrgLevel(code);
                String parentCode = getParentCode(code);
                Long parentId = parentCode == null ? 0 : getOrgPrentId(parentCode);
                String orgtype = getOrgType(code);
                if (parent_ids.size() != 0) {
                    if (code.length() == parent_codes.get(parent_codes.size() - 1).length()) {
                        // 比原来的节点级别低不用处理，和原来级别一样，换最后一个，比原来级别高，看高几级，删几个
                        // 通常不会出现现在的级别比原来的高两级的情况，因为parent_codes最多只有两个，又不会重新出现南京教委，所以不用考虑高两级
                        parent_codes.remove(parent_codes.size() - 1);
                        parent_ids.remove(parent_ids.size() - 1);
                    }
                    // else if(code.length() <
                    // parent_codes.get(parent_codes.size()-1).length()){
                    // Integer parentLevel =
                    // getOrgLevel(parent_codes.get(parent_codes.size()-1));
                    // int levelDiff = parentLevel - orgLevel;
                    // for(int j=levelDiff;j>=0;j--){
                    //
                    // }
                    // }
                }
                if (!isCitySubSchool(code)) {
                    if (orgMapper.getOrgByCode(code).size() == 0) {
                        Organization org = new Organization();
                        org.setId(Long.parseLong(id));
                        org.setCode(code);
                        org.setName(name);
                        org.setOrgType(orgtype);
                        org.setOrgLevel(orgLevel);
                        org.setSort(0);
                        org.setIsshow(1);
                        org.setParentId(parentId);
                        org.setParentIds("0/" + StringUtils.join(parent_ids, "/"));
                        org.setProvinceid(provinceid);
                        org.setCityid(cityid);
                        if (orgLevel >= 4) {
                            if (OrganizationType.school.getId().equals(orgtype)) {
                                org.setCountyid(getParentCode(code));
                            } else {
                                if (!code.equals("320100"))
                                    org.setCountyid(code);
                            }
                        }
                        orgMapper.insertSelective(org);

                        if (OrganizationType.school.getId().equals(orgtype)) {
                            School sch = new School();
                            sch.setXxdm(code);
                            sch.setXxmc(name);
                            sch.setXzqhm(getParentCode(code));
                            sch.setOrgid(Long.parseLong(id));
                            schoolMapper.insertSelective(sch);
                        }
                        if (code.length() < 11) {
                            parent_codes.add(code);
                            parent_ids.add(id);
                        }
                    }
                }
            }

            // 处理小学组织机构
            sheet = wb.getSheet("org1");
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String name = row.getCell(org_nameCol - 1).getStringCellValue();
                String code = row.getCell(org_codeCol - 1).getStringCellValue();
                if (null != code)
                    code = code.trim();
                Cell idCell = row.getCell(org_idCol - 1);
                idCell.setCellType(Cell.CELL_TYPE_STRING);
                String id = idCell.getStringCellValue();
                Integer orgLevel = getOrgLevel(code);
                String parentCode = getParentCode(code);
                Long parentId = parentCode == null ? 0 : getOrgPrentId(parentCode);
                String orgtype = getOrgType(code);
                if (parent_ids.size() != 0) {
                    if (code.length() == parent_codes.get(parent_codes.size() - 1).length()) {
                        // 比原来的节点级别低不用处理，和原来级别一样，换最后一个，比原来级别高，看高几级，删几个
                        // 通常不会出现现在的级别比原来的高两级的情况，因为parent_codes最多只有两个，又不会重新出现南京教委，所以不用考虑高两级
                        parent_codes.remove(parent_codes.size() - 1);
                        parent_ids.remove(parent_ids.size() - 1);
                    }
                    /*
                     * else if(code.length() <
                     * parent_codes.get(parent_codes.size()-1).length()){
                     * Integer parentLevel =
                     * getOrgLevel(parent_codes.get(parent_codes.size()-1)); int
                     * levelDiff = parentLevel - orgLevel; for(int
                     * j=levelDiff;j>=0;j--){
                     * 
                     * } }
                     */
                }
                if (!isCitySubSchool(code)) {
                    List<Organization> orgs = orgMapper.getOrgByCode(code);
                    if (orgs.size() == 0) {
                        Organization org = new Organization();
                        org.setId(Long.parseLong(id));
                        org.setCode(code);
                        org.setName(name);
                        org.setOrgType(orgtype);
                        org.setOrgLevel(orgLevel);
                        org.setSort(0);
                        org.setIsshow(1);
                        org.setParentId(parentId);
                        org.setParentIds("0/" + StringUtils.join(parent_ids, "/"));
                        org.setProvinceid(provinceid);
                        org.setCityid(cityid);
                        if (orgLevel >= 4) {
                            if (OrganizationType.school.getId().equals(orgtype)) {
                                org.setCountyid(getParentCode(code));
                            } else {
                                if (!code.equals("320100"))
                                    org.setCountyid(code);
                            }
                        }
                        orgMapper.insertSelective(org);

                        if (OrganizationType.school.getId().equals(orgtype)) {
                            School sch = new School();
                            sch.setXxdm(code);
                            sch.setXxmc(name);
                            sch.setXzqhm(getParentCode(code));
                            sch.setOrgid(Long.parseLong(id));
                            schoolMapper.insertSelective(sch);
                        }
                        if (code.length() < 11) {
                            parent_codes.add(code);
                            parent_ids.add(id);
                        }
                    } else {
                        if (code.length() < 11) {
                            parent_codes.add(code);
                            parent_ids.add(Long.toString(orgs.get(0).getId()));
                        }
                    }
                }
            }

            stream.close();
        } catch (Exception e) {
            stream.close();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /*
         * SqlSessionFactory sqlSessionFactory; String resource =
         * "com/njpes/www/service/baseinfo/mybatis-config.xml"; Reader reader =
         * Resources.getResourceAsReader(resource);
         * 
         * if (sqlSessionFactory == null) { sqlSessionFactory = new
         * SqlSessionFactoryBuilder().build(reader); }
         * 
         * SqlSession session = sqlSessionFactory.openSession();
         * session.selectList("", arg1); OrganizationMapper orgMapper =
         * (OrganizationMapper)ac.getBean("OrganizationMapper"); while (true) {
         * System.out.println(orgMapper.selectAllOrganizations());
         * Thread.sleep(10000); }
         */
    }

    @Override
    public Integer updateSeqId(Long maxId) {
        // TODO Auto-generated method stub
        return sequenceMapper.updateSeqId(maxId, "organization");
    }

}
