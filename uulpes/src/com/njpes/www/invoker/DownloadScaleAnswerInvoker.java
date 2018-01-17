package com.njpes.www.invoker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.dao.scaletoollib.ScaleMapper;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.utils.Zip;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.util.ScaleUtils;
import heracles.excel.WorkbookUtils;
import heracles.util.Pools;
import heracles.util.UtilMisc;
import heracles.web.config.ApplicationConfiguration;

//import edutec.scale.util.SchoolUtils;
@Service("downloadScaleAnswerInvoker")
public class DownloadScaleAnswerInvoker {
    // public static final int COL_START = 9;
    // public static final int START_ROW = 4;
    @Autowired
    public CachedScaleMgr cachedScaleMgr;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private TeacherMapper teacherDao;
    @Autowired
    private ScaleMapper scaleMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private ExamresultStudentMapper erStudentMapper;
    @Autowired
    private ExamresultTeacherMapper erTeacherMapper;

    /**
     * 学校用户下载答案
     * 
     * @param orgid
     * @param bjid
     * @param njmc
     * @param bjmc
     * @param scaleId
     * @param groupId
     * @param os
     */
    public void downloadStuAnswerForSchool(String orgid, String xd, String nj, String[] bjArray, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream os) {
        InputStream is = null;
        endTime += " 23:59:59";
        try {
            if (ScaleUtils.isThirdAngleScale(scaleId)) {
                is = getSchoolStuThreeAngleStream(orgid, scaleId, xd, nj, bjArray, attrnames, attrfields, attrIds,
                        startTime, endTime);
            } else {
                is = getSchoolStuStream(orgid, scaleId, xd, nj, bjArray, attrnames, attrfields, attrIds, startTime,
                        endTime);
            }

            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public void downloadTchAnswerForSchool(String orgid, String roleid, String scaleId, String[] attrnames,
            String[] attrfields, String[] attrIds, String startTime, String endTime, OutputStream os) {
        InputStream is = null;
        endTime += " 23:59:59";
        try {
            // long bjid = Integer.parseInt(params.get("bjid").toString());
            // Map<?, ?> param = UtilMisc.toMap("scaleid",
            // scaleId,"roleid",roleid,"startTime",startTime,"endTime",endTime);
            // List<StudentExamAnswer> studentExamAnswerList =
            // erStudentMapper.getStudentAnswerByBjid(param);//=
            // studentDao.getStudentsByClassId(bjid);
            is = getSchoolTchStream(orgid, roleid, scaleId, attrnames, attrfields, attrIds, startTime, endTime);

            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 教委用户下载答案
     * 
     * @param orgid
     * @param bjid
     * @param njmc
     * @param bjmc
     * @param scaleId
     * @param groupId
     * @param os
     */
    /*
     * public void downloadStuAnswerForEdu(int orglevel,String orgids,String
     * nj,String njmc,String scaleId,String[] attrnames,String[]
     * attrfields,String[] attrIds,String startTime,String endTime,OutputStream
     * os){ InputStream is = null; try {
     * if(ScaleUtils.isThirdAngleScale(scaleId)) { is =
     * getEduStuThreeAngleStream(orgids,scaleId,nj,njmc,attrnames,attrfields,
     * attrIds,startTime,endTime); } else{ is =
     * getEduStuStream(orgids,scaleId,nj,njmc,attrnames,attrfields,attrIds,
     * startTime,endTime); }
     * 
     * IOUtils.copy(is, os); } catch (Exception e) { e.printStackTrace(); }
     * finally { IOUtils.closeQuietly(os); IOUtils.closeQuietly(is); } }
     */
    public void downloadStuAnswerForCityEdu(String countyid, String xd, String nj, String scaleid, String[] headnames,
            String[] headfields, String[] attrIds, String startTime, String endTime, OutputStream os) {
        InputStream is = null;
        endTime += " 23:59:59";
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                is = getEduStuThreeAngleStream(countyid, scaleid, xd, nj, headnames, headfields, attrIds, startTime,
                        endTime);
            } else {
                is = getEduStuStream(countyid, scaleid, xd, nj, headnames, headfields, attrIds, startTime, endTime);
            }

            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public void downloadStuAnswerForCountyEdu(String countyid, String schoolid, String xd, String nj, String scaleid,
            String[] headnames, String[] headfields, String[] attrIds, String startTime, String endTime,
            OutputStream os) {
        InputStream is = null;
        endTime += " 23:59:59";
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                is = getSchoolStuThreeAngleStream(schoolid, scaleid, xd, nj, null, headnames, headfields, attrIds,
                        startTime, endTime);
                // is =
                // getEduStuThreeAngleStream(countyid,scaleid,xd,nj,headnames,headfields,attrIds,startTime,endTime);
            } else {
                is = getSchoolStuStream(schoolid, scaleid, xd, nj, null, headnames, headfields, attrIds, startTime,
                        endTime);
                // is =
                // getEduStuStream(countyid,scaleid,xd,nj,headnames,headfields,attrIds,startTime,endTime);
            }

            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public void downloadTchAnswerForCityEdu(String countyid, String roleid, String rolename, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream os) {
        InputStream is = null;
        endTime += " 23:59:59";
        try {
            is = getEduTchStream(countyid, roleid, rolename, scaleId, attrnames, attrfields, attrIds, startTime,
                    endTime);

            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public void downloadTchAnswerForCountyEdu(String schoolid, String roleid, String rolename, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream os) {
        InputStream is = null;
        endTime += " 23:59:59";
        try {
            is = getSchoolTchStream(schoolid, roleid, scaleId, attrnames, attrfields, attrIds, startTime, endTime);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public InputStream getSchoolStuThreeAngleStream(String orgid, String scaleId, String xd, String nj,
            String[] bjArray, String[] backgroundNames, String[] backgroundFields, String[] attrIds, String startTime,
            String endTime) {
        try {
            List<File> srcfile = new ArrayList<File>();
            String folder = ApplicationConfiguration.getInstance().getWorkDir() + File.separator + "download" + File.separator + "心理健康量表测评数据";
            // 在服务器端创建文件夹
            File myfolder = new File(folder);
            if (!myfolder.exists()) {
                myfolder.mkdir();
            }

            String[] thirdAngleScaleIDs = null;
            String thirdAngleScales[] = null;
            if (ScaleUtils.isThirdAngleScaleP(scaleId)) {
                thirdAngleScaleIDs = new String[] { "111000001", "111000002", "111000003" };
                thirdAngleScales = new String[] { "小学生心理健康量表-学生版", "小学生心理健康量表-家长版", "小学生心理健康量表-教师版" };
            }
            if (ScaleUtils.isThirdAngleScaleM(scaleId)) {
                thirdAngleScaleIDs = new String[] { "110110001", "110110002", "110110003" };
                thirdAngleScales = new String[] { "中学生心理健康量表-学生版", "中学生心理健康量表-家长版", "中学生心理健康量表-教师版" };
            }
            for (int i = 0; i < 3; i++) {
                Map<?, ?> param = UtilMisc.toMap("orgid", orgid, "scaleid", thirdAngleScaleIDs[i], "xd", xd, nj, nj,
                        "bjArray", bjArray, "startTime", startTime, "endTime", endTime);
                List<Map> stuExamAnswerList = erStudentMapper.getStudentMhAnswerInSchool(param);
                if (stuExamAnswerList == null)
                    continue;
                if (stuExamAnswerList.size() == 0)
                    continue;

                Scale scale = cachedScaleMgr.get(thirdAngleScaleIDs[i], true);
                if (scale == null)
                    break;
                String filename = thirdAngleScales[i];
                HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
                HSSFSheet sheet = workbook.createSheet();
                final int COL = backgroundNames.length;
                int idx = backgroundNames.length;
                for (int j = 0; j < backgroundNames.length; j++) {
                    WorkbookUtils.setCellValue(sheet, 0, j, backgroundNames[j]);
                }
                // 写背景字段表头
                List<FieldValue> attrlist = null;
                attrlist = writeAttrHead(sheet, 1, 0, backgroundNames.length, attrIds);
                // 写表体
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                idx = 1;
                for (Map stuExamAnswer : stuExamAnswerList) {
                    for (int j = 0; j < backgroundFields.length; j++) {
                        String headField = backgroundFields[j];
                        if (headField.equals("xbm")) {
                            String xbm = stuExamAnswer.get(headField).toString();
                            if (xbm.equals("1"))
                                WorkbookUtils.setCellValue(sheet, idx, j, "男");
                            else
                                WorkbookUtils.setCellValue(sheet, idx, j, "女");
                        } else
                            WorkbookUtils.setCellValue(sheet, idx, j,
                                    stuExamAnswer.get(backgroundFields[j]).toString());
                    }
                    String answer = stuExamAnswer.get("answerscore").toString();
                    String[] qscores = answer.split("#");
                    for (int j = 0; j < qscores.length; j++) {
                        String[] qinfos = qscores[j].split(",");
                        String qid = qinfos[0];
                        String score = qinfos[2];
                        double dScore = Double.parseDouble(score);
                        String qTitle = qid.substring(1);
                        if (qTitle.equals("_1")) {
                            int n = qTitle.indexOf('_');
                            qTitle.substring(0, n);
                        }
                        if (idx == 1)
                            WorkbookUtils.setCellValue(sheet, 0, j + backgroundFields.length,
                                    String.format("题%s", qTitle));
                        WorkbookUtils.setCellValue(sheet, idx, j + backgroundFields.length, dScore, cellStyle);
                    }
                    Object bkg = stuExamAnswer.get("attrs");
                    if (bkg != null) {
                        writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                    }
                    // 以下处理维度得分
                    String dimscore = stuExamAnswer.get("dimscore").toString();
                    String[] dscores = dimscore.split("#");
                    int k = 0;
                    for (int j = 0; j < dscores.length; j++) {
                        String[] dinfos = dscores[j].split(",");
                        String wid = dinfos[0];
                        if (wid.equals(Dimension.SUM_SCORE_DIM))
                            continue;
                        String dimTitle = scale.getDimensionMap().get(wid).getTitle();
                        String score = dinfos[1];
                        double dScore = Double.parseDouble(score);
                        if (idx == 1)
                            WorkbookUtils.setCellValue(sheet, 0, k + qscores.length + backgroundFields.length,
                                    dimTitle);
                        WorkbookUtils.setCellValue(sheet, idx, k + qscores.length + backgroundFields.length, dScore,
                                cellStyle);
                        k++;
                    }
                    idx++;
                }

                File f = new File(myfolder + File.separator + filename + ".xls");
                if (f.exists()) {
                    f.delete();
                } else {
                    f.createNewFile();
                }
                OutputStream os = new FileOutputStream(f);
                workbook.write(os);
                os.flush();
                os.close();
                IOUtils.closeQuietly(os);
            }
            Zip.zip(folder, folder + ".zip");
            StringBuilder uri = new StringBuilder();
            uri.append(folder);
            uri.append(".zip");
            File file = new File(uri.toString());
            FileInputStream inputStream = new FileInputStream(file);
            return inputStream;
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getEduStuThreeAngleStream(String countyid, String scaleId, String xd, String nj,
            String[] backgroundNames, String[] backgroundFields, String[] attrIds, String startTime, String endTime) {
        try {
            List<File> srcfile = new ArrayList<File>();
            String folder = ApplicationConfiguration.getInstance().getWorkDir() + File.separator + "download" + File.separator + "心理健康量表测评数据";
            // 在服务器端创建文件夹
            File myfolder = new File(folder);
            if (!myfolder.exists()) {
                myfolder.mkdir();
            }
            String[] thirdAngleScaleIDs = null;
            String thirdAngleScales[] = null;
            if (ScaleUtils.isThirdAngleScaleP(scaleId)) {
                thirdAngleScaleIDs = new String[] { "111000001", "111000002", "111000003" };
                thirdAngleScales = new String[] { "小学生心理健康量表-学生版", "小学生心理健康量表-家长版", "小学生心理健康量表-教师版" };
            }
            if (ScaleUtils.isThirdAngleScaleM(scaleId)) {
                thirdAngleScaleIDs = new String[] { "110110001", "110110002", "110110003" };
                thirdAngleScales = new String[] { "中学生心理健康量表-学生版", "中学生心理健康量表-家长版", "中学生心理健康量表-教师版" };
            }
            for (int i = 0; i < 3; i++) {
                Map<?, ?> param = UtilMisc.toMap("countyid", countyid, "scaleid", thirdAngleScaleIDs[i], "xd", xd, "nj",
                        nj, "startTime", startTime, "endTime", endTime);
                List<Map> stuExamAnswerList = erStudentMapper.getStudentMhAnswerInEdu(param);
                if (stuExamAnswerList == null)
                    continue;
                if (stuExamAnswerList.size() == 0)
                    continue;

                Scale scale = cachedScaleMgr.get(thirdAngleScaleIDs[i], true);
                if (scale == null)
                    break;
                String filename = thirdAngleScales[i];
                HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
                HSSFSheet sheet = workbook.createSheet();
                final int COL = backgroundNames.length;
                int idx = backgroundNames.length;
                for (int j = 0; j < backgroundNames.length; j++) {
                    WorkbookUtils.setCellValue(sheet, 0, j, backgroundNames[j]);
                }
                // 写背景字段表头
                List<FieldValue> attrlist = null;
                attrlist = writeAttrHead(sheet, 1, 0, backgroundNames.length, attrIds);
                // 写表体
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                idx = 1;
                for (Map stuExamAnswer : stuExamAnswerList) {
                    for (int j = 0; j < backgroundFields.length; j++) {
                        String headField = backgroundFields[j];
                        if (headField.equals("xbm")) {
                            String xbm = stuExamAnswer.get(headField).toString();
                            if (xbm.equals("1"))
                                WorkbookUtils.setCellValue(sheet, idx, j, "男");
                            else
                                WorkbookUtils.setCellValue(sheet, idx, j, "女");
                        } else
                            WorkbookUtils.setCellValue(sheet, idx, j,
                                    stuExamAnswer.get(backgroundFields[j]).toString());
                    }
                    String answer = stuExamAnswer.get("answerscore").toString();
                    String[] qscores = answer.split("#");
                    for (int j = 0; j < qscores.length; j++) {
                        String[] qinfos = qscores[j].split(",");
                        String qid = qinfos[0];
                        String score = qinfos[2];
                        double dScore = Double.parseDouble(score);
                        String qTitle = qid.substring(1);
                        if (qTitle.equals("_1")) {
                            int n = qTitle.indexOf('_');
                            qTitle.substring(0, n);
                        }
                        if (idx == 1)
                            WorkbookUtils.setCellValue(sheet, 0, j + backgroundFields.length,
                                    String.format("题%s", qTitle));
                        WorkbookUtils.setCellValue(sheet, idx, j + backgroundFields.length, dScore, cellStyle);
                    }
                    Object bkg = stuExamAnswer.get("attrs");
                    if (bkg != null) {
                        writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                    }
                    // 以下处理维度得分
                    String dimscore = stuExamAnswer.get("dimscore").toString();
                    String[] dscores = dimscore.split("#");
                    int k = 0;
                    for (int j = 0; j < dscores.length; j++) {
                        String[] dinfos = dscores[j].split(",");
                        String wid = dinfos[0];
                        if (wid.equals(Dimension.SUM_SCORE_DIM))
                            continue;
                        String dimTitle = scale.getDimensionMap().get(wid).getTitle();
                        String score = dinfos[1];
                        double dScore = Double.parseDouble(score);
                        if (idx == 1)
                            WorkbookUtils.setCellValue(sheet, 0, k + qscores.length + backgroundFields.length,
                                    dimTitle);
                        WorkbookUtils.setCellValue(sheet, idx, k + qscores.length + backgroundFields.length, dScore,
                                cellStyle);
                        k++;
                    }
                    idx++;
                }

                File f = new File(myfolder + File.separator + filename + ".xls");
                if (f.exists()) {
                    f.delete();
                } else {
                    f.createNewFile();
                }
                OutputStream os = new FileOutputStream(f);
                workbook.write(os);
                os.flush();
                os.close();
                IOUtils.closeQuietly(os);
            }
            Zip.zip(folder, folder + ".zip");
            StringBuilder uri = new StringBuilder();
            uri.append(folder);
            uri.append(".zip");
            File file = new File(uri.toString());
            FileInputStream inputStream = new FileInputStream(file);
            return inputStream;
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected InputStream getSchoolStuStream(String orgid, String scaleId, String xd, String nj, String[] bjArray,
            String[] headNames, String[] headFields, String[] attrIds, String startTime, String endTime)
            throws FileNotFoundException {
        try {
            Map<?, ?> param = UtilMisc.toMap("orgid", orgid, "scaleid", scaleId, "xd", xd, "nj", nj, "bjArray", bjArray,
                    "startTime", startTime, "endTime", endTime);
            List<Map> stuExamAnswerList = erStudentMapper.getStudentAnswerInSchool(param);
            HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
            HSSFSheet sheet = workbook.createSheet();
            Scale scale = cachedScaleMgr.get(scaleId, true);
            final int COL = headNames.length;
            int qNum = 0;
            int idx = headNames.length;
            List<FieldValue> attrlist = null;
            // 不是道德量表，道德量表有自己特定的xls模板
            // if (!ScaleUtils.isMoralityScale(scaleId)) {
            for (int i = 0; i < headNames.length; i++) {
                WorkbookUtils.setCellValue(sheet, 0, i, headNames[i]);
            }
            // 写背景字段表头
            attrlist = writeAttrHead(sheet, 1, 0, headNames.length, attrIds);
            // }
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // 写表体
            idx = 1;
            for (Map stuExamAnswer : stuExamAnswerList) {
                // 写基础信息字段值
                for (int i = 0; i < headFields.length; i++) {
                    String headField = headFields[i];
                    if (headField.equals("xbm")) {
                        String xbm = stuExamAnswer.get(headField).toString();
                        if (xbm.equals("1"))
                            WorkbookUtils.setCellValue(sheet, idx, i, "男");
                        else
                            WorkbookUtils.setCellValue(sheet, idx, i, "女");
                    } else
                        WorkbookUtils.setCellValue(sheet, idx, i, stuExamAnswer.get(headFields[i]).toString());
                }
                Object bkg = stuExamAnswer.get("attrs");
                if (bkg != null) {
                    writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                }
                // 以下处理题目得分
                String answer = stuExamAnswer.get("answerscore").toString();
                String[] qscores = answer.split("#");
                for (int i = 0; i < qscores.length; i++) {
                    String[] qinfos = qscores[i].split(",");
                    String qid = qinfos[0];
                    String score = qinfos[2];
                    double dScore = Double.parseDouble(score);
                    String qTitle = qid.substring(1);
                    if (qTitle.contains("_")) {
                        int n = qTitle.indexOf('_');
                        qTitle = qTitle.substring(0, n);
                    }
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length, String.format("题%s", qTitle));
                        else
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length + attrIds.length,
                                    String.format("题%s", qTitle));
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length, dScore, cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length + attrIds.length, dScore,
                                cellStyle);
                }
                // 以下处理维度得分

                String dimscore = stuExamAnswer.get("dimscore").toString();
                String[] dscores = dimscore.split("#");
                for (int i = 0; i < dscores.length; i++) {
                    String[] dinfos = dscores[i].split(",");
                    String wid = dinfos[0];
                    String dimTitle = scale.getDimensionMap().get(wid).getTitle();
                    String score = dinfos[1];
                    double dScore = Double.parseDouble(score);
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + qscores.length + headFields.length, dimTitle);
                        else
                            WorkbookUtils.setCellValue(sheet, 0,
                                    i + qscores.length + headFields.length + attrIds.length, dimTitle);
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length, dScore,
                                cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length + attrIds.length,
                                dScore, cellStyle);
                }
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getEduStuStream(String countyid, String scaleid, String xd, String nj, String[] headNames,
            String[] headFields, String[] attrIds, String startTime, String endTime) throws FileNotFoundException {
        try {
            Map<?, ?> param = UtilMisc.toMap("countyid", countyid, "xd", xd, "nj", nj, "scaleid", scaleid, "startTime",
                    startTime, "endTime", endTime);
            List<Map> stuExamAnswerList = erStudentMapper.getStudentAnswerInEdu(param);
            HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
            HSSFSheet sheet = workbook.createSheet();
            Scale scale = cachedScaleMgr.get(scaleid, true);
            int qNum = 0;
            final int COL = headNames.length;
            int idx = headNames.length;
            // 不是道德量表，道德量表有自己特定的xls模板
            // if (!ScaleUtils.isMoralityScale(scaleid)) {
            for (int i = 0; i < headNames.length; i++) {
                WorkbookUtils.setCellValue(sheet, 0, i, headNames[i]);
            }
            // 写背景字段表头
            List<FieldValue> attrlist = null;
            attrlist = writeAttrHead(sheet, 1, 0, headNames.length, attrIds);
            // }
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // 写表体
            idx = 1;
            for (Map stuExamAnswer : stuExamAnswerList) {

                for (int i = 0; i < headFields.length; i++) {
                    String headField = headFields[i];
                    if (headField.equals("xbm")) {
                        String xbm = stuExamAnswer.get(headField).toString();
                        if (xbm.equals("1"))
                            WorkbookUtils.setCellValue(sheet, idx, i, "男");
                        else
                            WorkbookUtils.setCellValue(sheet, idx, i, "女");
                    } else
                        WorkbookUtils.setCellValue(sheet, idx, i, stuExamAnswer.get(headFields[i]).toString());
                }
                Object bkg = stuExamAnswer.get("attrs");
                if (bkg != null) {
                    writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                }
                // 以下处理题目得分
                String answer = stuExamAnswer.get("answerscore").toString();
                String[] qscores = answer.split("#");
                for (int i = 0; i < qscores.length; i++) {
                    String[] qinfos = qscores[i].split(",");
                    String qid = qinfos[0];
                    String score = qinfos[2];
                    double dScore = Double.parseDouble(score);
                    String qTitle = qid.substring(1);
                    // if(qTitle.equals("_1")){
                    if (qTitle.contains("_")) {
                        int n = qTitle.indexOf('_');
                        qTitle.substring(0, n);
                    }
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length, String.format("题%s", qTitle));
                        else
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length + attrIds.length,
                                    String.format("题%s", qTitle));
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length, dScore, cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length + attrIds.length, dScore,
                                cellStyle);
                }
                // 以下处理维度得分

                String dimscore = stuExamAnswer.get("dimscore").toString();
                String[] dscores = dimscore.split("#");
                for (int i = 0; i < dscores.length; i++) {
                    String[] dinfos = dscores[i].split(",");
                    String wid = dinfos[0];
                    String dimTitle = scale.getDimensionMap().get(wid).getTitle();
                    String score = dinfos[1];
                    double dScore = Double.parseDouble(score);
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + qscores.length + headFields.length, dimTitle);
                        else
                            WorkbookUtils.setCellValue(sheet, 0,
                                    i + qscores.length + headFields.length + attrIds.length, dimTitle);
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length, dScore,
                                cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length + attrIds.length,
                                dScore, cellStyle);
                }
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getSchoolTchStream(String orgid, String roleid, String scaleId, String[] headNames,
            String[] headFields, String[] attrIds, String startTime, String endTime) throws FileNotFoundException {
        try {
            Map<?, ?> param = UtilMisc.toMap("orgid", orgid, "scaleid", scaleId, "roleid", roleid, "startTime",
                    startTime, "endTime", endTime);
            List<Map> teaExamAnswerList = erTeacherMapper.getTeacherAnswerInSchool(param);
            HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
            HSSFSheet sheet = workbook.createSheet();
            Scale scale = cachedScaleMgr.get(scaleId, true);
            int qNum = 0;
            final int COL = headNames.length;
            int idx = headNames.length;
            // 不是道德量表，道德量表有自己特定的xls模板
            // if (!ScaleUtils.isMoralityScale(scaleId)) {
            for (int i = 0; i < headNames.length; i++) {
                WorkbookUtils.setCellValue(sheet, 0, i, headNames[i]);
            }
            // 写背景字段表头
            List<FieldValue> attrlist = null;
            attrlist = writeAttrHead(sheet, 1, 0, headNames.length, attrIds);
            // }
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // 写表体
            idx = 1;
            for (Map teaExamAnswer : teaExamAnswerList) {

                for (int i = 0; i < headFields.length; i++) {
                    String headField = headFields[i];
                    if (headField.equals("xbm")) {
                        String xbm = teaExamAnswer.get(headField).toString();
                        if (xbm.equals("1"))
                            WorkbookUtils.setCellValue(sheet, idx, i, "男");
                        else
                            WorkbookUtils.setCellValue(sheet, idx, i, "女");
                    } else
                        WorkbookUtils.setCellValue(sheet, idx, i, teaExamAnswer.get(headFields[i]) == null ? ""
                                : teaExamAnswer.get(headFields[i]).toString());
                }
                Object bkg = teaExamAnswer.get("attrs");
                if (bkg != null) {
                    writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                }
                // 以下处理题目得分
                String answer = teaExamAnswer.get("answerscore").toString();
                String[] qscores = answer.split("#");
                for (int i = 0; i < qscores.length; i++) {
                    String[] qinfos = qscores[i].split(",");
                    String qid = qinfos[0];
                    String score = qinfos[2];
                    double dScore = Double.parseDouble(score);
                    String qTitle = qid.substring(1);
                    if (qTitle.contains("_")) {
                        int n = qTitle.indexOf('_');
                        qTitle.substring(0, n);
                    }
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length, String.format("题%s", qTitle));
                        else
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length + attrIds.length,
                                    String.format("题%s", qTitle));
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length, dScore, cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length + attrIds.length, dScore,
                                cellStyle);
                }
                // 以下处理维度得分

                String dimscore = teaExamAnswer.get("dimscore").toString();
                String[] dscores = dimscore.split("#");
                for (int i = 0; i < dscores.length; i++) {
                    String[] dinfos = dscores[i].split(",");
                    String wid = dinfos[0];
                    String dimTitle = scale.getDimensionMap().get(wid).getTitle();
                    String score = dinfos[1];
                    double dScore = Double.parseDouble(score);
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + qscores.length + headFields.length, dimTitle);
                        else
                            WorkbookUtils.setCellValue(sheet, 0,
                                    i + qscores.length + headFields.length + attrIds.length, dimTitle);
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length, dScore,
                                cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length + attrIds.length,
                                dScore, cellStyle);
                }
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getEduTchStream(String countyid, String roleid, String roleName, String scaleId,
            String[] headNames, String[] headFields, String[] attrIds, String startTime, String endTime)
            throws FileNotFoundException {
        try {
            Map<?, ?> param = UtilMisc.toMap("countyid", countyid, "roleid", roleid, "scaleid", scaleId, "startTime",
                    startTime, "endTime", endTime);
            List<Map> teaExamAnswerList = erTeacherMapper.getTeacherAnswerInEdu(param);

            HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
            HSSFSheet sheet = workbook.createSheet();
            Scale scale = cachedScaleMgr.get(scaleId, true);
            int qNum = 0;
            final int COL = headNames.length;
            int idx = headNames.length;
            // 不是道德量表，道德量表有自己特定的xls模板
            // if (!ScaleUtils.isMoralityScale(scaleId)) {
            for (int i = 0; i < headNames.length; i++) {
                WorkbookUtils.setCellValue(sheet, 0, i, headNames[i]);
            }
            // 写背景字段表头
            List<FieldValue> attrlist = null;
            attrlist = writeAttrHead(sheet, 1, 0, headNames.length, attrIds);
            // }
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // 写表体
            idx = 1;
            for (Map teaExamAnswer : teaExamAnswerList) {

                for (int i = 0; i < headFields.length; i++) {
                    String headField = headFields[i];
                    if (headField.equals("xbm")) {
                        String xbm = teaExamAnswer.get(headField).toString();
                        if (xbm.equals("1"))
                            WorkbookUtils.setCellValue(sheet, idx, i, "男");
                        else
                            WorkbookUtils.setCellValue(sheet, idx, i, "女");
                    } else
                        WorkbookUtils.setCellValue(sheet, idx, i, teaExamAnswer.get(headField).toString());
                }
                Object bkg = teaExamAnswer.get("attrs");
                if (bkg != null) {
                    writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                }
                // 以下处理题目得分
                String answer = teaExamAnswer.get("answerscore").toString();
                String[] qscores = answer.split("#");
                for (int i = 0; i < qscores.length; i++) {
                    String[] qinfos = qscores[i].split(",");
                    String qid = qinfos[0];
                    String score = qinfos[2];
                    double dScore = Double.parseDouble(score);
                    String qTitle = qid.substring(1);
                    // if(qTitle.equals("_1")){
                    if (qTitle.contains("_")) {
                        int n = qTitle.indexOf('_');
                        qTitle.substring(0, n);
                    }
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length, String.format("题%s", qTitle));
                        else
                            WorkbookUtils.setCellValue(sheet, 0, i + headFields.length + attrIds.length,
                                    String.format("题%s", qTitle));
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length, dScore, cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + headFields.length + attrIds.length, dScore,
                                cellStyle);
                }
                // 以下处理维度得分

                String dimscore = teaExamAnswer.get("dimscore").toString();
                String[] dscores = dimscore.split("#");
                for (int i = 0; i < dscores.length; i++) {
                    String[] dinfos = dscores[i].split(",");
                    String wid = dinfos[0];
                    String dimTitle = scale.getDimensionMap().get(wid).getTitle();
                    String score = dinfos[1];
                    double dScore = Double.parseDouble(score);
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + qscores.length + headFields.length, dimTitle);
                        else
                            WorkbookUtils.setCellValue(sheet, 0,
                                    i + qscores.length + headFields.length + attrIds.length, dimTitle);
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length, dScore,
                                cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headFields.length + attrIds.length,
                                dScore, cellStyle);
                }
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<FieldValue> writeAttrHead(HSSFSheet sheet, int typeFlag, int rowN, int startCol, String[] attrIds) {
        if (attrIds == null) {
            return Collections.emptyList();
        }
        PropObject propObject = PropObject.createPropObject(typeFlag);
        propObject.loadMetas();
        List<FieldValue> list = propObject.getAttrs();
        List<FieldValue> result = new ArrayList<FieldValue>(list.size());
        for (String attrId : attrIds) {
            for (FieldValue fieldValue : list) {
                if (StringUtils.equals(attrId, fieldValue.getId())) {
                    WorkbookUtils.setCellValue(sheet, rowN, startCol++, fieldValue.getLabel());
                    result.add(fieldValue);
                }
            }
        }
        return result;
    }

    private void writeAttrs(HSSFSheet sheet, int typeFlag, String backGrand, int rowN, int startCol,
            List<FieldValue> list) {

        PropObject propObject = PropObject.createPropObject(typeFlag);
        propObject.setBackGrand(backGrand);
        Map map = null;
        try {
            map = Pools.getInstance().borrowMap();
            propObject.loadBackGrandToMap(map);
            for (FieldValue fieldValue : list) {
                WorkbookUtils.setCellValue(sheet, rowN, startCol++, (String) map.get(fieldValue.getId()));
            }
        } finally {
            Pools.getInstance().returnMap(map);
        }
    }

	public void downloadAllStudentResult(String[] headnames, String[] headfields, String startTime, String endTime,
			OutputStream outputStream)  throws FileNotFoundException {
		InputStream is = null;
        endTime += " 23:59:59";
        try {
            is = getAllSchoolTchStream(headnames, headfields,startTime, endTime);
            IOUtils.copy(is, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(is);
        }
		
	}

	private InputStream getAllSchoolTchStream(String[] headnames, String[] headfields, String startTime,
			String endTime) {
		try {
            Map<?, ?> param = UtilMisc.toMap(
                    "startTime", startTime, "endTime", endTime);
            List<Map> stuExamAnswerList = erStudentMapper.getStudentAllResult(param);
            HSSFWorkbook workbook = WorkbookUtils.newWorkbook(false);
            HSSFSheet sheet = workbook.createSheet();
            //Scale scale = cachedScaleMgr.get(scaleId, true);
            String[] attrIds = null;
            final int COL = headnames.length;
            int qNum = 0;
            int idx = headnames.length;
            List<FieldValue> attrlist = null;
            // 不是道德量表，道德量表有自己特定的xls模板
            // if (!ScaleUtils.isMoralityScale(scaleId)) {
            for (int i = 0; i < headnames.length; i++) {
                WorkbookUtils.setCellValue(sheet, 0, i, headnames[i]);
            }
            // 写背景字段表头
            attrlist = writeAttrHead(sheet, 1, 0, headnames.length, attrIds);
            // }
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // 写表体
            idx = 1;
            for (Map stuExamAnswer : stuExamAnswerList) {
                // 写基础信息字段值
                for (int i = 0; i < headfields.length; i++) {
                    String headField = headfields[i];
                    if (headField.equals("xbm")) {
                        String xbm = stuExamAnswer.get(headField).toString();
                        if (xbm.equals("1"))
                            WorkbookUtils.setCellValue(sheet, idx, i, "男");
                        else
                            WorkbookUtils.setCellValue(sheet, idx, i, "女");
                    } else if(headField.equals("xxmc")){
                    	String string2 = headfields[i];
                    	String string = stuExamAnswer.get(headField).toString();
                    	String xxmc = schoolMapper.getSchoolnameByOrgid(stuExamAnswer.get(headField).toString());
                    	WorkbookUtils.setCellValue(sheet, idx, i, xxmc);
                    } else if(headField.equals("scalename")){
                    	String scalename = scaleMapper.getScaleName(stuExamAnswer.get(headField).toString());
                    	WorkbookUtils.setCellValue(sheet, idx, i, scalename);
                    } else
                        WorkbookUtils.setCellValue(sheet, idx, i, stuExamAnswer.get(headfields[i]).toString());
                }
                Object bkg = stuExamAnswer.get("attrs");
                if (bkg != null) {
                    writeAttrs(sheet, 1, bkg.toString(), idx, COL, attrlist);
                }
                // 以下处理题目得分
                String answer = stuExamAnswer.get("answerscore").toString();
                String[] qscores = answer.split("#");
                for (int i = 0; i < qscores.length; i++) {
                    String[] qinfos = qscores[i].split(",");
                    String qid = qinfos[0];
                    String score = qinfos[2];
                    double dScore = Double.parseDouble(score);
                    String qTitle = qid.substring(1);
                    if (qTitle.contains("_")) {
                        int n = qTitle.indexOf('_');
                        qTitle = qTitle.substring(0, n);
                    }
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + headfields.length, String.format("题%s", qTitle));
                        else
                            WorkbookUtils.setCellValue(sheet, 0, i + headfields.length + attrIds.length,
                                    String.format("题%s", qTitle));
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + headfields.length, dScore, cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + headfields.length + attrIds.length, dScore,
                                cellStyle);
                }
                // 以下处理维度得分

                String dimscore = stuExamAnswer.get("dimscore").toString();
                String[] dscores = dimscore.split("#");
                for (int i = 0; i < dscores.length; i++) {
                    String[] dinfos = dscores[i].split(",");
                    String wid = dinfos[0];
                    String dimTitle = "所有测试数据。xls";
                    String score = dinfos[1];
                    double dScore = Double.parseDouble(score);
                    if (idx == 1) {
                        if (attrIds == null)
                            WorkbookUtils.setCellValue(sheet, 0, i + qscores.length + headfields.length, dimTitle);
                        else
                            WorkbookUtils.setCellValue(sheet, 0,
                                    i + qscores.length + headfields.length + attrIds.length, dimTitle);
                    }
                    if (attrIds == null)
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headfields.length, dScore,
                                cellStyle);
                    else
                        WorkbookUtils.setCellValue(sheet, idx, i + qscores.length + headfields.length + attrIds.length,
                                dScore, cellStyle);
                }
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
}
