package edutec.scale.util;

public interface QuestionnaireConsts {
    /* 参数,用于量表xml配置文件中,做为特定的替换保留字 */

    /**
     * 分割
     */
    public static final char RANGE_SPARATOR = '-';

    /**
     * 分割
     */
    public static final char PART_SPARATOR = '#';

    /**
     * 分割
     */
    public static final char UNIT_SPARATOR = ',';

    // ----------以上是xml的dscriptor中的变量--------------------
    static final String MEAN = "m";
    static final String SD = "sd";
    static final String LO = "lo";
    static final String HI = "hi";
    static final String VAL = "val";
    /**
     * 如：select rawstr from ${table} where s1='${wid}' and i1=${degree} and
     * i2=${gender} ${table}，用于替换表名
     */
    static final String TABLE = "table";
    /**
     * ${wid},将被替换为维度id
     */
    static final String WID = "wid";
    /**
     * ${wval},将被维度值所替换
     */
    static final String WV = "wval";

    static final String WTITLE = "wtitle";

    static final String WSCORE = "wscore";
    /**
     * 表示rawstr字段
     */
    static final String RAWSTR = "rawstr";
    /**
     * 表示descn字段
     */
    static final String DESCN = "descn";
    /**
     * 表示维度粗分
     */
    static final String WR_SORE = "wrsore";
    /**
     * 
     */
    static final String FLAG = "flag";

    // ------------------
    public static final String DESCN_TITLE = "解释";
    public final static String LOW_NORM = "显著低于常模";
    public final static String UPPER_NORM = "显著高于常模";
    public final static String NEAR_NORM = "与常模没有差异";

    // ------------------------//
    /**
     * 
     */
    // 可见的维度数目，从第一个维度数目开始，默认为显示所有的维度,format:1 or 2-9
    // note ：that is close and open
    public static final String TBL_DIM_VISIBLE = "tbl-dim-visible";
    // note ：that is close and open,不可视的维度
    public static final String TBL_DIM_INVISIBLE = "tbl-dim-invisible";
    // 1是显示分，0是不显示，默认为不显示
    public static final String TBL_STD_VISIBLE = "tbl-std-visible";
    // 1是显示原始分，0是不显示，默认为不显示
    public static final String TBL_RAW_VISIBLE = "tbl-raw-visible";
    // 1是显示解释，0是不显示解释，默认为显示解释
    public static final String TBL_EXPLAIN_VISIBLE = "tbl-explain";
    // 1是横显示维度名和维度分,2是竖排维度名和分获/和解释,默认为横向
    public static final String TBL_ARRANGE = "tbl-arrange";
    // 报告模板
    public static final String REPORT_TPL = "rpt-tpl";

    // 指示如何处理结果值
    public static final String VALUE_HANDEL = "value-handel";

}
