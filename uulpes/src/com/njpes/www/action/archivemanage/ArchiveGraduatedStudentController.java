package com.njpes.www.action.archivemanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Record;
import com.njpes.www.entity.consultcenter.WarningInterveneSWithBLOBs;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.RecordServiceI;
import com.njpes.www.service.consultcenter.WarningInterveneSServiceI;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.SchoolYearTermUtil;

/**
 * @Description: 毕业班个体档案
 * @author zhaowanfeng
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/archivemanage/archiveGraduatedStudent")
public class ArchiveGraduatedStudentController extends BaseController {

    @Autowired
    private RecordServiceI recordService;
    @Autowired
    private WarningInterveneSServiceI warningInterveneSService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private ScaleService scaleService;
    @Autowired
    private FieldServiceI fieldService;

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(@CurrentOrg Organization orgEntity, Student stu, Model model,
            @PageAnnotation PageParameter page) {

        if (!StringUtils.equals(orgEntity.getOrgType(), OrganizationType.school.getId())) {
            return viewName("noAuth");
        }
        long schoolorgid = orgEntity.getId();
        List<String> graduateyearList = schoolService.getGraduateYear(schoolorgid);
        List<Dictionary> sexlist = DictionaryService.selectAllDic("dic_sex");
        model.addAttribute("sexlist", sexlist);
        model.addAttribute("graduateyearList", graduateyearList);
        model.addAttribute("entity", stu);
        model.addAttribute("list", studentService.getStudentsByPage(stu, schoolorgid, page, 1));
        model.addAttribute("page", page);
        return viewName("main");

    }

    @RequestMapping(value = "list")
    public String list(@CurrentOrg Organization orgEntity, Student stu, Model model,
            @PageAnnotation PageParameter page) {
        long schoolorgid = orgEntity.getId();
        List<StudentWithBLOBs> list = studentService.getStudentsByPage(stu, schoolorgid, page, 1);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("list");

    }

    @RequestMapping(value = "getClassListByGraduateYear", method = RequestMethod.POST)
    @ResponseBody
    public List<ClassSchool> getClassListByGraduateYear(@CurrentOrg Organization orgEntity,
            HttpServletRequest request) {
        long schoolorgid = orgEntity.getId();
        String graduateyear = request.getParameter("graduateyear");
        List<ClassSchool> classList = schoolService.getGraduatedClassByYear(schoolorgid, graduateyear);

        return classList;

    }

    @RequestMapping(value = "view")
    public String view(@CurrentOrg Organization orgEntity, HttpServletRequest request, Student stu, Model model,
            @PageAnnotation PageParameter page) {
        StudentWithBLOBs student = studentService.selectStudentInfoById(stu.getId());
        String id = request.getParameter("id");
        String[] attrIds = request.getParameterValues("attrIds");
        String cpDisplay = request.getParameter("cpcheckbox");
        String gyDisplay = request.getParameter("gycheckbox");
        String fdDisplay = request.getParameter("fdcheckbox");
        String startschoolyear = request.getParameter("startschoolyear");
        String startterm = request.getParameter("startterm");
        String endschoolyear = request.getParameter("endschoolyear");
        String endterm = request.getParameter("endterm");
        model.addAttribute("startschoolyear", startschoolyear);
        model.addAttribute("startterm", startterm);
        model.addAttribute("endschoolyear", endschoolyear);
        model.addAttribute("endterm", endterm);
        model.addAttribute("cpDisplay", cpDisplay);
        model.addAttribute("gyDisplay", gyDisplay);
        model.addAttribute("fdDisplay", fdDisplay);
        if (attrIds == null) {// 显示所有学生信息
            student.loadMetas();
            String bjxx = student.getBjxx();
            student.setBjxx(bjxx);
            student.setBackGrandByStr();
            Map<String, String> map = new HashMap<String, String>();
            student.loadBackGrandToMap(map);
            student.load();
            List<FieldValue> list = student.getAttrs();
            request.setAttribute("attr_list", list);
        } else {
            student.loadMetas();
            String backGrand = student.getBjxx();
            student.setBackGrand(backGrand);
            student.load();
            List<FieldValue> list = student.getAttrs();
            List<FieldValue> result = new ArrayList<FieldValue>(list.size());
            for (String attrId : attrIds) {
                for (FieldValue fieldValue : list) {
                    if (StringUtils.equals(attrId, fieldValue.getId())) {
                        result.add(fieldValue);
                    }
                }
            }
            request.setAttribute("attr_list", result);
            // Map<String,String> map = new HashMap<String,String>();
            // propObject.loadBackGrandToMap(map);
        }
        student.setClassid(String.valueOf(student.getBjid()));
        student.setGradeid(String.valueOf(student.getGradecodeid()));
        model.addAttribute("entity", student);
        return viewName("view");
    }

    @RequestMapping(value = "archiveSetting")
    public String archiveSetting(HttpServletRequest request, Student stu) {
        PropObject propObject = null;
        propObject = PropObject.createPropObject(AcountTypeFlag.student.getId());
        propObject.loadMetas();
        List<FieldValue> list = propObject.getAttrs();
        request.setAttribute("attr_list", list);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        request.setAttribute("schoolyears", schoolyears);
        request.setAttribute("schoolterm", schoolterm);
        return viewName("setting");
    }

    @RequestMapping(value = "/studentCompositeReport", method = RequestMethod.GET)
    public String getStudentCompositeReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String subjectUserIdStr = request.getParameter("userid");
        String startyear = request.getParameter("startyear");
        String startterm = request.getParameter("startterm");
        String endyear = request.getParameter("endyear");
        String endterm = request.getParameter("endterm");
        String starttime = null;
        String endtime = null;
        if (!StringUtils.isEmpty(startyear) && !StringUtils.isEmpty(startterm))
            starttime = AgeUitl.getStartTimeFromSchoolyear(startyear, Integer.parseInt(startterm));
        if (!StringUtils.isEmpty(endyear) && !StringUtils.isEmpty(endyear))
            endtime = AgeUitl.getEndTimeFromSchoolyear(endyear, Integer.parseInt(endterm));

        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.getStudentCompositeReport(subjectUserId, observerUser, starttime,
                endtime);

        model.addAttribute("page", page);
        return viewName("studentcompositereport");
    }

    @RequestMapping(value = "/studentRecord", method = RequestMethod.GET)
    public String studentRecord(Student stu, HttpServletRequest request, HttpServletResponse response, Model model)
            throws Exception {
        String startyear = request.getParameter("startyear");
        String startterm = request.getParameter("startterm");
        String endyear = request.getParameter("endyear");
        String endterm = request.getParameter("endterm");
        String starttime = null;
        String endtime = null;
        if (!StringUtils.isEmpty(startyear) && !StringUtils.isEmpty(startterm))
            starttime = AgeUitl.getStartTimeFromSchoolyear(startyear, Integer.parseInt(startterm));
        if (!StringUtils.isEmpty(endyear) && !StringUtils.isEmpty(endyear))
            endtime = AgeUitl.getEndTimeFromSchoolyear(endyear, Integer.parseInt(endterm));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startdate = StringUtils.isEmpty(starttime) ? null : sdf.parse(starttime);
        Date enddate = StringUtils.isEmpty(endtime) ? null : sdf.parse(endtime);

        Record recordWithBLOBs = new Record();
        recordWithBLOBs.setSfzjh(stu.getSfzjh());
        List<Record> list = recordService.selectListByRecord(recordWithBLOBs, new PageParameter(1, 20), startdate,
                enddate);

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
    public String warningIntervene(Student stu, HttpServletRequest request, HttpServletResponse response, Model model)
            throws Exception {
        String startyear = request.getParameter("startyear");
        String startterm = request.getParameter("startterm");
        String endyear = request.getParameter("endyear");
        String endterm = request.getParameter("endterm");
        String starttime = null;
        String endtime = null;
        if (!StringUtils.isEmpty(startyear) && !StringUtils.isEmpty(startterm))
            starttime = AgeUitl.getStartTimeFromSchoolyear(startyear, Integer.parseInt(startterm));
        if (!StringUtils.isEmpty(endyear) && !StringUtils.isEmpty(endyear))
            endtime = AgeUitl.getEndTimeFromSchoolyear(endyear, Integer.parseInt(endterm));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startdate = StringUtils.isEmpty(starttime) ? null : sdf.parse(starttime);
        Date enddate = StringUtils.isEmpty(endtime) ? null : sdf.parse(endtime);
        WarningInterveneSWithBLOBs warningInterveneS = new WarningInterveneSWithBLOBs();
        warningInterveneS.setCardid(stu.getSfzjh());
        warningInterveneS.setStatus("4");
        List<WarningInterveneSWithBLOBs> list = warningInterveneSService.selectListByEntityWithBLOBs(warningInterveneS,
                new PageParameter(1, 20), startdate, enddate, 0);
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
