package heracles.jfree;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;

public abstract class CategoryChartBase extends JfreeCharBase {
    protected String categoryAxisLabel;
    protected String valueAxisLabel;

    protected JFreeChart jfreechart;
    protected CategoryPlot categoryplot;
    protected NumberAxis numberaxis;
    protected CategoryAxis domainAxis;

    protected CategoryDataset categoryDataset;

    protected CategoryChartBase(final Map<String, Object> params) {
        super(params);
    }

    protected void init() {
        super.init();
        categoryAxisLabel = (String) params.get(ChartConstants.CATEGORY_AXIS_LABEL);
        valueAxisLabel = (String) params.get(ChartConstants.VALUE_AXIS_LABEL);
        buildDataSet();
        buildChart();
        postSetChartProps();
    }

    protected abstract void buildDataSet();

    protected void postSetChartProps() {
        if (StringUtils.isNotEmpty(this.title)) {
            TextTitle txttitle = new TextTitle(this.title, getDefaultTitleFont());
            jfreechart.setTitle(txttitle);
        }
        categoryplot = jfreechart.getCategoryPlot();
        domainAxis = categoryplot.getDomainAxis();
        numberaxis = (NumberAxis) categoryplot.getRangeAxis();

        domainAxis.setTickLabelFont(getDefaultFont());
        domainAxis.setLabelFont(getDefaultFont());
        numberaxis.setTickLabelFont(getDefaultFont());
        numberaxis.setLabelFont(getDefaultFont());

        String range = (String) params.get(ChartConstants.RANGE);
        if (StringUtils.isNotEmpty(range)) {
            String sts[] = range.split("-");
            numberaxis.setRange(NumberUtils.toDouble(sts[0]), NumberUtils.toDouble(sts[1]));
        }
    }

    @Override
    public JFreeChart getChart() {
        return jfreechart;
    }

    public CategoryPlot getPlot() {
        return categoryplot;
    }

    public NumberAxis getNumberaxis() {
        return numberaxis;
    }

    public CategoryAxis getDomainAxis() {
        return domainAxis;
    }

}
