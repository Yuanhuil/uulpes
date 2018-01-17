package com.njpes.www.action.workschedule;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.entity.workschedule.JobPlan;
import com.njpes.www.entity.workschedule.JobPlanWithBLOBs;
import com.njpes.www.entity.workschedule.enums.JobNoticeState;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.workschedule.JobPlanServiceI;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.SchoolYearTermUtil;

import heracles.web.config.ApplicationConfiguration;

@Controller
@RequestMapping(value = "/workschedule/plan")
public class JobPlanController extends BaseController {

    @Autowired
    private JobPlanServiceI jobPlanService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private SyslogServiceI logservice;

    public JobPlanController() {
        setResourceIdentity("workschedule:plan");
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, @CurrentUser Account account, JobPlanWithBLOBs entity,
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
                    if (reskey.equals("workschedule:plan")) {
                        canAudit = true;
                        break;
                    }
                }
            }
        }
        entity.setCanAudit(canAudit);
        entity.setDep(org.getId());
        entity.setAuthor(account.getId());
        List<JobPlan> list = jobPlanService.findByPage(entity, page);
        List<Dictionary> jobStateList = DictionaryService.selectAllDic("dic_job_state");
        List<Role> roleList = roleService.selectRolesByOrgLevel(org.getOrgLevel(), org.getOrgType(), false);
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("subOrgs", subOrgsList);
        model.addAttribute("jobstatelist", jobStateList);
        model.addAttribute("rolelist", roleList);
        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = "querydata")
    public String getData(@CurrentOrg Organization org, @CurrentUser Account account, JobPlanWithBLOBs entity,
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
                    if (reskey.equals("workschedule:plan")) {
                        canAudit = true;
                        break;
                    }
                }
            }
        }
        entity.setCanAudit(canAudit);
        entity.setDep(org.getId());
        entity.setAuthor(account.getId());
        model.addAttribute("list", jobPlanService.findByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "add")
    public String add(@CurrentOrg Organization org, JobPlanWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("subOrgs", subOrgsList);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "create")
    public String create(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobPlanWithBLOBs entity, Model model) {
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            entity.setWriteTime(df.parse(df.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        jobPlanService.insert(entity);
        logservice.log(request, "心理教育中心：工作计划", "批量删除活动计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String update(@CurrentOrg Organization org, JobPlanWithBLOBs entity, Model model) {
        JobPlanWithBLOBs showentity = jobPlanService.findById(entity, org.getId());
        model.addAttribute("entity", showentity);
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        model.addAttribute("subOrgs", subOrgsList);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobPlanService.resourceIdentity);
        model.addAttribute("attachments", atts);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String formupdate(HttpServletRequest request, JobPlanWithBLOBs entity, Model model) {
        jobPlanService.update(entity);
        logservice.log(request, "心理教育中心：工作计划", "修改工作计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/view")
    public String view(@CurrentOrg Organization org, JobPlanWithBLOBs entity, Model model) {
        entity = jobPlanService.findById(entity);
        model.addAttribute("entity", entity);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobPlanService.resourceIdentity);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("view");
    }

    @RequestMapping(value = "importform")
    public String importform(@CurrentOrg Organization org, Model model) {
        return viewName("importform");
    }

    @RequestMapping(value = "{id}/back")
    public String back(HttpServletRequest request, JobPlanWithBLOBs entity, Model model) {
        entity = jobPlanService.findById(entity);
        model.addAttribute("entity", entity);
        entity.setState(JobNoticeState.no_submit.value());
        jobPlanService.update(entity);
        logservice.log(request, "心理教育中心：工作计划", "撤回工作计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/audit", method = RequestMethod.GET)
    public String audit(JobPlanWithBLOBs entity, Model model) {
        entity = jobPlanService.findById(entity);
        entity.setState("3");// 默认选中同意
        model.addAttribute("entity", entity);
        model.addAttribute(Constants.OP_NAME, Constants.AUDIT_NAME);
        return viewName("view");
    }

    @RequestMapping(value = "{id}/send", method = RequestMethod.POST)
    public String formsend(HttpServletRequest request, @CurrentOrg Organization org, JobPlanWithBLOBs entity,
            Model model) {
        jobPlanService.updateOrSaveShareInfo(entity.getId(), entity.getOrg_ids(), org.getId());
        logservice.log(request, "心理教育中心：工作计划", "下发工作计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/send", method = RequestMethod.GET)
    public String send(@CurrentOrg Organization org, JobPlanWithBLOBs entity, Model model) {
        entity = jobPlanService.findById(entity, org.getId().longValue());
        model.addAttribute("entity", entity);
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        model.addAttribute("subOrgs", subOrgsList);
        model.addAttribute(Constants.OP_NAME, Constants.SEND_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/audit", method = RequestMethod.POST)
    public String formaudit(HttpServletRequest request, JobPlanWithBLOBs entity, Model model) {
        jobPlanService.justUpdateState(entity);
        logservice.log(request, "心理教育中心：工作计划", "审核工作计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/delete")
    public String delete(HttpServletRequest request, JobPlanWithBLOBs entity, Model model) {
        jobPlanService.delete(entity);
        logservice.log(request, "心理教育中心：工作计划", "删除工作计划");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long filedel(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobPlanService.resourceIdentity);
        return fileid;
    }

    @RequestMapping(value = "delselected")
    public String delselected(@RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobPlanService.delete(id);
            }
        }
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "downloadtemplate", method = RequestMethod.GET)
    public String downloadtemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("导入工作计划_新模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/workschedule/导入工作计划_新模板.xlsx");
            InputStream in = new BufferedInputStream(new FileInputStream(fullFileName));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;

            while ((i = in.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
