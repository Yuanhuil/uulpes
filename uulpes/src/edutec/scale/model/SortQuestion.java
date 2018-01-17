package edutec.scale.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.util.UtilCollection;

public class SortQuestion extends Question implements Calculable {
    private String values;
    private String options;

    @Override
    protected void xmlInter(StrBuilder sb) {
        sb.append("\t").append("<t>").append(this.getTitle()).append("</t>\n");
        sb.append("\t").append("<opt>").append(this.getOptions()).append("</opt>\n");
        sb.append("\t").append("<vas>").append(this.getValues()).append("</vas>\n");
        if (StringUtils.isNotBlank(getCalcExp()))
            sb.append("\t").append("<c>").append(this.getCalcExp()).append("</c>\n");
    }

    private byte[][] optionvals;

    public SortQuestion() {
        this.setType(QuestionConsts.TYPE_SORT);
    }

    public byte[][] getOptionVals() {
        if (optionvals == null) {
            String array[] = UtilCollection.toArray(values, '|');
            byte optionvals[][] = new byte[array.length][];
            for (int i = 0; i < array.length; ++i) {
                String vals[] = UtilCollection.toArray(values, ',');
                byte varray[] = new byte[vals.length];
                for (int j = 0; j < vals.length; ++j) {
                    varray[j] = (byte) NumberUtils.toInt(vals[j]);
                }
                optionvals[i] = varray;
            }
        }
        return optionvals;
    }

    public String[] options() {
        return StringUtils.split(getOptions(), ",");
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
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
