package heracles.jfree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.NumberUtils;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import heracles.util.Pools;
import heracles.util.SimpleCodec;
import heracles.util.UtilCollection;

/**
 * 当心理量表测试结果上需要显示图形时，使用此类定义图形的数据、样式 1.图片大小：宽x高 size = wXh <br>
 * 2.值范围: range=0-122 <br>
 * 3.mark值：在指定的刻度上画一条虚线 mark=1,2,4<br>
 * 4.显示x轴和/或y轴 axisY=t&axisX=f<br>
 * 5.是否分成2个区 第一区放在dataSet0中,第二个区放在dataset中，不分区则数据放在dataset中<br>
 * 6.数据:存放在一个map里，key为range轴数据，value为value数据 7.放入数据的个数：dataLengh 8.水平还是垂直:ort 9.
 * 
 * @author 王文
 */
public class JChartParam {
    public static void main(String[] args) {
        JChartParam p = new JChartParam();
        p.setWidth(200);
        p.setHeight(300);
        p.setOrt("v");
        p.addMarkerAt(9D);
        p.addMarkerAt(1D);
        p.putToDataSet("美丽", 12);
        p.putToDataSet("可恶", 34);
        p.putToDataSet("一般", 45);
        String st = p.paramToStr();

        JChartParam p1 = new JChartParam();
        p1.parseString(st);

        Map<String, Double> m = p1.getDataSet();
        for (Map.Entry<String, Double> ent : m.entrySet()) {
            System.out.print(ent.getKey());
            System.out.print("-");

        }

    }

    private Map<String, Double> dataSet0;
    private Map<String, Double> dataSet;
    private DoubleRange valueRange;
    private DoubleRange markRange;
    private List<Double> listMarker;
    private List<Double> dotMarkers;
    private List<Double> domainMarkers;
    private List<Double> dotDomainMarkers;
    private DefaultCategoryDataset stackedBarDataset;
    private CategoryDataset categoryDataset;
    private String stackedBarDomainAxisLabel;
    private String stackedBarRangeAxisLabel;
    private String chartTitle;
    private String stackedBarDatasetLine;
    private int labp;
    private String categoryAxisLabel;
    private String valueAxisLabel;

    public int getLabp() {
        return labp;
    }

    public void setLabp(int labp) {
        this.labp = labp;
    }

    public String getChartTitle() {
        return StringUtils.defaultString(chartTitle);
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public String getStackedBarDomainAxisLabel() {
        return stackedBarDomainAxisLabel;
    }

    public void setStackedBarDomainAxisLabel(String stackedBarDomainAxisLabel) {
        this.stackedBarDomainAxisLabel = stackedBarDomainAxisLabel;
    }

    public String getStackedBarRangeAxisLabel() {
        return stackedBarRangeAxisLabel;
    }

    public void setStackedBarRangeAxisLabel(String stackedBarRangeAxisLabel) {
        this.stackedBarRangeAxisLabel = stackedBarRangeAxisLabel;
    }

    public String getCategoryAxisLabel() {
        return categoryAxisLabel;
    }

    public void setCategoryAxisLabel(String categoryAxisLabel) {
        this.categoryAxisLabel = categoryAxisLabel;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public void setValueAxisLabel(String valueAxisLabel) {
        this.valueAxisLabel = valueAxisLabel;
    }

    public void addValue(Number value, String rowKey, String columnKey) {
        if (stackedBarDataset == null) {
            stackedBarDataset = new DefaultCategoryDataset();
        }
        stackedBarDataset.addValue(value, rowKey, columnKey);
    }

    public DefaultCategoryDataset getStackedBarDataset() {
        return stackedBarDataset;
    }

    public List<Double> getDomainMarkers() {
        return domainMarkers;
    }

    public List<Double> getDotDomainMarkers() {
        return dotDomainMarkers;
    }

    private boolean axisYVisible = true;
    private boolean axisXVisible = true;
    private int dataLen = 0;
    private int width;
    private int height;
    private String ort;
    private String chartType;
    private boolean outlineVisible = true;
    private String domainAxisLocation;

    public boolean isOutlineVisible() {
        return outlineVisible;
    }

    public void setOutlineVisible(boolean outlineVisible) {
        this.outlineVisible = outlineVisible;
    }

    public JChartParam() {
        dataSet = new LinkedHashMap<String, Double>();
    }

    public JChartParam(double number1, double number2) {
        valueRange = new DoubleRange(number1, number2);
        dataSet = new LinkedHashMap<String, Double>();
    }

    public JChartParam(double number1, double number2, int dataLen) {
        this(number1, number2);
        this.dataLen = dataLen;
    }

    public boolean isAxisXVisible() {
        return axisXVisible;
    }

    public void setAxisXVisible(boolean axisXVisible) {
        this.axisXVisible = axisXVisible;
    }

    public boolean isAxisYVisible() {
        return axisYVisible;
    }

    public void setAxisYVisible(boolean axisYVisible) {
        this.axisYVisible = axisYVisible;
    }

    public boolean isPart() {
        return MapUtils.isNotEmpty(dataSet0) && MapUtils.isNotEmpty(dataSet);
    }

    public void addMarkerAt(double marker) {
        if (listMarker == null) {
            listMarker = new ArrayList<Double>();
        }
        listMarker.add(marker);
    }

    public void addDomainMarkerAt(double marker) {
        if (domainMarkers == null) {
            domainMarkers = new ArrayList<Double>();
        }
        domainMarkers.add(marker);
    }

    public List<Double> getMarkerAtList() {
        return listMarker;
    }

    public void addDotMarkerAt(double marker) {
        if (dotMarkers == null) {
            dotMarkers = new ArrayList<Double>();
        }
        dotMarkers.add(marker);
    }

    public void addDotDomainMarkerAt(double marker) {
        if (dotDomainMarkers == null) {
            dotDomainMarkers = new ArrayList<Double>();
        }
        dotDomainMarkers.add(marker);
    }

    public List<Double> getDotMarkers() {
        return dotMarkers;
    }

    public void putToDataSet0(String key, double data) {
        if (dataSet0 == null) {
            dataSet0 = new LinkedHashMap<String, Double>();
        }
        dataSet0.put(key, data);
    }

    public Map<String, Double> getDataSet0() {
        return dataSet0;
    }

    public void putToDataSet(String key, double data) {
        if (getDataLen() == 0 || dataSet.size() < getDataLen())
            dataSet.put(key, data);
    }

    public Map<String, Double> getDataSet() {
        return dataSet;
    }

    /**
     * 将参数转换为字符串
     * 
     * @param model
     * @return
     */
    public String paramToStr() {
        StringBuilder sb = new StringBuilder();
        StringBuilder tmp = new StringBuilder();
        if (StringUtils.isNotEmpty(chartType)) {
            concatParamLeft(sb, ChartConstants.CHART_TYPE);
            sb.append(chartType);
        }
        // range=0.1-90
        if (getValueRange() != null) {
            concatParamLeft(sb, ChartConstants.DATA_RANGE);
            sb.append(getValueRange().getMinimumDouble());
            sb.append(UtilCollection.RGN_CHAR);
            sb.append(getValueRange().getMaximumDouble());
        }
        // marks=2.3,5,6
        if (CollectionUtils.isNotEmpty(getMarkerAtList())) {
            concatParamLeft(sb, ChartConstants.DATA_LINE_MARKS);
            for (double at : getMarkerAtList()) {
                sb.append(at);
                sb.append(UtilCollection.COMMA);
            }
            sb.setLength(sb.length() - 1);
        }
        if (this.getMarkRange() != null) {
            concatParamLeft(sb, ChartConstants.DATA_RAGNE_MARKS);
            sb.append(getMarkRange().getMinimumDouble());
            sb.append(UtilCollection.RGN_CHAR);
            sb.append(getMarkRange().getMaximumDouble());
        }
        // dotMarkers=2.3,5,6
        if (CollectionUtils.isNotEmpty(getDotMarkers())) {
            concatParamLeft(sb, ChartConstants.DATA_LINE_DOTMARKERS);
            for (double at : getDotMarkers()) {
                sb.append(at);
                sb.append(UtilCollection.COMMA);
            }
            sb.setLength(sb.length() - 1);
        }

        // data0=key:val;key:val...;key:val
        if (MapUtils.isNotEmpty(getDataSet0())) {
            concatParamLeft(sb, ChartConstants.DATA_0);
            for (Map.Entry<String, Double> ent : getDataSet0().entrySet()) {
                tmp.append(ent.getKey());
                tmp.append(UtilCollection.COLON);
                tmp.append(ent.getValue());
                tmp.append(UtilCollection.SEMICOLON);
            }
            tmp.setLength(tmp.length() - 1);
            String datas = StringEscapeUtils.escapeJava(tmp.toString());
            sb.append(datas);
        }
        // data=key:val;key:val...;key:val
        if (MapUtils.isNotEmpty(getDataSet())) {
            tmp.setLength(0);
            concatParamLeft(sb, ChartConstants.DATA);
            for (Map.Entry<String, Double> ent : getDataSet().entrySet()) {
                tmp.append(ent.getKey());
                tmp.append(UtilCollection.COLON);
                tmp.append(ent.getValue());
                tmp.append(UtilCollection.SEMICOLON);
            }
            tmp.setLength(tmp.length() - 1);
            String datas = StringEscapeUtils.escapeJava(tmp.toString());
            sb.append(datas);
        }
        // 堆栈柱状图数据
        if (StringUtils.isNotEmpty(stackedBarDatasetLine)) {
            concatParamLeft(sb, ChartConstants.DATA_SB);
            String datas = StringEscapeUtils.escapeJava(stackedBarDatasetLine);
            sb.append(datas);
        }
        if (!isAxisXVisible()) {
            concatParamLeft(sb, ChartConstants.CHART_AXISX);
            sb.append("n");
        }
        if (!isAxisYVisible()) {
            concatParamLeft(sb, ChartConstants.CHART_AXISY);
            sb.append("n");
        }

        if (!this.isOutlineVisible()) {
            concatParamLeft(sb, ChartConstants.OUTLINE_VISIBLE);
            sb.append("n");
        }

        if (StringUtils.isNotEmpty(getOrt())) {
            concatParamLeft(sb, ChartConstants.CHART_ORIENTATION);
            sb.append(getOrt());
        }
        if (this.getLabp() > 0) {
            concatParamLeft(sb, ChartConstants.LABELPOSITIONS);
            sb.append(this.getLabp());
        }
        if (StringUtils.isNotEmpty(domainAxisLocation)) {
            concatParamLeft(sb, ChartConstants.DOMAIN_AXIS_LOCATION);
            sb.append(domainAxisLocation);
        }
        if (StringUtils.isNotEmpty(chartTitle)) {
            concatParamLeft(sb, ChartConstants.CHART_TITLE);
            sb.append(StringEscapeUtils.escapeJava(chartTitle));
        }

        if (this.getWidth() > 0 && this.getHeight() > 0) {
            concatParamLeft(sb, ChartConstants.CHART_SIZE);
            sb.append(this.getWidth());
            sb.append(ChartConstants.SIZE_SPLIT_CHAR);
            sb.append(this.getHeight());
        }
        if (this.getDataLen() > 0) {
            concatParamLeft(sb, ChartConstants.DATA_LEN);
            sb.append(this.getDataLen());
        }
        if (sb.length() > 0 && sb.charAt(0) == UtilCollection.AMP_CHAR) {
            return sb.substring(1);
        }
        return sb.toString();
    }

    public String getDomainAxisLocation() {
        return domainAxisLocation;
    }

    private void concatParamLeft(StringBuilder sb, String key) {
        sb.append(UtilCollection.AMP_CHAR);
        sb.append(key);
        sb.append(UtilCollection.EQ);
    }

    /**
     * 将字符串转换为图形参数
     * 
     * @param str
     * @return
     */
    public JChartParam parseString(String str) {
        Map<String, String> params = UtilCollection.toMap(str, UtilCollection.AMP_CHAR, UtilCollection.EQ);
        String range = params.get(ChartConstants.DATA_RANGE);
        String marks = params.get(ChartConstants.DATA_LINE_MARKS);
        String markRangeStr = params.get(ChartConstants.DATA_RAGNE_MARKS);
        String dotMarkerStr = params.get(ChartConstants.DATA_LINE_DOTMARKERS);
        String doaminMarkStr = params.get(ChartConstants.DOMAIN_LINE_MARKS);
        String domainDotMarkerStr = params.get(ChartConstants.DOMAIN_LINE_DOTMARKERS);
        String data0 = params.get(ChartConstants.DATA_0);
        String data = params.get(ChartConstants.DATA);
        String dataSb = params.get(ChartConstants.DATA_SB);
        String dataL = params.get(ChartConstants.DATA_LEN);
        String mdata = params.get(ChartConstants.MDATA);
        String axisY = params.get(ChartConstants.CHART_AXISY);
        String axisX = params.get(ChartConstants.CHART_AXISX);
        String outline = params.get(ChartConstants.OUTLINE_VISIBLE);
        String chs = params.get(ChartConstants.CHART_SIZE);
        String ort = params.get(ChartConstants.CHART_ORIENTATION);
        String cht = params.get(ChartConstants.CHART_TYPE);
        String ctl = params.get(ChartConstants.CHART_TITLE);
        String labp = params.get(ChartConstants.LABELPOSITIONS);

        // 分类轴的位置
        domainAxisLocation = params.get(ChartConstants.DOMAIN_AXIS_LOCATION);
        List<String> tempList = new ArrayList<String>();
        Map<String, String> tempMap = new LinkedHashMap<String, String>();

        // 数值范围
        if (StringUtils.isNotEmpty(range)) {
            this.setValueRange(UtilCollection.toDoubleRange(range, UtilCollection.RGN_CHAR));
        }
        // 标记轴线位置
        if (StringUtils.isNotEmpty(marks)) {
            UtilCollection.toList(marks, UtilCollection.COMMA, tempList);
            for (String s : tempList) {
                this.addMarkerAt(NumberUtils.toDouble(s));
            }
        }
        // 虚线轴线的位置
        if (StringUtils.isNotEmpty(dotMarkerStr)) {
            UtilCollection.toList(dotMarkerStr, UtilCollection.COMMA, tempList);
            for (String s : tempList) {
                this.addDotMarkerAt(NumberUtils.toDouble(s));
            }
        }

        // 实线域轴线的位置
        if (StringUtils.isNotEmpty(doaminMarkStr)) {
            UtilCollection.toList(doaminMarkStr, UtilCollection.COMMA, tempList);
            for (String s : tempList) {
                this.addDomainMarkerAt(NumberUtils.toDouble(s));
            }
        }

        // 虚线域轴线的位置
        if (StringUtils.isNotEmpty(domainDotMarkerStr)) {
            UtilCollection.toList(domainDotMarkerStr, UtilCollection.COMMA, tempList);
            for (String s : tempList) {
                this.addDotDomainMarkerAt(NumberUtils.toDouble(s));
            }
        }

        if (StringUtils.isNotEmpty(markRangeStr)) {
            this.setMarkRange(UtilCollection.toDoubleRange(markRangeStr, UtilCollection.RGN_CHAR));
        }
        // 数据0 key:val;key:val
        if (StringUtils.isNotEmpty(data0)) {
            data0 = StringEscapeUtils.unescapeJava(data0);
            UtilCollection.toMap(data0, UtilCollection.SEMICOLON, UtilCollection.COLON, tempMap);
            for (Map.Entry<String, String> ent : tempMap.entrySet()) {
                this.putToDataSet0(ent.getKey(), NumberUtils.toDouble(ent.getValue()));
            }
        }
        // 数据
        if (StringUtils.isNotEmpty(data)) {
            data = StringEscapeUtils.unescapeJava(data);
            UtilCollection.toMap(data, UtilCollection.SEMICOLON, UtilCollection.COLON, tempMap);
            for (Map.Entry<String, String> ent : tempMap.entrySet()) {
                this.putToDataSet(ent.getKey(), NumberUtils.toDouble(ent.getValue()));
            }
        }
        // 多折线数据
        if (StringUtils.isNotEmpty(mdata)) {
            // mdata = StringEscapeUtils.unescapeJava(mdata);
            String[] temp1Array = mdata.split(";");
            for (int i = 0; i < temp1Array.length; i++) {
                String[] temp2Array = temp1Array[i].split(":");
                for (int j = 0; j < temp2Array.length; j++) {
                    String ss = SimpleCodec.dehex(temp2Array[0]);
                    this.addValue(NumberUtils.toDouble(temp2Array[2]), SimpleCodec.dehex(temp2Array[0]),
                            SimpleCodec.dehex(temp2Array[1]));
                }

            }

        }
        // dataSb:v,r,c#v,r,c#...
        if (StringUtils.isNotEmpty(dataSb)) {
            List<String> dataTempList = null;
            List<String> vals = null;
            try {
                dataTempList = Pools.getInstance().borrowStringList();
                vals = Pools.getInstance().borrowStringList();
                data = StringEscapeUtils.unescapeJava(dataSb);
                UtilCollection.toList(data, '$', dataTempList);
                for (String s : dataTempList) {
                    UtilCollection.toList(s, ',', vals);
                    this.addValue(NumberUtils.toDouble(vals.get(0)), SimpleCodec.dehex(vals.get(1)),
                            SimpleCodec.dehex(vals.get(2)));
                }
            } finally {
                Pools.getInstance().returnStringList(dataTempList);
                Pools.getInstance().returnStringList(vals);
            }
        }
        // 图形大小：宽X高
        if (StringUtils.isNotEmpty(chs)) {
            UtilCollection.toList(chs, ChartConstants.SIZE_SPLIT_CHAR, tempList);
            this.setWidth(NumberUtils.toInt(tempList.get(0)));
            this.setHeight(NumberUtils.toInt(tempList.get(1)));
        }
        // 是否显示x/y轴
        this.setAxisXVisible(StringUtils.defaultString(axisX, "y").equalsIgnoreCase("y"));
        this.setAxisYVisible(StringUtils.defaultString(axisY, "y").equalsIgnoreCase("y"));
        // 是否画图像的边缘
        this.setOutlineVisible(StringUtils.defaultString(outline, "y").equalsIgnoreCase("y"));
        // 垂直还是水平显示
        this.setOrt(StringUtils.defaultString(ort, "v"));
        this.setChartType(cht);
        if (StringUtils.isNotEmpty(dataL)) {
            this.setDataLen(NumberUtils.toInt(dataL));
        }
        if (StringUtils.isNotEmpty(ctl)) {
            ctl = StringEscapeUtils.unescapeJava(ctl);
            this.setChartTitle(ctl);
        }
        if (StringUtils.isNotEmpty(labp)) {
            this.setLabp(NumberUtils.toInt(labp));
        }
        return this;
    }

    public void setStackedBarDatasetLine(String stackedBarDatasetLine) {
        this.stackedBarDatasetLine = stackedBarDatasetLine;
    }

    public CategoryDataset getCategoryDataset() {
        return categoryDataset;
    }

    public void setCategoryDataset(CategoryDataset categoryDataset) {
        this.categoryDataset = categoryDataset;
    }

    public DoubleRange getValueRange() {
        return valueRange;
    }

    public void setValueRange(DoubleRange valueRange) {
        this.valueRange = valueRange;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public DoubleRange getMarkRange() {
        return markRange;
    }

    public void setMarkRange(DoubleRange markRange) {
        this.markRange = markRange;
    }

}
