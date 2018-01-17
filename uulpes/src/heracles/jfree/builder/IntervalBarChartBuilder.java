package heracles.jfree.builder;

import java.awt.Color;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

public class IntervalBarChartBuilder extends BarChartBuilder {

    @Override
    protected void confRender() {
        CategoryPlot categoryplot = this.getPlot();
        IntervalBarRenderer render = new IntervalBarRenderer();
        render.setBarPainter(new StandardBarPainter());
        // render.setItemMargin(-1);
        render.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        render.setBaseItemLabelFont(LABEL_FONT);
        // render.setBaseItemLabelsVisible(true);
        render.setShadowVisible(false);
        render.setDrawBarOutline(true);
        render.setMaximumBarWidth(0.06D);
        categoryplot.setRenderer(render);
    }

    @Override
    protected void confPlot() {
        super.confPlot();
        CategoryPlot categoryplot = this.getPlot();
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setRangeGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(Color.BLUE);
        categoryplot.setDomainGridlinePaint(Color.lightGray);
    }

}
