package com.njpes.www.service.scaletoollib;

import java.util.Map;

public interface ExamdistributeService {
    public void singledistribute(String taskname, String cityid, String countyid, long orgid, String flag,
            String starttime, String endtime, String objectType, String xd, String gradeid, String nj, String njmc,
            String bj, String bjmc, String[] scales, long createuserid, String[] studentIds, String[] teacherIds,
            String teacherRole, String rolename, Map resultMsgMap) throws Exception;// 教师角色：班主任、心理老师等

    public void groupdistribute(String taskname, String countyid, String cityid, long orgid, // 学校代码
            String xd, String[] classids, // 班级

            String flag, //
            String starttime, String endtime, String objectType, // 对象类型：老师，学生
            String[] scales, // 量表id
            long createuserid, String[] teacherRole, Map resultMsgMap);// 教师角色：班主任、心理老师等

    public void eduDistributeForStu(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String xd, String[] gradeIds,
            String[] scales, String[] schoolIds, Map resultMsgMap);

    public void eduDistributeForTeacher(int orglevel, String taskname, long createuserid, long creater_orgid,
            String[] areaIds, long[] orgIds, String starttime, String endtime, String[] scales, String[] teacherroleIds,
            String[] schoolIds, Map resultMsgMap);

    public int xiafaToEdu(long orgid, int taskid);

    public void xiafaToSchool(long orgid, int taskid, String objectType, // 对象类型：老师，学生
            Map resultMsgMap);

    public void toSchoolUsers(Map<String, Object> params) throws Exception;
}
