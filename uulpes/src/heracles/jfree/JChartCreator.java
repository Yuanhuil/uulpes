package heracles.jfree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class JChartCreator {

    private static final String DEFAULT_INFO = "无数据显示";
    private JChartParam chartParam;
    private JFreeChart freeChart;
    private static Font labelFont = new Font("黑体", Font.PLAIN, 14);

    public static void main(String[] args) {

        FileOutputStream fos = null;
        JFreeChart freeChart = null;
        try {
            JChartCreator cchart = new JChartCreator();

            JChartParam dm = new JChartParam(0.0, 100.0);
            cchart.setChartParam(dm);
            // 加入数据
            dm.putToDataSet("s", 72);
            dm.putToDataSet("e", 83);
            dm.putToDataSet("r", 54);
            dm.putToDataSet("d", 85);
            dm.putToDataSet("w", 61);
            dm.putToDataSet("a", 80);
            dm.putToDataSet("中国", 90);
            dm.putToDataSet("美国", 100);
            // xyChart EPQ制图
            // dm.putToDataSet("20", 83);
            // dm.putToDataSet("15", 54);
            // dm.putToDataSet("30", 85);
            // dm.putToDataSet("40", 61);
            // dm.putToDataSet("50", 80);
            // dm.putToDataSet("60", 90);

            dm.addMarkerAt(40); // 横分隔条
            dm.addMarkerAt(60); // 横分隔条

            // dm.setAxisXVisible(false);
            // dm.setAxisYVisible(false);

            // 常模
            // dm.putToDataSet0("h", 30);
            // dm.putToDataSet0("n", 60);
            // dm.putToDataSet0("o", 80);
            dm.setChartType("pi");
            // freeChart = cchart.lineChart();
            // freeChart = cchart.mLineChart();
            freeChart = cchart.pieChart();

            // freeChart = cchart.xyChart();

            fos = new FileOutputStream(new File("E:\\freeChart1.jpg"));
            ChartUtilities.writeChartAsJPEG(fos, freeChart, 800, 300);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JChartCreator() {
    }

    public JChartCreator(JChartParam chartParam) {
        this.chartParam = chartParam;
    }

    public JChartCreator(String paramStr) {
        JChartParam chartParam = new JChartParam().parseString(paramStr);
        this.setChartParam(chartParam);
    }

    public void writeChartAsPNG(OutputStream out) throws IOException {
        int w = chartParam.getWidth() == 0 ? 200 : chartParam.getWidth();
        int h = chartParam.getHeight() == 0 ? 125 : chartParam.getHeight();
        ChartUtilities.writeChartAsPNG(out, this.getChart(), w, h);
    }

    public JFreeChart getChart() {
        String cht = chartParam.getChartType(); // 什么类型的图片
        if (StringUtils.isEmpty(cht)) {
            cht = ChartConstants.CHART_LINE;
        }
        if (cht.equalsIgnoreCase(ChartConstants.CHART_LINE)) {
            lineChart();
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_BAR)) {
            barChart();
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_MBAR)) {
            mBarChart();// 多类型柱图
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_PIE)) {
            pieChart();
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_STACK_BAR)) {
            stackedBarChart();
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_MLINE)) {
            mLineChart();
        } else {
            xyChart();
        }
        freeChart.setTitle(new TextTitle(getChartParam().getChartTitle(), new Font("宋体", Font.BOLD, 16)));
        return freeChart;
    }

    private JFreeChart stackedBarChart() {
        DefaultCategoryDataset dataset = this.getChartParam().getStackedBarDataset();
        freeChart = ChartFactory.createStackedBarChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false,
                false);
        CategoryPlot categoryplot = (CategoryPlot) freeChart.getPlot();

        StandardChartTheme chartTheme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        DefaultDrawingSupplier drawingSupplier = new DefaultDrawingSupplier(apaint,
                new Paint[] { Color.decode("0xFFFF00"), Color.decode("0x0036CC") },
                new Stroke[] { new BasicStroke(2.0f) }, new Stroke[] { new BasicStroke(0.5f) },
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
        chartTheme.setDrawingSupplier(drawingSupplier);
        chartTheme.apply(freeChart);

        setChartBaseStyle();

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        CustomStackedBarRenderer stackedbarrenderer = new CustomStackedBarRenderer(apaint);
        categoryplot.setRenderer(stackedbarrenderer);
        stackedbarrenderer.setRenderAsPercentages(true);
        stackedbarrenderer.setDrawBarOutline(false);
        stackedbarrenderer.setBaseItemLabelsVisible(true);
        stackedbarrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        stackedbarrenderer.setBarPainter(new StandardBarPainter());
        stackedbarrenderer.setBaseItemLabelFont(labelFont);
        stackedbarrenderer.setBaseLegendTextFont(labelFont);
        stackedbarrenderer.setShadowVisible(false);
        stackedbarrenderer.setMaximumBarWidth(0.1D);

        LegendTitle legend = new LegendTitle(categoryplot);
        legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        legend.setFrame(new LineBorder());
        legend.setBackgroundPaint(Color.white);
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setItemFont(new Font("宋体", Font.PLAIN, 12));
        freeChart.addSubtitle(legend);
        return freeChart;
    }

    @SuppressWarnings("unchecked")
    private JFreeChart pieChart() {
        DefaultPieDataset pieDataset = (DefaultPieDataset) getDataSet();
        freeChart = ChartFactory.createPieChart("", pieDataset, false, false, false);
        PiePlot pieplot = (PiePlot) freeChart.getPlot();
        LegendTitle legend = new LegendTitle(pieplot);
        legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        legend.setFrame(new LineBorder());
        legend.setBackgroundPaint(Color.white);
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setItemFont(new Font("宋体", Font.PLAIN, 12));
        freeChart.addSubtitle(legend);
        pieplot.setLabelFont(new Font("宋体", 0, 12));
        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));
        pieplot.setBackgroundPaint(Color.WHITE);
        pieplot.setOutlineVisible(chartParam.isOutlineVisible());
        pieplot.setShadowPaint(Color.WHITE);
        List list = pieDataset.getKeys();
        for (int i = 0; i < list.size(); ++i) {
            pieplot.setSectionPaint((Comparable) list.get(i), apaint[i]);
            System.out.println(list.get(i));
        }
        return freeChart;
    }

    private JFreeChart mBarChart() {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) getDataSet();
        String ort = StringUtils.defaultString(getChartParam().getOrt(), ChartConstants.CHART_AXIS_V);
        if (ort.equalsIgnoreCase(ChartConstants.CHART_AXIS_H)) {
            freeChart = ChartFactory.createBarChart3D(chartParam.getChartTitle(), chartParam.getCategoryAxisLabel(),
                    chartParam.getValueAxisLabel(), dataset, PlotOrientation.HORIZONTAL, true, false, false);
        } else {
            freeChart = ChartFactory.createBarChart3D(chartParam.getChartTitle(), chartParam.getCategoryAxisLabel(),
                    chartParam.getValueAxisLabel(), dataset, PlotOrientation.VERTICAL, true, false, false);

        }
        setChartFont();
        return freeChart;
    }

    private JFreeChart barChart() {
        DefaultCategoryDataset bardataset = (DefaultCategoryDataset) getDataSet();
        String ort = StringUtils.defaultString(getChartParam().getOrt(), ChartConstants.CHART_AXIS_V);
        if (ort.equalsIgnoreCase(ChartConstants.CHART_AXIS_H)) {
            freeChart = ChartFactory.createBarChart("", "", "", bardataset, PlotOrientation.HORIZONTAL, false, false,
                    false);
        } else {
            freeChart = ChartFactory.createBarChart("", "", "", bardataset, PlotOrientation.VERTICAL, false, false,
                    false);
        }
        // freeChart.setBackgroundPaint(new GradientPaint(0.0F, 0.0F,
        // Color.yellow, 350F, 0.0F, Color.white, true));
        // freeChart.setNotify(true);
        setChartBaseStyle();
        // 设置柱型图的柱样式
        CategoryPlot categoryplot = (CategoryPlot) freeChart.getPlot();
        CustomBarRenderer barRenderer = new CustomBarRenderer(apaint);
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setItemMargin(-1);
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barRenderer.setBaseItemLabelFont(labelFont);
        barRenderer.setBaseItemLabelsVisible(true);
        barRenderer.setShadowVisible(false);
        barRenderer.setDrawBarOutline(true);
        barRenderer.setMaximumBarWidth(0.06D);
        categoryplot.setRenderer(barRenderer);
        // barRenderer.setItemMargin(0.3);*/
        return freeChart;

    }

    private JFreeChart mLineChart() {
        DefaultCategoryDataset linedataset = (DefaultCategoryDataset) getDataSet();
        // freeChart = ChartFactory.createLineChart("", "", "", linedataset,
        // PlotOrientation.VERTICAL, false, false, false);
        freeChart = createChart(linedataset);
        return freeChart;
    }

    private JFreeChart lineChart() {
        DefaultCategoryDataset linedataset = (DefaultCategoryDataset) getDataSet();
        String ort = StringUtils.defaultString(getChartParam().getOrt(), ChartConstants.CHART_AXIS_V);
        if (ort.equalsIgnoreCase(ChartConstants.CHART_AXIS_H)) {
            freeChart = ChartFactory.createLineChart("", "", "", linedataset, PlotOrientation.HORIZONTAL, false, false,
                    false);
        } else {
            freeChart = ChartFactory.createLineChart("", "", "", linedataset, PlotOrientation.VERTICAL, false, false,
                    false);
        }
        setChartBaseStyle();
        // 设置线型图的线样式
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) ((CategoryPlot) freeChart.getPlot())
                .getRenderer();
        lineandshaperenderer.setBaseShapesVisible(true);
        lineandshaperenderer.setBaseLinesVisible(true);
        lineandshaperenderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-2D, -2D, 5D, 5D));
        // lineandshaperenderer.setBaseItemLabelGenerator(new
        // StandardCategoryItemLabelGenerator());
        // lineandshaperenderer.setBaseItemLabelsVisible(true);
        lineandshaperenderer.setSeriesPaint(0, Color.BLACK);// 折线的颜色*/
        return freeChart;
    }

    /**
     * 仅对EQP做的图，如果将来有其它地方使用这种图，再进步修改为通用的
     * 
     * @return
     */
    private JFreeChart xyChart() {
        XYDataset xyDataSet = (XYDataset) getDataSet();
        String ort = StringUtils.defaultString(getChartParam().getOrt(), ChartConstants.CHART_AXIS_V);
        if (ort.equalsIgnoreCase(ChartConstants.CHART_AXIS_H)) {
            freeChart = ChartFactory.createXYLineChart("", "", "", xyDataSet, PlotOrientation.HORIZONTAL, false, false,
                    false);
        } else {
            freeChart = ChartFactory.createXYLineChart("", "", "", xyDataSet, PlotOrientation.VERTICAL, false, false,
                    false);
        }
        XYPlot xyplot = (XYPlot) freeChart.getPlot();
        // 设置网格线
        xyplot.setDomainGridlinesVisible(true);
        xyplot.setRangeGridlinesVisible(true);
        xyplot.setRangeGridlinePaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.lightGray);
        // xyplot.setAxisOffset(RectangleInsets.ZERO_INSETS);
        xyplot.setBackgroundPaint(Color.WHITE);

        // 图像轮廓是否显示
        xyplot.setOutlineVisible(chartParam.isOutlineVisible());
        // 画分割线
        if (chartParam.getMarkerAtList() != null) {
            for (double d : chartParam.getMarkerAtList()) {
                IntervalMarker marker = new IntervalMarker(d, d, Color.BLACK, new BasicStroke(1), Color.BLACK,
                        new BasicStroke(1), 1.0f);
                xyplot.addRangeMarker(marker);
            }
        }
        if (chartParam.getDotMarkers() != null) {
            for (double d : chartParam.getDotMarkers()) {
                xyplot.addRangeMarker(
                        new IntervalMarker(d, d, Color.BLACK, DOTTED_STROKE, Color.BLACK, DOTTED_STROKE, 1.0f));
            }
        }
        if (chartParam.getDomainMarkers() != null) {
            for (double d : chartParam.getDomainMarkers()) {
                IntervalMarker marker = new IntervalMarker(d, d, Color.BLACK, new BasicStroke(1), Color.BLACK,
                        new BasicStroke(1), 1.0f);
                xyplot.addDomainMarker(marker);
            }
        }
        if (chartParam.getDotDomainMarkers() != null) {
            for (double d : chartParam.getDotDomainMarkers()) {
                xyplot.addDomainMarker(
                        new IntervalMarker(d, d, Color.BLACK, DOTTED_STROKE, Color.BLACK, DOTTED_STROKE, 1.0f));
            }
        }
        DoubleRange range = chartParam.getValueRange();
        ValueAxis domainAxis = xyplot.getDomainAxis();
        ValueAxis rangeAxis = xyplot.getRangeAxis();
        ((NumberAxis) domainAxis).setTickUnit(new NumberTickUnit(5D));
        ((NumberAxis) rangeAxis).setTickUnit(new NumberTickUnit(5D));
        if (range != null) {
            domainAxis.setAutoRange(false);
            domainAxis.setRange(range.getMinimumDouble(), range.getMaximumDouble());
            rangeAxis.setAutoRange(false);
            rangeAxis.setRange(range.getMinimumDouble(), range.getMaximumDouble());
        }
        DoubleRange markRange = chartParam.getMarkRange();
        if (markRange != null) {
            Color color = new Color(0, 0, 255, 25);
            IntervalMarker intervalMarker = new IntervalMarker(markRange.getMinimumDouble(),
                    markRange.getMaximumDouble(), color, new BasicStroke(1), color, new BasicStroke(1), 1.0f);
            intervalMarker.setAlpha(1.0F);
            intervalMarker.setPaint(color);
            // xyplot.addRangeMarker(intervalMarker);
            // xyplot.addDomainMarker(intervalMarker);
            intervalMarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
            intervalMarker.setLabel("N量表");
            intervalMarker.setLabelFont(new Font("宋体", Font.BOLD, 14));
            intervalMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
            intervalMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
            xyplot.addDomainMarker(intervalMarker, Layer.BACKGROUND);
            try {
                intervalMarker = (IntervalMarker) intervalMarker.clone();
                intervalMarker.setLabel("E量表");
                intervalMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
                intervalMarker.setLabelTextAnchor(TextAnchor.BASELINE_RIGHT);
                xyplot.addRangeMarker(intervalMarker, Layer.BACKGROUND);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        Map<String, Double> data = chartParam.getDataSet();
        if (MapUtils.isNotEmpty(data)) {
            String fmt = "EN(%s,%d)";
            for (Map.Entry<String, Double> ent : data.entrySet()) {
                String s = String.format(fmt, ent.getKey(), ent.getValue().intValue());
                XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(s, Double.valueOf(ent.getKey()),
                        ent.getValue(), 20D);
                xypointerannotation.setBaseRadius(35D);
                xypointerannotation.setTipRadius(1.0D);
                xypointerannotation.setFont(new Font("SansSerif", 0, 14));
                xypointerannotation.setPaint(Color.blue);
                xypointerannotation.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT);
                xyplot.addAnnotation(xypointerannotation);
            }
        }
        xyplot.addAnnotation(createXYTextAnnotation("抑郁质（内向、不稳）", 3, 95, TextAnchor.HALF_ASCENT_LEFT));
        xyplot.addAnnotation(createXYTextAnnotation("胆汁质（外向、不稳）", 97, 95, TextAnchor.HALF_ASCENT_RIGHT));
        xyplot.addAnnotation(createXYTextAnnotation("粘液质（内向、稳定）", 3, 5, TextAnchor.HALF_ASCENT_LEFT));
        xyplot.addAnnotation(createXYTextAnnotation("多血质（外向、稳定）", 97, 5, TextAnchor.HALF_ASCENT_RIGHT));

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        xylineandshaperenderer.setBaseShapesFilled(true);
        return freeChart;
    }

    XYTextAnnotation createXYTextAnnotation(String text, double x, double y, TextAnchor anchor) {
        XYTextAnnotation annotation = new XYTextAnnotation(text, x, y);
        annotation.setFont(new Font("宋体", 0, 12));
        annotation.setPaint(Color.black);
        annotation.setTextAnchor(anchor);
        return annotation;
    }

    final static Stroke DOTTED_STROKE = new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 6F, 6F }, 0.0F);

    private void setChartBaseStyle() {
        // 设置样式
        CategoryPlot categoryplot = (CategoryPlot) freeChart.getPlot();
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setRangeGridlinesVisible(true);
        categoryplot.setAxisOffset(RectangleInsets.ZERO_INSETS);
        categoryplot.setOutlineVisible(chartParam.isOutlineVisible());
        if (ChartConstants.AXIS_LOCATION_BOTTOM_OR_RIGHT.equalsIgnoreCase(getChartParam().getDomainAxisLocation())) {
            categoryplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        }
        String ort = StringUtils.defaultString(getChartParam().getOrt(), ChartConstants.CHART_AXIS_V);
        String cht = StringUtils.defaultString(getChartParam().getChartType(), ChartConstants.CHART_LINE);
        if (ort.equalsIgnoreCase(ChartConstants.CHART_AXIS_H) || cht.equalsIgnoreCase(ChartConstants.CHART_LINE)) {
            categoryplot.setRangeGridlinePaint(Color.lightGray);
            categoryplot.setDomainGridlinePaint(Color.BLUE);
        } else {
            categoryplot.setRangeGridlinePaint(Color.BLUE);
            categoryplot.setDomainGridlinePaint(Color.lightGray);
        }
        categoryplot.setBackgroundPaint(Color.WHITE);
        categoryplot.setNoDataMessage(DEFAULT_INFO);

        if (chartParam.isPart()) {
            CategoryMarker dcm = new CategoryMarker("");
            dcm.setDrawAsLine(true);
            dcm.setPaint(Color.BLACK);
            dcm.setStroke(new BasicStroke(1));
            categoryplot.addDomainMarker(dcm);
        }
        if (chartParam.getMarkerAtList() != null) {
            for (double d : chartParam.getMarkerAtList()) {
                categoryplot.addRangeMarker(new IntervalMarker(d, d, Color.BLACK, new BasicStroke(1), Color.BLACK,
                        new BasicStroke(1), 1.0f));
            }
        }
        if (chartParam.getDotMarkers() != null) {
            for (double d : chartParam.getDotMarkers()) {
                categoryplot.addRangeMarker(
                        new IntervalMarker(d, d, Color.BLACK, DOTTED_STROKE, Color.BLACK, DOTTED_STROKE, 1.0f));
            }
        }
        DoubleRange markRange = chartParam.getMarkRange();
        if (markRange != null) {
            Color color = new Color(0, 0, 255, 25);
            IntervalMarker intervalMarker = new IntervalMarker(markRange.getMinimumDouble(),
                    markRange.getMaximumDouble(), color, new BasicStroke(1), color, new BasicStroke(1), 1.0f);
            intervalMarker.setAlpha(1.0F);
            categoryplot.addRangeMarker(intervalMarker);
        }

        // 设置图表轴的样式
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setLabelFont(labelFont);
        domainAxis.setTickLabelFont(labelFont);
        if (ort.equalsIgnoreCase(ChartConstants.CHART_AXIS_H)) {
            domainAxis.setLowerMargin(0.02);
            domainAxis.setUpperMargin(0.02);
        } else {
            domainAxis.setLowerMargin(0.02);
            domainAxis.setUpperMargin(0.02);
            if (chartParam.getLabp() > 0) {
                if (chartParam.getLabp() == 90) {
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
                }
            }
        }
        domainAxis.setAxisLinePaint(Color.BLACK);

        domainAxis.setCategoryMargin(0.05);
        domainAxis.setVisible(chartParam.isAxisXVisible());

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        // numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        DoubleRange range = chartParam.getValueRange();
        if (range != null) {
            numberaxis.setAutoRange(false);
            numberaxis.setRange(range.getMinimumDouble(), range.getMaximumDouble());
        }
        numberaxis.setLowerMargin(0.08);
        numberaxis.setUpperMargin(0.1);
        numberaxis.setAxisLinePaint(Color.BLACK);
        numberaxis.setFixedDimension(15);
        numberaxis.setVisible(chartParam.isAxisYVisible());
    }

    private Dataset getDataSet() {
        String cht = StringUtils.defaultIfEmpty(chartParam.getChartType(), ChartConstants.CHART_LINE);
        if (cht.equalsIgnoreCase(ChartConstants.CHART_XY)) {
            XYSeries xyseries = new XYSeries("Random Data");
            Map<String, Double> data = chartParam.getDataSet();
            if (MapUtils.isNotEmpty(data)) {
                for (Map.Entry<String, Double> ent : data.entrySet()) {
                    xyseries.add(Double.valueOf(ent.getKey()), ent.getValue());
                }
            }
            return new XYSeriesCollection(xyseries);
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_PIE)) {
            DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
            Map<String, Double> data = chartParam.getDataSet();
            if (MapUtils.isNotEmpty(data)) {
                for (Map.Entry<String, Double> ent : data.entrySet()) {
                    defaultpiedataset.setValue(ent.getKey(), ent.getValue());
                }
            }
            return defaultpiedataset;
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_MLINE)) {
            DefaultCategoryDataset result = chartParam.getStackedBarDataset();

            return result;
        } else if (cht.equalsIgnoreCase(ChartConstants.CHART_MBAR)) {
            DefaultCategoryDataset result = chartParam.getStackedBarDataset();
            return result;
        } else {
            Dataset result = null;
            result = new DefaultCategoryDataset();
            String series = "data";
            String gener = "gener";
            Map<String, Double> data0 = chartParam.getDataSet0();
            if (MapUtils.isNotEmpty(data0)) {
                for (Map.Entry<String, Double> ent : data0.entrySet()) {
                    ((DefaultCategoryDataset) result).addValue((Double) ent.getValue(), gener, ent.getKey());
                }
            }
            if (chartParam.isPart())
                ((DefaultCategoryDataset) result).addValue(null, "", "");

            Map<String, Double> data = chartParam.getDataSet();
            if (MapUtils.isNotEmpty(data)) {
                for (Map.Entry<String, Double> ent : data.entrySet()) {
                    ((DefaultCategoryDataset) result).addValue((Double) ent.getValue(), series, ent.getKey());
                }
            }
            return result;
        }

    }

    static Paint apaint[] = createPaint();

    private static Paint[] createPaint() {
        Paint apaint[] = new Paint[10];
        /*
         * apaint[0] = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F,
         * Color.white); apaint[1] = new GradientPaint(0.0F, 0.0F, Color.green,
         * 0.0F, 0.0F, Color.white); apaint[2] = new GradientPaint(0.0F, 0.0F,
         * Color.blue, 0.0F, 0.0F, Color.white); apaint[3] = new
         * GradientPaint(0.0F, 0.0F, Color.orange, 0.0F, 0.0F, Color.white);
         * apaint[4] = new GradientPaint(0.0F, 0.0F, Color.magenta, 0.0F, 0.0F,
         * Color.white); apaint[5] = new GradientPaint(0.0F, 0.0F, Color.cyan,
         * 0.0F, 0.0F, Color.white); apaint[6] = new GradientPaint(0.0F, 0.0F,
         * Color.lightGray, 0.0F, 0.0F, Color.white); apaint[7] = new
         * GradientPaint(0.0F, 0.0F, Color.pink, 0.0F, 0.0F, Color.white);
         * apaint[8] = new GradientPaint(0.0F, 0.0F, Color.yellow, 0.0F, 0.0F,
         * Color.white); apaint[9] = new GradientPaint(0.0F, 0.0F,
         * Color.darkGray, 0.0F, 0.0F, Color.white);
         */
        apaint[0] = Color.red;
        apaint[1] = Color.green;
        apaint[2] = Color.blue;
        apaint[3] = Color.orange;
        apaint[4] = Color.magenta;
        apaint[5] = Color.cyan;
        apaint[6] = Color.lightGray;
        apaint[7] = Color.pink;
        apaint[8] = Color.yellow;
        apaint[9] = Color.darkGray;
        return apaint;
    }

    static class CustomBarRenderer extends BarRenderer {
        private static final long serialVersionUID = 2627983432244364177L;
        private Paint colors[];

        public Paint getItemPaint(int i, int j) {
            return colors[j % colors.length];
        }

        public CustomBarRenderer(Paint apaint[]) {
            colors = apaint;
        }
    }

    static class CustomStackedBarRenderer extends StackedBarRenderer {
        private static final long serialVersionUID = 2627983432244364177L;
        private Paint colors[];

        public Paint getItemPaint(int i, int j) {
            return colors[i % colors.length];
        }

        public CustomStackedBarRenderer(Paint apaint[]) {
            colors = apaint;
        }
    }

    public void setChartParam(JChartParam chartParam) {
        this.chartParam = chartParam;
    }

    public JChartParam getChartParam() {
        return chartParam;
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
        Font font1 = new Font("微软雅黑", 10, 20); // 设定字体、类型、字号
        jfreechart.getTitle().setFont(font1); // 标题
        // ２、对图里面的汉字设定,也就是Ｐlot的设定
        Font font2 = new Font("微软雅黑", 10, 16); // 设定字体、类型、字号
        categoryplot.getDomainAxis().setLabelFont(font2);// 相当于横轴或理解为X轴
        categoryplot.getRangeAxis().setLabelFont(font2);// 相当于竖轴理解为Y轴
        // 3、下面的方块区域是 LegendTitle 对象
        Font font3 = new Font("微软雅黑", 10, 12); // 设定字体、类型、字号
        jfreechart.getLegend().setItemFont(font3);// 最下方
        // 这是处理Ｐlot里面的横轴，同理可以正理竖轴
        CategoryAxis categoryaxis = categoryplot.getDomainAxis(); // 横轴上的
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 45度倾斜,可以改成其他,默认是水平
        categoryaxis.setTickLabelFont(new Font("微软雅黑", 10, 12));// 设定字体、类型、字号
        // categoryaxis.setTickLabelFont(new Font("SimSun", 10, 12));//
        // 设定字体、类型、字号，也是可以的
        return jfreechart;

    }

    private void setChartFont() {
        Font font1 = new Font("微软雅黑", 10, 20); // 设定字体、类型、字号
        freeChart.getTitle().setFont(font1); // 标题
        // ２、对图里面的汉字设定,也就是Ｐlot的设定
        Font font2 = new Font("微软雅黑", 10, 16); // 设定字体、类型、字号
        CategoryPlot categoryplot = (CategoryPlot) freeChart.getPlot();
        BarRenderer render = (BarRenderer) categoryplot.getRenderer();
        render.setItemMargin(0.0);
        categoryplot.getDomainAxis().setLabelFont(font2);// 相当于横轴或理解为X轴
        categoryplot.getRangeAxis().setLabelFont(font2);// 相当于竖轴理解为Y轴
        // 3、下面的方块区域是 LegendTitle 对象
        Font font3 = new Font("微软雅黑", 10, 12); // 设定字体、类型、字号
        freeChart.getLegend().setItemFont(font3);// 最下方
        // 这是处理Ｐlot里面的横轴，同理可以正理竖轴
        CategoryAxis categoryaxis = categoryplot.getDomainAxis(); // 横轴上的
        // categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//
        // 45度倾斜,可以改成其他,默认是水平
        categoryaxis.setTickLabelFont(new Font("微软雅黑", 10, 12));// 设定字体、类型、字号
    }

}
