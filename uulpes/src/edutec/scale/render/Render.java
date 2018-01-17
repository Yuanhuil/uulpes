package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;

public interface Render {
    /**
     * 多选题提交
     */
    final String JS_CHK_MUTL_OK = "chkmutlok('%s','%s','%s');";
    final String JS_CHK_MX_OK = "chkmxok('%s','%s');";
    /**
     * 选择题的提交，按钮名，记录值的input hiden，如果记录值的input hiden为空，则alert（msg） function
     * commit(btname,recorderId,msg)
     */
    final String JS_SUBMIT = "submit('%s','%s','%s');";
    final String JS_SEL_ITEM = "selectItem('%s','%s')";

    /* 多选题中，用户选择chkbox/radio，触发此方法 */
    final String JS_SELECT_OPT = "selectOpt('%s','%s');";
    final String JS_CLEAR_SELECT_OPT = "clearText('%s');selectOpt('%s','%s');";

    /* 多选题中，用户选择多选盒，触发此方法 */
    final String JS_SELECT_XOR = "unchkOthers('%s',%d,%d,%d); selectOpt('%s','%s');";

    /* 用户点击了一个按钮，立即进入下一页 */
    final String JS_SELECT_ONE = "disableall('%s'); selectOne(%d);return false;";

    final String JS_START_ANSWER = "startanswer();";

    /* 提交排序题 */
    final String JS_SORT_FINSH = "disableall('%s'); sortfininsh('%s');";

    /* 提交问答题 */
    final String JS_QA_OK = "qaok('%s','%s');";

    /* 多选题中只有一个选项可选 */
    final String JS_UNCHK_OTHERS = "unchkOthers('%s',%d,%d,%d);";

    /* 多选题中不能超过制定选项 */
    final String JS_VALIDATE_NOT_GREAT_THEN = "validateNotGreatThen('%s',%d,%d,%d,%d,'%s');";

    /* 验证至少要选一项，用在矩阵混合多选时 */
    final String JS_VALIDATE_ATLAST_ONE = "if (!validateAtLeastOne('%s',%d,%d,'%s')) return false;\n";

    /* 做题按钮，最后一道题是完成 */
    final String PARA_BTN_TITLE = "btn";
    final String TITLE_BTN_END = "结束";
    final String TITLE_BTN_NQ = "下一题";
    final String TITLE_BTN_START = "开始";
    final String PARA_JS = "js";

    @SuppressWarnings("unchecked")
    void doRender(Question question, HashMap<Object, Object> cxt, Map params);
}
