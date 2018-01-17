package com.njpes.www.invoker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.ParentMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamTaskMapper;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.ExamDoEduTask;
import com.njpes.www.entity.scaletoollib.ExamDoSchoolTask;
import com.njpes.www.entity.scaletoollib.ExamDoTaskForEduSchool;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.entity.scaletoollib.MhTask;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.utils.AgeUitl;

import edu.emory.mathcs.backport.java.util.Arrays;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exam.ExamUtils;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilMisc;

@Component("ExamInvoker")
public class ExamInvoker {
    /* 限制 */
    public final static byte NO_LIMIT = 0;
    public final static byte LIMIT_DONE = 2;
    public final static byte LIMIT = 1;

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    SqlSessionFactoryBean sqlSessionFactory;

    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    public StudentServiceI studentService;
    @Autowired
    private TeacherMapper teacherDao;
    @Autowired
    private ParentMapper parentDao;
    @Autowired
    private ExamTaskMapper examTaskDao;
    @Autowired
    private OrganizationMapper organizationMaper;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;

    public StudentMapper getStudentDao() {
        return studentDao;
    }

    public void setStudentDao(StudentMapper studentDao) {
        this.studentDao = studentDao;
    }

    public TeacherMapper getTeacherDao() {
        return teacherDao;
    }

    public void setTeacherDao(TeacherMapper teacherDao) {
        this.teacherDao = teacherDao;
    }

    public ExamTaskMapper getExamTaskDao() {
        return examTaskDao;
    }

    public void setExamTaskDao(ExamTaskMapper examTaskDao) {
        this.examTaskDao = examTaskDao;
    }

    public void insertExamTaskToSchool(ExamDoSchoolTask task) {
        examTaskDao.insertExamDoTaskForSchool(task);
    }

    public void insertExamTaskToEdu(ExamDoEduTask task, boolean isToEdu, Map resultMsgMap) {
        List errorMsgList = (List) resultMsgMap.get("errorMsgList");
        Map statisMap = (Map) resultMsgMap.get("statisMap");
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 插入任务表记录
            examTaskDao.insertExamDoTaskForEdu(task);
            // long taskid = task.getId();
            // Map<String, Object> paramets = new HashMap<String, Object>();
            String orgidStr = task.getOrgids();

            // if(StringUtils.isEmpty(orgidStr)){//不是分发到直属学校
            if (isToEdu) {// 不是分发到直属学校
                // 获得所有学校
                // 区县机构id
                String[] areaIdstr = task.getAreaids().split(",");
                List areaids = Arrays.asList(areaIdstr);
                // List<Organization> schools =
                // organizationMaper.getSchoolOrgByCountyIds(areaids);

                // Map<String, Object> paramets = new HashMap<String, Object>();
                // paramets.put("taskid", task.getId());

                // SqlSessionFactory session = sqlSessionFactory.getObject();
                // SqlSession sqlSession = session.openSession();
                // AssignSchoolResultHandler assignSchoolResultHandler = new
                // AssignSchoolResultHandler(paramets);
                // sqlSession.select("getSchoolOrgByCountyIds",areaids,
                // assignSchoolResultHandler);
                List<Organization> schoolList = organizationMaper.getSchoolOrgByCountyIds(areaids);

                List<ExamDoTaskForEduSchool> examdoEduForSchoolList = new ArrayList<ExamDoTaskForEduSchool>();
                for (int i = 0; i < schoolList.size(); i++) {
                    // Map<String, String> paramets = new HashMap<String,
                    // String>();
                    ExamDoTaskForEduSchool examDoTaskForEduSchool = new ExamDoTaskForEduSchool();
                    Organization school = schoolList.get(i);
                    // paramets.put("taskid", String.valueOf(task.getId()));
                    // paramets.put("orgid", String.valueOf(school.getId()));
                    // paramets.put("cityid", school.getCityid());
                    // paramets.put("countyid",school.getCountyid());
                    // paramets.put("dispenseorgid",
                    // String.valueOf(school.getParentId()));
                    examDoTaskForEduSchool.setTaskid(String.valueOf(task.getId()));
                    examDoTaskForEduSchool.setOrgid(String.valueOf(school.getId()));
                    examDoTaskForEduSchool.setCityid(school.getCityid());
                    examDoTaskForEduSchool.setCountyid(school.getCountyid());
                    examDoTaskForEduSchool.setDispenseorgid(String.valueOf(school.getParentId()));
                    // examdoEduForSchoolList.add(paramets);
                    examdoEduForSchoolList.add(examDoTaskForEduSchool);
                    // examTaskDao.insertExamDoTaskForEduSchool(paramets);
                }
                if (examdoEduForSchoolList.size() > 0)
                    examTaskDao.insertExamDoTaskForEduSchoolBatch(examdoEduForSchoolList);

                int count = 0;// 已经分发过的人数
                String scaleids = task.getScaleids();
                String[] scaleArray = scaleids.split(",");
                String[] scaleidArray = new String[scaleArray.length];
                for (int i = 0; i < scaleArray.length; i++) {
                    String scaleidStr = scaleArray[i];
                    String[] scales = scaleidStr.split("_");
                    String scaleid = scales[0];
                    String scalename = cachedScaleMgr.get(scaleid).getTitle();
                    Map<?, ?> param = UtilMisc.toMap("counties", areaIdstr, "scaleid", scaleid, "starttime",
                            task.getStarttime());
                    if (task.getRoleids() == null) {
                        int testlimit = Integer.parseInt(scales[4]);
                        if (testlimit == 1) {// 如果有再测限制
                            count = examTaskDao.queryStuExamdoCountForCounties(param);
                            statisMap.put(scalename, count);
                        } else
                            statisMap.put(scalename, 0);
                    } else {
                        int testlimit = Integer.parseInt(scales[1]);
                        if (testlimit == 1) {
                            count = examTaskDao.queryTeaExamdoCountForCounties(param);
                            statisMap.put(scalename, count);
                        } else
                            statisMap.put(scalename, 0);

                    }
                }

            } else {// 分发到学校
                    // examTaskDao.insertExamDoTaskForEdu(task);
                String[] orgids = orgidStr.split(",");
                // List<Map<String,String>> examdoEduForSchoolList = new
                // ArrayList<Map<String,String>>();
                List<ExamDoTaskForEduSchool> examdoEduForSchoolList = new ArrayList<ExamDoTaskForEduSchool>();
                for (int i = 0; i < orgids.length; i++) {
                    // Map<String, String> paramets = new HashMap<String,
                    // String>();
                    ExamDoTaskForEduSchool examDoTaskForEduSchool = new ExamDoTaskForEduSchool();
                    long schoolid = Integer.parseInt(orgids[i]);
                    Organization school = organizationMaper.selectByPrimaryKey(schoolid);
                    // paramets.put("taskid", String.valueOf(task.getId()));
                    // paramets.put("orgid", String.valueOf(schoolid));
                    // paramets.put("cityid", school.getCityid());
                    // paramets.put("countyid",school.getCountyid());
                    // .put("townid",school.getTownid());
                    // paramets.put("dispenseorgid", String.valueOf(schoolid));
                    examDoTaskForEduSchool.setTaskid(String.valueOf(task.getId()));
                    examDoTaskForEduSchool.setOrgid(String.valueOf(schoolid));
                    examDoTaskForEduSchool.setCityid(school.getCityid());
                    examDoTaskForEduSchool.setCountyid(school.getCountyid());
                    if ("".equals(school.getTownid())) {
                        examDoTaskForEduSchool.setTownid(null);
                    } else {
                        examDoTaskForEduSchool.setTownid(school.getTownid());
                    }
                    examDoTaskForEduSchool.setDispenseorgid(orgids[i]);
                    // examDoTaskForEduSchool.setDispenseorgid(String.valueOf(school.getParentId()));
                    // examdoEduForSchoolList.add(paramets);
                    examdoEduForSchoolList.add(examDoTaskForEduSchool);
                }
                if (examdoEduForSchoolList.size() > 0)
                    examTaskDao.insertExamDoTaskForEduSchoolBatch(examdoEduForSchoolList);

                int count = 0;// 已经分发过的人数
                String scaleids = task.getScaleids();
                String[] scaleArray = scaleids.split(",");
                String[] scaleidArray = new String[scaleArray.length];
                for (int i = 0; i < scaleArray.length; i++) {
                    String scaleidStr = scaleArray[i];
                    String[] scales = scaleidStr.split("_");
                    String scaleid = scales[0];
                    String scalename = cachedScaleMgr.get(scaleid).getTitle();
                    Map<?, ?> param = UtilMisc.toMap("orgids", orgids, "scaleid", scaleid, "starttime",
                            task.getStarttime());
                    if (task.getRoleids() == null) {
                        int testlimit = Integer.parseInt(scales[5]);
                        if (testlimit == 1) {// 如果有再测限制
                            count = examTaskDao.queryStuExamdoCountForSchools(param);
                            statisMap.put(scalename, count);
                        } else
                            statisMap.put(scalename, 0);
                    } else {
                        int testlimit = Integer.parseInt(scales[1]);
                        if (testlimit == 1) {
                            count = examTaskDao.queryStuExamdoCountForSchools(param);
                            statisMap.put(scalename, count);
                        } else
                            statisMap.put(scalename, 0);

                    }
                }
            }

            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
        }
    }

    /**
     * 将量表分发给学校用户: 学校用户分两类-教师和学生
     * 
     * @param paramets
     *            参数是一个map,map中必含有的键 1.
     *            {@link Constants#SCALEIDS_PROP},其值是要分发的量表编号,其是一个字符数组; 2.
     *            {@link Constants#FLAG_PROP},用户角色，用来判断是教师还是学生 3.
     *            {@link Constants#ORG_ID_PROP},表示分发对象的学校id 4.
     *            如果是学生，至少要提供{@link Constants#GRADE_ORDER_ID_PROP}，表示分发学生的年级 5.
     *            限期;
     *            {@link Constants#LOTIME_PROP}和{@link Constants#HITIME_PROP},表示起止时间
     *            6. 分发限制: {@link Constants#LIMIT_FLAG_PROP},表示这次分发的限制 其它内容
     *            如果是学生，还可以提供{@link Constants#CLASS_ID_PROP}，表示分发学生的班级
     * @throws Exception
     */
    public void toSchoolUsersForSingle(Map<String, Object> paramets) throws Exception {
        // writeLock();
        Object o = MapUtils.getObject(paramets, Constants.SCALEIDS_PROP);
        Validate.notNull(o, "没有需要分发的量表.");
        Validate.notNull(paramets.get(Constants.LOTIME_PROP), "Constants.LOTIME_PROP");
        Validate.notNull(paramets.get(Constants.HITIME_PROP), "Constants.HITIME_PROP");
        String scaleIds[] = (String[]) o;
        int typeFlag = MapUtils.getIntValue(paramets, Constants.FLAG_PROP);
        // Validate.isTrue(typeFlag > 0, "必须设置flag属性，即用户的角色");

        // 根据角色选择测试任务表
        String examDotabl = ExamUtils.getExamDoTable(typeFlag);
        paramets.put(Constants.TABLE_PROP, examDotabl);
        // 如果是学生
        if (typeFlag == 1) {
            paramets.put(Constants.FLAG_PROP, typeFlag);
            storeStuduentExamdo(scaleIds, paramets);
        } else {// 如果是教师
            storeTeacherExamdo(scaleIds, paramets);
        }
    }

    public void toSchoolUsersForGroup(Map<String, Object> paramets) throws Exception {
        // writeLock();
        Object o = MapUtils.getObject(paramets, Constants.SCALEIDS_PROP);
        Validate.notNull(o, "没有需要分发的量表.");
        Validate.notNull(paramets.get(Constants.LOTIME_PROP), "Constants.LOTIME_PROP");
        Validate.notNull(paramets.get(Constants.HITIME_PROP), "Constants.HITIME_PROP");
        String scaleIds[] = (String[]) o;
        int typeFlag = MapUtils.getIntValue(paramets, Constants.FLAG_PROP);
        // Validate.isTrue(typeFlag > 0, "必须设置flag属性，即用户的角色");

        // 根据角色选择测试任务表
        String examDotabl = ExamUtils.getExamDoTable(typeFlag);
        paramets.put(Constants.TABLE_PROP, examDotabl);
        // 如果是学生
        if (typeFlag == 1) {
            String[] gradeClassids = (String[]) paramets.get("gradeClassids");
            if (gradeClassids != null) {
                List<String> bjids = new ArrayList<String>();
                for (int i = 0; i < gradeClassids.length; i++) {
                    String gcs = gradeClassids[i];
                    String[] xd_bj = gcs.split(",");
                    String bj = xd_bj[1];
                    String[] bjinfo = bj.split(":");
                    String bjid = bjinfo[0];
                    bjids.add(bjid);
                }
                paramets.put("bjids", bjids);
            }
            String[] gradeids = (String[]) paramets.get("gradeids");
            if (gradeids != null) {
                paramets.put("gradeids", gradeids);
            }
            storeGroupStuduentExamdo(scaleIds, paramets);
        } else {// 如果是教师
            storeTeacherExamdo(scaleIds, paramets);
        }
    }

    public void toSchoolUserList(List<Map<String, Object>> userList) throws Exception {

    }

    private void executeAssignIfMs(String[] scales, Map<String, Object> paramets) {
        Map<String, Object> newParamets = new HashMap<String, Object>(paramets);

        for (int i = 0; i < scales.length; ++i) {
            String s = scales[i];
            String[] scaleinfo = s.split("_");
            String scaleid = scaleinfo[0];
            if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                try {
                    // start();
                    // 取得学生
                    List<Integer> stuList;
                    newParamets.put(Constants.FLAG_PROP, 1);
                    Object studentId = paramets.get(Constants.USER_ID_PROP);
                    if (studentId == null) {
                        stuList = studentDao.getStudentIds(paramets);
                    } else {
                        stuList = new ArrayList<Integer>();
                        stuList.add(Integer.valueOf(studentId.toString()));
                    }

                    // 取得班主任老师
                    newParamets.put(Constants.FLAG_PROP, 2);
                    newParamets.remove(Constants.USER_ID_PROP);
                    List<Integer> teacherIdList = teacherDao.getBzrId(newParamets);

                    // 开始更新数据
                    // startBatch();
                    for (Integer id : stuList) {
                        MhTask mhTask = new MhTask();

                        mhTask.setTaskid(Integer.parseInt(MapUtils.getString(paramets, "taskid")));
                        mhTask.setStudentId(id.longValue());
                        mhTask.setHiTime(MapUtils.getString(paramets, Constants.HITIME_PROP));
                        mhTask.setLoTime(MapUtils.getString(paramets, Constants.LOTIME_PROP));
                        mhTask.setParentId(id.longValue() + 1);// 家长id和学生id一样？
                        if (CollectionUtils.isNotEmpty(teacherIdList)) {
                            mhTask.setTeacherId(teacherIdList.get(0).longValue());
                        }
                        // 取得家长

                        String gradeId = MapUtils.getString(paramets, "nj");
                        mhTask.setGradeId(Integer.parseInt(gradeId));
                        mhTask.setFlag(MapUtils.getIntValue(paramets, Constants.LIMIT_FLAG_PROP));
                        // if (!set.contains(mhTask.getStudentId())) {
                        // insert("insertMhTask", mhTask);
                        // }
                        examTaskDao.insertMhTask(mhTask);
                    }

                    break;
                } finally {

                }
            } // if
        } // for
    }

    private void executeAssignIfMs(String scale, Map<String, Object> paramets) {
        Map<String, Object> newParamets = new HashMap<String, Object>(paramets);

        String[] scaleinfo = scale.split("_");
        String scaleid = scaleinfo[0];
        try {
            // 取得学生
            newParamets.put(Constants.FLAG_PROP, 1);
            int studentId = (Integer) paramets.get(Constants.USER_ID_PROP);
            // 取得班主任老师
            newParamets.put(Constants.FLAG_PROP, 2);
            newParamets.remove(Constants.USER_ID_PROP);
            List<Integer> teacherIdList = teacherDao.getBzrId(newParamets);

            // 开始更新数据

            MhTask mhTask = new MhTask();
            mhTask.setResultId(Integer.parseInt(paramets.get("id").toString()));
            mhTask.setId(Integer.parseInt(MapUtils.getString(paramets, "id")));
            mhTask.setTaskid(Integer.parseInt(MapUtils.getString(paramets, "taskid")));
            mhTask.setStudentId(studentId);
            mhTask.setHiTime(MapUtils.getString(paramets, Constants.HITIME_PROP));
            mhTask.setLoTime(MapUtils.getString(paramets, Constants.LOTIME_PROP));
            mhTask.setParentId(studentId + 1);// 家长id和学生id一样？
            mhTask.setScaleid_t(MapUtils.getString(paramets, "scaleid_t"));
            mhTask.setScaleid_p(MapUtils.getString(paramets, "scaleid_p"));
            if (CollectionUtils.isNotEmpty(teacherIdList)) {
                mhTask.setTeacherId(teacherIdList.get(0).longValue());
            }
            // 取得家长
            String gradeId = MapUtils.getString(paramets, "nj");
            mhTask.setGradeId(Integer.parseInt(gradeId));
            // mhTask.setFlag(MapUtils.getIntValue(paramets,
            // Constants.LIMIT_FLAG_PROP));
            // if (!set.contains(mhTask.getStudentId())) {
            // insert("insertMhTask", mhTask);
            // }
            examTaskDao.insertMhTask(mhTask);

        } finally {

        }
    }

    private void executeAssignIfMs1(List<ExamdoStudent> examdoList) throws Exception {
        List<MhTask> mhTaskList = new ArrayList<MhTask>();
        try {
            for (int i = 0; i < examdoList.size(); i++) {
                // long resultid = lastInsertId+i+1;//主键id
                ExamdoStudent examdo = examdoList.get(i);
                long resultid = examdo.getId();
                Map newParamets = new HashMap();
                newParamets.put("orgid", examdo.getOrgid());
                newParamets.put("bj", examdo.getBj());
                // 取得学生
                int studentId = (int) examdo.getUserid();
                // 取得班主任老师
                newParamets.remove(Constants.USER_ID_PROP);
                // List<Integer> teacherIdList =
                // teacherDao.getBzrId(newParamets);

                // 开始更新数据

                MhTask mhTask = new MhTask();
                mhTask.setResultId(resultid);
                mhTask.setId(resultid);
                mhTask.setTaskid(examdo.getTaskid());
                mhTask.setStudentId(studentId);
                mhTask.setLoTime(examdo.getStarttime());
                mhTask.setHiTime(examdo.getEndtime());
                // mhTask.setParentId(studentId + 1);// 家长id和学生id一样？
                int gradeid = examdo.getGradeid();
                String scaleid_t = ScaleUtils.getThreeAngleScaleForTeacherByGradeid(gradeid);
                String scaleid_p = ScaleUtils.getThreeAngleScaleForParentByGradeid(gradeid);

                mhTask.setScaleid_t(scaleid_t);
                mhTask.setScaleid_p(scaleid_p);
                // if (CollectionUtils.isNotEmpty(teacherIdList)) {
                // mhTask.setTeacherId(teacherIdList.get(0).longValue());
                // }
                if (examdo.getBzrid() != null)
                    mhTask.setTeacherId(Long.parseLong(examdo.getBzrid().toString()));
                // 取得家长
                int gradeId = examdo.getGradeid();
                mhTask.setGradeId(gradeid);
                mhTask.setFlag(examdo.getTestlimit());
                if (examdo.getParentid() != null)
                    mhTask.setParentId(Long.parseLong(examdo.getParentid().toString()));
                mhTaskList.add(mhTask);
                // examTaskDao.insertMhTask(mhTask);
            }
            if (mhTaskList.size() > 0)
                examTaskDao.insertBatchMhTask(mhTaskList);
        } catch (Exception e) {
            throw new RuntimeException("三方共测量表分发入库错误！");
        }
    }

    /*
     * private void storeStuduentExamdo(String[] scaleIds,Map<String, Object>
     * paramets) throws Exception { Map resultMsgMap =
     * (Map)paramets.get("result"); List errorMsgList =
     * (List)resultMsgMap.get("errorMsgList"); Map statisMap =
     * (Map)resultMsgMap.get("statisMap"); Object studentId =
     * paramets.get(Constants.USER_ID_PROP); if(studentId == null){
     * List<Map>stuList = studentDao.getBasicStudents(paramets); for (Map stu :
     * stuList) { long id = Long.parseLong(stu.get("id").toString());
     * paramets.put("userId", id); paramets.put("bj", stu.get("bjid"));
     * paramets.put("bjmc",stu.get("bjmc")); for (int i=0;i<scaleIds.length;i++)
     * { String s = scaleIds[i]; String[] scaleinfo = s.split("_"); String
     * scaleid = scaleinfo[0]; //判断量表是否被此学生做过（90天之内） Map<String,Object> pMap =
     * new HashMap<String,Object>(); pMap.put("studentid",id);
     * pMap.put("scaleid", scaleid);
     * pMap.put("starttime",paramets.get("starttime").toString() );
     * 
     * Object statis = statisMap.get(scaleid); if(statis ==null)
     * statisMap.put(scaleid,"0");
     * 
     * int parentvisible = Integer.parseInt(scaleinfo[1]); int teachervisible =
     * Integer.parseInt(scaleinfo[2]); int studentvisible =
     * Integer.parseInt(scaleinfo[3]); int warningvisible =
     * Integer.parseInt(scaleinfo[4]); int testlimit =
     * Integer.parseInt(scaleinfo[5]); //pMap.put("starttime",
     * paramets.get("starttime").toString()); //List<Map> limitedExamdoMap =
     * examTaskDao.queryLimitedStudentExamdo(pMap); if(testlimit==1){ List<Map>
     * limitedExamdoMap = examTaskDao.queryLimitedStudentExamdo(pMap);
     * if(limitedExamdoMap!=null){ Map limitedExamdo = limitedExamdoMap.get(0);
     * if(limitedExamdo!=null){ long userid =
     * Long.parseLong(limitedExamdo.get("userid").toString()); if(userid ==
     * id)//当前用户已经在90天之内做过此量表 { //将结果输出页面 String njmc =
     * limitedExamdo.get("njmc").toString(); String bjmc =
     * limitedExamdo.get("bjmc").toString(); String xm =
     * limitedExamdo.get("xm").toString(); String scaletitle =
     * limitedExamdo.get("scaletitle").toString();
     * errorMsgList.add(scaletitle+"90天之前已被分到"+njmc+bjmc+xm+",不能再分发！");
     * continue; } } } }
     * 
     * paramets.put("parentvisible", parentvisible);
     * paramets.put("teachervisible", teachervisible);
     * paramets.put("studentvisible", studentvisible);
     * paramets.put("warnvisible", warningvisible); paramets.put("testlimit",
     * testlimit); paramets.put("scaleId", scaleid); paramets.put("statusflag",
     * 0);//完成状态，0：未答题，1，学生答完题，11学生教师答完题，111学生教师家长答完题 //paramets.put("scaleId",
     * scaleid); if(ScaleUtils.isThirdAngleScale(scaleid)) paramets.put("ifmh",
     * true); else paramets.put("ifmh", false);
     * 
     * examTaskDao.insertStudentExamDo(paramets); long resultid =
     * Integer.parseInt(paramets.get("id").toString()); if
     * (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
     * //三角视家长版和教师版量表赋给参数 int gradeid =
     * Integer.parseInt(paramets.get("gradeid").toString()); String scaleid_t =
     * ScaleUtils.getThreeAngleScaleForTeacherByGradeid(gradeid); String
     * scaleid_p = ScaleUtils.getThreeAngleScaleForParentByGradeid(gradeid);
     * paramets.put("scaleid_t", scaleid_t); paramets.put("scaleid_p",
     * scaleid_p); executeAssignIfMs(scaleid,paramets); }
     * 
     * //统计分发人数 int count = 0; statis = statisMap.get(scaleid); if(statis
     * ==null) count =0; else{ count = Integer.parseInt(statis.toString()); }
     * count++; statisMap.put(scaleid,String.valueOf(count)); } } } else{
     * paramets.put("userId", Integer.parseInt(studentId.toString())); String[]
     * scales = (String[]) paramets.get("scaleIds");
     * 
     * 
     * for (int i=0;i<scales.length;i++) { String s = scales[i]; String[]
     * scaleinfo = s.split("_"); String scaleid = scaleinfo[0]; int
     * parentvisible = Integer.parseInt(scaleinfo[1]); int teachervisible =
     * Integer.parseInt(scaleinfo[2]); int studentvisible =
     * Integer.parseInt(scaleinfo[3]); int warningvisible =
     * Integer.parseInt(scaleinfo[4]); int testlimit =
     * Integer.parseInt(scaleinfo[5]); paramets.put("parentvisible",
     * parentvisible); paramets.put("teachervisible", teachervisible);
     * paramets.put("studentvisible", studentvisible);
     * paramets.put("warnvisible", warningvisible); paramets.put("testlimit",
     * testlimit); paramets.put("scaleId", scaleid);
     * 
     * paramets.put("statusflag", 0);//完成状态，0：未答题，1，学生答完题，11学生教师答完题，111学生教师家长答完题
     * if(ScaleUtils.isThirdAngleScale(scaleid)) paramets.put("ifmh", true);
     * else paramets.put("ifmh", false);
     * 
     * Object statis = statisMap.get(scaleid); if(statis ==null)
     * statisMap.put(scaleid,"0");
     * 
     * Map<String,Object> pMap = new HashMap<String,Object>();
     * pMap.put("studentid",Integer.parseInt(studentId.toString()));
     * pMap.put("scaleid", scaleid);
     * pMap.put("starttime",paramets.get("starttime").toString() );
     * //pMap.put("starttime", paramets.get("starttime").toString());
     * if(testlimit==1){//如果有再测限制 List<Map> limitedExamdoMap =
     * examTaskDao.queryLimitedStudentExamdo(pMap); if(limitedExamdoMap!=null){
     * Map limitedExamdo = limitedExamdoMap.get(0); if(limitedExamdo!=null){
     * String limiteduserid = limitedExamdo.get("userid").toString();
     * if(limiteduserid.equals(studentId.toString()))//当前用户已经在90天之内做过此量表 {
     * //将结果输出页面 String njmc = limitedExamdo.get("njmc").toString(); String bjmc
     * = limitedExamdo.get("bjmc").toString(); String xm =
     * limitedExamdo.get("xm").toString(); String scaletitle =
     * limitedExamdo.get("scaletitle").toString();
     * errorMsgList.add(scaletitle+"90天之前已被分到"+njmc+bjmc+xm+",不能再分发！");
     * continue; } } } } examTaskDao.insertStudentExamDo(paramets);
     * 
     * if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
     * //三角视家长版和教师版量表赋给参数 int gradeid =
     * Integer.parseInt(paramets.get("gradeid").toString()); String scaleid_t =
     * ScaleUtils.getThreeAngleScaleForTeacherByGradeid(gradeid); String
     * scaleid_p = ScaleUtils.getThreeAngleScaleForParentByGradeid(gradeid);
     * paramets.put("scaleid_t", scaleid_t); paramets.put("scaleid_p",
     * scaleid_p); executeAssignIfMs(scaleid,paramets); } //统计分发人数 int count =
     * 0; statis = statisMap.get(scaleid); if(statis ==null) count =0; else{
     * count = Integer.parseInt(statis.toString()); } count++;
     * statisMap.put(scaleid,String.valueOf(count)); long resultid =
     * Integer.parseInt(paramets.get("id").toString());
     * 
     * } } }
     */
    private void storeStuduentExamdo(String[] scaleIds, Map<String, Object> paramets) throws Exception {
        Map resultMsgMap = (Map) paramets.get("result");
        List errorMsgList = (List) resultMsgMap.get("errorMsgList");
        Map statisMap = (Map) resultMsgMap.get("statisMap");
        // Object studentId = paramets.get(Constants.USER_ID_PROP);
        String[] studentIds = (String[]) paramets.get(Constants.USER_ID_PROP);
        List<ExamdoStudent> examdoList = new ArrayList<ExamdoStudent>();
        int rowCount = 0;
        int currentIndex = 0;
        int ind = 0;

        if (studentIds == null) {
            // 判断一下有没有三方共测量表
            for (int s = 0; s < scaleIds.length; s++) {
                String sc = scaleIds[s];
                String[] scaleinfo = sc.split("_");
                String scaleid = scaleinfo[0];
                if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                    paramets.put("isThreeAngle", true);
                    break;
                }
            }
            List<Map> stuList = studentDao.getBasicStudents(paramets);
            rowCount = stuList.size() * scaleIds.length;
            long lastInsertId = examTaskDao.getStudentMaxId();
            for (int i = 0; i < scaleIds.length; i++) {
                String s = scaleIds[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                int testlimit = Integer.parseInt(scaleinfo[5]);
                // 得到这批学生做此量表的限制情况
                Map<Long, Map> limitedExamdoMap = null;
                if (testlimit == 1) {
                    List<String> sids = new ArrayList<String>();
                    for (Map stu : stuList) {
                        sids.add(stu.get("userid").toString());
                    }
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("studentids", sids);
                    pMap.put("scaleid", scaleid);
                    pMap.put("starttime", paramets.get("starttime").toString());
                    List<Map> limitedExamdoList = examTaskDao.queryLimitedStudentExamdo(pMap);
                    limitedExamdoMap = new HashMap<Long, Map>();
                    for (Map limitedExamdo : limitedExamdoList) {
                        limitedExamdoMap.put((Long) limitedExamdo.get("userid"), limitedExamdo);
                    }
                }
                for (Map stu : stuList) {
                    ExamdoStudent examdo = new ExamdoStudent();

                    examdo.setTaskid(Long.parseLong(paramets.get("taskid").toString()));
                    examdo.setTaskfrom(Integer.parseInt(paramets.get("taskfrom").toString()));
                    examdo.setOrgid(Long.parseLong(paramets.get("orgid").toString()));
                    examdo.setCountyid(paramets.get("countyid").toString());
                    examdo.setCityid(paramets.get("cityid").toString());
                    // examdo.setStarttime();
                    examdo.setXd(Integer.parseInt(paramets.get("xd").toString()));
                    int gradeid = Integer.parseInt(paramets.get("gradeid").toString());
                    examdo.setGradeid(gradeid);
                    int realgradeid = 0;
                    BigDecimal xxxz = (BigDecimal) stu.get("xxxz");
                    if (xxxz != null)
                        realgradeid = AgeUitl.getRealGradeid(gradeid, String.valueOf(xxxz.intValue()));
                    else
                        realgradeid = gradeid;
                    // int realgradeid = AgeUitl.getRealGradeid(gradeid,
                    // stu.get("xxxz").toString());
                    examdo.setRealgradeid(realgradeid);

                    examdo.setNj(Integer.parseInt(paramets.get("nj").toString()));
                    examdo.setNjmc(paramets.get("njmc").toString());
                    examdo.setBj(Long.parseLong(paramets.get("bj").toString()));
                    examdo.setBjmc(paramets.get("bjmc").toString());
                    // 开始时间、结束时间和限期
                    examdo.setStarttime(paramets.get("loTime").toString());
                    examdo.setEndtime(paramets.get("hiTime").toString());
                    // examdo.setTestlimit(Integer.parseInt(paramets.get("limitFlag").toString()));

                    long uid = Long.parseLong(stu.get("id").toString());
                    examdo.setUserid(uid);

                    Object statis = statisMap.get(scaleid);
                    if (statis == null)
                        statisMap.put(scaleid, "0");

                    int parentvisible = Integer.parseInt(scaleinfo[1]);
                    int teachervisible = Integer.parseInt(scaleinfo[2]);
                    int studentvisible = Integer.parseInt(scaleinfo[3]);
                    int warningvisible = Integer.parseInt(scaleinfo[4]);
                    int normid = Integer.parseInt(scaleinfo[6]);
                    if (testlimit == 1) {// 如果有再测限制
                        if (limitedExamdoMap != null) {
                            Map limitedExamdo = limitedExamdoMap.get(uid);
                            if (limitedExamdo != null) {
                                String njmc = limitedExamdo.get("njmc").toString();
                                String bjmc = limitedExamdo.get("bjmc").toString();
                                String xm = limitedExamdo.get("xm").toString();
                                String scaletitle = limitedExamdo.get("scaletitle").toString();
                                errorMsgList.add(scaletitle + "90天之前已被分到" + njmc + bjmc + xm + ",不能再分发！");
                                continue;
                            }
                        }
                    }

                    examdo.setParentvisible(parentvisible);
                    examdo.setTeachervisible(teachervisible);
                    examdo.setStudentvisible(studentvisible);
                    examdo.setWarnvisible(warningvisible);
                    examdo.setNormid(normid);
                    examdo.setScaleid(scaleid);
                    examdo.setTestlimit(testlimit);
                    examdo.setStatusflag(0);// 完成状态，0：未答题，1，学生答完题，11学生教师答完题，111学生教师家长答完题

                    if (ScaleUtils.isThirdAngleScale(scaleid)) {
                        String bzrid = stu.get("teacherid") != null ? stu.get("teacherid").toString() : null;
                        examdo.setBzrid(bzrid);
                        String parentid = stu.get("parentid") != null ? stu.get("parentid").toString() : null;
                        examdo.setParentid(parentid);
                        examdo.setIfmh(true);
                    } else
                        examdo.setIfmh(false);

                    ind++;
                    examdo.setId(lastInsertId + ind);
                    examdoList.add(examdo);
                    if (examdoList.size() == 500 || currentIndex == rowCount - 1) {
                        if (examdoList.size() > 0) {
                            // long lastInsertId = examTaskDao.getMaxId();
                            int ii = examTaskDao.insertBatchExamdoStudent(examdoList);
                            if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                                executeAssignIfMs1(examdoList);
                            }
                            lastInsertId += examdoList.size();
                            examdoList = new ArrayList<ExamdoStudent>();
                        }
                    }

                    // 统计分发人数
                    int count = 0;
                    statis = statisMap.get(scaleid);
                    if (statis == null)
                        count = 0;
                    else {
                        count = Integer.parseInt(statis.toString());
                    }
                    count++;
                    statisMap.put(scaleid, String.valueOf(count));

                    currentIndex++;
                }
            }
        } else {

            int realgradeid = studentService.getRealGradeId(Long.parseLong(paramets.get("orgid").toString()),
                    Integer.parseInt(paramets.get("gradeid").toString()));

            rowCount = studentIds.length * scaleIds.length;
            long lastInsertId = examTaskDao.getStudentMaxId();
            for (int i = 0; i < scaleIds.length; i++) {
                String s = scaleIds[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                int testlimit = Integer.parseInt(scaleinfo[5]);
                // 得到这批学生做此量表的限制情况
                Map<Long, Map> limitedExamdoMap = null;
                if (testlimit == 1) {
                    List<String> sids = new ArrayList<String>();
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("studentids", studentIds);
                    pMap.put("scaleid", scaleid);
                    pMap.put("starttime", paramets.get("starttime").toString());
                    List<Map> limitedExamdoList = examTaskDao.queryLimitedStudentExamdo(pMap);
                    limitedExamdoMap = new HashMap<Long, Map>();
                    for (Map limitedExamdo : limitedExamdoList) {
                        limitedExamdoMap.put((Long) limitedExamdo.get("userid"), limitedExamdo);
                    }
                }
                for (int j = 0; j < studentIds.length; j++) {
                    ExamdoStudent examdo = new ExamdoStudent();
                    examdo.setTaskid(Long.parseLong(paramets.get("taskid").toString()));
                    examdo.setTaskfrom(Integer.parseInt(paramets.get("taskfrom").toString()));
                    examdo.setOrgid(Long.parseLong(paramets.get("orgid").toString()));
                    examdo.setCountyid(paramets.get("countyid").toString());
                    examdo.setCityid(paramets.get("cityid").toString());
                    // examdo.setStarttime();
                    examdo.setXd(Integer.parseInt(paramets.get("xd").toString()));
                    examdo.setGradeid(Integer.parseInt(paramets.get("gradeid").toString()));
                    examdo.setRealgradeid(realgradeid);
                    examdo.setNj(Integer.parseInt(paramets.get("nj").toString()));
                    examdo.setNjmc(paramets.get("njmc").toString());
                    examdo.setBj(Long.parseLong(paramets.get("bj").toString()));
                    examdo.setBjmc(paramets.get("bjmc").toString());
                    // 开始时间、结束时间和限期
                    examdo.setStarttime(paramets.get("loTime").toString());
                    examdo.setEndtime(paramets.get("hiTime").toString());

                    // examdo.setTestlimit(Integer.parseInt(paramets.get("limitFlag").toString()));

                    long uid = Long.parseLong(studentIds[j]);
                    examdo.setUserid(uid);
                    if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                        Map bzrAndParentMap = studentDao.getBzrAndParentIdByStudentid(uid);
                        if (bzrAndParentMap != null) {
                            if (bzrAndParentMap.get("teacherid") != null) {
                                String bzrid = bzrAndParentMap.get("teacherid").toString();
                                examdo.setBzrid(bzrid);
                            }
                            if (bzrAndParentMap.get("parentid") != null) {
                                String parentid = bzrAndParentMap.get("parentid").toString();
                                examdo.setParentid(parentid);
                            }
                        }
                    }

                    Object statis = statisMap.get(scaleid);
                    if (statis == null)
                        statisMap.put(scaleid, "0");

                    int parentvisible = Integer.parseInt(scaleinfo[1]);
                    int teachervisible = Integer.parseInt(scaleinfo[2]);
                    int studentvisible = Integer.parseInt(scaleinfo[3]);
                    int warningvisible = Integer.parseInt(scaleinfo[4]);
                    int normid = Integer.parseInt(scaleinfo[6]);
                    if (testlimit == 1) {// 如果有再测限制
                        if (limitedExamdoMap != null) {
                            Map limitedExamdo = limitedExamdoMap.get(uid);
                            if (limitedExamdo != null) {
                                String njmc = limitedExamdo.get("njmc").toString();
                                String bjmc = limitedExamdo.get("bjmc").toString();
                                String xm = limitedExamdo.get("xm").toString();
                                String scaletitle = limitedExamdo.get("scaletitle").toString();
                                errorMsgList.add(scaletitle + "90天之前已被分到" + njmc + bjmc + xm + ",不能再分发！");
                                continue;
                            }
                        }
                    }

                    examdo.setParentvisible(parentvisible);
                    examdo.setTeachervisible(teachervisible);
                    examdo.setStudentvisible(studentvisible);
                    examdo.setWarnvisible(warningvisible);
                    examdo.setNormid(normid);
                    examdo.setScaleid(scaleid);
                    examdo.setTestlimit(testlimit);
                    examdo.setStatusflag(0);// 完成状态，0：未答题，1，学生答完题，11学生教师答完题，111学生教师家长答完题

                    if (ScaleUtils.isThirdAngleScale(scaleid))
                        examdo.setIfmh(true);
                    else
                        examdo.setIfmh(false);
                    ind++;
                    examdo.setId(lastInsertId + ind);
                    examdoList.add(examdo);
                    if (examdoList.size() == 500 || currentIndex == rowCount - 1) {
                        if (examdoList.size() > 0) {
                            // long lastInsertId = examTaskDao.getMaxId();
                            int ii = examTaskDao.insertBatchExamdoStudent(examdoList);
                            if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                                executeAssignIfMs1(examdoList);
                            }
                            lastInsertId += examdoList.size();
                            examdoList = new ArrayList<ExamdoStudent>();
                        }
                    }
                    // 统计分发人数
                    int count = 0;
                    statis = statisMap.get(scaleid);
                    if (statis == null)
                        count = 0;
                    else {
                        count = Integer.parseInt(statis.toString());
                    }
                    count++;
                    statisMap.put(scaleid, String.valueOf(count));

                    currentIndex++;
                }
            }
        }
    }

    private void storeGroupStuduentExamdo(String[] scaleIds, Map<String, Object> paramets) throws Exception {
        Map resultMsgMap = (Map) paramets.get("result");
        List errorMsgList = (List) resultMsgMap.get("errorMsgList");
        Map statisMap = (Map) resultMsgMap.get("statisMap");
        // Object studentId = paramets.get(Constants.USER_ID_PROP);
        String[] studentIds = (String[]) paramets.get(Constants.USER_ID_PROP);
        List<ExamdoStudent> examdoList = new ArrayList<ExamdoStudent>();
        int rowCount = 0;
        int currentIndex = 0;
        int ind = 0;
        if (studentIds == null) {
            for (int s = 0; s < scaleIds.length; s++) {
                String sc = scaleIds[s];
                String[] scaleinfo = sc.split("_");
                String scaleid = scaleinfo[0];
                if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                    paramets.put("isThreeAngle", true);
                    break;
                }
            }
            List<Map> stuList = studentDao.getBasicStudents(paramets);
            rowCount = stuList.size() * scaleIds.length;
            long lastInsertId = examTaskDao.getStudentMaxId();
            for (int i = 0; i < scaleIds.length; i++) {
                String s = scaleIds[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                int testlimit = Integer.parseInt(scaleinfo[5]);
                // 得到这批学生做此量表的限制情况
                Map<Long, Map> limitedExamdoMap = null;
                if (testlimit == 1) {
                    List<String> sids = new ArrayList<String>();
                    for (Map stu : stuList) {
                        sids.add(stu.get("userid").toString());
                    }
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("studentids", sids);
                    pMap.put("scaleid", scaleid);
                    pMap.put("starttime", paramets.get("starttime").toString());
                    List<Map> limitedExamdoList = examTaskDao.queryLimitedStudentExamdo(pMap);
                    limitedExamdoMap = new HashMap<Long, Map>();
                    for (Map limitedExamdo : limitedExamdoList) {
                        limitedExamdoMap.put((Long) limitedExamdo.get("userid"), limitedExamdo);
                    }
                }

                for (Map stu : stuList) {
                    ExamdoStudent examdo = new ExamdoStudent();

                    examdo.setTaskid(Long.parseLong(paramets.get("taskid").toString()));
                    examdo.setTaskfrom(Integer.parseInt(paramets.get("taskfrom").toString()));
                    examdo.setOrgid(Long.parseLong(paramets.get("orgid").toString()));
                    examdo.setCountyid(paramets.get("countyid").toString());
                    examdo.setCityid(paramets.get("cityid").toString());
                    // examdo.setStarttime();
                    int gradeid = Integer.parseInt(stu.get("gradeid").toString());
                    int xd = AgeUitl.getXdByGrade(gradeid);
                    examdo.setXd(xd);
                    examdo.setGradeid(gradeid);
                    int realgradeid = 0;
                    BigDecimal xxxz = (BigDecimal) stu.get("xxxz");
                    if (xxxz != null)
                        realgradeid = AgeUitl.getRealGradeid(gradeid, String.valueOf(xxxz.intValue()));
                    else
                        realgradeid = gradeid;
                    examdo.setRealgradeid(realgradeid);

                    int nj = AgeUitl.getNj(gradeid);
                    examdo.setNj(nj);
                    examdo.setNjmc(AgeUitl.getNjName(gradeid));
                    examdo.setBj(Long.parseLong(stu.get("bjid").toString()));
                    examdo.setBjmc(stu.get("bjmc").toString());
                    // 开始时间、结束时间和限期
                    examdo.setStarttime(paramets.get("loTime").toString());
                    examdo.setEndtime(paramets.get("hiTime").toString());
                    long uid = Long.parseLong(stu.get("id").toString());
                    examdo.setUserid(uid);

                    Object statis = statisMap.get(scaleid);
                    if (statis == null)
                        statisMap.put(scaleid, "0");

                    int parentvisible = Integer.parseInt(scaleinfo[1]);
                    int teachervisible = Integer.parseInt(scaleinfo[2]);
                    int studentvisible = Integer.parseInt(scaleinfo[3]);
                    int warningvisible = Integer.parseInt(scaleinfo[4]);
                    int normid = Integer.parseInt(scaleinfo[6]);
                    if (testlimit == 1) {// 如果有再测限制
                        if (limitedExamdoMap != null) {
                            Map limitedExamdo = limitedExamdoMap.get(uid);
                            if (limitedExamdo != null) {
                                String njmc = limitedExamdo.get("njmc").toString();
                                String bjmc = limitedExamdo.get("bjmc").toString();
                                String xm = limitedExamdo.get("xm").toString();
                                String scaletitle = limitedExamdo.get("scaletitle").toString();
                                errorMsgList.add(scaletitle + "90天之前已被分到" + njmc + bjmc + xm + ",不能再分发！");
                                continue;
                            }
                        }
                    }

                    examdo.setParentvisible(parentvisible);
                    examdo.setTeachervisible(teachervisible);
                    examdo.setStudentvisible(studentvisible);
                    examdo.setWarnvisible(warningvisible);
                    examdo.setNormid(normid);
                    examdo.setScaleid(scaleid);
                    examdo.setTestlimit(testlimit);
                    examdo.setStatusflag(0);// 完成状态，0：未答题，1，学生答完题，11学生教师答完题，111学生教师家长答完题

                    if (ScaleUtils.isThirdAngleScale(scaleid))
                        if (ScaleUtils.isThirdAngleScale(scaleid)) {
                            String bzrid = stu.get("teacherid") != null ? stu.get("teacherid").toString() : null;
                            examdo.setBzrid(bzrid);
                            String parentid = stu.get("parentid") != null ? stu.get("parentid").toString() : null;
                            examdo.setParentid(parentid);
                            examdo.setIfmh(true);
                        } else
                            examdo.setIfmh(false);

                    ind++;
                    examdo.setId(lastInsertId + ind);
                    examdoList.add(examdo);
                    if (examdoList.size() == 500 || currentIndex == rowCount - 1) {
                        if (examdoList.size() > 0) {
                            // long lastInsertId = examTaskDao.getMaxId();
                            int ii = examTaskDao.insertBatchExamdoStudent(examdoList);
                            if (ScaleUtils.isThreeAngleMentalHealthScaleForStudent(scaleid)) {
                                executeAssignIfMs1(examdoList);
                            }
                            lastInsertId += examdoList.size();
                            examdoList = new ArrayList<ExamdoStudent>();
                        }
                    }

                    // 统计分发人数
                    int count = 0;
                    statis = statisMap.get(scaleid);
                    if (statis == null)
                        count = 0;
                    else {
                        count = Integer.parseInt(statis.toString());
                    }
                    count++;
                    statisMap.put(scaleid, String.valueOf(count));

                    currentIndex++;
                }
            }
        }

    }

    private void storeTeacherExamdo(String[] scaleIds, Map<String, Object> paramets) throws Exception {
        Map resultMsgMap = (Map) paramets.get("result");
        List errorMsgList = (List) resultMsgMap.get("errorMsgList");
        Map statisMap = (Map) resultMsgMap.get("statisMap");
        List<ExamdoTeacher> examdoList = new ArrayList<ExamdoTeacher>();
        // Object teacherId = paramets.get(Constants.USER_ID_PROP);
        String[] teacherIds = (String[]) paramets.get(Constants.USER_ID_PROP);
        int rowCount = 0;
        int currentIndex = 0;
        int ind = 0;
        if (teacherIds == null) {
            List<Map> tchList = teacherDao.getBasicTeachers(paramets);
            rowCount = tchList.size() * scaleIds.length;
            for (int i = 0; i < scaleIds.length; i++) {
                String s = scaleIds[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                int testlimit = Integer.parseInt(scaleinfo[1]);
                int normid = Integer.parseInt(scaleinfo[2]);
                // 得到这批教师做此量表的限制情况
                Map<Long, Map> limitedExamdoMap = null;
                if (testlimit == 1) {
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    List<String> tids = new ArrayList<String>();
                    for (Map tch : tchList) {
                        tids.add(tch.get("userid").toString());
                    }
                    pMap.put("teacherids", tids);
                    pMap.put("scaleid", scaleid);
                    pMap.put("starttime", paramets.get("starttime").toString());
                    List<Map> limitedExamdoList = examTaskDao.queryLimitedTeacherExamdo(pMap);
                    limitedExamdoMap = new HashMap<Long, Map>();
                    for (Map limitedExamdo : limitedExamdoList) {
                        limitedExamdoMap.put((Long) limitedExamdo.get("userid"), limitedExamdo);
                    }
                }
                for (Map tch : tchList) {
                    if (tch.size() > 2) {
                        ExamdoTeacher examdo = new ExamdoTeacher();
                        long uid = Long.parseLong(tch.get("userid").toString());
                        examdo.setTaskid(Long.parseLong(paramets.get("taskid").toString()));
                        examdo.setTaskfrom(Integer.parseInt(paramets.get("taskfrom").toString()));
                        examdo.setOrgid(Long.parseLong(paramets.get("orgid").toString()));
                        examdo.setCountyid(paramets.get("countyid").toString());
                        examdo.setCityid(paramets.get("cityid").toString());
                        examdo.setRoleid(Integer.parseInt(tch.get("roleid").toString()));
                        examdo.setRolename(tch.get("rolename").toString());
                        examdo.setStarttime(paramets.get("loTime").toString());
                        examdo.setEndtime(paramets.get("hiTime").toString());
                        examdo.setUserid(uid);
                        examdo.setTestlimit(testlimit);
                        examdo.setScaleid(scaleid);
                        examdo.setNormid(normid);
    
                        Object statis = statisMap.get(scaleid);
                        if (statis == null)
                            statisMap.put(scaleid, "0");
                        if (testlimit == 1) {
                            Map limitedExamdo = limitedExamdoMap.get(uid);
                            if (limitedExamdo != null) {
                                // 将结果输出页面
                                String rolename = examdo.getRolename();
                                String xm = limitedExamdo.get("xm").toString();
                                String scaletitle = limitedExamdo.get("scaletitle").toString();
                                errorMsgList.add(scaletitle + "90天之内已被分到" + rolename + xm + ",不能再分发！");
                                continue;
                            }
                        }
                        examdoList.add(examdo);
                        if (examdoList.size() == 500 || currentIndex == rowCount - 1) {
                            if (examdoList.size() > 0) {
                                int ii = examTaskDao.insertBatchExamdoTeacher(examdoList);
                                examdoList = new ArrayList<ExamdoTeacher>();
                            }
                        }
                        // examTaskDao.insertTeacherExamDo(paramets);
                        // 统计分发人数
                        int count = 0;
                        statis = statisMap.get(scaleid);
                        if (statis == null)
                            count = 0;
                        else {
                            count = Integer.parseInt(statis.toString());
                        }
                        count++;
                        statisMap.put(scaleid, String.valueOf(count));
                        // long resultid =
                        // Integer.parseInt(paramets.get("id").toString());
                        currentIndex++;
                    }

                }
            }
        } else {
            rowCount = teacherIds.length * scaleIds.length;
            // long lastInsertId = examTaskDao.getTeacherMaxId();
            for (int i = 0; i < scaleIds.length; i++) {
                String s = scaleIds[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                int testlimit = Integer.parseInt(scaleinfo[1]);
                int normid = Integer.parseInt(scaleinfo[2]);
                // 得到这批教师做此量表的限制情况
                Map<Long, Map> limitedExamdoMap = null;
                if (testlimit == 1) {
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("teacherids", teacherIds);
                    pMap.put("scaleid", scaleid);
                    pMap.put("starttime", paramets.get("starttime").toString());
                    List<Map> limitedExamdoList = examTaskDao.queryLimitedTeacherExamdo(pMap);
                    limitedExamdoMap = new HashMap<Long, Map>();
                    for (Map limitedExamdo : limitedExamdoList) {
                        limitedExamdoMap.put((Long) limitedExamdo.get("userid"), limitedExamdo);
                    }
                }
                for (int j = 0; j < teacherIds.length; j++) {
                    ExamdoTeacher examdo = new ExamdoTeacher();
                    examdo.setTaskid(Long.parseLong(paramets.get("taskid").toString()));
                    examdo.setTaskfrom(Integer.parseInt(paramets.get("taskfrom").toString()));
                    examdo.setOrgid(Long.parseLong(paramets.get("orgid").toString()));
                    examdo.setCountyid(paramets.get("countyid").toString());
                    examdo.setCityid(paramets.get("cityid").toString());
                    examdo.setRoleid(Integer.parseInt(paramets.get("roleid").toString()));
                    examdo.setRolename(paramets.get("rolename").toString());
                    examdo.setStarttime(paramets.get("loTime").toString());
                    examdo.setEndtime(paramets.get("hiTime").toString());
                    examdo.setTestlimit(testlimit);
                    examdo.setScaleid(scaleid);
                    examdo.setNormid(normid);
                    long uid = Long.parseLong(teacherIds[j]);
                    examdo.setUserid(uid);

                    Object statis = statisMap.get(scaleid);
                    if (statis == null)
                        statisMap.put(scaleid, "0");
                    if (testlimit == 1) {
                        Map limitedExamdo = limitedExamdoMap.get(uid);
                        if (limitedExamdo != null) {
                            {
                                // 将结果输出页面
                                String rolename = examdo.getRolename();
                                String xm = limitedExamdo.get("xm").toString();
                                String scaletitle = limitedExamdo.get("scaletitle").toString();
                                errorMsgList.add(scaletitle + "90天之内已被分到" + rolename + xm + ",不能再分发！");
                                continue;
                            }
                        }
                    }
                    examdoList.add(examdo);
                    if (examdoList.size() == 500 || currentIndex == rowCount - 1) {
                        if (examdoList.size() > 0) {
                            int ii = examTaskDao.insertBatchExamdoTeacher(examdoList);
                            examdoList = new ArrayList<ExamdoTeacher>();
                        }
                    }
                    // 统计分发人数
                    int count = 0;
                    statis = statisMap.get(scaleid);
                    if (statis == null)
                        count = 0;
                    else {
                        count = Integer.parseInt(statis.toString());
                    }
                    count++;
                    statisMap.put(scaleid, String.valueOf(count));
                    currentIndex++;
                }
            }
        }
    }

    /*
     * private void storeTeacherExamdo(String[] scaleIds,Map<String, Object>
     * paramets) throws Exception { Map resultMsgMap =
     * (Map)paramets.get("result"); List errorMsgList =
     * (List)resultMsgMap.get("errorMsgList"); Map statisMap =
     * (Map)resultMsgMap.get("statisMap");
     * 
     * Object teacherId = paramets.get(Constants.USER_ID_PROP); if(teacherId ==
     * null){ List<Long>tchList = teacherDao.getTeacherIds(paramets); for (Long
     * id : tchList) { paramets.put("userId", id.longValue()); for (int
     * i=0;i<scaleIds.length;i++) { String s = scaleIds[i]; String[] scaleinfo =
     * s.split("_"); String scaleid = scaleinfo[0]; int testlimit =
     * Integer.parseInt(scaleinfo[1]); //判断量表是否被做过（90天之内） Map<String,Object>
     * pMap = new HashMap<String,Object>(); pMap.put("teacherid",id);
     * pMap.put("scaleid", scaleid); pMap.put("starttime",
     * paramets.get("starttime").toString());
     * 
     * Object statis = statisMap.get(scaleid); if(statis ==null)
     * statisMap.put(scaleid,"0");
     * 
     * List<Map> limitedExamdoMap = examTaskDao.queryLimitedTeacherExamdo(pMap);
     * if(testlimit==1){ if(limitedExamdoMap!=null){ Map limitedExamdo =
     * limitedExamdoMap.get(0); if(limitedExamdo!=null){ long userid =
     * Long.parseLong(limitedExamdo.get("userid").toString()); if(userid ==
     * id)//当前用户已经在90天之内做过此量表 { //将结果输出页面 String rolename =
     * limitedExamdo.get("rolename").toString(); String xm =
     * limitedExamdo.get("xm").toString(); String scaletitle =
     * limitedExamdo.get("scaletitle").toString();
     * errorMsgList.add(scaletitle+"90天之内已被分到"+rolename+xm+",不能再分发！"); continue;
     * } } } } //paramets.put("warnvisible", warningvisible);
     * paramets.put("testlimit", testlimit); paramets.put("scaleId", scaleid);
     * 
     * examTaskDao.insertTeacherExamDo(paramets); //统计分发人数 int count = 0; statis
     * = statisMap.get(scaleid); if(statis ==null) count =0; else{ count =
     * Integer.parseInt(statis.toString()); } count++;
     * statisMap.put(scaleid,String.valueOf(count)); //long resultid =
     * Integer.parseInt(paramets.get("id").toString()); } } } else{
     * paramets.put("userId", Long.parseLong(teacherId.toString())); String[]
     * scales = (String[]) paramets.get("scaleIds");
     * 
     * 
     * for (int i=0;i<scales.length;i++) { String s = scales[i]; String[]
     * scaleinfo = s.split("_"); String scaleid = scaleinfo[0]; int testlimit =
     * Integer.parseInt(scaleinfo[1]); paramets.put("testlimit", testlimit);
     * paramets.put("scaleId", scaleid);
     * 
     * Map<String,Object> pMap = new HashMap<String,Object>();
     * pMap.put("teacherid",Integer.parseInt(teacherId.toString()));
     * pMap.put("scaleid", scaleid);
     * pMap.put("starttime",paramets.get("starttime").toString() ); Object
     * statis = statisMap.get(scaleid); if(statis ==null)
     * statisMap.put(scaleid,"0");
     * 
     * List<Map> limitedExamdoMap = examTaskDao.queryLimitedTeacherExamdo(pMap);
     * if(testlimit==1){ if(limitedExamdoMap!=null){ Map limitedExamdo =
     * limitedExamdoMap.get(0); if(limitedExamdo!=null){ String userid =
     * limitedExamdo.get("userid").toString();
     * if(userid.equals(teacherId.toString()))//当前用户已经在90天之内做过此量表 { //将结果输出页面
     * String rolename = limitedExamdo.get("rolename").toString(); String xm =
     * limitedExamdo.get("xm").toString(); String scaletitle =
     * limitedExamdo.get("scaletitle").toString();
     * errorMsgList.add(scaletitle+"90天之内已被分到"+rolename+xm+",不能再分发！"); continue;
     * } } } } paramets.put("testlimit", testlimit);
     * examTaskDao.insertTeacherExamDo(paramets);
     * 
     * //统计分发人数 int count = 0; statis = statisMap.get(scaleid); if(statis
     * ==null) count =0; else{ count = Integer.parseInt(statis.toString()); }
     * count++; statisMap.put(scaleid,String.valueOf(count)); } } }
     */
    public class AssignStuResultHandler implements ResultHandler {
        final Map<String, Object> paramets;

        public AssignStuResultHandler(Map<String, Object> paramets) {
            this.paramets = paramets;
        }

        @Override
        public void handleResult(ResultContext ctx) {
            // TODO Auto-generated method stub
            Integer id = (Integer) ctx.getResultObject();
            paramets.put("userId", id.intValue());
            String[] scales = (String[]) paramets.get("scaleIds");

            for (int i = 0; i < scales.length; i++) {
                String s = scales[i];
                String[] scaleinfo = s.split("_");
                String scaleid = scaleinfo[0];
                int parentvisible = Integer.parseInt(scaleinfo[1]);
                int teachervisible = Integer.parseInt(scaleinfo[2]);
                int studentvisible = Integer.parseInt(scaleinfo[3]);
                int warningvisible = Integer.parseInt(scaleinfo[4]);
                int testlimit = Integer.parseInt(scaleinfo[5]);
                paramets.put("parentvisible", parentvisible);
                paramets.put("teachervisible", teachervisible);
                paramets.put("studentvisible", studentvisible);
                paramets.put("warnvisible", warningvisible);
                paramets.put("testlimit", testlimit);
                paramets.put("scaleId", scaleid);
                paramets.put("scaleId", scaleid);
                examTaskDao.insertStudentExamDo(paramets);

            }

        }

    }

    public class AssignTeaResultHandler implements ResultHandler {
        final Map<String, Object> paramets;

        public AssignTeaResultHandler(Map<String, Object> paramets) {
            this.paramets = paramets;
        }

        @Override
        public void handleResult(ResultContext ctx) {
            // TODO Auto-generated method stub
            Integer id = (Integer) ctx.getResultObject();
            paramets.put("teacherId", id.intValue());
            int[] scales = (int[]) paramets.get("scaleIds");

            for (int i = 0; i < scales.length; i++) {
                int scaleid = scales[i];
                paramets.put(Constants.SCALEID_PROP, scaleid);
                examTaskDao.insertTeacherExamDo(paramets);

            }

        }

    }

    public class AssignSchoolResultHandler implements ResultHandler {
        final Map<String, Object> paramets;

        public AssignSchoolResultHandler(Map<String, Object> paramets) {
            this.paramets = paramets;
        }

        @Override
        public void handleResult(ResultContext ctx) {
            // TODO Auto-generated method stub
            Organization school = (Organization) ctx.getResultObject();
            paramets.put("orgid", school.getId());
            paramets.put("cityid", school.getCityid());
            paramets.put("countyid", school.getCountyid());
            examTaskDao.insertExamDoTaskForEduSchool(paramets);
        }

    }

}
