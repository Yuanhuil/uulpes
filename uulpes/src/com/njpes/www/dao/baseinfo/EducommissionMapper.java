package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.organization.Educommission;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.utils.PageParameter;

public interface EducommissionMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    int deleteByOrgid(@Param("orgid") Long orgid);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    int insert(Educommission record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    int insertSelective(Educommission record);

    int insertBatch(List<Educommission> ecs);

    int updateBatch(List<Educommission> ecs);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    Educommission selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    int updateByPrimaryKeySelective(Educommission record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    int updateByPrimaryKeyWithBLOBs(Educommission record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table educommission
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    int updateByPrimaryKey(Educommission record);

    Educommission selectByOrgId(@Param("orgid") Long id);

    List<Educommission> selectSubECsInWebQueryByPage(@Param("org") Organization org, @Param("page") PageParameter page);

    List<Educommission> getEdusUnderOrgid(@Param("orgid") Long orgid);
}