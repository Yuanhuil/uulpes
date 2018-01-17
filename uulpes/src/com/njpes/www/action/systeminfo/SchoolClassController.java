package com.njpes.www.action.systeminfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.SchoolBXLX;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Major;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.baseinfo.MajorServiceImpl;
import com.njpes.www.service.baseinfo.SchoolClassImportServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolClassServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;

import edutec.admin.ExportUtils;
import heracles.web.config.ApplicationConfiguration;

/**
 * 组织机构控制器
 * 
 * @author 赵忠诚
 */
@Controller
@RequestMapping(value = "/systeminfo/sys/organization/schoolclass")
public class SchoolClassController extends BaseController {
    @Autowired
    private SchoolClassServiceI schoolClassService;

    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private MajorServiceImpl majorService;
    @Autowired
    private SchoolClassImportServiceI scimortService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, Model model, @PageAnnotation PageParameter page) {
        if (!StringUtils.equals(org.getOrgType(), OrganizationType.school.getId())) {
            return redirectToUrl("/noAuth.jsp");
        }
        long schoolorgid = org.getId();
        ClassSchool entity = new ClassSchool();
        entity.setXxorgid(schoolorgid);
        entity.setGradeid(0);
        List<ClassSchool> classes = schoolService.getClassByGradeidInSchoolByPage(entity, page, 0);
        List<Grade> gradelist = schoolService.getGradeListInSchool(schoolorgid);
        List<Major> selectAllMajors = majorService.selectAllMajors();
        model.addAttribute("selectAllMajors", selectAllMajors);
        model.addAttribute("entity", entity);
        model.addAttribute("list", classes);
        model.addAttribute("gradelist", gradelist);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = "querydata")
    public String getData(@CurrentOrg Organization org, ClassSchool entity, @PageAnnotation PageParameter page,
            Model model) {
        long schoolorgid = org.getId();
        entity.setXxorgid(schoolorgid);
        model.addAttribute("list", schoolService.getClassByGradeidInSchoolByPage(entity, page, 0));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "add")
    public String add(@CurrentOrg Organization org, ClassSchool entity, Model model) {
        long schoolorgid = org.getId();
        model.addAttribute("entity", entity);
        List<Grade> gradelist = schoolService.getGradeListInSchool(schoolorgid);
        List<Dictionary> bjlxm = DictionaryService.selectAllDic("dic_school_bjlxm");
        List<Major> selectAllMajors = majorService.selectAllMajors();
        List<Teacher> teacherlist = teacherService.getTeacherInSchool(schoolorgid, 23);//23 班主任
        model.addAttribute("selectAllMajors", selectAllMajors);
        model.addAttribute("bjlxmlist", bjlxm);
        model.addAttribute("gradelist", gradelist);
        model.addAttribute("teacherlist", teacherlist);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "create")
    public String create(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            ClassSchool entity, Model model) {
        entity.setXxorgid(org.getId());
        entity.setXxdm(org.getCode());
        schoolClassService.insert(entity);
        logservice.log(request, "基础信息中心:班级管理", "新建班级:" + entity.getBjmc());
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/view", method = RequestMethod.GET)
    public String view(@CurrentOrg Organization org, ClassSchool entity, Model model) {
        ClassSchool showentity = schoolClassService.findById(entity.getId());
        long schoolorgid = org.getId();
        List<Grade> gradelist = schoolService.getGradeListInSchool(schoolorgid);
        model.addAttribute("gradelist", gradelist);
        model.addAttribute("entity", showentity);
        List<Dictionary> bjlxm = DictionaryService.selectAllDic("dic_school_bjlxm");
        model.addAttribute("bjlxmlist", bjlxm);
        List<Teacher> teacherlist = teacherService.getTeacherInSchool(schoolorgid, 0);
        model.addAttribute("teacherlist", teacherlist);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String update(HttpServletRequest request, @CurrentOrg Organization org, ClassSchool entity, Model model) {
        ClassSchool showentity = schoolClassService.findById(entity.getId());
        long schoolorgid = org.getId();
        model.addAttribute("xxorgid", entity.getXxorgid());
        List<Grade> gradelist = schoolService.getGradeListInSchool(schoolorgid);
        model.addAttribute("gradelist", gradelist);
        model.addAttribute("entity", showentity);
        List<Dictionary> bjlxm = DictionaryService.selectAllDic("dic_school_bjlxm");
        model.addAttribute("bjlxmlist", bjlxm);
        List<Teacher> teacherlist = teacherService.getTeacherInSchool(schoolorgid, 0);
        model.addAttribute("teacherlist", teacherlist);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);

        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String formupdate(HttpServletRequest request, @CurrentOrg Organization org, ClassSchool entity,
            Model model) {
        entity.setXxorgid(org.getId());
        entity.setXxdm(org.getCode());
        schoolClassService.update(entity);
        logservice.log(request, "基础信息中心:班级管理", "修改班级信息:" + entity.getBjmc());
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/delete")
    public String delete(ClassSchool entity, Model model ,HttpServletResponse response) throws Exception {
    	int count = studentService.getStudentCountByClassid(entity.getId());
    	//当count大于0时，则表示当前班级内还有学生，不能删除
    	if (count != 0) {
    		   response.setContentType("text/html; charset=UTF-8"); //转码
			   PrintWriter out = response.getWriter();
			   out.flush();
			   out.println("<script>");
			   out.println("alert('请清空班级内学生再执行删除');");
			   out.println("history.back();");
			   out.println("</script>");
			   return redirectToUrl(viewName("list.do"));
		}
    	schoolClassService.del(entity);
    	return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "delselected")
    public String delselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck,HttpServletResponse response) throws Exception {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
            	int count = studentService.getStudentCountByClassid(id);
            	if (count != 0) {
            		response.setContentType("text/html; charset=UTF-8"); //转码
        		    PrintWriter out = response.getWriter();
        		    out.flush();
        		    out.println("<script>");
        		    out.println("alert('请清空班级内学生再执行删除');");
        		    out.println("history.back();");
        		    out.println("</script>");
        		    return redirectToUrl(viewName("list.do"));
				}
                ClassSchool ec = schoolClassService.findById(id);
                schoolClassService.del(ec);
            }
        }
        logservice.log(request, "基础信息中心:班级管理", "删除班级");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "redirectToImportClass", method = RequestMethod.GET)
    public String redirectToImportSchool() {
        return viewName("import");
    }

    @RequestMapping(value = "downloadClasstemplate", method = RequestMethod.GET)
    public String downloadClasstemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("班级基本信息导入模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/班级基本信息导入模板.xlsx");
            InputStream in = new BufferedInputStream(new FileInputStream(fullFileName));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;

            while ((i = in.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = { "", "import" }, method = RequestMethod.POST)
    @ResponseBody
    public String imports(@CurrentOrg Organization org, HttpServletRequest request) throws Exception {
        if (!StringUtils.equals(org.getOrgType(), OrganizationType.school.getId())) {
            return redirectToUrl("/noAuth.jsp");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            FileOperate.saveFile(saveFilePath, fileName, mf);
            scimortService.importSchoolClass(saveFilePath + "\\" + fileName, org);
            logservice.log(request, "基础信息中心:班级管理", "导入班级");
        }
        return "";
    }

    @RequestMapping(value = "schoolclassupgrade")
    public String schoolclassupgrade(@CurrentOrg Organization org, Model model) {
        long schoolorgid = org.getId();
        return viewName("upgrade");
    }

    @RequestMapping(value = "upgrade", method = RequestMethod.POST)
    @ResponseBody
    public String upgrade(@CurrentOrg Organization org, Model model) throws Exception {
        long schoolorgid = org.getId();
        School school = schoolService.getSchoolInfoByOrgId(schoolorgid);

        String xxbxlxm = school.getXxbxlxm();
        int xxxz = 0;
        int byb_gradeid = 0;
        if (school.isPrimaryschool()) {
            xxxz = school.getXxxz().intValue();
            if (xxxz == 5)
                byb_gradeid = 5;
            else
                byb_gradeid = 6;
        } else if (xxbxlxm.equals(SchoolBXLX.junior_school_312.getId())) {// 九年一贯制
            xxxz = school.getXxxz().intValue();
            if (xxxz == 5)
                byb_gradeid = 10;
            else
                byb_gradeid = 9;

        } else if (xxbxlxm.equals(SchoolBXLX.senior_school_345.getId())) {// 十二年一贯制
            xxxz = school.getXxxz().intValue();
            byb_gradeid = 13;

        } else if (xxbxlxm.equals(SchoolBXLX.senior_school_341.getId())) {// 完全中学
            byb_gradeid = 13;
        } else if (school.isJuniorHighschool()) {// 初中
            byb_gradeid = 9;
        } else if (school.isSeniorHighschool()) {// 高中
            byb_gradeid = 13;
        }
        int cn = schoolClassService.upgrade(schoolorgid, xxxz, byb_gradeid, xxbxlxm);
        if (cn != -1)
            return "success";
        else
            return "failed";
    }
}
