package com.njpes.www.action.workschedule;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.workschedule.JobAttachment;
import com.njpes.www.service.workschedule.JobAttachmentServiceI;

@Controller
@RequestMapping(value = "/workschedule/jobattachment")
public class JobAttachmentController extends BaseController {

    @Autowired
    private JobAttachmentServiceI jobAttachmentService;

    @RequestMapping(value = { "save" })
    @ResponseBody
    public List<JobAttachment> save(@CurrentOrg Organization org, HttpServletRequest request,
            HttpServletResponse response) {
        MultipartHttpServletRequest mul = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = mul.getFiles("file");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        // return jobAttachmentService.saveFile(files,
        // Constants.UPLOAD_FILE_PATH + org.getId()+"_" + org.getName() + "/"
        // +year + "/" + month);
        return jobAttachmentService.saveFile(files,
                Constants.UPLOAD_FILE_PATH + org.getId() + "/" + year + "/" + month);
    }

    @RequestMapping(value = { "{id}/del" })
    @ResponseBody
    public int del(@CurrentOrg Organization org, @PathVariable("id") Long id) {
        return jobAttachmentService.delrecord(id);
    }

    @RequestMapping(value = { "{uuid}/firstpage" })
    @ResponseBody
    public int firstpage(@CurrentOrg Organization org, @PathVariable("uuid") String uuid, String showfirstpage) {
        JobAttachment record = jobAttachmentService.selectByUUid(uuid);
        record.setShowfirstpage(showfirstpage);
        return jobAttachmentService.update(record);
    }

    @RequestMapping(value = { "{uuid}/deluuid" })
    @ResponseBody
    public int deluuid(@CurrentOrg Organization org, @PathVariable("uuid") String uuid) {
        return jobAttachmentService.delrecord(uuid);
    }
}
