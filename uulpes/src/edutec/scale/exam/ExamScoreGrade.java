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

import edutec.group.domain.DimScoreGrade;
import edutec.group.domain.PropNorm;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilMisc;

@Scope("prototype")
@Component("ExamScoreGrade")
public class ExamScoreGrade {
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

    public ExamScoreGrade(Questionnaire questionnaire) throws SQLException {
        this.questionnaire = questionnaire;
        // calGrade();
    }

    public ExamScoreGrade(Questionnaire questionnaire, int gradeOrderId, int gender) throws SQLException {
        this.questionnaire = questionnaire;
        // calGrade();
    }

    public ExamScoreGrade() {

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
            for (DimensionBlock dim : dimBlks) {
                String wid = dim.getId();
                // 心理健康量表的得分等级另算
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
                // DimScoreGrade dimScoreGrade = DataQuery.getScoreGrade(param);
                DimScoreGrade dimScoreGrade = null;
                try {
                    dimScoreGrade = examResultDao.getScoreGrade(param);
                } catch (Exception e) {
                    continue;
                }
                if (dimScoreGrade == null)
                    continue;
                // 预警等级，这些先注释掉
                double score1 = dimScoreGrade.getScore1();
                double score2 = dimScoreGrade.getScore2();
                double score3 = dimScoreGrade.getScore3();
                double score4 = dimScoreGrade.getScore4();

                int sv = getScoreGrade(val, score1, score2, score3, score4);
                dim.setRank(sv);
                dim.setScoreGrade(sv);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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
}
