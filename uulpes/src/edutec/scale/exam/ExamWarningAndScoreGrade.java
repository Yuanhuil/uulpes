package edutec.scale.exam;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.njpes.www.constant.Constants;

import edutec.group.data.DataQuery;
import edutec.group.domain.DimWarningAndScoreGrade;
import edutec.group.domain.PropNorm;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.Questionnaire;
import heracles.util.UtilMisc;

public class ExamWarningAndScoreGrade {
    private static final String WARN_KEY = "warn";
    private Questionnaire questionnaire;
    private int gradeOrderId = 0; // 如果为0，则表示整体
    private int gender = 0; // 目前的分析没有包括性别，0表示整体，所以不更改和访问
    private PropNorm norm;
    private int warningGrade = 0;
    private static final double WARN1 = 0.67;
    private static final double WARN2 = 1.15;
    private static final double WARN3 = 1.96;

    private HashMap<String, Number> warnMap = new HashMap<String, Number>();

    public ExamWarningAndScoreGrade(Questionnaire questionnaire) throws SQLException {
        this.questionnaire = questionnaire;
        calGrade();
    }

    public ExamWarningAndScoreGrade(Questionnaire questionnaire, int gradeOrderId, int gender) throws SQLException {
        this.questionnaire = questionnaire;
        this.gradeOrderId = gradeOrderId;
        this.gender = gender;
        calGrade();
    }

    public ExamWarningAndScoreGrade() {

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
    private void calGrade() throws SQLException {

        DimensionBlock aBlk = null;
        double val = 0D;
        try {
            List<DimensionBlock> dimBlks = questionnaire.getDimensionBlocks();
            for (DimensionBlock dim : dimBlks) {
                String wid = dim.getId();
                if (dim.getStdScore() == null)
                    val = dim.getRawScore().doubleValue();
                else
                    val = dim.getStdScore().doubleValue();// 标准分
                Map<?, ?> param = UtilMisc.toMap(Constants.SCALEID_PROP, questionnaire.getScaleId(), Constants.WID_PROP,
                        wid);
                DimWarningAndScoreGrade dimWarningAndScoreGrade = DataQuery.getWarningAndScoreGrade(param);
                if (dimWarningAndScoreGrade == null)
                    return;
                // 预警等级，这些先注释掉
                // double w1 = dimWarningAndScoreGrade.getW1();
                // double w2 = dimWarningAndScoreGrade.getW2();
                // double w3 = dimWarningAndScoreGrade.getW3();
                double score1 = dimWarningAndScoreGrade.getScore1();
                double score2 = dimWarningAndScoreGrade.getScore2();
                double score3 = dimWarningAndScoreGrade.getScore3();
                double score4 = dimWarningAndScoreGrade.getScore4();

                // int lv = getWarningLevel(val,w1,w2,w3);
                int sv = getScoreGrade(val, score1, score2, score3, score4);
                // dim.setWarningGrade(lv);
                dim.setRank(sv);
                dim.setScoreGrade(sv);
                // warnMap.put(wid, lv);
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

    private static String getABString(String str) {
        String resultStr = "";
        str = str.replaceFirst("[0-9][A-B]", "");
        String[] arr;
        boolean morethanten = false;
        if (str.contains("10A")) {
            morethanten = true;
            arr = str.split("[0-9][A]|[1-2][0-9][A]");
        } else
            arr = str.split("[0-9][A]");
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            String[] subArr;
            if (morethanten) {
                subArr = arr[i].split("[0-9][B]|[1-2][0-9][B-D]");
            } else
                subArr = arr[i].split("[0-9][B-D]");
            int index = rand.nextInt(subArr.length);
            String randomStr = subArr[index];
            resultStr += randomStr;
            // for(int j =0;j<subArr.length;j++){
            // System.out.println(subArr[j]);
            // }
        }
        // System.out.println(resultStr);
        return resultStr;
    }
}
