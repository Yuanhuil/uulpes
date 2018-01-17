package com.njpes.www.action.archivemanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Record;
import com.njpes.www.entity.consultcenter.WarningInterveneTWithBLOBs;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.RecordServiceI;
import com.njpes.www.service.consultcenter.WarningInterveneTServiceI;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.PageParameter;

/**
 * @Description:
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/archivemanage/archiveTeacher")
public class ArchiveTeacherController extends BaseController {

    @Autowired
    private RecordServiceI recordService;
    @Autowired
    private WarningInterveneTServiceI warningInterveneTService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private TeacherServiceI teacherService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private ScaleService scaleService;
    @Autowired
    private FieldServiceI fieldService;

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(@CurrentOrg Organization orgEntity, TeacherQueryParam teacher, Model model,
            @PageAnnotation PageParameter page) {
        if (!StringUtils.equals(orgEntity.getOrgType(), OrganizationType.school.getId())) {
            return viewName("noAuth");
        }
        long schoolorgid = orgEntity.getId();
        model.addAttribute("entity", new TeacherQueryParam());
        List<Teacher> list = teacherService.getTeachersByPage(teacher, schoolorgid, page);
        List<Role> roleList = roleService.selectRolesByOrgLevel(orgEntity.getOrgLevel(), orgEntity.getOrgType(), false);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute("list", list);
        model.addAttribute("rolelist", roleList);
        model.addAttribute("page", page);
        return viewName("main");

    }

    @RequestMapping(value = "list")
    public String list(@CurrentOrg Organization orgEntity, TeacherQueryParam teacher, Model model,
            @PageAnnotation PageParameter page) {
        long schoolorgid = orgEntity.getId();
        List<Teacher> list = teacherService.getTeachersByPage(teacher, schoolorgid, page);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("list");

    }

    /*
     * @RequestMapping(value = { "/main" }) public String main(@CurrentOrg
     * Organization orgEntity, HttpServletRequest request, Model model) {
     *
     * WarningTeacher warningTeacher = new WarningTeacher();
     * warningTeacher.setOrgid(orgEntity.getId().intValue()); List<ConsultType>
     * consultTypes = getOpenConsultTypes(orgEntity.getId());
     * List<ConsultationModel> consultationModels = getConsultationModels();
     * PageParameter page = new PageParameter(1, 10); page.setUrl(null);
     * warningTeacher.setIswarnsure(new Byte("0")); List<WarningTeacher>
     * warningTeacherList = getList(request, page, warningTeacher, null, null);
     * long schoolorgid = orgEntity.getId(); List<XueDuan> xdlist =
     * schoolService.getXueDuanInSchool(schoolorgid);
     * if(xdlist!=null&&xdlist.size() == 1){ HashMap<XueDuan,List<Grade>>
     * xdNjMap = schoolService.getGradesInSchool(schoolorgid);
     * model.addAttribute("njlist", xdNjMap.get(xdlist.get(0))); }
     *
     * model.addAttribute("xdlist", xdlist); model.addAttribute("list",
     * warningTeacherList); model.addAttribute("warningTeacher",
     * warningTeacher); model.addAttribute("consultTypes", consultTypes);
     * model.addAttribute("consultationModels", consultationModels);
     * model.addAttribute("warningLever", DianXingGeAn.values()); //
     * model.addAttribute("typeEnum",TeamTypeEnum.values() );
     * model.addAttribute("page", page); return viewName("main"); }
     */

    @RequestMapping(value = "view")
    public String view(@CurrentOrg Organization orgEntity, Teacher teacher, Model model,
            @PageAnnotation PageParameter page) {
        TeacherQueryParam entity = teacherService.selectTeacherInfoById(teacher.getId());

        model.addAttribute("entity", entity);

        return viewName("view");

    }

    @RequestMapping(value = "/teacherCompositeReport", method = RequestMethod.GET)
    public String getTeacherCompositeReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {

        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.getTeacherCompositeReport(subjectUserId, observerUser);

        model.addAttribute("page", page);
        return viewName("teachercompositereport");
    }

    @RequestMapping(value = "/teacherRecord", method = RequestMethod.GET)
    public String teacherRecord(Teacher teacher, HttpServletRequest request, HttpServletResponse response,
            Model model) {

        Record recordWithBLOBs = new Record();
        recordWithBLOBs.setSfzjh(teacher.getSfzjh());
        List<Record> list = recordService.selectListByRecord(recordWithBLOBs, new PageParameter(1, 20), null, null);

        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("consultationtypeid", "id", "name", "coach_consult_type", false, "");
        NFTW.add(typeMap);
        FieldConfig teacheridMap = new FieldConfig("teacherid", "id", "xm", "teacher", false, "");
        NFTW.add(teacheridMap);
        Map<String, Map> m = fieldService.getFieldName(list, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }
        model.addAttribute("list", list);
        return viewName("record");
    }

    @RequestMapping(value = "/warningIntervene", method = RequestMethod.GET)
    public String warningIntervene(Teacher teacher, HttpServletRequest request, HttpServletResponse response,
            Model model) {

        WarningInterveneTWithBLOBs warningInterveneT = new WarningInterveneTWithBLOBs();
        warningInterveneT.setCardid(teacher.getSfzjh());
        warningInterveneT.setStatus("4");
        List<WarningInterveneTWithBLOBs> list = warningInterveneTService.selectListByEntityWithBLOBs(warningInterveneT,
                new PageParameter(1, 20), null, null, 0);
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("type", "id", "name", "coach_consult_type", false, "");
        NFTW.add(typeMap);

        Map<String, Map> m = fieldService.getFieldName(list, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }

        model.addAttribute("list", list);
        return viewName("warningIntervene");
    }

}
