package com.njpes.www.action.homepage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.dao.workschedule.JobAttachmentMappingMapper;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Introduce;
import com.njpes.www.entity.workschedule.JobActivityPlanWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivitySummaryWithBLOBs;
import com.njpes.www.entity.workschedule.JobNoticeWebQueryParam;
import com.njpes.www.entity.workschedule.JobNoticeWithBLOBs;
import com.njpes.www.service.consultcenter.IntroduceServiceI;
import com.njpes.www.service.workschedule.JobActivityPlanServiceI;
import com.njpes.www.service.workschedule.JobActivitySummaryServiceI;
import com.njpes.www.service.workschedule.JobNoticeServiceI;
import com.njpes.www.utils.PageParameter;

/**
 * 登陆控制
 * 
 * @author 赵忠诚
 */
@Controller
@RequestMapping(value = "/homepage")
public class HomePageController extends BaseController {

    @Autowired
    private JobNoticeServiceI jobNoticeService;

    @Autowired
    private JobActivityPlanServiceI jobActivityPlanService;

    @Autowired
    private JobActivitySummaryServiceI jobActivitySummaryService;

    @Autowired
    private JobAttachmentMappingMapper jobAttachmentMappingMapper;
    @Autowired
    private IntroduceServiceI introduceService;

    @RequestMapping(value = "homepage")
    public String index(@CurrentOrg Organization org, Model model, HttpServletRequest request,
            @PageAnnotation PageParameter page) {
        JobNoticeWebQueryParam e = new JobNoticeWebQueryParam();
        e.setDep(org.getId());
        e.setState("3");
        JobActivitySummaryWithBLOBs js = new JobActivitySummaryWithBLOBs();
        js.setDep(org.getId());
        JobActivityPlanWithBLOBs jp = new JobActivityPlanWithBLOBs();
        page.setPageSize(8);

        // e.setShare(BooleanStringEnum.TRUE.getId());
        jp.setDep(org.getId());
        jp.setState("3");

        List<JobNoticeWithBLOBs> jobnoticelist = jobNoticeService.findAllNoticeByPage(e, page);
        if (org.getOrgLevel() == 6 && Integer.parseInt(Constants.APPLICATION_USERLEVEL) < 6) {
            e.setDep(org.getParentId());
            List<JobNoticeWithBLOBs> qxJobnoticelist = jobNoticeService.findAllNoticeByPage(e, page);
            model.addAttribute("qxJobnoticelist", qxJobnoticelist);
        }
        for (JobNoticeWithBLOBs a : jobnoticelist) {
            String imageurl = jobAttachmentMappingMapper.selectimageUrlShowFirstpage(jobNoticeService.resourceIdentity,
                    a.getId());
            if (StringUtils.isNotBlank(imageurl)) {
                a.setFirstpageimageUrl(imageurl);
            }
        }
        List<JobActivitySummaryWithBLOBs> jobsummarylist = jobActivitySummaryService.findByPage(js, page);

        List<JobActivityPlanWithBLOBs> jobplanlist = jobActivityPlanService.findByPage(jp, page);
        Introduce introduce = introduceService.selectByPrimaryKey(org.getId());
        model.addAttribute("orgid", org.getId());
        model.addAttribute("parentorgid", org.getParentId());
        model.addAttribute("orgtype", org.getOrgType());
        model.addAttribute("orglevel", org.getOrgLevel());
        model.addAttribute("orgintroduce", org.getIntroduce());
        model.addAttribute("orgimage", org.getImageurl());
        model.addAttribute("introduce", introduce);
        model.addAttribute("jobnoticelist", jobnoticelist);
        model.addAttribute("jobsummarylist", jobsummarylist);
        model.addAttribute("jobplanlist", jobplanlist);
        model.addAttribute("userlevel", Constants.APPLICATION_USERLEVEL);
        return viewName("indexPage");
    }

    @RequestMapping(value = "homepage_nologin")
    public String index_nologin(Model model, HttpServletRequest request, @PageAnnotation PageParameter page) {
        JobNoticeWebQueryParam e = new JobNoticeWebQueryParam();
        JobActivitySummaryWithBLOBs js = new JobActivitySummaryWithBLOBs();
        JobActivityPlanWithBLOBs jp = new JobActivityPlanWithBLOBs();
        page.setPageSize(8);
        e.setState("3");
        // e.setShare(BooleanStringEnum.TRUE.getId());
        List<JobNoticeWithBLOBs> jobnoticelist = jobNoticeService.findAllNoticeByPage(e, page);

        /*
         * for(JobNoticeWithBLOBs a : jobnoticelist){ String imageurl =
         * jobAttachmentMappingMapper.selectimageUrlShowFirstpage(
         * jobNoticeService.resourceIdentity, a.getId());
         * if(StringUtils.isNotBlank(imageurl)){
         * a.setFirstpageimageUrl(imageurl); } }
         */

        List<JobActivitySummaryWithBLOBs> jobsummarylist = jobActivitySummaryService.findByPage(js, page);
        jp.setState("3");
        List<JobActivityPlanWithBLOBs> jobplanlist = jobActivityPlanService.findByPage(jp, page);
        model.addAttribute("jobnoticelist", jobnoticelist);
        model.addAttribute("jobsummarylist", jobsummarylist);
        model.addAttribute("jobplanlist", jobplanlist);
        model.addAttribute("userlevel", Constants.APPLICATION_USERLEVEL);
        model.addAttribute("appheadtitle", Constants.APPLICATION_APPHEADTITLE);
        model.addAttribute("appfooter", Constants.APPLICATION_APPFOOTER);
        return viewName("firstindex");
    }

    @RequestMapping(value = "{orgid}/morenotice")
    public String morenotice(Model model, HttpServletRequest request, @PageAnnotation PageParameter page,
            @PathVariable("orgid") long orgid) {
        String pagequery = request.getParameter("pagequery");
        JobNoticeWebQueryParam e = new JobNoticeWebQueryParam();
        // page.setPageSize(3);
        if (orgid > -1)
            e.setDep(orgid);
        e.setState("3");
        List<JobNoticeWithBLOBs> jobnoticelist = jobNoticeService.findAllNoticeByPage(e, page);
        model.addAttribute("jobnoticelist", jobnoticelist);
        model.addAttribute("appheadtitle", Constants.APPLICATION_APPHEADTITLE);
        model.addAttribute("appfooter", Constants.APPLICATION_APPFOOTER);
        model.addAttribute("page", page);
        if (pagequery == null)
            return viewName("morenotice");
        else
            return viewName("noticetablelist");
    }

    @RequestMapping(value = "{orgid}/moreactivityplan")
    public String moreactivityplan(Model model, HttpServletRequest request, @PageAnnotation PageParameter page,
            @PathVariable("orgid") long orgid) {
        JobActivityPlanWithBLOBs jp = new JobActivityPlanWithBLOBs();
        // page.setPageSize(3);
        String pagequery = request.getParameter("pagequery");
        if (orgid > -1)
            jp.setDep(orgid);
        jp.setState("3");
        List<JobActivityPlanWithBLOBs> jobplanlist = jobActivityPlanService.findByPage(jp, page);
        model.addAttribute("jobplanlist", jobplanlist);
        model.addAttribute("appheadtitle", Constants.APPLICATION_APPHEADTITLE);
        model.addAttribute("appfooter", Constants.APPLICATION_APPFOOTER);
        model.addAttribute("page", page);
        if (pagequery == null)
            return viewName("moreactivityplan");
        else
            return viewName("activityplantablelist");
    }

    @RequestMapping(value = "{orgid}/moreactivitysummary")
    public String moreactivitysummary(Model model, HttpServletRequest request, @PageAnnotation PageParameter page,
            @PathVariable("orgid") long orgid) {
        JobActivitySummaryWithBLOBs js = new JobActivitySummaryWithBLOBs();
        if (orgid > -1)
            js.setDep(orgid);
        // page.setPageSize(3);
        List<JobActivitySummaryWithBLOBs> jobsummarylist = jobActivitySummaryService.findByPage(js, page);
        String pagequery = request.getParameter("pagequery");
        model.addAttribute("jobsummarylist", jobsummarylist);
        model.addAttribute("appheadtitle", Constants.APPLICATION_APPHEADTITLE);
        model.addAttribute("appfooter", Constants.APPLICATION_APPFOOTER);
        model.addAttribute("page", page);
        if (pagequery == null)
            return viewName("moreactivitysummary");
        else
            return viewName("activitysummarytablelist");
    }
}
