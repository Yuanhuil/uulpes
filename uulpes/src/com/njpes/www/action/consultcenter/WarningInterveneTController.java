package com.njpes.www.action.consultcenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.InterveneResult;
import com.njpes.www.entity.baseinfo.enums.InterveneType;
import com.njpes.www.entity.baseinfo.enums.SexEnum;
import com.njpes.www.entity.baseinfo.enums.WarningLever;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.WarningInterveneT;
import com.njpes.www.entity.consultcenter.WarningInterveneTWithBLOBs;
import com.njpes.www.entity.consultcenter.WarningTeacher;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.WarningInterveneTServiceI;
import com.njpes.www.service.consultcenter.WarningTeacherServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 主动预警
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/warningInterveneT")
public class WarningInterveneTController extends BaseController {
    @Autowired
    private WarningTeacherServiceI warningTeacherService;
    @Autowired
    private WarningInterveneTServiceI warningInterveneTService;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        WarningInterveneT warningInterveneT = new WarningInterveneT();
        warningInterveneT.setSchoolId(orgEntity.getId());
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        warningInterveneT.setStatus("2");
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        List<ConsultationModel> consultationModels = getConsultationModels();
        List<WarningInterveneT> warningInterveneTList = getList(request, page, warningInterveneT, null, null, 1);
        // List<Role> roleList = roleService.selectAll();
        List<Role> roleList = roleService.selectRolesByRoleLevel(6, "2");
        model.addAttribute("rolelist", roleList);
        model.addAttribute("list", warningInterveneTList);
        model.addAttribute("warningInterveneT", warningInterveneT);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("warningLever", WarningLever.values());
        model.addAttribute("page", page);
        // model.addAttribute("typeEnum",TeamTypeEnum.values() );
        return viewName("main");
    }

    @RequestMapping(value = { "/main2" })
    public String main2(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        WarningInterveneT warningInterveneT = new WarningInterveneT();
        warningInterveneT.setSchoolId(orgEntity.getId());
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        List<ConsultationModel> consultationModels = getConsultationModels();
        warningInterveneT.setStatus("4");
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        List<WarningInterveneT> warningInterveneTList = getList(request, page, warningInterveneT, null, null, 2);
        // List<Role> roleList = roleService.selectAll();
        List<Role> roleList = roleService.selectRolesByRoleLevel(6, "2");
        model.addAttribute("rolelist", roleList);
        model.addAttribute("list", warningInterveneTList);
        model.addAttribute("warningInterveneT", warningInterveneT);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("interveneResult", InterveneResult.values());
        model.addAttribute("interveneType", InterveneType.values());
        model.addAttribute("warningLever", WarningLever.values());
        // model.addAttribute("typeEnum",TeamTypeEnum.values() );
        model.addAttribute("page", page);
        return viewName("main2");
    }

    private List<ConsultType> getOpenConsultTypes(long id) {
        return consultTypeService.getOpenListByFid(id);

    }

    private List<ConsultationModel> getConsultationModels() {
        return consultationModelService.selectAll();
    }

    @RequestMapping(value = { "/list" })
    public String list(@CurrentOrg Organization orgEntity, WarningInterveneT warningInterveneT,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        warningInterveneT.setSchoolId(orgEntity.getId());
        if (StringUtils.isEmpty(warningInterveneT.getStatus())) {
            warningInterveneT.setStatus("2");
        }
        warningInterveneT.setSchoolId(orgEntity.getId());
        List<WarningInterveneT> listWarningInterveneT = getList(request, page, warningInterveneT, beginDate, endDate,
                1);
        model.addAttribute("list", listWarningInterveneT);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = { "/list2" })
    public String list2(@CurrentOrg Organization orgEntity, WarningInterveneT warningInterveneT,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        warningInterveneT.setSchoolId(orgEntity.getId());
        if (StringUtils.isEmpty(warningInterveneT.getStatus())) {
            warningInterveneT.setStatus("4");
        }
        warningInterveneT.setSchoolId(orgEntity.getId());
        List<WarningInterveneT> listWarningInterveneT = getList(request, page, warningInterveneT, beginDate, endDate,
                2);
        model.addAttribute("list", listWarningInterveneT);
        model.addAttribute("page", page);
        return viewName("list2");
    }

    private List<WarningInterveneT> getList(HttpServletRequest request, PageParameter page,
            WarningInterveneT warningInterveneT, Date beginDate, Date endDate, int step) {
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

        List<WarningInterveneT> warningInterveneTList = warningInterveneTService.selectListByEntity(warningInterveneT,
                page, beginDate, endDate, step);
        return warningInterveneTList;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, WarningInterveneTWithBLOBs warningInterveneT,
            HttpServletRequest request, Model model) {
        String view = request.getParameter("view");
        model.addAttribute("view", view);
        String intervene_show = request.getParameter("intervene_show");
        String status = warningInterveneT.getStatus();
        if (warningInterveneT.getId() != null) {
            warningInterveneT = warningInterveneTService.selectByPrimaryKey(warningInterveneT.getId());
        }
        if (warningInterveneT.getSchoolId() == null) {
            warningInterveneT.setSchoolId(orgEntity.getId());
        }
        if (warningInterveneT.getWarningTime() == null) {
            warningInterveneT.setWarningTime(new Date());
        }
        if (status != null) {
            warningInterveneT.setStatus(status);
        }
        String oldstatus = request.getParameter("oldstatus");
        if (oldstatus != null) {
            model.addAttribute("oldstatus", oldstatus);
        }

        // List<Role> roleList = roleService.selectAll();
        List<Role> roleList = roleService.selectRolesByRoleLevel(6, "2");
        List<Teacher> disposePersonList = teacherService.getPsychologyTeacherInSchool(orgEntity.getId());
        model.addAttribute("rolelist", roleList);
        model.addAttribute("warningInterveneT", warningInterveneT);
        model.addAttribute("warningLever", WarningLever.values());
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("interveneResult", InterveneResult.values());
        model.addAttribute("interveneType", InterveneType.values());
        model.addAttribute("intervene_show", intervene_show);
        model.addAttribute("disposePersonList", disposePersonList);
        return viewName("add");
    }

    @RequestMapping(value = { "/add" })
    public String add(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        WarningTeacher warningTeacher = warningTeacherService.selectEntityById(Long.parseLong(id));

        String view = request.getParameter("view");
        model.addAttribute("view", view);

        WarningInterveneTWithBLOBs warningInterveneT = warningTeacherService
                .WarningTeacher2WarningInterveneT(warningTeacher);
        String intervene_show = request.getParameter("intervene_show");
        if (warningInterveneT.getSchoolId() == null) {
            warningInterveneT.setSchoolId(orgEntity.getId());
        }
        if (warningInterveneT.getWarningTime() == null) {
            warningInterveneT.setWarningTime(new Date());
        }

        String oldstatus = request.getParameter("oldstatus");
        if (oldstatus != null) {
            model.addAttribute("oldstatus", oldstatus);
        }

        // List<Role> roleList = roleService.selectAll();
        List<Role> roleList = roleService.selectRolesByRoleLevel(6, "2");
        model.addAttribute("rolelist", roleList);
        warningInterveneT.setId(Long.parseLong(id));
        model.addAttribute("warningInterveneT", warningInterveneT);
        model.addAttribute("warningLever", WarningLever.values());
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("interveneResult", InterveneResult.values());
        model.addAttribute("interveneType", InterveneType.values());
        model.addAttribute("intervene_show", intervene_show);
        return viewName("add1");
    }

    @RequestMapping(value = { "/save" })
    public String save(@CurrentOrg Organization orgEntity, WarningInterveneTWithBLOBs warningInterveneT,
            HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(warningInterveneT.getStatus())) {
            warningInterveneT.setStatus("2");
        }
        WarningInterveneTWithBLOBs warningInterveneT1 = null;
        if (warningInterveneT.getId() != null) {
            warningInterveneT1 = warningInterveneTService.selectByPrimaryKey(warningInterveneT.getId());

        }
        if (warningInterveneT1 == null) {
            warningInterveneTService.saveEntity(warningInterveneT);
        } else {
            if (StringUtils.isNotBlank(warningInterveneT.getLevel())) {
                warningInterveneT1.setLevel(warningInterveneT.getLevel());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getStatus())) {
                warningInterveneT1.setStatus(warningInterveneT.getStatus());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getCardid())) {
                warningInterveneT1.setCardid(warningInterveneT.getCardid());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getName())) {
                warningInterveneT1.setName(warningInterveneT.getName());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getRoleName())) {
                warningInterveneT1.setRoleName(warningInterveneT.getRoleName());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getRecord())) {
                warningInterveneT1.setRecord(warningInterveneT.getRecord());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getProcess())) {
                warningInterveneT1.setProcess(warningInterveneT.getProcess());
            }
            if (warningInterveneT.getInterveneTime() != null) {
                warningInterveneT1.setInterveneTime(warningInterveneT.getInterveneTime());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getDisposePerson())) {
                warningInterveneT1.setDisposePerson(warningInterveneT.getDisposePerson());
            }
            if (StringUtils.isNotBlank(warningInterveneT.getDisposeType())) {
                warningInterveneT1.setDisposeType(warningInterveneT.getDisposeType());
            }
            if (warningInterveneT.getSex() != null){
                warningInterveneT1.setSex(warningInterveneT.getSex());
            }
            if (warningInterveneT.getWarningTime() != null){
                warningInterveneT1.setWarningTime(warningInterveneT.getWarningTime());
            }
            if (warningInterveneT.getType() != null){
                warningInterveneT1.setType(warningInterveneT.getType());
            }
            if (warningInterveneT.getResult() != null){
                warningInterveneT1.setResult(warningInterveneT.getResult());
            }
            warningInterveneTService.updateEntity(warningInterveneT1);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        String oldstatus = request.getParameter("oldstatus");
        if (oldstatus != null && oldstatus.toString().length() > 0) {
            map.put("status", oldstatus);
        }
        String name1 = null;
        try {
            name1 = URLDecoder.decode(request.getParameter("name1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (name1 != null && name1.toString().length() > 0) {
            map.put("name", name1.toString());
        }
        String sex1 = request.getParameter("sex1");
        if (sex1 != null && sex1.toString().length() > 0) {
            map.put("sex", sex1.toString());
        }
        String level1 = request.getParameter("level1");
        if (level1 != null && level1.toString().length() > 0) {
            map.put("level", level1.toString());
        }

        String roleName1 = request.getParameter("roleName1");
        if (roleName1 != null && roleName1.toString().length() > 0) {
            map.put("roleName", roleName1.toString());
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
        if ((oldstatus == null
                && (warningInterveneT.getStatus().equals("4") || warningInterveneT.getStatus().equals("5")))
                || (oldstatus != null && oldstatus.equals("4"))) {
            return redirectToUrl(viewName("list2.do"));
        }
        logservice.log(request, "心理辅导中心:教师主动预警", "修改教师主动预警");
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/saveAdd" })
    public String saveAdd(@CurrentOrg Organization orgEntity, WarningInterveneTWithBLOBs warningInterveneT,
            HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        warningInterveneT.setStatus("2");

        WarningTeacher warningTeacher = new WarningTeacher();
        warningTeacher.setId(warningInterveneT.getId());
        warningTeacher.setIswarnsure(new Byte("2"));
        warningTeacherService.updateIswarnsure(warningTeacher);
        warningInterveneT.setId(null);
        warningInterveneTService.saveEntity(warningInterveneT);

        HashMap<String, Object> map = new HashMap<String, Object>();
        String iswarnsure1 = request.getParameter("iswarnsure1");
        if (iswarnsure1 != null && iswarnsure1.toString().length() > 0) {
            map.put("iswarnsure", iswarnsure1.toString());
        }
        String warningGrade1 = request.getParameter("warningGrade1");
        if (warningGrade1 != null && warningGrade1.toString().length() > 0) {
            map.put("warningGrade", warningGrade1.toString());
        }
        String xbm = request.getParameter("xbm1");
        if (xbm != null && xbm.toString().length() > 0) {
            map.put("xbm", xbm.toString());
        }
        String xm = null;
        try {
            xm = URLDecoder.decode(request.getParameter("xm1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (xm != null && xm.toString().length() > 0) {
            map.put("xm", xm.toString());
        }
        String scaleId1 = request.getParameter("scaleId1");
        if (scaleId1 != null && scaleId1.toString().length() > 0) {
            map.put("scaleId", scaleId1.toString());
        }
        String roleId1 = request.getParameter("roleId1");
        if (roleId1 != null && roleId1.toString().length() > 0) {
            map.put("roleId", roleId1.toString());
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

        logservice.log(request, "心理辅导中心:教师主动预警", "保存学生主动预警");
        return redirectToUrl(viewName("../warningTeacher/list.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(WarningInterveneT warningInterveneT, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        warningInterveneTService.delEntity(warningInterveneT);
        HashMap<String, Object> map = new HashMap<String, Object>();

        String name1 = null;
        try {
            name1 = URLDecoder.decode(request.getParameter("name1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (name1 != null && name1.toString().length() > 0) {
            map.put("name", name1.toString());
        }
        String sex1 = request.getParameter("sex1");
        if (sex1 != null && sex1.toString().length() > 0) {
            map.put("sex", sex1.toString());
        }
        String level1 = request.getParameter("level1");
        if (level1 != null && level1.toString().length() > 0) {
            map.put("level", level1.toString());
        }

        String roleName1 = request.getParameter("roleName1");
        if (roleName1 != null && roleName1.toString().length() > 0) {
            map.put("roleName", roleName1.toString());
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
        logservice.log(request, "心理辅导中心:教师主动预警", "删除教师主动预警");
        return redirectToUrl(viewName("list.do"));
    }
}
