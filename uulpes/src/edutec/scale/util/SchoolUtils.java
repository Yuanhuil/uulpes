// package edutec.scale.util;
//
// import java.util.Collection;
// import java.util.Iterator;
// import java.util.LinkedHashMap;
// import java.util.Map;
//
// import org.apache.commons.lang.math.IntRange;
//
// import edutec.primary.account.AccountSn;
// import edutec.primary.domain.Organization;
// import edutec.primary.enums.SchoolPartEnum;
//
// public class SchoolUtils {
// public static final int ELEM_SCHCOOL = 1;
// public static final int HIGH_SCHCOOL = 2;
// public static final int WHOLE_SCHOOL = 3;
//
// public static final int ELEM_STU_GROUP = 1;// 小学学生
// public static final int JUNIOR_STU_GROUP = 2;// 中学学生
// public static final int SENIOR_STU_GROUP = 3;// 高中学生
// public static final int VOCATIONAL_STU_GROUP = 5;// 中职学生
//
// public static final int TECHER_GROUP = 4;// 教师团体对象
//
// public static final String ELEM_STU_GROUP_DESC = "小学学生";
// public static final String JUNIOR_STU_GROUP_DESC = "初中学生";
// public static final String SENIOR_STU_GROUP_DESC = "高中学生";
// public static final String VOCATIONAL_STU_GROUP_DESC = "中职学生";
// public static final String TECHER_GROUP_DESC = "学校教师";
//
// public static final Map<String, String> getPartGroup(Organization
// organization) {
// Map<String, String> result = new LinkedHashMap<String, String>(4);
// SchoolPartEnum partEnum = organization.getSchoolPartEnum();
// Collection<String> partTags = partEnum.getPartTags();
// for (String tag : partTags) {
// if (tag.charAt(0) == SchoolPartEnum.ELEMENTARY_FLAG) {
// result.put(ELEM_STU_GROUP + "", ELEM_STU_GROUP_DESC);
// } else if (tag.charAt(0) == SchoolPartEnum.JUNIOR_FLAG) {
// result.put(JUNIOR_STU_GROUP + "", JUNIOR_STU_GROUP_DESC);
// } else if (tag.charAt(0) == SchoolPartEnum.SENIOR_FLAG) {
// result.put(SENIOR_STU_GROUP + "", SENIOR_STU_GROUP_DESC);
// }else if (tag.charAt(0) == SchoolPartEnum.VOCATIONAL_FLAG) {
// result.put(VOCATIONAL_STU_GROUP + "", VOCATIONAL_STU_GROUP_DESC);
// }
// }
// result.put(TECHER_GROUP + "", TECHER_GROUP_DESC);
// return result;
// }
//
// public static int getSchoolType(Organization organization) {
// SchoolPartEnum partEnum = organization.getSchoolPartEnum();
// Collection<String> tags = partEnum.getPartTags();
// Iterator<String> iterator = tags.iterator();
// if (tags.size() > 1) {
// if (iterator.next().charAt(0) == SchoolPartEnum.JUNIOR_FLAG) {
// return HIGH_SCHCOOL; // 完全中学（初级中学4年+高级中学3年）
// }
// return WHOLE_SCHOOL;
// }
// if (iterator.next().charAt(0) == SchoolPartEnum.ELEMENTARY_FLAG) {
// return ELEM_SCHCOOL;
// } else {
// return HIGH_SCHCOOL;
// }
// }
//
// /**
// * 判断年级在哪个学段中
// *
// * @param org
// * @param gradeOrderId
// * @return 1-处于小学段 2-处于中学 3-处于高中
// */
// public static int whichPartForGrade(Organization org, int gradeOrderId) {
// AccountSn sn = new AccountSn();
// sn.parseString(org.getAccountSn());
// SchoolPartEnum partEnum = sn.getSchoolPartEnum();
// int gradeOd = gradeOrderId;
// Map<String, Map<String, String>> ml = partEnum.getGradeMapList();
// Map<String, String> mpe = ml.get(SchoolPartEnum.ELEMENTARY_FLAG + "");
// Map<String, String> mpj = ml.get(SchoolPartEnum.JUNIOR_FLAG + "");
// // Map<String, String> mps = ml.get(SchoolPartEnum.SENIOR_FLAG);
// if (mpe != null) {
// if (gradeOd <= mpe.size()) {
// return 1;
// }
// }
// int start;
// int end;
// int sz;
// if (mpj != null) {
// sz = mpj.size();
// if (sz == 4) {
// start = 5;
// } else {
// start = 6;
// }
// end = start + sz;
// if (gradeOd <= end && gradeOd >= start) {
// return 2;
// }
// }
// // 不是小学、中学，一定是高中
// return 3;
// }
// /**
// * 由于我们的量表分中、小学，常模定在年级六。所以<br>
// *
// * 对于小学5年制，且有中学的，要初1按小学处理, 选中学年级时，要移除初1<br>
// *
// * 此方法要连在getPartGroup后用
// *
// * @param org
// * @param groupId
// * @return
// */
// public static Map<String, String> getGradesForGroup(Organization org, int
// groupId) {
// SchoolPartEnum partEnum = org.getSchoolPartEnum();
// Map<String, Map<String, String>> gaList = partEnum.getGradeMapList();
// Map<String, String> result = new LinkedHashMap<String, String>();
// String tag = partEnum.getName();
// if (tag.startsWith("e5")) {
// if (groupId == ELEM_STU_GROUP) {
// result.putAll(gaList.get(SchoolPartEnum.ELEMENTARY_FLAG + ""));
// if (gaList.size() > 0) {
// Map<String, String> tmp = gaList.get(SchoolPartEnum.JUNIOR_FLAG + "");
// Iterator<Map.Entry<String, String>> it = tmp.entrySet().iterator();
// Map.Entry<String, String> ent = it.next();
// result.put(ent.getKey(), ent.getValue());
// }
// } else if (groupId == JUNIOR_STU_GROUP) {
// result.putAll(gaList.get(SchoolPartEnum.JUNIOR_FLAG + ""));
// Iterator<Map.Entry<String, String>> it = result.entrySet().iterator();
// it.next();
// it.remove();
// } else if (groupId == SENIOR_STU_GROUP) {
// result.putAll(gaList.get(SchoolPartEnum.SENIOR_FLAG + ""));
// }else if (groupId == VOCATIONAL_STU_GROUP) {
// result.putAll(gaList.get(SchoolPartEnum.VOCATIONAL_FLAG + ""));
// }
// } else {
// switch (groupId) {
// case SchoolUtils.ELEM_STU_GROUP: // 小学学生
// result.putAll(gaList.get(SchoolPartEnum.ELEMENTARY_FLAG + ""));
// break;
// case SchoolUtils.JUNIOR_STU_GROUP:// 中学学生
// result.putAll(gaList.get(SchoolPartEnum.JUNIOR_FLAG + ""));
// break;
// case SchoolUtils.SENIOR_STU_GROUP:// 高中学生
// result.putAll(gaList.get(SchoolPartEnum.SENIOR_FLAG + ""));
// break;
// case SchoolUtils.VOCATIONAL_STU_GROUP:// 中职学生
// result.putAll(gaList.get(SchoolPartEnum.VOCATIONAL_FLAG + ""));
// break;
// }
// }
// return result;
// }
// /**
// * 获得年级区间
// *
// * @param groupId
// * @return
// */
// public static IntRange getGradeRangeForGroup(int groupId,boolean isScale) {
// IntRange result = null;
// if (groupId == ELEM_STU_GROUP) {
// if (isScale){
// result = new IntRange(3, 6);
// }
// else {
// result = new IntRange(1, 6);
// }
// } else if (groupId == JUNIOR_STU_GROUP) {
// result = new IntRange(7, 9);
// } else if (groupId == SENIOR_STU_GROUP) {
// result = new IntRange(10, 12);
// }else if (groupId == VOCATIONAL_STU_GROUP) {
// result = new IntRange(13, 15);
// }
// return result;
// }
//
// public static boolean isStudentGroup(int groupId){
// return groupId == ELEM_STU_GROUP|groupId == JUNIOR_STU_GROUP|groupId ==
// SENIOR_STU_GROUP|groupId == VOCATIONAL_STU_GROUP;
// }
// public static boolean isTeacherGroup(int groupId){
// return groupId == TECHER_GROUP;
// }
// }