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
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.DianXingGeAn;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.consultcenter.WarningTeacher;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.consultcenter.WarningTeacherServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 工作分析
 * @author zhangchao
 * @Date 2015-5-18 上午9:25:38
 */
@Controller
@RequestMapping(value = "/consultcenter/warningTeacher")
public class WarningTeacherController extends BaseController {

    @Autowired
    private WarningTeacherServiceI warningTeacherService;;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {

        WarningTeacher warningTeacher = new WarningTeacher();
        warningTeacher.setOrgId(orgEntity.getId());
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        warningTeacher.setIswarnsure(new Byte("0"));
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        List<ConsultationModel> consultationModels = getConsultationModels();
        List<WarningTeacher> warningTeacherList = getList(request, page, warningTeacher, null, null);
        // List<Role> roleList = roleService.selectAll();
        List<Role> roleList = roleService.selectRolesByRoleLevel(6, "2");
        model.addAttribute("rolelist", roleList);
        model.addAttribute("list", warningTeacherList);
        model.addAttribute("warningTeacher", warningTeacher);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        // Map m=new HashMap<Object, Object>();
        // m.put(1, "一级典型个案");
        // model.addAttribute("map", m);
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
    public String list(@CurrentOrg Organization orgEntity, WarningTeacher warningTeacher,
            @PageAnnotation PageParameter page,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        warningTeacher.setOrgId(orgEntity.getId());
        if (warningTeacher.getIswarnsure() == null) {
            warningTeacher.setIswarnsure(new Byte("0"));
        }
        List<WarningTeacher> listWarningTeacher = getList(request, page, warningTeacher, beginDate, endDate);
        model.addAttribute("list", listWarningTeacher);
        model.addAttribute("page", page);
        return viewName("list");
    }

    private List<WarningTeacher> getList(HttpServletRequest request, PageParameter page, WarningTeacher warningTeacher,
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

        List<WarningTeacher> warningTeacherList = warningTeacherService.selectListByEntity(warningTeacher, page,
                beginDate, endDate);
        return warningTeacherList;
    }

    @RequestMapping(value = { "/updateIswarnsure" })
    public String updateIswarnsure(WarningTeacher warningTeacher, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        int a = warningTeacherService.updateIswarnsure(warningTeacher);
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
        String scaleId1 = request.getParameter("scaleId1");
        if (scaleId1 != null && scaleId1.toString().length() > 0) {
            map.put("scaleId", scaleId1.toString());
        }
        String roleId1 = request.getParameter("roleId1");
        if (roleId1 != null && roleId1.toString().length() > 0) {
            map.put("roleId", roleId1.toString());
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
