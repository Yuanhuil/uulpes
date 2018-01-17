package com.njpes.www.dao.scaletoollib;

import java.util.List;
import java.util.Map;

import com.njpes.www.entity.scaletoollib.ExamDoEduTask;
import com.njpes.www.entity.scaletoollib.ExamDoSchoolTask;
import com.njpes.www.entity.scaletoollib.ExamDoTaskForEduSchool;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.entity.scaletoollib.MhTask;

public interface ExamTaskMapper {
    public int insertMhTask(MhTask mh);

    public int insertBatchMhTask(List<MhTask> mhTaskList);

    public int insertStudentExamDo(Map<String, Object> param);

    public int insertTeacherExamDo(Map<String, Object> param);

    public int insertExamDoTaskForEdu(ExamDoEduTask examDoEduTask);

    public int insertExamDoTaskForSchool(ExamDoSchoolTask examDoSchoolTask);

    public int insertExamDoTaskForEduSchool(Map<String, Object> param);

    public int insertExamDoTaskForEduSchoolBatch(List<ExamDoTaskForEduSchool> edForSchoolList);

    public List queryLimitedStudentExamdo(Map param);

    public List queryLimitedTeacherExamdo(Map param);

    public int queryStuExamdoCountForSchools(Map param);

    public int queryStuExamdoCountForCounties(Map param);

    public int queryTeaExamdoCountForSchools(Map param);

    public int queryTeaExamdoCountForCounties(Map param);

    public int getLastInsertId();

    public long getStudentMaxId();

    public long getTeacherMaxId();

    public int insertBatchExamdoStudent(List<ExamdoStudent> examdoList);

    public int insertBatchExamdoTeacher(List<ExamdoTeacher> examdoList);
}
