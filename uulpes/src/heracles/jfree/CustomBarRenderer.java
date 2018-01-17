package heracles.jfree;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;

public class CustomBarRenderer extends org.jfree.chart.renderer.category.BarRenderer {

    private static final long serialVersionUID = 784630226449158436L;
    private Paint[] colors;

    // 初始化柱子颜色 
    // private String[] colorValues =
    // {"#AFD8F8","#F6BD0F","#8BBA00","#FF8E46","#008E8E","#D64646" };
    public CustomBarRenderer() {
    }

    public CustomBarRenderer(List<Color> colorList) {
        colors = new Color[colorList.size()];
        for (int i = 0; i < colorList.size(); i++) {
            Color color = colorList.get(i);
            colors[i] = color;
        }
        // colors = (Paint[])colorList.toArray();
    }

    @Override
    public Paint getItemPaint(int row, int column) {
        // TODO Auto-generated method stub
        return colors[column % colors.length];
    }

    @Override
    public Paint getItemFillPaint(int row, int column) {
        // TODO Auto-generated method stub
        return colors[column % colors.length];
    }
}
