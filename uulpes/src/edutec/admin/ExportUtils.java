package edutec.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ExportUtils {
    // 以下表需要导入
    public static final String ACCOUNT_TABLE = "account";

    public static final String SCHOOLUSER_TABLE = "schooluser";
    public static final String SCHOOLUSER_ATTR_TABLE = "schooluser_attr";

    public static final String ORG_TABLE = "organization";
    public static final String ORG_ATTR_TABLE = "organization_attr";

    public static final String CLASS_TABLE = "class";
    public static final String CLASS_ATTR_TABLE = "class_attr";

    public static final String EXAMRESULT_STUDENT_TABLE = "examresult_student";
    public static final String EXAMRESULT_TEACHER_TABLE = "examresult_teacher";

    public static final String EXAMRESULT_DIM_STUDENT_TABLE = "examresult_dim_student";
    public static final String EXAMRESULT_DIM_TEACHER_TABLE = "examresult_dim_teacher";

    public static final String INDIVIDUAL_TABLE = "individual";
    public static final String INDIVIDUAL_ATTR_TABLE = "individual_attr";

    /* ======================== */
    // 临时路径
    public static final String DATA_TMP_DIR = "data_tmp"; // 存放生成的文件
    public static final String DATA_MASS_DIR = "data_mass";// 存放压缩后的文件
    public static final String DATA_STORE_DIR = "data_store";// 存放上传上来的文件
    public static final String SCALE_TMP_DIR = "scale_tmp_dir";// scale模板存放的目录

    /* 先删除本地的这个学校的数据 */
    public static final String DELETE_SQLS[] = {
            "delete from examresult_student where user_id in (select id from schooluser where org_id=?)",
            "delete from examresult_teacher where user_id in (select id from schooluser where org_id=?)",
            "delete from examresult_dim_student  where org_id=?", "delete from examresult_dim_teacher  where org_id=?",
            "delete from individual_attr where id in (select id from individual where child_id in (select id from schooluser where org_id=?))",
            "delete from individual where child_id in (select id from schooluser where org_id=?)",
            "delete from schooluser_attr where id in (select id from schooluser where org_id=?)",
            "delete from schooluser where org_id=?",
            "delete from class_attr where  id in  (select id from class where org_id=?)",
            "delete from class where org_id=?", "delete from organization_attr where id=?",
            "delete from organization where id=?", "delete from account where org_id=?" };

    static final char SEPETOR = '&';
    private static final String SELECT_SQL = "SELECT %s FROM %s";// WHERE id >
                                                                 // %d";
    private static final String SELECT_DIM_SQL = "SELECT %s FROM %s";// > %d";

    private static final Map<String, String> table2Fields = new HashMap<String, String>();

    public static final String MAX_ORG_SQL = "select max(id) from organization";
    public static final String MAX_ACC_SQL = "select max(id) from account";
    public static final String MAX_CLS_SQL = "select max(id) from class";
    public static final String MAX_EXS_SQL = "select max(id) from examresult_student";
    public static final String MAX_TCH_SQL = "select max(id) from examresult_teacher";

    static {
        table2Fields.put(ACCOUNT_TABLE,
                "id,sn,username,password,valid_period,create_time,update_time,role_flag,state,org_id,user_id");
        table2Fields.put(SCHOOLUSER_TABLE,
                "id,account_sn,name,gender,role_flag,org_id ,grade_order_id,class_id, frozen_flag,frozen_date");
        table2Fields.put(SCHOOLUSER_ATTR_TABLE, "id,val_s");
        table2Fields.put(ORG_TABLE, "id, account_sn,level");
        table2Fields.put(ORG_ATTR_TABLE, "id,val_s");
        table2Fields.put(CLASS_TABLE, "id,title,org_id,grade_order_id,class_order_id,frozen_flag,frozen_date");
        table2Fields.put(CLASS_ATTR_TABLE, "id,val_s");
        table2Fields.put(EXAMRESULT_STUDENT_TABLE,
                "id,user_id,scale_id,individual_score,question_score,dim_score,ok_time,start_time");
        table2Fields.put(EXAMRESULT_TEACHER_TABLE,
                "id,user_id,scale_id,individual_score,question_score,dim_score,ok_time,start_time");
        table2Fields.put(EXAMRESULT_DIM_STUDENT_TABLE,
                "result_id,user_id,scale_id,org_id,grade_order_id,class_id,gender,ok_time,"
                        + "w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12,w13,w14,w15,w16,w17,w18,w19,w20,w21,w22,w23,w24,w25");
        table2Fields.put(EXAMRESULT_DIM_TEACHER_TABLE,
                "result_id,user_id,scale_id,org_id,grade_order_id,class_id,gender,ok_time,"
                        + "w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12,w13,w14,w15,w16,w17,w18,w19,w20,w21,w22,w23,w24,w25");
        table2Fields.put(INDIVIDUAL_TABLE, "id,account_sn,name,gender,role_flag,child_id,frozen_flag");
        table2Fields.put(INDIVIDUAL_ATTR_TABLE, "id,val_s");
    }

    public Map<String, String> createSelSQLs() throws SQLException {
        Map<String, String> result = new HashMap<String, String>();
        /*
         * final String sql =
         * "select max_org_id,max_account_id,max_class_id,max_result_id from export_log"
         * ; final long ids[] = new long[4]; DbOperater dbOperater = new
         * DbOperater(); QueryRunner runner = dbOperater.getRunner();
         * runner.query(sql, new ResultSetHandler(){ public Object
         * handle(ResultSet rs) throws SQLException { if (rs.next()) { ids[0] =
         * rs.getLong(1); ids[1] = rs.getLong(2); ids[2] = rs.getLong(3); ids[3]
         * = rs.getLong(4); } return null; } }); long orgId = ids[0]; long
         * accountId = ids[1]; long classId = ids[2]; long resultId = ids[3];
         */
        final long ids[] = new long[4];
        long orgId = ids[0];
        long accountId = ids[1];
        long classId = ids[2];
        long resultId = ids[3];
        // 1.机构
        String fields = null;
        if (ids[0] == 0) {
            fields = table2Fields.get(ORG_TABLE);
            result.put(ORG_TABLE, String.format(SELECT_SQL, fields, ORG_TABLE, orgId));
            fields = table2Fields.get(ORG_ATTR_TABLE);
            result.put(ORG_ATTR_TABLE, String.format(SELECT_SQL, fields, ORG_ATTR_TABLE, orgId));
        }
        // 2.帐户
        fields = table2Fields.get(ACCOUNT_TABLE);
        result.put(ACCOUNT_TABLE, String.format(SELECT_SQL, fields, ACCOUNT_TABLE, accountId));

        // 3.班级表
        fields = table2Fields.get(CLASS_TABLE);
        result.put(CLASS_TABLE, String.format(SELECT_SQL, fields, CLASS_TABLE, classId));
        fields = table2Fields.get(CLASS_ATTR_TABLE);
        result.put(CLASS_ATTR_TABLE, String.format(SELECT_SQL, fields, CLASS_ATTR_TABLE, classId));

        // 4.1 学校用户
        fields = table2Fields.get(SCHOOLUSER_TABLE);
        result.put(SCHOOLUSER_TABLE, String.format(SELECT_SQL, fields, SCHOOLUSER_TABLE, accountId));
        fields = table2Fields.get(SCHOOLUSER_ATTR_TABLE);
        result.put(SCHOOLUSER_ATTR_TABLE, String.format(SELECT_SQL, fields, SCHOOLUSER_ATTR_TABLE, accountId));

        // 4.2 学校用户
        fields = table2Fields.get(INDIVIDUAL_TABLE);
        result.put(INDIVIDUAL_TABLE, String.format(SELECT_SQL, fields, INDIVIDUAL_TABLE, accountId));
        fields = table2Fields.get(INDIVIDUAL_ATTR_TABLE);
        result.put(INDIVIDUAL_ATTR_TABLE, String.format(SELECT_SQL, fields, INDIVIDUAL_ATTR_TABLE, accountId));

        // 5.测试结果表
        /*
         * fields = table2Fields.get(EXAMRESULT_STUDENT_TABLE);
         * result.put(EXAMRESULT_STUDENT_TABLE, String.format(SELECT_SQL,
         * fields, EXAMRESULT_STUDENT_TABLE, resultId)); fields =
         * table2Fields.get(EXAMRESULT_TEACHER_TABLE);
         * result.put(EXAMRESULT_TEACHER_TABLE, String.format(SELECT_SQL,
         * fields, EXAMRESULT_TEACHER_TABLE, resultId));
         */
        result.put(EXAMRESULT_STUDENT_TABLE, String.format(SELECT_SQL, "*", EXAMRESULT_STUDENT_TABLE));
        result.put(EXAMRESULT_TEACHER_TABLE, String.format(SELECT_SQL, "*", EXAMRESULT_TEACHER_TABLE));

        // 6.测试维度表
        fields = table2Fields.get(EXAMRESULT_DIM_STUDENT_TABLE);
        result.put(EXAMRESULT_DIM_STUDENT_TABLE,
                String.format(SELECT_DIM_SQL, fields, EXAMRESULT_DIM_STUDENT_TABLE, resultId));
        fields = table2Fields.get(EXAMRESULT_DIM_TEACHER_TABLE);
        result.put(EXAMRESULT_DIM_TEACHER_TABLE,
                String.format(SELECT_DIM_SQL, fields, EXAMRESULT_DIM_TEACHER_TABLE, resultId));

        return result;
    }

}
