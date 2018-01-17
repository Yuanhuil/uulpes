package edutec.scale.lie;

import java.util.ArrayList;
import java.util.List;

import edutec.scale.model.Question;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.Questionnaire;

public class CommLieInsertPosition implements LieInsertPosition {

    public List<Question> insertLies(Questionnaire questionnaire) {
        List<SelectionQuestion> lieQs = LieThink.getLieQuestions(questionnaire);
        List<Question> result = new ArrayList<Question>(questionnaire.getScale().getQuestions());
        int m = result.size();
        for (Question q : lieQs) {
            result.add(m, q);
            m -= 1;
        }
        return result;
    }

}
