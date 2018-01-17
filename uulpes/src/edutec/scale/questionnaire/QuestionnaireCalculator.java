package edutec.scale.questionnaire;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.util.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.assessmentcenter.ExamdoMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.utils.AgeUitl;

import edutec.group.domain.PropNorm;
import edutec.scale.exam.ExamScoreGrade;
import edutec.scale.exam.ExamWarning;
import edutec.scale.exception.DimensionException;
import edutec.scale.exception.QuestionException;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Dimension;
import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.util.ScaleUtils;
import heracles.util.Arith;
import heracles.util.Resources;
import heracles.util.UtilCollection;
import heracles.util.UtilMisc;

/**
 * 1) 问题ID一定要以Q开头<br>
 * 2) 维度ID一定要以W开头<br>
 * 3) 维度标准分一定要以WS开头<br>
 * 4) 维度粗分一定要以WR开头<br>
 * 1)sum,arg,produce 2)+、/、-、* WR1,WS2 3){x=sum;#if (${x}==0) {0;} else if
 * (${x}>=1&&${x}<=9) {1;} else if (${x}>=10&&${x}<=18) {2;} else {3;}}
 * 
 * @author Administrator
 */
@Scope("prototype")
@Service("QuestionnaireCalculator")
public class QuestionnaireCalculator {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    public ExamResultMapper examResultDao;
    @Autowired
    public ExamdoMapper examdoMapper;
    @Autowired
    private ExamWarning examWarning;
    @Autowired
    private ExamScoreGrade examScoreGrade;
    @Autowired
    public StudentServiceI studentService;
    public static final char TOK_QUESTION = 'Q';
    public static final char TOK_DIMENSION = 'W';
    public static final char TOK_STD = 'S';
    public static final char TOK_RAW = 'R';

    private Questionnaire questionnaire;
    private List<DimensionBlock> errList = null;

    public QuestionnaireCalculator() {

    }

    public QuestionnaireCalculator(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        buildErrList();
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        buildErrList();
    }

    /*
     * public ExamResultMapper getExamResultDao() { return examResultDao; }
     * 
     * public void setExamResultDao(ExamResultMapper examResultDao) {
     * this.examResultDao = examResultDao; }
     */

    public void calWarningAndScoreGrade() throws QuestionnaireException {
        try {
            if (questionnaire.getScale().isWarningOrNot()) {
                examWarning.setQuestionnaire(questionnaire);
                examWarning.calGrade();
            }
            // ExamScoreGrade examScoreGrade =
            // SpringContextHolder.getBean("examScoreGrade",ExamScoreGrade.class);
            examScoreGrade.setQuestionnaire(questionnaire);
            examScoreGrade.calGrade();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new QuestionnaireException("告警等级和得分等级计算有误!");
        }
    }

    /**
     * 计算问卷的值 因为计算某一维度分数时，很可能需要其它维度的分数，但是如果那个维度<br>
     * 没有被计算，则当前这个维度忽略，等待下一轮再计算<br>
     * 另外，如果由于用户的错误，出现计算式之间的相互等待对方的分值<br>
     * 所以规定最多运行的次数为，维度数减一
     */
    public void evaluate() throws QuestionnaireException {
        // 获得学生的真实年级
        // try{
        Object user = questionnaire.getSubjectUserInfo();
        if (user instanceof Student) {
            Student stu = (Student) user;
            // int realgrade = studentService.getRealGradeId(stu.getOrgid(),
            // stu.getGradecodeid());
            // stu.setRealGradeId(realgrade);
            ExamdoStudent examdoStudent = examdoMapper.selectStudentExamdoByResultId(questionnaire.getResultId());
            questionnaire.setExamdo(examdoStudent);
            int realgrade = examdoStudent.getRealgradeid();
            if (realgrade == 0)
                realgrade = studentService.getRealGradeId(stu.getOrgid(), stu.getGradecodeid());
            stu.setRealGradeId(realgrade);
        }
        int countor = questionnaire.getDimensionSize() - 1;
        while (!errList.isEmpty() && countor >= 0) {
            for (DimensionBlock dimBlk : questionnaire.getDimensionBlocks()) {
                evaluate(dimBlk);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("evaluate times:" + "=" + (questionnaire.getDimensionSize() - countor));
                logger.debug("errList size:" + errList.size());
            }
            if (!errList.isEmpty())
                --countor;
        }
        // }catch(QuestionnaireException e){
        // throw new QuestionnaireException("分数计算有误!");
        // }
    }

    private void evaluate(DimensionBlock dimBlk) throws QuestionnaireException {
        try {
            evaluateExp(dimBlk, true);// 计算原始分
            evaluateExp(dimBlk, false);// 计算标准分和T分

            errList.remove(dimBlk);
        } catch (Exception e) {
            /*
             * logger.error(e.getMessage(), e);
             * logger.error("======================================");
             * logger.error("be calu - " + dimBlk.getId() + "[" + e.getMessage()
             * + "]"); if (dimBlk.getFormula() != null) { logger.error("rawexp="
             * + dimBlk.getFormula().getRawexp()); logger.error("stdexp=" +
             * dimBlk.getFormula().getStdexp()); }
             * logger.error("======================================");
             */
            throw new QuestionnaireException(e.getMessage());
        }
    }

    public void evaluate(String dimensionId) throws QuestionnaireException {
        DimensionBlock dimBlk = questionnaire.findDimensionBlock(dimensionId);
        evaluate(dimBlk);
    }

    private void buildErrList() {
        // if (errList == null)
        errList = new LinkedList<DimensionBlock>(questionnaire.getDimensionBlocks());
    }

    private void evaluateZScore(DimensionBlock dimBlk) throws Exception {

    }

    private void evaluateExp(DimensionBlock dimBlk, boolean isRaw) throws Exception {
        Validate.notNull(dimBlk, "DimensionSegment对象不能为空");
        List<DimensionBlock> subdimblocks = dimBlk.getSubdimensionBlkList();
        if (subdimblocks != null) {
            for (DimensionBlock sub : subdimblocks) {
                evaluateExp(sub, isRaw);
            }
        }
        evaluateExp_(dimBlk, isRaw);
    }

    //
    private void evaluateExp_(DimensionBlock dimBlk, boolean isRaw) throws Exception {
        if (dimBlk.isCompleteCalc())
            return;
        Number number = null;
        if (dimBlk.getFormula() == null) {
            if (isRaw) {
                dimBlk.completeRawCalc();
                dimBlk.setRawScore(0D);
            } else {
                dimBlk.completeStdCalc();
                dimBlk.setStdScore(0D);
                dimBlk.setTScore(0D);
            }
            return;
        }
        String exp = dimBlk.getFormula().getRawexp();
        String nvalidexp = dimBlk.getFormula().getNvalidexp();
        if (isRaw) {
            // exp = dimBlk.getFormula().getRawexp();
            // else if(type==1)
            // exp = dimBlk.getFormula().getStdexp();
            // else
            // exp = dimBlk.getFormula().getTexp();
            if (StringUtils.isEmpty(exp)) {
                if (isRaw) {
                    dimBlk.completeRawCalc();
                    dimBlk.setRawScore(0D);
                } else {
                    dimBlk.completeStdCalc();
                    dimBlk.setStdScore(0D);
                    dimBlk.setTScore(0D);
                }
                return;
            }
        }
        /*
         * <stdexp> class:edutec.scale.questionnaire.RangeStdCalc </stdexp>
         */
        // 原始分
        if (isRaw) {
            if (exp.startsWith("class:")) {
                String clazz = exp.substring("class:".length());
                DimensionCalc calc = createCalc(clazz);
                calc.setDimBlk(dimBlk);
                number = calc.evaluate();
            } else {
                /* 先使用函数的评估，如果没有函数使用表达式评估 */
                number = evaluateFuctionExp_(dimBlk, exp, true);
                if (number == null) {
                    if (isStmtBlock(exp)) {
                        number = evaluateStmtBlock_(dimBlk, exp, true);
                    } else {
                        number = evaluateCountExp_(dimBlk, exp, true);
                        if (number == null)
                            number = evaluateFrequencyStmtBlock_(dimBlk, exp, true);
                        else {
                            if (number == null)
                                number = evaluateArithExp_(dimBlk, exp, true);
                        }

                    }
                }
            }
            dimBlk.completeRawCalc();
            dimBlk.setRawScore(number);
            if (StringUtils.isNotEmpty(nvalidexp)) {// 无效表达式
                evaludateQuestionnaireValid(number, nvalidexp);
            }
        }
        // Z和T分数
        else {

            setDimTAndZScore(dimBlk);
            dimBlk.completeStdCalc();
        }

    }

    protected void setDimTAndZScore(DimensionBlock dimBlock) throws QuestionnaireException {
        try {
            // 从常模表中获取维度值，返回值是一个map，即查询到的记录
            String normid = questionnaire.getNormid();
            String dimId = dimBlock.getId();
            Object user = questionnaire.getSubjectUserInfo();
            int age = 0;
            Map<?, ?> param = null;
            PropNorm norm = null;
            String normflag = questionnaire.getScale().getNormGradeOrAgeFlag();
            if (user instanceof Student) {
                Student student = (Student) user;
                String birthday = student.getCsrq();
                age = AgeUitl.getAgeByBirthdayStr(birthday);
                int gradeOrderId = student.getGradecodeid();
                // int gradeid =
                // studentService.getRealGradeId(student.getOrgid(),gradeOrderId);
                int realGrade = student.getRealGradeId();
                String xbm = student.getXbm();
                int gender = (xbm.equals("1") ? 1 : 2);
                if (normflag.equals("grade"))
                    param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                            Constants.WID_PROP, dimId, "gradeOrderId", realGrade, Constants.GENDER_PROP, gender);
                else
                    param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                            Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", age,
                            Constants.GENDER_PROP, gender);
                norm = examResultDao.getNorm(param);
                if (norm == null) {
                    if (normflag.equals("grade"))
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "gradeOrderId", realGrade, Constants.GENDER_PROP, 0);
                    else
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", age,
                                Constants.GENDER_PROP, 0);
                    norm = examResultDao.getNorm(param);
                }
                if (norm == null) {
                    if (normflag.equals("grade"))
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "gradeOrderId", 0, Constants.GENDER_PROP, gender);
                    else
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", 0,
                                Constants.GENDER_PROP, gender);
                    norm = examResultDao.getNorm(param);
                }
                if (norm == null) {
                    if (normflag.equals("grade"))
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "gradeOrderId", 0, Constants.GENDER_PROP, 0);
                    else
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", 0,
                                Constants.GENDER_PROP, 0);
                    norm = examResultDao.getNorm(param);
                }
                // if(norm==null)
                // throw new QuestionnaireException("没有取到常模数据!");

            } else if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                String birthday = teacher.getCsrq();
                age = AgeUitl.getAgeByBirthdayStr(birthday);
                int gender = teacher.getXbm().equals("1") ? 1 : 2;
                if (normflag.equals("grade"))
                    param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                            Constants.WID_PROP, dimId, "gradeOrderId", 14, Constants.GENDER_PROP, gender);
                else
                    param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                            Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", age,
                            Constants.GENDER_PROP, gender);
                norm = examResultDao.getNorm(param);
                if (norm == null) {
                    if (normflag.equals("grade"))
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "gradeOrderId", 14, Constants.GENDER_PROP, 0);
                    else
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", age,
                                Constants.GENDER_PROP, 0);
                    norm = examResultDao.getNorm(param);
                }
                if (norm == null) {
                    if (normflag.equals("grade"))
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "gradeOrderId", 0, Constants.GENDER_PROP, 0);
                    else
                        param = UtilMisc.toMap("normid", normid, Constants.SCALEID_PROP, questionnaire.getScaleId(),
                                Constants.WID_PROP, dimId, "normgradeorageflag", normflag, "age", age,
                                Constants.GENDER_PROP, 0);
                    norm = examResultDao.getNorm(param);
                }

                // if(norm==null)
                // throw new QuestionnaireException("没有取到常模数据!");
            }
            // int age = questionnaire.getSubjectUserInfo().getAge();
            // int gradeOrderId =
            // questionnaire.getSubjectUserInfo().getGradeOrderId();
            // int gender = questionnaire.getSubjectUserInfo().getGender();
            // Map<?, ?> param = UtilMisc.toMap(Constants.SCALEID_PROP,
            // questionnaire.getScaleId(),
            // Constants.WID_PROP, dimId, "gradeOrderId", gradeOrderId,
            // Constants.GENDER_PROP,
            // gender);

            // PropNorm norm = DataQuery.getNorm(param);
            // PropNorm norm = examResultDao.getNorm(param);
            if (norm == null)// 没有常模,用原始分代替
            {
                dimBlock.setStdScore(dimBlock.getRawScore());
                dimBlock.setTScore(dimBlock.getRawScore());
                dimBlock.setFinalScore(dimBlock.getRawScore());
            } else {

                double X = (Double) dimBlock.getRawScore();

                double M = norm.getMean();
                double SD = norm.getSdv();
                double zScore = MathUtils.round((X - M) / SD, 2);
                double tScore = zScore;// MathUtils.round(5.5+1.5*zScore,2);

                if (dimBlock.getFormula() == null) {
                    dimBlock.setStdScore(X);
                    dimBlock.setTScore(X);
                    dimBlock.setFinalScore(X);
                    return;
                }
                if (dimBlock.getFormula().getStdexp() == null) {
                    dimBlock.setStdScore(X);
                    dimBlock.setTScore(X);
                    dimBlock.setFinalScore(X);
                    return;
                }
                String stdexp = dimBlock.getFormula().getStdexp();
                String texp = dimBlock.getFormula().getTexp();
                if (isArithExp(texp)) {
                    if (texp.contains("T="))
                        ;
                    texp = texp.substring(2);
                    if (texp.contains("Z"))
                        texp = texp.replace("Z", String.valueOf(zScore));
                    Expression e = ExpressionFactory.createExpression(texp);
                    tScore = Arith.round(Double.parseDouble(e.evaluate(null).toString()), 2);
                }

                dimBlock.setStdScore(zScore);
                dimBlock.setTScore(tScore);
                dimBlock.setFinalScore(tScore);
                // dimDetail.setDimFinalScore(tScore);
            }

        } catch (Exception e) {
            throw new QuestionnaireException(e.getMessage());

        }
    }

    /**
     * 表达式评估 <formula> <rawexp>sum</rawexp> <stdexp>int(1.25*WR1)</stdexp>
     * </formula>
     * 
     * @param dimBlk
     * @param isRaw
     * @param exp
     * @return
     * @throws DimensionException
     * @throws QuestionException
     * @throws Exception
     */
    private Number evaluateArithExp_(DimensionBlock dimBlk, String exp, boolean isRaw) throws Exception {
        boolean isTokQuestion = false;
        boolean isTokDimension = false;
        boolean isInt = false;
        exp = exp.toUpperCase(); // 全部变为大写
        exp = exp.replaceAll("\\[", "(");
        exp = exp.replaceAll("\\]", ")");
        final String COUNT = "D_SIZE";
        if (exp.indexOf(COUNT) != StringUtils.INDEX_NOT_FOUND) {
            int sz = 0;
            if (dimBlk.isLeaf()) {
                sz = dimBlk.getQuestionBlockList().size();
            } else {
                List<DimensionBlock> list = dimBlk.getSubdimensionBlkList();
                for (DimensionBlock blk : list) {
                    sz += blk.getQuestionBlockList().size();
                }
            }
            exp = exp.replace(COUNT, sz + "");
        }
        int length = exp.length();
        isInt = exp.startsWith("INT(");
        if (isInt) {
            exp = exp.substring("INT(".length(), length - 1);
            length = exp.length();
        }
        StrBuilder sb = new StrBuilder(length + 32);
        StrBuilder sb1 = new StrBuilder(8);
        for (int i = 0; i < length; ++i) {
            char token = exp.charAt(i);
            isTokQuestion = isTokQuestion(token);
            isTokDimension = isTokDimension(token);
            if (isTokQuestion || isTokDimension) {
                sb1.clear();
                char lexchar = token;
                for (; CharUtils.isAsciiAlphanumeric(lexchar) || lexchar == '.';) {
                    sb1.append(lexchar);
                    ++i;
                    if (i >= length)
                        break;
                    lexchar = exp.charAt(i);
                }
                Number score = null;
                /* 如果是维度,区别粗分和标准分 */
                if (isTokDimension) {
                    DimensionBlock dblk = null;
                    boolean isRAW = isTokRAW(sb1.charAt(1));
                    boolean isSTD = isTokSTD(sb1.charAt(1));
                    if (isRAW || isSTD) {
                        sb1.deleteCharAt(1);
                    }
                    dblk = questionnaire.findDimensionBlock(sb1.toString());
                    if (isSTD) {
                        score = dblk.getStdScore();
                    } else {
                        score = dblk.getRawScore();
                    }
                } else {
                    QuestionBlock qblk = questionnaire.findQuestionBlock(sb1.toString());
                    score = qblk.getScore();
                }
                /* 将值放入列表中 */
                if (NumberUtils.compare(score.doubleValue(), 0.0) < 0) {
                    sb.append('(').append(score).append(')');
                } else {
                    sb.append(score);
                }
                --i;
            } else {
                sb.append(exp.charAt(i));
            }
        }
        Number number = null;
        Expression e = ExpressionFactory.createExpression(sb.toString());
        if (!isInt) {
            number = Arith.round(Double.parseDouble(e.evaluate(null).toString()), 2);
        } else {
            number = Double.valueOf(e.evaluate(null).toString()).intValue();
        }
        if (logger.isDebugEnabled())
            logger.debug(dimBlk.getId() + "=" + number);

        return number;
    }

    /**
     * 函数评估 <formula> <rawexp>sum</rawexp> <stdexp>arg</stdexp> </formula>
     * 
     * @param dimBlk
     * @param exp
     * @param isRaw
     * @return
     * @throws Exception
     */
    private Number evaluateFuctionExp_(DimensionBlock dimBlk, String exp, boolean isRaw) throws Exception {
        boolean issum = exp.trim().equalsIgnoreCase("sum") || exp.trim().equalsIgnoreCase("sum()")
                || exp.trim().equalsIgnoreCase("sum;") || exp.trim().equalsIgnoreCase("sum(*)");
        boolean isarg = exp.trim().equalsIgnoreCase("arg") || exp.trim().equalsIgnoreCase("arg()")
                || exp.trim().equalsIgnoreCase("arg;") || exp.trim().equalsIgnoreCase("arg(*)");
        boolean isproduct = exp.trim().equalsIgnoreCase("product") || exp.trim().equalsIgnoreCase("product()")
                || exp.trim().equalsIgnoreCase("product(*)");
        boolean isfuntionExp = issum || isarg || isproduct;
        if (!isfuntionExp) {
            return null;
        }
        ArrayDoubleList doubles = new ArrayDoubleList(16);
        int n = 0;
        int qnum = 0;
        Number sumScore = 0;
        if (dimBlk.isLeaf()) {// 子维度计算原始分
            List<QuestionBlock> list = dimBlk.getQuestionBlockList();
            for (QuestionBlock blk : list) {
                if (ScaleUtils.isSameReverseInMultiDimScale(this.questionnaire.getScale().getCode())) {
                    int rVal = evaluateReverseInMultiDim(dimBlk, blk);
                    if (rVal != -1)
                        doubles.add(rVal);
                } else
                    doubles.add(blk.getScore().doubleValue());
                if (blk.getId().contains("_2"))// 判断题，题目数重复，多了一倍，故此处将判断题题目数减半，赵万锋添加
                    continue;
                qnum++;
            }
            dimBlk.setQuestionNum(qnum);
            dimBlk.setSumScore(StatUtils.sum(doubles.toArray()));
        } else {// 父维度计算原始分
            List<DimensionBlock> list = dimBlk.getSubdimensionBlkList();
            for (DimensionBlock blk : list) {
                /*
                 * if (isRaw) { doubles.add(blk.getRawScore().doubleValue()); }
                 * else { doubles.add(blk.getStdScore().doubleValue()); }
                 */
                /* 当公式为sum、arg、product时，一定只是粗分 */
                doubles.add(blk.getRawScore().doubleValue());
                // n +=
                // blk.getQuestionBlockList().size();//n为子维度题目个数之和。这是用来计算平均分用的，因为3.0的原始分为sum，而4.0的原始分已经是平均分，因此不用再用n来求平均分。赵万锋
                // n++;
                qnum += blk.getQuestionNum();
                sumScore = sumScore.doubleValue() + blk.getSumScore().doubleValue();
            }
            dimBlk.setQuestionNum(qnum);
            dimBlk.setSumScore(sumScore);
        }
        Number number = null;
        if (issum) {
            number = Arith.round(StatUtils.sum(doubles.toArray()), 2);
        } else if (isarg) {
            if (dimBlk.isLeaf()) {// 子维度
                // number = Arith.round(StatUtils.mean(doubles.toArray()),
                // 2);//此处计算标准分，是按照平均分来计算的，而4.0是需要按照常模来计算的
                number = Arith.round(StatUtils.sum(doubles.toArray()) / qnum, 2);
            } else {// 非子维度
                // number = StatUtils.sum(doubles.toArray());
                // number = Arith.round(number.doubleValue() / n, 2);
                number = Arith.round(sumScore.doubleValue() / qnum, 2);
            }
        } else {
            number = Arith.round(StatUtils.product(doubles.toArray()), 2);
        }
        if (logger.isDebugEnabled())
            logger.debug(dimBlk.getId() + "=" + number + "[" + exp + "]" + doubles.toString());
        return number;
    }

    /**
     * 函数评估 <formula> <rawexp>count</rawexp> <stdexp>arg</stdexp> </formula>
     * 
     * @param dimBlk
     * @param exp
     * @param isRaw
     * @return
     * @throws Exception
     */
    private Number evaluateCountExp_(DimensionBlock dimBlk, String exp, boolean isRaw) throws Exception {
        exp = exp.trim();
        boolean iscount = exp.contains("计数");
        if (!iscount) {
            return null;
        }
        String v = exp.substring(2);
        int n = 0;
        int qnum = 0;
        Number sumScore = 0;
        List<QuestionBlock> list = questionnaire.getQuestionBlocks();
        for (QuestionBlock blk : list) {
            if (StringUtils.isEmpty(blk.getAnswerValue()))
                continue;
            if (blk.getAnswerValue().equalsIgnoreCase(v))
                n++;
        }
        // dimBlk.setQuestionNum(qnum);
        dimBlk.setRawScore(n);

        return Arith.round(n, 2);
    }

    /*
     * <stdexp>
     * (11-WS1)*2+2*WS2+WS4+(11-WS5)*2+WS7+2*WS8+WS10+(11-WS11)+W13+2*WS14
     * </stdexp>
     */
    protected double evaluateStmtBlock_(DimensionBlock dimBlk, String exp, boolean isRaw) throws Exception {
        StrBuilder var = new StrBuilder(), val = new StrBuilder();
        Map<String, String> varsMap = new HashMap<String, String>();
        int idx = 0;
        int state = 0;
        for (; idx < exp.length(); ++idx) {
            char c = exp.charAt(idx);
            if (c == Calc.OPEN_BLOCK)
                break;
        }
        // ---取句子前部的变量
        for (++idx; idx < exp.length(); ++idx) {
            char c = exp.charAt(idx);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c == Calc.EQ) {
                state = 1;
                continue;
            }
            if (c == Calc.STMT_SEPARATOR) {
                varsMap.put(var.toString(), val.toString());
                var.clear();
                val.clear();
                state = 0;
            }
            if (c == Calc.VARS_PROC_GAP) {
                break;
            }
            switch (state) {
            case 0:
                var.append(c);
                break;
            case 1:
                val.append(c);
                break;
            }
        }
        Map<String, Double> vmap = new HashMap<String, Double>();
        for (Map.Entry<String, String> ent : varsMap.entrySet()) {
            double calcVal = 0D;
            if (isFunction(ent.getValue())) {
                calcVal = evaluateFuctionExp_(dimBlk, ent.getValue(), isRaw).doubleValue();
            } else if (isArithExp(ent.getValue())) {
                calcVal = evaluateArithExp_(dimBlk, ent.getValue(), isRaw).doubleValue();
            } else {
                calcVal = Double.parseDouble(ent.getValue());
            }
            vmap.put(ent.getKey(), calcVal);
        }
        // 前部的变量已经获取完毕！---

        // 得到句子后部的表达式，将变量值赋入
        String stmt = exp.substring(idx + 1, exp.length() - 1);
        StrSubstitutor ss = new StrSubstitutor(vmap);
        stmt = ss.replace(stmt);
        if (logger.isDebugEnabled()) {
            logger.debug("vars=" + varsMap);
            logger.debug("stmt=" + stmt);
        }
        return QuestionCalc.jexlEvaluate(stmt);
    }

    /**
     * 频次计算
     * 
     * @param dimBlk
     * @param exp
     * @param isRaw
     * @return
     * @throws Exception
     */
    protected Number evaluateFrequencyStmtBlock_(DimensionBlock dimBlk, String exp, boolean isRaw) throws Exception {
        // int count = 0;
        exp = exp.trim();
        boolean isFrequence = exp.contains("f(");
        if (!isFrequence) {
            return null;
        }
        StrBuilder oprationSb = new StrBuilder();
        for (int idx = 0; idx < exp.length(); idx++) {
            char c = exp.charAt(idx);
            if (c == '+' || c == '-') {
                oprationSb.append(c);
            }
        }
        StringBuilder stmtSb = new StringBuilder();
        String[] foptions = exp.split("[+|-]");
        for (int i = 0; i < foptions.length; i++) {
            int count = 0;
            String foption = foptions[i].trim();
            String option = foption.substring(2, foption.length() - 1);
            if (dimBlk.isLeaf()) {// 子维度计算原始分
                List<QuestionBlock> list = dimBlk.getQuestionBlockList();
                for (QuestionBlock blk : list) {
                    String an = blk.getAnswerValue();
                    if (StringUtils.isEmpty(an))
                        continue;
                    if (an.equalsIgnoreCase(option))
                        count++;
                }
            }

            if (i != 0)
                stmtSb.append(oprationSb.charAt(i - 1));
            stmtSb.append(count);

        }

        return QuestionCalc.jexlEvaluate(stmtSb.toString());
    }

    void evaludateQuestionnaireValid(Number number, String nvalidexp) {
        int validflag = 1;
        if (nvalidexp.toLowerCase().trim().contains("r")) {
            String[] exps = nvalidexp.split("<|>|=");
            if (nvalidexp.trim().contains(">")) {
                validflag = number.longValue() > Integer.parseInt(exps[1]) ? 0 : 1;
            }
            if (nvalidexp.trim().contains("=")) {
                validflag = number.longValue() > Integer.parseInt(exps[1]) ? 0 : 1;
            }
            if (nvalidexp.trim().contains("<")) {
                validflag = number.longValue() < Integer.parseInt(exps[1]) ? 0 : 1;
            }
            if (validflag == 0)// 如果是无效问卷
                questionnaire.setValidVal(validflag);
        }
    }

    private boolean isTokQuestion(char token) {
        return token == (TOK_QUESTION);
    }

    private boolean isTokDimension(char token) {
        return token == (TOK_DIMENSION);
    }

    private boolean isTokSTD(char token) {
        return token == (TOK_STD);
    }

    private boolean isTokRAW(char token) {
        return token == (TOK_RAW);
    }

    protected boolean isArithExp(String cmputor) {
        return StringUtils.indexOfAny(cmputor, Calc.ARITH_OPERATOR) != StringUtils.INDEX_NOT_FOUND;
    }

    protected boolean isStmtBlock(String cmputor) {
        return cmputor.trim().charAt(0) == Calc.OPEN_BLOCK;
    }

    protected boolean isFunction(String cmputor) {
        return cmputor.startsWith(Calc.SUM_LEXEME) || cmputor.startsWith(Calc.ARG_LEXEME)
                || cmputor.startsWith(Calc.PRODUCT_LEXEME);
    }

    public List<DimensionBlock> getErrList() {
        return errList;
    }

    private DimensionCalc createCalc(String clazz) {
        try {
            return (DimensionCalc) Resources.instantiate(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // 计算同一题在不同维度中的反向题中的每个维度的题目得分
    private int evaluateReverseInMultiDim(DimensionBlock dimBlk, QuestionBlock questionBlk)
            throws QuestionnaireException {
        try {
            Dimension dim = dimBlk.getDimension();
            String reverseQuestionIds = dim.getReverseQuestionIds();
            if (StringUtils.isEmpty(reverseQuestionIds))
                return questionBlk.getScore().intValue();
            String questionIds = dim.getQuestionIds();
            String splitCharacter = UtilCollection.splitFlag(questionIds);
            String[] qIds = questionIds.split(splitCharacter);
            splitCharacter = UtilCollection.splitFlag(reverseQuestionIds);
            String[] qReverseIds = reverseQuestionIds.split(splitCharacter);
            // for(int i=0;i<qIds.length;i++){
            // if(qIds[i].equals(questionBlk.getId())){
            for (int i = 0; i < qReverseIds.length; i++) {
                if (qReverseIds[i].equals(questionBlk.getId())) {
                    return evaluateReverseQuestionValue(questionBlk);
                }
            }
            // break;
            // }
            // }
            return questionBlk.getScore().intValue();
        } catch (Exception e) {
            throw new QuestionnaireException("获取题目分出错");
        }
    }

    private int evaluateReverseQuestionValue(QuestionBlock questionBlk) {
        String answer = questionBlk.getAnswer();
        int selectedIndex = Integer.parseInt(answer);
        Question question = questionBlk.getQuestion();
        SelectionQuestion sQ = (SelectionQuestion) questionBlk.getQuestion();
        List<Option> options = ((SelectionQuestion) question).getOptions();

        if (options.get(selectedIndex).getValue().contains("？"))// 如果选项是？，说明此题是无法作答。参见明尼苏达量表
            return -1;
        else
            return Integer.parseInt(options.get(options.size() - 2 - selectedIndex).getValue());
    }

}
