package com.njpes.www.service.assessmentcenter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.assessmentcenter.InvestExamMapper;
import com.njpes.www.dao.assessmentcenter.InvestResultMapper;
import com.njpes.www.dao.assessmentcenter.InvestStatMapper;
import com.njpes.www.dao.assessmentcenter.InvestTaskUserMapper;
import com.njpes.www.dao.assessmentcenter.InvestdoEduMapper;
import com.njpes.www.dao.assessmentcenter.InvestdoSchoolMapper;
import com.njpes.www.dao.assessmentcenter.InvesttaskEduSchoolMapper;
import com.njpes.www.dao.assessmentcenter.InvesttaskMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.entity.assessmentcenter.InvestExam;
import com.njpes.www.entity.assessmentcenter.InvestResult;
import com.njpes.www.entity.assessmentcenter.InvestStat;
import com.njpes.www.entity.assessmentcenter.InvestTaskUser;
import com.njpes.www.entity.assessmentcenter.InvestdoEdu;
import com.njpes.www.entity.assessmentcenter.InvestdoSchool;
import com.njpes.www.entity.assessmentcenter.InvesttaskEduSchool;
import com.njpes.www.entity.assessmentcenter.InvesttaskWithBLOBs;
import com.njpes.www.entity.assessmentcenter.MyInvesttask;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.InvestDispatcherFilterParam;
//import com.njpes.www.entity.scaletoollib.ExamDoSchoolTask;
import com.njpes.www.invoker.ExamInvoker;
import com.njpes.www.service.baseinfo.ClassServiceI;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.PageParameter;

import edu.emory.mathcs.backport.java.util.Arrays;
import edutec.scale.digester.ScaleBuilder;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.Scale;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.QuestionBlock;
import edutec.scale.questionnaire.Questionnaire;
import heracles.util.Pools;
import jodd.io.StringInputStream;

@Service("investService")
public class InvestServiceImpl implements InvestServiceI {

    @Autowired
    private InvestExamMapper investMapper;
    @Autowired
    ExamInvoker examInvoker;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private InvesttaskMapper investTaskMapper;
    @Autowired
    private InvestResultMapper investResultMapper;
    @Autowired
    private InvestdoSchoolMapper investdoSchoolMapper;
    @Autowired
    private InvesttaskEduSchoolMapper investtaskEduSchoolMapper;
    @Autowired
    private InvestdoEduMapper investdoEdulMapper;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private ClassServiceI classService;
    @Autowired
    private ClassSchoolMapper classSchoolMapper;
    @Autowired
    private InvestResultMapper investResultMappaer;
    @Autowired
    private InvestTaskUserMapper investTaskUserMapper;
    @Autowired
    private InvestStatMapper investStatMapper;
    @Autowired
    private OrganizationMapper organizationMaper;

    @Override
    public List<InvestExam> getAllInvestPage(InvestExam ie, PageParameter page) {
        // TODO Auto-generated method stub
        return investMapper.selectAllInvestByPage(ie, page);
    }

    public void insertInvestExam(InvestExam ie) {
        investMapper.insert(ie);
    }

    public void delInvests(List ids) {
        investMapper.deleteByPrimaryKeys(ids);
    }

    @Override
    public void delInvest(String id) {
        // TODO Auto-generated method stub
        investMapper.deleteByPrimaryKey(Integer.parseInt(id));
    }

    public void investdone(long userid, long taskid, int scaleid, String answer) {
        InvestResult investResult = new InvestResult();
        investResult.setTaskid(taskid);
        investResult.setUserId(userid);
        investResult.setInvestId(scaleid);
        investResult.setAnswer(answer);
        investResultMapper.insert(investResult);
    }

    @Override
    public InvestExam selectByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return investMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeyWithBLOBs(InvestExam ie) {
        // TODO Auto-generated method stub
        investMapper.updateByPrimaryKeyWithBLOBs(ie);
    }

    @Override
    public Scale getScaleFromInvestExam(InvestExam investExam) {
        InputStream input = null;
        try {
            Scale scale = new Scale();
            scale.setCode(investExam.getId().toString());
            scale.setTitle(investExam.getName());
            String xmlStr = investExam.getXmlstr();
            xmlStr = xmlStr.replaceAll("&", "&amp;");
            input = new StringInputStream(xmlStr);
            ScaleBuilder.doInvestBuild(input, scale);
            return scale;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
        }
        return null;
    }

    @Override
    public void schooldistribute(long orgid, int objecttype, int scaleid, long ownerid, String taskname,
            String starttime, String endtime, String createtime, String[] gradeClassids, String[] teacherroleIds,
            List resultMsgList) {
        starttime += " 00:00:00";
        endtime += " 23:59:59";
        StringBuilder sb = null;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if (objecttype == 1) {// 分发对象是学生
                // 这里插入分发任务表记录
                InvesttaskWithBLOBs investTask = new InvesttaskWithBLOBs();
                investTask.setObjecttype(objecttype);
                investTask.setName(taskname);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                investTask.setCreatetime(new Date());
                investTask.setStarttime(df.parse(starttime));
                investTask.setEndtime(df.parse(endtime));
                investTask.setCreaterOrgid(orgid);
                investTask.setOwnerid(ownerid);
                investTask.setScaleid(scaleid);
                StringBuilder sbbj = new StringBuilder();
                sb = Pools.getInstance().borrowStringBuilder();
                for (int i = 0; i < gradeClassids.length; i++) {
                    String gcs = gradeClassids[i];
                    String[] xd_bj = gcs.split(",");
                    String gradeid = xd_bj[0];
                    String gradename = AgeUitl.getGradeName(Integer.parseInt(gradeid));
                    String bj = xd_bj[1];
                    sbbj.append(bj);
                    sbbj.append(",");
                    // ClassSchool cs =
                    // classService.selectByPrimaryKey(Integer.parseInt(bj));
                    String[] bj_names = bj.split(":");
                    String bjname = bj_names[1];
                    String classname = gradename + bjname;
                    sb.append(classname);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setBjnames(sb.toString());
                sbbj.deleteCharAt(sbbj.length() - 1);
                investTask.setBjids(sbbj.toString());

                int result = investTaskMapper.insert(investTask);
                long taskid = investTask.getId();
                List<InvestdoSchool> investdoList = new ArrayList<InvestdoSchool>();
                for (int i = 0; i < gradeClassids.length; i++) {
                    InvestdoSchool investdo = new InvestdoSchool();
                    investdo.setTaskid(taskid);
                    investdo.setOrgid(orgid);
                    investdo.setScaleid(scaleid);
                    investdo.setStarttime(df.parse(starttime));
                    investdo.setEndtime(df.parse(endtime));
                    investdo.setObjecttype(objecttype);

                    String gcs = gradeClassids[i];
                    String[] xd_bj = gcs.split(",");
                    String gradeid = xd_bj[0];
                    int nj = AgeUitl.getNj(Integer.parseInt(gradeid));
                    String bj = xd_bj[1];
                    String[] bjinfo = bj.split(":");
                    String bjid = bjinfo[0];
                    investdo.setGradeid(Integer.parseInt(gradeid));
                    // paramets.put("nj", nj);
                    // 分发对象学生的班级
                    investdo.setBjid((long) Integer.parseInt(bjid));
                    // 班号

                    // 开始时间、结束时间和限期
                    // paramets.put(Constants.LIMIT_FLAG_PROP, flag);
                    // paramets.put("result", resultMsgList);
                    investdoList.add(investdo);
                    // investdoSchoolMapper.insert(investdo);
                    if (investdoList.size() == 500 || i == gradeClassids.length) {
                        if (investdoList.size() > 0) {
                            investdoSchoolMapper.insertBatch(investdoList);
                            investdoList = new ArrayList<InvestdoSchool>();
                        }
                    }
                }

                createInvestStat(taskid, scaleid);

            } else if (objecttype == 2) {// 分发对象是教师
                InvesttaskWithBLOBs investTask = new InvesttaskWithBLOBs();
                investTask.setObjecttype(objecttype);
                investTask.setName(taskname);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                investTask.setCreatetime(new Date());
                investTask.setStarttime(df.parse(starttime));
                investTask.setEndtime(df.parse(endtime));
                investTask.setCreaterOrgid(orgid);
                investTask.setOwnerid(ownerid);
                investTask.setScaleid(scaleid);

                sb = Pools.getInstance().borrowStringBuilder();
                sb.setLength(0);
                for (int i = 0; i < teacherroleIds.length; i++) {
                    String roleidStr = teacherroleIds[i];
                    Role r = roleService.selectRole(Integer.parseInt(roleidStr));
                    sb.append(r.getRoleName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setRolenames(sb.toString());

                sb.setLength(0);
                for (int i = 0; i < teacherroleIds.length; i++) {
                    String roleidStr = teacherroleIds[i];
                    Role r = roleService.selectRole(Integer.parseInt(roleidStr));
                    sb.append(r.getId());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setRoleids(sb.toString());

                investTaskMapper.insert(investTask);
                long taskid = investTask.getId();
                List<InvestdoSchool> investdoList = new ArrayList<InvestdoSchool>();
                for (int i = 0; i < teacherroleIds.length; i++) {
                    InvestdoSchool investdo = new InvestdoSchool();
                    investdo.setTaskid(taskid);
                    investdo.setOrgid(orgid);
                    investdo.setScaleid(scaleid);
                    investdo.setStarttime(df.parse(starttime));
                    investdo.setEndtime(df.parse(endtime));
                    investdo.setObjecttype(objecttype);
                    int roleid = Integer.parseInt(teacherroleIds[i]);
                    investdo.setRoleid(roleid);
                    // investdoSchoolMapper.insert(investdo);
                    investdoList.add(investdo);
                    // investdoSchoolMapper.insert(investdo);
                    if (investdoList.size() == 500 || i == gradeClassids.length) {
                        if (investdoList.size() > 0) {
                            investdoSchoolMapper.insertBatch(investdoList);
                            investdoList = new ArrayList<InvestdoSchool>();
                        }
                    }
                }
                createInvestStat(taskid, scaleid);
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            // System.out.println("=============================================错误");
        } finally {
            // txManager.rollback(status);
        }

    }

    @Override
    public void eduDistributeForStu(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String[] gradeIds, int scaleid,
            String[] schoolIds, List resultMsgList) {
        starttime += " 00:00:00";
        endtime += " 23:59:59";
        StringBuilder sb = null;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 这里插入分发任务表记录
            InvesttaskWithBLOBs investTask = new InvesttaskWithBLOBs();
            investTask.setName(taskname);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
            investTask.setCreatetime(new Date());
            investTask.setStarttime(df.parse(starttime));
            investTask.setEndtime(df.parse(endtime));
            investTask.setCreaterOrgid(creater_orgid);
            investTask.setOwnerid(createuserid);
            investTask.setScaleid(scaleid);
            investTask.setObjecttype(1);
            sb = Pools.getInstance().borrowStringBuilder();
            if (areaIds != null) {
                sb.setLength(0);
                // sb =Pools.getInstance().borrowStringBuilder();
                for (int i = 0; i < areaIds.length; i++) {
                    District district = districtService.selectByCode(areaIds[i]);
                    sb.append(district.getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setAreanames(sb.toString());
            }
            if (orgIds != null) {
                sb.setLength(0);
                for (int i = 0; i < orgIds.length; i++) {
                    sb.append(orgIds[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setOrgids(sb.toString());

                sb.setLength(0);
                for (int i = 0; i < orgIds.length; i++) {
                    // long schoolid = Integer.parseInt(schoolIds[i]);
                    Organization org = organizationService.selectOrganizationById(orgIds[i]);
                    sb.append(org.getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setSchoolnames(sb.toString());
            }
            if (gradeIds != null) {
                sb.setLength(0);
                // sb =Pools.getInstance().borrowStringBuilder();
                for (int i = 0; i < gradeIds.length; i++) {
                    String gradeStr = gradeIds[i];
                    sb.append(gradeStr);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setGradeids(sb.toString());
            }
            if (gradeIds != null) {
                sb.setLength(0);
                // sb =Pools.getInstance().borrowStringBuilder();
                for (int i = 0; i < gradeIds.length; i++) {
                    String gradeStr = gradeIds[i];
                    String gradename = AgeUitl.getGradeName(Integer.parseInt(gradeStr));
                    sb.append(gradename);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setNjname(sb.toString());
            }
            // 直属学校
            if (schoolIds != null) {
                sb.setLength(0);
                for (int i = 0; i < schoolIds.length; i++) {
                    long schoolid = Integer.parseInt(schoolIds[i]);
                    Organization org = organizationService.selectOrganizationById(schoolid);
                    sb.append(org.getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setSchoolnames(sb.toString());
            }

            int result = investTaskMapper.insert(investTask);
            long taskid = investTask.getId();
            if (gradeIds != null) {
                sb.setLength(0);
                for (int i = 0; i < gradeIds.length; i++) {
                    sb.append(gradeIds[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }

            InvestdoEdu investdoEdu = null;
            List<InvesttaskEduSchool> investtaskEduSchoolList = new ArrayList<InvesttaskEduSchool>();
            // 市、县教委分发
            if (orgIds != null && schoolIds == null) {
                sb.setLength(0);
                List areaids = Arrays.asList(areaIds);
                List<Organization> schoolList = organizationMaper.getSchoolOrgByCountyIds(areaids);

                for (int i = 0; i < schoolList.size(); i++) {
                    InvesttaskEduSchool investtaskEduSchool = new InvesttaskEduSchool();
                    Organization school = schoolList.get(i);
                    investtaskEduSchool.setTaskid(taskid);
                    investtaskEduSchool.setSchoolorgid(school.getId());
                    investtaskEduSchool.setDispenseOrgid(school.getParentId());
                    investtaskEduSchool.setIsfinished(false);
                    investtaskEduSchoolList.add(investtaskEduSchool);
                    // investtaskEduSchoolMapper.insert(investtaskEduSchool);
                }
                if (investtaskEduSchoolList.size() > 0)
                    investtaskEduSchoolMapper.insertBatch(investtaskEduSchoolList);
            }
            // 直属学校
            if (schoolIds != null) {
                for (int i = 0; i < schoolIds.length; i++) {
                    InvesttaskEduSchool investtaskEduSchool = new InvesttaskEduSchool();
                    long schoolid = Integer.parseInt(schoolIds[i]);
                    investtaskEduSchool.setTaskid(taskid);
                    investtaskEduSchool.setSchoolorgid(schoolid);
                    investtaskEduSchool.setDispenseOrgid(schoolid);
                    investtaskEduSchool.setIsfinished(false);
                    investtaskEduSchoolList.add(investtaskEduSchool);
                    // investtaskEduSchoolMapper.insert(investtaskEduSchool);
                }
                if (investtaskEduSchoolList.size() > 0)
                    investtaskEduSchoolMapper.insertBatch(investtaskEduSchoolList);
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
        } finally {
            Pools.getInstance().returnStringBuilder(sb);
        }
    }

    @Override
    public void eduDistributeForTch(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String[] teacherroleIds, int scaleid,
            String[] schoolIds, List resultMsgList) {
        // TODO Auto-generated method stub
        starttime += " 00:00:00";
        endtime += " 23:59:59";
        StringBuilder sb = null;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 这里插入分发任务表记录
            InvesttaskWithBLOBs investTask = new InvesttaskWithBLOBs();
            investTask.setName(taskname);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
            investTask.setCreatetime(new Date());
            investTask.setStarttime(df.parse(starttime));
            investTask.setEndtime(df.parse(endtime));
            investTask.setCreaterOrgid(creater_orgid);
            investTask.setOwnerid(createuserid);
            investTask.setScaleid(scaleid);
            investTask.setObjecttype(2);
            sb = Pools.getInstance().borrowStringBuilder();
            if (areaIds != null) {
                sb.setLength(0);
                // sb =Pools.getInstance().borrowStringBuilder();
                for (int i = 0; i < areaIds.length; i++) {
                    District district = districtService.selectByCode(areaIds[i]);
                    sb.append(district.getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setAreanames(sb.toString());
            }
            // 直属学校
            if (schoolIds != null) {
                sb.setLength(0);
                for (int i = 0; i < schoolIds.length; i++) {
                    long schoolid = Integer.parseInt(schoolIds[i]);
                    Organization org = organizationService.selectOrganizationById(schoolid);
                    sb.append(org.getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setSchoolnames(sb.toString());
            }
            if (orgIds != null) {
                sb.setLength(0);
                for (int i = 0; i < orgIds.length; i++) {
                    sb.append(orgIds[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setOrgids(sb.toString());
            }
            if (teacherroleIds != null) {
                sb.setLength(0);
                // sb =Pools.getInstance().borrowStringBuilder();
                for (int i = 0; i < teacherroleIds.length; i++) {
                    String r = teacherroleIds[i];
                    Role role = roleService.selectRole(Integer.parseInt(r));
                    if (role == null)
                        continue;
                    String rolename = role.getRoleName();
                    sb.append(rolename);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                investTask.setRolenames(sb.toString());
            }
            int result = investTaskMapper.insert(investTask);
            long taskid = investTask.getId();

            if (teacherroleIds != null) {
                // sbrole = Pools.getInstance().borrowStringBuilder();
                sb.setLength(0);
                for (int i = 0; i < teacherroleIds.length; i++) {
                    sb.append(teacherroleIds[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }

            InvestdoEdu investdoEdu = null;
            List<InvesttaskEduSchool> investtaskEduSchoolList = new ArrayList<InvesttaskEduSchool>();
            // 市、县教委分发
            if (orgIds != null && schoolIds == null) {
                /*
                 * for(int i=0;i<orgIds.length;i++){ InvesttaskEduSchool
                 * investtaskEduSchool=new InvesttaskEduSchool();
                 * investtaskEduSchool.setTaskid(taskid);
                 * investtaskEduSchool.setSchoolorgid(orgIds[i]);
                 * investtaskEduSchool.setDispenseOrgid(orgIds[i]);
                 * investtaskEduSchool.setIsfinished(false);
                 * investtaskEduSchoolList.add(investtaskEduSchool); }
                 */

                List areaids = Arrays.asList(areaIds);
                List<Organization> schoolList = organizationMaper.getSchoolOrgByCountyIds(areaids);

                for (int i = 0; i < schoolList.size(); i++) {
                    InvesttaskEduSchool investtaskEduSchool = new InvesttaskEduSchool();
                    Organization school = schoolList.get(i);
                    investtaskEduSchool.setTaskid(taskid);
                    investtaskEduSchool.setSchoolorgid(school.getId());
                    investtaskEduSchool.setDispenseOrgid(school.getParentId());
                    investtaskEduSchool.setIsfinished(false);
                    investtaskEduSchoolList.add(investtaskEduSchool);
                }

                if (investtaskEduSchoolList.size() > 0)
                    investtaskEduSchoolMapper.insertBatch(investtaskEduSchoolList);
            }
            // 直属学校
            if (schoolIds != null) {
                for (int i = 0; i < schoolIds.length; i++) {
                    InvesttaskEduSchool investtaskEduSchool = new InvesttaskEduSchool();
                    long schoolid = Integer.parseInt(schoolIds[i]);
                    investtaskEduSchool.setTaskid(taskid);
                    investtaskEduSchool.setSchoolorgid(schoolid);
                    investtaskEduSchool.setDispenseOrgid(schoolid);
                    investtaskEduSchool.setIsfinished(false);
                    investtaskEduSchoolList.add(investtaskEduSchool);
                    // investtaskEduSchoolMapper.insert(investtaskEduSchool);
                }
                if (investtaskEduSchoolList.size() > 0)
                    investtaskEduSchoolMapper.insertBatch(investtaskEduSchoolList);
            }
            if (investdoEdu != null)
                investdoEdulMapper.insert(investdoEdu);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
        } finally {
            Pools.getInstance().returnStringBuilder(sb);
        }
    }

    @Override
    public List<InvesttaskWithBLOBs> getAllInvestTaskForSchoolPage(
            InvestDispatcherFilterParam investDispatcherFilterParam, PageParameter page) {
        return investTaskMapper.selectAllInvestTaskForSchoolByPage(investDispatcherFilterParam, page);
    }

    @Override
    public List<InvesttaskWithBLOBs> getAllInvestTaskForEduPage(InvestDispatcherFilterParam investDispatcherFilterParam,
            PageParameter page) {
        return investTaskMapper.selectAllInvestTaskForEduByPage(investDispatcherFilterParam, page);
    }

    @Override
    public List<MyInvesttask> getInvestdoByPage(InvestDispatcherFilterParam investDispatcherFilterParam,
            PageParameter page) {
        return investdoSchoolMapper.getInvestdoByPage(investDispatcherFilterParam, page);
    }

    @Override
    public MyInvesttask getInvestdoById(String id) {
        return investdoSchoolMapper.getInvestdoById(id);
    }

    @Override
    public List<InvesttaskWithBLOBs> requireStatus(List<InvesttaskWithBLOBs> itsList, long orgid, long ownerid) {
        List<Integer> taskIdList = this.investtaskEduSchoolMapper.getDistributableTask(orgid, false);
        // 判断是否下发
        if (itsList == null)
            return null;
        Date now = new Date();
        for (int i = 0; i < itsList.size(); i++) {
            InvesttaskWithBLOBs it = itsList.get(i);
            long id = it.getId();
            if (taskIdList.contains(id)) {
                it.setXfflag(1);
            } else {
                it.setXfflag(0);
            }
            Date endtime = it.getEndtime();
            if (endtime.after(now)) {// 如果没有过期
                it.setExpireflag(0);
            } else
                it.setExpireflag(1);
            // 判断是否可以撤回
            boolean result = this.checkProcessingStatus(id, ownerid);
            if (result) {
                // 可以撤回
                it.setChflag(1);
            } else {
                // 不可以撤回
                it.setChflag(0);
            }
        }
        return itsList;
    }

    @Override
    public boolean checkProcessingStatus(long taskid, long ownerid) {
        Date starttime = investTaskMapper.checkProcessingStatus(taskid, ownerid);
        if (starttime == null)
            return false;
        Date nowdate = new Date();
        // 如果现在的时间已经在开始时间之后
        if (nowdate.after(starttime))
            return false;
        return true;
    }

    @Override
    public void withdraw(long itid, String stype) {
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if (stype.equals("sch")) {
                // 删除教委给学生or老师发的task
                investTaskMapper.deleteByPrimaryKey((long) itid);
                // 删除教委给学生or老师发的result
                investdoSchoolMapper.deleteByTaskid((long) itid);
            }
            if (stype.equals("edu")) {
                // 删除教委给学生or老师发的task
                investTaskMapper.deleteByPrimaryKey((long) itid);
                // 删除教委给学生or老师发的result
                investtaskEduSchoolMapper.deleteByTaskid((long) itid);
            }
            txManager.commit(status);
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
        }
    }

    @Override
    public String investTransfer(long itid, long dispense_orgid, String stype) {
        String result = "success";
        InvesttaskWithBLOBs investtask = investTaskMapper.selectByPrimaryKey(itid);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if (stype.equals("sch")) {
                String gradeids = investtask.getGradeids();
                String[] gradeList = gradeids.split(",");
                List investdoList = new ArrayList();
                for (int i = 0; i < gradeList.length; i++) {
                    int gradeid = Integer.parseInt(gradeList[i]);
                    List<ClassSchool> classList = classSchoolMapper.selectClassByGradeIdInSchool(dispense_orgid,
                            gradeid, 0);
                    for (int j = 0; j < classList.size(); j++) {
                        ClassSchool cs = classList.get(j);
                        long bjid = cs.getId();

                        InvestdoSchool investdo = new InvestdoSchool();
                        investdo.setTaskid(itid);
                        investdo.setOrgid(dispense_orgid);
                        investdo.setScaleid(investtask.getScaleid());
                        investdo.setStarttime(investtask.getStarttime());
                        investdo.setEndtime(investtask.getEndtime());
                        investdo.setObjecttype(investtask.getObjecttype());
                        investdo.setGradeid(gradeid);
                        investdo.setBjid(bjid);
                        investdoList.add(investdo);
                        // investdoSchoolMapper.insert(investdo);
                    }
                }
                if (investdoList.size() > 0)
                    investdoSchoolMapper.insertBatch(investdoList);

                InvesttaskEduSchool investtaskEduSchool = new InvesttaskEduSchool();
                investtaskEduSchool.setTaskid(investtask.getId());
                investtaskEduSchool.setSchoolorgid(dispense_orgid);
                investtaskEduSchool.setDispenseOrgid(dispense_orgid);
                investtaskEduSchool.setIsfinished(true);
                investtaskEduSchoolMapper.distributeTransferBySchool(investtaskEduSchool);

            }
            if (stype.equals("edu")) {// 县教委转发，只需要更新分发单位为学校并且将isfinish字段设置为false
                InvesttaskEduSchool investtaskEduSchool = new InvesttaskEduSchool();
                investtaskEduSchool.setDispenseOrgid(dispense_orgid);
                investtaskEduSchool.setTaskid(investtask.getId());
                investtaskEduSchool.setIsfinished(false);
                investtaskEduSchoolMapper.distributeTransferByEdu(investtaskEduSchool);

            }
            txManager.commit(status);
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
            result = "fail";
        }
        return result;
    }

    @Override
    public List getInvestResult(long taskid) {

        return investStatMapper.getInvestStatByTaskId(taskid);

    }

    private void createInvestStat(long taskid, int scaleid) {
        InvestExam IE = selectByPrimaryKey(scaleid);
        String xmlStr = IE.getXmlstr();
        Scale scale = getScaleFromInvestExam(IE);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setScale(scale);
        questionnaire.openInvest();
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks();
        StringBuilder sb = new StringBuilder();
        List<InvestStat> investStatList = new ArrayList<InvestStat>();
        for (int i = 0; i < questionnaire.getQuestionSize(); i++) {
            QuestionBlock questionBlock = questions.get(i);
            if (questionBlock.getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                InvestStat investStat = new InvestStat();
                investStat.setTaskid(taskid);
                String qid = questionBlock.getDisplayId();
                qid = qid.replace("Q", "");
                investStat.setQid(Integer.parseInt(qid));
                investStat.setOpt0(0);
                investStat.setOpt1(0);
                investStat.setOpt2(0);
                investStat.setOpt3(0);
                investStat.setOpt4(0);
                investStat.setOpt5(0);
                investStat.setOpt6(0);
                investStat.setOpt7(0);
                investStat.setOpt8(0);
                investStat.setOpt9(0);
                investStatList.add(investStat);

                investStatMapper.insertBatch(investStatList);
                // investStatMapper.insert(investStat);
            }
        }
        if (investStatList.size() > 0)
            investStatMapper.insertBatch(investStatList);
    }

    @Override
    public void investdone(long userid, long taskid, int scaleid, Questionnaire questionnaire) {
        // TODO Auto-generated method stub
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            InvestTaskUser itUser = new InvestTaskUser();
            itUser.setTaskid(taskid);
            itUser.setUserid(userid);
            itUser.setOktime(new Date());
            investTaskUserMapper.insert(itUser);
            List<QuestionBlock> questions = questionnaire.getQuestionBlocks();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < questionnaire.getQuestionSize(); i++) {
                QuestionBlock questionBlock = questions.get(i);
                if (questionBlock.getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                    SelectionQuestion sQ = (SelectionQuestion) questionBlock.getQuestion();
                    if (sQ.getChoiceMode() == QuestionConsts.CHOICE_SINGLE_MODE)// 单选题
                    {
                        String qid = sQ.getId();
                        qid = qid.replace("Q", "");
                        String answerStr = questionBlock.getAnswer();
                        int answer = Integer.parseInt(answerStr);
                        InvestStat investStat = new InvestStat();
                        investStat.setTaskid(taskid);
                        investStat.setQid(Integer.parseInt(qid));
                        investStat.setOpt(answer);
                        investStatMapper.updateByTaskid(investStat);
                    }
                    if (sQ.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE)// 多选题，分数之和
                    {
                        String answerStr = questionBlock.getAnswer();
                        String qid = sQ.getId();
                        qid = qid.replace("Q", "");
                        InvestStat investStat = new InvestStat();
                        investStat.setTaskid(taskid);
                        investStat.setQid(Integer.parseInt(qid));
                        String[] optArray = answerStr.split(":");
                        for (int j = 0; j < optArray.length; j++) {
                            int answer = Integer.parseInt(optArray[j]);
                            investStat.setOpt(answer);
                        }
                        investStatMapper.updateByTaskid(investStat);
                    }

                }
            }
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        }
    }
}
