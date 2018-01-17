package com.njpes.www.action.systeminfo;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;
import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Menu;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.UserResPerm;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.service.baseinfo.AuthServiceI;
import com.njpes.www.service.baseinfo.ResourceServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.UserResPermServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;

@Controller("adminIndexController")
@RequestMapping("/systeminfo/index")
public class IndexController extends BaseController {

    @Autowired
    private ResourceServiceI resourceService;

    @Autowired
    private AuthServiceI authService;
    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private UserResPermServiceI userResPermService;

    @RequestMapping(value = { "/{index.do:index.do;?.*}" })
    // spring3.2.2 bug see http://jinnianshilongnian.iteye.com/blog/1831408
    public String index(Model model, HttpServletRequest request) {
        Account user = (Account) request.getAttribute(Constants.CURRENT_USER);
        // 需要重新加载用户信息，保证在刷新页面可以读取更改后信息 2016/9/9

        // ----------------------
        // 如果是学生用户登录
        if (user.getTypeFlag() != null && user.getTypeFlag().intValue() == AcountTypeFlag.student.getId()) {
            Role r = roleService.selectRole(Long.valueOf(39));// 学生的角色id在role表中记录
            Set<Role> roles = Sets.newHashSet();
            roles.add(r);
            user.setRoles(roles);
        }
        // 如果是家长用户登录
        else if (user.getTypeFlag() != null && user.getTypeFlag().intValue() == AcountTypeFlag.parent.getId()) {
            Role r = roleService.selectRole(Long.valueOf(44));// 家长的角色id在role表中记录
            Set<Role> roles = Sets.newHashSet();
            roles.add(r);
            user.setRoles(roles);

        } else {
            Set<Role> roles = authService.findRoles(user.getId(), user.getLogintype());
            List<UserResPerm> userResPermList = userResPermService.findResPermByUser(user.getId(), user.getLogintype());
            user.setRoles(roles);
            user.setUserResPermsList(userResPermList);
        }

        request.getSession().setAttribute(Constants.CURRENT_USER, user);

        List<Menu> menus = resourceService.findMenus(user);
        model.addAttribute("menus", menus);

        List<AccountOrgJob> organizationJobs = user.getOrganizationJobs();
        if (organizationJobs == null || organizationJobs.size() == 0) {
            return null;
        }
        long orgid = organizationJobs.get(0).getOrgId();
        Organization orgEntity = organizationService.selectOrganizationById(orgid);
        request.getSession().setAttribute(Constants.CURRENT_USER_ORG, orgEntity);
        model.addAttribute("user_org", orgEntity);
        model.addAttribute("appheadtitle", Constants.APPLICATION_APPHEADTITLE);
        model.addAttribute("appfooter", Constants.APPLICATION_APPFOOTER);
        return viewName("index");
    }

}
