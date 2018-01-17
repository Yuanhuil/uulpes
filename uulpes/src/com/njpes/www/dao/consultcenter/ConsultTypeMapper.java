package com.njpes.www.dao.consultcenter;

import java.util.List;

import com.njpes.www.entity.consultcenter.ConsultType;

public interface ConsultTypeMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_consult_type
     * 
     * @mbggenerated Sat May 02 16:14:19 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_consult_type
     * 
     * @mbggenerated Sat May 02 16:14:19 CST 2015
     */
    int insert(ConsultType record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_consult_type
     * 
     * @mbggenerated Sat May 02 16:14:19 CST 2015
     */
    int insertSelective(ConsultType record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_consult_type
     * 
     * @mbggenerated Sat May 02 16:14:19 CST 2015
     */
    ConsultType selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_consult_type
     * 
     * @mbggenerated Sat May 02 16:14:19 CST 2015
     */
    int updateByPrimaryKeySelective(ConsultType record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_consult_type
     * 
     * @mbggenerated Sat May 02 16:14:19 CST 2015
     */
    int updateByPrimaryKey(ConsultType record);

    List<ConsultType> getListByFid(long id);

    int deleteByFid(long fid);

    /**
     * @Description:
     * @param id
     * @return
     */
    List<ConsultType> getOpenListByFid(long id);

}