package edutec.scale.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.web.util.HtmlStr;

public class Formula implements Serializable, Calculable {
    /**
     * 
     */
    private static final long serialVersionUID = 3848180940792711888L;
    private String rawexp;
    private String stdexp;// Z分数公式
    private String texp;// T分数公式
    private String data;
    private float mean;
    private float stdv;
    private String nvalidexp;// 维度无效表达式

    public Formula() {
    }

    /**
     * 方便下面的情况： 1）建立维度的公式
     * 
     * @param rawexp
     * @param stdexp
     */
    public Formula(String rawexp, String stdexp) {
        this.rawexp = rawexp;
        this.stdexp = stdexp;
    }

    public Formula(String rawexp, String stdexp, String texp) {
        this.rawexp = rawexp;
        this.stdexp = stdexp;
        this.texp = texp;
    }

    /**
     * 方便下面的情况： 1）当量表的取分规则，被定为单一转换值时
     * 
     * @param mean
     * @param stdv
     * @param stdexp
     */
    public Formula(float mean, float stdv, String stdexp) {
        this.mean = mean;
        this.stdv = stdv;
        this.stdexp = stdexp;
    }

    public String getRawexp() {
        return rawexp;
    }

    public void setRawexp(String rawexp) {
        // rawexp = StringEscapeUtils.unescapeXml(rawexp);
        rawexp = HtmlStr.decodeString(rawexp);
        this.rawexp = rawexp;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public String getStdexp() {
        return stdexp;
    }

    public void setStdexp(String stdexp) {
        this.stdexp = stdexp;
    }

    public String getNvalidexp() {
        return nvalidexp;
    }

    public void setNvalidexp(String nvalidexp) {
        this.nvalidexp = nvalidexp;
    }

    public float getStdv() {
        return stdv;
    }

    public String getTexp() {
        return texp;
    }

    public void setTexp(String texp) {
        this.texp = texp;
    }

    public void setStdv(float stdv) {
        this.stdv = stdv;
    }

    public String getCalcExp() {
        return null;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toXml() {
        StrBuilder sb = new StrBuilder(64);
        sb.append("\t<formula>\n");
        if (StringUtils.isNotEmpty(HtmlStr.htmlEncode(getRawexp()))) {
            sb.append("\t<rawexp>");
            String xmlexp = HtmlStr.htmlEncode(rawexp.trim());
            sb.append(xmlexp);
            sb.append("</rawexp>\n");
        }
        if (StringUtils.isNotEmpty(HtmlStr.htmlEncode(stdexp))) {
            sb.append("\t<stdexp>");
            sb.append(stdexp.trim());
            sb.append("</stdexp>\n");
        }
        if (StringUtils.isNotEmpty(HtmlStr.htmlEncode(texp))) {
            sb.append("\t<texp>");
            sb.append(texp.trim());
            sb.append("</texp>\n");
        }
        if (StringUtils.isNotEmpty(HtmlStr.htmlEncode(nvalidexp))) {
            sb.append("\t<nvalidexp>");
            // sb.append(nvalidexp.trim());
            // 把>,<转义成xml格式
            // String xmlexp = StringEscapeUtils.escapeXml(nvalidexp.trim());
            String xmlexp = HtmlStr.decodeString(nvalidexp.trim());

            sb.append(xmlexp);
            sb.append("</nvalidexp>\n");
        }
        sb.append("\t</formula>\n");
        return sb.toString();
    }
}
