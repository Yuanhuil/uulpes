package heracles.jfree.chart;

import java.awt.Color;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import heracles.jfree.CategoryChartBase;
import heracles.jfree.ChartConstants;
import heracles.util.UtilCollection;

public class LineChart extends CategoryChartBase {
    @Override
    protected void postSetChartProps() {
        super.postSetChartProps();
        CategoryPlot categoryplot = getPlot();
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setRangeGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(Color.BLUE);
        categoryplot.setDomainGridlinePaint(Color.BLUE);
        categoryplot.setBackgroundPaint(Color.WHITE);
    }

    private LineAndShapeRenderer lineandshaperenderer;

    public LineChart(Map<String, Object> params) {
        super(params);
    }

    @Override
    protected void buildChart() {
        this.jfreechart = ChartFactory.createLineChart(null, categoryAxisLabel, valueAxisLabel, categoryDataset,
                orientation, legend, tooltips, urls);
        categoryplot = jfreechart.getCategoryPlot();
        lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();

    }

    public void addValue(Number value, Comparable<?> rowKey, Comparable<?> columnKey) {
        ((DefaultCategoryDataset) categoryDataset).addValue(value, rowKey, columnKey);
    }

    public LineAndShapeRenderer getRenderer() {
        return lineandshaperenderer;
    }

    @Override
    protected void buildDataSet() {
        this.categoryDataset = new DefaultCategoryDataset();
        String data = (String) params.get(ChartConstants.DATA);
        if (StringUtils.isEmpty(data)) {
            return;
        }
        Map<String, String> map = UtilCollection.toMap(data, UtilCollection.SEMICOLON, UtilCollection.EQ);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.addValue(NumberUtils.createNumber(entry.getValue()), "", entry.getKey());
        }
    }

}
