package com.njpes.www.action.systeminfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.systeminfo.ExamdoStudent;
import com.njpes.www.entity.systeminfo.ExamdoTeacher;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.systeminfo.ExamdoStudentServiceI;
import com.njpes.www.service.systeminfo.ExamdoTeacherServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/systeminfo/sys/test")
public class PersonalTestController extends BaseController {

    @Autowired
    private ExamdoStudentServiceI examdoStudentService;
    @Autowired
    private ExamdoTeacherServiceI examdoTeacherService;
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private FieldServiceI fieldService;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentUser Account account, HttpServletRequest request, Model model) {
        PageParameter page = new PageParameter(1, 10);
        String SOrT = "";
        page.setUrl(null);
        if (account.getTypeFlag() == 1) {
            Student student = (Student) accountService.getAccountInfo(account.getId(), account.getTypeFlag());
            ExamdoStudent examdoStudent = new ExamdoStudent();
            examdoStudent.setUserId(student.getId());
            List<ExamdoStudent> examdoStudentList = getListStudent(request, page, examdoStudent, null, null, model);
            model.addAttribute("list", examdoStudentList);
            model.addAttribute("examdo", examdoStudent);
            SOrT = "student";
        } else {
            ExamdoTeacher examdoTeacher = new ExamdoTeacher();
            Teacher teacher = (Teacher) accountService.getAccountInfo(account.getId(), account.getTypeFlag());
            examdoTeacher.setUserId(teacher.getId());
            List<ExamdoTeacher> examdoTeacherList = getListTeacher(request, page, examdoTeacher, null, null, model);
            model.addAttribute("list", examdoTeacherList);
            model.addAttribute("examdo", examdoTeacher);
            SOrT = "teacher";
        }
        Date date = new Date();
        model.addAttribute("currentTime", date.getTime());
        model.addAttribute("page", page);
        model.addAttribute("SOrT", SOrT);
        if (SOrT.equals("teacher")) {
            return viewName("mainT");
        }
        return viewName("mainS");

    }

    @RequestMapping(value = { "/list" })
    public String list(@CurrentUser Account account, ExamdoStudent examdoStudent, @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        String SOrT = "";
        if (account.getTypeFlag() == 1) {
            List<ExamdoStudent> examdoStudentList = getListStudent(request, page, examdoStudent, beginDate, endDate,
                    model);
            model.addAttribute("list", examdoStudentList);
            model.addAttribute("examdo", examdoStudent);
            SOrT = "student";
        } else {
            ExamdoTeacher examdoTeacher = new ExamdoTeacher();
            examdoTeacher.setFlag(examdoStudent.getFlag());
            examdoTeacher.setUserId(examdoStudent.getUserId());
            List<ExamdoTeacher> examdoTeacherList = getListTeacher(request, page, examdoTeacher, beginDate, endDate,
                    model);
            model.addAttribute("list", examdoTeacherList);
            model.addAttribute("examdo", examdoTeacher);
            SOrT = "teacher";
        }
        Date date = new Date();
        System.out.println(date.after(date));
        model.addAttribute("page", page);
        model.addAttribute("currentTime", date.getTime());
        model.addAttribute("SOrT", SOrT);
        if (SOrT.equals("teacher")) {
            return viewName("listT");
        }
        return viewName("listS");
    }

    private List<ExamdoStudent> getListStudent(HttpServletRequest request, PageParameter page,
            ExamdoStudent examdoStudent, Date beginDate, Date endDate, Model model) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        if (page == null) {
            int currentPage;
            int pageSize;
            String currentPageStr = request.getParameter("currentPage");
            String pageSizeStr = request.getParameter("pageSize");
            if (currentPageStr == null) {
                currentPage = 1;
            } else {
                currentPage = Integer.parseInt(currentPageStr);
            }
            pageSizeStr = Constants.PAGE_LIST_NUM;
            pageSize = Integer.parseInt(pageSizeStr);
            String url = request.getRequestURI();
            page = new PageParameter(currentPage, pageSize);
            page.setUrl(url);
        }

        List<ExamdoStudent> examdoStudentList = examdoStudentService.selectListByEntity(examdoStudent, page, beginDate,
                endDate);
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();

        FieldConfig teamNameMap = new FieldConfig("scaleId", "code", "showtitle", "scale", true, "", "int");
        NFTW.add(teamNameMap);
        Map<String, Map> m = fieldService.getFieldName(examdoStudentList, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }

        return examdoStudentList;
    }

    private List<ExamdoTeacher> getListTeacher(HttpServletRequest request, PageParameter page,
            ExamdoTeacher examdoTeacher, Date beginDate, Date endDate, Model model) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        if (page == null) {
            int currentPage;
            int pageSize;
            String currentPageStr = request.getParameter("currentPage");
            String pageSizeStr = request.getParameter("pageSize");
            if (currentPageStr == null) {
                currentPage = 1;
            } else {
                currentPage = Integer.parseInt(currentPageStr);
            }
            pageSizeStr = Constants.PAGE_LIST_NUM;
            pageSize = Integer.parseInt(pageSizeStr);
            String url = request.getRequestURI();
            page = new PageParameter(currentPage, pageSize);
            page.setUrl(url);
        }

        List<ExamdoTeacher> examdoTeacherList = examdoTeacherService.selectListByEntity(examdoTeacher, page, beginDate,
                endDate);
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();

        FieldConfig teamNameMap = new FieldConfig("scaleId", "code", "title", "scale", true, "", "int");
        NFTW.add(teamNameMap);
        Map<String, Map> m = fieldService.getFieldName(examdoTeacherList, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }
        return examdoTeacherList;
    }

}
