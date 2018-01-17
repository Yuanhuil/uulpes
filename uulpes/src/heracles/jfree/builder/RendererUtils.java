package heracles.jfree.builder;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

import org.jfree.chart.renderer.category.BarRenderer;

public class RendererUtils {
    static Paint apaint[] = createPaint();

    private static Paint[] createPaint() {
        Paint apaint[] = new Paint[10];
        apaint[0] = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, Color.white);
        apaint[1] = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, Color.white);
        apaint[2] = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, Color.white);
        apaint[3] = new GradientPaint(0.0F, 0.0F, Color.orange, 0.0F, 0.0F, Color.white);
        apaint[4] = new GradientPaint(0.0F, 0.0F, Color.magenta, 0.0F, 0.0F, Color.white);
        apaint[5] = new GradientPaint(0.0F, 0.0F, Color.cyan, 0.0F, 0.0F, Color.white);
        apaint[6] = new GradientPaint(0.0F, 0.0F, Color.lightGray, 0.0F, 0.0F, Color.white);
        apaint[7] = new GradientPaint(0.0F, 0.0F, Color.pink, 0.0F, 0.0F, Color.white);
        apaint[8] = new GradientPaint(0.0F, 0.0F, Color.yellow, 0.0F, 0.0F, Color.white);
        apaint[9] = new GradientPaint(0.0F, 0.0F, Color.darkGray, 0.0F, 0.0F, Color.white);
        return apaint;
    }

    public static Paint[] getGradientPaints() {
        return apaint;
    }

    public static Paint[] getPaints() {
        Paint apaint[] = new Paint[10];
        apaint[0] = Color.red;
        apaint[1] = Color.green;
        apaint[2] = Color.blue;
        apaint[3] = Color.orange;
        apaint[4] = Color.magenta;
        apaint[5] = Color.cyan;
        apaint[6] = Color.lightGray;
        apaint[7] = Color.pink;
        apaint[8] = Color.yellow;
        apaint[9] = Color.darkGray;
        return apaint;
    }

    public static BarRenderer createGradientBarRenderer() {
        return new CustBarRenderer(apaint);
    }

    public static BarRenderer createBarRenderer() {
        return new CustBarRenderer(getPaints());
    }

    static class CustBarRenderer extends BarRenderer {
        private static final long serialVersionUID = 2627983432244364177L;
        private Paint colors[];

        public Paint getItemPaint(int i, int j) {
            return colors[j % colors.length];
        }

        public CustBarRenderer(Paint apaint[]) {
            colors = apaint;
        }
    }

}
