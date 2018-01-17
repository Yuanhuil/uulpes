package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.District;

public interface DistrictMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table area
     * 
     * @mbggenerated Wed May 20 22:44:15 CST 2015
     */
    int deleteByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table area
     * 
     * @mbggenerated Wed May 20 22:44:15 CST 2015
     */

    District selectByCode(String code);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table area
     * 
     * @mbggenerated Wed May 20 22:44:15 CST 2015
     */

    public List<District> getAllProvince();

    public List<District> getCities(@Param("parentcode") String parentcode);

    public List<District> getCounties(@Param("parentcode") String parentcode);

    public List<District> getTowns(@Param("parentcode") String parentcode);

    public List<District> getSubCities(@Param("parentcode") String parentcode);

    public List<District> getSubCounties(@Param("parentcode") String parentcode);

    public List<District> getSubTowns(@Param("parentcode") String parentcode);
}
