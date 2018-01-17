package edutec.scale.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import edutec.scale.model.ScaleHidenPage;
import heracles.excel.WorkbookUtils;

public class HiddenSheetParser extends SheetParser {

    private Map propMap;

    public HiddenSheetParser() {
        // TODO Auto-generated constructor stub
        propMap = new HashMap();
        propMap.put("id", "2_3");
        propMap.put("testType", "3_3");
        propMap.put("source", "4_3");
        propMap.put("scaleType", "5_3");
        propMap.put("applicablePerson", "6_3");
        propMap.put("reportGraph", "7_3");
        propMap.put("examineTime", "8_3");
    }

    @Override
    public void parse(Sheet sheet, Object obj) throws Exception {
        // TODO Auto-generated method stub
        String[] rc = ((String) propMap.get("id")).split("_");
        String id = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        if (StringUtils.isNotEmpty(id)) {
            // ((StringBuffer) obj).delete(0, ((StringBuffer) obj).length());
            // ((StringBuffer) obj).append(id);
            // ((HashMap)obj).put("id",id);
            ((ScaleHidenPage) obj).setId(id);
        }
        rc = ((String) propMap.get("testType")).split("_");
        String testType = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        if (StringUtils.isNotEmpty(testType)) {
            ((ScaleHidenPage) obj).setTestType(testType);
        }
        // TODO Auto-generated method stub
        rc = ((String) propMap.get("source")).split("_");
        String source = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        if (StringUtils.isNotEmpty(source)) {
            ((ScaleHidenPage) obj).setSource(source);
        }
        // TODO Auto-generated method stub
        rc = ((String) propMap.get("scaleType")).split("_");
        String scaleType = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        if (StringUtils.isNotEmpty(scaleType)) {
            ((ScaleHidenPage) obj).setScaleType(scaleType);
        }
        // TODO Auto-generated method stub
        rc = ((String) propMap.get("applicablePerson")).split("_");
        String applicablePerson = WorkbookUtils
                .get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1).getStringCellValue();
        if (StringUtils.isNotEmpty(applicablePerson)) {
            ((ScaleHidenPage) obj).setApplicablePerson(applicablePerson);
        }
        // TODO Auto-generated method stub
        rc = ((String) propMap.get("reportGraph")).split("_");
        String reportGraph = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        if (StringUtils.isNotEmpty(reportGraph)) {
            ((ScaleHidenPage) obj).setReportGraph(reportGraph);
        }

        rc = ((String) propMap.get("examineTime")).split("_");
        String examineTime = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        Pattern pattern = Pattern.compile("[0-9]*");
        if (StringUtils.isNotEmpty(examineTime)) {
            if (pattern.matcher(examineTime).matches()) {
                long etime = Long.parseLong(examineTime);
                ((ScaleHidenPage) obj).setExamineTime(etime);
            } else {
                throw new Exception("输入测试时间有误");
            }
        }
    }
}
