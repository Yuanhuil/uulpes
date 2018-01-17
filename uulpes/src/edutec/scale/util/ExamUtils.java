package edutec.scale.util;

public class ExamUtils {
    public static String getStuPartERTable(long orgid) {
        long a = orgid % 10;
        return "examresult_student_" + a;
    }

    public static String getStuPartAnswerTable(long userid) {
        long a = userid % 10;
        return "exam_stu_answer_" + a;
    }
}
