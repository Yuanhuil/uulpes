package com.njpes.www.action.workschedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.njpes.www.entity.workschedule.JobAttachment;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.entity.workschedule.JobNoticeWebQueryParam;
import com.njpes.www.entity.workschedule.JobNoticeWithBLOBs;
import com.njpes.www.entity.workschedule.enums.JobNoticeState;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.workschedule.JobAttachmentMappnigServiceI;
import com.njpes.www.service.workschedule.JobAttachmentServiceI;
import com.njpes.www.service.workschedule.JobNoticeServiceI;
import com.njpes.www.utils.PageParameter;

/**
 * 工作部署通知通告
 * 
 * @author 赵忠诚
 */
@Controller
@RequestMapping(value = "/workschedule/notice")
public class JobNoticeController extends BaseController {

    @Autowired
    private JobNoticeServiceI jobNoticeService;

    @Autowired
    private JobAttachmentMappnigServiceI jobAttachmentMappnigService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private JobAttachmentServiceI jobAttachmentService;
    @Autowired
    private SyslogServiceI logservice;

    public JobNoticeController() {
        setResourceIdentity(jobNoticeService.resourceIdentity);
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, @CurrentUser Account account, JobNoticeWebQueryParam entity,
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
                    if (reskey.equals("workschedule:notice")) {
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
        List<JobNoticeWithBLOBs> sendNoticeLists = jobNoticeService.findNoticeByPage(entity, page);
        model.addAttribute("list", sendNoticeLists);
        List<Role> roleList = roleService.selectRolesByOrgLevel(org.getOrgLevel(), org.getOrgType(), false);
        List<Dictionary> catalogList = DictionaryService.selectAllDic("dic_job_notice_catalog");
        List<Dictionary> jobStateList = DictionaryService.selectAllDic("dic_job_state");
        model.addAttribute("cataloglist", catalogList);
        model.addAttribute("jobstatelist", jobStateList);
        model.addAttribute("rolelist", roleList);
        model.addAttribute("entity", entity);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = "querydata")
    public String getData(@CurrentOrg Organization org, @CurrentUser Account account, JobNoticeWebQueryParam entity,
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
                    if (reskey.equals("workschedule:notice")) {
                        canAudit = true;
                        break;
                    }
                }
            }
        }
        entity.setCanAudit(canAudit);
        entity.setDep(org.getId());
        entity.setAuthor(account.getId());
        model.addAttribute("list", jobNoticeService.findNoticeByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "add")
    public String add(@CurrentOrg Organization org, JobNoticeWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        List<Dictionary> catalogList = DictionaryService.selectAllDic("dic_job_notice_catalog");
        List<Dictionary> isno = DictionaryService.selectAllDic("dic_common_sf");
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        model.addAttribute("subOrgs", subOrgsList);
        model.addAttribute("cataloglist", catalogList);
        model.addAttribute("isno", isno);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "create")
    public String create(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobNoticeWebQueryParam entity, Model model) {
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            entity.setWriteTime(df.parse(df.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        jobNoticeService.insert(entity);
        logservice.log(request, "心理教育中心：通知公告", "添加通知公告");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String update(@CurrentOrg Organization org, JobNoticeWebQueryParam entity, Model model) {
        JobNoticeWithBLOBs showentity = jobNoticeService.findById(entity, org.getId());
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobNoticeService.resourceIdentity);
        model.addAttribute("attachments", atts);
        model.addAttribute("entity", showentity);
        List<Dictionary> catalogList = DictionaryService.selectAllDic("dic_job_notice_catalog");
        model.addAttribute("cataloglist", catalogList);
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        model.addAttribute("subOrgs", subOrgsList);
        List<Dictionary> isno = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("isno", isno);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String formupdate(HttpServletRequest request, @ModelAttribute JobNoticeWebQueryParam entity, Model model,
            BindingResult result) {
        jobNoticeService.update(entity);
        logservice.log(request, "心理教育中心：通知公告", "修改通知公告");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/view")
    public String view(@CurrentOrg Organization org, JobNoticeWithBLOBs entity, Model model) {
        entity = jobNoticeService.findById(entity);
        model.addAttribute("entity", entity);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobNoticeService.resourceIdentity);
        model.addAttribute("attachments", atts);
        List<Dictionary> catalogList = DictionaryService.selectAllDic("dic_job_notice_catalog");
        model.addAttribute("cataloglist", catalogList);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("viewnotice");
    }

    @RequestMapping(value = "{id}/back")
    public String back(HttpServletRequest request, JobNoticeWithBLOBs entity, Model model) {
        entity = jobNoticeService.findById(entity);
        model.addAttribute("entity", entity);
        entity.setState(JobNoticeState.no_submit.value());
        jobNoticeService.update(entity);
        logservice.log(request, "心理教育中心：通知公告", "撤回通知公告");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/audit", method = RequestMethod.GET)
    public String audit(JobNoticeWithBLOBs entity, Model model) {
        entity = jobNoticeService.findById(entity);
        model.addAttribute("entity", entity);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobNoticeService.resourceIdentity);
        model.addAttribute("attachments", atts);
        List<Dictionary> catalogList = DictionaryService.selectAllDic("dic_job_notice_catalog");
        model.addAttribute("cataloglist", catalogList);
        model.addAttribute(Constants.OP_NAME, Constants.AUDIT_NAME);
        return viewName("viewnotice");
    }

    @RequestMapping(value = "{id}/send", method = RequestMethod.POST)
    public String formsend(HttpServletRequest request, @CurrentOrg Organization org, JobNoticeWithBLOBs entity,
            Model model) {
        jobNoticeService.updateOrSaveShareInfo(entity.getId(), entity.getOrg_ids(), org.getId());
        logservice.log(request, "心理教育中心：通知公告", "下发通知公告");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/send", method = RequestMethod.GET)
    public String send(@CurrentOrg Organization org, JobNoticeWithBLOBs entity, Model model) {
        entity = jobNoticeService.findById(entity, org.getId().longValue());
        model.addAttribute("entity", entity);
        List<Dictionary> catalogList = DictionaryService.selectAllDic("dic_job_notice_catalog");
        model.addAttribute("cataloglist", catalogList);
        List<Organization> subOrgsList = organizationService.getSonSubOrgsList(org);
        model.addAttribute("subOrgs", subOrgsList);
        model.addAttribute(Constants.OP_NAME, Constants.SEND_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/audit", method = RequestMethod.POST)
    public String formaudit(HttpServletRequest request, JobNoticeWithBLOBs entity, Model model) {
        jobNoticeService.justUpdateState(entity);
        logservice.log(request, "心理教育中心：通知公告", "审核通知公告");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/delete")
    public String delete(HttpServletRequest request, JobNoticeWithBLOBs entity, Model model) {
        jobNoticeService.delete(entity);
        logservice.log(request, "心理教育中心：通知公告", "删除通知公告");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long filedel(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobNoticeService.resourceIdentity);
        return fileid;
    }

    @RequestMapping(value = "file/{entityid}/{fileid}/firstpage")
    @ResponseBody
    public Long firstpage(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid,
            String showfirstpage) {
        JobAttachment record = new JobAttachment();
        record.setId(fileid);
        record.setShowfirstpage(showfirstpage);
        jobAttachmentService.update(record);
        return fileid;
    }

    @RequestMapping(value = "delselected")
    public String delselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobNoticeService.del(id);
            }
        }
        logservice.log(request, "心理教育中心：通知公告", "批量删除通知公告");
        return redirectToUrl(viewName("list.do"));
    }
}
