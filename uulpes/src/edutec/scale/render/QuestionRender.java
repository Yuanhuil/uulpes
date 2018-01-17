package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;

/**
 * 注意：Render类没有成员变量，无状态，所以可以作为静态实例； 为了提高性能，而使Render类以及其子类，没有状态
 * 
 * @author Administrator
 */
public class QuestionRender extends RenderSupport {
    private static final QuestionRender render = new QuestionRender();

    private static final Render[] renderArray = { new MxRender(), new FillBlankRender(), new QaRender(),
            new SelecionRender(), new SortRender(), new TemplateRender(), new TitleRender(), new JudgeRender() };

    public static Render getInstance() {
        return render;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Question question, HashMap<Object, Object> cxt, Map params) {
        Render rder = renderArray[question.getTypeMode()];
        if (rder == null) {
            throw new RuntimeException("不能识别问题类型");
        }
        rder.doRender(question, cxt, params);
    }

}
