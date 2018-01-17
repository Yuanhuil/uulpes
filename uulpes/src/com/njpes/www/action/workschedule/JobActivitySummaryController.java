package com.njpes.www.action.workschedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.action.util.DateEditor;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivitySummaryWithBLOBs;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.workschedule.JobActivityRecordServiceI;
import com.njpes.www.service.workschedule.JobActivitySummaryServiceI;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.SchoolYearTermUtil;

@Controller
@RequestMapping(value = "/workschedule/activitysummary")
public class JobActivitySummaryController extends BaseController {

    @Autowired
    private JobActivitySummaryServiceI jobActivitySummaryService;
    @Autowired
    private JobActivityRecordServiceI jobActivityRecordService;
    @Autowired
    private SyslogServiceI logservice;

    public JobActivitySummaryController() {
        setResourceIdentity(jobActivitySummaryService.resourceIdentity);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, JobActivitySummaryWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        List<JobActivitySummaryWithBLOBs> list = jobActivitySummaryService.findByPage(entity, page);
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");

        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("entity", entity);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = "getType")
    @ResponseBody
    public List<Dictionary> getPlanTypeByCat(String catid) {
        String where = " catid = '" + catid + "'";
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", where);
        return plantype;
    }

    @RequestMapping(value = "querydata")
    public String getData(@CurrentOrg Organization org, JobActivitySummaryWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        model.addAttribute("list", jobActivitySummaryService.findByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "add")
    public String add(@CurrentOrg Organization org, JobActivityRecordWithBLOBs entity, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("entity", entity);
        model.addAttribute("plancatalog", planCatalog);
        List<JobActivityRecordWithBLOBs> list = jobActivityRecordService.findAllRecordInView(entity);
        model.addAttribute("list", list);
        return viewName("create/select");
    }

    @RequestMapping(value = "{id}/delete")
    public String delete(HttpServletRequest request, JobActivitySummaryWithBLOBs entity, Model model) {
        jobActivitySummaryService.delete(entity);
        logservice.log(request, "心理教育中心：教育活动", "删除活动总结");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "delselected")
    public String deleteSelect(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobActivitySummaryService.delete(id);
            }
        }
        logservice.log(request, "心理教育中心：教育活动", "批量删除活动总结");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String update(@CurrentOrg Organization org, JobActivitySummaryWithBLOBs entity, Model model) {
        entity = jobActivitySummaryService.findById(entity);
        model.addAttribute("entity", entity);
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        String where = " catid = '" + entity.getActivitycatalog() + "'";
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", where);
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute("plantype", plantype);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String formupdate(HttpServletRequest request, @CurrentOrg Organization org,
            JobActivitySummaryWithBLOBs entity, Model model) {
        jobActivitySummaryService.update(entity);
        logservice.log(request, "心理教育中心：教育活动", "修改活动总结");
        return redirectToUrl(viewName("querydata.do"));
    }

    @RequestMapping(value = "create/querydata")
    public String createQueryData(JobActivityRecordWithBLOBs entity, Model model) {
        List<JobActivityRecordWithBLOBs> list = jobActivityRecordService.findAllRecordInView(entity);
        model.addAttribute("list", list);
        return viewName("create/tablelist");
    }

    @RequestMapping(value = "create/select2", method = RequestMethod.POST)
    public String createSummary(@CurrentOrg Organization org, @CurrentUser Account user,
            @RequestParam(value = "rowcheck[]", required = false) String[] rowcheck, Model model,
            JobActivityRecordWithBLOBs entity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (rowcheck != null && rowcheck.length > 0) {
            List<JobActivityRecordWithBLOBs> list = new ArrayList<JobActivityRecordWithBLOBs>();
            for (String r : rowcheck) {
                String[] idTables = r.split("#");
                JobActivityRecordWithBLOBs record = jobActivityRecordService.selectRecordInView(idTables[0],
                        idTables[1]);
                if (record != null)
                    list.add(record);
            }
            if (list != null && list.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (JobActivityRecordWithBLOBs r : list) {
                    sb.append(sdf.format(r.getStarttime())).append("-").append(sdf.format(r.getEndtime()))
                            .append(" " + r.getTitle()).append(r.getContent()).append("<br>");
                }
                JobActivitySummaryWithBLOBs record = new JobActivitySummaryWithBLOBs();
                record.setIniContent(sb.toString());
                record.setContent(sb.toString());
                // jobActivitySummaryService.insert(record);
                record.setSchoolyear(entity.getSchoolyear());
                record.setTerm(entity.getTerm());
                record.setActivitycatalog(entity.getActivitycatalog());
                record.setActivitytype(entity.getActivitytype());
                model.addAttribute("entity", record);
                List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
                String where = " catid = '" + entity.getActivitycatalog() + "'";
                List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", where);
                List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
                List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
                model.addAttribute("schoolyears", schoolyears);
                model.addAttribute("schoolterm", schoolterm);
                model.addAttribute("plancatalog", planCatalog);
                model.addAttribute("plantype", plantype);
                return viewName("create/select2");
            }
        }
        return viewName("create/error");
    }

    @RequestMapping(value = "create/update", method = RequestMethod.POST)
    public String updateSummary(HttpServletRequest request, JobActivitySummaryWithBLOBs record, Model model) {
        jobActivitySummaryService.update(record);
        model.addAttribute("entity", record);
        logservice.log(request, "心理教育中心：教育活动", "修改活动总结");
        return viewName("create/select2");
    }

    @RequestMapping(value = "create/add", method = RequestMethod.POST)
    public String addSummary(HttpServletRequest request, @CurrentOrg Organization org, @CurrentUser Account user,
            JobActivitySummaryWithBLOBs record, Model model) {
        record.setDep(org.getId());
        record.setAuthor(user.getUsername());
        jobActivitySummaryService.insert(record);
        model.addAttribute("entity", record);
        logservice.log(request, "心理教育中心：教育活动", "添加活动总结");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/view")
    public String view(JobActivitySummaryWithBLOBs entity, Model model) {
        entity = jobActivitySummaryService.findById(entity);
        model.addAttribute("entity", entity);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("view");
    }
}
