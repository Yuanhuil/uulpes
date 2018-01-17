package com.njpes.www.action.archivemanage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.enums.InterveneResult;
import com.njpes.www.entity.baseinfo.enums.InterveneType;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.WarningLever;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.consultcenter.Analyze;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.scaletoollib.ReportLookStudentFilterParam;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.AnalyzeServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

import edutec.scale.model.Scale;

@Controller
@RequestMapping(value = "/archivemanage/archiveTeamStudent")
public class ArchiveTeamStudentController extends BaseController {

    @Autowired
    private AnalyzeServiceI analyzeService;
    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private ConsultationModelServiceI consultationModelService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private ReportLookService reportLookService;
    @Autowired
    private ExamdoServiceI examdoService;
    @Autowired
    private StudentServiceI studentService;

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        String orgTypeId = orgType.getId();
        request.setAttribute("orgType", orgTypeId);
        String orgtype = orgEntity.getOrgType();
        int orgLevel = orgEntity.getOrgLevel();
        request.setAttribute("orgLevel", orgLevel);

        Long orgId = orgEntity.getId();
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        Analyze analyze = new Analyze();
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        List<ConsultationModel> consultationModels = getConsultationModels();
        long schoolorgid = orgEntity.getId();

        if (orgtype.equals("2")) {// 学校
            List<XueDuan> xdlist = schoolService.getXueDuanInSchool(schoolorgid);
            if (xdlist != null && xdlist.size() == 1) {
                HashMap<XueDuan, List<Grade>> xdNjMap = schoolService.getGradesInSchool(schoolorgid);
                model.addAttribute("njlist", xdNjMap.get(xdlist.get(0)));

            }
            model.addAttribute("xdlist", xdlist);
            analyze.setSchoolid(orgEntity.getId());
        }
        // 如果是区县教委用户
        else {
            if (orgLevel == 4) {
                // 获取该组织下面的所有学校
                List<Organization> schoolList = reportLookService
                        .getSchoolsFromExamresultStudent(orgEntity.getCountyid());
                request.setAttribute("schoolList", schoolList);
            }
            if (orgLevel == 3) {
                String cityId = orgEntity.getCityid();
                model.addAttribute("cityId", cityId);
            }
        }

        // List<Role> roleList = roleService.selectAll();
        model.addAttribute("analyze", analyze);
        // model.addAttribute("rolelist", roleList);
        // model.addAttribute("xdlist", xdlist);
        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
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
    public String list(@CurrentOrg Organization orgEntity, Analyze analyze,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        String filed = request.getParameter("filed");
        analyze.setSchoolid(orgEntity.getId());
        if (analyze.getRole().equals("2")) {
            filed = filed.replace("gradeid,", "");
        }
        if (filed.length() == 0) {
            filed = "consultationTypeId";
        } else {
            filed = filed.substring(0, filed.length() - 1);
        }

        List<Map> map = analyzeService.select(filed, analyze, beginDate, endDate);
        String[] fileds = filed.split(",");
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        Map<Long, String> consultTypesMap = new HashMap<Long, String>();
        for (ConsultType consultType : consultTypes) {
            consultTypesMap.put(consultType.getId(), consultType.getName());
        }
        List<ConsultationModel> consultationModels = getConsultationModels();
        Map<Long, String> consultationModelsMap = new HashMap<Long, String>();
        for (ConsultationModel consultationModel : consultationModels) {
            consultationModelsMap.put(consultationModel.getId(), consultationModel.getName());
        }

        int filedNum = fileds.length;
        String[][] strs = new String[map.size() + 1][filedNum + 2];
        for (int i = 0; i < filedNum; i++) {
            if (filed.split(",")[i].equals("consultationTypeId")) {
                strs[0][i] = "咨询类型";
            } else if (filed.split(",")[i].equals("consultationModeId")) {
                strs[0][i] = "咨询方式";
            } else if (filed.split(",")[i].equals("groupeid")) {
                strs[0][i] = "学段";
            } else if (filed.split(",")[i].equals("gradeid")) {
                strs[0][i] = "年级";
            } else if (filed.split(",")[i].equals("groupeid")) {
                strs[0][i] = "班级";
            }
        }
        int total = 0;
        strs[0][filedNum] = "数量";
        strs[0][filedNum + 1] = "百分比";
        int i = 1;
        for (Map map2 : map) {
            for (int j = 0; j < fileds.length; j++) {
                if (fileds[j].equals("consultationModeId")) {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他" : consultationModelsMap.get(map2.get(fileds[j]));
                } else if (fileds[j].equals("consultationTypeId")) {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他" : consultTypesMap.get(map2.get(fileds[j]));
                } else {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                }
            }
            strs[i][fileds.length] = map2.get("num").toString();
            total += (Long) map2.get("num");
            i++;
        }
        for (int j = 1; j < strs.length; j++) {
            String[] strings = strs[j];
            strings[filedNum + 1] = percentage(strings[filedNum], total);

        }

        model.addAttribute("list", strs);
        return viewName("list");
    }

    @RequestMapping(value = "archiveinschool")
    public String archiveinschool(@CurrentOrg Organization orgEntity, Analyze analyze,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);

        Student stu = new Student();
        stu.setXd(analyze.getGroupid().intValue());
        stu.setNj(analyze.getNj());
        if (!StringUtils.isEmpty(analyze.getBj()))
            stu.setBjid(Integer.parseInt(analyze.getBj()));
        List<StudentWithBLOBs> stList = studentService.getAllStudentsInClass(stu, orgEntity.getId(), 0);
        String[] attrIds = { "105", "106", "205", "218", "219", "220", "221" };
        int totalNum = stList.size();
        int maleNum = 0, femaleNum = 0, hNationNum = 0, otherNationNum = 0;
        int xxANum = 0, xxBNum = 0, xxABNum = 0, xxONum = 0;
        int health10 = 0, health20 = 0, health30 = 0, health40 = 0, health50 = 0;
        int jxfs1 = 0, jxfs2 = 0, jxfs3 = 0, jxfs4 = 0;
        int family10 = 0, family20 = 0, family30 = 0, family40 = 0, family50 = 0, family60 = 0, family70 = 0,
                family80 = 0, family90 = 0;
        int tzdb1 = 0, tzdb2 = 0, tzdb3 = 0, tzdb4 = 0;
        int xsly1 = 0, xsly2 = 0, xsly3 = 0;
        int kncd1 = 0, kncd2 = 0, kncd3 = 0;
        for (int i = 0; i < totalNum; i++) {
            StudentWithBLOBs student = stList.get(i);
            String xbm = student.getXbm();
            if (xbm != null && xbm.equals("1"))
                maleNum++;
            if (xbm != null && xbm.equals("2"))
                femaleNum++;
            String mzm = student.getMzm();
            if (mzm != null && mzm.equals("01"))
                hNationNum++;
            if (mzm != null && !mzm.equals("01"))
                otherNationNum++;

            student.loadMetas();
            student.setBackGrandByStr();
            Map<String, String> map = new HashMap<String, String>();
            student.loadBackGrandToMap(map);

            List<FieldValue> list = student.getAttrs();
            for (String attrId : attrIds) {

                String value = (String) map.get(attrId);
                if (StringUtils.isEmpty(value))
                    continue;
                if (attrId.equals("106")) {
                    if (value.equals("A型"))
                        xxANum++;
                    if (value.equals("B型"))
                        xxBNum++;
                    if (value.equals("AB型"))
                        xxABNum++;
                    if (value.equals("O型"))
                        xxONum++;
                }
                if (attrId.equals("105")) {
                    if (value.equals("健康或良好"))
                        health10++;
                    if (value.equals("一般或较弱"))
                        health20++;
                    if (value.equals("有慢性病"))
                        health30++;
                    if (value.equals("有生理缺陷"))
                        health40++;
                    if (value.equals("残疾"))
                        health50++;
                }
                if (attrId.equals("205")) {
                    if (value.equals("走读"))
                        jxfs1++;
                    if (value.equals("住校"))
                        jxfs2++;
                    if (value.equals("借宿"))
                        jxfs3++;
                    if (value.equals("其它"))
                        jxfs4++;
                }
                if (attrId.equals("218")) {
                    if (value.equals("双亲健全"))
                        family10++;
                    if (value.equals("孤儿"))
                        family20++;
                    if (value.equals("单亲"))
                        family30++;
                    if (value.equals("父母离异"))
                        family40++;
                    if (value.equals("双亲有残疾"))
                        family50++;
                    if (value.equals("本人有残疾"))
                        family60++;
                    if (value.equals("军烈属或优抚对象"))
                        family70++;
                    if (value.equals("重病"))
                        family80++;
                    if (value.equals("五保户"))
                        family90++;
                }
                if (attrId.equals("219")) {
                    if (value.equals("优秀"))
                        tzdb1++;
                    if (value.equals("良好"))
                        tzdb2++;
                    if (value.equals("及格"))
                        tzdb3++;
                    if (value.equals("不合格"))
                        tzdb4++;
                }
                if (attrId.equals("220")) {
                    if (value.equals("正常入学"))
                        xsly1++;
                    if (value.equals("借读"))
                        xsly2++;
                    if (value.equals("其它"))
                        xsly3++;
                }
                if (attrId.equals("221")) {
                    if (value.equals("特别困难"))
                        kncd1++;
                    if (value.equals("一般困难"))
                        kncd2++;
                    if (value.equals("不困难"))
                        kncd3++;
                }
            }

        }
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("maleNum", maleNum);
        model.addAttribute("femaleNum", femaleNum);
        model.addAttribute("hNationNum", hNationNum);
        model.addAttribute("otherNationNum", otherNationNum);
        model.addAttribute("xxANum", xxANum);
        model.addAttribute("xxBNum", xxBNum);
        model.addAttribute("xxABNum", xxABNum);
        model.addAttribute("xxONum", xxONum);
        model.addAttribute("health10", health10);
        model.addAttribute("health20", health20);
        model.addAttribute("health30", health30);
        model.addAttribute("health40", health40);
        model.addAttribute("health50", health50);

        model.addAttribute("jxfs1", jxfs1);
        model.addAttribute("jxfs2", jxfs2);
        model.addAttribute("jxfs3", jxfs3);
        model.addAttribute("jxfs4", jxfs4);
        model.addAttribute("family10", family10);
        model.addAttribute("family20", family20);
        model.addAttribute("family30", family30);
        model.addAttribute("family40", family40);
        model.addAttribute("family50", family50);
        model.addAttribute("family60", family60);
        model.addAttribute("family70", family70);
        model.addAttribute("family80", family80);
        model.addAttribute("family90", family90);
        model.addAttribute("tzdb1", tzdb1);
        model.addAttribute("tzdb2", tzdb1);
        model.addAttribute("tzdb3", tzdb3);
        model.addAttribute("tzdb4", tzdb4);
        model.addAttribute("xsly1", xsly1);
        model.addAttribute("xsly2", xsly2);
        model.addAttribute("xsly3", xsly3);
        model.addAttribute("kncd1", kncd1);
        model.addAttribute("kncd2", kncd2);
        model.addAttribute("kncd3", kncd3);

        // List<ClassSchool>
        // listClassSchool=schoolService.getClassByGradeInSchool(orgEntity.getId(),
        // XueDuan.valueOf(analyze.getGroupid().intValue()),analyze.getNj(),0);
        analyze.setRole("1");
        analyze.setSchoolid(orgEntity.getId());
        List<String> list = new ArrayList<String>();
        list.add("consultationTypeId");
        for (int index = 0; index < list.size(); index++) {
            String filed = list.get(index);
            List<Map> map = analyzeService.selectInSchool(filed, analyze, beginDate, endDate);
            String[] fileds = filed.split(",");
            List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
            Map<Long, String> consultTypesMap = new HashMap<Long, String>();
            for (ConsultType consultType : consultTypes) {
                consultTypesMap.put(consultType.getId(), consultType.getName());
            }
            List<ConsultationModel> consultationModels = getConsultationModels();
            Map<Long, String> consultationModelsMap = new HashMap<Long, String>();
            for (ConsultationModel consultationModel : consultationModels) {
                consultationModelsMap.put(consultationModel.getId(), consultationModel.getName());
            }

            int filedNum = fileds.length;
            String[][] strs = new String[map.size() + 1][filedNum + 2];
            for (int i = 0; i < filedNum; i++) {
                if (filed.split(",")[i].equals("consultationTypeId")) {
                    strs[0][i] = "咨询类型";
                } else if (filed.split(",")[i].equals("consultationModeId")) {
                    strs[0][i] = "咨询方式";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "学段";
                } else if (filed.split(",")[i].equals("gradeid")) {
                    strs[0][i] = "年级";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "班级";
                }
            }
            int total = 0;
            strs[0][filedNum] = "数量";
            strs[0][filedNum + 1] = "百分比";
            int i = 1;
            for (Map map2 : map) {
                for (int j = 0; j < fileds.length; j++) {
                    if (fileds[j].equals("consultationModeId")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : consultationModelsMap.get(map2.get(fileds[j]));
                    } else if (fileds[j].equals("consultationTypeId")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : consultTypesMap.get(map2.get(fileds[j]));
                    } else {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                    }
                }
                strs[i][fileds.length] = map2.get("num").toString();
                total += (Long) map2.get("num");
                i++;
            }
            for (int j = 1; j < strs.length; j++) {
                String[] strings = strs[j];
                strings[filedNum + 1] = percentage(strings[filedNum], total);

            }

            model.addAttribute("list" + index, strs);
        }
        //
        List<String> list1 = new ArrayList<String>();
        list1.add("result");
        list1.add("LEVEL");
        list1.add("type");
        list1.add("dispose_type");
        for (int index = 0; index < list1.size(); index++) {
            String filed = list1.get(index);
            Map<String, String> disposeTypesMap = new HashMap<String, String>();
            disposeTypesMap.put("1", "与班主任会商");
            disposeTypesMap.put("2", "重点观察");
            disposeTypesMap.put("3", "班主任约谈");
            disposeTypesMap.put("4", "咨询员约谈");
            disposeTypesMap.put("5", "报告校领导");
            disposeTypesMap.put("6", "校内干预与保护");
            // disposeTypesMap.put("7", "报告校领导");
            // disposeTypesMap.put("8", "校内干预与保护");
            disposeTypesMap.put("7", "区县中心介入");
            disposeTypesMap.put("8", "市中心介入");
            List<Map> map = analyzeService.select1InSchool(filed, analyze, beginDate, endDate);
            String[] fileds = filed.split(",");

            int filedNum = fileds.length;
            String[][] strs = new String[map.size() + 1][filedNum + 2];
            for (int i = 0; i < filedNum; i++) {
                if (filed.split(",")[i].equals("result")) {
                    strs[0][i] = "干预结果";
                } else if (filed.split(",")[i].equals("LEVEL")) {
                    strs[0][i] = "预警级别";
                } else if (filed.split(",")[i].equals("type")) {
                    strs[0][i] = "干预方式";
                } else if (filed.split(",")[i].equals("dispose_type")) {
                    strs[0][i] = "处置方式";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "学段";
                } else if (filed.split(",")[i].equals("gradeid")) {
                    strs[0][i] = "年级";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "班级";
                }
            }
            int total = 0;
            strs[0][filedNum] = "数量";
            strs[0][filedNum + 1] = "百分比";
            int i = 1;
            for (Map map2 : map) {
                for (int j = 0; j < fileds.length; j++) {
                    if (fileds[j].equals("result")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : InterveneResult.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("LEVEL")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : WarningLever.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("type")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : InterveneType.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("dispose_type")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : disposeTypesMap.get(map2.get(fileds[j]));
                    } else {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                    }
                }
                strs[i][fileds.length] = map2.get("num").toString();
                total += (Long) map2.get("num");
                i++;
            }
            for (int j = 1; j < strs.length; j++) {
                String[] strings = strs[j];
                strings[filedNum + 1] = percentage(strings[filedNum], total);

            }

            model.addAttribute("list_" + index, strs);
        }
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        // reportLookStudentFilterParam.setNj(AgeUitl.getNjName(Integer.parseInt(analyze.getNj())));
        reportLookStudentFilterParam.setNj(String.valueOf(AgeUitl.getNj(Integer.parseInt(analyze.getNj()))));
        reportLookStudentFilterParam.setXd(analyze.getGroupid().intValue());
        if (!StringUtils.isEmpty(analyze.getBj()))
            reportLookStudentFilterParam.setBj(Integer.parseInt(analyze.getBj()));
        // reportLookStudentFilterParam.setEndtime(DateUtil.SimpleDateFormat(endDate,
        // ""));
        // reportLookStudentFilterParam.setStarttime(DateUtil.SimpleDateFormat(beginDate,
        // ""));
        reportLookStudentFilterParam.setStarttime(analyze.getStarttime());
        reportLookStudentFilterParam.setEndtime(analyze.getEndtime());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<Scale> scaleList = reportLookService.queryDistinctScalesInSchool(orgEntity, reportLookStudentFilterParam);
        reportLookStudentFilterParam.setBjidarray(request.getParameter("bjarray"));
        // request.setAttribute("listClassSchool", listClassSchool);
        request.setAttribute("scaleList", scaleList);
        return viewName("viewforschool");
    }

    @RequestMapping(value = "archiveincounty")
    public String archiveincounty(@CurrentOrg Organization orgEntity, Analyze analyze, HttpServletRequest request,
            Model model, Date beginDate, Date endDate) {
        String qx_or_xx = analyze.getQx_or_xx();

        Student stu = new Student();
        stu.setXd(analyze.getGroupid().intValue());
        stu.setNj(analyze.getNj());
        List<StudentWithBLOBs> stList = null;
        if (StringUtils.isEmpty(qx_or_xx))
            stList = studentService.getAllStudentsInCounty(stu, orgEntity.getCountyid(), 0);
        else {
            stList = studentService.getAllStudentsInClass(stu, Long.parseLong(qx_or_xx), 0);
        }
        String[] attrIds = { "105", "106", "205", "218", "219", "220", "221" };
        int totalNum = stList.size();
        int maleNum = 0, femaleNum = 0, hNationNum = 0, otherNationNum = 0;
        int xxANum = 0, xxBNum = 0, xxABNum = 0, xxONum = 0;
        int health10 = 0, health20 = 0, health30 = 0, health40 = 0, health50 = 0;
        int jxfs1 = 0, jxfs2 = 0, jxfs3 = 0, jxfs4 = 0;
        int family10 = 0, family20 = 0, family30 = 0, family40 = 0, family50 = 0, family60 = 0, family70 = 0,
                family80 = 0, family90 = 0;
        int tzdb1 = 0, tzdb2 = 0, tzdb3 = 0, tzdb4 = 0;
        int xsly1 = 0, xsly2 = 0, xsly3 = 0;
        int kncd1 = 0, kncd2 = 0, kncd3 = 0;
        for (int i = 0; i < totalNum; i++) {
            StudentWithBLOBs student = stList.get(i);
            String xbm = student.getXbm();
            if (xbm != null && xbm.equals("1"))
                maleNum++;
            if (xbm != null && xbm.equals("2"))
                femaleNum++;
            String mzm = student.getMzm();
            if (mzm != null && mzm.equals("01"))
                hNationNum++;
            if (mzm != null && !mzm.equals("01"))
                otherNationNum++;

            student.loadMetas();
            student.setBackGrandByStr();
            Map<String, String> map = new HashMap<String, String>();
            student.loadBackGrandToMap(map);

            List<FieldValue> list = student.getAttrs();
            for (String attrId : attrIds) {

                String value = (String) map.get(attrId);
                if (StringUtils.isEmpty(value))
                    continue;
                if (attrId.equals("106")) {
                    if (value.equals("A型"))
                        xxANum++;
                    if (value.equals("B型"))
                        xxBNum++;
                    if (value.equals("AB型"))
                        xxABNum++;
                    if (value.equals("O型"))
                        xxONum++;
                }
                if (attrId.equals("105")) {
                    if (value.equals("健康或良好"))
                        health10++;
                    if (value.equals("一般或较弱"))
                        health20++;
                    if (value.equals("有慢性病"))
                        health30++;
                    if (value.equals("有生理缺陷"))
                        health40++;
                    if (value.equals("残疾"))
                        health50++;
                }
                if (attrId.equals("205")) {
                    if (value.equals("走读"))
                        jxfs1++;
                    if (value.equals("住校"))
                        jxfs2++;
                    if (value.equals("借宿"))
                        jxfs3++;
                    if (value.equals("其它"))
                        jxfs4++;
                }
                if (attrId.equals("218")) {
                    if (value.equals("双亲健全"))
                        family10++;
                    if (value.equals("孤儿"))
                        family20++;
                    if (value.equals("单亲"))
                        family30++;
                    if (value.equals("父母离异"))
                        family40++;
                    if (value.equals("双亲有残疾"))
                        family50++;
                    if (value.equals("本人有残疾"))
                        family60++;
                    if (value.equals("军烈属或优抚对象"))
                        family70++;
                    if (value.equals("重病"))
                        family80++;
                    if (value.equals("五保户"))
                        family90++;
                }
                if (attrId.equals("219")) {
                    if (value.equals("优秀"))
                        tzdb1++;
                    if (value.equals("良好"))
                        tzdb2++;
                    if (value.equals("及格"))
                        tzdb3++;
                    if (value.equals("不合格"))
                        tzdb4++;
                }
                if (attrId.equals("220")) {
                    if (value.equals("正常入学"))
                        xsly1++;
                    if (value.equals("借读"))
                        xsly2++;
                    if (value.equals("其它"))
                        xsly3++;
                }
                if (attrId.equals("221")) {
                    if (value.equals("特别困难"))
                        kncd1++;
                    if (value.equals("一般困难"))
                        kncd2++;
                    if (value.equals("不困难"))
                        kncd3++;
                }
            }

        }
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("maleNum", maleNum);
        model.addAttribute("femaleNum", femaleNum);
        model.addAttribute("hNationNum", hNationNum);
        model.addAttribute("otherNationNum", otherNationNum);
        model.addAttribute("xxANum", xxANum);
        model.addAttribute("xxBNum", xxBNum);
        model.addAttribute("xxABNum", xxABNum);
        model.addAttribute("xxONum", xxONum);
        model.addAttribute("health10", health10);
        model.addAttribute("health20", health20);
        model.addAttribute("health30", health30);
        model.addAttribute("health40", health40);
        model.addAttribute("health50", health50);

        model.addAttribute("jxfs1", jxfs1);
        model.addAttribute("jxfs2", jxfs2);
        model.addAttribute("jxfs3", jxfs3);
        model.addAttribute("jxfs4", jxfs4);
        model.addAttribute("family10", family10);
        model.addAttribute("family20", family20);
        model.addAttribute("family30", family30);
        model.addAttribute("family40", family40);
        model.addAttribute("family50", family50);
        model.addAttribute("family60", family60);
        model.addAttribute("family70", family70);
        model.addAttribute("family80", family80);
        model.addAttribute("family90", family90);
        model.addAttribute("tzdb1", tzdb1);
        model.addAttribute("tzdb2", tzdb1);
        model.addAttribute("tzdb3", tzdb3);
        model.addAttribute("tzdb4", tzdb4);
        model.addAttribute("xsly1", xsly1);
        model.addAttribute("xsly2", xsly2);
        model.addAttribute("xsly3", xsly3);
        model.addAttribute("kncd1", kncd1);
        model.addAttribute("kncd2", kncd2);
        model.addAttribute("kncd3", kncd3);

        analyze.setRole("1");
        analyze.setCountyid(orgEntity.getCountyid());
        analyze.setQx_or_xx(qx_or_xx);
        List<String> list = new ArrayList<String>();
        list.add("consultationTypeId");
        for (int index = 0; index < list.size(); index++) {
            String filed = list.get(index);
            List<Map> map = analyzeService.selectInCounty(filed, analyze, beginDate, endDate);
            String[] fileds = filed.split(",");
            List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
            Map<Long, String> consultTypesMap = new HashMap<Long, String>();
            for (ConsultType consultType : consultTypes) {
                consultTypesMap.put(consultType.getId(), consultType.getName());
            }
            List<ConsultationModel> consultationModels = getConsultationModels();
            Map<Long, String> consultationModelsMap = new HashMap<Long, String>();
            for (ConsultationModel consultationModel : consultationModels) {
                consultationModelsMap.put(consultationModel.getId(), consultationModel.getName());
            }

            int filedNum = fileds.length;
            String[][] strs = new String[map.size() + 1][filedNum + 2];
            for (int i = 0; i < filedNum; i++) {
                if (filed.split(",")[i].equals("consultationTypeId")) {
                    strs[0][i] = "咨询类型";
                } else if (filed.split(",")[i].equals("consultationModeId")) {
                    strs[0][i] = "咨询方式";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "学段";
                } else if (filed.split(",")[i].equals("gradeid")) {
                    strs[0][i] = "年级";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "班级";
                }
            }
            int total = 0;
            strs[0][filedNum] = "数量";
            strs[0][filedNum + 1] = "百分比";
            int i = 1;
            for (Map map2 : map) {
                for (int j = 0; j < fileds.length; j++) {
                    if (fileds[j].equals("consultationModeId")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : consultationModelsMap.get(map2.get(fileds[j]));
                    } else if (fileds[j].equals("consultationTypeId")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : consultTypesMap.get(map2.get(fileds[j]));
                    } else {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                    }
                }
                strs[i][fileds.length] = map2.get("num").toString();
                total += (Long) map2.get("num");
                i++;
            }
            for (int j = 1; j < strs.length; j++) {
                String[] strings = strs[j];
                strings[filedNum + 1] = percentage(strings[filedNum], total);

            }

            model.addAttribute("list" + index, strs);
        }
        //
        List<String> list1 = new ArrayList<String>();
        list1.add("result");
        for (int index = 0; index < list1.size(); index++) {
            String filed = list1.get(index);
            Map<String, String> disposeTypesMap = new HashMap<String, String>();
            disposeTypesMap.put("1", "与班主任会商");
            disposeTypesMap.put("2", "重点观察");
            disposeTypesMap.put("3", "班主任约谈");
            disposeTypesMap.put("4", "咨询员约谈");
            disposeTypesMap.put("5", "报告校领导");
            disposeTypesMap.put("6", "校内干预与保护");
            // disposeTypesMap.put("7", "报告校领导");
            // disposeTypesMap.put("8", "校内干预与保护");
            disposeTypesMap.put("7", "区县中心介入");
            disposeTypesMap.put("8", "市中心介入");
            List<Map> map = analyzeService.select1InCounty(filed, analyze, beginDate, endDate);
            String[] fileds = filed.split(",");

            int filedNum = fileds.length;
            String[][] strs = new String[map.size() + 1][filedNum + 2];
            for (int i = 0; i < filedNum; i++) {
                if (filed.split(",")[i].equals("result")) {
                    strs[0][i] = "干预结果";
                } else if (filed.split(",")[i].equals("LEVEL")) {
                    strs[0][i] = "预警级别";
                } else if (filed.split(",")[i].equals("type")) {
                    strs[0][i] = "干预方式";
                } else if (filed.split(",")[i].equals("dispose_type")) {
                    strs[0][i] = "处置方式";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "学段";
                } else if (filed.split(",")[i].equals("gradeid")) {
                    strs[0][i] = "年级";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "班级";
                }
            }
            int total = 0;
            strs[0][filedNum] = "数量";
            strs[0][filedNum + 1] = "百分比";
            int i = 1;
            for (Map map2 : map) {
                for (int j = 0; j < fileds.length; j++) {
                    if (fileds[j].equals("result")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : InterveneResult.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("LEVEL")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : WarningLever.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("type")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : InterveneType.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("dispose_type")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : disposeTypesMap.get(map2.get(fileds[j]));
                    } else {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                    }
                }
                strs[i][fileds.length] = map2.get("num").toString();
                total += (Long) map2.get("num");
                i++;
            }
            for (int j = 1; j < strs.length; j++) {
                String[] strings = strs[j];
                strings[filedNum + 1] = percentage(strings[filedNum], total);

            }
            model.addAttribute("list_" + index, strs);
        }
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        reportLookStudentFilterParam.setNj(String.valueOf(AgeUitl.getNj(Integer.parseInt(analyze.getNj()))));
        reportLookStudentFilterParam.setXd(analyze.getGroupid().intValue());
        reportLookStudentFilterParam.setStarttime(analyze.getStarttime());
        reportLookStudentFilterParam.setEndtime(analyze.getEndtime());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<Scale> scaleList = null;
        if (!StringUtils.isEmpty(qx_or_xx)) {
            Organization og = new Organization();
            og.setId(Long.parseLong(qx_or_xx));
            scaleList = reportLookService.queryDistinctScalesInSchool(og, reportLookStudentFilterParam);
        } else {
            scaleList = reportLookService.queryDistinceScalesInCounty(orgEntity.getCountyid(),
                    reportLookStudentFilterParam);
        }
        request.setAttribute("scaleList", scaleList);
        reportLookStudentFilterParam.setQxorxxarray(request.getParameter("qxorxxarray"));
        return viewName("viewforcounty");
    }

    @RequestMapping(value = "archiveincity")
    public String archiveincity(@CurrentOrg Organization orgEntity, Analyze analyze, HttpServletRequest request,
            Model model, Date beginDate, Date endDate) {
        String xzxs = request.getParameter("xzxs");

        String qx_or_xx = analyze.getQx_or_xx();

        Student stu = new Student();
        stu.setXd(analyze.getGroupid().intValue());
        stu.setNj(analyze.getNj());
        List<StudentWithBLOBs> stList = null;
        if (xzxs.equals("1")) {// 选择区县
            if (StringUtils.isEmpty(qx_or_xx))
                stList = studentService.getAllStudentsInCity(stu, orgEntity.getCityid(), xzxs, 0);
            else {
                stList = studentService.getAllStudentsInCounty(stu, qx_or_xx, 0);
            }
        } else {// 选择直属学校
            if (StringUtils.isEmpty(qx_or_xx))// 没有选择学校，取所有直属学校
                stList = studentService.getAllStudentsInCity(stu, orgEntity.getCountyid(), xzxs, 0);
            else {// 选择了某个直属学校
                stList = studentService.getAllStudentsInClass(stu, Long.parseLong(qx_or_xx), 0);
            }
        }
        String[] attrIds = { "105", "106", "205", "218", "219", "220", "221" };
        int totalNum = stList.size();
        int maleNum = 0, femaleNum = 0, hNationNum = 0, otherNationNum = 0;
        int xxANum = 0, xxBNum = 0, xxABNum = 0, xxONum = 0;
        int health10 = 0, health20 = 0, health30 = 0, health40 = 0, health50 = 0;
        int jxfs1 = 0, jxfs2 = 0, jxfs3 = 0, jxfs4 = 0;
        int family10 = 0, family20 = 0, family30 = 0, family40 = 0, family50 = 0, family60 = 0, family70 = 0,
                family80 = 0, family90 = 0;
        int tzdb1 = 0, tzdb2 = 0, tzdb3 = 0, tzdb4 = 0;
        int xsly1 = 0, xsly2 = 0, xsly3 = 0;
        int kncd1 = 0, kncd2 = 0, kncd3 = 0;
        for (int i = 0; i < totalNum; i++) {
            StudentWithBLOBs student = stList.get(i);
            String xbm = student.getXbm();
            if (xbm != null && xbm.equals("1"))
                maleNum++;
            if (xbm != null && xbm.equals("2"))
                femaleNum++;
            String mzm = student.getMzm();
            if (mzm != null && mzm.equals("01"))
                hNationNum++;
            if (mzm != null && !mzm.equals("01"))
                otherNationNum++;

            student.loadMetas();
            student.setBackGrandByStr();
            Map<String, String> map = new HashMap<String, String>();
            student.loadBackGrandToMap(map);

            List<FieldValue> list = student.getAttrs();
            for (String attrId : attrIds) {

                String value = (String) map.get(attrId);
                if (StringUtils.isEmpty(value))
                    continue;
                if (attrId.equals("106")) {
                    if (value.equals("A型"))
                        xxANum++;
                    if (value.equals("B型"))
                        xxBNum++;
                    if (value.equals("AB型"))
                        xxABNum++;
                    if (value.equals("O型"))
                        xxONum++;
                }
                if (attrId.equals("105")) {
                    if (value.equals("健康或良好"))
                        health10++;
                    if (value.equals("一般或较弱"))
                        health20++;
                    if (value.equals("有慢性病"))
                        health30++;
                    if (value.equals("有生理缺陷"))
                        health40++;
                    if (value.equals("残疾"))
                        health50++;
                }
                if (attrId.equals("205")) {
                    if (value.equals("走读"))
                        jxfs1++;
                    if (value.equals("住校"))
                        jxfs2++;
                    if (value.equals("借宿"))
                        jxfs3++;
                    if (value.equals("其它"))
                        jxfs4++;
                }
                if (attrId.equals("218")) {
                    if (value.equals("双亲健全"))
                        family10++;
                    if (value.equals("孤儿"))
                        family20++;
                    if (value.equals("单亲"))
                        family30++;
                    if (value.equals("父母离异"))
                        family40++;
                    if (value.equals("双亲有残疾"))
                        family50++;
                    if (value.equals("本人有残疾"))
                        family60++;
                    if (value.equals("军烈属或优抚对象"))
                        family70++;
                    if (value.equals("重病"))
                        family80++;
                    if (value.equals("五保户"))
                        family90++;
                }
                if (attrId.equals("219")) {
                    if (value.equals("优秀"))
                        tzdb1++;
                    if (value.equals("良好"))
                        tzdb2++;
                    if (value.equals("及格"))
                        tzdb3++;
                    if (value.equals("不合格"))
                        tzdb4++;
                }
                if (attrId.equals("220")) {
                    if (value.equals("正常入学"))
                        xsly1++;
                    if (value.equals("借读"))
                        xsly2++;
                    if (value.equals("其它"))
                        xsly3++;
                }
                if (attrId.equals("221")) {
                    if (value.equals("特别困难"))
                        kncd1++;
                    if (value.equals("一般困难"))
                        kncd2++;
                    if (value.equals("不困难"))
                        kncd3++;
                }
            }

        }
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("maleNum", maleNum);
        model.addAttribute("femaleNum", femaleNum);
        model.addAttribute("hNationNum", hNationNum);
        model.addAttribute("otherNationNum", otherNationNum);
        model.addAttribute("xxANum", xxANum);
        model.addAttribute("xxBNum", xxBNum);
        model.addAttribute("xxABNum", xxABNum);
        model.addAttribute("xxONum", xxONum);
        model.addAttribute("health10", health10);
        model.addAttribute("health20", health20);
        model.addAttribute("health30", health30);
        model.addAttribute("health40", health40);
        model.addAttribute("health50", health50);

        model.addAttribute("jxfs1", jxfs1);
        model.addAttribute("jxfs2", jxfs2);
        model.addAttribute("jxfs3", jxfs3);
        model.addAttribute("jxfs4", jxfs4);
        model.addAttribute("family10", family10);
        model.addAttribute("family20", family20);
        model.addAttribute("family30", family30);
        model.addAttribute("family40", family40);
        model.addAttribute("family50", family50);
        model.addAttribute("family60", family60);
        model.addAttribute("family70", family70);
        model.addAttribute("family80", family80);
        model.addAttribute("family90", family90);
        model.addAttribute("tzdb1", tzdb1);
        model.addAttribute("tzdb2", tzdb1);
        model.addAttribute("tzdb3", tzdb3);
        model.addAttribute("tzdb4", tzdb4);
        model.addAttribute("xsly1", xsly1);
        model.addAttribute("xsly2", xsly2);
        model.addAttribute("xsly3", xsly3);
        model.addAttribute("kncd1", kncd1);
        model.addAttribute("kncd2", kncd2);
        model.addAttribute("kncd3", kncd3);
        analyze.setRole("1");
        analyze.setSchoolid(orgEntity.getId());
        List<String> list = new ArrayList<String>();
        list.add("consultationTypeId");
        for (int index = 0; index < list.size(); index++) {
            String filed = list.get(index);
            List<Map> map = analyzeService.selectInCity(filed, analyze, xzxs, beginDate, endDate);
            String[] fileds = filed.split(",");
            List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
            Map<Long, String> consultTypesMap = new HashMap<Long, String>();
            for (ConsultType consultType : consultTypes) {
                consultTypesMap.put(consultType.getId(), consultType.getName());
            }
            List<ConsultationModel> consultationModels = getConsultationModels();
            Map<Long, String> consultationModelsMap = new HashMap<Long, String>();
            for (ConsultationModel consultationModel : consultationModels) {
                consultationModelsMap.put(consultationModel.getId(), consultationModel.getName());
            }

            int filedNum = fileds.length;
            String[][] strs = new String[map.size() + 1][filedNum + 2];
            for (int i = 0; i < filedNum; i++) {
                if (filed.split(",")[i].equals("consultationTypeId")) {
                    strs[0][i] = "咨询类型";
                } else if (filed.split(",")[i].equals("consultationModeId")) {
                    strs[0][i] = "咨询方式";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "学段";
                } else if (filed.split(",")[i].equals("gradeid")) {
                    strs[0][i] = "年级";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "班级";
                }
            }
            int total = 0;
            strs[0][filedNum] = "数量";
            strs[0][filedNum + 1] = "百分比";
            int i = 1;
            for (Map map2 : map) {
                for (int j = 0; j < fileds.length; j++) {
                    if (fileds[j].equals("consultationModeId")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : consultationModelsMap.get(map2.get(fileds[j]));
                    } else if (fileds[j].equals("consultationTypeId")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : consultTypesMap.get(map2.get(fileds[j]));
                    } else {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                    }
                }
                strs[i][fileds.length] = map2.get("num").toString();
                total += (Long) map2.get("num");
                i++;
            }
            for (int j = 1; j < strs.length; j++) {
                String[] strings = strs[j];
                strings[filedNum + 1] = percentage(strings[filedNum], total);

            }

            model.addAttribute("list" + index, strs);
        }
        //
        List<String> list1 = new ArrayList<String>();
        list1.add("result");
        for (int index = 0; index < list1.size(); index++) {
            String filed = list1.get(index);
            Map<String, String> disposeTypesMap = new HashMap<String, String>();
            disposeTypesMap.put("1", "与班主任会商");
            disposeTypesMap.put("2", "重点观察");
            disposeTypesMap.put("3", "班主任约谈");
            disposeTypesMap.put("4", "咨询员约谈");
            disposeTypesMap.put("5", "报告校领导");
            disposeTypesMap.put("6", "校内干预与保护");
            // disposeTypesMap.put("7", "报告校领导");
            // disposeTypesMap.put("8", "校内干预与保护");
            disposeTypesMap.put("7", "区县中心介入");
            disposeTypesMap.put("8", "市中心介入");
            List<Map> map = analyzeService.select1InCity(filed, analyze, xzxs, beginDate, endDate);
            String[] fileds = filed.split(",");

            int filedNum = fileds.length;
            String[][] strs = new String[map.size() + 1][filedNum + 2];
            for (int i = 0; i < filedNum; i++) {
                if (filed.split(",")[i].equals("result")) {
                    strs[0][i] = "干预结果";
                } else if (filed.split(",")[i].equals("LEVEL")) {
                    strs[0][i] = "预警级别";
                } else if (filed.split(",")[i].equals("type")) {
                    strs[0][i] = "干预方式";
                } else if (filed.split(",")[i].equals("dispose_type")) {
                    strs[0][i] = "处置方式";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "学段";
                } else if (filed.split(",")[i].equals("gradeid")) {
                    strs[0][i] = "年级";
                } else if (filed.split(",")[i].equals("groupeid")) {
                    strs[0][i] = "班级";
                }
            }
            int total = 0;
            strs[0][filedNum] = "数量";
            strs[0][filedNum + 1] = "百分比";
            int i = 1;
            for (Map map2 : map) {
                for (int j = 0; j < fileds.length; j++) {
                    if (fileds[j].equals("result")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : InterveneResult.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("LEVEL")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : WarningLever.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("type")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                                : InterveneType.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                    } else if (fileds[j].equals("dispose_type")) {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : disposeTypesMap.get(map2.get(fileds[j]));
                    } else {
                        strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                    }
                }
                strs[i][fileds.length] = map2.get("num").toString();
                total += (Long) map2.get("num");
                i++;
            }
            for (int j = 1; j < strs.length; j++) {
                String[] strings = strs[j];
                strings[filedNum + 1] = percentage(strings[filedNum], total);

            }
            model.addAttribute("list_" + index, strs);
        }
        ReportLookStudentFilterParam reportLookStudentFilterParam = new ReportLookStudentFilterParam();
        reportLookStudentFilterParam.setNj(String.valueOf(AgeUitl.getNj(Integer.parseInt(analyze.getNj()))));
        reportLookStudentFilterParam.setXd(analyze.getGroupid().intValue());
        reportLookStudentFilterParam.setStarttime(analyze.getStarttime());
        reportLookStudentFilterParam.setEndtime(analyze.getEndtime());
        request.setAttribute("reportLookStudentFilterParam", reportLookStudentFilterParam);
        List<Scale> scaleList = null;

        if (xzxs.equals("1")) {// 选择区县
            if (!StringUtils.isEmpty(qx_or_xx)) {
                scaleList = reportLookService.queryDistinceScalesInCounty(qx_or_xx, reportLookStudentFilterParam);
            } else {// 全市
                scaleList = reportLookService.queryDistinctScalesInCity(orgEntity.getCityid(), xzxs,
                        reportLookStudentFilterParam);
            }
        }
        if (xzxs.equals("2")) {
            if (!StringUtils.isEmpty(qx_or_xx)) {
                Organization og = new Organization();
                og.setId(Long.parseLong(qx_or_xx));
                scaleList = reportLookService.queryDistinctScalesInSchool(og, reportLookStudentFilterParam);
            } else {
                scaleList = reportLookService.queryDistinctScalesInCity(orgEntity.getCityid(), xzxs,
                        reportLookStudentFilterParam);
            }
        }
        request.setAttribute("scaleList", scaleList);
        reportLookStudentFilterParam.setQxorxxarray(request.getParameter("qxorxxarray"));
        reportLookStudentFilterParam.setXzxs(xzxs);
        return viewName("viewforcity");
    }

    @RequestMapping(value = { "/list1" })
    public String list1(@CurrentOrg Organization orgEntity, Analyze analyze,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        String filed = request.getParameter("filed");
        analyze.setSchoolid(orgEntity.getId());
        if (analyze.getRole().equals("2")) {
            filed = filed.replace("gradeid,", "");
        }
        if (filed.length() == 0) {
            filed = "result";
        } else {
            filed = filed.substring(0, filed.length() - 1);
        }
        Map<String, String> disposeTypesMap = new HashMap<String, String>();
        disposeTypesMap.put("1", "与班主任会商");
        disposeTypesMap.put("2", "重点观察");
        disposeTypesMap.put("3", "班主任约谈");
        disposeTypesMap.put("4", "咨询员约谈");
        disposeTypesMap.put("5", "报告校领导");
        disposeTypesMap.put("6", "校内干预与保护");
        // disposeTypesMap.put("7", "报告校领导");
        // disposeTypesMap.put("8", "校内干预与保护");
        disposeTypesMap.put("7", "区县中心介入");
        disposeTypesMap.put("8", "市中心介入");
        List<Map> map = analyzeService.select1(filed, analyze, beginDate, endDate);
        String[] fileds = filed.split(",");

        int filedNum = fileds.length;
        String[][] strs = new String[map.size() + 1][filedNum + 2];
        for (int i = 0; i < filedNum; i++) {
            if (filed.split(",")[i].equals("result")) {
                strs[0][i] = "干预结果";
            } else if (filed.split(",")[i].equals("LEVEL")) {
                strs[0][i] = "预警级别";
            } else if (filed.split(",")[i].equals("type")) {
                strs[0][i] = "干预方式";
            } else if (filed.split(",")[i].equals("dispose_type")) {
                strs[0][i] = "处置方式";
            } else if (filed.split(",")[i].equals("groupeid")) {
                strs[0][i] = "学段";
            } else if (filed.split(",")[i].equals("gradeid")) {
                strs[0][i] = "年级";
            } else if (filed.split(",")[i].equals("groupeid")) {
                strs[0][i] = "班级";
            }
        }
        int total = 0;
        strs[0][filedNum] = "数量";
        strs[0][filedNum + 1] = "百分比";
        int i = 1;
        for (Map map2 : map) {
            for (int j = 0; j < fileds.length; j++) {
                if (fileds[j].equals("result")) {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                            : InterveneResult.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                } else if (fileds[j].equals("LEVEL")) {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                            : WarningLever.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                } else if (fileds[j].equals("type")) {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他"
                            : InterveneType.getName(Integer.parseInt(map2.get(fileds[j]) + ""));
                } else if (fileds[j].equals("dispose_type")) {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他" : disposeTypesMap.get(map2.get(fileds[j]));
                } else {
                    strs[i][j] = map2.get(fileds[j]) == null ? "其他" : map2.get(fileds[j]).toString();
                }
            }
            strs[i][fileds.length] = map2.get("num").toString();
            total += (Long) map2.get("num");
            i++;
        }
        for (int j = 1; j < strs.length; j++) {
            String[] strings = strs[j];
            strings[filedNum + 1] = percentage(strings[filedNum], total);

        }

        model.addAttribute("list", strs);
        return viewName("list");
    }

    private String percentage(String str, int total) {
        if (StringUtils.isEmpty(str)) {
            return "0";
        } else {
            int a = Integer.parseInt(str) * 10000 / total;
            double b = a / 100.0;
            return b + "";
        }
    }

}
