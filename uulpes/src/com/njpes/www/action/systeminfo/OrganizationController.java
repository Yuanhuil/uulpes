package com.njpes.www.action.systeminfo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicjob;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicteam;
import com.njpes.www.entity.baseinfo.organization.Educommission;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicteam;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.util.ZTree;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.organization.EcpsychicJobServiceI;
import com.njpes.www.service.baseinfo.organization.EcpsychicteamServiceI;
import com.njpes.www.service.baseinfo.organization.EduCommissionServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolpsychicJobServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolpsychicteamServiceI;
import com.njpes.www.service.util.DictionaryServiceI;

/**
 * 组织机构控制器
 *
 * @author 赵忠诚
 */
@Controller
@RequestMapping(value = "/systeminfo/sys/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private SchoolServiceI schoolService;

    @Autowired
    private EduCommissionServiceI eduCommissionService;

    @Autowired
    private AccountServiceI accountService;

    @Autowired
    private EcpsychicteamServiceI ecpsychicteamService;

    @Autowired
    private EcpsychicJobServiceI ecpsychicJobService;

    @Autowired
    private SchoolpsychicJobServiceI schoolpsychicJobService;

    @Autowired
    private SchoolpsychicteamServiceI schoolpsychicteamService;

    @Autowired
    private DictionaryServiceI DictionaryService;

    public OrganizationController() {
        setResourceIdentity("systeminfo:organization");
    }

    /**
     * 展示组织机构信息,v1.0版本只能实现在一个人在一个组织机构的情况，如果存在一个人在多个组织机构的情况
     * 目前处理是只取第一个组织机构信息，其他组织机构信息不管
     * 这个功能应该是具有管理员功能的人才可以使用，如果一个人在多个组织机构，并且在某一个机构是管理员 就会存在问题
     *
     * @author 赵忠诚
     * @throws Exception
     */
    @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
    public String list(HttpServletRequest request, @CurrentOrg Organization org, Model model, @CurrentUser Account user)
            throws Exception {
        Long orgid = 0L;
        // 获取登录用户所在的组织机构信息
        // 理论上一个用户在一个组织机构任职，但也存在减值和特殊情况，所以返回是list
        // 如果不为空的情况下，先默认取第一个记录，待有具体的业务时候再修改完善该功能
        orgid = org.getId();
        Organization orgEntity = organizationService.selectOrganizationById(orgid);
        String locked = orgEntity.getLocked();
        model.addAttribute("orgtype", orgEntity.getOrgType());

        model.addAttribute("orglevelist", DictionaryService.selectAllDic("dic_organizationlevel"));
        // 如果是教委
        if (StringUtils.equals(orgEntity.getOrgType(), OrganizationType.ec.getId())) {
            Educommission ecEntity = eduCommissionService.getECInfoOrgID(orgid);
            if (ecEntity == null) {
                throw new Exception("没有该机构信息");
            }
            List<Ecpsychicjob> ecpsychicjobs = eduCommissionService.getJobsByOrgid(ecEntity.getId());
            List<Ecpsychicteam> ecpsychicteams = eduCommissionService.getTeamsByOrgid(ecEntity.getId());
            ecEntity.setPsychicyJobs(ecpsychicjobs);
            ecEntity.setPsychicyTeams(ecpsychicteams);
            model.addAttribute("entity", ecEntity);

            if (!model.containsAttribute("ecteam")) {
                model.addAttribute("ecteam", new Ecpsychicteam());
            }
            if (!model.containsAttribute("ecjob")) {
                model.addAttribute("ecjob", new Ecpsychicjob());
            }
            // 获取该单位用户的所有信息
            if (user.getUsername().toString().equals("admin")) {
                model.addAttribute("editorgname", "1");
            }
            model.addAttribute("locked", locked);
            List<Account> usersInOrg = accountService.getAccountsByOrgid(orgid, AcountTypeFlag.ecuser.getId());
            model.addAttribute("usersInOrg", usersInOrg);
            // request.getSession().setAttribute("action", "查看本机构信息");
            return viewName("ec/list");
        } else if (StringUtils.equals(orgEntity.getOrgType(), OrganizationType.school.getId())) {// 如果是学校
            School entity = schoolService.getSchoolInfoByOrgId(orgid);
            List<SchoolPsychicjob> psychicjobs = schoolService.getJobsByOrgid(entity.getId());
            List<SchoolPsychicteam> psychicteams = schoolService.getTeamsByOrgid(entity.getId());
            entity.setPsychicyJobs(psychicjobs);
            entity.setPsychicyTeams(psychicteams);
            if (!model.containsAttribute("team")) {
                model.addAttribute("team", new SchoolPsychicteam());
            }
            if (!model.containsAttribute("job")) {
                model.addAttribute("job", new SchoolPsychicjob());
            }
            model.addAttribute("entity", entity);
            model.addAttribute("locked", locked);
            // 获得办学类型信息
            List<Dictionary> firstLevel = DictionaryService.selectAllDic("dic_school_jyhy");
            // List<Dictionary> xzList =
            // DictionaryService.selectAllDic("dic_school_xz");
            List<ZTree> ztreeBxlx = new ArrayList<ZTree>();
            for (Dictionary d : firstLevel) {
                ZTree tree = new ZTree();
                tree.setId(Long.parseLong(d.getId()));
                tree.setName(d.getName());
                tree.setpId(0L);
                ztreeBxlx.add(tree);
                String where = " id like '" + d.getId() + "%'";
                List<Dictionary> secondLevel = DictionaryService.selectAllDicWhere("dic_school_xxlbm", where);
                for (Dictionary dd : secondLevel) {
                    ZTree tree_2 = new ZTree();
                    tree_2.setId(Long.parseLong(dd.getId()));
                    tree_2.setName(dd.getName());
                    tree_2.setpId(Long.parseLong(d.getId()));
                    ztreeBxlx.add(tree_2);
                    /*
                     * where = " id like '" + dd.getId() + "%'" ;
                     * List<Dictionary> thirdLevel =
                     * DictionaryService.selectAllDicWhere("dic_school_bxlx",
                     * where); for(Dictionary ddd : thirdLevel){ ZTree tree_3 =
                     * new ZTree(); tree_3.setId(Long.parseLong(ddd.getId()));
                     * tree_3.setName(ddd.getName());
                     * tree_3.setpId(Long.parseLong(dd.getId()));
                     * ztreeBxlx.add(tree_3); }
                     */
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            // String ztreeBxlxJson = mapper.writeValueAsString(ztreeBxlx);
            List<Dictionary> list = DictionaryService.selectAllDicWhere("dic_school_bxlx","id="+entity.getXxbxlxm());
            model.addAttribute("bxlx", list);
            // model.addAttribute("xzlist",xzList);
            // request.getSession().setAttribute("action", "查看本机构信息");
            return viewName("school/list");
        } else {// 其他情况鬼才知道怎么会出现这种情况
            return viewName("noType");
        }
    }

    /**
     * 根据id更新组织机构内容
     *
     * @author 赵忠诚
     */
    @RequestMapping("ec/{orgid}/{orgtype}/update")
    public String updateEc(HttpServletRequest request, Educommission org, Model model,
            @PathVariable("orgid") Long orgid, @PathVariable("orgtype") String orgtype,
            RedirectAttributes redirectAttributes) {
        int c = -1;
        org.setId(orgid);
        c = eduCommissionService.updateEC(org);

        if (c > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "保存成功");
            request.getSession().setAttribute("action", "更新机构信息成功");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "保存失败");
            request.getSession().setAttribute("action", "更新机构信息失败");
        }
        return redirectToUrl(viewName("list.do"));
    }

    // -----------------------school op--------------------------------
    @RequestMapping("school/{id}/{orgtype}/update")
    public String updateSchool(HttpServletRequest request, School school, Model model, @PathVariable("id") Long id,
            @PathVariable("orgtype") String orgtype, RedirectAttributes redirectAttributes) {
        int c = -1;
        c = schoolService.update(school);
        if (c > 0) {
            redirectAttributes.addFlashAttribute(Constants.SUCCESE_MESSAGE, "保存成功");
            request.getSession().setAttribute("action", "更新学校信息成功");
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "保存失败");
            request.getSession().setAttribute("action", "更新学校信息成功");
        }
        return redirectToUrl(viewName("list.do"));
    }

    @RequestMapping(value = "school/{schoolid}/team/add", method = RequestMethod.GET)
    public String schooladdForm(@CurrentOrg Organization org, Model model, SchoolPsychicteam e,
            @PathVariable("schoolid") long schoolid) {
        model.addAttribute("team", e);
        List<Account> usersInOrg = accountService.getAccountsByOrgid(org.getId(), AcountTypeFlag.teacher.getId());
        model.addAttribute("usersInOrg", usersInOrg);
        return viewName("school/addteam");
    }

    @RequestMapping("school/{schoolid}/team/create")
    public String schoolcreateTeam(SchoolPsychicteam e, Model model, @PathVariable("schoolid") long schoolid) {
        int c = -1;
        c = schoolpsychicteamService.insert(e);
        List<SchoolPsychicteam> psychicteams = schoolService.getTeamsByOrgid(schoolid);
        model.addAttribute("psychicteams", psychicteams);
        return viewName("school/listteam");
    }

    @RequestMapping(value = "school/team/{id}/view", method = RequestMethod.GET)
    public String schoolview(@CurrentOrg Organization org, Model model, @PathVariable("id") int id) {
        SchoolPsychicteam e = schoolpsychicteamService.findById(id);
        model.addAttribute("team", e);
        List<Account> usersInOrg = accountService.getAccountsByOrgid(org.getId(), AcountTypeFlag.teacher.getId());
        model.addAttribute("usersInOrg", usersInOrg);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("school/addteam");
    }

    @RequestMapping(value = "school/team/{id}/update", method = RequestMethod.GET)
    public String schoolshowUpdateForm(@CurrentOrg Organization org, Model model, @PathVariable("id") int id) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        SchoolPsychicteam e = schoolpsychicteamService.findById(id);
        model.addAttribute("team", e);
        List<Account> usersInOrg = accountService.getAccountsByOrgid(org.getId(), AcountTypeFlag.teacher.getId());
        model.addAttribute("usersInOrg", usersInOrg);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("school/addteam");
    }

    @RequestMapping(value = "school/{schoolid}/team/{id}/delete", method = RequestMethod.GET)
    public String schooldelete(HttpServletRequest request, @CurrentOrg Organization org, Model model,
            SchoolPsychicteam e) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        schoolpsychicteamService.del(e);
        List<SchoolPsychicteam> psychicteams = schoolService.getTeamsByOrgid(e.getSchoolid());
        model.addAttribute("psychicteams", psychicteams);
        request.getSession().setAttribute("action", "删除心理队伍成功");
        return viewName("school/listteam");
    }

    @RequestMapping(value = "school/team/{id}/{schoolid}/update", method = RequestMethod.POST)
    public String schoolupdate(HttpServletRequest request, Model model, @PathVariable("id") int id,
            @PathVariable("schoolid") long schoolid, SchoolPsychicteam e) {

        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */

        int c = -1;
        c = schoolpsychicteamService.update(e);
        List<SchoolPsychicteam> psychicteams = schoolService.getTeamsByOrgid(schoolid);
        model.addAttribute("psychicteams", psychicteams);
        request.getSession().setAttribute("action", "更新心理队伍信息成功");
        return viewName("school/listteam");
    }

    // *******************job school
    @RequestMapping(value = "school/{schoolid}/job/add", method = RequestMethod.GET)
    public String schooladdFormJob(Model model, SchoolPsychicjob e, @PathVariable("schoolid") long schoolid) {
        model.addAttribute("job", e);
        return viewName("school/addjob");
    }

    @RequestMapping("school/{schoolid}/job/create")
    public String schoolcreateJob(HttpServletRequest request, SchoolPsychicjob e, Model model,
            @PathVariable("schoolid") long schoolid) {
        int c = -1;
        c = schoolpsychicJobService.insert(e);
        List<SchoolPsychicjob> psychicjobs = schoolService.getJobsByOrgid(schoolid);
        model.addAttribute("psychicyJobs", psychicjobs);
        request.getSession().setAttribute("action", "添加主要工作成功");
        return viewName("school/listjobs");
    }

    @RequestMapping(value = "school/job/{id}/view", method = RequestMethod.GET)
    public String schooljobview(Model model, @PathVariable("id") int id) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasViewPermission(); }
         */
        SchoolPsychicjob e = schoolpsychicJobService.findById(id);
        model.addAttribute("job", e);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("school/addjob");
    }

    @RequestMapping(value = "school/job/{id}/update", method = RequestMethod.GET)
    public String schoolshowJobUpdateForm(Model model, @PathVariable("id") int id) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        SchoolPsychicjob e = schoolpsychicJobService.findById(id);
        model.addAttribute("job", e);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("school/addjob");
    }

    @RequestMapping(value = "school/{ecid}/job/{id}/delete", method = RequestMethod.GET)
    public String schooldeletejob(HttpServletRequest request, Model model, SchoolPsychicjob e) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        schoolpsychicJobService.del(e);
        List<SchoolPsychicjob> psychicjobs = schoolService.getJobsByOrgid(e.getSchoolid());
        model.addAttribute("psychicyJobs", psychicjobs);
        request.getSession().setAttribute("action", "删除主要工作成功");
        return viewName("school/listjobs");
    }

    @RequestMapping(value = "school/job/{id}/{schoolid}/update", method = RequestMethod.POST)
    public String schoolupdateJob(HttpServletRequest request, Model model, @PathVariable("id") int id,
            @PathVariable("schoolid") long schoolid, SchoolPsychicjob e) {

        int c = -1;
        c = schoolpsychicJobService.update(e);
        List<SchoolPsychicjob> ecpsychicjobs = schoolService.getJobsByOrgid(e.getSchoolid());
        model.addAttribute("psychicyJobs", ecpsychicjobs);
        request.getSession().setAttribute("action", "更新主要工作信息成功");
        return viewName("school/listjobs");
    }

    // ----------------------------------------------
    /**
     * 增加组织机构的心理老师队伍
     *
     * @author 赵忠诚
     */
    @RequestMapping("ec/{ecid}/job/create")
    public String createJob(HttpServletRequest request, Ecpsychicjob e, Model model, @PathVariable("ecid") Long ecid) {
        int c = -1;
        c = ecpsychicJobService.insert(e);
        if (c == 1) {
            List<Ecpsychicjob> ecpsychicjobs = eduCommissionService.getJobsByOrgid(ecid);
            model.addAttribute("psychicyJobs", ecpsychicjobs);
            request.getSession().setAttribute("action", "添加主要工作成功");
            return viewName("ec/listjobs");
        } else {
            request.getSession().setAttribute("action", "添加主要工作失败");
            return viewName("ec/listjobs");
        }
    }

    /**
     * 增加组织机构的心理老师队伍
     *
     * @author 赵忠诚
     */
    @RequestMapping("ec/{ecid}/team/create")
    public String createTeam(HttpServletRequest request, Ecpsychicteam e, Model model,
            @PathVariable("ecid") Long ecid) {
        int c = -1;
        c = ecpsychicteamService.insert(e);
        if (c == 1) {
            List<Ecpsychicteam> ecpsychicteams = eduCommissionService.getTeamsByOrgid(ecid);
            model.addAttribute("ecpsychicteams", ecpsychicteams);
            request.getSession().setAttribute("action", "添加心理老师队伍成功");
            return viewName("ec/listteam");
        } else {
            request.getSession().setAttribute("action", "添加心理老师队伍失败");
            return viewName("ec/listteam");
        }
    }

    @RequestMapping(value = "ec/job/{id}/view", method = RequestMethod.GET)
    public String jobview(Model model, @PathVariable("id") int id) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasViewPermission(); }
         */
        Ecpsychicjob e = ecpsychicJobService.findById(id);
        model.addAttribute("ecjob", e);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("ec/addjob");
    }

    @RequestMapping(value = "ec/job/{id}/update", method = RequestMethod.GET)
    public String showJobUpdateForm(Model model, @PathVariable("id") int id) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        Ecpsychicjob e = ecpsychicJobService.findById(id);
        model.addAttribute("ecjob", e);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("ec/addjob");
    }

    @RequestMapping(value = "ec/{ecid}/job/{id}/delete", method = RequestMethod.GET)
    public String deletejob(HttpServletRequest request, Model model, Ecpsychicjob e,
            RedirectAttributes redirectAttributes) {
        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        int c = ecpsychicJobService.del(e);
        List<Ecpsychicjob> ecpsychicjobs = eduCommissionService.getJobsByOrgid(e.getEcid());
        model.addAttribute("psychicyJobs", ecpsychicjobs);
        if (c > 0) {
            model.addAttribute(Constants.SUCCESE_MESSAGE, "删除成功!");
            request.getSession().setAttribute("action", "删除主要工作成功");
        } else {
            model.addAttribute(Constants.ERROR_MESSAGE, "删除失败!");
            request.getSession().setAttribute("action", "删除主要工作失败");
        }
        return viewName("ec/listjobs");
    }

    @RequestMapping(value = "ec/job/{id}/{ecid}/update", method = RequestMethod.POST)
    public String updateJob(HttpServletRequest request, Model model, @PathVariable("id") int id,
            @PathVariable("ecid") long ecid, Ecpsychicjob e) {

        int c = -1;
        c = ecpsychicJobService.update(e);
        List<Ecpsychicjob> ecpsychicjobs = eduCommissionService.getJobsByOrgid(e.getEcid());
        model.addAttribute("psychicyJobs", ecpsychicjobs);
        request.getSession().setAttribute("action", "更新主要工作信息成功");
        return viewName("ec/listjobs");
    }

    @RequestMapping(value = "ec/{ecid}/job/add", method = RequestMethod.GET)
    public String addFormJob(Model model, Ecpsychicjob e, @PathVariable("ecid") long orgid) {

        model.addAttribute("ecjob", e);
        return viewName("ec/addjob");
    }

    @RequestMapping(value = "ec/team/{id}/view", method = RequestMethod.GET)
    public String view(Model model, @PathVariable("id") int id, @CurrentOrg Organization org) {
        Ecpsychicteam e = ecpsychicteamService.findById(id);
        model.addAttribute("ecteam", e);
        List<Account> usersInOrg = accountService.getAccountsByOrgid(org.getId(), AcountTypeFlag.ecuser.getId());
        model.addAttribute("usersInOrg", usersInOrg);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("ec/addteam");
    }

    @RequestMapping(value = "ec/team/{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@CurrentOrg Organization org, Model model, @PathVariable("id") int id) {
        Ecpsychicteam e = ecpsychicteamService.findById(id);
        model.addAttribute("ecteam", e);
        List<Account> usersInOrg = accountService.getAccountsByOrgid(org.getId(), AcountTypeFlag.ecuser.getId());
        model.addAttribute("usersInOrg", usersInOrg);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("ec/addteam");
    }

    @RequestMapping(value = "ec/{ecid}/team/{id}/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request, Model model, Ecpsychicteam e) {
        ecpsychicteamService.del(e);
        List<Ecpsychicteam> ecpsychicteams = eduCommissionService.getTeamsByOrgid(e.getEcid());
        model.addAttribute("ecpsychicteams", ecpsychicteams);
        request.getSession().setAttribute("action", "删除心理队伍成功");
        return viewName("ec/listteam");
    }

    @RequestMapping(value = "ec/team/{id}/{ecid}/update", method = RequestMethod.POST)
    public String update(HttpServletRequest request, Model model, @PathVariable("id") int id,
            @PathVariable("ecid") long ecid, Ecpsychicteam e) {

        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */

        int c = -1;
        c = ecpsychicteamService.update(e);
        if (c == 1) {
            List<Ecpsychicteam> ecpsychicteams = eduCommissionService.getTeamsByOrgid(ecid);
            model.addAttribute("ecpsychicteams", ecpsychicteams);
            request.getSession().setAttribute("action", "更新心理队伍信息成功");
            return viewName("ec/listteam");
        } else {
            request.getSession().setAttribute("action", "更新心理队伍信息失败");
            return viewName("ec/listteam");
        }
    }

    @RequestMapping(value = "ec/{ecid}/team/add", method = RequestMethod.GET)
    public String addForm(@CurrentOrg Organization org, Model model, Ecpsychicteam e,
            @PathVariable("ecid") long orgid) {

        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */

        model.addAttribute("ecteam", e);
        List<Account> usersInOrg = accountService.getAccountsByOrgid(org.getId(), AcountTypeFlag.ecuser.getId());
        model.addAttribute("usersInOrg", usersInOrg);
        return viewName("ec/addteam");
    }

    /**
     * 下属机构增加
     *
     * @author 赵忠诚
     */
    @RequestMapping("{parent}/addsub")
    public void addSub(HttpServletRequest request, Model model, @PathVariable("parent") Organization parent,
            @ModelAttribute("child") Organization child) {
        Long parentId = parent.getId();
        if (child.getParentId() == null)
            child.setParentId(parentId);
        int cnt = organizationService.saveOrg((Organization) child);
        request.getSession().setAttribute("action", "添加下属机构");
    }

    /**
     * 删除组织机构
     *
     * @author 赵忠诚
     */
    @RequestMapping("{orgid}/del.do")
    public void delOrg(HttpServletRequest request, Model model, @PathVariable("parent") Organization org) {
        int cnt = organizationService.delOrg(org);
        request.getSession().setAttribute("action", "删除下属机构");
    }

}
