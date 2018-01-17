package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;

public class TitleRender extends RenderSupport {
    private static final String PAGE_TITLE = "TitleQuestion";

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TagInfo tr = new TagInfo(question);
        String title = question.getTitle(); /* 填充题的标题中含${X},代表放置文本域的地方 */

        // String btnclickjs = JS_START_ANSWER;
        String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
        // Button button = new Button("btn_title",TITLE_BTN_START, btnclickjs);
        String limittime = question.getLimittime();
        cxt.put("limittime", limittime);
        cxt.put("titleQuetion", title);
        // cxt.put("buttonCtl", button.toHtml());
        cxt.put("template", PAGE_TITLE);
    }
}
