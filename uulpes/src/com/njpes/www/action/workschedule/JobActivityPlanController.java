package com.njpes.www.action.workschedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Resource;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobActivityPlanWithBLOBs;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.entity.workschedule.enums.JobNoticeState;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.workschedule.JobActivityPlanServiceI;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.SchoolYearTermUtil;

@Controller
@RequestMapping(value = "/workschedule/activityplan")
public class JobActivityPlanController extends BaseController {

    @Autowired
    private JobActivityPlanServiceI jobActivityPlanService;
    @Autowired
    private SyslogServiceI logservice;

    public JobActivityPlanController() {
        setResourceIdentity("workschedule:activityplan");
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, @CurrentUser Account account, JobActivityPlanWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        boolean canAudit = false;
        Set<Role> roles = account.getRoles();
        for (Role r : roles) {
            if (r.getId().intValue() == 24 || r.getId().intValue() == 67) {
                canAudit = true;
                break;
            } else {
                List<RoleResourcePermission> resPermissionList = r.getResourcePermissions();
                for (int i = 0; i < resPermissionList.size(); i++) {
                    RoleResourcePermission resPermission = resPermissionList.get(i);
                    Resource res = resPermission.getResource();
                    String reskey = res.getActualreskey();
                    /*
                     * if(reskey.equals("workschedule:activityplan")){ canAudit
                     * = true; break; }
                     */
                    if (reskey.equals("workschedule:eduactivity:activityplan")) {
                        canAudit = true;
                        break;
                    }
                }
            }
        }
        entity.setCanAudit(canAudit);
        entity.setDep(org.getId());
        entity.setAuthor(account.getId());
        // 该组织发的notice
        List<JobActivityPlanWithBLOBs> list = jobActivityPlanService.findByPage(entity, page);
        List<Dictionary> jobStateList = DictionaryService.selectAllDic("dic_job_statenosend");
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");

        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("jobstatelist", jobStateList);
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute("entity", entity);
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
    public String getData(@CurrentOrg Organization org, @CurrentUser Account account, JobActivityPlanWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        boolean canAudit = false;
        Set<Role> roles = account.getRoles();
        for (Role r : roles) {
            if (r.getId().intValue() == 24 || r.getId().intValue() == 67) {
                canAudit = true;
                break;
            } else {
                List<RoleResourcePermission> resPermissionList = r.getResourcePermissions();
                for (int i = 0; i < resPermissionList.size(); i++) {
                    RoleResourcePermission resPermission = resPermissionList.get(i);
                    Resource res = resPermission.getResource();
                    String reskey = res.getActualreskey();
                    if (reskey.equals("workschedule:activityplan")) {
                        canAudit = true;
                        break;
                    }
                }
            }
        }
        entity.setCanAudit(canAudit);
        entity.setDep(org.getId());
        entity.setAuthor(account.getId());
        model.addAttribute("list", jobActivityPlanService.findByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "add")
    public String add(@CurrentOrg Organization org, JobActivityPlanWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("plancatalog", planCatalog);
        String schoolyear = SchoolYearTermUtil.getCurrentSchoolYear();
        entity.setSchoolyear(schoolyear);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "create")
    public String create(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobActivityPlanWithBLOBs entity, Model model) {
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        entity.setWriteTime(new Date());
        jobActivityPlanService.insert(entity);
        logservice.log(request, "心理教育中心：教育活动", "添加活动计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String update(@CurrentOrg Organization org, JobActivityPlanWithBLOBs entity, Model model) {
        JobActivityPlanWithBLOBs showentity = jobActivityPlanService.findById(entity);
        model.addAttribute("entity", showentity);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        String where = " catid = '" + showentity.getActivitycatalog() + "'";
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", where);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute("plantype", plantype);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobActivityPlanService.resourceIdentity);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String formupdate(HttpServletRequest request, JobActivityPlanWithBLOBs entity, Model model) {
        jobActivityPlanService.update(entity);
        logservice.log(request, "心理教育中心：教育活动", "修改活动计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/back")
    public String back(HttpServletRequest request, JobActivityPlanWithBLOBs entity, Model model) {
        entity = jobActivityPlanService.findById(entity);
        model.addAttribute("entity", entity);
        entity.setState(JobNoticeState.no_submit.value());
        jobActivityPlanService.update(entity);
        logservice.log(request, "心理教育中心：教育活动", "撤回活动计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/audit", method = RequestMethod.GET)
    public String audit(JobActivityPlanWithBLOBs entity, Model model) {
        entity = jobActivityPlanService.findById(entity);
        model.addAttribute("entity", entity);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute(Constants.OP_NAME, Constants.AUDIT_NAME);
        return viewName("view");
    }

    @RequestMapping(value = "{id}/audit", method = RequestMethod.POST)
    public String formaudit(HttpServletRequest request, JobActivityPlanWithBLOBs entity, Model model) {
        jobActivityPlanService.justUpdateState(entity);
        logservice.log(request, "心理教育中心：教育活动", "活动计划审核");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/delete")
    public String delete(HttpServletRequest request, JobActivityPlanWithBLOBs entity, Model model) {
        jobActivityPlanService.delete(entity);
        logservice.log(request, "心理教育中心：教育活动", "删除活动计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/view")
    public String view(JobActivityPlanWithBLOBs entity, Model model) {
        entity = jobActivityPlanService.findById(entity);
        model.addAttribute("entity", entity);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobActivityPlanService.resourceIdentity);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("view");
    }

    @RequestMapping(value = "delselected")
    public String deleteSelect(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobActivityPlanService.delete(id);
            }
        }
        logservice.log(request, "心理教育中心：教育活动", "批量删除活动计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long filedel(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobActivityPlanService.resourceIdentity);
        return fileid;
    }
}
