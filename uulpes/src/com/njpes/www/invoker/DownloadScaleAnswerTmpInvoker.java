package com.njpes.www.invoker;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.systeminfo.ExamdoStudentMapper;
import com.njpes.www.dao.systeminfo.ExamdoTeacherMapper;
import com.njpes.www.utils.Zip;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Question;
import edutec.scale.model.Scale;
import edutec.scale.util.ScaleUtils;
import heracles.excel.WorkbookUtils;
import heracles.util.UtilMisc;
import heracles.web.config.ApplicationConfiguration;

//import edutec.scale.util.SchoolUtils;
@Service("downloadScaleAnswerTmpInvoker")
public class DownloadScaleAnswerTmpInvoker {
    public static final int COL_START = 9;
    public static final int START_ROW = 4;
    private String attachmentFileName;
    private String relatedFileName;
    @Autowired
    public CachedScaleMgr cachedScaleMgr;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private TeacherMapper teacherDao;
    @Autowired
    private ExamdoStudentMapper examdoStudentMapper;
    @Autowired
    private ExamdoTeacherMapper examdoTeacherMapper;

    public void downloadStudentAnswerTemp(long orgid, long gradeid, long bjid, String scaleId, int groupId,
            OutputStream os) {
        InputStream is = null;
        try {
            List<Map> userList;
            if (bjid > 0) {
                Map<?, ?> param = UtilMisc.toMap("orgid", orgid, "classid", bjid, "scaleid", scaleId);
                userList = examdoStudentMapper.queryExamdoStudentsByClassId(param);
            } else {
                Map<?, ?> param = UtilMisc.toMap("orgid", orgid, "gradeid", gradeid, "scaleid", scaleId);
                userList = examdoStudentMapper.queryExamdoStudentsByGradeId(param);
            }
            if (ScaleUtils.isThirdAngleScale(scaleId)) {
                is = batchExport2007(userList, scaleId);
            } else {
                is = getStudentInputStream2007(userList, scaleId);
            }
            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public void downloadTeacherAnswerTemp(long orgid, String roleName, long teacherRole, String scaleId,
            OutputStream os) {
        InputStream is = null;
        try {
            // long teacherRole =
            // Integer.parseInt(params.get("teacherRole").toString());
            // TeacherQueryParam teacher = new TeacherQueryParam();
            // teacher.setRoleId(String.valueOf(teacherRole));
            // List<Teacher> userList = teacherDao.getAllTeachers(teacher,
            // orgid, null);//getTeachers(String.valueOf(teacherRole),
            // String.valueOf(orgid));
            Map<?, ?> param = UtilMisc.toMap("orgid", orgid, "roleid", teacherRole, "scaleid", scaleId);
            List<Map> userList = examdoTeacherMapper.queryExamdoTeachersByRoleId(param);
            // is =
            // getTeacherInputStream(userList,teacherRole,roleName,scaleId);
            is = getTeacherInputStream2007(userList, teacherRole, roleName, scaleId);
            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    public InputStream batchExport(List<Map> userList, String scaleId) {
        try {
            List<File> srcfile = new ArrayList<File>();
            String folder = ApplicationConfiguration.getInstance().getWorkDir() + File.separator + "download";
            // 在服务器端创建文件夹
            // String auto_name =
            // PrimaryKeyGenerator.getShortStringKey();//自动的生成一个文件码
            File myfolder = new File(folder);
            if (!myfolder.exists()) {
                myfolder.mkdir();
            }
            String[] thirdAngleScaleIDs = null;
            String thirdAngleScales[] = null;
            if (ScaleUtils.isThirdAngleScaleP(scaleId)) {
                // if(scaleId.equals("111000001")||scaleId.equals("111000002")||scaleId.equals("111000003")){
                // thirdAngleScaleIDs = new
                // String[]{"100011","100012","100013"};
                thirdAngleScaleIDs = ScaleUtils.H_THIRD_ANGLE_SCALES_P;
                thirdAngleScales = new String[] { "小学生心理健康量表-学生版", "小学生心理健康量表-家长版", "小学生心理健康量表-教师版" };
            }
            if (ScaleUtils.isThirdAngleScaleM(scaleId)) {
                // if(scaleId.equals("200011")||scaleId.equals("200012")||scaleId.equals("200013")){
                // thirdAngleScaleIDs = new
                // String[]{"200011","200012","200013"};
                thirdAngleScaleIDs = ScaleUtils.H_THIRD_ANGLE_SCALES_M;
                thirdAngleScales = new String[] { "中学生心理健康量表-学生版", "中学生心理健康量表-家长版", "中学生心理健康量表-教师版" };
            }
            // if(scaleId.equals("100011")||scaleId.equals("100012")||scaleId.equals("100013"))
            // {
            UUID uuid = UUID.randomUUID();
            String uid = UUID.randomUUID().toString();
            for (int i = 0; i < 3; i++) {
                Scale scale = cachedScaleMgr.get(thirdAngleScaleIDs[i], true);
                if (scale == null)
                    break;
                InputStream in = null;
                ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/studentanswer.xls");
                in = new BufferedInputStream(new FileInputStream(fullFileName));

                String filename = thirdAngleScales[i];

                HSSFWorkbook workbook = WorkbookUtils.openWorkbook(in);
                HSSFSheet sheet = null;

                sheet = workbook.getSheetAt(0);

                WorkbookUtils.setCellValue(sheet, 2, 1, thirdAngleScaleIDs[i] + "-1");
                WorkbookUtils.setCellValue(sheet, 2, 2, scale.getTitle());
                WorkbookUtils.setCellValue(sheet, 2, 3, "三角视识别码");

                WorkbookUtils.setCellValue(sheet, 2, 4, uid);
                int idx = 4;
                // List<Question> qList = scale.getQuestions();
                // boolean genderQ = false;
                List<Question> qList = scale.getQuestions();
                for (int j = 0; j < qList.size(); j++) {
                    String qId = qList.get(j).getId();
                    if (qId.contains("QN"))
                        continue;
                    if (qId.contains("QPD"))
                        continue;
                    if (qId.contains("_1")) {
                        qId = qId.split("_")[0];
                    }
                    if (qId.contains("_2")) {
                        continue;
                    }
                    WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx, String.format("%s", qId));
                    idx++;
                }
                // for(int j=0;j<scale.getQuestionNum();j++){
                // WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx,
                // String.format("题%d", j+1));
                // idx++;
                // }
                // 写表体

                idx = START_ROW;
                for (Map student : userList) {
                    if (student.get("sfzjh") == null)
                        continue;
                    WorkbookUtils.setCellValue(sheet, idx, 0,
                            student.get("sfzjh") == null ? "" : student.get("sfzjh").toString());
                    WorkbookUtils.setCellValue(sheet, idx, 1, student.get("xm").toString());
                    WorkbookUtils.setCellValue(sheet, idx, 2, student.get("xbm").toString().equals("1") ? "男" : "女");
                    WorkbookUtils.setCellValue(sheet, idx, 3,
                            student.get("njmc").toString() + student.get("bjmc").toString());
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

                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(os);
            }
            // edutec.admin.CompressUtils.zip(folder, folder + ".zip");
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

    public InputStream batchExport2007(List<Map> userList, String scaleId) {
        try {
            String folder = ApplicationConfiguration.getInstance().getWorkDir() + File.separator + "download";
            String folderName = folder;
            String[] thirdAngleScaleIDs = null;
            String thirdAngleScales[] = null;
            if (ScaleUtils.isThirdAngleScaleP(scaleId)) {
                folderName = folder + File.separator + "小学生心理健康量表答案导入模板";
                thirdAngleScaleIDs = ScaleUtils.H_THIRD_ANGLE_SCALES_P;
                thirdAngleScales = new String[] { "小学生心理健康量表-学生版", "小学生心理健康量表-家长版", "小学生心理健康量表-教师版" };
            }
            if (ScaleUtils.isThirdAngleScaleM(scaleId)) {
                folderName = folder + File.separator + "中学生心理健康量表答案导入模板";
                thirdAngleScaleIDs = ScaleUtils.H_THIRD_ANGLE_SCALES_M;
                thirdAngleScales = new String[] { "中学生心理健康量表-学生版", "中学生心理健康量表-家长版", "中学生心理健康量表-教师版" };
            }
            File myfolder = new File(folderName);
            if (!myfolder.exists()) {
                myfolder.mkdir();
            }
            String uid = UUID.randomUUID().toString();
            for (int i = 0; i < 3; i++) {
                Scale scale = cachedScaleMgr.get(thirdAngleScaleIDs[i], true);
                if (scale == null)
                    break;
                InputStream in = null;
                ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/studentanswer.xlsx");
                in = new BufferedInputStream(new FileInputStream(fullFileName));

                String filename = thirdAngleScales[i];
                XSSFWorkbook workbook = new XSSFWorkbook(in);
                XSSFSheet sheet = null;
                sheet = workbook.getSheetAt(0);

                WorkbookUtils.setCellValue(sheet, 2, 1, thirdAngleScaleIDs[i] + "-1");
                WorkbookUtils.setCellValue(sheet, 2, 2, scale.getTitle());
                WorkbookUtils.setCellValue(sheet, 2, 3, "三角视识别码");

                WorkbookUtils.setCellValue(sheet, 2, 4, uid);
                int idx = 4;
                List<Question> qList = scale.getQuestions();
                for (int j = 0; j < qList.size(); j++) {
                    String qId = qList.get(j).getId();
                    if (qId.contains("QN"))
                        continue;
                    if (qId.contains("QPD"))
                        continue;
                    if (qId.contains("_1")) {
                        qId = qId.split("_")[0];
                    }
                    if (qId.contains("_2")) {
                        continue;
                    }
                    WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx, String.format("%s", qId));
                    idx++;
                }
                idx = START_ROW;
                for (Map student : userList) {
                    if (student.get("sfzjh") == null)
                        continue;
                    WorkbookUtils.setCellValue(sheet, idx, 0,
                            student.get("sfzjh") == null ? "" : student.get("sfzjh").toString());
                    WorkbookUtils.setCellValue(sheet, idx, 1, student.get("xm").toString());
                    WorkbookUtils.setCellValue(sheet, idx, 2, student.get("xbm").toString().equals("1") ? "男" : "女");
                    WorkbookUtils.setCellValue(sheet, idx, 3,
                            student.get("njmc").toString() + student.get("bjmc").toString());
                    idx++;
                }

                File f = new File(myfolder + File.separator + filename + ".xlsx");
                f.mkdirs();
                if (f.exists()) {
                    f.delete();
                } else {
                    f.createNewFile();
                }
                OutputStream os = new FileOutputStream(f);
                workbook.write(os);
                os.flush();
                os.close();

                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(os);
            }
            Zip.zip(folderName, folderName + ".zip");
            File file = new File(folderName + ".zip");
            FileInputStream inputStream = new FileInputStream(file);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getStudentInputStream(List<Map> userList, String scaleId) throws FileNotFoundException {
        try {
            InputStream in = null;
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            if (ScaleUtils.isMoralityScale(scaleId)) {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/ms-tpl.xls");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            } else {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/studentanswer.xls");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            }
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = null;
            sheet = workbook.getSheetAt(0);
            Scale scale = cachedScaleMgr.get(scaleId, true);
            WorkbookUtils.setCellValue(sheet, 2, 1, scaleId + "-1");
            WorkbookUtils.setCellValue(sheet, 2, 2, scale.getTitle());

            int idx = 4;
            // 不是道德量表，道德量表有自己特定的xls模板
            if (!ScaleUtils.isMoralityScale(scaleId)) {
                // List<Question> qList = scale.getQuestions();
                // boolean genderQ = false;
                int j = 0;
                List<Question> qList = scale.getQuestions();
                for (int i = 0; i < qList.size(); i++) {
                    String qId = qList.get(i).getId();
                    if (qId.contains("QN"))
                        continue;
                    if (qId.contains("QPD"))
                        continue;
                    if (qId.contains("_1")) {
                        qId = qId.split("_")[0];
                    }
                    if (qId.contains("_2")) {
                        continue;
                    }
                    WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx, String.format("%s", qId));
                    idx++;
                }
                // for(int i=0;i<scale.getQuestionNum();i++){
                // WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx,
                // String.format("题%d", i+1));
                // idx++;
                // }

            }
            // 写表体
            idx = START_ROW;
            for (Map student : userList) {
                if (student.get("xh") == null)
                    continue;
                WorkbookUtils.setCellValue(sheet, idx, 0,
                        student.get("xh") == null ? "" : student.get("xh").toString());
                WorkbookUtils.setCellValue(sheet, idx, 1, student.get("xm").toString());
                WorkbookUtils.setCellValue(sheet, idx, 2, student.get("xbm").equals("1") ? "男" : "女");
                WorkbookUtils.setCellValue(sheet, idx, 3,
                        student.get("njmc").toString() + student.get("bjmc").toString());
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getStudentInputStream2007(List<Map> userList, String scaleId) throws FileNotFoundException {
        try {
            InputStream in = null;
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            if (ScaleUtils.isMoralityScale(scaleId)) {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/ms-tpl.xlsx");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            } else {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/studentanswer.xlsx");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            }
            // HSSFWorkbook workbook = WorkbookUtils.openWorkbook(in);
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = null;
            sheet = workbook.getSheetAt(0);
            Scale scale = cachedScaleMgr.get(scaleId, true);
            WorkbookUtils.setCellValue(sheet, 2, 1, scaleId + "-1");
            WorkbookUtils.setCellValue(sheet, 2, 2, scale.getTitle());

            int idx = 4;
            // 不是道德量表，道德量表有自己特定的xls模板
            if (!ScaleUtils.isMoralityScale(scaleId)) {
                // List<Question> qList = scale.getQuestions();
                // boolean genderQ = false;
                int j = 0;
                List<Question> qList = scale.getQuestions();
                for (int i = 0; i < qList.size(); i++) {
                    String qId = qList.get(i).getId();
                    if (qId.contains("QN"))
                        continue;
                    if (qId.contains("QPD"))
                        continue;
                    if (qId.contains("_1")) {
                        qId = qId.split("_")[0];
                    }
                    if (qId.contains("_2")) {
                        continue;
                    }
                    WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx, String.format("%s", qId));
                    idx++;
                }
                // for(int i=0;i<scale.getQuestionNum();i++){
                // WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx,
                // String.format("题%d", i+1));
                // idx++;
                // }

            }
            // 写表体
            idx = START_ROW;
            for (Map student : userList) {
                if (student.get("sfzjh") == null)
                    continue;
                WorkbookUtils.setCellValue(sheet, idx, 0,
                        student.get("sfzjh") == null ? "" : student.get("sfzjh").toString());
                WorkbookUtils.setCellValue(sheet, idx, 1, student.get("xm").toString());
                WorkbookUtils.setCellValue(sheet, idx, 2, student.get("xbm").equals("1") ? "男" : "女");
                WorkbookUtils.setCellValue(sheet, idx, 3,
                        student.get("njmc").toString() + student.get("bjmc").toString());
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getTeacherInputStream(List<Map> userList, long teacherRole, String roleName, String scaleId)
            throws FileNotFoundException {
        try {
            InputStream in = null;
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            if (ScaleUtils.isMoralityScale(scaleId)) {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/ms-tpl.xls");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            } else {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/teacheranswer.xls");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            }
            HSSFWorkbook workbook = WorkbookUtils.openWorkbook(in);
            HSSFSheet sheet = null;
            sheet = workbook.getSheetAt(0);
            Scale scale = cachedScaleMgr.get(scaleId, true);
            WorkbookUtils.setCellValue(sheet, 2, 1, scaleId + "-1");
            WorkbookUtils.setCellValue(sheet, 2, 2, scale.getTitle());

            int idx = 4;

            // 不是道德量表，道德量表有自己特定的xls模板
            if (!ScaleUtils.isMoralityScale(scaleId)) {
                // List<Question> qList = scale.getQuestions();
                // boolean genderQ = false;
                int j = 0;
                List<Question> qList = scale.getQuestions();
                for (int i = 0; i < qList.size(); i++) {
                    String qId = qList.get(i).getId();
                    if (qId.contains("QN"))
                        continue;
                    if (qId.contains("QPD"))
                        continue;
                    if (qId.contains("_1")) {
                        qId = qId.split("_")[0];
                    }
                    if (qId.contains("_2")) {
                        continue;
                    }
                    WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx, String.format("%s", qId));
                    idx++;
                }

            }
            // 写表体
            idx = START_ROW;
            for (Map teacher : userList) {
                if (teacher.get("sfzjh") == null)
                    continue;
                WorkbookUtils.setCellValue(sheet, idx, 0, teacher.get("sfzjh").toString());
                WorkbookUtils.setCellValue(sheet, idx, 1, teacher.get("xm").toString());
                WorkbookUtils.setCellValue(sheet, idx, 2, teacher.get("xbm").toString().equals("1") ? "男" : "女");
                WorkbookUtils.setCellValue(sheet, idx, 3, roleName);
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream getTeacherInputStream2007(List<Map> userList, long teacherRole, String roleName,
            String scaleId) throws FileNotFoundException {
        try {
            InputStream in = null;
            ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
            if (ScaleUtils.isMoralityScale(scaleId)) {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/ms-tpl.xlsx");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            } else {
                String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/teacheranswer.xlsx");
                in = new BufferedInputStream(new FileInputStream(fullFileName));
            }
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = null;
            sheet = workbook.getSheetAt(0);
            Scale scale = cachedScaleMgr.get(scaleId, true);
            WorkbookUtils.setCellValue(sheet, 2, 1, scaleId + "-1");
            WorkbookUtils.setCellValue(sheet, 2, 2, scale.getTitle());

            int idx = 4;

            // 不是道德量表，道德量表有自己特定的xls模板
            if (!ScaleUtils.isMoralityScale(scaleId)) {
                // List<Question> qList = scale.getQuestions();
                // boolean genderQ = false;
                int j = 0;
                List<Question> qList = scale.getQuestions();
                for (int i = 0; i < qList.size(); i++) {
                    String qId = qList.get(i).getId();
                    if (qId.contains("QN"))
                        continue;
                    if (qId.contains("QPD"))
                        continue;
                    if (qId.contains("_1")) {
                        qId = qId.split("_")[0];
                    }
                    if (qId.contains("_2")) {
                        continue;
                    }
                    WorkbookUtils.setCellValue(sheet, START_ROW - 1, idx, String.format("%s", qId));
                    idx++;
                }

            }
            // 写表体
            idx = START_ROW;
            for (Map teacher : userList) {
                if (teacher.get("sfzjh") == null)
                    continue;
                WorkbookUtils.setCellValue(sheet, idx, 0, teacher.get("sfzjh").toString());
                WorkbookUtils.setCellValue(sheet, idx, 1, teacher.get("xm").toString());
                WorkbookUtils.setCellValue(sheet, idx, 2, teacher.get("xbm").toString().equals("1") ? "男" : "女");
                WorkbookUtils.setCellValue(sheet, idx, 3,
                        teacher.get("rolename") == null ? "" : teacher.get("rolename").toString());
                idx++;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(output);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getFullFileName() {
        ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
        String fullFileName = FilenameUtils.concat(cfg.getWorkDir(), relatedFileName);
        return fullFileName;
    }
}
