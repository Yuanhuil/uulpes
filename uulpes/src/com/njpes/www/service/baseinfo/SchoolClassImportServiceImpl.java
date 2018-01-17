package com.njpes.www.service.baseinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.AccountMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.DistrictMapper;
import com.njpes.www.dao.baseinfo.GradeCodeMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.service.util.DictionaryServiceI;

import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

@Service("SchoolClassImportService")
public class SchoolClassImportServiceImpl implements SchoolClassImportServiceI {

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
    DictionaryServiceI dicService;

    @Autowired
    GradeCodeMapper gradeMapper;

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

    public void importSchoolClass(String excelUrl, Organization corg) throws Exception {
        List<ClassSchool> existClass = classMapper.selectClassBySchool(corg.getId());
        Map<String, ClassSchool> codeClsMap = new HashMap<String, ClassSchool>();
        for (ClassSchool cs : existClass) {
            byte[] classByte = SerializationUtils.serialize(cs);
            codeClsMap.put(cs.getBh(), (ClassSchool) SerializationUtils.deserialize(classByte));
        }
        String[] titles = new String[] { "编号", "班号", "班级名称", "建班年月", "班主任工号", "班长学号", "班级荣誉称号", "学制", "班级类型", "文理类型",
                "毕业日期", "是否少数民族双语教学班", "双语教学模式", "年级" };

        List<ClassSchool> classes_to_insert = new ArrayList<ClassSchool>();
        List<ClassSchool> classes_to_update = new ArrayList<ClassSchool>();

        String sheetName = "class";
        Map<String, Integer> colIndex = new HashMap<String, Integer>();

        Long nextId = -1l;
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

            // 班级信息

            int idCol = colIndex.get("编号");
            int bhCol = colIndex.get("班号");
            int bjmcCol = colIndex.get("班级名称");
            int jbnyCol = colIndex.get("建班年月");
            int bzrghCol = colIndex.get("班主任工号");
            int bzxhCol = colIndex.get("班长学号");
            int bjrychCol = colIndex.get("班级荣誉称号");
            int xzCol = colIndex.get("学制");
            int bjlxmCol = colIndex.get("班级类型");
            int wllxCol = colIndex.get("文理类型");
            int byrqCol = colIndex.get("毕业日期");
            int sfshmzsyjybCol = colIndex.get("是否少数民族双语教学班");
            int syjxmsmCol = colIndex.get("双语教学模式");
            int gradeorderCol = colIndex.get("年级");
            int rowNum = sheet.getLastRowNum();
            for (int i = 1; i <= rowNum; i++) {
                System.out.println(i);
                int insert_or_update = 0; // 1-insert,2-update
                Row row = sheet.getRow(i);
                if (null == row.getCell(idCol)) {
                    continue;
                }

                /*
                 * String id = null; if(null != row.getCell(idCol-1)){
                 * row.getCell(idCol-1).setCellType(Cell.CELL_TYPE_STRING); id =
                 * row.getCell(idCol-1).getStringCellValue(); //编号
                 * if(StringUtils.isEmpty(id)) continue; }
                 */

                String bh = null;
                if (null != row.getCell(bhCol)) {
                    row.getCell(bhCol).setCellType(Cell.CELL_TYPE_STRING);
                    bh = row.getCell(bhCol).getStringCellValue(); // 班号
                    if (null != bh) {
                        bh = bh.trim();
                    } else {
                        continue;
                    }

                    if (!codeClsMap.containsKey(bh)) {
                        insert_or_update = 1;
                    } else {
                        insert_or_update = 2;
                        nextId = codeClsMap.get(bh).getId();
                    }
                } else {
                    throw new Exception("请填写第" + (i + 1) + "行的班号");
                }

                String bjmc = null;
                if (null != row.getCell(bjmcCol)) {
                    row.getCell(bjmcCol).setCellType(Cell.CELL_TYPE_STRING);
                    bjmc = row.getCell(bjmcCol).getStringCellValue(); // 名称
                    if (null != bjmc)
                        bjmc = bjmc.trim();
                } else {
                    throw new Exception("请填写第" + (i + 1) + "行的班级名称");
                }

                String jbny = null;
                if (null != row.getCell(jbnyCol)) {
                    row.getCell(jbnyCol).setCellType(Cell.CELL_TYPE_STRING);
                    jbny = row.getCell(jbnyCol).getStringCellValue(); // 建班年月
                }

                String bjrych = null;
                if (null != row.getCell(bjrychCol)) {
                    row.getCell(bjrychCol).setCellType(Cell.CELL_TYPE_STRING);
                    bjrych = row.getCell(bjrychCol).getStringCellValue(); // 班级荣誉称号
                }

                String xz = null;
                if (null != row.getCell(xzCol)) {
                    row.getCell(xzCol).setCellType(Cell.CELL_TYPE_STRING);
                    xz = row.getCell(xzCol).getStringCellValue(); // 学制
                }

                String bjlxm = null;
                if (null != row.getCell(bjlxmCol)) {
                    row.getCell(bjlxmCol).setCellType(Cell.CELL_TYPE_STRING);
                    bjlxm = row.getCell(bjlxmCol).getStringCellValue(); // 班级类型码
                    if (null != bjlxm)
                        bjlxm = bjlxm.trim();
                }

                String wllx = null;
                if (null != row.getCell(wllxCol)) {
                    row.getCell(wllxCol).setCellType(Cell.CELL_TYPE_STRING);
                    wllx = row.getCell(wllxCol).getStringCellValue(); // 文理类型
                }

                String byrq = null;
                if (null != row.getCell(byrqCol)) {
                    row.getCell(byrqCol).setCellType(Cell.CELL_TYPE_STRING);
                    byrq = row.getCell(byrqCol).getStringCellValue(); // 毕业日期
                }

                String sfshmzsyjyb = null;
                if (null != row.getCell(sfshmzsyjybCol)) {
                    row.getCell(sfshmzsyjybCol).setCellType(Cell.CELL_TYPE_STRING);
                    sfshmzsyjyb = row.getCell(sfshmzsyjybCol).getStringCellValue(); // 是否少数民族双语教育班
                }

                String syjxmsm = null;
                if (null != row.getCell(syjxmsmCol)) {
                    row.getCell(syjxmsmCol).setCellType(Cell.CELL_TYPE_STRING);
                    syjxmsm = row.getCell(syjxmsmCol).getStringCellValue(); // 双语教育模式码
                }

                String gradeorder = null;
                if (null != row.getCell(gradeorderCol)) {
                    row.getCell(gradeorderCol).setCellType(Cell.CELL_TYPE_STRING);
                    gradeorder = row.getCell(gradeorderCol).getStringCellValue(); // 年级
                    if (StringUtils.isNotEmpty(gradeorder))
                        gradeorder = Integer.toString(gradeMapper.getIdByName(gradeorder));
                }

                ClassSchool cls = new ClassSchool();

                cls.setBh(bh);
                cls.setBjmc(bjmc);
                cls.setJbny(jbny);
                cls.setBjrych(bjrych);
                cls.setXz(new BigDecimal(xz));
                cls.setBjlxm(bjlxm);
                cls.setWllx(wllx);
                cls.setByrq(byrq);
                cls.setSfssmzsyjxb(sfshmzsyjyb);
                cls.setSyjxmsm(syjxmsm);
                cls.setXxdm(corg.getCode());
                // cls.setNj(nj);
                cls.setGradeid(StringUtils.isNumeric(gradeorder) && !"".equals(gradeorder)
                        ? Integer.parseInt(gradeorder) : null);
                cls.setXxorgid(corg.getId());

                if (insert_or_update == 1) {
                    classes_to_insert.add(cls);
                    codeClsMap.put(bh, cls);
                } else if (insert_or_update == 2) {
                    classes_to_update.add(cls);
                    codeClsMap.put(bh, cls);
                }

            }

            if (classes_to_insert.size() > 0)
                classMapper.insertBatch(classes_to_insert);
            if (classes_to_update.size() > 0)
                classMapper.updateBatch(classes_to_update);

        } catch (Exception e) {
            e.printStackTrace();
            stream.close();
            throw new Exception(e.getMessage());
        }
    }
}
