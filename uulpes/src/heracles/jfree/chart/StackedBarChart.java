package heracles.jfree.chart;

import java.awt.Color;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

import heracles.jfree.CategoryChartBase;
import heracles.jfree.ChartConstants;

public class StackedBarChart extends CategoryChartBase {
    public final StackedDataSet dataSet;
    public final Map<String, Object> params;
    private BarRenderer barRenderer;

    public StackedBarChart(final Map<String, Object> params) {
        this(params, false);
    }

    public StackedBarChart(final Map<String, Object> params, boolean hasColumns) {
        super(params);
        this.params = params;
        if (hasColumns) {
            Object obj = params.get(ChartConstants.COLUMNS);
            Validate.notNull(obj, "没有提供列");
            this.dataSet = new StackedDataSet((String[]) obj);
        } else {
            this.dataSet = new StackedDataSet();
        }
    }

    public void addSeries(String seriesName, Number[] values) {
        dataSet.addSeries(seriesName, values);
    }

    public void addValue(Number value, Comparable<?> rowKey, Comparable<?> columnKey) {
        dataSet.addValue(value, rowKey, columnKey);
    }

    public JFreeChart getChart() {
        super.getChart();
        barRenderer = (BarRenderer) categoryplot.getRenderer();
        barRenderer.setBaseLegendTextFont(getDefaultFont());
        barRenderer.setBaseItemLabelFont(getDefaultFont());
        Color cl = new Color(254, 238, 238);
        categoryplot.setBackgroundPaint(cl); // 设定图表数据显示部分背景色
        categoryplot.setDomainGridlinePaint(Color.gray); // 横坐标网格线白色
        categoryplot.setDomainGridlinesVisible(true); // 可见
        categoryplot.setRangeGridlinePaint(Color.gray); // 纵坐标网格线白色
        jfreechart.setBackgroundPaint(cl);
        return jfreechart;
    }

    public void buildChart() {
        if (this.is3d) {
            jfreechart = ChartFactory.createStackedBarChart3D(null, categoryAxisLabel, valueAxisLabel,
                    dataSet.getDataset(), orientation, legend, tooltips, urls);
        } else {
            jfreechart = ChartFactory.createStackedBarChart(null, categoryAxisLabel, valueAxisLabel,
                    dataSet.getDataset(), orientation, legend, tooltips, urls);
        }
    }

    public BarRenderer getBarRenderer() {
        return barRenderer;
    }

    public void setDrawBarOutline(boolean draw) {
        barRenderer.setDrawBarOutline(draw);
    }

    public void setRenderer(CategoryItemRenderer renderer) {
        categoryplot.setRenderer(renderer);
    }

    public void setItemLabelsVisible(boolean visible) {
        barRenderer.setBaseItemLabelsVisible(visible);
        if (visible)
            barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    }

    @Override
    protected void buildDataSet() {

    }

}
