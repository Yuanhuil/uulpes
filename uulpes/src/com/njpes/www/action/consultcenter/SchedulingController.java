package com.njpes.www.action.consultcenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.enums.TimeEnum;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Scheduling;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.consultcenter.SchedulingServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 排班
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/scheduling")
public class SchedulingController extends BaseController {
    private static HashMap<Long, HashMap<String, Long>> schoolTeacherMap = new HashMap<Long, HashMap<String, Long>>();
    private static HashMap<Long, String> teacherMap = new HashMap<Long, String>();
    @Autowired
    private SchedulingServiceI schedulingService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        long id = orgEntity.getId();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0);
        Date beginDate = null;
        Date endDate = null;
        Map<String, Date> dateMap = setBeginEndDate(beginDate, endDate, null);
        beginDate = dateMap.get("1");
        endDate = dateMap.get("2");
        Scheduling scheduling = new Scheduling();
        scheduling.setSchoolid(id);
        List<Scheduling> schedulingList = getList(request, null, scheduling, beginDate, endDate);
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        HashMap<String, Long> teacherMap1 = new HashMap<String, Long>();
        for (Teacher teacher : teachers) {
            teacherMap1.put(teacher.getXm(), teacher.getId());
            teacherMap.put(teacher.getId(), teacher.getXm());
        }

        schoolTeacherMap.put(id, teacherMap1);
        model.addAttribute("teachers", teachers);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("defaultDate", time.format(beginDate));

        // model.addAttribute("list", schedulingList);
        model.addAttribute("scheduling", scheduling);
        String str = list2json(schedulingList);
        model.addAttribute("str", str);
        return viewName("main");
    }

    private Map<String, Date> setBeginEndDate(Date beginDate, Date endDate, Date valDate) {
        Map<String, Date> map = new HashMap<String, Date>();
        Calendar calendar = Calendar.getInstance();
        if (valDate != null) {
            calendar.setTime(valDate);
        } else {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0);
        }

        if (beginDate == null) {
            beginDate = calendar.getTime();
        }
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        if (endDate == null) {
            endDate = calendar.getTime();
        }
        map.put("1", beginDate);
        map.put("2", endDate);
        return map;
    }

    @RequestMapping(value = { "/list" })
    public String list(Scheduling scheduling, @PageAnnotation PageParameter page, @CurrentOrg Organization orgEntity,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        Date valDate = (Date) request.getAttribute("valDate");
        scheduling.setSchoolid(orgEntity.getId());
        Map<String, Date> dateMap = setBeginEndDate(beginDate, endDate, valDate);
        ;
        beginDate = dateMap.get("1");
        endDate = dateMap.get("2");
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        model.addAttribute("teachers", teachers);
        List<Scheduling> schedulingList = getList(request, page, scheduling, beginDate, endDate);
        String str = list2json(schedulingList);
        model.addAttribute("list", schedulingList);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
        Date defaultDate = calendar.getTime();
        // System.out.println(time.format(defaultDate));
        model.addAttribute("defaultDate", time.format(defaultDate));
        model.addAttribute("str", str);
        return viewName("list");
    }

    /**
     * @Description:
     * @param schedulingList
     * @return
     */
    private String list2json(List<Scheduling> schedulingList) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        Map<String, Scheduling> schedulingMap = new HashMap<String, Scheduling>();

        for (Scheduling scheduling : schedulingList) {
            String key = scheduling.getTeacherid() + scheduling.getDate().getTime() + "";
            Scheduling maxScheduling = schedulingMap.get(key);
            if (maxScheduling != null) {
                if (maxScheduling.getTimeid() < scheduling.getTimeid()) {
                    schedulingMap.put(key, scheduling);
                }
            } else {
                schedulingMap.put(key, scheduling);
            }
        }
        str = "[";
        for (String key : schedulingMap.keySet()) {
            Scheduling scheduling = schedulingMap.get(key);
            String name = teacherMap.get(scheduling.getTeacherid());
            String endTime = time.format(scheduling.getDate()).substring(0, 10) + " "
                    + TimeEnum.value2info(scheduling.getTimeid() + 1) + ":00";
            str += "{title: '" + name + "',start: '" + time.format(scheduling.getDate()) + "',oldTime: '"
                    + time.format(scheduling.getDate()) + "',id:'" + scheduling.getId() + "',end:'" + endTime
                    + "',editable:false},";

        }
        str += "]";
        return str;
    }

    // 获取当天最后一个时间片排班
    private Scheduling getLastScheduling(List<Scheduling> schedulingList) {
        Scheduling maxscheduling = null;

        for (Scheduling scheduling : schedulingList) {
            String key = scheduling.getTeacherid() + scheduling.getDate().getTime() + "";

            if (maxscheduling != null) {
                if (maxscheduling.getTimeid() < scheduling.getTimeid()) {
                    maxscheduling = scheduling;
                }
            } else {
                maxscheduling = scheduling;
            }
        }
        return maxscheduling;
    }

    private List<Scheduling> getList(HttpServletRequest request, PageParameter page, Scheduling scheduling,
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

        List<Scheduling> list = schedulingService.selectListByEntity(scheduling, page, beginDate, endDate);
        return list;
    }

    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, Scheduling scheduling, HttpServletRequest request,
            Model model) {
        long orgid = orgEntity.getId();
        String teacherName = "";
        try {
            teacherName = URLDecoder.decode(request.getParameter("teacherName"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String day = request.getParameter("day");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date befDate = null;
        Date aftDate = null;
        int week = 0;
        try {
            befDate = sdf.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(befDate);
            week = (calendar.get(Calendar.DAY_OF_WEEK) + 7) % 8;
            calendar.add(Calendar.HOUR, 24);
            aftDate = calendar.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(week + "————————————————————————————————————————————————————————————————————");
        if (scheduling.getId() != null) {
            List<Scheduling> schedulingList = schedulingService.selectListByEntity(scheduling, new PageParameter(0, 5),
                    befDate, aftDate);
            scheduling = getLastScheduling(schedulingList);

        } else {
            scheduling.setSchoolid(orgid);
            long tid = schoolTeacherMap.get(orgid).get(teacherName);
            scheduling.setDate(befDate);
            scheduling.setTeacherid(tid);
        }
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("scheduling", scheduling);
        if (scheduling.getDate() != null) {

            model.addAttribute("startTimeId", TimeEnum.info2value(sdfTime.format(scheduling.getDate())));
        }

        model.addAttribute("endTimeId", scheduling.getTimeid() == null ? "44" : scheduling.getTimeid() + 1);
        model.addAttribute("teacherName", teacherName);
        model.addAttribute("timeEnum", TimeEnum.values());
        return viewName("add");
    }

    @RequestMapping(value = { "/save" })
    public String save(@CurrentOrg Organization orgEntity, Scheduling scheduling, HttpServletRequest request,
            Model model) {
        long orgid = orgEntity.getId();

        String endTime = request.getParameter("endTimeId");
        String startTime = request.getParameter("startTimeId");
        String endDatestr = request.getParameter("endDate");
        String dateType = request.getParameter("dateType");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = null;
        int startTimeId = 0;
        int endTimeId = 0;
        try {

            if (StringUtils.isNoneEmpty(endDatestr)) {
                if (endDatestr.length() > 12) {
                    endDate = sdfTime.parse(endDatestr);
                } else {
                    endDate = sdf.parse(endDatestr);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
         * Calendar calendar = Calendar.getInstance();
         * calendar.setTime(startDate); int w =
         * calendar.get(Calendar.DAY_OF_WEEK) - 1;
         * 
         * IntroduceJobTime introduceJobTime = new IntroduceJobTime();
         * introduceJobTime.setFid((long) orgid);
         * introduceJobTime.setWeek((short) w); List<IntroduceJobTime> list =
         * introduceJobTimeService .getListByEntity(introduceJobTime); if
         * (list.size() > 0) { introduceJobTime = list.get(0); String
         * startTimeHH = TimeEnum.value2info(introduceJobTime
         * .getStarttimeid()); try { startTime = startTime.substring(0, 10) +
         * " " + startTimeHH + ":00"; minStartDate = sdfTime.parse(startTime);
         * if (minStartDate.after(startDate)) { startDate = minStartDate; } }
         * catch (ParseException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); } }
         */

        if (startTimeId == 0) {
            startTimeId = Integer.parseInt(startTime);
        }
        if (endTimeId == 0) {
            endTimeId = Integer.parseInt(endTime);
        }
        if (scheduling.getDate() == null) {
            return null;
        }
        scheduling.setSchoolid(orgid);
        String str = schedulingService.saveScheduling(scheduling, startTimeId, endTimeId, endDate, dateType);
        model.addAttribute("valDate", scheduling.getDate());
        model.addAttribute("result", str);
        logservice.log(request, "心理辅导中心", "保存值班安排");
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/delete" })
    public String delete(Scheduling scheduling, HttpServletRequest request, Model model) {
        String endDatestr = request.getParameter("endDate");
        String dateType = request.getParameter("dateType");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = null;
        try {

            if (StringUtils.isNoneEmpty(endDatestr)) {
                if (endDatestr.length() > 12) {
                    endDate = sdfTime.parse(endDatestr);
                } else {
                    endDate = sdf.parse(endDatestr);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Scheduling scheduling1 = schedulingService.selectByPrimaryKey(scheduling.getId());
        request.setAttribute("valDate", scheduling1.getDate());
        String str = schedulingService.delByEntity(scheduling, endDate, dateType);
        logservice.log(request, "心理辅导中心", "删除值班安排");
        return redirectToUrl(viewName("list.do"));
    }

}
