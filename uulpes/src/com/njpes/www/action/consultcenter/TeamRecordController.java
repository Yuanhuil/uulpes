package com.njpes.www.action.consultcenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.enums.TeamTypeEnum;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.Team;
import com.njpes.www.entity.consultcenter.TeamRecordWithBLOBs;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.TeamRecordServiceI;
import com.njpes.www.service.consultcenter.TeamServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 团队辅导记录
 * @author zhangchao
 * @Date 2015-5-25 下午5:02:06
 */
@Controller
@RequestMapping(value = "/consultcenter/teamRecord")
public class TeamRecordController extends BaseController {

    @Autowired
    private TeamRecordServiceI teamRecordService;
    @Autowired
    private TeamServiceI teamService;

    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId().intValue());
        List<ConsultationModel> consultationModels = getConsultationModels();
        TeamRecordWithBLOBs record = new TeamRecordWithBLOBs();
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        List<TeamRecordWithBLOBs> recordWithBLOBsList = getList(request, page, record, null, null, model);
        Team team = new Team();

        PageParameter page1 = new PageParameter(1, 10);
        page1.setUrl(null);

        List<Team> teams = teamService.selectListByTeam(team, page1);
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        model.addAttribute("teachers", teachers);
        model.addAttribute("list", recordWithBLOBsList);
        model.addAttribute("record", record);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("teamTypeEnum", TeamTypeEnum.values());
        model.addAttribute("team", new Team());
        model.addAttribute("listTeam", teams);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("page", page);
        model.addAttribute("page1", page1);
        return viewName("main");
    }

    private List<ConsultType> getOpenConsultTypes(long id) {
        return consultTypeService.getOpenListByFid(id);

    }

    private List<ConsultationModel> getConsultationModels() {
        return consultationModelService.selectAll();
    }

    @RequestMapping(value = { "/list" })
    public String list(TeamRecordWithBLOBs record, @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {

        List<TeamRecordWithBLOBs> recordWithBLOBsList = getList(request, page, record, beginDate, endDate, model);
        ;
        model.addAttribute("list", recordWithBLOBsList);
        model.addAttribute("page", page);
        return viewName("list");
    }

    private List<TeamRecordWithBLOBs> getList(HttpServletRequest request, PageParameter page,
            TeamRecordWithBLOBs recordWithBLOBs, Date beginDate, Date endDate, Model model) {
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

        String teacheridStr = request.getParameter("teacherid");
        if (!StringUtils.isEmpty(teacheridStr)) {
            recordWithBLOBs.setTeacherid(Long.parseLong(teacheridStr));
        }

        List<TeamRecordWithBLOBs> recordWithBLOBsList = teamRecordService
                .selectListByTeamRecordWithBLOBs(recordWithBLOBs, page, beginDate, endDate);
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("consultationtypeid", "id", "name", "coach_consult_type", false, "");
        NFTW.add(typeMap);
        FieldConfig teacheridMap = new FieldConfig("teacherid", "id", "xm", "teacher", false, "");
        NFTW.add(teacheridMap);
        FieldConfig teamNameMap = new FieldConfig("teamid", "id", "name", "coach_team", false, "");
        NFTW.add(teamNameMap);
        Map<String, Map> m = fieldService.getFieldName(recordWithBLOBsList, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }
        return recordWithBLOBsList;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        long orgid = orgEntity.getId();
        List<ConsultType> consultTypes = getOpenConsultTypes(orgid);
        List<ConsultationModel> consultationModels = getConsultationModels();
        String id = request.getParameter("id");
        String teamid = request.getParameter("teamid");
        TeamRecordWithBLOBs record = new TeamRecordWithBLOBs();
        if (teamid != null) {
            long teamidl = Long.parseLong(teamid);
            record.setTeamid(teamidl);
        }
        if (id != null) {
            long idL = Long.parseLong(id);
            record = teamRecordService.selectByPrimaryKey(idL);
        }
        if (record.getReason() == null) {
            record.setReason(
                    "<p><span style=\"font-size:14px\"><strong>辅导缘由：</strong></span></p><p>&nbsp;</p><p><span style=\"font-size:14px\"><strong>辅导过程：</strong></span></p><p>&nbsp;</p><p><span style=\"font-size:14px\"><strong>辅导感悟：</strong></span></p><p>&nbsp;</p><p><span style=\"font-size:14px\"><strong>辅导效果：</strong></span></p><p>&nbsp;</p><p><span style=\"font-size:14px\"><strong>备注：</strong></span></p><p>&nbsp;</p>");

        }
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("record", record);
        model.addAttribute("teachers", teachers);
        return viewName("add");
    }

    @RequestMapping(value = { "/save" })
    public String save(@CurrentOrg Organization orgEntity, TeamRecordWithBLOBs teamRecordWithBLOBs,
            HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (teamRecordWithBLOBs.getId() == null) {
            teamRecordWithBLOBs.setSchoolid(orgEntity.getId());
            int str = teamRecordService.saveTeamRecordWithBLOBs(teamRecordWithBLOBs);

        } else {
            teamRecordWithBLOBs.setSchoolid(orgEntity.getId());
            int str = teamRecordService.updateTeamRecordWithBLOBs(teamRecordWithBLOBs);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();

        String consultationtypeid = request.getParameter("consultationtypeid1");
        if (consultationtypeid != null && consultationtypeid.toString().length() > 0) {
            map.put("consultationtypeid", consultationtypeid.toString());
        }
        String consultationmodeid = request.getParameter("consultationmodeid1");
        if (consultationmodeid != null && consultationmodeid.toString().length() > 0) {
            map.put("consultationmodeid", consultationmodeid.toString());
        }
        String teacherid = request.getParameter("teacherid1");
        if (teacherid != null && teacherid.toString().length() > 0) {
            map.put("teacherid", teacherid.toString());
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
        logservice.log(request, "心理辅导中心:辅导记录", "保存团队辅导记录");
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(TeamRecordWithBLOBs teamRecordWithBLOBs, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        String str = "";
        if (teamRecordWithBLOBs.getId() != null) {

            int a = teamRecordService.delTeamRecordWithBLOBs(teamRecordWithBLOBs);
            if (a == 1) {
                str = "删除成功";
                logservice.log(request, "心理辅导中心:辅导记录", "删除团队辅导记录");
            } else {
                str = "删除失败";
            }
        } else {
            str = "删除失败";
        }
        HashMap<String, Object> map = new HashMap<String, Object>();

        String consultationtypeid = request.getParameter("consultationtypeid1");
        if (consultationtypeid != null && consultationtypeid.toString().length() > 0) {
            map.put("consultationtypeid", consultationtypeid.toString());
        }
        String consultationmodeid = request.getParameter("consultationmodeid1");
        if (consultationmodeid != null && consultationmodeid.toString().length() > 0) {
            map.put("consultationmodeid", consultationmodeid.toString());
        }
        String teacherid = request.getParameter("teacherid1");
        if (teacherid != null && teacherid.toString().length() > 0) {
            map.put("teacherid", teacherid.toString());
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
        request.setAttribute("result", str);
        return redirectToUrl(viewName("list.do"));
    }

}
