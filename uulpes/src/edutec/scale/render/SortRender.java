package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrSubstitutor;

import edutec.scale.model.Question;
import edutec.scale.model.SortQuestion;
import edutec.scale.util.html.Button;
import edutec.scale.util.html.HiddenField;
import edutec.scale.util.html.Input;
import edutec.scale.util.html.Select;
import edutec.scale.util.html.SelectOption;
import heracles.util.UtilMisc;

public class SortRender extends RenderSupport {
    private static final String PAGE_SORT = "sortQuestion.flt";
    private static final String SCRIPT1 = "moveUp('${qid}','${qid}_a')";
    private static final String SCRIPT2 = "moveDown('${qid}','${qid}_a')";

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        SortQuestion sortQ = (SortQuestion) question;
        TagInfo nr = new TagInfo(question);

        /* 创建select标记 */
        String[] options = sortQ.options();
        SelectOption selectOptions[] = new SelectOption[options.length];
        StrBuilder sb = new StrBuilder();
        for (int i = 0; i < options.length; ++i) {
            selectOptions[i] = new SelectOption(i + "", options[i]);
            if (i != options.length - 1)
                sb.append(i).append(",");
        }
        Select selectctl = new Select(nr.getNameOrId());
        selectctl.setOptions(selectOptions);

        /* 创建按钮标记 */
        Map map = UtilMisc.toMap("qid", nr.getNameOrId());
        StrSubstitutor ss = new StrSubstitutor(map);

        String onclick1 = ss.replace(SCRIPT1);
        String onclick2 = ss.replace(SCRIPT2);

        Input button1 = new Button("x", "上移", onclick1);
        Input button2 = new Button("y", "下移", onclick2);

        HiddenField hidden = new HiddenField(nr.getRecorderId());
        hidden.setValue(sb.toString());

        cxt.put("selectCtl", selectctl.toHtml());
        cxt.put("buttonCtl1", button1.toHtml());
        cxt.put("buttonCtl2", button2.toHtml());
        cxt.put("recordCtl", hidden.toString());

        // cxt.setTemplate(PAGE_SORT);
        cxt.put("template", PAGE_SORT);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onAfterRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TagInfo nr = new TagInfo(question);
        String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
        String btnclickjs = String.format(Render.JS_SORT_FINSH, nr.getBtname(), nr.getRecorderId());
        Input button = new Button(btnTitle, nr.getBtname(), btnclickjs);
        cxt.put("buttonCtl", button.toHtml());
    }

}
