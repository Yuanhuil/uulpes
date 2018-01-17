package edutec.scale.exam;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.organization.Organization;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;
import heracles.util.SimpleDateFormat;
import heracles.util.UtilCollection;

public class Examdo {
    @Autowired
    public CachedScaleMgr cachedScaleMgr;
    public static final String START_DO = "开始做题";
    public static final String DONE = "已测（%s）";
    public static final String LIMIT_DO = "再测此量表，需%s天后";
    public static final String HREF;

    public final static byte NO_LIMIT = 0;
    public final static byte LIMIT_DONE = 2;
    public final static byte LIMIT = 1;

    private String name;
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private int gradeid;
    private int classid;
    private Organization org;

    public String getClassTitle() {
        // ClassDetail cl = org.getClassDetail(getClassid()+"");
        // return cl.getGradeName() + cl.getTitle();
        return "";
    }

    public String getGenderWord() {
        return Constants.G_DESC[getGender()];
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    public int getGradeid() {
        return gradeid;
    }

    public void setGradeid(int gradeid) {
        this.gradeid = gradeid;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static {
        HREF = "<a href=\"javascript:location.replace('openQuestionnaire.ques?scaleId=%s\')\">" + START_DO + "</a>";
    }

    private String scaleId;
    private Date okTime;
    private Date loTime;
    private Date hiTime;
    private byte flag;

    public void setScaleId(int sid) {
        this.setScaleId(sid + "");
    }

    public Scale getScale() {
        List<String> scaleid_ = UtilCollection.toList(scaleId, '|');
        return cachedScaleMgr.get(scaleid_.get(0));
    }

    public String getTitle() {
        Scale sx = getScale();
        return sx.getTitle();
    }

    public String getType() {
        Scale sx = getScale();
        Validate.notNull(sx);
        return sx.getType();
    }

    public String getDuration() {
        StrBuilder sb = new StrBuilder(11 * 2 + 2);
        sb.append(SimpleDateFormat.format(getLoTime())).append("至");
        sb.append(SimpleDateFormat.format(getHiTime()));
        return sb.toString();
    }

    public String getOkTime() {
        if (this.okTime != null)
            return SimpleDateFormat.format(okTime, SimpleDateFormat.DATE_PATTEN_TM);
        else {
            return "未做";
        }
    }

    public int getQuestionSize() {
        Scale sx = getScale();
        return sx.getQuestionSize();
    }

    public String getAction() {
        String herf = String.format(HREF, this.getScaleId());
        if (okTime == null) {
            return herf;
        } else {
            if (Examdo.NO_LIMIT == (getFlag() & Examdo.LIMIT)) {
                if (this.okTime != null) {
                    return "已经做完";
                }
                return herf;
            } else if (Examdo.LIMIT == (getFlag() & Examdo.LIMIT)) {
                /* 三个月后的时间 */
                Date limitDate = DateUtils.addMonths(okTime, 3);
                /* 三个月后的开始时间 */
                Date limitStart = SimpleDateFormat.getDayStart(limitDate);
                /* 今天 */
                Date nowStart = SimpleDateFormat.getDayStart(new Date());
                /* 今天在okTime三个月后之前 */
                if (nowStart.before(limitDate)) {
                    String fmtdu = DurationFormatUtils.formatDuration(limitStart.getTime() - nowStart.getTime(), "dd");
                    String a = String.format(LIMIT_DO, fmtdu);
                    return a;
                } else {
                    return herf;
                }
            }
        }
        return "还没有";
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public void setOkTime(Date okTime) {
        this.okTime = okTime;
    }

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public Date getHiTime() {
        return hiTime;
    }

    public void setHiTime(Date hiTime) {
        this.hiTime = hiTime;
    }

    public Date getLoTime() {
        return loTime;
    }

    public void setLoTime(Date loTime) {
        this.loTime = loTime;
    }
}
