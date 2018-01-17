package com.njpes.www.service.scaletoollib;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ExamAnswerServiceI {

    public void importStudentAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception;

    public void importTeacherAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception;

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

    public void downloadTchAnswerForCityEdu(String countyid, String roleid, String rolename, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream);

    public void downloadTchAnswerForCountyEdu(String schoolid, String roleid, String rolename, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream);

    public Map<String, Object> getStudentAnswer(long resultid);

    public Map<String, Object> getTeacherAnswer(long resultid);

    public Map<String, Object> getThreeAngleAnswer(long resultid, int typeflag);

    public void ScaleWarningReCalculate(String scaleid, int typeflag);

	public void downloadAllStudentResult(String[] headnames, String[] headfields, String startTime, String endTime,
			OutputStream outputStream);
}
