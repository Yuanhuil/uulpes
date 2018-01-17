package edutec.scale.explain;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import edutec.scale.descriptor.ScaleReportDescriptor;
import edutec.scale.model.Dimension;
import edutec.scale.questionnaire.DimDetail;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.util.ScaleUtils;
import heracles.jfree.CustomBarRenderer;
import heracles.jfree.CustomBarRenderer3D;
import heracles.jfree.JChartCreator;
import heracles.jfree.JChartParam;
import heracles.jfree.bean.ChartParamBean;
import heracles.util.Pools;
import heracles.util.SimpleCodec;
import heracles.util.UtilCollection;
import sun.misc.BASE64Encoder;

/*******************************************************************************
 * 形成参数格式， 1）propChart.cht?d=b7a8d6c6b3ccb6c8:2.85:0
 * 2）propChart.cht?d=d7d4ced2b3ccb6c8,b7a8d6c6b3ccb6c8,b5c0b5c2b3ccb6c8,d0c5d1f6b3ccb6c8:2.73,2.85,3.33,2.9:0
 */
public class ReportImg {
    private static final String IMG_DIM_HERF = "reportchar.do?";
    private final List<DimDetail> dimDetailList;
    private final int type; // 0:表示5点分，1表示百分比
    private final String scaleId;

    public static void mainaa(String args[]) {
        String str1 = "Java EE开发";
        String str2 = "IOS开发";
        String str3 = "Android开发";
        String str4 = "1月";
        String str5 = "2月";
        String str6 = "3月";
        String str7 = "4月";
        String str8 = "5月";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0D, "", str4);
        dataset.addValue(4.0D, "", str5);
        dataset.addValue(3.0D, "", str6);

        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.VERTICAL, true, true,
                false);
        /**
         * VALUE_TEXT_ANTIALIAS_OFF表示将文字的抗锯齿关闭,
         * 使用的关闭抗锯齿后，字体尽量选择12到14号的宋体字,这样文字最清晰好看
         */
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        // 背景色
        chart.setBackgroundPaint(Color.white);
        // 设置标题字体
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 14));
        // 图例背景色
        chart.getLegend().setBackgroundPaint(new Color(110, 182, 229));
        // 图例字体
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));

        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        // 设置纵虚线可见
        // categoryPlot.setDomainGridlinesVisible(true);
        // 虚线色彩
        // categoryPlot.setDomainGridlinePaint(Color.black);
        // 设置横虚线可见
        categoryPlot.setRangeGridlinesVisible(true);
        // 虚线色彩
        categoryPlot.setRangeGridlinePaint(Color.black);
        // 设置柱的透明度
        categoryPlot.setForegroundAlpha(1.0f);
        // 设置柱图背景色（注意，系统取色的时候要使用
        // 16位的模式来查看颜色编码，这样比较准确）
        categoryPlot.setBackgroundPaint(new Color(110, 182, 229));

        /*
         * categoryPlot.setRangeCrosshairVisible(true);
         * categoryPlot.setRangeCrosshairPaint(Color.blue);
         */

        // 纵坐标--范围轴
        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        // 纵坐标y轴坐标字体
        numberAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
        // 纵坐标y轴标题字体
        numberAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        // 设置最高的一个 Item 与图片顶端的距离
        // numberAxis.setUpperMargin(0.5);
        // 设置最低的一个 Item 与图片底端的距离
        // numberAxis.setLowerMargin(0.5);
        // 设置刻度单位 为Integer
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // 横坐标--类别轴、域
        CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        // 横坐标x轴坐标字体
        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
        // 横坐标x轴标题字体
        categoryAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        // 类别轴的位置，倾斜度
        categoryAxis
                .setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988D));
        // 横轴上的 Lable
        // categoryAxis.setMaximumCategoryLabelWidthRatio(0.6f);
        // 是否完整显示
        // 设置距离图片左端距离
        categoryAxis.setLowerMargin(0.1D);
        // 设置距离图片右端距离
        categoryAxis.setUpperMargin(0.1D);

        // 渲染 - 中间的部分
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();

        // 设置柱子宽度
        barRenderer.setMaximumBarWidth(0.05);
        // 设置柱子高度
        barRenderer.setMinimumBarLength(0.2);
        // 设置柱子边框颜色
        barRenderer.setBaseOutlinePaint(Color.BLACK);
        // 设置柱子边框可见
        barRenderer.setDrawBarOutline(true);
        // 设置柱的颜色
        barRenderer.setSeriesPaint(0, new Color(0, 255, 0));
        barRenderer.setSeriesPaint(1, new Color(0, 0, 255));
        barRenderer.setSeriesPaint(2, new Color(255, 0, 0));

        // 设置每个柱之间距离
        barRenderer.setItemMargin(0.2D);
        // 显示每个柱的数值，并修改该数值的字体属性
        barRenderer.setIncludeBaseInRange(true);
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barRenderer.setBaseItemLabelsVisible(true);
        categoryPlot.setRenderer(barRenderer);
        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream("D:\\aaa.png");
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 800, 300);
        } catch (Exception e) {

        } finally {
            try {
                fos_jpg.close();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String args[]) {
        List titleList = new ArrayList();
        titleList.add("第一组");
        titleList.add("第二组");
        titleList.add("第三组");

        List valueList = new ArrayList();
        valueList.add("5");
        valueList.add("8");
        valueList.add("10");

        List<Color> colorList = new ArrayList<Color>();
        Color color1 = getBarColor(4);
        Color color2 = getBarColor(3);
        Color color3 = getBarColor(2);
        colorList.add(color1);
        colorList.add(color2);
        colorList.add(color3);

        CategoryDataset createdataset = createDataset(titleList, valueList, colorList);
        // JFreeChart jf = JFreeChartExample.createChart(createdataset, name,
        // num, flag,"");
        JFreeChart jf = ChartFactory.createBarChart3D("测验得分统计图", "维度", "得分(T)", createdataset, PlotOrientation.VERTICAL,
                true, false, false);
        CategoryPlot plot = (CategoryPlot) jf.getCategoryPlot();
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setRange(0, 10);

        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 显示每个柱的数值，并修改该数值的字体属性
        // BarRenderer renderer = (BarRenderer) plot.getRenderer();
        CustomBarRenderer renderer = new CustomBarRenderer(colorList);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setItemLabelAnchorOffset(10D);
        renderer.setItemMargin(0.0);
        renderer.setMinimumBarLength(0.0);
        renderer.setMaximumBarWidth(0.1);
        CategoryAxis domainAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 13));
        vAxis.setLabelFont(new Font("黑体", Font.PLAIN, 13));
        plot.setRenderer(renderer);
        jf.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 16));
        jf.getTitle().setFont(new Font("黑体", Font.PLAIN, 13)); // 标题
        CategoryAxis categoryaxis = plot.getDomainAxis(); // 横轴上的
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 45度倾斜,可以改成其他,默认是水平
        categoryaxis.setTickLabelFont(new Font("微软雅黑", 10, 12));// 设定字体、类型、字号
        // categoryaxis.setTickLabelFont(new Font("SimSun", 10, 12));//
        for (int i = 0; i < colorList.size(); i++) {
            // renderer.setSeriesPaint(i, colorList.get(i)); //
        }
        // renderer.setSeriesPaint(0, new Color(0, 255, 0));
        // renderer.setSeriesPaint(1, new Color(0, 0, 255));
        // renderer.setSeriesPaint(2, new Color(255, 0, 0));
        plot.setRenderer(renderer);
        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream("D:\\piefruit.jpg");
            ChartUtilities.writeChartAsJPEG(fos_jpg, 0.9f, jf, 800, 300);
        } catch (Exception e) {

        } finally {
            try {
                fos_jpg.close();
            } catch (Exception e) {
            }
        }
    }

    public ReportImg(ScaleReportDescriptor descriptor) {
        super();
        // this.dimDetailList = descriptor.evaluateDimRank(null);
        this.dimDetailList = descriptor.getDimDetails();
        this.scaleId = descriptor.getQuestionnaire().getScaleId();
        // 如果不是能力量表
        if (!ScaleUtils.isAbilityScale(scaleId)) {
            this.type = 0; // 五点图
        } else {
            this.type = 1; // 百分图
        }
    }

    private String[] dimHerfs = null;
    private String sumHerf = null;

    // 图表类型为“柱-多”或"柱2边"的总图
    public String getSumHerf1(/* String chartstyle */) {
        if (sumHerf == null) {
            StrBuilder sb = null;
            try {
                sb = Pools.getInstance().borrowStrBuilder();
                // sb.append(IMG_DIM_HERF);
                sb.append("d=");
                // 如果是健康量表或道德量表的W0，不表示总分只是一个控制变量,它们不属于有量表总分的量表
                // if (scaleId.endsWith(ScaleReportDescriptor.MHS) ||
                // scaleId.endsWith(ScaleReportDescriptor.MS)) {
                // 生成标题段
                DimDetail dimW0 = null;
                for (DimDetail dimDetail : dimDetailList) {
                    if (dimDetail.getDimBlk().isRoot()) {
                        if (Dimension.SUM_SCORE_DIM.equals(dimDetail.getId()))
                            dimW0 = dimDetail;
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())
                                && !Dimension.SUM_SCORE_DIM0.equals(dimDetail.getId())) {
                            String t = dimDetail.getTitle();
                            sb.append(SimpleCodec.enhex(t));
                            sb.append(UtilCollection.COMMA);
                        }
                    }
                }
                if (dimW0 != null && !ScaleUtils.isMentalHealthScale(scaleId)) {
                    sb.append(SimpleCodec.enhex(dimW0.getTitle()));
                    sb.append(UtilCollection.COMMA);
                }
                sb.setCharAt(sb.length() - 1, UtilCollection.COLON);
                // 生成值段
                for (DimDetail dimDetail : dimDetailList) {
                    if (dimDetail.getDimBlk().isRoot())
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())
                                && !Dimension.SUM_SCORE_DIM0.equals(dimDetail.getId())) {
                            double v;
                            v = getScore(dimDetail);
                            sb.append(v);
                            sb.append(UtilCollection.COMMA);
                        }
                }
                if (dimW0 != null && !ScaleUtils.isMentalHealthScale(scaleId)) {
                    double v = getScore(dimW0);
                    sb.append(v);
                    sb.append(UtilCollection.COMMA);
                }
                sb.setCharAt(sb.length() - 1, UtilCollection.COLON);
                // 生成得分等级段
                for (DimDetail dimDetail : dimDetailList) {
                    if (dimDetail.getDimBlk().isRoot())
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())
                                && !Dimension.SUM_SCORE_DIM0.equals(dimDetail.getId())) {
                            int rank = dimDetail.getRank();
                            sb.append(rank);
                            sb.append(UtilCollection.COMMA);
                        }
                }
                if (dimW0 != null && !ScaleUtils.isMentalHealthScale(scaleId)) {
                    int rank = dimW0.getRank();
                    sb.append(rank);
                    sb.append(UtilCollection.COMMA);
                }
                sb.setLength(sb.length() - 1);
                // }
                sb.append(UtilCollection.COLON);
                sb.append(type);
                sumHerf = sb.toString();
            } finally {
                Pools.getInstance().returnStrBuilder(sb);
            }
        }
        return sumHerf;
    }

    // 图表类型为“折2-中"的总图
    public String getZheTwoHref() {
        if (sumHerf == null) {
            StrBuilder sb = null;
            try {
                sb = Pools.getInstance().borrowStrBuilder();
                // sb.append(IMG_DIM_HERF);
                sb.append("d=");
                // 如果是健康量表或道德量表的W0，不表示总分只是一个控制变量,它们不属于有量表总分的量表
                // if (scaleId.endsWith(ScaleReportDescriptor.MHS) ||
                // scaleId.endsWith(ScaleReportDescriptor.MS)) {
                // 生成标题段
                DimDetail dimW0 = null;
                for (DimDetail dimDetail : dimDetailList) {
                    if (dimDetail.getDimBlk().isRoot()) {
                        if (Dimension.SUM_SCORE_DIM.equals(dimDetail.getId()))
                            dimW0 = dimDetail;
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())
                                && !Dimension.SUM_SCORE_DIM0.equals(dimDetail.getId())) {
                            String t = dimDetail.getTitle();
                            sb.append(SimpleCodec.enhex(t));
                            sb.append(UtilCollection.COMMA);
                        }
                    }
                }
                if (dimW0 != null && !ScaleUtils.isMentalHealthScale(scaleId)) {
                    sb.append(SimpleCodec.enhex(dimW0.getTitle()));
                    sb.append(UtilCollection.COMMA);
                }
                sb.setCharAt(sb.length() - 1, UtilCollection.COLON);
                // 生成值段
                for (DimDetail dimDetail : dimDetailList) {
                    if (dimDetail.getDimBlk().isRoot())
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())
                                && !Dimension.SUM_SCORE_DIM0.equals(dimDetail.getId())) {
                            double v;
                            v = getScore(dimDetail);
                            sb.append(v);
                            sb.append(UtilCollection.COMMA);
                        }
                }
                if (dimW0 != null && !ScaleUtils.isMentalHealthScale(scaleId)) {
                    double v = getScore(dimW0);
                    sb.append(v);
                    sb.append(UtilCollection.COMMA);
                }
                sb.setCharAt(sb.length() - 1, UtilCollection.COLON);
                // 生成得分等级段
                for (DimDetail dimDetail : dimDetailList) {
                    if (dimDetail.getDimBlk().isRoot())
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())
                                && !Dimension.SUM_SCORE_DIM0.equals(dimDetail.getId())) {
                            int rank = dimDetail.getRank();
                            sb.append(rank);
                            sb.append(UtilCollection.COMMA);
                        }
                }
                if (dimW0 != null && !ScaleUtils.isMentalHealthScale(scaleId)) {
                    int rank = dimW0.getRank();
                    sb.append(rank);
                    sb.append(UtilCollection.COMMA);
                }
                sb.setLength(sb.length() - 1);
                // }
                sb.append(UtilCollection.COLON);
                sb.append(type);
                sumHerf = sb.toString();
            } finally {
                Pools.getInstance().returnStrBuilder(sb);
            }
        }
        return sumHerf;
    }

    // 图表类型为“柱-多”或"柱2边"的总图
    public String getSumImageData() {
        try {
            // 如果是健康量表或道德量表的W0，不表示总分只是一个控制变量,它们不属于有量表总分的量表
            // if (scaleId.endsWith(ScaleReportDescriptor.MHS) ||
            // scaleId.endsWith(ScaleReportDescriptor.MS)) {
            // 生成标题段
            List<String> titleList = new ArrayList<String>();
            for (DimDetail dimDetail : dimDetailList) {
                if (dimDetail.getDimBlk().isRoot()) {
                    if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                        String t = dimDetail.getTitle();
                        titleList.add(t);
                    }
                }
            }
            // 生成值段
            List valueList = new ArrayList();
            for (DimDetail dimDetail : dimDetailList) {
                if (dimDetail.getDimBlk().isRoot())
                    if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                        double v;
                        v = getScore(dimDetail);
                        valueList.add(v);
                    }
            }
            // 生成得分等级段
            List<Color> colorList = new ArrayList<Color>();
            for (DimDetail dimDetail : dimDetailList) {
                if (dimDetail.getDimBlk().isRoot())
                    if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                        int rank = dimDetail.getRank();
                        Color color = getBarColor(rank);
                        colorList.add(color);
                    }
            }

            String imageData = getImage(titleList, valueList, colorList, 500, 500);
            return imageData;
        } finally {
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
                ChartUtilities.writeChartAsJPEG(outputStream, chart, w, h);
                BASE64Encoder encoder = new BASE64Encoder();
                String imageData = encoder.encode(outputStream.toByteArray());
                return imageData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public String getImage(List titleList, List valueList, List<Color> colorList, int picWidth, int picHeight) {

        CategoryDataset createdataset = createDataset(titleList, valueList, colorList);
        // JFreeChart jf = JFreeChartExample.createChart(createdataset, name,
        // num, flag,"");
        JFreeChart jf = ChartFactory.createBarChart3D("测验得分统计图", "维度", "得分(T)", createdataset, PlotOrientation.VERTICAL,
                true, false, false);
        CategoryPlot plot = (CategoryPlot) jf.getCategoryPlot();
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setRange(0, 10);

        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 显示每个柱的数值，并修改该数值的字体属性
        // BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
        CustomBarRenderer3D renderer = new CustomBarRenderer3D(colorList);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setItemLabelAnchorOffset(10D);
        renderer.setItemMargin(0.0);
        renderer.setMinimumBarLength(0.0);
        renderer.setMaximumBarWidth(0.1);
        CategoryAxis domainAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 13));
        vAxis.setLabelFont(new Font("黑体", Font.PLAIN, 13));

        jf.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 16));
        jf.getTitle().setFont(new Font("黑体", Font.PLAIN, 13)); // 标题
        CategoryAxis categoryaxis = plot.getDomainAxis(); // 横轴上的
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 45度倾斜,可以改成其他,默认是水平
        categoryaxis.setTickLabelFont(new Font("微软雅黑", 10, 12));// 设定字体、类型、字号
        // categoryaxis.setTickLabelFont(new Font("SimSun", 10, 12));//
        // for(int i=0;i<colorList.size();i++)
        // {
        // renderer.setSeriesPaint(i, colorList.get(i)); //
        // }
        plot.setRenderer(renderer);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG(outputStream, jf, 800, 300);
            BASE64Encoder encoder = new BASE64Encoder();
            String imageData = encoder.encode(outputStream.toByteArray());
            return imageData;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    static private CategoryDataset createDataset(List titleList, List valueList, List colorList) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        for (int i = 0; i < valueList.size(); i++) {
            double d = Double.valueOf(valueList.get(i).toString());
            defaultcategorydataset.addValue(d, "", (Comparable) titleList.get(i));
        }
        return defaultcategorydataset;
    }

    /**
     * 对于有总分的量表的总分图片显示 心理健康不显示总分，而显示所有一级维度， 其它只显示总分
     * 
     * @return
     */
    public String getSumHerf() {
        if (sumHerf == null) {
            StrBuilder sb = null;
            try {
                sb = Pools.getInstance().borrowStrBuilder();
                sb.append("scalechart.do?");
                sb.append("d=");
                // 如果是健康量表或道德量表的W0，不表示总分只是一个控制变量,它们不属于有量表总分的量表
                if (scaleId.endsWith(ScaleReportDescriptor.MHS) || scaleId.endsWith(ScaleReportDescriptor.MS)) {
                    // 生成标题段
                    for (DimDetail dimDetail : dimDetailList) {
                        if (dimDetail.getDimBlk().isRoot()) {
                            if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                                String t = dimDetail.getTitle();
                                sb.append(SimpleCodec.enhex(t));
                                sb.append(UtilCollection.COMMA);
                            }
                        }
                    }
                    sb.setCharAt(sb.length() - 1, UtilCollection.COLON);
                    // 生成值段
                    for (DimDetail dimDetail : dimDetailList) {
                        if (dimDetail.getDimBlk().isRoot())
                            if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                                double v;
                                v = getScore(dimDetail);
                                sb.append(v);
                                sb.append(UtilCollection.COMMA);
                            }
                    }
                    sb.setLength(sb.length() - 1);
                } else { // 不是健康量表且有总分,针对总分的量表和能力量表
                    for (DimDetail dimDetail : dimDetailList) {
                        if (Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                            String t = dimDetail.getTitle();
                            double v = getScore(dimDetail);
                            sb.append(SimpleCodec.enhex(t));
                            sb.append(UtilCollection.COLON);
                            sb.append(v);
                            break;
                        }
                    }
                }
                sb.append(UtilCollection.COLON);
                sb.append(type);
                sumHerf = sb.toString();
            } finally {
                Pools.getInstance().returnStrBuilder(sb);
            }
        }
        return sumHerf;
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

    public String[] getDimHerfs1() {
        if (dimHerfs == null) {
            // StrBuilder sb = new StrBuilder();
            dimHerfs = new String[dimDetailList.size()];
            int index = 0;
            for (DimDetail dimDetail : dimDetailList) {
                {
                    if (dimDetail.getDimBlk().isRoot())
                        if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                            StrBuilder sb = new StrBuilder();
                            sb.append("assessmentcenter/report/scalechart.do?");
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

                            dimHerfs[index] = sb.toString();
                            index++;
                        }
                }
            }

        }
        return dimHerfs;
    }

    public String[] getDimHerfs() {
        if (dimHerfs == null) {
            StrBuilder sb = new StrBuilder();
            dimHerfs = new String[dimDetailList.size()];
            int index = 0;
            for (DimDetail dimDetail : dimDetailList) {
                if (dimDetail.getDimBlk().isRoot())
                    if (!Dimension.SUM_SCORE_DIM.equals(dimDetail.getId())) {
                        double v = getScore(dimDetail);
                        sb.clear();
                        sb.append(IMG_DIM_HERF);
                        sb.append("d=");
                        sb.append(SimpleCodec.enhex(dimDetail.getTitle()));
                        sb.append(UtilCollection.COLON);
                        sb.append(v);
                        sb.append(UtilCollection.COLON).append(type);
                        dimHerfs[index] = sb.toString();
                        index++;
                    }
            }
        }
        return dimHerfs;
    }

    static private double getScore(DimensionBlock blk, boolean b) {
        double v;
        v = Double.parseDouble(blk.getFinalScore().toString());
        if (b) {
            v = 100 * v;
        }
        return v;
    }

    static public Color getBarColor(int grade) {
        switch (grade) {
        case 1:
            return Color.black;
        case 2:
            return Color.orange;
        case 3:
            return Color.yellow;
        case 4:
            return Color.green;
        case 5:
            return Color.blue;
        }
        return Color.green;
    }
}
