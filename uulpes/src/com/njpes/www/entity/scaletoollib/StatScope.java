package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.List;

public class StatScope implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StatScope() {
    }

    public StatScope(String cityId, List<String> countyId, List<String> townId, List<String> schoolId, String gradeId,
            String classId) {
        this.cityId = cityId;
        this.countyId = countyId;
        this.townId = townId;
        this.schoolId = schoolId;
        this.gradeId = gradeId;
        this.classId = classId;
    }

    private String cityId; // 市id
    private List<String> countyId; // 区县id
    private List<String> townId; // 乡镇id
    private List<String> schoolId; // 学校编号,对应xxdm
    private String gradeId; // 年级编号
    private String classId; // 班级编号
    private String startTime; // 开始时间
    private String endTime; // 结束时间
    private String scaleId; // 量表ids

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleIds) {
        this.scaleId = scaleIds;
    }

    public boolean isAll() {
        return (cityId == null && countyId == null && schoolId == null && gradeId == null && classId == null);
    }

    public boolean isCity() {
        return (cityId != null && countyId == null && schoolId == null);
    }

    public boolean isCounty() {
        return (countyId != null && schoolId == null);
    }

    public boolean isSchool() {
        return (schoolId != null && gradeId == null && classId == null);
    }

    public String getGradeId() {
        return gradeId;
    }

    public List<String> getTownId() {
        return townId;
    }

    public void setTownId(List<String> townId) {
        this.townId = townId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String starttime) {
        this.startTime = starttime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endtime) {
        this.endTime = endtime;
    }

    public List<String> getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(List<String> shcoolId) {
        this.schoolId = shcoolId;
    }

    public List<String> getCountyId() {
        return countyId;
    }

    public void setCountyId(List<String> countyId) {
        this.countyId = countyId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
