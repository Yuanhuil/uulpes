package edutec.scale.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.ecs.html.TR;

//import edutec.scale.descriptor.DescnFile;
//import edutec.scale.descriptor.DescriptorBuilder;
import edutec.scale.exception.DimensionException;
import edutec.scale.util.ScaleUtils;
import edutec.scale.util.html.SimpleHtmlTable;
import heracles.util.OrderValueMap;
import heracles.util.UtilMisc;
import heracles.web.util.HtmlStr;

public class Scale extends TitleableSupport implements Serializable, Calculable {

    private static final long serialVersionUID = -7427655658294624457L;

    public static void main(String[] args) {
        Scale s = new Scale();
        s.setId("90");
        @SuppressWarnings("unused")
        Scale s1 = s.clone();
        // System.out.println(s1.getId());
    }

    private String id;
    private String code; // shibin新增编码，id用自增id，code记录原来的名字编码，例如112001
    private String shortname; // shibin新增量表简称
    private long examtime; // shibin新增测试时间
    private List suitegrades; // shibin新增量表适合的年级名称

    private String type; // 类型名称
    private String title;
    private String descn;
    private String officeDescn;
    private String guidance;
    private String testType; // 自评类型
    private String source; // 量表来源
    private String scaleType; // 量表类型
    private String applicablePerson; // 适用人群
    private String reportGraph; // 图表
    private String reportImageParam1;// 图表参数
    private String reportImageParam2;// 图表参数
    private boolean warningOrNot; // 是否预警

    // private Individual individual;
    private String questiontype;
    private String questiontype0;
    private String questiontype1;
    private String questiontype2;
    private String questiontype3;
    private String reversequestions;
    private OrderValueMap<String, Dimension> dimensionMap = new OrderValueMap<String, Dimension>();
    private OrderValueMap<String, Question> questionMap = new OrderValueMap<String, Question>();
    // private DescriptorBuilder descriptorBuilder;
    private int flag;
    private int countVisibleQuestion = 0;
    private int countPlaceholderQuestion = 0;
    private int category = 0; // 判别哪个种类的量表,由于有新的分类出现，强加的此字段
    private int starlevel; // 星级
    private int priority; // 推荐级别
    private String createdBy;
    private Date creationTime;
    private long orgId;
    private int questionNum;
    private String normGradeOrAgeFlag;
    private String showtitle;
    private int isOriginalFlag; // 原始量表标记

    public String getShowtitle() {
        return showtitle;
    }

    public void setShowtitle(String showtitle) {
        this.showtitle = showtitle;
    }

    public boolean isWarningOrNot() {
        return warningOrNot;
    }

    public void setWarningOrNot(boolean warningOrNot) {
        this.warningOrNot = warningOrNot;
    }

    public List getSuitegrades() {
        return suitegrades;
    }

    public void setSuitegrades(List suitegrades) {
        this.suitegrades = suitegrades;
    }

    public long getExamtime() {
        return examtime;
    }

    public void setExamtime(long examtime) {
        this.examtime = examtime;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public String getCreatedBy() {
        return StringUtils.trimToEmpty(createdBy);
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Scale() {
    }

    public String getNormGradeOrAgeFlag() {
        return normGradeOrAgeFlag;
    }

    public void setNormGradeOrAgeFlag(String normGradeOrAgeFlag) {
        this.normGradeOrAgeFlag = normGradeOrAgeFlag;
    }

    public Scale(String scaleId) {
        this.id = scaleId;
    }

    public void clear() {
        // individual = null;
        dimensionMap.clear();
        questionMap.clear();
        dimensionMap = null;
        questionMap = null;
    }

    /**
     * 组装维度关系
     * @throws DimensionException
     */
    public void mountDimensions() throws DimensionException {
        questionMap.optimizeListStorage();
        if (MapUtils.isNotEmpty(dimensionMap)) {
            dimensionMap.optimizeListStorage();
            for (Dimension dimension : dimensionMap.valueList()) {
                if ((!dimension.getTitle().equalsIgnoreCase("")) && (!dimension.getTitle().equalsIgnoreCase("总量表")))
                    dimension.mount();
            }
        }
    }

    // public void setIndividual(Individual individual) {
    // this.individual = individual;
    // this.individual.setScale(this);
    // }
    public void addDimension(Dimension dimension) {
        dimension.setScale(this);
        dimensionMap.put(dimension.getId(), dimension);
    }

    public void addQuestion(Question o) {
        o.setScale(this);
        questionMap.put(o.getId(), o);
        if (o.isVisible()) {
            countVisibleQuestion++;
        }
        if (o.isPlaceholder()) {
            countPlaceholderQuestion++;
        }
    }

    public Question findQuestion(String questionId) {
        String qid = questionId.startsWith("Q") ? questionId : "Q" + questionId;
        return questionMap.get(qid);
    }

    public Question findQuestion(int nquestionId) {
        return findQuestion(nquestionId + "");
    }

    public List<Question> getQuestions() {
        return questionMap.valueList();
    }

    public int getQuestionSize() {
        return getQuestions().size();
    }

    public int getDimensionSize() {
        return getDimensions().size();
    }

    public Dimension findDimension(String dimensionId) {
        return dimensionMap.get(dimensionId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Dimension> getDimensions() {
        return dimensionMap.valueList();
    }

    public List<Dimension> getRootDimensions() {
        List<Dimension> result = new ArrayList<Dimension>();
        for (Dimension dimension : getDimensions()) {
            if (dimension.isRoot()) {
                result.add(dimension);
            }
        }
        return result;
    }

    public String getDescn() {
        String descnStr = descn.replaceAll("\n", "<br>");
        descnStr = HtmlStr.decodeString(descnStr);
        return StringUtils.defaultIfEmpty(descnStr, StringUtils.EMPTY);
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getOfficeDescn() {
        String descnStr = descn.replaceAll("\r\n", "<w:br/>");
        descnStr = HtmlStr.decodeString(descnStr);
        descnStr = descnStr.replaceAll("<br>", "<w:br/>");
        return StringUtils.defaultIfEmpty(descnStr, StringUtils.EMPTY);
    }

    // public Individual getIndividual() {
    // if (individual == null)
    // return Individual.NULL_INDIVIDUAL;
    // return individual;
    // }
    public List<ScaleFlag> getFlagDescn() {
        return getFlagDescn(ScaleUtils.SCALE_FLAGS_DESC);
    }

    public List<ScaleFlag> getCustFlagDescn() {
        return getFlagDescn(ScaleUtils.SCALE_FLAGS_CUST_DESC);
    }

    private List<ScaleFlag> getFlagDescn(Map<String, String> map) {
        List<ScaleFlag> result = new ArrayList<ScaleFlag>(map.size());
        ;
        for (Map.Entry<String, String> ent : map.entrySet()) {
            int key = NumberUtils.toInt(ent.getKey());
            ScaleFlag scaleFlag = new ScaleFlag();
            scaleFlag.setFlag(ent.getKey());
            scaleFlag.setDescn(ent.getValue());
            if ((getFlag() & key) > 0) {
                scaleFlag.setSet(true);
            }
            result.add(scaleFlag);
        }
        return result;
    }

    /**
     * Return a short description of the total running time.
     */
    public String shortSummary() {
        return "量表：" + this.id + "'" + this.getTitle() + "'";
    }

    public String toXml(boolean encoding) {
        StrBuilder sb = new StrBuilder(1024 << 2);
        if (encoding) {
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        } else {
            sb.append("<?xml version=\"1.0\"?>\n");
        }
        sb.append("<scale>\n");
        // sb.append("<id>").append(this.getId()).append("</id>\n");
        sb.append("<id>").append(this.getCode()).append("</id>\n");
        sb.append("<title>").append(HtmlStr.htmlEncode(getTitle())).append("</title>\n");
        sb.append("<descn>").append(HtmlStr.htmlEncode(getDescn())).append("</descn>\n");
        sb.append("<guidance>").append(HtmlStr.htmlEncode(getGuidance())).append("</guidance>\n");
        sb.append("<testType>").append(HtmlStr.htmlEncode(getTestTypeXML())).append("</testType>\n");
        sb.append("<source>").append(HtmlStr.htmlEncode(getSourceXML())).append("</source>\n");
        sb.append("<scaleType>").append(HtmlStr.htmlEncode(getScaleTypeXML())).append("</scaleType>\n");
        sb.append("<warningOrNot>").append(HtmlStr.htmlEncode(getIsWarningXML())).append("</warningOrNot>\n");
        sb.append("<applicablePerson>").append(HtmlStr.htmlEncode(getApplicablePersonXML()))
                .append("</applicablePerson>\n");
        sb.append("<reportGraph>").append(HtmlStr.htmlEncode(getReportGraph())).append("</reportGraph>\n");
        if (StringUtils.isNotEmpty(reportImageParam1)) {
            sb.append("<reportImageParam1>").append(HtmlStr.htmlEncode(getReportImageParam1()))
                    .append("</reportImageParam1>\n");
        }
        if (StringUtils.isNotEmpty(reportImageParam2)) {
            sb.append("<reportImageParam2>").append(HtmlStr.htmlEncode(getReportImageParam2()))
                    .append("</reportImageParam2>\n");
        }
        sb.append("<dimensions>\n");
        for (Dimension dim : getDimensions()) {
            sb.append(dim.toXml());
            sb.append("\n");
        }
        sb.append("</dimensions>\n");
        sb.append("<questions");
        marshallQuestions();
        if (StringUtils.isNotEmpty(questiontype)) {
            sb.append(" questiontype=");
            sb.append(UtilMisc.quote(HtmlStr.htmlEncode(questiontype)));
        }
        sb.append(">\n");
        if (StringUtils.isNotEmpty(reversequestions)) {
            sb.append("\t<reversequestions>");
            sb.append(reversequestions);
            sb.append("</reversequestions>\n");
        }
        for (Question q : getQuestions()) {
            sb.append(q.toXml());
        }
        sb.append("</questions>\n");
        sb.append("</scale>");
        return sb.toString();
    }

    public String prettyPrint() {
        StringBuffer sb = new StringBuffer(shortSummary());
        sb.append('\n');
        sb.append("-----------------------------------------\n");
        sb.append("类型：").append(this.getType());
        sb.append('\n');
        sb.append("-----------------------------------------\n");
        sb.append("指导语：").append(this.getGuidance());
        sb.append('\n');
        sb.append("-----------------------------------------\n");
        sb.append("介绍：").append(this.getDescn());
        sb.append('\n');
        sb.append("-----------------------------------------\n");
        // if (this.getIndividual() != null) {
        // sb.append("个人问题数量：").append(this.getIndividual().getQuestions().size());
        // sb.append('\n');
        // }
        sb.append("-----------------------------------------\n");
        sb.append("题目数量：").append(this.getQuestions().size());
        sb.append('\n');
        sb.append("-----------------------------------------\n");
        sb.append("维度:");
        for (Dimension dimension : this.getDimensions()) {
            sb.append("\t").append(dimension.getTitle()).append('\n');
        }
        sb.append("问题:\n");
        for (Question q : this.getQuestions()) {
            sb.append(q.toString()).append('\n');
        }
        return sb.toString();
    }

    public String getGuidance() {
        // if ((getFlag() & ScaleUtils.SCALE_PROPER_FLAG) > 0 &&
        // StringUtils.isEmpty(guidance)) {
        // return ScaleBeanFactory.getGlobalProp(ScaleBeanFactory.GUIDANCE);
        // }
        guidance = StringUtils.defaultString(guidance);
        String guidanceStr = guidance.replaceAll("\n", "<br>");
        guidanceStr = HtmlStr.decodeString(guidanceStr);
        // guidanceStr =guidance.replaceAll("&nbsp;", " ");
        return StringUtils.defaultString(guidanceStr);
    }

    public String getTestTypeXML() {
        return StringUtils.defaultString(testType);
    }

    public String getSourceXML() {
        return StringUtils.defaultString(source);
    }

    public String getScaleTypeXML() {
        return StringUtils.defaultString(scaleType);
    }

    public String getApplicablePersonXML() {
        return StringUtils.defaultString(applicablePerson);
    }

    public String getReportGraphXML() {
        return StringUtils.defaultString(reportGraph);
    }

    public String getReportImageParam1XML() {
        return StringUtils.defaultString(reportImageParam1);
    }

    public String getIsWarningXML() {
        return StringUtils.defaultString(String.valueOf(warningOrNot));
    }

    // public String getGuidlang() {
    // String Guidlang = StringUtils.EMPTY;
    // if (StringUtils.startsWithIgnoreCase(guidance, "file://")) {
    // try {
    // DescnFile descnFile = new DescnFile();
    // Guidlang = descnFile.getString(guidance.substring("file://".length()));
    // } catch (IOException e) {
    //
    // }
    // } else if (StringUtils.startsWithIgnoreCase(guidance, "empty:")) {
    // Guidlang = guidance.substring("empty:".length());
    // } else {
    // return this.getGuidance();
    // }
    // return Guidlang;
    // }
    public void setGuidance(String guidance) {
        this.guidance = guidance = StringUtils.trimToEmpty(guidance);
        this.guidance = HtmlStr.decodeString(this.guidance);
        if (StringUtils.startsWithIgnoreCase(guidance, "none")) {
            this.guidance = StringUtils.EMPTY;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCalcExp() {
        return null;
    }

    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }

    public String getDataTablename() {
        return "data" + getId();
    }

    public String getTextTablename() {
        return "text" + getId();
    }

    public String getReportname() {
        return getId() + ".flt";
    }

    public void printReverQuestions() {
        for (Dimension d : this.getDimensions()) {
            // System.out.println(d.getId());
            // System.out.println("============================");
            for (Question q : d.getQuestionList()) {
                if (q.isReverse()) {
                    // System.out.println(q.getId());
                }
            }
            // System.out.println("============================");
        }
    }

    public void printQuestionsOfDim() {
        List<Dimension> list = getDimensions();
        for (Dimension d : list) {
            // System.out.println(d.getId());
            // System.out.println("============================");
            for (Question q : d.getQuestionList()) {
                if (q.isReverse()) {
                }
                // System.out.print("-");
                // System.out.println(q.getId());
            }
            // System.out.println("============================");
        }
    }

    public String getReversequestions() {
        return reversequestions;
    }

    public void setReversequestions(String reversequestions) {
        this.reversequestions = reversequestions;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getQuestiontype1() {
        return questiontype1;
    }

    public void setQuestiontype1(String questiontype1) {
        this.questiontype1 = questiontype1;
    }

    public String getQuestiontype2() {
        return questiontype2;
    }

    public void setQuestiontype2(String questiontype2) {
        this.questiontype2 = questiontype2;
    }

    public int getCountVisibleQuestion() {
        return countVisibleQuestion;
    }

    public int getCountPlaceholderQuestion() {
        return countPlaceholderQuestion;
    }

    /**
     * 返回必要的题目数,即答题人要回答的，系统要记录其分数的题目数
     * @return
     */
    public int getCountNeedQuestion() {
        return countVisibleQuestion - countPlaceholderQuestion;
    }

    public void marshallQuestions() {
        Set<String> set = new HashSet<String>();
        // StrBuilder revsb = new StrBuilder();
        for (Question q : getQuestions()) {
            if (q instanceof SelectionQuestion) {
                SelectionQuestion sq = (SelectionQuestion) q;
                String optkey = sq.optionsToString();
                set.add(optkey);
                /*
                 * if (sq.isReverse()) { revsb.append(sq.getId());
                 * revsb.append(","); }
                 */
            }
        }
        // revsb.setLength(revsb.length()-1);
        // setReversequestions(revsb.toString());
        if (set.size() == 1) {
            for (Question q : getQuestions()) {
                if (q instanceof SelectionQuestion) {
                    SelectionQuestion sq = (SelectionQuestion) q;
                    sq.setWriteOptXml(false);
                }
            }
            setQuestiontype(set.iterator().next());
        }
    }

    public String getQuestiontype3() {
        return questiontype3;
    }

    public void setQuestiontype3(String questiontype3) {
        this.questiontype3 = questiontype3;
    }

    public String getQuestiontype0() {
        return questiontype0;
    }

    public void setQuestiontype0(String questiontype0) {
        this.questiontype0 = questiontype0;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(int starlevel) {
        this.starlevel = starlevel;
    }

    /**
     * 是否是产权量表
     * @return
     */
    public boolean isPropFlag() {
        return (getFlag() & ScaleUtils.SCALE_PROPER_FLAG) > 0;
    }

    /**
     * 是否是国际量表
     * @return
     */
    public boolean isInternFlag() {
        return (getFlag() & ScaleUtils.SCALE_FREE_FLAG) > 0;
    }

    /**
     * 是否是高考量表
     * @return
     */
    public boolean isCeeFlag() {
        return (getFlag() & ScaleUtils.SCALE_CEE_FLAG) > 0;
    }

    public Scale clone() {
        Scale scale = new Scale();
        scale.setId(id);
        scale.setType(type); // 类型名称
        scale.setTitle(title);
        scale.setDescn(descn);
        scale.setGuidance(guidance);
        // if (individual != null) {
        // try {
        // scale.setIndividual((Individual) individual.clone());
        // } catch (CloneNotSupportedException e) {
        // e.printStackTrace();
        // }
        // }
        scale.setQuestiontype(questiontype);
        scale.setQuestiontype0(questiontype0);
        scale.setQuestiontype1(questiontype1);
        scale.setQuestiontype2(questiontype2);
        scale.setQuestiontype3(questiontype3);
        scale.setReversequestions(reversequestions);
        scale.dimensionMap = this.dimensionMap;
        scale.questionMap = this.questionMap;
        // scale.descriptorBuilder = descriptorBuilder;
        scale.flag = flag;
        scale.countVisibleQuestion = countVisibleQuestion;
        scale.countPlaceholderQuestion = countPlaceholderQuestion;
        scale.category = category;
        scale.starlevel = starlevel; // 星级
        scale.priority = priority; // 推荐级别
        scale.setCreatedBy(this.getCreatedBy());
        scale.setCreationTime(this.getCreationTime());
        scale.setOrgId(orgId);

        return scale;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    public String getApplicablePerson() {
        return applicablePerson;
    }

    public void setApplicablePerson(String applicablePerson) {
        this.applicablePerson = applicablePerson;
    }

    public String getReportGraph() {
        return reportGraph;
    }

    public void setReportGraph(String reportGraph) {
        this.reportGraph = reportGraph;
    }

    public String getReportImageParam1() {
        return reportImageParam1;
    }

    public void setReportImageParam1(String reportImageParam1) {
        this.reportImageParam1 = reportImageParam1;
    }

    public String getReportImageParam2() {
        return reportImageParam2;
    }

    public void setReportImageParam2(String reportImageParam2) {
        this.reportImageParam2 = reportImageParam2;
    }

    public OrderValueMap<String, Dimension> getDimensionMap() {
        return dimensionMap;
    }

    public void setDimensionMap(OrderValueMap<String, Dimension> dimensionMap) {
        this.dimensionMap = dimensionMap;
    }

    public OrderValueMap<String, Question> getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(OrderValueMap<String, Question> questionMap) {
        this.questionMap = questionMap;
    }

    public int getIsOriginalFlag() {
        return isOriginalFlag;
    }

    public void setIsOriginalFlag(int isOriginalFlag) {
        this.isOriginalFlag = isOriginalFlag;
    }

    public String toQuestionHTML() {
        Iterator<Entry<String, Question>> it = this.questionMap.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        // 根据量表的题目数量进行,下面这种方式肯定不行，整个读取excel生成结构部分需要重新架构，分析所有题的类型并结构化，需要考虑设计模式
        /*
         * for(int i=1;i<=this.questionNum;i++){ Question q =
         * this.questionMap.get("Q"+i); if(q==null) continue;
         * sb.append(q.toHTML()); }
         */
        while (it.hasNext()) {
            Map.Entry<String, Question> ovm = it.next();
            Question q = ovm.getValue();
            if (!StringUtils.isEmpty(q.getHeadtitle()))// 如果有矩阵题的主标题，则在第一题前面显示主标题
            {
                String qid = q.getId();
                String lastqid = qid.substring(qid.length() - 2, qid.length());
                if (lastqid.equals(".1"))
                    sb.append(q.toHeadtitleHTML());
            }
            sb.append(q.toHTML());
        }
        return sb.toString();
    }

    public String toDimHTML1() {
        // Iterator<Entry<String, Dimension>> it =
        // this.dimensionMap.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= this.dimensionMap.size(); i++) {
            Dimension dim = this.dimensionMap.get("W" + i);
            if (dim == null || dim.getParenDimension() != null) {
                if (i == 0) {
                    Dimension dim0 = this.dimensionMap.get("W00");
                    if (dim0 != null)
                        sb.append("<div><span style='margin-left:10px;font: bold 12pt Arial;'>总量表</span></div>");
                }
                continue;
            }
            String desc = dim.getDescn();
            if (desc == null)
                sb.append("<div><span style='margin-left:10px;font: bold 12pt Arial;'>" + dim.getTitle()
                        + "</span></div>");
            else
                sb.append("<div><span style='margin-left:10px;font: bold 12pt Arial;'>" + dim.getTitle()
                        + ":</span><span>" + desc + "</span></div>");
        }
        /*
         * while(it.hasNext()){ Map.Entry<String, Dimension> ovm = it.next();
         * Dimension dim = ovm.getValue();
         * sb.append("<div><span style='margin-left:10px;'>"+dim.getId()+
         * "</span><span>"+dim.getTitle()+"</span></div>"); }
         */
        return sb.toString();
    }

    public String toDimHTML() {
        // Iterator<Entry<String, Dimension>> it =
        // this.dimensionMap.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        SimpleHtmlTable table = new SimpleHtmlTable(true);
        TR head = table.newTr();
        head.addElement(table.newTDWidthStyle("一级维度", 100, "text-align:center;"));
        head.addElement(table.newTDWidthStyle("简介", 400, "text-align:center;"));

        SimpleHtmlTable subtable = new SimpleHtmlTable(true);
        head = subtable.newTr();
        head.addElement(subtable.newTDWidthStyle("一级维度", 100, "text-align:center;"));
        head.addElement(subtable.newTDWidthStyle("二级维度", 100, "text-align:center;"));
        head.addElement(subtable.newTDWidthStyle("简介", 300, "text-align:center;"));
        boolean hasSubdim = false;
        for (int i = 0; i <= this.dimensionMap.size(); i++) {
            Dimension dim = this.dimensionMap.get("W" + i);
            if (dim == null || dim.getParenDimension() != null) {// 一级维度或者0级维度
                if (i == 0) {
                    Dimension dim0 = this.dimensionMap.get("W00");
                    if (dim0 != null)
                        sb.append("<div><span style='margin-left:10px;font: bold 12pt Arial;'>总量表</span></div>");
                }
                continue;
            }
            if (i == 0) {
                if (ScaleUtils.isMentalHealthScale(this.getCode()))
                    continue;
            }
            String desc = dim.getDescn();
            // if(dim.getSubdimensions()!=null){//一级维度
            head = table.newTr();
            head.addElement(table.newTDWidthStyle(dim.getTitle(), 100, "text-align:left;"));
            head.addElement(table.newTDWidthStyle(desc, 400, "text-align:left;"));
            List<Dimension> subdimList = dim.getSubdimensions();
            if (subdimList != null) {
                hasSubdim = true;
                for (int j = 0; j < subdimList.size(); j++) {
                    Dimension subdim = subdimList.get(j);
                    head = subtable.newTr();
                    head.addElement(subtable.newTDWidthStyle(dim.getTitle(), 100, "text-align:left;"));
                    head.addElement(subtable.newTDWidthStyle(subdim.getTitle(), 100, "text-align:left;"));
                    head.addElement(subtable.newTDWidthStyle(subdim.getDescn(), 300, "text-align:left;"));

                }
            }
        }
        if (hasSubdim)
            return table.toString() + "<br>" + subtable.toString();
        else
            return table.toString();
    }
}
