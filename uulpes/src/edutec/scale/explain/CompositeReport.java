package edutec.scale.explain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.math.util.MathUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.NationUtil;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.descriptor.ScaleReportDescriptor;
import edutec.scale.descriptor.ScaleReportEffect;
//import edutec.scale.exam.ExamQuery;
import edutec.scale.exam.ExamResult;
import edutec.scale.exam.ExamScoreGrade;
import edutec.scale.exam.ExamWarning;
import edutec.scale.questionnaire.DimDetail;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.questionnaire.QuestionnaireReportListener;
import edutec.scale.util.ScaleUtils;
import freemarker.template.TemplateException;
import heracles.jfree.JChartCreator;
import heracles.jfree.JChartParam;
import heracles.util.Pools;
import heracles.util.SimpleCodec;
import heracles.util.SimpleDateFormat;
import heracles.util.TreeNode;
import heracles.web.util.HtmlTable;
import sun.misc.BASE64Encoder;

@Scope("prototype")
@Component("compositeReport")
public class CompositeReport {
    // @Autowired
    protected QuestionnaireReportListener questionnaireListener;
    @Autowired
    protected ExamResultMapper examResultMapper;

    public Map<String, Object> getRootMap() {
        return rootMap;
    }

    protected Object subjectUser; // 被查看的人
    protected Object observerUser; // 查看报告的人
    protected String starttime;
    protected String endtime;
    protected Map<String, ExamResult> examrsMap; // 保存测试结果
    protected Map<String, List> examrsListMap; // 保存测试结果
    protected Map<Integer, String[]> idsMap; // 保存各组的量表id号
    protected Map<String, ScaleReportDescriptor> dtorMap; // 保存问卷描述对象
    protected Map<String, Object> rootMap;
    protected Map<String, String> groupTitle; // 保存报告标题,以显示在页面上，形成herf
    protected String stulev;
    public static final String RANK_DESC[] = { "得分非常低", "得分比较低", "中等", "得分比较高", "得分非常高" };
    String scaleIds[] = null;
    static String TITLE = "个体复合测评报告";
    static String[] headTitleArray = { "原始得分", "T分", "标准分" };
    String reportTile = null;
    protected Collection<ScaleReportDescriptor> orderedRandDescnList;

    // allRankDescOfScale和allExamResults是用来记录所有的
    protected MultiValueMap allRankDescMap; // 保存所有的量表RankDescriptor
    protected List<ExamResult> allExamResults; // 保存所有的测试结果
    // //
    protected Map<Object, Object> advicMap; // 放发展建议
    protected Map<Object, Object> detalMap; // 放详细解释
    protected StrBuilder sb;

    protected Map<String, Object> dataMap;// 所有报告数据

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public CompositeReport(Object subjectUser, Object observerUser) {
        this.subjectUser = subjectUser;
        this.observerUser = observerUser;
        String[] partTitle = { "小学", "初中", "高中" };

        stulev = partTitle[0];// 这里获得年级名称，暂时写死
    }

    public CompositeReport(Object subjectUser, Object observerUser, String starttime, String endtime) {
        this.subjectUser = subjectUser;
        this.observerUser = observerUser;
        this.starttime = starttime;
        this.endtime = endtime;
        String[] partTitle = { "小学", "初中", "高中" };

        stulev = partTitle[0];// 这里获得年级名称，暂时写死
    }

    public void setExamResultMapper(ExamResultMapper erm) {
        this.examResultMapper = erm;
    }

    public String getGroupTitle() {
        bulidReportTile();
        return reportTile;
    }

    public void getData(Map dataMap) {
        if (subjectUser instanceof Student) {
            Student stu = (Student) subjectUser;
            dataMap.put("xm", stu.getXm());
            String xb = stu.getXbm().equals("1") ? "男" : "女";
            dataMap.put("xb", xb);
            dataMap.put("xh", stu.getXh());
            dataMap.put("sfzjh", stu.getSfzjh());
            dataMap.put("mz", NationUtil.getMz(stu.getMzm()));
            dataMap.put("xmpy", stu.getXmpy());
            dataMap.put("njmc", stu.getNjmc());
            dataMap.put("bjmc", stu.getBjmch());
            dataMap.put("age", AgeUitl.getAgeByBirthdayStr(stu.getCsrq()));
            dataMap.put("xxmc", stu.getXxmc());

        }

        else if (subjectUser instanceof Teacher) {
            Teacher teacher = (Teacher) subjectUser;
            dataMap.put("xm", teacher.getXm());
            String xb = teacher.getXbm().equals("1") ? "男" : "女";
            dataMap.put("xb", xb);
            dataMap.put("xh", teacher.getGh());
            dataMap.put("sfzjh", teacher.getSfzjh());
            dataMap.put("mz", teacher.getMzm());
            dataMap.put("xmpy", teacher.getXmpy());
            dataMap.put("rolename", teacher.getRolename());
            dataMap.put("age", AgeUitl.getAgeByBirthdayStr(teacher.getCsrq()));
            dataMap.put("xxmc", teacher.getXxmc());
        }
    }

    /**
     * 写出报告
     * 
     * @param tag
     *            报告类型 {@ScaleGroup}中定义的5种类型
     * @param tml
     *            报告模板
     * @param writer
     *            报告书写器
     * @throws IOException
     * @throws TemplateException
     */

    public void writeReportToHtml(String tag) {
        // bulidGroupType();
        // 加载测试结果记录
        loadExamrs();
        if (MapUtils.isEmpty(examrsListMap)) {
            return;
        }
        // String scaleIds[] = null;
        scaleIds = examrsListMap.keySet().toArray(new String[0]);

        try {
            detalMap = Pools.getInstance().borrowMap();
            detalMap = new HashMap<Object, Object>();
            advicMap = new HashMap<Object, Object>();
            rootMap = new HashMap<String, Object>();
            // 设置个人信息
            setSubjectUserInfo(rootMap);
            String userName = "";
            if (subjectUser instanceof Student) {
                Student stu = (Student) subjectUser;
                userName = stu.getXm();
            }

            else if (subjectUser instanceof Teacher) {
                Teacher teacher = (Teacher) subjectUser;
                userName = teacher.getXm();
            }
            rootMap.put("report_title", userName + TITLE);
            makeupTable(scaleIds);
            // makeupExplain();
        } finally {

        }
    }

    public void exportReport(boolean isDownload) throws Exception {

        // 加载测试结果记录
        loadExamrs();
        if (MapUtils.isEmpty(examrsListMap)) {
            return;
        }
        // String scaleIds[] = null;
        scaleIds = examrsListMap.keySet().toArray(new String[0]);

        try {
            // detalMap = Pools.getInstance().borrowMap();
            // = new HashMap<Object,Object>();
            // advicMap = new HashMap<Object,Object>();
            dataMap = new HashMap<String, Object>();
            getData(dataMap);
            // 设置个人信息
            // setSubjectUserInfo(dataMap);
            String userName = "";
            if (subjectUser instanceof Student) {
                Student stu = (Student) subjectUser;
                userName = stu.getXm();
            }

            else if (subjectUser instanceof Teacher) {
                Teacher teacher = (Teacher) subjectUser;
                userName = teacher.getXm();
            }
            // rootMap.put("report_title", userName+TITLE);
            makeupData(scaleIds, isDownload);
            // makeupExplain();
        } finally {

        }
    }

    /**
     * 为每一个量表生成一个表格
     * 
     * @param scaleIds
     */
    @SuppressWarnings("unchecked")
    private void makeupTable(String[] scaleIds) {
        List<HtmlTable> tables = new ArrayList<HtmlTable>(scaleIds.length);

        for (int i = 0; i < scaleIds.length; i++) {
            List erList = examrsListMap.get(scaleIds[i]);
            buildTable(erList, tables);
        }

        rootMap.put("tables", tables);

    }

    protected void makeupData(String[] scaleIds, boolean isDownload) throws Exception {
        List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>(scaleIds.length);

        for (int i = 0; i < scaleIds.length; i++) {
            List erList = examrsListMap.get(scaleIds[i]);
            buildScaleResult(erList, resultlist, isDownload);
        }

        dataMap.put("resultlist", resultlist);

    }

    public void buildScaleResult(List erList, List<Map<String, Object>> resultlist, boolean isDownload)
            throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();

        StrBuilder sb = null;
        if (erList.size() > 1) {
            sb = new StrBuilder();
            sb.append("cht=mln&chs=800x300&mdata=");
        }

        for (int i = 0; i < erList.size(); i++) {
            ExamResult er = (ExamResult) erList.get(i);
            ScaleReportDescriptor desc = buildDescriptor(er);
            if (desc == null)
                continue;
            ScaleReportDescriptor descriptor = desc;
            List<DimDetail> dims = descriptor.getDimDetails();
            if (i == 0) {
                int typeFlag;
                if (observerUser instanceof Student)
                    typeFlag = 1;
                else if (observerUser instanceof Teacher)
                    typeFlag = 2;
                else
                    typeFlag = 3;
                ReportArticle article = new ReportArticle(descriptor.getQuestionnaire(), dims, typeFlag);
                article.buildCompositeReportComment();
                Map<String, StrBuilder> dimDescnMap = article.getSimpleDimDesciptor();
                String summarize = article.getSummarize();

                String scaleTitle = descriptor.getQuestionnaire().getTitle();
                result.put("scaletitle", scaleTitle);
                Timestamp starttime = er.getStartTime();
                Timestamp endtime = er.getOkTime();
                String testtime = SimpleDateFormat.formatZH1(starttime) + "－" + SimpleDateFormat.formatZH2(endtime);
                result.put("testtime", testtime);

                // if(erList.size()>1){
                result.put("testnum", erList.size());// 测试次数
                // }
                List scoretable = new ArrayList();// 得分表
                dims = descriptor.getDimDetails();// descriptor.getOrderDims();
                for (DimDetail detail : dims) {
                    Map dimMap = new HashMap();
                    dimMap.put("dimtitle", detail.getTitle());
                    dimMap.put("rawscore", MathUtils.round(detail.getRawScore().doubleValue(), 2));
                    dimMap.put("tscore", MathUtils.round(detail.getTScore().doubleValue(), 2));
                    // dimMap.put("scorerank",
                    // RANK_DESC[detail.getDimBlk().getRank() - 1]);
                    scoretable.add(dimMap);
                }
                result.put("scoretable", scoretable);

                StrBuilder scaleDesc = new StrBuilder();
                Iterator<Map.Entry<String, StrBuilder>> it = dimDescnMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, StrBuilder> ent = it.next();
                    if (isDownload)
                        scaleDesc.append(ent.getValue() + "<w:br/>");
                    else
                        scaleDesc.append(ent.getValue() + "<br>");

                }
                result.put("scaledesc", scaleDesc.toString());
                result.put("summarize", summarize);
            }
            if (erList.size() > 1) {
                List<DimensionBlock> listLev1 = descriptor.getQuestionnaire().getRootDimensionBlocks();
                for (DimensionBlock dimBlk : listLev1) {
                    String title = dimBlk.getTitle();
                    title = SimpleCodec.enhex(title);
                    double value = MathUtils.round(dimBlk.getTScore().doubleValue(), 2);
                    String testNum = "第" + (i + 1) + "次";
                    sb.append(SimpleCodec.enhex(testNum)).append(":").append(title).append(":").append(value)
                            .append(";");
                }
            }
        }
        if (erList.size() > 1) {
            if (isDownload) {
                JChartCreator chartCreator = new JChartCreator();
                JChartParam chartParam = new JChartParam().parseString(sb.toString());
                chartCreator.setChartParam(chartParam);
                JFreeChart chart = chartCreator.getChart();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ChartUtilities.writeChartAsJPEG(outputStream, chart, 800, 300);
                BASE64Encoder encoder = new BASE64Encoder();
                String imageData = encoder.encode(outputStream.toByteArray());
                result.put("image", imageData);
            } else {
                sb.setLength(sb.length() - 1);
                sb.append("&cht=mln&chs=800x300");
                result.put("imageUrl", sb.toString());
            }
        }

        resultlist.add(result);
    }

    private void buildTable(List erList, List<HtmlTable> tables) {
        Map<String, Map<String, Double>> linearData = new HashMap<String, Map<String, Double>>();
        HtmlTable table = new HtmlTable();
        table.setTableStyle("width='90%' border='0' cellspacing='1' " + "cellpadding='0' class='table_style'");
        StrBuilder sb = null;
        if (erList.size() > 1) {
            sb = new StrBuilder();
            sb.append("reportchart.do?");
            sb.append("mdata=");
        }
        TreeNode graphNode = null;
        for (int i = 0; i < erList.size(); i++) {
            ExamResult er = (ExamResult) erList.get(i);
            ScaleReportDescriptor desc = buildDescriptor(er);
            ScaleReportDescriptor descriptor = desc;
            List<DimDetail> dims = descriptor.getDimDetails();

            if (i == 0) {
                int typeFlag;
                if (observerUser instanceof Student)
                    typeFlag = 1;
                else if (observerUser instanceof Teacher)
                    typeFlag = 2;
                else
                    typeFlag = 3;
                ReportArticle article = new ReportArticle(descriptor.getQuestionnaire(), dims, typeFlag);
                article.content(false);
                Map<String, StrBuilder> dimDescnMap = article.getDetailOfDims();

                TreeNode scaleTitleNode = new TreeNode(descriptor.getQuestionnaire().getTitle());
                Timestamp timestamp = descriptor.getQuestionnaire().getOkTime();
                TreeNode timeNode = new TreeNode("    测评时间：" + SimpleDateFormat.formatDateTime(timestamp));
                TreeNode scaleScoreNode = new TreeNode();
                TreeNode descNode = new TreeNode();

                table.addNode(scaleTitleNode);
                table.addNode(timeNode);
                table.addNode(scaleScoreNode);
                if (erList.size() > 1 && i == 0) {
                    graphNode = new TreeNode();
                    table.addNode(graphNode);
                }
                table.addNode(descNode);

                HtmlTable scoreTable = new HtmlTable();

                scoreTable.setTableStyle("border='0' cellspacing='1' " + "cellpadding='0' class='table_style'");
                // strs.clear();

                scoreTable.addHeadFactorTitiles(new String[] { "维度" });
                scoreTable.addValColTitles(headTitleArray);
                scoreTable.setTdStyle("width='120pt' align='center'");
                scoreTable.setColHeadStyle("width='120pt' align='center'");

                dims = descriptor.getDimDetails();// descriptor.getOrderDims();
                for (DimDetail detail : dims) {
                    TreeNode node = new TreeNode(detail.getTitle());
                    node.addValue(MathUtils.round(detail.getRawScore().doubleValue(), 2));
                    node.addValue(MathUtils.round(detail.getTScore().doubleValue(), 2));
                    // node.addValue(RANK_DESC[detail.getDimBlk().getRank() -
                    // 1]);

                    scoreTable.addNode(node);
                }

                StrBuilder scaleDesc = new StrBuilder();
                Iterator<Map.Entry<String, StrBuilder>> it = dimDescnMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, StrBuilder> ent = it.next();
                    scaleDesc.append(ent.getKey() + ": " + ent.getValue() + "<br>");

                }
                descNode.addValue(scaleDesc.toString());

                scoreTable.setTableStyle("align='center'");
                scaleScoreNode.addValue(scoreTable);
                tables.add(table);
            }
            if (erList.size() > 1) {
                List<DimensionBlock> listLev1 = descriptor.getQuestionnaire().getRootDimensionBlocks();
                for (DimensionBlock dimBlk : listLev1) {
                    String title = dimBlk.getTitle();
                    title = SimpleCodec.enhex(title);
                    double value = MathUtils.round(dimBlk.getTScore().doubleValue(), 2);
                    String testNum = "第" + (i + 1) + "次";
                    sb.append(SimpleCodec.enhex(testNum)).append(":").append(title).append(":").append(value)
                            .append(";");
                }
            }

        }
        if (erList.size() > 1) {
            sb.setLength(sb.length() - 1);
            sb.append("&cht=mln&chs=800x300");
            String imageContent = "<p><div align=\"center\"><img src=\"" + sb.toString()
                    + "\" width=\"600\" height=\"400\"/></div>";
            graphNode.addValue(imageContent);
        }
    }

    // 制作总评
    private void makeupExplain() {
        for (ScaleReportDescriptor descriptor : orderedRandDescnList) {
            List<DimDetail> dimDetailList = descriptor.getDimDetails();
            // 根据量表及其维度和所看报告的对象，提取words
            int typeFlag;
            if (observerUser instanceof Student)
                typeFlag = 1;
            else if (observerUser instanceof Teacher)
                typeFlag = 2;
            else
                typeFlag = 3;

            ReportArticle article = new ReportArticle(descriptor.getQuestionnaire(), dimDetailList, typeFlag);
            article.content(false);
            Map<String, StrBuilder> dimDescnMap = article.getDetailOfDims();
            // sb.append(article.getSummarize());
            // detalMap.put(titl, article.detailOfDimsToString());
            // advicMap.put(titl, article.getAdvice());
        }
        // rootMap.put("summarize", sb);
        // rootMap.put("detalMap", detalMap);
        // rootMap.put("advicMap", advicMap);

    }

    // 设置答题人个人信息
    public void setSubjectUserInfo(Map<String, Object> page) {
        Map<String, String> userinfo = new HashMap<String, String>();
        if (subjectUser instanceof Student) {
            Student stu = (Student) subjectUser;
            userinfo.put("xxmc", stu.getXxmc());
            userinfo.put("xm", stu.getXm());
            userinfo.put("sfzjh", stu.getSfzjh());
            // String nj = AgeUitl.getGradeName(stu.getGradecodeid());
            long classid = stu.getBjid();

            // ClassSchool classSchool =
            // classService.selectByPrimaryKey(Integer.valueOf((int)classid));
            userinfo.put("njmc", stu.getNjmc());
            userinfo.put("bjmc", stu.getBjmch());
            userinfo.put("xm", stu.getXm());
            userinfo.put("xh", stu.getXh());
            userinfo.put("crsq", stu.getCsrq());
            String xb = stu.getXbm() == "1" ? "男" : "女";
            userinfo.put("xb", xb);
            userinfo.put("xmpy", stu.getXmpy());
            userinfo.put("mz", stu.getMzm());
            userinfo.put("age", String.valueOf(AgeUitl.getAgeByBirthdayStr(stu.getCsrq())));
            page.put("userinfo", userinfo); // 个人信息
        }
        if (subjectUser instanceof Teacher)
            page.put("userinfo", (Teacher) subjectUser); // 个人信息
        if (subjectUser instanceof Parent)
            page.put("userinfo", (Parent) subjectUser); // 个人信息
    }

    /**
     * 构建每个量表的RankDescriptor对象，其对象是用来计算量表分数用的,
     * 分数的计算见RankDescriptor的方法getOrderRank（）；
     * 
     * @param scaleIds
     */
    private void buildRankDescriptor(String[] scaleIds) {
        try {
            dtorMap = new HashMap<String, ScaleReportDescriptor>();
            for (String sid : scaleIds) {
                List erList = examrsListMap.get(sid);
                ExamResult er = (ExamResult) erList.get(erList.size() - 1);
                Questionnaire qaire = er.toNewQuestionnaire(questionnaireListener);
                if ((qaire.getFlag() & ScaleUtils.SCALE_PROPER_FLAG) == 0) {
                    continue;
                }
                ScaleReportDescriptor reportDescription = new ScaleReportDescriptor();
                reportDescription.setQuestionnaire(qaire);
                // reportDescription.setResultId(resultId);//用于三角视获取教师、家长测试结果
                qaire.setDescriptor(reportDescription);
                reportDescription.setObserverUserInfo(observerUser);
                reportDescription.setSubjectUserInfo(subjectUser);

                reportDescription.evaluateDimRank(null);
                dtorMap.put(qaire.getScaleId(), reportDescription);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ScaleReportDescriptor buildDescriptor(ExamResult er) {
        Questionnaire qaire = er.toNewQuestionnaire(questionnaireListener);
        if (qaire == null)
            return null;
        ScaleReportDescriptor reportDescription = new ScaleReportDescriptor();
        ExamWarning examWarning = (ExamWarning) SpringContextHolder.getBean("ExamWarning", ExamWarning.class);
        ExamScoreGrade examScoreGrade = (ExamScoreGrade) SpringContextHolder.getBean("ExamScoreGrade",
                ExamScoreGrade.class);
        ScaleReportEffect scaleReportEffect = (ScaleReportEffect) SpringContextHolder.getBean("ScaleReportEffect",
                ScaleReportEffect.class);
        reportDescription.setQuestionnaire(qaire);
        reportDescription.setExamWarning(examWarning);
        reportDescription.setExamScoreGrade(examScoreGrade);
        reportDescription.setScaleReportEffect(scaleReportEffect);
        // reportDescription.setResultId(resultId);//用于三角视获取教师、家长测试结果
        qaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(observerUser);
        reportDescription.setSubjectUserInfo(subjectUser);

        reportDescription.evaluateDimRank(null);
        return reportDescription;
    }

    /**
     * 构建所有的测试过的量表
     * 
     * @param scaleIds
     */
    private void buildAllRankDescriptor(String[] scaleIds) {
        allRankDescMap = new MultiValueMap();
        for (ExamResult er : allExamResults) {
            boolean isFind = ArrayUtils.contains(scaleIds, er.getScaleId() + "");
            if (isFind) {
                Questionnaire qaire = er.toNewQuestionnaire(questionnaireListener);
                if ((qaire.getFlag() & ScaleUtils.SCALE_PROPER_FLAG) == 0) {
                    continue;
                }
                ScaleReportDescriptor reportDescription = new ScaleReportDescriptor();
                reportDescription.setQuestionnaire(qaire);
                // reportDescription.setResultId(resultId);//用于三角视获取教师、家长测试结果
                qaire.setDescriptor(reportDescription);
                reportDescription.setObserverUserInfo(observerUser);
                reportDescription.setSubjectUserInfo(subjectUser);

                reportDescription.evaluateDimRank(null);
                allRankDescMap.put(er.getScaleId() + "", reportDescription);
            }
        }
    }

    /**
     * 从数据库中載入学生的测试结果数据
     */
    public void loadExamrs() {
        if (MapUtils.isNotEmpty(examrsListMap)) {
            return;
        }
        questionnaireListener = SpringContextHolder.getBean("QuestionnaireReportListener",
                QuestionnaireReportListener.class);
        long subjectuserid = -1;
        if (subjectUser instanceof Student) {
            Student stu = (Student) subjectUser;
            subjectuserid = stu.getId();
            allExamResults = examResultMapper.getExamResultListForStu(subjectuserid, starttime, endtime);
        }
        if (subjectUser instanceof Teacher) {
            Teacher tea = (Teacher) subjectUser;
            subjectuserid = tea.getId();
            allExamResults = examResultMapper.getExamResultListForTeacher(subjectuserid, starttime, endtime);
        }
        questionnaireListener.setSubjectUserInfo(subjectUser);
        questionnaireListener.setObserverUserInfo(observerUser);
        // allExamResults =
        // examResultMapper.getExamResultListForStu(subjectuserid);
        examrsListMap = new HashMap<String, List>(allExamResults.size());
        int scaleId = 0;
        // sql语句已经排序
        for (ExamResult mr : allExamResults) {
            // if ((mr.getScale().getFlag() & ScaleUtils.SCALE_PROPER_FLAG) ==
            // 0)
            // continue;
            int sid = mr.getScaleId();
            // if (sid != scaleId) {
            // examrsMap.put(sid + "", mr);
            // scaleId = sid;
            // }
            List resultsList = examrsListMap.get(sid + "");
            if (resultsList == null) {
                resultsList = new ArrayList();
                resultsList.add(mr);
                examrsListMap.put(sid + "", resultsList);
            } else {
                resultsList.add(mr);
            }
        }
    }

    /**
     * 构建完全心检报告的类型,是根据学生所做过的量表来构建的 groupTitle存放构建结果
     */
    public void bulidReportTile() {
        if (scaleIds != null) {
            return;
        }
        loadExamrs();
        if (MapUtils.isEmpty(examrsListMap)) {
            return;
        }
        String userName = "";
        if (subjectUser instanceof Student) {
            Student stu = (Student) subjectUser;
            userName = stu.getXm();
        }

        else if (subjectUser instanceof Teacher) {
            Teacher teacher = (Teacher) subjectUser;
            userName = teacher.getXm();
        }
        reportTile = userName + TITLE;

    }

    private class TimeSortClass implements Comparator<ExamResult> {
        public int compare(ExamResult arg0, ExamResult arg1) {
            ExamResult er1 = (ExamResult) arg0;
            ExamResult er2 = (ExamResult) arg1;
            int flag = er1.getOkTime().compareTo(er1.getOkTime());
            return flag;
        }
    }

    public static void main(String[] args) throws IOException {
        CategoryDataset data = createDataset();
        JFreeChart chart = createChart(data);
        // BufferedImage image1 = chart.createBufferedImage(800, 300);
        // return image1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream("D:\\piefruit.jpg");
            ChartUtilities.writeChartAsJPEG(fos_jpg, 0.9f, chart, 800, 300);// 0.9是图像的品质：0.0-1.0f
        } catch (Exception e) {

        } finally {
            try {
                fos_jpg.close();
            } catch (Exception e) {
            }
        }

    }

    private JFreeChart createChart() {
        JFreeChart freeChart;
        DefaultCategoryDataset linedataset = (DefaultCategoryDataset) createDataset();

        freeChart = ChartFactory.createLineChart("", "", "", linedataset, PlotOrientation.HORIZONTAL, false, false,
                false);

        // setChartBaseStyle();
        // 设置线型图的线样式
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) ((CategoryPlot) freeChart.getPlot())
                .getRenderer();
        lineandshaperenderer.setBaseShapesVisible(true);
        lineandshaperenderer.setBaseLinesVisible(true);
        lineandshaperenderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-2D, -2D, 5D, 5D));
        lineandshaperenderer.setSeriesPaint(0, Color.BLACK);// 折线的颜色*/
        return freeChart;
    }

    private static CategoryDataset createDataset() {
        String series1 = "第一";
        String series2 = "第二";
        String series3 = "第三";
        String type1 = "类型1";
        String type2 = "类型 2";
        String type3 = "类型 3";
        String type4 = "类型 4";
        String type5 = "类型 5";
        String type6 = "类型 6";
        String type7 = "类型 7";
        String type8 = "类型 8";
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        defaultcategorydataset.addValue(1.0D, series1, type1);
        defaultcategorydataset.addValue(4D, series1, type2);
        defaultcategorydataset.addValue(3D, series1, type3);
        defaultcategorydataset.addValue(5D, series1, type4);
        defaultcategorydataset.addValue(5D, series1, type5);
        defaultcategorydataset.addValue(7D, series1, type6);
        defaultcategorydataset.addValue(7D, series1, type7);
        defaultcategorydataset.addValue(8D, series1, type8);
        defaultcategorydataset.addValue(5D, series2, type1);
        defaultcategorydataset.addValue(7D, series2, type2);
        defaultcategorydataset.addValue(6D, series2, type3);
        defaultcategorydataset.addValue(8D, series2, type4);
        defaultcategorydataset.addValue(4D, series2, type5);
        defaultcategorydataset.addValue(4D, series2, type6);
        defaultcategorydataset.addValue(2D, series2, type7);
        defaultcategorydataset.addValue(1.0D, series2, type8);
        defaultcategorydataset.addValue(4D, series3, type1);
        defaultcategorydataset.addValue(3D, series3, type2);
        defaultcategorydataset.addValue(2D, series3, type3);
        defaultcategorydataset.addValue(3D, series3, type4);
        defaultcategorydataset.addValue(6D, series3, type5);
        defaultcategorydataset.addValue(3D, series3, type6);
        defaultcategorydataset.addValue(4D, series3, type7);
        defaultcategorydataset.addValue(3D, series3, type8);
        return defaultcategorydataset;

    }

    private static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createLineChart("折线图例", "类型", "数值", categorydataset,
                PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setRangeGridlinePaint(Color.white);
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setAutoRangeIncludesZero(true);
        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();
        lineandshaperenderer.setShapesVisible(true); // series 点（即数据点）可见
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1, 1.0F, new float[] { 10F, 6F }, 0.0F)); // 定义series为”First”的（即series1）点之间的连线
        // ，这里是虚线，默认是直线
        lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 1, 1.0F, new float[] { 6F, 6F }, 0.0F)); // 定义series为”Second”的（即series2）点之间的连线
        lineandshaperenderer.setSeriesStroke(2, new BasicStroke(2.0F, 1, 1, 1.0F, new float[] { 2.0F, 6F }, 0.0F)); // 定义series为”Third”的（即series3）点之间的连线
        // 一些重要的方法：
        lineandshaperenderer.setLinesVisible(true);// series 点（即数据点）间有连线可见
        // 一些重要的方法：（增加一块标记）
        IntervalMarker intervalmarker = new IntervalMarker(3.5D, 5.5D);// 上下限制
        intervalmarker.setLabel("目标值");// 目标名称
        intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));// 字体
        intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);// 标签的位置，左对齐
        // intervalmarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);//标签的位置，左对齐
        intervalmarker.setLabelTextAnchor(TextAnchor.CENTER_LEFT);// 整个背景的对齐方式
        // intervalmarker.setLabelTextAnchor(TextAnchor.BASELINE_LEFT);//整个背景的对齐方式
        intervalmarker.setPaint(new Color(222, 222, 255, 128));// 颜色
        categoryplot.addRangeMarker(intervalmarker, Layer.BACKGROUND);// 作为以前图片的背景
        // 解决中文乱码问题,共要处理这三部分
        // １、对标题
        Font font1 = new Font("SimSun", 10, 20); // 设定字体、类型、字号
        jfreechart.getTitle().setFont(font1); // 标题
        // ２、对图里面的汉字设定,也就是Ｐlot的设定
        Font font2 = new Font("SimSun", 10, 16); // 设定字体、类型、字号
        categoryplot.getDomainAxis().setLabelFont(font2);// 相当于横轴或理解为X轴
        categoryplot.getRangeAxis().setLabelFont(font2);// 相当于竖轴理解为Y轴
        // 3、下面的方块区域是 LegendTitle 对象
        Font font3 = new Font("SimSun", 10, 12); // 设定字体、类型、字号
        jfreechart.getLegend().setItemFont(font3);// 最下方
        // 这是处理Ｐlot里面的横轴，同理可以正理竖轴
        CategoryAxis categoryaxis = categoryplot.getDomainAxis(); // 横轴上的
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 45度倾斜,可以改成其他,默认是水平
        categoryaxis.setTickLabelFont(new Font("SansSerif", 10, 12));// 设定字体、类型、字号
        // categoryaxis.setTickLabelFont(new Font("SimSun", 10, 12));//
        // 设定字体、类型、字号，也是可以的
        return jfreechart;

    }

}
