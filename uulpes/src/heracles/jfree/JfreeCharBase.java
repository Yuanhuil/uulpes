package heracles.jfree;

import java.awt.Font;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

public abstract class JfreeCharBase {
    protected String title;
    protected boolean legend;
    protected boolean tooltips;
    protected boolean urls;
    protected PlotOrientation orientation;
    protected boolean is3d;

    private Font defaultFont;
    private Font defaultTitleFont;

    final protected Map<String, Object> params;

    protected JfreeCharBase(final Map<String, Object> params) {
        this.params = params;
        init();
    }

    protected void init() {
        title = (String) params.get(ChartConstants.TITLE);
        String ort = (String) params.get(ChartConstants.CHART_ORIENTATION);
        ort = StringUtils.defaultIfEmpty(ort, "v");
        orientation = ort.equalsIgnoreCase("v") ? PlotOrientation.VERTICAL : PlotOrientation.HORIZONTAL;
        legend = ((String) params.get(ChartConstants.LEGEND)) == null ? false : true;
        tooltips = ((String) params.get(ChartConstants.TOOLTIPS)) == null ? false : true;
        urls = ((String) params.get(ChartConstants.URLS)) == null ? false : true;
        is3d = ((String) params.get(ChartConstants.IS_3D)) == null ? false : true;
        defaultFont = new Font("Dialog", Font.PLAIN, 12);
        defaultTitleFont = new Font("Dialog", Font.BOLD, 24);
    }

    public abstract JFreeChart getChart();

    protected abstract void buildChart();

    public Font getDefaultFont() {
        return defaultFont;
    }

    public boolean isIs3d() {
        return is3d;
    }

    public Font getDefaultTitleFont() {
        return defaultTitleFont;
    }

    public void setDefaultTitleFont(Font defaultTitleFont) {
        this.defaultTitleFont = defaultTitleFont;
    }

    public void setDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
    }
}
