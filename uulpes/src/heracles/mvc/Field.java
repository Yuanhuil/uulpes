package heracles.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import heracles.db.ibatis.DbQuery;
import heracles.domain.model.Titleable;
import heracles.util.Resources;
import heracles.util.UtilCollection;
import heracles.web.util.HtmlStr;

public class Field extends PropBean {
    public static void main(String[] args) {
        Field field = new Field();
        field.setLabel("我 们");
        System.out.println(field.getLabel());
    }

    public static String FLD_TYPE_SELECT = "select";
    public static String FLD_TYPE_YESNO = "yesno";
    public static String FLD_TYPE_RANGE = "range";
    public static String FLD_TYPE_TEXT = "text";
    public static String FLD_TYPE_DATE = "date";

    public static String TYPE_DATE_TL = "日期型";
    public static String TYPE_TEXT_TL = "文本型";
    public static String TYPE_RANGE_TL = "范围型";
    public static String TYPE_YESNO_TL = "是否型";
    public static String TYPE_SELECT_TL = "选择型";

    public static Map<String, String> FLD_TYPE_DESC;

    static {
        FLD_TYPE_DESC = new LinkedHashMap<String, String>(5);
        FLD_TYPE_DESC.put(FLD_TYPE_TEXT, TYPE_TEXT_TL);
        FLD_TYPE_DESC.put(FLD_TYPE_DATE, TYPE_DATE_TL);
        FLD_TYPE_DESC.put(FLD_TYPE_YESNO, TYPE_YESNO_TL);
        FLD_TYPE_DESC.put(FLD_TYPE_SELECT, TYPE_SELECT_TL);
        FLD_TYPE_DESC.put(FLD_TYPE_RANGE, TYPE_RANGE_TL);
    }

    public static int DATA_TYPE_NUMBER = 1;
    public static int DATA_TYPE_STRING = 2;
    public static int FLAG_ENCODE = 1;
    public static int FLAG_REQ = 2; // 必须的字段

    private String id;
    private String name;
    private String label;
    private String type;
    private String fname;
    private String optDataSource;
    private List<String> dependList;
    private int dataType = DATA_TYPE_STRING;
    private int flag;

    public void setEnableReq(boolean enable) {
        if (enable) {
            flag |= FLAG_REQ;
        } else {
            flag &= ~FLAG_REQ;
        }
    }

    public boolean isEnableReq() {
        return (flag & FLAG_REQ) > 0;
    }

    public void setEnableEncode(boolean enable) {
        if (enable) {
            flag |= FLAG_ENCODE;
        } else {
            flag &= ~FLAG_ENCODE;
        }
    }

    public boolean isRquest() {
        if (dependList == null)
            return false;
        for (String depend : dependList) {
            if (depend.indexOf("required") != -1) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnableEncode() {
        return (flag & FLAG_ENCODE) > 0;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public Field() {
        super();
    }

    public Field(String str) throws IllegalAccessException, InvocationTargetException {
        super(str);
    }

    public List<String> getDependList() {
        return dependList;
    }

    public void setDepends(String depends) {
        this.dependList = UtilCollection.toList(depends, '#');
        ;
    }

    public String getDepends() {
        return UtilCollection.toString(dependList, '#');
    }

    public String getType() {
        return StringUtils.defaultIfEmpty(type, "text");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    /**
     * 一律转换为小写
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return StringUtils.defaultIfEmpty(name, getId());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return HtmlStr.htmlEncode(label);
    }

    public String getPlatLabel() {
        return StringUtils.deleteWhitespace(label);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addDepend(String depend) {
        if (dependList == null) {
            dependList = new ArrayList<String>();
        }
        dependList.add(depend);
    }

    public String getOptDataSource() {
        return optDataSource;
    }

    public void setOptDataSource(String dataSource) {
        this.optDataSource = dataSource;
    }

    public String getFname() {
        return StringUtils.defaultIfEmpty(fname, getId());
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * 1. 来自某个类的静态方法<br>
     * 如： static:edutec.primary.enums.AccountTypeEnum.getSysEnumMap<br>
     * 2. 来自数据库查询<br>
     * 如： sql:select key, value from xxxx<br>
     * 3. 来自一个字符串<br>
     * 如： string:1=是;0=否<br>
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SelectOption> getOptionList() {
        List<SelectOption> optionList = new ArrayList<SelectOption>();
        List<String> strs = UtilCollection.toList(this.optDataSource, UtilCollection.COLON);
        if (strs.isEmpty()) {
            return optionList;
        }
        try {
            Map map = null;
            if (strs.size() == 1) {
                // 静态值{k=v;k1=v1;...kn=vn}
                map = UtilCollection.lineConvertMap(strs.get(0));
            } else {
                String direct = strs.get(0);
                String mothedOrStr = strs.get(1);
                if (direct.equalsIgnoreCase("static")) {
                    // 返回值为Map
                    map = (Map) Resources.callStaticMethod(mothedOrStr);
                } else if (direct.equalsIgnoreCase("sql")) {
                    // sql必为：select k， v from table,第一个为key，第二个为值
                    DbQuery qry = new DbQuery();
                    List list = (List) qry.query(mothedOrStr, new ArrayListHandler());
                    Iterator it = list.iterator();
                    map = new LinkedHashMap();
                    while (it.hasNext()) {
                        Object os[] = (Object[]) it.next();
                        map.put(os[0], os[1]);
                    }
                } else if (direct.equalsIgnoreCase("string")) {
                    // 静态值{k=v;k1=v1;...kn=vn}
                    map = UtilCollection.lineConvertMap(mothedOrStr);
                } else if (direct.equalsIgnoreCase("list")) {
                    List list = (List) Resources.callStaticMethod(mothedOrStr);
                    for (Object obj : list) {
                        map = new LinkedHashMap();
                        if (obj instanceof KeyValue) {
                            KeyValue keyValue = (KeyValue) obj;
                            map.put(keyValue.getKey(), keyValue.getValue());
                        } else if (obj instanceof Titleable) {
                            Titleable titleable = (Titleable) obj;
                            map.put(titleable.getObjectIdentifier(), titleable.getTitle());
                        } else {

                        }
                    }
                } else if (direct.equalsIgnoreCase("opts")) {
                    List<SelectOption> opts = (List<SelectOption>) Resources.callStaticMethod(mothedOrStr);
                    return opts;
                }
            }
            if (map != null) {
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry ent = (Map.Entry) it.next();
                    String value = ent.getKey().toString();
                    String title = ent.getValue().toString();
                    SelectOption opt = new SelectOption(value, title);
                    optionList.add(opt);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return optionList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
