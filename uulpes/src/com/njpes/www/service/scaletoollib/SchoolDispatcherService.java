package com.njpes.www.service.scaletoollib;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskSchool;
import com.njpes.www.entity.scaletoollib.ScaleProcessStruct;
import com.njpes.www.utils.PageParameter;

public interface SchoolDispatcherService {

    public List<ExamdoTaskSchool> queryETS(HttpServletRequest request, int typeflag, PageParameter page);

    public List<ExamdoTaskSchool> schoolAdminsearchSDispatched(HttpServletRequest request, int typeflag,
            PageParameter page, DispatcherFilterParam dispatcherFilterParam);

    public boolean checkStudentProcessingStatus(int etschoolid);

    public void deleteEtschoolid(int typeflag, int etschoolid);

    public List<ScaleProcessStruct> getStudentCheckProcessStatusForSchool(HttpServletRequest request, Organization org,
            int etschoolid, int taskfrom);

    public List<ScaleProcessStruct> getTeacherCheckProcessStatusForSchool(HttpServletRequest request, Organization org,
            int etschoolid, int taskfrom);

    public ExamdoTaskSchool queryETSAccordingId(int etschoolid);

    public List<ExamdoTaskSchool> queryETSAccordingCondition(HttpServletRequest request,
            DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskSchool> requireStatus(List<ExamdoTaskSchool> etsList, Organization org);

    public List<ExamdoTaskSchool> requireScaleName(List<ExamdoTaskSchool> etsList);

    public List<ExamdoTaskSchool> processScaleStatus(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskSchool> filterByTaskName(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskSchool> filterByGrade(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskSchool> filterTypeRecord(List<ExamdoTaskSchool> etsList, int flag);

    public List<ExamdoTaskSchool> filterByRoleid(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam);

    public List getStudentNoTestDetail(long orgid, int taskid, int bj, String scaleid);

    public List getTeacherNoTestDetail(long orgid, int taskid, int roleid, String scaleid);

    public void delayEndTime(int taskid);
}
