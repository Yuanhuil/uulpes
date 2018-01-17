package heracles.jfree;

public interface ChartConstants {
    static final String CHART_ORIENTATION = "ort"; // 水平还是垂直,默认v(垂直),其他非v则为水平
    static final String VALUE_AXIS_LABEL = "vbl"; // 数值轴的标签
    static final String CATEGORY_AXIS_LABEL = "cbl"; // 类别轴的标签
    static final String IS_3D = "3d"; // 是否3d,只要提供值即为true
    static final String URLS = "ul"; // 是否提供url,只要提供值即为true
    static final String TOOLTIPS = "tlp"; // 是否有提示,只要提供值即为true
    static final String LEGEND = "led"; // 是否有图例,只要提供值即为true
    static final String RANGE = "rng"; // 数值轴的值范围
    static final String TITLE = "tl"; // 图片标题
    static final String COLUMNS = "columns"; // 提供的列名称

    static final String CHART_TYPE = "cht"; // 图表类型
    static final String CHART_SIZE = "chs"; // 图表大小
    static final String CHART_LINE = "ln"; // 线图
    static final String CHART_MLINE = "mln"; // 多线图
    static final String CHART_BAR = "br"; // 条图
    static final String CHART_MBAR = "mbr";
    static final String CHART_PIE = "pi"; // 饼图
    static final String CHART_XY = "xy"; // 条图
    static final String CHART_STACK_BAR = "sbr"; // 堆栈条图
    static final String CHART_TITLE = "ctl"; // 标题

    static final String CHART_AXISX = "axisX"; // 显示图表的x轴？
    static final String CHART_AXISY = "axisY"; // 显示图表的y轴？
    static final String CHART_AXIS_H = "h"; // 水平显示
    static final String CHART_AXIS_V = "v"; // 垂直显示

    static final char SIZE_SPLIT_CHAR = 'x'; // 如:100x200
    static final String DATA_RANGE = "range"; // 数值区间
    static final String DATA_LINE_MARKS = "marks"; // 标记
    static final String DATA_RAGNE_MARKS = "markRange"; // 标记
    static final String DATA_LINE_DOTMARKERS = "dotMarkers";
    static final String DOMAIN_LINE_MARKS = "domainMarks"; // 标记
    static final String DOMAIN_LINE_DOTMARKERS = "dotDomainMarkers";
    static final String DATA_0 = "data0"; // data0
    static final String DATA = "data"; // data
    static final String MDATA = "mdata";// added by 赵万锋 用于多条折线数据
    static final String DATA_LEN = "dataLen"; // dataLen
    static final String OUTLINE_VISIBLE = "outlineVisible"; // 是否显示边线
    static final String DATA_SB = "dataSb"; // 堆栈柱图的数据

    static final String DOMAIN_AXIS_LOCATION = "domainAxisLocation"; // 分类轴的位置

    static final String AXIS_LOCATION_BOTTOM_OR_RIGHT = "b_r"; // 分类轴的位置
    static final String LABELPOSITIONS = "labp"; // 分类轴的位置

}
