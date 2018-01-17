package com.njpes.www.action.scaletoollib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.GradeCode;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskEducommission;
import com.njpes.www.entity.scaletoollib.ExamdoTaskSchool;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.GradeCodeServiceImpl;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.EducommissionDispatcherService;
import com.njpes.www.service.scaletoollib.ExamdistributeService;
import com.njpes.www.service.scaletoollib.SchoolDispatcherService;
import com.njpes.www.utils.PageParameter;

/**
 * @author huangc 下面部分函数可以封装成一个接口
 */
@Controller
@RequestMapping("/scaletoollib/monitorprocess")
public class MonitorDispatcherController extends BaseController {

    @Autowired
    private SchoolServiceI schoolService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    DistrictService districtService;

    @Autowired
    GradeCodeServiceImpl gradeCodeService;

    @Autowired
    private EducommissionDispatcherService educommissionDispatcherService;
    @Autowired
    private SchoolDispatcherService schoolDispatcherService;

    @Autowired
    private ExamdistributeService examdistributeService;

    @RequestMapping(value = "/listdispatchrecord")
    public String listDispatcherRecord(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        DispatcherFilterParam dfp = new DispatcherFilterParam();
        request.setAttribute("dispatcherFilterParam", dfp);
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("dispatchtab");
    }

    @RequestMapping(value = "/taskmanageOfTeacher")
    public String taskmanageOfTeacher(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,@ModelAttribute("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long ownerid = account.getId();
        // 开始处理老师
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        int orgLevel = org.getOrgLevel();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        request.setAttribute("orgLevel", org.getOrgLevel());
        long orgid = org.getId();
        // 如果是学校
        if (orgType.equals("2")) {
            // 获得这个学校的老师的角色
            List<Role> roleList = roleService.getTeacherRoles(orgid);
            request.setAttribute("roleList", roleList);
            // 获取该学校管理员对老师分发的记录
            List<ExamdoTaskSchool> etsList = schoolDispatcherService.queryETS(request, 2, page);
            // 添加针对于老师的过滤
            // etsList = schoolDispatcherService.filterTypeRecord(etsList, 1);
            // 下面开始给任务添加任务的状态，包括下发，撤回
            etsList = schoolDispatcherService.requireStatus(etsList, org);
            // 下面开始根据量表的id查找量表的名称
            etsList = schoolDispatcherService.requireScaleName(etsList);
            request.setAttribute("etsList", etsList);

        } else if (orgType.equals("1")) { // 如果是教委用户，3表示的是陶老师
            List<ExamdoTaskEducommission> eteList = educommissionDispatcherService.searchEduAdminSearchStuAndTea(org,
                    request, page, dispatcherFilterParam, 2);
            // eteList =
            // educommissionDispatcherService.filterTypeRecord(eteList,1);
            eteList = educommissionDispatcherService.requireStatus(eteList, org, ownerid);
            eteList = educommissionDispatcherService.requireScaleName(eteList);
            // 下面开始根据年级进行过滤
            // 下面开始根据根据区域进行过滤
            if (orgLevel == 3)// 市教委分发的区县，县教委没有区县，而有学校
                eteList = educommissionDispatcherService.addAreaNameTitle(eteList, dispatcherFilterParam);
            if (orgLevel == 4)// 市教委分发的区县，县教委没有区县，而有学校
                eteList = educommissionDispatcherService.addSchoolTitle(eteList, org.getId());
            request.setAttribute("eteList", eteList);

            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = org.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = org.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("teachersdispatched");
    }

    @RequestMapping(value = "/taskmanageOfStudent")
    public String taskmanageOfStudent(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,@ModelAttribute("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long ownerid = account.getId();
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        request.setAttribute("orgLevel", org.getOrgLevel().intValue());
        int orgLevel = org.getOrgLevel();
        // 如果是学校
        if (orgType.equals("2")) {
            long orgCode = org.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(orgCode);
            request.setAttribute("gradeList", gradeList);
            List<ExamdoTaskSchool> etsList = schoolDispatcherService.queryETS(request, 1, page);
            // 添加针对于学生的过滤
            // etsList = schoolDispatcherService.filterTypeRecord(etsList, 0);
            // 下面开始给任务添加任务的状态，包括下发，撤回
            etsList = schoolDispatcherService.requireStatus(etsList, org);
            // 下面开始根据量表的id查找量表的名称
            etsList = schoolDispatcherService.requireScaleName(etsList);
            request.setAttribute("etsList", etsList);
        } else if (orgType.equals("1") || orgType.equals("3")) { // 如果是教委用户，3表示的是陶老师
            List<ExamdoTaskEducommission> eteList = educommissionDispatcherService.searchEduAdminSearchStuAndTea(org,
                    request, page, dispatcherFilterParam, 1);
            eteList = educommissionDispatcherService.requireStatus(eteList, org, ownerid);
            eteList = educommissionDispatcherService.requireScaleName(eteList);
            // 下面开始根据年级进行过滤
            eteList = educommissionDispatcherService.addGradeTitle(eteList, dispatcherFilterParam);
            if (orgLevel == 3)// 市教委分发的区县，县教委没有区县，而有学校
                // 下面开始根据根据区域进行过滤
                eteList = educommissionDispatcherService.addAreaNameTitle(eteList, dispatcherFilterParam);
            if (orgLevel == 4)// 市教委分发的区县，县教委没有区县，而有学校
                eteList = educommissionDispatcherService.addSchoolTitle(eteList, org.getId());
            request.setAttribute("eteList", eteList);

            List<GradeCode> gradeAllList = gradeCodeService.getAllGrades();
            request.setAttribute("gradeAllList", gradeAllList);
            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = org.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = org.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("studentsdispatched");
    }

    @RequestMapping(value = "/queryClassAccoridngXd", method = RequestMethod.POST)
    @ResponseBody
    public String queryClassAccoridngXd(@CurrentOrg Organization org, HttpServletRequest requst,
            @RequestParam("xueduan") int xueduan) {
        ArrayList<String> allClassList = new ArrayList<String>();
        long orgid = org.getId();
        List<Grade> gradeList = schoolService.getGradesInSchool(orgid, XueDuan.valueOf(xueduan));
        for (int i = 0; i < gradeList.size(); i++) {
            Grade grade = gradeList.get(i);
            String nj = grade.getNj();
            List<ClassSchool> classList = schoolService.getClassByGradeInSchool(orgid, XueDuan.valueOf(xueduan), nj, 0);
            for (int j = 0; j < classList.size(); j++) {
                ClassSchool cs = classList.get(j);
                String bjmc = cs.getBjmc();
                String bjstr = cs.getBh() + "@" + bjmc;
                allClassList.add(bjstr);
            }
        }
        Gson gson = new Gson();
        String liststr = gson.toJson(allClassList);
        return liststr;
    }

    // 学校管理员搜索学生
    @RequestMapping(value = "/schoolAdminsearchSDispatched", method = RequestMethod.POST)
    public String searchSDispatched(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam) {
        dispatcherFilterParam.setOrgid(org.getId());
        // 不管是教委用户还是学校用户，最终检索条件的过滤全部放到数据库中去
        List<ExamdoTaskSchool> etsList = schoolDispatcherService.schoolAdminsearchSDispatched(request, 1, page,
                dispatcherFilterParam);
        // 由于在sql中无法更好地处理分发事件状态,放到内存中执行
        etsList = schoolDispatcherService.processScaleStatus(etsList, dispatcherFilterParam);
        // 下面开始给任务添加任务的状态，包括下发，撤回
        etsList = schoolDispatcherService.requireStatus(etsList, org);
        // 下面开始根据量表的id查找量表的名称
        etsList = schoolDispatcherService.requireScaleName(etsList);
        // 下面是根据任务名进行过滤
        etsList = schoolDispatcherService.filterByTaskName(etsList, dispatcherFilterParam);
        request.setAttribute("etsList", etsList);
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        request.setAttribute("orgLevel", org.getOrgLevel());
        // 如果是学校
        if (orgType.equals("2")) {
            long orgCode = org.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(orgCode);
            request.setAttribute("gradeList", gradeList);
        } else if (orgType.equals("1")) { // 如果是教委用户，3表示的是陶老师
            int orgLevel = org.getOrgLevel();
            List<GradeCode> gradeAllList = gradeCodeService.getAllGrades();
            request.setAttribute("gradeAllList", gradeAllList);
            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = org.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = org.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }
        request.setAttribute("page", page);
        return viewName("studentsdispatched");
    }

    // 教委搜索学生
    @RequestMapping(value = "/eduAdminsearchSDispatched", method = RequestMethod.POST)
    public String searchTDispatched(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long ownerid = account.getId();
        // 最终检索条件的过滤全部放到数据库中去
        List<ExamdoTaskEducommission> eteList = educommissionDispatcherService.searchEduAdminSearchStuAndTea(org,
                request, page, dispatcherFilterParam, 1);
        eteList = educommissionDispatcherService.filterTypeRecord(eteList, 0);
        // 由于在sql中无法更好地处理分发事件状态,放到内存中执行
        eteList = educommissionDispatcherService.processScaleStatus(eteList, dispatcherFilterParam);
        // 下面开始给任务添加任务的状态，包括下发，撤回
        eteList = educommissionDispatcherService.requireStatus(eteList, org, ownerid);
        // 下面开始根据量表的id查找量表的名称
        eteList = educommissionDispatcherService.requireScaleName(eteList);
        eteList = educommissionDispatcherService.filterByTaskName(eteList, dispatcherFilterParam);
        // 下面开始根据年级进行过滤
        eteList = educommissionDispatcherService.addGradeTitle(eteList, dispatcherFilterParam);
        // 下面开始根据根据区域进行过滤
        eteList = educommissionDispatcherService.addSchoolTitle(eteList, org.getId());
        request.setAttribute("eteList", eteList);
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        request.setAttribute("orgLevel", org.getOrgLevel());
        // 如果是学校
        if (orgType.equals("2")) {
            long orgCode = org.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(orgCode);
            request.setAttribute("gradeList", gradeList);
        } else if (orgType.equals("1")) { // 如果是教委用户，3表示的是陶老师
            int orgLevel = org.getOrgLevel();
            List<GradeCode> gradeAllList = gradeCodeService.getAllGrades();
            request.setAttribute("gradeAllList", gradeAllList);
            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = org.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = org.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }
        request.setAttribute("page", page);
        return viewName("studentsdispatched");
    }

    // 学校管理员搜索老师
    @RequestMapping(value = "/schoolAdminsearchTDispatched", method = RequestMethod.POST)
    public String schoolAdminsearchTDispatched(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam) {
        // 不管是教委用户还是学校用户，最终检索条件的过滤全部放到数据库中去
        dispatcherFilterParam.setOrgid(org.getId());
        List<ExamdoTaskSchool> etsList = schoolDispatcherService.schoolAdminsearchSDispatched(request, 2, page,
                dispatcherFilterParam);
        etsList = schoolDispatcherService.filterTypeRecord(etsList, 1);
        etsList = schoolDispatcherService.filterByRoleid(etsList, dispatcherFilterParam);
        // 由于在sql中无法更好地处理分发事件状态,放到内存中执行
        etsList = schoolDispatcherService.processScaleStatus(etsList, dispatcherFilterParam);
        // 下面开始给任务添加任务的状态，包括下发，撤回
        etsList = schoolDispatcherService.requireStatus(etsList, org);
        // 下面开始根据量表的id查找量表的名称
        etsList = schoolDispatcherService.requireScaleName(etsList);
        request.setAttribute("etsList", etsList);
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        request.setAttribute("orgLevel", org.getOrgLevel());
        long orgid = org.getId();
        // 如果是学校
        if (orgType.equals("2")) {
            // 获得这个学校的老师的角色
            List<Role> roleList = roleService.getTeacherRoles(orgid);
            request.setAttribute("roleList", roleList);
        } else if (orgType.equals("1")) { // 如果是教委用户，3表示的是陶老师
            List<Role> roleList = roleService.getEduTeacherRoles(orgid);
            request.setAttribute("roleList", roleList);
            // 如果是市教委
            int orgLevel = org.getOrgLevel();
            if (orgLevel == 3) {
                String cityId = org.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = org.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }
        request.setAttribute("page", page);
        return viewName("teachersdispatched");
    }

    // 教委搜索老师
    @RequestMapping(value = "/eduAdminsearchTDispatched", method = RequestMethod.POST)
    public String eduAdminsearchTDispatched(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dispatcherFilterParam") DispatcherFilterParam dispatcherFilterParam) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long ownerid = account.getId();
        List<ExamdoTaskEducommission> eteList = educommissionDispatcherService.searchEduAdminSearchStuAndTea(org,
                request, page, dispatcherFilterParam, 2);
        // 过滤出分发给老师的
        eteList = educommissionDispatcherService.filterTypeRecord(eteList, 1);
        // 如果有角色，根据角色过滤
        eteList = educommissionDispatcherService.filterByRoleid(eteList, dispatcherFilterParam);
        // 由于在sql中无法更好地处理分发事件状态,放到内存中执行
        eteList = educommissionDispatcherService.processScaleStatus(eteList, dispatcherFilterParam);
        // 下面开始给任务添加任务的状态，包括下发，撤回
        eteList = educommissionDispatcherService.requireStatus(eteList, org, ownerid);
        // 下面开始根据量表的id查找量表的名称
        eteList = educommissionDispatcherService.requireScaleName(eteList);
        eteList = educommissionDispatcherService.filterByTaskName(eteList, dispatcherFilterParam);
        // 下面开始根据年级进行过滤
        eteList = educommissionDispatcherService.addGradeTitle(eteList, dispatcherFilterParam);
        // 下面开始根据根据区域进行过滤
        eteList = educommissionDispatcherService.addSchoolTitle(eteList, org.getId());
        request.setAttribute("eteList", eteList);
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        request.setAttribute("orgLevel", org.getOrgLevel());
        long orgid = org.getId();
        // 如果是学校
        if (orgType.equals("2")) {
            // 获得这个学校的老师的角色
            List<Role> roleList = roleService.getTeacherRoles(orgid);
            request.setAttribute("roleList", roleList);
        } else if (orgType.equals("1")) { // 如果是教委用户，3表示的是陶老师
            List<Role> roleList = roleService.getEduTeacherRoles(orgid);
            request.setAttribute("roleList", roleList);
            // 如果是市教委
            int orgLevel = org.getOrgLevel();
            if (orgLevel == 3) {
                String cityId = org.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = org.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }

        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("teachersdispatched");
    }

    @RequestMapping(value = "/lookStudentProcessingStatusForSchool", method = RequestMethod.GET)
    public String lookStudentProcessingStatusForSchool(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etschoolid") int etschoolid, @RequestParam("taskfrom") int taskfrom) {
        // 首先根据当前用户，得到当前用户的角色级别
        List result = this.schoolDispatcherService.getStudentCheckProcessStatusForSchool(request, org, etschoolid,
                taskfrom);
        request.setAttribute("resultList", result);
        return viewName("studentprocessingstatus");
    }

    @RequestMapping(value = "/lookTeacherProcessingStatusForSchool", method = RequestMethod.GET)
    public String lookTeacherProcessingStatusForSchool(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etschoolid") int etschoolid, @RequestParam("taskfrom") int taskfrom) {
        // 首先根据当前用户，得到当前用户的角色级别
        List result = this.schoolDispatcherService.getTeacherCheckProcessStatusForSchool(request, org, etschoolid,
                taskfrom);
        request.setAttribute("resultList", result);
        return viewName("teacherprocessingstatus");
    }

    @RequestMapping(value = "/lookStudentProcessingStatusForCounty", method = RequestMethod.GET)
    public String lookStudentProcessingStatusForCounty(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etschoolid") int eteducommissionid) {
        // 首先根据当前用户，得到当前用户的角色级别
        List result = this.educommissionDispatcherService.getStudentProcessStatusForCounty(request, eteducommissionid);
        request.setAttribute("resultList", result);
        return viewName("studentprocessingstatus");
    }

    @RequestMapping(value = "/lookTeacherProcessingStatusForCounty", method = RequestMethod.GET)
    public String lookTeacherProcessingStatusForCounty(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etschoolid") int eteducommissionid) {
        // 首先根据当前用户，得到当前用户的角色级别
        List result = this.educommissionDispatcherService.getTeacherProcessStatusForCounty(request, eteducommissionid);
        request.setAttribute("resultList", result);
        return viewName("teacherprocessingstatus");
    }

    @RequestMapping(value = "/lookStudentProcessingStatusForCity", method = RequestMethod.GET)
    public String lookStudentProcessingStatusForCity(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etschoolid") int eteducommissionid) {
        // 首先根据当前用户，得到当前用户的角色级别
        List result = this.educommissionDispatcherService.getStudentProcessStatusForCity(request, eteducommissionid);
        request.setAttribute("resultList", result);
        return viewName("studentprocessingstatus");
    }

    @RequestMapping(value = "/lookTeacherProcessingStatusForCity", method = RequestMethod.GET)
    public String lookTeacherProcessingStatusForCity(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etschoolid") int eteducommissionid) {
        // 首先根据当前用户，得到当前用户的角色级别
        List result = this.educommissionDispatcherService.getTeacherProcessStatusForCity(request, eteducommissionid);
        request.setAttribute("resultList", result);
        return viewName("studentprocessingstatus");
    }

    @RequestMapping(value = "/deleteEtschoolid", method = RequestMethod.POST)
    @ResponseBody
    public String deleteEtschoolid(HttpServletRequest requst, @RequestParam("typeflag") int typeflag,
            @RequestParam("etschoolid") int etschoolid) {
        String result = "success";
        try {
            this.schoolDispatcherService.deleteEtschoolid(typeflag, etschoolid);
        } catch (Exception e) {
            result = "fail";
        }
        return result;
    }

    @RequestMapping(value = "/deleteEudId", method = RequestMethod.POST)
    @ResponseBody
    public String deleteEduid(HttpServletRequest requst, @RequestParam("typeflag") int typeflag,
            @RequestParam("etuid") int etuid) {
        String result = "success";
        try {
            this.educommissionDispatcherService.deleteEtuId(etuid);
        } catch (Exception e) {
            result = "fail";
        }
        return result;
    }

    /*
     * @RequestMapping(value = "/lookTeacherProcessingStatus", method =
     * RequestMethod.GET) public String
     * lookTeacherProcessingStatus(HttpServletRequest request,
     * 
     * @RequestParam("eteducommissionid") int eteducommissionid) { List result =
     * this.educommissionDispatcherService .getCheckProcessStatus(request,
     * eteducommissionid); request.setAttribute("resultList", result); return
     * viewName("teacherprocessingstatus"); }
     */

    // 查看进程是否可以撤销的接口
    @RequestMapping(value = "/checkProcessingStatus", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkProcessingStatus(HttpServletRequest request, @RequestParam("etid") int etid,
            @RequestParam("stype") String stype) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long ownerid = account.getId();
        boolean res = false;
        // 如果是学校给老师或者学生发
        if (stype.equals("sch")) {
            res = this.schoolDispatcherService.checkStudentProcessingStatus(etid);
        } else {
            // 如果教委给老师或者学生发
            res = this.educommissionDispatcherService.checkProcessingStatus(etid, ownerid);
        }
        return res;
    }

    // 转发分发任务接口
    @RequestMapping(value = "/xiafaToEdu", method = RequestMethod.POST)
    @ResponseBody
    public boolean xiafaToEdu(@CurrentOrg Organization org, HttpServletRequest request, @RequestParam("etid") int etid,
            @RequestParam("objecttype") String objecttype) {
        boolean res = false;
        int resultid = examdistributeService.xiafaToEdu(org.getId(), etid);
        if (resultid == 0)
            return false;
        else
            return true;
    }

    // 转发分发任务接口
    @RequestMapping(value = "/xiafaToSchool", method = RequestMethod.GET)
    public String xiafaToSchool(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("etid") int etid, @RequestParam("objecttype") String objecttype, Model model) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        Map<String, Object> resultMsgMap = new HashMap<String, Object>();
        List errorMsgList = new ArrayList();
        Map<String, Object> statisMap = new HashMap<String, Object>();
        resultMsgMap.put("errorMsgList", errorMsgList);
        resultMsgMap.put("statisMap", statisMap);
        boolean res = false;
        examdistributeService.xiafaToSchool(org.getId(), etid, objecttype, resultMsgMap);
        model.addAttribute("orgtype", org.getOrgType());
        model.addAttribute("resultmsgMap", resultMsgMap);
        return "assessmentcenter/scaledispense/dispenseclose";
    }

    @RequestMapping(value = "/getStudentNoTestDetail", method = RequestMethod.POST)
    @ResponseBody
    public List getStudentNoTestDetail(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("orgid") long orgid, @RequestParam("bj") int bj, @RequestParam("scaleid") String scaleid,
            @RequestParam("taskId") int taskId, Model model) {
        return schoolDispatcherService.getStudentNoTestDetail(orgid, taskId, bj, scaleid);

    }

    @RequestMapping(value = "/getTeacherNoTestDetail", method = RequestMethod.POST)
    @ResponseBody
    public List getTeacherNoTestDetail(@CurrentOrg Organization org, HttpServletRequest request,
            @RequestParam("orgid") long orgid, @RequestParam("roleid") int roleid,
            @RequestParam("scaleid") String scaleid, @RequestParam("taskId") int taskId, Model model) {
        return schoolDispatcherService.getTeacherNoTestDetail(orgid, taskId, roleid, scaleid);

    }

    @RequestMapping(value = "/delayEndTime", method = RequestMethod.POST)
    @ResponseBody
    public String delayEndTime(HttpServletRequest requst, @RequestParam("etschoolid") int etschoolid) {
        String result = "success";
        try {
            this.schoolDispatcherService.delayEndTime(etschoolid);
        } catch (Exception e) {
            result = "fail";
        }
        return result;
    }

}
