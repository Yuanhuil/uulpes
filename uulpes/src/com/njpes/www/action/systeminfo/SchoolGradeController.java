package com.njpes.www.action.systeminfo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.service.baseinfo.organization.SchoolGradeServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;

/**
 * 组织机构控制器 该类暂时没有使用，待以后扩展使用，与之相关的service mapper entity待日后扩展使用
 * 
 * @author 赵忠诚
 */
@Controller
@RequestMapping(value = "/systeminfo/sys/organization/grade")
public class SchoolGradeController extends BaseController {

    @Autowired
    private SchoolGradeServiceI schoolGradeService;
    @Autowired
    private SchoolServiceI schoolService;

    /**
     * 根据学制和办学类型确定年级情况，只能查看不能修改和添加，如果需要添加只能在 学校的办学类型中添加
     */
    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, Model model) {
        if (!StringUtils.equals(org.getOrgType(), OrganizationType.school.getId())) {
            return redirectToUrl("/noAuth.jsp");
        }
        long schoolorgid = org.getId();
        List<Grade> njinfos = schoolService.getGradeListInSchool(schoolorgid);
        model.addAttribute("list", njinfos);
        return viewName("list");
    }
}
