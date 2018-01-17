package com.njpes.www.service.scaletoollib;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.NormInfo;
import com.njpes.www.entity.scaletoollib.QueryInfo;
import com.njpes.www.entity.scaletoollib.ScaleDetailInfo;
import com.njpes.www.entity.scaletoollib.Scalenorm;
import com.njpes.www.entity.scaletoollib.Scaletype;
import com.njpes.www.entity.scaletoollib.ScaleFilterParam;
import edutec.scale.model.Scale;

@Service("ScaleService")
public interface ScaleService {

    /*************************** 量表类型部分 *********************/

    /**
     * 获得系统中所有的量表类型
     *
     * @return List量表类型，用Int表示
     */
    public List getScaleTypes();

    /**
     * 根据适应人群获得相应的量表类型
     *
     * @return List量表类型
     */
    public List getScaleTypesByGroupid(int groupid);

    public List getScaleSource();

    public List queryScaleByGroupId(int groupid);

    /**
     * 增加自定义量表类型
     *
     * @param scaleTypes
     *            量表类型列表
     * @return 是否添加成功
     */
    public Boolean addScaleType(Scaletype scaleType);

    /**
     * 删除自定义量表类型
     *
     * @param scaleTypes
     *            量表类型列表
     * @return 是否删除成功
     */
    public Boolean delScaleType(int scaleTypeId);

    /*************************** 量表部分 *********************/

    /**
     * 根据查询参数查询量表的简单元信息
     *
     * @param para
     *            查询参数 queryInfo对应的类
     * @return 量表简单信息列表
     */
    public List QueryScaleList(QueryInfo para);

    /**
     * 获得某张量表的详细元信息
     *
     * @param scaleId
     *            量表id
     * @return 量表的详细信息
     */
    public ScaleDetailInfo getScaleInfo(int scaleId);

    /**
     * 导入量表
     *
     * @param wb
     *            Excel模板文件
     * @return 是否导入成功
     */
    public String ImportScale(InputStream stream, String excelUrl, long userid, Organization org) throws Exception;

    public String ImportScaleQuestion(InputStream stream, String excelUrl, Organization org) throws Exception;
    // public String ImportScale(InputStream inputStream,String fileName);

    public String ImportScaleNorm(InputStream stream, long orgid, int orglevel, String scaleId, int areaid, long userid,
            String description, String editer, String edittime, String excelUrl) throws Exception;

    /**
     * 下载量表支持word、pdf两种格式
     *
     * @param scaleid
     *            量表id
     * @param type
     *            1表示导出word，2表示导出PDF
     * @return 文件流
     */
    /**
     * 上传测评答案
     */
    public void importStudentAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception;

    public void importTeacherAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception;

    public FileInputStream downloadScale(String scalecode, Map saveInfo) throws Exception;;

    /**
     * 下载量表模版（空的）
     */
    public String downloadScaleModel();

    /**
     * 下载供老师记录答案的excel
     *
     * @param scaleid
     *            量表id
     * @param type
     *            年级还是班级和学校
     * @param recordId
     *            对应年级或班级表的id
     */
    public FileInputStream downloadAnswerModel(String scaleid, String type, int recordId);

    public String downloadAnswerModelForStu(String scaleid, long xxdm, long nj, long bj, OutputStream outputStream);

    public String downloadAnswerModelForTeach(String scaleid, long orgid, String roleName, long teacherRole,
            OutputStream outputStream);

    // public void downloadStuAnswerForSch(String njmc, String bjmc, String
    // nj,String bj, String scaleId,String startTime, String endTime,
    // OutputStream outputStream);
    public void downloadStuAnswerForSch(String orgid, String xd, String nj, String[] bjArray, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream);

    public void downloadTchAnswerForSch(String orgid, String roleid, String scaleId, String[] attrnames,
            String[] attrfields, String[] attrIds, String startTime, String endTime, OutputStream outputStream);

    public void downloadStuAnswerForCityEdu(String countyid, String xd, String nj, String scaleid, String[] headnames,
            String[] headfields, String[] attrIds, String startTime, String endTime, OutputStream outputStream);

    public void downloadStuAnswerForCountyEdu(String countyid, String schoolid, String xd, String nj, String scaleid,
            String[] headnames, String[] headfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream);

    public void downloadStuAnswerForEdu(int orglevel, String xd, String nj, List countyIdList, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream);

    public void downloadTchAnswerForEdu(int orglevel, String roleid, String rolename, List countyIdList, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream);

    // public void downloadStuAnswerForCountyEdu(String nj,String njmc,List
    // townIdList, String scaleId,String[] attrnames,String[] attrfields, String
    // startTime, String endTime, OutputStream outputStream);
    // public void downloadTchAnswerForCountyEdu(String rolename,List
    // townIdList, String scaleId,String[] attrnames,String[] attrfields,String
    // startTime, String endTime, OutputStream outputStream);
    /**
     * 删除导入量表
     *
     * @param scaleids
     *            要删除的量表id列表
     * @return 是否删除成功
     */
    public List DeleteScales(List scaleids);

    /*************************** 问卷答案部分 *********************/

    /**
     * 导入个人量表答案
     *
     * @param wb
     *            答案模板文件
     * @return 答案对应的id
     */
    public Integer ImportIndividualAnswers(String url);

    /**
     * 导入团体量表答案
     *
     * @param wb
     *            答案模板文件
     * @return 团体答案对应的id
     */
    public Integer ImportGroupAnswers(InputStream inputStream);

    /*************************** 报告部分 *********************/
    /**
     * 个体报告导出
     *
     * @param answerId
     *            答案id
     * @param type
     *            学生家长老师
     * @param userid
     *            学生、家长、老师的id号码
     * @return 报告文件流
     */
    public FileInputStream personalReportExport(int scaleId, String type, String userid);

    /**
     * 个人复合报告导出
     *
     * @param scaleIds
     *            多个量表的id
     * @param type
     *            学生家长老师
     * @param userid
     *            学生、家长、老师的id号码
     * @return 报告文件流
     */
    public FileInputStream personalReportExport(long resultId, String subjectUserId, String obsverUserId,
            String threeAngleUUID);

    public FileInputStream personalCompositeReportExport(List scaleIds, String type, String userid);

    public Map<Object, Object> getStudentPersonalReporter(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) throws Exception;

    public Map<Object, Object> getTeacherPersonalReporter(long subjectUserId, Object obsverUser, long resultId,
            boolean isDownload);

    public Map<String, Object> getStudentCompositeReport(long subjectUserId, Object obsverUser);

    public Map<String, Object> getStudentCompositeReport(long subjectUserId, Object obsverUser, String starttime,
            String endtime);

    public Map<String, Object> getStudentRemarkReport(long subjectUserId, Object obsverUser);

    public Map<String, Object> downloadStudentCompositeReport(long subjectUserId, Object obsverUser);

    public Map<String, Object> getTeacherCompositeReport(long subjectUserId, Object observerUser);

    public Map<String, Object> downloadTeacherCompositeReport(long subjectUserId, Object observerUser);

    public Map<Object, Object> getStudentGroupReportForSchool(long orgid, String njmc, String[] bjArray, String scaleId,
            String startDate, String endDate);

    public Map<Object, Object> getStudentGroupReportForCounty(String countyid, String[] schoolnames, String njmc,
            String scaleid, String startDate, String endDate);

    public Map<Object, Object> getStudentGroupReportForCity(String cityname, String[] qxArray, String njmc,
            String scaleid, String startDate, String endDate);

    public Map<Object, Object> getStudentGroupReportForSubSchool(String cityname, String[] schoolnames, String njmc,
            String scaleid, String startDate, String endDate);

    public Map<Object, Object> GetThreeAngleReporterForStudent(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload);

    public Map<Object, Object> GetThreeAngleReporterForTeacher(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload);

    public Map<Object, Object> GetThreeAngleReporterForParent(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload);

    public Map<String, Object> getStudentAnswer(long resultid);

    public Map<String, Object> getTeacherAnswer(long resultid);

    public Map<String, Object> getThreeAngleAnswer(long resultid, int typeflag);

    /**
     * 团体报告导出
     * 
     * @param scaleId
     *            量表id
     * @param type
     *            班级年级或学校
     * @param type
     *            班级年级或学校的id
     * @return 报告文件流
     */
    public FileInputStream groupReportExport(int scaleId, String type, int id);

    /**
     * 获得自定义常模的详细信息
     *
     * @param scaleId
     *            对应的量表id
     * @return 常模的基础信息
     */
    public List<Scalenorm> getNorm(int scaleId);

    /**
     * 获得自定义常模的基础信息
     *
     * @param scaleId
     *            对应的量表id
     * @return 常模的基础信息
     */
    public List<NormInfo> getNormInfo(int scaleId);

    /**
     * 根据数据库中的所有答案，重新计算生成自定义常模
     *
     * @param scaleId
     *            对应的量表id
     * @param userId
     *            创建者的用户id
     * @param userName
     *            创建者的用户名称
     * @return 自定义常模创建是否成功
     */
    public Boolean addNorm(int scaleId);

    // public List<ScaleNormLog> getScaleNorminfo(int scaleId,int type,int
    // orglevel);
    public List<NormInfo> getScaleNorminfo(NormInfo norminfo);

    public List<Scalenorm> getNormById(String scaleid, int normid);

    public String createNorm(String scaleid, Organization org, long userid, String normname, String description,
            String starttime, String endtime);

    public String downloadscalenormtemp(Scale scale, OutputStream outputStream) throws Exception;

    public int deleteScaleNorm(String scaleid, int normid);

    public void exportScaleNorm(Scale scale, int normid, OutputStream outputStream) throws Exception;

    public boolean updateShowTitle(String code, String showtitle);

	public List<Scale> getAllScale();

}
