package com.njpes.www.action.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.njpes.www.dao.scaletoollib.ScaleInfoDao;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.entity.scaletoollib.QueryInfo;
import com.njpes.www.entity.scaletoollib.ScaleFilterParam;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.Scaletype;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.AgeUitl;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilMisc;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/ajax")
public class ajaxController {
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    CachedScaleMgr cachedScaleMgr;
    @Autowired
    ScaleInfoDao scaleInfoDao;
    @Autowired
    StudentServiceI studentService;
    @Autowired
    TeacherServiceI teacherService;
    @Autowired
    RoleServiceI roleService;
    @Autowired
    DistrictService districtService;
    @Autowired
    OrganizationServiceI organizationService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    private ExamdoServiceI examdoService;

    private static int getScaleFlag(int xdid) {
        if (xdid == 1)
            return ScaleUtils.SCALE_EL_FLAG;
        else if (xdid == 2)
            return ScaleUtils.SCALE_SE_FLAG;
        else
            return ScaleUtils.SCALE_HS_FLAG;
    }

    /**
     * 得到学校的对应年级的所有班级和量表
     * @param req
     * @param resp
     */
    @RequestMapping(value = "/getClassesByGradeId", method = RequestMethod.POST)
    @ResponseBody
    public String getClassesAndScalesAction(@CurrentOrg Organization org, HttpServletRequest req,
            HttpServletResponse resp) {
        // String xdinfo = req.getParameter("gradeOrderId");
        String gradeOrderId = req.getParameter("gradeOrderId");
        List<ScaleInfo> scaleList;

        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        mess.append("<scales><scaleId>-1</scaleId>");
        mess.append("<scaleTitle>请选择</scaleTitle></scales>");
        mess.append("<classes><classId>-1</classId>");
        mess.append("<name>请选择</name></classes>");
        // if(xdinfo==null||xdinfo.length()==0){
        if (gradeOrderId == null) {
            return null;
        } else {
            // String[] xdgrade = xdinfo.split("-");
            // int xdid = Integer.valueOf(xdgrade[0]).intValue();

            // XueDuan xd = XueDuan.valueOf(xdid);
            // List<ClassSchool> classList =
            // schoolService.getClassByGradeInSchool(org.getCode(), xd,
            // xdgrade[1]);
            int gradeid = Integer.parseInt(gradeOrderId);
            List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(org.getId(), gradeid, 0);
            for (ClassSchool clDetail : classList) {
                mess.append("<classes><classId>" + clDetail.getId() + "," + clDetail.getBjmc() + "</classId>");
                mess.append("<name>" + clDetail.getBjmc() + "</name></classes>");
            }
            // scaleList = (List<ScaleInfo>)
            // scaleInfoDao.queryScaleForStudent(xdid);
            // for(ScaleInfo scale : scaleList){
            // mess.append("<scales><scaleId>"+scale.getCode()+"</scaleId>");
            // mess.append("<scaleTitle>"+scale.getTitle()+"</scaleTitle></scales>");
            // }
        }
        mess.append("</mess>");
        // req.setAttribute("result", mess);
        return mess.toString();

    }

    @RequestMapping(value = "/getClassesByGradeId2", method = RequestMethod.POST)
    @ResponseBody
    public String getClassesByGradeId2(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            HttpServletRequest req, HttpServletResponse resp) {
        int grade = Integer.parseInt(gradeid);
        List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(org.getId(), grade, 0);
        Gson gson = new Gson();
        String classStr = gson.toJson(classList);
        return classStr;
    }

    @RequestMapping(value = "/getClassesAndScaleTypeByGradeId", method = RequestMethod.POST)
    @ResponseBody
    public String getClassesAndScaleTypeByGradeId(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            @Param("bjid") String bjid, HttpServletRequest req, HttpServletResponse resp) {
        int grade = Integer.parseInt(gradeid);
        List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(org.getId(), grade, 0);
        int groupid = 0;
        if (grade < 7)
            groupid = 1;
        else if (grade < 11)
            groupid = 2;
        else if (grade < 14)
            groupid = 3;

        List<Scaletype> scaletypeList = scaleService.queryScaleByGroupId(groupid);
        List<ScaleInfo> scaleList = examdoService.selectDistinctScaleByGradeId(org, gradeid, bjid);
        Map classAndScaleTypeMap = new HashMap<String, Object>();
        classAndScaleTypeMap.put("classList", classList);
        classAndScaleTypeMap.put("scaletypeList", scaletypeList);
        classAndScaleTypeMap.put("scaleList", scaleList);

        Gson gson = new Gson();
        String classStr = gson.toJson(classAndScaleTypeMap);
        return classStr;
    }

    @RequestMapping(value = "/getScaleTypeByClassId", method = RequestMethod.POST)
    @ResponseBody
    public String getScaleTypeByClassId(@CurrentOrg Organization org, @Param("bjid") String bjid,
            HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.selectDistinctScaleByGradeId(org, null, bjid);
        // Map classAndScaleTypeMap = new HashMap<String,Object>();
        // classAndScaleTypeMap.put("scaleList",scaleList);

        Gson gson = new Gson();
        String scaleStr = gson.toJson(scaleList);
        return scaleStr;
    }

    @RequestMapping(value = "/getClassesAndScalesByGradeId", method = RequestMethod.POST)
    @ResponseBody
    public String getClassesAndScalesByGradeId(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            HttpServletRequest req, HttpServletResponse resp) {
        int grade = Integer.parseInt(gradeid);
        List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(org.getId(), grade, 0);
        String group = null;
        if (grade < 7)
            group = "小学";
        else if (grade < 10)
            group = "初中";
        else if (grade < 14)
            group = "高中";
        else if (grade > 13)
            group = "成人";
        ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
        scaleFilterParam.setApplicablePerson(group);
        scaleFilterParam.setIsWarn("-1");
        scaleFilterParam.setScaleId("");
        scaleFilterParam.setScaleSourceId("-1");
        scaleFilterParam.setScaleTypeId("-1");
        List<Scale> scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        Map classAndScaleMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                String scaleid = scaleList.get(i).getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleList.get(i).getTitle());
                scaleinfoList.add(scaleinfo);
            }
        }
        classAndScaleMap.put("classList", classList);
        classAndScaleMap.put("scaleList", scaleinfoList);

        Gson gson = new Gson();
        String classStr = gson.toJson(classAndScaleMap);
        return classStr;
    }

    @RequestMapping(value = "/getStudentGradeAndScaleInCounty", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentGradeAndScaleInCounty(@Param("countyid") String countyid, HttpServletRequest req,
            HttpServletResponse resp) {
        List<Grade> gradeList = examdoService.getStudentDistinctGradeInCounty(countyid);
        List<ScaleInfo> scaleList = examdoService.getStudentDistinctScaleInCounty(countyid);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                // Map scaletypeMap = new HashMap();
                // Map scalesourceMap = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }
        }
        resultMap.put("gradeList", gradeList);
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);
        // resultMap.put("scaleTypeList",scaleTypeList);
        // resultMap.put("scaleSourceList",scaleSourceList);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/queryBJAndScaleFromExamresultStudentInSchool", method = RequestMethod.POST)
    @ResponseBody
    public String queryBJAndScaleFromExamresultStudentInSchool(@RequestParam("nj") int nj, @RequestParam("xd") int xd,
            @RequestParam("bj") int bj, @CurrentOrg Organization orgEntity) {
        int orgId = orgEntity.getId().intValue();
        List<ExamresultStudent> ersList = examdoService.queryBJFromExamresultStudentByNj(orgId, xd, nj);
        Map<?, ?> param = null;
        if (bj == -1)
            param = UtilMisc.toMap("schoolid", orgId, "xd", xd, "nj", nj, "bj", null);
        else
            param = UtilMisc.toMap("schoolid", orgId, "xd", xd, "nj", nj, "bj", bj);
        List<ScaleInfo> scaleList = examdoService.getStudentDistinctScaleInSchool(param);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                // Map scaletypeMap = new HashMap();
                // Map scalesourceMap = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }
        }
        resultMap.put("ersList", ersList);
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/getStudentGradeAndScaleInSchool", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentGradeAndScaleInSchool(@Param("schoolid") String schoolid, HttpServletRequest req,
            HttpServletResponse resp) {
        List<Grade> gradeList = examdoService.getStudentDistinctGradeInSchool(schoolid);
        List<ScaleInfo> scaleList = examdoService.getStudentDistinctScaleInSchool(schoolid, null);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                // Map scaletypeMap = new HashMap();
                // Map scalesourceMap = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }
        }
        resultMap.put("gradeList", gradeList);
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);
        // resultMap.put("scaleTypeList",scaleTypeList);
        // resultMap.put("scaleSourceList",scaleSourceList);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/getScaleInCountyByGradeidFromExamdoStudent", method = RequestMethod.POST)
    @ResponseBody
    public String getScaleInCountyByGradeidFromExamdoStudent(@Param("countyid") String countyid,
            @Param("gradeid") String gradeid, HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.getStudentDistinctScaleInCountyByGrade(countyid, gradeid);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }
        }
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/getStudentGradeAndScaleInSonSchool", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentGradeAndScaleInSonSchool(@Param("schoolid") String schoolid, String gradeid,
            HttpServletRequest req, HttpServletResponse resp) {
        List<Grade> gradeList = examdoService.getStudentDistinctGradeInSchool(schoolid);
        List<ScaleInfo> scaleList = examdoService.getStudentDistinctScaleInSchool(schoolid, null);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {
                Map scaleinfo = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }

            for (Object key : scaleTypeMap.keySet()) {
                String value = scaleTypeMap.get(key.toString()).toString();
            }
            for (Object key : scaleSourceMap.keySet()) {
                String value = scaleSourceMap.get(key.toString()).toString();
            }
        }
        resultMap.put("gradeList", gradeList);
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @RequestMapping(value = "/getScaleByType", method = RequestMethod.POST)
    @ResponseBody
    public String getScaleByTypeAction(@Param("typeid") String typeid, HttpServletRequest req,
            HttpServletResponse resp) {
        int type = Integer.parseInt(typeid);
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTypeid(type);
        List<ScaleInfo> scaleList = scaleService.QueryScaleList(queryInfo);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/selectDistinctScales", method = RequestMethod.POST)
    @ResponseBody
    public String selectDistinctScales(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            @Param("bjid") String bjid, @Param("roleid") String roleid, @Param("typeid") int typeid,
            @Param("sourceid") int sourceid, HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.selectDistinctScales(org, gradeid, bjid, roleid, typeid, sourceid);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/selectDistinctScaleAcorStype", method = RequestMethod.POST)
    @ResponseBody
    public String selectDistinctScaleAcorStype(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            @Param("bjid") String bjid, @Param("roleid") String roleid, @Param("typeid") int typeid,
            HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.selectDistinctScaleAcorStype(org, gradeid, bjid, roleid, typeid);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/selectDistinctScaleBySource", method = RequestMethod.POST)
    @ResponseBody
    public String selectDistinctScaleBySource(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            @Param("bjid") String bjid, @Param("roleid") String roleid, @Param("sourceid") int sourceid,
            HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.selectDistinctScaleBySource(org, gradeid, bjid, roleid, sourceid);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/selectDistinctScaleByGradeId", method = RequestMethod.POST)
    @ResponseBody
    public String selectDistinctScaleByGradeId(@CurrentOrg Organization org, @Param("gradeid") String gradeid,
            @Param("bjid") String bjid, HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.selectDistinctScaleByGradeId(org, gradeid, bjid);
        // scaleList = ScaleUtils.filterThreadAngleForNoStudent(scaleList);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/selectDistinctScaleByRoleId", method = RequestMethod.POST)
    @ResponseBody
    public String selectDistinctScaleByRoleId(@CurrentOrg Organization org, @Param("roleid") String roleid,
            HttpServletRequest req, HttpServletResponse resp) {
        List<ScaleInfo> scaleList = examdoService.selectDistinctScaleByRoleId(org, roleid);
        // scaleList = ScaleUtils.filterThreadAngleForNoStudent(scaleList);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/queryScales1", method = RequestMethod.POST)
    @ResponseBody
    public String queryScales1(@Param("typeid") String typeid, @Param("sourceid") String sourceid,
            HttpServletRequest req, HttpServletResponse resp) {
        int type = Integer.parseInt(typeid);
        int source = Integer.parseInt(sourceid);
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTypeid(type);
        queryInfo.setSource(source);
        List<ScaleInfo> scaleList = scaleService.QueryScaleList(queryInfo);
        scaleList = ScaleUtils.filterThreadAngleForNoStudent(scaleList);
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleList);
        return classStr;
    }

    @RequestMapping(value = "/queryScales", method = RequestMethod.POST)
    @ResponseBody
    public String queryScales(@Param("typeid") String typeid, @Param("sourceid") String sourceid,
            @Param("gradeid") String gradeid, HttpServletRequest req, HttpServletResponse resp) {
        ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
        if (gradeid != null) {
            int grade = Integer.parseInt(gradeid);
            String group = null;
            if (grade < 7)
                group = "小学";
            else if (grade < 10)
                group = "初中";
            else if (grade < 14)
                group = "高中";
            else if (grade > 13)
                group = "成人";
            scaleFilterParam.setApplicablePerson(group);
        }

        // ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
        // scaleFilterParam.setApplicablePerson("成人");
        scaleFilterParam.setIsWarn("-1");
        scaleFilterParam.setScaleId("");
        scaleFilterParam.setScaleSourceId(sourceid);
        scaleFilterParam.setScaleTypeId(typeid);
        List<Scale> scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        Map classAndScaleMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                String scaleid = scaleList.get(i).getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleList.get(i).getCode());
                scaleinfo.put("title", scaleList.get(i).getTitle());
                scaleinfoList.add(scaleinfo);
            }
        }
        Gson gson = new Gson();
        String classStr = gson.toJson(scaleinfoList);
        return classStr;
    }

    @RequestMapping(value = "/getGradeScales", method = RequestMethod.POST)
    @ResponseBody
    public String getGradeScales(HttpServletRequest req, HttpServletResponse resp) {
        String gradepart = req.getParameter("gradepart");
        int xdid = Integer.parseInt(gradepart);

        List scaleTypes = scaleService.getScaleTypes();
        List scaleSources = scaleService.getScaleSource();
        JSONArray jArray = new JSONArray();
        JSONArray jArray1 = new JSONArray();
        // JSONObject jObj = new JSONObject();

        for (Object obj : scaleTypes) {
            JSONObject jObj = new JSONObject();
            Scaletype st = (Scaletype) obj;
            // jObj.put("scaleTypeId", st.getId());
            jObj.put("scaleTypeId", st.getName());
            jObj.put("scaleTypeTitle", st.getName());
            jArray1.add(jObj);
        }
        JSONObject jScaletypeObj = new JSONObject();
        jScaletypeObj.put("scaletype", jArray1);
        JSONArray jArray2 = new JSONArray();
        for (Object obj : scaleSources) {
            // ScaleSource ss = (ScaleSource)obj;
            Dictionary ss = (Dictionary) obj;
            JSONObject jObj = new JSONObject();
            // jObj.put("scaleSourceId", ss.getId());
            jObj.put("scaleSourceId", ss.getName());
            jObj.put("scaleSourceTitle", ss.getName());
            jArray2.add(jObj);
        }
        JSONObject jScaleSourceObj = new JSONObject();
        jScaleSourceObj.put("scalesource", jArray2);

        jArray.add(jScaletypeObj);

        jArray.add(jScaleSourceObj);

        List<Scale> scaleList = null;
        String objectType = req.getParameter("objectType");
        if (objectType.equals("1"))// 学生
        {
            String gradeStr = req.getParameter("gradeOrderId");
            String st = "";
            if (xdid == 1)
                st = "小学";
            else if (xdid == 2)
                st = "初中";
            else if (xdid == 3)
                st = "高中";
            if (xdid == 5)
                st = "成人";
            ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
            scaleFilterParam.setApplicablePerson(st);
            scaleFilterParam.setIsWarn("-1");
            scaleFilterParam.setScaleId("");
            scaleFilterParam.setScaleSourceId("-1");
            scaleFilterParam.setScaleTypeId("-1");
            scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
            // scaleList = (List<ScaleInfo>)
            // scaleService.queryScaleByGroupId(groupid);
        }
        if (objectType.equals("2")) {// 教师
            // scaleList = (List<ScaleInfo>)
            // scaleInfoDao.queryScaleForTeacher();
            ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
            scaleFilterParam.setApplicablePerson("成人");
            scaleFilterParam.setIsWarn("-1");
            scaleFilterParam.setScaleId("");
            scaleFilterParam.setScaleSourceId("-1");
            scaleFilterParam.setScaleTypeId("-1");
            scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        }
        JSONArray jArray3 = new JSONArray();
        for (Scale scale : scaleList) {
            String scaleid = scale.getCode();
            if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                continue;
            JSONObject jObj = new JSONObject();
            jObj.put("scaleId", scale.getCode());
            jObj.put("scaleShortName", scale.getShortname());
            jObj.put("scaleQNum", scale.getQuestionNum());
            jObj.put("scaleType", scale.getScaleType());
            jObj.put("scaleTitle", scale.getTitle());
            jObj.put("scaleShowTitle", scale.getShowtitle());
            jArray3.add(jObj);
        }

        JSONObject jScale = new JSONObject();
        jScale.put("scale", jArray3);
        jArray.add(jScale);
        return jArray.toString();
    }

    @RequestMapping(value = "/searchScale", method = RequestMethod.POST)
    @ResponseBody
    public String searchScale(HttpServletRequest req, HttpServletResponse resp) {
        String scaletype = req.getParameter("scaletype");
        String scalesource = req.getParameter("scalesource");
        String iswarning = req.getParameter("iswarning");
        String scalename = req.getParameter("scalename");
        String objectType = req.getParameter("objectType");
        List<Scale> scaleList = null;

        if (objectType.equals("1"))// 学生
        {
            String gradeStr = req.getParameter("gradeOrderId");
            String gradepart = req.getParameter("gradepart");
            int xdid = 0;
            if (gradeStr != null)
                xdid = AgeUitl.getXdByGrade(Integer.parseInt(gradeStr));
            if (gradepart != null)
                xdid = Integer.parseInt(gradepart);
            String st = "";
            if (xdid == 1)
                st = "小学";
            else if (xdid == 2)
                st = "初中";
            else if (xdid == 3)
                st = "高中";
            ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
            scaleFilterParam.setApplicablePerson(st);
            scaleFilterParam.setIsWarn(iswarning);
            scaleFilterParam.setScaleId(scalename);
            scaleFilterParam.setScaleSourceId(scalesource);
            scaleFilterParam.setScaleTypeId(scaletype);
            scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        }
        if (objectType.equals("2")) {// 教师
            ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
            scaleFilterParam.setApplicablePerson("成人");
            scaleFilterParam.setIsWarn(iswarning);
            scaleFilterParam.setScaleId(scalename);
            scaleFilterParam.setScaleSourceId(scalesource);
            scaleFilterParam.setScaleTypeId(scaletype);
            scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        }
        JSONArray jArray = new JSONArray();
        for (Scale scale : scaleList) {
            String scaleid = scale.getCode();
            if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                continue;
            JSONObject jObj = new JSONObject();
            jObj.put("scaleId", scale.getCode());
            jObj.put("scaleShortName", scale.getShortname());
            jObj.put("scaleQNum", scale.getQuestionNum());
            jObj.put("scaleType", scale.getScaleType());
            jObj.put("scaleTitle", scale.getTitle());
            jArray.add(jObj);
        }

        JSONObject jScale = new JSONObject();
        jScale.put("scale", jArray);
        return jScale.toString();

    }

    @RequestMapping(value = "/getGradeScales1", method = RequestMethod.POST)
    @ResponseBody
    public String getGradeScales1(HttpServletRequest req, HttpServletResponse resp) {
        String objectType = req.getParameter("objectType");
        String gradepart = req.getParameter("gradepart");
        int xdid = Integer.parseInt(gradepart);
        List<ScaleInfo> scaleList;

        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        scaleList = (List<ScaleInfo>) scaleInfoDao.queryScaleForStudent(xdid);
        scaleList = ScaleUtils.filterThreadAngleForNoStudent(scaleList);
        for (ScaleInfo scale : scaleList) {

            mess.append("<scales><scaleId>" + scale.getCode() + "</scaleId>");
            mess.append("<scaleTitle>" + scale.getTitle() + "</scaleTitle></scales>");
        }
        mess.append("</mess>");
        return mess.toString();

    }

    @RequestMapping(value = "/getTeacherRoles", method = RequestMethod.POST)
    @ResponseBody
    public String getTeacherRoles(@CurrentOrg Organization org, HttpServletRequest req) {
        long orgid = org.getId();
        List<Role> roleList = roleService.getTeacherRoles(orgid);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");

        for (Role role : roleList) {
            mess.append("<roles><roleId>" + role.getId() + "</roleId>");
            mess.append("<roleName>" + role.getRoleName() + "</roleName></roles>");
        }
        mess.append("</mess>");
        return mess.toString();

    }

    @RequestMapping(value = "/getSchoolTeacherRoles", method = RequestMethod.POST)
    @ResponseBody
    public String getSchoolTeacherRoles(@CurrentOrg Organization org, HttpServletRequest req) {
        long orgid = org.getId();
        List<Role> roleList = roleService.getTeacherRoles(orgid);
        Gson gson = new Gson();
        String roleStr = gson.toJson(roleList);
        return roleStr;

    }

    @RequestMapping(value = "/getTeacherRolesByOrglevel", method = RequestMethod.POST)
    @ResponseBody
    public String getTeacherRolesByOrglevel(@Param("orglevel") int orglevel, HttpServletRequest req) {
        List<Role> roleList = roleService.getTeacherRolesByOrglevel(orglevel);
        Gson gson = new Gson();
        String roleStr = gson.toJson(roleList);
        return roleStr;

    }

    @RequestMapping(value = "/getEduTeacherRoles", method = RequestMethod.POST)
    @ResponseBody
    public String getEduTeacherRoles(@CurrentOrg Organization org, HttpServletRequest req) {
        long orgid = org.getId();
        List<Role> roleList = roleService.getEduTeacherRoles(orgid);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");

        for (Role role : roleList) {
            mess.append("<roles><roleId>" + role.getId() + "</roleId>");
            mess.append("<roleName>" + role.getRoleName() + "</roleName></roles>");
        }
        mess.append("</mess>");
        return mess.toString();

    }

    @RequestMapping(value = "/getGradeClass", method = RequestMethod.POST)
    @ResponseBody
    public String getGradeClassAction(@CurrentOrg Organization org, HttpServletRequest req) {
        String xxdm = org.getCode();
        String xdStr = req.getParameter("xueduan");

        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        if (xdStr == null || xdStr.length() == 0 || "0".equals(xdStr)) {

        } else {
            int xdid = Integer.parseInt(xdStr);
            List<Grade> gradeList = schoolService.getGradesInSchool(org.getId(), XueDuan.valueOf(xdid));
            for (int i = 0; i < gradeList.size(); i++) {
                Grade grade = gradeList.get(i);
                // String nj = grade.getNj();
                int gradeid = Integer.parseInt(grade.getGradeid());
                // String njname = getGradeName(xdid,nj);
                String njname = grade.getNjmc();
                mess.append("<grades><gradeId>" + gradeid + "</gradeId><name>" + njname + "</name>");
                // List<ClassSchool> classList =
                // schoolService.getClassByGradeInSchool(orgid,
                // XueDuan.valueOf(xdid), nj);
                List<ClassSchool> classList = schoolService.getClassByGradeIdInSchool(org.getId(), gradeid, 0);
                for (int j = 0; j < classList.size(); j++) {
                    ClassSchool cs = classList.get(j);
                    String bjmc = cs.getBjmc();
                    // mess.append("<gradeclass><classid>"+nj+"-"+cs.getId()+"</classid>");
                    mess.append("<gradeclass><classid>" + gradeid + "," + cs.getId() + ":" + bjmc + "</classid>");
                    mess.append("<classname>" + bjmc + "</classname></gradeclass>");

                }
                mess.append("</grades>");
            }
        }
        mess.append("</mess>");
        return mess.toString();
    }

    /**
     * 得到学校的对应年级对应班级的所有学生
     * @param req
     * @param resp
     * @param page
     */
    @RequestMapping(value = "/getStudents", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentsAction(@CurrentOrg Organization org, HttpServletRequest req) {

        String xdinfo = req.getParameter("gradeOrderId");
        String bjinfo = req.getParameter("classId");
        String[] bjinfoArray = bjinfo.split(",");
        String bjid = bjinfoArray[0];

        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        if (xdinfo == null || xdinfo.length() == 0 || "0".equals(xdinfo) || bjid == null || bjid.length() == 0) {

        } else {
            // String[] xdgrade = xdinfo.split("-");
            // int xdid = Integer.valueOf(xdgrade[0]).intValue();
            // String nj = xdgrade[1];
            Student stu = new Student();
            stu.setBjid(Integer.parseInt(bjid));
            // stu.setXd(xdid);
            // stu.setNj(nj);
            stu.setBjid(Integer.parseInt(bjid));
            // Collection<StudentWithBLOBs>
            // stList=studentService.getAllStudentsInClass(stu, org.getCode());
            Collection<StudentWithBLOBs> stList = studentService.getAllStudentsInClass(stu, org.getId(), 0);
            for (StudentWithBLOBs student : stList) {
                mess.append("<students><studentId>" + student.getId() + "</studentId>");
                mess.append("<name>" + student.getXm() + "</name></students>");
            }
        }
        mess.append("</mess>");
        return mess.toString();
    }

    /**
     * 得到学校的对应年级对应班级的所有学生
     * @param req
     * @param resp
     * @param page
     */
    @RequestMapping(value = "/getTeachers", method = RequestMethod.POST)
    @ResponseBody
    public String getTeachersAction(@CurrentOrg Organization org, HttpServletRequest req) {

        String teacherRole = req.getParameter("teacherRole");
        int roleid = Integer.parseInt(teacherRole);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        Collection<Teacher> teaList = teacherService.getTeacherInSchool(org.getId(), roleid);
        for (Teacher teacher : teaList) {
            mess.append("<teachers><teacherId>" + teacher.getId() + "</teacherId>");
            mess.append("<name>" + teacher.getXm() + "</name></teachers>");
        }
        mess.append("</mess>");
        return mess.toString();
    }

    /**
     * 得到量表
     * @param req
     * @param resp
     * @param page
     */
    @RequestMapping(value = "/getScales", method = RequestMethod.POST)
    @ResponseBody
    public String getScalesAction(HttpServletRequest req) {
        List scaleTypes = scaleService.getScaleTypes();
        List scaleSources = scaleService.getScaleSource();
        JSONArray jArray = new JSONArray();
        JSONArray jArray1 = new JSONArray();
        // JSONObject jObj = new JSONObject();

        for (Object obj : scaleTypes) {
            JSONObject jObj = new JSONObject();
            Scaletype st = (Scaletype) obj;
            // jObj.put("scaleTypeId", st.getId());
            jObj.put("scaleTypeId", st.getName());
            jObj.put("scaleTypeTitle", st.getName());
            jArray1.add(jObj);
        }
        JSONObject jScaletypeObj = new JSONObject();
        jScaletypeObj.put("scaletype", jArray1);
        JSONArray jArray2 = new JSONArray();
        for (Object obj : scaleSources) {
            // ScaleSource ss = (ScaleSource)obj;
            Dictionary ss = (Dictionary) obj;
            JSONObject jObj = new JSONObject();
            // jObj.put("scaleSourceId", ss.getId());
            jObj.put("scaleSourceId", ss.getName());
            jObj.put("scaleSourceTitle", ss.getName());
            jArray2.add(jObj);
        }
        JSONObject jScaleSourceObj = new JSONObject();
        jScaleSourceObj.put("scalesource", jArray2);

        jArray.add(jScaletypeObj);

        jArray.add(jScaleSourceObj);

        List<Scale> scaleList = null;
        String objectType = req.getParameter("objectType");
        ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
        if (objectType.equals("1")) {// 学生
            String gradeStr = req.getParameter("gradeOrderId");
            String gradepart = req.getParameter("gradepart");
            int xdid = 0;
            if (gradeStr != null)
                xdid = AgeUitl.getXdByGrade(Integer.parseInt(gradeStr));
            if (gradepart != null)
                xdid = Integer.parseInt(gradepart);
            String st = "";
            if (xdid == 1)
                st = "小学";
            else if (xdid == 2)
                st = "初中";
            else if (xdid == 3)
                st = "高中";
            else if (xdid == 4)
                st = "成人";
            scaleFilterParam.setApplicablePerson(st);
            scaleFilterParam.setIsWarn("-1");
            scaleFilterParam.setScaleId("");
            scaleFilterParam.setScaleSourceId("-1");
            scaleFilterParam.setScaleTypeId("-1");
        }
        if (objectType.equals("2")) {// 教师
            scaleFilterParam.setApplicablePerson("成人");
            scaleFilterParam.setIsWarn("-1");
            scaleFilterParam.setScaleId("");
            scaleFilterParam.setScaleSourceId("-1");
            scaleFilterParam.setScaleTypeId("-1");
        }
        scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        JSONArray jArray3 = new JSONArray();
        for (Scale scale : scaleList) {
            String scaleid = scale.getCode();
            if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                continue;
            JSONObject jObj = new JSONObject();
            jObj.put("scaleId", scale.getCode());
            jObj.put("scaleShortName", scale.getShortname());
            jObj.put("scaleQNum", scale.getQuestionNum());
            jObj.put("scaleType", scale.getScaleType());
            jObj.put("scaleTitle", scale.getTitle());
            jObj.put("scaleShowTitle", scale.getShowtitle());
            jArray3.add(jObj);
        }

        JSONObject jScale = new JSONObject();
        jScale.put("scale", jArray3);
        jArray.add(jScale);
        return jArray.toString();
    }

    @RequestMapping(value = "/getScales1", method = RequestMethod.POST)
    @ResponseBody
    public String getScalesAction1(HttpServletRequest req) {
        List<ScaleInfo> scaleList = null;
        String objectType = req.getParameter("objectType");
        if (objectType.equals("1"))// 学生
        {
            String xdinfo = req.getParameter("gradeOrderId");
            String[] xdgrade = xdinfo.split("-");
            int xdid = Integer.valueOf(xdgrade[0]).intValue();
            scaleList = (List<ScaleInfo>) scaleInfoDao.queryScaleForStudent(xdid);
        }
        if (objectType.equals("2")) {// 教师
            scaleList = (List<ScaleInfo>) scaleInfoDao.queryScaleForTeacher();
        }
        scaleList = ScaleUtils.filterThreadAngleForNoStudent(scaleList);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        for (ScaleInfo scale : scaleList) {
            if (Integer.valueOf(scale.getId()) <= 100000) {
                mess.append("<inScales><scaleId>" + scale.getCode() + "</scaleId>");
                mess.append("<scaleTitle>" + scale.getTitle() + "</scaleTitle></inScales>");
            } else if (Integer.valueOf(scale.getId()) >= 500000) {
                mess.append("<selfScales><scaleId>" + scale.getId() + "</scaleId>");
                mess.append("<scaleTitle>" + scale.getTitle() + "</scaleTitle></selfScales>");
            } else {
                mess.append("<scales><scaleId>" + scale.getId() + "</scaleId>");
                mess.append("<scaleTitle>" + scale.getTitle() + "</scaleTitle></scales>");
            }
        }
        mess.append("</mess>");
        return mess.toString();
    }

    @RequestMapping(value = "/getQuxian", method = RequestMethod.POST)
    @ResponseBody
    public String getQuxianAction(HttpServletRequest req) {
        String areaid = req.getParameter("areaid");
        List<District> districtList = districtService.getCounties(areaid);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        for (District dist : districtList) {
            String code = dist.getCode();
            String name = dist.getName();
            mess.append("<areas><areaid>" + code + "</areaid>");
            mess.append("<areaname>" + name + "</areaname></areas>");
        }
        mess.append("</mess>");
        return mess.toString();
    }

    @RequestMapping(value = "/getQuxianStr", method = RequestMethod.POST)
    @ResponseBody
    public String getQuxianStr(HttpServletRequest req) {
        String areaid = req.getParameter("cityId");
        List<District> districtList = districtService.getCounties(areaid);
        Gson gson = new Gson();
        String result = gson.toJson(districtList);
        return result;
    }

    @RequestMapping(value = "/getCountyEduStr", method = RequestMethod.POST)
    @ResponseBody
    public String getCountyEduStr(HttpServletRequest req) {
        String areaid = req.getParameter("cityId");

        List<Organization> orgnizationList = organizationService.getEduOrganizationByCityId(areaid);
        Gson gson = new Gson();
        String result = gson.toJson(orgnizationList);
        return result;
    }

    @RequestMapping(value = "/getStudentExamdoQuxianStr", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentExamdoQuxianStr(HttpServletRequest req) {
        List<District> districtList = examdoService.selectStudentDistinctQuxian();
        Gson gson = new Gson();
        String result = gson.toJson(districtList);
        return result;
    }

    @RequestMapping(value = "/getTeacherExamdoQuxianStr", method = RequestMethod.POST)
    @ResponseBody
    public String getTeacherExamdoQuxianStr(HttpServletRequest req) {
        List<District> districtList = examdoService.selectTeacherDistinctQuxian();
        Gson gson = new Gson();
        String result = gson.toJson(districtList);
        return result;
    }

    /**
     * 根据机构id，获得所有学校
     * @param req
     * @return
     */
    @RequestMapping(value = "/getSchoolByOrg", method = RequestMethod.POST)
    @ResponseBody
    public String getSchoolByOrg(HttpServletRequest req) {
        String areaid = req.getParameter("areaid");
        List<Organization> orgList = organizationService.getSchoolOrgByCountyId(areaid);
        Gson gson = new Gson();
        String result = gson.toJson(orgList);
        return result;
    }

    @RequestMapping(value = "/getSchoolByTownId", method = RequestMethod.POST)
    @ResponseBody
    public String getSchoolByTownId(HttpServletRequest req) {
        String areaid = req.getParameter("areaid");
        List<Organization> orgList = organizationService.getSchoolOrgByTownId(areaid);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        for (Organization school : orgList) {
            Long id = school.getId();
            String name = school.getName();
            mess.append("<schools><schoolid>" + id + "</schoolid>");
            mess.append("<schoolname>" + name + "</schoolname></schools>");
        }
        mess.append("</mess>");
        return mess.toString();
    }

    @RequestMapping(value = "/getSchoolByTownIdStr", method = RequestMethod.POST)
    @ResponseBody
    public String getSchoolByTownIdStr(HttpServletRequest req) {
        String areaid = req.getParameter("areaid");
        List<Organization> orgList = organizationService.getSchoolOrgByTownId(areaid);
        Gson gson = new Gson();
        String result = gson.toJson(orgList);
        return result;
    }

    @RequestMapping(value = "/getTowns", method = RequestMethod.POST)
    @ResponseBody
    public String getTownsAction(HttpServletRequest req) {
        String areaid = req.getParameter("areaid");
        List<District> districtList = districtService.getTowns(areaid);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        for (District dist : districtList) {
            String code = dist.getCode();
            String name = dist.getName();
            mess.append("<areas><areaid>" + code + "</areaid>");
            mess.append("<areaname>" + name + "</areaname></areas>");
        }
        mess.append("</mess>");
        return mess.toString();
    }

    // 获得本区县的str
    @RequestMapping(value = "/getTownsStr", method = RequestMethod.POST)
    @ResponseBody
    public String getTownsStr(HttpServletRequest req) {
        String areaid = req.getParameter("cityId");
        List<District> districtList = districtService.getTowns(areaid);
        Gson gson = new Gson();
        String districtStr = gson.toJson(districtList);
        return districtStr;
    }

    @RequestMapping(value = "/getSonSchools", method = RequestMethod.POST)
    @ResponseBody
    public String getSonSchools(@CurrentOrg Organization orgEntity, HttpServletRequest req) {
        // Organization org = new Organization();
        // org.setId(orgEntity.getId());
        // org.setId((long)6);
        List<Organization> orgList = organizationService.getSonSchoolList(orgEntity);
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        for (Organization school : orgList) {
            Long id = school.getId();
            String name = school.getName();
            mess.append("<schools><schoolid>" + id + "</schoolid>");
            mess.append("<schoolname>" + name + "</schoolname></schools>");
        }
        mess.append("</mess>");
        return mess.toString();
    }

    @RequestMapping(value = "/getStudentExamdoSonSchools", method = RequestMethod.POST)
    @ResponseBody
    public String getStudentExamdoSonSchools(@CurrentOrg Organization orgEntity, HttpServletRequest req) {
        List<Organization> orgList = examdoService.selectStudentDistinctSonSchool();
        Gson gson = new Gson();
        String result = gson.toJson(orgList);
        return result;
    }

    @RequestMapping(value = "/getTeacherExamdoSonSchools", method = RequestMethod.POST)
    @ResponseBody
    public String getTeacherExamdoSonSchools(@CurrentOrg Organization orgEntity, HttpServletRequest req) {
        List<Organization> orgList = examdoService.selectTeacherDistinctSonSchool();
        Gson gson = new Gson();
        String result = gson.toJson(orgList);
        return result;
    }

    // 获得直属学校的str
    @RequestMapping(value = "/getSonSchoolsStr", method = RequestMethod.POST)
    @ResponseBody
    public String getSonSchoolsStr(@CurrentOrg Organization orgEntity, HttpServletRequest req) {
        // Organization org = new Organization();
        // //org.setId(orgEntity.getId());
        // org.setId((long)6);
        // orgEntity.setId((long)100);
        List<Organization> orgList = organizationService.getSonSchoolList(orgEntity);
        Gson gson = new Gson();
        String result = gson.toJson(orgList);
        return result;
    }

    // 首页直通车
    @RequestMapping(value = "/getDirectTrainForSchools", method = RequestMethod.POST)
    @ResponseBody
    public String getDirectTrainForSchools(@Param("code") String code, HttpServletRequest req) {
        List<Map> schoolList = schoolService.getDirectTrainForSchools(code);
        Gson gson = new Gson();
        String roleStr = gson.toJson(schoolList);
        return roleStr;

    }

    /**
     * 删除报告
     * @param req
     * @param resp
     * @param page
     */
    @RequestMapping(value = "/delExamResultAction", method = RequestMethod.GET)
    public void delExamResultAction(HttpServletRequest req, HttpServletResponse resp) {
        String resultid = req.getParameter("resultid");
        StringBuffer mess = new StringBuffer();
        mess.append("<mess>");
        try {
            // eMgr.deleteExamResultById(resultId, flag);
            // mess.append("<res>ok</res>");
            // mess.append("<info>删除报告成功！</info>");
            // mess.append("<resultId>"+resultId+"</resultId>");
        } catch (Exception e) {
            // mess.append("<info>删除报告失败！</info>");
        }
    }

    static private String getGradeName(int xueduan, String nj) {
        switch (xueduan) {
        case 1: {
            if (nj.equals("1"))
                return "小学一年级";
            if (nj.equals("2"))
                return "小学二年级";
            if (nj.equals("3"))
                return "小学三年级";
            if (nj.equals("4"))
                return "小学四年级";
            if (nj.equals("5"))
                return "小学五年级";
            if (nj.equals("6"))
                return "小学六年级";
        }
        case 2: {
            if (nj.equals("1"))
                return "初中一年级";
            if (nj.equals("2"))
                return "初中二年级";
            if (nj.equals("3"))
                return "初中三年级";
            if (nj.equals("4"))
                return "初中四年级";
        }
        case 3: {
            if (nj.equals("1"))
                return "高中一年级";
            if (nj.equals("2"))
                return "高中二年级";
            if (nj.equals("3"))
                return "高中三年级";
        }
        default:
            return "";
        }
    }
}
