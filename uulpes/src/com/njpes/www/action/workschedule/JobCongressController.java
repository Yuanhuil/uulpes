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
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;
import com.njpes.www.entity.workschedule.JobCongressWithBLOBs;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.workschedule.JobCongressServiceI;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;

import heracles.web.config.ApplicationConfiguration;

@Controller
@RequestMapping(value = "/workschedule/congress")
public class JobCongressController extends BaseController {
    @Autowired
    private JobCongressServiceI jobCongressService;
    @Autowired
    private SyslogServiceI logservice;

    public JobCongressController() {
        setResourceIdentity(jobCongressService.resourceIdentity);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, JobCongressWithBLOBs entity, @PageAnnotation PageParameter page,
            Model model) {
        entity.setDep(org.getId());
        // 该组织发的notice
        List<JobCongressWithBLOBs> list = jobCongressService.findByPage(entity, page);

        model.addAttribute("entity", entity);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = "querydata")
    public String getData(@CurrentOrg Organization org, JobCongressWithBLOBs entity, @PageAnnotation PageParameter page,
            Model model) {
        if (!org.getId().equals(Constants.CHINA_ADMIN))
            entity.setDep(org.getId());
        model.addAttribute("list", jobCongressService.findByPage(entity, page));
        model.addAttribute("page", page);
        return viewName("tablelist");
    }

    @RequestMapping(value = "add")
    public String add(@CurrentOrg Organization org, JobCongressWithBLOBs entity, Model model) {
        model.addAttribute("entity", entity);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "create")
    public String create(HttpServletRequest request, @CurrentUser Account account, @CurrentOrg Organization org,
            JobCongressWithBLOBs entity, Model model) {
        entity.setAuthor(account.getId());
        entity.setDep(org.getId());
        entity.setWriteTime(new Date());
        jobCongressService.insert(entity);
        logservice.log(request, "心理教育中心：会议记录", "添加会议记录");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String update(@CurrentOrg Organization org, JobCongressWithBLOBs entity, Model model) {
        JobCongressWithBLOBs showentity = jobCongressService.findById(entity);
        model.addAttribute("entity", showentity);
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        model.addAttribute("schoolterm", schoolterm);
        model.addAttribute("plancatalog", planCatalog);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobCongressService.resourceIdentity);
        model.addAttribute("attachments", atts);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("editform");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String formupdate(HttpServletRequest request, JobCongressWithBLOBs entity, Model model) {
        jobCongressService.update(entity);
        logservice.log(request, "心理教育中心：会议记录", "修改会议记录");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/delete")
    public String delete(HttpServletRequest request, JobCongressWithBLOBs entity, Model model) {
        jobCongressService.delete(entity);
        logservice.log(request, "心理教育中心：会议记录", "删除会议记录");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "delselected")
    public String delselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                jobCongressService.delete(id);
            }
        }
        logservice.log(request, "心理教育中心：会议记录", "批量删除会议记录");
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "{id}/view")
    public String view(@CurrentOrg Organization org, JobCongressWithBLOBs entity, Model model) {
        entity = jobCongressService.findById(entity);
        model.addAttribute("entity", entity);
        List<JobAttachmentMapping> atts = jobAttachmentMappnigService.selectFileIdsByFidResource(entity.getId(),
                jobCongressService.resourceIdentity);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        model.addAttribute("attachments", atts);
        return viewName("view");
    }

    @RequestMapping(value = "file/{entityid}/{fileid}/del")
    @ResponseBody
    public Long filedel(@PathVariable("entityid") Long entityid, @PathVariable("fileid") Long fileid) {
        jobAttachmentMappnigService.delete(entityid, fileid, jobCongressService.resourceIdentity);
        return fileid;
    }

    @RequestMapping(value = "importform")
    public String importform(@CurrentUser Account account, @CurrentOrg Organization org, Model model) {
        return viewName("importform");
    }

    @RequestMapping(value = "downloadtemplate", method = RequestMethod.GET)
    public String downloadtemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("导入会议记录_新模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/workschedule/导入会议记录_新模板.xlsx");
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
