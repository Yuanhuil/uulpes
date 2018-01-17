package com.njpes.www.service.baseinfo;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.AttrMapper;
import com.njpes.www.entity.baseinfo.attr.AttrDefine;

import heracles.util.UtilCollection;

@Service("AttrDefineManagerImpl")
public class AttrDefineManagerImpl {
    @Autowired
    AttrCategoryServiceImpl attrCategoryServiceImpl;
    private List<AttrDefine> allAttrDefs;
    private AttrMapper attrMapper;
    private static final char SEPT = '-';

    @Autowired
    public AttrDefineManagerImpl(AttrMapper attrMapper) {
        this.attrMapper = attrMapper;
        allAttrDefs = attrMapper.getAllAttrDefine();
    }

    public Collection<AttrDefine> getForOrg(final int orgId) {
        return CollectionUtils.select(allAttrDefs, new Predicate() {
            public boolean evaluate(Object object) {
                AttrDefine attrDefine = (AttrDefine) object;
                return (attrDefine.getOrgId() == 0 || attrDefine.getOrgId() == orgId);
            }
        });
    }

    public Collection<AttrDefine> getForSys() {
        return CollectionUtils.select(allAttrDefs, new Predicate() {
            public boolean evaluate(Object object) {
                AttrDefine attrDefine = (AttrDefine) object;
                return (attrDefine.getOrgId() == 0);
            }
        });
    }

    public Collection<AttrDefine> getForCate(final int ceteId) {
        return CollectionUtils.select(allAttrDefs, new Predicate() {
            public boolean evaluate(Object object) {
                AttrDefine attrDefine = (AttrDefine) object;
                return (attrDefine.getCateId() == ceteId);
            }
        });
    }

    public AttrDefine getForId(final int attrId) {
        return (AttrDefine) CollectionUtils.find(allAttrDefs, new Predicate() {
            public boolean evaluate(Object object) {
                AttrDefine attrDefine = (AttrDefine) object;
                return attrDefine.getObjectIdentifier() == attrId;
            }
        });
    }

    public Collection<AttrDefine> getForAnalysis(final int cateId) {
        return CollectionUtils.select(allAttrDefs, new Predicate() {
            public boolean evaluate(Object object) {
                AttrDefine attrDefine = (AttrDefine) object;
                return (attrDefine.getCateId() == cateId) && attrDefine.isAnalysisType();
            }
        });
    }

    public void updateAttrLabel(final AttrDefine attrDefine) {
        attrMapper.updateAttrLabel(attrDefine);
    }

    public void updateAttrOption(final AttrDefine attrDefine) {
        attrMapper.updateAttrOption(attrDefine);
    }

    public void insertAttr(AttrDefine attrDefine) {
        // long nextId = SequenceGenerator.nextID(SequenceConst.ATTR_DEFINES);
        // attrDefine.setObjectIdentifier(nextId);
        attrMapper.insertAttr(attrDefine);
        allAttrDefs.add(attrDefine);
    }

    public void batchInsertAttrs(Collection<AttrDefine> attrDefines) {
        if (CollectionUtils.isEmpty(attrDefines)) {
            return;
        }

    }

    public void deleteAttr(String attrId) {
        AttrDefine ad = null;
        for (AttrDefine attrDefine : allAttrDefs) {
            if (attrDefine.getId().equals(attrId)) {
                ad = attrDefine;
                break;
            }
        }
        if (ad == null)
            return;
        attrMapper.deleteAttr(ad);
        allAttrDefs.remove(ad);
    }

    public Map<String, String> getAttrDefinesForAnalysis(String attrIds) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        List<String> list = UtilCollection.toList(attrIds, SEPT);
        for (String s : list) {
            int cateId = NumberUtils.toInt(s);
            Collection<AttrDefine> collection = getForAnalysis(cateId);
            for (AttrDefine attrDefine : collection) {
                result.put(attrDefine.getId(), attrDefine.getLabel());
            }
        }
        return result;
    }

}
