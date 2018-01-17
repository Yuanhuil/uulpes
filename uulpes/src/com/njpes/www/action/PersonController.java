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
import com.njpes.www.datacenter.entity.StudentData;
import com.njpes.www.datacenter.entity.TeacherData;
import com.njpes.www.datacenter.service.StudentDataServiceI;
import com.njpes.www.datacenter.service.TeacherDataServiceI;

/**
 * 学生教师的数据接口
 * @Date 2017年4月14日
 */

@RequestMapping(value = "/person")
@Controller
public class PersonController {

    private String msg;
    @Autowired
    private StudentDataServiceI studentDataService;
    @Autowired
    private TeacherDataServiceI teacherDataService;

    // ================================== student ==============================

    /**
     * 添加学生
     * @param request
     * @return String
     * @Date 2017年4月15日 下午5:37:08
     */
    @RequestMapping(value = "student", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addStudent(HttpServletRequest request) {
        return null;
    };

    /**
     * 删除学生
     * @param request
     * @param id
     * @return String
     * @Date 2017年4月15日 下午5:37:39
     */
    @RequestMapping(value = "student/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delStudent(HttpServletRequest request, @PathVariable("id") String id) {
        return null;
    };

    /**
     * 更新学生
     * @param request
     * @return String
     * @Date 2017年4月15日 下午5:37:55
     */
    @RequestMapping(value = "student", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateStudent(HttpServletRequest request) {
        return null;
    };

    /**
     * 根据条件获取student
     * @param request
     * @param condition
     * @param value
     * @return String
     * @Date 2017年4月15日 下午5:38:14
     */
    @RequestMapping(value = "student/{condition}/{value}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getStudent(HttpServletRequest request, @PathVariable("condition") String condition,
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
        StudentData studentData = setStudentCondition(condition, value);// 设置查询条件
        if (studentData != null) {
            List<StudentData> student = studentDataService.getStudentByCondition(studentData);// 查询结果
            if (student != null && student.size() > 0) {
                result.put("code", "200");
                result.put("msg", "success");
                result.put("count", student.size());
                result.put("data", student);
                return JSON.toJSONString(result);
            } else {
                msg = "未查询到结果";
            }
        }
        result.put("code", "100");
        result.put("msg", msg);
        return JSON.toJSONString(result);
    };

    // ========================= teacher =============================

    /**
     * 删除教师
     * @param request
     * @return String
     * @Date 2017年5月27日 上午10:44:00
     */
    @RequestMapping(value = "teacher", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delTeacher(HttpServletRequest request) {
        return null;
    };

    /**
     * 添加教师
     * @param request
     * @return String
     * @Date 2017年5月27日 上午10:44:12
     */
    @RequestMapping(value = "teacher", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addTeacher(HttpServletRequest request) {
        return null;
    };

    /**
     * 更新教师
     * @param request
     * @return String
     * @Date 2017年5月27日 上午10:42:49
     */
    @RequestMapping(value = "teacher", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateTeacher(HttpServletRequest request) {
        return null;
    };

    /**
     * 根据条件获取老师
     * @param request
     * @param condition
     * @param value
     * @return String
     * @Date 2017年5月27日 上午10:45:32
     */
    @RequestMapping(value = "teacher/{condition}/{value}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTeacher(HttpServletRequest request, @PathVariable("condition") String condition,
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
        TeacherData teacherData = setTeacherCondition(condition, value);// 设置查询条件
        if (teacherData != null) {
            List<TeacherData> teacher = teacherDataService.getTeacherByCondition(teacherData);// 查询结果
            if (teacher != null && teacher.size() > 0) {
                result.put("code", "200");
                result.put("msg", "success");
                result.put("count", teacher.size());
                result.put("data", teacher);
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
     * 设置学生数据的查询条件
     * @param condition
     * @param value
     * @return StudentData
     * @Date 2017年4月17日 上午11:22:13
     */
    private StudentData setStudentCondition(String condition, String value) {
        StudentData studentData = new StudentData();
        if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
            switch (condition) {
            case "id":
                studentData.setId(value);
                break;
            case "mzm":
                studentData.setMzm(value);
                break;
            case "xh":
                studentData.setXh(value);
                break;
            case "xm":
                studentData.setXm(value);
                break;
            case "xbm":
                studentData.setXbm(value);
                break;
            case "lxdh":
                studentData.setLxdh(value);
                break;
            case "idCard":
                studentData.setSfzjh(value);
                break;
            case "classId":
                studentData.setClassId(value);
                break;
            case "className":
                studentData.setClassName(value);
                break;
            case "gradeId":
                studentData.setGradeId(value);
                break;
            case "gradeName":
                studentData.setGradeName(value);
                break;
            case "schoolId":
                studentData.setSchoolId(value);
                break;
            case "schoolName":
                studentData.setSchoolName(value);
                break;
            case "groupId":
                studentData.setGroupId(value);
                break;
            case "groupName":
                studentData.setGroupName(value);
                break;
            default:
                this.msg = "不支持的参数:{'" + condition + "'}";
                return null;
            }
            return studentData;
        }
        this.msg = "参数不能为空";
        return null;
    }

    /**
     * 设置教师数据的查询条件
     * @param condition
     * @param value
     * @return TeacherData
     * @Date 2017年5月27日 上午10:45:32
     */
    private TeacherData setTeacherCondition(String condition, String value) {
        TeacherData teacherData = new TeacherData();
        if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
            switch (condition) {
            case "id":
                teacherData.setId(value);
                break;
            case "mzm":
                teacherData.setMzm(value);
                break;
            case "gh":
                teacherData.setGh(value);
                break;
            case "xm":
                teacherData.setXm(value);
                break;
            case "xbm":
                teacherData.setXbm(value);
                break;
            case "lxdh":
                teacherData.setLxdh(value);
                break;
            case "idCard":
                teacherData.setSfzjh(value);
                break;
            case "job":
                teacherData.setJob(value);
                break;
            case "classId":
                teacherData.setClassId(value);
                break;
            case "className":
                teacherData.setClassName(value);
                break;
            case "gradeId":
                teacherData.setGradeId(value);
                break;
            case "gradeName":
                teacherData.setGradeName(value);
                break;
            case "schoolId":
                teacherData.setSchoolId(value);
                break;
            case "schoolName":
                teacherData.setSchoolName(value);
                break;
            case "groupId":
                teacherData.setGroupId(value);
                break;
            case "groupName":
                teacherData.setGroupName(value);
                break;
            default:
                this.msg = "不支持的参数:{'" + condition + "'}";
                return null;
            }
            return teacherData;
        }
        this.msg = "参数不能为空";
        return null;
    }

}
