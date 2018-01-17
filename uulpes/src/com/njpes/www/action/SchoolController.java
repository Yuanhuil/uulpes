package com.njpes.www.action;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.njpes.www.datacenter.entity.ClassData;
import com.njpes.www.datacenter.entity.SchoolData;
import com.njpes.www.datacenter.service.ClassDataServiceI;
import com.njpes.www.datacenter.service.SchoolDataServiceI;

/**
 * 学校 班级 年级的数据接口
 */
@RequestMapping(value = "/org")
@Controller
public class SchoolController {

    private String msg;
    @Autowired
    private SchoolDataServiceI schoolDataService;
    @Autowired
    private ClassDataServiceI classDataService;

    // ================================== school ==============================

    /**
     * 添加学校
     * @param request
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "school", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addSchool(HttpServletRequest request) {
        return null;
    };

    /**
     * 删除学校
     * @param request
     * @param id
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "school/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delSchool(HttpServletRequest request, @PathVariable("id") String id) {
        return null;
    };

    /**
     * 更新学校
     * @param request
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "school", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateSchool(HttpServletRequest request) {
        return null;
    };

    /**
     * 根据条件获取school
     * @param request
     * @param condition
     * @param value
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "school/{condition}/{value}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getSchool(HttpServletRequest request, @PathVariable("condition") String condition,
            @PathVariable("value") String value) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        try {
            value = new String(value.getBytes("iso8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result.put("code", "100");
            msg = "参数编码有误!";
            result.put("msg", msg);
            return JSON.toJSONString(result);
        }
        SchoolData schoolData = setSchoolCondition(condition, value);// 设置查询条件
        if (schoolData != null) {
            List<SchoolData> school = schoolDataService.getSchoolByCondition(schoolData);// 查询结果
            if (school != null && school.size() > 0) {
                result.put("code", "200");
                result.put("msg", "success");
                result.put("count", school.size());
                result.put("data", school);
                return JSON.toJSONString(result);
            } else {
                msg = "未查询到结果";
            }
        }
        result.put("code", "100");
        result.put("msg", msg);
        return JSON.toJSONString(result);
    };

    // ========================= class =============================

    /**
     * 删除班级
     * @param request
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "class", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delClass(HttpServletRequest request) {
        return null;
    };

    /**
     * 添加班级
     * @param request
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "class", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addClass(HttpServletRequest request) {
        return null;
    };

    /**
     * 更新班级
     * @param request
     * @return String
     * @Date 2017年5月31日 上午10:28:44
     */
    @RequestMapping(value = "class", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateClass(HttpServletRequest request) {
        return null;
    };

    /**
     * 根据条件获取班级
     * @param request
     * @param condition
     * @param value
     * @return String
     * @Date 2017年5月27日 上午10:45:32
     */
    @RequestMapping(value = "class/{condition}/{value}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getClass(HttpServletRequest request, @PathVariable("condition") String condition,
            @PathVariable("value") String value) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        try {
            value = new String(value.getBytes("iso8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result.put("code", "100");
            msg = "参数编码有误!";
            result.put("msg", msg);
            return JSON.toJSONString(result);
        }
        ClassData classData = setClassCondition(condition, value);// 设置查询条件
        if (classData != null) {
            List<ClassData> clazz = classDataService.getClassByCondition(classData);// 查询结果
            if (clazz != null && clazz.size() > 0) {
                result.put("code", "200");
                result.put("msg", "success");
                result.put("count", clazz.size());
                result.put("data", clazz);
                return JSON.toJSONString(result);
            } else {
                msg = "未查询到结果";
            }
        }
        result.put("code", "100");
        result.put("msg", msg);
        return JSON.toJSONString(result);
    };

    /**
     * 设置学校数据的查询条件
     * @param condition
     * @param value
     * @return SchoolData
     * @Date 2017年4月17日 上午11:22:13
     */
    private SchoolData setSchoolCondition(String condition, String value) {
        SchoolData schoolData = new SchoolData();
        if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
            switch (condition) {
            case "id":
                schoolData.setId(value);
                break;
            case "name":
                schoolData.setName(value);
                break;
            case "address":
                schoolData.setAddress(value);
                break;
            case "xxbxlxm":
                schoolData.setXxbxlxm(value);
                break;
            case "xxbxlxmName":
                schoolData.setXxbxlxmName(value);
                break;
            default:
                this.msg = "不支持的参数:{'" + condition + "'}";
                return null;
            }
            return schoolData;
        }
        this.msg = "参数不能为空";
        return null;
    }

    /**
     * 设置班级数据的查询条件
     * @param condition
     * @param value
     * @return ClassData
     * @Date 2017年5月27日 上午10:45:32
     */
    private ClassData setClassCondition(String condition, String value) {
        ClassData classData = new ClassData();
        if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
            switch (condition) {
            case "id":
                classData.setId(value);
                break;
            case "bh":
                classData.setBh(value);
                break;
            case "jbny":
                classData.setJbny(value);
                break;
            case "bzrName":
                classData.setBzrName(value);
                break;
            case "bjmc":
                classData.setBjmc(value);
                break;
            case "gradeId":
                classData.setGradeId(value);
                break;
            case "gradeName":
                classData.setGradeName(value);
                break;
            case "schoolId":
                classData.setSchoolId(value);
                break;
            case "schoolName":
                classData.setSchoolName(value);
                break;
            case "groupId":
                classData.setGroupId(value);
                break;
            case "groupName":
                classData.setGroupName(value);
                break;
            default:
                this.msg = "不支持的参数:{'" + condition + "'}";
                return null;
            }
            return classData;
        }
        this.msg = "参数不能为空";
        return null;
    }

}
