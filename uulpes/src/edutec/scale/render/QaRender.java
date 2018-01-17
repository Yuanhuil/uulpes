package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;
import edutec.scale.util.html.Button;
import edutec.scale.util.html.TextField;

public class QaRender extends RenderSupport {
    private static final String PAGE_QA = "qaQuestion.flt";

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TagInfo tr = new TagInfo(question);
        TextField textField = new TextField(question.getId());
        String btnclickjs = String.format(JS_QA_OK, tr.getBtname(), tr.getNameOrId());
        String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
        Button button = new Button(btnTitle, tr.getBtname(), btnclickjs);
        button.setName(tr.getBtname());
        /*
         * sbContent.append("<textarea rows='").append(qaQuestion.getRows());
         * sbContent.append("' name='").append(PARAM_ANSWER);
         * sbContent.append("' cols='").append(qaQuestion.getCols());
         * sbContent.append("'></textarea>");
         */
        cxt.put("qaQuestion", textField.toString());
        cxt.put("buttonCtl", button.toHtml());
        // cxt.setTemplate(PAGE_QA);
        cxt.put("template", PAGE_QA);
    }

}
