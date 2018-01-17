package com.njpes.www.invoker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.assessmentcenter.ExamdoMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
//import com.njpes.www.entity.baseinfo.UserInfo;
//import com.njpes.www.service.scaletoollib.DBException;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exam.ExamResult;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.Scale;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.MuiltQuestionCalc;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.questionnaire.QuestionnaireAnswerImportStoreListener;
import edutec.scale.questionnaire.QuestionnaireCalcListener;
import edutec.scale.questionnaire.QuestionnaireCompistionListener;
import edutec.scale.util.ScaleUtils;
import heracles.excel.WorkbookUtils;
import heracles.util.UtilCollection;
import heracles.util.UtilDateTime;
import heracles.util.UtilMisc;

//import edutec.scale.util.SchoolUtils;
@Service("ScaleAnswerInvoker")
public class ScaleAnswerInvoker {
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private TeacherMapper teacherDao;
    @Autowired
    ExamdoMapper examDoMapper;
    @Autowired
    private ExamResultMapper examResultDao;
    @Autowired
    private PlatformTransactionManager txManager;
    public static final int COL_START = 9;
    public static final int START_ROW = 4;
    // private StringBuilder errSb;
    // private Questionnaire questionnaire;
    // private QuestionnaireCompistionListener listener;
    // private String scaleId;
    // private int groupId;
    // private IntList qIdxList = new ArrayIntList();
    // private int questionIdx = -1; // 由-1开始
    // private String currentJudgeId;//当前判断题id
    // private String judgeAnswer;//判断题答案

    public ScaleAnswerInvoker() {
        // questionIdx = -1;
        // questionnaire = null;
        // currentJudgeId = "";
    }

    public void importTeacherAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception {
        Questionnaire questionnaire = null;
        QuestionnaireCompistionListener listener = null;
        IntList qIdxList = new ArrayIntList();
        List resulMsgList = new ArrayList();
        String scaleId = null;
        page.put("resultMsgList", resulMsgList);
        page.put("answerimport", true);
        // HSSFWorkbook wb;
        XSSFWorkbook wb;
        // try {
        // wb = WorkbookUtils.openWorkbook(inputStream);
        wb = new XSSFWorkbook(inputStream);
        // HSSFSheet sheet = wb.getSheetAt(0);
        XSSFSheet sheet = wb.getSheetAt(0);
        scaleId = WorkbookUtils.getStringCellValue(sheet, 2, 1);

        List<String> list = UtilCollection.toList(scaleId, '-');
        if (list.size() != 2) {
            resulMsgList.add("量表ID号被更动，请重新下载模板！");
            return;
        }
        scaleId = list.get(0);
        int groupId = NumberUtils.toInt(list.get(1));
        if (StringUtils.isBlank(scaleId)) {
            resulMsgList.add("没有量表id<br>");
            return;
        }
        questionnaire = SpringContextHolder.getBean("Questionnaire", Questionnaire.class);
        listener = SpringContextHolder.getBean("QuestionnaireCompistionListener",
                QuestionnaireCompistionListener.class);
        buildQuestionnaire(questionnaire, listener, scaleId);
        int row = START_ROW;
        // HSSFRow hrow = null;
        XSSFRow hrow = null;
        // TeacherQueryParam tq = new TeacherQueryParam();
        int totalNum = 0;
        int validNum = 0;
        List<ExamResult> erList = new ArrayList<ExamResult>();
        page.put("erList", erList);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            while (true) {
                hrow = sheet.getRow(row);
                if (hrow == null)
                    break;
                totalNum++;
                String sfzjh = WorkbookUtils.getStringCellValue(sheet, row, 0);// 身份证号
                String xm = WorkbookUtils.getStringCellValue(sheet, row, 1);// 姓名
                if (StringUtils.isBlank(sfzjh)) {
                    break;
                }
                // 根据学号和机构id查找学生
                // tq.setSfzjh(sfzjh);
                // List<Teacher> teacherList = teacherDao.getAllTeachers(tq,
                // orgid,null);
                Teacher teacher = teacherDao.getTeacherByIdentify(sfzjh);
                if (teacher == null) {
                    txManager.rollback(status);
                    row++;
                    resulMsgList.add("姓名为:" + xm + ",身份证号为:" + sfzjh + "的老师不存在<br>");
                    txManager.rollback(status);
                    throw new Exception("姓名为:" + xm + ",身份证号为:" + sfzjh + "的老师不存在!");
                    // continue;
                }
                // Teacher teacher = teacherList.get(0);
                Map<?, ?> param = UtilMisc.toMap("teacherid", teacher.getId(), "scaleid", scaleId);
                List<ExamdoTeacher> examdoTeacherList = examDoMapper.selectTeacherExamdoByTeacherId(param);
                if (examdoTeacherList == null)// 没有分发记录
                {
                    row++;
                    resulMsgList.add("姓名为:" + xm + "身份证号为:" + sfzjh + "的老师没有符合条件的分发记录！<br>");
                    txManager.rollback(status);
                    throw new Exception("姓名为:" + xm + "身份证号为:" + sfzjh + "的老师没有符合条件的分发记录!");
                    // continue;
                } else {
                    if (examdoTeacherList.size() == 0) {
                        row++;
                        resulMsgList.add("姓名为:" + xm + "身份证号为:" + sfzjh + "的老师没有符合条件的分发记录！<br>");
                        txManager.rollback(status);
                        throw new Exception("姓名为:" + xm + "身份证号为:" + sfzjh + "的老师没有符合条件的分发记录!");
                        // continue;
                    }
                }
                ExamdoTeacher examdoTeacher = examdoTeacherList.get(0);
                questionnaire.setExamdo(examdoTeacher);
                questionnaire.setResultId(examdoTeacher.getResultid());
                questionnaire.setStartTime(UtilDateTime.nowTimestamp());
                questionnaire.setTesttype(2);
                questionnaire.setSubjectUserInfo(teacher);
                listener.setSubjectUserInfo(teacher);
                Scale scale = questionnaire.getScale();
                int colStart = 4;
                boolean hasDataError = false;
                // 填充答案
                List<Question> qList = questionnaire.getQuestions();
                // 如果是道德量表
                if (ScaleUtils.isMoralityScale(scaleId)) {
                    StringBuilder sb = new StringBuilder();
                    boolean xx = scaleId.startsWith("1");
                    for (int i = 0; i < qIdxList.size(); ++i) {

                    }
                } else {
                    int colOff = 0;
                    for (int i = 0; i < qList.size(); i++) {
                        hasDataError = false;
                        String qId = qList.get(i).getId();
                        if (qId.contains("QN"))
                            continue;
                        if (qId.contains("QPD"))
                            continue;
                        // 处理判断题
                        if (teacher.getXbm().equals("1")) {// 男生
                            if (qId.contains("_2"))
                                continue;
                        } else {// 女生
                            if (qId.contains("_1"))
                                continue;
                        }
                        Question q = qList.get(i);
                        String answer = WorkbookUtils.getStringCellValue(sheet, row, colOff + colStart);
                        if (StringUtil.isEmpty(answer)) {
                            hasDataError = true;
                            String err = String.format("第%d行，第%d格是空格!<br>", row + 1, colOff + colStart + 1);
                            resulMsgList.add(err);
                            txManager.rollback(status);
                            throw new Exception(err);
                            // break;
                        }
                        answer = UtilMisc.toDBCase(answer);
                        // int iaw = NumberUtils.toInt(answer) - 1;
                        int iaw = 0;
                        String maw = null;
                        int[] selectedIdx = null;
                        if (q instanceof SelectionQuestion) {// 如果是多选题，得分为所填选项值
                            SelectionQuestion sq = (SelectionQuestion) q;
                            if (sq.getChoiceMode() == QuestionConsts.CHOICE_SINGLE_MODE) {
                                iaw = NumberUtils.toInt(answer) - 1;
                            }
                            if (sq.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE) {
                                selectedIdx = UtilCollection.toIntArray(answer);
                                StringBuilder sb = new StringBuilder();
                                for (int k = 0; k < selectedIdx.length; k++) {
                                    selectedIdx[k] = selectedIdx[k] - 1;
                                    int miaw = selectedIdx[k];
                                    sb.append(miaw + ",");
                                }
                                sb.deleteCharAt(sb.length() - 1);
                                maw = sb.toString();
                            }
                        }

                        if (!(q instanceof SelectionQuestion)) {
                        } else {
                            SelectionQuestion sq = (SelectionQuestion) q;
                            int optsz = sq.optionSize();
                            if (sq.getChoiceMode() == QuestionConsts.CHOICE_IMG_MODE
                                    || sq.getChoiceMode() == QuestionConsts.CHOICE_IMG2_MODE
                                    || sq.getChoiceMode() == QuestionConsts.CHOICE_IMGX_MODE) {
                                optsz = sq.getSize();
                            }
                            if (maw == null) {
                                if (iaw > optsz - 1) {
                                    hasDataError = true;
                                    String err = String.format("第%d行第%d列，题目选项1-%d，而填写的选项不在这个范围内<br>", row + 1,
                                            colOff + colStart + 1, optsz);
                                    resulMsgList.add(err);
                                    txManager.rollback(status);
                                    throw new Exception(err);
                                    // break;
                                }
                            } else {
                                if (selectedIdx.length > optsz) {
                                    String err = String.format("第%d行第%d列，题目选项总数，而填写的选项个数超出选项总数<br>", row + 1,
                                            colOff + colStart + 1, optsz);
                                    resulMsgList.add(err);
                                    txManager.rollback(status);
                                    throw new Exception(err);
                                    // break;
                                } else {
                                    boolean hasSelOptionError = false;
                                    for (int m = 0; m < selectedIdx.length; m++) {
                                        if (selectedIdx[m] > optsz - 1) {
                                            hasSelOptionError = true;
                                            hasDataError = true;
                                            String err = String.format("第%d行第%d列，多选题的第%d个选项，题目选项1-%d，而填写的选项不在这个范围内<br>",
                                                    row + 1, colOff + colStart + 1, m + 1, optsz);
                                            resulMsgList.add(err);
                                            txManager.rollback(status);
                                            throw new Exception(err);
                                            // break;
                                        }
                                    }
                                    if (hasSelOptionError)
                                        break;// 如果多选题中的某个选项值大于选项总数，报错
                                }
                            }

                        }
                        if (maw == null)
                            questionnaire.answer(i, iaw + "", false);
                        else
                            questionnaire.answer(i, maw, false);
                        colOff++;
                    }
                }
                if (hasDataError) {// 如果记录有错，忽略并读取下一行
                    row++;
                    continue;
                }
                row++;
                validNum++;
                // 保存答案等信息
                try {
                    questionnaire.close(page);
                } catch (QuestionnaireException e) {
                    // errSb.append(e.getMessage());
                    break;
                }
            }
            txManager.commit(status);
            String resultStr = "总计：" + String.valueOf(totalNum) + "条记录；成功导入" + String.valueOf(validNum) + "个教师的答案；";
            resulMsgList.add(0, resultStr);
            afterRead(questionnaire, qIdxList);
        } catch (Exception e) {
            txManager.rollback(status);
            afterRead(questionnaire, qIdxList);
            e.printStackTrace();
            throw new Exception("答案导入失败，请与管理员联系！");
        }
    }

    public void importStudentAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception {
        Questionnaire questionnaire = null;
        QuestionnaireCompistionListener listener = null;
        IntList qIdxList = new ArrayIntList();
        List resulMsgList = new ArrayList();
        String scaleId = null;
        page.put("resultMsgList", resulMsgList);
        page.put("answerimport", true);
        // HSSFWorkbook wb;
        XSSFWorkbook wb;
        // Object userInfo = null;
        // try {
        // FileInputStream stream= new FileInputStream(url);
        // wb = WorkbookUtils.openWorkbook(inputStream);
        wb = new XSSFWorkbook(inputStream);
        // HSSFSheet sheet = wb.getSheetAt(0);
        XSSFSheet sheet = wb.getSheetAt(0);
        scaleId = WorkbookUtils.getStringCellValue(sheet, 2, 1);

        List<String> list = UtilCollection.toList(scaleId, '-');
        if (list.size() != 2) {
            resulMsgList.add("量表ID号被更动，请重新下载模板！");
            return;
            // throw new ProcessException("量表ID号被更动，请重新下载模板！");
        }
        scaleId = list.get(0);
        int groupId = NumberUtils.toInt(list.get(1));
        if (StringUtils.isBlank(scaleId)) {
            resulMsgList.add("没有量表id<br>");
            // errSb.append("没有量表id<br>");
            return;
        }
        String threeAngleUUID = null;
        if (ScaleUtils.isThirdAngleScale(scaleId))// 如果是三角视，则取出三角视识别码
            threeAngleUUID = WorkbookUtils.getStringCellValue(sheet, 2, 4);
        questionnaire = SpringContextHolder.getBean("Questionnaire", Questionnaire.class);
        listener = SpringContextHolder.getBean("QuestionnaireCompistionListener",
                QuestionnaireCompistionListener.class);
        buildQuestionnaire(questionnaire, listener, scaleId);
        int row = START_ROW;
        // HSSFRow hrow = null;
        XSSFRow hrow = null;
        int totalNum = 0;
        int validNum = 0;

        List<ExamResult> erList = new ArrayList<ExamResult>();
        page.put("erList", erList);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            while (true) {
                hrow = sheet.getRow(row);
                if (hrow == null) {
                    page.put("rowCount", totalNum);
                    break;
                }
                totalNum++;
                String sfzjh = WorkbookUtils.getStringCellValue(sheet, row, 0);
                String xm = WorkbookUtils.getStringCellValue(sheet, row, 1);
                if (StringUtils.isBlank(sfzjh)) {
                    txManager.rollback(status);
                    String err = String.format("第%d行身份证件号不能为空!<br>", row + 1);
                    throw new Exception(err);
                    // break;
                }
                // String xm = WorkbookUtils.getStringCellValue(sheet, row, 1);
                // if (StringUtils.isBlank(xm)) {
                // break;
                // }
                // 根据学号和机构id查找学生
                Student student = studentDao.getStudentBySfzjh(sfzjh);
                if (student == null) {
                    row++;
                    resulMsgList.add("身份证件号为:" + sfzjh + "的学生不存在<br>");
                    // errSb.append("学号为:"+xh+"的学生不存在<br>");
                    txManager.rollback(status);
                    throw new Exception("身份证件号为:" + sfzjh + "的学生不存在");
                    // continue;
                }
                Map<?, ?> param = UtilMisc.toMap("studentid", student.getId(), "scaleid", scaleId);
                List<ExamdoStudent> examdoStudentList = examDoMapper.selectStudentExamdoByStudentId(param);
                if (examdoStudentList == null)// 没有分发记录
                {
                    row++;
                    resulMsgList.add("姓名为:" + xm + "身份证件号为:" + sfzjh + "的学生没有符合条件的分发记录！<br>");
                    txManager.rollback(status);
                    throw new Exception("姓名为:" + xm + "身份证件号为:" + sfzjh + "的学生没有符合条件的分发记录！");
                    // continue;
                } else {
                    if (examdoStudentList.size() == 0) {
                        row++;
                        resulMsgList.add("姓名为:" + xm + "身份证件号为:" + sfzjh + "的学生没有符合条件的分发记录！<br>");
                        txManager.rollback(status);
                        throw new Exception("姓名为:" + xm + "身份证件号为:" + sfzjh + "的学生没有符合条件的分发记录！");
                        // continue;
                    }
                }
                ExamdoStudent examdoStudent = examdoStudentList.get(0);
                questionnaire.setExamdo(examdoStudent);
                questionnaire.setResultId(examdoStudent.getResultid());
                questionnaire.setStartTime(UtilDateTime.nowTimestamp());
                questionnaire.setTesttype(2);// 标记为导入
                questionnaire.setSubjectUserInfo(student);
                questionnaire.setThreeAngleUUID(threeAngleUUID);

                listener.setSubjectUserInfo(student);
                Scale scale = questionnaire.getScale();
                int colStart = 4;
                boolean hasDataError = false;
                // initQuestionState();
                // questionIdx = getNextQuestionIdx();//第一题索引号

                // 填充答案
                List<Question> qList = questionnaire.getQuestions();
                // 如果是道德量表
                if (ScaleUtils.isMoralityScale(scaleId)) {
                    int colOff = 0;
                    StringBuilder sb = new StringBuilder();
                    int T = 0;
                    for (int i = 0; i < qList.size(); ++i) {
                        Question question = qList.get(i);
                        if (question.isTemplate()) {
                            sb.setLength(0);
                            sb.append("0,");
                            // String answer="";
                            String[] templateQeslist = new String[8];
                            for (int j = 0; j < 12; j++) {
                                if (j < 4) {
                                    templateQeslist[j] = "Q" + (T + j + 1) + "_1";
                                }
                                if (j < 8 && j > 3) {
                                    templateQeslist[j] = "Q" + (T + j - 3) + "_2";
                                }
                                String answer = WorkbookUtils.getStringCellValue(sheet, row, j + colStart + colOff);
                                answer = UtilMisc.toDBCase(answer);
                                if (StringUtils.isEmpty(answer))
                                    answer = "0";
                                int iaw = NumberUtils.toInt(answer, 0) - 1;
                                sb.append(iaw).append(UtilCollection.COMMA);
                                if (j == 7)
                                    sb.setCharAt(sb.length() - 1, MuiltQuestionCalc.SEP);
                            }
                            colOff += 12;
                            sb.deleteCharAt(sb.length() - 1);
                            question.setTemplateQeslist(templateQeslist);
                            questionnaire.answer(i, sb.toString(), false);
                            T = T + 4;
                        }

                    }
                } else {
                    int colOff = 0;
                    for (int i = 0; i < qList.size(); i++) {
                        String qId = qList.get(i).getId();
                        if (qId.contains("QN"))
                            continue;
                        if (qId.contains("QPD"))
                            continue;
                        // 处理判断题
                        if (student.getXbm().equals("1")) {// 男生
                            if (qId.contains("_2"))
                                continue;
                        } else {// 女生
                            if (qId.contains("_1"))
                                continue;
                        }
                        Question q = qList.get(i);
                        String answer = WorkbookUtils.getStringCellValue(sheet, row, colOff + colStart);

                        if (StringUtil.isEmpty(answer)) {
                            hasDataError = true;
                            String err = String.format("第%d行，第%d格是空格!<br>", row + 1, colOff + colStart + 1);
                            resulMsgList.add(err);
                            txManager.rollback(status);
                            throw new Exception(err);
                            // break;
                        }
                        answer = UtilMisc.toDBCase(answer);
                        // int iaw = NumberUtils.toInt(answer) - 1;
                        int iaw = 0;
                        String maw = null;
                        int[] selectedIdx = null;
                        if (q instanceof SelectionQuestion) {// 如果是多选题，得分为所填选项值
                            SelectionQuestion sq = (SelectionQuestion) q;
                            if (sq.getChoiceMode() == QuestionConsts.CHOICE_SINGLE_MODE) {
                                iaw = NumberUtils.toInt(answer) - 1;
                            }
                            if (sq.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE) {
                                selectedIdx = UtilCollection.toIntArray(answer);
                                StringBuilder sb = new StringBuilder();
                                for (int k = 0; k < selectedIdx.length; k++) {
                                    selectedIdx[k] = selectedIdx[k] - 1;
                                    int miaw = selectedIdx[k];
                                    sb.append(miaw + ",");
                                }
                                sb.deleteCharAt(sb.length() - 1);
                                maw = sb.toString();
                            }
                        }

                        if (!(q instanceof SelectionQuestion)) {
                        } else {
                            SelectionQuestion sq = (SelectionQuestion) q;
                            int optsz = sq.optionSize();
                            if (sq.getChoiceMode() == QuestionConsts.CHOICE_IMG_MODE
                                    || sq.getChoiceMode() == QuestionConsts.CHOICE_IMG2_MODE
                                    || sq.getChoiceMode() == QuestionConsts.CHOICE_IMGX_MODE) {
                                optsz = sq.getSize();
                            }
                            if (maw == null) {
                                if (iaw > optsz - 1) {
                                    hasDataError = true;
                                    String err = String.format("第%d行第%d列，题目选项1-%d，而填写的选项不在这个范围内<br>", row + 1,
                                            colOff + colStart + 1, optsz);
                                    resulMsgList.add(err);
                                    txManager.rollback(status);
                                    throw new Exception(err);
                                    // break;
                                }
                            } else {
                                if (selectedIdx.length > optsz) {
                                    String err = String.format("第%d行第%d列，题目选项总数，而填写的选项个数超出选项总数<br>", row + 1,
                                            colOff + colStart + 1, optsz);
                                    resulMsgList.add(err);
                                    txManager.rollback(status);
                                    throw new Exception(err);
                                    // break;
                                } else {
                                    boolean hasSelOptionError = false;
                                    for (int m = 0; m < selectedIdx.length; m++) {
                                        if (selectedIdx[m] > optsz - 1) {
                                            hasSelOptionError = true;
                                            hasDataError = true;
                                            String err = String.format("第%d行第%d列，多选题的第%d个选项，题目选项1-%d，而填写的选项不在这个范围内<br>",
                                                    row + 1, colOff + colStart + 1, m + 1, optsz);
                                            resulMsgList.add(err);
                                            txManager.rollback(status);
                                            throw new Exception(err);
                                            // break;
                                        }
                                    }
                                    if (hasSelOptionError)
                                        break;// 如果多选题中的某个选项值大于选项总数，报错
                                }
                            }

                        }
                        if (maw == null)
                            questionnaire.answer(i, iaw + "", false);
                        else
                            questionnaire.answer(i, maw, false);
                        colOff++;
                    }
                }
                if (hasDataError) {// 如果记录有错，忽略并读取下一行
                    row++;
                    continue;
                }
                row++;
                validNum++;
                // 保存答案等信息
                try {
                    questionnaire.close(page);
                } catch (QuestionnaireException e) {
                    // errSb.append(e.getMessage());
                    break;
                }
            }
            txManager.commit(status);
            String resultStr = "总计：" + String.valueOf(totalNum) + "条记录；成功导入" + String.valueOf(validNum) + "个学生的答案；";
            resulMsgList.add(0, resultStr);
            afterRead(questionnaire, qIdxList);
        } catch (Exception e) {
            txManager.rollback(status);
            afterRead(questionnaire, qIdxList);
            e.printStackTrace();
            throw new Exception("答案导入失败，请与管理员联系！");

        }
    }

    public void batchCommit(List erList) {
        examResultDao.insertExamResultBatch(erList);
    }

    /*
     * public void importStudentAnswerFromXls(long orgid, InputStream
     * inputStream,Map<String,Object> page)throws Exception{ Questionnaire
     * questionnaire = null; QuestionnaireCompistionListener listener = null;
     * IntList qIdxList = new ArrayIntList(); List resulMsgList = new
     * ArrayList(); String scaleId = null; page.put("resultMsgList",
     * resulMsgList); HSSFWorkbook wb; //Object userInfo = null; try {
     * //FileInputStream stream= new FileInputStream(url); wb =
     * WorkbookUtils.openWorkbook(inputStream); HSSFSheet sheet =
     * wb.getSheetAt(0); scaleId = WorkbookUtils.getStringCellValue(sheet, 2,
     * 1);
     * 
     * List<String> list = UtilCollection.toList(scaleId, '-'); if (list.size()
     * != 2) { resulMsgList.add("量表ID号被更动，请重新下载模板！"); return; //throw new
     * ProcessException("量表ID号被更动，请重新下载模板！"); } scaleId = list.get(0); int
     * groupId = NumberUtils.toInt(list.get(1)); if
     * (StringUtils.isBlank(scaleId)) { resulMsgList.add("没有量表id<br>");
     * //errSb.append("没有量表id<br>"); return; } String threeAngleUUID=null;
     * if(ScaleUtils.isThirdAngleScale(scaleId))//如果是三角视，则取出三角视识别码
     * threeAngleUUID = WorkbookUtils.getStringCellValue(sheet, 2, 4);
     * questionnaire =
     * SpringContextHolder.getBean("Questionnaire",Questionnaire.class);
     * listener = SpringContextHolder.getBean("QuestionnaireCompistionListener",
     * QuestionnaireCompistionListener.class);
     * buildQuestionnaire(questionnaire,listener,scaleId); int row = START_ROW;
     * HSSFRow hrow = null; int totalNum = 0; int validNum = 0;
     * 
     * TransactionDefinition def = new DefaultTransactionDefinition();
     * TransactionStatus status = txManager.getTransaction(def); while (true) {
     * hrow = sheet.getRow(row); if (hrow == null) break; totalNum++; String xh
     * = WorkbookUtils.getStringCellValue(sheet, row, 0); String xm =
     * WorkbookUtils.getStringCellValue(sheet, row, 1); if
     * (StringUtils.isBlank(xh)) { break; } //String xm =
     * WorkbookUtils.getStringCellValue(sheet, row, 1); //if
     * (StringUtils.isBlank(xm)) { //break; //} //根据学号和机构id查找学生 Student student
     * = studentDao.getStudentByXh(orgid,xh); if(student == null){ row++;
     * resulMsgList.add("学号为:"+xh+"的学生不存在<br>");
     * //errSb.append("学号为:"+xh+"的学生不存在<br>"); continue; } Map<?, ?> param =
     * UtilMisc.toMap("studentid", student.getId(),"scaleid",scaleId);
     * List<ExamdoStudent> examdoStudentList =
     * examDoMapper.selectStudentExamdoByStudentId(param);
     * if(examdoStudentList==null)//没有分发记录 { row++;
     * resulMsgList.add("姓名为:"+xm+"学号为:"+xh+"的学生没有符合条件的分发记录！<br>"); continue; }
     * else{ if(examdoStudentList.size()==0){ row++;
     * resulMsgList.add("姓名为:"+xm+"学号为:"+xh+"的学生没有符合条件的分发记录！<br>"); continue; }
     * } ExamdoStudent examdoStudent= examdoStudentList.get(0);
     * questionnaire.setExamdo(examdoStudent);
     * questionnaire.setResultId(examdoStudent.getResultid());
     * questionnaire.setStartTime(UtilDateTime.nowTimestamp());
     * questionnaire.setTesttype(2);//标记为导入
     * questionnaire.setSubjectUserInfo(student);
     * questionnaire.setThreeAngleUUID(threeAngleUUID);
     * 
     * listener.setSubjectUserInfo(student); Scale scale =
     * questionnaire.getScale(); int colStart = 4; boolean hasDataError = false;
     * //initQuestionState(); //questionIdx = getNextQuestionIdx();//第一题索引号
     * 
     * 
     * // 填充答案 List<Question> qList = questionnaire.getQuestions(); // 如果是道德量表
     * if (ScaleUtils.isMoralityScale(scaleId)) { StringBuilder sb = new
     * StringBuilder(); boolean xx = scaleId.startsWith("1"); for (int i = 0; i
     * < qIdxList.size(); ++i) {
     * 
     * } } else { int colOff = 0; for(int i=0;i<qList.size();i++){ String qId =
     * qList.get(i).getId(); if(qId.contains("QN")) continue;
     * if(qId.contains("QPD")) continue; //处理判断题
     * if(student.getXbm().equals("1")){//男生 if(qId.contains("_2"))continue; }
     * else{//女生 if(qId.contains("_1"))continue; } Question q = qList.get(i);
     * String answer = WorkbookUtils.getStringCellValue(sheet, row, colOff +
     * colStart);
     * 
     * if(StringUtil.isEmpty(answer)){ hasDataError = true; String err =
     * String.format("第%d行，第%d格是空格!<br>", row + 1, colOff+colStart+1);
     * resulMsgList.add(err); break; } answer = UtilMisc.toDBCase(answer); //int
     * iaw = NumberUtils.toInt(answer) - 1; int iaw = 0; String maw = null;
     * int[] selectedIdx=null; if (q instanceof
     * SelectionQuestion){//如果是多选题，得分为所填选项值 SelectionQuestion sq =
     * (SelectionQuestion) q; if(sq.getChoiceMode() ==
     * QuestionConsts.CHOICE_SINGLE_MODE){ iaw = NumberUtils.toInt(answer) - 1;
     * } if(sq.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE){ selectedIdx
     * = UtilCollection.toIntArray(answer); StringBuilder sb = new
     * StringBuilder(); for(int k=0;k<selectedIdx.length;k++){ selectedIdx[k] =
     * selectedIdx[k]-1; int miaw = selectedIdx[k]; sb.append(miaw+","); }
     * sb.deleteCharAt(sb.length()-1); maw=sb.toString(); } }
     * 
     * if (!(q instanceof SelectionQuestion)) {} else { SelectionQuestion sq =
     * (SelectionQuestion) q; int optsz = sq.optionSize(); if
     * (sq.getChoiceMode() == QuestionConsts.CHOICE_IMG_MODE ||
     * sq.getChoiceMode() == QuestionConsts.CHOICE_IMG2_MODE ||
     * sq.getChoiceMode() == QuestionConsts.CHOICE_IMGX_MODE) { optsz =
     * sq.getSize(); } if(maw==null){ if (iaw > optsz - 1) { hasDataError =
     * true; String err = String.format("第%d行第%d列，题目选项1-%d，而填写的选项不在这个范围内<br>",
     * row + 1,colOff+colStart+1, optsz); resulMsgList.add(err); break; } }
     * else{ if(selectedIdx.length>optsz){ String err =
     * String.format("第%d行第%d列，题目选项总数，而填写的选项个数超出选项总数<br>", row +
     * 1,colOff+colStart+1, optsz); resulMsgList.add(err); break; } else{
     * boolean hasSelOptionError = false; for(int m=0;m<selectedIdx.length;m++){
     * if (selectedIdx[m] > optsz-1) { hasSelOptionError = true; hasDataError =
     * true; String err =
     * String.format("第%d行第%d列，多选题的第%d个选项，题目选项1-%d，而填写的选项不在这个范围内<br>", row +
     * 1,colOff+colStart+1, m+1,optsz); resulMsgList.add(err); //break; } }
     * if(hasSelOptionError)break;//如果多选题中的某个选项值大于选项总数，报错 } }
     * 
     * } if(maw==null) questionnaire.answer(i, iaw + "", false); else
     * questionnaire.answer(i, maw, false); colOff++; } }
     * if(hasDataError){//如果记录有错，忽略并读取下一行 row++; continue; } row++; validNum++;
     * // 保存答案等信息 try { questionnaire.close(null); } catch
     * (QuestionnaireException e) { //errSb.append(e.getMessage()); break; } }
     * String resultStr =
     * "总计："+String.valueOf(totalNum)+"条记录；成功导入"+String.valueOf(validNum)+
     * "个学生的答案；"; resulMsgList.add(0, resultStr);
     * afterRead(questionnaire,qIdxList); } catch (Exception e) {
     * afterRead(questionnaire,qIdxList); e.printStackTrace(); throw new
     * Exception("答案导入失败，请与管理员联系！");
     * 
     * } }
     */
    /**
     * 获得select题目的index;
     */
    // boolean hasTmQt = false;
    private void buildQuestionnaire(Questionnaire questionnaire, QuestionnaireCompistionListener listener,
            String scaleId) {
        // if (questionnaire == null) {
        Scale scale = cachedScaleMgr.get(scaleId, true);
        // listener =
        // SpringContextHolder.getBean("QuestionnaireCompistionListener",QuestionnaireCompistionListener.class);
        QuestionnaireCalcListener qCalcListener = SpringContextHolder.getBean("QuestionnaireCalcListener",
                QuestionnaireCalcListener.class);
        // QuestionnaireStoreListener qStoreListener =
        // SpringContextHolder.getBean("QuestionnaireStoreListener",QuestionnaireStoreListener.class);
        QuestionnaireAnswerImportStoreListener qStoreListener = SpringContextHolder
                .getBean("QuestionnaireAnswerImportStoreListener", QuestionnaireAnswerImportStoreListener.class);

        listener.addQuestionnaireListener(qCalcListener); // 计算题目分数和维度分数
        listener.addQuestionnaireListener(qStoreListener); // 存储结果,并为页面提供答题时间
        // questionnaire =
        // SpringContextHolder.getBean("Questionnaire",Questionnaire.class);
        questionnaire.setScale(scale);
        questionnaire.setQuestionnaireLister(listener);

        listener.setQuestionnaire(questionnaire);

        try {
            questionnaire.open(null);
        } catch (QuestionnaireException e) {
            throw new RuntimeException(e);
        }
        // }
    }

    /*
     * private void initQuestionState() { questionIdx = -1; //题目索引由-1开始
     * initQuestionIndex();//初始化题目的索引号列表 -赵万锋 }
     * 
     * private void initQuestionIndex(){ Object user =
     * questionnaire.getSubjectUserInfo(); String gender =null; if(user
     * instanceof Student){ Student stu = (Student)user; gender = stu.getXbm();
     * //if(gender.equals("男") || gender.equals("0"))
     * 
     * } if(user instanceof Teacher){ Teacher teacher = (Teacher)user; gender =
     * teacher.getXbm(); } qIdxList = new ArrayIntList(); List<Question> qList =
     * questionnaire.getQuestions(); int idx = 0; boolean genderM = false;
     * boolean genderW = false; for (Question q : qList) { String qID =
     * q.getId();
     * 
     * if(qID.contains("QPD")&& !qID.contains("_")){//判断题 //idx++;
     * //currentJudgeId = qID; //continue; }
     * 
     * 
     * if(qID.contains("QN")){
     * q.setType(QuestionConsts.TYPE_TITLE);//临时添加，表示这是题干题，不需要计数 genderM =
     * false; genderW = false; if(gender.equals("男") ||
     * gender.equals("1"))//如果是男的 { if(qID.contains("_M")){ genderM = true;
     * genderW = false; } if(qID.contains("_W"))//过滤掉女性题的题干 { genderM = false;
     * genderW = true; idx++; continue; } } if(gender.equals("女") ||
     * gender.equals("2")){//如果是女的 if(qID.contains("_M"))//过滤掉男性题的题干 { genderM =
     * true; genderW = false; idx++; continue; } if(qID.equals("_W")){ genderW =
     * true; genderM = false; } } //if(qID.contains("_M")){ //genderQ = true;
     * //} //else //genderQ = false; //idx++; //continue; }
     * if(qID.contains("_")){ if(gender.equals("男") ||
     * gender.equals("1"))//如果是男的 { if(genderM == true) { } if(genderW == true){
     * idx++; continue; } } if(gender.equals("女") || gender.equals("2"))//如果是男的
     * { if(genderM == true){ idx++; continue; } } } qIdxList.add(idx); idx++; }
     * 
     * } private int getNextQuestionIdx() { List<QuestionBlock> questions =
     * questionnaire.getQuestionBlocks(false); int idx = questionIdx + 1; for (;
     * idx < qIdxList.size(); ++idx) { int index = qIdxList.get(idx);
     * QuestionBlock question = questions.get(index); String qId =
     * question.getQuestion().getId(); if
     * (qId.contains("QPD")&&qId.contains("_"))
     * {//如果是判断题，忽略。并且需要记录当前的判断题题干id，以便找到相应的判断题 currentJudgeId = qId; continue;
     * } else if(qId.contains("QN"))//如果是题干，忽略 { currentJudgeId = ""; continue;
     * }
     * 
     * else { //处理判断题 if(currentJudgeId.contains("_Y")){
     * if(judgeAnswer.equals("0")){ break; } if(judgeAnswer.equals("1")){
     * continue; } } if(currentJudgeId.contains("_N")){
     * if(judgeAnswer.equals("0")){ continue; } if(judgeAnswer.equals("1")){
     * break; } } break; } } return idx; } public Object getResult() { return
     * null; }
     */

    private void afterRead(Questionnaire questionnaire, IntList qIdxList) {
        if (questionnaire != null) {
            questionnaire.clear();
            questionnaire = null;
        }
        qIdxList.clear();
    }

}
