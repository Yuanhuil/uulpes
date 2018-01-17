package heracles.jfree.chart;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class StackedDataSet {
    private DefaultCategoryDataset categoryDataset;

    private String columns[];

    public StackedDataSet() {
        this(null);
    }

    public StackedDataSet(final String columns[]) {
        categoryDataset = new DefaultCategoryDataset();
        this.columns = columns;
    }

    public CategoryDataset getDataset() {
        return categoryDataset;
    }

    public void addSeries(String rowKey, Number[] values) {
        if (values.length != columns.length) {
            throw new IllegalArgumentException("所提供的values的数目不等于列数.");
        }
        for (int i = 0; i < columns.length; i++) {
            categoryDataset.addValue(values[i], rowKey, columns[i]);
        }
    }

    public void addValue(Number value, Comparable<?> rowKey, Comparable<?> columnKey) {
        categoryDataset.addValue(value, rowKey, columnKey);
    }
}
