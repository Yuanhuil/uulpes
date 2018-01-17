package edutec.scale.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import heracles.util.Pools;

/**
 * 根据学生所做的量表，来确认完全心检报告类型
 * 
 * @author
 */
public class ScaleGroup implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2906974038026240086L;
    /*
     * public static void main(String[] args) { String scaleIds[] = {"00011",
     * "12202", "12301","11001", "21211", "21212", "21213"}; Map<Integer,
     * String[]> m = ScaleGroup.split(scaleIds); for
     * (@SuppressWarnings("unused") Map.Entry<Integer, String[]> ent
     * :m.entrySet()) { // System.out.println(ent.getKey());
     * //System.out.println(ArrayUtils.toString(ent.getValue())); } }
     */

    // 共5种类型
    final public static int MENTAL_HEALTH = 0;// 心理健康
    final public static int CHARACTER = 1;// 性格
    final public static int DISPOSITION = 2;// 性情
    final public static int PERSONALITY = 3;// 人格
    final public static int LEARNINGABILITY = 4;// 学能
    final public static int POTENTIAL = 5;// 潜能

    final public static int SCALE_GROUP[] = { MENTAL_HEALTH, CHARACTER, DISPOSITION, PERSONALITY, LEARNINGABILITY,
            POTENTIAL };
    final public static String SCALE_GROUP_TL[] = { "心理健康", "性格", "性情", "人格", "学能", "潜能" };

    // 以下量表分组，其中的编号为量表编号的后5位
    // 心理健康类量表
    public final static String mentalHealthScaleIds[] = { "00011" };
    // 性格类量表
    public final static String characterScaleIds[] = { "12201", "12202", "12203", "12204" };
    // 性情类量表
    public final static String dispositionScaleIds[] = { "12101", "12201", "12202", "12203", "12204", "12301" };
    // 人格类量表
    public final static String personalityScaleIds[] = { "11001", "12101", "12201", "12202", "12203", "12204",
            "12301" };
    // 学能类量表
    public final static String learningAbilityScaleIds[] = { "21211", "21212", "21213", "21221", "21231", "21241" };
    // 潜能类量表
    public final static String potentialScaleIds[] = { "21101", "21211", "21212", "21213", "21221", "21231", "21241" };

    /**
     * 对学生所做过的量表编号做计算，以确认会产生应该生成哪几类完全心检报告
     * 
     * @param scaleIds
     *            学生所做过的量表编号
     * @return
     */
    static public Map<Integer, String[]> split(String scaleIds[]) {

        // Validate.isTrue(scaleIds.length >= 2);
        Map<Integer, String[]> result = new LinkedHashMap<Integer, String[]>(5);
        List<String> list = null;
        try {
            list = Pools.getInstance().borrowStringList();
            if (scaleIds.length == 1) {
                // 首先判断是否只做过心理健康量表，这部分没有被运行过，具体如何处理待定
                if ("100011".equals(scaleIds[0]) || "200011".equals(scaleIds[0]))
                    result.put(MENTAL_HEALTH, list.toArray(new String[0]));
            } else {
                Validate.isTrue(scaleIds.length >= 2);
                // 先确定是否做了心理健康量表
                containEndWith(scaleIds, mentalHealthScaleIds, list);
                if (!list.isEmpty()) {
                    result.put(MENTAL_HEALTH, list.toArray(new String[0])); // 心理健康
                }
                int m = 0;
                // 是否做了人格类量表
                if (containEndWith(scaleIds, "11001")) {
                    containEndWith(scaleIds, personalityScaleIds, list);
                    m = PERSONALITY;// 人格
                } else if (containEndWith(scaleIds, "12101") || containEndWith(scaleIds, "12301")) {
                    // 是否做了《小学生学科兴趣兴量表》《中学生学科兴趣量表》or 《小学生情绪适应量表》《中学生情绪适应量表》
                    // 将所做过的性情类量表，放入list中
                    containEndWith(scaleIds, dispositionScaleIds, list);
                    m = DISPOSITION;// 性情
                } else {
                    // 没有做过《兴趣兴量表》or《情绪适应量表》，则按性格类量表计算，将结果放入list中
                    containEndWith(scaleIds, characterScaleIds, list);
                    m = CHARACTER;// 性格
                }
                // 如果list不为空,放入一个类型以及其类量表
                if (list.size() > 1) {
                    result.put(m, list.toArray(new String[0]));
                }
                // 添加潜能段
                if (containEndWith(scaleIds, "21101")) {
                    containEndWith(scaleIds, potentialScaleIds, list);
                    m = POTENTIAL; // 潜能
                } else {
                    containEndWith(scaleIds, learningAbilityScaleIds, list);
                    m = LEARNINGABILITY; // 学能
                }
                if (list.size() > 1) {
                    result.put(m, list.toArray(new String[0]));
                }
            }
            return result;
        } finally {
            Pools.getInstance().returnStringList(list);
        }
    }

    public static void containEndWith(String strs[], String suffixes[], List<String> list) {
        list.clear();
        for (int i = 0; i < strs.length; ++i) {
            for (int j = 0; j < suffixes.length; ++j) {
                if (strs[i].endsWith(suffixes[j])) {
                    list.add(strs[i]);
                    break;
                }
            }
        }
    }

    public static boolean containEndWith(String strs[], String suffix) {
        for (int i = 0; i < strs.length; ++i) {
            if (strs[i].endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
