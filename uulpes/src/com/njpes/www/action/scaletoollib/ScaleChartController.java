package com.njpes.www.action.scaletoollib;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.math.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import edutec.scale.questionnaire.CeeImg;
import edutec.scale.util.BarChart;
import edutec.scale.util.ImageData;
import heracles.jfree.CustomBarRenderer3D;
import heracles.util.SimpleCodec;
import heracles.util.UtilCollection;
import heracles.web.servlet.ServletRequestUtils;
import oracle.charts.axischart.AxisChart;
import oracle.charts.types.BarDesc;
import oracle.charts.types.ChartException;

//import edutec.web.util.JFreeChartExample;
@Controller
@RequestMapping("/assessmentcenter/report")
public class ScaleChartController {
    /*******************************************************************************
     * 1）propChart.cht?d=b7a8d6c6b3ccb6c8:2.85:3:0
     * 2）propChart.cht?d=d7d4ced2b3ccb6c8,b7a8d6c6b3ccb6c8,b5c0b5c2b3ccb6c8,d0c5d1f6b3ccb6c8:2.73,2.85,3.33,2.9:1,2,3,4:0
     */
    /**
     * 维度图片 0为五点分的图 1为百分比的图
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/scalechart", method = RequestMethod.GET)
    public void propChartAction(HttpServletRequest req, HttpServletResponse resp) {
        String d = ServletRequestUtils.getStringParameter(req, CeeImg.DATASET_PARAM, null); // 显示数据
        String w = ServletRequestUtils.getStringParameter(req, "w", null); // 宽度
        String h = ServletRequestUtils.getStringParameter(req, "h", null); // 高度
        String ort = ServletRequestUtils.getStringParameter(req, "org", null); // 高度
        int picWidth, picHeight;
        if (w == null)
            picWidth = 500;
        else
            picWidth = Integer.parseInt(w);
        if (h == null)
            picHeight = 400;
        else
            picHeight = Integer.parseInt(w);
        if (ort == null)
            ort = "v";

        int i = d.indexOf(UtilCollection.COLON);
        int j = d.indexOf(UtilCollection.COLON, i + 1);
        int k = d.indexOf(UtilCollection.COLON, j + 1);
        String name = d.substring(0, i);// 维度名称
        String cent = d.substring(i + 1, j);// T分数
        String grade = d.substring(j + 1, k);// 得分等级
        String flag = d.substring(k + 1, d.length());// 图表类型
        List<String> centlist = UtilCollection.toList(cent, UtilCollection.COMMA);
        List<String> namelist = UtilCollection.toList(name, UtilCollection.COMMA);
        List<String> gradelist = UtilCollection.toList(grade, UtilCollection.COMMA);
        double[] dd = new double[centlist.size()];
        int idx = 0;
        for (String s : centlist) {
            dd[idx++] = NumberUtils.toDouble(s);
        }
        idx = 0;
        String[] ns = new String[namelist.size()];
        for (String s : namelist) {
            ns[idx++] = SimpleCodec.dehex(s);
        }
        idx = 0;
        // Color[] cc = new Color[gradelist.size()];
        List<Color> colorList = new ArrayList<Color>();
        for (String s : gradelist) {
            int scoreGrade = NumberUtils.toInt(s);
            Color color = getBarColor(scoreGrade);
            colorList.add(color);
            // cc[idx++] = color;
        }
        ImageData picsix = new ImageData();
        picsix.setFlag(flag); // 五点分的
        picsix.setPicdatename(ns);
        picsix.setPicdatenum(dd);
        // picsix.setBarcolor(cc);
        picsix.setPicname("pic");
        picsix.setKind("xuke");
        streamImage(picsix.getPicdatename(), resp, picsix.getPicdatenum(), picsix.getFlag(), picsix, colorList,
                picWidth, picHeight, ort);
    }

    private void streamImage(String[] name, HttpServletResponse resp, double[] num, String flag, ImageData picc,
            List<Color> colorList, int picWidth, int picHeight, String orient) {
        CategoryDataset createdataset = createDataset(name, num, flag);
        // JFreeChart jf = JFreeChartExample.createChart(createdataset, name,
        // num, flag,"");
        PlotOrientation port = PlotOrientation.VERTICAL;
        if (orient.equals("v"))
            port = PlotOrientation.VERTICAL;
        if (orient.equals("h"))
            port = PlotOrientation.HORIZONTAL;
        JFreeChart jf = ChartFactory.createBarChart3D("测验得分统计图", "维度", "得分(T)", createdataset, port, false, false,
                false);
        CategoryPlot plot = (CategoryPlot) jf.getCategoryPlot();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double maxval = StatUtils.max(num);
        if (maxval > 10)
            rangeAxis.setRange(0, 100);
        else
            rangeAxis.setRange(0, 10);

        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 显示每个柱的数值，并修改该数值的字体属性
        // BarRenderer3D renderer = new BarRenderer3D();
        // List<Color> colorList = new ArrayList<Color>();
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

        // jf.getLegend().setItemFont(new Font("宋体",Font.PLAIN,16));
        jf.getTitle().setFont(new Font("黑体", Font.PLAIN, 13)); // 标题
        CategoryAxis categoryaxis = plot.getDomainAxis(); // 横轴上的
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 45度倾斜,可以改成其他,默认是水平
        categoryaxis.setTickLabelFont(new Font("微软雅黑", 10, 12));// 设定字体、类型、字号
        // categoryaxis.setTickLabelFont(new Font("SimSun", 10, 12));//

        // BarRenderer3D customBarRenderer = (BarRenderer3D) plot.getRenderer();
        // Color[] colors = picc.getBarcolor();
        // for(int i=0;i<colors.length;i++)
        // {
        // renderer.setSeriesPaint(i, colors[i]); //
        // }
        plot.setRenderer(renderer);

        // int Chart_height = 160;
        // int Chart_width = 400;
        BufferedImage bi = null;
        if (picc.getKind().equals("shutu")) {
            // bi = this.shutu(num);
        } else {
            // bi = jf.createBufferedImage(Chart_width, Chart_height);
        }
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            if (picc.getKind().equals("shutu")) {
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
                bi = this.shutu(num);
                encoder.encode(bi);
                os.flush();
            } else {
                resp.setContentType("image/png");
                ChartUtilities.writeChartAsPNG(os, jf, picWidth, picHeight);
                resp.flushBuffer();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private BufferedImage shutu(double douNew[]) {
        // 几条柱
        int numCountries = 7;
        String[] seriesName = new String[numCountries];
        for (int i = 0; i < numCountries; i++) {
            seriesName[i] = i + 1 + "";
        }
        // 设置大小.
        int Chart_height = 282;
        int Chart_width = 440;

        // ---------------------------模拟分数，正式情况从其它页面获得
        // X轴
        String strX[] = { "", "现实型", "研究型", "艺术型", "社会型", "企业型", "常规型" };
        // double[][] FileObs = new double[1][];
        // FileObs[0] = douNew;

        try {
            // ORACLE 的画图类

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
            AxisCh.getFootnote().setText(new String[] { "数据来源: 北京师大易度科技有限公司" });
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

    private CategoryDataset createDataset(String[] name, double[] dou, String flag) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        for (int i = 0; i < dou.length; i++) {
            defaultcategorydataset.addValue(dou[i], "", name[i]);
        }
        return defaultcategorydataset;
    }

    private CategoryDataset createDataset1(String[] name, double[] dou, Color[] barColor, String flag) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        String category1 = "";
        String[] str = new String[] { " (非常差)", " (比较差)", " (中等)", " (比较好)", " (非常好)" };
        String avgStr = "评分区间";
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

    private double[] setDouble(double[] dou) {
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

    private String[] setString1(String[] name) {
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

    private Color getBarColor(int grade) {
        switch (grade) {
        case 1:
            return Color.red;
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
