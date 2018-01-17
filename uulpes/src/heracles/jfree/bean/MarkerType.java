package heracles.jfree.bean;

public enum MarkerType {
    category, interval;

    public static MarkerType getMarkerType(String s) {
        MarkerType[] dts = MarkerType.values();
        for (MarkerType dt : dts) {
            if (dt.name().equalsIgnoreCase(s)) {
                return dt;
            }
        }
        return null;
    }
}