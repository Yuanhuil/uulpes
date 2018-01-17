package com.njpes.www.service.util;

import java.util.List;
import java.util.Map;

import com.njpes.www.entity.util.Dictionary;

public interface DictionaryServiceI {

    Dictionary selectByPrimaryKey(Map para);

    List<Dictionary> selectAllDic(String table);

    /**
     * 根据where条件查询码表
     *
     * @author 赵忠诚
     */
    List<Dictionary> selectAllDicWhere(String table, String where);

    List<Dictionary> selectDicWhere(String tablename, List<String> fields, String where);

    Map selectAllDicMap(String table);

    List<Dictionary> selectAllType(String table);
}
