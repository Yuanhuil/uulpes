package com.njpes.www.action.workschedule;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyactWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsycourseWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyresearchWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBs;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.service.workschedule.JobActivityRecordServiceI;
import com.njpes.www.service.workschedule.JobOverviewServiceI;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/workschedule/joboverview")
public class JobOverviewController extends BaseController {
    @Autowired
    private JobOverviewServiceI jobOverviewService;

    @Autowired
    private JobActivityRecordServiceI jobActivityRecordService;

    public JobOverviewController() {
        setResourceIdentity(jobOverviewService.resourceIdentity);
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, JobActivityRecordWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        entity.setDep(org.getId());
        List<JobActivityRecordWithBLOBs> list = jobActivityRecordService.findSchoolRecordsInViewByPage(entity, page);
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        List<Dictionary> sf = DictionaryService.selectAllDic("dic_common_sf");
        model.addAttribute("plancatalog", planCatalog);
        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("sf", sf);
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
    public String getData(@CurrentOrg Organization org, JobActivityRecordWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN)) {
            entity.setDep(org.getId());
        }
        model.addAttribute("list", jobActivityRecordService.findSchoolRecordsInViewByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "genvipevent")
    @ResponseBody
    public String genJobOverview(@RequestParam(value = "rowcheck[]", required = false) String[] rowcheck,
            @RequestParam(value = "rownocheck[]", required = false) String[] rownocheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (String r : rowcheck) {
                String[] idTables = r.split("#");
                jobActivityRecordService.updateVipEventField(idTables[1], Long.parseLong(idTables[0]), "1");
            }
        }
        /*
         * if(rownocheck!=null && rownocheck.length >0){ for(String r :
         * rownocheck){ String[] idTables = r.split("#");
         * jobActivityRecordService.updateJobOverviewField(idTables[1],
         * Long.parseLong(idTables[0]), false); } }
         */
        return "更新成功";
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
