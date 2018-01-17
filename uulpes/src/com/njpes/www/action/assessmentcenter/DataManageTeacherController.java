package com.njpes.www.action.assessmentcenter;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.assessmentcenter.DataManageFilterParam;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.scaletoollib.ExamresultTeacher;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ExamAnswerServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;

@Controller
@RequestMapping(value = "/assessmentcenter/datamanager")
public class DataManageTeacherController extends BaseController {
    @Autowired
    RoleServiceI roleService;
    @Autowired
    private ReportLookService reportLookService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    private ExamdoServiceI examdoService;
    @Autowired
    public CachedScaleMgr cachedScaleMgr;
    @Autowired
    DistrictService districtService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    ExamAnswerServiceI examAnswerService;

    // 点击左侧的导航页面
    @RequestMapping(value = "dispatchToTeaDataManagePage", method = RequestMethod.GET)
    public String dispatchToDataManagePage(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("teacherdatamanagetab");
    }

    // 点击导入数据tab的action
    @RequestMapping(value = "dataTImportUrl", method = RequestMethod.GET)
    public String dataImportUrl(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        dataManageFilterParam.setOrgid(org.getId());
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // 添加分页的过滤
        request.setAttribute("page", page);

        if (orgType.equals("2")) {
            long schoolorgid = org.getId();
            List<Role> roleList = roleService.getTeacherRoles(schoolorgid);

            request.setAttribute("roleList", roleList);
        }
        List<Scale> scaleList = examdoService.queryTeacherDistinctScales(org);
        // List<Scale> scaleList =
        // reportLookService.queryTeacherDistinctScales(org);
        request.setAttribute("scaleList", scaleList);
        List scaleTypes = this.scaleService.getScaleTypes();
        request.setAttribute("scaleTypes", scaleTypes);
        List scaleSources = this.scaleService.getScaleSource();
        request.setAttribute("scaleSources", scaleSources);
        List examdoList = examdoService.queryTeacherExamdo(page, dataManageFilterParam);
        request.setAttribute("examdoList", examdoList);
        return viewName("teacherdataimportpage");
    }

    @RequestMapping(value = "dataTImportUrl1", method = RequestMethod.GET)
    public String dataImportUrl1(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        Long orgId = org.getId();
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        request.setAttribute("exList", exList);
        List<Scale> scaleList = examdoService.queryStudentDistinctScales(org);
        // List<Scale> scaleList = reportLookService.queryDistinctScales(org);
        request.setAttribute("scaleList", scaleList);
        List scaleTypes = this.scaleService.getScaleTypes();
        request.setAttribute("scaleTypes", scaleTypes);
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("teacherdataimportpage");
    }

    // 点击导出数据的tab的action
    @RequestMapping(value = "dataTExportUrl", method = RequestMethod.GET)
    public String dataExportUrl(@CurrentOrg Organization orgEntity, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        int orgLevel = orgEntity.getOrgLevel();
        request.setAttribute("orgLevel", orgLevel);
        Long orgId = orgEntity.getId();
        if (orgtype.equals("2")) {// 学校
            List<Role> roleList = roleService.getTeacherRoles(orgId);
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
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        dataManageFilterParam.setOrgid(orgEntity.getId());

        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("teacherdataexportpage");
    }

    // 搜索和分页用的导出数据的action
    @RequestMapping(value = "searchTExport", method = RequestMethod.POST)
    public String searchSExport(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam) {
        Long orgId = org.getId();
        List<ExamresultTeacher> exList = this.reportLookService
                .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
        request.setAttribute("exList", exList);
        List<Scale> scaleList = reportLookService.queryDistinctScales(org);
        request.setAttribute("scaleList", scaleList);
        List scaleTypes = this.scaleService.getScaleTypes();
        request.setAttribute("scaleTypes", scaleTypes);
        return viewName("teacherdatamanagetab");
    }

    // 搜索和分页用的导入数据的action
    @RequestMapping(value = "searchTImport", method = RequestMethod.POST)
    public String searchSImport(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam) {
        Long orgId = org.getId();
        dataManageFilterParam.setOrgid(orgId);
        /*
         * List<ExamresultTeacher> exList = this.reportLookService
         * .queryDistinctTeacherFromExamresultTeacher(orgId.intValue());
         * request.setAttribute("exList", exList); List<Scale> scaleList =
         * examdoService.queryTeacherDistinctScales(org);
         * request.setAttribute("scaleList", scaleList); List
         * scaleTypes=this.scaleService.getScaleTypes();
         * request.setAttribute("scaleTypes", scaleTypes);
         */
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        /*
         * if(orgType.equals("2")){ long schoolorgid = org.getId(); List<Role>
         * roleList = roleService.getTeacherRoles(schoolorgid);
         * 
         * request.setAttribute("roleList", roleList); }
         */
        // 添加分页的过滤
        request.setAttribute("page", page);
        List examdoList = examdoService.queryTeacherExamdo(page, dataManageFilterParam);
        request.setAttribute("examdoList", examdoList);
        // return viewName("teacherdataimportpage");
        return viewName("teacherdataimportpagetablecon");

    }

    @RequestMapping(value = "teacherbatchSubmit", method = RequestMethod.POST)
    public String teacherbatchSubmit(@CurrentOrg Organization org, HttpServletRequest request) {
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // 把用户的类型添加到request中去
        if (orgType.equals("2")) {
            long schoolorgid = org.getId();
            List<Role> roleList = roleService.getTeacherRoles(org.getId());
            request.setAttribute("roleList", roleList);
        }
        // 量表类别
        List scaleTypes = this.scaleService.getScaleTypes();
        List scaleSources = this.scaleService.getScaleSource();
        List<Scale> scaleList = examdoService.queryTeacherDistinctScales(org);
        request.setAttribute("scaleTypes", scaleTypes);
        request.setAttribute("scaleSources", scaleSources);
        request.setAttribute("scaleList", scaleList);
        return viewName("batchimportteacheranswer");
    }

    /*
     * @RequestMapping(value = "teacherbatchSubmit", method =
     * RequestMethod.POST) public String teacherbatchSubmit(@CurrentOrg
     * Organization org, HttpServletRequest request) { OrganizationType ot =
     * org.returnOrgTypeEnum(); String orgType = ot.getId();
     * request.setAttribute("orgType", orgType); // 把用户的类型添加到request中去 if
     * (orgType.equals("2")) { long schoolorgid = org.getId(); List<Role>
     * roleList = roleService.getTeacherRoles(org.getId());
     * request.setAttribute("roleList", roleList); } // 量表类别 List scaleTypes =
     * this.scaleService.getScaleTypes(); List scaleSources =
     * this.scaleService.getScaleSource(); request.setAttribute("scaleTypes",
     * scaleTypes); request.setAttribute("scaleSources", scaleSources); return
     * viewName("batchimportteacheranswer"); }
     */
    @RequestMapping(value = "/exportteacheranswerinsch", method = RequestMethod.POST)
    public void exportTchAnswerInSchool(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String roleid = request.getParameter("roleName");
        String scaleid = request.getParameter("scaleId");
        String startTime = request.getParameter("testStartTime");
        String endTime = request.getParameter("testEndTime");
        String orgid = String.valueOf(orgEntity.getId());
        String gender = request.getParameter("gender");
        Scale scale = cachedScaleMgr.get(scaleid);
        String[] attrIds = request.getParameterValues("attrIds");
        String title = scale.getTitle();
        title = title + "测评数据.xls";
        String filename = FileOperate.encodeFilename(title, request);
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            String[] attrnames = { "身份证号", "姓名", "性别", "角色职务", "工号" };
            String[] attrfields = { "sfzjh", "xm", "xbm", "rolename", "gh" };
            scaleService.downloadTchAnswerForSch(orgid, roleid, scaleid, attrnames, attrfields, attrIds, startTime,
                    endTime, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/exportteacheranswerinedu", method = RequestMethod.POST)
    public void exportteacheranswerinedu(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        int orgLevel = orgEntity.getOrgLevel();
        String roleid = request.getParameter("roleName");
        String scaleid = request.getParameter("scaleName");
        String startTime = request.getParameter("testStartTime");
        String endTime = request.getParameter("testEndTime");
        String orgid = String.valueOf(orgEntity.getId());
        String countyidOrSchoolid = request.getParameter("countyidOrSchoolid");
        String gender = request.getParameter("gender");
        Scale scale = cachedScaleMgr.get(scaleid);
        String[] attrIds = request.getParameterValues("attrIds");
        String title = scale.getTitle();
        title = title + "测评数据.xls";
        String filename = FileOperate.encodeFilename(title, request);
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            String[] attrnames = { "身份证号", "姓名", "性别", "角色职务", "工号" };
            String[] attrfields = { "sfzjh", "xm", "xbm", "rolename", "gh" };
            if (orgLevel == 3) {
                String xzxs = request.getParameter("xzxs");
                if (xzxs.equals("1")) {
                    String countyid = request.getParameter("qx_or_xx");
                    examAnswerService.downloadTchAnswerForCityEdu(countyid, roleid, null, scaleid, attrnames,
                            attrfields, attrIds, startTime, endTime, outputStream);
                }
                if (xzxs.equals("2")) {
                    String schoolid = request.getParameter("qx_or_xx");//
                    examAnswerService.downloadTchAnswerForSch(schoolid, roleid, scaleid, attrnames, attrfields, attrIds,
                            startTime, endTime, outputStream);
                }
            }
            if (orgLevel == 4) {
                examAnswerService.downloadTchAnswerForSch(countyidOrSchoolid, roleid, scaleid, attrnames, attrfields,
                        attrIds, startTime, endTime, outputStream);
            }
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/showSelectTeacherAttrForm", method = RequestMethod.POST)
    public String showSelectAttrForm(@CurrentOrg Organization org, HttpServletRequest request) {
        PropObject propObject = null;
        propObject = PropObject.createPropObject(AcountTypeFlag.teacher.getId());
        propObject.loadMetas();
        List<FieldValue> list = propObject.getAttrs();
        request.setAttribute("attr_list", list);
        return viewName("attrform");
    }
}
