package edutec.scale.questionnaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.exception.QuestionnaireException;

@Scope("prototype")
@Component("QuestionnaireCompistionListener")
public class QuestionnaireCompistionListener extends QuestionnaireListener {
    private List<QuestionnaireListener> listeners = new ArrayList<QuestionnaireListener>();

    public void addQuestionnaireListener(QuestionnaireListener listener) {
        listeners.add(listener);
    }

    public void removeAllQuestionnaireListener() {
        listeners.clear();
    }

    @Override
    public void setSubjectUser(Object user) {
        super.setSubjectUser(user);
        for (QuestionnaireListener listener : listeners) {
            listener.setSubjectUser(user);
        }

    }

    @Override
    public void setObserverUser(Object observerUser) {
        super.setObserverUser(observerUser);
        for (QuestionnaireListener listener : listeners) {
            listener.setObserverUser(observerUser);
        }
    }

    @Override
    public void setSubjectUserInfo(Object subjectUserId) {
        super.setSubjectUserInfo(subjectUserId);
        for (QuestionnaireListener listener : listeners) {
            listener.setSubjectUserInfo(subjectUserId);
        }

    }

    @Override
    public void setObserverUserInfo(Object observerUser) {
        super.setObserverUserInfo(observerUser);
        for (QuestionnaireListener listener : listeners) {
            listener.setObserverUserInfo(observerUser);
        }
    }

    @Override
    public void onClose(Map<Object, Object> params) throws QuestionnaireException {
        for (QuestionnaireListener listener : listeners) {
            listener.onClose(params);
        }

    }

    @Override
    public void setQuestionnaire(Questionnaire questionnaire) {
        super.setQuestionnaire(questionnaire);
        for (QuestionnaireListener listener : listeners) {
            listener.setQuestionnaire(questionnaire);
        }

    }

    @Override
    public void onAnswerQuesiton(int questionIdx, String answer, boolean isIndividual) throws QuestionnaireException {
        for (QuestionnaireListener listener : listeners) {
            listener.onAnswerQuesiton(questionIdx, answer, isIndividual);
        }
    }

    @Override
    public void onOpen(Map<Object, Object> params) throws QuestionnaireException {
        for (QuestionnaireListener listener : listeners) {
            listener.onOpen(params);
        }

    }

}
