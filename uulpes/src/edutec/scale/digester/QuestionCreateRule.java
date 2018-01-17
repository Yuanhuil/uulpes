package edutec.scale.digester;

import org.apache.commons.digester.Rule;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

import edutec.scale.model.FillBlankQuestion;
import edutec.scale.model.MatrixQuestion;
import edutec.scale.model.QaQuestion;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.model.SortQuestion;
import edutec.scale.model.TmplateQuestion;

public class QuestionCreateRule extends Rule {
    public static final String TYPE = "type";

    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        String value = attributes.getValue(TYPE);
        if (StringUtils.isBlank(value)) {
            value = QuestionConsts.TYPE_SELECTION;
        }
        Object instance = null;
        if (value.equals(QuestionConsts.TYPE_SELECTION)) {
            instance = new SelectionQuestion();
        } else if (value.equals(QuestionConsts.TYPE_QA)) {
            instance = new QaQuestion();
        } else if (value.equals(QuestionConsts.TYPE_FILLBLANK)) {
            instance = new FillBlankQuestion();
        } else if (value.equals(QuestionConsts.TYPE_MATRIX)) {
            instance = new MatrixQuestion();
        } else if (value.equals(QuestionConsts.TYPE_TMPLATE)) {
            instance = new TmplateQuestion();
        } else if (value.equals(QuestionConsts.TYPE_SORT)) {
            instance = new SortQuestion();
        } else {
            throw new IllegalArgumentException("attributes not contain \"" + value + "\" attribute!");
        }
        digester.push(instance);
    }

    @Override
    public void end(String namespace, String name) throws Exception {
        digester.pop();
    }
}
