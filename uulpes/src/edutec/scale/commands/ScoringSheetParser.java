package edutec.scale.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import edutec.scale.model.Dimension;
import edutec.scale.model.Formula;
import edutec.scale.util.ScaleUtils;
import heracles.excel.WorkbookUtils;
import heracles.util.UtilCollection;

public class ScoringSheetParser extends SheetParser {
    private String scaleCode;
    private HashMap<String, String> dimensionTitleMap;
    private Map propsMap;
    private Map indexMap;
    private String startCell;
    private List reverseQuestons;

    public ScoringSheetParser() {
        this.startCell = "3_1";
        if (null == propsMap)
            propsMap = new HashMap<String, String>();
        this.propsMap.put("zeroLevelTitle", "总分");
        this.propsMap.put("firstLevelTitle", "一级");
        this.propsMap.put("secondLevelTitle", "二级");
        this.propsMap.put("questionsTitle", "题目包含");
        this.propsMap.put("reverseTitle", "反向题目");
        this.propsMap.put("originalScoreTitle", "原始分（脚本待定）");
        this.propsMap.put("ZScoreTitle", "Z分数（脚本待定）");
        this.propsMap.put("TScoreTitle", "T分数");
        this.propsMap.put("descTitle", "说明");
    }

    public void parse(Sheet sheet, Object obj) {
        // 将dimension的内容重新组织成title为key，id为value的map供查询
        if (obj instanceof Map)
            generateDimensionIdMap((Map) obj);
        String[] rc = startCell.split("_");
        int startRow = Integer.parseInt(rc[0]);
        int startCol = Integer.parseInt(rc[1]);
        int usedRowCount = sheet.getLastRowNum();
        int level = -1;
        // 轮循所有行，读取标题行和维度内容
        for (int i = startRow - 1; i <= usedRowCount; i++) {
            Cell firstCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i, startCol - 1);
            if (null != firstCell) {
                String firstCellStr = firstCell.getStringCellValue();
                firstCellStr = firstCellStr.trim();
                // 如果是标题行，则记录列和标题的对应关系
                if (isHeadRow(sheet, i)) {
                    level = getDimensionLevel(sheet, i);
                    recordColIndex(sheet, i);
                    continue;
                }
                // 如果第一列为空或者不是维度的内容
                if (StringUtils.isEmpty(firstCellStr)
                        || !dimensionTitleMap.containsKey(firstCellStr.trim()) && !"*".equals(firstCellStr)) {
                    continue;
                }
                // 当前行的列数
                int usedColCount = sheet.getRow(i).getPhysicalNumberOfCells();
                // 读取维度内容
                if (1 == level) {
                    // 一级维度标题
                    String dimTitle = indexMap.containsKey("firstLevelTitle")
                            ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("firstLevelTitle"))
                                    .getStringCellValue()
                            : null;
                    // 有可能存在一道题的情况，这时候需要特殊处理
                    String questionsstr = null;
                    // 设置了一下celltype，有做了特殊处理
                    if (indexMap.containsKey("questionsTitle")) {
                        WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("questionsTitle"))
                                .setCellType(HSSFCell.CELL_TYPE_STRING);
                        questionsstr = WorkbookUtils
                                .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("questionsTitle"))
                                .getStringCellValue();
                    }
                    // 如果有包含题目，说明只有一级维度，那么写入包含题目
                    String questionIds = questionsstr;
                    if (StringUtils.isNotEmpty(questionIds)) {
                        questionIds = "Q" + questionIds;
                        String splitCharacter = UtilCollection.splitFlag(questionIds);
                        /*
                         * if(questionIds.contains(String.valueOf(UtilCollection
                         * .COMMA))) splitCharacter=UtilCollection.COMMA; else
                         * if(questionIds.contains(String.valueOf(UtilCollection
                         * .COMMACHINA)))
                         * splitCharacter=UtilCollection.COMMACHINA; else
                         * if(questionIds.contains(String.valueOf('、')))
                         * splitCharacter='、';
                         */
                        questionIds = questionIds.replace(splitCharacter, ",Q");
                    }
                    // 记录反向计分题目，设置到questions里,如果只有一个题，那么会报错，所以冗余处理
                    String reverseQus = "";
                    try {
                        reverseQus = indexMap.containsKey("reverseTitle")
                                ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("reverseTitle"))
                                        .getStringCellValue()
                                : null;
                    } catch (Exception e) {
                        reverseQus = String.valueOf(indexMap.containsKey("reverseTitle")
                                ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("reverseTitle"))
                                : null);
                        if (reverseQus.equals("null")) {
                            reverseQus = "";
                        } else {
                            reverseQus = reverseQus.substring(0, reverseQus.indexOf("."));
                        }

                    }

                    // 记录原始分计分公式
                    String originScoreFomula = indexMap.containsKey("originalScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("originalScoreTitle"))
                            .getStringCellValue() : null;
                    // 记录Z分数公式
                    String ZFormula = indexMap.containsKey("ZScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("ZScoreTitle")).getStringCellValue()
                            : null;
                    // 记录T分数公式
                    String TFormula = indexMap.containsKey("TScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("TScoreTitle")).getStringCellValue()
                            : null;
                    String instruction = indexMap.containsKey("descTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("descTitle")).getStringCellValue()
                            : null;

                    // 找到dimension在list中的位置
                    String index = dimensionTitleMap.get(dimTitle);
                    // 设置公式
                    Dimension dim = (Dimension) ((Map) obj).get(index);
                    Formula formula = null;

                    // 先写入原始分的原始内容，如果可能出现sum;arg格式，还需要用分号切割开，分别加入
                    // 要修改模板，尽量保持与xml一致
                    if (StringUtils.isNotEmpty(originScoreFomula) || StringUtils.isNotEmpty(ZFormula)
                            || StringUtils.isNotEmpty(TFormula)) {
                        formula = new Formula();
                        if ("sum;".equals(originScoreFomula.toLowerCase().trim())) {
                            formula.setRawexp("sum");
                        } else if ("ave;".equals(originScoreFomula.toLowerCase().trim())) {
                            formula.setRawexp("arg");
                        } else {
                            formula.setRawexp(originScoreFomula.toLowerCase().trim());
                        }
                        formula.setStdexp(ZFormula);
                        formula.setTexp(TFormula);
                        if (StringUtils.isNotEmpty(instruction)) {
                            if (instruction.toLowerCase().trim().contains("r")
                                    || instruction.toLowerCase().trim().contains("num")) {
                                formula.setNvalidexp(instruction);
                            }
                        }
                        dim.setFormula(formula);
                    }

                    if (StringUtils.isNotEmpty(questionIds))
                        dim.setQuestionIds(questionIds);
                    // 如果同一反向题出现在不同维度中，则每个维度的反向题都记录下来
                    if (ScaleUtils.isSameReverseInMultiDimScale(scaleCode)) {
                        String newReverseQus = "";
                        if (StringUtils.isNotEmpty(reverseQus)) {
                            newReverseQus = "Q" + reverseQus;
                            String splitCharacter = UtilCollection.splitFlag(newReverseQus);
                            newReverseQus = newReverseQus.replace(splitCharacter, ",Q");
                        }
                        dim.setReverseQuestionIds(newReverseQus);
                    }

                    // 设置反向题目
                    if (StringUtils.isNotEmpty(reverseQus)) {
                        String charactor = UtilCollection.splitFlag(reverseQus);
                        String[] revsIndex = reverseQus.split(charactor);
                        for (String revsi : revsIndex) {
                            if (StringUtils.isNotEmpty(revsi)) {
                                if (null == reverseQuestons)
                                    reverseQuestons = new ArrayList();
                                reverseQuestons.add(revsi);
                            }
                        }
                    }

                } else if (0 == level) {
                    String questionsstr = null;
                    if (indexMap.containsKey("questionsTitle")) {
                        WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("questionsTitle"))
                                .setCellType(HSSFCell.CELL_TYPE_STRING);
                        questionsstr = WorkbookUtils
                                .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("questionsTitle"))
                                .getStringCellValue();
                    }
                    // 如果有包含题目，说明只有一级维度，那么写入包含题目
                    String questionIds = questionsstr;
                    if (StringUtils.isNotEmpty(questionIds)) {
                        questionIds = "Q" + questionIds;
                        String splitCharacter = UtilCollection.splitFlag(questionIds);
                        questionIds = questionIds.replace(splitCharacter, ",Q");
                    }
                    // 记录反向计分题目，设置到questions里,如果只有一个题，那么会报错，所以冗余处理
                    String reverseQus = "";
                    try {
                        reverseQus = indexMap.containsKey("reverseTitle")
                                ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("reverseTitle"))
                                        .getStringCellValue()
                                : null;
                    } catch (Exception e) {
                        reverseQus = String.valueOf(indexMap.containsKey("reverseTitle")
                                ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("reverseTitle"))
                                        .getStringCellValue()
                                : null);
                        reverseQus = reverseQus.substring(0, reverseQus.indexOf("."));
                    }
                    // 记录原始分计分公式
                    String originScoreFomula = indexMap.containsKey("originalScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("originalScoreTitle"))
                            .getStringCellValue() : null;
                    // 记录Z分数公式
                    String ZFormula = indexMap.containsKey("ZScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("ZScoreTitle")).getStringCellValue()
                            : null;
                    // 记录T分数公式
                    String TFormula = indexMap.containsKey("TScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("TScoreTitle")).getStringCellValue()
                            : null;
                    String instruction = indexMap.containsKey("descTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("descTitle")).getStringCellValue()
                            : null;

                    Dimension dim = new Dimension();
                    // 因为维度为0；表示的是总分维度，如果解析中没有，则加入到维度map中去
                    if (dimensionTitleMap.containsKey("总分")) {
                        // 找到dimension在list中的位置
                        String index = dimensionTitleMap.get("总分");
                        // 设置公式
                        dim = (Dimension) ((Map) obj).get(index);
                    } else {
                        dim.setId("W0");
                        dim.setTitle("总分");
                        ((Map) obj).put("W0", dim);
                        this.dimensionTitleMap.put("总分", "W0");
                    }
                    Formula formula = null;
                    // 先写入原始分的原始内容，如果可能出现sum;arg格式，还需要用分号切割开，分别加入
                    // 要修改模板，尽量保持与xml一致
                    if (StringUtils.isNotEmpty(originScoreFomula) || StringUtils.isNotEmpty(ZFormula)
                            || StringUtils.isNotEmpty(TFormula)) {
                        formula = new Formula();
                        if ("sum;".equals(originScoreFomula.toLowerCase())) {
                            formula.setRawexp("sum");
                        } else if ("ave;".equals(originScoreFomula.toLowerCase())) {
                            formula.setRawexp("arg");
                        } else {
                            formula.setRawexp(originScoreFomula.toLowerCase());
                        }
                        formula.setStdexp(ZFormula);
                        formula.setTexp(TFormula);
                        if (StringUtils.isNotEmpty(instruction)) {
                            if (instruction.toLowerCase().trim().contains("r")
                                    || instruction.toLowerCase().trim().contains("num")) {
                                formula.setNvalidexp(instruction);
                            }
                        }
                        dim.setFormula(formula);
                    }
                    if (StringUtils.isEmpty(questionIds)) {
                        dim.setQuestionIds("*");
                    } else {
                        dim.setQuestionIds(questionIds);
                    }
                    // 设置反向题目
                    if (StringUtils.isNotEmpty(reverseQus)) {
                        String charactor = UtilCollection.splitFlag(reverseQus);
                        String[] revsIndex = reverseQus.split(charactor);
                        for (String revsi : revsIndex) {
                            if (StringUtils.isNotEmpty(revsi)) {
                                if (null == reverseQuestons)
                                    reverseQuestons = new ArrayList();
                                reverseQuestons.add(revsi);
                            }
                        }
                    }
                } else if (2 == level) {
                    // 一级维度标题
                    String dimTitle = indexMap.containsKey("secondLevelTitle")
                            ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("secondLevelTitle"))
                                    .getStringCellValue()
                            : null;
                    // 下面开始处理万一二级维度为空的情况
                    if (dimTitle.equalsIgnoreCase("")) {
                        dimTitle = WorkbookUtils
                                .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("firstLevelTitle"))
                                .getStringCellValue();
                    }
                    // 如果有包含题目，说明只有一级维度，那么写入包含题目
                    String questionIds = indexMap.containsKey("questionsTitle")
                            ? WorkbookUtils.get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("questionsTitle"))
                                    .getStringCellValue()
                            : null;
                    // 记录反向计分题目，设置到questions里
                    Cell reverseQusCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i,
                            (Integer) indexMap.get("reverseTitle"));
                    String reverseQus = null;
                    if (reverseQusCell != null)
                        reverseQus = indexMap.containsKey("reverseTitle") ? reverseQusCell.getStringCellValue() : null;
                    // 记录原始分计分公式
                    String originScoreFomula = indexMap.containsKey("originalScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("originalScoreTitle"))
                            .getStringCellValue() : null;
                    // 记录Z分数公式
                    String ZFormula = indexMap.containsKey("ZScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("ZScoreTitle")).getStringCellValue()
                            : null;
                    // 记录T分数公式
                    String TFormula = indexMap.containsKey("TScoreTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("TScoreTitle")).getStringCellValue()
                            : null;
                    String instruction = indexMap.containsKey("descTitle") ? WorkbookUtils
                            .get07Cell((XSSFSheet) sheet, i, (Integer) indexMap.get("descTitle")).getStringCellValue()
                            : null;

                    // 写入公式

                    // 找到dimension在list中的位置
                    String index = dimensionTitleMap.get(dimTitle);
                    // 设置公式
                    Dimension dim = (Dimension) ((Map) obj).get(index);
                    Formula formula = null;

                    // 先写入原始分的原始内容，如果可能出现sum;arg格式，还需要用分号切割开，分别加入
                    // 要修改模板，尽量保持与xml一致
                    if (StringUtils.isNotEmpty(originScoreFomula) || StringUtils.isNotEmpty(ZFormula)
                            || StringUtils.isNotEmpty(TFormula)) {
                        formula = new Formula();
                        if ("sum;".equals(originScoreFomula.toLowerCase())) {
                            formula.setRawexp("sum");
                        } else if ("ave;".equals(originScoreFomula.toLowerCase())) {
                            formula.setRawexp("arg");
                        } else {
                            formula.setRawexp(originScoreFomula.toLowerCase());
                        }
                        formula.setStdexp(ZFormula);
                        formula.setTexp(TFormula);
                        if (StringUtils.isNotEmpty(instruction)) {
                            if (instruction.toLowerCase().trim().contains("r")
                                    || instruction.toLowerCase().trim().contains("num")) {
                                formula.setNvalidexp(instruction);
                            }
                        }
                        dim.setFormula(formula);
                    }

                    if (StringUtils.isNotEmpty(questionIds)) {
                        questionIds = "Q" + questionIds;
                        String splitCharacter = UtilCollection.splitFlag(questionIds);
                        questionIds = questionIds.replace(splitCharacter, ",Q");
                        dim.setQuestionIds(questionIds);
                    }
                    // 设置反向题目
                    if (StringUtils.isNotEmpty(reverseQus)) {
                        String charactor = UtilCollection.splitFlag(reverseQus);
                        String[] revsIndex = reverseQus.split(charactor);
                        for (String revsi : revsIndex) {
                            if (StringUtils.isNotEmpty(revsi)) {
                                if (null == reverseQuestons)
                                    reverseQuestons = new ArrayList();
                                reverseQuestons.add(revsi);
                            }
                        }
                    }
                }
            }
        }
    }

    public List getReverseQuestions() {
        return reverseQuestons;
    }

    /**
     * 生成title为key，List<Dimension>序号为value的map
     * 
     * @param dimensionMap
     */
    private void generateDimensionIdMap(List<Dimension> dimensions) {
        for (int i = 0; i < dimensions.size(); i++) {
            Dimension dim = dimensions.get(i);
            String title = dim.getTitle();
            if (StringUtils.isNotEmpty(title))
                this.dimensionTitleMap.put(title, "W" + i);
        }
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
     * 判断当前行是不是标题行
     * 
     * @param sheet
     *            当前sheet
     * @param row
     *            当前行
     * @return true false
     */
    private boolean isHeadRow(Sheet sheet, int row) {
        return this.propsMap.get("firstLevelTitle")
                .equals(WorkbookUtils.get07Cell((XSSFSheet) sheet, row, 0).getStringCellValue())
                || this.propsMap.get("zeroLevelTitle")
                        .equals(WorkbookUtils.get07Cell((XSSFSheet) sheet, row, 0).getStringCellValue())
                || this.propsMap.get("secondLevelTitle")
                        .equals(WorkbookUtils.get07Cell((XSSFSheet) sheet, row, 0).getStringCellValue());
    }

    /**
     * 判断当前行是否包含字符串str
     * 
     * @param row
     *            当前行
     * @param str
     *            字符串
     * @return true false
     */
    private boolean containStr(Row row, String str) {
        Iterator cells = row.cellIterator();
        while (cells.hasNext()) {
            Cell cell = (Cell) cells.next();
            if (str.equals(cell.getStringCellValue()))
                return true;
        }
        return false;
    }

    /**
     * 判断当前行下面的内容维度的级别
     * 
     * @param sheet
     *            当前sheet
     * @param row
     *            当前行
     * @return 维度的级别
     */
    private int getDimensionLevel(Sheet sheet, int row) {
        if (isHeadRow(sheet, row)) {
            if (containStr(sheet.getRow(row), (String) this.propsMap.get("secondLevelTitle")))
                return 2;
            if (containStr(sheet.getRow(row), (String) this.propsMap.get("firstLevelTitle"))) {
                return 1;
            }
            if (containStr(sheet.getRow(row), (String) this.propsMap.get("zeroLevelTitle")))
                return 0;
        }
        return -1;
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
            Cell strcell = row.getCell(i);
            if (strcell == null) {
                return;
            }
            String str = strcell.getStringCellValue();
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

    public void setScaleCode(String scaleCode) {
        this.scaleCode = scaleCode;
    }

}