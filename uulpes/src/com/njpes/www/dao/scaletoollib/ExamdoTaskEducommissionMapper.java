package com.njpes.www.dao.scaletoollib;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskEducommission;
import com.njpes.www.entity.scaletoollib.ScaleProcessStruct;
import com.njpes.www.utils.PageParameter;

public interface ExamdoTaskEducommissionMapper {
    public List<ExamdoTaskEducommission> queryETEducommissionByPage(@Param("org") Organization org,
            @Param("orglevel") int orglevel, @Param("typeflag") int typeflag, @Param("page") PageParameter page);

    public List<ExamdoTaskEducommission> searchEduAdminSearchStuAndTeaByPage(@Param("org") Organization org,
            @Param("typeflag") int typeflag, @Param("page") PageParameter page,
            @Param("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam);

    public Date checkProcessingStatus(@Param("taskid") long eteducommissionid, @Param("ownerid") long owerid);

    public void deleteEteducommissionid(int eteducommissionid);

    public void deleteMiddleOfEducommissionid(int eteducommissionid);

    public ExamdoTaskEducommission queryETEAccordingId(int eteducommissionid);

    public int queryStuETEduAccordProviceYes(ScaleProcessStruct sps);

    public int queryStuETEduAccordProviceNo(ScaleProcessStruct sps);

    public int queryStuETEduAccordCityYes(ScaleProcessStruct sps);

    public int queryStuETEduAccordCityNo(ScaleProcessStruct sps);

    public int queryStuETEduAccordCountyYes(ScaleProcessStruct sps);

    public int queryStuETEduAccordCountyNo(ScaleProcessStruct sps);

    public int queryStuETEduAccordSchoolYes(ScaleProcessStruct sps);

    public int queryStuETEduAccordSchoolNo(ScaleProcessStruct sps);

    public int queryTchETEduAccordProviceYes(ScaleProcessStruct sps);

    public int queryTchETEduAccordProviceNo(ScaleProcessStruct sps);

    public int queryTchETEduAccordCityYes(ScaleProcessStruct sps);

    public int queryTchETEduAccordCityNo(ScaleProcessStruct sps);

    public int queryTchETEduAccordCountyYes(ScaleProcessStruct sps);

    public int queryTchETEduAccordCountyNo(ScaleProcessStruct sps);

    public int queryTchETEduAccordSchoolYes(ScaleProcessStruct sps);

    public int queryTchETEduAccordSchoolNo(ScaleProcessStruct sps);

    public List<Integer> queryOrgidFromETES(@Param("orgId") long orgId);

    public int xiafaToEdu(@Param("orgid") long orgid, @Param("taskid") long taskid);
}
