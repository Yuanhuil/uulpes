package com.njpes.www.service.scaletoollib;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskEducommission;
import com.njpes.www.entity.scaletoollib.ScaleProcessStruct;
import com.njpes.www.utils.PageParameter;

/**
 * @author huangc
 */
public interface EducommissionDispatcherService {

    public List<ExamdoTaskEducommission> queryETE(HttpServletRequest request, int orglevel, int typeflag,
            PageParameter page, Organization org);

    public List<ExamdoTaskEducommission> searchEduAdminSearchStuAndTea(Organization org, HttpServletRequest request,
            PageParameter page, DispatcherFilterParam dispatcherFilterParam, int typeflag);

    public boolean checkProcessingStatus(long eteducommissionid, long ownerid);

    public void deleteEteducommissionid(int eteducommissionid);

    // public List<ScaleProcessStruct> getCheckProcessStatus(HttpServletRequest
    // request, int eteducommissionid,int taskfrom);
    public List<ScaleProcessStruct> getStudentProcessStatusForCity(HttpServletRequest request, int eteducommissionid);

    public List<ScaleProcessStruct> getStudentProcessStatusForCounty(HttpServletRequest request, int eteducommissionid);

    public List<ScaleProcessStruct> getTeacherProcessStatusForCity(HttpServletRequest request, int eteducommissionid);

    public List<ScaleProcessStruct> getTeacherProcessStatusForCounty(HttpServletRequest request, int eteducommissionid);

    public ExamdoTaskEducommission queryETEAccordingId(int eteducommissionid);

    public List<ExamdoTaskEducommission> requireStatus(List<ExamdoTaskEducommission> eteList, Organization org,
            long ownerid);

    public List<ExamdoTaskEducommission> requireScaleName(List<ExamdoTaskEducommission> eteList);

    public List<ExamdoTaskEducommission> processScaleStatus(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam);

    // 下面用来进行任务名称的过滤
    public List<ExamdoTaskEducommission> filterByTaskName(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskEducommission> filterByGrade(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam);

    // 把年级名称对应上
    public List<ExamdoTaskEducommission> addGradeTitle(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam);

    // 把区域对应上
    public List<ExamdoTaskEducommission> addAreaNameTitle(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam);

    public List<ExamdoTaskEducommission> addSchoolTitle(List<ExamdoTaskEducommission> eteList, long orgid);

    public void deleteEtuId(int etuId);

    public List<ExamdoTaskEducommission> filterTypeRecord(List<ExamdoTaskEducommission> eteList, int flag);

    public List<ExamdoTaskEducommission> filterByRoleid(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam);
}
