package edutec.scale.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import edutec.scale.model.Dimension;
import edutec.scale.model.ScaleNormObject;
import heracles.excel.WorkbookUtils;

public class ScaleNormParser {
    private String normGradeOrAgeFlag;

    public void parse(Sheet sheet, List<ScaleNormObject> normObjList, Map<String, Dimension> dimensionMap) {
        HashMap<String, Integer> cacheMap = new HashMap<String, Integer>();
        int chenrenFlag = 0;
        int stopFlag = 0;
        // 读取第3行，取出每个维度所对应的第一列
        Row titleRow = sheet.getRow(2);
        int columnNum = titleRow.getLastCellNum();
        int dimNum = (columnNum - 2) / 2;
        for (int i = 1; i <= dimNum; i++) {
            if (stopFlag == 1) {
                break;
            }
            Cell ccell = WorkbookUtils.get07Cell(sheet, 2, i * 2);
            if (ccell == null) {
                break;
            }
            String dimValue = ccell.getStringCellValue().trim();
            // 寻找维度所对应的维度id
            Iterator<String> it = dimensionMap.keySet().iterator();
            while (it.hasNext()) {
                String dimId = it.next();
                Dimension dimension = dimensionMap.get(dimId);
                if (dimension.getTitle().equals(dimValue)) {
                    // 记住每个维度的id和其对应的行
                    cacheMap.put(dimId, i * 2);
                    break;
                }
            }
        }
        // 读出第四行，判断常模是记录年龄还是年级
        String gradeOrAgeFlag = WorkbookUtils.get07Cell(sheet, 3, 0).getStringCellValue();
        if (gradeOrAgeFlag.equals("年级")) {
            normGradeOrAgeFlag = "grade";
        }
        if (gradeOrAgeFlag.equals("年龄")) {
            normGradeOrAgeFlag = "age";
        }
        // 下面开始一行一行扫描，每一行都代表数据库中的记录
        int rowNum = sheet.getLastRowNum();
        for (int m = 4; m <= rowNum; m++) {
            if (stopFlag == 1) {
                break;
            }
            String grade = "";
            try {
                grade = WorkbookUtils.get07Cell(sheet, m, 0).getStringCellValue().trim();
                if ((grade.trim()).equalsIgnoreCase("成人")) {
                    chenrenFlag = 1;
                    m = m + 2;
                    continue;
                }
            } catch (Exception e) {

            }
            // int age = 0;
            // if(StringUtils.isNotEmpty(grade))
            // age = Integer.parseInt(grade);
            int gender = 0;
            String genderCell = null;
            try {
                genderCell = WorkbookUtils.get07Cell(sheet, m, 1).getStringCellValue().trim();
            } catch (Exception e) {
                continue;
            }
            if (!genderCell.equals("")) {
                if (genderCell.equals("男")) {
                    gender = 1;
                } else {
                    gender = 2;
                }
            }
            // 循环遍历维度
            Iterator<String> iterator = cacheMap.keySet().iterator();
            while (iterator.hasNext()) {
                ScaleNormObject sno = new ScaleNormObject();
                String dimId = iterator.next();
                int startColumn = cacheMap.get(dimId);
                double mValue = 0;
                try {
                    mValue = Double.parseDouble(WorkbookUtils.get07Cell(sheet, m, startColumn).getStringCellValue());
                } catch (Exception e) {
                    stopFlag = 1;
                    break;
                }
                double sd = Double.parseDouble(WorkbookUtils.get07Cell(sheet, m, startColumn + 1).getStringCellValue());
                sno.setGender(gender);
                sno.setM(mValue);
                sno.setSd(sd);
                sno.setW_id(dimId);
                // sno.setAge(age);
                if (StringUtils.isNotEmpty(grade))
                    // sno.setGrade_id(GradeCodes.getGradeId(grade));
                    try {
                    sno.setGrade_id(Integer.parseInt(grade));
                    } catch (Exception e) {
                    if (gradeOrAgeFlag.equals("年龄")) {
                    String[] ages = grade.split("~|-|—");
                    int agemin = Integer.parseInt(ages[0]);
                    sno.setAgemin(agemin);
                    if (ages.length == 2) {
                    int agemax = Integer.parseInt(ages[1]);
                    sno.setAgemax(agemax);
                    } else {
                    sno.setAgemax(120);
                    }
                    }
                    // sno.setAges(grade);
                    }
                // 如果是成人，那么 grade就设置成-1
                if (chenrenFlag == 1) {
                    sno.setGrade_id(14);
                }
                normObjList.add(sno);
            }
        }
    }

    public String getNormGradeOrAgeFlag() {
        return normGradeOrAgeFlag;
    }

    public void setNormGradeOrAgeFlag(String normGradeOrAgeFlag) {
        this.normGradeOrAgeFlag = normGradeOrAgeFlag;
    }

}
