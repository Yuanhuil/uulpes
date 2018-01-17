package edutec.scale.model;

public interface QuestionConsts {

    /* 以下列出的是题目类型：题目类型字符型 */
    public static final String TYPE_MATRIX = "matrix"; // 矩阵题
    public static final String TYPE_FILLBLANK = "fillblank"; // 填充题
    public static final String TYPE_QA = "qa"; // 问答题
    public static final String TYPE_SELECTION = "selection"; // 选择题
    public static final String TYPE_SORT = "sort"; // 排序题
    public static final String TYPE_TITLE = "title";
    public static final String TYPE_JUDGE = "judge";// 判断题
    // 模板题,此类题仅显示页面,这类题目用来显示一些说明信息，信息保存在一个网页上
    // 通常不计为量表题目
    public static final String TYPE_TMPLATE = "tmpl";

    /* 题目类型编号 ，将题目类型转换成int型，目的是提高性能和减少存储空间 */
    public static final int TYPE_MATRIX_MODE = 0;
    public static final int TYPE_FILLBLANK_MODE = 1;
    public static final int TYPE_QA_MODE = 2;
    public static final int TYPE_SELECTION_MODE = 3;
    public static final int TYPE_SORT_MODE = 4;
    public static final int TYPE_TMPLATE_MODE = 5;

    /**
     * 建立类型编号和题目见的对应关系
     */
    public static final String[] QUESTION_TYPE = { TYPE_MATRIX, TYPE_FILLBLANK, TYPE_QA, TYPE_SELECTION, TYPE_SORT,
            TYPE_TMPLATE, TYPE_TITLE, TYPE_JUDGE };

    /**
     * ###选择题又有不同的选择形式，以后如果出现新的题目种类，变形在choice上，即选择题为最基础的题目类型
     */
    /* 选择题的选择形式：一般控制着在页面上的显示 , 注意：系统对option也使用了此内容 */
    public static final String CHOICE_SINGLE = "single";// 单选
    public static final String CHOICE_MULTI = "multi";// 多选
    public static final String CHOICE_MIX = "mix";// 混合选
    public static final String CHOICE_SEL = "sel";// 选择,即是通知程序，答案是按选择列表样式在页面上显示的
    public static final String CHOICE_CHK = "chk";//
    public static final String CHOICE_RADIO = "radio";// 即是通知程序，答案是单选按钮显示的
    public static final String CHOICE_NO = "chkno";// 特殊
    public static final String CHOICE_TEXT = "text";// 文本形式，将替换文本题型（填充题和问答题）

    /**
     * 选择形式的显示是一个个图片，一般是含正确答案的，如瑞文智力测验
     * <q size="6" id="Q1" choice="img" descn="calc.correct:3"></q> 每个选项由图片组成的
     */
    public static final String CHOICE_IMG = "img";
    public static final String CHOICE_IMG2 = "img2";
    public static final String CHOICE_IMGX = "imgx";

    /* 选择形式整数型 */
    public static final int CHOICE_SINGLE_MODE = 0;
    public static final int CHOICE_MULTI_MODE = 1;
    public static final int CHOICE_MIX_MODE = 2;
    public static final int CHOICE_SEL_MODE = 3;
    public static final int CHOICE_CHK_MODE = 4;
    public static final int CHOICE_RADIO_MODE = 5;
    public static final int CHOICE_NO_MODE = 6;
    public static final int CHOICE_TEXT_MODE = 7;
    public static final int CHOICE_IMG_MODE = 8;
    public static final int CHOICE_IMG2_MODE = 9;
    public static final int CHOICE_IMGX_MODE = 10;

    public static final String[] CHOICE_TYPE = { CHOICE_SINGLE, CHOICE_MULTI, CHOICE_MIX, CHOICE_SEL, CHOICE_CHK,
            CHOICE_RADIO, CHOICE_NO, CHOICE_TEXT, CHOICE_IMG, CHOICE_IMG2, CHOICE_IMGX };

    // 含在descn字符串中的key
    public static final String DESCN_COMPONENT_KEY = "component";
    public static final String DESCN_TEMPLATE_KEY = "template";
    public static final String DESCN_CALC_CORRECT_KEY = "calc.correct"; // 表示正确答案选项
    public static final String DESCN_SCORE_KEY = "score"; // 表示正确答案选项后的分数
    // 表示计算此题目的类，由于一些题目计算很复杂，需要一个特定的类来计算
    public static final String DESCN_CALC_CLASS_KEY = "calc.class";
    public static final String DESCN_IMGSRC_KEY = "imgsrc";
    public static final String DESCN_LABEL_KEY = "label";

}
