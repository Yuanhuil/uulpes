package com.njpes.www.dao.scaletoollib;

import com.njpes.www.entity.scaletoollib.Scalewarning;

public interface ScalewarningMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_warning
     *
     * @mbggenerated Sat Mar 21 23:15:25 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_warning
     *
     * @mbggenerated Sat Mar 21 23:15:25 CST 2015
     */
    int insert(Scalewarning record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_warning
     *
     * @mbggenerated Sat Mar 21 23:15:25 CST 2015
     */
    int insertSelective(Scalewarning record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_warning
     *
     * @mbggenerated Sat Mar 21 23:15:25 CST 2015
     */
    Scalewarning selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_warning
     *
     * @mbggenerated Sat Mar 21 23:15:25 CST 2015
     */
    int updateByPrimaryKeySelective(Scalewarning record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_warning
     *
     * @mbggenerated Sat Mar 21 23:15:25 CST 2015
     */
    int updateByPrimaryKey(Scalewarning record);
}