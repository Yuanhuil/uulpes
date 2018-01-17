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
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.InterveneResult;
import com.njpes.www.entity.baseinfo.enums.InterveneType;
import com.njpes.www.entity.baseinfo.enums.WarningLever;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.consultcenter.Analyze;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.entity.consultcenter.ConsultationModel;
import com.njpes.www.entity.scaletoollib.ReportLookTeacherFilterParam;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.consultcenter.AnalyzeServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;
import com.njpes.www.service.consultcenter.ConsultationModelServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/archivemanage/archiveTeamTeacher")
public class ArchiveTeamTeacherController extends BaseController {

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

    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        Analyze analyze = new Analyze();
        List<ConsultType> consultTypes = getOpenConsultTypes(orgEntity.getId());
        List<ConsultationModel> consultationModels = getConsultationModels();
        long schoolorgid = orgEntity.getId();

        analyze.setSchoolid(orgEntity.getId());
        List<Role> roleList = roleService.selectAll();
        model.addAttribute("analyze", analyze);

        List<Role> roleList1 = new ArrayList<Role>();
        for (Role role : roleList) {
            if (role.getOrg_level() == orgEntity.getOrgLevel()) {
                roleList1.add(role);
            }
        }
        model.addAttribute("rolelist", roleList1);

        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("consultationModels", consultationModels);
        model.addAttribute("page", page);
        // model.addAttribute("typeEnum",TeamTypeEnum.values() );
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

    @RequestMapping(value = "view")
    public String view(@CurrentOrg Organization orgEntity, Analyze analyze,

            HttpServletRequest request, Model model, Date beginDate, Date endDate) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        int roleid = Integer.parseInt(analyze.getRole());
        request.setAttribute("roleName", analyze.getRole());
        analyze.setRole("2");
        analyze.setSchoolid(orgEntity.getId());
        List<String> list = new ArrayList<String>();
        list.add("consultationTypeId");
        for (int index = 0; index < list.size(); index++) {
            String filed = list.get(index);
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
            disposeTypesMap.put("1", "电话疏导");
            disposeTypesMap.put("2", "辅导室约见");
            disposeTypesMap.put("3", "赶赴现场");
            disposeTypesMap.put("4", "其他");

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

            model.addAttribute("list_" + index, strs);
        }

        ReportLookTeacherFilterParam reportLookTeacherFilterParam = new ReportLookTeacherFilterParam();

        reportLookTeacherFilterParam.setEndtime(DateUtil.SimpleDateFormat(endDate, ""));
        reportLookTeacherFilterParam.setStarttime(DateUtil.SimpleDateFormat(beginDate, ""));
        request.setAttribute("reportLookTeacherFilterParam", reportLookTeacherFilterParam);
        List<ScaleInfo> scaleList = reportLookService.queryTeacherDistinctScales(orgEntity, null, roleid, -1, -1,
                false);
        request.setAttribute("scaleList", scaleList);
        return viewName("view");
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
        disposeTypesMap.put("1", "电话疏导");
        disposeTypesMap.put("2", "辅导室约见");
        disposeTypesMap.put("3", "赶赴现场");
        disposeTypesMap.put("4", "其他");

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
