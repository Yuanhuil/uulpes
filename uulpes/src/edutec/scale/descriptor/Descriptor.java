package edutec.scale.descriptor;

import java.util.List;
import java.util.Map;

import edutec.scale.questionnaire.DimDetail;
import edutec.scale.questionnaire.Questionnaire;
import heracles.jfree.JChartParam;

@SuppressWarnings("unchecked")
public interface Descriptor {
    public final String DESCN_STD_VALUES_KEY = "std-values";

    void setData(String data);

    void setText(String text);

    void setImage(String image);

    void setDescn(String descn);

    void setDefaultText(String defaultText);

    void setEffectClassname(String effectClassname);

    void setQuestionnaire(Questionnaire questionnaire);

    void setSubjectUser(Object user);

    void setObserverUser(Object user);

    void setDimDetailList(List<DimDetail> dimDetailList);

    // added by zhaowanfeng
    void setSubjectUserInfo(Object user);

    void setObserverUserInfo(Object user);

    Object getSubjectUserInfo();

    Object getObserverUserInfo();

    String getData();

    String getText();

    String getImage();

    String getDescn();

    String getDefaultText();

    Object getSubjectUser();

    Object getObserverUser();

    Questionnaire getQuestionnaire();

    String toHtml(Map<Object, Object> params) throws Exception;

    void toOfficeWord(Map<Object, Object> params) throws Exception;

    String getDataTablename();

    String getTextTablename();

    Map<String, Object> getDataDetail(Map map);

    Map<String, Object> getFactorDetail();

    Map<String, Object> getUserInfoDetail();

    Map<String, Object> getTextDetail(Map params);

    String getDescnPropStr(String key, String defaultVal);

    int getDescnPropInt(String key);

    JChartParam getChartParam();

    boolean isNeedRecalulate();

    public void setNeedRecalulate(boolean needRecalulate);

    // public void calAllDimFinalScore();

}
