package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class MhTask implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private long taskid;
    private long parentId;
    private long teacherId;
    private long studentId;
    private int gradeId;
    private long resultId;
    private int flag;
    private String loTime;
    private String hiTime;
    private String scaleid_t;
    private String scaleid_p;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

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

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
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

    public String getScaleid_t() {
        return scaleid_t;
    }

    public void setScaleid_t(String scaleid_t) {
        this.scaleid_t = scaleid_t;
    }

    public String getScaleid_p() {
        return scaleid_p;
    }

    public void setScaleid_p(String scaleid_p) {
        this.scaleid_p = scaleid_p;
    }

}
