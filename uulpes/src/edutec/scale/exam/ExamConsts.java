package edutec.scale.exam;

public interface ExamConsts {
    /// ftl模板需要的替换字
    final String FTL_USER_KEY = "user";
    final String FTL_ANSWER_RESULT_KEY = "anwserResult";
    final String FTL_SCALE_KEY = "scale";
    final String FORBID_REPORT = "forbidReport";
    final String CHART_PARAM = "chartParam";

    /* 用户在哪里做题 */
    int ROOM_APP = 1; // 使用程序做题
    int ROOM_IND = 2; // 使用网页做题
    int ROOM_CEE_CARD = 3; // 使用高考卡片做题
    int ROOM_PSY_CARD = 4; // 使用心检卡片做题

    int ROOM_IMPORT = 5; // 使用心检卡片做题
}
