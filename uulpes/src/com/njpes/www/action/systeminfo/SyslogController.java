package com.njpes.www.action.systeminfo;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Syslog;
import com.njpes.www.entity.baseinfo.SyslogFilterParam;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.service.baseinfo.PermissionServiceI;
import com.njpes.www.service.baseinfo.ResourceServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationLevelServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.utils.ExcelUtils;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/systeminfo/sys")
public class SyslogController extends BaseController {
    @Autowired
    private SyslogServiceI syslogService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private OrganizationLevelServiceI organizationLevelService;
    @Autowired
    private PermissionServiceI permissionService;
    @Autowired
    private ResourceServiceI resourceService;

    public SyslogController() {
        setResourceIdentity("syslog:syslog");
    }

    @RequestMapping("/querySyslogs")
    public String querySyslogs(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        int orglevel = orgEntity.getOrgLevel();
        String orgtype = orgEntity.getOrgType();
        List<Role> roles = null;
        // 市级
        if (orglevel == 3) {
            request.setAttribute("cityId", Constants.APPLICATION_CITYCODE);
        }
        // 区县一级
        if (orglevel == 4 && !orgtype.equals("2")) {
            List<Organization> schoollist = organizationService.getSchoolOrgByCountyId(orgEntity.getCountyid());
            request.setAttribute("schoollist", schoollist);
            roles = roleService.selectRolesByOrgLevelAndRoleLevel(orglevel, 4, orgtype);
        }
        if (orglevel == 6) {
            roles = roleService.selectRolesByOrgLevelAndRoleLevel(orglevel, 6, orgtype);
        }
        // List<Role> roles = this.roleService.selectAll();
        request.setAttribute("myorgid", orgEntity.getId());
        request.setAttribute("myorgname", orgEntity.getName());
        request.setAttribute("orglevel", orglevel);
        request.setAttribute("orgType", orgtype);
        request.setAttribute("roles", roles);
        request.setAttribute("syslogFilterParam", new SyslogFilterParam());
        return viewName("syslog/syslog");
    }

    @RequestMapping(value = "/searchSyslogs", method = RequestMethod.POST)
    public String searchSyslogs(@CurrentOrg Organization orgEntity,
            @ModelAttribute("syslogFilterParam") SyslogFilterParam syslogFilterParam,
            @PageAnnotation PageParameter page, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (orgEntity.getOrgLevel() == 6) {
            syslogFilterParam.setOrgid(orgEntity.getId().intValue());
        }
        List<Syslog> syslogList = this.syslogService.querySyslogByPage(page, syslogFilterParam);

        request.setAttribute("syslogList", syslogList);
        request.setAttribute("page", page);
        return viewName("syslog/syslogtable");
    }

    @RequestMapping(value = "/chageRole", method = RequestMethod.POST)
    @ResponseBody
    public String changeRole(@Param("orglevel") String orglevel, @Param("rolelevel") String rolelevel,
            @Param("orgtype") String orgtype, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Role> roles = roleService.selectRolesByOrgLevelAndRoleLevel(Integer.parseInt(orglevel),
                Integer.parseInt(orglevel), orgtype);
        Gson gson = new Gson();
        String scaleStr = gson.toJson(roles);
        return scaleStr;
    }

    /*
     * @RequestMapping(value="/pageSearchSyslogs",method=RequestMethod.POST)
     * public String pageSearchSyslogs(
     * 
     * @ModelAttribute("syslogFilterParam") SyslogFilterParam syslogFilterParam,
     * 
     * @RequestParam("pageNumber") int pageNumber, HttpServletRequest request,
     * HttpServletResponse response) throws IOException { List<Syslog>
     * syslogList = this.syslogService.queryPageSyslogEntity(syslogFilterParam);
     * PageSeparatorEntity pse = new PageSeparatorEntity();
     * pse.setItemCount(syslogList.size());
     * if(pse.getItemCount()%pse.getPercount()==0)
     * pse.setPageCount(pse.getItemCount()/pse.getPercount()); else
     * pse.setPageCount(pse.getItemCount()/pse.getPercount()+1);
     * pse.setPageNumber(pageNumber); if(syslogList.size()>pse.getPercount()){
     * syslogList=syslogList.subList(pse.getPercount()*(pageNumber-1),pse.
     * getPercount()*pageNumber); } request.setAttribute("syslogList",
     * syslogList); request.setAttribute("pageSeparatorEntity", pse); return
     * viewName("syslog/syslogtable"); }
     */
    @RequestMapping(value = "/downloadSyslogs", method = RequestMethod.POST)
    public void downloadSyslogs(@CurrentOrg Organization orgEntity,
            @ModelAttribute("syslogFilterParam") SyslogFilterParam syslogFilterParam, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String[] header = { "时间", "操作模块", "操作人", "操作内容" };
        if (orgEntity.getOrgLevel() == 6) {
            syslogFilterParam.setOrgid(orgEntity.getId().intValue());
        }
        List<Syslog> syslogList = this.syslogService.queryPageSyslogEntity(syslogFilterParam);
        ExcelUtils.exportExcel(response, "下载日志信息", header, syslogList);
    }

}
