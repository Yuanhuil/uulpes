package com.njpes.www.entity.baseinfo.attr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

//import heracles.mvc.Field;
//import heracles.mvc.SelectOption;
import heracles.util.UtilCollection;

/**
 * 属性定义类
 * 
 * @author
 */
public class AttrDefine extends Field {
    public final static String ANALYSIS_TYPES[] = { FLD_TYPE_SELECT, FLD_TYPE_YESNO, FLD_TYPE_RANGE };
    private long objectIdentifier;
    private static final String PREFIX_ID = "";
    private Map<String, String> optionMap;
    private int roleFlag;
    private long orgId;
    private int cateId;

    public long getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(long objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    @Override
    public String getId() {
        if (objectIdentifier != 0) {
            return PREFIX_ID + objectIdentifier;
        } else {
            return super.getId();
        }
    }

    public AttrDefine() {
        super();
    }

    public int getRoleFlag() {
        return roleFlag;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    // public Organization getOrganization() {
    // return OrganizationImpl.getOrganization(getOrgId());
    // }
    public int getCateId() {
        return cateId;
    }

    public boolean isAnalysisType() {
        return ArrayUtils.contains(ANALYSIS_TYPES, getType());
    }

    /**
     * 种类映射到角色，所以定义了种类意味着定义了角色
     * 
     * @param cateId
     */
    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public void setRoleFlag(int roleFlag) {
        this.roleFlag = roleFlag;
    }

    @Override
    public void setOptDataSource(String dataSource) {
        optionMap = UtilCollection.lineConvertMap(dataSource);
    }

    @Override
    public String getOptDataSource() {
        if (MapUtils.isEmpty(optionMap)) {
            return StringUtils.EMPTY;
        }
        return UtilCollection.toString(optionMap, UtilCollection.SEMICOLON, UtilCollection.EQ);
    }

    @Override
    public List<SelectOption> getOptionList() {
        Map<String, String> map = getOptionMap();
        if (MapUtils.isEmpty(map)) {
            return Collections.emptyList();
        }
        List<SelectOption> optionList = null;
        optionList = new ArrayList<SelectOption>(optionMap.size());
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> ent = it.next();
            String value = ent.getKey();
            String title = ent.getValue();
            SelectOption opt = new SelectOption(value, title);
            optionList.add(opt);
        }
        return optionList;
    }

    synchronized public void setOption(String key, String val) {
        if (MapUtils.isEmpty(optionMap)) {
            optionMap = new LinkedHashMap<String, String>();
        }
        optionMap.put(key, val);
    }

    synchronized public void removeOption(String key) {
        if (MapUtils.isNotEmpty(optionMap)) {
            optionMap.remove(key);
        }
    }

    public Map<String, String> getOptionMap() {
        if (MapUtils.isEmpty(optionMap)) {
            if (getType().equals(FLD_TYPE_YESNO)) {
                optionMap = new LinkedHashMap<String, String>();
                optionMap.put("1", "是");
                optionMap.put("2", "否");
            } else {
                return Collections.emptyMap();
            }
        }
        return optionMap;
    }

}
