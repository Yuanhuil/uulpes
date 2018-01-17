package com.njpes.www.action.systeminfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.enums.BooleanStringEnum;
import com.njpes.www.entity.baseinfo.enums.OrgLevel;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.SchoolXXSX;
import com.njpes.www.entity.baseinfo.organization.Educommission;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.OrgImportServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.EduCommissionServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationLevelServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.util.DictionaryServiceI;
import com.njpes.www.utils.FileOperate;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.PropertiesUtil;

import edutec.admin.ExportUtils;
import heracles.web.config.ApplicationConfiguration;

@Controller
@RequestMapping(value = "/systeminfo/sys/organization/sub")
public class OrganizationSubController extends BaseController {
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private DistrictService DistrictService;

    @Autowired
    private OrganizationLevelServiceI organizationLevelService;
    @Autowired
    private DictionaryServiceI DictionaryService;

    @Autowired
    private OrgImportServiceI orgImportService;

    @Autowired
    private SchoolServiceI schoolService;

    @Autowired
    private EduCommissionServiceI eduCommissionService;
    @Autowired
    private SyslogServiceI logservice;

    public OrganizationSubController() {
        setResourceIdentity("systeminfo:suborganization");
    }

    /**
     * 展示组织机构信息,v1.0版本只能实现在一个人在一个组织机构的情况，如果存在一个人在多个组织机构的情况
     * 目前处理是只取第一个组织机构信息，其他组织机构信息不管
     * 这个功能应该是具有管理员功能的人才可以使用，如果一个人在多个组织机构，并且在某一个机构是管理员 就会存在问题
     * @author 赵忠诚
     */
    @RequestMapping(value = { "ec/list" }, method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, Educommission ec, @PageAnnotation PageParameter page,
            Model model) {
        long orgid = 0L;
        Organization queryOrg = new Organization();
        queryOrg.setOrgType(OrganizationType.ec.getId());
        // queryOrg.setOrgLevel(org.getOrgLevel() + 1);
        queryOrg.setParentId(org.getId());

        if (org.getOrgLevel().intValue() == (OrgLevel.province.getIdentify())) {
            queryOrg.setProvinceid(org.getProvinceid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.city.getIdentify())) {
            queryOrg.setProvinceid(org.getProvinceid());
            queryOrg.setCityid(org.getCityid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.county.getIdentify())) {
            queryOrg.setProvinceid(org.getProvinceid());
            queryOrg.setCityid(org.getCityid());
            queryOrg.setCountyid(org.getCountyid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.town.getIdentify())) {
            queryOrg.setProvinceid(org.getProvinceid());
            queryOrg.setCityid(org.getCityid());
            queryOrg.setCountyid(org.getCountyid());
            queryOrg.setTownid(org.getTownid());
        }
        ec.setOrg(queryOrg);
        orgid = org.getId();
        model.addAttribute("list", eduCommissionService.selectSubECsInWebQueryByPage(ec, page));
        model.addAttribute("page", page);
        model.addAttribute("entity", ec);
        model.addAttribute("parentorgid", orgid);
        model.addAttribute("currentOrglevel", org.getOrgLevel());
        return viewName("ec/list");
    }

    @RequestMapping(value = "ec/search")
    public String search(Model model, @CurrentOrg Organization org, Educommission ec,
            @PageAnnotation PageParameter page) {
        ec.getOrg().setId(org.getId());
        model.addAttribute("list", eduCommissionService.selectSubECsInWebQueryByPage(ec, page));
        model.addAttribute("page", page);
        return viewName("ec/tablelist");
    }

    @RequestMapping(value = "ec/{parentorgid}/add", method = RequestMethod.GET)
    public String addForm(Model model, @CurrentOrg Organization org, @PathVariable("parentorgid") long parentorgid) {

        /*
         * if (permissionList != null) {
         * this.permissionList.assertHasUpdatePermission(); }
         */
        Educommission ec = new Educommission();
        Organization newOrg = new Organization();
        if (org.getOrgLevel().intValue() == (OrgLevel.province.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.city.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
            newOrg.setCityid(org.getCityid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.county.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
            newOrg.setCityid(org.getCityid());
            newOrg.setCountyid(org.getCountyid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.town.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
            newOrg.setCityid(org.getCityid());
            newOrg.setCountyid(org.getCountyid());
            newOrg.setTownid(org.getTownid());
        }
        org.setOrgType(OrganizationType.ec.getId());
        org.setIsshow(1);
        ec.setOrg(newOrg);
        List<OrganizationLevel> l = organizationLevelService.queryOrgLevelList(org.getOrgLevel(), false, true);
        model.addAttribute("entity", ec);
        model.addAttribute("orglevelList", l);
        model.addAttribute("parentId", parentorgid);
        model.addAttribute("currprov", org.getProvinceid());
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("ec/editform");
    }

    @RequestMapping(value = "ec/{parentId}/create", method = RequestMethod.POST)
    public String createTeam(@CurrentOrg Organization org, Educommission ec, HttpServletRequest request, Model model) {
        ec.getOrg().setOrgType(OrganizationType.ec.getId());
        ec.getOrg().setParentId(org.getId());
        eduCommissionService.insertEc(ec);
        logservice.log(request, "基础信息中心:下属机构管理", "新建" + ec.getOrg().getName());
        return redirectToUrl(viewName("ec/list.do"));
    }

    @RequestMapping(value = "ec/downloadectemplate", method = RequestMethod.GET)
    public String downloadectemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("机构基本信息导入模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/机构基本信息导入模板.xlsx");
            InputStream in = new BufferedInputStream(new FileInputStream(fullFileName));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;

            while ((i = in.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "ec/{id}/view", method = RequestMethod.GET)
    public String view(Model model, @PathVariable("id") long id) {
        Organization e = organizationService.selectOrganizationById(id);
        model.addAttribute("entity", e);
        model.addAttribute(Constants.OP_NAME, Constants.VIEW_NAME);
        return viewName("ec/editform");
    }

    @RequestMapping(value = "ec/{id}/update", method = RequestMethod.GET)
    public String update(@CurrentOrg Organization org, Model model, @PathVariable("id") long id) {
        Educommission e = eduCommissionService.getECInfoByKeyId(id);
        List<OrganizationLevel> l = organizationLevelService.queryOrgLevelList(org.getOrgLevel(), false, true);
        model.addAttribute("entity", e);
        model.addAttribute("orglevelList", l);
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("ec/editform");
    }

    @RequestMapping(value = "ec/{id}/update", method = RequestMethod.POST)
    public String updateform(HttpServletRequest request, Educommission ec, Model model, @PathVariable("id") long id) {
        ec.getOrg().setOrgType(OrganizationType.ec.getId());
        eduCommissionService.updateEC(ec);
        logservice.log(request, "基础信息中心:下属机构管理", ec.getOrg().getName() + "信息更新");
        return redirectToUrl(viewName("ec/list.do"));
    }

    @RequestMapping(value = "ec/{id}/delete", method = RequestMethod.GET)
    public String del(HttpServletRequest request, Model model, @PathVariable("id") long id) {
        Educommission ec = eduCommissionService.getECInfoByKeyId(id);
        eduCommissionService.delByEntity(ec);
        logservice.log(request, "基础信息中心:下属机构管理", ec.getOrg().getName() + "删除");
        return redirectToUrl(viewName("ec/list.do"));
    }

    @RequestMapping(value = "ec/{id}/{orgid}/lock", method = RequestMethod.GET)
    public String lock(HttpServletRequest request, Model model, @PathVariable("id") long id, Educommission ec) {
        organizationService.updateOrgLocked(ec.getOrgid(), BooleanStringEnum.TRUE.getId());
        logservice.log(request, "基础信息中心:下属机构管理", ec.getOrg().getName() + "被锁定");
        return redirectToUrl(viewName("ec/list.do"));
    }

    @RequestMapping(value = "ec/{id}/{orgid}/unlock", method = RequestMethod.GET)
    public String unlock(HttpServletRequest request, Model model, @PathVariable("id") long id, Educommission ec) {
        organizationService.updateOrgLocked(ec.getOrgid(), BooleanStringEnum.FALSE.getId());
        logservice.log(request, "基础信息中心:下属机构管理", ec.getOrg().getName() + "解锁");
        return redirectToUrl(viewName("ec/list.do"));
    }

    @RequestMapping(value = "ec/delselected", method = RequestMethod.POST)
    public String delselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                Educommission ec = eduCommissionService.getECInfoByKeyId(id);
                eduCommissionService.delByEntity(ec);
            }
        }
        logservice.log(request, "基础信息中心:下属机构管理", "下属机构批量删除");
        return redirectToUrl(viewName("ec/list.do"));
    }

    @RequestMapping(value = "ec/{parentId}/import", method = RequestMethod.POST)
    public String importec(@CurrentOrg Organization org, HttpServletRequest request, String excelUrl) throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // tmpScaleDir = "picScale"+System.currentTimeMillis();
            FileOperate.saveFile(saveFilePath, fileName, mf);
            // InputStream inputStream = mf.getInputStream();
            orgImportService.importEdu(saveFilePath + "\\" + fileName, org);
            logservice.log(request, "基础信息中心:下属机构管理", "下属机构导入");
        }

        return redirectToUrl(viewName("ec/list.do"));
    }

    // -----------------------------------------------------school
    @RequestMapping(value = { "school/list" }, method = RequestMethod.GET)
    public String school_list(@CurrentOrg Organization org, School queryschool, @PageAnnotation PageParameter page,
            Model model) {
        Organization queryOrg = new Organization();
        queryOrg.setOrgType(OrganizationType.school.getId());
        queryOrg.setParentId(org.getId());

        queryschool.setOrg(queryOrg);
        model.addAttribute("list", schoolService.selectSubSchoolsInWebQueryByPage(queryschool, page));
        List<Dictionary> bxlx = DictionaryService.selectAllDic("dic_school_bxlx");
        List<Dictionary> bxbb = DictionaryService.selectAllDic("dic_school_xxbb");
        List<Dictionary> xxdwcc = DictionaryService.selectAllDic("dic_school_xxdwcc");
        model.addAttribute("bxlx", bxlx);
        model.addAttribute("xxbb", bxbb);
        model.addAttribute("xxdwcc", xxdwcc);
        model.addAttribute("page", page);
        model.addAttribute("entity", queryschool);
        model.addAttribute("parentorgid", org.getId());
        return viewName("school/list");
    }

    @RequestMapping(value = "school/search")
    public String school_search(Model model, @CurrentOrg Organization org, School querysch,
            @PageAnnotation PageParameter page) {
        querysch.getOrg().setParentId(org.getId());
        model.addAttribute("list", schoolService.selectSubSchoolsInWebQueryByPage(querysch, page));
        model.addAttribute("page", page);
        return viewName("school/tablelist");
    }

    @RequestMapping(value = "school/count", method = RequestMethod.POST)
    @ResponseBody
    public String isBeyondControlCount(HttpServletRequest request,String orgType , String parentorgid) {
        PropertiesUtil p = new PropertiesUtil("resource/resources.properties");
        String string = p.readProperty("school.count");
        PageParameter page = new PageParameter(1, 1000);
        Organization queryOrg = new Organization();
        if (orgType.equals("1")) {
            orgType = "2";
        }
        School school = new School();
        queryOrg.setOrgType(orgType);
        queryOrg.setParentId(Long.valueOf(parentorgid));
        school.setOrg(queryOrg);
        List<School> list = schoolService.selectSubSchoolsInWebQueryByPage(school, page);
        if (list.size() >= Integer.parseInt(string)) {
            return "0";
        }
        return "1";
    }

    @RequestMapping(value = "school/{parentorgid}/add", method = RequestMethod.GET)
    public String schooladdForm(Model model, @CurrentOrg Organization org,
            @PathVariable("parentorgid") long parentorgid) {

        School school = new School();
        Organization newOrg = new Organization();
        if (org.getOrgLevel().intValue() == (OrgLevel.province.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.city.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
            newOrg.setCityid(org.getCityid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.county.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
            newOrg.setCityid(org.getCityid());
            newOrg.setCountyid(org.getCountyid());
        } else if (org.getOrgLevel().intValue() == (OrgLevel.town.getIdentify())) {
            newOrg.setProvinceid(org.getProvinceid());
            newOrg.setCityid(org.getCityid());
            newOrg.setCountyid(org.getCountyid());
            newOrg.setTownid(org.getTownid());
        }
        org.setOrgType(OrganizationType.school.getId());
        org.setIsshow(1);
        school.setOrg(newOrg);
        List<Dictionary> bxlx = DictionaryService.selectAllDic("dic_school_bxlx");
        List<Dictionary> bxbb = DictionaryService.selectAllDic("dic_school_xxbb");
        List<Dictionary> xxdwcc = DictionaryService.selectAllDic("dic_school_xxdwcc");
        model.addAttribute("bxlx", bxlx);
        model.addAttribute("xxbb", bxbb);
        model.addAttribute("xxdwcc", xxdwcc);
        model.addAttribute("entity", school);
        model.addAttribute("parentId", parentorgid);
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        return viewName("school/editform");
    }

    @RequestMapping(value = "school/{parentId}/create", method = RequestMethod.POST)
    public String schoolcreateTeam(HttpServletRequest request, @CurrentOrg Organization org, School school,
            Model model) {
        if (org.getOrgLevel().equals(new Integer(OrgLevel.province.getIdentify()))) {
            school.setXxsx(SchoolXXSX.province_school.getId());
        } else if (org.getOrgLevel().equals(new Integer(OrgLevel.city.getIdentify()))) {
            school.setXxsx(SchoolXXSX.city_school.getId());
        } else if (org.getOrgLevel().equals(new Integer(OrgLevel.county.getIdentify()))) {
            school.setXxsx(SchoolXXSX.county_school.getId());
        } else if (org.getOrgLevel().equals(new Integer(OrgLevel.town.getIdentify()))) {
            school.setXxsx(SchoolXXSX.town_school.getId());
        }
        school.getOrg().setOrgType(OrganizationType.school.getId());
        school.getOrg().setParentId(org.getId());
        if (org.getOrgLevel().equals(new Integer(OrgLevel.city.getIdentify()))) {
            // 市直属学校，学校的机构级别等同于区县教委
            school.getOrg().setOrgLevel(OrgLevel.county.getIdentify());
        } else
            school.getOrg().setOrgLevel(OrgLevel.school.getIdentify());// 基层学校机构级别
        school.getOrg().setIsshow(1);
        // TODO 初建学校时默认设置年制
        if (school.getCzxz() == null) {
            school.setCzxz(new BigDecimal(3));
        }
        if (school.getGzxz() == null) {
            school.setGzxz(new BigDecimal(3));
        }
        if (school.getXxxz() == null) {
            school.setXxxz(new BigDecimal(6));
        }
        if (school.getDxxz() == null) {
            school.setDxxz(new BigDecimal(4));
        }
        schoolService.insert(school);
        logservice.log(request, "基础信息中心:下属机构管理", "新建学校:" + school.getXxmc());
        return redirectToUrl(viewName("school/list.do"));
    }

    @RequestMapping(value = "school/{id}/update", method = RequestMethod.GET)
    public String schoolupdate(@CurrentOrg Organization org, Model model, @PathVariable("id") long id) {
        School e = schoolService.getSchoolInfoByKeyid(id);
        List<Dictionary> bxlx = DictionaryService.selectAllDic("dic_school_bxlx");
        List<Dictionary> bxbb = DictionaryService.selectAllDic("dic_school_xxbb");
        List<Dictionary> xxdwcc = DictionaryService.selectAllDic("dic_school_xxdwcc");
        model.addAttribute("bxlx", bxlx);
        model.addAttribute("xxbb", bxbb);
        model.addAttribute("xxdwcc", xxdwcc);
        model.addAttribute("entity", e);
        model.addAttribute("parentId", org.getId());
        model.addAttribute(Constants.OP_NAME, Constants.EDIT_NAME);
        return viewName("school/editform");
    }

    @RequestMapping(value = "school/{id}/update", method = RequestMethod.POST)
    public String schoolupdateform(HttpServletRequest request, @CurrentOrg Organization org, School school, Model model,
            @PathVariable("id") long id) {
        if (org.getOrgLevel().equals(new Integer(OrgLevel.province.getIdentify()))) {
            school.setXxsx(SchoolXXSX.province_school.getId());
        } else if (org.getOrgLevel().equals(new Integer(OrgLevel.city.getIdentify()))) {
            school.setXxsx(SchoolXXSX.city_school.getId());
        } else if (org.getOrgLevel().equals(new Integer(OrgLevel.county.getIdentify()))) {
            school.setXxsx(SchoolXXSX.county_school.getId());
        } else if (org.getOrgLevel().equals(new Integer(OrgLevel.town.getIdentify()))) {
            school.setXxsx(SchoolXXSX.town_school.getId());
        }
        school.getOrg().setOrgType(OrganizationType.school.getId());
        school.getOrg().setOrgLevel(OrgLevel.school.getIdentify());
        schoolService.update(school);
        logservice.log(request, "基础信息中心:下属机构管理", school.getXxmc() + "信息更新");
        return redirectToUrl(viewName("school/list.do"));
    }

    @RequestMapping(value = "school/{id}/delete", method = RequestMethod.GET)
    public String schooldel(HttpServletRequest request, Model model, @PathVariable("id") long id) {
        School e = schoolService.getSchoolInfoByKeyid(id);
        schoolService.delByEntity(e);
        logservice.log(request, "基础信息中心:下属机构管理", e.getXxmc() + "删除");
        return redirectToUrl(viewName("school/list.do"));
    }

    @RequestMapping(value = "school/{id}/{orgid}/lock", method = RequestMethod.GET)
    public String schoollock(HttpServletRequest request, Model model, @PathVariable("id") long id, School school) {
        organizationService.updateOrgLocked(school.getOrgid(), BooleanStringEnum.TRUE.getId());
        logservice.log(request, "基础信息中心:下属机构管理", school.getXxmc() + "被锁定");
        return redirectToUrl(viewName("school/list.do"));
    }

    @RequestMapping(value = "school/{id}/{orgid}/unlock", method = RequestMethod.GET)
    public String schoolunlock(HttpServletRequest request, Model model, @PathVariable("id") long id, School school) {
        organizationService.updateOrgLocked(school.getOrgid(), BooleanStringEnum.FALSE.getId());
        logservice.log(request, "基础信息中心:下属机构管理", school.getXxmc() + "解锁");
        return redirectToUrl(viewName("school/list.do"));
    }

    @RequestMapping(value = "school/delselected", method = RequestMethod.POST)
    public String schooldelselected(HttpServletRequest request,
            @RequestParam(value = "rowcheck[]", required = false) Long[] rowcheck) {
        if (rowcheck != null && rowcheck.length > 0) {
            for (Long id : rowcheck) {
                School school = schoolService.selectByPrimaryKey(id);
                schoolService.delByEntity(school);
            }
        }
        logservice.log(request, "基础信息中心:下属机构管理", "学校批量删除");
        return redirectToUrl(viewName("school/list.do"));
    }

    @RequestMapping(value = "school/downloadschooltemplate", method = RequestMethod.GET)
    public String downloadstutemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = FileOperate.encodeFilename("学校基本信息导入模板.xlsx", request);
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/学校基本信息导入模板.xlsx");
            InputStream in = new BufferedInputStream(new FileInputStream(fullFileName));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;

            while ((i = in.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "school/redirectToImportSchool", method = RequestMethod.GET)
    public String redirectToImportSchool() {
        return viewName("school/schoolimport");
    }

    @RequestMapping(value = "ec/redirectToImportEc", method = RequestMethod.GET)
    public String redirectToImportEc(@CurrentOrg Organization org, Model model) {
        model.addAttribute("parentorgid", org.getId());
        return viewName("ec/ecimport");
    }

    @RequestMapping(value = "school/import", method = RequestMethod.POST)
    public String imports(@CurrentOrg Organization org, HttpServletRequest request, String excelUrl) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // tmpScaleDir = "picScale"+System.currentTimeMillis();
            FileOperate.saveFile(saveFilePath, fileName, mf);
            // InputStream inputStream = mf.getInputStream();
            orgImportService.importOrg(saveFilePath + "\\" + fileName, org);
        }
        logservice.log(request, "基础信息中心:下属机构管理", "导入学校");
        return redirectToUrl(viewName("school/list.do"));
    }
}
