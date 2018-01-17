package edutec.scale.util.html;

public class TextField extends org.apache.ecs.html.Input {
    /*
     * public static void main(String[] args) { TextField tf = new
     * TextField("dd"); //System.out.println(tf); }
     */
    /**
     * 
     */
    private static final long serialVersionUID = -2693393040068488176L;

    protected String type;

    public TextField(String id) {
        this();
        this.setID(id);
    }

    public TextField() {
        super(org.apache.ecs.html.Input.text);
        setSize(50);
        setMaxlength(20);
    }
}
