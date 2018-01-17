package com.njpes.www.action.workschedule;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.njpes.www.action.util.DateEditor;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.baseinfo.util.SchoolYearTerm;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyactWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsycourseWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyresearchWithBLOBs;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.workschedule.JobActivityRecordServiceI;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.SchoolYearTermUtil;

import heracles.web.config.ApplicationConfiguration;

@Controller
@RequestMapping(value = "/workschedule/activityrecord")
public class JobActivityRecordController extends BaseController {

    @Autowired
    private JobActivityRecordServiceI jobActivityRecordService;

    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private SyslogServiceI logservice;

    public JobActivityRecordController() {
        setResourceIdentity(jobActivityRecordService.resourceIdentity);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model) {
        return viewName("list");
    }

    @RequestMapping(value = "course/list", method = RequestMethod.GET)
    public String courselist(@CurrentOrg Organization org, JobActivityRecordPsycourseWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());

        List<JobActivityRecordPsycourseWithBLOBs> list = jobActivityRecordService.findPsycourseByPage(entity, page);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='1'");
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        model.addAttribute("plantype", plantype);
        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("course/list");
    }

    @RequestMapping(value = "course/querydata")
    public String getCourseData(@CurrentOrg Organization org, JobActivityRecordPsycourseWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        model.addAttribute("list", jobActivityRecordService.findPsycourseByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("course/tablelist");
    }

    @RequestMapping(value = "course/add")
    public String addCourse(@CurrentOrg Organization org, JobActivityRecordPsycourseWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='1'");
        model.addAttribute("plantype", plantype);
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("course/editform");
    }

    @RequestMapping(value = "course/create")
    public String createCourse(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobActivityRecordPsycourseWithBLOBs entity, Model model) {
        try {
            SchoolYearTerm syt = SchoolYearTermUtil.getSchoolYearByDate(entity.getStartactivitytime(),
                    entity.getEndactivitytime());
            entity.setSchoolyear(syt.getSchoolyear());
            entity.setTerm(syt.getTerm());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        entity.setWriteTime(new Date());
        jobActivityRecordService.insertPsycourse(entity);
        logservice.log(request, "心理教育中心：教育活动", "添加心理课");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "course/importform")
    public String courseimportform(@CurrentUser Account account, @CurrentOrg Organization org, Model model) {
        return viewName("course/importform");
    }

    @RequestMapping(value = "course/{id}/view")
    public String viewCourse(@CurrentOrg Organization org, JobActivityRecordPsycourseWithBLOBs entity, Model model) {
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
        return viewName("course/view");
    }

    @RequestMapping(value = "course/{id}/update", method = RequestMethod.GET)
    public String updateCourse(HttpServletRequest request, @CurrentOrg Organization org,
            JobActivityRecordPsycourseWithBLOBs entity, Model model) {
        JobActivityRecordPsycourseWithBLOBs showentity = jobActivityRecordService.findPsycourseById(entity);
        model.addAttribute("entity", showentity);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='1'");
        model.addAttribute("plantype", plantype);
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobActivityRecordService.resourceIdentity_course);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        logservice.log(request, "心理教育中心：教育活动", "修改心理课");
        return viewName("course/editform");
    }

    @RequestMapping(value = "course/{id}/update", method = RequestMethod.POST)
    public String formupdateCourse(HttpServletRequest request, JobActivityRecordPsycourseWithBLOBs entity,
            Model model) {
        try {
            SchoolYearTerm syt = SchoolYearTermUtil.getSchoolYearByDate(entity.getStartactivitytime(),
                    entity.getEndactivitytime());
            entity.setSchoolyear(syt.getSchoolyear());
            entity.setTerm(syt.getTerm());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        jobActivityRecordService.updatePsycourse(entity);
        logservice.log(request, "心理教育中心：教育活动", "修改心理课");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "course/{id}/delete")
    public String deleteCourse(HttpServletRequest request, JobActivityRecordPsycourseWithBLOBs entity, Model model) {
        jobActivityRecordService.deletePsycourse(entity);
        logservice.log(request, "心理教育中心：教育活动", "删除心理课");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "course/delselected")
    public String coursedeleteSelect(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobActivityRecordService.deletePsycourse(id);
            }
        }
        logservice.log(request, "心理教育中心：教育活动", "批量删除心理课");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "course/file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long coursefiledel(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobActivityRecordService.resourceIdentity_course);
        return fileid;
    }

    // 0----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "act/list", method = RequestMethod.GET)
    public String actlist(@CurrentOrg Organization org, JobActivityRecordPsyactWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());

        List<JobActivityRecordPsyactWithBLOBs> list = jobActivityRecordService.findPsyactByPage(entity, page);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='2'");
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        model.addAttribute("plantype", plantype);
        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("act/list");
    }

    @RequestMapping(value = "act/querydata")
    public String getActData(@CurrentOrg Organization org, JobActivityRecordPsyactWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        model.addAttribute("list", jobActivityRecordService.findPsyactByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("act/tablelist");
    }

    @RequestMapping(value = "act/add")
    public String addAct(@CurrentOrg Organization org, JobActivityRecordPsyactWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='2'");
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        model.addAttribute("plantype", plantype);
        List<Teacher> teachers = teacherService.getTeacherInSchool(org.getId(), 0);
        model.addAttribute("teachers", teachers);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("act/editform");
    }

    @RequestMapping(value = "act/create")
    public String createAct(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobActivityRecordPsyactWithBLOBs entity, Model model) {
        try {
            SchoolYearTerm syt = SchoolYearTermUtil.getSchoolYearByDate(entity.getStarttime(), entity.getEndtime());
            entity.setSchoolyear(syt.getSchoolyear());
            entity.setTerm(syt.getTerm());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        entity.setWriteTime(new Date());
        jobActivityRecordService.insertPsyact(entity);
        logservice.log(request, "心理教育中心：教育活动", "添加活动记");
        return redirectToUrl(viewName("act/list.do"));
    }

    @RequestMapping(value = "act/{id}/view")
    public String viewAct(@CurrentOrg Organization org, JobActivityRecordPsyactWithBLOBs entity, Model model) {
        entity = jobActivityRecordService.findPsyactById(entity);
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        model.addAttribute("entity", entity);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobActivityRecordService.resourceIdentity_act);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("act/view");
    }

    @RequestMapping(value = "act/{id}/update", method = RequestMethod.GET)
    public String updateAct(@CurrentOrg Organization org, JobActivityRecordPsyactWithBLOBs entity, Model model) {
        JobActivityRecordPsyactWithBLOBs showentity = jobActivityRecordService.findPsyactById(entity);
        List<Dictionary> levellist = DictionaryService.selectAllDic("dic_job_activity_level");
        model.addAttribute("levellist", levellist);
        model.addAttribute("entity", showentity);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='2'");
        model.addAttribute("plantype", plantype);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobActivityRecordService.resourceIdentity_act);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("act/editform");
    }

    @RequestMapping(value = "act/{id}/update", method = RequestMethod.POST)
    public String formupdateAct(HttpServletRequest request, JobActivityRecordPsyactWithBLOBs entity, Model model) {
        try {
            SchoolYearTerm syt = SchoolYearTermUtil.getSchoolYearByDate(entity.getStarttime(), entity.getEndtime());
            entity.setSchoolyear(syt.getSchoolyear());
            entity.setTerm(syt.getTerm());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        jobActivityRecordService.updatePsyact(entity);
        logservice.log(request, "心理教育中心：教育活动", "修改活动记");
        return redirectToUrl(viewName("act/list.do"));
    }

    @RequestMapping(value = "act/{id}/delete")
    public String deleteAct(HttpServletRequest request, JobActivityRecordPsyactWithBLOBs entity, Model model) {
        jobActivityRecordService.deletePsyact(entity);
        return redirectToUrl(viewName("act/list.do"));
    }

    @RequestMapping(value = "act/delselected")
    public String actdeleteSelect(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobActivityRecordService.deletePsyact(id);
            }
        }
        logservice.log(request, "心理教育中心：教育活动", "批量修改活动记");
        return redirectToUrl(viewName("act/list.do"));
    }

    @RequestMapping(value = "act/file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long actfiledel(HttpServletRequest request, @PathVariable("entityid") Long entityid,
            @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobActivityRecordService.resourceIdentity_act);
        // logservice.log(request,"心理教育中心：活动记录","删除活动记");
        return fileid;
    }

    @RequestMapping(value = "act/importform")
    public String actimportform(@CurrentUser Account account, @CurrentOrg Organization org, Model model) {
        return viewName("act/importform");
    }

    // ----------------------------------------------------------------------------------------------------
    @RequestMapping(value = "research/list", method = RequestMethod.GET)
    public String researchlist(@CurrentOrg Organization org, JobActivityRecordPsyresearchWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());

        List<JobActivityRecordPsyresearchWithBLOBs> list = jobActivityRecordService.findPsyresearchByPage(entity, page);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='3'");
        model.addAttribute("plantype", plantype);
        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("research/list");
    }

    @RequestMapping(value = "research/querydata")
    public String getResearchData(@CurrentOrg Organization org, JobActivityRecordPsyresearchWithBLOBs entity,
            @PageAnnotation PageParameter page, Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        model.addAttribute("list", jobActivityRecordService.findPsyresearchByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("research/tablelist");
    }

    @RequestMapping(value = "research/add")
    public String addResearch(@CurrentOrg Organization org, JobActivityRecordPsyresearchWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='3'");
        List<Dictionary> researchStep = DictionaryService.selectAllDic("dic_job_activity_researchstep");
        model.addAttribute("plantype", plantype);
        model.addAttribute("researchStep", researchStep);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("research/editform");
    }

    @RequestMapping(value = "research/create")
    public String createResearch(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobActivityRecordPsyresearchWithBLOBs entity, Model model) {
        try {
            SchoolYearTerm syt = SchoolYearTermUtil.getSchoolYearByDate(entity.getStarttime(), entity.getEndtime());
            entity.setSchoolyear(syt.getSchoolyear());
            entity.setTerm(syt.getTerm());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        entity.setWriteTime(new Date());
        jobActivityRecordService.insertPsyresearch(entity);
        logservice.log(request, "心理教育中心：教育活动", "添加心理教科研");
        return redirectToUrl(viewName("research/list.do"));
    }

    @RequestMapping(value = "research/{id}/view")
    public String viewResearch(@CurrentOrg Organization org, JobActivityRecordPsyresearchWithBLOBs entity,
            Model model) {
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
        return viewName("research/view");
    }

    @RequestMapping(value = "research/{id}/update", method = RequestMethod.GET)
    public String updateResearch(@CurrentOrg Organization org, JobActivityRecordPsyresearchWithBLOBs entity,
            Model model) {
        JobActivityRecordPsyresearchWithBLOBs showentity = jobActivityRecordService.findPsyresearchById(entity);
        model.addAttribute("entity", showentity);
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", "catid='3'");
        List<Dictionary> researchStep = DictionaryService.selectAllDic("dic_job_activity_researchstep");
        model.addAttribute("plantype", plantype);
        model.addAttribute("researchStep", researchStep);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobActivityRecordService.resourceIdentity_research);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("research/editform");
    }

    @RequestMapping(value = "research/{id}/update", method = RequestMethod.POST)
    public String formupdateResearch(HttpServletRequest request, JobActivityRecordPsyresearchWithBLOBs entity,
            Model model) {
        try {
            SchoolYearTerm syt = SchoolYearTermUtil.getSchoolYearByDate(entity.getStarttime(), entity.getEndtime());
            entity.setSchoolyear(syt.getSchoolyear());
            entity.setTerm(syt.getTerm());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        jobActivityRecordService.updatePsyresearch(entity);
        logservice.log(request, "心理教育中心：教育活动", "修改心理教科研");
        return redirectToUrl(viewName("research/list.do"));
    }

    @RequestMapping(value = "research/{id}/delete")
    public String deleteResearch(HttpServletRequest request, JobActivityRecordPsyresearchWithBLOBs entity,
            Model model) {
        jobActivityRecordService.deletePsyresearch(entity);
        logservice.log(request, "心理教育中心：教育活动", "删除心理教科研");
        return redirectToUrl(viewName("research/list.do"));
    }

    @RequestMapping(value = "research/delselected")
    public String researchdeleteSelect(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobActivityRecordService.deletePsyresearch(id);
            }
        }
        logservice.log(request, "心理教育中心：教育活动", "批量删除心理教科研");
        return redirectToUrl(viewName("research/list.do"));
    }

    @RequestMapping(value = "research/file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long filedel(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobActivityRecordService.resourceIdentity_research);
        return fileid;
    }

    @RequestMapping(value = "research/importform")
    public String researchimportform(@CurrentUser Account account, @CurrentOrg Organization org, Model model) {
        return viewName("research/importform");
    }

    @RequestMapping(value = "{file}/downloadtemplate", method = RequestMethod.GET)
    public String downloadresearchtemplate(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("file") String file) {
        try {
            file += ".xlsx";
            String filename = FileOperate.encodeFilename(file, request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/workschedule/" + file);
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
