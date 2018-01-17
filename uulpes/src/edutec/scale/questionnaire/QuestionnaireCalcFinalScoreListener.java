package edutec.scale.questionnaire;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.descriptor.Descriptor;
import edutec.scale.exception.QuestionnaireException;

@Scope("prototype")
@Component("QuestionnaireCalcFinalScoreListener")
public class QuestionnaireCalcFinalScoreListener extends QuestionnaireListener {

    @Override
    public void onAnswerQuesiton(int questionIdx, String answer, boolean isIndividual) {

    }

    @Override
    public void onClose(Map<Object, Object> params) throws QuestionnaireException {
        Descriptor descriptor = questionnaire.getDescriptor();
        // descriptor.calAllDimFinalScore();
    }

    @Override
    public void onOpen(Map<Object, Object> params) throws QuestionnaireException {

    }

}
