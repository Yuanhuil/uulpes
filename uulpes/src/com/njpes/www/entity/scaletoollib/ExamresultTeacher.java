package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.Date;

public class ExamresultTeacher implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.id
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.teacher_id
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Long teacherId;
    private String teacherName;
    private int roleId;
    private String roleName;
    private String gender;
    private String indentify;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.scale_id
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Integer scaleId;
    private String scaleName;
    private int questionnum;
    private int testtype;
    private int second;
    private String testtime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.ok_time
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Date okTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.start_time
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.warning_grade
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Integer warningGrade;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_teacher.valid_val
     * 
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    private Integer validVal;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.id
     * 
     * @return the value of examresult_teacher.id
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.id
     * 
     * @param id
     *            the value for examresult_teacher.id
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    public int getTesttype() {
        return testtype;
    }

    public void setTesttype(int testtype) {
        this.testtype = testtype;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.teacher_id
     * 
     * @return the value of examresult_teacher.teacher_id
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Long getTeacherId() {
        return teacherId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.teacher_id
     * 
     * @param teacherId
     *            the value for examresult_teacher.teacher_id
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.scale_id
     * 
     * @return the value of examresult_teacher.scale_id
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Integer getScaleId() {
        return scaleId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.scale_id
     * 
     * @param scaleId
     *            the value for examresult_teacher.scale_id
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setScaleId(Integer scaleId) {
        this.scaleId = scaleId;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getTesttime() {
        int minute = second / 60;
        int sc = second % 60;
        return minute + "分" + sc + "秒";
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.ok_time
     * 
     * @return the value of examresult_teacher.ok_time
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Date getOkTime() {
        return okTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.ok_time
     * 
     * @param okTime
     *            the value for examresult_teacher.ok_time
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setOkTime(Date okTime) {
        this.okTime = okTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.start_time
     * 
     * @return the value of examresult_teacher.start_time
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.start_time
     * 
     * @param startTime
     *            the value for examresult_teacher.start_time
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.warning_grade
     * 
     * @return the value of examresult_teacher.warning_grade
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Integer getWarningGrade() {
        return warningGrade;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.warning_grade
     * 
     * @param warningGrade
     *            the value for examresult_teacher.warning_grade
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setWarningGrade(Integer warningGrade) {
        this.warningGrade = warningGrade;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_teacher.valid_val
     * 
     * @return the value of examresult_teacher.valid_val
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public Integer getValidVal() {
        return validVal;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_teacher.valid_val
     * 
     * @param validVal
     *            the value for examresult_teacher.valid_val
     * @mbggenerated Sat Mar 21 23:18:41 CST 2015
     */
    public void setValidVal(Integer validVal) {
        this.validVal = validVal;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndentify() {
        return indentify;
    }

    public void setIndentify(String indentify) {
        this.indentify = indentify;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(int questionnum) {
        this.questionnum = questionnum;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}