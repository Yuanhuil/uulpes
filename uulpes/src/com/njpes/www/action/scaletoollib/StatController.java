package com.njpes.www.action.scaletoollib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.dao.scaletoollib.StatColumnMapper;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.entity.baseinfo.attr.SelectOption;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.GradeCode;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.scaletoollib.ExamDoSchoolTask;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.entity.scaletoollib.NormInfo;
import com.njpes.www.entity.scaletoollib.QueryInfo;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.Scalenorm;
import com.njpes.www.entity.scaletoollib.StatConfigWithBLOBs;
import com.njpes.www.entity.scaletoollib.StatDependentVars;
import com.njpes.www.entity.scaletoollib.StatInDependentVars;
import com.njpes.www.entity.scaletoollib.StatObject;
import com.njpes.www.entity.scaletoollib.StatParams;
import com.njpes.www.entity.scaletoollib.StatResult;
import com.njpes.www.entity.scaletoollib.StatScope;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.organization.OrganizationLevelServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ScaleMgrService;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.service.scaletoollib.StatisticsService;
import com.njpes.www.service.util.DictionaryServiceI;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/scaletoollib/statistics")
public class StatController extends BaseController {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    OrganizationLevelServiceI orgLevelService;

    @Autowired
    OrganizationServiceI orgService;

    @Autowired
    DistrictService distService;

    @Autowired
    SchoolServiceI schoolService;

    @Autowired
    CachedScaleMgr cachedScaleMgr;

    @Autowired
    StatColumnMapper columnMapper;

    @Autowired
    ScaleMgrService scaleService;

    @Autowired
    ScaleService scaleInfoService;

    @Autowired
    DictionaryServiceI dicService;

    @Autowired
    ExamresultStudentMapper examresultStudentMapper;

    @Autowired
    ExamresultTeacherMapper examresultTeacherMapper;

    @RequestMapping(value = "{statType}/statFrame", method = RequestMethod.GET)
    public String statFrame(HttpServletRequest request, HttpServletResponse response, Model model,
            @RequestParam("step") Integer step, @PathVariable("statType") int statType) {
        model.addAttribute("statType", statType);
        model.addAttribute("step", step);
        model.addAttribute("stepshow", step);
        return viewName("statframe");
    }

    /**
     * 选择了学校年级班级后点下一步，获取所有的统计方法
     * 
     * @throws Exception
     */
    @RequestMapping(value = "{statType}/nextStep", method = RequestMethod.POST)
    public String nextStep(HttpServletRequest request, HttpServletResponse response, Model model,
            @CurrentOrg Organization org, @RequestParam Integer step, @PathVariable("statType") int statType,
            @RequestParam Integer statObj, RedirectAttributes attrs, HttpSession session) throws Exception {
        // 重新统计清除配置信息
        if (step == 0)
            session.removeAttribute("configs");
        StatConfigWithBLOBs configs = (StatConfigWithBLOBs) session.getAttribute("configs");
        // 如果session里没有config信息，则从数据库中读出来
        if (null == configs) {
            configs = statisticsService.getStatConfig(statType);
            // stat_param表中保存的配置参数放到session里
            session.setAttribute("configs", configs);
            JSONArray configArr = JSONArray.fromObject(configs.getStepPages());
            if (configArr != null)
                session.setAttribute("stepcnt", configArr.size());
        }

        // 从request里获取参数，统计对象是学生还是老师，用用户的所属机构判断
        if (null == statObj)
            statObj = (Integer) session.getAttribute("statObj");
        if (StatObject.STUDENT == statObj || StatObject.TEACHER == statObj) {
            session.setAttribute("statObj", statObj);
            model.addAttribute("statObj", statObj);
        } else {
            throw new Exception("统计对象错误");
        }

        StatParams params = (StatParams) session.getAttribute("statParam");
        if (null == params) {
            params = new StatParams();
            // session里一直保存用来最后做统计的参数,全程不断完善的统计参数，包括对象、因变量、自变量
            session.setAttribute("statParam", params);
        }

        // 根据请求处理session中保存的统计参数
        composalSession(org, request, session);
        String stepshow = request.getParameter("stepshow");
        step = step == null ? 0 : step;
        stepshow = stepshow == null ? "0" : Integer.toString(Integer.parseInt(stepshow) + 1);
        // 根据config里的page[{1:StatScope}, {3:DependentVars},
        // {4:StatResult}]来判断下一步转发的controller和视图
        String jsonPages = configs.getStepPages();
        JSONArray pages = JSONArray.fromObject(jsonPages);
        JSONObject pageObj = pages.getJSONObject(step);
        String nextController = (String) pageObj.keys().next();
        model.addAttribute("step", step);
        // 跳转的规则是/类型/下一个步骤号(1,2,3,4)
        return "redirect:../" + statType + "/" + nextController + ".do?step=" + step + "&stepshow=" + stepshow;
        // return "studentSchoolStatMethod";
    }

    /**
     * 判断所有步骤中可能存在的请求参数，如果存在就保存到session中
     * 
     * @param request
     * @param session
     * @throws UnsupportedEncodingException
     */
    private void composalSession(Organization org, HttpServletRequest request, HttpSession session)
            throws UnsupportedEncodingException {
        Map<String, String[]> paraMap = request.getParameterMap();
        String statObj = "0".equals(request.getParameter("statObj")) ? StringUtils.EMPTY
                : request.getParameter("statObj");
        String school = "[\"0\"]".equals(request.getParameter("school")) ? StringUtils.EMPTY
                : request.getParameter("school");
        String grade = "0".equals(request.getParameter("grade")) ? StringUtils.EMPTY : request.getParameter("grade");
        String clazz = "0".equals(request.getParameter("class")) ? StringUtils.EMPTY : request.getParameter("class");
        String orgLevel = "0".equals(request.getParameter("orgLevel")) ? StringUtils.EMPTY
                : request.getParameter("orgLevel");
        String district = "[\"0\"]".equals(request.getParameter("district")) ? StringUtils.EMPTY
                : request.getParameter("district");
        String starttime = "0".equals(request.getParameter("starttime")) ? StringUtils.EMPTY
                : request.getParameter("starttime");
        String endtime = "0".equals(request.getParameter("endtime")) ? StringUtils.EMPTY
                : request.getParameter("endtime");
        // 写入自变量的参数
        String table = "0".equals(request.getParameter("table")) ? StringUtils.EMPTY : request.getParameter("table");
        String cols = "[\"0\"]".equals(request.getParameter("cols")) ? StringUtils.EMPTY : request.getParameter("cols");
        String vals = "[\"0\"]".equals(request.getParameter("vals")) ? StringUtils.EMPTY : request.getParameter("vals");
        // 写入因变量的参数
        String scaleCode = "0".equals(request.getParameter("scale")) ? StringUtils.EMPTY
                : request.getParameter("scale");
        String dims = "[\"0\"]".equals(request.getParameter("dims")) ? StringUtils.EMPTY : request.getParameter("dims");
        String norm = "0".equals(request.getParameter("norm")) ? StringUtils.EMPTY : request.getParameter("norm");
        String taskid = "0".equals(request.getParameter("task")) ? StringUtils.EMPTY : request.getParameter("task");
        // 写入对比因变量的参数
        String scaleCode1 = "0".equals(request.getParameter("scale1")) ? StringUtils.EMPTY
                : request.getParameter("scale1");
        String dims1 = "[\"0\"]".equals(request.getParameter("dims1")) ? StringUtils.EMPTY
                : request.getParameter("dims1");
        String norm1 = "0".equals(request.getParameter("norm1")) ? StringUtils.EMPTY : request.getParameter("norm1");
        String taskid1 = "0".equals(request.getParameter("task1")) ? StringUtils.EMPTY : request.getParameter("task1");

        StatScope scope = null;
        if (StringUtils.isNotEmpty(orgLevel) && StringUtils.isNotEmpty(district)) {
            scope = new StatScope();
            if ("3".equals(orgLevel)) { // 市级
                List<String> districts = JSONArray.toList(JSONArray.fromObject(district), new String(),
                        new JsonConfig());
                if (org.getOrgLevel() == Integer.parseInt(orgLevel)) {
                    scope.setCityId(districts.get(0));
                }
            } else if ("4".equals(orgLevel)) { // 区县级
                List<String> districts = JSONArray.toList(JSONArray.fromObject(district), new String(),
                        new JsonConfig());
                scope.setCountyId(districts);
            } else if ("5".equals(orgLevel)) { // 乡镇级
                List<String> districts = JSONArray.toList(JSONArray.fromObject(district), new String(),
                        new JsonConfig());
                scope.setTownId(districts);
            }
        }

        if (StringUtils.isNotEmpty(school)) {
            if (scope == null)
                scope = new StatScope();
            if ("0".equals(school)) {
                scope.setSchoolId(null);
            } else {
                List<String> schools = JSONArray.toList(JSONArray.fromObject(school), new String(), new JsonConfig());
                scope.setSchoolId(schools);
            }
        }
        if (StringUtils.isNotEmpty(grade)) {
            if (scope == null)
                scope = new StatScope();
            if ("0".equals(grade)) {
                scope.setGradeId(null);
            } else {
                scope.setGradeId(grade);
            }
        }
        if (StringUtils.isNotEmpty(clazz)) {
            if (scope == null)
                scope = new StatScope();
            if ("0".equals(clazz)) {
                scope.setClassId(null);
            } else {
                scope.setClassId(clazz);
            }
        }
        if (StringUtils.isNotEmpty(starttime)) {
            if (scope == null)
                scope = new StatScope();
            scope.setStartTime(starttime);
        }
        if (StringUtils.isNotEmpty(endtime)) {
            if (scope == null)
                scope = new StatScope();
            scope.setEndTime(endtime);
        }
        if (scope != null)
            ((StatParams) session.getAttribute("statParam")).setScope(scope);

        if (StringUtils.isNotEmpty(statObj) && StringUtils.isNumeric(statObj)) {
            ((StatParams) session.getAttribute("statParam")).setStatObj(Integer.parseInt(statObj));
        }

        // 表名写入session
        if (((StatParams) session.getAttribute("statParam")).getIndpVars() == null) {
            List<StatInDependentVars> varList = new ArrayList<StatInDependentVars>();
            varList.add(new StatInDependentVars());
            ((StatParams) session.getAttribute("statParam")).setIndpVars(varList);
        }
        if (StringUtils.isNotEmpty(table)) {
            ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0).setTableName(table);
        } else if (StringUtils.EMPTY.equals(table)) {
            if (((StatParams) session.getAttribute("statParam")).getIndpVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getIndpVars().size() > 0
                    && ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0) != null)
                ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0).setTableName(null);
        }
        // 列名写入session
        if (null != cols) {
            if (StringUtils.EMPTY.equals(cols)) {
                if (((StatParams) session.getAttribute("statParam")).getIndpVars() != null
                        && ((StatParams) session.getAttribute("statParam")).getIndpVars().size() > 0
                        && ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0) != null)
                    ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0).setCols(null);
            } else {
                JSONArray colsArray = JSONArray.fromObject(cols);
                ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0).setCols(colsArray);
            }
        }
        // 值写入session
        if (StringUtils.isNotEmpty(vals)) {
            if (StringUtils.EMPTY.equals(vals)) {
                if (((StatParams) session.getAttribute("statParam")).getIndpVars() != null
                        && ((StatParams) session.getAttribute("statParam")).getIndpVars().size() > 0
                        && ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0) != null)
                    ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0).setVals(null);
            } else {
                JSONArray valsArray = JSONArray.fromObject(vals);
                ((StatParams) session.getAttribute("statParam")).getIndpVars().get(0).setVals(valsArray);
            }
        }

        // 量表写入session
        if (StringUtils.isNotEmpty(scaleCode)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                List statDeps = new ArrayList<StatDependentVars>();
                ((StatParams) session.getAttribute("statParam")).setDepVars(statDeps);
            }
            if (((StatParams) session.getAttribute("statParam")).getDepVars().size() == 0)
                ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            Scale scale = cachedScaleMgr.get(scaleCode);
            if (null != scale) {
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setScaleCode(scale);
                if (session.getAttribute("statParam") != null
                        && ((StatParams) session.getAttribute("statParam")).getScope() != null)
                    ((StatParams) session.getAttribute("statParam")).getScope().setScaleId(scaleCode);
            }
        } else if (StringUtils.EMPTY.equals(scaleCode)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 0
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(0) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setScaleCode(null);
            if (((StatParams) session.getAttribute("statParam")).getScope() != null)
                ((StatParams) session.getAttribute("statParam")).getScope().setScaleId(null);
        }
        // 维度写入session
        if (StringUtils.isNotEmpty(dims)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                List statDeps = new ArrayList<StatDependentVars>();
                ((StatParams) session.getAttribute("statParam")).setDepVars(statDeps);
            } else {
                if (((StatParams) session.getAttribute("statParam")).getDepVars().size() == 0)
                    ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            }
            JSONArray dimsStrArray = JSONArray.fromObject(dims);
            if (dimsStrArray.size() == 1 && dimsStrArray.getString(0).equals("0"))
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setDims(null);
            else {
                if (((StatParams) session.getAttribute("statParam")).getDepVars().size() > 0) {
                    Scale scale = ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).getScaleCode();
                    if (null != scale) {
                        JSONArray dimsArray = new JSONArray();
                        for (int i = 0; i < dimsStrArray.size(); i++) {
                            String dimStr = dimsStrArray.getString(i);
                            if (scale.getDimensionMap().containsKey(dimStr))
                                dimsArray.add(dimStr);
                            ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setDims(dimsArray);
                        }
                    }
                }
            }
        } else if (StringUtils.EMPTY.equals(dims)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 0
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(0) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setDims(null);
        }
        // 常模写入session
        if (StringUtils.isNotEmpty(norm)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                List statDeps = new ArrayList<StatDependentVars>();
                ((StatParams) session.getAttribute("statParam")).setDepVars(statDeps);
            } else {
                if (((StatParams) session.getAttribute("statParam")).getDepVars().size() == 0)
                    ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            }
            if (((StatParams) session.getAttribute("statParam")).getDepVars().size() > 0) {
                Scale scale = ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).getScaleCode();
                if (null != scale) {
                    List<Scalenorm> ni = scaleInfoService.getNorm(Integer.parseInt(scale.getId()));
                    ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setNorm(ni);
                    /*
                     * if(null != ni && ni.size()>0){ for(Scalenorm n: ni){
                     * ((StatParams)session.getAttribute("statParam")).
                     * getDepVars().get(0).getNorm().add(n); } }
                     */
                }
            }
        } else if (StringUtils.EMPTY.equals(norm)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 0
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(0) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setNorm(null);
        }
        if (StringUtils.isNotEmpty(taskid)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                List statDeps = new ArrayList<StatDependentVars>();
                ((StatParams) session.getAttribute("statParam")).setDepVars(statDeps);
            } else {
                if (((StatParams) session.getAttribute("statParam")).getDepVars().size() == 0)
                    ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            }
            ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setTaskid(Integer.parseInt(taskid));
        } else if (StringUtils.EMPTY.equals(taskid)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 0
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(0) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(0).setTaskid(null);
        }

        // 对比量表写入session
        if (StringUtils.isEmpty(scaleCode1) && StringUtils.isNotEmpty(scaleCode))
            scaleCode1 = scaleCode;
        if (StringUtils.isNotEmpty(scaleCode1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                ((StatParams) session.getAttribute("statParam")).setDepVars(new ArrayList<StatDependentVars>());
                ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            }

            if (((StatParams) session.getAttribute("statParam")).getDepVars().size() == 1) {
                ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            }
            Scale scale = cachedScaleMgr.get(scaleCode1);
            if (null != scale) {
                if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                        && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1
                        && ((StatParams) session.getAttribute("statParam")).getDepVars().get(1) != null)
                    ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setScaleCode(scale);
            }
        } else if (StringUtils.EMPTY.equals(scaleCode1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(1) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setScaleCode(null);
        }
        // 对比维度写入session
        if (StringUtils.isEmpty(dims1))
            dims1 = dims;
        if (StringUtils.isNotEmpty(dims1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                ((StatParams) session.getAttribute("statParam")).setDepVars(new ArrayList<StatDependentVars>());
            }
            JSONArray dimsStrArray = JSONArray.fromObject(dims1);
            if (dimsStrArray.size() == 1 && dimsStrArray.getString(0).equals("0"))
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setDims(null);
            if (((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1) {
                Scale scale = ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).getScaleCode();
                if (null != scale) {
                    JSONArray dimsArray = new JSONArray();
                    for (int i = 0; i < dimsStrArray.size(); i++) {
                        String dimStr = dimsStrArray.getString(i);
                        Dimension dim = scale.getDimensionMap().get(dimStr);
                        if (scale.getDimensionMap().containsKey(dimStr))
                            dimsArray.add(dimStr);
                        ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setDims(dimsArray);
                    }
                }
            }
        } else if (StringUtils.EMPTY.equals(dims1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(1) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setDims(null);
        }
        // 对比常模写入session
        if (StringUtils.isNotEmpty(norm1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                ((StatParams) session.getAttribute("statParam")).setDepVars(new ArrayList<StatDependentVars>());
            }
            if (((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1) {
                Scale scale = ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).getScaleCode();
                if (null != scale) {
                    List<Scalenorm> ni = scaleInfoService.getNorm(Integer.parseInt(scale.getId()));
                    ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setNorm(ni);
                    /*
                     * if(null != ni && ni.size()>0){ for(Scalenorm n: ni){
                     * ((StatParams)session.getAttribute("statParam")).
                     * getDepVars().get(1).getNorm().add(n); } }
                     */
                }
            }
        } else if (StringUtils.EMPTY.equals(norm1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(1) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setNorm(null);
        }

        if (StringUtils.isNotEmpty(taskid1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() == null) {
                List statDeps = new ArrayList<StatDependentVars>();
                ((StatParams) session.getAttribute("statParam")).setDepVars(statDeps);
            } else {
                if (((StatParams) session.getAttribute("statParam")).getDepVars().size() == 0)
                    ((StatParams) session.getAttribute("statParam")).getDepVars().add(new StatDependentVars());
            }
            ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setTaskid(Integer.parseInt(taskid1));
        } else if (StringUtils.EMPTY.equals(taskid1)) {
            if (((StatParams) session.getAttribute("statParam")).getDepVars() != null
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().size() > 1
                    && ((StatParams) session.getAttribute("statParam")).getDepVars().get(1) != null)
                ((StatParams) session.getAttribute("statParam")).getDepVars().get(1).setTaskid(null);
        }

        String statmethod = request.getParameter("statType");
        if (StringUtils.isNotEmpty(statmethod) && statmethod.matches("\\d+"))
            ((StatParams) session.getAttribute("statParam")).setMethod(Integer.parseInt(statmethod));
    }

    /**
     * 准备组织机构、行政区划、学校、年级
     * 
     * @throws Exception
     */
    @RequestMapping(value = "{statType}/1", method = RequestMethod.GET)
    public String main(@CurrentOrg Organization org, HttpServletRequest request, Model model,
            @PathVariable("statType") int statType, HttpSession session) throws Exception {
        String orgType = org.getOrgType();
        model.addAttribute("statType", statType);
        StatConfigWithBLOBs configs = (StatConfigWithBLOBs) session.getAttribute("configs");
        String jsonPages = configs.getStepPages();
        JSONArray pages = JSONArray.fromObject(jsonPages);
        String jspPage = null; // 引用的jsp模板
        String stepStr = request.getParameter("step");
        String stepShow = request.getParameter("stepshow");
        if (StringUtils.isNotEmpty(stepStr)) {
            Integer step = Integer.parseInt(stepStr);
            Integer stepshow = Integer.parseInt(stepShow);
            if (null != step) {
                model.addAttribute("step", step);
                model.addAttribute("stepshow", stepshow);
                JSONObject pageObj = pages.getJSONObject(step);
                jspPage = (String) pageObj.values().iterator().next();
            }
        }

        if (OrganizationType.ec.getId().equals(orgType)) {
            List<OrganizationLevel> orgLevelList = orgLevelService.queryOrgLevelList(org.getOrgLevel(), true, true);
            // 删除全国级别
            if (orgLevelList.get(0).getId() == 1)
                orgLevelList.remove(0);
            // 删除学校级别
            if (orgLevelList.get(orgLevelList.size() - 1).getId() == 6)
                orgLevelList.remove(orgLevelList.size() - 1);
            model.addAttribute("orgLevelList", orgLevelList);
            model.addAttribute("orgType", orgType);
            return viewName("studentComm" + jspPage);
        } else if (OrganizationType.school.getId().equals(orgType)) {
            // 如果是学校的管理员或心理老师
            List<School> schools = new ArrayList<School>();
            School sc = schoolService.getSchoolInfoByOrgId(org.getId());
            schools.add(sc);
            model.addAttribute("schoolList", schools);
            model.addAttribute("orgType", orgType);
            return viewName("studentSchool" + jspPage);
        } else {
            // 如果用户所在机构不是教委也不是学校报错页面
            throw new Exception("登录用户信息错误");
        }
    }

    /**
     * 准备行政区划
     * 
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/getDistricts", method = RequestMethod.POST)
    @ResponseBody
    public String getDistricts(@CurrentOrg Organization org, HttpServletRequest request, Model model,
            HttpSession session) throws Exception {
        String levelStr = request.getParameter("orgLevel");
        composalSession(org, request, session);
        if (StringUtils.isNotEmpty(levelStr) && !levelStr.equals("0")) {
            Integer currentLevel = Integer.parseInt(levelStr);
            // 如果要查的行政区划级别低于等于用户所属的组织机构级别才可以获得结果
            List<Organization> districts = null;
            if (currentLevel.equals(org.getOrgLevel())) {
                districts = new ArrayList<Organization>();
                Organization o = new Organization();
                String code = org.getCityid();
                o.setId(Long.parseLong(code));
                o.setName(distService.selectByCode(code).getName());
                districts.add(o);
            } else if (currentLevel > org.getOrgLevel()) {
                switch (currentLevel) {
                case 4:
                    districts = orgService.getCountiesByCity(org.getCityid());
                    break;
                case 5:
                    districts = orgService.getTownsByCounty(org.getCountyid());
                }
            }
            JSONArray dists = new JSONArray();
            if (null != districts) {
                for (Organization district : districts) {
                    JSONObject districtObj = new JSONObject();
                    districtObj.accumulate("id", district.getId());
                    districtObj.accumulate("name", district.getName());
                    dists.add(districtObj);
                }
            }
            return dists.size() == 0 ? "" : dists.toString();
        }
        return "";
    }

    /**
     * 准备学校
     */
    @RequestMapping(value = "/getSchools", method = RequestMethod.POST)
    @ResponseBody
    public String getSchools(@CurrentOrg Organization org, HttpServletRequest request, Model model, HttpSession session)
            throws Exception {
        String orgLevel = request.getParameter("orgLevel");
        String dist = request.getParameter("dist");
        composalSession(org, request, session);
        List<String> dists = new ArrayList<String>();
        if (StringUtils.isNoneEmpty(dist)) {
            dists = JSONArray.toList(JSONArray.fromObject(dist), new String(), new JsonConfig());
        }
        List<School> schoolList = null;
        if ("3".equals(orgLevel)) {
            // ((StatParams)session.getAttribute("statParam")).getScope().setCityId(dist);
            // schoolList = orgService.getSchoolOrgByCityId(dist);
            List<String> ids = new ArrayList<String>();
            ids.addAll(dists);
            schoolList = schoolService.getSchoolAccordingCityIds(ids);
        }
        if ("4".equals(orgLevel)) {
            // ((StatParams)session.getAttribute("statParam")).getScope().setCountyId(dist);
            // schoolList = orgService.getSchoolOrgByCountyId(dist);
            List<String> ids = new ArrayList<String>();
            ids.addAll(dists);
            schoolList = schoolService.getSchoolAccordingCountyIds(ids);
        }
        if ("5".equals(orgLevel)) {
            // ((StatParams)session.getAttribute("statParam")).getScope().setTownId(dist);
            // schoolList = orgService.getSchoolOrgByTownId(dist);
            List<String> ids = new ArrayList<String>();
            ids.addAll(dists);
            schoolList = schoolService.getSchoolAccordingTownIds(ids);
        }
        JSONArray schoolArray = new JSONArray();
        for (School school : schoolList) {
            JSONObject schObj = new JSONObject();
            schObj.accumulate("id", school.getId());
            schObj.accumulate("xxmc", school.getXxmc());
            schoolArray.add(schObj);
        }
        return schoolArray.toString();
    }

    /**
     * 根据学校选择年级
     */
    @RequestMapping(value = "/getGrades", method = RequestMethod.POST)
    @ResponseBody
    public String getGrades(@CurrentOrg Organization org, HttpServletRequest request, HttpSession session)
            throws Exception {
        String school = request.getParameter("school");
        composalSession(org, request, session);
        JSONArray gradeArray = new JSONArray();
        Map<String, String> gradeMap = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(school) && !"0".equals(school)) {
            List<String> schools = JSONArray.toList(JSONArray.fromObject(school), new String(), new JsonConfig());
            List<Grade> grades = schoolService.getGradeinSchools(schools);
            gradeArray.addAll(grades);
            /*
             * for(String sch:schools){ long scode =
             * schoolService.selectByPrimaryKey(Long.parseLong(sch)).getOrgid();
             * List<Grade> grades = schoolService.getGradeListInSchool(scode);
             * for(Grade grade: grades){
             * if(!gradeMap.containsKey(grade.getGradeid())){ JSONObject
             * gradeObj = new JSONObject(); gradeObj.accumulate("id",
             * grade.getGradeid()); gradeObj.accumulate("nj", grade.getNj());
             * gradeObj.accumulate("njmc", grade.getNjmc());
             * gradeArray.add(gradeObj); gradeMap.put(grade.getGradeid(), ""); }
             * } }
             */
        }
        return gradeArray.toString();
    }

    /**
     * 根据学校选择班级
     */
    @RequestMapping(value = "/getClasses", method = RequestMethod.POST)
    @ResponseBody
    public String getClasses(@CurrentOrg Organization org, HttpServletRequest request, HttpSession session)
            throws Exception {
        String schoolStr = request.getParameter("school");
        String grade = request.getParameter("grade");
        composalSession(org, request, session);
        JSONArray classArray = new JSONArray();
        ClassSchool cs = new ClassSchool();
        if (StringUtils.isNotEmpty(schoolStr)) {
            List<String> schools = JSONArray.toList(JSONArray.fromObject(schoolStr), new String(), new JsonConfig());
            for (String school : schools) {
                long sid = Long.parseLong(school);
                cs.setId(sid);
                if (StringUtils.isNoneEmpty(grade) && grade.matches("\\d+"))
                    cs.setGradeid(Integer.parseInt(grade));
                List<ClassSchool> classes = schoolService.getClassByGradeidInSchool(cs, 0);
                for (ClassSchool clas : classes) {
                    JSONObject classObj = new JSONObject();
                    classObj.accumulate("bh", clas.getBh());
                    classObj.accumulate("bjmc", clas.getBjmc());
                    classObj.accumulate("id", clas.getId());
                    classArray.add(classObj);
                }
            }
        }
        return classArray.toString();
    }

    /**
     * 获得所有的因变量
     * 
     * @throws Exception
     */
    @RequestMapping(value = "{statType}/2", method = RequestMethod.GET)
    public String getDependentVars(HttpServletRequest request, HttpServletResponse response, Model model,
            HttpSession session, @PathVariable("statType") int statType) {
        // 根据stat_param中配置的各个步骤跳转的页面来决定初始化什么参数
        StatConfigWithBLOBs configs = (StatConfigWithBLOBs) session.getAttribute("configs");
        StatScope scope = ((StatParams) session.getAttribute("statParam")).getScope();
        model.addAttribute("statType", statType);
        Integer statObj = (Integer) session.getAttribute("statObj");
        model.addAttribute("statObj", statObj);
        String jsonPages = configs.getStepPages();
        JSONArray pages = JSONArray.fromObject(jsonPages);
        String stepStr = request.getParameter("step");
        String stepShow = request.getParameter("stepshow");
        if (StringUtils.isNotEmpty(stepStr)) {
            Integer step = Integer.parseInt(stepStr);
            Integer stepshow = Integer.parseInt(stepShow);
            String jspPage = null;
            if (null != step) {
                model.addAttribute("step", step);
                model.addAttribute("stepshow", stepshow);
                JSONObject pageObj = pages.getJSONObject(step);
                jspPage = (String) pageObj.values().iterator().next();
            }
            if (StringUtils.isNotEmpty(jspPage) && isInDependentPage(jspPage)) {
                String independentConfig = configs.getIndependentVars();
                if (StringUtils.isNotEmpty(independentConfig)) {
                    if ("column".equals(independentConfig)) {
                        // 调用columnMapper获得当前统计对象（学生）对应所有的可统计列
                        if (statObj == StatObject.STUDENT) {
                            // List<StatColumn> cloumnList =
                            // columnMapper.selectByTable("student");
                            Map<String, String> columns = new HashMap<String, String>();
                            // for(StatColumn col:cloumnList){
                            // columns.put(col.getColName(), col.getColTitle());
                            // }
                            PropObject propObject = null;
                            propObject = PropObject.createPropObject(AcountTypeFlag.student.getId());
                            propObject.loadMetas();
                            List<FieldValue> list = propObject.getAttrs();
                            for (FieldValue col : list) {
                                if (col.getType().equals("select"))
                                    columns.put(col.getId(), col.getLabel());
                            }
                            if (StringUtils.isEmpty(scope.getClassId()))
                                columns.put("999", "班级");
                            model.addAttribute("columns", columns);
                        } else {
                            // 老师可以统计的列
                            Map<String, String> columns = new HashMap<String, String>();
                            PropObject propObject = null;
                            propObject = PropObject.createPropObject(AcountTypeFlag.teacher.getId());
                            propObject.loadMetas();
                            List<FieldValue> list = propObject.getAttrs();
                            for (FieldValue col : list) {
                                if (col.getType().equals("select"))
                                    columns.put(col.getId(), col.getLabel());
                            }
                            model.addAttribute("columns", columns);
                        }
                    } else {
                        // 其他可能的自变量选项
                    }
                } else {
                    model.addAttribute("ex", new Exception("自变量配置错误"));
                    return viewName("/common/error");
                }
            }
            return viewName("studentSchool" + jspPage);
        } else {
            model.addAttribute("ex", new Exception("步骤设置错误"));
            return viewName("/common/error");
        }
    }

    @RequestMapping(value = "/getColumnValues", method = RequestMethod.POST)
    @ResponseBody
    public String getColumnValues(@CurrentOrg Organization org, HttpServletRequest request, HttpSession session)
            throws Exception {
        /*
         * String colName = (String)request.getParameter("colName"); StatColumn
         * sc =
         * (StatColumn)SpringContextHolder.getBean("statColumn",StatColumn.class
         * ); sc.setTableName("student"); sc.setColName(colName);
         * List<StatColumn> dictables = columnMapper.getDicTableName(sc); String
         * dicTable = dictables.get(0).getDicTableName();
         * if(StringUtils.isNotEmpty(dicTable)){ List<Dictionary> colValues =
         * dicService.selectAllDic(dicTable); JSONArray colValueArray = new
         * JSONArray(); for(Dictionary colValue: colValues){ JSONObject
         * colValueObj = new JSONObject(); colValueObj.accumulate("id",
         * colValue.getId()); colValueObj.accumulate("name",
         * colValue.getName()); colValueArray.add(colValueObj); } return
         * colValueArray.toString(); }else{ throw new Exception("字段信息错误"); }
         */

        String colName = request.getParameter("colName");
        composalSession(org, request, session);
        if (StringUtils.isNoneEmpty(colName)) {
            // 如果选中的字段是年级
            if ("999".equals(colName)) {
                // 选择所有做过测评的年级
                List<ExamresultStudent> njs = examresultStudentMapper
                        .queryDistinctNJFromExamresultStudent(new Organization(), -1, false);
                JSONArray colValueArray = new JSONArray();
                for (ExamresultStudent nj : njs) {
                    JSONObject colValueObj = new JSONObject();
                    colValueObj.accumulate("id", GradeCode.getGradecodeByXdNj(nj.getXd(), nj.getNj()));
                    colValueObj.accumulate("name", nj.getNjmc());
                    colValueArray.add(colValueObj);
                }
                return colValueArray.toString();
            }
            Integer statObj = (Integer) session.getAttribute("statObj");
            PropObject propObject = null;
            if (statObj == StatObject.STUDENT) {
                propObject = PropObject.createPropObject(AcountTypeFlag.student.getId());
            } else {
                propObject = PropObject.createPropObject(AcountTypeFlag.teacher.getId());
            }
            propObject.loadMetas();
            List<FieldValue> list = propObject.getFieldValues(new String[] { colName });
            JSONArray colValueArray = new JSONArray();
            for (FieldValue col : list) {
                List<SelectOption> opts = col.getOptionList();
                for (SelectOption opt : opts) {
                    JSONObject colValueObj = new JSONObject();
                    colValueObj.accumulate("id", opt.getValue());
                    colValueObj.accumulate("name", opt.getTitle());
                    colValueArray.add(colValueObj);
                }
            }
            return colValueArray.toString();
        } else {
            return new JSONArray().toString();
        }
    }

    /**
     * 根据学生、教师选择所有的量表
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "{statType}/3", method = RequestMethod.GET)
    public String getAllScales(HttpServletRequest request, Model model, HttpSession session,
            @PathVariable("statType") int statType) throws Exception {
        StatParams statParam = (StatParams) session.getAttribute("statParam");
        StatScope scope = ((StatParams) session.getAttribute("statParam")).getScope();
        // Integer statObj =
        // Integer.parseInt((String)request.getAttribute("statObj"));
        model.addAttribute("statType", statType);
        Integer statObj = (Integer) session.getAttribute("statObj");
        model.addAttribute("statObj", statObj);
        List<ScaleInfo> scales;
        List<Integer> scaleIds = new ArrayList<Integer>();
        List<Dictionary> scaleTypes = new ArrayList<Dictionary>();
        Map<String, String> allScaleTypes = dicService.selectAllDicMap("dic_scaletype");
        StatConfigWithBLOBs configs = (StatConfigWithBLOBs) session.getAttribute("configs");
        String jsonPages = configs.getStepPages();
        JSONArray pages = JSONArray.fromObject(jsonPages);
        String stepStr = request.getParameter("step");
        String stepShow = request.getParameter("stepshow");
        if (StringUtils.isNotEmpty(stepStr)) {
            Integer step = Integer.parseInt(stepStr);
            Integer stepshow = Integer.parseInt(stepShow);
            String jspPage = null;
            if (null != step) {
                model.addAttribute("step", step);
                model.addAttribute("stepshow", stepshow);
                JSONObject pageObj = pages.getJSONObject(step);
                jspPage = (String) pageObj.values().iterator().next();
            }
            if (StringUtils.isNotEmpty(jspPage) && isDependentPage(jspPage)) {
                String dependentConfig = configs.getDependentVars();
                if ("scale".equals(dependentConfig)) {
                    if (statObj == StatObject.STUDENT) {
                        scaleIds = examresultStudentMapper.getScaleTypeByScope(scope);
                        scales = examresultStudentMapper.getScaleListByTypeAndScope(0, scope);
                        // statisticsService.queryAllScalesForStudent();
                    } else {
                        scales = examresultTeacherMapper.getScaleListByTypeAndScope(0, scope);
                        // statisticsService.queryAllScalesForTeacher();
                        scaleIds = examresultTeacherMapper.getScaleTypeByScope(scope);
                    }
                    for (Integer scaleId : scaleIds) {
                        if (scaleId != null && !scaleId.equals(0)) {
                            Dictionary kv = new Dictionary();
                            kv.setId(Integer.toString(scaleId));
                            kv.setName(allScaleTypes.get(Integer.toString(scaleId)));
                            scaleTypes.add(kv);
                        }
                    }
                    model.addAttribute("scaleTypeList", scaleTypes);
                    model.addAttribute("scaleList", scales);
                } else {
                    // 除了量表以外的因变量其他可能性
                }
            }
            return viewName("studentSchool" + jspPage);
        } else {
            throw new Exception("步骤设置错误");
        }
    }

    /**
     * 根据量表类型获得所有的量表
     */
    @RequestMapping(value = "/getScales", method = RequestMethod.POST)
    @ResponseBody
    public String getScales(@CurrentOrg Organization org, HttpServletRequest request, HttpSession session)
            throws Exception {
        String scaleType = request.getParameter("scaleType");
        if (scaleType == null)
            scaleType = request.getParameter("scaleType1");
        composalSession(org, request, session);
        Integer statObj = (Integer) session.getAttribute("statObj");
        StatScope scope = ((StatParams) session.getAttribute("statParam")).getScope();
        QueryInfo qparam = new QueryInfo();
        JSONArray scaleArray = new JSONArray();
        if (StringUtils.isNotEmpty(scaleType)) {
            qparam.setTypeid(Integer.parseInt(scaleType));
            List<ScaleInfo> scales = new ArrayList<ScaleInfo>();
            if (statObj == StatObject.STUDENT) {
                scales = examresultStudentMapper.getScaleListByTypeAndScope(Integer.parseInt(scaleType), scope);
                // examresultStudentMapper.QueryScaleList(qparam);
            } else {
                scales = examresultTeacherMapper.getScaleListByTypeAndScope(Integer.parseInt(scaleType), scope);
                // scales = examresultTeacherMapper.QueryScaleList(qparam);
            }
            for (ScaleInfo scale : scales) {
                JSONObject scaleObj = new JSONObject();
                scaleObj.accumulate("code", scale.getCode());
                scaleObj.accumulate("name", scale.getTitle());
                scaleArray.add(scaleObj);
            }
        }
        return scaleArray.toString();
    }

    /**
     * 根据量表类型和统计对象获得所有量表以及测评结果对应的分发时间
     */
    @RequestMapping(value = "/getTaskScales", method = RequestMethod.POST)
    @ResponseBody
    public String getTaskScales(@CurrentOrg Organization org, HttpServletRequest request, HttpSession session)
            throws Exception {
        StatParams statParam = (StatParams) session.getAttribute("statParam");
        JSONArray scaleArray = new JSONArray();
        composalSession(org, request, session);
        Integer statObj = (Integer) session.getAttribute("statObj");
        if (null != statParam) {
            StatScope scope = statParam.getScope();
            String scaleType = request.getParameter("scaleType");
            if (StringUtils.isNotEmpty(scaleType)) {
                // List<StatExamResultTask> tasks = new
                // ArrayList<StatExamResultTask>();
                List<ScaleInfo> scales = new ArrayList<ScaleInfo>();
                if (statObj == StatObject.STUDENT) {
                    scales = examresultStudentMapper.getScaleListByTypeAndScope(Integer.parseInt(scaleType), scope);
                } else {
                    scales = examresultTeacherMapper.getScaleListByTypeAndScope(Integer.parseInt(scaleType), scope);
                }
                for (ScaleInfo scale : scales) {
                    JSONObject scaleObj = new JSONObject();
                    // scaleObj.accumulate("code",
                    // task.getTaskid()+"_"+task.getCode());
                    scaleObj.accumulate("code", scale.getCode());
                    /*
                     * SimpleDateFormat format = new
                     * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); SimpleDateFormat
                     * format1 = new SimpleDateFormat("yyyy年MM月"); Date date =
                     * null; try { date = format.parse(task.getStartTime()); }
                     * catch (ParseException e) { e.printStackTrace(); }
                     */
                    // scaleObj.accumulate("name",
                    // format1.format(date)+task.getTitle());
                    scaleObj.accumulate("name", scale.getTitle());
                    scaleArray.add(scaleObj);
                }
            }
        }
        return scaleArray.toString();
    }

    /**
     * 根据量表获得所有的分发任务时间
     * 
     * @throws ParseException
     */
    @RequestMapping(value = "/getTaskTimes", method = RequestMethod.POST)
    @ResponseBody
    public String getTaskTimes(@CurrentOrg Organization org, HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws Exception {
        composalSession(org, request, session);
        StatParams statParam = (StatParams) session.getAttribute("statParam");
        JSONArray tasktimes = new JSONArray();
        String scaleCode = (String) request.getParameter("scale");
        Integer statObj = (Integer) session.getAttribute("statObj");

        if (null != statParam) {
            StatScope scope = statParam.getScope();
            scope.setScaleId(scaleCode);
            List<ExamDoSchoolTask> taskTimes = new ArrayList<ExamDoSchoolTask>();
            if (statObj == StatObject.STUDENT) {
                taskTimes = examresultStudentMapper.getTaskTimesByTypeAndScope(scope);
            } else {
                taskTimes = examresultTeacherMapper.getTaskTimesByTypeAndScope(scope);
            }
            for (ExamDoSchoolTask taskTime : taskTimes) {
                JSONObject obj = new JSONObject();
                obj.accumulate("taskid", taskTime.getId());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月");
                Date date = null;
                date = format.parse(taskTime.getStarttime());
                obj.accumulate("createtime", format1.format(date));
                tasktimes.add(obj);
            }

        }

        return tasktimes.toString();
    }

    /**
     * 根据量表获得所有的维度
     */
    @RequestMapping(value = "/getDimensions", method = RequestMethod.POST)
    @ResponseBody
    public String getDimensions(@CurrentOrg Organization org, HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws Exception {
        composalSession(org, request, session);
        String scaleCode = (String) request.getParameter("scale");
        if (scaleCode == null)
            scaleCode = (String) request.getParameter("scale1");
        Scale scale = cachedScaleMgr.get(scaleCode);
        List<Dimension> dimensions = scale.getDimensions();
        JSONArray dimArray = new JSONArray();
        for (Dimension dim : dimensions) {
            JSONObject dimObj = new JSONObject();
            dimObj.accumulate("id", dim.getId());
            dimObj.accumulate("name", dim.getTitle());
            dimArray.add(dimObj);
        }
        return dimArray.toString();
    }

    /**
     * 根据量表获得所有的维度
     */
    @RequestMapping(value = "/getNorms", method = RequestMethod.POST)
    @ResponseBody
    public String getNorms(@CurrentOrg Organization org, HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws Exception {
        composalSession(org, request, session);
        String scaleCode = (String) request.getParameter("scale");
        if (scaleCode == null)
            scaleCode = (String) request.getParameter("scale1");
        Scale scale = cachedScaleMgr.get(scaleCode);
        int scale_id = Integer.parseInt(scale.getId());
        List<NormInfo> norms = scaleInfoService.getNormInfo(scale_id);
        JSONArray normArray = new JSONArray();
        for (NormInfo norm : norms) {
            JSONObject normObj = new JSONObject();
            normObj.accumulate("id", norm.getId());
            normObj.accumulate("name", norm.getName());
            normArray.add(normObj);
        }
        return normArray.toString();
    }

    /**
     * 根据统计类型、统计方法和选择的因变量自变量以及统计人群计算统计结果
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "{statType}/4", method = RequestMethod.GET)
    public String getStatResult(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("statType") int statType, HttpSession session, Model model) throws Exception {
        StatParams statParam = (StatParams) session.getAttribute("statParam");
        Integer statObj = (Integer) session.getAttribute("statObj");
        String stepShow = request.getParameter("stepshow");
        model.addAttribute("stepshow", stepShow);
        // 在前面的tab标签里把学生和教师分开
        // ===========to be continued============
        statParam.setStatObj(statObj);
        statParam.setMethod(statType); // 统计方法和统计类型的值是相同的
        // 根据统计类型不同，获得统计结果，然后渲染到页面
        StatResult res = statisticsService.doStat(statParam);
        model.addAttribute("results", res);
        return viewName("statResult");
    }

    /**
     * 判断配置的页面路径中是否是关于因变量的，原则就是包含"dependent"关键字，不包含"independent"
     * 
     * @param page
     * @return
     */
    private boolean isDependentPage(String page) {
        String pageStr = page.toLowerCase();
        return pageStr.indexOf("dependent") >= 0 && pageStr.indexOf("independent") < 0;
    }

    /**
     * 判断配置的页面路径中是否是关于自变量的，原则就是包含"independent"关键字
     * 
     * @param page
     * @return
     */
    private boolean isInDependentPage(String page) {
        String pageStr = page.toLowerCase();
        return pageStr.indexOf("independent") >= 0;
    }
}
