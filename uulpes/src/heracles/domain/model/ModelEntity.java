package heracles.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import heracles.util.UtilCollection;

public class ModelEntity extends HashMap<Object, Object> implements Serializable {
    private static final long serialVersionUID = -2022561580795921714L;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATION_TIME = "creationTime";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED_TIME = "lastModifiedTime";
    public static final String VERSION = "version";
    public static final String USE_ID = "userId";
    public static final String ORG_ID = "orgId";
    public static final String GRADE_META_ID = "grademetaId";
    public static final String GRADE_CLASS_ID = "gradeclassId";
    public static final String ATTRS = "attributes";
    public static final String USER_FLAG = "flag";
    public static final String USER_NAME = "username";
    public static final String SCALE_ID = "scaleId";
    public static final String PAGE_PARAM = "pageParam";
    public static final String SN = "sn";
    public static final String GENDER = "gender";
    public static final String SCHOOLSN = "schoolSn";
    public static final String STUDENTID = "studentId";
    public static final String BIRTHDAY = "birthday";
    public static final String GRADE_ORDER_ID = "gradeOrderId";

    public ModelEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        ModelEntity e = (ModelEntity) o;
        return this.getId() == (e.getId());
    }

    public ModelEntity(HashMap<Object, Object> m) {
        super(m);
    }

    public ModelEntity(Map<Object, Object> m) {
        super.putAll(m);
    }

    public ModelEntity(ModelEntity me) {
        super.putAll(me);
    }

    public void set(Object key, Object val) {
        this.put(key, val);
    }

    public String getString(String key) {
        Object o = this.get(key);
        return o == null ? null : o.toString();
    }

    @SuppressWarnings("unchecked")
    public Map getMap(String key) {
        String o = this.getString(key);
        if (o == null)
            return null;
        return UtilCollection.toMap(o, " ", "=");
    }

    public int getInt(String key) {
        Object o = this.get(key);
        return o == null ? -1 : Integer.valueOf(o.toString()).intValue();
    }

    public String getCreatedBy() {
        Object o = this.get(CREATED_BY);
        return o == null ? null : o.toString();
    }

    public void setCreatedBy(String createdBy) {
        this.put(CREATED_BY, createdBy);
    }

    public Timestamp getCreationTime() {
        Object o = this.get(CREATION_TIME);
        return o == null ? null : (Timestamp) o;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.put(CREATION_TIME, creationTime);
    }

    public String getLastModifiedBy() {
        Object o = this.get(LAST_MODIFIED_BY);
        return o == null ? null : o.toString();
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.put(LAST_MODIFIED_BY, lastModifiedBy);
    }

    public Timestamp getLastModifiedTime() {
        Object o = this.get(LAST_MODIFIED_TIME);
        return o == null ? null : (Timestamp) o;
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.put(LAST_MODIFIED_TIME, lastModifiedTime);
    }

    public String getDescription() {
        return this.getString(DESCRIPTION);
    }

    public String getName() {
        return this.getString(NAME);
    }

    public String getTitle() {
        return this.getString(TITLE);
    }

    public String getId() {
        return this.getString(ID);
    }

    public void setVersion(int version) {
        this.put(VERSION, version);
    }

    public int getVersion() {
        return Integer.valueOf(getString(VERSION)).intValue();
    }

    public void setTitle(String title) {
        this.set(TITLE, title);
    }

    public void setAttrs(String attrs) {
        this.set(ATTRS, attrs);
    }

}
