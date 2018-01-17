package com.njpes.www.action.consultcenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.enums.DianXingGeAn;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.WarningStudent;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.WarningStudentServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 工作分析
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/warningStudent")
public class WarningStudentController extends BaseController {

    @Autowired
    private WarningStudentServiceI warningStudentService;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        WarningStudent warningStudent = new WarningStudent();
        warningStudent.setOrgid(orgEntity.getId().intValue());
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        List<ConsultationModel> consultationModels = getConsultationModels();
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        warningStudent.setIswarnsure(new Byte("0"));
        List<WarningStudent> warningStudentList = getList(request, page, warningStudent, null, null);
        long schoolorgid = orgEntity.getId();
        /*
         * List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
         * if(xdlist!=null&&xdlist.size() == 1){ HashMap<XueDuan,List<Grade>>
         * xdNjMap = schoolService.getGradesInSchool(schoolorgid);
         * model.addAttribute("njlist", xdNjMap.get(xdlist.get(0))); }
         * 
         * model.addAttribute("xdlist", xdlist);
         */
        model.addAttribute("list", warningStudentList);
        model.addAttribute("warningStudent", warningStudent);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("warningLever", DianXingGeAn.values());
        // model.addAttribute("typeEnum",TeamTypeEnum.values() );
        model.addAttribute("page", page);
        return viewName("main");
    }

    private List<ConsultType> getOpenConsultTypes(long id) {
        return consultTypeService.getOpenListByFid(id);

    }

    private List<ConsultationModel> getConsultationModels() {
        return consultationModelService.selectAll();
    }

    @RequestMapping(value = { "/list" })
    public String list(@CurrentOrg Organization orgEntity, WarningStudent warningStudent,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        warningStudent.setOrgid(orgEntity.getId().intValue());
        if (warningStudent.getIswarnsure() == null) {
            warningStudent.setIswarnsure(new Byte("0"));
        }

        List<WarningStudent> listWarningStudent = getList(request, page, warningStudent, beginDate, endDate);
        model.addAttribute("list", listWarningStudent);
        model.addAttribute("page", page);
        return viewName("list");
    }

    private List<WarningStudent> getList(HttpServletRequest request, PageParameter page, WarningStudent warningStudent,
            Date beginDate, Date endDate) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        if (page == null) {
            int currentPage;
            int pageSize;
            String currentPageStr = request.getParameter("currentPage");
            String pageSizeStr = request.getParameter("pageSize");
            if (currentPageStr == null) {
                currentPage = 1;
            } else {
                currentPage = Integer.parseInt(currentPageStr);
            }
            pageSizeStr = Constants.PAGE_LIST_NUM;
            pageSize = Integer.parseInt(pageSizeStr);
            String url = request.getRequestURI();
            page = new PageParameter(currentPage, pageSize);
            page.setUrl(url);
        }

        List<WarningStudent> warningStudentList = warningStudentService.selectListByEntity(warningStudent, page,
                beginDate, endDate);
        return warningStudentList;
    }

    @RequestMapping(value = { "/updateIswarnsure" })
    public String updateIswarnsure(WarningStudent warningStudent, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        int a = warningStudentService.updateIswarnsure(warningStudent);
        HashMap<String, Object> map = new HashMap<String, Object>();

        String iswarnsure1 = request.getParameter("iswarnsure1");
        if (iswarnsure1 != null && iswarnsure1.toString().length() > 0) {
            map.put("iswarnsure", iswarnsure1.toString());
        }
        String warningGrade1 = request.getParameter("warningGrade1");
        if (warningGrade1 != null && warningGrade1.toString().length() > 0) {
            map.put("warningGrade", warningGrade1.toString());
        }
        String xbm = request.getParameter("xbm1");
        if (xbm != null && xbm.toString().length() > 0) {
            map.put("xbm", xbm.toString());
        }

        String xm = null;
        try {
            xm = URLDecoder.decode(request.getParameter("xm1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (xm != null && xm.toString().length() > 0) {
            map.put("xm", xm.toString());
        }

        String beginDate = request.getParameter("beginDate");
        if (beginDate != null && beginDate.toString().length() > 0) {
            map.put("beginDate", beginDate.toString());
        }
        String endDate = request.getParameter("endDate");
        if (endDate != null && endDate.toString().length() > 0) {
            map.put("endDate", endDate.toString());
        }
        redirectAttributes.addAllAttributes(map);
        return redirectToUrl(viewName("list.do"));

    }

}
