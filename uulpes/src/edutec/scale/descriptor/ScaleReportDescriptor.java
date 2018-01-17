package edutec.scale.descriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.management.modelmbean.DescriptorSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.util.MathUtils;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.User;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.NationUtil;

import edutec.group.data.DataQuery;
import edutec.group.domain.DimDescription;
import edutec.scale.exam.ExamScoreGrade;
import edutec.scale.exam.ExamWarning;
import edutec.scale.explain.ReportArticle;
import edutec.scale.explain.ReportImg;
import edutec.scale.model.Dimension;
import edutec.scale.model.Option;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.DimDetail;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.QuestionBlock;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.QuestionnaireConsts;
import edutec.scale.util.ScaleUtils;
import edutec.scale.util.html.SimpleHtmlTable;
import heracles.db.ibatis.DefaultSqlMapClient;
import heracles.jfree.JChartParam;
import heracles.jfree.bean.DatasetBean;
import heracles.util.OrderValueMap;
import heracles.util.Pools;
import heracles.util.UtilCollection;
import heracles.util.UtilMisc;
import heracles.web.util.HtmlStr;
import heracles.web.util.HtmlTable;
import jodd.util.StringUtil;

@Scope("prototype")
@Component("ScaleReportDescriptor")
public class ScaleReportDescriptor implements Descriptor {

    @Autowired
    private ExamWarning examWarning;
    @Autowired
    private ExamScoreGrade examScoreGrade;
    @Autowired
    private ScaleReportEffect scaleReportEffect;
    private static final Log logger = LogFactory.getLog(DescriptorSupport.class);

    String data;
    String text;
    private String image;
    private String descn;
    private String defaultText;
    protected Questionnaire questionnaire;
    private heracles.util.Effect effect;
    protected String effectClassname;
    protected Map<String, String> descnProps;
    protected List<DimDetail> dimDetailList;
    private JChartParam chartParam;
    private boolean needRecalulate = true;
    private Object subjectUser;
    private Object observerUser;
    private long resultId;// 测试结果编号
    private String threeAngleUUID;// 三角视识别码
    protected static final String IMAGE1 = "image1";
    private String image1Url = "";
    private String image2Url = "";
    private int isImage1Display = 0;
    private int isImage2Display = 0;

    private static final String RECOMMEND_MX = "recommendMx";
    public static final String MHS = "00011"; // 心理健康量表
    public static final String MS = "11001"; // 道德（品德）
    public static final String RANK_DESC[] = { "得分非常低", "得分比较低", "中等", "得分比较高", "得分非常高" };

    public static final String INDIV_KEY = "indiv";
    private static final String SUMMARIZE_KEY = "summarize";
    private static final String PROP_IMG_KEY = "propImg";
    private static final String DIMS_DESCN_TABLE_KEY = "dimsDescnTable";
    private static final String ADVICE_KEY = "advice";

    // added by zhaowanfeng
    private Object subjectUserInfo;
    private Object observerUserInfo;
    private Account obsverUserAccount;

    public void setExamWarning(ExamWarning examWarning) {
        this.examWarning = examWarning;
    }

    public void setExamScoreGrade(ExamScoreGrade examScoreGrade) {
        this.examScoreGrade = examScoreGrade;
    }

    public void setScaleReportEffect(ScaleReportEffect scaleReportEffect) {
        this.scaleReportEffect = scaleReportEffect;
    }

    public void clearDimList() {
        dimDetailList = null;
    }

    public Account getObsverUserAccount() {
        return obsverUserAccount;
    }

    public void setObsverUserAccount(Account obsverUserAccount) {
        this.obsverUserAccount = obsverUserAccount;
    }

    public Object getSubjectUserInfo() {
        return subjectUserInfo;
    }

    public void setSubjectUserInfo(Object subjectUserInfo) {
        this.subjectUserInfo = subjectUserInfo;
    }

    public Object getObserverUserInfo() {
        return observerUserInfo;
    }

    public void setObserverUserInfo(Object observerUserInfo) {
        this.observerUserInfo = observerUserInfo;
    }

    public String getData() {
        return StringUtils.trimToEmpty(data);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImage() {
        return StringUtils.trimToEmpty(image);
    }

    public void setImage(String image) {
        if (StringUtils.isNotEmpty(image)) {
            chartParam = new JChartParam().parseString(image);
        }
    }

    public String getText() {
        return StringUtils.trimToEmpty(text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    protected HtmlTable createHtmlTable() {
        HtmlTable htmTable = new HtmlTable(512);
        htmTable.addValColTitles(new String[] { "分数", "解释" });
        htmTable.addHeadFactorTitiles(new String[] { "维度" });
        return htmTable;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public List<DimDetail> evaluateDimRank(Map<Object, Object> params) {
        try {
            List<DimDetail> dimList = getDimDetails();// 此句应该放到三角视计算完毕！
            // 如果是《中学生心理健康》，计算三角视。即计算各方结果之和的平均值
            if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
                // calcThreeAngle(dimList);//入库时三角视已经计算过，这里不再计算。赵万锋
                // 做题时已经计算过告警等级和得分等级，这里也不再计算。重新计算告警等级和得分等级
                // ExamWarning warning =
                // SpringContextHolder.getBean("examWarning",ExamWarning.class);
                // warning.setQuestionnaire(questionnaire);
                // warning.calGrade();
                // ExamScoreGrade examScoreGrade =
                // SpringContextHolder.getBean("examScoreGrade",ExamScoreGrade.class);
                // examScoreGrade.setQuestionnaire(questionnaire);
                // examScoreGrade.calGrade();
            }

            setDimDescriptor();// 计算维度的结果解释和建议信息
            evalMentalHealthRank();// 计算心理健康量表的总分等级

            return dimList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 《心理健康量表》需要重新计算总分的等级rank; 《心理健康量表》根据已经计算好的等级再次做计算；
     * 心理健康量表的总分不是根据题目得分计算出来的，而是根据三个维度的得分水平判断出来的
     */
    private void evalMentalHealthRank() {
        /**
         * 中学生心理健康量表，有三个父维度和12子维度，这里使用3个父维度
         */
        String scaleId = questionnaire.getScaleId();
        // if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
        if (ScaleUtils.isMentalHealthScale(questionnaire.getScaleId())) {
            // 算出总分等级，因为总分的结果解释需要从数据库中取
            DimensionBlock dimBlk0 = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
            DimensionBlock dimBlk10 = questionnaire.findDimensionBlock("W2");// 困扰
            DimensionBlock dimBlk5 = questionnaire.findDimensionBlock("W1");// 适应
            DimensionBlock dimBlk15 = questionnaire.findDimensionBlock("W3");// 复原力

            int i1 = dimBlk10.getRank(); // 困扰等级
            int i2 = dimBlk5.getRank(); // 适应力等级
            int i3 = dimBlk15.getRank(); // 复原力等级

            if (i1 <= 2) {
                dimBlk0.setRank(1); // 一级
            } else {
                if (i2 > 2 && i3 > 2)
                    dimBlk0.setRank(5); //
                if (i2 < 3 && i3 > 2)
                    dimBlk0.setRank(4); //
                if (i2 > 2 && i3 < 3)
                    dimBlk0.setRank(3); //
                if (i2 < 3 && i3 < 3)
                    dimBlk0.setRank(2); //
            }
            // 计算w0的结果解释和建议赵万锋
            List<DimDetail> list = getDimDetails();
            for (int i = 0; i < list.size(); i++) {
                DimDetail dimDetail = list.get(i);
                if (dimDetail.getId().equals("W0")) {
                    scaleReportEffect.dimExplain(dimDetail, this);
                    break;
                }
            }
        }
    }

    private void calcThreeAngle(List<DimDetail> dimList) {
        DefaultSqlMapClient mapClient = DefaultSqlMapClient.getInstance();
        Long existRId = this.getResultId();
        String sjssbm = this.getThreeAngleUUID();// 三角视识别码
        if (existRId != null && existRId > 0) {
            // SELECT * FROM examresult_dim_mental_health WHERE
            // result_id=#value#
            // 从examresult_dim_mental_health表获取该学生所有三角视量表结果
            // SELECT * FROM examresult_dim_mental_health WHERE sjssbm=#value#
            List<Map> list = mapClient.queryForList("selectExamDimResultMh1", sjssbm);
            if (list == null)
                return;
            int sz = list.size();
            // 最多3个
            if (sz != 0) {
                for (DimDetail detail : dimList) {
                    String dimId = detail.getId();
                    // 如果是三角视，那么W0维度不参与算分。added by 赵万锋
                    if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
                        if (dimId == "W0")
                            continue;
                    }

                    // 当前人的测试结果
                    double dimV = (Double) detail.getStdScore();

                    if (sz == 1) {
                        Map map = list.get(0);
                        int value = (Integer) map.get(dimId);
                        double v = value / 100D;
                        detail.setzScore(MathUtils.round((v + dimV) / 2D, 2));
                        detail.setDimFinalScore(MathUtils.round((v + dimV) / 2D, 2));
                        detail.settScore(5.5 + 1.5 * MathUtils.round((v + dimV) / 2D, 2));
                    } else if (sz == 2) {
                        Map map1 = list.get(0);
                        Map map2 = list.get(1);
                        int value1 = (Integer) map1.get(dimId);
                        double v1 = value1 / 100D;
                        int value2 = (Integer) map2.get(dimId);
                        double v2 = value2 / 100D;
                        detail.setzScore(MathUtils.round((dimV + v1 + v2) / 3D, 2));
                        detail.setDimFinalScore(MathUtils.round((dimV + v1 + v2) / 3D, 2));
                        detail.settScore(5.5 + 1.5 * MathUtils.round((dimV + v1 + v2) / 3D, 2));
                    } else {
                        Map map1 = list.get(0);
                        Map map2 = list.get(1);
                        Map map3 = list.get(2);
                        int value1 = (Integer) map1.get(dimId);
                        int value2 = (Integer) map2.get(dimId);
                        int value3 = (Integer) map3.get(dimId);
                        double v1 = value1 / 100D;
                        double v2 = value2 / 100D;
                        double v3 = value3 / 100D;
                        detail.setzScore(MathUtils.round((v1 + v2 + v3) / 3D, 2));
                        detail.setDimFinalScore(MathUtils.round((v1 + v2 + v3) / 3D, 2));
                        detail.settScore(5.5 + 1.5 * MathUtils.round((v1 + v2 + v3) / 3D, 2));
                    }
                }
            }
        }
    }

    /**
     * 获得维度结果信息，是此类的核心方法，获得计算结果/解释的唯一入口
     * 
     * @return
     */
    public List<DimDetail> getDimDetails() {
        if (dimDetailList == null) {

            try {
                // ExamWarningAndScoreGrade statis = new
                // ExamWarningAndScoreGrade(questionnaire);
                // 设置告警等级以及得分等级，然后才可以获得结果解释和建议
                // ExamWarning warning = new ExamWarning(questionnaire);
                // ExamScoreGrade grade = new ExamScoreGrade(questionnaire);
                // ExamWarning warning =
                // SpringContextHolder.getBean("examWarning",ExamWarning.class);
                // 计算维度分数的时候同时已经计算了维度得分等级和告警等级，因此这里不需要再进行计算
                /*
                 * examWarning.setQuestionnaire(questionnaire);
                 * examWarning.calGrade(); //ExamScoreGrade examScoreGrade =
                 * SpringContextHolder.getBean("examScoreGrade",ExamScoreGrade.
                 * class); examScoreGrade.setQuestionnaire(questionnaire);
                 * examScoreGrade.calGrade();
                 */
                // calAllDimFinalScore();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return dimDetailList;
    }

    public void setDimDescriptor() throws Exception {
        // scaleReportEffect.setDescriptor(this);
        dimDetailList = new ArrayList<DimDetail>();
        List<DimensionBlock> list = questionnaire.getDimensionBlocks();
        int idx = 0;
        int dimInVisiIdx[] = null;
        // if (chartParam != null) {
        // String dimInVisi =
        // getDescnPropStr(QuestionnaireConsts.TBL_DIM_INVISIBLE,
        // StringUtils.EMPTY);
        // dimInVisiIdx = UtilCollection.toIntArray(dimInVisi);
        // }
        for (DimensionBlock dimBlk : list) {
            try {
                DimDetail dimDetail = new DimDetail(dimBlk);
                // 三角视总得分另算
                if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
                    if (!Dimension.SUM_SCORE_DIM.equals(dimBlk.getId()))
                        scaleReportEffect.dimExplain(dimDetail, this);
                } else if (ScaleUtils.isMoralityScale(questionnaire.getScaleId())) {
                    if (Dimension.SUM_SCORE_DIM0.equals(dimBlk.getId()))
                        setMoralityW0Descriptor(dimDetail);
                    else
                        scaleReportEffect.dimExplain(dimDetail, this);

                } else {
                    scaleReportEffect.dimExplain(dimDetail, this);
                }
                // 结果解释和建议的设置

                dimDetailList.add(dimDetail);
                /*
                 * if (chartParam != null) { if
                 * (ArrayUtils.indexOf(dimInVisiIdx, idx) ==
                 * ArrayUtils.INDEX_NOT_FOUND) { Object o =
                 * dimDetail.getDimFinalScore(); double v =
                 * Double.parseDouble(o.toString());
                 * chartParam.putToDataSet(dimDetail.getTitle(), v); } }
                 */
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            idx++;
        }
    }

    private void setMoralityW0Descriptor(DimDetail dim) throws Exception {
        double maxValue = 0;
        String w0Desc = "";
        String[] descArray = { "在思考问题和做出判断时，你更多的是从%s角度出发。", "在思考问题和做出判断时，该学生更多的是从%s角度出发",
                "在思考问题和做出判断时，您的孩子更多的是从%s角度出发。" };
        List<DimensionBlock> list = questionnaire.getDimensionBlocks();
        for (DimensionBlock dimBlk : list) {
            if (!Dimension.SUM_SCORE_DIM0.equals(dimBlk.getId())) {
                double raw = dimBlk.getRawScore().doubleValue();
                if (raw > maxValue) {
                    maxValue = raw;
                    w0Desc = dimBlk.getTitle();
                }
            }
        }
        Object observerUser = getObserverUserInfo();// 看报告人
        if (observerUser instanceof Student) {
            String s = descArray[0];
            s = String.format(s, w0Desc);
            dim.setDimDescn(s);
        }
        if (observerUser instanceof Teacher) {
            String s = descArray[1];
            s = String.format(s, w0Desc);
            dim.setDimDescn(s);
        }
        if (observerUser instanceof Parent) {
            String s = descArray[2];
            s = String.format(s, w0Desc);
            dim.setDimDescn(s);
        }
    }

    private void setMBTIDescriptor(Map<Object, Object> params) {
        dimDetailList = new ArrayList<DimDetail>();
        List<DimensionBlock> list = questionnaire.getDimensionBlocks();
        for (DimensionBlock dimBlk : list) {

            DimDetail dimDetail = new DimDetail(dimBlk);
            dimDetailList.add(dimDetail);

        }
        String dimTitle = "";
        try {
            DimensionBlock dimBlk = questionnaire.findDimensionBlock("W1");
            Number rawScore = dimBlk.getRawScore();
            if (rawScore.intValue() >= 4)
                dimTitle += "I";
            else
                dimTitle += "E";

            dimBlk = questionnaire.findDimensionBlock("W2");
            rawScore = dimBlk.getRawScore();
            if (rawScore.intValue() >= 4)
                dimTitle += "N";
            else
                dimTitle += "S";

            dimBlk = questionnaire.findDimensionBlock("W3");
            rawScore = dimBlk.getRawScore();
            if (rawScore.intValue() >= 4)
                dimTitle += "F";
            else
                dimTitle += "T";

            dimBlk = questionnaire.findDimensionBlock("W4");
            rawScore = dimBlk.getRawScore();
            if (rawScore.intValue() >= 4)
                dimTitle += "J";
            else
                dimTitle += "P";
            scaleReportEffect.MBTIExplain(dimTitle, params, this);
            ;

        } catch (Exception e) {

        }

    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getDataDetail(Map params) {
        params.put(QuestionnaireConsts.TABLE, getDataTablename());
        return (Map<String, Object>) DescriptorHelper.queryMap(data, params);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getTextDetail(Map params) {
        params.put(QuestionnaireConsts.TABLE, getTextTablename());
        return (Map<String, Object>) DescriptorHelper.queryMap(text, params);
    }

    public Map<String, Object> getFactorDetail() {
        return questionnaire.getFactorDetail();
    }

    // added by zhaowanfeng
    public Map<String, Object> getUserInfoDetail() {
        return questionnaire.getUserInfoDetail();
    }

    public String getDataTablename() {
        return questionnaire.getDataTablename();
    }

    public String getTextTablename() {
        return questionnaire.getTextTablename();
    }

    public String getReportTpl() {
        return questionnaire.getReportTpl();
    }

    public String toHtml(Map<Object, Object> page) throws Exception {
        // 计算量表的维度的rank
        // evaluateDimRank(page);
        if (ScaleUtils.isMBTIScale(questionnaire.getScaleId())) {
            setMBTIDescriptor(page);
        } else
            setDimDescriptor();// 计算维度的结果解释和建议信息
        evalMentalHealthRank();// 计算心理健康量表的总分等级

        // 个人信息
        Object subjectUeserInfo = getSubjectUserInfo();
        // 根据量表及其维度和所看报告的对象，提取words
        // 如果是中学生家长或教师，将转换为
        String oldScalesId = questionnaire.getScaleId();
        // if (oldScalesId.equals("100012") || oldScalesId.equals("100013")) {
        // questionnaire.setScaleId("100011");
        // }
        int typeFlag;
        Object observerUser = this.getObserverUserInfo();
        if (observerUser instanceof Student)
            typeFlag = 1;
        else if (observerUser instanceof Teacher)
            typeFlag = 2;
        else
            typeFlag = 3;

        ReportArticle article = new ReportArticle(questionnaire, dimDetailList, typeFlag);
        // 得到维度的结果解析和发现建议
        // article.content(true);
        article.buildPersonalReport(0);
        // 得到维度解释和发展建议
        Map<String, StrBuilder> dimDescnMap = article.getDetailOfDims();
        // 向页面上添加信息
        // 设置答题人 个人信息
        setSubjectUserInfo(page);
        // imgs
        ReportImg propImg = new ReportImg(this);
        String dimScoreTable = null;// 维度得分表
        // 维度统计图参数构建
        // buildModels(page);

        String reportGraph = questionnaire.getScale().getReportGraph();
        String reportImageParam1 = StringEscapeUtils.unescapeXml(questionnaire.getScale().getReportImageParam1());
        String reportImageParam2 = StringEscapeUtils.unescapeXml(questionnaire.getScale().getReportImageParam2());
        page.put("reportGraph", reportGraph);
        if (reportGraph != null) {
            if (reportGraph.equals("折-1中")) {
                page.put("isImage1Display", 0);// 不显示维度统计图
                List<Score> pf16DimList = getPf16DimList();
                page.put("sDimList", pf16DimList);
                // dimDescnTable = pf16DimDescnTable();
                JChartParam chartParam = buildScaleImgChartParam(reportImageParam1);
                if (chartParam != null) {
                    image1Url = chartParam.paramToStr();
                    page.put(IMAGE1, chartParam.paramToStr());
                }
            } else if (reportGraph.equals("柱-多") || reportGraph.equals("柱-2边")) {
                dimScoreTable = getDimScoreTable();
                page.put("isImage1Display", 1);// 显示维度统计图
                page.put(IMAGE1, propImg.getSumHerf1());
            } else if (reportGraph.equals("柱-2中")) {
                dimScoreTable = getDimScoreTable();
                String hrefFmt = "bean=edutec.scale.descriptor.Mbti5ChartBuilder&chs=500x250&";
                page.put("isImage1Display", 0);// 不显示维度统计图
                String imageUrl = buildMBTIScaleImgURL(hrefFmt);
                page.put("image2", imageUrl);

            }

            else if (reportGraph.contains("折-2中")) {
                dimScoreTable = getDimScoreTable();
                page.put("isImage1Display", 0);// 不显示维度统计图
                // page.put(IMAGE1,propImg.getZheTwoHref());

                JChartParam chartParam1 = buildScaleImgChartParam(reportImageParam1);
                if (chartParam1 != null) {
                    image1Url = chartParam1.paramToStr();
                    page.put("image2", chartParam1.paramToStr());
                }

                if (StringUtil.isNotEmpty(reportImageParam2)) {
                    if (ScaleUtils.isEPQScale(questionnaire.getScaleId())) {
                        Number eVal = questionnaire.findDimensionBlock("W1").getTScore();
                        Number nVal = questionnaire.findDimensionBlock("W2").getTScore();

                        String hrefFmt = "chs=480x480&range=0-100&marks=43.3,50,56.7&dotMarkers=38.5,61.5&"
                                + "cht=xy&domainMarks=43.3,50,56.7&dotDomainMarkers=38.5,61.5&"
                                + "markRange=38.5-61.5&data=%d:%d";

                        page.put("image3", String.format(hrefFmt, eVal.intValue(), nVal.intValue()));
                    } else {
                        JChartParam chartParam2 = buildScaleImgChartParam(reportImageParam2);
                        if (chartParam2 != null) {
                            image2Url = chartParam2.paramToStr();
                            page.put("image3", chartParam2.paramToStr());
                        }

                    }
                }
            } else
                dimScoreTable = getDimScoreTable();
        }

        // 设置维度得分表
        page.put("dimScoreTable", dimScoreTable);

        // page.put(IMAGE1,getW0Graph(propImg));

        // 总评,无总评的（即无W0的）这里为空,能力量表也将把维度详细也放入此
        // page.put(SUMMARIZE_KEY, getSummarize(article));
        // page.put(DIMS_DESCN_TABLE_KEY, getDimsDescnTable(dimDescnMap,
        // propImg)); // 维度详细,能力表为空
        // page.put(ADVICE_KEY, article.getAdvice()); // 建议
        // page.put(PROP_IMG_KEY, propImg); // 图片

        page.put("summarizedesc", article.getSummarizeDesc());
        page.put("summarizedevice", article.getSummarizeDevice());
        page.put("dimDetailList", article.getDetailList()); // 维度详细
        page.put("scale", questionnaire.getScale());
        // postSetPage(page, article);
        questionnaire.setScaleId(oldScalesId);
        return null;
        // 选择freema模板，模板上的变量在page上；
        // return reportTpl(page);
        // return "";
    }

    public void toOfficeWord(Map<Object, Object> page) throws Exception {
        // 计算量表的维度的rank
        // evaluateDimRank(page);
        setDimDescriptor();// 计算维度的结果解释和建议信息
        evalMentalHealthRank();// 计算心理健康量表的总分等级

        // 个人信息
        Object subjectUeserInfo = getSubjectUserInfo();
        // 根据量表及其维度和所看报告的对象，提取words
        // 如果是中学生家长或教师，将转换为
        String oldScalesId = questionnaire.getScaleId();
        // if (oldScalesId.equals("100012") || oldScalesId.equals("100013")) {
        // questionnaire.setScaleId("100011");
        // }
        int typeFlag;
        Object observerUser = this.getObserverUserInfo();
        if (observerUser instanceof Student)
            typeFlag = 1;
        else if (observerUser instanceof Teacher)
            typeFlag = 2;
        else
            typeFlag = 3;

        ReportArticle article = new ReportArticle(questionnaire, dimDetailList, typeFlag);
        // 得到维度的结果解析和发现建议
        article.buildPersonalReport(1);
        // 得到维度解释和发展建议
        Map<String, StrBuilder> dimDescnMap = article.getDetailOfDims();
        // 向页面上添加信息
        // 设置答题人 个人信息
        setSubjectUserInfo(page);
        // imgs
        ReportImg propImg = new ReportImg(this);
        List dimScoreTable = null;// 维度得分表
        // 维度统计图参数构建
        // buildModels(page);

        String reportGraph = questionnaire.getScale().getReportGraph();
        /*
         * page.put("reportGraph",reportGraph); if(reportGraph!=null){
         * if(reportGraph.equals("折-1中")){ page.put("isImage1Display",
         * 0);//不显示维度统计图 List<Score> pf16DimList =getPf16DimList();
         * page.put("sDimList", pf16DimList); //dimDescnTable =
         * pf16DimDescnTable(); } else
         * if(reportGraph.equals("柱多")||reportGraph.equals("柱-2边")){
         * dimScoreTable = getDimScoreList(); page.put("isImage1Display",
         * 1);//显示维度统计图 page.put(IMAGE1,propImg.getSumImageData()); } else
         * dimScoreTable = getDimScoreList(); } //设置维度得分表 page.put("scorelist",
         * dimScoreTable);
         */
        String reportImageParam1 = StringEscapeUtils.unescapeXml(questionnaire.getScale().getReportImageParam1());
        String reportImageParam2 = StringEscapeUtils.unescapeXml(questionnaire.getScale().getReportImageParam2());
        page.put("reportGraph", reportGraph);
        if (reportGraph != null) {
            if (reportGraph.equals("折-1中")) {
                page.put("isImage1Display", 0);// 不显示维度统计图
                List<Score> pf16DimList = getPf16DimList();
                page.put("sDimList", pf16DimList);
                // dimDescnTable = pf16DimDescnTable();
                JChartParam chartParam = buildScaleImgChartParam(reportImageParam1);
                if (chartParam != null) {
                    image1Url = chartParam.paramToStr();
                    String iamgeData = propImg.getSumImageData(chartParam.paramToStr());
                    page.put(IMAGE1, iamgeData);
                }
            } else if (reportGraph.equals("柱-多") || reportGraph.equals("柱-2边")) {
                dimScoreTable = getDimScoreList();
                page.put("isImage1Display", 1);// 显示维度统计图
                page.put(IMAGE1, propImg.getSumImageData());
            } else if (reportGraph.equals("柱-2中")) {
                dimScoreTable = getDimScoreList();
                String hrefFmt = "bean=edutec.scale.descriptor.Mbti5ChartBuilder&chs=500x250&";
                page.put("isImage1Display", 0);// 不显示维度统计图
                String imageUrl = buildMBTIScaleImgURL(hrefFmt);
                String iamgeData = propImg.getSumImageData(imageUrl);
                page.put("image2", iamgeData);
            }

            else if (reportGraph.contains("折-2中")) {
                dimScoreTable = getDimScoreList();
                page.put("isImage1Display", 0);// 不显示维度统计图
                JChartParam chartParam1 = buildScaleImgChartParam(reportImageParam1);
                if (chartParam1 != null) {
                    image1Url = chartParam1.paramToStr();
                    String iamgeData = propImg.getSumImageData(image1Url);
                    page.put("image2", iamgeData);
                }

                if (StringUtil.isNotEmpty(reportImageParam2)) {
                    if (ScaleUtils.isEPQScale(questionnaire.getScaleId())) {
                        Number eVal = questionnaire.findDimensionBlock("W1").getTScore();
                        Number nVal = questionnaire.findDimensionBlock("W2").getTScore();

                        String hrefFmt = "chs=480x480&range=0-100&marks=43.3,50,56.7&dotMarkers=38.5,61.5&"
                                + "cht=xy&domainMarks=43.3,50,56.7&dotDomainMarkers=38.5,61.5&"
                                + "markRange=38.5-61.5&data=%d:%d";
                        String iamgeData = propImg
                                .getSumImageData(String.format(hrefFmt, eVal.intValue(), nVal.intValue()));
                        page.put("image3", iamgeData);
                    } else {
                        JChartParam chartParam2 = buildScaleImgChartParam(reportImageParam2);
                        if (chartParam2 != null) {
                            image2Url = chartParam2.paramToStr();
                            String iamgeData = propImg.getSumImageData(image2Url);
                            page.put("image3", iamgeData);
                        }

                    }
                }
            } else
                dimScoreTable = getDimScoreList();
        }

        page.put("scorelist", dimScoreTable);

        // 总评,无总评的（即无W0的）这里为空,能力量表也将把维度详细也放入此
        page.put("summarizedesc", article.getSummarizeDesc());
        page.put("summarizedevice", article.getSummarizeDevice());
        page.put("dimlist", article.getDetailList()); // 维度详细
        // page.put(PROP_IMG_KEY, propImg); // 图片
        page.put("scale", questionnaire.getScale());
        questionnaire.setScaleId(oldScalesId);
    }

    // 设置答题人个人信息
    private void setSubjectUserInfo(Map<Object, Object> page) {
        Map<String, String> userinfo = new HashMap<String, String>();
        if (subjectUserInfo instanceof Student) {
            Student stu = (Student) subjectUserInfo;

            ExamdoStudent examdo = (ExamdoStudent) questionnaire.getExamdo();
            userinfo.put("xxmc", stu.getXxmc());
            userinfo.put("xm", stu.getXm());
            String nj = AgeUitl.getGradeName(examdo.getGradeid());
            userinfo.put("njmc", stu.getNjmc());
            userinfo.put("bjmc", examdo.getBjmc());
            userinfo.put("name", stu.getXm());
            userinfo.put("sfzjh", stu.getSfzjh());
            userinfo.put("xh", stu.getXh());
            userinfo.put("crsq", stu.getCsrq());
            String xb = stu.getXbm().equals("1") ? "男" : "女";
            userinfo.put("xb", xb);
            String endTime = page.get("endTime").toString();
            if (stu.getCsrq() != null) {
                int age = AgeUitl.getAgeAtTime(stu.getCsrq(), endTime);
                userinfo.put("age", String.valueOf(age));
            } else
                userinfo.put("age", "");
            userinfo.put("xmpy", stu.getXmpy());
            userinfo.put("mz", NationUtil.getMz(stu.getMzm()));
            // userinfo.put("mz", stu.getMzm());
            String zp = "/pes/themes/theme1/images/user_default.png";
            // if(stu.getZp()!=null){
            long time = System.currentTimeMillis();
            zp = "/pes/systeminfo/sys/getZp.do?id=" + stu.getId() + "&typeflag=1";
            // }
            userinfo.put("zp", zp);

            page.put("userinfo", userinfo); // 个人信息
        }
        if (subjectUserInfo instanceof Teacher) {
            Teacher teacher = (Teacher) subjectUserInfo;

            ExamdoTeacher examdo = (ExamdoTeacher) questionnaire.getExamdo();
            userinfo.put("xxmc", teacher.getXxmc());
            userinfo.put("xm", teacher.getXm());
            userinfo.put("sfzjh", teacher.getSfzjh());
            userinfo.put("gh", teacher.getGh());
            userinfo.put("crsq", teacher.getCsrq());
            String xb = teacher.getXbm().equals("1") ? "男" : "女";
            userinfo.put("xb", xb);
            String endTime = page.get("endTime").toString();
            if (teacher.getCsrq() != null) {
                int age = AgeUitl.getAgeAtTime(teacher.getCsrq(), endTime);
                userinfo.put("age", String.valueOf(age));
            } else
                userinfo.put("age", "");
            userinfo.put("xmpy", teacher.getXmpy());
            userinfo.put("mz", NationUtil.getMz(teacher.getMzm()));
            userinfo.put("rolename", examdo.getRolename());
            // userinfo.put("mz", stu.getMzm());
            String zp = "/pes/themes/theme1/images/user_default.png";
            // if(teacher.getZp()!=null){
            long time = System.currentTimeMillis();
            zp = "/pes/systeminfo/sys/getZp.do?id=" + teacher.getId() + "&typeflag=2";
            // }
            userinfo.put("zp", zp);
            page.put("userinfo", userinfo); // 个人信息
        }
        if (subjectUserInfo instanceof Parent)
            page.put("userinfo", (Parent) subjectUserInfo); // 个人信息
    }

    /*
     * private UserInfo createUserinfo(Object subjectUser){ UserInfo userInfo =
     * new UserInfo(); if(subjectUser instanceof Student){ Student student =
     * (Student)subjectUser; // 个人信息 userInfo.setAge(student.get); }
     * if(subjectUser instanceof Teacher) page.put(INDIV_KEY,
     * (Teacher)subjectUser); // 个人信息 if(subjectUser instanceof Parent)
     * page.put(INDIV_KEY, (Parent)subjectUser); // 个人信息
     * 
     * return userInfo; }
     */
    public void buildModels(Map<Object, Object> params) {

        // buildModelsImpl(page);
        // buildEqpModelsImpl(page);
        String scaleId = this.getQuestionnaire().getScale().getId();
        String s = getImgHerfQry(scaleId);
        if (StringUtils.startsWithIgnoreCase(s, "bean=")) {
            String qry = bulidScaleImgHerfQry(s);
            image1Url = qry;
            System.out.println(qry);
            params.put(IMAGE1, "create.chart?" + qry);
            params.put("isImage1Display", 1);
        } else {
            JChartParam chartParam = buildScaleImgChartParam(s);
            if (chartParam != null) {
                image1Url = chartParam.paramToStr();
                params.put("image2", chartParam.paramToStr());
                params.put("isImage1Display", 1);
            }
        }
    }

    /**
     * 返回总评内容，如果不具备W0，表示没有总评，能力量表和具有量表级总评的都有W0
     * 
     * @param article
     * @return
     */
    public String getSummarize(ReportArticle article) {
        if (!isHasW0()) {
            return StringUtils.EMPTY;
        }
        // String visible = getDescnPropStr("rpt-item-visible", "y");
        // String summarize = visible.equals("y") ? article.getSummarize() :
        // StringUtils.EMPTY;
        // return summarize;
        return article.getSummarize();
    }

    /**
     * 得到关于维度的水平显示table<BR>
     * 如：维度1 维度2 维度3 维度4 1 2 3 4
     * 
     * @return
     */
    public String getDimScoreTable() {
        // 1是横显示维度名和维度分,0是竖排维度名和分获/和解释,默认为横向
        // String arrange = getDescnPropStr(QuestionnaireConsts.TBL_ARRANGE,
        // "1");
        String arrange = "2";
        boolean isExplain = true;
        ;
        boolean isRawVisi = true;
        boolean isStdVisi = false;
        boolean isTVisi = true;

        SimpleHtmlTable table = new SimpleHtmlTable(true);
        table.setWidth("80%");
        List<DimDetail> list = getDimDetails();
        int startDimVisiN = 0;
        int endDimVisiN = list.size();

        if (arrange.equals("1")) {
            TR trTitle = table.newTr();
            trTitle.addElement(table.newTD("维度", 80, "center"));
            TR trRaw = null;
            TR trStd = null;
            TR trT = null;
            if (isRawVisi) {
                trRaw = table.newTr();
                trRaw.addElement(table.newTD("原始分", 80, "center"));
            }
            if (isStdVisi) {
                trStd = table.newTr();
                trStd.addElement(table.newTD("标准分", 80, "center"));
            }
            if (isTVisi) {
                trT = table.newTr();
                trT.addElement(table.newTD("T分", 80, "center"));
            }

            // for (int index = startDimVisiN; index < endDimVisiN; index++) {
            // 一级维度得分，维度名称粗体、靠左
            List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
            for (DimensionBlock dimBlk : listLev1) {
                // DimDetail dimDetail = list.get(index);
                DimDetail dimDetail = getDimDetail(dimBlk);
                if (dimDetail.getId().equals("W0") && ScaleUtils.isThirdAngleScale(getQuestionnaire().getScaleId()))// added
                                                                                                                    // by
                                                                                                                    // 赵万锋
                    continue;
                TD td = table.newTD(dimDetail.getTitle(), 80, "center");
                td.setAlign("center");
                trTitle.addElement(td);
                if (trRaw != null) {
                    trRaw.addElement(
                            table.newTD(UtilMisc.ereaseZeros(dimDetail.getRawScore().toString()), 80, "center"));
                }
                if (trStd != null) {
                    trStd.addElement(
                            table.newTD(UtilMisc.ereaseZeros(dimDetail.getStdScore().toString()), 80, "center"));
                }
                if (trT != null) {
                    double t = MathUtils.round(dimDetail.getTScore().doubleValue(), 2);
                    trT.addElement(table.newTD(UtilMisc.ereaseZeros(Double.toString(t)), 80, "center"));
                }
                // 子维度得分紧跟父维度之后，并且维度名称居中
                List<DimensionBlock> subList = dimBlk.getSubdimensionBlkList();
                if (subList != null) {
                    for (DimensionBlock subdimBlk : subList) {
                        DimDetail dimDetail1 = getDimDetail(subdimBlk);
                        TD td1 = table.newTD(dimDetail.getTitle(), 80);
                        td1.setAlign("center");
                        trTitle.addElement(td1);
                        if (trRaw != null) {
                            trRaw.addElement(table.newTD(UtilMisc.ereaseZeros(dimDetail1.getRawScore().toString()), 80,
                                    "center"));
                        }
                        if (trStd != null) {
                            trStd.addElement(table.newTD(UtilMisc.ereaseZeros(dimDetail1.getStdScore().toString()), 80,
                                    "center"));
                        }
                        if (trT != null) {
                            double t = MathUtils.round(dimDetail1.getTScore().doubleValue(), 2);
                            trT.addElement(table.newTD(UtilMisc.ereaseZeros(Double.toString(t)), 80, "center"));
                        }
                    }
                }

            }
        } else {
            TR head = table.newTr();
            head.addElement(table.newTDWidthStyle("维度", 80, "text-align:center;"));
            if (isRawVisi) {
                head.addElement(table.newTD("原始分", 80, "center"));
            }
            if (isStdVisi) {
                head.addElement(table.newTD("标准分", 80, "center"));
            }
            if (isTVisi) {
                head.addElement(table.newTD("T分", 80, "center"));
            }
            List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
            for (DimensionBlock dimBlk : listLev1) {
                // DimDetail dimDetail = list.get(index);
                DimDetail dimDetail = getDimDetail(dimBlk);
                if (dimDetail.getId().equals("W00"))
                    continue;
                // if(dimDetail.getId().equals("W0")&&ScaleUtils.isThirdAngleScale(getQuestionnaire().getScaleId()))//added
                // by 赵万锋
                // continue;
                if (dimDetail.getId().equals("W0"))
                    continue;
                TR tr = table.newTr();
                tr.addElement(table.newTD(dimDetail.getTitle(), 80, "center"));
                if (isRawVisi) {
                    tr.addElement(table.newTD(UtilMisc.ereaseZeros(dimDetail.getRawScore().toString()), 80, "center"));
                }
                if (isStdVisi) {
                    tr.addElement(
                            table.newTD(UtilMisc.ereaseZeros(dimDetail.getDimFinalScore().toString()), 80, "center"));
                }
                if (isTVisi) {
                    double t = MathUtils.round(dimDetail.getTScore().doubleValue(), 2);
                    tr.addElement(table.newTD(UtilMisc.ereaseZeros(Double.toString(t)), 80, "center"));
                }
                // 子维度得分紧跟父维度之后，并且维度名称居中
                List<DimensionBlock> subList = dimBlk.getSubdimensionBlkList();
                if (subList != null) {
                    for (DimensionBlock subdimBlk : subList) {
                        DimDetail dimDetail1 = getDimDetail(subdimBlk);
                        TR tr1 = table.newTr();
                        tr1.addElement(new TD(dimDetail1.getTitle()));
                        if (isRawVisi) {
                            tr1.addElement(table.newTD(UtilMisc.ereaseZeros(dimDetail1.getRawScore().toString()), 80,
                                    "center"));
                        }
                        if (isStdVisi) {
                            tr1.addElement(table.newTD(UtilMisc.ereaseZeros(dimDetail1.getStdScore().toString()), 80,
                                    "center"));
                        }
                        if (isExplain) {
                            double t = MathUtils.round(dimDetail1.getTScore().doubleValue(), 2);
                            tr1.addElement(table.newTD(UtilMisc.ereaseZeros(Double.toString(t)), 80, "center"));
                        }

                    }
                }
            }
            // 以下将总分维度分放在表格最后边
            DimensionBlock dimW0 = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
            if (dimW0 != null && !ScaleUtils.isMentalHealthScale(getQuestionnaire().getScaleId())) {
                DimDetail dimDetail = getDimDetail(dimW0);
                TR tr = table.newTr();
                tr.addElement(table.newTD(dimDetail.getTitle(), 80, "center"));
                if (isRawVisi) {
                    tr.addElement(table.newTD(UtilMisc.ereaseZeros(dimDetail.getRawScore().toString()), 80, "center"));
                }
                if (isStdVisi) {
                    tr.addElement(
                            table.newTD(UtilMisc.ereaseZeros(dimDetail.getDimFinalScore().toString()), 80, "center"));
                }
                if (isTVisi) {
                    double t = MathUtils.round(dimDetail.getTScore().doubleValue(), 2);
                    tr.addElement(table.newTD(UtilMisc.ereaseZeros(Double.toString(t)), 80, "center"));
                }
            }

        }
        return table.toString();
    }

    public List getDimScoreList() {
        boolean isExplain = true;
        ;
        boolean isRawVisi = true;
        boolean isStdVisi = true;
        boolean isTVisi = true;
        List scoretable = new ArrayList();// 得分表

        List<DimDetail> list = getDimDetails();
        int startDimVisiN = 0;
        int endDimVisiN = list.size();
        DimDetail dimW0 = null;
        List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
        for (DimensionBlock dimBlk : listLev1) {
            DimDetail dimDetail = getDimDetail(dimBlk);
            // if(dimDetail.getId()=="W0"&&ScaleUtils.isThirdAngleScale(getQuestionnaire().getScaleId()))//added
            // by 赵万锋
            if (dimDetail.getId().equals("W0")) {
                dimW0 = dimDetail;
                continue;
            }
            if (dimDetail.getId().equals("W00"))
                continue;
            Map dimMap = new HashMap();
            dimMap.put("dimtitle", dimDetail.getTitle().toString());
            double d = MathUtils.round(dimDetail.getRawScore().doubleValue(), 2);
            dimMap.put("rawscore", UtilMisc.ereaseZeros(Double.toString(d)));
            d = MathUtils.round(dimDetail.getTScore().doubleValue(), 2);
            dimMap.put("tscore", UtilMisc.ereaseZeros(Double.toString(d)));
            scoretable.add(dimMap);

            // 子维度得分紧跟父维度之后，并且维度名称居中
            List<DimensionBlock> subList = dimBlk.getSubdimensionBlkList();
            if (subList != null) {
                for (DimensionBlock subdimBlk : subList) {
                    DimDetail subdimDetail = getDimDetail(subdimBlk);
                    Map subDimMap = new HashMap();
                    subDimMap.put("dimtitle", subdimDetail.getTitle().toString());
                    d = MathUtils.round(subdimDetail.getRawScore().doubleValue(), 2);
                    subDimMap.put("rawscore", UtilMisc.ereaseZeros(Double.toString(d)));
                    d = MathUtils.round(subdimDetail.getTScore().doubleValue(), 2);
                    subDimMap.put("tscore", UtilMisc.ereaseZeros(Double.toString(d)));
                    scoretable.add(subDimMap);
                }
            }
        }
        if (dimW0 != null && !ScaleUtils.isThirdAngleScale(getQuestionnaire().getScaleId())) {
            Map dimMap = new HashMap();
            dimMap.put("dimtitle", dimW0.getTitle().toString());
            double d = MathUtils.round(dimW0.getRawScore().doubleValue(), 2);
            dimMap.put("rawscore", UtilMisc.ereaseZeros(Double.toString(d)));
            d = MathUtils.round(dimW0.getTScore().doubleValue(), 2);
            dimMap.put("tscore", UtilMisc.ereaseZeros(Double.toString(d)));
            scoretable.add(dimMap);
        }
        return scoretable;
    }

    /*
     * private String pf16DimDescnTable(){ List<DimDetail> list =
     * getDimDetails(); int startDimVisiN = 0; int endDimVisiN = list.size();
     * StrBuilder result = null; try { result =
     * Pools.getInstance().borrowStrBuilder(); result.
     * append("<table border=0 cellpadding=1 cellspacing=1 bgcolor=#ffcb97 style=\"font-size:12px;line-height:15px\">"
     * ); result.append("<tbody>");
     * result.append("<tr bgcolor=#ffffff height=\"20px\" >");
     * result.append("<td width=60 rowspan=\"2\" align=\"center\">人格因素</td>");
     * result.append("<td width=48 rowspan=\"2\" align=\"center\">原始分</td>");
     * result.append("<td width=48 rowspan=\"2\" align=\"center\">标准分</td>");
     * result.append("<td width=142 rowspan=\"2\" align=\"center\">低分者特性</td>");
     * result.append("<td width=266 align=\"center\">标准分</td>");
     * result.append("<td width=122 rowspan=\"2\" align=\"center\">高分者特性</td>");
     * result.append("</tr>");
     * 
     * result.append("<tr bgcolor=#ffffff height=\"20px\">");
     * result.append("<td align=\"center\"><table width=\"100%\">");
     * result.append("<tr>"); result.
     * append("<td width=\"10%\">1</td><td width=\"10%\">2</td><td width=\"10%\">3</td><td width=\"10%\">4</td><td width=\"10%\">5</td>"
     * ); result.
     * append("<td width=\"10%\">6</td><td width=\"10%\">7</td><td width=\"10%\">8</td><td width=\"10%\">9</td><td width=\"10%\">10</td>"
     * ); result.append("</tr></table></td></tr>"); int size = list.size();
     * for(int i=0;i<size;i++){
     * result.append("<tr bgcolor=#ffffff height=\"36px\">");
     * result.append("<td align=\"center\">A.合群性</td>");
     * result.append("<td align=\"center\">").append(list.get(i).getRawScore().
     * floatValue()).append("</td>");
     * result.append("<td align=\"center\">").append(list.get(i).gettScore()).
     * append("</td>"); result.append("<td>缄默、孤独、冷漠</td>"); if(i==0)
     * result.append("<td rowspan=\"").append(size).
     * append("\" ><div style=\"width:266px;height:").append(36*(size+1)).
     * append("px;\" class=\"img1\"></div></td>");
     * result.append("<td>外向、热情、乐群</td>"); result.append(" </tr>"); }
     * result.append("</tr></tbody></table>"); return result.toString();
     * 
     * } finally { Pools.getInstance().returnStrBuilder(result); } }
     */
    public void setEffectClassname(String effectClassname) {
        this.effectClassname = effectClassname;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
        // this.descnProps = getDescnProps();
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    // public FreemarkerCfg getReportCfg() {
    // return reportCfg;
    // }
    public String reportTpl(Map<Object, Object> page) {
        // String reportTpl = getDescnPropStr(QuestionnaireConsts.REPORT_TPL,
        // getReportTpl());
        String reportTpl = "student_personal_report.flt";
        return reportTpl(page, reportTpl);
    }

    public String reportTpl(Map<Object, Object> page, String tpl) {
        // try {
        page.put("template", tpl);
        return tpl;// reportCfg.process(page);
        // } catch (IOException e) {
        // e.printStackTrace();
        // } //catch (TemplateException e) {
        // e.printStackTrace();
        // }

    }

    public void setDescnProp(String key, String value) {
        if (descnProps == null) {
            descnProps = new HashMap<String, String>();
        }
        descnProps.put(key, value);
    }

    public String getDescnPropStr(String key, String defaultVal) {
        if (descnProps == null)
            return StringUtils.EMPTY;
        return StringUtils.defaultIfEmpty(descnProps.get(key), defaultVal);
    }

    public int getDescnPropInt(String key) {
        return NumberUtils.toInt(getDescnPropStr(key, ""));
    }

    public void clearDescnProp() {
        descnProps.clear();
    }

    public Map<String, String> tempDescnProps() {
        return new HashMap<String, String>(descnProps);
    }

    public void resetDescnProps(Map<String, String> tempprops) {
        descnProps.clear();
        descnProps.putAll(tempprops);
    }

    public String personal() {
        if (getQuestionnaire() == null) {
            return StringUtils.EMPTY;
        }
        List<QuestionBlock> list = getQuestionnaire().getQuestionBlocks(true);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            return HtmlStr.blankIfEmpty(null);
        }
        StrBuilder sb = new StrBuilder();
        Iterator<QuestionBlock> iter = list.iterator();
        while (iter.hasNext()) {
            QuestionBlock qblk = iter.next();
            String title = qblk.getQuestion().getTitle();
            int pos = title.indexOf('(');
            if (pos != -1) {
                title = title.substring(0, pos) + ":";
            }
            sb.append(title);
            String answer = qblk.getAnswer();
            if (qblk.getType().equals(QuestionConsts.TYPE_SELECTION)) {
                SelectionQuestion sQ = (SelectionQuestion) qblk.getQuestion();
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_TEXT_MODE) {
                    sb.append(answer);
                } else {
                    int idx = NumberUtils.toInt(answer, -1);
                    if (idx != -1) {
                        Option opt = sQ.findOption(idx);
                        sb.append(opt.getTitle());
                    }
                }
            } else {
                sb.append(answer);
            }
            if (iter.hasNext()) {
                sb.append(HtmlStr.blankIfEmpty(null));
                sb.append(HtmlStr.blankIfEmpty(null));
            }
        }
        return sb.toString();
    }

    public String imgHerf() {
        return new IMG(getImage()).toString();
    }

    public void setSubjectUser(User user) {
        this.subjectUser = user;
    }

    public void setDimDetailList(List<DimDetail> dimDetailList) {
        this.dimDetailList = dimDetailList;
    }

    public Object getSubjectUser() {
        return subjectUser;
    }

    public Object getObserverUser() {
        return observerUser;
    }

    public void setObserverUser(Object user) {
        observerUser = user;
    }

    public JChartParam getChartParam() {
        getDimDetails();
        return chartParam;
    }

    public boolean isNeedRecalulate() {
        return needRecalulate;
    }

    public void setNeedRecalulate(boolean needRecalulate) {
        this.needRecalulate = needRecalulate;
    }

    public boolean isHasW0() {
        boolean result = false;
        // 是否有总分
        try {
            result = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM) != null;
        } catch (Exception e) {
            result = false;
        }
        if (questionnaire.getDimensionSize() == 1) {
            result = true;
        }
        return result;
    }

    public long getResultId() {
        return resultId;
    }

    public void setResultId(long resultId) {
        this.resultId = resultId;
    }

    public String getThreeAngleUUID() {
        return threeAngleUUID;
    }

    public void setThreeAngleUUID(String threeAngleUUID) {
        this.threeAngleUUID = threeAngleUUID;
    }

    public String getW0Graph(ReportImg propImg) {
        StrBuilder result = null;
        try {
            result = Pools.getInstance().borrowStrBuilder();
            result.append("<p><div align=\"center\"><img src=\"").append(propImg.getSumHerf1())
                    .append("\" width=\"350\" height=\"160\"/></div>");
            return result.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(result);
        }
    }

    public String getDimsDescnTable(Map<String, StrBuilder> dimDescnMap, ReportImg propImg) {
        if (ScaleUtils.isAbilityScale(getQuestionnaire().getScaleId())) {
            return StringUtils.EMPTY;
        }
        StrBuilder result = null;
        try {
            result = Pools.getInstance().borrowStrBuilder();
            String reportGraph = getQuestionnaire().getScale().getReportGraph();
            Iterator<Map.Entry<String, StrBuilder>> it = dimDescnMap.entrySet().iterator();
            int row = 0;
            while (it.hasNext()) {
                // for (int i = 0; i < 2; ++i) {
                if (it.hasNext()) {
                    Map.Entry<String, StrBuilder> ent = it.next();
                    result.append("<p><div align=\"left\">").append(ent.getKey()).append("</strong></div>");
                    if (reportGraph != null && reportGraph.equals("柱-多")) {
                        String dimHerf = propImg.getDimHerfs1()[row];
                        if (dimHerf != null)
                            result.append("<p><div align=\"center\"><img src=\"").append(dimHerf).append("\" alt=\"")
                                    .append(ent.getKey()).append("\" width=\"350\" height=\"160\"/></div>");
                    }
                    result.append("<p><div align=\"left\"><p>").append(ent.getValue()).append("</div>");
                    // row++;
                }
                // }
            }
            return result.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(result);
        }
    }

    protected void dimExplain(DimDetail dimDetail) {
        // UserInfo observerUser = getObserverUserInfo();//看报告人
        int typeFlag = 1;
        // int userFlag = observerUser.getUserType();
        // int userFlag = obsverUserAccount.getRoleFlag();
        Object observerUser = this.getObserverUserInfo();
        if (observerUser instanceof Student)
            typeFlag = 1;
        else if (observerUser instanceof Teacher)
            typeFlag = 2;
        else
            typeFlag = 3;

        Map para = UtilMisc.toMap(Constants.SCALEID_PROP, getQuestionnaire().getScaleId(), QuestionnaireConsts.WID,
                dimDetail.getId(), "grade", dimDetail.getDimBlk().getRank(), "typeFlag", typeFlag);
        DimDescription dimDescription = DataQuery.getDescription(para);
        if (dimDescription != null) {
            String firstStr = dimDescription.getFirstStr();
            String otherStr = dimDescription.getOtherStr();
            String advice = dimDescription.getAdvice();
            if (firstStr != null)
                firstStr = getABString(firstStr);
            if (otherStr != null)
                otherStr = getABString(otherStr);
            if (advice != null)
                advice = getABString(advice);
            dimDetail.setDimDescn(firstStr + otherStr);
            dimDetail.setDimDescn1(firstStr);
            dimDetail.setDimAdvice(advice);
        } else {
            dimDetail.setDimDescn(StringUtils.defaultIfEmpty(getDefaultText(), ""));
            dimDetail.setDimAdvice(StringUtils.defaultIfEmpty(getDefaultText(), ""));
        }
    }

    /*
     * private static String getABString(String str){ String resultStr=""; str =
     * str.replaceAll("[1-9]", ""); str = str.replaceFirst("[aA]", ""); String[]
     * arr = str.split("[aA]"); Random rand = new Random(); for(int i
     * =0;i<arr.length;i++){
     * 
     * String[] subArr = arr[i].split("[a-zA-Z]"); int index =
     * rand.nextInt(subArr.length); String randomStr = subArr[index]; resultStr
     * += randomStr; //for(int j =0;j<subArr.length;j++){
     * //System.out.println(subArr[j]); //} } return resultStr;
     * //System.out.println(resultStr); } private String getABString(String
     * str){ String resultStr=""; str = str.replaceFirst("[0-9][A-B]", "");
     * String[] arr = str.split("[0-9][A]"); Random rand = new Random(); for(int
     * i =0;i<arr.length;i++){
     * 
     * String[] subArr = arr[i].split("[0-9][B]"); int index =
     * rand.nextInt(subArr.length); String randomStr = subArr[index]; resultStr
     * += randomStr; } return resultStr; }
     */
    private static String getABString(String str) {
        String resultStr = "";
        str = str.replaceFirst("[0-9][A-B]", "");
        String[] arr;
        boolean morethanten = false;
        if (str.contains("10A")) {
            morethanten = true;
            arr = str.split("[0-9][A]|[1-2][0-9][A]");
        } else
            arr = str.split("[0-9][A]");
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            String[] subArr;
            if (morethanten) {
                subArr = arr[i].split("[0-9][B]|[1-2][0-9][B-D]");
            } else
                subArr = arr[i].split("[0-9][B-D]");
            int index = rand.nextInt(subArr.length);
            String randomStr = subArr[index];
            resultStr += randomStr;
            // for(int j =0;j<subArr.length;j++){
            // System.out.println(subArr[j]);
            // }
        }
        // System.out.println(resultStr);
        return resultStr;
    }

    /**
     * 用来做排序的rank <br>
     * 1)有量表级总分，就取量表级总分，没有量表级总分就取一级维度分;<br>
     * 2)一级维度分抽取方法：取一级维度中分值最高的;<br>
     * 3）心理健康量表始终在第一位
     * 
     * @return
     */
    public int getOrderRank() {
        if (questionnaire.getScaleId().endsWith(MHS)) {
            return 100;
        }
        DimensionBlock dimBlk0 = null;
        if (ScaleUtils.hasTotalScore(questionnaire.getScaleId())) {
            dimBlk0 = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
            return dimBlk0.getRank();
        } else {
            int r = 0;
            // 一级维度
            List<DimensionBlock> list = questionnaire.getRootDimensionBlocks();
            for (DimensionBlock block : list) {
                if (block.getRank() > r) {
                    r = block.getRank();
                    ;
                }
            }
            return r;
        }
    }

    public List<DimensionBlock> getOrderDims() {
        String scalesId = questionnaire.getScaleId();
        List<DimensionBlock> dims = getQuestionnaire().getRootDimensionBlocks();
        List<DimensionBlock> list = new ArrayList<DimensionBlock>(dims.size());
        // 能力表只要显示总分，按单为处理
        if (ScaleUtils.isAbilityScale(scalesId)) {
            list.add(questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM));
        } else {
            for (DimensionBlock block : dims) {
                if (block.getId().equals(Dimension.SUM_SCORE_DIM)) {
                    // 如果是总分量表，将W0加入
                    if (ScaleUtils.hasTotalScore(scalesId)) {
                        list.add(block);
                    }
                } else {
                    list.add(block);
                }
            }
        }
        // 对列表进行排序
        /*
         * Collections.sort(list, new Comparator<DimensionBlock>() { public int
         * compare(DimensionBlock o1, DimensionBlock o2) { return o2.getRank() -
         * o1.getRank(); } });
         */
        return list;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void buildEqpModelsImpl(Map<Object, Object> page) {
        Number eVal = this.getQuestionnaire().findDimensionBlock("W2").getTScore();
        Number nVal = this.getQuestionnaire().findDimensionBlock("W3").getTScore();

        String hrefFmt = "chs=480x480&range=0-10&marks=4.33,5.0,5.67&dotMarkers=3.85,6.15&"
                + "cht=xy&domainMarks=4.33,5.0,5.67&dotDomainMarkers=3.85,6.15&" + "markRange=3.85-6.15&data=%f:%f";
        // image2Url = String.format(hrefFmt,eVal.intValue(),nVal.intValue());
        image2Url = String.format(hrefFmt, eVal.floatValue(), nVal.floatValue());
        isImage2Display = 1;
        page.put("image2", image2Url);
        page.put("isImage2Display", 1);

    }

    private List<Score> getPf16DimList() {
        OrderValueMap<String, DimensionBlock> map = questionnaire.getDimensionMap();
        List<Score> scoreList = new ArrayList<Score>();
        List<DimensionBlock> dimList = new ArrayList<DimensionBlock>();
        try {
            Score score = new Score();
            score.setRawScore(map.get("W1").getRawScore());
            scoreList.add(0, score);
            score = new Score();
            score.setRawScore(map.get("W2").getRawScore());
            scoreList.add(1, score);
            score = new Score();
            score.setRawScore(map.get("W3").getRawScore());
            scoreList.add(2, score);
            score = new Score();
            score.setRawScore(map.get("W4").getRawScore());
            scoreList.add(3, score);
            score = new Score();
            score.setRawScore(map.get("W5").getRawScore());
            scoreList.add(4, score);
            score = new Score();
            score.setRawScore(map.get("W6").getRawScore());
            scoreList.add(5, score);
            score = new Score();
            score.setRawScore(map.get("W7").getRawScore());
            scoreList.add(6, score);
            score = new Score();
            score.setRawScore(map.get("W8").getRawScore());
            scoreList.add(7, score);
            score = new Score();
            score.setRawScore(map.get("W9").getRawScore());
            scoreList.add(8, score);
            score = new Score();
            score.setRawScore(map.get("W10").getRawScore());
            scoreList.add(9, score);
            score = new Score();
            score.setRawScore(map.get("W11").getRawScore());
            scoreList.add(10, score);
            score = new Score();
            score.setRawScore(map.get("W12").getRawScore());
            scoreList.add(11, score);
            score = new Score();
            score.setRawScore(map.get("W13").getRawScore());
            scoreList.add(12, score);
            score = new Score();
            score.setRawScore(map.get("W14").getRawScore());
            scoreList.add(13, score);
            score = new Score();
            score.setRawScore(map.get("W15").getRawScore());
            scoreList.add(14, score);
            score = new Score();
            score.setRawScore(map.get("W16").getRawScore());
            scoreList.add(15, score);

        } catch (Exception e) {

        }
        return scoreList;

    }

    /*
     * protected void buildPf16ModelsImpl(Page page) { //构造维度解释列表
     * buildScaleDimScoreList(); List<ScaleDimExplain> tmp = new
     * ArrayList<ScaleDimExplain>(sdimlist.size()); List<ScaleDimExplain>
     * cyDims1 = new ArrayList<ScaleDimExplain>(); List<ScaleDimExplain> cyDims2
     * = new ArrayList<ScaleDimExplain>(); int idx = 0; for (; idx < 16; ++idx)
     * { ScaleDimExplain dimExplain = sdimlist.get(idx); tmp.add(dimExplain); }
     * //拿出一级次元维度 for (;idx < 20 ;++idx ) { ScaleDimExplain dimExplain =
     * sdimlist.get(idx); cyDims1.add(dimExplain); } //找出二级次元维度 List<DimDetail>
     * list = getDimDetails(); for (;idx < list.size();++idx){ DimDetail detail
     * = list.get(idx); Number rowScore = detail.getRawScore(); Number stdScore
     * = (Number) detail.getDimFinalScore(); ScaleDimExplain dimEpl = new
     * ScaleDimExplain(); dimEpl.setDimId(detail.getId());
     * dimEpl.setDimTitle(detail.getTitle());
     * dimEpl.setRawScore(rowScore.doubleValue());
     * dimEpl.setStdScore(stdScore.doubleValue()); cyDims2.add(dimEpl); }
     * sdimlist.clear(); sdimlist = null; sdimlist = tmp;
     * 
     * //tmp.clear(); //tmp = null;
     * 
     * page.put(SDIMLIST, sdimlist); page.put("cyDims1", cyDims1);
     * page.put("cyDims2", cyDims2);
     * 
     * }
     */
    protected String getImgHerfQry(String scaleId) {
        String s = null;
        if (scaleId.equals("310011116"))
            s = "chs=600x400&range=0.0-100.0&marks=50.0,70.0";
        if (scaleId.equals("13") || scaleId.equals("14"))
            s = "chs=600x400&range=0-10&marks=4.33,5.0,5.67&dotMarkers=3.85,6.15";
        if (scaleId.equals("6"))
            s = "chs=600x400&cht=br&labp=90";
        if (scaleId.equals("12"))
            s = "bean=y&chs=600x250&cht=br";
        // if(scaleId == "5")
        // s = "bean=edutec.scale.descriptor.Mbti5ChartBuilder&amp;chs=500x250";
        if (scaleId.equals("903"))
            s = "bean=y&chs=600x250&cht=br";
        if (scaleId.equals("10"))
            s = "chs=280x648&amp;outlineVisible=f&ort=h&amp&range=0.5-10.5&marks=4.5,6.5&domainAxisLocation=b_r&dataLen=16&axisX=f&axisY=f";
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return s;
    }

    protected String bulidScaleImgHerfQry(String imgHerfQry) {
        StringBuilder sb = new StringBuilder(imgHerfQry);
        sb.append(UtilCollection.AMP_CHAR);
        sb.append(buildImgUrlData());
        return StringEscapeUtils.escapeHtml(sb.toString());

    }

    protected String buildImgUrlData() {
        DatasetBean bean = new DatasetBean();
        for (DimDetail dimDetail : dimDetailList) {
            bean.putToDataSet(dimDetail.getTitle(), dimDetail.getTScore());
        }
        return bean.dataToUrlStr();
    }

    protected JChartParam buildScaleImgChartParam(String imgHerfQry) {
        JChartParam chartParam = createChartParam(imgHerfQry);
        if (chartParam == null) {
            chartParam = this.getChartParam();
        }
        if (chartParam == null) {
            return null;
        }

        for (DimDetail dimDetail : dimDetailList) {
            chartParam.putToDataSet(dimDetail.getTitle(), dimDetail.getTScore().doubleValue());
        }

        return chartParam;
    }

    protected String buildMBTIScaleImgURL(String imgHerfQry) {
        DatasetBean bean = new DatasetBean();
        for (DimDetail dimDetail : dimDetailList) {
            Number rawScore = dimDetail.getRawScore();
            bean.putToDataSet(dimDetail.getId(), rawScore.intValue() - 3.5);
        }
        String dataurl = bean.dataToUrlStr();
        String imageurl = imgHerfQry + dataurl;
        return imageurl;
    }

    public JChartParam createChartParam(String imgHerfQry) {
        if (StringUtils.isEmpty(imgHerfQry)) {
            return null;
        }
        return new JChartParam().parseString(imgHerfQry);
    }

    @Override
    public void setSubjectUser(Object user) {
        // TODO Auto-generated method stub

    }

    private DimDetail getDimDetail(DimensionBlock dim) {
        for (DimDetail dimDetail : dimDetailList) {
            if (dimDetail.getId() == dim.getId())
                return dimDetail;
        }
        return null;

    }
}
