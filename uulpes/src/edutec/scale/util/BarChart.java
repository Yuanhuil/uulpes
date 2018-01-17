package edutec.scale.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import oracle.charts.axischart.AxisChart;
import oracle.charts.types.BarDesc;
import oracle.charts.types.ChartException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class BarChart extends AxisChart {
    /**
     * 
     */
    private static final long serialVersionUID = 5272961136080788553L;

    int itemp = 8;

    public BarChart() {
        super();

    }

    /**
     * 补齐柱状图数据不足
     */
    public void setXMoreSeries(Date[] temp) {
        Date[] dt = new Date[itemp];
        int it = temp.length;
        if (it < itemp) {
            if (it == 1) {
                dt[0] = java.sql.Date.valueOf("");
                dt[1] = java.sql.Date.valueOf("");
                dt[2] = java.sql.Date.valueOf("");
                dt[3] = temp[0];
                dt[4] = java.sql.Date.valueOf("");
                dt[5] = java.sql.Date.valueOf("");
                dt[6] = java.sql.Date.valueOf("");
                dt[7] = java.sql.Date.valueOf("");
            } else if (it == 2) {
                dt[0] = java.sql.Date.valueOf("");
                dt[1] = java.sql.Date.valueOf("");
                dt[2] = temp[0];
                dt[3] = java.sql.Date.valueOf("");
                dt[4] = java.sql.Date.valueOf("");
                dt[5] = temp[1];
                dt[6] = java.sql.Date.valueOf("");
                dt[7] = java.sql.Date.valueOf("");
            } else if (it == 3) {
                dt[0] = java.sql.Date.valueOf("");
                dt[1] = temp[0];
                dt[2] = java.sql.Date.valueOf("");
                dt[3] = temp[1];
                dt[4] = java.sql.Date.valueOf("");
                dt[5] = temp[2];
                dt[6] = java.sql.Date.valueOf("");
                dt[7] = java.sql.Date.valueOf("");
            } else if (it == 4) {
                dt[0] = java.sql.Date.valueOf("");
                dt[1] = temp[0];
                dt[2] = java.sql.Date.valueOf("");
                dt[3] = temp[1];
                dt[4] = java.sql.Date.valueOf("");
                dt[5] = temp[2];
                dt[6] = java.sql.Date.valueOf("");
                dt[7] = temp[3];
            } else {
                dt[0] = temp[0];
                ;
                for (int i = 1; i < temp.length; i++) {
                    dt[i] = temp[i];
                }
                for (int j = temp.length; j < itemp; j++) {
                    dt[j] = java.sql.Date.valueOf("");
                }
            }
        } else {
            dt = temp;
        }
        try {
            setXSeries(dt);
        } catch (ChartException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     */
    public void setXMoreSeries(String[] temp) {
        String[] dt = new String[itemp];
        int it = temp.length;
        if (it < itemp) {
            if (it == 1) {
                dt[0] = "";
                dt[1] = "";
                dt[2] = "";
                dt[3] = temp[0];
                dt[4] = "";
                dt[5] = "";
                dt[6] = "";
                dt[7] = "";
            } else if (it == 2) {
                dt[0] = "";
                dt[1] = "";
                dt[2] = temp[0];
                dt[3] = "";
                dt[4] = "";
                dt[5] = temp[1];
                dt[6] = "";
                dt[7] = "";
            } else if (it == 3) {
                dt[0] = "";
                dt[1] = temp[0];
                dt[2] = "";
                dt[3] = temp[1];
                dt[4] = "";
                dt[5] = temp[2];
                dt[6] = "";
                dt[7] = "";
            } else if (it == 4) {
                dt[0] = "";
                dt[1] = temp[0];
                dt[2] = "";
                dt[3] = temp[1];
                dt[4] = "";
                dt[5] = temp[2];
                dt[6] = "";
                dt[7] = temp[3];
            } else {
                dt[0] = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    dt[i] = temp[i];
                }
                for (int j = temp.length; j < itemp; j++) {
                    dt[j] = "";
                }
            }
        } else {
            dt = temp;
        }
        try {
            setXSeries(dt);
        } catch (ChartException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setYMoreSeries(java.lang.String seriesName, double[] temp) {
        double[] dt = new double[itemp];
        int it = temp.length;
        if (it < itemp) {
            if (it == 1) {
                dt[0] = Double.parseDouble("0");
                dt[1] = Double.parseDouble("0");
                dt[2] = Double.parseDouble("0");
                dt[3] = temp[0];
                dt[4] = Double.parseDouble("0");
                dt[5] = Double.parseDouble("0");
                dt[6] = Double.parseDouble("0");
                dt[7] = Double.parseDouble("0");
            } else if (it == 2) {
                dt[0] = Double.parseDouble("0");
                dt[1] = Double.parseDouble("0");
                dt[2] = temp[0];
                dt[3] = Double.parseDouble("0");
                dt[4] = Double.parseDouble("0");
                dt[5] = temp[1];
                dt[6] = Double.parseDouble("0");
                dt[7] = Double.parseDouble("0");
            } else if (it == 3) {
                dt[0] = 0.0;
                dt[1] = temp[0];
                dt[2] = 0.0;
                dt[3] = temp[1];
                dt[4] = 0.0;
                dt[5] = temp[2];
                dt[6] = 0.0;
                dt[7] = 0.0;
            } else if (it == 4) {
                dt[0] = Double.parseDouble("0");
                dt[1] = temp[0];
                dt[2] = Double.parseDouble("0");
                dt[3] = temp[1];
                dt[4] = Double.parseDouble("0");
                dt[5] = temp[2];
                dt[6] = Double.parseDouble("0");
                dt[7] = temp[3];
            } else {
                dt[0] = temp[0];
                ;
                for (int i = 1; i < temp.length; i++) {
                    dt[i] = temp[i];
                }
                for (int j = temp.length; j < itemp; j++) {
                    dt[j] = Double.parseDouble("0");
                }
            }
        } else {
            dt = temp;
        }
        try {
            setYSeries(seriesName, dt);
        } catch (ChartException e) {
            System.out.println(e.getMessage());
        }
    }

    public ByteArrayInputStream twoEncodeJpgFile(BufferedImage bi) {
        // log.info("关闭1");
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

        // PipedOutputStream pout = new PipedOutputStream();

        try {

            // BufferedInputStream bs = new BufferedInputStream();

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baosPDF);
            // log.info("关闭前1");
            encoder.encode(bi);
            // log.info("关闭前2");
            ByteArrayInputStream bin = new ByteArrayInputStream(baosPDF.toByteArray());

            // Hibernate.createBlob(pin);
            // log.info("关闭前3");
            baosPDF.close();
            // pout.close();
            // log.info("关闭后");
            return bin;

        } catch (Exception e) {
            // System.err.println("Cannot create ./" + name+e);
            // System.exit(1);
            // log.info("关闭" + e);
            return null;
        }
    }

    // public PipedInputStream onCreateChart(double douNew[], String username) {
    @SuppressWarnings("static-access")
    public ByteArrayInputStream onCreateChart(String[] name, double[] dou, String flag) {
        // 几条柱
        /*
         * int numCountries = strX.length; // double douNew[] = {2.1, 3.2 , 2.6
         * , 5.6 , 4.6, 4.6, 4.6}; /*String[] seriesName = new
         * String[numCountries]; for (int i = 0; i < numCountries; i++) {
         * seriesName[i] = i + 1 + ""; }
         */
        // 设置大小.
        // log.info("IN on CreatChart!");
        @SuppressWarnings("unused")
        int Chart_height = 125;
        @SuppressWarnings("unused")
        int Chart_width = 315;

        // ---------------------------模拟分数，正式情况从其它页面获得
        // X轴
        // String strX[] = {"","现实型", "研究型", "艺术型", "社会型", "企业型", "常规型"};
        //
        // Y轴
        //
        // double douNew[] = {
        // 0,23, 55, 36, 8, 43,
        // 21};

        // ------------------------------

        /*
         * double[][] FileObs = new double[1][]; FileObs[0] = douNew;
         * 
         * try { // ORACLE 的画图类
         * 
         * BarChart AxisCh = new BarChart();
         * 
         * AxisCh.setSize(Chart_width, Chart_height); // 改变轴的方向
         * AxisCh.setChartOrientation(AxisChart.HORIZONTAL); // ---------
         * 
         * AxisCh.setLegendFont(new java.awt.Font("dialog", java.awt.Font.BOLD,
         * 12)); // Set edge colors to match a white background.
         * AxisCh.setBackground(new java.awt.Color(245, 249, 227));
         * AxisCh.setEdgeColor(new java.awt.Color(232, 241, 185));
         * AxisCh.setPlotBackground(new java.awt.Color(153, 172, 185));
         * AxisCh.setPlotEdgeColor(Color.gray);
         * AxisCh.getGridDesc(AxisChart.YAXIS).setColor( new java.awt.Color(165,
         * 197, 219)); // 要显示的文字。 //AxisCh.setXMoreSeries(strX);
         * //AxisCh.setXSeries(strX); AxisCh.setXMoreSeries(strX); // 值 BarDesc
         * desc1 = new BarDesc(); // desc1.setBarStyle(BarDesc.STYLE_EFFECT3D);
         * desc1.setPointLabelsInterior();
         * desc1.setBarStyle(BarDesc.STYLE_DROP_SHADOW);
         * desc1.setBarShadowColor(new java.awt.Color(130, 243, 45));
         * desc1.setBarColor(new java.awt.Color(45, 172, 217));
         * //AxisCh.setYSeries(numCountries + "", douNew);
         * AxisCh.setYMoreSeries(numCountries + "", douNew);
         * AxisCh.setSeriesGraphic(numCountries + "", desc1); // Set title //
         * added by lty for crm2.1 // String strTitle = "整体报告"; // if (str !=
         * null) { // if (str.equalsIgnoreCase("Y")) { // strTitle = "维度得分柱状图";
         * // } // } // AxisCh.getTitle().setText(strTitle);
         * 
         * AxisCh.getSubtitle().setText("单位：分数");
         * AxisCh.getFootnote().setText(new String[] { "数据来源: 北京师大易度软件有限公司" });
         * // // Based on the input argument, alter the legend's orientation //
         * 
         * AxisCh.setLegendAlignment(oracle.charts.legend.Legend.EAST); //
         * 选择生成图片的模式 // BufferedImage bi = new BufferedImage(Chart_width,
         * Chart_height, BufferedImage.TYPE_INT_RGB);
         * 
         * Graphics2D g2 = null;
         * 
         * g2 = bi.createGraphics();
         * 
         * AxisCh.drawBuffer(g2);
         * /////////////////////////////////////////////////////////////////////
         * ///////////////////
         */
        BufferedImage bi = null;
        try {

            // 生成jpg文件
            // log.info("生成图片缓存");
            if (flag.equals("shutu")) {
                bi = this.shutu(dou);
            } else {
                bi = this.getJfreeChart(name, dou, flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return twoEncodeJpgFile(bi);

    }

    /*
     * public void drawImage(String[][] strarray, String username){ double[]
     * dsource = new double[7]; for(int strnum = 0 ; strnum < 6 ; strnum++){
     * log.info(" drawImage string[][] ****"+strarray[strnum][1].charAt(0));
     * switch(strarray[strnum][1].charAt(0)) { case 'R' :
     * dsource[1]=Double.parseDouble(strarray[strnum][0]); break; case 'I' :
     * dsource[2]=Double.parseDouble(strarray[strnum][0]); break; case 'A' :
     * dsource[3]=Double.parseDouble(strarray[strnum][0]); break; case 'S' :
     * dsource[4]=Double.parseDouble(strarray[strnum][0]); break; case 'E' :
     * dsource[5]=Double.parseDouble(strarray[strnum][0]); break; case 'C' :
     * dsource[6]=Double.parseDouble(strarray[strnum][0]); break; } } log.info("
     * double drawImage method ****
     * "+dsource[0]+dsource[1]+dsource[2]+dsource[3]+dsource[4]+dsource[5]+
     * dsource[6]); //this.onCreateChart(dsource,username); }
     */

    public static void main(String[] argv) {
        try {
            @SuppressWarnings("unused")
            BarChart bc = new BarChart();
        } catch (Exception e) {
            System.out.print("no " + e);
        }
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
        @SuppressWarnings("unused")
        String factStr = "百分等级";
        double[] doub = setDouble(dou);
        String[] nameStr = setString1(name);
        if (flag.equals("1")) {

            for (int i = 0; i < doub.length; i++) {
                if (i == 0) {
                    defaultcategorydataset.addValue(3.593000000000000D, avgStr + str[0], category1);// value
                                                                                                    // row
                                                                                                    // key
                                                                                                    // column
                                                                                                    // key
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
                    defaultcategorydataset.addValue(1.500000000000000D, avgStr + str[0], category1);// value
                                                                                                    // row
                                                                                                    // key
                                                                                                    // column
                                                                                                    // key
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
    private static JFreeChart createChart(CategoryDataset categorydataset, String[] name, double[] dou, String flag) {
        JFreeChart jfreechart = ChartFactory.createStackedBarChart(null// 图形标题名称
                , null, // 纵坐标的lable
                null, // 横坐标的lable
                categorydataset // dataset
                , PlotOrientation.HORIZONTAL // 水平显示
                , true// legeng? 是否显示图例
                , true // tooltips? 是否工具提示
                , false // URLS? 是否做连接
        );
        jfreechart.setBackgroundPaint(Color.white); // 设定背景色为白色
        /*
         * 过JFreeChart 对象获得它，然后再通过它对图形内部部分 （例：折线的类型）调整。同样，针对不同类型的报表图，它有
         * 着不同的子类实现！在下面我们简称它为 Renderer
         * 
         */
        GroupedStackedBarRenderer groupedstackedbarrenderer = new GroupedStackedBarRenderer();
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
            ;

            for (int i = 0; i < nameStr.length; i++) {
                if (nameStr[i].equals("a0")) {
                    GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, new Color(34, 34, 255), 0.0F, 0.0F,
                            new Color(136, 136, 255));// 蓝色
                    groupedstackedbarrenderer.setSeriesPaint(i * 5 + 0, gradientpaint);
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

        groupedstackedbarrenderer.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL));
        SubCategoryAxis subcategoryaxis = new SubCategoryAxis("");
        String[] nameStrr = setString(name);
        for (int i = 0; i < nameStrr.length; i++) {
            subcategoryaxis.setCategoryMargin(0.050000000000000003D);
            subcategoryaxis.addSubCategory(nameStrr[i]);
        }

        // org.jfree.chart.plot.Plot 通过JFreeChart 对象获得它，然后再通过它对图形外部部分（例：坐标轴）调整
        // 注意：它有很多子类，一般都下嗍造型到它的子类！
        CategoryPlot categoryplot = jfreechart.getCategoryPlot(); // 获得
        // plot：CategoryPlot！！

        categoryplot.setBackgroundPaint(Color.lightGray); // 设定图表数据显示部分背景色
        categoryplot.setDomainGridlinePaint(Color.white); // 横坐标网格线白色
        categoryplot.setDomainGridlinesVisible(true); // 可见
        categoryplot.setRangeGridlinePaint(Color.white); // 纵坐标网格线白色
        categoryplot.setDomainAxis(subcategoryaxis);
        categoryplot.setRenderer(groupedstackedbarrenderer);
        categoryplot.setFixedLegendItems(createLegendItems());
        ;
        ValueAxis rangeAxis = categoryplot.getRangeAxis();
        // 设置最高的一个 Item 与图片顶端的距离
        // rangeAxis.setUpperMargin(0.15);
        // 设置最低的一个 Item 与图片底端的距离
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
        categoryplot.setRangeAxis(rangeAxis);
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

    public static BufferedImage getJfreeChart(String[] name, double[] dou, String flag) {
        JFreeChart jfreechart = createChart(createDataset(name, dou, flag), name, dou, flag);
        int Chart_height = 125;
        int Chart_width = 315;
        BufferedImage bi = jfreechart.createBufferedImage(Chart_width, Chart_height);
        return bi;
    }

    // ///////////////竖图
    public BufferedImage shutu(double douNew[]) {
        // 几条柱
        int numCountries = 7;
        String[] seriesName = new String[numCountries];
        for (int i = 0; i < numCountries; i++) {
            seriesName[i] = i + 1 + "";
        }
        // 设置大小.
        int Chart_height = 320;
        int Chart_width = 500;

        // ---------------------------模拟分数，正式情况从其它页面获得
        // X轴
        String strX[] = { "", "现实型", "研究型", "艺术型", "社会型", "企业型", "常规型" };
        //
        // Y轴
        //
        // double douNew[] = {
        // 0,23, 55, 36, 8, 43,
        // 21};

        // ------------------------------

        // double[][] FileObs = new double[1][];
        // FileObs[0] = douNew;

        try {
            // ORACLE 的画图类
            @SuppressWarnings("unused")
            String fileName = "test";

            BarChart AxisCh = new BarChart();
            AxisCh.setSize(Chart_width, Chart_height);
            // 改变轴的方向
            // AxisCh.setChartOrientation(AxisChart.HORIZONTAL);
            // ---------

            AxisCh.setLegendFont(new java.awt.Font("dialog", java.awt.Font.BOLD, 12));
            // Set edge colors to match a white background.
            AxisCh.setBackground(new java.awt.Color(255, 255, 255));
            AxisCh.setEdgeColor(new java.awt.Color(255, 255, 255));
            AxisCh.setPlotEdgeColor(new java.awt.Color(255, 255, 255));
            AxisCh.getGridDesc(AxisChart.YAXIS).setColor(new java.awt.Color(192, 192, 192));

            //
            // Load the timestamps.
            // We assume that the dates in the first
            // file are identical to the dates in other files.
            //
            AxisCh.setXMoreSeries(strX);

            //
            // Load observations for each country.
            //

            // for (int i = 0; i < 1; i++) {
            // BarDesc desc1 = new BarDesc();
            // desc1.setBarStyle(BarDesc.STYLE_EFFECT3D);
            // desc1.setBarColor(new java.awt.Color(102, 102, 255));
            // AxisCh.setYSeries(seriesName[i], FileObs[i]);
            // AxisCh.setSeriesGraphic(seriesName[i], desc1);
            // }
            BarDesc desc1 = new BarDesc();
            desc1.setBarStyle(BarDesc.STYLE_EFFECT3D);
            desc1.setBarColor(new java.awt.Color(102, 102, 255));
            AxisCh.setYSeries(numCountries + "", douNew);
            AxisCh.setSeriesGraphic(numCountries + "", desc1);

            // Set title
            // added by lty for crm2.1
            // String strTitle = "整体报告";
            String strTitle = "";
            // if (str != null) {
            // if (str.equalsIgnoreCase("Y")) {
            // strTitle = "维度得分柱状图";
            // }
            // }
            AxisCh.getTitle().setText(strTitle);

            // AxisCh.getSubtitle().setText("单位：分数");
            AxisCh.getFootnote().setText(new String[] { "数据来源: 北京师大易度软件有限公司" });

            //
            // Based on the input argument, alter the legend's orientation
            //

            AxisCh.setLegendAlignment(oracle.charts.legend.Legend.EAST);
            // 选择生成图片的模式
            //
            BufferedImage bi = new BufferedImage(Chart_width, Chart_height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2 = null;

            g2 = bi.createGraphics();

            AxisCh.drawBuffer(g2);

            // 生成jpg文件
            // EncodeJpgFile(bi, fileName);
            return bi;
        } catch (ChartException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

}