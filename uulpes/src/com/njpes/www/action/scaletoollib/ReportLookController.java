package com.njpes.www.action.scaletoollib;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.entity.scaletoollib.ExamresultStudentWithBackGround;
import com.njpes.www.entity.scaletoollib.ExamresultTeacher;
import com.njpes.www.entity.scaletoollib.ReportLookStudentFilterParam;
import com.njpes.www.entity.scaletoollib.ReportLookTeacherFilterParam;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.baseinfo.ClassServiceI;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.ScaleUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import heracles.web.freemarker.FreemarkerCfg;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/scaletoollib/reportlook")
public class ReportLookController extends BaseController {

    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private ReportLookService reportLookService;
    // @Autowired
    // private ScaleService scaleService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private ExamdoServiceI examdoService;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private ClassServiceI classService;

    @RequestMapping(value = "/studentreportpage", method = RequestMethod.GET)
    public String studentreportpage(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        Account account = (Account) request.getSession().getAttribute("user");
        int typeflag = account.getTypeFlag();
        request.setAttribute("typeflag", typeflag);
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        // 如果是学校用户
        // List<Grade> gradeList =
        // schoolService.getGradeListInSchool(orgEntity.getId());
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        if (account.getTypeFlag().intValue() == 3)
        {
          String idcard = account.getUsername().substring(0, 18);
          reportLookStudentFilterParam.setIdentiyId(String.valueOf(studentService.getStudentBySfzjh(idcard).getId()));
        }
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentPersonalReport = this.reportLookService
                .getStudentResultByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentPersonalReport", elistStudentPersonalReport);
        List<Scale> scaleList = reportLookService.queryDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        request.setAttribute("page", page);
        return viewName("studentreport");
    }

    @RequestMapping(value = "/studentPersonalReport", method = RequestMethod.GET)
    public String studentPersonalReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        Account account = (Account) request.getSession().getAttribute("user");
        request.setAttribute("role", "");
        Set<Role> roleSet = account.getRoles();
        for (Role role : roleSet) {
            if (role.getRoleName().contains("班主任")) {
                request.setAttribute("role", "班主任");
                break;
            }
        }

        int typeflag = account.getTypeFlag();
        request.setAttribute("typeflag", typeflag);
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是教委用户
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        if (account.getTypeFlag().intValue() == 3){
          String idcard = account.getUsername().substring(0, 18);
          reportLookStudentFilterParam.setIdentiyId(String.valueOf(this.studentService.getStudentBySfzjh(idcard).getId()));
        }
        List<ClassSchool> selectByBzrAccountid = this.classService.selectByBzrAccountid(account.getId().longValue());
        if (selectByBzrAccountid.size() > 0){
          long id = ((ClassSchool)selectByBzrAccountid.get(0)).getId().longValue();
          reportLookStudentFilterParam.setBj((int)id);
        }
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentPersonalReport = this.reportLookService
                .getStudentResultByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentPersonalReport", elistStudentPersonalReport);
        List<Scale> scaleList = reportLookService.queryDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        request.setAttribute("page", page);
        return viewName("studentPersonalReport");
    }

    @RequestMapping(value = "/studentComplianceReport", method = RequestMethod.GET)
    public String studentComplianceReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        Account account = (Account)request.getSession().getAttribute("user");
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是学校用户
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        List<ClassSchool> selectByBzrAccountid = classService.selectByBzrAccountid(account.getId().longValue());
        if (selectByBzrAccountid.size() > 0)
        {
          long id = ((ClassSchool)selectByBzrAccountid.get(0)).getId().longValue();
          reportLookStudentFilterParam.setBj((int)id);
        }
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentComplianceReport = this.reportLookService
                .getStuComplianceReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentComplianceReport", elistStudentComplianceReport);
        request.setAttribute("page", page);
        return viewName("studentComplianceReport");
    }

    @RequestMapping(value = "/studentTeamReport", method = RequestMethod.GET)
    public String studentTeamReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        int orgLevel = orgEntity.getOrgLevel();
        request.setAttribute("orgLevel", orgLevel);
        Long orgId = orgEntity.getId();
        // List<ExamresultStudent> njList =
        // reportLookService.queryDistinctNJFromExamresultStudentAccordingOrgId(orgEntity.getId().intValue());
        // request.setAttribute("njList", njList);
        if (orgtype.equals("2")) {// 学校
            List<ExamresultStudent> xdList = reportLookService
                    .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
            request.setAttribute("xdList", xdList);
        }
        // 如果是区县教委用户
        else {
            if (orgLevel == 4) {
                // 获取该组织下面的所有学校
                List<Organization> schoolList = reportLookService
                        .getSchoolsFromExamresultStudent(orgEntity.getCountyid());
                request.setAttribute("schoolList", schoolList);
            }
            if (orgLevel == 3) {
            }
        }
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        /*
         * List<ExamresultStudentWithBackGround> elistStudentComplianceReport =
         * this.reportLookService
         * .getStuTeamReportByPage(reportLookStudentFilterParam, page);
         * request.setAttribute("elistStudentComplianceReport",
         * elistStudentComplianceReport); List<Scale> scaleList =
         * reportLookService .queryDistinctScales(orgEntity);
         * request.setAttribute("scaleList", scaleList);
         * request.setAttribute("page", page);
         */
        return viewName("studentTeamReport");
    }

    @RequestMapping(value = "/studentRemarkReport", method = RequestMethod.GET)
    public String studentRemarkReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        Account account = (Account)request.getSession().getAttribute("user");
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是学校用户
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        List<ClassSchool> selectByBzrAccountid = classService.selectByBzrAccountid(account.getId().longValue());
        if (selectByBzrAccountid.size() > 0)
        {
          long id = ((ClassSchool)selectByBzrAccountid.get(0)).getId().longValue();
          reportLookStudentFilterParam.setBj((int)id);
        }
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentComplianceReport = this.reportLookService
                .getStuComplianceReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentRemarkReport", elistStudentComplianceReport);
        request.setAttribute("page", page);
        return viewName("studentRemarkReport");
    }

    @RequestMapping(value = "/teacherreportpage", method = RequestMethod.GET)
    public String teacherreportpage(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        // 添加角色
        Long orgId = orgEntity.getId();
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        request.setAttribute("exList", exList);
        // 添加量表
        List<Scale> scaleList = reportLookService.queryTeacherDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        ReportLookTeacherFilterParam reportLookTeacherFilterParam = new ReportLookTeacherFilterParam();
        reportLookTeacherFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultTeacher> elistTeacherPersonalReport = this.reportLookService
                .getTeacherResultByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherPersonalReport", elistTeacherPersonalReport);

        request.setAttribute("reportLookTeacherFilterParam", reportLookTeacherFilterParam);
        return viewName("teacherreport");
    }

    @RequestMapping(value = "/selectSchool", method = RequestMethod.POST)
    @ResponseBody
    public String selectSchool(HttpServletRequest request, @RequestParam("xzqh") String xzqh) {
        List<School> schoolList = this.schoolService.getSchoolsAccordingOrgId(xzqh);
        Gson gson = new Gson();
        String schoolResult = gson.toJson(schoolList);
        return schoolResult;
    }

    @RequestMapping(value = "/teacherPersonalReport", method = RequestMethod.GET)
    public String teacherPersonalReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        // 添加角色
        Long orgId = orgEntity.getId();
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        request.setAttribute("exList", exList);
        // 添加量表
        List<Scale> scaleList = reportLookService.queryTeacherDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        ReportLookTeacherFilterParam reportLookTeacherFilterParam = new ReportLookTeacherFilterParam();
        reportLookTeacherFilterParam.setOrgid(orgId);
        request.setAttribute("reportLookTeacherFilterParam", reportLookTeacherFilterParam);
        List<ExamresultTeacher> elistTeacherPersonalReport = this.reportLookService
                .getTeacherResultByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherPersonalReport", elistTeacherPersonalReport);
        request.setAttribute("page", page);
        return viewName("teacherPersonalReport");
    }

    @RequestMapping(value = "/teacherComplianceReport", method = RequestMethod.GET)
    public String teacherComplianceReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        // 添加角色
        Long orgId = orgEntity.getId();
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        request.setAttribute("exList", exList);
        ReportLookTeacherFilterParam reportLookTeacherFilterParam = new ReportLookTeacherFilterParam();
        reportLookTeacherFilterParam.setOrgid(orgId);
        request.setAttribute("reportLookTeacherFilterParam", reportLookTeacherFilterParam);
        List<ExamresultTeacher> elistTeacherComplianceReport = this.reportLookService.getTeaComplianceReportByPage(reportLookTeacherFilterParam,page);
        request.setAttribute("elistTeacherComplianceReport", elistTeacherComplianceReport);
        request.setAttribute("page", page);
        return viewName("teacherComplianceReport");
    }

    @RequestMapping(value = "/teacherRemarkReport", method = RequestMethod.GET)
    public String teacherRemarkReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        // 添加角色
        Long orgId = orgEntity.getId();
        ReportLookTeacherFilterParam reportLookTeacherFilterParam = new ReportLookTeacherFilterParam();
        reportLookTeacherFilterParam.setOrgid(orgId);
        request.setAttribute("reportLookTeacherFilterParam", reportLookTeacherFilterParam);
        List<ExamresultTeacher> elistTeacherRemarkReport = this.reportLookService
                .getTeacherResultByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherRemarkReport", elistTeacherRemarkReport);
        request.setAttribute("page", page);
        return viewName("teacherRemarkReport");
    }

    @RequestMapping(value = "/teacherTeamReport", method = RequestMethod.GET)
    public String teacherTeamReport(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        int orgLevel = orgEntity.getOrgLevel();
        request.setAttribute("orgLevel", orgLevel);
        Long orgId = orgEntity.getId();
        if (orgtype.equals("2")) {// 学校
            List<Role> roleList = reportLookService.queryDistinctRoleFromExamresultTeacher(orgEntity);
            request.setAttribute("roleList", roleList);
        }
        // 如果是区县教委用户
        else {
            if (orgLevel == 4) {
                // 获取该组织下面的所有学校
                List<Organization> schoolList = reportLookService
                        .getSchoolsFromExamresultTeacher(orgEntity.getCountyid());
                request.setAttribute("schoolList", schoolList);
                List<Role> roleList = reportLookService.queryDistinctRoleFromExamresultTeacher(orgEntity);
                request.setAttribute("roleList", roleList);
            }
            if (orgLevel == 3) {
            }
        }
        ReportLookTeacherFilterParam reportLookTeacherFilterParam = new ReportLookTeacherFilterParam();
        request.setAttribute("reportLookTeacherFilterParam", reportLookTeacherFilterParam);
        return viewName("teacherTeamReport");
    }

    // 只有学校管理员才能看到这个界面
    @RequestMapping(value = "/stuPersonalReporturl", method = RequestMethod.POST)
    public String personalReporturl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentPersonalReport = this.reportLookService
                .getStudentResultByPage(reportLookStudentFilterParam, page);

        request.setAttribute("elistStudentPersonalReport", elistStudentPersonalReport);
        List<Scale> scaleList = reportLookService.queryDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        request.setAttribute("page", page);
        return viewName("studentPersonalReport");
    }

    // 只有学校管理员才能看到这个界面
    @RequestMapping(value = "/searchStuPersonalReporturl", method = RequestMethod.POST)
    public String searchStuPersonalReporturl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentPersonalReport = this.reportLookService
                .getStudentResultByPage(reportLookStudentFilterParam, page);

        request.setAttribute("elistStudentPersonalReport", elistStudentPersonalReport);
        List<Scale> scaleList = reportLookService.queryDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        request.setAttribute("page", page);
        return viewName("studentPersonalReportTableCon");
    }

    @RequestMapping(value = "/stuComplianceReportStuUrl", method = RequestMethod.POST)
    public String stuComplianceReportStuUrl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        long orgid = orgEntity.getId();
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        reportLookStudentFilterParam.setOrgid(orgid);
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        request.setAttribute("gradeList", gradeList);
        List<ExamresultStudentWithBackGround> elistStudentComplianceReport = this.reportLookService
                .getStuComplianceReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentComplianceReport", elistStudentComplianceReport);
        request.setAttribute("page", page);
        return viewName("studentComplianceReport");
    }

    @RequestMapping(value = "/searchStuComplianceReportStuUrl", method = RequestMethod.POST)
    public String searchStuComplianceReportStuUrl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        long orgid = orgEntity.getId();
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        reportLookStudentFilterParam.setOrgid(orgid);
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        request.setAttribute("gradeList", gradeList);
        List<ExamresultStudentWithBackGround> elistStudentComplianceReport = this.reportLookService
                .getStuComplianceReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentComplianceReport", elistStudentComplianceReport);
        request.setAttribute("page", page);
        return viewName("studentComplianceReportTableCon");
    }

    @RequestMapping(value = "/stuRemarkReportStuUrl", method = RequestMethod.POST)
    public String stuRemarkReportStuUrl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        long orgid = orgEntity.getId();
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        reportLookStudentFilterParam.setOrgid(orgid);
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        request.setAttribute("gradeList", gradeList);
        List<ExamresultStudentWithBackGround> elistStudentRemarkReport = this.reportLookService
                .getStuComplianceReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentRemarkReport", elistStudentRemarkReport);
        request.setAttribute("page", page);
        return viewName("studentRemarkReport");
    }

    @RequestMapping(value = "/searchStuRemarkReportStuUrl", method = RequestMethod.POST)
    public String searchStuRemarkReportStuUrl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        long orgid = orgEntity.getId();
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        reportLookStudentFilterParam.setOrgid(orgid);
        // List<ExamresultStudent> xdList =
        // reportLookService.queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        // request.setAttribute("xdList", xdList);
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        request.setAttribute("gradeList", gradeList);
        List<ExamresultStudentWithBackGround> elistStudentRemarkReport = this.reportLookService
                .getStuComplianceReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentRemarkReport", elistStudentRemarkReport);
        request.setAttribute("page", page);
        return viewName("studentRemarkReportTableCon");
    }

    @RequestMapping(value = "/stuPersonalCommentsurl", method = RequestMethod.POST)
    public String stuPersonalCommentsurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        // 如果是教委用户
        if (orgtype.equals("1")) {
            HashMap<OrganizationType, List<Organization>> mapResult = organizationService.getAllSubOrgs(orgEntity);
            request.setAttribute("orgSubMap", mapResult);
        } else {
            // 如果是学校用户
            List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
            request.setAttribute("gradeList", gradeList);
        }
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultStudent> elistStudentPersonalComments = this.reportLookService
                .getStuPersonalCommentsByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentPersonalComments", elistStudentPersonalComments);
        request.setAttribute("page", page);
        return viewName("studentPersonalComments");
    }

    @RequestMapping(value = "/stuTeamReportsurl", method = RequestMethod.POST)
    public String stuTeamReportsurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        // 如果是教委用户
        if (orgtype.equals("1")) {
            HashMap<OrganizationType, List<Organization>> mapResult = organizationService.getAllSubOrgs(orgEntity);
            request.setAttribute("orgSubMap", mapResult);
        } else {
            // 如果是学校用户
            List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
            request.setAttribute("gradeList", gradeList);
        }
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultStudentWithBackGround> elist = this.reportLookService
                .getStuTeamReportByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elist", elist);
        request.setAttribute("page", page);
        return viewName("studentTeamReport");
    }

    @RequestMapping(value = "/teacherPersonalReporturl", method = RequestMethod.POST)
    public String teacherPersonalReporturl(
            @ModelAttribute("reportLookTeacherFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        // 添加角色
        Long orgId = orgEntity.getId();
        reportLookTeacherFilterParam.setOrgid(orgId);
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        request.setAttribute("exList", exList);
        // 添加量表
        List<Scale> scaleList = reportLookService.queryTeacherDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        reportLookTeacherFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultTeacher> elistTeacherPersonalReport = this.reportLookService
                .getTeacherResultByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherPersonalReport", elistTeacherPersonalReport);
        request.setAttribute("page", page);
        return viewName("teacherPersonalReport");
    }

    @RequestMapping(value = "/searchTeacherPersonalReporturl", method = RequestMethod.POST)
    public String searchTeacherPersonalReporturl(
            @ModelAttribute("reportLookTeacherFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        // 添加角色
        Long orgId = orgEntity.getId();
        reportLookTeacherFilterParam.setOrgid(orgId);
        // List<ExamresultTeacher> exList =
        // this.reportLookService.queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        // request.setAttribute("exList", exList);
        // 添加量表
        reportLookTeacherFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultTeacher> elistTeacherPersonalReport = this.reportLookService
                .getTeacherResultByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherPersonalReport", elistTeacherPersonalReport);
        request.setAttribute("page", page);
        return viewName("teacherPersonalReportTableCon");
    }

    @RequestMapping(value = "/teaComplianceReportsurl", method = RequestMethod.POST)
    public String teaComplianceReportsurl(
            @ModelAttribute("reportLookTeacherFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgEntity.getId().intValue());
        request.setAttribute("exList", exList);
        // 如果是教委用户
        /*
         * if (orgtype.equals("1")) { HashMap<OrganizationType,
         * List<Organization>> mapResult = organizationService
         * .getAllSubOrgs(orgEntity); request.setAttribute("orgSubMap",
         * mapResult); } else {
         */
        reportLookTeacherFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultTeacher> elistTeacherComplianceReport = this.reportLookService
                .getTeaComplianceReportByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherComplianceReport", elistTeacherComplianceReport);
        request.setAttribute("page", page);
        return viewName("teacherComplianceReport");
    }

    @RequestMapping(value = "/searchTeaComplianceReportsurl", method = RequestMethod.POST)
    public String searchTeaComplianceReportsurl(
            @ModelAttribute("reportLookTeacherFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgEntity.getId().intValue());
        request.setAttribute("exList", exList);
        // 如果是教委用户
        /*
         * if (orgtype.equals("1")) { HashMap<OrganizationType,
         * List<Organization>> mapResult = organizationService
         * .getAllSubOrgs(orgEntity); request.setAttribute("orgSubMap",
         * mapResult); } else {
         */
        reportLookTeacherFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultTeacher> elistTeacherComplianceReport = this.reportLookService
                .getTeaComplianceReportByPage(reportLookTeacherFilterParam, page);
        request.setAttribute("elistTeacherComplianceReport", elistTeacherComplianceReport);
        request.setAttribute("page", page);
        return viewName("teacherComplianceReportTableCon");
    }

    @RequestMapping(value = "/teaTeamReportsurl", method = RequestMethod.POST)
    public String teaTeamReportsurl(
            @ModelAttribute("reportLookTeacherFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, @PageAnnotation PageParameter page) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        // 如果是教委用户
        if (orgtype.equals("1")) {
            HashMap<OrganizationType, List<Organization>> mapResult = organizationService.getAllSubOrgs(orgEntity);
            request.setAttribute("orgSubMap", mapResult);
        } else {
            // 如果是学校用户
            List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
            request.setAttribute("gradeList", gradeList);
        }
        reportLookTeacherFilterParam.setOrgid(orgEntity.getId());
        List<ExamresultTeacher> elist = this.reportLookService.getTeaTeamReportsByPage(reportLookTeacherFilterParam,
                page);
        request.setAttribute("elist", elist);
        request.setAttribute("page", page);
        return viewName("teacherTeamReportTable");
    }

    @RequestMapping(value = "/getStudentExamdoQuxianStr", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentExamdoQuxianStr(HttpServletRequest req) {
        List<District> districtList = reportLookService.selectStudentDistinctQuxian();
        Gson gson = new Gson();
        String result = gson.toJson(districtList);
        return result;
    }

    @RequestMapping(value = "/getTeacherExamdoQuxianStr", method = RequestMethod.POST)
    @ResponseBody
    public String getTeacherExamdoQuxianStr(HttpServletRequest req, @CurrentOrg Organization orgEntity,
            @RequestParam("isSonSchool") String isSonSchool) {
        Map resultMap = new HashMap();
        List<Role> roleList = null;
        if (isSonSchool.equals("yes")) {
            List<Organization> schoolList = reportLookService.selectTeacherDistinctSubSchool();
            roleList = reportLookService.queryDistinctRoleFromExamresultTeacher(orgEntity, null, true);
            resultMap.put("schoolList", schoolList);
        }
        if (isSonSchool.equals("no")) {
            List<District> districtList = reportLookService.selectTeacherDistinctQuxian();
            roleList = reportLookService.queryDistinctRoleFromExamresultTeacher(orgEntity, null, false);
            resultMap.put("districtList", districtList);
        }

        resultMap.put("roleList", roleList);

        Gson gson = new Gson();
        String result = gson.toJson(resultMap);
        return result;
    }

    @RequestMapping(value = "/queryDistinctXdInSchool", method = RequestMethod.POST)
    @ResponseBody
    public String queryDistinctXdInSchool(@RequestParam("schoolid") int schoolid, @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        List<ExamresultStudent> xdList = reportLookService.queryDistinctXDFromExamresultStudent(schoolid);
        Gson gson = new Gson();
        String classStr = gson.toJson(xdList);
        return classStr;
    }

    @RequestMapping(value = "/queryDistinctNJFromExamresultStudent", method = RequestMethod.POST)
    @ResponseBody
    public String queryDistinctNJFromExamresultStudent(@RequestParam("xd") int xd, @RequestParam("xzxs") String xzxs,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = null;
        if (xzxs.equals("2"))
            result = this.reportLookService.queryDistinctNJFromExamresultStudent(orgEntity, xd, true);
        else
            result = this.reportLookService.queryDistinctNJFromExamresultStudent(orgEntity, xd, false);
        return result;
    }

    @RequestMapping(value = "/queryDistinctNJByCountyidOrSchoolid", method = RequestMethod.POST)
    @ResponseBody
    public String queryDistinctNJByCountyidOrSchoolid(@RequestParam("id") String id, @RequestParam("xd") int xd,
            @RequestParam("xzxs") String xzxs, @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = null;
        if (xzxs.equals("2"))
            result = this.reportLookService.queryDistinctNJByCountyidOrSchoolid(orgEntity, id, xd, true);
        else
            result = this.reportLookService.queryDistinctNJByCountyidOrSchoolid(orgEntity, id, xd, false);
        return result;
    }

    /**
     * 教委机构获取团体报告时获得量表
     * 
     * @param xd
     * @param nj
     * @param xzxs
     * @param orgEntity
     * @return
     */
    @RequestMapping(value = "/getEduScaleInfoFromExamdoStudent", method = RequestMethod.POST)
    @ResponseBody
    public String getEduScaleInfoFromExamdoStudent(@RequestParam("countyidOrSchoolid") String countyidOrSchoolid,
            @RequestParam("xd") int xd, @RequestParam("nj") int nj, @RequestParam("xzxs") String xzxs,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = null;

        if (xzxs.equals("2"))
            result = this.reportLookService.getEduScaleInfoFromExamdoStudent(orgEntity, countyidOrSchoolid, xd, nj,
                    true);
        else if (orgEntity.getOrgLevel() == 4)
            result = this.reportLookService.getEduScaleInfoFromExamdoStudent(orgEntity, orgEntity.getCountyid(), xd, nj,
                    false);
        else
            result = this.reportLookService.getEduScaleInfoFromExamdoStudent(orgEntity, countyidOrSchoolid, xd, nj,
                    false);

        return result;
    }

    @RequestMapping(value = "/getEduScaleInfoFromExamdoStudent1", method = RequestMethod.POST)
    @ResponseBody
    public String getEduScaleInfoFromExamdoStudent1(@RequestParam("countyidOrSchoolid") String countyidOrSchoolid,
            @RequestParam("xd") int xd, @RequestParam("nj") int nj, @RequestParam("xzxs") String xzxs,
            @RequestParam("scaletype") int scaletype, @RequestParam("scalesource") int scalesource,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = null;

        if (xzxs.equals("2"))
            result = this.reportLookService.getEduScaleInfoFromExamdoStudent(orgEntity, countyidOrSchoolid, xd, nj,
                    scaletype, scalesource, true);
        else if (orgEntity.getOrgLevel() == 4)
            result = this.reportLookService.getEduScaleInfoFromExamdoStudent(orgEntity, orgEntity.getCountyid(), xd, nj,
                    scaletype, scalesource, false);
        else
            result = this.reportLookService.getEduScaleInfoFromExamdoStudent(orgEntity, countyidOrSchoolid, xd, nj,
                    scaletype, scalesource, false);

        return result;
    }

    @RequestMapping(value = "/queryDistinctBJFromExamresultStudent", method = RequestMethod.POST)
    @ResponseBody
    public String queryDistinctBJFromExamresultStudent(@RequestParam("nj") int nj, @RequestParam("xd") int xd,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = this.reportLookService.queryDistinctBJFromExamresultStudent(orgId, xd, nj);
        return result;
    }

    @RequestMapping(value = "/queryDistinctBJAndScaleFromExamresultStudent", method = RequestMethod.POST)
    @ResponseBody
    public String queryDistinctBJAndScaleFromExamresultStudent(@RequestParam("nj") int nj, @RequestParam("xd") int xd,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = this.reportLookService.queryDistinctBJFromExamresultStudent(orgId, xd, nj);
        return result;
    }

    @RequestMapping(value = "/queryDistinctBJFromExamresultStudentByNJ", method = RequestMethod.POST)
    @ResponseBody
    public String queryDistinctBJFromExamresultStudentByNJ(@RequestParam("njmc") String njmc,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        String result = this.reportLookService.queryDistinctBJFromExamresultStudentByNJ(orgId, njmc);
        return result;
    }

    @RequestMapping(value = "/getScaleFromExamResultTeacher", method = RequestMethod.POST)
    @ResponseBody
    public String getScaleFromExamResultTeacher(@RequestParam("orgType") String orgType,
            @RequestParam("countyidOrSchoolid") String countyidOrSchoolid, @RequestParam("orgLevel") String orgLevel,
            @RequestParam("roleid") int roleid, @RequestParam("isSonSchool") String isSonSchool,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        List<ScaleInfo> scaleList = null;
        if (isSonSchool.equals("yes"))
            scaleList = this.reportLookService.queryTeacherDistinctScales(orgEntity, countyidOrSchoolid, roleid, -1, -1,
                    true);
        else
            scaleList = this.reportLookService.queryTeacherDistinctScales(orgEntity, countyidOrSchoolid, roleid, -1, -1,
                    false);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }
        }
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/getScaleFromExamResultTeacher1", method = RequestMethod.POST)
    @ResponseBody
    public String getScaleFromExamResultTeacher1(@RequestParam("countyidOrSchoolid") String countyidOrSchoolid,
            @RequestParam("roleid") int roleid, @RequestParam("scaletype") int scaletype,
            @RequestParam("scalesource") int scalesource, @RequestParam("xzxs") String xzxs,
            @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        List<ScaleInfo> scaleList = null;
        if (xzxs.equals("2"))
            scaleList = this.reportLookService.queryTeacherDistinctScales(orgEntity, countyidOrSchoolid, roleid,
                    scaletype, scalesource, true);
        else
            scaleList = this.reportLookService.queryTeacherDistinctScales(orgEntity, countyidOrSchoolid, roleid,
                    scaletype, scalesource, false);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);
            }
        }
        resultMap.put("scaleList", scaleinfoList);
        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/getRoleInfoFromExamResultTeacher", method = RequestMethod.POST)
    @ResponseBody
    public String getRoleInfoFromExamResultTeacher(@RequestParam("countyidOrSchoolid") String countyidOrSchoolid,
            @RequestParam("isSonSchool") String isSonSchool, @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        List<Role> roleList = null;
        if (isSonSchool.equals("yes"))
            roleList = reportLookService.queryDistinctRoleFromExamresultTeacher(orgEntity, countyidOrSchoolid, true);
        if (isSonSchool.equals("no"))
            roleList = reportLookService.queryDistinctRoleFromExamresultTeacher(orgEntity, countyidOrSchoolid, false);
        Gson gson = new Gson();
        String classStr = gson.toJson(roleList);
        return classStr;
    }

    @RequestMapping(value = "/stuTeamReportsSchoolurl", method = RequestMethod.POST)
    public String stuTeamReportsSchoolurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        String download = request.getParameter("download");
        int orgId = orgEntity.getId().intValue();
        String xd = request.getParameter("xd");
        if (xd.equals("1"))
            xd = "小学";
        if (xd.equals("2"))
            xd = "初中";
        if (xd.equals("3"))
            xd = "高中";
        String njmc = request.getParameter("nj");
        if (!njmc.contains("级"))
            njmc = xd + njmc + "级";
        String[] bjArray = null;
        String bjarrayJson = request.getParameter("bjarray");
        if (!StringUtils.isEmpty(bjarrayJson)) {
            JSONArray jsonArray = JSONArray.fromObject(bjarrayJson);
            if (jsonArray.size() > 0) {
                bjArray = new String[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object o = jsonArray.get(i);
                    bjArray[i] = o.toString();
                }
            } else {
                List<ExamresultStudent> ersList = examdoService.queryBJFromExamresultStudentByNj(orgId,
                        Integer.parseInt(request.getParameter("xd")), Integer.parseInt(request.getParameter("nj")));
                if (ersList.size() > 0) {
                    bjArray = new String[ersList.size()];
                    for (int i = 0; i < ersList.size(); i++) {
                        ExamresultStudent es = ersList.get(i);
                        String bj = es.getBj() + "";
                        bjArray[i] = bj;
                    }
                }
            }

        }
        // orgId = 119;
        String startDate = request.getParameter("starttime");
        String endDate = request.getParameter("endtime");

        // String startDate = "2015-10-13 13:38:25";
        // String endDate = "2015-11-17 13:38:25";
        String scaleId = request.getParameter("scaleName");
        String scaleName = cachedScaleMgr.get(scaleId).getTitle();
        // String njmc = "高中2015级";
        // String[] bjmcArray = { "1501班" };
        // String scaleId = "120100001";
        if (!"yes".equals(download)) {
        } else {
            try {
                response.setContentType("application/msword");
                response.setHeader("Content-disposition", "attachment;   filename="
                        + new String(("[" + scaleName + "]" + "量表团体测评报告.doc").getBytes("gb2312"), "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Map<Object, Object> page = reportLookService.getStudentGroupReportForSchool(orgId, njmc, bjArray, scaleId,
                startDate, endDate);
        model.addAttribute("page", page);
        return viewName("studentgroupreport");
    }

    @RequestMapping(value = "/teaTeamReportsSchoolurl", method = RequestMethod.POST)
    public String teaTeamReportsSchoolurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        String download = request.getParameter("download");
        int orgId = orgEntity.getId().intValue();
        String roleidStr = request.getParameter("roleName");
        int roleid = Integer.parseInt(roleidStr);
        String roleName = "";
        if (roleid > 0) {
            Role role = roleService.selectRole(roleid);
            roleName = role.getRoleName();
        } else
            roleName = "所有教师";
        String startDate = request.getParameter("starttime");
        String endDate = request.getParameter("endtime");

        String scaleId = request.getParameter("scaleName");
        String scaleName = cachedScaleMgr.get(scaleId).getTitle();
        if (!"yes".equals(download)) {
        } else {
            try {
                response.setContentType("application/msword");
                response.setHeader("Content-disposition", "attachment;   filename="
                        + new String(("[" + scaleName + "]" + "量表团体测评报告.doc").getBytes("gb2312"), "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Map<Object, Object> page = reportLookService.getTeacherGroupReportForSchool(orgId, roleid, roleName, scaleId,
                startDate, endDate);
        model.addAttribute("page", page);
        return viewName("teachergroupreport");
    }

    @RequestMapping(value = "/stuTeamReportsEduCountryurl", method = RequestMethod.POST)
    public String stuTeamReportsEduCountryurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        String countyid = orgEntity.getCountyid();
        int orgid = orgEntity.getId().intValue();

        String xd = request.getParameter("xd");
        if (xd.equals("1"))
            xd = "小学";
        if (xd.equals("2"))
            xd = "初中";
        if (xd.equals("3"))
            xd = "高中";
        String xzxs = request.getParameter("xzxs");
        String njmc = request.getParameter("nj");
        njmc = xd + njmc + "级";
        String[] qxOrxxArray = null;
        String qxorxxarrayJson = request.getParameter("qxorxxarray");
        if (qxorxxarrayJson != null) {
            JSONArray jsonArray = JSONArray.fromObject(qxorxxarrayJson);
            if (jsonArray.size() > 0) {
                qxOrxxArray = new String[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object o = jsonArray.get(i);
                    qxOrxxArray[i] = o.toString();
                }
            } else {// 没有选择学校，则取所有学校
                List<String> schoolids = examdoService.querySchoolsFromExamresult(countyid);
                if (schoolids != null && schoolids.size() > 0) {
                    qxOrxxArray = new String[schoolids.size()];
                    for (int i = 0; i < schoolids.size(); i++) {
                        qxOrxxArray[i] = String.valueOf(schoolids.get(i));
                    }
                }

                // qxOrxxArray = (String[])schoolids.toArray();
            }

        }

        String scaleid = request.getParameter("scaleName");
        String startDate = request.getParameter("starttime");
        String endDate = request.getParameter("endtime");

        Map<Object, Object> page = reportLookService.getStudentGroupReportForCounty(countyid, qxOrxxArray, njmc,
                scaleid, startDate, endDate);
        model.addAttribute("page", page);
        return viewName("studentgroupreport");
    }

    @RequestMapping(value = "/teaTeamReportsEduCountryurl", method = RequestMethod.POST)
    public String teaTeamReportsEduCountryurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        String countyid = orgEntity.getCountyid();
        int orgid = orgEntity.getId().intValue();
        String roleidStr = request.getParameter("roleName");
        int roleid = Integer.parseInt(roleidStr);
        String roleName = "";

        if (roleid > 0) {
            Role role = roleService.selectRole(roleid);
            roleName = role.getRoleName();
        }

        // String rolename = role.getRoleName();
        String[] qxOrxxArray = null;
        String qxorxxarrayJson = request.getParameter("qxorxxarray");
        if (qxorxxarrayJson != null) {
            JSONArray jsonArray = JSONArray.fromObject(qxorxxarrayJson);
            qxOrxxArray = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                Object o = jsonArray.get(i);
                qxOrxxArray[i] = o.toString();
            }
        }
        String scaleid = request.getParameter("scaleName");
        String startDate = request.getParameter("starttime");
        String endDate = request.getParameter("endtime");

        Map<Object, Object> page = reportLookService.getTeacherGroupReportForCounty(countyid, qxOrxxArray, roleid,
                roleName, scaleid, startDate, endDate);
        model.addAttribute("page", page);
        return viewName("studentgroupreport");
    }

    @RequestMapping(value = "/stuTeamReportsEduCityurl", method = RequestMethod.POST)
    public String stuTeamReportsEduCityurl(
            @ModelAttribute("reportLookStudentFilterParam") ReportLookStudentFilterParam reportLookStudentFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        int orgid = orgEntity.getId().intValue();
        String cityid = orgEntity.getCityid();
        String cityname = districtService.selectByCode(cityid).getName();

        String xd = request.getParameter("xd");
        if (xd.equals("1"))
            xd = "小学";
        if (xd.equals("2"))
            xd = "初中";
        if (xd.equals("3"))
            xd = "高中";
        String xzxs = request.getParameter("xzxs");
        String njmc = request.getParameter("nj");
        njmc = xd + njmc + "级";
        String[] qxOrxxArray = null;
        String qxorxxarrayJson = request.getParameter("qxorxxarray");
        if (qxorxxarrayJson != null) {
            JSONArray jsonArray = JSONArray.fromObject(qxorxxarrayJson);
            qxOrxxArray = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                Object o = jsonArray.get(i);
                qxOrxxArray[i] = o.toString();
            }

        }

        String scaleid = request.getParameter("scaleName");
        String startDate = request.getParameter("starttime");
        String endDate = request.getParameter("endtime");

        Map<Object, Object> page = null;
        if (xzxs.equals("1")) {// 区县
            page = reportLookService.getStudentGroupReportForCity(cityname, qxOrxxArray, njmc, scaleid, startDate,
                    endDate);
        }
        if (xzxs.equals("2")) {// 直属学校
            page = reportLookService.getStudentGroupReportForSubSchool(cityname, qxOrxxArray, njmc, scaleid, startDate,
                    endDate);
        }
        model.addAttribute("page", page);
        return viewName("studentgroupreport");
    }

    @RequestMapping(value = "/teaTeamReportsEduCityurl", method = RequestMethod.POST)
    public String teaTeamReportsEduCityurl(
            @ModelAttribute("reportLookTeacherFilterParam") ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            @CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        int orgid = orgEntity.getId().intValue();
        String cityid = orgEntity.getCityid();
        String cityname = districtService.selectByCode(cityid).getName();
        String roleidStr = request.getParameter("roleName");
        int roleid = Integer.parseInt(roleidStr);
        // Role role = roleService.selectRole(roleid);
        // String rolename = role.getRoleName();

        String roleName = "";

        if (roleid > 0) {
            Role role = roleService.selectRole(roleid);
            roleName = role.getRoleName();
        }

        String[] qxOrxxArray = null;
        String qxorxxarrayJson = request.getParameter("qxorxxarray");
        if (qxorxxarrayJson != null) {
            JSONArray jsonArray = JSONArray.fromObject(qxorxxarrayJson);
            qxOrxxArray = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                Object o = jsonArray.get(i);
                qxOrxxArray[i] = o.toString();
            }
        }
        String scaleid = request.getParameter("scaleName");
        String startDate = request.getParameter("starttime");
        String endDate = request.getParameter("endtime");
        String xzxs = request.getParameter("xzxs");
        Map<Object, Object> page = null;
        if (xzxs.equals("1")) {// 区县
            page = reportLookService.getTeacherGroupReportForCity(cityname, qxOrxxArray, roleid, roleName, scaleid,
                    startDate, endDate);
        }
        if (xzxs.equals("2")) {// 直属学校
            page = reportLookService.getTeacherGroupReportForSubSchool(cityname, qxOrxxArray, roleid, roleName, scaleid,
                    startDate, endDate);
        }
        model.addAttribute("page", page);
        return viewName("studentgroupreport");
    }

    /**
     * 显示学生答案
     * 
     * @throws IOException
     * @throws TemplateException
     */
    @RequestMapping(value = "/showStudentAnswerAction", method = RequestMethod.GET)
    public void showStudentAnswer(HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException, TemplateException {
        String resultid = request.getParameter("resultid");
        String export = request.getParameter("export");
        String threeangle = request.getParameter("threeangle");
        String typeflag = request.getParameter("v");

        String scaleid = "";
        String qscore = "";
        String tester = "";
        Map<String, Object> page = null;
        if (!threeangle.equals("yes"))
            page = reportLookService.getStudentAnswer((long) Integer.parseInt(resultid));
        else
            page = reportLookService.getThreeAngleAnswer((long) Integer.parseInt(resultid), Integer.parseInt(typeflag));
        Questionnaire questionnaire = (Questionnaire) page.get("questionnaire");
        if (!"yes".equals(export)) {

        } else {
            try {
                page.put("export", export);
                response.setHeader("Content-disposition", "attachment;   filename=" + new String(
                        (page.get("tester").toString() + questionnaire.getTitle() + "题目答案.doc").getBytes("gb2312"),
                        "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // page.setTemplate("testmanage/student_answers.ftl");
        FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
        Template t = reportCfg.getTemplate("scale_answers.flt");
        t.setEncoding("UTF-8");

        // Writer out = response.getWriter();
        ServletOutputStream out = response.getOutputStream();
        Writer w = new OutputStreamWriter(out, "utf-8");
        t.process(page, w);
        w.close();
        IOUtils.closeQuietly(out);

    }

    @RequestMapping(value = "/showTeacherAnswerAction", method = RequestMethod.GET)
    public void showTeacherAnswer(HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException, TemplateException {
        String resultid = request.getParameter("resultid");
        String export = request.getParameter("export");
        String scaleid = "";
        String qscore = "";
        String tester = "";
        Map<String, Object> page = null;
        page = reportLookService.getTeacherAnswer((long) Integer.parseInt(resultid));
        Questionnaire questionnaire = (Questionnaire) page.get("questionnaire");
        if (!"yes".equals(export)) {

        } else {
            try {
                page.put("export", export);
                response.setHeader("Content-disposition", "attachment;   filename=" + new String(
                        (page.get("tester").toString() + questionnaire.getTitle() + "题目答案.doc").getBytes("gb2312"),
                        "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // page.setTemplate("testmanage/student_answers.ftl");
        FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
        Template t = reportCfg.getTemplate("scale_answers.flt");
        t.setEncoding("UTF-8");

        // Writer out = response.getWriter();
        ServletOutputStream out = response.getOutputStream();
        Writer w = new OutputStreamWriter(out, "utf-8");
        t.process(page, w);
        w.close();
        IOUtils.closeQuietly(out);

    }

    /**
     * 删除报告
     * 
     * @param req
     * @param resp
     * @param page
     */
    @RequestMapping(value = "/delExamResultAction", method = RequestMethod.GET)
    public String delExamResultAction(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        String resultid = request.getParameter("resultid");
        StringBuffer mess = new StringBuffer();
        int result = reportLookService.deleteStudentExamResult((long) Integer.parseInt(resultid));
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是教委用户
        List<ExamresultStudent> xdList = reportLookService
                .queryDistinctXDFromExamresultStudent(orgEntity.getId().intValue());
        request.setAttribute("xdList", xdList);
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        reportLookStudentFilterParam.setOrgid(orgEntity.getId());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<ExamresultStudentWithBackGround> elistStudentPersonalReport = this.reportLookService
                .getStudentResultByPage(reportLookStudentFilterParam, page);
        request.setAttribute("elistStudentPersonalReport", elistStudentPersonalReport);
        List<Scale> scaleList = reportLookService.queryDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        request.setAttribute("page", page);
        return viewName("studentPersonalReport");

    }

}
