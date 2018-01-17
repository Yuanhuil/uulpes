package com.njpes.www.action.systeminfo;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Menu;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.service.baseinfo.PermissionServiceI;
import com.njpes.www.service.baseinfo.ResourceServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationLevelServiceI;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/systeminfo/sys/role")
public class RoleInfoController extends BaseController {

    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private OrganizationLevelServiceI organizationLevelService;
    @Autowired
    private PermissionServiceI permissionService;
    @Autowired
    private ResourceServiceI resourceService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping("queryRolePage")
    public String queryRolePage(Model model, Role role, @PageAnnotation PageParameter page) {
        model.addAttribute("roleList", roleService.selectAllConditionRole(role, page));
        List<Role> allRole = this.roleService.selectAll();
        model.addAttribute("allRole", allRole);
        List<OrganizationLevel> orglevels = this.organizationLevelService.queryOrgLevelList();
        model.addAttribute("orglevels", orglevels);
        model.addAttribute("page", page);
        return viewName("list");
    }

    @RequestMapping(value = "searchRole", method = RequestMethod.POST)
    public String searchRole(Role role, @PageAnnotation PageParameter page, Model model) {
        List<Role> countRoleList = roleService.selectAllConditionRole(role, page);
        model.addAttribute("roleList", countRoleList);
        List<Role> allRole = this.roleService.selectAll();
        model.addAttribute("allRole", allRole);
        List<OrganizationLevel> orglevels = this.organizationLevelService.queryOrgLevelList();
        model.addAttribute("orglevels", orglevels);
        model.addAttribute("page", page);
        return viewName("roletable");
    }

    @RequestMapping(value = "pageSearchRole", method = RequestMethod.POST)
    public String pageSearchRole(Role role, @PageAnnotation PageParameter page, Model model) {
        List<Role> allRole = this.roleService.selectAll();
        List<Role> countRoleList = roleService.selectAllConditionRole(role, page);
        model.addAttribute("roleList", countRoleList);
        model.addAttribute("allRole", allRole);
        List<OrganizationLevel> orglevels = this.organizationLevelService.queryOrgLevelList();
        model.addAttribute("orglevels", orglevels);
        model.addAttribute("page", page);
        return viewName("roletable");
    }

    @RequestMapping(value = "alreadyAddRole", method = RequestMethod.GET)
    public String alreadyAddRole(Model model) throws IOException {
        Role role = new Role();
        List<OrganizationLevel> orglevels = this.organizationLevelService.queryOrgLevelList();
        model.addAttribute("orglevels", orglevels);
        model.addAttribute("entity", role);

        List<Menu> menus = resourceService.findAllMenus();
        model.addAttribute("allmenus", menus);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("editform");
    }

    @RequestMapping("{id}/deleteRole")
    public String deleteRole(HttpServletRequest request, Role role, Model model) throws IOException {
        this.roleService.deleteRoles(role.getId().toString());
        logservice.log(request, "基础信息中心:角色权限", "删除角色:" + role.getRoleName());
        return redirectToUrl(viewName("queryRolePage.do"));
    }

    @RequestMapping("uploadExcelToDb")
    public String uploadExcelToDb(@RequestParam("file") MultipartFile excelFile, ModelMap map) {
        if (!excelFile.getContentType().equals("text/plain")) {
            // 如果传入的不是文本文件
            map.put("result", "typeWrong");
            return "typeWrong";
        }
        try {

        } catch (Exception e) {

        }
        return "";
    }

    @RequestMapping("showOrNotRoleFlag")
    public String showOrNotRoleFlag(@RequestParam("isShow") long roleId, @RequestParam("roleId") String isShow) {
        this.roleService.showOrNotRoleFlag(roleId, isShow);
        return "ok";
    }

    @RequestMapping(value = "insertRole", method = RequestMethod.POST)
    public String addRole(HttpServletRequest request, Role roleentity,
            @RequestParam("perm_resource") String perm_resource) throws IOException {
        this.roleService.addRole(roleentity, perm_resource);
        logservice.log(request, "基础信息中心:角色权限", "删除角色:" + roleentity.getRoleName());
        return redirectToUrl(viewName("queryRolePage.do"));
    }

    @RequestMapping(value = "{id}/modifyRole", method = RequestMethod.POST)
    public String modifyRole(HttpServletRequest request, Role roleentity,
            @RequestParam("perm_resource") String allPermResource, HttpServletResponse response) throws IOException {
        this.roleService.updateRole(roleentity, allPermResource);
        logservice.log(request, "基础信息中心:角色权限", "更改角色" + roleentity.getRoleName() + "权限");
        return redirectToUrl(viewName("queryRolePage.do"));
    }

    @RequestMapping(value = "{id}/selectRole", method = RequestMethod.GET)
    public String selectRole(Role roleentity, Model model) throws IOException {
        List<OrganizationLevel> orglevels = this.organizationLevelService.queryOrgLevelList();
        Role role = this.roleService.selectRole(roleentity.getId());
        // 史斌增加查询所有的资源节点
        List<RoleResourcePermission> rrpList = this.roleService.selectRRPAccordingRoleId(roleentity.getId());
        Gson gson = new Gson();
        String rrpListStr = gson.toJson(rrpList);
        model.addAttribute("orglevels", orglevels);
        model.addAttribute("entity", role);
        model.addAttribute("rrp", rrpListStr);
        List<Menu> menus = resourceService.findAllMenus();
        model.addAttribute("allmenus", menus);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("editform");
    }

}
