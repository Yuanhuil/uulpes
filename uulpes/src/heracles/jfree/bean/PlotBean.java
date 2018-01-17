package heracles.jfree.bean;

import java.awt.Color;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;

public class PlotBean extends JsonBean {
    public PlotBean() {
        super();
    }

    public PlotBean(String jsonStr) {
        super(jsonStr);
    }

    /**
     * 默认为Category
     * 
     * @return
     */
    public PlotType getType() {
        String type = this.has(Consts.TYPE) ? this.getString(Consts.TYPE) : Consts.Category;
        return PlotType.getPlotType(type);
    }

    public void confPlot(Plot plot) {
        plot.setBackgroundPaint(Color.WHITE);
        String ort = this.has("ort") ? this.getString("ort") : "v"; // ，默认为垂直
        if (PlotType.category == this.getType()) {
            CategoryPlot categoryplot = (CategoryPlot) plot;
            if (ort.equalsIgnoreCase("v")) {
                categoryplot.setOrientation(PlotOrientation.VERTICAL);
            } else {
                categoryplot.setOrientation(PlotOrientation.HORIZONTAL);
            }
        }
    }

}
