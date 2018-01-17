package edutec.scale.util;

import java.awt.Color;
import java.io.Serializable;

/**
 * @author 胡晓宁
 * @version 0.1
 * @since 2008-5-16
 */
@SuppressWarnings("serial")
public class ImageData implements Serializable {
    String picname;

    String[] picdatename;

    double[] picdatenum;

    Color[] barcolor;

    String flag;
    // 竖，横。高考为"shutu"
    String kind;

    /**
     * @return Returns the kind.
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind
     *            The kind to set.
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return Returns the flag.
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @return Returns the picdatename.
     */
    public String[] getPicdatename() {
        return picdatename;
    }

    /**
     * @return Returns the picdatenum.
     */
    public double[] getPicdatenum() {
        return picdatenum;
    }

    /**
     * @return Returns the picname.
     */
    public String getPicname() {
        return picname;
    }

    /**
     * @param flag
     *            The flag to set.
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * @param picdatename
     *            The picdatename to set.
     */
    public void setPicdatename(String[] picdatename) {
        this.picdatename = picdatename;
    }

    /**
     * @param picdatenum
     *            The picdatenum to set.
     */
    public void setPicdatenum(double[] picdatenum) {
        this.picdatenum = picdatenum;
    }

    /**
     * @param picname
     *            The picname to set.
     */
    public void setPicname(String picname) {
        this.picname = picname;
    }

    public Color[] getBarcolor() {
        return barcolor;
    }

    public void setBarcolor(Color[] barcolor) {
        this.barcolor = barcolor;
    }

}
