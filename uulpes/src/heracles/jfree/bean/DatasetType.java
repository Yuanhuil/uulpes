package heracles.jfree.bean;

public enum DatasetType {
    category, interval;
    public static DatasetType getDatasetType(String s) {
        DatasetType[] dts = DatasetType.values();
        for (DatasetType dt : dts) {
            if (dt.name().equalsIgnoreCase(s)) {
                return dt;
            }
        }
        return null;
    }
}
