package com.njpes.www.service.util;

import java.util.List;
import java.util.Map;

import com.njpes.www.entity.baseinfo.FieldConfig;

/**
 * @author zhangchao
 */
public interface FieldServiceI {

    /**
     * 根据要展示的数据集合和要变成中文的字段配置获取要变成中文的字段组成的map 注意如果FieldConfig的fieldtype
     * 与key类型不匹配要指定类型目前主要针对char类型
     * 
     * @param list
     *            查询结果集合
     * @param NFTW
     *            要查询的字段配置集合
     * @return 字段名 该字段的key value 组成的map
     */
    Map<String, Map> getFieldName(List list, List<FieldConfig> NFTW);

    List<Map> selectAllDic(String field, String table);

    List<Map> selectAllDicWhere(String field, String table, String where);

}
