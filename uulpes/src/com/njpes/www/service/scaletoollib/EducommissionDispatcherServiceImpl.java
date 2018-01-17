package com.njpes.www.service.scaletoollib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.scaletoollib.ExamdoTaskEducommissionMapper;
import com.njpes.www.dao.scaletoollib.ExamdoTaskSchoolMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskEducommission;
import com.njpes.www.entity.scaletoollib.ScaleProcessStruct;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.GradeCodeServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;

/**
 * @author huangc
 */
@Service("educommissionDispatcherService")
public class EducommissionDispatcherServiceImpl implements EducommissionDispatcherService {

    @Autowired
    private ExamdoTaskEducommissionMapper educommissionDispatcherMapper;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private GradeCodeServiceI gradeCodeService;
    @Autowired
    DistrictService districtService;
    @Autowired
    private SchoolDispatcherService schoolDispatcherService;
    @Autowired
    OrganizationServiceI organizationService;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private ExamdoTaskSchoolMapper schoolDispatcherMapper;
    @Autowired
    private SchoolMapper schoolMappaer;

    @Override
    public List<ExamdoTaskEducommission> queryETE(HttpServletRequest request, int orglevel, int typeflag,
            PageParameter page, Organization org) {
        // TODO Auto-generated method stub
        List<ExamdoTaskEducommission> eteList = educommissionDispatcherMapper.queryETEducommissionByPage(org, orglevel,
                typeflag, page);
        return eteList;
    }

    @Override
    public boolean checkProcessingStatus(long eteducommissionid, long ownerid) {
        // TODO Auto-generated method stub
        Date starttime = educommissionDispatcherMapper.checkProcessingStatus(eteducommissionid, ownerid);
        if (starttime == null)
            return false;
        Date nowdate = new Date();
        // 如果现在的时间已经在开始时间之后
        if (nowdate.after(starttime))
            return false;
        return true;
    }

    @Override
    public void deleteEteducommissionid(int eteducommissionid) {
        // TODO Auto-generated method stub
        educommissionDispatcherMapper.deleteEteducommissionid(eteducommissionid);
    }

    @Override
    public List<ScaleProcessStruct> getStudentProcessStatusForCounty(HttpServletRequest request,
            int eteducommissionid) {
        // 从session中取出Account
        // ExamdoTaskSchool ets =
        // schoolDispatcherService.queryETSAccordingId(etschoolid);
        ExamdoTaskEducommission ete = queryETEAccordingId(eteducommissionid);
        String[] scaleids = null;
        if (ete.getScaleids().contains(",")) {
            scaleids = ete.getScaleids().split(",");
        } else {
            scaleids = new String[] { ete.getScaleids() };
        }

        String[] njarray = null;
        if (ete.getGradeids().contains(",")) {
            njarray = ete.getGradeids().split(",");
        } else {
            njarray = new String[] { ete.getGradeids() };
        }
        String[] orgarray = null;
        if (ete.getOrgids().contains(",")) {
            orgarray = ete.getOrgids().split(",");
        } else {
            orgarray = new String[] { ete.getOrgids() };
        }
        ArrayList<ScaleProcessStruct> tempList = new ArrayList<ScaleProcessStruct>();
        for (int i = 0; i < scaleids.length; i++) {
            for (int j = 0; j < orgarray.length; j++) {
                for (int k = 0; k < njarray.length; k++) {
                    // for (int m = 0; m < bjarray.length; m++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    sps.setScaleName(sdi.getTitle());
                    sps.setOrgid(Integer.parseInt(orgarray[j]));
                    Organization org = organizationService.selectOrganizationById(Long.parseLong(orgarray[j]));
                    sps.setOrgName(org.getName());
                    sps.setNj(Integer.parseInt(njarray[k]));
                    int gradeid = Integer.parseInt(njarray[k]);
                    String gradeName = AgeUitl.getGradeName(gradeid);
                    sps.setGradeName(gradeName);
                    sps.setTaskId(eteducommissionid);
                    tempList.add(sps);
                    // }
                }
            }
        }

        stuProcessStatusForCounty(tempList);
        request.setAttribute("orglevel", 4);
        return tempList;
    }

    @Override
    public List<ScaleProcessStruct> getTeacherProcessStatusForCounty(HttpServletRequest request,
            int eteducommissionid) {
        // 从session中取出Account
        // ExamdoTaskSchool ets =
        // schoolDispatcherService.queryETSAccordingId(etschoolid);
        ExamdoTaskEducommission ete = queryETEAccordingId(eteducommissionid);
        String[] scaleids = null;
        if (ete.getScaleids().contains(",")) {
            scaleids = ete.getScaleids().split(",");
        } else {
            scaleids = new String[] { ete.getScaleids() };
        }

        String[] roleNameArray = null;
        if (ete.getRolename().contains(",")) {
            roleNameArray = ete.getRolename().split(",");
        } else {
            roleNameArray = new String[] { ete.getGradeids() };
        }
        String[] roleArray = null;
        if (ete.getRoleids().contains(",")) {
            roleArray = ete.getRoleids().split(",");
        } else {
            roleArray = new String[] { ete.getGradeids() };
        }
        String[] orgarray = null;
        if (ete.getOrgids().contains(",")) {
            orgarray = ete.getOrgids().split(",");
        } else {
            orgarray = new String[] { ete.getOrgids() };
        }
        ArrayList<ScaleProcessStruct> tempList = new ArrayList<ScaleProcessStruct>();
        for (int i = 0; i < scaleids.length; i++) {
            for (int j = 0; j < orgarray.length; j++) {
                for (int k = 0; k < roleNameArray.length; k++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    sps.setScaleName(sdi.getTitle());
                    sps.setOrgid(Integer.parseInt(orgarray[j]));
                    Organization org = organizationService.selectOrganizationById(Long.parseLong(orgarray[j]));
                    sps.setOrgName(org.getName());
                    sps.setRoleName(roleNameArray[k]);
                    sps.setRoleid(Integer.parseInt(roleArray[k]));
                    sps.setTaskId(eteducommissionid);
                    tempList.add(sps);
                }
            }
        }

        tchProcessStatusForCounty(tempList);
        request.setAttribute("orglevel", 4);
        return tempList;
    }

    @Override
    public List<ScaleProcessStruct> getStudentProcessStatusForCity(HttpServletRequest request, int eteducommissionid) {
        // 从session中取出Account
        // ExamdoTaskSchool ets =
        // schoolDispatcherService.queryETSAccordingId(etschoolid);
        ExamdoTaskEducommission ete = queryETEAccordingId(eteducommissionid);
        String[] scaleids = null;
        if (ete.getScaleids().contains(",")) {
            scaleids = ete.getScaleids().split(",");
        } else {
            scaleids = new String[] { ete.getScaleids() };
        }

        String[] njarray = null;
        if (ete.getGradeids().contains(",")) {
            njarray = ete.getGradeids().split(",");
        } else {
            njarray = new String[] { ete.getGradeids() };
        }
        String[] areaarray = null;
        String[] areanameArray = null;
        if (!StringUtils.isEmpty(ete.getAreaids())) {
            if (ete.getAreaids().contains(",")) {
                areaarray = ete.getAreaids().split(",");
            } else {
                areaarray = new String[] { ete.getAreaids() };
            }
            areanameArray = new String[areaarray.length];
            for (int i = 0; i < areaarray.length; i++) {
                District district = districtService.selectByCode(areaarray[i]);
                areanameArray[i] = district.getName();
            }
        }
        String[] orgarray = null;
        String[] schoolNameArray = null;

        if (!StringUtils.isEmpty(ete.getOrgids())) {
            if (ete.getOrgids().contains(",")) {
                orgarray = ete.getOrgids().split(",");
            } else {
                orgarray = new String[] { ete.getOrgids() };
            }
            schoolNameArray = new String[orgarray.length];
            for (int i = 0; i < orgarray.length; i++) {
                School school = schoolMappaer.selectSchoolInfoBySchoolOrgId(Long.parseLong(orgarray[i]));
                schoolNameArray[i] = school.getXxmc();
            }
        }
        // if(ete.getAreanames()!=null)
        // areanameArray = ete.getAreanames().split(",");
        ArrayList<ScaleProcessStruct> tempList = new ArrayList<ScaleProcessStruct>();
        if (!StringUtils.isEmpty(ete.getAreaids())) {
            for (int i = 0; i < scaleids.length; i++) {
                for (int j = 0; j < areaarray.length; j++) {
                    for (int k = 0; k < njarray.length; k++) {
                        // for (int m = 0; m < bjarray.length; m++) {
                        ScaleProcessStruct sps = new ScaleProcessStruct();
                        String[] scaleidss = scaleids[i].split("_");
                        String scaleid = scaleidss[0];
                        sps.setScaleid(scaleid);
                        Scale sdi = cachedScaleMgr.get(scaleid);
                        sps.setScaleName(sdi.getTitle());
                        sps.setCountyid(areaarray[j]);
                        sps.setCountyName(areanameArray[j]);
                        sps.setOrgName(areanameArray[j]);
                        int gradeid = Integer.parseInt(njarray[k]);
                        sps.setNj(gradeid);
                        sps.setGradeid(gradeid);
                        String gradeName = AgeUitl.getGradeName(gradeid);
                        sps.setGradeName(gradeName);
                        tempList.add(sps);
                        // }
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(ete.getOrgids())) {
            for (int i = 0; i < scaleids.length; i++) {
                for (int j = 0; j < orgarray.length; j++) {
                    for (int k = 0; k < njarray.length; k++) {
                        // for (int m = 0; m < bjarray.length; m++) {
                        ScaleProcessStruct sps = new ScaleProcessStruct();
                        String[] scaleidss = scaleids[i].split("_");
                        String scaleid = scaleidss[0];
                        sps.setScaleid(scaleid);
                        Scale sdi = cachedScaleMgr.get(scaleid);
                        sps.setScaleName(sdi.getTitle());
                        sps.setOrgid(Long.parseLong(orgarray[j]));
                        sps.setOrgName(schoolNameArray[j]);
                        int gradeid = Integer.parseInt(njarray[k]);
                        sps.setNj(gradeid);
                        sps.setGradeid(gradeid);
                        String gradeName = AgeUitl.getGradeName(gradeid);
                        sps.setGradeName(gradeName);
                        tempList.add(sps);
                        // }
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(ete.getAreaids())) {
            stuProcessStatusForCity(tempList);
        }
        if (!StringUtils.isEmpty(ete.getOrgids())) {
            stuProcessStatusForSchool(tempList);
        }
        request.setAttribute("orglevel", 3);
        return tempList;
    }

    @Override
    public List<ScaleProcessStruct> getTeacherProcessStatusForCity(HttpServletRequest request, int eteducommissionid) {
        // 从session中取出Account
        // ExamdoTaskSchool ets =
        // schoolDispatcherService.queryETSAccordingId(etschoolid);
        ExamdoTaskEducommission ete = queryETEAccordingId(eteducommissionid);
        String[] scaleids = null;
        if (ete.getScaleids().contains(",")) {
            scaleids = ete.getScaleids().split(",");
        } else {
            scaleids = new String[] { ete.getScaleids() };
        }

        // String[] njarray = null;
        // if(ete.getGradeids().contains(",")){
        // njarray=ete.getGradeids().split(",");
        // }else{
        // njarray=new String[]{ete.getGradeids()};
        // }
        String[] areaarray = null;
        String[] areanameArray = null;
        if (!StringUtils.isEmpty(ete.getAreaids())) {
            if (ete.getAreaids().contains(",")) {
                areaarray = ete.getAreaids().split(",");
            } else {
                areaarray = new String[] { ete.getAreaids() };
            }
            areanameArray = new String[areaarray.length];
            for (int i = 0; i < areaarray.length; i++) {
                District district = districtService.selectByCode(areaarray[i]);
                areanameArray[i] = district.getName();
            }
        }
        // String[] areanameArray = null;
        // if(ete.getAreanames()!=null)
        // areanameArray = ete.getAreanames().split(",");
        String[] rolearray = null;
        String[] rolenamearray = null;
        String[] orgarray = null;
        String[] schoolNameArray = null;
        rolearray = ete.getRoleids().split(",");
        rolenamearray = ete.getRolename().split(",");

        if (!StringUtils.isEmpty(ete.getOrgids())) {
            if (ete.getOrgids().contains(",")) {
                orgarray = ete.getOrgids().split(",");
            } else {
                orgarray = new String[] { ete.getOrgids() };
            }
            schoolNameArray = new String[orgarray.length];
            for (int i = 0; i < orgarray.length; i++) {
                School school = schoolMappaer.selectSchoolInfoBySchoolOrgId(Long.parseLong(orgarray[i]));
                schoolNameArray[i] = school.getXxmc();
            }
        }

        ArrayList<ScaleProcessStruct> tempList = new ArrayList<ScaleProcessStruct>();
        if (!StringUtils.isEmpty(ete.getAreaids())) {
            for (int i = 0; i < scaleids.length; i++) {
                for (int j = 0; j < areaarray.length; j++) {
                    // for (int k = 0; k < orgarray.length; k++) {
                    // for (int m = 0; m < bjarray.length; m++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    sps.setScaleName(sdi.getTitle());
                    sps.setCountyid(areaarray[j]);
                    sps.setCountyName(areanameArray[j]);
                    // sps.setOrgName(areanameArray[j]);
                    // int gradeid = Integer.parseInt(orgarray[k]);
                    // sps.setNj(gradeid);
                    // String gradeName = AgeUitl.getGradeName(gradeid);
                    // sps.setGradeName(gradeName);
                    tempList.add(sps);
                    // }
                    // }
                }
            }
        }
        if (!StringUtils.isEmpty(ete.getOrgids())) {
            for (int i = 0; i < scaleids.length; i++) {
                for (int j = 0; j < orgarray.length; j++) {
                    // for (int k = 0; k < orgarray.length; k++) {
                    // for (int m = 0; m < bjarray.length; m++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    sps.setScaleName(sdi.getTitle());
                    sps.setOrgid(Long.parseLong(orgarray[j]));
                    sps.setOrgName(schoolNameArray[j]);
                    tempList.add(sps);
                    // }
                    // }
                }
            }
        }
        if (!StringUtils.isEmpty(ete.getAreaids())) {
            tchProcessStatusForCity(tempList);
        }
        if (!StringUtils.isEmpty(ete.getOrgids())) {
            tchProcessStatusForSchool(tempList);
        }
        request.setAttribute("orglevel", 3);
        return tempList;
    }

    public void stuProcessStatusForProvince(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryStuETEduAccordProviceYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryStuETEduAccordProviceNo(sps);
            int total = dowork + nodowork;
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void tchProcessStatusForProvince(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryTchETEduAccordProviceYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryTchETEduAccordProviceNo(sps);
            int total = dowork + nodowork;
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void stuProcessStatusForCity(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryTchETEduAccordCityYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryTchETEduAccordCityNo(sps);
            int total = dowork + nodowork;
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void tchProcessStatusForCity(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryTchETEduAccordCityYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryTchETEduAccordCityNo(sps);
            int total = dowork + nodowork;
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void stuProcessStatusForCounty(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryStuETEduAccordCountyYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryStuETEduAccordCountyNo(sps);
            int total = dowork + nodowork;
            String percentage = null;
            if(dowork == 0){
                percentage = String.valueOf("0%");
            }else{
                DecimalFormat df = new DecimalFormat("#.##");
                percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            }
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void tchProcessStatusForCounty(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryTchETEduAccordCountyYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryTchETEduAccordCountyNo(sps);
            int total = dowork + nodowork;
            String percentage = null;
            if(dowork == 0){
                percentage = String.valueOf("0%");
            }else{
                DecimalFormat df = new DecimalFormat("#.##");
                percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            }
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void stuProcessStatusForSchool(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryStuETEduAccordSchoolYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryStuETEduAccordSchoolNo(sps);
            int total = dowork + nodowork;
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void tchProcessStatusForSchool(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.educommissionDispatcherMapper.queryTchETEduAccordSchoolYes(sps);
            int nodowork = this.educommissionDispatcherMapper.queryTchETEduAccordSchoolNo(sps);
            int total = dowork + nodowork;
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    @Override
    public ExamdoTaskEducommission queryETEAccordingId(int eteducommissionid) {
        // TODO Auto-generated method stub
        return this.educommissionDispatcherMapper.queryETEAccordingId(eteducommissionid);
    }

    @Override
    public List<ExamdoTaskEducommission> searchEduAdminSearchStuAndTea(Organization org, HttpServletRequest request,
            PageParameter page, DispatcherFilterParam dispatcherFilterParam, int typeflag) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long userid = account.getId();
        List<ExamdoTaskEducommission> eteList = educommissionDispatcherMapper.searchEduAdminSearchStuAndTeaByPage(org,
                typeflag, page, dispatcherFilterParam);
        return eteList;
    }

    @Override
    public List<ExamdoTaskEducommission> requireStatus(List<ExamdoTaskEducommission> etsList, Organization org,
            long ownerid) {
        List<Integer> taskIdList = this.educommissionDispatcherMapper.queryOrgidFromETES(org.getId());
        // 判断是否下发
        Date now = new Date();
        for (int i = 0; i < etsList.size(); i++) {
            ExamdoTaskEducommission ets = etsList.get(i);
            long id = ets.getId();
            if (taskIdList.contains(id) && org.getOrgLevel() != 3) {
                ets.setXfflag(1);
            } else {
                ets.setXfflag(0);
            }
            Date endtime = ets.getEndtime();
            if (endtime.after(now)) {// 如果没有过期
                ets.setExpireflag(0);
            } else
                ets.setExpireflag(1);
            // 判断是否可以撤回
            boolean result = this.checkProcessingStatus(id, ownerid);
            if (result) {
                // 可以撤回
                ets.setChflag(1);
            } else {
                // 不可以撤回
                ets.setChflag(0);
            }
        }
        return etsList;
    }

    @Override
    public List<ExamdoTaskEducommission> requireScaleName(List<ExamdoTaskEducommission> eteList) {
        // TODO Auto-generated method stub
        for (int i = 0; i < eteList.size(); i++) {
            ExamdoTaskEducommission ets = eteList.get(i);
            String scaleids = ets.getScaleids();
            String[] scaleArray = null;
            if (scaleids.contains(",")) {
                scaleArray = scaleids.split(",");
            } else {
                scaleArray = new String[1];
                scaleArray[0] = scaleids;
            }
            String scaleName = "";
            for (int j = 0; j < scaleArray.length; j++) {
                String scaleid = "";
                if (scaleArray[j].contains("_")) {
                    scaleid = scaleArray[j].substring(0, scaleArray[j].indexOf("_"));
                } else {
                    scaleid = scaleArray[j];
                }
                scaleName = scaleName + cachedScaleMgr.get(scaleid).getTitle() + ",";
            }
            scaleName = scaleName.substring(0, scaleName.length() - 1);
            ets.setScalenames(scaleName);
        }
        return eteList;
    }

    @Override
    public List<ExamdoTaskEducommission> processScaleStatus(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam) {
        if (dispatcherFilterParam.getProgressStatus().equals("1")) {
            // 尚未开始
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ets = it.next();
                Date starttime = ets.getStarttime();
                Date now = new Date();
                if (!(starttime.after(now))) {
                    it.remove();
                }
            }
        } else if (dispatcherFilterParam.getProgressStatus().equals("2")) {
            // 正在进行
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ets = it.next();
                Date starttime = ets.getStarttime();
                Date endtime = ets.getEndtime();
                Date now = new Date();
                if (!((now.after(starttime)) && (now.before(endtime)))) {
                    it.remove();
                }
            }
        } else if (dispatcherFilterParam.getProgressStatus().equals("3")) {
            // 已经结束
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ets = it.next();
                Date endtime = ets.getEndtime();
                Date now = new Date();
                if (!(now.after(endtime))) {
                    it.remove();
                }
            }
        }
        return eteList;
    }

    @Override
    public List<ExamdoTaskEducommission> filterByTaskName(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam) {
        // TODO Auto-generated method stub
        if (!dispatcherFilterParam.getTaskKeywords().equals("")) {
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ets = it.next();
                if (!(ets.getTaskname().contains(dispatcherFilterParam.getTaskKeywords()))) {
                    it.remove();
                }
            }
        }
        return eteList;
    }

    @Override
    public List<ExamdoTaskEducommission> filterByGrade(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam) {
        if (dispatcherFilterParam.getSgradeId() != -1) {
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ets = it.next();
                if (!(ets.getGradeids().contains(String.valueOf(dispatcherFilterParam.getSgradeId())))) {
                    it.remove();
                }
            }
        }
        return eteList;
    }

    @Override
    public List<ExamdoTaskEducommission> addGradeTitle(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam) {

        for (int i = 0; i < eteList.size(); i++) {
            String gradeTitles = "";
            ExamdoTaskEducommission ete = eteList.get(i);
            String gradeids = ete.getGradeids();
            if (gradeids != null) {
                String[] gradeArray = gradeids.split(",");
                for (int j = 0; j < gradeArray.length; j++) {
                    int gradeorderid = Integer.parseInt(gradeArray[j]);
                    gradeTitles = gradeTitles + AgeUitl.getGradeName(gradeorderid) + ",";
                }
                gradeTitles = gradeTitles.substring(0, gradeTitles.length() - 1);
            }
            ete.setGradeTitles(gradeTitles);
        }

        return eteList;
    }
    /*
     * @Override public List<ExamdoTaskEducommission> addGradeTitle(
     * List<ExamdoTaskEducommission> eteList, DispatcherFilterParam
     * dispatcherFilterParam) { // 说明有搜索条件 if
     * (dispatcherFilterParam.getSgradeId() != -1) {
     * ListIterator<ExamdoTaskEducommission> it = eteList.listIterator(); while
     * (it.hasNext()) { String gradeTitles = ""; ExamdoTaskEducommission ete =
     * it.next(); String gradeids = ete.getGradeids(); // 如果不包含搜索条件 if (gradeids
     * != null && (!(gradeids.contains(String
     * .valueOf(dispatcherFilterParam.getSgradeId()))))) { it.remove();
     * continue; } else { if (gradeids != null && gradeids.contains(".")) {
     * String[] gradeLists = gradeids.split(","); for (int j = 0; j <
     * gradeLists.length; j++) { int id = Integer.parseInt(gradeLists[j]);
     * gradeTitles = gradeTitles + gradeCodeService.getGradecodeTitile(id)
     * .getTitle() + "<br>"; } } else if(gradeids != null){ gradeTitles =
     * gradeTitles + gradeCodeService.getGradecodeTitile(
     * Integer.parseInt(gradeids)).getTitle(); }
     * ete.setGradeTitles(gradeTitles); } } } else { for (int i = 0; i <
     * eteList.size(); i++) { String gradeTitles = ""; ExamdoTaskEducommission
     * ete = eteList.get(i); String gradeids = ete.getGradeids(); if (gradeids
     * != null && gradeids.contains(".")) { String[] gradeLists =
     * gradeids.split(","); for (int j = 0; j < gradeLists.length; j++) { int id
     * = Integer.parseInt(gradeLists[j]); gradeTitles = gradeTitles +
     * gradeCodeService.getGradecodeTitile(id) .getTitle() + "<br>"; } } else
     * if(gradeids != null){ gradeTitles = gradeTitles +
     * gradeCodeService.getGradecodeTitile(
     * Integer.parseInt(gradeids)).getTitle(); }
     * ete.setGradeTitles(gradeTitles); } } return eteList; }
     */

    @Override
    public List<ExamdoTaskEducommission> addAreaNameTitle(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam) {

        for (int i = 0; i < eteList.size(); i++) {
            String areaTitles = "";
            ExamdoTaskEducommission ete = eteList.get(i);
            String areaids = ete.getAreaids();
            if (!StringUtils.isEmpty(areaids)) {
                // if (areaids != null && areaids.contains(",")) {
                String[] areaLists = areaids.split(",");
                for (int j = 0; j < areaLists.length; j++) {
                    String id = areaLists[j];
                    areaTitles = areaTitles + districtService.selectByCode(id).getName() + "<br>";
                }
            }

            String orgids = ete.getOrgids();
            if (!StringUtils.isEmpty(orgids)) {

                String[] orgArray = orgids.split(",");
                List<String> schoolNames = schoolMappaer.getSchoolnamesByOrgids(orgArray);
                if (schoolNames == null)
                    continue;
                for (int j = 0; j < schoolNames.size(); j++) {
                    areaTitles += schoolNames.get(j) + ",";
                }
                areaTitles = areaTitles.substring(0, areaTitles.length() - 1);

            }
            ete.setAreanames(areaTitles);
        }

        return eteList;
    }

    @Override
    public List<ExamdoTaskEducommission> addSchoolTitle(List<ExamdoTaskEducommission> eteList, long orgid) {
        for (int i = 0; i < eteList.size(); i++) {
            String schoolTitles = "";
            ExamdoTaskEducommission ete = eteList.get(i);
            String orgids = ete.getOrgids();
            if (orgids != null) {
                if (orgids.length() > 0 && ete.getCreaterorgid() == orgid) {
                    String[] orgArray = orgids.split(",");
                    List<String> schoolNames = schoolMappaer.getSchoolnamesByOrgids(orgArray);
                    if (schoolNames == null)
                        continue;
                    for (int j = 0; j < schoolNames.size(); j++) {
                        schoolTitles += schoolNames.get(j) + ",";
                    }
                    schoolTitles = schoolTitles.substring(0, schoolTitles.length() - 1);
                }
            }
            if (ete.getCreaterorgid() != orgid)
                ete.setOrgnames("转发上级分发量表");
            else
                ete.setOrgnames(schoolTitles);
        }

        return eteList;
    }

    @Override
    public void deleteEtuId(int etuId) {
        // TODO Auto-generated method stub
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 删除教委给学生or老师发的task
            educommissionDispatcherMapper.deleteEteducommissionid(etuId);
            // 删除中间表中针对于该etuid的记录
            educommissionDispatcherMapper.deleteMiddleOfEducommissionid(etuId);
            // 删除教委给学生or老师发的examdo
            this.schoolDispatcherMapper.deleteResultSchoolStudentId(etuId);
            txManager.commit(status);
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
        }
    }

    @Override
    public List<ExamdoTaskEducommission> filterTypeRecord(List<ExamdoTaskEducommission> eteList, int flag) {
        // 过滤学生
        if (flag == 0) {
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ete = it.next();
                if (ete.getRoleids() != null) {// modified by 赵万锋
                    // if (ete.getRoleid() != 0) {
                    it.remove();
                }
            }
            return eteList;
        } else {
            // 过滤老师
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ete = it.next();
                // if (ete.getRoleid() == 0) {
                if (ete.getRoleids() != null) {// modified by 赵万锋
                    it.remove();
                }
            }
            return eteList;
        }
    }

    @Override
    public List<ExamdoTaskEducommission> filterByRoleid(List<ExamdoTaskEducommission> eteList,
            DispatcherFilterParam dispatcherFilterParam) {
        // TODO Auto-generated method stub
        if (dispatcherFilterParam.getRoleid() != -1) {
            ListIterator<ExamdoTaskEducommission> it = eteList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskEducommission ete = it.next();
                // if (ete.getRoleid() != dispatcherFilterParam.getRoleid()) {
                String[] roleArray = ete.getRoleids().split(",");
                int flag = 0;
                for (String s : roleArray) {
                    if (s.equals(dispatcherFilterParam.getRoleid())) {
                        flag = 1;
                        break;
                    }
                }
                it.remove();
            }
            return eteList;
        }
        return eteList;
    }
}
