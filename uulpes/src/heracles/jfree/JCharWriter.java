package heracles.jfree;

import java.awt.Dimension;
import java.io.File;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import heracles.jfree.bean.ChartParamBean;

public class JCharWriter {
    static public void write(String qryStr, File file) throws Exception {
        OutputStream out = FileUtils.openOutputStream(file);
        write(qryStr, out);
        IOUtils.closeQuietly(out);
    }

    static public void write(String qryStr, OutputStream out) throws Exception {
        JFreeChart chart = null;
        int w = 0;
        int h = 0;
        if (StringUtils.startsWithIgnoreCase(qryStr, "bean=")) {
            ChartParamBean paramBean = new ChartParamBean();
            qryStr = StringEscapeUtils.unescapeHtml(qryStr);
            paramBean.urlDecode(qryStr);
            Dimension dimension = paramBean.getSize();
            w = (int) dimension.getWidth();
            h = (int) dimension.getHeight();
            chart = paramBean.createChartBuilder().doBuilder();
        } else {
            JChartCreator chartCreator = new JChartCreator();
            JChartParam chartParam = new JChartParam().parseString(qryStr);
            chartCreator.setChartParam(chartParam);
            chart = chartCreator.getChart();
            w = chartParam.getWidth() == 0 ? 200 : chartParam.getWidth();
            h = chartParam.getHeight() == 0 ? 125 : chartParam.getHeight();
        }
        if (chart != null) {
            ChartUtilities.writeChartAsPNG(out, chart, w, h);
        }
    }
}
