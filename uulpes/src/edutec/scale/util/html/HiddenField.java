package edutec.scale.util.html;

public class HiddenField extends org.apache.ecs.html.Input {
    private static final long serialVersionUID = -184015257959127349L;

    public HiddenField(String id) {
        super(org.apache.ecs.html.Input.hidden);
        this.setID(id);
    }

    public HiddenField() {
        super(org.apache.ecs.html.Input.hidden);
    }

}
