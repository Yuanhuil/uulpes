package com.njpes.www.dao.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface FieldMapper {

    List<Map> selectAllDic(@Param("field") String field, @Param("table") String table);

    /**
     * 带where条件查询
     * 
     * @author zzc
     */
    List<Map> selectAllDicWhere(@Param("field") String field, @Param("table") String table,
            @Param("where") String where);

    List<Map> selectCountWhere(@Param("field") String field, @Param("table") String table, @Param("where") String where,
            @Param("groupby") String groupby);

}