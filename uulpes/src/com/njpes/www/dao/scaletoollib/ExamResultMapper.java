package com.njpes.www.dao.scaletoollib;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.scaletoollib.ExamResultForNorm;

import edutec.group.domain.DimDescription;
import edutec.group.domain.DimScoreGrade;
import edutec.group.domain.DimWarning;
import edutec.group.domain.DimWarningAndGradeStore;
import edutec.group.domain.DimWarningAndScoreGrade;
import edutec.group.domain.PropNorm;
import edutec.scale.exam.ExamResult;

public interface ExamResultMapper {

    ExamResult getExamResult(Map map);

    ExamResult getStudentExamResult(Map map);

    ExamResult getTeacherExamResult(Map map);

    ExamResult getExamResultMh(@Param("id") Long id, @Param("typeflag") int typeflag);

    List getStudentExamResults(Map map);

    List getTeacherExamResults(Map map);

    ExamResult getExamResultForIndv(Map map);

    ExamResult getExamResultForStu(Map map);

    List getExamResultsForStu(Map map);

    List getMhExamResult(@Param("resultid") Long resultid);

    List getMhExamREsultByMhCode(@Param("sjssbm") String sjssbm);

    List getStuErForSchool(Map map);

    List getStuErForCounty(Map map);

    List getStuErForCity(Map map);

    List getStuErForCitySubSchool(Map map);

    List getTeaErForSchool(Map map);

    List getTeaErForCounty(Map map);

    List getTeaErForCity(Map map);

    List getTeaErForCitySubSchool(Map map);

    // List<Role> getRoleListForSchool(Map map);
    // List<Role> getRoleListForCounty(Map map);
    // List<Role> getRoleListForCity(Map map);
    // List<Role> getRoleListForSubSchool(Map map);

    List getExamResultListForStu(@Param("userid") Long userid, @Param("starttime") String starttime,
            @Param("endtime") String endtime);

    List getExamResultListForTeacher(@Param("userid") Long userid, @Param("starttime") String starttime,
            @Param("endtime") String endtime);

    // 获取得分，创建常模
    List<ExamResultForNorm> getExamResultForNorm(Map map);

    // List<ExamResultForNorm> getTeacherExamResultForNorm(Map map);
    List<ExamResultForNorm> getExamMhResultForNorm(Map map);

    PropNorm getNorm(Map map);

    DimWarning getWarning(Map map);

    DimWarningAndScoreGrade getWarningAndScoreGrade(Map map);

    DimScoreGrade getScoreGrade(Map map);

    // 根据scaleid获取维度分和选项分集合
    List<DimWarningAndGradeStore> getDimWarningAndGradeStore(Map map);

    DimDescription getDimDescription(Map map);

    DimDescription getMBTIDescription(Map map);

    void deleteExamByResultID(ExamResult er);

    void deleteExamResult(ExamResult er);

    int deleteExamresultMh(@Param("id") Long id);

    int deleteExamresultMhBatch(String[] ids);

    void deleteExamResultById(Long id);

    void deleteByPrimaryKey(Long id);

    void deleteTeaExamResultById(Long id);

    void deleteTeaExamDimResultById(Long id);

    void deleteExamDimResultById(Long id);

    void deleteExamMhDimById(Long id);

    void deleteExamResultByUserId(Map map);

    void deleteExamDoByUserId(Map map);

    void deleteExamDimByUserId(Map map);

    void deleteExamMhDoByUserId(Map map);

    void deleteExamMhDimByUserId(Map map);

    void insertExamResult(ExamResult er);

    void insertExamResultBatch(List erlist);

    void insertExamMHResult(ExamResult er);

    void insertExamDimResultMh(ExamResult er);

    void insertExamDimResult(ExamResult er);

    void insert(ExamResult record);

    void insertSelective(ExamResult record);

    void updateByPrimaryKeySelective(ExamResult record);

    void updateByPrimaryKey(ExamResult record);

    void updateTeacherExamDoOk(Map map);

    void updateParentExamDoOk(Map map);

    void updateExamDoOk(Map map);

    void updateResultId(ExamResult record);

    void updateExamResult(Map map);

    void updateDimScoreAndWarning(Map map);

    void updateDimScoreExamResult(ExamResult er);

    void updateExamDimResult(ExamResult er);

    List<Map> queryStuGroupStaticForSchool(Map param);// 团体统计的样本统计

    List<Map> queryStuGroupStaticForCounty(Map param);// 团体统计的样本统计

    List<Map> queryStuGroupStaticForCity(Map param);// 团体统计的样本统计

    List<Map> queryStuGroupStaticForCitySubSchool(Map param);// 团体统计的样本统计

    List<Map> queryTeaGroupStaticForSchool(Map param);// 团体统计的样本统计

    List<Map> queryTeaGroupStaticForCounty(Map param);// 团体统计的样本统计

    List<Map> queryTeaGroupStaticForCity(Map param);// 团体统计的样本统计

    List<Map> queryTeaGroupStaticForCitySubSchool(Map param);// 团体统计的样本统计

}