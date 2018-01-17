package heracles.jfree.builder;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.apache.commons.lang.math.DoubleRange;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.data.general.Dataset;
import org.jfree.ui.Layer;

import heracles.jfree.bean.AxisBean;
import heracles.jfree.bean.AxisType;
import heracles.jfree.bean.DatasetBean;
import heracles.jfree.bean.MarkerBean;
import heracles.jfree.bean.MarkerType;
import heracles.util.UtilCollection;

public abstract class CategoryPlotChartBuilder extends PlotBuilderSupport {

    @Override
    protected void config() {
        confMarkers(); // 配置标记
        confCategoryAxis();// 配置类别轴
        confValueaxis();// 配置数值轴
    }

    protected void confMarkers() {
        CategoryPlot categoryplot = getPlot();
        String chrm = this.chartParamBean.getChrm();
        List<String> rmItems = UtilCollection.toList(chrm, '|');
        for (String rmItem : rmItems) {
            MarkerBean markerBean = new MarkerBean(rmItem);
            Marker marker = markerBean.newMarker();
            if (markerBean.getType() == MarkerType.interval) {
                categoryplot.addRangeMarker(marker);
            } else {
                categoryplot.addDomainMarker((CategoryMarker) marker, Layer.BACKGROUND);
            }
        }
    }

    protected void confValueaxis() {
        CategoryPlot categoryplot = getPlot();
        ValueAxis valueaxis = categoryplot.getRangeAxis();
        valueaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        valueaxis.setLowerMargin(0.08);
        valueaxis.setUpperMargin(0.1);
        valueaxis.setAxisLinePaint(Color.BLACK);
        valueaxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        // valueaxis.setFixedDimension(15);
        DoubleRange range = chartParamBean.getChdRange();
        if (range != null) {
            valueaxis.setAutoRange(false);
            valueaxis.setRange(range.getMinimumDouble(), range.getMaximumDouble());
        }
    }

    protected void confCategoryAxis() {
        CategoryAxis domainAxis = getPlot().getDomainAxis();
        domainAxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
        domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 12));
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setAxisLinePaint(Color.BLACK);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setCategoryMargin(0.05);
        AxisBean bean = chartParamBean.getAxisBean();
        if (bean != null && bean.getType() == AxisType.category) {
            if (bean.has("labpos") && bean.getInt("labpos") == 90) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            }
            if (bean.has("fnsz")) {
                Font fn = new Font("宋体", Font.PLAIN, bean.getInt("fnsz"));
                domainAxis.setTickLabelFont(fn);
            }
        }

    }

    protected Dataset buildDataset() {
        DatasetBean bean = new DatasetBean(chartParamBean.getChd());
        return bean.newDataset();
    }

    protected CategoryPlot getPlot() {
        CategoryPlot categoryplot = (CategoryPlot) freeChart.getPlot();
        return categoryplot;
    }
}
