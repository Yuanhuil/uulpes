package com.njpes.www.dao.scaletoollib;

import java.util.List;

import com.njpes.www.entity.scaletoollib.Scale;

public interface ScaleMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale
     *
     * @mbggenerated Sat Mar 21 23:13:00 CST 2015
     */
    int insert(Scale record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale
     *
     * @mbggenerated Sat Mar 21 23:13:00 CST 2015
     */
    int insertSelective(Scale record);

	List<edutec.scale.model.Scale> getAllScale();

	String getScaleName(String scaleId);
}