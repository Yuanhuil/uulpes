package edutec.scale.util.html;

public class Radio extends org.apache.ecs.html.Input {
    private static final long serialVersionUID = 1410785816305603696L;

    public static void main(String[] args) {
        Radio r = new Radio();
        r.setText("text");
        // System.out.println(r);
    }

    public Radio() {
        super(org.apache.ecs.html.Input.radio);
    }

    public void setText(String text) {
        addElement(text);
    }

}
