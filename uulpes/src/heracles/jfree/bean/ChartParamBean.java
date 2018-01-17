package heracles.jfree.bean;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.DoubleRange;

import heracles.jfree.ChartConstants;
import heracles.jfree.builder.BarChartBuilder;
import heracles.jfree.builder.ChartBuilder;
import heracles.jfree.builder.IntervalBarChartBuilder;
import heracles.jfree.builder.PlotBuilderSupport;
import heracles.util.Resources;
import heracles.util.UtilCollection;
import net.sf.json.JSONObject;

/**
 * ================================================================= 数据编码如下：
 * 一.类别数据格式,用于饼图，柱形图 chd={type:"category",data:"2,row1,col1|2,row2,col2|...."};
 * chd={type:"category",data:"2,col1|2,col2|...."}; 二. 间隔数据格式，用于柱形图
 * chd={type:"interval",data:"col1,1,2|col2,3,8|..."};
 * ==================================================================
 * cht=br&chs=300x400&chd={type:"category",data:"2,xx,xx|2,xx,xx|"}&
 * chdr=100,700&chrm={type:"range",start:123,end:234}&
 * chax={type:"category",labpos:90} chrm={type:"interval",val:"12.5,12.5"}
 * chplot={ort:"h",bg:}; //ort表示h=horizontal or v=vertical
 * 
 * @author Administrator
 */
public class ChartParamBean {

    private String cht; // 图片类型
    private String chs; // 图表大小
    private String chd; // 数据
    private String chdr; // 数据范围
    private String chrm; // RangeMarkers,将放置在数值轴面上的区间marks
    private String chax; // 图形的轴
    private String chplot; // Plot相关属性
    private String bean; //

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getChplot() {
        return chplot;
    }

    public void setChplot(String chplot) {
        this.chplot = chplot;
    }

    public String getChax() {
        return chax;
    }

    public void setChax(String chax) {
        this.chax = chax;
    }

    static public ChartParamBean fromJson(String json) {
        return (ChartParamBean) JSONObject.toBean(JSONObject.fromObject(json));
    }

    public String getChdr() {
        return chdr;
    }

    public void setChdr(String chdr) {
        this.chdr = chdr;
    }

    public DoubleRange getChdRange() {
        DoubleRange rng = null;
        try {
            rng = UtilCollection.toDoubleRange(chdr, UtilCollection.COMMA);
        } catch (Exception e) {
            try {
                rng = UtilCollection.toDoubleRange(chdr, UtilCollection.RGN_CHAR);
            } catch (Exception ex) {
                rng = null;
            }
        }
        return rng;
    }

    public ChartParamBean() {
    }

    @SuppressWarnings("unchecked")
    public String urlEncode() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> describe = BeanUtils.describe(this);
        return UtilCollection.toString(describe, UtilCollection.AMP_CHAR, UtilCollection.EQ);
    }

    public void urlDecode(String str) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> properties = UtilCollection.toMap(str, UtilCollection.AMP_CHAR, UtilCollection.EQ);
        BeanUtils.populate(this, properties);
    }

    public String getCht() {
        return cht;
    }

    public void setCht(String cht) {
        this.cht = cht;
    }

    public String getChs() {
        return chs;
    }

    public void setChs(String chs) {
        this.chs = chs;
    }

    public String getChd() {
        return chd;
    }

    public void setChd(String chd) {
        this.chd = chd;
    }

    public String getChrm() {
        return chrm;
    }

    public void setChrm(String chrm) {
        this.chrm = chrm;
    }

    public Dimension getSize() {
        if (StringUtils.isEmpty(chs)) {
            return new Dimension();
        } else {
            int[] pp = UtilCollection.toIntArray(chs, ChartConstants.SIZE_SPLIT_CHAR);
            return new Dimension(pp[0], pp[1]);

        }
    }

    public AxisBean getAxisBean() {
        String s = this.getChax();
        if (StringUtils.isNotEmpty(s)) {
            AxisBean axisBean = new AxisBean(s);
            return axisBean;
        }
        return null;
    }

    public ChartBuilder createChartBuilder()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        PlotBuilderSupport builder = null;
        if (!StringUtils.equalsIgnoreCase(getBean(), "y")) {
            builder = (PlotBuilderSupport) Resources.instantiate(getBean());
        } else if (this.getCht().equalsIgnoreCase("br")) {
            builder = new BarChartBuilder();
        } else if (this.getCht().equalsIgnoreCase("ibr")) {
            builder = new IntervalBarChartBuilder();
        }
        builder.setChartParamBean(this);
        return builder;
    }

}
