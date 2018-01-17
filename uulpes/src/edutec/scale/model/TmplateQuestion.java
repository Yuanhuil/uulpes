package edutec.scale.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.text.StrBuilder;

public class TmplateQuestion extends Question {
    private String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        StrBuilder sb = new StrBuilder();
        sb.append(ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE));
        sb.append("\n");
        return sb.toString();
    }

    public String getCalcExp() {
        return null;
    }

    @Override
    public String toHTML() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toAnswerHtml(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toHeadtitleHTML() {
        // TODO Auto-generated method stub
        return null;
    }
}
