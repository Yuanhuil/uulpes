package edutec.scale.commands;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edutec.scale.model.Dimension;
import edutec.scale.model.Option;
import edutec.scale.model.PreWarn;
import edutec.scale.model.Question;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.model.ScaleHidenPage;
import edutec.scale.model.ScaleHomePage;
import edutec.scale.model.ScaleModuleTitleEnum;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.model.ScoreGrade;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.util.ScaleUtils;
import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

public class ScaleXlsReader {
    final int START_ROW = 3;

    private String id; // 如果量表是下载的模板，那么在隐藏页记录量表的id值

    private String picPath; // 如果量表是包含图片的，把解压到服务器的地址传过来
    private String title;
    private String abbr; // 英文简称
    private String synopsis;
    private String instruction;
    private String endWord; // 结束语
    private String testType; // 自评类型
    private String source; // 量表来源
    private String scaleType; // 量表类型
    private String applicablePerson; // 适用人群
    private long examineTime; // 测试时间

    private String reportGraph; // 图表//结束语
    private List<Question> questions; // 题本里所有的问题，后加的，因为新的模板题本比较复杂
    private Map<String, Dimension> dimensionMap; // 模板里的维度信息
    private boolean hasTotalScore; // 模板是否包含总分

    private List<ScoreGrade> scoreGrades; // 保存得分水平
    private List<PreWarn> preWarns; // 保存预警级别
    private String indvQNums;
    private int indvQNum;
    private int qnum;
    private int dimNum;
    private boolean isErr;
    private List<String[]> indvQList;
    private List<String[]> qusList;
    private List<String[]> dimList;
    private List<ScaleExplainObject> explainObjList = new ArrayList<ScaleExplainObject>();;
    private List<ScaleNormObject> normObjList = new ArrayList<ScaleNormObject>();

    private String normGradeOrAgeFlag = "grade";
    private String reportImageParam1;
    private String reportImageParam2;
    private boolean isRead = false;

    public long getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(long examineTime) {
        this.examineTime = examineTime;
    }

    public String getApplicablePerson() {
        return applicablePerson;
    }

    public void setApplicablePerson(String applicablePerson) {
        this.applicablePerson = applicablePerson;
    }

    public boolean isHasTotalScore() {
        return hasTotalScore;
    }

    public void setHasTotalScore(boolean hasTotalScore) {
        this.hasTotalScore = hasTotalScore;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getId() {
        return id;
    }

    public String getNormGradeOrAgeFlag() {
        return normGradeOrAgeFlag;
    }

    public void setNormGradeOrAgeFlag(String normGradeOrAgeFlag) {
        this.normGradeOrAgeFlag = normGradeOrAgeFlag;
    }

    /*
     * 下面主要用来读取2003版本的excel huang
     */
    public HSSFWorkbook read2003Excel(InputStream stream, StringBuilder errSb) throws Exception {
        HSSFWorkbook wb = null;
        try {
            wb = WorkbookUtils.openWorkbook(stream);

            // 解析首页
            Sheet curSheet = wb.getSheet(ScaleModuleTitleEnum.HOMEPAGE.getTitle());
            readHomePage(curSheet);

            // 解析题本
            curSheet = wb.getSheet(ScaleModuleTitleEnum.QUESTION.getTitle());
            readQuestions(curSheet);

            // 解析分数
            curSheet = wb.getSheet(ScaleModuleTitleEnum.SCORE.getTitle());
            readScore(curSheet);

            // 解析维度信息
            curSheet = wb.getSheet(ScaleModuleTitleEnum.DIMENSION.getTitle());
            readDimension(curSheet);

            // 解析计分信息，包括一部分维度和题目的对应关系，和反向计分题信息，还有计分公式，保存在维度里
            curSheet = wb.getSheet(ScaleModuleTitleEnum.SCORING.getTitle());
            readScoring(curSheet);

            // 解析图表参数
            curSheet = wb.getSheet(ScaleModuleTitleEnum.IMAGE_PARAM.getTitle());
            readReportImageParam(curSheet);

            isRead = true;
        } catch (ExcelException e) {
            errSb.append(e.getMessage());
        }
        return wb;
    }

    /**
     * 读取2007Excel文件内容
     * 
     * @param stream
     * @param errSb
     */
    public XSSFWorkbook read2007Excel(InputStream stream, StringBuilder errSb, String filePath) throws Exception {
        XSSFWorkbook wb = null;
        picPath = filePath;
        try {
            wb = new XSSFWorkbook(stream);
            // 解析首页
            Sheet curSheet = wb.getSheet(ScaleModuleTitleEnum.HOMEPAGE.getTitle());
            readHomePage(curSheet);

            // 解析隐藏页
            curSheet = wb.getSheet(ScaleModuleTitleEnum.HIDDEN.getTitle());
            readConfiguration(curSheet);

            // 解析题本
            curSheet = wb.getSheet(ScaleModuleTitleEnum.QUESTION.getTitle());
            readQuestions(curSheet);

            // 解析分数
            curSheet = wb.getSheet(ScaleModuleTitleEnum.SCORE.getTitle());
            readScore(curSheet);

            // 解析维度信息
            curSheet = wb.getSheet(ScaleModuleTitleEnum.DIMENSION.getTitle());
            readDimension(curSheet);

            // 解析计分信息，包括一部分维度和题目的对应关系，和反向计分题信息，还有计分公式，保存在维度里
            curSheet = wb.getSheet(ScaleModuleTitleEnum.SCORING.getTitle());
            readScoring(curSheet);
            // 常模
            curSheet = wb.getSheet(ScaleModuleTitleEnum.NORM.getTitle());
            readNorm(curSheet);

            // 解析得分水平划分信息
            curSheet = wb.getSheet(ScaleModuleTitleEnum.CLASSIFY.getTitle());
            readClassify(curSheet);

            // 解析结果解释和指导建议并缓存起来_学生
            curSheet = wb.getSheet(ScaleModuleTitleEnum.EXPL.getTitle());
            Sheet stuInstrSheet = wb.getSheet(ScaleModuleTitleEnum.INSTR.getTitle());
            if (curSheet != null)
                readExpl_stu(curSheet, stuInstrSheet, explainObjList, ScaleModuleTitleEnum.EXPL_INSTR.getTitle(),
                        hasTotalScore);
            // //解析结果解释和指导建议并缓存起来_学生
            curSheet = wb.getSheet(ScaleModuleTitleEnum.EXPL_STU.getTitle());
            Sheet InstrSheet = wb.getSheet(ScaleModuleTitleEnum.INSTR_STU.getTitle());
            if (curSheet != null)
                readExpl_stu(curSheet, InstrSheet, explainObjList, ScaleModuleTitleEnum.EXPL_INSTR_STU.getTitle(),
                        hasTotalScore);
            // 解析结果解释和指导建议并缓存起来_老师
            curSheet = wb.getSheet(ScaleModuleTitleEnum.EXPL_TEAC.getTitle());
            Sheet teacherInstrSheet = wb.getSheet(ScaleModuleTitleEnum.INSTR_TEAC.getTitle());
            if (curSheet != null)
                readExpl_stu(curSheet, teacherInstrSheet, explainObjList,
                        ScaleModuleTitleEnum.EXPL_INSTR_TEAC.getTitle(), hasTotalScore);
            // 解析结果解释和指导建议并缓存起来_家长
            curSheet = wb.getSheet(ScaleModuleTitleEnum.EXPL_PAR.getTitle());
            Sheet parentInstrSheet = wb.getSheet(ScaleModuleTitleEnum.INSTR_PAR.getTitle());
            if (curSheet != null)
                readExpl_stu(curSheet, parentInstrSheet, explainObjList, ScaleModuleTitleEnum.EXPL_INSTR_PAR.getTitle(),
                        hasTotalScore);

            // 解析预警级别
            curSheet = wb.getSheet(ScaleModuleTitleEnum.PREWARN.getTitle());
            readPrewarn(curSheet);

            // 解析图表参数
            curSheet = wb.getSheet(ScaleModuleTitleEnum.IMAGE_PARAM.getTitle());
            readReportImageParam(curSheet);

            isRead = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            errSb.append(e.getMessage());
        }
        return wb;
    }

    public void readQuestion2003Excel(InputStream stream) throws Exception {
        HSSFWorkbook wb = null;
        try {
            wb = WorkbookUtils.openWorkbook(stream);
            // 解析首页
            Sheet curSheet = wb.getSheet(ScaleModuleTitleEnum.HOMEPAGE.getTitle());
            readHomePage(curSheet);
            // 解析隐藏页
            curSheet = wb.getSheet(ScaleModuleTitleEnum.HIDDEN.getTitle());
            readConfiguration(curSheet);
        } catch (ExcelException e) {
            e.printStackTrace();
        }
    }

    public void readQuestion2007Excel(InputStream stream) throws Exception {
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(stream);
            // 解析首页
            Sheet curSheet = wb.getSheet(ScaleModuleTitleEnum.HOMEPAGE.getTitle());
            readHomePage(curSheet);
            // 解析隐藏页
            curSheet = wb.getSheet(ScaleModuleTitleEnum.HIDDEN.getTitle());
            readConfiguration(curSheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * huangc 下面主要根据上面解析完成的对象来得到生成的最终的Scale对象
     */

    public Scale buildScale2() {
        Scale scale = new Scale();
        // 基础信息
        if (StringUtils.isNotEmpty(id))
            scale.setCode(id); // 量表的code是这里的id，例如110101
        scale.setShortname(abbr); // 量表的英文简称
        scale.setTitle(title); // 量表的题目
        scale.setDescn(synopsis); // 量表的简介
        scale.setGuidance(instruction); // 量表的指导语
        scale.setApplicablePerson(applicablePerson);
        scale.setTestType(testType);
        scale.setReportGraph(reportGraph);
        scale.setScaleType(scaleType);
        scale.setSource(source);
        scale.setQuestionNum(qnum); // 量表题目数量
        scale.setWarningOrNot(preWarns != null && !preWarns.isEmpty());
        scale.setReportImageParam1(reportImageParam1);
        scale.setReportImageParam2(reportImageParam2);
        List<String> list = new ArrayList<String>();
        int idx = 1;
        // 题目信息
        for (Question question : questions) {
            scale.addQuestion(question); // 把question赋给scale
        }
        // 维度信息
        Iterator<String> it = dimensionMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Dimension dimension = dimensionMap.get(key);
            scale.addDimension(dimension);
        }
        return scale;
    }

    /**
     * 解析Excel模板隐藏页配置信息
     * 
     * @param curSheet
     */
    private void readConfiguration(Sheet curSheet) {
        HiddenSheetParser hparser = new HiddenSheetParser();
        ScaleHidenPage hp = new ScaleHidenPage();
        try {
            hparser.parse(curSheet, hp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        id = hp.getId();
        testType = hp.getTestType();
        source = hp.getSource();
        scaleType = hp.getScaleType();
        applicablePerson = hp.getApplicablePerson();
        reportGraph = hp.getReportGraph();
        examineTime = hp.getExamineTime();
    }

    /**
     * 解析Excel模板首页内容
     * 
     * @param sheet
     *            首页sheet对象
     */
    private void readHomePage(Sheet sheet) {
        ScaleHomePage hp = new ScaleHomePage();
        HomePageSheetParser hpParser = new HomePageSheetParser();
        hpParser.parse(sheet, hp);
        title = hp.getName();
        /*
         * synopsis = hp.getIntro().replaceAll(" ", ""); instruction =
         * hp.getInstr().replaceAll(" ", ""); abbr =
         * hp.getAbbr().replaceAll(" ", ""); endWord =
         * hp.getEndWord().replaceAll(" ", "");
         */
        synopsis = hp.getIntro();
        instruction = hp.getInstr();
        abbr = hp.getAbbr();
        endWord = hp.getEndWord();
    }

    /**
     * 解析Excel模板题本内容
     * 
     * @param sheet
     *            题本sheet对象
     */
    private void readQuestions(Sheet sheet) throws Exception {
        QuestionsSheetParser qusParser = new QuestionsSheetParser();
        if (null == questions)
            questions = new ArrayList<Question>();
        try {
            qusParser.parse(sheet, questions, picPath);
        } catch (Exception e) {
            throw new Exception("读取题本有误，请检查量表模板。");
        }
        qnum = qusParser.getQnum();
    }

    /**
     * 解析Excel模板维度内容
     * 
     * @param curSheet
     *            题本sheet对象
     */
    private void readDimension(Sheet curSheet) {
        // TODO Auto-generated method stub
        DimensionSheetParser dimenParser = new DimensionSheetParser();
        if (null == dimensionMap)
            dimensionMap = new HashMap<String, Dimension>();
        dimenParser.parse(curSheet, dimensionMap);
        if (dimensionMap.containsKey("W0"))
            this.hasTotalScore = true;
    }

    /**
     * 解析Excel模板计分内容
     * 
     * @param curSheet
     *            题本sheet对象
     */
    private void readScoring(Sheet curSheet) {
        ScoringSheetParser scoringParser = new ScoringSheetParser();
        scoringParser.setScaleCode(id);
        if (null != questions && null != dimensionMap) {
            scoringParser.parse(curSheet, dimensionMap);
            // 除了维度外，计分也有可能添加了总分
            if (dimensionMap.containsKey("W0"))
                this.hasTotalScore = true;

            if (ScaleUtils.isSameReverseInMultiDimScale(id))// 如果同一反向题出现在不同维度中，则反向题需要根据每个维度单独计算，而不能把反向题设置为反向，因为这个维度是反向，到了另一个维度就不是反向了。
                return;
            List<String> reverse = scoringParser.getReverseQuestions();
            // 如果有反向积分的体型那么进行设置
            if (reverse != null && reverse.size() > 0) {
                for (String id : reverse) {
                    for (Question question : questions) {
                        String tempstr = "Q" + id.trim();
                        if (question.getId().equalsIgnoreCase(tempstr)) {
                            question.setReverse(true);
                            List<Option> options = ((SelectionQuestion) question).getOptions();

                            List<String> values = new ArrayList<String>();
                            for (int i = options.size() - 1; i >= 0; i--) {
                                if (options.get(i).getValue().contains("？"))// 如果选项是？，说明此题是无法作答。参见明尼苏达量表
                                    continue;
                                values.add(options.get(i).getValue());
                            }
                            for (int i = 0; i < options.size(); i++) {
                                if (options.get(i).getValue().contains("？"))
                                    continue;
                                ((SelectionQuestion) question).getOptions().get(i).setValue(values.get(i));
                                // options.get(i).setValue(values.get(values.size()-i-1));
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * 解析结果解释，并插入到数据库中去
     */
    public void readExpl_stu(Sheet expSheet, Sheet stuInstrSheet, List<ScaleExplainObject> explainObjList,
            String typeflag, boolean hasTotalScore) {
        ExplainSheetParser explSheetPaser = new ExplainSheetParser();
        explSheetPaser.parse(expSheet, stuInstrSheet, explainObjList, dimensionMap, typeflag, hasTotalScore, this.id);
    }

    /* 读取常模 */
    public void readNorm(Sheet expSheet) {
        ScaleNormParser scaleNormParser = new ScaleNormParser();
        scaleNormParser.parse(expSheet, normObjList, dimensionMap);
        setNormGradeOrAgeFlag(scaleNormParser.getNormGradeOrAgeFlag());

    }

    /* 获取常模对象列表 */
    public List<ScaleNormObject> getScaleNormObjectList() {
        if (normObjList.size() > 0) {
            return normObjList;
        } else {
            return null;
        }
    }

    public List<ScaleExplainObject> getScaleExplainObject() {
        if (explainObjList.size() > 0)
            return explainObjList;
        else
            return null;
    }

    public String getEndWord() {
        return endWord;
    }

    public void setEndWord(String endWord) {
        this.endWord = endWord;
    }

    public List<ScoreGrade> getScoreGrades() {
        return scoreGrades;
    }

    public List<PreWarn> getPreWarns() {
        return preWarns;
    }

    /**
     * 解析Excel模板得分水平内容
     * 
     * @param curSheet
     */
    private void readClassify(Sheet curSheet) {
        // TODO Auto-generated method stub
        ClassifySheetParser classifyParser = new ClassifySheetParser();
        if (null == scoreGrades)
            scoreGrades = new ArrayList<ScoreGrade>();
        if (null != dimensionMap) {
            classifyParser.parse(curSheet, dimensionMap, scoreGrades);
        }
    }

    /**
     * 解析Excel预警级别水平内容
     * 
     * @param curSheet
     */
    private void readPrewarn(Sheet curSheet) {
        // TODO Auto-generated method stub
        PreWarnSheetParser preWarnParser = new PreWarnSheetParser();
        if (null == preWarns)
            preWarns = new ArrayList<PreWarn>();
        if (null != dimensionMap) {
            preWarnParser.parse(curSheet, dimensionMap, preWarns);
        }
    }

    /**
     * 解析Excel模板题本内容
     * 
     * @param 给questions添加计分
     */
    private void readScore(Sheet sheet) throws Exception {
        try {
            ScoreSheetParser scoreParser = new ScoreSheetParser();
            if (null != questions) {
                scoreParser.parser(sheet, questions);
            }
        } catch (Exception e) {
            throw new Exception("读取分数有误，请检查量表模板。");
        }
    }

    /* 读取统计图参数 */
    public void readReportImageParam(Sheet sheet) {
        if (sheet == null)
            return;
        ReportImageParamParser imageParamParser = new ReportImageParamParser();
        imageParamParser.parse(sheet);
        reportImageParam1 = imageParamParser.getImageParam1();
        reportImageParam2 = imageParamParser.getImageParam2();

    }

}
