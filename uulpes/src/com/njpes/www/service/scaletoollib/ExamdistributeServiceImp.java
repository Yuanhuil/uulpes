package com.njpes.www.service.scaletoollib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.RoleMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.scaletoollib.ExamTaskMapper;
import com.njpes.www.dao.scaletoollib.ExamdoTaskEducommissionMapper;
import com.njpes.www.dao.scaletoollib.ExamdoTaskSchoolMapper;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.scaletoollib.ExamDoEduTask;
import com.njpes.www.entity.scaletoollib.ExamDoSchoolTask;
import com.njpes.www.entity.scaletoollib.ExamdoTaskSchoolForXiafa;
import com.njpes.www.invoker.ExamInvoker;
import com.njpes.www.utils.AgeUitl;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exam.ExamUtils;

@Service("ExamdistributeService")
public class ExamdistributeServiceImp implements ExamdistributeService {
    @Autowired
    ExamInvoker examInvoker;
    // @Autowired
    // private ExamTaskMapper examTaskDao;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private ExamTaskMapper examTaskDao;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;

    @Autowired
    private ExamdoTaskEducommissionMapper educommissionDispatcherMapper;
    @Autowired
    private ExamdoTaskSchoolMapper examdoTaskSchoolMapper;

    /*
     * 量表分发 Author:赵万锋
     */
    public void singledistribute(String taskname, String cityid, String countyid, long orgid, String flag,
            String starttime, String endtime, String objectType, String xd, String gradeid, String nj, String njmc,
            String bj, String bjmc, String[] scales, long createuserid, String[] studentIds, String[] teacherIds,
            String teacherRole, String rolename, Map resultMsgMap) throws Exception {
        starttime += " 00:00:00";
        endtime += " 23:59:59";

        // List<Map<String,Object>> examdoList = new
        // ArrayList<Map<String,Object>>();
        // List resultMsgList = new ArrayList();
        // resultMsgMap.put("resultMsgList", resultMsgList);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if ("1".equals(objectType)) {// 分发对象是学生
                // 这里插入分发任务表记录
                ExamDoSchoolTask examDoSchoolTask = new ExamDoSchoolTask();
                examDoSchoolTask.setTaskname(taskname);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                examDoSchoolTask.setCreatetime(df.format(new Date()));
                examDoSchoolTask.setStarttime(starttime);
                examDoSchoolTask.setEndtime(endtime);
                examDoSchoolTask.setOrgid(orgid);
                examDoSchoolTask.setXd(xd);
                examDoSchoolTask.setGradeids(gradeid);
                examDoSchoolTask.setNj(nj);
                examDoSchoolTask.setNjmc(njmc);
                examDoSchoolTask.setBj(bj);
                examDoSchoolTask.setBjmc(bjmc);
                examDoSchoolTask.setCityid(cityid);
                examDoSchoolTask.setCountyid(countyid);
                examDoSchoolTask.setCreateuserid(createuserid);
                // examDoSchoolTask.setTownid(townid);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < scales.length; i++) {
                    sb.append(scales[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setScaleids(sb.toString());
                sb = new StringBuffer();
                for (int i = 0; i < studentIds.length; i++) {
                    sb.append(studentIds[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setStudentids(sb.toString());
                examInvoker.insertExamTaskToSchool(examDoSchoolTask);
                long taskid = examDoSchoolTask.getId();
                // for(int i=0;i<studentIds.length;i++){
                Map<String, Object> paramets = new HashMap<String, Object>();
                paramets.put("taskid", taskid);
                paramets.put("taskfrom", 0);// 任务来源，0:本学校老师创建 1:上级教委创建
                paramets.put("orgid", orgid);
                paramets.put("countyid", countyid);
                paramets.put("cityid", cityid);
                paramets.put("starttime", starttime);
                // paramets.put("endtime", endtime);
                // 要分发的量表
                paramets.put(Constants.SCALEIDS_PROP, scales);
                // 分发对象学生
                paramets.put(Constants.FLAG_PROP, objectType);
                // paramets.put("xm", student.getXm());
                // paramets.put("xmpy", student.getXmpy());
                // paramets.put("csrq", student.getCsrq());
                // paramets.put("xh", student.getXh());
                paramets.put("xd", xd);
                paramets.put("gradeid", gradeid);
                // 分发对象学生的年级
                paramets.put("nj", nj);
                paramets.put("njmc", njmc);
                // 分发对象学生的班级
                paramets.put("bj", bj);
                paramets.put("bjmc", bjmc);
                // 班号
                // paramets.put("bh", bh);

                // paramets.put(Constants.USER_ID_PROP, studentIds[i]);
                paramets.put(Constants.USER_ID_PROP, studentIds);
                String examDotabl = ExamUtils.getExamDoTable(1);
                paramets.put(Constants.TABLE_PROP, examDotabl);
                // 开始时间、结束时间和限期
                paramets.put(Constants.LOTIME_PROP, starttime);
                paramets.put(Constants.HITIME_PROP, endtime);
                paramets.put(Constants.LIMIT_FLAG_PROP, flag);
                paramets.put("result", resultMsgMap);
                examInvoker.toSchoolUsersForSingle(paramets);
                // }
            } else if ("2".equals(objectType)) {// 分发对象是教师
                // System.out.println("=============================================教师开始");
                // 这里插入分发任务表记录
                ExamDoSchoolTask examDoSchoolTask = new ExamDoSchoolTask();
                examDoSchoolTask.setTaskname(taskname);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                examDoSchoolTask.setCreatetime(df.format(new Date()));
                examDoSchoolTask.setStarttime(starttime);
                examDoSchoolTask.setEndtime(endtime);
                examDoSchoolTask.setOrgid(orgid);
                examDoSchoolTask.setCityid(cityid);
                examDoSchoolTask.setCountyid(countyid);
                // int roleid = Integer.parseInt(teacherRole);
                examDoSchoolTask.setCreateuserid(createuserid);
                examDoSchoolTask.setRoleids(teacherRole);
                examDoSchoolTask.setRolename(rolename);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < scales.length; i++) {
                    sb.append(scales[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setScaleids(sb.toString());
                sb = new StringBuffer();
                for (int i = 0; i < teacherIds.length; i++) {
                    sb.append(teacherIds[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setTeacherids(sb.toString());
                examInvoker.insertExamTaskToSchool(examDoSchoolTask);
                long taskid = examDoSchoolTask.getId();
                // for(int i=0;i<teacherIds.length;i++){
                Map<String, Object> paramets = new HashMap<String, Object>();
                paramets.put("taskid", taskid);
                paramets.put("taskfrom", 0);// 任务来源，0:本学校老师创建 1:上级教委创建
                paramets.put("orgid", orgid);
                paramets.put("countyid", countyid);
                paramets.put("cityid", cityid);
                paramets.put("starttime", starttime);
                // 要分发的量表
                paramets.put(Constants.SCALEIDS_PROP, scales);

                paramets.put("roleid", teacherRole);
                paramets.put("rolename", rolename);
                // paramets.put(Constants.USER_ID_PROP, teacherIds[i]);
                paramets.put(Constants.USER_ID_PROP, teacherIds);
                // 分发对象教师
                paramets.put(Constants.FLAG_PROP, objectType);
                String examDotabl = ExamUtils.getExamDoTable(2);
                paramets.put(Constants.TABLE_PROP, examDotabl);
                // 开始时间、结束时间和限期
                paramets.put(Constants.LOTIME_PROP, starttime);
                paramets.put(Constants.HITIME_PROP, endtime);
                paramets.put(Constants.LIMIT_FLAG_PROP, flag);
                // paramets.put(Constants.USER_ID_PROP,teacherIds[i]);
                paramets.put("result", resultMsgMap);
                examInvoker.toSchoolUsersForSingle(paramets);
                // }
            }

            boolean hasDispense = false;
            Map statisMap = (Map) resultMsgMap.get("statisMap");
            Map<String, String> scaleMap = new HashMap<String, String>();
            resultMsgMap.put("scaleMap", scaleMap);
            for (int i = 0; i < scales.length; i++) {
                String s = scales[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                String scalename = cachedScaleMgr.get(scaleid).getTitle();
                scaleMap.put(scaleid, scalename);
                int count = 0;
                if (statisMap == null)
                    count = 0;
                else {
                    String countStr = statisMap.get(scaleid).toString();
                    count = Integer.parseInt(countStr);
                }
                if (count > 0) {// 有分发记录
                    hasDispense = true;
                    // break;
                }
            }
            if (hasDispense)
                txManager.commit(status);
            else
                txManager.rollback(status);
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
            // System.out.println("=============================================错误");
        } finally {
            // txManager.rollback(status);
        }
    }

    /*
     * public void singledistribute1(String taskname, String cityid, String
     * countyid, long orgid, String flag, String starttime, String endtime,
     * String objectType, String xd, String gradeid, String nj, String njmc,
     * String bj, String bjmc, String[] scales, long createuserid, String[]
     * studentIds, String[] teacherIds, String teacherRole, String rolename, Map
     * resultMsgMap) throws Exception { starttime += " 00:00:00"; endtime +=
     * " 23:59:59";
     * 
     * // List<Map<String,Object>> examdoList = new //
     * ArrayList<Map<String,Object>>(); // List resultMsgList = new ArrayList();
     * // resultMsgMap.put("resultMsgList", resultMsgList);
     * TransactionDefinition def = new DefaultTransactionDefinition();
     * TransactionStatus status = txManager.getTransaction(def); try { if
     * ("1".equals(objectType)) {// 分发对象是学生 // 这里插入分发任务表记录 ExamDoSchoolTask
     * examDoSchoolTask = new ExamDoSchoolTask();
     * examDoSchoolTask.setTaskname(taskname); SimpleDateFormat df = new
     * SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");// 设置日期格式
     * examDoSchoolTask.setCreatetime(df.format(new Date()));
     * examDoSchoolTask.setStarttime(starttime);
     * examDoSchoolTask.setEndtime(endtime); examDoSchoolTask.setOrgid(orgid);
     * examDoSchoolTask.setXd(xd); examDoSchoolTask.setGradeids(gradeid);
     * examDoSchoolTask.setNj(nj); examDoSchoolTask.setNjmc(njmc);
     * examDoSchoolTask.setBj(bj); examDoSchoolTask.setBjmc(bjmc);
     * examDoSchoolTask.setCityid(cityid);
     * examDoSchoolTask.setCountyid(countyid);
     * examDoSchoolTask.setCreateuserid(createuserid); //
     * examDoSchoolTask.setTownid(townid); StringBuffer sb = new StringBuffer();
     * for (int i = 0; i < scales.length; i++) { sb.append(scales[i]);
     * sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setScaleids(sb.toString()); sb = new StringBuffer(); for
     * (int i = 0; i < studentIds.length; i++) { sb.append(studentIds[i]);
     * sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setStudentids(sb.toString());
     * examInvoker.insertExamTaskToSchool(examDoSchoolTask); long taskid =
     * examDoSchoolTask.getId(); for (int i = 0; i < studentIds.length; i++) {
     * Map<String, Object> paramets = new HashMap<String, Object>();
     * paramets.put("taskid", taskid); paramets.put("taskfrom", 0);//
     * 任务来源，0:本学校老师创建 1:上级教委创建 paramets.put("orgid", orgid);
     * paramets.put("countyid", countyid); paramets.put("cityid", cityid);
     * paramets.put("starttime", starttime); // paramets.put("endtime",
     * endtime); // 要分发的量表 paramets.put(Constants.SCALEIDS_PROP, scales); //
     * 分发对象学生 paramets.put(Constants.FLAG_PROP, objectType); //
     * paramets.put("xm", student.getXm()); // paramets.put("xmpy",
     * student.getXmpy()); // paramets.put("csrq", student.getCsrq()); //
     * paramets.put("xh", student.getXh()); paramets.put("xd", xd);
     * paramets.put("gradeid", gradeid); // 分发对象学生的年级 paramets.put("nj", nj);
     * paramets.put("njmc", njmc); // 分发对象学生的班级 paramets.put("bj", bj);
     * paramets.put("bjmc", bjmc); // 班号 // paramets.put("bh", bh);
     * 
     * paramets.put(Constants.USER_ID_PROP, studentIds[i]); String examDotabl =
     * ExamUtils.getExamDoTable(1); paramets.put(Constants.TABLE_PROP,
     * examDotabl); // 开始时间、结束时间和限期 paramets.put(Constants.LOTIME_PROP,
     * starttime); paramets.put(Constants.HITIME_PROP, endtime);
     * paramets.put(Constants.LIMIT_FLAG_PROP, flag); paramets.put("result",
     * resultMsgMap); examInvoker.toSchoolUsers(paramets); } } else if
     * ("2".equals(objectType)) {// 分发对象是教师 //
     * System.out.println("=============================================教师开始");
     * // 这里插入分发任务表记录 ExamDoSchoolTask examDoSchoolTask = new
     * ExamDoSchoolTask(); examDoSchoolTask.setTaskname(taskname);
     * SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");//
     * 设置日期格式 examDoSchoolTask.setCreatetime(df.format(new Date()));
     * examDoSchoolTask.setStarttime(starttime);
     * examDoSchoolTask.setEndtime(endtime); examDoSchoolTask.setOrgid(orgid);
     * examDoSchoolTask.setCityid(cityid);
     * examDoSchoolTask.setCountyid(countyid); // int roleid =
     * Integer.parseInt(teacherRole);
     * examDoSchoolTask.setCreateuserid(createuserid);
     * examDoSchoolTask.setRoleids(teacherRole);
     * examDoSchoolTask.setRolename(rolename); StringBuffer sb = new
     * StringBuffer(); for (int i = 0; i < scales.length; i++) {
     * sb.append(scales[i]); sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setScaleids(sb.toString()); sb = new StringBuffer(); for
     * (int i = 0; i < teacherIds.length; i++) { sb.append(teacherIds[i]);
     * sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setTeacherids(sb.toString());
     * examInvoker.insertExamTaskToSchool(examDoSchoolTask); long taskid =
     * examDoSchoolTask.getId(); for (int i = 0; i < teacherIds.length; i++) {
     * Map<String, Object> paramets = new HashMap<String, Object>();
     * paramets.put("taskid", taskid); paramets.put("taskfrom", 0);//
     * 任务来源，0:本学校老师创建 1:上级教委创建 paramets.put("orgid", orgid);
     * paramets.put("countyid", countyid); paramets.put("cityid", cityid);
     * paramets.put("starttime", starttime); // 要分发的量表
     * paramets.put(Constants.SCALEIDS_PROP, scales);
     * 
     * paramets.put("roleid", teacherRole); paramets.put("rolename", rolename);
     * paramets.put(Constants.USER_ID_PROP, teacherIds[i]); // 分发对象教师
     * paramets.put(Constants.FLAG_PROP, objectType); String examDotabl =
     * ExamUtils.getExamDoTable(2); paramets.put(Constants.TABLE_PROP,
     * examDotabl); // 开始时间、结束时间和限期 paramets.put(Constants.LOTIME_PROP,
     * starttime); paramets.put(Constants.HITIME_PROP, endtime);
     * paramets.put(Constants.LIMIT_FLAG_PROP, flag);
     * paramets.put(Constants.USER_ID_PROP, teacherIds[i]);
     * paramets.put("result", resultMsgMap);
     * examInvoker.toSchoolUsers(paramets); } }
     * 
     * boolean hasDispense = false; Map statisMap = (Map)
     * resultMsgMap.get("statisMap"); Map<String, String> scaleMap = new
     * HashMap<String, String>(); resultMsgMap.put("scaleMap", scaleMap); for
     * (int i = 0; i < scales.length; i++) { String s = scales[i]; String[]
     * scaleinfo = s.split("_"); String scaleid = scaleinfo[0]; String scalename
     * = cachedScaleMgr.get(scaleid).getTitle(); scaleMap.put(scaleid,
     * scalename); int count = 0; if (statisMap == null) count = 0; else {
     * String countStr = statisMap.get(scaleid).toString(); count =
     * Integer.parseInt(countStr); } if (count > 0) {// 有分发记录 hasDispense =
     * true; // break; } } if (hasDispense) txManager.commit(status); else
     * txManager.rollback(status); } catch (Exception e) {
     * txManager.rollback(status); e.printStackTrace(); //
     * System.out.println("=============================================错误"); }
     * finally { // txManager.rollback(status); } }
     */
    public void groupdistribute(String taskname, String countyid, String cityid, long orgid, // 学校代码
            String xd, String[] gradeClassids, // 班级

            String flag, //
            String starttime, String endtime, String objectType, // 对象类型：老师，学生
            String[] scales, // 量表id
            long createuerid, String[] teacherRole, Map resultMsgMap) {

        starttime += " 00:00:00";
        endtime += " 23:59:59";
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if ("1".equals(objectType)) {// 分发对象是学生
                // 这里插入分发任务表记录
                ExamDoSchoolTask examDoSchoolTask = new ExamDoSchoolTask();
                examDoSchoolTask.setTaskname(taskname);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                examDoSchoolTask.setCreatetime(df.format(new Date()));
                examDoSchoolTask.setStarttime(starttime);
                examDoSchoolTask.setEndtime(endtime);
                examDoSchoolTask.setOrgid(orgid);
                examDoSchoolTask.setCityid(cityid);
                examDoSchoolTask.setCountyid(countyid);
                examDoSchoolTask.setXd(xd);
                examDoSchoolTask.setCreateuserid(createuerid);
                // examDoSchoolTask.setNj(nj);
                examDoSchoolTask.setOrgid(orgid);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < scales.length; i++) {
                    sb.append(scales[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setScaleids(sb.toString());

                StringBuffer sbnj = new StringBuffer();
                StringBuffer sbnjmc = new StringBuffer();
                StringBuffer sbbj = new StringBuffer();
                StringBuffer sbgradeid = new StringBuffer();
                StringBuffer sbbjmc = new StringBuffer();
                String gradeid = "";
                for (int i = 0; i < gradeClassids.length; i++) {
                    String gcs = gradeClassids[i];

                    String[] xd_bj = gcs.split(",");
                    String grade = xd_bj[0];
                    if (!gradeid.equals(grade)) {
                        sbgradeid.append(grade);
                        sbgradeid.append(",");
                        int nj = AgeUitl.getNj(Integer.parseInt(grade));
                        String njmc = AgeUitl.getNjName(Integer.parseInt(grade));
                        sbnj.append(String.valueOf(nj));
                        sbnj.append(",");
                        sbnjmc.append(njmc);
                        sbnjmc.append(",");
                    }
                    gradeid = grade;

                    String bj = xd_bj[1];
                    String[] bjinfo = bj.split(":");
                    String bjid = bjinfo[0];
                    // String bjmc =
                    // AgeUitl.getNjName(Integer.parseInt(gradeid))+bjinfo[1];
                    String bjmc = bjinfo[1];
                    sbbjmc.append(bjmc);
                    sbbjmc.append(",");
                    // sbnj. append(gradeid);
                    sbbj.append(bjid);
                    // sbnj.append(",");
                    sbbj.append(",");
                }
                sbnj.deleteCharAt(sbnj.length() - 1);
                sbnjmc.deleteCharAt(sbnjmc.length() - 1);
                sbbj.deleteCharAt(sbbj.length() - 1);
                sbbjmc.deleteCharAt(sbbjmc.length() - 1);
                sbgradeid.deleteCharAt(sbgradeid.length() - 1);
                examDoSchoolTask.setGradeids(sbgradeid.toString());
                examDoSchoolTask.setNj(sbnj.toString());
                examDoSchoolTask.setNjmc(sbnjmc.toString());
                examDoSchoolTask.setBj(sbbj.toString());
                examDoSchoolTask.setBjmc(sbbjmc.toString());
                examInvoker.insertExamTaskToSchool(examDoSchoolTask);
                long taskid = examDoSchoolTask.getId();
                // for(int i=0;i<gradeClassids.length;i++){
                // System.out.println("=============================================学生开始"+studentId[i]);
                Map<String, Object> paramets = new HashMap<String, Object>();
                paramets.put("taskid", taskid);
                paramets.put("taskfrom", 0);// 任务来源，0:本学校老师创建 1:上级教委创建
                paramets.put("orgid", orgid);
                paramets.put("countyid", countyid);
                paramets.put("cityid", cityid);
                // 要分发的量表
                paramets.put(Constants.SCALEIDS_PROP, scales);
                // 分发对象学生
                paramets.put(Constants.FLAG_PROP, objectType);
                paramets.put("gradeClassids", gradeClassids);
                /*
                 * String gcs = gradeClassids[i]; String[] xd_bj =
                 * gcs.split(","); gradeid = xd_bj[0]; String njmc =
                 * AgeUitl.getGradeName(Integer.parseInt(gradeid)); int nj =
                 * AgeUitl.getNj(Integer.parseInt(gradeid)); njmc =
                 * AgeUitl.getNjName(Integer.parseInt(gradeid)); String bj =
                 * xd_bj[1]; String[] bjinfo = bj.split(":"); String bjid =
                 * bjinfo[0]; String bjmc = bjinfo[1]; paramets.put("xd", xd);
                 * //分发对象学生的年级 paramets.put("gradeid", gradeid);
                 * paramets.put("nj", nj); paramets.put("njmc", njmc);
                 * //分发对象学生的班级 paramets.put("bj", bjid); paramets.put("bjmc",
                 * bjmc);
                 */
                // 开始时间、结束时间和限期
                paramets.put("starttime", starttime);
                paramets.put(Constants.LOTIME_PROP, starttime);
                paramets.put(Constants.HITIME_PROP, endtime);
                paramets.put(Constants.LIMIT_FLAG_PROP, flag);
                // paramets.put("result", resultMsgList);
                paramets.put("result", resultMsgMap);
                examInvoker.toSchoolUsersForGroup(paramets);
                // }
            } else if ("2".equals(objectType)) {// 分发对象是教师
                // System.out.println("=============================================教师开始");
                // 这里插入分发任务表记录
                ExamDoSchoolTask examDoSchoolTask = new ExamDoSchoolTask();
                examDoSchoolTask.setTaskname(taskname);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                examDoSchoolTask.setCreatetime(df.format(new Date()));
                examDoSchoolTask.setStarttime(starttime);
                examDoSchoolTask.setEndtime(endtime);
                examDoSchoolTask.setOrgid(orgid);
                examDoSchoolTask.setCreateuserid(createuerid);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < scales.length; i++) {
                    sb.append(scales[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setScaleids(sb.toString());
                // 角色id号
                sb.setLength(0);
                for (int i = 0; i < teacherRole.length; i++) {
                    sb.append(teacherRole[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setRoleids(sb.toString());
                // 角色名称
                String[] rolenames = new String[teacherRole.length];
                sb.setLength(0);
                for (int i = 0; i < teacherRole.length; i++) {
                    long roleid = Integer.parseInt(teacherRole[i]);
                    Role r = roleMapper.selectByPrimaryKey(roleid);
                    if (r == null)
                        continue;
                    String rolename = r.getRoleName();
                    rolenames[i] = rolename;
                    sb.append(rolename);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                examDoSchoolTask.setRolename(sb.toString());

                examInvoker.insertExamTaskToSchool(examDoSchoolTask);
                long taskid = examDoSchoolTask.getId();

                // for(int i=0;i<teacherRole.length;i++){
                Map<String, Object> paramets = new HashMap<String, Object>();
                paramets.put("taskid", taskid);
                paramets.put("taskfrom", 0);// 任务来源，0:本学校老师创建 1:上级教委创建
                // paramets.put(Constants.ORG_ID_PROP, orgid);
                paramets.put("orgid", orgid);
                paramets.put("countyid", countyid);
                paramets.put("cityid", cityid);
                // 要分发的量表
                paramets.put(Constants.SCALEIDS_PROP, scales);

                paramets.put(Constants.FLAG_PROP, objectType);
                // 开始时间、结束时间和限期
                paramets.put("starttime", starttime);
                paramets.put(Constants.LOTIME_PROP, starttime);
                paramets.put(Constants.HITIME_PROP, endtime);
                paramets.put(Constants.LIMIT_FLAG_PROP, flag);
                // int roleid = Integer.parseInt(teacherRole[i]);
                // paramets.put("roleid",roleid);
                // paramets.put("rolename", rolenames[i]);
                paramets.put("roleids", teacherRole);
                paramets.put("result", resultMsgMap);
                examInvoker.toSchoolUsersForGroup(paramets);
                // txManager.commit(status);
                // }
                // System.out.println("=============================================教师结束");
            }
            boolean hasDispense = false;
            Map statisMap = (Map) resultMsgMap.get("statisMap");
            Map<String, String> scaleMap = new HashMap<String, String>();
            resultMsgMap.put("scaleMap", scaleMap);
            for (int i = 0; i < scales.length; i++) {
                String s = scales[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                String scalename = cachedScaleMgr.get(scaleid).getTitle();
                scaleMap.put(scaleid, scalename);
                int count = 0;
                if (statisMap == null)
                    count = 0;
                else {
                    String countStr = statisMap.get(scaleid).toString();
                    count = Integer.parseInt(countStr);
                }
                if (count > 0) {// 有分发记录
                    hasDispense = true;
                    // break;
                }
            }
            if (hasDispense)
                txManager.commit(status);
            else
                txManager.rollback(status);
            // txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            // System.out.println("=============================================错误");
        } finally {
            // txManager.rollback(status);
        }

    }

    /*
     * public void groupdistribute( String taskname, String countyid, String
     * cityid, long orgid,//学校代码 String xd, String[] gradeClassids,//班级
     * 
     * String flag,// String starttime, String endtime, String
     * objectType,//对象类型：老师，学生 String[] scales,//量表id long createuerid, String[]
     * teacherRole, Map resultMsgMap){
     * 
     * starttime += " 00:00:00"; endtime += " 23:59:59"; TransactionDefinition
     * def = new DefaultTransactionDefinition(); TransactionStatus status =
     * txManager.getTransaction(def); try{ if("1".equals(objectType)){//分发对象是学生
     * //这里插入分发任务表记录 ExamDoSchoolTask examDoSchoolTask = new ExamDoSchoolTask();
     * examDoSchoolTask.setTaskname(taskname); SimpleDateFormat df = new
     * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
     * examDoSchoolTask.setCreatetime(df.format(new Date()));
     * examDoSchoolTask.setStarttime(starttime);
     * examDoSchoolTask.setEndtime(endtime); examDoSchoolTask.setOrgid(orgid);
     * examDoSchoolTask.setCityid(cityid);
     * examDoSchoolTask.setCountyid(countyid); examDoSchoolTask.setXd(xd);
     * examDoSchoolTask.setCreateuserid(createuerid);
     * //examDoSchoolTask.setNj(nj); examDoSchoolTask.setOrgid(orgid);
     * StringBuffer sb = new StringBuffer(); for(int i=0;i<scales.length;i++){
     * sb. append(scales[i]); sb.append(","); } sb.deleteCharAt(sb.length() -
     * 1); examDoSchoolTask.setScaleids(sb.toString());
     * 
     * 
     * StringBuffer sbnj = new StringBuffer(); StringBuffer sbnjmc = new
     * StringBuffer(); StringBuffer sbbj = new StringBuffer(); StringBuffer
     * sbgradeid = new StringBuffer(); StringBuffer sbbjmc = new StringBuffer();
     * String gradeid=""; for(int i=0;i<gradeClassids.length;i++){ String gcs =
     * gradeClassids[i];
     * 
     * String[] xd_bj = gcs.split(","); String grade = xd_bj[0];
     * if(!gradeid.equals(grade)){ sbgradeid.append(grade);
     * sbgradeid.append(","); int nj = AgeUitl.getNj(Integer.parseInt(grade));
     * String njmc = AgeUitl.getNjName(Integer.parseInt(grade));
     * sbnj.append(String.valueOf(nj)); sbnj.append(","); sbnjmc.append(njmc);
     * sbnjmc.append(","); } gradeid = grade;
     * 
     * String bj = xd_bj[1]; String[] bjinfo = bj.split(":"); String bjid =
     * bjinfo[0]; //String bjmc =
     * AgeUitl.getNjName(Integer.parseInt(gradeid))+bjinfo[1]; String bjmc =
     * bjinfo[1]; sbbjmc.append(bjmc); sbbjmc.append(","); //sbnj.
     * append(gradeid); sbbj. append(bjid); //sbnj.append(",");
     * sbbj.append(","); } sbnj.deleteCharAt(sbnj.length() - 1);
     * sbnjmc.deleteCharAt(sbnjmc.length() - 1); sbbj.deleteCharAt(sbbj.length()
     * - 1); sbbjmc.deleteCharAt(sbbjmc.length() - 1);
     * sbgradeid.deleteCharAt(sbgradeid.length() - 1);
     * examDoSchoolTask.setGradeids(sbgradeid.toString());
     * examDoSchoolTask.setNj(sbnj.toString());
     * examDoSchoolTask.setNjmc(sbnjmc.toString());
     * examDoSchoolTask.setBj(sbbj.toString());
     * examDoSchoolTask.setBjmc(sbbjmc.toString());
     * examInvoker.insertExamTaskToSchool(examDoSchoolTask); long taskid =
     * examDoSchoolTask.getId(); for(int i=0;i<gradeClassids.length;i++){
     * //System.out.println("=============================================学生开始"+
     * studentId[i]); //String orderId[]=studentId[i].split("_"); Map<String,
     * Object> paramets = new HashMap<String, Object>(); paramets.put("taskid",
     * taskid); paramets.put("taskfrom", 0);//任务来源，0:本学校老师创建 1:上级教委创建
     * paramets.put("orgid", orgid); paramets.put("countyid", countyid);
     * paramets.put("cityid", cityid); //要分发的量表
     * paramets.put(Constants.SCALEIDS_PROP, scales); //分发对象学生
     * paramets.put(Constants.FLAG_PROP, objectType);
     * 
     * String gcs = gradeClassids[i]; String[] xd_bj = gcs.split(","); gradeid =
     * xd_bj[0]; String njmc = AgeUitl.getGradeName(Integer.parseInt(gradeid));
     * int nj = AgeUitl.getNj(Integer.parseInt(gradeid)); njmc =
     * AgeUitl.getNjName(Integer.parseInt(gradeid)); String bj = xd_bj[1];
     * String[] bjinfo = bj.split(":"); String bjid = bjinfo[0]; String bjmc =
     * bjinfo[1]; paramets.put("xd", xd); //分发对象学生的年级 paramets.put("gradeid",
     * gradeid); paramets.put("nj", nj); paramets.put("njmc", njmc); //分发对象学生的班级
     * paramets.put("bj", bjid); paramets.put("bjmc", bjmc); //班号
     * //paramets.put("bh", bh[i]);
     * 
     * //开始时间、结束时间和限期 paramets.put("starttime", starttime);
     * paramets.put(Constants.LOTIME_PROP, starttime);
     * paramets.put(Constants.HITIME_PROP, endtime);
     * paramets.put(Constants.LIMIT_FLAG_PROP, flag); //paramets.put("result",
     * resultMsgList); paramets.put("result", resultMsgMap);
     * examInvoker.toSchoolUsers(paramets); } }else
     * if("2".equals(objectType)){//分发对象是教师
     * //System.out.println("=============================================教师开始")
     * ; //这里插入分发任务表记录 ExamDoSchoolTask examDoSchoolTask = new
     * ExamDoSchoolTask(); examDoSchoolTask.setTaskname(taskname);
     * SimpleDateFormat df = new
     * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
     * examDoSchoolTask.setCreatetime(df.format(new Date()));
     * examDoSchoolTask.setStarttime(starttime);
     * examDoSchoolTask.setEndtime(endtime); examDoSchoolTask.setOrgid(orgid);
     * examDoSchoolTask.setCreateuserid(createuerid); StringBuffer sb = new
     * StringBuffer(); for(int i=0;i<scales.length;i++){ sb. append(scales[i]);
     * sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setScaleids(sb.toString()); //角色id号 sb.setLength(0);
     * for(int i=0;i<teacherRole.length;i++){ sb. append(teacherRole[i]);
     * sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setRoleids(sb.toString()); //角色名称 String[] rolenames =
     * new String[teacherRole.length]; sb.setLength(0); for(int
     * i=0;i<teacherRole.length;i++){ long roleid =
     * Integer.parseInt(teacherRole[i]); Role r =
     * roleMapper.selectByPrimaryKey(roleid); if(r == null)continue; String
     * rolename = r.getRoleName(); rolenames[i] = rolename; sb.
     * append(rolename); sb.append(","); } sb.deleteCharAt(sb.length() - 1);
     * examDoSchoolTask.setRolename(sb.toString());
     * 
     * examInvoker.insertExamTaskToSchool(examDoSchoolTask); long taskid =
     * examDoSchoolTask.getId();
     * 
     * for(int i=0;i<teacherRole.length;i++){ Map<String, Object> paramets = new
     * HashMap<String, Object>(); paramets.put("taskid", taskid);
     * paramets.put("taskfrom", 0);//任务来源，0:本学校老师创建 1:上级教委创建
     * //paramets.put(Constants.ORG_ID_PROP, orgid); paramets.put("orgid",
     * orgid); paramets.put("countyid", countyid); paramets.put("cityid",
     * cityid); //要分发的量表 paramets.put(Constants.SCALEIDS_PROP, scales);
     * 
     * paramets.put(Constants.FLAG_PROP, objectType); //开始时间、结束时间和限期
     * paramets.put("starttime", starttime); paramets.put(Constants.LOTIME_PROP,
     * starttime); paramets.put(Constants.HITIME_PROP, endtime);
     * paramets.put(Constants.LIMIT_FLAG_PROP, flag); int roleid =
     * Integer.parseInt(teacherRole[i]); paramets.put("roleid",roleid);
     * paramets.put("rolename", rolenames[i]); //paramets.put("result",
     * resultMsgList); paramets.put("result", resultMsgMap);
     * examInvoker.toSchoolUsers(paramets); //txManager.commit(status); }
     * //System.out.println("=============================================教师结束")
     * ; } boolean hasDispense = false; Map statisMap =
     * (Map)resultMsgMap.get("statisMap"); Map<String,String> scaleMap = new
     * HashMap<String,String>(); resultMsgMap.put("scaleMap", scaleMap); for
     * (int i=0;i<scales.length;i++) { String s = scales[i]; String[] scaleinfo
     * = s.split("_"); String scaleid = scaleinfo[0]; String scalename =
     * cachedScaleMgr.get(scaleid).getTitle(); scaleMap.put(scaleid, scalename);
     * int count =0; if(statisMap==null)count=0; else{ String countStr =
     * statisMap.get(scaleid).toString(); count = Integer.parseInt(countStr); }
     * if(count>0){//有分发记录 hasDispense = true; //break; } } if(hasDispense)
     * txManager.commit(status); else txManager.rollback(status);
     * //txManager.commit(status); }catch (Exception e) { e.printStackTrace();
     * txManager.rollback(status);
     * //System.out.println("=============================================错误");
     * }finally { //txManager.rollback(status); }
     * 
     * }
     */
    public int xiafaToEdu(long orgid, int taskid) {
        return educommissionDispatcherMapper.xiafaToEdu(orgid, taskid);
    }

    // 下发量表分发任务接口
    public void xiafaToSchool(long orgid, int taskid, String objectType, // 对象类型：老师，学生
            Map resultMsgMap) {
        ExamdoTaskSchoolForXiafa examdoTaskEdu = examdoTaskSchoolMapper.getXiafaExamdoTask(orgid, taskid);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String starttime = df.format(examdoTaskEdu.getStarttime());
        String endtime = df.format(examdoTaskEdu.getEndtime());
        String grades = examdoTaskEdu.getGradeids();
        String roleids = examdoTaskEdu.getRoleids();
        String rolename = examdoTaskEdu.getRolename();
        String cityid = examdoTaskEdu.getCityid();
        String countyid = examdoTaskEdu.getCountyid();
        String[] scales = examdoTaskEdu.getScaleids().split(",");
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if ("1".equals(objectType)) {// 分发对象是学生
                String[] gradeids = grades.split(",");
                examdoTaskSchoolMapper.updateDispenseStatus(orgid, taskid);// 转发单位由区县教委变更为学校
                // for(int i=0;i<gradeids.length;i++){
                // System.out.println("=============================================学生开始"+studentId[i]);
                // String orderId[]=studentId[i].split("_");
                Map<String, Object> paramets = new HashMap<String, Object>();
                paramets.put("taskid", taskid);
                paramets.put("taskfrom", 0);// 任务来源，0:本学校老师创建 1:上级教委创建
                paramets.put("orgid", orgid);
                paramets.put("countyid", countyid);
                paramets.put("cityid", cityid);
                // 要分发的量表
                paramets.put(Constants.SCALEIDS_PROP, scales);
                // 分发对象学生
                paramets.put(Constants.FLAG_PROP, objectType);
                paramets.put("gradeids", gradeids);
                // 开始时间、结束时间和限期
                paramets.put("starttime", starttime);
                paramets.put(Constants.LOTIME_PROP, starttime);
                paramets.put(Constants.HITIME_PROP, endtime);
                paramets.put("result", resultMsgMap);
                examInvoker.toSchoolUsersForGroup(paramets);
                // }
            } else if ("2".equals(objectType)) {// 分发对象是教师
                examdoTaskSchoolMapper.updateDispenseStatus(orgid, taskid);
                String[] teacherRole = roleids.split(",");
                String[] rolenames = rolename.split(",");
                // System.out.println("=============================================教师开始");
                // for(int i=0;i<teacherRole.length;i++){
                Map<String, Object> paramets = new HashMap<String, Object>();
                paramets.put("taskid", taskid);
                paramets.put("taskfrom", 1);// 任务来源，0:本学校老师创建 1:上级教委创建
                paramets.put("orgid", orgid);
                paramets.put("countyid", countyid);
                paramets.put("cityid", cityid);
                // 要分发的量表
                paramets.put(Constants.SCALEIDS_PROP, scales);

                // paramets.put(Constants.FLAG_PROP,
                // Integer.valueOf(teacherRole[i]));
                // 开始时间、结束时间和限期
                paramets.put("starttime", starttime);
                paramets.put(Constants.LOTIME_PROP, starttime);
                paramets.put(Constants.HITIME_PROP, endtime);
                // int roleid = Integer.parseInt(teacherRole[i]);
                // paramets.put("roleid",roleid);
                paramets.put("roleids", teacherRole);
                // paramets.put("rolename", rolenames[i]);
                paramets.put("result", resultMsgMap);
                examInvoker.toSchoolUsersForGroup(paramets);
                // txManager.commit(status);
                // }
                // System.out.println("=============================================教师结束");
            }
            boolean hasDispense = false;
            Map statisMap = (Map) resultMsgMap.get("statisMap");
            Map<String, String> scaleMap = new HashMap<String, String>();
            resultMsgMap.put("scaleMap", scaleMap);
            for (int i = 0; i < scales.length; i++) {
                String s = scales[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                String scalename = cachedScaleMgr.get(scaleid).getTitle();
                scaleMap.put(scaleid, scalename);
                int count = 0;
                if (statisMap == null)
                    count = 0;
                else {
                    String countStr = statisMap.get(scaleid).toString();
                    count = Integer.parseInt(countStr);
                }
                if (count > 0) {// 有分发记录
                    hasDispense = true;
                    break;
                }
            }
            if (hasDispense)
                txManager.commit(status);
            else
                txManager.rollback(status);
            // txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            // System.out.println("=============================================错误");
        } finally {
            // txManager.rollback(status);
        }

    }

    public void toSchoolUsers(Map<String, Object> params) throws Exception {
        // examInvoker.toSchoolUsers(params);
    }

    @Override
    public void eduDistributeForStu(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String xd, String[] gradeIds,
            String[] scales, String[] schoolIds, Map resultMsgMap) {
        // TODO Auto-generated method stub
        starttime += " 00:00:00";
        endtime += " 23:59:59";
        // TransactionDefinition def = new DefaultTransactionDefinition();
        // TransactionStatus status = txManager.getTransaction(def);
        try {
            // 这里插入分发任务表记录
            ExamDoEduTask examdoEduTask = new ExamDoEduTask();
            examdoEduTask.setTaskname(taskname);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
            examdoEduTask.setCreatetime(df.format(new Date()));
            examdoEduTask.setStarttime(starttime);
            examdoEduTask.setEndtime(endtime);
            examdoEduTask.setXd(Integer.parseInt(xd));
            examdoEduTask.setCreateuserid(createuserid);
            examdoEduTask.setCreaterorgid(creater_orgid);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < scales.length; i++) {
                sb.append(scales[i]);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            examdoEduTask.setScaleids(sb.toString());
            if (orgIds != null) {
                if (orgIds.length > 0) {
                    sb = new StringBuffer();
                    for (int i = 0; i < orgIds.length; i++) {
                        sb.append(orgIds[i]);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    examdoEduTask.setOrgids(sb.toString());
                }
            }
            StringBuffer sbnjmc, sbnj;
            if (gradeIds != null) {
                sb = new StringBuffer();
                sbnjmc = new StringBuffer();
                sbnj = new StringBuffer();
                for (int i = 0; i < gradeIds.length; i++) {
                    sb.append(gradeIds[i]);
                    sb.append(",");
                    String njmc = AgeUitl.getNjName(Integer.parseInt(gradeIds[i]));
                    //String njmc = AgeUitl.getGradeName(Integer.parseInt(gradeIds[i]));
                    sbnjmc.append(njmc);
                    sbnjmc.append(",");
                    int nj = AgeUitl.getNj(Integer.parseInt(gradeIds[i]));
                    sbnj.append(nj);
                    sbnj.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sbnjmc.deleteCharAt(sbnjmc.length() - 1);
                sbnj.deleteCharAt(sbnj.length() - 1);
                examdoEduTask.setGradeids(sb.toString());
                examdoEduTask.setNj(sbnj.toString());
                examdoEduTask.setNjmc(sbnjmc.toString());

            }
            if (areaIds != null) {
                if (areaIds.length > 0) {
                    sb = new StringBuffer();
                    for (int i = 0; i < areaIds.length; i++) {
                        sb.append(areaIds[i]);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    examdoEduTask.setAreaids(sb.toString());
                }
            }
            if (schoolIds != null) {
                if (schoolIds.length > 0) {
                    sb = new StringBuffer();
                    for (int i = 0; i < schoolIds.length; i++) {
                        sb.append(schoolIds[i]);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    examdoEduTask.setOrgids(sb.toString());
                }
            }
            boolean isToEdu = false;
            if (schoolIds == null)
                isToEdu = true;
            else
                isToEdu = false;
            examInvoker.insertExamTaskToEdu(examdoEduTask, isToEdu, resultMsgMap);

            // txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            // txManager.rollback(status);
            // System.out.println("=============================================错误");
        } finally {
            // txManager.rollback(status);
        }
    }

    @Override
    public void eduDistributeForTeacher(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String[] scales, String[] teacherroleIds,
            String[] schoolIds, Map resultMsgMap) {
        // TODO Auto-generated method stub
        starttime += " 00:00:00";
        endtime += " 23:59:59";
        // TransactionDefinition def = new DefaultTransactionDefinition();
        // TransactionStatus status = txManager.getTransaction(def);
        try {
            // 这里插入分发任务表记录
            ExamDoEduTask examdoEduTask = new ExamDoEduTask();
            examdoEduTask.setTaskname(taskname);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
            examdoEduTask.setCreatetime(df.format(new Date()));
            examdoEduTask.setStarttime(starttime);
            examdoEduTask.setEndtime(endtime);
            // examdoEduTask.setXd(Integer.parseInt(xd));
            examdoEduTask.setCreateuserid(createuserid);
            examdoEduTask.setCreaterorgid(creater_orgid);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < scales.length; i++) {
                sb.append(scales[i]);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            examdoEduTask.setScaleids(sb.toString());
            if (orgIds != null) {
                if (orgIds.length > 0) {
                    sb = new StringBuffer();
                    for (int i = 0; i < orgIds.length; i++) {
                        sb.append(orgIds[i]);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    examdoEduTask.setOrgids(sb.toString());
                }
            }

            // 角色名称
            String[] rolenames = new String[teacherroleIds.length];
            sb.setLength(0);
            for (int i = 0; i < teacherroleIds.length; i++) {
                long roleid = Integer.parseInt(teacherroleIds[i]);
                Role r = roleMapper.selectByPrimaryKey(roleid);
                String rolename = r.getRoleName();
                rolenames[i] = rolename;
                sb.append(roleid);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            examdoEduTask.setTeacherRoleIds(sb.toString());
            examdoEduTask.setRoleids(sb.toString());
            sb.setLength(0);
            for (int i = 0; i < rolenames.length; i++) {
                String rolename = rolenames[i];
                sb.append(rolename);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            examdoEduTask.setRolename(sb.toString());

            if (areaIds != null) {
                if (areaIds.length > 0) {
                    sb = new StringBuffer();
                    for (int i = 0; i < areaIds.length; i++) {
                        sb.append(areaIds[i]);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    examdoEduTask.setAreaids(sb.toString());
                }
            }
            if (schoolIds != null) {
                if (schoolIds.length > 0) {
                    sb = new StringBuffer();
                    for (int i = 0; i < schoolIds.length; i++) {
                        sb.append(schoolIds[i]);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    examdoEduTask.setOrgids(sb.toString());
                }
            }
            boolean isToEdu = false;
            if (schoolIds == null)
                isToEdu = true;
            else
                isToEdu = false;
            examInvoker.insertExamTaskToEdu(examdoEduTask, isToEdu, resultMsgMap);

            // txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            // txManager.rollback(status);
            // System.out.println("=============================================错误");
        } finally {
            // txManager.rollback(status);
        }
    }
}
