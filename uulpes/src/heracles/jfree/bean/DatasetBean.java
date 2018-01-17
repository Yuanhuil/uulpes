package heracles.jfree.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.general.Dataset;

import heracles.util.Pools;
import heracles.util.UtilCollection;

public class DatasetBean extends JsonBean {
    private Map<String, Object> data;

    public DatasetBean() {
        this.setType(DatasetType.category);
    }

    public DatasetBean(String jsonStr) {
        super(jsonStr);
    }

    @SuppressWarnings("unchecked")
    public Dataset newDataset() {
        DatasetType dt = getType();
        String data = this.getString("data");
        data = StringEscapeUtils.unescapeJava(data);
        List<List<String>> rows = UtilCollection.toStringList(data, '|', UtilCollection.COMMA);
        if (dt == DatasetType.category) {
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            for (List<String> vals : rows) {
                int sz = vals.size();
                Number value = Double.valueOf(vals.get(0));
                Comparable columnKey = null;
                Comparable rowKey = "p";
                if (sz == 3) {
                    rowKey = vals.get(1);
                }
                columnKey = vals.get(sz - 1);
                ds.addValue(value, rowKey, columnKey);
            }
            return ds;
        }
        if (dt == DatasetType.interval) {
            List<String> columns = new ArrayList<String>();
            List<Double> starts = new ArrayList<Double>();
            List<Double> ends = new ArrayList<Double>();
            for (List<String> vals : rows) {
                columns.add(vals.get(0));
                starts.add(Double.valueOf(vals.get(1)));
                ends.add(Double.valueOf(vals.get(2)));
            }
            Comparable[] categoryKeys = columns.toArray(new String[columns.size()]);
            Number[][] starts_ = { starts.toArray(new Double[starts.size()]) };
            Number[][] ends_ = { ends.toArray(new Double[ends.size()]) };
            DefaultIntervalCategoryDataset ds = new DefaultIntervalCategoryDataset(null, categoryKeys, starts_, ends_);
            return ds;
        }
        return null;
    }

    public DatasetType getType() {
        String type = this.has(Consts.TYPE) ? this.getString(Consts.TYPE) : Consts.Category;
        DatasetType dt = DatasetType.getDatasetType(type);
        return dt;
    }

    public void setType(DatasetType datasetType) {
        this.put(Consts.TYPE, datasetType.name());
    }

    public void putToDataSet(String key, Object value) {
        if (data == null) {
            data = new LinkedHashMap<String, Object>();
        }
        if (getType() != DatasetType.category) {
            this.setType(DatasetType.category);
        }
        data.put(key, value);
    }

    public void putToDataSet(String key, Number start, Number end) {
        if (data == null) {
            data = new LinkedHashMap<String, Object>();
        }
        if (getType() != DatasetType.interval) {
            this.setType(DatasetType.interval);
        }
        data.put(key, new DoubleRange(start, end));
    }

    public String dataToUrlStr() {
        StringBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStringBuilder(64);
            for (Map.Entry<String, Object> ent : data.entrySet()) {
                if (getType() == DatasetType.category) {
                    sb.append(ent.getValue());
                    sb.append(",");
                    sb.append(ent.getKey());
                    sb.append("|");
                } else if (getType() == DatasetType.interval) {
                    DoubleRange value = (DoubleRange) ent.getValue();
                    sb.append(ent.getKey());
                    sb.append(",");
                    sb.append(value.getMinimumDouble());
                    sb.append(",");
                    sb.append(value.getMaximumDouble());
                    sb.append("|");
                }
            }
            sb.setLength(sb.length() - 1);
            this.put("data", StringEscapeUtils.escapeJava(sb.toString()));
            sb.setLength(0);
            sb.append("chd=");
            sb.append(this.toJson());
            return sb.toString();
        } finally {
            Pools.getInstance().returnStringBuilder(sb);
        }
    }

}
