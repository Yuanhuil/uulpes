package com.njpes.www.action.consultcenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.enums.AppointmentState;
import com.njpes.www.entity.baseinfo.enums.TimeEnum;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Appointment;
import com.njpes.www.entity.consultcenter.AppointmentView;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.Scheduling;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.consultcenter.AppointmentServiceI;
import com.njpes.www.service.consultcenter.AppointmentViewServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.SchedulingServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 预约管理
 * @author zhangchao
 * @Date 2015-6-6 下午8:44:47
 */
@Controller
@RequestMapping(value = "/consultcenter/appointment")
public class AppointmentController extends BaseController {
    private static HashMap<Long, String> teacherMap = new HashMap<Long, String>();
    private static HashMap<Long, String> consultTypesMap = new HashMap<Long, String>();
    private static String colorNoAppointment = "#7cb5ec";
    private static String colorAppointment = "#90ed7d";
    private static String colorFinishAppointment = "#f7a35c";
    private static String colorNotFinishAppointment = "#8085e9";
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private AppointmentServiceI appointmentService;
    @Autowired
    private AppointmentViewServiceI appointmentViewService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private SchedulingServiceI schedulingService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        long id = orgEntity.getId();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE), 0, 0);
        Date beginDate = calendar.getTime();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        Date endDate = calendar.getTime();

        Scheduling scheduling = new Scheduling();
        scheduling.setSchoolid(id);
        List<Scheduling> schedulingList = getSchedulingList(request, null, scheduling, beginDate, endDate);
        Long[] ids = new Long[schedulingList.size()];
        for (int i = 0; i < schedulingList.size(); i++) {
            ids[i] = schedulingList.get(i).getId();
        }
        List<Appointment> appointmentList = appointmentService.selectListByIds(ids);
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        HashMap<String, Long> teacherMap1 = new HashMap<String, Long>();
        for (Teacher teacher : teachers) {
            teacherMap1.put(teacher.getXm(), teacher.getId());
            teacherMap.put(teacher.getId(), teacher.getXm());
        }
        List<ConsultationModel> consultationModels = consultationModelService.selectAll();
        for (ConsultationModel consultationModel : consultationModels) {
            consultTypesMap.put(consultationModel.getId(), consultationModel.getName());
        }
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId().intValue());

        model.addAttribute("consultTypes", consultTypes);
        String str = list2json(schedulingList, appointmentList);
        model.addAttribute("str", str);
        AppointmentView appointmentView = new AppointmentView();
        appointmentView.setSchoolid(id);
        model.addAttribute("appointmentView", appointmentView);
        model.addAttribute("beginDate", beginDate.toLocaleString());
        model.addAttribute("endDate", endDate.toLocaleString());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        model.addAttribute("defaultDate", formatter.format(beginDate));

        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("teachers", teachers);
        Appointment appointment = new Appointment();

        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentState", AppointmentState.values());
        return viewName("main");
    }

    private List<Scheduling> getSchedulingList(HttpServletRequest request, PageParameter page, Scheduling scheduling,
            Date beginDate, Date endDate) {

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

        List<Scheduling> list = schedulingService.selectListByEntity(scheduling, page, beginDate, endDate);
        return list;
    }

    @RequestMapping(value = { "/list" })
    public String list(@CurrentOrg Organization orgEntity, Scheduling scheduling, Appointment appointment,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        scheduling.setSchoolid(orgEntity.getId());
        if (appointment.getName() != null && appointment.getName().equals("")) {
            appointment.setName(null);
        }
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        model.addAttribute("teachers", teachers);
        List<Scheduling> schedulingList = getSchedulingList(request, page, scheduling, beginDate, endDate);

        Long[] ids = new Long[schedulingList.size()];
        for (int i = 0; i < schedulingList.size(); i++) {
            ids[i] = schedulingList.get(i).getId();
        }
        List<Appointment> appointmentList = appointmentService.selectListByIds(ids);
        String str = list2json(schedulingList, appointmentList);
        model.addAttribute("list", schedulingList);
        model.addAttribute("str", str);

        List<Appointment> listAppointment = getList(request, page, appointment, beginDate, endDate);
        model.addAttribute("listAppointment", listAppointment);
        if (beginDate != null) {
            model.addAttribute("beginDate", beginDate.toLocaleString());
        }
        if (endDate != null) {
            model.addAttribute("endDate", endDate.toLocaleString());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("defaultDate", formatter.format(beginDate));
        return viewName("list");
    }

    @RequestMapping(value = { "/listJson" })
    @ResponseBody
    public String listJson(@CurrentOrg Organization orgEntity, Scheduling scheduling, Appointment appointment,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        if (appointment.getName() != null && appointment.getName().equals("")) {
            appointment.setName(null);
        }

        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));

        model.addAttribute("teachers", teachers);
        List<Scheduling> schedulingList = getSchedulingList(request, page, scheduling, beginDate, endDate);

        Long[] ids = new Long[schedulingList.size()];
        for (int i = 0; i < schedulingList.size(); i++) {
            ids[i] = schedulingList.get(i).getId();
        }
        List<Appointment> appointmentList = new ArrayList<Appointment>();
        if (schedulingList.size() > 0) {
            appointmentList = appointmentService.selectListByIds(ids);
        }
        String str = list2json(schedulingList, appointmentList);
        model.addAttribute("list", schedulingList);
        model.addAttribute("str", str);

        List<Appointment> listAppointment = getList(request, page, appointment, beginDate, endDate);
        model.addAttribute("listAppointment", listAppointment);
        return str;
    }

    private HashMap initStartAndEndDate(Long id) {
        HashMap<String, Date> map = new HashMap<String, Date>();
        Scheduling scheduling = schedulingService.selectByPrimaryKey(id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduling.getDate());

        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = calendar.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        Date beginDate = calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return map;

    }

    private List<Appointment> getList(HttpServletRequest request, PageParameter page, Appointment appointment,
            Date beginDate, Date endDate) {
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

        List<Appointment> appointmentList = appointmentService.selectListByEntity(appointment, page, beginDate,
                endDate);
        return appointmentList;
    }

    /**
     * @Description:
     * @param schedulingList
     * @param appointmentList
     * @return
     */
    private String list2json(List<Scheduling> schedulingList, List<Appointment> appointmentList) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        Map<Long, Appointment> appointmentMap = new HashMap<Long, Appointment>();
        for (Appointment appointment : appointmentList) {
            appointmentMap.put(appointment.getId(), appointment);
        }
        str = "[";
        for (Scheduling scheduling : schedulingList) {
            Appointment appointment = appointmentMap.get(scheduling.getId());
            String TName = teacherMap.get(scheduling.getTeacherid());
            String color = colorNoAppointment;
            String sName = "无";
            String title = "咨询员：" + TName;
            String describes = "无";
            String contact = "无";
            String status = "未预约";
            String endTime = "";
            if (appointment != null) {
                if (appointment.getSchedulingids() != null) {
                    continue;
                }
                if (StringUtils.isNoneEmpty(appointment.getName())) {
                    sName = appointment.getName();
                    title = "学生：" + sName;
                }
                if (StringUtils.isNoneEmpty(appointment.getDescribes())) {
                    describes = appointment.getDescribes();
                }
                if (StringUtils.isNoneEmpty(appointment.getContact())) {
                    contact = appointment.getContact();
                }

                if (appointment.getStatus() != null && appointment.getStatus().equals("0")) {
                    color = colorAppointment;
                    status = "已预约";
                } else if (appointment.getStatus() != null && appointment.getStatus().equals("1")) {
                    color = colorFinishAppointment;
                    status = "完成";
                } else if (appointment.getStatus() != null && appointment.getStatus().equals("2")) {
                    color = colorNotFinishAppointment;
                    status = "未完成";
                }
                endTime = time.format(scheduling.getDate()).substring(0, 10) + " "
                        + TimeEnum.value2info(scheduling.getTimeid() + 2) + ":00";
            } else {
                endTime = time.format(scheduling.getDate()).substring(0, 10) + " "
                        + TimeEnum.value2info(scheduling.getTimeid() + 1) + ":00";
            }
            String realEndTime = time.format(scheduling.getDate()).substring(0, 10) + " "
                    + TimeEnum.value2info(scheduling.getTimeid() + 2) + ":00";
            String startTime = time.format(scheduling.getDate()).substring(0, 10) + " "
                    + TimeEnum.value2info(scheduling.getTimeid()) + ":00";

            str += "{title: '" + title + "',start: '" + startTime + "',id:'" + scheduling.getId() + "',end:'" + endTime
                    + "',tName:'" + TName + "',describes:'" + describes + "',sName:'" + sName + "',contact:'" + contact
                    + "',color:'" + color + "',status:'" + status + "',realEndTime:'" + realEndTime
                    + "',editable:false},";

        }

        str += "]";
        return str;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, Appointment appointment, HttpServletRequest request,
            Model model) {
        Scheduling scheduling = schedulingService.selectByPrimaryKey(appointment.getId());
        long id = appointment.getId();
        appointment = appointmentService.selectByPrimaryKey(appointment.getId());
        if (appointment == null) {
            if (!checkNextTimeidFree(id)) {
                return viewName("nofree");
            }
            appointment = new Appointment();
            appointment.setId(scheduling.getId());
            appointment.setStatus("0");
        }
        List<ConsultationModel> consultationModels = consultationModelService.selectAll();
        for (ConsultationModel consultationModel : consultationModels) {
            consultTypesMap.put(consultationModel.getId(), consultationModel.getName());
        }
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId().intValue());
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("teacherName", teacherMap.get(scheduling.getTeacherid()));
        model.addAttribute("appointment", appointment);
        model.addAttribute("scheduling", scheduling);
        model.addAttribute("endtimeid", scheduling.getTimeid() + 2);

        if (request.getParameter("view") != null && request.getParameter("view").equals("true")) {
            model.addAttribute("view", true);
        }

        model.addAttribute("timeEnum", TimeEnum.valueMap());

        model.addAttribute("appointmentState", AppointmentState.values());
        return viewName("add");
    }

    @RequestMapping(value = { "/save" })
    public String save(@CurrentOrg Organization orgEntity, Appointment appointment, HttpServletRequest request,
            Model model, RedirectAttributes redirectAttributes) {
        Appointment appointment1 = appointmentService.selectByPrimaryKey(appointment.getId());

        HashMap<String, Date> map = initStartAndEndDate(appointment.getId());
        int str = 0;
        if (appointment1 == null) {
            long nextid = schedulingService.getNextId(appointment.getId());
            if (nextid > 0) {
                Appointment appointment2 = appointmentService.selectByPrimaryKey(nextid);
                if (appointment2 == null) {
                    str = appointmentService.saveAppointment(appointment);
                    appointment2 = new Appointment();
                    appointment2.setId(nextid);
                    appointment2.setSchedulingids(appointment.getId().toString());
                    appointmentService.saveAppointment(appointment2);
                    logservice.log(request, "预约管理", "新建预约成功");
                }
            } else {
                str = 0;
            }

        } else {
            str = appointmentService.updateAppointment(appointment);
            logservice.log(request, "预约管理", "更新预约成功");
        }
        redirectAttributes.addAllAttributes(map);
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(Appointment appointment, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {

        if (request.getParameter("step") != null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String name = null;
            try {
                name = URLDecoder.decode(request.getParameter("name"), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (name != null && name.toString().length() > 0) {
                map.put("name", name.toString());
            }

            String model1 = request.getParameter("model");
            if (model1 != null && model1.toString().length() > 0) {
                map.put("model", model1.toString());
            }
            String teacherid = request.getParameter("teacherid");
            if (teacherid != null && teacherid.toString().length() > 0) {
                map.put("teacherid", teacherid.toString());
            }
            String status = request.getParameter("status");
            if (status != null && status.toString().length() > 0) {
                map.put("status", status.toString());
            }
            String type = request.getParameter("type");
            if (type != null && type.toString().length() > 0) {
                map.put("type", type.toString());
            }

            String beginDate = request.getParameter("beginDate");
            if (beginDate != null && beginDate.toString().length() > 0) {
                map.put("beginDate", beginDate.toString());
            }
            String endDate = request.getParameter("endDate");
            if (endDate != null && endDate.toString().length() > 0) {
                map.put("endDate", endDate.toString());
            }
            if (appointment.getId() != null) {

                int str = appointmentService.delAppointment(appointment);
                logservice.log(request, "预约管理", "预约删除");
            }
            redirectAttributes.addAllAttributes(map);
            return redirectToUrl(viewName("listView.do"));
        }
        HashMap<String, Date> map = initStartAndEndDate(appointment.getId());

        if (appointment.getId() != null) {

            int str = appointmentService.delAppointment(appointment);

        }
        redirectAttributes.addAllAttributes(map);
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = { "/listView" })
    public String listView(@CurrentOrg Organization orgEntity, AppointmentView appointmentView,
            @PageAnnotation PageParameter page, HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        if (StringUtils.isEmpty(appointmentView.getName())) {
            appointmentView.setName(null);
        }
        if (StringUtils.isEmpty(appointmentView.getStatus())) {
            appointmentView.setStatus(null);
        }
        appointmentView.setSchoolid(orgEntity.getId());
        List<AppointmentView> list = appointmentViewService.selectByEntity(appointmentView, page, beginDate, endDate);
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("type", "id", "name", "coach_consult_type", false, "");
        NFTW.add(typeMap);
        FieldConfig teacheridMap = new FieldConfig("teacherid", "id", "xm", "teacher", false, "");
        NFTW.add(teacheridMap);
        Map<String, Map> m = fieldService.getFieldName(list, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("list1");
    }

    private List<ConsultType> getOpenConsultTypes(int id) {
        return consultTypeService.getOpenListByFid(id);

    }

    // 查询该教师的下半个小时是否可预约
    private boolean checkNextTimeidFree(long id) {
        long nextid = schedulingService.getNextId(id);
        if (nextid > 0) {
            Appointment appointment = appointmentService.selectByPrimaryKey(nextid);
            if (appointment == null) {
                return true;
            }
        }
        return false;
    }
}
