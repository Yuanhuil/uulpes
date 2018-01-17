package edutec.scale.explain;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.DimDetail;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.Questionnaire;
import heracles.jfree.JChartCreator;
import heracles.jfree.JChartParam;
import heracles.jfree.bean.ChartParamBean;
import heracles.util.Pools;
import heracles.util.SimpleCodec;
import heracles.util.UtilCollection;
import heracles.util.UtilMisc;
import sun.misc.BASE64Encoder;

public class ReportArticle {
    private static final int EXPLAIN = 1; // 总分说明
    private static final int COMMENT = 2; // 总评
    private static final int DETAIL = 3; // 详细解释
    private static final int ADVICE = 4; // 发展建议
    private Scale scale;
    private int userFlag;
    private Questionnaire questionnaire;
    private Map<String, DimDetail> dimDetailMap;
    private List<DimDetail> dimDetailList;
    private final int type = 1; // 0:表示5点分，1表示百分比
    private String comment = StringUtils.EMPTY;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @param scale
     *            量表
     * @param userFlag
     *            用户 学生-老师-家长
     * @param rankMap
     */
    public ReportArticle(Scale scale, int userFlag, Map<String, DimDetail> demDetailMap) {
        this.scale = scale;
        this.userFlag = userFlag;
        // this.userFlag = tfUserFlag(userFlag);
        this.dimDetailMap = demDetailMap;
    }

    public ReportArticle(Questionnaire scale, List<DimDetail> dimDetailList, int userFlag) {
        this.questionnaire = scale;
        // this.userFlag = tfUserFlag(userFlag);
        this.userFlag = userFlag;
        this.dimDetailList = dimDetailList;
    }

    public Scale getScale() {
        return scale;
    }

    StrBuilder summarize = new StrBuilder();
    StrBuilder advice = new StrBuilder();
    Map<String, StrBuilder> detail = new LinkedHashMap<String, StrBuilder>();
    Map<String, StrBuilder> simpleDetail = new LinkedHashMap<String, StrBuilder>();
    List<Map<String, Object>> detailList = new ArrayList();
    String summarizeDesc = null;// 总量表结果解释
    String summarizeDevice = null;// 总量表建议

    public String getSummarizeDesc() {
        return summarizeDesc;
    }

    public String getSummarizeDevice() {
        return summarizeDevice;
    }

    public List<Map<String, Object>> getDetailList() {
        return detailList;
    }

    /**
     * 量表级总评
     * 
     * @return
     */
    public void content(boolean isPersonalReport) {
        // summarize.append(getComment());
        // 一级维度和子维度解释和发展建议
        generalComment_detail_advice(detail, advice, isPersonalReport);
        // W0总分结果解释和发展建议
        scaleGeneralComment(summarize, isPersonalReport);
    }

    // 复合报告的维度结果解析
    public void buildCompositeReportComment() {
        // 一级维度和子维度解释和发展建议
        buildCompositeReportComment_detail(simpleDetail);
        // W0总分结果解释和发展建议
        buildCompositeReportGeneralComment(summarize);
    }

    public void buildPersonalReport(int type) {
        buildPersonalReportComment_detail(type, detailList);
        // W0总分结果解释和发展建议
        buildPersonalReportGeneralComment();
    }

    // 个人评语的维度结果解析
    public void buildRemarkReportComment() {
        buildRemarkReportGeneralComment(summarize);
    }

    /**
     * category=1的内容，并且维度规定为是W0
     * 
     * @param sb
     */
    private void scaleGeneralComment(StrBuilder detailSb, boolean isPersonalReport) {
        DimensionBlock dim = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
        if (dim != null) {

            int rank = dim.getRank();// 维度等级
            DimDetail dimDetail = getDimDetail(dim);
            if (dimDetail.getDimDescn() == null)
                return;
            // StrBuilder detailSb = new StrBuilder();
            // detailSb.append("<br>");
            detailSb.append("总分结果解释:");
            detailSb.append(dimDetail.getDimDescn());
            if (isPersonalReport) {
                if (dimDetail.getDimAdvice() != null && !dimDetail.getDimAdvice().equals("")) {
                    detailSb.append("<br>");
                    detailSb.append("指导建议:");
                    detailSb.append(dimDetail.getDimAdvice());
                }
            }
            // detailMap.put(dim.getTitle(), detailSb);
        }
    }

    private void buildPersonalReportGeneralComment() {
        DimensionBlock dim = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
        if (dim == null)
            dim = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM0);
        if (dim != null) {
            int rank = dim.getRank();// 维度等级
            DimDetail dimDetail = getDimDetail(dim);
            if (dimDetail.getDimDescn() != null) {
                if (!dimDetail.getDimDescn().equals(""))
                    summarizeDesc = dimDetail.getDimDescn();
            }
            // summarizeDesc = dimDetail.getDimDescn();
            if (dimDetail.getDimAdvice() != null) {
                if (!dimDetail.getDimAdvice().equals("")) {
                    summarizeDevice = dimDetail.getDimAdvice();
                }
            }
        }
    }

    private void buildCompositeReportGeneralComment(StrBuilder detailSb) {
        DimensionBlock dim = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
        if (dim != null) {

            int rank = dim.getRank();// 维度等级
            DimDetail dimDetail = getDimDetail(dim);
            if (!StringUtils.isEmpty(dimDetail.getDimDescn())) {
                detailSb.append("总评:");
                detailSb.append(dimDetail.getDimDescn1());
            }
            if (!StringUtils.isEmpty(dimDetail.getDimAdvice())) {
                detailSb.append("<br>");
                detailSb.append("总指导建议:");
                detailSb.append(dimDetail.getDimAdvice());
            }

        }
    }

    private void buildRemarkReportGeneralComment(StrBuilder detailSb) {
        // 添加总分维度的首句。
        DimensionBlock dim = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
        if (dim == null)
            dim = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM0);
        if (dim != null) {

            int rank = dim.getRank();// 维度等级
            DimDetail dimDetail = getDimDetail(dim);
            if (dimDetail.getDimDescn1() != null) {
                detailSb.append(dimDetail.getDimDescn1());
            }
        }

        // 添加一级维度的首句
        List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
        for (DimensionBlock dimBlk : listLev1) {
            DimDetail dimDetail = getDimDetail(dimBlk);
            if (!Dimension.SUM_SCORE_DIM.equals(dimBlk.getId()) && !Dimension.SUM_SCORE_DIM0.equals(dimBlk.getId())) {
                detailSb.append(dimDetail.getDimDescn1());
            }
        }
    }

    private void buildPersonalReportComment_detail(int type, List<Map<String, Object>> detailList) {
        List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
        for (DimensionBlock dimBlk : listLev1) {
            DimDetail dimDetail = getDimDetail(dimBlk);
            if (!Dimension.SUM_SCORE_DIM.equals(dimBlk.getId()) && !Dimension.SUM_SCORE_DIM0.equals(dimBlk.getId())) {
                Map<String, Object> dimInfo = new HashMap<String, Object>();
                detailList.add(dimInfo);
                String dimN1 = UtilMisc.extractDigit(dimBlk.getId());
                int rank = getRank(dimBlk);
                dimInfo.put("dimtitle", dimBlk.getTitle());
                dimInfo.put("desc", dimDetail.getDimDescn());
                dimInfo.put("device", dimDetail.getDimAdvice());

                List<DimensionBlock> subList = dimBlk.getSubdimensionBlkList();
                dimInfo.put("subdimlist", null);
                if (subList != null) {
                    if (type == 0) {
                        String dimImageUrl = getDimImageUrl(dimDetail);
                        dimInfo.put("image", dimImageUrl);
                    } else {
                        // String dimImageUrl = getDimImageUrl(dimDetail);
                        // String imagedata = getSumImageData(dimImageUrl);
                        String imagedata = getSubSumImageData(subList);
                        dimInfo.put("image", imagedata);
                    }
                    List<Map<String, Object>> subDimInfoList = new ArrayList();
                    for (DimensionBlock subdimBlk : subList) {
                        Map<String, Object> subDimInfo = new HashMap<String, Object>();
                        DimDetail dimDetail1 = getDimDetail(subdimBlk);
                        subDimInfo.put("dimtitle", subdimBlk.getTitle());
                        subDimInfo.put("desc", dimDetail1.getDimDescn());
                        subDimInfo.put("device", dimDetail1.getDimAdvice());
                        subDimInfoList.add(subDimInfo);
                    }
                    dimInfo.put("subdimlist", subDimInfoList);
                }
            }
        }
    }

    public String getSumImageData(String qryStr) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            JFreeChart chart = null;
            int w = 0;
            int h = 0;
            if (StringUtils.startsWithIgnoreCase(qryStr, "bean=")) {
                ChartParamBean paramBean = new ChartParamBean();
                paramBean.urlDecode(qryStr);
                java.awt.Dimension dimension = paramBean.getSize();
                w = (int) dimension.getWidth();
                h = (int) dimension.getHeight();
                chart = paramBean.createChartBuilder().doBuilder();
            } else {
                JChartCreator chartCreator = new JChartCreator();
                JChartParam chartParam = new JChartParam().parseString(qryStr);
                chartCreator.setChartParam(chartParam);
                chart = chartCreator.getChart();
                w = chartParam.getWidth() == 0 ? 200 : chartParam.getWidth();
                h = chartParam.getHeight() == 0 ? 125 : chartParam.getHeight();
            }
            if (chart != null) {
                // *** CHART SIZE ***
                ChartUtilities.writeChartAsPNG(outputStream, chart, w, h);
                BASE64Encoder encoder = new BASE64Encoder();
                String imageData = encoder.encode(outputStream.toByteArray());
                return imageData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSubSumImageData(List<DimensionBlock> subList) {
        try {
            // 如果是健康量表或道德量表的W0，不表示总分只是一个控制变量,它们不属于有量表总分的量表
            // if (scaleId.endsWith(ScaleReportDescriptor.MHS) ||
            // scaleId.endsWith(ScaleReportDescriptor.MS)) {
            // 生成标题段
            List<String> titleList = new ArrayList<String>();
            for (DimensionBlock dimBlk : subList) {
                String t = dimBlk.getTitle();
                titleList.add(t);

            }
            // 生成值段
            List valueList = new ArrayList();
            for (DimensionBlock dimBlk : subList) {
                double v;
                v = getScore(dimBlk);
                valueList.add(v);
            }
            // 生成得分等级段
            List<Color> colorList = new ArrayList<Color>();
            for (DimensionBlock dimBlk : subList) {
                int rank = dimBlk.getRank();
                Color color = ReportImg.getBarColor(rank);
                colorList.add(color);
            }

            String imageData = ReportImg.getImage(titleList, valueList, colorList, 500, 500);
            return imageData;
        } finally {
        }

    }

    /**
     * category=2的内容，并且维度是一级维度
     * 
     * @param comment
     */
    private void generalComment_detail_advice(Map<String, StrBuilder> detailMap, StrBuilder advice,
            boolean isPersonalReport) {
        List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
        for (DimensionBlock dimBlk : listLev1) {
            DimDetail dimDetail = getDimDetail(dimBlk);
            if (!Dimension.SUM_SCORE_DIM.equals(dimBlk.getId())) {
                String dimN1 = UtilMisc.extractDigit(dimBlk.getId());
                int rank = getRank(dimBlk);
                // Paragraph paragraphComment = getParagraph(rank, COMMENT,
                // dimN1, "0"); // 一级(非总分)总评
                // Paragraph paragraphDetail = getParagraph(rank, DETAIL, dimN1,
                // "0"); // 一级(非总分)详细解释
                // Paragraph paragraphAdvice = getParagraph(rank, ADVICE, dimN1,
                // "0"); // 一级(非总分)的建议

                StrBuilder detailSb = new StrBuilder();
                detailSb.append(dimDetail.getDimDescn());
                detailSb.append("<br>");
                detailSb.append("建议:");
                detailSb.append(dimDetail.getDimAdvice());
                // .append("<p><div align=\"center\"><span
                // style=\"font-size:20px;\">").append(ent.getKey()).append("</span></strong></div>");
                /* 连接子维度的“建议”和/或"详细解释"的句子 * */
                if (isPersonalReport) {
                    List<DimensionBlock> subList = dimBlk.getSubdimensionBlkList();
                    if (subList != null) {
                        for (DimensionBlock subdimBlk : subList) {
                            DimDetail dimDetail1 = getDimDetail(subdimBlk);

                            detailSb.append("<br>");
                            detailSb.append(dimDetail1.getTitle());
                            detailSb.append(":");
                            detailSb.append(dimDetail1.getDimDescn());
                            detailSb.append("<br>");
                            if (dimDetail1.getDimAdvice() != null) {
                                detailSb.append("建议:");
                                detailSb.append(dimDetail1.getDimAdvice());
                            }
                            // 加入子维度的发展建议

                        }
                    }
                }

                detailMap.put(dimBlk.getTitle(), detailSb);
            }
        }

    }

    // 复合报告的维度解析
    private void buildCompositeReportComment_detail(Map<String, StrBuilder> detailMap) {
        List<DimensionBlock> listLev1 = questionnaire.getRootDimensionBlocks();
        for (DimensionBlock dimBlk : listLev1) {
            DimDetail dimDetail = getDimDetail(dimBlk);
            if (!Dimension.SUM_SCORE_DIM.equals(dimBlk.getId())) {
                String dimN1 = UtilMisc.extractDigit(dimBlk.getId());
                int rank = getRank(dimBlk);

                StrBuilder detailSb = new StrBuilder();
                detailSb.append(dimDetail.getTitle());
                detailSb.append(":");
                detailSb.append(dimDetail.getDimDescn1());
                detailMap.put(dimBlk.getTitle(), detailSb);
                List<DimensionBlock> subList = dimBlk.getSubdimensionBlkList();
                if (subList != null) {
                    for (DimensionBlock subdimBlk : subList) {
                        DimDetail dimDetail1 = getDimDetail(subdimBlk);
                        detailSb.append("\n");
                        detailSb.append(dimDetail1.getTitle());
                        detailSb.append(":");
                        detailSb.append(dimDetail1.getDimDescn1());
                    }
                }
            }
        }

    }

    private int getRank(DimensionBlock dim) {
        return dim.getRank();
    }

    public String getAdvice() {
        return advice.toString();
    }

    public Map<String, StrBuilder> getDetailOfDims() {
        return detail;
    }

    public Map<String, StrBuilder> getSimpleDimDesciptor() {
        return simpleDetail;
    }

    public String detailOfDimsToString() {
        Map<String, StrBuilder> map = getDetailOfDims();
        StrBuilder result = null;
        try {
            result = Pools.getInstance().borrowStrBuilder();
            for (StrBuilder strb : map.values()) {
                result.append(strb);
            }
            return result.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(result);
        }
    }

    public String getSummarize() {
        return summarize.toString();
    }

    private DimDetail getDimDetail(DimensionBlock dim) {
        for (DimDetail dimDetail : dimDetailList) {
            if (dimDetail.getId() == dim.getId())
                return dimDetail;
        }
        return null;

    }

    public String getDimImageUrl(DimDetail dimDetail) {
        StrBuilder sb = new StrBuilder();
        if (dimDetail.getDimBlk().isRoot()) {
            if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                sb.append("scalechart.do?");
                sb.append("d=");

                String t = dimDetail.getTitle();
                sb.append(SimpleCodec.enhex(t));
                sb.append(UtilCollection.COMMA);

                List<DimensionBlock> subDims = dimDetail.getDimBlk().getSubdimensionBlkList();
                if (subDims != null) {
                    for (DimensionBlock sumDim : subDims) {
                        t = sumDim.getTitle();
                        sb.append(SimpleCodec.enhex(t));
                        sb.append(UtilCollection.COMMA);
                    }
                }
                sb.setCharAt(sb.length() - 1, UtilCollection.COLON);

                double v = getScore(dimDetail);
                sb.append(v);
                sb.append(UtilCollection.COMMA);
                if (subDims != null) {
                    for (DimensionBlock sumDim : subDims) {

                        v = getScore(sumDim);
                        sb.append(v);
                        sb.append(UtilCollection.COMMA);
                    }
                }
                // sb.setLength(sb.length() - 1);
                sb.setCharAt(sb.length() - 1, UtilCollection.COLON);
                int rank = dimDetail.getRank();
                sb.append(rank);
                sb.append(UtilCollection.COMMA);
                // 生成得分等级段
                if (subDims != null) {
                    for (DimensionBlock sumDim : subDims) {
                        rank = sumDim.getRank();
                        sb.append(rank);
                        sb.append(UtilCollection.COMMA);
                    }
                }
                sb.setLength(sb.length() - 1);
                sb.append(UtilCollection.COLON);
                sb.append(type);

            }
        }
        return sb.toString();
    }

    private double getScore(DimDetail dimDetail) {
        double v;
        if (dimDetail.getTScore() != null) {
            v = dimDetail.getTScore().doubleValue();
            return v;
        }
        return 0d;
    }

    private double getScore(DimensionBlock dimBlock) {
        double v;
        if (dimBlock.getTScore() != null) {
            v = dimBlock.getTScore().doubleValue();
            return v;
        }
        return 0d;
    }
}
