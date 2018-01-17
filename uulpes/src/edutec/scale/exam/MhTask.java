package edutec.scale.exam;

import java.io.Serializable;

public class MhTask implements Serializable {
    private long parentId;
    private long teacherId;
    private long studentId;
    private int gradeOrderId;
    private long resultId;
    private int flag;
    private String loTime;
    private String hiTime;

    public String getLoTime() {
        return loTime;
    }

    public void setLoTime(String loTime) {
        this.loTime = loTime;
    }

    public String getHiTime() {
        return hiTime;
    }

    public void setHiTime(String hiTime) {
        this.hiTime = hiTime;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getGradeOrderId() {
        return gradeOrderId;
    }

    public void setGradeOrderId(int gradeOrderId) {
        this.gradeOrderId = gradeOrderId;
    }

    public long getResultId() {
        return resultId;
    }

    public void setResultId(long resultId) {
        this.resultId = resultId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
