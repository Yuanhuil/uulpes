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
import com.njpes.www.entity.consultcenter.Appointment;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.Record;
import com.njpes.www.entity.consultcenter.RecordEvent;
import com.njpes.www.entity.consultcenter.Scheduling;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.consultcenter.AppointmentServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.RecordServiceI;
import com.njpes.www.service.consultcenter.SchedulingServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 辅导记录
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/record")
public class RecordController extends BaseController {
    @Autowired
    private AppointmentServiceI appointmentService;
    @Autowired
    private RecordServiceI recordService;

    @Autowired
    private SchedulingServiceI schedulingService;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId().intValue());
        List<ConsultationModel> consultationModels = getConsultationModels();
        Record record = new Record();
        PageParameter page = new PageParameter(1, 10);
        record.setSchoolid(orgEntity.getId());
        List<Record> recordWithBLOBsList = getList(request, page, record, null, null, model);
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        model.addAttribute("teachers", teachers);
        model.addAttribute("list", recordWithBLOBsList);
        model.addAttribute("record", record);
        model.addAttribute("teachers", teachers);
        model.addAttribute("page", page);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("typeEnum", TeamTypeEnum.values());
        return viewName("main");
    }

    private List<ConsultType> getOpenConsultTypes(int id) {
        return consultTypeService.getOpenListByFid(id);

    }

    private List<ConsultationModel> getConsultationModels() {
        return consultationModelService.selectAll();
    }

    @RequestMapping(value = { "/list" })
    public String list(Record record, @CurrentOrg Organization orgEntity, @PageAnnotation PageParameter page,
            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        record.setSchoolid(orgEntity.getId());
        if (StringUtils.isEmpty(record.getObjtype())) {
            record.setObjtype(null);
        }
        if (StringUtils.isEmpty(record.getUsername())) {
            record.setUsername(null);
        }

        List<Record> recordWithBLOBsList = getList(request, page, record, beginDate, endDate, model);
        model.addAttribute("list", recordWithBLOBsList);
        model.addAttribute("page", page);
        return viewName("list");
    }

    private List<Record> getList(HttpServletRequest request, PageParameter page, Record recordWithBLOBs, Date beginDate,
            Date endDate, Model model) {
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
        if (StringUtils.isNotEmpty(teacheridStr)) {
            recordWithBLOBs.setTeacherid(Long.parseLong(teacheridStr));
        }

        List<Record> recordWithBLOBsList = recordService.selectListByRecord(recordWithBLOBs, page, beginDate, endDate);
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("consultationtypeid", "id", "name", "coach_consult_type", false, "");
        NFTW.add(typeMap);
        FieldConfig teacheridMap = new FieldConfig("teacherid", "id", "xm", "teacher", false, "");
        NFTW.add(teacheridMap);
        Map<String, Map> m = fieldService.getFieldName(recordWithBLOBsList, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }

        return recordWithBLOBsList;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        int orgid = orgEntity.getId().intValue();
        List<ConsultType> consultTypes = getOpenConsultTypes(orgid);
        List<ConsultationModel> consultationModels = getConsultationModels();

        String id = request.getParameter("id");
        String fid = request.getParameter("fid");
        Record record = new Record();

        if (id != null) {
            long idL = Long.parseLong(id);
            record = recordService.selectByPrimaryKey(idL);

        }
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        if (record.getDescribes() == null) {
            record.setDescribes("<p style=\"text-align:center\"><br />"
                    + "<strong><span style=\"font-size:20px\">个体咨询记录（面对面咨询）</span></strong></p>" + "<p><br />"
                    + "<strong>咨询缘由</strong><br />" + "<strong>来访者自述</strong>：（对症状的自身体验）<br />"
                    + "<strong>家属报告</strong>：（与主诉内容相关的报告）<br />"
                    + "<strong>重大生活事件</strong>：（为了进一步搜集资料，也为了核对以上资料的真实性）<br />" + "<strong>既往病史</strong>：<br />"
                    + "<strong>成长简史</strong>：<br />" + "个人成长、发展中的问题（经受的挫折或不良行为等）：<br />"
                    + "<strong>现实生活状况</strong>：<br />" + "人际关系中的问题：<br />" + "身体方面的主观感受（主观症状）：<br />"
                    + "<strong>其他</strong>：<br />" + "<strong>初步分析</strong><br />"
                    + "临床症状与相关因素之间的联系（说明是因果关系或横向影响关系）<br />" + "<strong>咨询过程</strong><br />"
                    + "<strong>咨询效果</strong><br />" + "<strong>咨询感悟</strong><br />" + "<strong>备注</strong><br />"
                    + "&nbsp;<br />" + "&nbsp;</p>" + "<p style=\"text-align:center\"><br />"
                    + "<span style=\"font-size:20px\"><strong>个体咨询记录（电话咨询）</strong></span></p>" + "<p><br />"
                    + "<strong>咨询缘由</strong><br />" + "<strong>来访者自述</strong>：（对症状的自身体验）<br />"
                    + "<strong>家属报告</strong>：（与主诉内容相关的报告）<br />"
                    + "<strong>重大生活事件</strong>：为了进一步搜集资料，也为了核对以上资料的真实性。<br />" + "<strong>既往病史</strong>：<br />"
                    + "<strong>成长简史</strong>：<br />" + "个人成长、发展中的问题（经受的挫折或不良行为等）：<br />"
                    + "<strong>现实生活状况</strong>：<br />" + "人际关系中的问题：<br />" + "身体方面的主观感受（主观症状）：<br />"
                    + "情绪体验、生活态度：<br />" + "<strong>其他</strong>：<br />" + "<strong>初步分析</strong><br />"
                    + "临床症状与相关因素之间的联系（说明是因果关系或横向影响关系）<br />"
                    + "<strong>通话过程</strong>(可用&ldquo;X&rdquo;代表来电者，用&ldquo;S&rdquo;代表咨询员)<br />"
                    + "<strong>咨询效果</strong><br />" + "<strong>咨询感悟</strong><br />" + "<strong>备注</strong></p>");
        }

        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("record", record);

        model.addAttribute("teachers", teachers);
        model.addAttribute("typeEnum", TeamTypeEnum.values());
        model.addAttribute("fid", fid);
        return viewName("add");
    }

    // 预约咨询信息添加
    @RequestMapping(value = { "/add" })
    public String add(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        int orgid = orgEntity.getId().intValue();
        List<ConsultType> consultTypes = getOpenConsultTypes(orgid);
        List<ConsultationModel> consultationModels = getConsultationModels();

        Record record = new Record();
        String id = request.getParameter("id");
        Appointment appointment = appointmentService.selectByPrimaryKey(Long.parseLong(id));
        if (appointment != null) {
            Scheduling scheduling = schedulingService.selectByPrimaryKey(appointment.getId());
            record.setConsultationmodeid(appointment.getModel());
            record.setConsultationtypeid(appointment.getType());
            record.setTeacherid(scheduling.getTeacherid());
            record.setUsername(appointment.getName());
            record.setObjtype("0");
        }

        List<RecordEvent> recordEvents = new ArrayList<RecordEvent>();

        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        if (record.getDescribes() == null) {
            record.setDescribes("<p style=\"text-align:center\"><br />"
                    + "<strong><span style=\"font-size:20px\">个体咨询记录（面对面咨询）</span></strong></p>" + "<p><br />"
                    + "<strong>咨询缘由</strong><br />" + "<strong>来访者自述</strong>：（对症状的自身体验）<br />"
                    + "<strong>家属报告</strong>：（与主诉内容相关的报告）<br />"
                    + "<strong>重大生活事件</strong>：（为了进一步搜集资料，也为了核对以上资料的真实性）<br />" + "<strong>既往病史</strong>：<br />"
                    + "<strong>成长简史</strong>：<br />" + "个人成长、发展中的问题（经受的挫折或不良行为等）：<br />"
                    + "<strong>现实生活状况</strong>：<br />" + "人际关系中的问题：<br />" + "身体方面的主观感受（主观症状）：<br />"
                    + "<strong>其他</strong>：<br />" + "<strong>初步分析</strong><br />"
                    + "临床症状与相关因素之间的联系（说明是因果关系或横向影响关系）<br />" + "<strong>咨询过程</strong><br />"
                    + "<strong>咨询效果</strong><br />" + "<strong>咨询感悟</strong><br />" + "<strong>备注</strong><br />"
                    + "&nbsp;<br />" + "&nbsp;</p>" + "<p style=\"text-align:center\"><br />"
                    + "<span style=\"font-size:20px\"><strong>个体咨询记录（电话咨询）</strong></span></p>" + "<p><br />"
                    + "<strong>咨询缘由</strong><br />" + "<strong>来访者自述</strong>：（对症状的自身体验）<br />"
                    + "<strong>家属报告</strong>：（与主诉内容相关的报告）<br />"
                    + "<strong>重大生活事件</strong>：为了进一步搜集资料，也为了核对以上资料的真实性。<br />" + "<strong>既往病史</strong>：<br />"
                    + "<strong>成长简史</strong>：<br />" + "个人成长、发展中的问题（经受的挫折或不良行为等）：<br />"
                    + "<strong>现实生活状况</strong>：<br />" + "人际关系中的问题：<br />" + "身体方面的主观感受（主观症状）：<br />"
                    + "情绪体验、生活态度：<br />" + "<strong>其他</strong>：<br />" + "<strong>初步分析</strong><br />"
                    + "临床症状与相关因素之间的联系（说明是因果关系或横向影响关系）<br />"
                    + "<strong>通话过程</strong>(可用&ldquo;X&rdquo;代表来电者，用&ldquo;S&rdquo;代表咨询员)<br />"
                    + "<strong>咨询效果</strong><br />" + "<strong>咨询感悟</strong><br />" + "<strong>备注</strong></p>");
        }

        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("record", record);
        model.addAttribute("recordEventSize", recordEvents.size());
        model.addAttribute("teachers", teachers);
        model.addAttribute("appid", appointment.getId());
        model.addAttribute("typeEnum", TeamTypeEnum.values());

        return viewName("add1");
    }

    @RequestMapping(value = { "/save" })
    public String save(@CurrentOrg Organization orgEntity, Record record, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        // String sfzjh = record.getSfzjh();
        // if(!StringUtils.isEmpty(sfzjh)){
        // Student student = studentService.getStudentBySfzjh(sfzjh);
        // if(student != null){
        // record.setXbm(student.getXbm());
        // record.setBjid(student.getBjid());
        // }
        // }
        if (record.getId() == null) {
            record.setSchoolid(orgEntity.getId());
            int str = recordService.saveRecord(record);
        } else {
            record.setSchoolid(orgEntity.getId());
            int str = recordService.updateRecord(record);
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
        String username = request.getParameter("username1");
        if (username != null && username.toString().length() > 0) {
            map.put("username", username.toString());
        }
        String objtype = request.getParameter("objtype1");
        if (objtype != null && objtype.toString().length() > 0) {
            map.put("objtype", objtype.toString());
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
        logservice.log(request, "心理辅导中心:辅导记录", "保存辅导记录");
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/save1" })
    public String save1(@CurrentOrg Organization orgEntity, Record record, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {

        if (record.getId() == null) {
            record.setSchoolid(orgEntity.getId());
            int str = recordService.saveRecord(record);
        } else {
            record.setSchoolid(orgEntity.getId());
            int str = recordService.updateRecord(record);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        String appid = request.getParameter("appid");
        Appointment appointment = appointmentService.selectByPrimaryKey(Long.parseLong(appid));
        appointment.setStatus("1");
        appointmentService.updateAppointment(appointment);
        String status1 = request.getParameter("status1");
        if (status1 != null && status1.toString().length() > 0) {
            map.put("status", status1.toString());
        }
        String model1 = request.getParameter("model1");
        if (model1 != null && model1.toString().length() > 0) {
            map.put("model", model1.toString());
        }
        String teacherid = request.getParameter("teacherid1");
        if (teacherid != null && teacherid.toString().length() > 0) {
            map.put("teacherid", teacherid.toString());
        }
        String username = request.getParameter("name1");
        if (username != null && username.toString().length() > 0) {
            map.put("name", username.toString());
        }
        String objtype = request.getParameter("type1");
        if (objtype != null && objtype.toString().length() > 0) {
            map.put("type", objtype.toString());
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
        logservice.log(request, "心理辅导中心:辅导记录", "保存辅导记录");
        return redirectToUrl(viewName("../appointment/listView.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(Record record, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        String str = "";
        if (record.getId() != null) {

            int a = recordService.delRecord(record);
            logservice.log(request, "心理辅导中心:辅导记录", "删除辅导记录");
            // if (a == 1) {
            // str = "删除成功";
            // } else {
            // str = "删除失败";
            // }
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
        String username = null;

        try {
            username = URLDecoder.decode(request.getParameter("username1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (username != null && username.toString().length() > 0) {
            map.put("username", username.toString());
        }
        String objtype = request.getParameter("objtype1");
        if (objtype != null && objtype.toString().length() > 0) {
            map.put("objtype", objtype.toString());
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
