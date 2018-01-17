package heracles.jfree.bean;

public class AxisBean extends JsonBean {
    public AxisBean() {
        super();
    }

    public AxisBean(String jsonStr) {
        super(jsonStr);
    }

    public AxisType getType() {
        return AxisType.getAxisType(this.getString(Consts.TYPE));
    }

}
