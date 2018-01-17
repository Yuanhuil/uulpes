package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;
import edutec.scale.util.html.Button;
import edutec.scale.util.html.HiddenField;

public class ButtonWithRecorder {
    static ButtonWithRecorder instance = new ButtonWithRecorder();

    public static ButtonWithRecorder instance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void doRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TagInfo nr = TagInfo.create(question);
        HiddenField recorder = new HiddenField(nr.getRecorderId());
        // 按钮的标题需要动态确定
        String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
        // 传递按钮的jscript代码
        String btnclickjs = params.get(Render.PARA_JS).toString();
        Button button = new Button(nr.getBtname(), btnTitle, btnclickjs);
        // 设置id和name
        button.setId(nr.getBtnId());
        // 向模板传递html代码
        cxt.put("buttonCtlId", nr.getBtnId()); // css按钮
        cxt.put("buttonCtl", button.toHtml());
        cxt.put("recordCtl", recorder.toString());
    }

}
