package edutec.scale.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import edutec.scale.model.Dimension;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.util.ScaleUtils;
import heracles.excel.WorkbookUtils;

public class ExplainSheetParser {
    private Map propMap;

    public ExplainSheetParser() {

    }

    public void parse(Sheet expSheet, Sheet stuInstrSheet, List<ScaleExplainObject> explainList,
            Map<String, Dimension> dimensionMap, String flag, boolean hasTotalScore, String scaleId) {
        if (ScaleUtils.isMBTIScale(scaleId)) {
            this.parseMBTI(expSheet, stuInstrSheet, explainList, dimensionMap, flag, hasTotalScore, scaleId);
            return;
        }

        // 采用新的对象格式来进行结果解释的插入

        int rownum = expSheet.getLastRowNum();
        int i = 4;
        while (i <= rownum) {
            // 维度名称
            String wdtitile = "";
            try {
                wdtitile = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i, 0).getStringCellValue();
            } catch (Exception e) {
                return;
            }
            if (wdtitile.equals("")) {
                i++;
                continue;
            }
            // 维度级别
            String wlevel = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i, 1).getStringCellValue();
            String dimensionId = "";
            if (wlevel.equals("0")) {
                // 判断是否是三角式量表，如果是那么就添加一个W0纬度进去，否则添加纬度W00
                // if (ScaleUtils.isThirdAngleScale(scaleId)){
                if (ScaleUtils.isMentalHealthScale(scaleId)) {// modified by
                                                              // zhaowanfeng
                    dimensionId = "W0";
                    Dimension dim = new Dimension();
                    dim.setId(dimensionId);
                    dim.setTitle("总量表");
                    dimensionMap.put(dimensionId, dim);
                } else {
                    // 如果Scale中没有维度，那么就把维度标注为无意义，并添加进去,否则，说明已经有总分在map中
                    if (!hasTotalScore) {
                        dimensionId = "W00";
                        Dimension dim = new Dimension();
                        dim.setId(dimensionId);
                        dim.setTitle("总量表");
                        dimensionMap.put(dimensionId, dim);
                    } else {
                        dimensionId = "W0";
                    }
                }
            } else {
                Iterator<String> it = dimensionMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    Dimension dimension = dimensionMap.get(key);
                    if (dimension.getTitle().trim().equals(wdtitile)) {
                        dimensionId = key;
                        break;
                    }
                }
            }
            Row row = expSheet.getRow(i);
            int columnNum = row.getLastCellNum();
            for (int j = 3; j < columnNum; j++) {
                ScaleExplainObject obj = new ScaleExplainObject();
                String firstStr = "";
                try {
                    firstStr = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i, j).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // if(firstStr.equalsIgnoreCase("")){
                // continue;//赵万锋注释2016/5/21，此处如果continue，那么当第一句为空时，就读不到"其它"解释。
                // }
                String otherStr = "";
                try {
                    otherStr = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i + 1, j).getStringCellValue();
                } catch (Exception e) {

                }
                if (firstStr.equalsIgnoreCase("") && otherStr.equalsIgnoreCase(""))
                    break;
                int dgrade = 0;
                if (j == 3) {
                    dgrade = 5;
                } else if (j == 4) {
                    dgrade = 4;
                } else if (j == 5) {
                    dgrade = 3;
                } else if (j == 6) {
                    dgrade = 2;
                } else if (j == 7) {
                    dgrade = 1;
                }
                String advice = "";
                // 下面是获取advice
                if (stuInstrSheet != null) {
                    if (wlevel.equalsIgnoreCase("1") || wlevel.equalsIgnoreCase("0")) {
                        int instrnum = stuInstrSheet.getLastRowNum();
                        for (int m = 4; m <= instrnum; m++) {
                            String title = "";
                            try {
                                title = WorkbookUtils.get07Cell((XSSFSheet) stuInstrSheet, m, 0).getStringCellValue();
                            } catch (Exception e) {
                                break;
                            }
                            if (title.equalsIgnoreCase("")) {
                                break;
                            }
                            if (title.trim().equalsIgnoreCase(wdtitile.trim())) {
                                advice = WorkbookUtils.get07Cell((XSSFSheet) stuInstrSheet, m, j - 1)
                                        .getStringCellValue();
                                break;
                            }
                        }
                    }
                }
                obj.setAdvice(advice);
                obj.setDgrade(dgrade);
                obj.setDimensionId(dimensionId);
                obj.setFirstStr(firstStr);
                obj.setOtherStr(otherStr);
                obj.setWlevel(Integer.parseInt(wlevel));
                obj.setTypeFlag(Integer.parseInt(flag));
                explainList.add(obj);
            }
            i = i + 2;
        }
    }

    public void parseMBTI(Sheet expSheet, Sheet stuInstrSheet, List<ScaleExplainObject> explainList,
            Map<String, Dimension> dimensionMap, String flag, boolean hasTotalScore, String scaleId) {
        // 采用新的对象格式来进行结果解释的插入
        int rownum = expSheet.getLastRowNum();
        int i = 4;
        while (i <= rownum) {
            // 维度名称
            String wdtitile = "";
            try {
                wdtitile = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i, 0).getStringCellValue();
            } catch (Exception e) {
                return;
            }
            if (wdtitile.equals("")) {
                i++;
                continue;
            }
            // 维度级别
            String wlevel = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i, 1).getStringCellValue();
            String dimensionId = wdtitile;

            Row row = expSheet.getRow(i);
            int columnNum = row.getLastCellNum();
            for (int j = 3; j < columnNum; j++) {
                ScaleExplainObject obj = new ScaleExplainObject();
                String firstStr = "";
                try {
                    firstStr = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i, j).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (firstStr.equalsIgnoreCase("")) {
                    // continue;//赵万锋注释2016/5/21，此处如果continue，那么当第一句为空时，就读不到"其它"解释。
                }
                String otherStr = "";
                try {
                    otherStr = WorkbookUtils.get07Cell((XSSFSheet) expSheet, i + 1, j).getStringCellValue();
                } catch (Exception e) {

                }
                int dgrade = 0;

                String advice = "";
                // 下面是获取advice
                if (stuInstrSheet != null) {
                    if (wlevel.equalsIgnoreCase("1") || wlevel.equalsIgnoreCase("0")) {
                        int instrnum = stuInstrSheet.getLastRowNum();
                        for (int m = 4; m <= instrnum; m++) {
                            String title = "";
                            try {
                                title = WorkbookUtils.get07Cell((XSSFSheet) stuInstrSheet, m, 0).getStringCellValue();
                            } catch (Exception e) {
                                break;
                            }
                            if (title.equalsIgnoreCase("")) {
                                break;
                            }
                            if (title.trim().equalsIgnoreCase(wdtitile.trim())) {
                                advice = WorkbookUtils.get07Cell((XSSFSheet) stuInstrSheet, m, j - 1)
                                        .getStringCellValue();
                                break;
                            }
                        }
                    }
                }
                obj.setAdvice(advice);
                obj.setDgrade(dgrade);
                obj.setDimensionId(dimensionId);
                obj.setFirstStr(firstStr);
                obj.setOtherStr(otherStr);
                obj.setWlevel(Integer.parseInt(wlevel));
                obj.setTypeFlag(Integer.parseInt(flag));
                explainList.add(obj);
            }
            i = i + 2;
        }
    }
}
