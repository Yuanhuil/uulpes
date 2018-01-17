package heracles.jfree;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;

public class CustomBarRenderer3D extends org.jfree.chart.renderer.category.BarRenderer3D {

    /**
     * 
     */
    private static final long serialVersionUID = -4082873898675614181L;
    private Paint[] colors;

    public CustomBarRenderer3D() {

    }

    public CustomBarRenderer3D(List<Color> colorList) {
        colors = new Color[colorList.size()];
        for (int i = 0; i < colorList.size(); i++) {
            Color color = colorList.get(i);
            colors[i] = color;
        }
        // colors = (Paint[])colorList.toArray();
    }

    @Override
    public Paint getItemFillPaint(int row, int column) {
        // TODO Auto-generated method stub
        return colors[column % colors.length];
    }

    @Override
    public Paint getItemPaint(int row, int column) {
        // TODO Auto-generated method stub
        return colors[column % colors.length];
    }

}
