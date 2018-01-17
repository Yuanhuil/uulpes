package edutec.scale.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.util.MathUtils;

import com.njpes.www.entity.scaletoollib.ScaleInfo;

import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.DimensionBlock;
import heracles.util.UtilCollection;

/**
 * 有总分的表：<br>
 * 00011心理健康量表、11001道德判断量表、12101兴趣量表、12203自信心量表、12301情绪适应量表、<br>
 * 21101创新意识量表、21231学习态度量表<br>
 * 其中：健康和道德量表有子维度，也有总分<br>
 * 
 * @author
 */
public class ScaleUtils {

    public static void main(String add[]) {
        int flag = SCALE_CEE_FLAG | SCALE_PROPER_FLAG;
        System.out.println((flag & SCALE_CEE_FLAG) > 0);
        System.out.println(SCALE_CEE_FLAG | SCALE_PROPER_FLAG);
    }

    /**
     * 量表
     */
    public static final int SCALE_FREE_FLAG = 1; // 免费量表
    public static final int SCALE_PROPER_FLAG = 2; // 产权量表
    public static final int SCALE_EL_FLAG = 4; // 小学量表
    public static final int SCALE_SE_FLAG = 8; // 初中学量表
    public static final int SCALE_HS_FLAG = 16; // 高中量表
    public static final int SCALE_CEE_FLAG = 32; // 高考量表
    public static final int SCALE_CUST_FLAG = 64; // 自定义量表
    public static final int SCALE_PAY_FLAG = 128; // 收费量表
    public static final int SCALE_ADULT_FLAG = 256; // 成人量表
    public static final int SCALE_CARD_FLAG = 512; // 心检尺量表
    public static final int SCALE_RECOMMAND_FLAG = 1024; // 推荐量表
    public static final int SCALE_TEACHER_FLAG = 2048; // 教师
    public static final int SCALE_RELEASE_FLAG = 2048 << 1; // 可发布量表
    public static final int SCALE_STU_FLAGS = SCALE_EL_FLAG | SCALE_SE_FLAG | SCALE_HS_FLAG;

    public static final String SCALE_FREE_DESC = "免费量表";
    public static final String SCALE_PROPER_DESC = "产权量表";
    public static final String SCALE_EL_DESC = "小学量表";
    public static final String SCALE_SE_DESC = "初中量表";
    public static final String SCALE_HS_DESC = "高中量表";
    public static final String SCALE_CEE_DESC = "高考量表";
    public static final String SCALE_CUST_DESC = "自定义量表";
    public static final String SCALE_PAY_DESC = "收费量表";
    public static final String SCALE_ADULT_DESC = "成人量表";
    public static final String SCALE_CARD_DESC = "心检尺量表";
    public static final String SCALE_RECOMMAND_DESC = "推荐量表";
    public static final String SCALE_TEACHER_DESC = "教师量表";
    public static final String SCALE_RELEASE_DESC = "可分发量表";

    public static Map<String, String> SCALE_FLAGS_DESC = new LinkedHashMap<String, String>();
    public static Map<String, String> SCALE_FLAGS_CUST_DESC = new LinkedHashMap<String, String>();
    static {
        // SCALE_FLAGS_DESC.put(SCALE_FREE_FLAG+"", SCALE_FREE_DESC);
        SCALE_FLAGS_DESC.put(SCALE_PROPER_FLAG + "", SCALE_PROPER_DESC);
        SCALE_FLAGS_DESC.put(SCALE_EL_FLAG + "", SCALE_EL_DESC);
        SCALE_FLAGS_DESC.put(SCALE_SE_FLAG + "", SCALE_SE_DESC);
        SCALE_FLAGS_DESC.put(SCALE_HS_FLAG + "", SCALE_HS_DESC);
        SCALE_FLAGS_DESC.put(SCALE_CEE_FLAG + "", SCALE_CEE_DESC);
        SCALE_FLAGS_DESC.put(SCALE_CUST_FLAG + "", SCALE_CUST_DESC);
        SCALE_FLAGS_DESC.put(SCALE_PAY_FLAG + "", SCALE_PAY_DESC);
        // SCALE_FLAGS_DESC.put(SCALE_ADULT_FLAG + "", SCALE_ADULT_DESC);
        SCALE_FLAGS_DESC.put(SCALE_CARD_FLAG + "", SCALE_CARD_DESC);
        SCALE_FLAGS_DESC.put(SCALE_RECOMMAND_FLAG + "", SCALE_RECOMMAND_DESC);
        SCALE_FLAGS_DESC.put(SCALE_TEACHER_FLAG + "", SCALE_TEACHER_DESC);
        SCALE_FLAGS_DESC.put(SCALE_RELEASE_FLAG + "", SCALE_RELEASE_DESC);

        SCALE_FLAGS_CUST_DESC.put(SCALE_EL_FLAG + "", SCALE_EL_DESC);
        SCALE_FLAGS_CUST_DESC.put(SCALE_SE_FLAG + "", SCALE_SE_DESC);
        SCALE_FLAGS_CUST_DESC.put(SCALE_HS_FLAG + "", SCALE_HS_DESC);
        SCALE_FLAGS_CUST_DESC.put(SCALE_CEE_FLAG + "", SCALE_CEE_DESC);
        SCALE_FLAGS_CUST_DESC.put(SCALE_TEACHER_FLAG + "", SCALE_TEACHER_DESC);
        // SCALE_FLAGS_CUST_DESC.put(SCALE_ADULT_FLAG + "", SCALE_ADULT_DESC);

    }
    public static final int SCALE_SOURCE_INTERNATIONAL_ID = 1;
    public static final int SCALE_SOURCE_NATIONAL_ID = 2;
    public static final int SCALE_SOURCE_PROP_ID = 3;
    public static final int SCALE_SOURCE_OTHER_ID = 4;
    public static final String SCALE_SOURCE_INTERNATIONAL_DESC = "国际量表";
    public static final String SCALE_SOURCE_NATIONAL_DESC = "北标量表";
    public static final String SCALE_SOURCE_PROP_DESC = "产权量表";
    public static final String SCALE_SOURCE_OTHER_DESC = "其他量表";
    public static Map<String, String> SCALE_SOURCE_DESC = new HashMap<String, String>();
    public static Map<String, String> SCALE_TYPE_DESC = new HashMap<String, String>();

    public static final int SCALE_TYPE_MENTALITY_ID = 1;
    public static final int SCALE_TYPE_ABILITY_ID = 2;
    public static final int SCALE_TYPE_PERSONALITY_ID = 3;
    public static final int SCALE_TYPE_STUDY_ID = 4;
    public static final int SCALE_TYPE_CAREER_ID = 5;
    public static final int SCALE_TYPE_VOCATION_ID = 6;
    public static final int SCALE_TYPE_OTHER_ID = 7;
    public static final String SCALE_TYPE_MENTALITY_DESC = "心理健康";
    public static final String SCALE_TYPE_ABILITY_DESC = "能力量表";
    public static final String SCALE_TYPE_PERSONALITY_DESC = "人格量表";
    public static final String SCALE_TYPE_STUDY_DESC = "学业量表";
    public static final String SCALE_TYPE_CAREER_DESC = "生涯规划";
    public static final String SCALE_TYPE_VOCATION_DESC = "职业倦怠";
    public static final String SCALE_TYPE_OTHER_DESC = "其他量表";
    static {
        SCALE_SOURCE_DESC.put(SCALE_SOURCE_INTERNATIONAL_ID + "", SCALE_SOURCE_INTERNATIONAL_DESC);
        SCALE_SOURCE_DESC.put(SCALE_SOURCE_NATIONAL_ID + "", SCALE_SOURCE_NATIONAL_DESC);
        SCALE_SOURCE_DESC.put(SCALE_SOURCE_PROP_ID + "", SCALE_SOURCE_PROP_DESC);
        SCALE_SOURCE_DESC.put(SCALE_SOURCE_OTHER_ID + "", SCALE_SOURCE_OTHER_DESC);

        SCALE_TYPE_DESC.put(SCALE_TYPE_MENTALITY_ID + "", SCALE_TYPE_MENTALITY_DESC);
        SCALE_TYPE_DESC.put(SCALE_TYPE_ABILITY_ID + "", SCALE_TYPE_ABILITY_DESC);
        SCALE_TYPE_DESC.put(SCALE_TYPE_PERSONALITY_ID + "", SCALE_TYPE_PERSONALITY_DESC);
        SCALE_TYPE_DESC.put(SCALE_TYPE_STUDY_ID + "", SCALE_TYPE_STUDY_DESC);
        SCALE_TYPE_DESC.put(SCALE_TYPE_CAREER_ID + "", SCALE_TYPE_CAREER_DESC);
        SCALE_TYPE_DESC.put(SCALE_TYPE_VOCATION_ID + "", SCALE_TYPE_VOCATION_DESC);
        SCALE_TYPE_DESC.put(SCALE_TYPE_OTHER_ID + "", SCALE_TYPE_OTHER_DESC);
    }

    /**
     * 判断此量表号，是否是能力量表
     * 
     * @param scalesId
     *            量表号
     * @return 如果是，返回true，否则返回false
     */
    public static boolean isAbilityScale(String scalesId) {
        if (StringUtils.isBlank(scalesId)) {
            return false;
        }
        // 言语能力/数学能力/图形能力推理
        String abilitys[] = { "121211", "121212", "121213", "221211", "221212", "221213" };
        return ArrayUtils.contains(abilitys, scalesId);
    }

    // 是否人格量表
    public static boolean isPersonalityScale(String scalesId) {
        if (StringUtils.isBlank(scalesId)) {
            return false;
        }
        String personalities[] = { "131000001", "130110001", "151000001", "150110001", "111000004", "130110002",
                "131000002", "130110003", "131000003", "130110004", "111000005", "130110005", "131000004", "110110104",
                "121000001", "120100001" };
        return ArrayUtils.contains(personalities, scalesId);
    }

    // 是否智能量表
    public static boolean isPotentialScale(String scalesId) {
        if (StringUtils.isBlank(scalesId)) {
            return false;
        }
        String portentials[] = { "141000001", "140100001", "141000002", "140110002", "121000002", "120110002",
                "121000003", "120110003", "121000004", "140110003" };
        return ArrayUtils.contains(portentials, scalesId);
    }

    public static boolean isSameReverseInMultiDimScale(String scaleId) {
        if (StringUtils.isBlank(scaleId)) {
            return false;
        }
        String scales[] = { "310011116" };
        return ArrayUtils.contains(scales, scaleId);
    }

    public static boolean isEPQScale(String scaleId) {
        if (StringUtils.isBlank(scaleId)) {
            return false;
        }
        String scales[] = { "331110016", "330001117" };
        return ArrayUtils.contains(scales, scaleId);
    }

    public static boolean isMBTIScale(String scaleId) {
        if (StringUtils.isBlank(scaleId)) {
            return false;
        }
        String scales[] = { "350001101" };
        return ArrayUtils.contains(scales, scaleId);
    }

    /**
     * 有量表总分的量表： 自信心量表、情绪适应量表、创新意识量表、学习态度量表；
     * 
     * @param scalesId
     * @return
     */
    public static boolean hasTotalScore(String scalesId) {
        // 自信心量表/情绪适应量表/创新意识量表/学习态度量表
        String ts[] = { "12203", "12301", "21101", "21231" };
        return UtilCollection.endsWithAny(scalesId, ts);
    }

    // 单维量表
    public static final String[] SINGLE_DIM_SCALES = { "112202", // 小学生内外向问卷
            "112204", // 小学生意志力量表
            "212202", // 中学生内外向量表
            "212204", // 中学生意志力量表
            "221221" // 中学生学习方法量表
    };
    public static final String[] H_THIRD_ANGLE_SCALES_STUDENT = { "111000001", // 小学生心理健康学生版
            "110110001"// 中学生心理健康学生版
    };
    public static final String[] H_THIRD_ANGLE_SCALES_TEACHER = { "111000003", // 小学生心理健康学生版
            "110110003"// 中学生心理健康学生版
    };
    public static final String[] H_THIRD_ANGLE_SCALES_PARENT = { "111000002", // 小学生心理健康学生版
            "110110002"// 中学生心理健康学生版
    };
    public static final String[] H_THIRD_ANGLE_SCALES_P = { "111000001", // 小学生心理健康量表
            "111000002", // 小学生心理健康家长版
            "111000003"// 小学生心理健康教师版
    };
    public static final String[] H_THIRD_ANGLE_SCALES_P_1 = { "111000002", // 小学生心理健康家长版
            "111000003"// 小学生心理健康教师版
    };

    public static final String[] H_THIRD_ANGLE_SCALES_M = { "110110001", // 中学生心理健康量表
            "110110002", // 中学生心理健康家长版
            "110110003"// 中学生心理健康教师版
    };
    public static final String[] H_THIRD_ANGLE_SCALES_M_1 = { "110110002", // 中学生心理健康家长版
            "110110003"// 中学生心理健康教师版
    };
    public static final String[] H_THIRD_ANGLE_SCALES = { // 添加了小学生量表为三角视，赵万锋
            "111000001", // 小学生心理健康量表
            "111000002", // 小学生心理健康家长版
            "111000003", // 小学生心理健康教师版
            "110110001", // 中学生心理健康量表
            "110110002", // 中学生心理健康量表家长版
            "110110003"// 中学生心理健康量表教师版
    };
    public static final String[] MENTAL_HEALTH_SCALES = { "111000001", // 小学生心理健康量表
            "111000002", // 小学生心理健康家长版
            "111000003", // 小学生心理健康教师版
            "110110001", // 中学生心理健康量表
            "110110002", // 中学生心理健康量表家长版
            "110110003", // 中学生心理健康量表教师版
            "110000102", // 教师心理健康量表
            "110000101"// 成人心理健康量表
    };

    public static boolean isThirdAngleScaleForStudent(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_STUDENT, scaleId);
    }

    public static boolean isThirdAngleScaleForTeacher(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_TEACHER, scaleId);
    }

    public static boolean isThirdAngleScaleForParent(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_PARENT, scaleId);
    }

    public static boolean isThirdAngleScale(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES, scaleId);
    }

    public static boolean isThirdAngleScaleP(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_P, scaleId);
    }

    public static boolean isThirdAngleScaleP1(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_P_1, scaleId);
    }

    public static boolean isThirdAngleScaleM(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_M, scaleId);
    }

    public static boolean isThirdAngleScaleM1(String scaleId) {
        return ArrayUtils.contains(H_THIRD_ANGLE_SCALES_M_1, scaleId);
    }

    /**
     * 判断此量表号，是否是单维量表.实际上的定义是一个维度且维度编号不为W0;
     * 
     * @param scaleId
     *            量表号
     * @return 如果是，返回true，否则返回false
     */
    public static boolean isSingleDimScale(String scaleId) {
        return ArrayUtils.contains(ScaleUtils.SINGLE_DIM_SCALES, scaleId);
    }

    /**
     * 根据量表号判定是否是心理健康量表
     * 
     * @param scaleId
     * @return
     */
    public static boolean isThreeAngleMentalHealthScaleForStudent(String scaleId) {
        return scaleId.equals("110110001") || scaleId.equals("111000001");
    }

    public static boolean isMentalHealthScale(String scaleId) {
        return ArrayUtils.contains(ScaleUtils.MENTAL_HEALTH_SCALES, scaleId);
    }

    public static final String[] HE_SCALES = { "211001", "212101", "212301", "212201", "212204", "212203", "212202",
            "221221", "221231", "221211", "221212", "221213", "221241", "221101" };

    /**
     * 中学生量表的推荐量表，在做完心理健康量表后，根据其结果推荐给答题人的量表 <br>
     * 下面是它的维度 W1="学习",W2="人际",W3="青春期 W4="问题行为"<br>
     * W5="适应",一级维度<br>
     * W6="抑郁" W7="焦虑",W8="强迫",W9="恐惧",<br>
     * W10="困扰",一级维度<br>
     * W11="自信";W12="积极认知";W13="自我调节" W14="社会支持";<br>
     * W15="复原力",一级维度<br>
     */
    public static final String[][] HE_RECOMMEND_SCALES_RX = { { "211001", "W4,W14" }, { "212101", "W2,W6,W14" },
            { "212301", "W2,W3,W4,W7,W9,W8,W6,W11,W12" }, { "212201", "W6,W13" }, { "212204", "W8,W13" },
            { "212203", "W2,W3,W4,W7,W9,W8,W11,W12,W13" }, { "212202", "W2,W3,W14" }, { "221221", "W1,W12" },
            { "221231", "W1,W12" }, { "221211", "W1,W11" }, { "221212", "W1,W11" }, { "221213", "W1,W11" },
            { "221241", "W1,W7" }, { "221101", "W11,W12" } };

    public static final String[][] EL_RECOMMEND_SCALES_RX = { { "111001", "W5,W1" }, { "112101", "W3,W4,W1" },
            { "112301", "W8,W7,W5,W4,W1" }, { "112201", "W5,W4" }, { "112204", "W7,W2" },
            { "112203", "W8,W7,W5,W4,W1" }, { "112202", "W5,W4" }, { "121221", "W1,W3" }, { "121231", "W1,W3,W4" },
            { "121211", "W8,W3" }, { "121212", "W8,W3" }, { "121213", "W8,W3" }, { "121101", "W8,W2" } };

    /**
     * 是否是学生量表
     * 
     * @param scale
     * @return
     */
    public static boolean isStuScale(Scale scale) {
        return (scale.getFlag() & ScaleUtils.SCALE_EL_FLAG) > 0 | (scale.getFlag() & ScaleUtils.SCALE_SE_FLAG) > 0
                | (scale.getFlag() & ScaleUtils.SCALE_HS_FLAG) > 0;
    }

    /**
     * 获得有效的维度，用来做群体分析或其他地方
     * 
     * @param scale
     * @return
     */
    public static List<Dimension> getValidateDims(Scale scale) {
        List<Dimension> result = new ArrayList<Dimension>();
        // 产权量表取
        String[] dimIds = null;
        // 《小学生心理健康量表》
        if (scale.getId().equals("100011")) {
            dimIds = new String[] { "W3", "W6", "W1", "W9", "W2" };
        } else if (scale.getId().equals("200011")) {
            // 《中学生心理健康量表》
            dimIds = new String[] { "W5", "W10", "W15" };
        }
        if (dimIds != null) {
            for (String dimId : dimIds) {
                result.add(scale.findDimension(dimId));
            }
        } else if ((scale.getFlag() & ScaleUtils.SCALE_PROPER_FLAG) > 0) {
            for (Dimension dim : scale.getRootDimensions()) {
                if (!dim.getId().equalsIgnoreCase(Dimension.SUM_SCORE_DIM)) {
                    result.add(dim);
                }
            }
        } else if (scale.getId().equals("902")) {// 902没有w9,
                                                 // w9为测谎题的维度,《学生心理健康测验》
            for (Dimension dim : scale.getDimensions()) {
                if (!dim.getId().equalsIgnoreCase(Dimension.SUM_SCORE_DIM) && !dim.getId().equalsIgnoreCase("W9")) {
                    result.add(dim);
                }
            }
        } else {
            for (Dimension dim : scale.getDimensions()) {
                if (!dim.getId().equalsIgnoreCase(Dimension.SUM_SCORE_DIM) && dim.isLeaf()) {
                    result.add(dim);
                }
            }
        }
        return result;
    }

    static public void treatDimensionBlock(DimensionBlock block) {
        if (block.getId().equals("W10") && block.getQuestionnaire().getScaleId().equals("200011")) {
            block.setFinalScore(MathUtils.round(6.00 - block.getAScore().doubleValue(), 2));
            block.setRank(6 - block.getRank());
        }
    }

    public static boolean isMoralityScale(String scaleId) {
        return scaleId.endsWith("131000001") || scaleId.endsWith("130110001");
    }

    public static List<ScaleInfo> filterThreadAngleForNoStudent(List<ScaleInfo> scaleinfoList) {
        ListIterator<ScaleInfo> it = scaleinfoList.listIterator();
        while (it.hasNext()) {
            ScaleInfo scaleinfo = it.next();
            String scaleid = scaleinfo.getCode();
            if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                it.remove();
        }
        return scaleinfoList;
    }

    public static String getThreeAngleScaleForTeacherByGradeid(int gradeid) {
        if (gradeid < 7)
            return H_THIRD_ANGLE_SCALES_TEACHER[0];
        else
            return H_THIRD_ANGLE_SCALES_TEACHER[1];
    }

    public static String getThreeAngleScaleForParentByGradeid(int gradeid) {
        if (gradeid < 7)
            return H_THIRD_ANGLE_SCALES_PARENT[0];
        else
            return H_THIRD_ANGLE_SCALES_PARENT[1];
    }

    public static String getThreeAngleScaleForTeacherByScaleId(String stuScaleId) {
        if (ArrayUtils.contains(H_THIRD_ANGLE_SCALES_P, stuScaleId))// 小学生心理健康量表
            return H_THIRD_ANGLE_SCALES_P[2];
        if (ArrayUtils.contains(H_THIRD_ANGLE_SCALES_M, stuScaleId))// 小学生心理健康量表
            return H_THIRD_ANGLE_SCALES_M[2];
        return null;
    }

    public static String getThreeAngleScaleForParentByScaleId(String stuScaleId) {
        if (ArrayUtils.contains(H_THIRD_ANGLE_SCALES_P, stuScaleId))// 小学生心理健康量表
            return H_THIRD_ANGLE_SCALES_P[1];
        if (ArrayUtils.contains(H_THIRD_ANGLE_SCALES_M, stuScaleId))// 小学生心理健康量表
            return H_THIRD_ANGLE_SCALES_M[1];
        return null;
    }
}
