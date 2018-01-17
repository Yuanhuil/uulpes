package com.njpes.www.action.scaletoollib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.scaletoollib.NormInfo;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ExamdistributeService;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.AgeUitl;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;

@Controller
@RequestMapping("/assessmentcenter/scaledispense")
public class ExamdistributeController extends BaseController {
    @Autowired
    private ExamdistributeService examdistributeService;
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private ScaleService scaleService;
    @Autowired
    private SyslogServiceI logservice;
    // * 量表个体分发

    // public String singledistribute(HttpServletRequest req,
    // HttpServletResponse resp ){
    // examdistributeService.singledistribute(orgid, flag, starttime, endtime,
    // objectType, classId, scales, teacherRole);
    // return "";
    // }

    /**
     * 量表群体分发开始
     */
    @RequestMapping(value = "/groupdispenseview", method = RequestMethod.GET)
    public String groupdispenseAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        int orglevel = orgEntity.getOrgLevel();
        List<XueDuan> xdList = schoolService.getXueDuanInSchool(orgEntity.getId());
        List xdidList = new ArrayList<Integer>();
        if (xdList != null) {
            for (int i = 0; i < xdList.size(); i++) {
                XueDuan xd = xdList.get(i);
                xdidList.add(xd.getId());
            }
        }
        model.addAttribute("orgtype", orgType.value());
        model.addAttribute("orglevel", orglevel);
        // model.addAttribute("orgtype", "1");
        // model.addAttribute("orglevel", 2);
        model.addAttribute("xdlist", xdidList);
        return viewName("groupdispenseview");

    }

    @RequestMapping(value = "/edudispenseview", method = RequestMethod.GET)
    public String edudispenseAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        int orglevel = orgEntity.getOrgLevel();
        String cityid = orgEntity.getCityid();
        String countyid = orgEntity.getCountyid();
        model.addAttribute("orgtype", orgType.value());
        model.addAttribute("orglevel", orglevel);
        model.addAttribute("cityid", cityid);
        model.addAttribute("countyid", countyid);
        // model.addAttribute("orgtype", "1");
        // model.addAttribute("orglevel", 3);
        // model.addAttribute("cityid", "3201");
        // model.addAttribute("countyid","320102");
        return viewName("edudispenseview");

    }

    /**
     * 量表分发开始
     */
    @RequestMapping(value = "/personaldispenseview", method = RequestMethod.GET)
    public String unitdispenseAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model) {
        HashMap<String, String> gradeList = new HashMap<String, String>();
        long schoolorgId = orgEntity.getId();
        HashMap<XueDuan, List<Grade>> njlist = schoolService.getGradesInSchool(schoolorgId);
        if (njlist == null)
            return null;
        Iterator iter = njlist.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            XueDuan xueduan = (XueDuan) entry.getKey();
            int xdId = xueduan.getId();
            String xdmc = xueduan.getInfo();
            List<Grade> grades = (List) entry.getValue();

            for (int i = 0; i < grades.size(); i++) {
                Grade g = grades.get(i);
                // String nj = g.getNj();
                String gradeid = g.getGradeid();
                String njmc = g.getNjmc();
                // if(xdId==1){
                if (gradeid.equals("1") || gradeid.equals("2"))
                    continue;
                // }
                // gradeList.put(xdId+"-"+nj, njmc);
                gradeList.put(gradeid, njmc);
            }
        }
        model.addAttribute("gradeList", gradeList);
        model.addAttribute("orgtype", "2");
        return viewName("personaldispenseview");

    }

    @RequestMapping(value = "/singledistribute", method = RequestMethod.POST)
    public String unitdispenseinfoAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model)
            throws Exception {
        long orgid = orgEntity.getId();
        String orgtype = orgEntity.getOrgType();
        Account account = (Account) req.getSession().getAttribute("user");
        long createuserid = account.getId();
        String countyid = orgEntity.getCountyid();
        String cityid = orgEntity.getCityid();
        String taskname = req.getParameter("taskname");
        String flag = req.getParameter("flag"); // 时间限制
        String starttime = req.getParameter("starttime"); // 开始时间
        String endtime = req.getParameter("endtime"); // 结束时间
        String objectType = req.getParameter("objectType"); // 分发对象
        String[] scales = req.getParameterValues("scaleId"); // 分发量表

        if (taskname == null) {
            String firstScale = scales[0];
            taskname = cachedScaleMgr.get(firstScale).getTitle();
        }
        Map<String, Object> resultMsgMap = new HashMap<String, Object>();
        List errorMsgList = new ArrayList();
        Map<String, Object> statisMap = new HashMap<String, Object>();
        resultMsgMap.put("errorMsgList", errorMsgList);
        resultMsgMap.put("statisMap", statisMap);
        if (objectType.equals("1")) {
            String gradeId = req.getParameter("gradeOrderId"); // 分发学生
            int gradeorderid = Integer.parseInt(gradeId);
            String xd = String.valueOf(AgeUitl.getXdByGrade(gradeorderid));
            String nj = String.valueOf(AgeUitl.getNj(gradeorderid));// 入学年级
            // String njmc = AgeUitl.getGradeName(gradeorderid);
            String njmc = AgeUitl.getNjName(gradeorderid);
            String bjinfo = req.getParameter("classId"); // 分发学生
            String[] bjinfoArray = bjinfo.split(",");
            String bj = bjinfoArray[0];
            String bjmc = bjinfoArray[1];
            String[] studentIds = req.getParameterValues("studentId"); // 分发学生
            examdistributeService.singledistribute(taskname, cityid, countyid, orgid, flag, starttime, endtime,
                    objectType, xd, gradeId, nj, njmc, bj, bjmc, scales, createuserid, studentIds, null, null, null,
                    resultMsgMap);
        }
        if (objectType.equals("2")) {
            String[] teacherIds = req.getParameterValues("teacherId"); // 分发教师
            String teacherRole = req.getParameter("teacherRole"); // 分发教师权限
            Role role = roleService.selectRole(Integer.parseInt(teacherRole));
            String rolename = role.getRoleName();
            examdistributeService.singledistribute(taskname, cityid, countyid, orgid, flag, starttime, endtime,
                    objectType, null, null, null, null, null, null, scales, createuserid, null, teacherIds, teacherRole,
                    rolename, resultMsgMap);
        }
        model.addAttribute("orgtype", orgtype);
        model.addAttribute("resultmsgMap", resultMsgMap);
        logservice.log(req, "心理检测中心", "个体量表分发");
        return viewName("dispenseclose");
    }

    @RequestMapping(value = "/groupdistribute", method = RequestMethod.POST)
    public String dispenseinfoAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model)
            throws Exception {
        long orgid = orgEntity.getId();
        String orgtype = orgEntity.getOrgType();
        Account account = (Account) req.getSession().getAttribute("user");
        long createuserid = account.getId();
        String countyid = orgEntity.getCountyid();
        String cityid = orgEntity.getCityid();
        String taskname = req.getParameter("taskname");
        String flag = req.getParameter("flag"); // 时间限制
        String starttime = req.getParameter("starttime"); // 开始时间
        String endtime = req.getParameter("endtime"); // 结束时间
        String objectType = req.getParameter("objectType"); // 分发对象
        String xd = req.getParameter("gradepart");// 学段
        String[] gradeClassIds = req.getParameterValues("gradeClassId"); // 分发班级
        String[] scales = req.getParameterValues("scaleId"); // 分发量表
        String[] teacherRole = req.getParameterValues("teacherRole"); // 分发教师权限
        if (taskname == null) {
            String firstScale = scales[0];
            taskname = cachedScaleMgr.get(firstScale).getTitle();
        }
        // List resultMsgList= new ArrayList();
        Map<String, Object> resultMsgMap = new HashMap<String, Object>();
        List errorMsgList = new ArrayList();
        Map<String, Object> statisMap = new HashMap<String, Object>();
        resultMsgMap.put("errorMsgList", errorMsgList);
        resultMsgMap.put("statisMap", statisMap);
        if (objectType.equals("1")) {
            // String[] xd_bj = gradeOrderId.split("-");
            // String nj = xd_bj[0];
            // String bj = xd_bj[1];
            examdistributeService.groupdistribute(taskname, countyid, cityid, orgid, xd, gradeClassIds, flag, starttime,
                    endtime, objectType, scales, createuserid, null, resultMsgMap);
        }
        if (objectType.equals("2")) {
            examdistributeService.groupdistribute(taskname, countyid, cityid, orgid, null, gradeClassIds, flag,
                    starttime, endtime, objectType, scales, createuserid, teacherRole, resultMsgMap);
        }
        // model.addAttribute("resultmsglist", resultMsgList);
        model.addAttribute("orgtype", orgtype);
        model.addAttribute("resultmsgMap", resultMsgMap);
        logservice.log(req, "心理检测中心", "群体量表分发");
        return viewName("dispenseclose");
    }

    @RequestMapping(value = "/edudistribute", method = RequestMethod.POST)
    public String eduDispenseinfoAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model)
            throws Exception {
        try {
            long orgid = orgEntity.getId();
            int orglevel = orgEntity.getOrgLevel();
            String orgtype = orgEntity.getOrgType();
            Account account = (Account) req.getSession().getAttribute("user");
            long createuserid = account.getId();
            long creater_orgid = orgEntity.getId();

            String taskname = req.getParameter("taskname");
            String flag = req.getParameter("flag"); // 时间限制
            String starttime = req.getParameter("starttime"); // 开始时间
            String endtime = req.getParameter("endtime"); // 结束时间
            String objectType = req.getParameter("objectType"); // 分发对象
            String xd = req.getParameter("gradepart");// 学段
            String[] gradeIds = null;
            if (objectType.equals("1")) {
                if (xd.equals("1"))
                    gradeIds = req.getParameterValues("nj1"); // 分发年级
                if (xd.equals("2"))
                    gradeIds = req.getParameterValues("nj2"); // 分发年级
                if (xd.equals("3"))
                    gradeIds = req.getParameterValues("nj3"); // 分发年级
            }
            String[] scales = req.getParameterValues("scaleId"); // 分发量表
            String[] teacherroleIds = req.getParameterValues("teacherRole"); // 分发教师权限
            String[] schoolIds = null;
            if (!StringUtils.isEmpty(req.getParameter("schoolarray"))) {
                String schoolArrayStr = req.getParameter("schoolarray");
                schoolIds = schoolArrayStr.split(",");
                // schoolIds = req.getParameterValues("schoolid"); //分发学校
            }
            if (req.getParameterValues("subschoolid") != null)
                schoolIds = req.getParameterValues("subschoolid"); // 分发直属学校
            String[] areaIds = req.getParameterValues("areaid"); // 分发区县、乡镇
            long[] orgIds = null;
            if (areaIds != null) {// 县级以上教委，机构集合就是选择的行政区所属教委
                orgIds = new long[areaIds.length];
                for (int i = 0; i < areaIds.length; i++) {
                    if (orgEntity.getOrgLevel() == 3) {// 市教委
                        Organization orgnization = organizationService.getEduOrganizationByCountyId(areaIds[i]);
                        if (orgnization == null)
                            continue;
                        orgIds[i] = orgnization.getId();
                    }

                }
            }

            if (orgEntity.getOrgLevel() == 4) {// 县教委,机构集合就是下发学校的机构集合
                schoolIds = req.getParameterValues("schoolid");
                // Organization orgnization =
                // organizationService.getEduOrganizationByCountyId(areaIds[i]);
                if (schoolIds != null && schoolIds.length > 0) {
                    orgIds = new long[schoolIds.length];
                    for (int i = 0; i < schoolIds.length; i++) {
                        orgIds[i] = Integer.parseInt(schoolIds[i]);
                    }
                }
            }

            // if(orgEntity.getOrgLevel() == 4) orgIds[0] = creater_orgid;

            if (taskname == null) {
                String firstScale = scales[0];
                taskname = cachedScaleMgr.get(firstScale).getTitle();
            }
            // List resultMsgList= new ArrayList();
            Map<String, Object> resultMsgMap = new HashMap<String, Object>();
            List errorMsgList = new ArrayList();
            Map<String, Object> statisMap = new HashMap<String, Object>();
            resultMsgMap.put("errorMsgList", errorMsgList);
            resultMsgMap.put("statisMap", statisMap);

            if (objectType.equals("1")) {
                // String[] xd_bj = gradeOrderId.split("-");
                // String nj = xd_bj[0];
                // String bj = xd_bj[1];
                // examdistributeService.groupdistribute(taskname,countyid,cityid,orgid,xd,gradeClassIds,
                // flag, starttime, endtime, objectType, scales,createuserid,
                // null);
                examdistributeService.eduDistributeForStu(orglevel, taskname, createuserid, creater_orgid, areaIds,
                        orgIds, starttime, endtime, xd, gradeIds, scales, schoolIds, resultMsgMap);
            }
            if (objectType.equals("2")) {
                examdistributeService.eduDistributeForTeacher(orglevel, taskname, createuserid, creater_orgid, areaIds,
                        orgIds, starttime, endtime, scales, teacherroleIds, schoolIds, resultMsgMap);
                // examdistributeService.groupdistribute(taskname,countyid,cityid,orgid,null,
                // gradeClassIds,flag, starttime, endtime, objectType, scales,
                // createuserid,teacherRole);
            }
            // return
            // "redirect:/scaletoollib/monitorprocess/taskmanageOfEducommission.do";
            model.addAttribute("orgtype", orgtype);
            model.addAttribute("resultmsgMap", resultMsgMap);
            logservice.log(req, "心理检测中心", "教委量表分发");
            return viewName("dispenseclose");
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("=============================================错误");
        }
        return null;
    }

    @RequestMapping(value = "normlist", method = RequestMethod.GET)
    public String viewnorm(HttpServletRequest request, Model model) {
        String scaleid = request.getParameter("scaleid");
        Scale scale = cachedScaleMgr.get(scaleid);
        String scalename = scale.getTitle();
        NormInfo norminfo = new NormInfo();
        // norminfo.setOrglevel(Integer.parseInt(orglevel));
        // norminfo.setType(Integer.parseInt(normtype));
        norminfo.setScaleId(Integer.parseInt(scaleid));
        List<NormInfo> scaleNormLogList = scaleService.getScaleNorminfo(norminfo);
        request.setAttribute("scaleNormLogList", scaleNormLogList);
        request.setAttribute("scalename", scalename);
        // requst.setAttribute("orglevel", orglevel);
        // requst.setAttribute("oplevel",orgEntity.getOrgLevel() );
        // requst.setAttribute("userlevel", Constants.APPLICATION_USERLEVEL);
        // requst.setAttribute("areacode", Constants.APPLICATION_AREACODE);
        return viewName("scalenormlist");

    }
}
