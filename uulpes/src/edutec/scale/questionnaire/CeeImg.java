package edutec.scale.questionnaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;

import heracles.util.UtilCollection;

/**
 * 图形数据模型
 * 
 * @author 王文
 */
public class CeeImg {
    private static final String IMG_HERF = "ceeScaleChart.cht?";
    private static final String IMG_DIM_HERF = "ceeDimChart.cht?";
    public static final String HEIGH_PARAM = "h";
    public static final String WIDTH_PARAM = "w";
    public static final String DATASET_PARAM = "d";
    private final List<DimDetail> dimDetailList;
    private final int gender;

    private static Map<String, Double> r = new HashMap<String, Double>(9);
    private static Map<String, Double> i = new HashMap<String, Double>(9);
    private static Map<String, Double> a = new HashMap<String, Double>(9);
    private static Map<String, Double> s = new HashMap<String, Double>(9);
    private static Map<String, Double> e = new HashMap<String, Double>(9);
    private static Map<String, Double> c = new HashMap<String, Double>(9);
    private static List<Map<String, Double>> dimArray = new ArrayList<Map<String, Double>>(6);
    private final static String F = "f";
    private final static String D = "d";
    private final static String Y = "y";
    static {
        r.put(F, 17.4647);
        r.put(D, 6.2057);
        r.put(Y, 28.7237);
        r.put(F + "1", 22.967);
        r.put(D + "1", 11.509);
        r.put(Y + "1", 34.425);
        r.put(F + "2", 11.841);
        r.put(D + "2", 4.113);
        r.put(Y + "2", 19.569);
        i.put(F, 17.1781);
        i.put(D, 5.3122);
        i.put(Y, 29.044);
        i.put(F + 1, 18.728);
        i.put(D + 1, 6.042);
        i.put(Y + 1, 31.414);
        i.put(F + 2, 15.594);
        i.put(D + 2, 4.846);
        i.put(Y + 2, 26.342);

        a.put(F, 16.1532);
        a.put(D, 4.9623);
        a.put(Y, 27.3441);
        a.put(F + 1, 15.089);
        a.put(D + 1, 3.896);
        a.put(Y + 1, 26.282);
        a.put(F + 2, 17.241);
        a.put(D + 2, 6.631);
        a.put(Y + 2, 29.067);
        s.put(F, 26.5741);
        s.put(D, 16.8842);
        s.put(Y, 36.264);
        s.put(F + 1, 25.077);
        s.put(D + 1, 15.124);
        s.put(Y + 1, 35.03);
        s.put(F + 2, 28.105);
        s.put(D + 2, 18.931);
        s.put(Y + 2, 37.279);

        e.put(F, 19.2988);
        e.put(D, 7.2648);
        e.put(Y, 31.3328);
        e.put(F + 1, 20.715);
        e.put(D + 1, 8.083);
        e.put(Y + 1, 33.347);
        e.put(F + 2, 17.849);
        e.put(D + 2, 6.631);
        e.put(Y + 2, 29.067);

        c.put(F, 17.9192);
        c.put(D, 6.2092);
        c.put(Y, 29.6292);
        c.put(F + 1, 16.156);
        c.put(D + 1, 5.208);
        c.put(Y + 1, 27.104);
        c.put(F + 2, 19.718);
        c.put(D + 2, 7.528);
        c.put(Y + 2, 31.908);

        dimArray.add(r);
        dimArray.add(i);
        dimArray.add(a);
        dimArray.add(s);
        dimArray.add(e);
        dimArray.add(c);
    }

    public CeeImg(List<DimDetail> dimDetailList, int gender) {
        this.dimDetailList = dimDetailList;
        this.gender = gender;
    }

    public String getSummarizeHerf() {
        StrBuilder sb = new StrBuilder(IMG_HERF);
        sb.append(DATASET_PARAM).append("=");
        for (DimDetail dimDetail : dimDetailList) {
            sb.append(dimDetail.getRawScore());
            sb.append(UtilCollection.COMMA);
        }
        return sb.toString();
    }

    private String[] dimHerfs = null;

    public String[] getDimHerfs() {
        if (dimHerfs == null) {
            dimHerfs = new String[dimDetailList.size()];
            StrBuilder sb = new StrBuilder();
            String fkey = F + gender;
            String dkey = D + gender;
            String ykey = Y + gender;
            int index = 0;
            for (DimDetail dimDetail : dimDetailList) {
                sb.clear();
                sb.append(IMG_DIM_HERF);
                sb.append(DATASET_PARAM).append("=");
                sb.append(dimArray.get(index).get(dkey));
                sb.append(UtilCollection.COMMA);
                sb.append(dimArray.get(index).get(ykey));
                sb.append(UtilCollection.COMMA);
                sb.append(dimDetail.getRawScore());
                sb.append(UtilCollection.COMMA);
                sb.append(dimArray.get(index).get(fkey));
                sb.append(UtilCollection.COMMA);
                dimHerfs[index] = sb.toString();
                index++;
            }
        }
        return dimHerfs;
    }

}
