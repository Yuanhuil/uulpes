package com.njpes.www.action.systeminfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

//import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.EcUserWithBLOBs;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.UserResPerm;
import com.njpes.www.entity.baseinfo.attr.AttrDefine;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrgLevel;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.AuthServiceI;
import com.njpes.www.service.baseinfo.ClassServiceI;
import com.njpes.www.service.baseinfo.EcUserServiceI;
import com.njpes.www.service.baseinfo.EcuserImportServiceI;
import com.njpes.www.service.baseinfo.PasswordService;
import com.njpes.www.service.baseinfo.ResourceServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.StudentImportService;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherImportServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.UserResPermServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;

import edutec.admin.ExportUtils;
import heracles.web.config.ApplicationConfiguration;

/**
 * 用户管理控制器 此功能只有管理员才可以访问
 *
 * @author 赵忠诚
 */
@Controller
@RequestMapping(value = "/systeminfo/sys/user")
public class UserController extends BaseController {
    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private AccountServiceI accountService;

    @Autowired
    private SchoolServiceI schoolService;

    @Autowired
    private StudentServiceI studentService;

    @Autowired
    private TeacherServiceI teacherService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    private EcUserServiceI ecuserService;

    @Autowired
    private StudentImportService stuImportService;

    @Autowired
    private TeacherImportServiceI teacherImportService;

    @Autowired
    EcuserImportServiceI ecuserImportService;

    @Autowired
    AuthServiceI authService;

    @Autowired
    ResourceServiceI resourceService;

    @Autowired
    UserResPermServiceI userResPermService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    private SyslogServiceI logservice;
    @Autowired
    private ClassServiceI classService;

    public UserController() {
        setResourceIdentity("systeminfo:user");
    }

    @RequestMapping(value = "usercheck")
    @ResponseBody
    public Object userCheck(HttpServletRequest request) {
        String fieldId = request.getParameter("fieldId");
        String fieldValue = request.getParameter("fieldValue");
        Boolean ch = accountService.checkUser(fieldValue);
        Object[] rs = new Object[3];
        rs[0] = fieldId;
        rs[1] = !ch;
        if (ch.booleanValue())
            rs[2] = "已经存在系统身份证号或者用户名";
        return JSON.toJSON(rs);
    }

    @RequestMapping(value = "StudentXjhcheck")
    @ResponseBody
    public Object StudentXjhcheck(HttpServletRequest request,@CurrentOrg Organization orgEntity){
        String fieldId = request.getParameter("fieldId");
        String fieldValue = request.getParameter("fieldValue");
        long schoolorgid=orgEntity.getId();
        Boolean ch = studentService.getStudentsXjh(schoolorgid,fieldValue);
        Object[] rs = new Object[3];
        rs[0] = fieldId;
        rs[1] = !ch;
        if (ch.booleanValue())
            rs[2] = "已经存在此学籍号，请输入正确的学籍号";
        return JSON.toJSON(rs);
    }

    @RequestMapping(value = "student", method = RequestMethod.GET)
    public String studentList(HttpServletRequest request, @CurrentOrg Organization orgEntity, Student stu, Model model,
            @PageAnnotation PageParameter page) {
        // 如果当前用户没有管理员权限，则不能查看该信息，需要判断给用户的授权
        // need shiro sooning

        // 如果程序可以执行到这里说明该用户是具有管理员权限的，查看当前用户的组织机构代码
        // 此时的组织机构就是学校代码
        // if(!StringUtils.equals(orgEntity.getOrgType(),
        // OrganizationType.school.getId())){
        // return viewName("noAuth");
        // }
        Account account = (Account)request.getSession().getAttribute("user");
        long schoolorgid = orgEntity.getId().longValue();
        List<XueDuan> xdlist = this.schoolService.getXueDuanInSchool(schoolorgid);
        if ((xdlist != null) && (xdlist.size() == 1))
        {
          HashMap<XueDuan, List<Grade>> xdNjMap = this.schoolService.getGradesInSchool(schoolorgid);
          model.addAttribute("njlist", xdNjMap.get(xdlist.get(0)));
          stu.setXd(((XueDuan)xdlist.get(0)).getId());
        }
        List<Dictionary> sexlist = this.DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute("xdlist", xdlist);
        
        List<ClassSchool> selectByBzrAccountid = classService.selectByBzrAccountid(account.getId().longValue());
        if (selectByBzrAccountid.size() > 0) {
          stu.setBjid(((ClassSchool)selectByBzrAccountid.get(0)).getId().longValue());
        }
        model.addAttribute("entity", stu);
        model.addAttribute("list", this.studentService.getStudentsByPage(stu, schoolorgid, page, 0));
        model.addAttribute("page", page);
        return viewName("student/list");

    }

    @RequestMapping(value = "student/search", method = RequestMethod.POST)
    public String studentSearch(HttpServletRequest request,@CurrentOrg Organization orgEntity, Student stu, Model model,
            @PageAnnotation PageParameter page) {
        Account account = (Account)request.getSession().getAttribute("user");
        
        List<ClassSchool> selectByBzrAccountid = this.classService.selectByBzrAccountid(account.getId().longValue());
        if (selectByBzrAccountid.size() > 0) {
          stu.setBjid(((ClassSchool)selectByBzrAccountid.get(0)).getId().longValue());
        }
        long schoolorgid = orgEntity.getId().longValue();
        List<StudentWithBLOBs> list = studentService.getStudentsByPage(stu, schoolorgid, page, 0);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("student/tablelist");

    }

    @RequestMapping(value = "student/recordSearch", method = RequestMethod.POST)
    public @ResponseBody List<Student> studentRecordSearch(@CurrentOrg Organization orgEntity, int xd,String nj,String sbjid,long id,
            @PageAnnotation PageParameter page) {
        long bjid =  Long.parseLong(sbjid);
        Student stu = new Student();
        stu.setId(id);
        stu.setXd(xd);
        stu.setNj(nj);
        stu.setBjid(bjid);
        long schoolorgid = orgEntity.getId();
        return studentService.getStudentRecordAll(stu, schoolorgid, 0);
    }

    @RequestMapping(value = "student/getGrades", method = RequestMethod.POST)
    @ResponseBody
    public List<Grade> getGrades(@CurrentOrg Organization orgEntity, int xd, HttpServletRequest request) {
        long schoolorgid = orgEntity.getId();
        /*
         * if (orgEntity.getOrgType().equals("2")) schoolorgid =
         * orgEntity.getId(); else schoolorgid =
         * Long.parseLong(request.getParameter("schoolid"));
         */
        return schoolService.getGradesInSchool(schoolorgid, XueDuan.valueOf(xd));
    }

    @RequestMapping(value = "student/getClasses", method = RequestMethod.POST)
    @ResponseBody
    public List<ClassSchool> getClasses(@CurrentOrg Organization orgEntity, int xd, String nj) {
        long schoolorgid = orgEntity.getId();
        return schoolService.getClassByGradeInSchool(schoolorgid, XueDuan.valueOf(xd), nj, 0);
    }

    @RequestMapping(value = "student/getXd", method = RequestMethod.POST)
    @ResponseBody
    public List getXd(@CurrentOrg Organization orgEntity,HttpServletRequest request) {
        long schoolorgidRecord = orgEntity.getId();
        String schoolorgid = request.getParameter("schoolid");
        List<XueDuan> xdList = new ArrayList<XueDuan>();
        if(schoolorgid != null){
            xdList = schoolService.getXueDuanInSchool(Long.parseLong(schoolorgid));
        }else{
            xdList = schoolService.getXueDuanInSchool(schoolorgidRecord);
        }
        List xdidList = new ArrayList<Integer>();
        for (int i = 0; i < xdList.size(); i++) {
            XueDuan xd = xdList.get(i);
            xdidList.add(xd.getId());
        }
        return xdidList;
    }

    @RequestMapping(value = "student/{xd}/{nj}/gotochangeclass", method = RequestMethod.GET)
    public String gotochangeclass(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model,
            @PathVariable("xd") int xd, @PathVariable("nj") String nj) {
        long schoolorgid = orgEntity.getId();
        // return schoolService.getClassByGradeInSchool(schoolorgid,
        // XueDuan.valueOf(xd),nj);
        List<ClassSchool> classList = schoolService.getClassByGradeInSchool(schoolorgid, XueDuan.valueOf(xd), nj, 0);
        model.addAttribute("classList", classList);
        return viewName("student/changeclass");
    }

    // 史斌增加导入学生数据
    @RequestMapping(value = "student/import", method = RequestMethod.POST)
    @ResponseBody
    public String imports(@CurrentOrg Organization org, HttpServletRequest request, String excelUrl) throws Exception {
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // tmpScaleDir = "picScale"+System.currentTimeMillis();
            FileOperate.saveFile(saveFilePath, fileName, mf);
            // InputStream inputStream = mf.getInputStream();
            stuImportService.importStuinfo(saveFilePath + "\\" + fileName, org);

            logservice.log(org, userInfo, "基础信息中心:人员管理:学生用户管理", "导入学生信息");
        }
        return "";
    }

    @RequestMapping(value = "student/downloadstutemplate", method = RequestMethod.GET)
    public String downloadstutemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("学生基本信息导入模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/学生基本信息导入模板.xlsx");
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

    @RequestMapping(value = "student/add")
    public String studentAdd(@CurrentOrg Organization orgEntity, Model model) {
        model.addAttribute("entity", new StudentWithBLOBs());
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        model.addAttribute("gradeList", gradeList);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("student/editform");

    }

    @RequestMapping(value = "student/create")
    public String studentCreate(HttpServletRequest request, @CurrentOrg Organization orgEntity,
            StudentWithBLOBs student) {
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        String sfzjh=student.getSfzjh();
        String csrq=sfzjh.substring(6,sfzjh.length()-4);
        student.setCsrq(csrq);
        long orgid = orgEntity.getId();
        student.setOrgid(orgid);
        studentService.insert(student);
        logservice.log(orgEntity, userInfo, "基础信息中心:人员管理:学生用户管理", "添加学生成功");
        return redirectToUrl(viewName("student.do"));
    }

    @RequestMapping(value = "student/{id}/view")
    public String studentView(@CurrentOrg Organization orgEntity, @PathVariable("id") long id, Model model) {
        StudentWithBLOBs entity = studentService.selectStudentInfoById(id);
        entity.setClassid(String.valueOf(entity.getBjid()));
        entity.setGradeid(String.valueOf(entity.getGradecodeid()));
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        model.addAttribute("entity", entity);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        model.addAttribute("gradeList", gradeList);
        List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(orgEntity.getId(),
                entity.getGradecodeid(), 0);
        model.addAttribute("classList", classList);
        return viewName("student/editform");
    }

    @RequestMapping(value = "student/{id}/update", method = RequestMethod.GET)
    public String studentUpdateView(@CurrentOrg Organization orgEntity, @PathVariable("id") long id, Model model) {
        StudentWithBLOBs entity = studentService.selectStudentInfoById(id);
        entity.setGradeid(String.valueOf(entity.getGradecodeid()));
        entity.setClassid(String.valueOf(entity.getBjid()));
        List<Grade> gradeList = schoolService.getGradeListInSchool(orgEntity.getId());
        model.addAttribute("gradeList", gradeList);
        List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(orgEntity.getId(),
                entity.getGradecodeid(), 0);
        model.addAttribute("classList", classList);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        model.addAttribute("entity", entity);

        return viewName("student/editform");
    }

    @RequestMapping(value = "student/{id}/update", method = RequestMethod.POST)
    public String studentUpdate(HttpServletRequest request, StudentWithBLOBs student, Model model) {
        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        studentService.update(student);
        request.getSession().setAttribute("action", "更新学生信息成功");
        logservice.log(org, userInfo, "基础信息中心:人员管理:学生用户管理", "更新学生信息成功");
        return redirectToUrl(viewName("student.do"));
    }

    @RequestMapping(value = "student/{id}/delete")
    public String deletestudent(HttpServletRequest request, @CurrentOrg Organization orgEntity, Student student,
            Model model) {
        studentService.deleteByPrimaryKey(student);
        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:学生用户管理", "删除学生信息成功");
        return redirectToUrl(viewName("student.do"));
    }

    @RequestMapping(value = "student/resetstudentpasswd", method = RequestMethod.POST)
    @ResponseBody
    public String resetStudentPasswd(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        StudentWithBLOBs student = studentService.selectStudentInfoById(id);
        String defaultpasswd = passwordService.encryptPassword(student.getSfzjh(), Constants.DEFAULT_PASSWORD);
        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:学生用户管理", "重置学生" + student.getXm() + "的登录密码");
        return accountService.passwdreset(student.getAccountId(), defaultpasswd);

    }

    /*
     * @RequestMapping(value="student/delselected") public String
     * studentdeleteselected(@RequestParam(value="rowcheck[]",required= false)
     * Long[] rowcheck){ if(rowcheck!=null && rowcheck.length >0){ for(Long id :
     * rowcheck){ Student st = studentService.selectByPrimaryKey(id);
     * studentService.deleteByPrimaryKey(st); } } return
     * redirectToUrl(viewName("student.do")); }
     */
    @RequestMapping(value = "student/delselected")
    public String studentdeleteselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        List<Student> userList = new ArrayList<Student>();
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                Student st = studentService.selectByPrimaryKey(id);
                userList.add(st);
            }
            studentService.deleteSelected(userList);

            Organization org = (Organization) request.getSession().getAttribute("user_org");
            Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
            logservice.log(org, userInfo, "基础信息中心:人员管理:学生用户管理", "批量删除学生");
        }

        return redirectToUrl(viewName("student.do"));
    }

    // 批量调整班级
    @RequestMapping(value = "student/changeclass")
    public String changeclass(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        String newclassid = request.getParameter("newclassid");
        int newbjid = Integer.parseInt(newclassid);
        List<Student> userList = new ArrayList<Student>();
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                Student st = studentService.selectByPrimaryKey(id);
                userList.add(st);
            }
            studentService.changeClass(userList, newbjid);
            Organization org = (Organization) request.getSession().getAttribute("user_org");
            Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
            logservice.log(org, userInfo, "基础信息中心:人员管理:学生用户管理", "调整学生所属班级");
        }

        return redirectToUrl(viewName("student.do"));
    }

    @RequestMapping(value = "student/redirectToImportStudent", method = RequestMethod.GET)
    public String redirectToImportStudent() {
        return viewName("student/studentimport");
    }

    @RequestMapping(value = "student/{id}/editStudentBjxx/{flag}", method = RequestMethod.GET)
    public String editStudentBjxx(@CurrentOrg Organization orgEntity, @PathVariable("id") long id,
            @PathVariable("flag") String flag, HttpServletRequest request, Model model) {
        StudentWithBLOBs student = studentService.selectStudentInfoById(id);
        // PropObject propObject = null;
        // propObject =
        // PropObject.createPropObject(AcountTypeFlag.student.getId());
        student.loadMetas();
        // propObject.loadMetas();
        // List<FieldValue> list = student.getAttrs();
        // request.setAttribute("attr_list", list);
        String bjxx = student.getBjxx();// "102=03";
        student.setBjxx(bjxx);
        student.setBackGrandByStr();
        // propObject.setBackGrand(bjxx);
        Map<String, String> map = new HashMap<String, String>();
        student.loadBackGrandToMap(map);
        student.load();
        List<FieldValue> list = student.getAttrs();
        request.setAttribute("attr_list", list);
        // 史斌增加页面绑定对象
        model.addAttribute("student", student);
        if (StringUtils.equals(flag, "personalCenter")) {
            request.setAttribute("flag", flag);
        }
        // request.setAttribute("studentid", student.getId());
        // propObject.loadBackGrandToMap(map);
        // request.setAttribute("fieldmap",map);
        return viewName("student/studentbjinfo");
    }

    @RequestMapping(value = "student/saveStudentBjxx", method = RequestMethod.POST)
    @ResponseBody
    public String saveBjxx(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        StudentWithBLOBs student = studentService.selectStudentInfoById(id);
        // PropObject propObject = null;
        // propObject =
        // PropObject.createPropObject(AcountTypeFlag.student.getId());
        student.loadMetas();
        // propObject.loadMetas();
        List<FieldValue> list = student.getAttrs();
        StringBuilder sb = new StringBuilder();
        for (FieldValue fv : list) {
            AttrDefine af = (AttrDefine) fv.getField();
            long attrid = af.getObjectIdentifier();
            String value = request.getParameter(attrid + "");

            if (!StringUtil.isEmpty(value)) {
                fv.setValue(value);
            }
        }
        student.update(AcountTypeFlag.student.getId());
        // List<FieldValue> list = student.getAttrs();
        // request.setAttribute("attr_list", list);
        // propObject.loadBackGrandToMap(map);
        // request.setAttribute("fieldmap",map);

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(orgEntity, userInfo, "基础信息中心:人员管理:学生用户管理", "更新学生" + student.getXm() + "背景信息");
        return "success";
    }

    @RequestMapping(value = "student/saveStudentBjxx1", method = RequestMethod.POST)
    @ResponseBody
    public String saveBjxx1(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model,
            @ModelAttribute("bjform") String fvs) {
        int id = Integer.parseInt(request.getParameter("id"));
        String fvs1 = request.getParameter("fvs");
        StudentWithBLOBs student = studentService.selectStudentInfoById(id);
        student.setBjxx(fvs1);
        // PropObject propObject = null;
        // propObject =
        // PropObject.createPropObject(AcountTypeFlag.student.getId());
        // student.loadMetas();
        // propObject.loadMetas();
        // List<FieldValue> list = student.getAttrs();
        /*
         * StringBuilder sb = new StringBuilder(); for(FieldValue fv : list){
         * AttrDefine af = (AttrDefine)fv.getField(); long attrid =
         * af.getObjectIdentifier(); String value =
         * request.getParameter(attrid+"");
         *
         * if(!StringUtil.isEmpty(value)){ //fv.setValue(value);
         * sb.append(attrid+"=" + value + " "); } }
         * student.setBjxx(sb.toString().trim());
         */
        // student.update(AcountTypeFlag.student.getId());
        studentService.update(student);
        // List<FieldValue> list = student.getAttrs();
        // request.setAttribute("attr_list", list);
        // propObject.loadBackGrandToMap(map);
        // request.setAttribute("fieldmap",map);
        return "success";
    }

    @RequestMapping(value = "teacher")
    public String teacherList(@CurrentOrg Organization orgEntity, HttpServletRequest request, TeacherQueryParam teacher,
            Model model, @PageAnnotation PageParameter page) {
        // if(!StringUtils.equals(orgEntity.getOrgType(),
        // OrganizationType.school.getId())){
        // return viewName("noAuth");
        // }
        Account account = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        Set<Role> roles = account.getRoles();
        boolean isSuperAdmin = false;
        for (Role r : roles) {
            if (r.getId().intValue() == 1) {
                isSuperAdmin = true;
                break;
            }
        }
        // 获得直属学校列表
        if (orgEntity.getOrgType().equals(OrganizationType.ec.getId())) {
            if (isSuperAdmin) {
                List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
                List<Teacher> list = teacherService.getAdminTeachersByPage(teacher, 0, -1, page);
                List<Role> roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(),
                        OrganizationType.school.getId());
                model.addAttribute("sonschoolist", sonschoolist);
                model.addAttribute("list", list);
                model.addAttribute("rolelist", roleList);
                model.addAttribute("loadFlag",true);
            } else {
                List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
                List<Teacher> list = teacherService.getAdminTeachersByPage(teacher, 0, orgEntity.getId(), page);
                List<Role> roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(),
                        OrganizationType.school.getId());
                model.addAttribute("sonschoolist", sonschoolist);
                model.addAttribute("list", list);
                model.addAttribute("rolelist", roleList);
            }
        } else {
            long schoolorgid = orgEntity.getId();
            List<Teacher> list = teacherService.getTeachersByPage(teacher, schoolorgid, page);
            model.addAttribute("list", list);
            List<Role> roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(), orgEntity.getOrgType());
            model.addAttribute("rolelist", roleList);
        }
        model.addAttribute("entity", new TeacherQueryParam());

        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute("page", page);
        return viewName("teacher/list");

    }

    @RequestMapping(value = "teacher/search")
    public String teacherSearch(@CurrentOrg Organization orgEntity, HttpServletRequest request, Long schoolorgid,
            TeacherQueryParam teacher, Model model, @PageAnnotation PageParameter page) {
        Account account = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        Set<Role> roles = account.getRoles();
        boolean isSuperAdmin = false;
        for (Role r : roles) {
            if (r.getId().intValue() == 1) {
                isSuperAdmin = true;
                break;
            }
        }
        if (orgEntity.getOrgType().equals(OrganizationType.ec.getId())) {
            if (isSuperAdmin) {
                List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
                if (schoolorgid == null)
                    schoolorgid = -1L;
                List<Teacher> list = teacherService.getAdminTeachersByPage(teacher, schoolorgid, -1, page);
                model.addAttribute("sonschoolist", sonschoolist);
                model.addAttribute("list", list);
            } else {
                List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
                if (schoolorgid == null)
                    schoolorgid = -1L;
                List<Teacher> list = teacherService.getAdminTeachersByPage(teacher, schoolorgid, orgEntity.getId(),
                        page);
                model.addAttribute("sonschoolist", sonschoolist);
                model.addAttribute("list", list);
            }
        } else {
            List<Teacher> list = teacherService.getTeachersByPage(teacher, orgEntity.getId(), page);
            model.addAttribute("list", list);
        }
        model.addAttribute("page", page);
        return viewName("teacher/tablelist");

    }

    @RequestMapping(value = "teacher/add")
    public String teacherAdd(@CurrentOrg Organization orgEntity, Model model, @CurrentUser Account user) {
        if (user.getUsername().equals("admin")) {// 如果用户是超级管理员，可以读取所有学校
            List<Role> roleList = roleService.selectAll();
            model.addAttribute("rolelist", roleList);
            List<Organization> sonschoolist = organizationService.getAllSchool(orgEntity);
            model.addAttribute("sonschoolist", sonschoolist);
        } else if (orgEntity.getOrgType().equals(OrganizationType.ec.getId())) {
            List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
            List<Role> roleList = null;
            model.addAttribute("sonschoolist", sonschoolist);
            if (orgEntity.getOrgLevel() == 3)// 市教委，获取直属学校
                roleList = roleService.selectTeacherAdmin(2);
            if (orgEntity.getOrgLevel() == 4)// 区县教委，获取基层学校
                roleList = roleService.selectTeacherAdmin(1);
            model.addAttribute("rolelist", roleList);
        } else {
            List<Role> roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(), orgEntity.getOrgType());
            model.addAttribute("rolelist", roleList);
        }
        model.addAttribute("entity", new TeacherQueryParam());
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        List<Dictionary> edulist = DictionaryService.selectAllDic("dic_education");
        model.addAttribute("edulist", edulist);
        Set<Role> roles = user.getRoles();
        boolean showcreatedog = false;
        for (Role r : roles) {
            if (r.getId().intValue() == 1) {
                showcreatedog = true;
            }
        }
        List<Dictionary> sflist = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("sflist", sflist);
        model.addAttribute("showcreatedog", showcreatedog);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("teacher/editform");

    }

    @RequestMapping(value = "teacher/create")
    public String teacherCreate(HttpServletRequest request, @CurrentOrg Organization orgEntity, Long schoolorgid,
            TeacherQueryParam teacher, Model model) {
        String roleIds = teacher.getRoleId();
        long[] roleIdArray = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(roleIds)) {
            roleIdArray = convert2longarry(roleIds.split(","));
        }
        long orgid = 0L;
        if (orgEntity.getOrgType().equals(OrganizationType.ec.getId())) {
            orgid = schoolorgid;
        } else {
            orgid = orgEntity.getId();
        }
        teacherService.insert(teacher, orgid, roleIdArray);

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(orgEntity, userInfo, "基础信息中心:人员管理:教师用户管理", "添加教师" + teacher.getXm());
        return redirectToUrl(viewName("teacher.do"));
    }

    @RequestMapping(value = "teacher/{id}/view")
    public String teacherView(@CurrentOrg Organization orgEntity, @PathVariable("id") long id, Model model) {
        TeacherQueryParam entity = teacherService.selectTeacherInfoById(id);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        model.addAttribute("entity", entity);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        // List<Role> roleList =
        // roleService.selectRolesByOrgLevel(orgEntity.getOrgLevel(),orgEntity.getOrgType(),false);
        List<Role> roleList = null;
        if (orgEntity.getOrgLevel() == 3)// 市教委，获取直属学校
            roleList = roleService.selectTeacherAdmin(2);
        else if (orgEntity.getOrgLevel() == 4)// 区县教委，获取基层学校
            roleList = roleService.selectTeacherAdmin(1);
        else
            // roleList =
            // roleService.selectRolesByOrgLevel(orgEntity.getOrgLevel(),orgEntity.getOrgType(),false);
            roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(), OrganizationType.school.getId());
        List<Dictionary> edulist = DictionaryService.selectAllDic("dic_education");
        model.addAttribute("edulist", edulist);
        model.addAttribute("rolelist", roleList);
        if (orgEntity.getOrgType().equals(OrganizationType.ec.getId())) {
            List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
            model.addAttribute("sonschoolist", sonschoolist);
        }
        List<Dictionary> sflist = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("sflist", sflist);
        return viewName("teacher/editform");
    }

    @RequestMapping(value = "teacher/{id}/update", method = RequestMethod.GET)
    public String teacherUpdateView(@CurrentOrg Organization orgEntity, @PathVariable("id") long id, Model model,
            @CurrentUser Account user) {
        TeacherQueryParam entity = teacherService.selectTeacherInfoById(id);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        model.addAttribute("entity", entity);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        List<Dictionary> edulist = DictionaryService.selectAllDic("dic_education");
        model.addAttribute("edulist", edulist);
        if (user.getUsername().equals("admin")) {// 如果用户是超级管理员，可以读取所有学校
            List<Role> roleList = roleService.selectAll();
            model.addAttribute("rolelist", roleList);
            List<Organization> sonschoolist = organizationService.getAllSchool(orgEntity);
            model.addAttribute("sonschoolist", sonschoolist);
        } else if (orgEntity.getOrgType().equals(OrganizationType.ec.getId())) {
            List<Organization> sonschoolist = organizationService.getSonSchoolList(orgEntity);
            model.addAttribute("sonschoolist", sonschoolist);
            // List<Role> roleList =
            // roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(),OrganizationType.school.getId());
            List<Role> roleList = null;
            if (orgEntity.getOrgLevel() == 3)// 市教委，获取直属学校
                roleList = roleService.selectTeacherAdmin(2);
            else if (orgEntity.getOrgLevel() == 4)// 区县教委，获取基层学校
                roleList = roleService.selectTeacherAdmin(1);
            else
                roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(), OrganizationType.school.getId());
            model.addAttribute("rolelist", roleList);
        } else {
            List<Role> roleList = roleService.selectRolesByRoleLevel(orgEntity.getOrgLevel(), orgEntity.getOrgType());
            model.addAttribute("rolelist", roleList);
        }
        Set<Role> roles = user.getRoles();
        boolean showcreatedog = false;
        for (Role r : roles) {
            if (r.getId().intValue() == 1) {
                showcreatedog = true;
            }
        }
        List<Dictionary> sflist = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("sflist", sflist);
        model.addAttribute("showcreatedog", showcreatedog);
        return viewName("teacher/editform");
    }

    @RequestMapping(value = "teacher/{id}/update", method = RequestMethod.POST)
    public String teacherUpdate(HttpServletRequest request, TeacherQueryParam teacher, Model model) {
        String roleIds = teacher.getRoleId();
        long[] roleIdArray = convert2longarry(roleIds.split(","));
        teacherService.update(teacher, roleIdArray);

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:教师用户管理", "更新教师" + teacher.getXm() + "信息成功");
        return redirectToUrl(viewName("teacher.do"));
    }

    @RequestMapping(value = "teacher/{id}/delete")
    public String deleteteacher(HttpServletRequest request, @CurrentOrg Organization orgEntity, Teacher teacher,
            Model model) {
        teacherService.deleteByPrimaryKey(teacher);
        request.getSession().setAttribute("action", "添加教师" + teacher.getXm());
        return redirectToUrl(viewName("teacher.do"));
    }

    @RequestMapping(value = "teacher/resetteacherpasswd", method = RequestMethod.POST)
    @ResponseBody
    public String resetTeacherPasswd(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        TeacherQueryParam teacher = teacherService.selectTeacherInfoById(id);
        String defaultpasswd = passwordService.encryptPassword(teacher.getSfzjh(), Constants.DEFAULT_PASSWORD);

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:教师用户管理", "重置教师" + teacher.getXm() + "的登录密码");
        return accountService.passwdreset(teacher.getAccountId(), defaultpasswd);

    }

    // @RequestMapping(value="teacher/delselected")
    // public String
    // teacherdeleteselected(@RequestParam(value="rowcheck[]",required= false)
    // Long[] rowcheck){
    // if(rowcheck!=null && rowcheck.length >0){
    // for(Long id : rowcheck){
    // Teacher ec = teacherService.selectByPrimaryKey(id);
    // teacherService.deleteByPrimaryKey(ec);
    // }
    // }
    // return redirectToUrl(viewName("teacher.do"));
    // }
    @RequestMapping(value = "teacher/delselected")
    public String teacherdeleteselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        List<Teacher> teacherList = new ArrayList<Teacher>();
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                Teacher teacher = teacherService.selectByPrimaryKey(id);
                teacherList.add(teacher);
            }
            teacherService.deleteSelected(teacherList);
        }

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:教师用户管理", "批量删除教师");
        return redirectToUrl(viewName("teacher.do"));
    }

    @RequestMapping(value = "teacher/downloadtchtemplate", method = RequestMethod.GET)
    public String downloadtchtemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("教师基本信息导入模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/教师基本信息导入模板.xlsx");
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

    // 史斌增加导入学生数据
    @RequestMapping(value = "teacher/import", method = RequestMethod.POST)
    @ResponseBody
    public String importT(HttpServletRequest request, String excelUrl, @CurrentOrg Organization org) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // tmpScaleDir = "picScale"+System.currentTimeMillis();
            FileOperate.saveFile(saveFilePath, fileName, mf);
            // InputStream inputStream = mf.getInputStream();
            teacherImportService.importTeahinfo(saveFilePath + "\\" + fileName, org);
            request.getSession().setAttribute("action", "导入教师信息");

            Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
            logservice.log(org, userInfo, "基础信息中心:人员管理:教师用户管理", "导入教师信息");
        }
        return "";
    }

    @RequestMapping(value = "teacher/{id}/editTeacherBjxx/{flag}", method = RequestMethod.GET)
    public String editTeacherBjxx(@CurrentOrg Organization orgEntity, @PathVariable("id") long id,
            @PathVariable("flag") String flag, HttpServletRequest request, Model model) {
        // TeacherWithBLOBs teacher = teacherService.selectTeacherInfoById(id);
        Teacher teacher = teacherService.selectByPrimaryKey(id);
        // PropObject propObject = null;
        // propObject =
        // PropObject.createPropObject(AcountTypeFlag.student.getId());
        teacher.loadMetas();
        // propObject.loadMetas();
        // List<FieldValue> list = student.getAttrs();
        // request.setAttribute("attr_list", list);

        String bjxx = teacher.getBjxx();// "102=03";
        teacher.setBjxx(bjxx);
        teacher.setBackGrandByStr();
        // propObject.setBackGrand(bjxx);
        // Map<String,String> map = new HashMap<String,String>();
        // teacher.loadBackGrandToMap(map);
        teacher.load();
        List<FieldValue> list = teacher.getAttrs();
        request.setAttribute("attr_list", list);
        request.setAttribute("teacherid", teacher.getId());
        if (StringUtils.equals(flag, "personalCenter")) {
            request.setAttribute("flag", flag);
        }
        // propObject.loadBackGrandToMap(map);
        // request.setAttribute("fieldmap",map);
        return viewName("teacher/teacherbjinfo");
    }

    @RequestMapping(value = "teacher/saveTeacherBjxx", method = RequestMethod.POST)
    @ResponseBody
    public String saveTeacherBjxx(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("id"));
        // TeacherWithBLOBs teacher = teacherService.selectTeacherInfoById(id);
        Teacher teacher = teacherService.selectByPrimaryKey(id);
        // PropObject propObject = null;
        // propObject =
        // PropObject.createPropObject(AcountTypeFlag.student.getId());
        teacher.loadMetas();
        List<FieldValue> list = teacher.getAttrs();
        StringBuilder sb = new StringBuilder();
        for (FieldValue fv : list) {
            AttrDefine af = (AttrDefine) fv.getField();
            long attrid = af.getObjectIdentifier();
            String value = request.getParameter(attrid + "");

            if (!StringUtil.isEmpty(value)) {
                fv.setValue(value);
            }
        }
        teacher.update(AcountTypeFlag.teacher.getId());

        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(orgEntity, userInfo, "基础信息中心:人员管理:教师用户管理", "修改教师背景信息成功");
        return "success";
    }

    @RequestMapping(value = "ecuser", method = RequestMethod.GET)
    public String ecuserList(@CurrentOrg Organization orgEntity, @CurrentUser Account user, EcUserWithBLOBs ecuser,
            Organization queryOrgEntity, Model model, @PageAnnotation PageParameter page) {
        // 如果当前用户没有管理员权限，则不能查看该信息，需要判断给用户的授权
        // need shiro sooning

        // 如果程序可以执行到这里说明该用户是具有管理员权限的，查看当前用户的组织机构代码
        // 此时的组织机构就是学校代码
        // if(!StringUtils.equals(orgEntity.getOrgType(),
        // OrganizationType.ec.getId())){
        // return viewName("noAuth");
        // }

        if (orgEntity.getOrgLevel().intValue() == (OrgLevel.province.getIdentify())) {
            queryOrgEntity.setProvinceid(orgEntity.getProvinceid());
        } else if (orgEntity.getOrgLevel().intValue() == (OrgLevel.city.getIdentify())) {
            queryOrgEntity.setProvinceid(orgEntity.getProvinceid());
            queryOrgEntity.setCityid(orgEntity.getCityid());
        } else if (orgEntity.getOrgLevel().intValue() == (OrgLevel.county.getIdentify())) {
            queryOrgEntity.setProvinceid(orgEntity.getProvinceid());
            queryOrgEntity.setCityid(orgEntity.getCityid());
            queryOrgEntity.setCountyid(orgEntity.getCountyid());
        } else if (orgEntity.getOrgLevel().intValue() == (OrgLevel.town.getIdentify())) {
            queryOrgEntity.setProvinceid(orgEntity.getProvinceid());
            queryOrgEntity.setCityid(orgEntity.getCityid());
            queryOrgEntity.setCountyid(orgEntity.getCountyid());
            queryOrgEntity.setTownid(orgEntity.getTownid());
        }
        if (!user.getAdmin().equals(1)) {
            return viewName("noAuth");
        }
        boolean flag = false;
        if (queryOrgEntity.getOrgLevel() != null
                && queryOrgEntity.getOrgLevel().intValue() == OrgLevel.school.getIdentify()) {
            flag = true;

        }
        List<OrganizationLevel> l = organizationService.selectDirectSubOrgLevel(orgEntity, true, flag);
        model.addAttribute("entity", queryOrgEntity);
        model.addAttribute("orglevelList", l);
        model.addAttribute("currorgType", orgEntity.getOrgType());
        model.addAttribute("currprov", orgEntity.getProvinceid());
        long parentorgid = orgEntity.getId();
        List<EcUserWithBLOBs> list = ecuserService.getEcUsersByParentOrgIdByPage(ecuser, parentorgid, queryOrgEntity,
                page);

        List<Role> roleList = roleService.selectRolesByRoleAndOrgLevelIncludeSonAdmin(orgEntity.getOrgLevel(),
                orgEntity.getOrgLevel(), orgEntity.getOrgType());
        model.addAttribute("rolelist", roleList);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("ecuser/list");

    }

    @RequestMapping(value = "ecuser/search")
    public String ecuserSearch(@CurrentOrg Organization orgEntity, Model model, EcUserWithBLOBs ecuser,
            Organization queryOrgEntity, @PageAnnotation PageParameter page) {
        List<EcUserWithBLOBs> list = ecuserService.getEcUsersByParentOrgIdByPage(ecuser, orgEntity.getId(),
                queryOrgEntity, page);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("ecuser/tablelist");

    }

    @RequestMapping(value = "ecuser/add")
    public String ecuserAdd(@CurrentOrg Organization orgEntity, Model model, @CurrentUser Account user) {
        model.addAttribute("entity", new EcUserWithBLOBs());
        model.addAttribute("orgid", orgEntity.getId());
        List<OrganizationLevel> l = organizationService.selectDirectSubOrgLevel(orgEntity, true, false);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        List<Dictionary> sflist = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute("sflist", sflist);
        model.addAttribute("orglevelList", l);
        Set<Role> roles = user.getRoles();
        boolean showcreatedog = false;
        for (Role r : roles) {
            if (r.getId().intValue() == 1) {
                showcreatedog = true;
            }
        }
        model.addAttribute("showcreatedog", showcreatedog);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("ecuser/editform");

    }

    @RequestMapping(value = "ecuser/getSonSubOrgs")
    @ResponseBody
    public List<Organization> getSonSubOrgs(@CurrentOrg Organization orgEntity, int orglevel) {
        if (orgEntity.getOrgLevel().intValue() == orglevel) {
            List<Organization> includeMesunOrgList = new ArrayList<Organization>();
            orgEntity.setName("本级机构");
            includeMesunOrgList.add(orgEntity);
            return includeMesunOrgList;
        } else {
            List<Organization> sunOrgList = organizationService.getSonSubOrgsList(orgEntity, orglevel);
            return sunOrgList;
        }
    }

    @RequestMapping(value = "ecuser/getroles")
    @ResponseBody
    public List<Role> getroles(@CurrentOrg Organization orgEntity, int orglevel) {
        if (orgEntity.getOrgLevel().intValue() == orglevel) {
            return roleService.selectRolesByOrgLevel(orglevel, orgEntity.getOrgType(), false);
        } else {
            return roleService.selectRolesByOrgLevel(orglevel, orgEntity.getOrgType(), true);
        }
    }

    @RequestMapping(value = "ecuser/create")
    public String ecuserCreate(HttpServletRequest request, EcUserWithBLOBs ecuser, Model model) {
        ecuserService.insert(ecuser);

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:教委用户管理", "添加教委机构人员:" + ecuser.getXm());
        return redirectToUrl(viewName("ecuser.do"));
    }

    @RequestMapping(value = "ecuser/{id}/update", method = RequestMethod.GET)
    public String ecuserUpdateView(@CurrentOrg Organization orgEntity, @PathVariable("id") long id, Model model,
            @CurrentUser Account user) {
        EcUserWithBLOBs entity = ecuserService.selectById(id);
        Account account = accountService.findOne(entity.getAccountId());
        entity.setIsdogid(account.getIsdoglongin());
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        model.addAttribute("orgid", orgEntity.getId());
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        List<OrganizationLevel> l = organizationService.selectDirectSubOrgLevel(orgEntity, true, true);
        int orglevel = entity.getOrglevel();
        if (orglevel > 0) {
            List<Organization> listorgs = getSonSubOrgs(orgEntity, orglevel);
            List<Role> roleList = getroles(orgEntity, orglevel);
            model.addAttribute("rolelist", roleList);
            model.addAttribute("sunOrgList", listorgs);
        }
        List<Dictionary> sflist = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("sflist", sflist);
        model.addAttribute("orglevelList", l);
        model.addAttribute("sexlist", sexlist);
        model.addAttribute("entity", entity);
        Set<Role> roles = user.getRoles();
        boolean showcreatedog = false;
        for (Role r : roles) {
            if (r.getId().intValue() == 1) {
                showcreatedog = true;
            }
        }
        model.addAttribute("showcreatedog", showcreatedog);
        return viewName("ecuser/editform");
    }

    @RequestMapping(value = "ecuser/{id}/update", method = RequestMethod.POST)
    public String ecuserUpdate(HttpServletRequest request, @CurrentOrg Organization orgEntity, EcUserWithBLOBs ecuser,
            Model model) {
        ecuserService.update(ecuser);

        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(orgEntity, userInfo, "基础信息中心:人员管理:教委用户管理", "更新教委人员" + ecuser.getXm() + "信息");
        return redirectToUrl(viewName("ecuser.do"));
    }

    @RequestMapping(value = "ecuser/{id}/delete")
    public String delete(HttpServletRequest request, @CurrentOrg Organization orgEntity, EcUserWithBLOBs ecuser,
            Model model) {
        ecuserService.deleteByPrimaryKey(ecuser);

        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(orgEntity, userInfo, "基础信息中心:人员管理:教委用户管理", "删除教委人员" + ecuser.getXm());
        return redirectToUrl(viewName("ecuser.do"));
    }

    @RequestMapping(value = "ecuser/resetecuserpasswd", method = RequestMethod.POST)
    @ResponseBody
    public String resetEcuserPasswd(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        EcUserWithBLOBs ecuser = ecuserService.selectById(id);
        String defaultpasswd = passwordService.encryptPassword(ecuser.getSfzjh(), Constants.DEFAULT_PASSWORD);

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:教委用户管理", "重置教委人员" + ecuser.getXm() + "的登录密码");
        return accountService.passwdreset(ecuser.getAccountId(), defaultpasswd);

    }

    @RequestMapping(value = "ecuser/redirectToImportEcuser", method = RequestMethod.GET)
    public String redirectToImportEcuser() {
        return viewName("ecuser/ecuserimport");
    }

    @RequestMapping(value = "ecuser/downloadecusertemplate", method = RequestMethod.GET)
    public String downloadecusertemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("教委人员基本信息导入模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/教委人员基本信息导入模板.xlsx");
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

    /*
     * @RequestMapping(value="ecuser/delselected") public String
     * deleteselected(@RequestParam(value="rowcheck[]",required= false) Long[]
     * rowcheck){ if(rowcheck!=null && rowcheck.length >0){ for(Long id :
     * rowcheck){ EcUserWithBLOBs ec = ecuserService.selectById(id);
     * ecuserService.deleteByPrimaryKey(ec); } } return
     * redirectToUrl(viewName("ecuser.do")); }
     */
    @RequestMapping(value = "ecuser/delselected")
    public String ecuserdeleteselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        List<EcUserWithBLOBs> ecuserList = new ArrayList<EcUserWithBLOBs>();
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                EcUserWithBLOBs ec = ecuserService.selectById(id);
                ecuserList.add(ec);
            }
            ecuserService.deleteSelected(ecuserList);
        }

        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        logservice.log(org, userInfo, "基础信息中心:人员管理:教委用户管理", "批量删除教委人员");
        return redirectToUrl(viewName("ecuser.do"));
    }

    private long[] convert2longarry(String[] src) {
        long[] num = new long[src.length];
        for (int idx = 0; idx < src.length; idx++) {
            num[idx] = Long.parseLong(src[idx]);
        }
        return num;
    }

    @RequestMapping(value = "teacher/getTeachers")
    @ResponseBody
    public List<Teacher> getTeachers(@CurrentOrg Organization orgEntity, TeacherQueryParam teacher, Model model) {
        /*
         * if(!StringUtils.equals(orgEntity.getOrgType(),
         * OrganizationType.school.getId())){ return null; }
         */
        long schoolorgid = orgEntity.getId();
        PageParameter page = new PageParameter(0, 100);
        List<Teacher> list = teacherService.getTeachersByPage(teacher, schoolorgid, page);

        return list;

    }

    @RequestMapping(value = "teacher/getTeachersJson")
    @ResponseBody
    public String getTeachersJson(@CurrentOrg Organization orgEntity, TeacherQueryParam teacher, Model model) {
        /*
         * if(!StringUtils.equals(orgEntity.getOrgType(),
         * OrganizationType.school.getId())){ return null; }
         */
        long schoolorgid = orgEntity.getId();
        PageParameter page = new PageParameter(0, 100);
        List<Teacher> list = teacherService.getTeachersByPage(teacher, schoolorgid, page);
        Gson gson = new Gson();

        return gson.toJson(list);

    }

    // 史斌增加导入教委用户
    @RequestMapping(value = "ecuser/import", method = RequestMethod.POST)
    @ResponseBody
    public String importEC(HttpServletRequest request, String excelUrl, @CurrentOrg Organization org) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // tmpScaleDir = "picScale"+System.currentTimeMillis();
            FileOperate.saveFile(saveFilePath, fileName, mf);
            // InputStream inputStream = mf.getInputStream();
            ecuserImportService.importEcuserinfo(saveFilePath + "\\" + fileName, org);

            Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
            logservice.log(org, userInfo, "基础信息中心:人员管理:教委用户管理", "导入教委人员");
        }
        return "";
    }

    @RequestMapping(value = "teacher/redirectToImportTeacher", method = RequestMethod.GET)
    public String redirectToImportTeacher() {
        return viewName("teacher/teacherimport");
    }

    @RequestMapping(value = "{accountId}/userauth", method = RequestMethod.GET)
    public String userauth(@CurrentOrg Organization orgEntity, @CurrentUser Account currentuser,
            @PathVariable("accountId") long accountId, Model model) {
        Set<Role> selectUserIdroles = authService.findRoles(accountId, currentuser.getLogintype());
        Iterator<Role> selectUserit = selectUserIdroles.iterator();

        List<RoleResourcePermission> resources = new ArrayList<RoleResourcePermission>();
        while (selectUserit.hasNext()) {
            Role selectR = selectUserit.next();
            List<RoleResourcePermission> rrpList = this.roleService.selectRRPAccordingRoleId(selectR.getId());
            resources.addAll(rrpList);
        }
        List<UserResPerm> userResPermList = userResPermService.findResPermByUser(accountId, orgEntity.getOrgType());

        Account user = accountService.findOne(accountId);
        Gson gson = new Gson();
        String rrpListStr = gson.toJson(resources);
        String urpListStr = gson.toJson(userResPermList);
        model.addAttribute("rrp", rrpListStr);
        model.addAttribute("urp", urpListStr);
        model.addAttribute("user", user);
        return viewName("userAuth");
    }

    @RequestMapping(value = "{accountId}/userperm", method = RequestMethod.POST)
    @ResponseBody
    public String userperm(HttpServletRequest request, @CurrentOrg Organization orgEntity,
            @CurrentUser Account currentuser, @PathVariable("accountId") long accountId,
            @RequestParam("perm_resource") String perm_resource, Model model) {
        Account userInfo = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        int i = userResPermService.update(accountId, perm_resource, orgEntity.getOrgType());
        if (i == 1) {
            logservice.log(orgEntity, userInfo, "基础信息中心:人员管理", "授权成功");
            return "授权成功!";
        } else {
            logservice.log(orgEntity, userInfo, "基础信息中心:人员管理", "授权失败");
            return "授权失败，请联系管理员！";
        }
    }

}
