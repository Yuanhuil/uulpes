package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import edutec.scale.model.Question;
import edutec.scale.util.html.Button;
import edutec.scale.util.html.TextField;
import heracles.util.UtilMisc;

public class FillBlankRender extends RenderSupport {
    private static final String PAGE_FB = "fiilBlankQuestion.flt";

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TagInfo tr = new TagInfo(question);
        String title = question.getTitle(); /* 填充题的标题中含${X},代表放置文本域的地方 */
        TextField textField = new TextField(question.getId());
        String btnclickjs = String.format(JS_QA_OK, tr.getBtname(), tr.getNameOrId());
        String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
        Button button = new Button(btnTitle, tr.getBtname(), btnclickjs);
        button.setName(tr.getBtname());
        Map m = UtilMisc.toMap("X", textField.toString());
        StrSubstitutor ss = new StrSubstitutor(m);
        cxt.put("fillBlankQuetion", ss.replace(title));
        cxt.put("buttonCtl", button.toHtml());
        cxt.put("template", PAGE_FB);
    }

}
