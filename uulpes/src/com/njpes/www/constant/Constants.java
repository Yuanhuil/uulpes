
package com.njpes.www.constant;

import com.njpes.www.entity.baseinfo.Role;

public class Constants {
    /**
     * 操作名称
     */
    public static final String OP_NAME = "op";

    public static final String VIEW_NAME = "查看";

    public static final String ADD_NAME = "新增";

    public static final String EDIT_NAME = "修改";

    public static final String DELETE_NAME = "删除";

    public static final String AUDIT_NAME = "审核";

    public static final String SEND_NAME = "下发";

    public static final String IMPORT_NAME = "导入";

    public static final String DEFAULT_PASSWORD = "admin123";
    /**
     * 陶老师工作站内置 编码，在organization中应给为2， 2这个编码不能被任何机构占用，否则出错
     */
    public static final int TAOLAOSHI_STATION = 2;

    public static final long CHINA_ADMIN = 1;

    public static String READ_FILE_SERVER_URL = "";
    public static String UPLOAD_SERVER_PATH = "";
    public static final String UPLOAD_FILE_PATH = "upload_file/";
    /**
     * 消息key
     */
    public static final String MESSAGE = "message";

    /**
     * 错误key
     */
    public static final String ERROR = "error";

    /**
     * 上个页面地址
     */
    public static final String BACK_URL = "BackURL";

    public static final String IGNORE_BACK_URL = "ignoreBackURL";

    /**
     * 当前请求的地址 带参数
     */
    public static final String CURRENT_URL = "currentURL";

    /**
     * 当前请求的地址 不带参数
     */
    public static final String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";

    public static final String CONTEXT_PATH = "ctx";

    /**
     * 当前登录的用户
     */
    public static final String CURRENT_USER = "user";
    public static final String CURRENT_USER_ORG = "user_org";
    public static final String CURRENT_USERNAME = "username";

    public static final String ENCODING = "UTF-8";

    public static final String PAGE_LIST_NUM = "10";

    public static final String ACCOUNT_ID_PROP = "accountId";
    public static final String ACCOUNT_STATE_PROP = "state";
    public static final String ACCOUNT_SN_PROP = "accountSn";
    public static final String SN_PROP = "sn";

    public static final String AREACODE_PROP = "areaCode";
    public static final String ORG_ID_PROP = "orgId";
    public static final String ORG_LEV_PROP = "level";
    public static final String GRADE_ORDER_ID_PROP = "gradeOrderId";
    public static final String CLASS_ORDER_ID_PROP = "classOrderId";
    public static final String CLASS_ID_PROP = "classId";
    public static final String CLASS_NAME_PROP = "className";
    public static final String USER_ID_PROP = "userId";
    public static final String FLAG_PROP = "flag";
    public static final String OBJ_ID_PROP = "objId";
    public static final String TEACHER_ROLE_PROP = "teacherRole";
    public static final String GRADE_RANGE_PROP = "gradeRange";
    public static final String FROZEN_DATE_PROP = "frozenDate";
    public static final String FROZEN_FLAG_PROP = "frozenFlag";

    public static final String USER_FIELD_ID_PROP = "user_id";

    public static final String ID_PROP = "id";
    public static final String GENDER_PROP = "gender";
    public static final String NAME_PROP = "name";
    public static final String ATTR_IDS_PROP = "attrIds";
    public static final String ATTR_ID_PROP = "attrId";
    public static final String ATTR_OPT_ID_PROP = "attrOptId";
    public static final String GROUP_ID_PROP = "groupId";

    public static final String BIRTHDAY_PROP = "birthday";
    public static final String BIRTHDAY_KEY_PROP = "104";
    public static final String VISITROOM_PROP = "visitRoom";
    public static final String TEST_CARD_PROP = "testCardId";
    public static final String TABLE_PROP = "table";
    public static final String LOTIME_PROP = "loTime";
    public static final String HITIME_PROP = "hiTime";
    public static final String SCALEID_PROP = "scaleId";
    public static final String SCALEIDS_PROP = "scaleIds";
    public static final String LIMIT_FLAG_PROP = "limitFlag";
    public static final String DO_FLAG_PROP = "doFlag";
    public static final String LIMIT_PROP = "limit";

    public static final String WID_PROP = "wid";
    public static final String MESSAGE_KEY = "message";

    public static final String REQ_OBJECT = "o"; // 需求对象
    public static final String REQ_OSCHOOL = "s"; // 需求对象是学校用户
    public static final String REQ_OCLASS = "c"; // 需求对象是班级

    public static final String EXAMRESULT_ID_PROP = "resultId";
    /* 性别 */
    public static final int G_NONE = 0;
    public static final int G_MAN = 1;
    public static final int G_WOMEN = 2;
    public static final String G_DESC[] = { "不确定", "男", "女" };

    // 属性key
    public static final String NAME_KEY = "100";
    public static final String GENDER_KEY = "101";
    public static final String BIRTHDAY = "104";
    public static final String SCHOOL_NAME_KEY = "500";
    public static final String BIRTHDAY_KEY = BIRTHDAY_KEY_PROP;

    // 用户属性特定prop编号
    public static final int[] USR_PROP_NUMS = new int[] { 1 }; // 用户
    public static final int[] STU_PROP_NUMS = new int[] { 1, 2 }; // 学生
    public static final int[] TCH_PROP_NUMS = new int[] { 1, 6 }; // 教师
    public static final int[] CLS_PROP_NUMS = new int[] { 3 }; // 班级
    // int[] PAR_PROP_NUMS = new int[] { 4 }; // 父母
    public static final int[] SCH_PROP_NUMS = new int[] { 5 }; // 学校
    public static final int[] BUR_PROP_NUMS = new int[] { 8 }; // 教育局

    public static final String RESUTL_CODE = "resultCode";
    public static final String RESUTL_OK = "ok";
    public static final String RESUTL_FAIL = "fail";
    public static final String RESUTL_TEXT = "text";

    public static final String HANDLE_OBJECT = "object";

    public static final String PSID = "psid";

    // --------------------------------系统内置角色id不可修改---------------------------
    public static final long ROLE_PSY_TEACHER = 21;
    public static final long ROLE_PSY_CONSULTER = 22;

    // ------------------------------end
    // 系统内置角色id不可修改-------------------------add by zhaozhongcheng

    public static final String SUCCESE_MESSAGE = "successMessage";
    public static final String ERROR_MESSAGE = "errorMessage";
    // -----------------------------学年配置月份------------------------------------
    public static final int SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH = 8;// 8.1-3.1

    public static final int SCHOOLYEAR_SECONDTERM_BEGIN_MONTH = 3;// 3.1-7.31
    // ------------------------end by zhaozhongcheng
    // -------------------------------

    // -----------------------------目标用户和行政区编码------------------------------------
    public static String APPLICATION_USERLEVEL = "";// 市教委：3；县教委：4；学校：6
    public static String APPLICATION_PROVINCECODE = "";
    public static String APPLICATION_CITYCODE = "";
    public static String APPLICATION_APPHEADTITLE = "";
    public static String APPLICATION_APPFOOTER = "";

    public static boolean isNeedDog(Role role) {
        if (role.getId() == ROLE_PSY_CONSULTER || role.getId() == ROLE_PSY_TEACHER || role.getIsadmin().equals("1")) {
            return true;
        }
        return false;
    }
}
