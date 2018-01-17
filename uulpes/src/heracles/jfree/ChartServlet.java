package heracles.jfree;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import heracles.jfree.bean.ChartParamBean;

public class ChartServlet extends HttpServlet {
    private static final long serialVersionUID = -4004374794551337762L;

    public ChartServlet() {
        // nothing required
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = response.getOutputStream();
        try {
            JFreeChart chart = null;
            int w = 0;
            int h = 0;
            String qryStr = request.getQueryString();
            if (StringUtils.startsWithIgnoreCase(qryStr, "bean=")) {
                ChartParamBean paramBean = new ChartParamBean();
                // qryStr = StringEscapeUtils.unescapeHtml(qryStr);
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
                response.setContentType("image/png");
                // *** CHART SIZE ***
                ChartUtilities.writeChartAsPNG(out, chart, w, h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

}
