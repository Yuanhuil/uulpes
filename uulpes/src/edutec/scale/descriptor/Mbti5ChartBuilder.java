package edutec.scale.descriptor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.text.TextBlock;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import heracles.jfree.builder.BarChartBuilder;
import heracles.jfree.builder.RendererUtils;

@SuppressWarnings("unchecked")
public class Mbti5ChartBuilder extends BarChartBuilder {
    public static double COMP_VAL = 3.5D;

    @Override
    protected JFreeChart buildJFreeChart(Dataset ds) {
        ValueAxis valueaxis0 = new MbtiValueAxis();
        ValueAxis valueaxis1 = new MbtiValueAxis();
        CategoryPlot plot = new CategoryPlot();
        plot.setDataset((CategoryDataset) ds);
        plot.setDataset(1, (CategoryDataset) ds);
        plot.setRangeAxis(valueaxis0);
        plot.setRangeAxis(1, valueaxis1);
        CategoryAxis categoryaxis0 = new MbtiCategoryAxis(
                ArrayUtils.toMap(new String[][] { { "W1", "内倾" }, { "W2", "直觉" }, { "W3", "情感" }, { "W4", "知觉" } }));
        CategoryAxis categoryaxis1 = new MbtiCategoryAxis(
                ArrayUtils.toMap(new String[][] { { "W1", "外倾" }, { "W2", "感觉" }, { "W3", "思维" }, { "W4", "判断" } }));
        plot.setDomainAxis(0, categoryaxis0);
        plot.setDomainAxis(1, categoryaxis1);

        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToDomainAxis(1, 1);
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRangeGridlinePaint(Color.BLUE);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.CYAN);

        // CategoryDataset cds = (CategoryDataset) ds;
        // cds.getColumnKeys();
        JFreeChart jc = new JFreeChart("", null, plot, false);
        return jc;
    }

    @Override
    protected void config() {
    }

    @Override
    protected void confRender() {
        CategoryPlot categoryplot = this.getPlot();
        categoryplot.setAxisOffset(RectangleInsets.ZERO_INSETS);
        categoryplot.setOutlineVisible(false);
        BarRenderer render = RendererUtils.createBarRenderer();
        render.setBarPainter(new StandardBarPainter());
        render.setItemMargin(-1);
        render.setBaseItemLabelGenerator(new MbtiLabelGenerator());
        render.setBaseItemLabelFont(new Font("宋体", Font.PLAIN, 12));
        render.setBaseItemLabelsVisible(true);
        render.setShadowVisible(false);
        render.setMaximumBarWidth(0.085D);
        categoryplot.setRenderer(render);
    }

    class MbtiValueAxis extends NumberAxis {
        private static final long serialVersionUID = 1L;

        public MbtiValueAxis() {
            this.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            this.setRange(0 - COMP_VAL, 7 - COMP_VAL);
        }

        @Override
        public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
            List list = super.refreshTicks(g2, state, dataArea, edge);
            List result = new ArrayList(list.size());
            for (Object o : list) {
                NumberTick tick = (NumberTick) o;
                NumberTick newTick = new NumberTick(tick.getNumber().doubleValue(),
                        (tick.getNumber().doubleValue() + COMP_VAL) + "", tick.getTextAnchor(),
                        tick.getRotationAnchor(), tick.getAngle());
                result.add(newTick);
            }
            return result;
        }
    }

    class MbtiCategoryAxis extends CategoryAxis {
        private static final long serialVersionUID = 1L;
        private Map labels;

        public MbtiCategoryAxis(Map labels) {
            super();
            this.labels = labels;
            this.setTickLabelFont(new Font("黑体", Font.PLAIN, 12));
        }

        @Override
        protected TextBlock createLabel(Comparable category, float width, RectangleEdge edge, Graphics2D g2) {
            TextBlock block = super.createLabel((String) labels.get(category), width, edge, g2);
            return block;
        }

    }

    static class MbtiLabelGenerator extends AbstractCategoryItemLabelGenerator implements CategoryItemLabelGenerator {
        public MbtiLabelGenerator() {
            super("", NumberFormat.getInstance());
        }

        private static final long serialVersionUID = 1L;

        public String generateLabel(CategoryDataset categorydataset, int i, int j) {
            Number number = categorydataset.getValue(i, j);
            number = number.doubleValue() + COMP_VAL;
            return String.valueOf(number.intValue());
        }
    }

}
