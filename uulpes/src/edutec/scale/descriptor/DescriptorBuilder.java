package edutec.scale.descriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.text.StrBuilder;

import edutec.scale.model.TitleableSupport;
import heracles.util.UtilCollection;
import heracles.util.UtilMisc;
import heracles.web.util.HtmlStr;

public class DescriptorBuilder extends TitleableSupport {
    private static final String PACKAGE = DescriptorBuilder.class.getPackage().getName() + ".";
    private String data;
    private String text;
    private String image;
    private String descn;
    private String defaultText;
    private String[] classnames;

    public Descriptor doBuild() {
        Descriptor descriptor = DescriptorHelper.createDescriptor(classnames[0]);
        Validate.notNull(descriptor, "没有定义[" + classnames[0] + "].");
        descriptor.setEffectClassname(classnames[1]);
        descriptor.setData(data); // 数据
        descriptor.setImage(image);// 图像
        descriptor.setText(text); // 文字
        descriptor.setDescn(descn); // 描述
        descriptor.setDefaultText(defaultText);
        return descriptor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setClassname(String classname) {
        String strs[] = UtilCollection.toArray(classname, '|');
        if (strs.length == 2) {
            classnames = new String[] { PACKAGE + strs[0], PACKAGE + strs[1] };
        } else {
            classnames = new String[] { PACKAGE + strs[0], "null" };
        }

    }

    @Override
    public String toString() {
        StrBuilder sb = new StrBuilder();
        sb.append(ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE));
        sb.append("\n");
        return sb.toString();
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getTitle() {
        return null;
    }

    public String getId() {
        return null;
    }

    /**
     * <descriptor classname="SpmDescriptor|null" data="select val from ${table}
     * where agegroup=${agegroup} order by val desc" descn="rpt-tpl:explain.flt"
     * />
     * 
     * @return
     */
    public String toXml() {
        StrBuilder sb = new StrBuilder();
        sb.append("<descriptor classname=");
        String classnamsProp = getClassname(classnames[0]) + "|" + getClassname(classnames[1]);
        sb.append(UtilMisc.quote(classnamsProp));
        if (StringUtils.isNotEmpty(this.getData()))
            sb.append(" data=").append(UtilMisc.quote(HtmlStr.htmlEncode(getData())));
        if (StringUtils.isNotEmpty(this.getDefaultText()))
            sb.append(" defaultText=").append(UtilMisc.quote(getDefaultText()));
        String text = StringUtils.defaultString(getText());
        text = HtmlStr.htmlEncode(text);
        sb.append(" text=").append(UtilMisc.quote(text));

        if (StringUtils.isNotEmpty(descn))
            sb.append(" descn=").append(UtilMisc.quote(HtmlStr.htmlEncode(descn)));
        if (StringUtils.isNotEmpty(image))
            sb.append(" image=").append(UtilMisc.quote(image));
        sb.append("/>\n");
        return sb.toString();
    }

    String getClassname(String str) {
        return str.substring(PACKAGE.length());
    }
}
