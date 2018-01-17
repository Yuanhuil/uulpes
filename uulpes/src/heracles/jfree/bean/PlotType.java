package heracles.jfree.bean;

public enum PlotType {
    category, pie, xy;

    public static PlotType getPlotType(String s) {
        PlotType[] dts = PlotType.values();
        for (PlotType dt : dts) {
            if (dt.name().equalsIgnoreCase(s)) {
                return dt;
            }
        }
        return null;
    }
}
