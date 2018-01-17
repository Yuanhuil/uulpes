package com.njpes.www.service.scaletoollib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.assessmentcenter.ExamdoMentalHealthMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.scaletoollib.ExamdoTaskEducommissionMapper;
import com.njpes.www.dao.scaletoollib.ExamdoTaskSchoolMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.DispatcherFilterParam;
import com.njpes.www.entity.scaletoollib.ExamdoTaskEducommission;
import com.njpes.www.entity.scaletoollib.ExamdoTaskSchool;
import com.njpes.www.entity.scaletoollib.ScaleProcessStruct;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.PageParameter;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;

@Service("schoolDispatcherService")
public class SchoolDispatcherServiceImpl implements SchoolDispatcherService {

    @Autowired
    private ExamdoTaskSchoolMapper schoolDispatcherMapper;
    @Autowired
    private ExamdoTaskEducommissionMapper eduDispatcherMapper;
    @Autowired
    private ExamdoMentalHealthMapper examMhMapper;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    OrganizationServiceI organizationService;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private ClassSchoolMapper classMapper;

    @Override
    public List<ExamdoTaskSchool> queryETS(HttpServletRequest request, int typeflag, PageParameter page) {
        // TODO Auto-generated method stub
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long userid = account.getId();
        List<AccountOrgJob> organizationJobs = account.getOrganizationJobs();
        if (organizationJobs == null || organizationJobs.size() == 0) {
            return null;
        }
        long orgid = organizationJobs.get(0).getOrgId();
        List<ExamdoTaskSchool> etsList = this.schoolDispatcherMapper.queryETSchoolByPage((int) userid, typeflag, page,
                orgid);
        return etsList;
    }

    @Override
    public boolean checkStudentProcessingStatus(int etschoolid) {
        // TODO Auto-generated method stub
        Date tempdate = this.schoolDispatcherMapper.checkStudentProcessingStatus(etschoolid);
        if (tempdate == null)
            return false;
        Date nowdate = new Date();
        if (nowdate.after(tempdate))
            return false;
        return true;
    }

    @Override
    public void deleteEtschoolid(int typeflag, int etschoolid) {
        // TODO Auto-generated method stub
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 删除学校给学生or老师发的task
            if (typeflag == 1) {
                this.schoolDispatcherMapper.deleteTaskSchoolById(etschoolid);
                // 删除学校给学生or老师发的result
                this.schoolDispatcherMapper.deleteResultSchoolStudentId(etschoolid);

                examMhMapper.deleteByPrimaryKey((long) etschoolid);
            }
            if (typeflag == 2) {
                this.schoolDispatcherMapper.deleteTaskSchoolById(etschoolid);
                // 删除学校给学生or老师发的result
                this.schoolDispatcherMapper.deleteResultSchoolTeacherId(etschoolid);
            }

            txManager.commit(status);
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
        }
    }

    @Override
    public List<ScaleProcessStruct> getStudentCheckProcessStatusForSchool(HttpServletRequest request, Organization org,
            int etschoolid, int taskfrom) {
        ArrayList<ScaleProcessStruct> tempList = new ArrayList<ScaleProcessStruct>();
        // 从session中取出Account
        String[] scaleids = null;
        String[] gradearray = null;
        String[] njarray = null;
        String[] bjarray = null;
        String[] njmcarray = null;
        String[] bjmcarray = null;
        if (taskfrom == 0) {// 本学校创建的任务
            ExamdoTaskSchool ets = this.queryETSAccordingId(etschoolid);
            if (ets == null)
                return null;
            scaleids = ets.getScaleids().split(",");
            njarray = ets.getNj().split(",");
            bjarray = ets.getBj().split(",");
            njmcarray = ets.getNjmc().split(",");
            bjmcarray = ets.getBjmc().split(",");
            // String[] orgarray = ets.getOrgid().split(",");
            for (int i = 0; i < scaleids.length; i++) {
                // for (int j = 0; j < orgarray.length; j++) {
                // for (int k = 0; k < njarray.length; k++) {
                for (int m = 0; m < bjarray.length; m++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    if (sdi == null)
                        continue;
                    sps.setScaleName(sdi.getTitle());
                    sps.setOrgid(org.getId());
                    sps.setOrgName(org.getName());
                    // sps.setNj(Integer.parseInt(njarray[k]));
                    sps.setBj(Integer.parseInt(bjarray[m]));
                    // sps.setGradeName(njmcarray[k]);
                    sps.setClassName(bjmcarray[m]);
                    sps.setTaskId(etschoolid);
                    sps.setTaskfrom(taskfrom);
                    tempList.add(sps);
                }
                // }
                // }
            }
        } else {// 上级教委分发的任务
            ExamdoTaskEducommission ete = eduDispatcherMapper.queryETEAccordingId(etschoolid);
            if (ete == null)
                return null;
            scaleids = ete.getScaleids().split(",");
            gradearray = ete.getGradeids().split(",");
            njarray = ete.getNj().split(",");
            njmcarray = ete.getNjmc().split(",");
            for (int i = 0; i < scaleids.length; i++) {
                for (int j = 0; j < njarray.length; j++) {
                    int gradeid = Integer.parseInt(gradearray[j]);
                    int nj = Integer.parseInt(njarray[j]);
                    List<ClassSchool> classList = classMapper.selectClassByGradeIdInSchool(org.getId(), gradeid, 0);
                    List<Integer> bjList = schoolDispatcherMapper.queryBjArrayFromExamdoStudent(org.getId(), etschoolid,
                            nj);
                    // for(int k=0;k<classList.size();k++){
                    String edcBjmc = "";
                    for (int n = 0; n < classList.size(); n++) {
                        if (n == classList.size() - 1) {
                            edcBjmc = edcBjmc + classList.get(n).getBjmc();
                        } else {
                            edcBjmc = classList.get(n).getBjmc() + "/" + edcBjmc;
                        }
                    }
                    for (int k = 0; k < bjList.size(); k++) {
                        Integer bjIn = bjList.get(k);
                        ScaleProcessStruct sps = new ScaleProcessStruct();
                        String[] scaleidss = scaleids[i].split("_");
                        String scaleid = scaleidss[0];
                        sps.setScaleid(scaleid);
                        Scale sdi = cachedScaleMgr.get(scaleid);
                        if (sdi == null)
                            continue;
                        sps.setScaleName(sdi.getTitle());
                        sps.setOrgid(org.getId());
                        sps.setOrgName(org.getName());
                        sps.setNj(gradeid);
                        sps.setNj(nj);
                        // sps.setBj(cs.getId());
                        sps.setBj(bjIn.intValue());
                        String gradename = AgeUitl.getGradeName(gradeid);
                        sps.setGradeName(gradename);
                        sps.setClassName(edcBjmc);
                        sps.setTaskId(etschoolid);
                        sps.setTaskfrom(taskfrom);
                        tempList.add(sps);
                    }
                }
            }
        }

        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        Set<Role> roleSet = account.getRoles();
        Iterator it = roleSet.iterator();
        // 获取权限最大的那个用户
        int max = 0;
        while (it.hasNext()) {
            Role role = (Role) it.next();
            int temp = role.getOrg_level();
            if (temp > max) {
                max = temp;
            }
        }
        switch (max) {
        // 省级
        case 2:
            proviceProcessStatus(tempList);
            break;
        // 市级
        case 3:
            cityProcessStatus(tempList);
            break;
        // 区县级
        case 4:
            townProcessStatus(tempList);
            break;
        // 学校
        default:
            schoolProcessStatus(tempList, 1);
            break;
        }
        request.setAttribute("orglevel", max);
        return tempList;
    }

    @Override
    public List<ScaleProcessStruct> getTeacherCheckProcessStatusForSchool(HttpServletRequest request, Organization org,
            int etschoolid, int taskfrom) {
        ArrayList<ScaleProcessStruct> tempList = new ArrayList<ScaleProcessStruct>();
        String[] scaleids = null;
        String[] rolearray = null;
        String[] rolenamearray = null;
        String[] orgarray = null;
        if (taskfrom == 0) {// 本学校创建的任务
            ExamdoTaskSchool ets = this.queryETSAccordingId(etschoolid);
            if (ets == null)
                return null;
            scaleids = ets.getScaleids().split(",");
            rolearray = ets.getRoleids().split(",");
            rolenamearray = ets.getRolename().split(",");
            orgarray = ets.getOrgid().split(",");
            for (int i = 0; i < scaleids.length; i++) {
                // for (int j = 0; j < orgarray.length; j++) {
                for (int m = 0; m < rolearray.length; m++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    if (sdi == null)
                        continue;
                    sps.setScaleName(sdi.getTitle());
                    sps.setOrgid(org.getId());
                    sps.setOrgName(org.getName());
                    sps.setRoleid(Integer.parseInt(rolearray[m]));
                    sps.setRoleName(rolenamearray[m]);
                    sps.setTaskId(etschoolid);
                    sps.setTaskfrom(taskfrom);
                    tempList.add(sps);
                }
                // }
            }
        } else {// 上级教委分发的任务
            ExamdoTaskEducommission ete = eduDispatcherMapper.queryETEAccordingId(etschoolid);
            if (ete == null)
                return null;
            scaleids = ete.getScaleids().split(",");
            rolearray = ete.getRoleids().split(",");
            rolenamearray = ete.getRolename().split(",");
            for (int i = 0; i < scaleids.length; i++) {
                for (int j = 0; j < rolearray.length; j++) {
                    ScaleProcessStruct sps = new ScaleProcessStruct();
                    String[] scaleidss = scaleids[i].split("_");
                    String scaleid = scaleidss[0];
                    sps.setScaleid(scaleid);
                    Scale sdi = cachedScaleMgr.get(scaleid);
                    if (sdi == null)
                        continue;
                    sps.setScaleName(sdi.getTitle());
                    sps.setOrgid(org.getId());
                    sps.setOrgName(org.getName());
                    sps.setRoleid(Integer.parseInt(rolearray[j]));
                    sps.setRoleName(rolenamearray[j]);
                    sps.setTaskId(etschoolid);
                    sps.setTaskfrom(taskfrom);
                    tempList.add(sps);
                }
            }
        }
        ExamdoTaskSchool ets = this.queryETSAccordingId(etschoolid);

        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        Set<Role> roleSet = account.getRoles();
        Iterator it = roleSet.iterator();
        // 获取权限最大的那个用户
        int max = 0;
        while (it.hasNext()) {
            Role role = (Role) it.next();
            int temp = role.getOrg_level();
            if (temp > max) {
                max = temp;
            }
        }
        switch (max) {
        // 省级
        case 2:
            proviceProcessStatus(tempList);
            break;
        // 市级
        case 3:
            cityProcessStatus(tempList);
            break;
        // 区县级
        case 4:
            townProcessStatus(tempList);
            break;
        // 学校
        default:
            schoolProcessStatus(tempList, 2);
            break;
        }
        request.setAttribute("orglevel", max);
        return tempList;
    }

    public void proviceProcessStatus(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.schoolDispatcherMapper.queryETSchoolAccordProviceYes(sps);
            int nodowork = this.schoolDispatcherMapper.queryETSchoolAccordProviceYes(sps);
            int total = dowork + nodowork;
            // String percentage = String.valueOf((dowork * 1.0 / total) * 100)+
            // "%";
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void cityProcessStatus(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.schoolDispatcherMapper.queryETSchoolAccordCityYes(sps);
            int nodowork = this.schoolDispatcherMapper.queryETSchoolAccordCityNo(sps);
            int total = dowork + nodowork;
            // String percentage = String.valueOf((dowork * 1.0 / total) * 100)+
            // "%";
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void townProcessStatus(List tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            int dowork = this.schoolDispatcherMapper.queryETSchoolAccordTownYes(sps);
            int nodowork = this.schoolDispatcherMapper.queryETSchoolAccordTownNo(sps);
            int total = dowork + nodowork;
            // String percentage = String.valueOf((dowork * 1.0 / total) * 100)+
            // "%";
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
            sps.setNoTestPerson(nodowork);
            sps.setTotalPerson(total);
            sps.setPercentage(percentage);
        }
    }

    public void schoolProcessStatus(List tempList, int typeflag) {
        for (int i = 0; i < tempList.size(); i++) {
            ScaleProcessStruct sps = (ScaleProcessStruct) tempList.get(i);
            if (typeflag == 1) {
                Map doworkMap = this.schoolDispatcherMapper.queryETStudentAccordSchoolYes(sps);
                Map nodoworkMap = this.schoolDispatcherMapper.queryETStudentAccordSchoolNo(sps);

                int dowork = Integer.parseInt(doworkMap.get("num").toString());
                int nodowork = Integer.parseInt(nodoworkMap.get("num").toString());
                Object njmc = doworkMap.get("njmc");
                if (njmc == null)
                    njmc = nodoworkMap.get("njmc");
                if (njmc != null)
                    sps.setGradeName(njmc.toString());
                int total = dowork + nodowork;
                // String percentage = String.valueOf((dowork * 1.0 / total) *
                // 100)+ "%";
                DecimalFormat df = new DecimalFormat("#.##");
                String percentage = df.format((dowork * 1.0 / total) * 100) + "%";
                sps.setNoTestPerson(nodowork);
                sps.setTotalPerson(total);
                sps.setPercentage(percentage);
            }
            if (typeflag == 2) {
                int dowork = this.schoolDispatcherMapper.queryETTeacherAccordSchoolYes(sps);
                int nodowork = this.schoolDispatcherMapper.queryETTeacherAccordSchoolNo(sps);
                int total = dowork + nodowork;
                String percentage = null;
                if(dowork == 0){
                    percentage = String.valueOf("0%");
                }else{
                    DecimalFormat df = new DecimalFormat("#.##");
                    percentage = df.format((dowork * 1.0 / total) * 100) + "%";
                }
                sps.setNoTestPerson(nodowork);
                sps.setTotalPerson(total);
                sps.setPercentage(percentage);
            }
        }
    }

    @Override
    public ExamdoTaskSchool queryETSAccordingId(int etschoolid) {
        // TODO Auto-generated method stub
        return this.schoolDispatcherMapper.queryETSAccordingId(etschoolid);
    }

    @Override
    public List<ExamdoTaskSchool> queryETSAccordingCondition(HttpServletRequest request,
            DispatcherFilterParam dispatcherFilterParam) {
        // 获取用户的id
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long userid = account.getId();
        List<ExamdoTaskSchool> resultList = this.schoolDispatcherMapper
                .queryETSAccordingCondition(dispatcherFilterParam);
        return resultList;
    }

    @Override
    public List<ExamdoTaskSchool> schoolAdminsearchSDispatched(HttpServletRequest request, int typeflag,
            PageParameter page, DispatcherFilterParam dispatcherFilterParam) {
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        long userid = account.getId();
        List<ExamdoTaskSchool> etsList = this.schoolDispatcherMapper.schoolAdminsearchSDispatchedByPage(userid,
                typeflag, page, dispatcherFilterParam);
        return etsList;
    }

    @Override
    public List<ExamdoTaskSchool> requireStatus(List<ExamdoTaskSchool> etsList, Organization org) {
        // TODO Auto-generated method stub
        List<Integer> taskIdList = this.schoolDispatcherMapper.queryOrgidFromETES(org.getId());
        // 判断是否下发
        Date now = new Date();
        for (int i = 0; i < etsList.size(); i++) {
            ExamdoTaskSchool ets = etsList.get(i);
            long id = ets.getId();
            if (taskIdList.contains(id)) {
                ets.setXfflag(1);
            } else {
                ets.setXfflag(0);
            }
            Date endtime = ets.getEndtime();
            if (endtime.after(now)) {// 如果没有过期
                ets.setExpireflag(0);
            } else
                ets.setExpireflag(1);
            // 判断是否可以撤回
            boolean result = false;
            if (ets.getTaskfrom() == 0)// 如果是学校自己分发的量表
                result = this.checkStudentProcessingStatus((int) id);
            if (result) {
                // 可以撤回
                ets.setChflag(1);
            } else {
                // 不可以撤回
                ets.setChflag(0);
            }
        }
        return etsList;
    }

    @Override
    public List<ExamdoTaskSchool> requireScaleName(List<ExamdoTaskSchool> etsList) {
        // TODO Auto-generated method stub
        for (int i = 0; i < etsList.size(); i++) {
            ExamdoTaskSchool ets = etsList.get(i);
            String scaleids = ets.getScaleids();
            String[] scaleArray = null;
            if (scaleids.contains(",")) {
                scaleArray = scaleids.split(",");
            } else {
                scaleArray = new String[1];
                scaleArray[0] = scaleids;
            }
            String scaleName = "";
            for (int j = 0; j < scaleArray.length; j++) {
                String scaleid = "";
                if (scaleArray[j].contains("_")) {
                    scaleid = scaleArray[j].substring(0, scaleArray[j].indexOf("_"));
                } else {
                    scaleid = scaleArray[j];
                }
                try {
                    scaleName = scaleName + cachedScaleMgr.get(scaleid).getTitle() + ",";
                } catch (Exception e) {
                    continue;
                }

            }
            try {
                scaleName = scaleName.substring(0, scaleName.length() - 1);
            } catch (Exception e) {
                continue;
            }
            ets.setScalenames(scaleName);
        }
        return etsList;
    }

    public List<ExamdoTaskSchool> processScaleStatus(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam) {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (dispatcherFilterParam.getProgressStatus().equals("1")) {
            // 尚未开始
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                Date starttime = ets.getStarttime();
                Date now = new Date();
                if (!(starttime.after(now))) {
                    it.remove();
                }
            }
        } else if (dispatcherFilterParam.getProgressStatus().equals("2")) {
            // 尚未开始
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                Date starttime = ets.getStarttime();
                Date endtime = ets.getEndtime();
                Date now = new Date();
                if (!((now.after(starttime)) && (now.before(endtime)))) {
                    it.remove();
                }
            }
        } else if (dispatcherFilterParam.getProgressStatus().equals("3")) {
            // 已经结束
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                Date endtime = ets.getEndtime();
                Date now = new Date();
                if (!(now.after(endtime))) {
                    it.remove();
                }
            }
        }
        return etsList;
    }

    @Override
    public List<ExamdoTaskSchool> filterByTaskName(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam) {
        if (!dispatcherFilterParam.getTaskKeywords().equals("")) {
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                if (!(ets.getTaskname().contains(dispatcherFilterParam.getTaskKeywords()))) {
                    it.remove();
                }
            }
        }
        return etsList;
    }

    public List<ExamdoTaskSchool> filterByGrade(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam) {
        if (dispatcherFilterParam.getSgradeId() != -1) {
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                if (!(ets.getNj().contains(String.valueOf(dispatcherFilterParam.getSgradeId())))) {
                    it.remove();
                }
            }
        }
        return etsList;
    }

    // 过滤学生记录
    @Override
    public List<ExamdoTaskSchool> filterTypeRecord(List<ExamdoTaskSchool> etsList, int flag) {
        // TODO Auto-generated method stub
        // 过滤学生
        if (flag == 0) {
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                if (ets.getRoleids() != null) {
                    it.remove();
                }
            }
            return etsList;
        } else {
            // 过滤老师
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ets = it.next();
                if (ets.getRoleids() == null) {
                    it.remove();
                }
            }
            return etsList;
        }
    }

    @Override
    public List<ExamdoTaskSchool> filterByRoleid(List<ExamdoTaskSchool> etsList,
            DispatcherFilterParam dispatcherFilterParam) {
        // TODO Auto-generated method stub
        if (dispatcherFilterParam.getRoleid() != -1) {
            ListIterator<ExamdoTaskSchool> it = etsList.listIterator();
            while (it.hasNext()) {
                ExamdoTaskSchool ete = it.next();
                if (ete.getRoleids() != null) {// 赵万锋修改，不知合理否？
                    String[] roleids = ete.getRoleids().split(",");
                    for (int i = 0; i < roleids.length; i++) {
                        if (roleids[i].equals(dispatcherFilterParam.getRoleid())) {
                            it.remove();
                            break;
                        }
                    }
                }
                // if
                // (ete.getRoleids().contains(dispatcherFilterParam.getRoleid()))
                // {
                // it.remove();
                // }
            }
            return etsList;
        }
        return etsList;
    }

    @Override
    public List getStudentNoTestDetail(long orgid, int taskid, int bj, String scaleid) {
        return schoolDispatcherMapper.getStudentNoTestDetail(orgid, taskid, bj);
    }

    @Override
    public List getTeacherNoTestDetail(long orgid, int taskid, int roleid, String scaleid) {
        return schoolDispatcherMapper.getTeacherNoTestDetail(orgid, taskid, roleid);
    }

    @Override
    public void delayEndTime(int taskid) {
        // 将分发检测任务从当前日期延期7天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        schoolDispatcherMapper.delayEndTime(taskid, calendar.getTime());
    }

}
