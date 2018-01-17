package heracles.jfree.bean;

public enum AxisType {
    category, value;
    public static AxisType getAxisType(String s) {
        AxisType[] dts = AxisType.values();
        for (AxisType dt : dts) {
            if (dt.name().equalsIgnoreCase(s)) {
                return dt;
            }
        }
        return null;
    }
}
