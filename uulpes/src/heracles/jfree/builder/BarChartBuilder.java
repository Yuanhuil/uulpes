package heracles.jfree.builder;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.ui.RectangleInsets;

public class BarChartBuilder extends CategoryPlotChartBuilder {

    @Override
    protected JFreeChart buildJFreeChart(Dataset ds) {
        JFreeChart freeChart = ChartFactory.createBarChart("", "", "", (CategoryDataset) ds, PlotOrientation.HORIZONTAL,
                false, false, false);
        return freeChart;
    }

    @Override
    protected void confRender() {
        CategoryPlot categoryplot = this.getPlot();
        categoryplot.setAxisOffset(new RectangleInsets(1.0, 0.0, 1.0, 0.0));
        categoryplot.setOutlineVisible(false);
        BarRenderer render = RendererUtils.createBarRenderer();
        render.setBarPainter(new StandardBarPainter());
        render.setItemMargin(-1);
        render.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        render.setBaseItemLabelFont(new Font("宋体", Font.PLAIN, 12));
        render.setBaseItemLabelsVisible(true);
        render.setShadowVisible(false);
        // render.setDrawBarOutline(true);
        render.setMaximumBarWidth(0.085D);
        categoryplot.setRenderer(render);
    }

}
