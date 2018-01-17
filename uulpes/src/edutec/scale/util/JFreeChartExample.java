package edutec.scale.util;
/*
 * $Id: JFreeChartExample.java,v 1.6 2010/03/17 06:15:32 wangwen Exp $
 * $Name:  $
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://itextdocs.lowagie.com/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 * JFreeChart example.
 */
public class JFreeChartExample extends ApplicationFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -6653761876864372926L;

    public JFreeChartExample(String s) {
        super(s);
        String[] name = { "你", "我" };
        // String[] name ={"you","me"};
        double[] dou = { 3.98, 4.11 };
        String flag = "0";
        JFreeChart jfreechart = createChart(createDataset(name, dou, flag), name, dou, flag, "");
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartpanel);
    }

    /**
     * Creates some PDFs with JFreeCharts.
     * 
     * @param args
     *            no arguments needed
     */
    public static void main(String[] args) {
        /**
         * the following line is a workaround for JDK 1.5 (fix by Adriaan
         * Joubert)
         */
        // org.jfree.text.TextUtilities.setUseDrawRotatedStringWorkaround(false);
        JFreeChartExample stackedbarchartdemo3 = new JFreeChartExample("Stacked Bar Chart Demo 3");
        stackedbarchartdemo3.pack();
        RefineryUtilities.centerFrameOnScreen(stackedbarchartdemo3);
        stackedbarchartdemo3.setVisible(true);
    }

    /**
     * Gets an example barchart.
     * 
     * @return a barchart
     */
    public static JFreeChart getBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(40, "hits/hour", "index.html");
        dataset.setValue(20, "hits/hour", "download.html");
        dataset.setValue(15, "hits/hour", "faq.html");
        dataset.setValue(8, "hits/hour", "links.html");
        dataset.setValue(31, "hits/hour", "docs.html");
        return ChartFactory.createBarChart("Popularity of iText pages", "Page", "hits/hour", dataset,
                PlotOrientation.VERTICAL, false, true, false);
    }

    /**
     * Gets an example piechart.
     * 
     * @return a piechart
     */
    public static JFreeChart getPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("iText", 60);
        dataset.setValue("cinema.lowagie.com", 10);
        dataset.setValue("tutorial", 30);
        return ChartFactory.createPieChart("Website popularity", dataset, true, true, false);
    }

    /**
     * Gets an example XY chart
     * 
     * @return an XY chart
     */
    public static JFreeChart getXYChart() {
        XYSeries series = new XYSeries("XYGraph");
        series.add(1, 5);
        series.add(2, 7);
        series.add(3, 3);
        series.add(4, 5);
        series.add(5, 4);
        series.add(6, 5);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return ChartFactory.createXYLineChart("XY Chart", "X-axis", "Y-axis", dataset, PlotOrientation.VERTICAL, true,
                true, false);
    }

    /**
     * Gets an example XY chart
     * 
     * @return an XY chart
     */
    public static JFreeChart get5Chart() {
        XYSeries series = new XYSeries("XYGraph");
        series.add(1, 5);
        series.add(2, 7);
        series.add(3, 3);
        series.add(4, 5);
        series.add(5, 4);
        series.add(6, 5);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return ChartFactory.createXYLineChart("XY Chart", "X-axis", "Y-axis", dataset, PlotOrientation.VERTICAL, true,
                true, false);
    }

    /**
     * @param name
     *            数组里边存的是各个维度的名称
     * @param dou
     *            数组里边存的是各个维度的大小(1-5,0-100);
     * @param flag
     *            为"0"的是五点量表 为"1"的时候是百分量表 avgstr "评分区间" factStr 百分等级
     * @return
     */
    public static CategoryDataset createDataset(String[] name, double[] dou, String flag) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

        String category1 = "";
        String[] str = new String[] { " (非常差)", " (比较差)", " (中等)", " (比较好)", " (非常好)" };
        String avgStr = "评分区间";
        // String factStr = "百分等级";
        double[] doub = setDouble(dou);
        String[] nameStr = setString1(name);
        if (flag.equals("1")) {
            for (int i = 0; i < doub.length; i++) {
                if (i == 0) {
                    defaultcategorydataset.addValue(3.593000000000000D, avgStr + str[0], category1);
                    defaultcategorydataset.addValue(23.832000000000000D, avgStr + str[1], category1);
                    defaultcategorydataset.addValue(45.150000000000000D, avgStr + str[2], category1);
                    defaultcategorydataset.addValue(23.832000000000000D, avgStr + str[3], category1);
                    defaultcategorydataset.addValue(3.593000000000000D, avgStr + str[4], category1);
                }
                if (i != 0) {
                    defaultcategorydataset.addValue(doub[i], nameStr[i] + str[0], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[1], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[2], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[3], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[4], category1);
                }
            }
        } else if (flag.equals("0")) {
            for (int i = 0; i < doub.length; i++) {
                if (doub[i] == 100.0) {
                    defaultcategorydataset.addValue(1.500000000000000D, avgStr + str[0], category1);
                    defaultcategorydataset.addValue(1.000000000000000D, avgStr + str[1], category1);
                    defaultcategorydataset.addValue(1.000000000000000D, avgStr + str[2], category1);
                    defaultcategorydataset.addValue(1.000000000000000D, avgStr + str[3], category1);
                    defaultcategorydataset.addValue(0.500000000000000D, avgStr + str[4], category1);
                }
                if (doub[i] != 100.0) {
                    double doubu = doub[i];
                    if (doubu < 0) {
                        doubu = 0.00;
                    }
                    defaultcategorydataset.addValue(doubu, nameStr[i] + str[0], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[1], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[2], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[3], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[4], category1);
                }
            }
        } else if (flag.equals("gaokaoPicture")) {
            int iLength = dou.length;
            double[] dDouble = new double[iLength - 2];
            for (int i = 2; i < dou.length; i++) {
                dDouble[i - 2] = dou[i];
            }
            double[] returnDouble = setDouble(dDouble);

            for (int i = 0; i < returnDouble.length; i++) {
                if (returnDouble[i] == 100.0) {
                    defaultcategorydataset.addValue(dou[0], avgStr + str[0], category1);
                    defaultcategorydataset.addValue(dou[1] - dou[0], avgStr + str[1], category1);
                    defaultcategorydataset.addValue(50 - dou[1], avgStr + str[2], category1);
                } else {
                    double doubu = returnDouble[i];
                    if (doubu < 0) {
                        doubu = 0.00;
                    } else {
                        ;
                    }
                    defaultcategorydataset.addValue(doubu, nameStr[i] + str[0], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[1], category1);
                    defaultcategorydataset.addValue(0.00, nameStr[i] + str[2], category1);
                }
            }
        } else {
            ;
        }
        return defaultcategorydataset;
    }

    /**
     * 得到JFreeChart
     * 
     * @param categorydataset
     * @param name
     * @param dou
     * @param flag
     * @return
     */
    public static JFreeChart createChart(CategoryDataset categorydataset, String[] name, double[] dou, String flag,
            String kind) {
        JFreeChart jfreechart = ChartFactory.createStackedBarChart(null// 图形标题名称
                , null, // 纵坐标的lable
                null, // 横坐标的lable
                categorydataset // dataset
                , PlotOrientation.VERTICAL // 水平显示
                , true// legeng? 是否显示图例
                , true // tooltips? 是否工具提示
                , false // URLS? 是否做连接
        );
        Color cl = new Color(254, 238, 238);
        if (kind.equalsIgnoreCase("word")) {
            jfreechart.setBackgroundPaint(Color.WHITE); // 设定背景色为白色
        } else {
            jfreechart.setBackgroundPaint(cl);
        }

        GroupedStackedBarRenderer groupedstackedbarrenderer = new GroupedStackedBarRenderer();
        groupedstackedbarrenderer.setBarPainter(new StandardBarPainter());
        KeyToGroupMap keytogroupmap = new KeyToGroupMap("G1");
        String[] str = new String[] { " (非常差)", " (比较差)", " (中等)", " (比较好)", " (非常好)" };
        if (flag.equals("gaokaoPicture")) {
            String[] nameStr = setString1(name);
            int sum = 1;
            for (int i = 0; i < nameStr.length; i++) {
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[0], "G" + sum);
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[1], "G" + sum);
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[2], "G" + sum);
                sum++;
            }
            groupedstackedbarrenderer.setSeriesToGroupMap(keytogroupmap);// update
            // the
            // key
            // to
            // the
            // map
            groupedstackedbarrenderer.setItemMargin(0.10000000000000001D);
            groupedstackedbarrenderer.setDrawBarOutline(false);// Bar的外轮廓线不画

            for (int i = 0; i < nameStr.length; i++) {
                if (nameStr[i].equals("a0")) {

                    GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, Color.blue);// 蓝色
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint);
                } else if (nameStr[i].equals("a2")) {
                    GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.yellow, 0.0F, 0.0F,
                            Color.yellow);
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint1);

                } else if (nameStr[i].equals("a4")) {
                    GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.black, 0.0F, 0.0F, Color.black);
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint2);
                } else {
                    GradientPaint gradientpaint3 = new GradientPaint(0.0F, 0.0F, Color.cyan, 0.0F, 0.0F, Color.cyan);
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint3);
                }
            }
            GradientPaint gradientpaint4 = new GradientPaint(0.0F, 0.0F, Color.yellow, 0.0F, 0.0F, Color.yellow);
            for (int i = 0; i < nameStr.length; i++) {
                groupedstackedbarrenderer.setSeriesPaint(i * 5 + 1, gradientpaint4);
            }
            GradientPaint gradientpaint5 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, Color.red);
            for (int i = 0; i < nameStr.length; i++) {
                groupedstackedbarrenderer.setSeriesPaint(i * 5 + 2, gradientpaint5);
            }

        } else {
            String[] nameStr = setString1(name);
            int sum = 1;
            for (int i = 0; i < nameStr.length; i++) {

                keytogroupmap.mapKeyToGroup(nameStr[i] + str[0], "G" + sum);
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[1], "G" + sum);
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[2], "G" + sum);
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[3], "G" + sum);
                keytogroupmap.mapKeyToGroup(nameStr[i] + str[4], "G" + sum);

                sum++;
            }
            groupedstackedbarrenderer.setSeriesToGroupMap(keytogroupmap);// update
            // the
            // key
            // to
            // the
            // map
            groupedstackedbarrenderer.setItemMargin(0.10000000000000001D);
            groupedstackedbarrenderer.setDrawBarOutline(false);// Bar的外轮廓线不画

            for (int i = 0; i < nameStr.length; i++) {
                if (nameStr[i].equals("a0")) {
                    GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, new Color(34, 34, 255), 0.0F, 0.0F,
                            new Color(136, 136, 255));// 蓝色
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint);
                    // 柱外文字，要指定坐标。
                    // TextUtilities.drawRotatedString(totalFormatter.format(d9),
                    // graphics2d, (float)double1.getCenterX(),
                    // (float)(double1.getMinY() - 4D),
                    // TextAnchor.BOTTOM_CENTER, 0.0D,
                    // TextAnchor.BOTTOM_CENTER);
                }
                if (!nameStr[i].equals("a0")) {
                    GradientPaint gradientpaint5 = new GradientPaint(0.0F, 0.0F, Color.cyan, 0.0F, 0.0F, Color.cyan);
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint5);
                }
            }
            GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, new Color(34, 255, 34), 0.0F, 0.0F,
                    new Color(136, 255, 136));// 绿色
            for (int i = 0; i < nameStr.length; i++) {
                groupedstackedbarrenderer.setSeriesPaint(i * 5 + 1, gradientpaint1);
            }
            GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, Color.red);// 红色
            for (int i = 0; i < nameStr.length; i++) {
                groupedstackedbarrenderer.setSeriesPaint(i * 5 + 2, gradientpaint2);
            }
            GradientPaint gradientpaint3 = new GradientPaint(0.0F, 0.0F, new Color(34, 255, 34), 0.0F, 0.0F,
                    new Color(136, 255, 136));// 绿色
            for (int i = 0; i < nameStr.length; i++) {
                groupedstackedbarrenderer.setSeriesPaint(i * 5 + 3, gradientpaint3);
            }
            GradientPaint gradientpaint4 = new GradientPaint(0.0F, 0.0F, new Color(34, 34, 255), 0.0F, 0.0F,
                    new Color(136, 136, 255)); // 橘色
            for (int i = 0; i < nameStr.length; i++) {
                groupedstackedbarrenderer.setSeriesPaint(i * 5 + 4, gradientpaint4);
            }
        }
        /*
         * GradientPaint gradientpaint6 = new GradientPaint(0.0F, 0.0F,
         * Color.green, 0.0F, 0.0F, new Color(0, 64, 0));//逐渐变化的颜色
         */
        // groupedstackedbarrenderer.setGradientPaintTransformer(new
        // StandardGradientPaintTransformer(
        // GradientPaintTransformType.HORIZONTAL));
        SubCategoryAxis subcategoryaxis = new SubCategoryAxis("");
        String[] nameStrr = setString(name);
        for (int i = 0; i < nameStrr.length; i++) {
            subcategoryaxis.setCategoryMargin(0.050000000000000003D);
            subcategoryaxis.addSubCategory(nameStrr[i]);
        }

        CategoryPlot categoryplot = jfreechart.getCategoryPlot();
        categoryplot.setBackgroundPaint(cl); // 设定图表数据显示部分背景色
        categoryplot.setDomainGridlinePaint(Color.gray); // 横坐标网格线白色
        categoryplot.setDomainGridlinesVisible(true); // 可见
        categoryplot.setRangeGridlinePaint(Color.white); // 纵坐标网格线白色
        categoryplot.setDomainAxis(subcategoryaxis);
        categoryplot.setRenderer(groupedstackedbarrenderer);
        categoryplot.setFixedLegendItems(createLegendItems());

        // 显示在柱子上的数字，要够大才能显示出来。
        StandardCategoryItemLabelGenerator standardcategoryitemlabelgenerator = new StandardCategoryItemLabelGenerator();
        groupedstackedbarrenderer.setBaseItemLabelGenerator(standardcategoryitemlabelgenerator);
        groupedstackedbarrenderer.setBaseItemLabelsVisible(true);
        ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.INSIDE3, TextAnchor.CENTER_RIGHT);
        groupedstackedbarrenderer.setBasePositiveItemLabelPosition(itemlabelposition);
        groupedstackedbarrenderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition());
        groupedstackedbarrenderer.setShadowVisible(false);
        // groupedstackedbarrenderer.setBase(5D);
        groupedstackedbarrenderer.setDrawBarOutline(true);
        // groupedstackedbarrenderer.setGradientPaintTransformer(new
        // StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
        // categoryplot.set

        ValueAxis rangeAxis = categoryplot.getRangeAxis();
        // 设置最高的一个 Item 与图片顶端的距离
        // rangeAxis.setUpperMargin(0.15);
        // 设置最低的一个 Item 与图片底端的距离true
        // rangeAxis.setLowerMargin(0.15);
        // 设定柱值的开始和结束数值范围
        if (flag.equals("1")) {
            rangeAxis.setRange(0, 100);
        } else if (flag.equals("0")) {
            rangeAxis.setRange(1, 5);
        } else if (flag.equals("gaokaoPicture")) {
            rangeAxis.setRange(0, 50);
        } else {
            ;
        }
        return jfreechart;
    }

    private static LegendItemCollection createLegendItems() {
        LegendItemCollection legenditemcollection = new LegendItemCollection();

        /*
         * LegendItem legenditem = new LegendItem("", "", null, null,
         * Plot.DEFAULT_LEGEND_ITEM_BOX, Color.white);
         * legenditemcollection.add(legenditem);
         */
        return legenditemcollection;
    }

    /**
     * 设置柱状图的长度 以及不足的时候长度置为零
     */

    public static double[] setDouble(double[] dou) {
        int sum = 0;
        int it = dou.length;
        if (it >= 4) {
            sum = it + 1;
        } else {
            sum = 6;
            if (it == 3) {
                sum = 7;
            }
        }

        double[] doub = new double[sum];
        for (int i = 0; i < doub.length; i++) {
            doub[i] = 0;

        }
        if (it == 1) {
            doub[0] = 100.0;
            doub[3] = dou[0];
        }
        if (it == 2) {
            doub[0] = 100.0;
            doub[2] = dou[0];
            doub[4] = dou[1];
        }
        if (it == 3) {
            doub[0] = 100.0;
            doub[2] = dou[0];
            doub[4] = dou[1];
            doub[6] = dou[2];
        }
        /*
         * if(it==4){ doub[0]=100.0; doub[1]=dou[0]; doub[2]=dou[1];
         * doub[3]=dou[2]; doub[4]=dou[3]; } if(it==5){ doub[0]=100.0;
         * doub[1]=dou[0]; doub[2]=dou[1]; doub[3]=dou[2]; doub[4]=dou[3];
         * doub[5]=dou[4]; }
         */
        if (it >= 4) {
            doub[0] = 100.0;
            for (int i = 0; i < dou.length; i++) {
                doub[i + 1] = dou[i];
            }
        }
        for (int i = 0; i < doub.length; i++) {
        }
        return doub;
    }

    /**
     * 给各个维度赋上名称,并按照顺序排列姓名
     * 
     * @param name
     * @return
     */
    public static String[] setString(String[] name) {
        int sum = 0;
        int it = name.length;
        if (it >= 4) {
            sum = it + 1;
        } else {
            sum = 6;
            if (it == 3) {
                sum = 7;
            }
        }
        String[] nameStr = new String[sum];
        for (int i = 0; i < nameStr.length; i++) {
            nameStr[i] = "";
        }
        if (it == 1) {
            nameStr[0] = "评分区间";
            nameStr[3] = name[0];
        }
        if (it == 2) {
            nameStr[0] = "评分区间";
            nameStr[2] = name[0];
            nameStr[4] = name[1];
        }
        if (it == 3) {
            nameStr[0] = "评分区间";
            nameStr[2] = name[0];
            nameStr[4] = name[1];
            nameStr[6] = name[2];
        }
        /*
         * if(it==4){ nameStr[0]="评分区间"; nameStr[1]=name[0]; nameStr[2]=name[1];
         * nameStr[3]=name[2]; nameStr[4]=name[3]; } if(it==5){
         * nameStr[0]="评分区间"; nameStr[1]=name[0]; nameStr[2]=name[1];
         * nameStr[3]=name[2]; nameStr[4]=name[3]; nameStr[5]=name[4]; }
         */
        if (it >= 4) {
            nameStr[0] = "评分区间";
            for (int i = 0; i < name.length; i++) {
                nameStr[i + 1] = name[i];
            }
        }
        for (int i = 0; i < nameStr.length; i++) {
        }
        return nameStr;
    }

    public static String[] setString1(String[] name) {
        int sum = 0;
        int it = name.length;
        if (it >= 4) {
            sum = it + 1;
        } else {
            sum = 6;
            if (it == 3) {
                sum = 7;
            }
        }
        String[] nameStr = new String[sum];
        for (int i = 0; i < nameStr.length; i++) {
            nameStr[i] = "a" + i;
        }
        return nameStr;
    }

}
