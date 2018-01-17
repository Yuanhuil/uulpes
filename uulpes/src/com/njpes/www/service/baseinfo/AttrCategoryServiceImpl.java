package com.njpes.www.service.baseinfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.AttrMapper;
import com.njpes.www.entity.baseinfo.attr.AttrCategory;

@Service("AttrCategoryServiceImpl")
public class AttrCategoryServiceImpl {
    private static final char SEPT = '-';
    static List<AttrCategory> list = null;

    @Autowired
    public AttrCategoryServiceImpl(AttrMapper attrMapper) {
        list = attrMapper.getAllAttrCategory();
    }

    public AttrCategory getAttrCategory(String attrId) {
        List<AttrCategory> attrCats = this.list;
        final long id = NumberUtils.toLong(attrId);
        Object o = CollectionUtils.find(attrCats, new Predicate() {
            public boolean evaluate(Object object) {
                AttrCategory category = (AttrCategory) object;
                return category.getId() == id;
            }
        });
        return (AttrCategory) o;
    }

    public Map<String, String> getAttrCategoriesForAnalysis(int orgType) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        int stuProps[];
        switch (orgType) {
        case 1:// 学校
            stuProps = Constants.STU_PROP_NUMS;
            int clsProps[] = Constants.CLS_PROP_NUMS;
            result.put(toString(stuProps), "学生背景");
            result.put(toString(clsProps), "班级背景");
            break;
        case 2:// 教委
            stuProps = Constants.STU_PROP_NUMS;
            result.put(toString(stuProps), "学生背景");
            break;

        default:
            break;
        }
        return result;
    }

    private static String toString(int props[]) {
        StringBuffer buffer = new StringBuffer();
        for (int p : props) {
            buffer.append(p);
            buffer.append(SEPT);
        }
        return buffer.toString();
    }
}
