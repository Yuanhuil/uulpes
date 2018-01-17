package edutec.scale.exam;

import org.springframework.beans.factory.annotation.Autowired;

import edutec.scale.db.CachedScaleMgr;

public final class ExamUtils implements ExamConsts {
    @Autowired
    public CachedScaleMgr cachedScaleMgr;

    /**
     * // 心检卡测试产权量表时 // 使用心检卡直接输入卡号进入,要为每个量表新建立一份对象，因为原来的设计是针对学生用户的,
     * 个人信息已经存在于数据库中，共享一份scale，其scale的个人题目不可视，而心检卡需要得到那个人的个人信息，保存在数据库中 //
     * 所以，使用心检尺是针对个人用户的,需要重新建立个人信息，那么得新建一个scale，以此建立一个问卷
     * 
     * @return
     */
    /*
     * public Scale getScaleForTest(String scaleId, User answerUser) { Scale
     * scale = cachedScaleMgr.get(scaleId, true); int visitRoom =
     * ExamUtils.getVisitRoomForUser(answerUser);
     * 
     * return scale; }
     */
    public static String getExamDoTable(int typeflag) {
        if (typeflag == 1) {
            return "examdo_student";
        } else {
            return "examdo_teacher";
        }
    }

    /**
     * 如果通过应用程序做的测试，则测试结果放入examresult_student或examresult_teacher表中，
     * 否则都放入examresult_individual，不论用户是什么身份
     * 
     * @param roleFlag
     *            用户角色
     * @param visitRoom
     *            在哪里做题
     * @return
     */
    public static String getExamResultTable(int typeFlag, int visitRoom) {
        if (visitRoom == ROOM_APP || visitRoom == ROOM_IMPORT) {
            if (typeFlag == 1) {
                return "examresult_student";
            } else if (typeFlag == 2) {
                return "examresult_teacher";
            }
            return "examresult_individual";
        } else {
            return "examresult_individual";
        }
    }

    public static String getExamDimResultTable(int typeFlag, int visitRoom) {
        if (visitRoom == ROOM_APP || visitRoom == ROOM_IMPORT) {
            if (typeFlag == 1) {
                return "examresult_dim_student";
            } else if (typeFlag == 2) {
                return "examresult_dim_teacher";
            }
            throw new IllegalArgumentException("没符合角色");
        } else {
            throw new IllegalArgumentException("其它不记录");
        }
    }

    public static String getExamResultTable(int typeFlag) {
        if (typeFlag == 1) {
            return "examresult_student";
        } else if (typeFlag == 2) {
            return "examresult_teacher";
        }
        return "examresult_individual";

    }

    public static String getStuPartERTable(long orgid) {
        long a = orgid % 10;
        return "examresult_student_" + a;
    }

    public static String getStuPartAnswerTable(long userid) {
        long a = userid % 10;
        return "exam_stu_answer_" + a;
    }

    public static String getExamDimResultTable(int typeFlag) {

        if (typeFlag == 1) {
            return "examresult_dim_student";
        } else if (typeFlag == 2) {
            return "examresult_dim_teacher";
        }
        throw new IllegalArgumentException("没符合角色");

    }

    /**
     * 性别、姓名、生日、年级（如果是学校用户）
     * 
     * @param user
     * @param questionnaire
     */
    /*
     * public static void copyUserPropsToIndivOfQuestionaire(User user,
     * Questionnaire questionnaire) { //if (questionnaire.getIndividual() ==
     * null) { //return; //} // 先从用户本身提取数据 QuestionBlock genderQ =
     * questionnaire.findIndivQuestionBlock(Constants.GENDER_PROP);
     * QuestionBlock nameQ =
     * questionnaire.findIndivQuestionBlock(Constants.NAME_PROP); QuestionBlock
     * birthdayQ =
     * questionnaire.findIndivQuestionBlock(Constants.BIRTHDAY_PROP); if
     * (genderQ != null && StringUtils.isEmpty(genderQ.getAnswer())) {
     * SelectionQuestion sGenderQ = (SelectionQuestion) genderQ.getQuestion();
     * List<Option> list = sGenderQ.getOptions(); int i = 0; for (; i <
     * list.size(); ++i) { Option o = list.get(i); if
     * (o.getValue().equals(user.getGender())) { genderQ.setAnswer(i + "");
     * genderQ.setScore(NumberUtils.toInt(o.getValue() + "")); break; } } } if
     * (nameQ != null && StringUtils.isEmpty(nameQ.getAnswer())) {
     * nameQ.setAnswer(user.getName()); nameQ.setScore(0); } if (birthdayQ !=
     * null && StringUtils.isEmpty(birthdayQ.getAnswer())) { PropObject
     * propObject = (PropObject) user; // propObject.load(); Map<String, Object>
     * attrMap = propObject.getProps(); if (MapUtils.isNotEmpty(attrMap)) {
     * Object birthday = attrMap.get(Constants.BIRTHDAY_KEY_PROP); // 生日 if
     * (birthday != null) { birthdayQ.setAnswer((String) birthday); } }
     * birthdayQ.setScore(0); } // 如果是学校对象，还要放入年级属性数据 if (user instanceof
     * SchoolUser) { SchoolUser schoolUser = (SchoolUser) user; QuestionBlock
     * qblkGrade =
     * questionnaire.findIndivQuestionBlock(Constants.GRADE_ORDER_ID_PROP); if
     * (qblkGrade != null && StringUtils.isEmpty(qblkGrade.getAnswer())) {
     * SelectionQuestion sGrade = (SelectionQuestion) qblkGrade.getQuestion();
     * List<Option> list = sGrade.getOptions(); int i = 0; for (; i <
     * list.size(); ++i) { Option o = list.get(i); if
     * (o.getValue().equals(schoolUser.getGradeOrderId() + "")) {
     * qblkGrade.setAnswer(i + "");
     * qblkGrade.setScore(schoolUser.getGradeOrderId()); break; } } } } }
     * 
     * public static PropObject castPropObject(User user) { return (PropObject)
     * user; } public static PropObject loadUser(User user) { PropObject
     * propObject = castPropObject(user); if (user.getObjectIdentifier() > 0) {
     * propObject.load(); } return propObject; }
     * 
     * public static int getVisitRoomForUser(User user) { PropObject propObject
     * = castPropObject(user); return
     * propObject.getPropInt(Constants.VISITROOM_PROP); } public static void
     * setVisitRoomForUser(User user, int visitRoom) { PropObject propObject =
     * castPropObject(user); propObject.setProp(Constants.VISITROOM_PROP,
     * visitRoom); }
     */
}
