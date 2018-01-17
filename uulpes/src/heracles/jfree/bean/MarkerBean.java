package heracles.jfree.bean;

import org.apache.commons.lang.math.DoubleRange;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;

import heracles.util.UtilCollection;

public class MarkerBean extends JsonBean {
    public MarkerBean() {
        super();
    }

    public MarkerBean(String jsonStr) {
        super(jsonStr);
    }

    public MarkerType getType() {
        String type = this.has(Consts.TYPE) ? this.getString(Consts.TYPE) : Consts.Category;
        return MarkerType.getMarkerType(type);
    }

    public Marker newMarker() {
        Marker result = null;
        String val = this.getString("val");
        if (getType() == MarkerType.interval) {
            DoubleRange range = UtilCollection.toDoubleRange(val, UtilCollection.COMMA);
            double startValue = range.getMinimumDouble();
            double endValue = range.getMaximumDouble();
            result = new IntervalMarker(startValue, endValue);
        } else {
            result = new CategoryMarker(val);
        }
        if (this.has("label")) {
            result.setLabel(this.getString("label"));
        }
        return result;
    }

    public void setType(String type) {
        this.put(Consts.TYPE, type);
    }
}
