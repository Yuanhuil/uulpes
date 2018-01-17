package edutec.scale.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.ecs.AlignType;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.TR;

import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.util.html.Checkbox;
import edutec.scale.util.html.Radio;
import edutec.scale.util.html.Select;
import edutec.scale.util.html.SelectOption;
import edutec.scale.util.html.SimpleHtmlTable;
import edutec.scale.util.html.TextField;
import heracles.util.UtilCollection;
import heracles.web.util.HtmlStr;

/**
 * 为选择题生成页面<br>
 * 1）mutil button;<br>
 * 2）checkbox+button<br>
 * 3）select+button<br>
 * 
 * @author Administrator
 */
public class SelecionRender extends RenderSupport {
    private static final String SELECTION_QUESTION_KEY = "selectionQuestion";
    private static final String RENDER_XOR = "render:xor(";

    // FLT 模板页
    /*
     * private static final String PAGE_SEL_SGL = "selectionQuestionSgl.flt"; //
     * 单选按钮 private static final String PAGE_SEL_MULT =
     * "selectionQuestionMult.flt";// 多选框和Xor题目 private static final String
     * PAGE_SEL = "selectionQuestion.flt"; // Radio和select private static final
     * String PAGE_MX = "selectionQuestionMx.flt"; // 单选中含有文本框 private static
     * final String PAGE_TEXT = "selectionQuestionText.flt"; // 只有文本框 private
     * static final String PAGE_IMG = "selectionQuestionImg.flt"; // 图片
     */
    // 模板页
    private static final String PAGE_SEL_SGL = "selectionQuestionSgl"; // 单选按钮
    private static final String PAGE_SEL_SGL_PREFIX = "prefixSelectionQuestion"; // 单选有前缀后缀
    private static final String PAGE_SEL_MULT = "selectionQuestionMult";// 多选框和Xor题目
    private static final String PAGE_SEL = "selectionQuestion"; // Radio和select
    private static final String PAGE_MX = "selectionQuestionMx"; // 单选中含有文本框
    private static final String PAGE_TEXT = "selectionQuestionText"; // 只有文本框
    private static final String PAGE_IMG = "selectionQuestionImg"; // 图片

    // 顺序字母

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        if (question.isTemplate()) {
            Map<String, String> props = question.getDescnProps();
            String template = props.get(QuestionConsts.DESCN_TEMPLATE_KEY);
            String paramstr = props.get("param");
            if (!StringUtils.isEmpty(paramstr)) {
                List<String> paramList = UtilCollection.toList(paramstr, UtilCollection.COMMA);
                int i = 0;
                for (String param : paramList) {
                    cxt.put("param" + i, param);
                    i++;
                }
            }
            // cxt.setTemplate(template);
            cxt.put("template", template);
            return;
        }
        SelectionQuestion sQ = (SelectionQuestion) question;
        switch (sQ.getChoiceMode()) {
        case QuestionConsts.CHOICE_SINGLE_MODE:
            buildChoiceSgl(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_MULTI_MODE:
            buildChoiceMutli(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_SEL_MODE:
            buildChoiceSel(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_RADIO_MODE:
            buildChoiceRadio(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_MIX_MODE:
            buildChoiceMix(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_TEXT_MODE:
            buildChoiceText(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_IMG_MODE:
        case QuestionConsts.CHOICE_IMG2_MODE:
            buildChoiceImg(sQ, cxt);
            break;
        case QuestionConsts.CHOICE_IMGX_MODE:
            buildChoiceImgx(sQ, cxt);
            break;
        }

        // buildChoiceSgl(sQ, cxt);
    }

    private void buildChoiceMutli(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        if (sQ.getDescn() != null && sQ.getDescn().startsWith("render")) {
            cxt.put(SELECTION_QUESTION_KEY, buildWithDescnXor(sQ, cxt));
        } else {
            int pos = 0;
            StrBuilder sbContent = new StrBuilder(sQ.getSize() * 128);
            TagInfo tagInfo = TagInfo.create(sQ);
            String chkclickjs = String.format(JS_SELECT_OPT, tagInfo.getNameOrId(), tagInfo.getRecorderId());
            List optionList = new ArrayList();
            for (Option opt : sQ.getOptions()) {
                Checkbox checkbox = new Checkbox(tagInfo.getNameOrId(), "" + (pos++), chkclickjs);
                sbContent.append(checkbox.toHtml());
                sbContent.append(opt.getTitle());
                optionList.add(opt);
            }
            cxt.put(SELECTION_QUESTION_KEY, sbContent.toString());
            cxt.put("optionList", optionList);
        }
        // cxt.setTemplate(PAGE_SEL_MULT);
        cxt.put("template", PAGE_SEL_MULT);

    }

    private void buildChoiceSel(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        TagInfo tagInfo = TagInfo.create(sQ);
        String onchangeJs = String.format(JS_SEL_ITEM, tagInfo.getNameOrId(), tagInfo.getRecorderId());
        Select select = new Select(tagInfo.getNameOrId());
        SelectOption selectOptions[] = new SelectOption[sQ.optionSize() + 1];
        select.setMultiple(false);
        select.setOnChange(onchangeJs);
        List<Option> opts = sQ.getOptions();
        selectOptions[0] = new SelectOption(StringUtils.EMPTY, "==请选择==");
        for (int i = 0; i < sQ.optionSize(); ++i) {
            // 选项的索引次序，就是html option的值，取得其索引
            selectOptions[i + 1] = new SelectOption(String.valueOf(i), opts.get(i).getTitle());
        }
        select.setOptions(selectOptions);
        // cxt.setTemplate(PAGE_SEL);
        cxt.put("template", PAGE_SEL);
        cxt.put(SELECTION_QUESTION_KEY, select.toHtml());
    }

    private void buildChoiceImg(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        final String IMG_POSTFIX = ".GIF";
        final String IMG_QSCR_STR = "mywork/img_scale/%s/%s" + IMG_POSTFIX;
        final String IMG_QSCR_STR2 = "mywork/img_scale/%s/%s_" + IMG_POSTFIX;
        final String IMG_ASCR_STR = "mywork/img_scale/%s/%s_%d" + IMG_POSTFIX;

        TagInfo tagInfo = TagInfo.create(sQ);

        String qId = sQ.getId();
        String scaleId = sQ.getScale().getId();
        String qimgscr = String.format(IMG_QSCR_STR, scaleId, qId);

        IMG qimg = new IMG(qimgscr);
        String imgshow = qimg.toString();
        if (sQ.getChoice().endsWith("2")) {
            String qimgscr2 = String.format(IMG_QSCR_STR2, scaleId, qId);
            IMG qimg2 = new IMG(qimgscr2);
            imgshow += qimg2.toString();
        }

        if (StringUtils.isNotEmpty(sQ.getTitle())) {
            SimpleHtmlTable imgshowTable = new SimpleHtmlTable();
            imgshowTable.addTrH(new String[] { imgshow, sQ.getTitle() });
            imgshow = imgshowTable.toString();
        }

        SimpleHtmlTable table = new SimpleHtmlTable();
        TR tr1 = table.newTr();
        TR tr2 = table.newTr();

        int qsize = sQ.getSize();
        if (qsize > 0) {
            int part = qsize / 2;
            if (qsize <= 4) {
                part = qsize;
            }
            char c = 'A';
            for (int i = 0; i < part; ++i) {
                String btnclickjs = String.format(JS_SELECT_ONE, tagInfo.getBtname(), i);
                String aimgscr = String.format(IMG_ASCR_STR, scaleId, qId, i + 1);
                IMG aimg = new IMG(aimgscr);
                aimg.setName(tagInfo.getBtname());
                aimg.setOnMouseMove("javascript:this.style.border='2px solid #0000FF';");
                aimg.setOnMouseOut("javascript:this.style.border='0px';");
                aimg.setStyle("cursor:pointer");
                aimg.setOnClick(btnclickjs);
                tr1.addElement(table.newTD(c + ".", 50, AlignType.right));
                tr1.addElement(table.newTD(aimg.toString(), AlignType.center));
                ++c;
            }
            for (int i = part; i < qsize; ++i) {
                String btnclickjs = String.format(JS_SELECT_ONE, tagInfo.getBtname(), i);
                String aimgscr = String.format(IMG_ASCR_STR, scaleId, qId, i + 1);
                IMG aimg = new IMG(aimgscr);
                aimg.setName(tagInfo.getBtname());
                aimg.setOnMouseMove("javascript:this.style.border='2px solid #0000FF';");
                aimg.setOnMouseOut("javascript:this.style.border='0px';");
                aimg.setStyle("cursor:pointer");
                aimg.setOnClick(btnclickjs);
                tr2.addElement(table.newTD(c + ".", 50, AlignType.right));
                tr2.addElement(table.newTD(aimg.toString(), AlignType.center));
                ++c;
            }
        }
        cxt.put("questionImg", imgshow);
        if (sQ.optionSize() > 0) {
            buildChoiceSgl(sQ, cxt);
            cxt.put("answerImgTable", StringUtils.EMPTY);
        } else {
            cxt.put(SELECTION_QUESTION_KEY, StringUtils.EMPTY);
            cxt.put("width", StringUtils.EMPTY);
            cxt.put("align", StringUtils.EMPTY);
            cxt.put("answerImgTable", table.toString());
        }
        // cxt.setTemplate(PAGE_IMG);
        cxt.put("template", PAGE_IMG);
    }

    private void buildChoiceImgx(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        TagInfo tagInfo = TagInfo.create(sQ);
        Map<String, String> props = sQ.getDescnProps();
        // 如果==null显示lab，否不显示
        boolean ishasLab = props.get(QuestionConsts.DESCN_LABEL_KEY) == null;
        IMG qimg = new IMG(props.get(QuestionConsts.DESCN_IMGSRC_KEY));

        String imgshow = qimg.toString();
        if (StringUtils.isNotEmpty(sQ.getTitle())) {
            SimpleHtmlTable imgshowTable = new SimpleHtmlTable();
            imgshowTable.addTrH(new String[] { imgshow, sQ.getTitle() });
            imgshow = imgshowTable.toString();
        }

        SimpleHtmlTable table = new SimpleHtmlTable();
        TR tr1 = table.newTr();
        TR tr2 = table.newTr();

        int qsize = sQ.optionSize();
        if (qsize > 0) {
            int part = qsize / 2;
            if (qsize <= 4) {
                part = qsize;
            }
            char c = 'A';
            for (int i = 0; i < part; ++i) {
                String btnclickjs = String.format(JS_SELECT_ONE, tagInfo.getBtname(), i);
                Map<String, String> oprops = sQ.getOptions().get(i).getDescnProps();
                String aimgscr = oprops.get(QuestionConsts.DESCN_IMGSRC_KEY);
                IMG aimg = new IMG(aimgscr);
                aimg.setName(tagInfo.getBtname());
                aimg.setOnMouseMove("javascript:this.style.border='2px solid #0000FF';");
                aimg.setOnMouseOut("javascript:this.style.border='0px';");
                aimg.setStyle("cursor:pointer");
                aimg.setOnClick(btnclickjs);
                if (ishasLab) {
                    tr1.addElement(table.newTD(c + ".", 50, AlignType.right));
                } else {
                    tr1.addElement(table.newTD(StringUtils.EMPTY, 50));
                }
                tr1.addElement(table.newTD(aimg.toString(), AlignType.center));
                ++c;
            }
            for (int i = part; i < qsize; ++i) {
                String btnclickjs = String.format(JS_SELECT_ONE, tagInfo.getBtname(), i);
                Map<String, String> oprops = sQ.getOptions().get(i).getDescnProps();
                String aimgscr = oprops.get(QuestionConsts.DESCN_IMGSRC_KEY);
                IMG aimg = new IMG(aimgscr);
                aimg.setName(tagInfo.getBtname());
                aimg.setOnMouseMove("javascript:this.style.border='2px solid #0000FF';");
                aimg.setOnMouseOut("javascript:this.style.border='0px';");
                aimg.setStyle("cursor:pointer");
                aimg.setOnClick(btnclickjs);
                tr2.addElement(table.newTD(c + ".", 50, AlignType.right));
                tr2.addElement(table.newTD(aimg.toString(), AlignType.center));
                ++c;
            }
        }
        cxt.put("questionImg", imgshow);
        cxt.put(SELECTION_QUESTION_KEY, StringUtils.EMPTY);
        cxt.put("width", StringUtils.EMPTY);
        cxt.put("align", StringUtils.EMPTY);
        cxt.put("answerImgTable", table.toString());
        // cxt.setTemplate(PAGE_IMG);
        cxt.put("template", PAGE_IMG);
    }

    private void buildChoiceText(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        TagInfo tagInfo = TagInfo.create(sQ);
        TextField textField = new TextField();
        textField.setID(tagInfo.getRecorderId());
        cxt.put("txtId", tagInfo.getRecorderId());
        cxt.put("validate", sQ.getValidator());
        // cxt.setTemplate(PAGE_TEXT);
        cxt.put("template", PAGE_TEXT);
        StrBuilder sb = new StrBuilder(StringUtils.defaultString(sQ.getFormat()));
        sb.append("<br>").append(textField.toString());
        cxt.put(SELECTION_QUESTION_KEY, sb);
    }

    private void buildChoiceMix(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        int pos = 0;
        TagInfo tagInfo = TagInfo.create(sQ);
        SimpleHtmlTable table = new SimpleHtmlTable();
        String cells[] = new String[sQ.optionSize()];
        String jscript1 = String.format(JS_SELECT_OPT, tagInfo.getNameOrId(), tagInfo.getRecorderId());
        String jscript2 = String.format(JS_CLEAR_SELECT_OPT, tagInfo.getTextId(), tagInfo.getNameOrId(),
                tagInfo.getRecorderId());
        StrBuilder optTitle = new StrBuilder();
        for (Option opt : sQ.getOptions()) {
            Radio radio = new Radio();
            optTitle.clear();
            Map<String, String> props = opt.getDescnProps();
            optTitle.append(opt.getTitle());
            String val = props.get(QuestionConsts.DESCN_COMPONENT_KEY);
            if (val != null) {
                if (val.equals("TextField")) {
                    TextField textField = new TextField(tagInfo.getTextId());
                    optTitle.append("<br>");
                    optTitle.append(textField.toString());
                    cxt.put("txtId", tagInfo.getTextId());
                    cxt.put("chkId", tagInfo.getChkId(pos));
                    radio.setOnClick(jscript1);
                }
            } else {
                radio.setOnClick(jscript2);
            }
            radio.setValue(pos);
            radio.setID(tagInfo.getChkId(pos));
            radio.setName(tagInfo.getNameOrId());
            radio.setText(optTitle.toString());
            cells[pos] = radio.toString();
            ++pos;
        }
        table.addTrV(cells);
        cxt.put(SELECTION_QUESTION_KEY, table.toString());
        // cxt.setTemplate(PAGE_MX);
        cxt.put("template", PAGE_MX);
    }

    /**
     * 生成页面，页面格式为 <input id='ff' type='radio' value='1'>ll
     * 
     * @param sQ
     * @param cxt
     * @return
     */
    private void buildChoiceRadio(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        int pos = 0;
        TagInfo tagInfo = TagInfo.create(sQ);
        SimpleHtmlTable table = new SimpleHtmlTable();
        String cells[] = new String[sQ.optionSize()];
        String jscript = String.format(JS_SELECT_OPT, tagInfo.getNameOrId(), tagInfo.getRecorderId());
        for (Option opt : sQ.getOptions()) {
            Radio radio = new Radio();
            radio.setValue(pos);
            radio.setName(tagInfo.getNameOrId());
            radio.setOnClick(jscript);
            radio.setText(opt.getTitle());
            cells[pos] = radio.toString();
            ++pos;
        }
        // cxt.setTemplate(PAGE_SEL);
        cxt.put("template", PAGE_SEL);
        table.addTrV(cells);
        cxt.put(SELECTION_QUESTION_KEY, table.toString());
    }

    /**
     * 生成页面，处理带有descn属性内容的题目,用于特殊的题目
     * 
     * @param sQ
     * @param sbContent
     * @param tagInfo
     * @param pos
     */
    private String buildWithDescnXor(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        int pos = 0;
        TagInfo tagInfo = TagInfo.create(sQ);
        String descn = sQ.getDescn().toLowerCase();
        SimpleHtmlTable table = new SimpleHtmlTable();
        String cells[] = new String[sQ.optionSize()];
        StrBuilder chkCtl = new StrBuilder();
        if (descn.startsWith(RENDER_XOR)) {
            XorInfo xor = buildXorJs(sQ, tagInfo, descn);
            for (Option opt : sQ.getOptions()) {
                chkCtl.clear();
                String js = null;
                if (pos < xor.getGap()) {
                    js = xor.getJsStms()[0];
                } else {
                    js = xor.getJsStms()[1];
                }
                String choice = opt.getChoice();
                if (choice != null && choice.equals(QuestionConsts.CHOICE_NO)) {
                    chkCtl.append(opt.getTitle());
                } else {
                    Checkbox checkbox = new Checkbox(tagInfo.getNameOrId(), "" + pos, js);
                    chkCtl.append(checkbox.toHtml());
                    chkCtl.append(opt.getTitle());
                }
                cells[pos] = chkCtl.toString();
                pos++;
            }
            table.addTrV(cells);
            table.setAlign("center");
            return table.toString();
        }
        return "";
    }

    /**
     * 生成按钮，一个选项一个按钮，点击一下按钮进入下一页
     * 
     * @param sQ
     * @return
     */
    private void buildChoiceSgl(SelectionQuestion sQ, HashMap<Object, Object> cxt) {
        // int pos = 0;
        // TagInfo tagInfo = TagInfo.create(sQ);
        // SimpleHtmlTable table = new SimpleHtmlTable();
        // table.setStyle("margin-top:20px;border-collapse:collapse;");
        // String cells[] = new String[sQ.optionSize()];
        // int width = 150;
        // StrBuilder btnTitle = new StrBuilder();
        // char c = 'A';
        List optionList = new ArrayList();
        for (Option opt : sQ.getOptions()) {
            /*
             * String btnclickjs = "";//String.format(JS_SELECT_ONE,
             * tagInfo.getBtname(), pos); btnTitle.clear(); if
             * (!sQ.isPlaceholder()) {
             * btnTitle.append(c).append(")").append(HtmlStr.blankIfEmpty(null))
             * .append(opt.getTitle()); } else {
             * btnTitle.append(opt.getTitle()); } int size = btnTitle.length() *
             * 20; if (size > width) { width = size; } Input button = new
             * Button(tagInfo.getBtname(), btnTitle.toString(), btnclickjs);
             */
            // Checkbox checkbox = new
            // Checkbox(tagInfo.getBtname(),btnTitle.toString(), btnclickjs);
            opt.setTitle(HtmlStr.decodeString(opt.getTitle()));
            optionList.add(opt);
            // cells[pos] = button.toHtml();
            // pos++;
            // ++c;
        }
        // String style = "";
        String align;
        align = AlignType.left;
        /*
         * if (width < 300) { align = "center"; table.addTrH(cells); } else {
         * align = "left"; table.addTrV(cells); }
         */
        // table.addTrV(cells);
        // cxt.put("width", width);
        cxt.put("align", align);
        // table.setAlign(AlignType.center);
        if (StringUtils.isNotEmpty(sQ.getPrefix())) {
            int optSize = sQ.optionSize();
            cxt.put("prefixMin", 0);
            cxt.put("prefixMax", optSize - 1);
            cxt.put("prefix", sQ.getPrefix());
            cxt.put("postfix", sQ.getPostfix());
            cxt.put("template", PAGE_SEL_SGL_PREFIX);
        } else
            cxt.put("template", PAGE_SEL_SGL);
        // cxt.put(SELECTION_QUESTION_KEY, table.toString());
        cxt.put("optionList", optionList);
    }

    private XorInfo buildXorJs(SelectionQuestion sQ, TagInfo tagInfo, String descn) {
        String result[] = new String[2];
        char c = descn.charAt(RENDER_XOR.length());
        int gap = CharUtils.toIntValue(c);
        // 去除gap后面的所有check,gap,sQ.optionSize()-1是由于多了一项说明option
        result[0] = String.format(JS_SELECT_XOR, tagInfo.getNameOrId(), gap - 1, gap, sQ.optionSize() - 1,
                tagInfo.getNameOrId(), tagInfo.getRecorderId());
        // 去除gap前面的check
        result[1] = String.format(JS_SELECT_XOR, tagInfo.getNameOrId(), -1, gap - 1, gap, tagInfo.getNameOrId(),
                tagInfo.getRecorderId());
        return new XorInfo(gap, result);
    }

    private class XorInfo {
        private final int gap;
        private final String jsStms[];

        public XorInfo(int gap, String[] jss) {
            this.gap = gap;
            this.jsStms = jss;
        }

        public String[] getJsStms() {
            return jsStms;
        }

        public int getGap() {
            return gap;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onAfterRender(Question question, HashMap<Object, Object> cxt, Map params) {
        TagInfo nr = new TagInfo(question);
        String btnclickjs;
        switch (question.getChoiceMode()) {
        case QuestionConsts.CHOICE_MULTI_MODE:
            btnclickjs = String.format(Render.JS_CHK_MUTL_OK, nr.getBtname(), nr.getNameOrId(), nr.getRecorderId());
            params.put(Render.PARA_JS, btnclickjs);
            ButtonWithRecorder.instance().doRender(question, cxt, params);
            break;
        case QuestionConsts.CHOICE_SEL_MODE:
        case QuestionConsts.CHOICE_RADIO_MODE:
        case QuestionConsts.CHOICE_MIX_MODE:
        case QuestionConsts.CHOICE_TEXT_MODE:
            String msg = "您需要选择一项！";
            if (question.getMsg() != null) {
                msg = question.getMsg();
            }
            btnclickjs = String.format(Render.JS_SUBMIT, nr.getBtname(), nr.getRecorderId(), msg);
            params.put(Render.PARA_JS, btnclickjs);
            ButtonWithRecorder.instance().doRender(question, cxt, params);
            break;
        }
    }

}
