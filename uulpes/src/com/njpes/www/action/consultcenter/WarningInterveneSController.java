package com.njpes.www.action.consultcenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.InterveneResult;
import com.njpes.www.entity.baseinfo.enums.InterveneType;
import com.njpes.www.entity.baseinfo.enums.SexEnum;
import com.njpes.www.entity.baseinfo.enums.WarningLever;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.WarningInterveneS;
import com.njpes.www.entity.consultcenter.WarningInterveneSWithBLOBs;
import com.njpes.www.entity.consultcenter.WarningStudent;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.WarningInterveneSServiceI;
import com.njpes.www.service.consultcenter.WarningStudentServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 主动预警
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/warningInterveneS")
public class WarningInterveneSController extends BaseController {
    @Autowired
    private WarningStudentServiceI warningStudentService;;
    @Autowired
    private WarningInterveneSServiceI warningInterveneSService;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        Account account = (Account)request.getAttribute("user");
        WarningInterveneS warningInterveneS = new WarningInterveneS();
        warningInterveneS.setSchoolId(orgEntity.getId());
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        warningInterveneS.setStatus("2");
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        List<ConsultationModel> consultationModels = getConsultationModels();
        List<WarningInterveneS> warningInterveneSList = null;
        if (orgEntity.getOrgLevel() == 4 && orgEntity.getOrgType().equals("1")) {
            warningInterveneS.setLevel("3");
            warningInterveneS.setSchoolId(null);
            warningInterveneSList = getList(request, page, warningInterveneS, null, null, 1, model);
        }else{
            warningInterveneSList = getList(request, page, warningInterveneS, null, null, 1, model);
        }
        long schoolorgid = orgEntity.getId();
        /*
         * List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
         * if (xdlist.size() == 1) { HashMap<XueDuan, List<Grade>> xdNjMap =
         * schoolService .getGradesInSchool(schoolorgid);
         * model.addAttribute("njlist", xdNjMap.get(xdlist.get(0))); }
         * 
         * model.addAttribute("xdlist", xdlist);
         */
        model.addAttribute("list", warningInterveneSList);
        model.addAttribute("warningInterveneS", warningInterveneS);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("warningLever", WarningLever.values());
        // model.addAttribute("typeEnum",TeamTypeEnum.values() );
        model.addAttribute("page", page);
        return viewName("main");
    }

    @RequestMapping(value = { "/main2" })
    public String main2(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        WarningInterveneS warningInterveneS = new WarningInterveneS();
        warningInterveneS.setSchoolId(orgEntity.getId());
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        List<ConsultationModel> consultationModels = getConsultationModels();
        warningInterveneS.setStatus("4");
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        warningInterveneS.setSchoolId(orgEntity.getId());
        List<WarningInterveneS> warningInterveneSList = getList(request, page, warningInterveneS, null, null, 2, model);
        long schoolorgid = orgEntity.getId();
        /*
         * List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
         * if (xdlist!=null&&xdlist.size() == 1) { HashMap<XueDuan, List<Grade>>
         * xdNjMap = schoolService .getGradesInSchool(schoolorgid);
         * model.addAttribute("njlist", xdNjMap.get(xdlist.get(0))); }
         * 
         * model.addAttribute("xdlist", xdlist);
         */
        model.addAttribute("list", warningInterveneSList);
        model.addAttribute("warningInterveneS", warningInterveneS);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("interveneResult", InterveneResult.values());
        model.addAttribute("interveneType", InterveneType.values());
        model.addAttribute("warningLever", WarningLever.values());
        model.addAttribute("page", page);
        // model.addAttribute("typeEnum",TeamTypeEnum.values() );
        return viewName("main2");
    }

    private List<ConsultType> getOpenConsultTypes(long id) {
        return consultTypeService.getOpenListByFid(id);

    }

    private List<ConsultationModel> getConsultationModels() {
        return consultationModelService.selectAll();
    }

    @RequestMapping(value = { "/list" })
    public String list(@CurrentOrg Organization orgEntity, WarningInterveneS warningInterveneS,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        if (StringUtils.isEmpty(warningInterveneS.getStatus())) {
            warningInterveneS.setStatus("2");
        }
        warningInterveneS.setSchoolId(orgEntity.getId());
        List<WarningInterveneS> listWarningInterveneS = getList(request, page, warningInterveneS, beginDate, endDate, 1,
                model);
        model.addAttribute("list", listWarningInterveneS);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = { "/list2" })
    public String list2(@CurrentOrg Organization orgEntity, WarningInterveneS warningInterveneS,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        warningInterveneS.setSchoolId(orgEntity.getId());
        if (StringUtils.isEmpty(warningInterveneS.getStatus())) {
            warningInterveneS.setStatus("4");
        }
        warningInterveneS.setSchoolId(orgEntity.getId());
        List<WarningInterveneS> listWarningInterveneS = getList(request, page, warningInterveneS, beginDate, endDate, 2,
                model);
        model.addAttribute("list", listWarningInterveneS);
        model.addAttribute("page", page);
        return viewName("list2");
    }

    private List<WarningInterveneS> getList(HttpServletRequest request, PageParameter page,
            WarningInterveneS warningInterveneS, Date beginDate, Date endDate, int step, Model model) {
        endDate = endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
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

        List<WarningInterveneS> warningInterveneSList = warningInterveneSService.selectListByEntity(warningInterveneS,
                page, beginDate, endDate, step);

        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("type", "id", "name", "coach_consult_type", false, "");
        NFTW.add(typeMap);
        // FieldConfig njMap=new FieldConfig("grade", "id","title", "gradecode",
        // false, "","string");
        // NFTW.add(njMap);
        Map<String, Map> m = fieldService.getFieldName(warningInterveneSList, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }

        return warningInterveneSList;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, WarningInterveneSWithBLOBs warningInterveneS,
            HttpServletRequest request, Model model) {
        String view = request.getParameter("view");
        model.addAttribute("view", view);
        String intervene_show = request.getParameter("intervene_show");
        String status = warningInterveneS.getStatus();
        if (warningInterveneS.getId() != null) {
            warningInterveneS = warningInterveneSService.selectByPrimaryKey(warningInterveneS.getId());
        }
        if (warningInterveneS.getSchoolId() == null) {
            warningInterveneS.setSchoolId(orgEntity.getId());
        }
        if (warningInterveneS.getWarningTime() == null) {
            warningInterveneS.setWarningTime(new Date());
        }
        long schoolorgid = orgEntity.getId();
        List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
        if (xdlist != null && xdlist.size() == 1) {
            HashMap<XueDuan, List<Grade>> xdNjMap = schoolService.getGradesInSchool(schoolorgid);
            model.addAttribute("njlist", xdNjMap.get(xdlist.get(0)));

        }

        if (status != null) {
            warningInterveneS.setStatus(status);
        }
        String oldstatus = request.getParameter("oldstatus");
        if (oldstatus != null) {
            model.addAttribute("oldstatus", oldstatus);
        }
        List<Role> roleList = roleService.selectAll();
        List<Teacher> disposePersonList = teacherService.getPsychologyTeacherInSchool(orgEntity.getId());
        model.addAttribute("rolelist", roleList);
        model.addAttribute("xdlist", xdlist);
        model.addAttribute("warningInterveneS", warningInterveneS);
        model.addAttribute("warningLever", WarningLever.values());
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("interveneResult", InterveneResult.values());
        model.addAttribute("interveneType", InterveneType.values());
        model.addAttribute("intervene_show", intervene_show);
        model.addAttribute("disposePersonList", disposePersonList);
        model.addAttribute("pageOpenFlag", request.getParameter("openflag"));
        return viewName("add");
    }

    @RequestMapping(value = { "/add" })
    public String add(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        WarningStudent warningStudent = warningStudentService.selectEntityById(Long.parseLong(id));
        String view = request.getParameter("view");
        model.addAttribute("view", view);
        WarningInterveneSWithBLOBs warningInterveneS = warningStudentService
                .WarningStudent2WarningInterveneS(warningStudent);
        String intervene_show = request.getParameter("intervene_show");

        if (warningInterveneS.getSchoolId() == null) {
            warningInterveneS.setSchoolId(orgEntity.getId());
        }
        if (warningInterveneS.getWarningTime() == null) {
            warningInterveneS.setWarningTime(new Date());
        }
        long schoolorgid = orgEntity.getId();
        List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
        if (xdlist != null && xdlist.size() == 1) {
            HashMap<XueDuan, List<Grade>> xdNjMap = schoolService.getGradesInSchool(schoolorgid);
            model.addAttribute("njlist", xdNjMap.get(xdlist.get(0)));

        }

        String oldstatus = request.getParameter("oldstatus");
        if (oldstatus != null) {
            model.addAttribute("oldstatus", oldstatus);
        }
        List<Role> roleList = roleService.selectAll();
        model.addAttribute("rolelist", roleList);
        model.addAttribute("xdlist", xdlist);
        warningInterveneS.setId(Long.parseLong(id));// Id为wariningStudent
                                                    // Id用于更新学生状态
        model.addAttribute("warningInterveneS", warningInterveneS);
        model.addAttribute("warningLever", WarningLever.values());
        model.addAttribute("sexEnum", SexEnum.values());
        model.addAttribute("interveneResult", InterveneResult.values());
        model.addAttribute("interveneType", InterveneType.values());
        model.addAttribute("intervene_show", intervene_show);
        return viewName("add1");
    }

    @RequestMapping(value = { "/save" })
    public String save(@CurrentOrg Organization orgEntity, WarningInterveneSWithBLOBs warningInterveneS,
            HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(warningInterveneS.getStatus())) {
            warningInterveneS.setStatus("2");
        }
        WarningInterveneSWithBLOBs warningInterveneS1 = null;
        if (warningInterveneS.getId() != null) {
            warningInterveneS1 = warningInterveneSService.selectByPrimaryKey(warningInterveneS.getId());
        }
        if (warningInterveneS1 == null) {
            warningInterveneSService.saveEntity(warningInterveneS);
        } else {
            if (warningInterveneS.getLevel() != null) {
                warningInterveneS1.setLevel(warningInterveneS.getLevel());
            }
            if (warningInterveneS.getStatus() != null) {
                warningInterveneS1.setStatus(warningInterveneS.getStatus());
            }
            if (warningInterveneS.getCardid() != null) {
                warningInterveneS1.setCardid(warningInterveneS.getCardid());
            }
            if (warningInterveneS.getName() != null) {
                warningInterveneS1.setName(warningInterveneS.getName());
            }
            if (warningInterveneS.getResult() != null) {
                warningInterveneS1.setResult(warningInterveneS.getResult());
            }
            if (warningInterveneS.getType() != null) {
                warningInterveneS1.setType(warningInterveneS.getType());
            }
            if (warningInterveneS.getRecord() != null) {
                warningInterveneS1.setRecord(warningInterveneS.getRecord());
            }
            if (warningInterveneS.getProcess() != null) {
                warningInterveneS1.setProcess(warningInterveneS.getProcess());
            }
            if (warningInterveneS.getInterveneTime() != null) {
                warningInterveneS1.setInterveneTime(warningInterveneS.getInterveneTime());
            }
            if (warningInterveneS.getDisposePerson() != null) {
                warningInterveneS1.setDisposePerson(warningInterveneS.getDisposePerson());
            }
            if (warningInterveneS.getDisposeType() != null) {
                warningInterveneS1.setDisposeType(warningInterveneS.getDisposeType());
            }
            if (warningInterveneS.getGroupid() != null) {
                warningInterveneS1.setGroupid(warningInterveneS.getGroupid());
            }
            if (warningInterveneS.getClassName() != null) {
                warningInterveneS1.setClassName(warningInterveneS.getClassName());
            }
            if (warningInterveneS.getGrade() != null) {
                warningInterveneS1.setGrade(warningInterveneS.getGrade());
            }
            warningInterveneSService.updateEntity(warningInterveneS1);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        // 如果是更新状态继续查询原来的状态
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
        String result1 = request.getParameter("result1");
        if (result1 != null && result1.toString().length() > 0) {
            map.put("result", result1.toString());
        }
        String type1 = request.getParameter("type1");
        if (type1 != null && type1.toString().length() > 0) {
            map.put("type", type1.toString());
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
                && (warningInterveneS.getStatus().equals("4") || warningInterveneS.getStatus().equals("5")))
                || (oldstatus != null && oldstatus.equals("4"))) {
            return redirectToUrl(viewName("list2.do"));
        }
        logservice.log(request, "心理辅导中心:主动预警", "修改主动预警");
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/saveAdd" })
    public String saveAdd(@CurrentOrg Organization orgEntity, WarningInterveneSWithBLOBs warningInterveneS,
            HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        warningInterveneS.setStatus("2");

        WarningStudent warningStudent = new WarningStudent();
        warningStudent.setId(warningInterveneS.getId());
        warningStudent.setIswarnsure(new Byte("2"));
        warningStudentService.updateIswarnsure(warningStudent);
        warningInterveneS.setId(null);
        warningInterveneSService.saveEntity(warningInterveneS);

        HashMap<String, Object> map = new HashMap<String, Object>();
        // 如果是更新状态继续查询原来的状态

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

        String beginDate = request.getParameter("beginDate");
        if (beginDate != null && beginDate.toString().length() > 0) {
            map.put("beginDate", beginDate.toString());
        }
        String endDate = request.getParameter("endDate");
        if (endDate != null && endDate.toString().length() > 0) {
            map.put("endDate", endDate.toString());
        }
        redirectAttributes.addAllAttributes(map);

        logservice.log(request, "心理辅导中心:学生主动预警", "保存主动预警");
        return redirectToUrl(viewName("../warningStudent/list.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(WarningInterveneS warningInterveneS, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        warningInterveneSService.delEntity(warningInterveneS);
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
        String result1 = request.getParameter("result1");
        if (result1 != null && result1.toString().length() > 0) {
            map.put("result", result1.toString());
        }
        String type1 = request.getParameter("type1");
        if (type1 != null && type1.toString().length() > 0) {
            map.put("type", type1.toString());
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
        logservice.log(request, "心理辅导中心:学生主动预警", "删除主动预警");
        return redirectToUrl(viewName("../warningInterveneS/list.do"));
    }
}
