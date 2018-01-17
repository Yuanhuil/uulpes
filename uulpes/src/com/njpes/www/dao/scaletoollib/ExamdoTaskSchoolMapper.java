package com.njpes.www.dao.scaletoollib;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskSchool;
import com.njpes.www.entity.scaletoollib.ExamdoTaskSchoolForXiafa;
import com.njpes.www.entity.scaletoollib.ScaleProcessStruct;
import com.njpes.www.utils.PageParameter;

public interface ExamdoTaskSchoolMapper {

    public List<ExamdoTaskSchool> queryETSchoolByPage(@Param("userid") int userid, @Param("typeflag") int typeflag,
            @Param("page") PageParameter page, @Param("orgid") long orgid);

    public List<ExamdoTaskSchool> schoolAdminsearchSDispatchedByPage(@Param("userid") long userid,
            @Param("typeflag") int typeflag, @Param("page") PageParameter page,
            @Param("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskSchool> queryETSAccordingCondition(DispatcherFilterParam dispatcherFilterParam);

    public ExamdoTaskSchoolForXiafa getXiafaExamdoTask(@Param("orgid") long orgid, @Param("id") long id);

    public int updateDispenseStatus(@Param("orgid") long orgid, @Param("taskid") long taskid);

    public Date checkStudentProcessingStatus(int etschoolid);

    public void deleteTaskSchoolById(int etschoolid);

    public void deleteResultSchoolStudentId(int etschoolid);

    public void deleteResultSchoolTeacherId(int etschoolid);

    public ExamdoTaskSchool queryETSAccordingId(int etschoolid);

    public int queryETSchoolAccordProviceYes(ScaleProcessStruct sps);

    public int queryETSchoolAccordProviceNo(ScaleProcessStruct sps);

    public int queryETSchoolAccordCityYes(ScaleProcessStruct sps);

    public int queryETSchoolAccordCityNo(ScaleProcessStruct sps);

    public int queryETSchoolAccordTownYes(ScaleProcessStruct sps);

    public int queryETSchoolAccordTownNo(ScaleProcessStruct sps);

    public Map queryETStudentAccordSchoolYes(ScaleProcessStruct sps);

    public Map queryETStudentAccordSchoolNo(ScaleProcessStruct sps);

    public int queryETTeacherAccordSchoolYes(ScaleProcessStruct sps);

    public int queryETTeacherAccordSchoolNo(ScaleProcessStruct sps);

    public List<Integer> queryOrgidFromETES(@Param("orgId") long orgId);

    public List<Integer> queryBjArrayFromExamdoStudent(@Param("orgid") long orgid, @Param("taskid") long taskid,
            @Param("nj") long nj);

    public List getStudentNoTestDetail(@Param("orgid") long orgid, @Param("taskid") long taskid, @Param("bj") int bj);

    public List getTeacherNoTestDetail(@Param("orgid") long orgid, @Param("taskid") long taskid,
            @Param("roleid") int roleid);

    public void delayEndTime(@Param("taskid")int taskid, @Param("endtime") Date endtime);
}
