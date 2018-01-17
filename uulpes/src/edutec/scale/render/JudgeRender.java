package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;
import edutec.scale.util.html.Radio;
import edutec.scale.util.html.SimpleHtmlTable;

public class JudgeRender extends RenderSupport {
    private static final String PAGE_JUDGE = "judgequestion";

    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {

        TagInfo tagInfo = TagInfo.create(question);
        SimpleHtmlTable table = new SimpleHtmlTable();
        table.setStyle("border-collapse:collapse;width:100px;");
        String cells[] = new String[2];
        // String jscript = String.format(JS_SELECT_OPT, tagInfo.getNameOrId(),
        // tagInfo.getRecorderId());
        Radio radioY = new Radio();
        radioY.setStyle("vertical-align:top;width:16px;");
        radioY.setValue(0);
        radioY.setName(tagInfo.getNameOrId());
        radioY.setOnClick(String.format(JS_SELECT_ONE, tagInfo.getBtname(), 0));
        radioY.setText(params.get("jyLabel").toString());
        // radioY.setType("font-size:20px;");
        cells[0] = radioY.toString();

        Radio radioN = new Radio();
        radioN.setStyle("vertical-align:top;width:16px;");
        radioN.setValue(1);
        radioN.setName(tagInfo.getNameOrId());
        radioN.setOnClick(String.format(JS_SELECT_ONE, tagInfo.getBtname(), 1));
        radioN.setText(params.get("jnLabel").toString());
        // radioN.setType("font-size:20px;");
        cells[1] = radioN.toString();
        cxt.put("template", PAGE_JUDGE);
        table.addTrV1(cells);
        cxt.put("selectionQuestion", table.toString());

    }

}
