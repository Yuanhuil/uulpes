package com.njpes.www.action.assessmentcenter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.assessmentcenter.DataManageFilterParam;
import com.njpes.www.entity.assessmentcenter.Examdo;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.GradeCode;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.entity.scaletoollib.StatScope;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.GradeCodeServiceImpl;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ExamAnswerServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;
import edutec.scale.util.ScaleUtils;
import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/assessmentcenter/datamanager")
public class DataManageStudentController extends BaseController {

    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    private ExamdoServiceI examdoService;
    @Autowired
    public CachedScaleMgr cachedScaleMgr;
    @Autowired
    private ReportLookService reportLookService;
    @Autowired
    DistrictService districtService;

    @Autowired
    GradeCodeServiceImpl gradeCodeService;
    @Autowired
    ExamAnswerServiceI examAnswerService;
    
 // 点击导出数据的tab的action
    @RequestMapping(value = "exportAllData", method = RequestMethod.GET)
    public String exportAllData(@CurrentOrg Organization orgEntity, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是教委用户
        // 把用户的类型添加到request中去
        request.setAttribute("orgLevel", orgEntity.getOrgLevel());
        // 如果是学校
        if (orgTypeId.equals("2")) {
            long schoolorgid = orgEntity.getId();
            List<ExamresultStudent> xdList = reportLookService.queryDistinctXDFromExamresultStudent((int) schoolorgid);
            request.setAttribute("xdList", xdList);
            // List<Grade> gradeList =
            // schoolService.getGradeListInSchool(schoolorgid);
            // request.setAttribute("gradeList", gradeList);
        } else if (orgTypeId.equals("1") || orgTypeId.equals("3")) { // 如果是教委用户，3表示的是陶老师
            int orgLevel = orgEntity.getOrgLevel();
            List<GradeCode> gradeAllList = gradeCodeService.getAllGrades();
            request.setAttribute("gradeAllList", gradeAllList);
            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = orgEntity.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                List<Organization> schoolList = reportLookService
                        .getSchoolsFromExamresultStudent(orgEntity.getCountyid());
                request.setAttribute("schoollist", schoolList);
            }
        }
        // List<Scale> scaleList =
        // reportLookService.queryDistinctScales(orgEntity);
        // request.setAttribute("scaleList", scaleList);
        // List scaleTypes = this.scaleService.getScaleTypes();
        // List scaleSources = this.scaleService.getScaleSource();
        // request.setAttribute("scaleTypes", scaleTypes);
        // request.setAttribute("scaleSources", scaleSources);
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        dataManageFilterParam.setOrgid(orgEntity.getId());
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("exportalldata");
    }


    // 点击左侧的导航页面
    @RequestMapping(value = "dispatchToStuDataManagePage", method = RequestMethod.GET)
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

        return viewName("datamanagetab");
    }

    // 点击导入数据tab的action
    @RequestMapping(value = "dataImportUrl", method = RequestMethod.GET)
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
        // 为页面添加下拉列表的东西
        if (orgType.equals("2")) {
            long schoolorgid = org.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(schoolorgid);
            request.setAttribute("gradeList", gradeList);
        }
        // List<Scale> scaleList = reportLookService.queryDistinctScales(org);
        List<Scale> scaleList = examdoService.queryStudentDistinctScales(org);
        request.setAttribute("scaleList", scaleList);
        List scaleTypes = this.scaleService.getScaleTypes();
        request.setAttribute("scaleTypes", scaleTypes);
        List scaleSources = this.scaleService.getScaleSource();
        request.setAttribute("scaleSources", scaleSources);
        List examdoList = examdoService.queryStudentExamdo(page, dataManageFilterParam);
        request.setAttribute("examdoList", examdoList);
        return viewName("dateimportpage");
    }

    // 点击导出数据的tab的action
    @RequestMapping(value = "dataExportUrl", method = RequestMethod.GET)
    public String dataExportUrl(@CurrentOrg Organization orgEntity, @PageAnnotation PageParameter page,
            HttpServletRequest request) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是教委用户
        // 把用户的类型添加到request中去
        request.setAttribute("orgLevel", orgEntity.getOrgLevel());
        // 如果是学校
        if (orgTypeId.equals("2")) {
            long schoolorgid = orgEntity.getId();
            List<ExamresultStudent> xdList = reportLookService.queryDistinctXDFromExamresultStudent((int) schoolorgid);
            request.setAttribute("xdList", xdList);
            // List<Grade> gradeList =
            // schoolService.getGradeListInSchool(schoolorgid);
            // request.setAttribute("gradeList", gradeList);
        } else if (orgTypeId.equals("1") || orgTypeId.equals("3")) { // 如果是教委用户，3表示的是陶老师
            int orgLevel = orgEntity.getOrgLevel();
            List<GradeCode> gradeAllList = gradeCodeService.getAllGrades();
            request.setAttribute("gradeAllList", gradeAllList);
            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = orgEntity.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                List<Organization> schoolList = reportLookService
                        .getSchoolsFromExamresultStudent(orgEntity.getCountyid());
                request.setAttribute("schoollist", schoolList);
            }
        }
        // List<Scale> scaleList =
        // reportLookService.queryDistinctScales(orgEntity);
        // request.setAttribute("scaleList", scaleList);
        // List scaleTypes = this.scaleService.getScaleTypes();
        // List scaleSources = this.scaleService.getScaleSource();
        // request.setAttribute("scaleTypes", scaleTypes);
        // request.setAttribute("scaleSources", scaleSources);
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        dataManageFilterParam.setOrgid(orgEntity.getId());
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("dataexportpage");
    }

    // 搜索和分页用的导出数据的action
    @RequestMapping(value = "searchSExport", method = RequestMethod.POST)
    public String searchSExport(@CurrentOrg Organization orgEntity, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        // 如果是教委用户
        // 把用户的类型添加到request中去
        request.setAttribute("orgLevel", orgEntity.getOrgLevel());
        // 如果是学校
        if (orgTypeId.equals("2")) {
            long schoolorgid = orgEntity.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(schoolorgid);
            request.setAttribute("gradeList", gradeList);
        } else if (orgTypeId.equals("1")) { // 如果是教委用户，3表示的是陶老师
            int orgLevel = orgEntity.getOrgLevel();
            List<GradeCode> gradeAllList = gradeCodeService.getAllGrades();
            request.setAttribute("gradeAllList", gradeAllList);
            // 如果是市教委
            if (orgLevel == 3) {
                String cityId = orgEntity.getCityid();
                request.setAttribute("cityId", cityId);
            } else if (orgLevel == 4) { // 如果是县教委
                String countyId = orgEntity.getCountyid();
                List<District> districtList = districtService.getTowns(countyId);
                request.setAttribute("districtList", districtList);
            }
        }
        List<Scale> scaleList = reportLookService.queryDistinctScales(orgEntity);
        request.setAttribute("scaleList", scaleList);
        List scaleTypes = this.scaleService.getScaleTypes();
        List scaleSources = this.scaleService.getScaleSource();
        request.setAttribute("scaleTypes", scaleTypes);
        request.setAttribute("scaleSources", scaleSources);
        // 添加分页的过滤
        request.setAttribute("page", page);
        return viewName("datamanagetab");
    }

    // 搜索和分页用的导入数据的action
    @RequestMapping(value = "searchSImport1", method = RequestMethod.GET)
    public String searchSImport1(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam) {
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // 添加分页的过滤
        request.setAttribute("page", page);
        // 为页面添加下拉列表的东西
        if (orgType.equals("2")) {
            long schoolorgid = org.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(schoolorgid);
            request.setAttribute("gradeList", gradeList);
        }
        // 量表类别
        List scaleTypeList = scaleService.getScaleTypes();
        request.setAttribute("scaleTypes", scaleTypeList);
        return viewName("dateimportpage");
    }

    @RequestMapping(value = "searchSImport", method = RequestMethod.POST)
    public String searchSImport(@CurrentOrg Organization org,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam,
            HttpServletRequest request, @PageAnnotation PageParameter page) {
        // 把用户的类型添加到request中去
        dataManageFilterParam.setOrgid(org.getId());
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // DataManageFilterParam dataManageFilterParam = new
        // DataManageFilterParam();
        // 添加分页的过滤
        request.setAttribute("page", page);
        // 为页面添加下拉列表的东西
        /*
         * if (orgType.equals("2")) { long schoolorgid = org.getId();
         * List<Grade> gradeList = schoolService
         * .getGradeListInSchool(schoolorgid); request.setAttribute("gradeList",
         * gradeList); }
         */
        // 量表类别
        // List scaleTypeList = scaleService.getScaleTypes();
        // request.setAttribute("scaleTypes", scaleTypeList);
        // List<Scale> scaleList =
        // examdoService.queryStudentDistinctScales(org);
        // request.setAttribute("scaleList", scaleList);
        List examdoList = examdoService.queryStudentExamdo(page, dataManageFilterParam);
        request.setAttribute("examdoList", examdoList);
        return viewName("dateimportpagetableCon");
    }

    @RequestMapping(value = "/exportstuanswerinsch", method = RequestMethod.POST)
    public void exportStuAnswerInSchool(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

        // String type = request.getParameter("type");
        String scaleid = request.getParameter("scaleId");
        String startTime = request.getParameter("testStartTime");
        String endTime = request.getParameter("testEndTime");
        String[] attrIds = request.getParameterValues("attrIds");
        String orgid, nj, bj, xd;
        String njmc, bjmc;
        orgid = String.valueOf(orgEntity.getId());
        Scale scale = cachedScaleMgr.get(scaleid);
        String title = scale.getTitle();
        title = title + "测评数据.xls";
        String filename = FileOperate.encodeFilename(title, request);
        // Map<String, Object> paramets = new HashMap<String, Object>();
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                if (ScaleUtils.isThirdAngleScaleP(scaleid))
                    filename = FileOperate.encodeFilename("小学生心理健康量表测评数据.zip", request);
                if (ScaleUtils.isThirdAngleScaleM(scaleid))
                    filename = FileOperate.encodeFilename("中学生心理健康量表测评数据.zip", request);
                response.setContentType("application/x-msdownload;charset=UTF-8");
            } else
                response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();

            njmc = request.getParameter("njmc");
            bjmc = request.getParameter("bjmc");
            xd = request.getParameter("xd");
            nj = request.getParameter("nj");
            bj = request.getParameter("bj");
            String[] bjArray = null;
            String bjarrayJson = request.getParameter("bjarray");
            if (!StringUtils.isEmpty(bjarrayJson)) {
                JSONArray jsonArray = JSONArray.fromObject(bjarrayJson);
                bjArray = new String[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object o = jsonArray.get(i);
                    bjArray[i] = o.toString();
                }

            }
            String[] headnames = { "学号", "姓名", "性别", "年级", "班级" };
            String[] headfields = { "xh", "xm", "xbm", "njmc", "bjmc" };
            scaleService.downloadStuAnswerForSch(orgid, xd, nj, bjArray, scaleid, headnames, headfields, attrIds,
                    startTime, endTime, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * 导出所有量表所有测试数据
     * @param orgEntity
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/exportstuallanswerinsch", method = RequestMethod.POST)
    public void exportstuallanswerinsch(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
    	String startTime = request.getParameter("testStartTime");
    	String endTime = request.getParameter("testEndTime");
    	//获得当前教委下所有学校
    	List<School> schools = schoolService.getSchoolsAccordingOrgId(String.valueOf(orgEntity.getId()));
    	//根据本校学生下载数据
    	if (schools.size() > 0) {
    		List<String> schoolList = new ArrayList<>();
    		for (School school : schools) {
    			schoolList.add(String.valueOf(school.getId()));
    		}
			String title = "所有学生测评数据.xls";
			String filename = FileOperate.encodeFilename(title, request);
			//获得当前学校所有的测评数据
			StatScope scope = new StatScope();
			scope.setSchoolId(schoolList);
			scope.setStartTime(startTime);
			scope.setEndTime(endTime);
			//所有测试数据
			List<ExamresultHuman> studentResult = examdoService.getStudentResult(scope);
			//获得所有量表
			String[] headnames = {"校名", "量表名", "学号", "姓名", "性别", "年级", "班级" };
            String[] headfields = { "xxmc", "scalename", "xh", "xm", "xbm", "njmc", "bjmc" };
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = null;
			try {
				outputStream = response.getOutputStream();
				examAnswerService.downloadAllStudentResult(headnames, headfields,
						startTime, endTime, outputStream);
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("没有学校测试数据");
		}
        int orglevel = orgEntity.getOrgLevel().intValue();
        String type = request.getParameter("type");
        String scaleid = request.getParameter("scaleId");
        
        
        String[] attrIds = request.getParameterValues("attrIds");
        String teacherRole, orgid, gradeid, bj;
        Scale scale = cachedScaleMgr.get(scaleid);
        String title = scale.getTitle();
        title = title + "测评数据.xls";
        String filename = FileOperate.encodeFilename(title, request);
        // Map<String, Object> paramets = new HashMap<String, Object>();
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                if (ScaleUtils.isThirdAngleScaleP(scaleid))
                    filename = FileOperate.encodeFilename("小学生心理健康量表测评数据.zip", request);
                if (ScaleUtils.isThirdAngleScaleM(scaleid))
                    filename = FileOperate.encodeFilename("中学生心理健康量表测评数据.zip", request);
                response.setContentType("application/x-msdownload;charset=UTF-8");
            } else
                response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            String xd = request.getParameter("xd");
            String nj = request.getParameter("nj");
            String[] headnames = { "学号", "姓名", "性别", "年级", "班级" };
            String[] headfields = { "xh", "xm", "xbm", "njmc", "bjmc" };
            if (orglevel == 4) {
                String schoolid = request.getParameter("qx_or_xx");//
                examAnswerService.downloadStuAnswerForSch(schoolid, xd, nj, null, scaleid, headnames, headfields,
                        attrIds, startTime, endTime, outputStream);
                // scaleService.downloadStuAnswerForCountyEdu(schoolid,gradeid,
                // scaleid, attrnames, attrfields,attrIds, startTime,
                // endTime,outputStream);
            }
            if (orglevel == 3) {
                String xzxs = request.getParameter("xzxs");
                if (xzxs.equals("1")) {
                    String countyid = request.getParameter("qx_or_xx");
                    examAnswerService.downloadStuAnswerForCityEdu(countyid, xd, nj, scaleid, headnames, headfields,
                            attrIds, startTime, endTime, outputStream);
                }
                if (xzxs.equals("2")) {
                    String schoolid = request.getParameter("qx_or_xx");//
                    examAnswerService.downloadStuAnswerForSch(schoolid, xd, nj, null, scaleid, headnames, headfields,
                            attrIds, startTime, endTime, outputStream);
                }
            }
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @RequestMapping(value = "/exportstuanswerinedu", method = RequestMethod.POST)
    public void exportstuanswerinedu(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        int orglevel = orgEntity.getOrgLevel().intValue();
        String type = request.getParameter("type");
        String scaleid = request.getParameter("scaleId");
        String startTime = request.getParameter("testStartTime");
        String endTime = request.getParameter("testEndTime");
        String[] attrIds = request.getParameterValues("attrIds");
        String teacherRole, orgid, gradeid, bj;
        Scale scale = cachedScaleMgr.get(scaleid);
        String title = scale.getTitle();
        title = title + "测评数据.xls";
        String filename = FileOperate.encodeFilename(title, request);
        // Map<String, Object> paramets = new HashMap<String, Object>();
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                if (ScaleUtils.isThirdAngleScaleP(scaleid))
                    filename = FileOperate.encodeFilename("小学生心理健康量表测评数据.zip", request);
                if (ScaleUtils.isThirdAngleScaleM(scaleid))
                    filename = FileOperate.encodeFilename("中学生心理健康量表测评数据.zip", request);
                response.setContentType("application/x-msdownload;charset=UTF-8");
            } else
                response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            String xd = request.getParameter("xd");
            String nj = request.getParameter("nj");
            String[] headnames = { "学号", "姓名", "性别", "年级", "班级" };
            String[] headfields = { "xh", "xm", "xbm", "njmc", "bjmc" };
            if (orglevel == 4) {
                String schoolid = request.getParameter("qx_or_xx");//
                examAnswerService.downloadStuAnswerForSch(schoolid, xd, nj, null, scaleid, headnames, headfields,
                        attrIds, startTime, endTime, outputStream);
                // scaleService.downloadStuAnswerForCountyEdu(schoolid,gradeid,
                // scaleid, attrnames, attrfields,attrIds, startTime,
                // endTime,outputStream);
            }
            if (orglevel == 3) {
                String xzxs = request.getParameter("xzxs");
                if (xzxs.equals("1")) {
                    String countyid = request.getParameter("qx_or_xx");
                    examAnswerService.downloadStuAnswerForCityEdu(countyid, xd, nj, scaleid, headnames, headfields,
                            attrIds, startTime, endTime, outputStream);
                }
                if (xzxs.equals("2")) {
                    String schoolid = request.getParameter("qx_or_xx");//
                    examAnswerService.downloadStuAnswerForSch(schoolid, xd, nj, null, scaleid, headnames, headfields,
                            attrIds, startTime, endTime, outputStream);
                }
            }
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * @RequestMapping(value = "/exporteaanswerinedu", method =
     * RequestMethod.GET) public void exportTeaAnswerInEdu(@CurrentOrg
     * Organization orgEntity, HttpServletRequest request, HttpServletResponse
     * response, Model model) { int orglevel =
     * orgEntity.getOrgLevel().intValue(); String scaleid =
     * request.getParameter("scaleid"); String roleid =
     * request.getParameter("roleid"); String rolename =
     * request.getParameter("rolename"); String startTime =
     * request.getParameter("starttime"); String endTime =
     * request.getParameter("endtime"); String[] attrIds =
     * request.getParameterValues("attrIds"); String[] areaids =
     * request.getParameterValues("areaids"); List arealist =
     * Arrays.asList(areaids); String teacherRole, orgid; String njmc, bjmc;
     * Scale scale = cachedScaleMgr.get(scaleid); String title =
     * scale.getTitle(); title = "《" + title + "》" + "测评数据.xls"; String filename
     * = FileOperate.encodeFilename(title, request); // Map<String, Object>
     * paramets = new HashMap<String, Object>(); try {
     * response.setContentType("application/vnd.ms-excel");
     * response.setHeader("Content-disposition", "attachment;filename=" +
     * filename); OutputStream outputStream = response.getOutputStream();
     * String[] attrnames = { "身份证号", "姓名", "性别", "角色名称" }; String[] attrfields
     * = { "sfzjh", "xm", "xbm", "roleid" }; //
     * scaleService.downloadStuAnswerForSch(njmc, bjmc, nj, bj, //
     * scaleid,attrnames,attrfields, startTime, endTime, outputStream);
     * scaleService.downloadTchAnswerForEdu(orglevel,roleid, rolename, arealist,
     * scaleid, attrnames, attrfields, attrIds,startTime, endTime,
     * outputStream); outputStream.flush(); outputStream.close();
     * 
     * } catch (Exception e) { System.out.println(e.getMessage()); } }
     */
    @RequestMapping(value = "batchSubmit", method = RequestMethod.POST)
    public String batchSubmit(@CurrentOrg Organization org, HttpServletRequest request) {
        DataManageFilterParam dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        dataManageFilterParam.setOrgid(org.getId());
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        String orgType = ot.getId();
        request.setAttribute("orgType", orgType);
        // 为页面添加下拉列表的东西
        if (orgType.equals("2")) {
            long schoolorgid = org.getId();
            List<Grade> gradeList = schoolService.getGradeListInSchool(schoolorgid);
            request.setAttribute("gradeList", gradeList);
        }
        // List<Scale> scaleList = reportLookService.queryDistinctScales(org);
        List<Scale> scaleList = examdoService.queryStudentDistinctScales(org);
        request.setAttribute("scaleList", scaleList);
        List scaleTypes = this.scaleService.getScaleTypes();
        List scaleSources = this.scaleService.getScaleSource();
        request.setAttribute("scaleTypes", scaleTypes);
        request.setAttribute("scaleSources", scaleSources);
        List examdoList = examdoService.queryStudentExamdo(dataManageFilterParam);
        request.setAttribute("examdoList", examdoList);
        return viewName("batchimportstudentanswer");
    }

    /*
     * @RequestMapping(value = "batchSubmit", method = RequestMethod.POST)
     * public String batchSubmit(@CurrentOrg Organization org,
     * HttpServletRequest request) { OrganizationType ot =
     * org.returnOrgTypeEnum(); String orgType = ot.getId();
     * request.setAttribute("orgType", orgType); // 把用户的类型添加到request中去 if
     * (orgType.equals("2")) { long schoolorgid = org.getId(); List<Grade>
     * gradeList = schoolService .getGradeListInSchool(schoolorgid);
     * request.setAttribute("gradeList", gradeList); } // 量表类别 List scaleTypes =
     * this.scaleService.getScaleTypes(); List scaleSources =
     * this.scaleService.getScaleSource(); request.setAttribute("scaleTypes",
     * scaleTypes); request.setAttribute("scaleSources", scaleSources); return
     * viewName("batchimportstudentanswer"); }
     */
    @RequestMapping(value = "/showSelectStudentAttrForm", method = RequestMethod.POST)
    public String showSelectAttrForm(@CurrentOrg Organization org, HttpServletRequest request) {
        PropObject propObject = null;
        propObject = PropObject.createPropObject(AcountTypeFlag.student.getId());
        propObject.loadMetas();
        List<FieldValue> list = propObject.getAttrs();
        request.setAttribute("attr_list", list);
        return viewName("attrform");
    }

}
