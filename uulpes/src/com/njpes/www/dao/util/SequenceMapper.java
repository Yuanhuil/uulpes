package com.njpes.www.dao.util;

import org.apache.ibatis.annotations.Param;

public interface SequenceMapper {

    /**
     * 把表的自增id起始值设置为固定值
     * 
     * @author 史斌
     */
    Integer updateSeqId(@Param("startValue") Long value, @Param("table") String table);
}