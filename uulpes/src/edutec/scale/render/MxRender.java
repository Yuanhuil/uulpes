package edutec.scale.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import edutec.scale.model.MatrixQuestion;
import edutec.scale.model.MatrixQuestion.MxInfo;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.util.html.Button;
import edutec.scale.util.html.Checkbox;
import edutec.scale.util.html.Input;
import heracles.util.TreeNode;
import heracles.web.util.HtmlStr;
import heracles.web.util.HtmlTable;

public class MxRender extends RenderSupport {
    private static final String PAGE_MX_SGL = "mxQuestionSgl.flt";
    private static final String PAGE_MX_MUL = "mxQuestionMult.flt";
    private static final String PAGE_MX_MIX = "mxQuestionMix.flt";

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        MatrixQuestion mxQuestion = (MatrixQuestion) question;
        MatrixQuestion.MxInfo mxInfo = mxQuestion.getMxInfo();

        HtmlTable htmTable = new HtmlTable(mxInfo.rowsize() * mxInfo.colsize() * 128);
        htmTable.setTableClass("table");
        htmTable.setTdClass("td");
        /* 设置标题 */
        htmTable.addValColTitles(mxInfo.getColtitles());
        /* 设置内容 */
        htmTable.addAll(newChkHtmlMaker(mxInfo).doMake());

        cxt.put("mxQuestion", htmTable.toString());

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onAfterRender(Question question, HashMap<Object, Object> cxt, Map params) {
        MatrixQuestion mxQuestion = (MatrixQuestion) question;
        if (mxQuestion.getChoice().equals(QuestionConsts.CHOICE_SINGLE)) {
            // cxt.setTemplate(PAGE_MX_SGL);
            cxt.put("template", PAGE_MX_SGL);
        }
        if (mxQuestion.getChoice().equals(QuestionConsts.CHOICE_MULTI)) {
            /* 为多选 */
            TagInfo nr = new TagInfo(question);
            String btnclickjs = String.format(Render.JS_CHK_MUTL_OK, nr.getBtname(), nr.getNameOrId(),
                    nr.getRecorderId());
            params.put(Render.PARA_JS, btnclickjs);
            new ButtonWithRecorder().doRender(question, cxt, params);
            // cxt.setTemplate(PAGE_MX_MUL);
            cxt.put("template", PAGE_MX_MUL);
        } else if (mxQuestion.getChoice().equals(QuestionConsts.CHOICE_MIX)) {
            /* 为页面添加按钮，点击此按钮可以进入下一页 */
            MxInfo mxInfo = mxQuestion.getMxInfo();
            TagInfo nr = new TagInfo(question);
            StrBuilder sbJs = new StrBuilder(256);
            sbJs.append("{");
            if (StringUtils.isNotBlank(mxQuestion.getSelectOpts())) {
                for (int rowIdx = 0; rowIdx < mxInfo.rowsize(); ++rowIdx) {
                    int startidx = mxInfo.rowStartIdx(rowIdx);
                    int endidx = mxInfo.rowEndIdx(rowIdx);
                    sbJs.append(validateScript(nr.getNameOrId(), startidx, endidx, mxInfo.rowTitle(rowIdx)));
                }
            }
            String selectok = String.format(Render.JS_CHK_MX_OK, nr.getBtname(), nr.getNameOrId());
            sbJs.append(selectok);
            sbJs.append("}");
            String btnTitle = params.get(Render.PARA_BTN_TITLE).toString();
            Input button = new Button(nr.getBtname(), btnTitle, sbJs.toString());
            cxt.put("buttonCtl", button.toHtml());
            // cxt.setTemplate(PAGE_MX_MIX);
            cxt.put("template", PAGE_MX_MIX);
        }
    }

    private MxChkHtmlMaker newChkHtmlMaker(MxInfo mxInfo) {
        if (mxInfo.getChoice().equals(QuestionConsts.CHOICE_SINGLE)) {
            return new ChkSglMaker(mxInfo);
        }
        if (mxInfo.getChoice().equals(QuestionConsts.CHOICE_MULTI)) {
            return new ChkMultMaker(mxInfo);
        } else if (mxInfo.getChoice().equals(QuestionConsts.CHOICE_MIX)) {
            return new ChkMixMaker(mxInfo);
        }
        return null;
    }

    class ChkMixMaker extends MxChkHtmlMaker {
        public ChkMixMaker(MxInfo mxInfo) {
            super(mxInfo);
        }

        @Override
        protected String getHtml() {
            Checkbox checkbox = new Checkbox(mxInfo.getId());
            checkbox.setValue(curridx + "");
            int startidx = mxInfo.rowStartIdx(currrowIdx);
            int endidx = mxInfo.rowEndIdx(currrowIdx);
            int limit = mxInfo.limit(currrowIdx);
            if (limit != -1) {
                if (mxInfo.limit(currrowIdx) == 1) {
                    String chkclickjs = String.format(JS_UNCHK_OTHERS, mxInfo.getId(), curridx, startidx, endidx);
                    checkbox.setOnClick(chkclickjs);
                } else {
                    String msg = mxInfo.rowTitle(currrowIdx);
                    String chkclickjs = String.format(JS_VALIDATE_NOT_GREAT_THEN, mxInfo.getId(), curridx, startidx,
                            endidx, limit, msg);
                    checkbox.setOnClick(chkclickjs);
                }
            }
            return checkbox.toHtml();
        }
    }

    class ChkMultMaker extends MxChkHtmlMaker {
        private String chkclickjs;

        public ChkMultMaker(MxInfo mxInfo) {
            super(mxInfo);
            TagInfo nr = new TagInfo(mxInfo.getQuestion());
            chkclickjs = String.format(JS_SELECT_OPT, nr.getNameOrId(), nr.getRecorderId());
        }

        @Override
        protected String getHtml() {
            Checkbox checkbox = new Checkbox(mxInfo.getId(), "" + curridx, chkclickjs);
            return checkbox.toHtml();
        }
    }

    class ChkSglMaker extends MxChkHtmlMaker {
        public ChkSglMaker(MxInfo mxInfo) {
            super(mxInfo);
        }

        @Override
        protected String getHtml() {
            TagInfo nr = new TagInfo(mxInfo.getQuestion());
            String btnclickjs = String.format(JS_SELECT_ONE, nr.getBtname(), curridx++);
            Input button = new Button("选择", nr.getBtname(), btnclickjs);
            return button.toHtml();
        }
    }

    abstract class MxChkHtmlMaker {
        protected int curridx = 0;
        protected MatrixQuestion.MxInfo mxInfo;
        protected int currrowIdx = 0;

        public MxChkHtmlMaker(MatrixQuestion.MxInfo mxInfo) {
            this.mxInfo = mxInfo;
        }

        public List<TreeNode> doMake() {

            List<TreeNode> list = new ArrayList<TreeNode>(mxInfo.rowsize());
            for (currrowIdx = 0; currrowIdx < mxInfo.rowsize(); ++currrowIdx) {
                TreeNode tn = new TreeNode(mxInfo.rowTitle(currrowIdx));
                for (int colIdx = 0; colIdx < mxInfo.colsize(); ++colIdx) {
                    String html = getHtml();
                    tn.addValue(html);
                    ++curridx;
                }
                list.add(tn);
            }
            int pos = -1;
            MatrixQuestion.MxPart mP = mxInfo.getMxPart();
            if (mP != null) {
                pos = mP.getPosition();
                TreeNode tn = new TreeNode(mP.getHoldPlace());
                for (int colIdx = 0; colIdx < mxInfo.colsize(); ++colIdx) {
                    tn.addValue(HtmlStr.blankIfEmpty(null));
                }
                list.add(pos, tn);
            }
            return list;
        }

        protected abstract String getHtml();
    }

    private String validateScript(String chkname, int staridx, int endidx, String msg) {
        String chkclickjs = String.format(JS_VALIDATE_ATLAST_ONE, chkname, staridx, endidx, msg);
        return chkclickjs;
    }

}
