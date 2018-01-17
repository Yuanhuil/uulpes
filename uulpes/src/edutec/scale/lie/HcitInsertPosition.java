package edutec.scale.lie;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import edutec.scale.model.Question;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.Questionnaire;

public class HcitInsertPosition implements LieInsertPosition {
    public List<Question> insertLies(Questionnaire questionnaire) {
        List<Question> result = new ArrayList<Question>(questionnaire.getScale().getQuestions());
        List<SelectionQuestion> lieQs = LieThink.getLieQuestions(questionnaire);
        int pos = 0;
        int i = 0;
        for (; i < 7; i++) {
            Question q = lieQs.get(i);
            pos = 34 + RandomUtils.nextInt(32);
            result.add(pos, q);
        }
        for (; i < lieQs.size(); i++) {
            Question q = lieQs.get(i);
            pos = 107 + RandomUtils.nextInt(32);
            result.add(pos, q);
        }
        return result;
    }

}
