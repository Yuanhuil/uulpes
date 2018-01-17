package edutec.scale.exam;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;

import edutec.group.domain.DimWarning;
import edutec.group.domain.PropNorm;
import edutec.scale.model.Dimension;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilMisc;

/**
 * @作者： weir @创建时间： Dec 31, 2008 11:36:44 AM @项目名： pes @简单说明： 计算预警等级
 */
@Scope("prototype")
@Component("ExamWarning")
public class ExamWarning {
    @Autowired
    private ExamResultMapper examResultDao;
    private static final String WARN_KEY = "warn";
    private Questionnaire questionnaire;
    private PropNorm norm;
    private int warningGrade = 0;
    private static final double WARN1 = 0.67;
    private static final double WARN2 = 1.15;
    private static final double WARN3 = 1.96;

    private HashMap<String, Number> warnMap = new HashMap<String, Number>();

    public ExamWarning(Questionnaire questionnaire) throws SQLException {
        this.questionnaire = questionnaire;
        // calGrade();
    }

    public ExamWarning() {

    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public int getGrade() {
        return warningGrade;
    }

    public HashMap<String, Number> getWarningMap() {
        return warnMap;
    }

    /*
     * 计算预警等级
     */
    public void calGrade() throws SQLException {

        DimensionBlock aBlk = null;
        double val = 0D;
        try {
            List<DimensionBlock> dimBlks = questionnaire.getDimensionBlocks();
            int maxWg = 0;// 告警等级最大值
            for (DimensionBlock dim : dimBlks) {
                String wid = dim.getId();
                if (wid.equals("W0")) {
                    if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId()))
                        continue;
                }

                if (dim.getStdScore() == null)
                    val = dim.getRawScore().doubleValue();
                else
                    val = dim.getStdScore().doubleValue();// 标准分
                Map<?, ?> param = UtilMisc.toMap(Constants.SCALEID_PROP, questionnaire.getScaleId(), Constants.WID_PROP,
                        wid);
                // DimWarning dimWarning = DataQuery.getWarning(param);
                DimWarning dimWarning = examResultDao.getWarning(param);
                if (dimWarning == null)
                    continue;
                // 预警等级，这些先注释掉
                double w1 = dimWarning.getW1();
                double w2 = dimWarning.getW2();
                double w3 = dimWarning.getW3();

                int lv = getWarningLevel(val, w1, w2, w3);

                dim.setWarningGrade(lv);
                if (maxWg < lv)
                    maxWg = lv;
                // 如果量表有告警
                if (questionnaire.getScale().isWarningOrNot()) {
                    // 如果有总分维度，或者只有一个维度
                    if (wid.equals(Dimension.SUM_SCORE_DIM) || questionnaire.getDimensionSize() == 1)
                        questionnaire.setWarningGrade(lv);
                }
            }
            // 如果没有总分维度，并且维度数超过1个，那么告警等级为一级维度的最大值
            if (questionnaire.getScale().isWarningOrNot()) {
                if (questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM) == null
                        && questionnaire.getDimensionSize() > 1) {
                    questionnaire.setWarningGrade(maxWg);
                }
                if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
                    questionnaire.setWarningGrade(maxWg);
                }
            }

        } catch (Exception ex) {

        }

    }

    private int getWarningLevel(double val, double w1, double w2, double w3) {
        if (w1 > 0) {
            if (val > w3)
                return 3;
            else if ((val <= w3) && (val > w2))
                return 2;
            else if ((val <= w2) && (val > w1))
                return 1;
        } else {
            if (val < w3)
                return 3;
            else if ((val < w2) && (val >= w3))
                return 2;
            else if ((val < w1) && (val >= w2))
                return 1;
        }
        return 0;
    }

    private int getScoreGrade(double val, double score1, double score2, double score3, double score4) {
        if (score1 > score4)// 得分越高，等级越低
        {
            if (val < score4)
                return 5;
            else if ((val >= score4) && (val < score3))
                return 4;
            else if ((val >= score3) && (val < score2))
                return 3;
            else if ((val >= score2) && (val < score1))
                return 2;
            else
                return 1;
        } else// 得分越高，等级越高
        {
            if (val > score4)
                return 5;
            else if ((val <= score4) && (val > score3))
                return 4;
            else if ((val <= score3) && (val > score2))
                return 3;
            else if ((val <= score2) && (val > score1))
                return 2;
            else
                return 1;

        }

    }

    private void cutWarning(double mean, double normM, double SD) {
        if (mean <= (normM - SD * WARN1) && mean > (normM - SD * WARN2)) {
            warningGrade = (warningGrade < 1) ? 1 : warningGrade;
        } else if (mean <= (normM - SD * WARN2) && mean > (normM - SD * WARN3)) {
            warningGrade = (warningGrade < 2) ? 2 : warningGrade;
        } else if (mean <= (normM - SD * WARN3)) {
            warningGrade = 3;
        }
    }

    private void addWarning(double mean, double normM, double SD) {
        if (mean >= (normM + SD * WARN1) && mean < (normM + SD * WARN2)) {
            warningGrade = (warningGrade < 1) ? 1 : warningGrade;
        } else if (mean >= (normM + SD * WARN2) && mean < (normM + SD * WARN3)) {
            warningGrade = (warningGrade < 2) ? 2 : warningGrade;
        } else if (mean >= (normM + SD * WARN3)) {
            warningGrade = 3;
        }
    }

    public static void main(String[] args) {
        // new ClassPathXmlApplicationContext("applicationContext.xml");
        // ExamWarning statis = new ExamWarning();
        // statis.updateWarning();
    }

}
