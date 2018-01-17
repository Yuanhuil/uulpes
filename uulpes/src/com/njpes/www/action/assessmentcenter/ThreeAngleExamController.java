package com.njpes.www.action.assessmentcenter;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.assessmentcenter.DataManageFilterParam;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.GradeCodeServiceImpl;
import com.njpes.www.service.baseinfo.ParentServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;

@Controller
@RequestMapping(value = "/assessmentcenter/threeangle")
public class ThreeAngleExamController extends BaseController {
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    private ExamdoServiceI examdoService;
    @Autowired
    public CachedScaleMgr cachedScaleMgr;
    @Autowired
    private ReportLookService reportLookService;
    @Autowired
    DistrictService districtService;
    @Autowired
    TeacherServiceI teacherService;
    @Autowired
    ParentServiceI parentService;
    @Autowired
    GradeCodeServiceImpl gradeCodeService;

    // 点击导入数据tab的action
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, @PageAnnotation PageParameter page, HttpServletRequest request,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam) throws Exception {
        if (dataManageFilterParam == null)
            dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        dataManageFilterParam.setOrgid(org.getId());
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        // 添加分页的过滤
        request.setAttribute("page", page);
        Account account = (Account) request.getSession().getAttribute("user");
        boolean canThreeAngleExam = false;
        Set<Role> roles = account.getRoles();
        for (Role role : roles) {
            String rolename = role.getRoleName();
            if (rolename.contains("班主任") || rolename.contains("家长"))
                canThreeAngleExam = true;
        }
        if (!canThreeAngleExam)
            throw new Exception("只有班主任和家长才可以对学生进行三方共测！");
        List examdoList = null;
        int typeflag = account.getTypeFlag();
        if (typeflag == AcountTypeFlag.teacher.getId()) {
            Teacher teacher = teacherService.getTeacherByAccountId(account.getId());
            dataManageFilterParam.setThreeAngleUserId(teacher.getId());
            // 为页面添加下拉列表的东西
            examdoList = examdoService.queryExamdoMhForTeacher(page, dataManageFilterParam);
        }
        if (typeflag == AcountTypeFlag.parent.getId()) {
            Parent parent = parentService.getParentByAccountId(account.getId());
            dataManageFilterParam.setThreeAngleUserId(parent.getId());
            // 为页面添加下拉列表的东西
            examdoList = examdoService.queryExamdoMhForParent(page, dataManageFilterParam);
        }
        request.setAttribute("typeflag", typeflag);
        request.setAttribute("examdoList", examdoList);
        return viewName("threeangle");
    }

    @RequestMapping(value = "searchthreeanglelist", method = RequestMethod.POST)
    public String searchThreeAngleList(@CurrentOrg Organization org, @PageAnnotation PageParameter page,
            HttpServletRequest request,
            @ModelAttribute("dataManageFilterParam") DataManageFilterParam dataManageFilterParam) throws Exception {
        if (dataManageFilterParam == null)
            dataManageFilterParam = new DataManageFilterParam();
        request.setAttribute("dataManageFilterParam", dataManageFilterParam);
        dataManageFilterParam.setOrgid(org.getId());
        // 把用户的类型添加到request中去
        OrganizationType ot = org.returnOrgTypeEnum();
        // 添加分页的过滤
        request.setAttribute("page", page);
        Account account = (Account) request.getSession().getAttribute("user");

        List examdoList = null;
        int typeflag = account.getTypeFlag();
        if (typeflag == AcountTypeFlag.teacher.getId()) {
            Teacher teacher = teacherService.getTeacherByAccountId(account.getId());
            dataManageFilterParam.setThreeAngleUserId(teacher.getId());
            // 为页面添加下拉列表的东西
            examdoList = examdoService.queryExamdoMhForTeacher(page, dataManageFilterParam);
        }
        if (typeflag == AcountTypeFlag.parent.getId()) {
            Parent parent = parentService.getParentByAccountId(account.getId());
            dataManageFilterParam.setThreeAngleUserId(parent.getId());
            // 为页面添加下拉列表的东西
            examdoList = examdoService.queryExamdoMhForParent(page, dataManageFilterParam);
        }
        request.setAttribute("typeflag", typeflag);
        request.setAttribute("examdoList", examdoList);
        return viewName("threeangletablecon");
    }

}
