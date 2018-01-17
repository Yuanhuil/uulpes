package com.njpes.www.service.assessmentcenter;

import java.util.List;

import com.njpes.www.entity.assessmentcenter.InvestExam;
import com.njpes.www.entity.assessmentcenter.InvesttaskWithBLOBs;
import com.njpes.www.entity.assessmentcenter.MyInvesttask;
import com.njpes.www.entity.scaletoollib.InvestDispatcherFilterParam;
import com.njpes.www.utils.PageParameter;

import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;

public interface InvestServiceI {
    public List<InvestExam> getAllInvestPage(InvestExam ie, PageParameter page);

    public List<InvesttaskWithBLOBs> getAllInvestTaskForSchoolPage(
            InvestDispatcherFilterParam investDispatcherFilterParam, PageParameter page);

    public List<InvesttaskWithBLOBs> getAllInvestTaskForEduPage(InvestDispatcherFilterParam investDispatcherFilterParam,
            PageParameter page);

    public List<MyInvesttask> getInvestdoByPage(InvestDispatcherFilterParam investDispatcherFilterParam,
            PageParameter page);

    public MyInvesttask getInvestdoById(String id);

    public void insertInvestExam(InvestExam ie);

    // public void investdone(long userid,long taskid,int scaleid,String
    // answer);
    public void investdone(long userid, long taskid, int scaleid, Questionnaire questionnaire);

    public void delInvests(List ids);

    public void delInvest(String id);

    public InvestExam selectByPrimaryKey(Integer id);

    public void updateByPrimaryKeyWithBLOBs(InvestExam ie);

    public void schooldistribute(long orgid, int objecttype, int scaleid, long onwerid, String taskname,
            String starttime, String endtime, String createtime, String[] classIds, String[] teacherroleIds,
            List resultMsgList);

    public void eduDistributeForStu(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String[] gradeIds, int scaleid,
            String[] schoolIds, List resultMsgList);

    public void eduDistributeForTch(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String[] teacherroleIds, int scaleid,
            String[] schoolIds, List resultMsgList);

    // public List<InvesttaskWithBLOBs>
    // filterTypeRecord(List<InvesttaskWithBLOBs> investTaskList,int flag);
    public List<InvesttaskWithBLOBs> requireStatus(List<InvesttaskWithBLOBs> itsList, long orgid, long ownerid);

    public boolean checkProcessingStatus(long taskid, long ownerid);

    public void withdraw(long itid, String stype);

    public String investTransfer(long itid, long dispense_orgid, String stype);

    public Scale getScaleFromInvestExam(InvestExam investExam);

    public List getInvestResult(long taskid);
}
