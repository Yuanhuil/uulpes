package edutec.scale.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edutec.scale.model.Question;
import edutec.scale.model.TmplateQuestion;
import heracles.util.UtilCollection;

public class TemplateRender extends RenderSupport {

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TmplateQuestion tQ = (TmplateQuestion) question;
        String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
        cxt.put(Render.PARA_BTN_TITLE, btnTitle);
        cxt.put("question_title", question.getTitle());

        Map<String, String> map = tQ.getDescnProps();
        String paramstr = map.get("param");
        if (!StringUtils.isEmpty(paramstr)) {
            List<String> paramList = UtilCollection.toList(paramstr, UtilCollection.COMMA);
            int i = 0;
            for (String param : paramList) {
                cxt.put("param" + i, param);
                i++;
            }
        }
        // cxt.setTemplate(question.getDescn());
        cxt.put("template", question.getDescn());
    }
}
