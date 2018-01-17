package edutec.scale.questionnaire;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.exception.QuestionnaireException;

@Scope("prototype")
@Component("QuestionnaireCalcListener")
public class QuestionnaireCalcListener extends QuestionnaireListener {
    @Autowired
    private QuestionnaireCalculator qCalc;
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void onClose(Map<Object, Object> params) throws QuestionnaireException {
        if (logger.isDebugEnabled())
            logger.debug("答题结束..." + getQuestionnaire().getTitle());
        // 做计算
        // QuestionnaireCalc sc = new QuestionnaireCalc(questionnaire);
        // QuestionnaireCalculator sc = new
        // QuestionnaireCalculator(questionnaire);
        // QuestionnaireCalculator sc =
        // SpringContextHolder.getBean("questionnaireCalculator",QuestionnaireCalculator.class);
        qCalc.setQuestionnaire(questionnaire);
        qCalc.evaluate();

        qCalc.calWarningAndScoreGrade();
    }

    @Override
    public void onAnswerQuesiton(int questionIdx, String answer, boolean isIndividual) {
        QuestionBlock quesseg = questionnaire.findQuestionBlock(questionIdx, isIndividual);
        quesseg.setAnswer(answer);
        try {
            quesseg.getScore(); /* 能够计算分数 */
            if (logger.isDebugEnabled()) {
                logger.debug("======================");
                logger.debug("量表编号：" + questionnaire.getScaleId());
                logger.debug("问题编号：" + quesseg.getId());
                logger.debug("问题分数：" + quesseg.getScore());
                logger.debug("第几道问题：" + questionIdx);
                logger.debug("答案(选项)：" + answer);
                logger.debug("subjion：" + quesseg.getSubjoin());
                logger.debug("\n" + quesseg.getQuestion());
                logger.debug("=========================");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onOpen(Map<Object, Object> params) throws QuestionnaireException {
        if (logger.isDebugEnabled())
            logger.debug("开始答题..." + getQuestionnaire().getTitle());
    }

}
