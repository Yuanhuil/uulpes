package heracles.jfree.builder;

import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.Dataset;

import heracles.jfree.bean.ChartParamBean;
import heracles.jfree.bean.PlotBean;

public abstract class PlotBuilderSupport implements ChartBuilder {
    protected static Font LABEL_FONT = new Font("宋体", Font.PLAIN, 14);

    protected ChartParamBean chartParamBean;
    protected JFreeChart freeChart;

    public ChartParamBean getChartParamBean() {
        return chartParamBean;
    }

    public void setChartParamBean(ChartParamBean chartParamBean) {
        this.chartParamBean = chartParamBean;
    }

    public JFreeChart doBuilder() {
        Dataset ds = buildDataset();
        freeChart = buildJFreeChart(ds);
        confPlot();
        confRender();
        config();
        return freeChart;
    }

    abstract protected Dataset buildDataset();

    abstract protected JFreeChart buildJFreeChart(Dataset ds);

    abstract protected void config();

    protected void confPlot() {
        // 设置样式
        PlotBean plotBean = new PlotBean(chartParamBean.getChplot());
        Plot plot = freeChart.getPlot();
        plotBean.confPlot(plot);
    }

    abstract protected void confRender();
}
