package heracles.enums;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

public class PadStringTransformer implements Transformer {
    public static enum Aligment {
        LEFT, RIGHT
    };

    private final Aligment aligment;
    private final int size;
    private final char padChar;

    public PadStringTransformer(Aligment aligment, int size, char padChar) {
        super();
        this.aligment = aligment;
        this.size = size;
        this.padChar = padChar;
    }

    public PadStringTransformer(int size, char padChar) {
        super();
        this.aligment = Aligment.LEFT;
        this.size = size;
        this.padChar = padChar;
    }

    public PadStringTransformer(int size) {
        super();
        this.aligment = Aligment.LEFT;
        this.size = size;
        this.padChar = '0';
    }

    public Object transform(Object value) {
        String str = value.toString();
        if (Aligment.LEFT == aligment)
            return StringUtils.leftPad(str, size, padChar);
        else {
            return StringUtils.rightPad(str, size, padChar);
        }
    }

}
