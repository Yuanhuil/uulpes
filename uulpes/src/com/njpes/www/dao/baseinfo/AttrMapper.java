package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.attr.AttrCategory;
import com.njpes.www.entity.baseinfo.attr.AttrDefine;

public interface AttrMapper {
    public List<AttrCategory> getAllAttrCategory();

    public List<AttrDefine> getAllAttrDefine();

    public int deleteAttr(AttrDefine attrDefine);

    public int updateAttrLabel(AttrDefine attrDefine);

    public int updateAttrOption(AttrDefine attrDefine);

    public int insertAttr(AttrDefine attrDefine);

    public AttrCategory getAttrCateByTitle(@Param("title") String title);

    public List<AttrDefine> selectByParam(@Param("qparam") AttrDefine qparam);

    public List<AttrDefine> selectByUserParam(@Param("qparam") AttrDefine qparam);
}
