package com.njpes.www.action.consultcenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.enums.TeamTypeEnum;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Team;
import com.njpes.www.entity.consultcenter.TeamPage;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.TeamServiceI;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 辅导记录
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/team")
public class TeamController extends BaseController {

    @Autowired
    private TeamServiceI teamService;

    @Autowired
    private TeacherServiceI teacherService;

    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(HttpServletRequest request, Model model) {
        int id = 1;

        Team team = new Team();
        List<Team> teamList = getList(request, null, team);
        model.addAttribute("list", teamList);
        model.addAttribute("team", team);

        return viewName("main");
    }

    @RequestMapping(value = { "/list" })
    public String list(Team team, @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        if (team.getName() != null && team.getName().equals("")) {
            team.setName(null);
        }
        List<Team> listTeam = getList(request, page, team);
        model.addAttribute("listTeam", listTeam);
        model.addAttribute("page1", page);

        return viewName("list1");
    }

    private List<Team> getList(HttpServletRequest request, PageParameter page, Team team) {

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

        List<Team> teamList = teamService.selectListByTeam(team, page);
        return teamList;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        long orgid = orgEntity.getId();
        String id = request.getParameter("id");
        String fid = request.getParameter("fid");
        List<Teacher> teacherList = null;
        List<StudentWithBLOBs> studentWithBLOBs = null;
        TeamPage teamPage = new TeamPage();
        Team team = new Team();
        if (id != null) {
            long idL = Long.parseLong(id);
            team = teamService.selectByPrimaryKey(idL);
            List<Long> ids = teamService.getTeamPersonIds(team.getId());
            if (ids.size() > 0) {
                if (team.getTeamtype().endsWith("1")) {
                    teacherList = teacherService.getTeachersByIds(ids);
                } else {
                    studentWithBLOBs = studentService.getStudentsByIds(ids);
                }
            }

        } else {
            team.setSchoolid(orgid);
            team.setPersonnum(0);
        }
        Student student = new Student();
        long schoolorgid = orgEntity.getId();

        List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
        if (xdlist != null && xdlist.size() == 1) {
            HashMap<XueDuan, List<Grade>> xdNjMap = schoolService.getGradesInSchool(schoolorgid);
            model.addAttribute("njlist", xdNjMap.get(xdlist.get(0)));

        }
        model.addAttribute("xdlist", xdlist);
        TeacherQueryParam teacher = new TeacherQueryParam();
        model.addAttribute("teacher", teacher);

        model.addAttribute("student", student);
        model.addAttribute("teacherList", teacherList);
        model.addAttribute("studentList", studentWithBLOBs);
        List<Role> roleList = roleService.selectRolesByOrgLevel(orgEntity.getOrgLevel(), orgEntity.getOrgType(), false);
        model.addAttribute("rolelist", roleList);

        model.addAttribute("teamTypeEnum", TeamTypeEnum.values());
        teamPage.setTeam(team);
        model.addAttribute("teamPage", teamPage);
        model.addAttribute("fid", fid);

        return viewName("add");
    }

    @RequestMapping(value = { "/selectTeacher" })
    public String selectTeacher(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        long orgid = orgEntity.getId();
        int roleFlagI = 0;
        String roleFlag = request.getParameter("roleId");
        if (StringUtils.isNotEmpty(roleFlag)) {
            roleFlagI = Integer.parseInt(roleFlag);
        }
        List<Teacher> teachers = teacherService.getTeacherInSchool(orgid, roleFlagI);

        model.addAttribute("list", teachers);

        return viewName("listT");
    }

    @RequestMapping(value = { "/selectStudent" })
    public String selectStudent(@CurrentOrg Organization orgEntity, Long teamid, Student stu,
            HttpServletRequest request, Model model) {

        List<Long> studentids = teamService.getTeamPersonIds(teamid);
        List<StudentWithBLOBs> students = studentService.getStudents(orgEntity.getId(), stu, false, null, studentids,
                0);

        model.addAttribute("list", students);

        return viewName("listS");
    }

    @RequestMapping(value = { "/save" })
    public String save(TeamPage teamPage, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        String pids = request.getParameter("pid");
        System.out.println(pids);
        String str = teamService.saveTeamPage(teamPage);
        HashMap<String, Object> map = new HashMap<String, Object>();

        String title1 = request.getParameter("title1");
        if (title1 != null && title1.toString().length() > 0) {
            map.put("title", title1.toString());
        }
        String beginDate = request.getParameter("beginDate");
        if (beginDate != null && beginDate.toString().length() > 0) {
            map.put("beginDate", beginDate.toString());
        }
        String endDate = request.getParameter("endDate");
        if (endDate != null && endDate.toString().length() > 0) {
            map.put("endDate", endDate.toString());
        }
        redirectAttributes.addAllAttributes(map);
        logservice.log(request, "心理辅导中心:辅导记录", "保存团体辅导记录");
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(Team team, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String str = "";
        if (team.getId() != null) {

            str = teamService.delTeamPage(team);
            logservice.log(request, "心理辅导中心:辅导记录", "删除团体辅导记录");

        } else {
            str = "删除失败";
        }
        HashMap<String, Object> map = new HashMap<String, Object>();

        String teamtype1 = request.getParameter("teamtype1");
        if (teamtype1 != null && teamtype1.toString().length() > 0) {
            map.put("teamtype", teamtype1.toString());
        }
        String name1 = request.getParameter("name1");
        if (name1 != null && name1.toString().length() > 0) {
            map.put("name", name1.toString());
        }
        String personnum1 = request.getParameter("personnum1");
        if (personnum1 != null && personnum1.toString().length() > 0) {
            map.put("personnum", personnum1.toString());
        }
        redirectAttributes.addAllAttributes(map);
        request.setAttribute("result", str);
        return redirectToUrl(viewName("list.do"));
    }

}
