package com.njpes.www.action.workschedule;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.enums.OrgLevel;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyactWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsycourseWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyresearchWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBsQuery;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.workschedule.JobActivityRecordServiceI;
import com.njpes.www.service.workschedule.JobMemorabiliaServiceI;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/workschedule/jobmemorabilia")
public class JobMemorabiliaController extends BaseController {
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private JobMemorabiliaServiceI jobMemorabiliaService;

    @Autowired
    private JobActivityRecordServiceI jobActivityRecordService;

    public JobMemorabiliaController() {
        setResourceIdentity(jobMemorabiliaService.resourceIdentity);
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, JobActivityRecordWithBLOBsQuery entity,
            @PageAnnotation PageParameter page, Model model) {
        entity.setVipEvent("1");
        entity.setDep(org.getId());
        if (org.getOrgLevel().intValue() <= OrgLevel.county.getIdentify()) {
            List<OrganizationLevel> l = organizationService.selectSubOrgLevel(org, false, true);
            model.addAttribute("orglevelList", l);
        }
        List<JobActivityRecordWithBLOBs> list = jobActivityRecordService.findRecordsInViewByPage(entity, page);
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute("entity", entity);
        model.addAttribute("currentorg", org);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("orgtype", org.returnOrgTypeEnum().getId());
        model.addAttribute("orglevel", org.getOrgLevel());
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
    public String querydata(@CurrentOrg Organization org, JobActivityRecordWithBLOBsQuery entity,
            @PageAnnotation PageParameter page, Model model) {
        entity.setVipEvent("1");
        entity.setDep(org.getId());
        List<JobActivityRecordWithBLOBs> list = jobActivityRecordService.findRecordsInViewByPage(entity, page);
        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "{tablename}/{id}/view")
    public String viewdetail(@CurrentOrg Organization org, @PathVariable("tablename") String tablename,
            @PathVariable("id") Long id, Model model) {
        if (StringUtils.equals(tablename, "job_activityrecord_psycourse")) {
            JobActivityRecordPsycourseWithBLOBs entity = new JobActivityRecordPsycourseWithBLOBs();
            entity.setId(id);
            entity = jobActivityRecordService.findPsycourseById(entity);
            model.addAttribute("entity", entity);
            List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='1'");
            model.addAttribute("plantype", plantype);
            List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
            model.addAttribute("levellist", levellist);
            List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                    jobActivityRecordService.resourceIdentity_course);
            model.addAttribute("attachments", atts);
            model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
            return "/workschedule/activityrecord/course/view";
        } else if (StringUtils.equals(tablename, "job_activityrecord_psyact")) {
            JobActivityRecordPsyactWithBLOBs entity = new JobActivityRecordPsyactWithBLOBs();
            entity.setId(id);
            entity = jobActivityRecordService.findPsyactById(entity);
            List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
            model.addAttribute("levellist", levellist);
            model.addAttribute("entity", entity);
            List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                    jobActivityRecordService.resourceIdentity_act);
            model.addAttribute("attachments", atts);
            model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
            return "/workschedule/activityrecord/act/view";
        } else if (StringUtils.equals(tablename, "job_activityrecord_psyresearch")) {
            JobActivityRecordPsyresearchWithBLOBs entity = new JobActivityRecordPsyresearchWithBLOBs();
            entity.setId(id);
            entity = jobActivityRecordService.findPsyresearchById(entity);
            model.addAttribute("entity", entity);
            List<Dictionary> researchStep = DictionaryService.selectAllDic("dic_job_activity_researchstep");
            List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='3'");
            model.addAttribute("plantype", plantype);
            model.addAttribute("researchStep", researchStep);
            List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                    jobActivityRecordService.resourceIdentity_research);
            model.addAttribute("attachments", atts);
            model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
            return "/workschedule/activityrecord/research/view";
        }

        return viewName("tablelist");
    }
}
