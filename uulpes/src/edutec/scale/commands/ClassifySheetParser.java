package edutec.scale.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import edutec.scale.model.Dimension;
import edutec.scale.model.ScoreGrade;
import edutec.scale.model.ScoreMethod;
import heracles.excel.WorkbookUtils;

public class ClassifySheetParser {
    private HashMap<String, String> dimensionTitleMap;
    private Map propsMap;
    private String startCell;
    private String ZStr;
    private String scoreGradeStr;
    private String headTitle;
    private HashMap<String, Integer> indexMap;
    private String[] stopComma; // 保存应该去掉的()[]符号
    private String split = "，"; // 范围值用逗号分割

    public ClassifySheetParser() {
        this.startCell = "3_1";
        scoreGradeStr = "得分水平";
        ZStr = "z分数";
        headTitle = "维度";
        stopComma = new String[] { "(", ")", "[", "]", "（", "）" };
        if (null == propsMap)
            propsMap = new HashMap<String, String>();
        this.propsMap.put("dimensionTitle", "维度");
        this.propsMap.put("dimensionLevel", "维度级别");
        this.propsMap.put("classFive", "五");
        this.propsMap.put("classFour", "四");
        this.propsMap.put("classThree", "三");
        this.propsMap.put("classTwo", "二");
        this.propsMap.put("classOne", "一");

    }

    public void parse(Sheet sheet, Map<String, Dimension> dimMap, List<ScoreGrade> scoreGrades) {
        // 将维度解析为标题为key的map
        generateDimensionIdMap(dimMap);
        String[] rc = startCell.split("_");
        int startRow = Integer.parseInt(rc[0]);
        int startCol = Integer.parseInt(rc[1]);
        int usedRowCount = sheet.getPhysicalNumberOfRows();
        int level = -1;
        int scoreMethod = -1; // 计分方法：1是z分数，2是原始分
        // 轮循所有行，读取标题行和维度内容
        for (int i = startRow - 1; i <= usedRowCount; i++) {
            Cell firstCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, 0);
            if (null != firstCell) {
                String firstColStr = firstCell.getStringCellValue();
                // 如果是标题行，则记录列和标题的对应关系
                level = getRowLevel(sheet, i);
                if (level > 0) {
                    // 如果是标题列行，记录列名和列号的对应关系
                    if (level == 2)
                        recordColIndex(sheet, i);
                    if (level == 1) {
                        // 如果第三个格的内容包含"z分数"表示计算方法是用z值，否则用原始分
                        if (WorkbookUtils.get07Cell((XSSFSheet) sheet, i, 2).getStringCellValue().indexOf(ZStr) > -1) {
                            scoreMethod = ScoreMethod.ZVALUE;
                        } else {
                            scoreMethod = ScoreMethod.ORIGIN;
                        }
                    }
                    continue;
                }
                // 如果第一列为空或者不是维度的内容
                if (StringUtils.isEmpty(firstColStr) || !dimensionTitleMap.containsKey(firstColStr)) {
                    continue;
                }

                // 获得对应dimenstion的title
                Cell dimTitleCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("dimensionTitle"));
                String dimTitle = dimTitleCell.getStringCellValue();

                // 获得对应的dimension的Id
                String dimId = dimensionTitleMap.get(dimTitle);
                Dimension dim = dimMap.get(dimId);

                // 获得dimension的level
                int dimlevel = -1;
                Cell dimLevelCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("dimensionLevel"));
                if (null != dimLevelCell)
                    dimlevel = Integer.parseInt(dimLevelCell.getStringCellValue());

                // 获得score1，score12，score13
                String score1 = null, score2 = null, score3 = null, score4 = null;
                Cell fiveCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("classFive"));
                Cell fourCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("classFour"));
                Cell threeCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("classThree"));
                Cell twoCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("classTwo"));
                Cell oneCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, indexMap.get("classOne"));
                if (null != fiveCell && null != fourCell && null != threeCell && null != twoCell && null != oneCell) {
                    String[] values5 = composeValueCell(fiveCell.getStringCellValue());
                    String[] values3 = composeValueCell(threeCell.getStringCellValue());
                    String[] values1 = composeValueCell(oneCell.getStringCellValue());
                    // 只有两边的会有正负无穷，所以先读出score1和score4
                    if ("-∞".equals(values5[0])) {
                        score4 = values5[1];
                    } else if ("∞".equals(values5[1])) {
                        score4 = values5[0];
                    }
                    if ("-∞".equals(values1[0])) {
                        score1 = values1[1];
                    } else if ("∞".equals(values1[1])) {
                        score1 = values1[0];
                    }
                    // 把value3的内容赋给score2和score3
                    if (Float.parseFloat(score4) > Float.parseFloat(score1)) {
                        score2 = values3[0];
                        score3 = values3[1];
                    } else {
                        score3 = values3[0];
                        score2 = values3[1];
                    }
                    ScoreGrade scoreGrade = new ScoreGrade();
                    scoreGrade.setWid(dimId);
                    scoreGrade.setWlevel(dimlevel);
                    scoreGrade.setScoreMethod(scoreMethod);
                    scoreGrade.setScore1(score1);
                    scoreGrade.setScore2(score2);
                    scoreGrade.setScore3(score3);
                    scoreGrade.setScore4(score4);
                    scoreGrades.add(scoreGrade);
                }
            }
        }
    }

    /**
     * 判断当前行是不是标题行
     * 
     * @param sheet
     *            当前sheet
     * @param row
     *            当前行
     * @return true false
     */
    private int getRowLevel(Sheet sheet, int row) {
        Cell firstCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, row, 0);
        Cell thirdCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, row, 2);
        if (null != firstCell && null != thirdCell) {
            String firstStr = firstCell.getStringCellValue();
            String thirdStr = thirdCell.getStringCellValue();
            if (headTitle.equals(firstStr)) {
                // 如果第三列包含“得分水平”，则表示是第一行标题
                if (thirdStr.indexOf(scoreGradeStr) > -1) {
                    return 1;
                }
                return 2;
            }
        }

        return -1;
    }

    private void generateDimensionIdMap(Map<String, Dimension> dimensions) {
        Iterator<String> keyIt = dimensions.keySet().iterator();
        if (null == dimensionTitleMap)
            dimensionTitleMap = new HashMap<String, String>();
        while (keyIt.hasNext()) {
            String id = keyIt.next();
            String title = dimensions.get(id).getTitle();
            dimensionTitleMap.put(title, id);
        }
    }

    /**
     * 记录标题行每列标题和列数之间的map，例如："firstLevelTitle"：1
     * 
     * @param sheet
     *            当前sheet
     * @param headRow
     *            标题行
     */
    private void recordColIndex(Sheet sheet, int headRow) {
        Row row = sheet.getRow(headRow);
        this.indexMap = new HashMap<String, Integer>();
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            Iterator<String> props = this.propsMap.keySet().iterator();
            String str = row.getCell(i).getStringCellValue();
            while (props.hasNext()) {
                String prop = props.next();
                String value = (String) propsMap.get(prop);
                if (value.equals(str)) {
                    this.indexMap.put(prop, i);
                    break;
                }
            }
        }
    }

    /**
     * 处理单元格里的范围值，删掉()[]符号，用，分割
     * 
     * @param content
     * @return
     */
    private String[] composeValueCell(String content) {
        for (String comma : stopComma) {
            content = content.replace(comma, "");
        }
        String[] result = content.split(this.split);
        if (result.length == 1) {
            result = content.split(",");
        }
        if (result.length == 1) {
            result = content.split("、");
        }
        return result;
    }
}