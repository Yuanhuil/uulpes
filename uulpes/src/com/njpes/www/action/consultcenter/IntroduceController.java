package com.njpes.www.action.consultcenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.action.util.CommonAttachmentController;
import com.njpes.www.entity.baseinfo.enums.SwitchEnum;
import com.njpes.www.entity.baseinfo.enums.TimeEnum;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.consultcenter.CoachAttachment;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.Introduce;
import com.njpes.www.entity.consultcenter.IntroduceJobTime;
import com.njpes.www.entity.consultcenter.IntroducePage;
import com.njpes.www.entity.util.CommonAttachment;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.consultcenter.CoachAttachmentServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.IntroduceJobTimeServiceI;
import com.njpes.www.service.consultcenter.IntroduceServiceI;
import com.njpes.www.service.util.CommonAttachmentServiceI;

/**
 * @Description: 中心简介
 * @author zhangchao
 * @Date 2015-5-18 上午9:23:34
 */
@Controller
@RequestMapping(value = "/consultcenter/introduce")
public class IntroduceController extends BaseController {

    @Autowired
    private IntroduceServiceI introduceService;

    @Autowired
    private CoachAttachmentServiceI coachAttachmentService;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private IntroduceJobTimeServiceI introduceJobTimeService;
    @Autowired
    private CommonAttachmentServiceI commonAttachmentService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/addOrUpdate" })
    // spring3.2.2 bug see http://jinnianshilongnian.iteye.com/blog/1831408
    public String addOrUpdate(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        IntroducePage introducePage = new IntroducePage();
        long id = orgEntity.getId();

        String imageIds = "";

        Introduce introduce = introduceService.selectByPrimaryKey(id);
        if (introduce == null) {
            introduce = new Introduce();
            introduce.setId(id);
        }
        List<IntroduceJobTime> introduceJobTimes = introduceJobTimeService.getListByFid(id);
        if (introduceJobTimes.size() == 0) {
            introduceJobTimes = new ArrayList<IntroduceJobTime>();
            for (int i = 1; i <= 7; i++) {
                IntroduceJobTime introduceJobTime1 = new IntroduceJobTime();
                introduceJobTime1.setWeek((short) i);
                introduceJobTime1.setStarttimeid(8);
                introduceJobTime1.setEndtimeid(17);
                introduceJobTimes.add(introduceJobTime1);
            }
        }
        // List<ConsultType>
        // consultTypes=consultTypeService.getListByFid(id);

        List<CoachAttachment> coachAttachments = coachAttachmentService.getByFid(id);
        for (CoachAttachment coachAttachment : coachAttachments) {
            imageIds += "," + coachAttachment.getFileid();
        }
        introducePage.setCoachAttachments(coachAttachments);
        // introducePage.setConsultTypes(consultTypes);
        introducePage.setIntroduce(introduce);
        introducePage.setIntroduceJobTimes(introduceJobTimes);

        model.addAttribute("timeEnum", TimeEnum.values());
        model.addAttribute("introducePage", introducePage);
        model.addAttribute("imageIds", imageIds);

        return viewName("editForm");
    }

    @RequestMapping(value = { "/view" })
    // spring3.2.2 bug see http://jinnianshilongnian.iteye.com/blog/1831408
    public String view(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        IntroducePage introducePage = new IntroducePage();
        long id = orgEntity.getId();
        if (id != 0) {
            Introduce introduce = introduceService.selectByPrimaryKey(id);
            List<IntroduceJobTime> introduceJobTimes = introduceJobTimeService.getListByFid(id);
            if (introduceJobTimes.size() == 0) {
                introduceJobTimes = new ArrayList<IntroduceJobTime>();
                for (int i = 1; i <= 7; i++) {
                    IntroduceJobTime introduceJobTime1 = new IntroduceJobTime();
                    introduceJobTime1.setWeek((short) i);
                    introduceJobTime1.setStarttimeid(8);
                    introduceJobTime1.setEndtimeid(17);
                    introduceJobTimes.add(introduceJobTime1);
                }
            }

            List<CoachAttachment> coachAttachments = coachAttachmentService.getByFid(id);
            List<CommonAttachment> attachments = new ArrayList<CommonAttachment>();
            for (CoachAttachment coachAttachment : coachAttachments) {
                CommonAttachment commonAttachment = commonAttachmentService
                        .selectByPrimaryKey(coachAttachment.getFileid());
                ;
                File file = null;
                if (commonAttachment != null) {
                    // path是指欲下载的文件的路径。
                    file = new File(request.getRealPath("/") + CommonAttachmentController.UPLOAD_FILE_DIR
                            + CommonAttachmentController.SLASH + commonAttachment.getSaveName()
                            + commonAttachment.getMineType());
                }
                if (!file.exists()) {
                    commonAttachment.setSaveName("themes/theme1/images/nopictrue");
                    commonAttachment.setMineType(".jpg");
                    commonAttachment.setSavePath("");
                }
                attachments.add(commonAttachment);
            }

            model.addAttribute("attachments", attachments);
            introducePage.setCoachAttachments(coachAttachments);
            // introducePage.setConsultTypes(consultTypes);
            introducePage.setIntroduce(introduce);
            introducePage.setIntroduceJobTimes(introduceJobTimes);
        }
        List<ConsultType> consultTypes = consultTypeService.getListByFid(id);
        model.addAttribute("introducePage", introducePage);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultType", new ConsultType());
        model.addAttribute("switchEnum", SwitchEnum.values());
        model.addAttribute("timeEnum", TimeEnum.valueMap());
        model.addAttribute("fid", id);
        return viewName("show");
    }

    /*
     * 保存或更新页面所有内容
     */
    @RequestMapping(value = { "/save" })
    // spring3.2.2 bug see http://jinnianshilongnian.iteye.com/blog/1831408
    public String save(IntroducePage introducePage, HttpServletRequest request, Model model) {
        String imageIds = request.getParameter("imageIds");
        List<CoachAttachment> coachAttachments = new ArrayList<CoachAttachment>();
        if (!StringUtils.isEmpty(imageIds)) {
            String[] ids = imageIds.split(",");
            HashMap<String, String> map = new HashMap<String, String>();
            for (String string : ids) {
                map.put(string, string);
            }
            for (String string : map.keySet()) {
                try {
                    if (string.length() > 0) {
                        CoachAttachment coachAttachment = new CoachAttachment();
                        coachAttachment.setFileid(Long.parseLong(string));
                        coachAttachment.setSort(0);
                        coachAttachments.add(coachAttachment);
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }

        }
        introducePage.setCoachAttachments(coachAttachments);

        String str = introduceService.saveIntroducePage(introducePage);
        logservice.log(request, "心理辅导中心:中心简介", "保存简介");
        model.addAttribute("result", str);
        return redirectToUrl(viewName("view.do?id=" + introducePage.getIntroduce().getId()));
    }

    /*
     * 根据简介id删除页面所有内容
     */
    @RequestMapping(value = { "/delete" })
    @ResponseBody
    // spring3.2.2 bug see http://jinnianshilongnian.iteye.com/blog/1831408
    public String delete(@RequestParam("introducePage") Introduce introduce, HttpServletRequest request, Model model) {
        String str = introduceService.deleteIntroducePage(introduce);
        logservice.log(request, "心理辅导中心:中心简介", "删除简介");
        return str;
    }

    @RequestMapping(value = { "/deleteImg" })
    @ResponseBody
    // spring3.2.2 bug see http://jinnianshilongnian.iteye.com/blog/1831408
    public String deleteImg(@RequestParam("id") long id, HttpServletRequest request, Model model) {
        CoachAttachment coachAttachment = coachAttachmentService.selectByPrimaryKey(id);
        if (coachAttachment != null) {
            CommonAttachment commonAttachment = new CommonAttachment();
            commonAttachment.setId(coachAttachment.getFileid());
            commonAttachmentService.delCommonAttachment(commonAttachment);
        }

        int s = coachAttachmentService.delCoachAttachment(coachAttachment);
        logservice.log(request, "心理辅导中心:中心简介", "删除图片");
        String str = "" + s;
        return str;
    }

}
