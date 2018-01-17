package edutec.scale.render;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.model.Question;

@SuppressWarnings("unchecked")
public abstract class RenderSupport implements Render {

    public void doRender(Question question, HashMap<Object, Object> cxt, Map params) {
        onBeforeRender(question, cxt, params);
        onRender(question, cxt, params);
        onAfterRender(question, cxt, params);
    }

    protected void onBeforeRender(Question question, HashMap<Object, Object> cxt, Map params) {
    }

    protected abstract void onRender(Question question, HashMap<Object, Object> cxt, Map params);

    protected void onAfterRender(Question question, HashMap<Object, Object> cxt, Map params) {
    }
}
