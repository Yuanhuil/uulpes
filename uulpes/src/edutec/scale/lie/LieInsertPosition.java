package edutec.scale.lie;

import java.util.List;

import edutec.scale.model.Question;
import edutec.scale.questionnaire.Questionnaire;

public interface LieInsertPosition {
    List<Question> insertLies(Questionnaire questionnaire);
}
