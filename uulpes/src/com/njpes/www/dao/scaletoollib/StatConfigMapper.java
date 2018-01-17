package com.njpes.www.dao.scaletoollib;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.scaletoollib.StatConfig;
import com.njpes.www.entity.scaletoollib.StatConfigWithBLOBs;

public interface StatConfigMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(StatConfigWithBLOBs record);

    int insertSelective(StatConfigWithBLOBs record);

    StatConfigWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StatConfigWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(StatConfigWithBLOBs record);

    int updateByPrimaryKey(StatConfig record);

    /**
     * 根据统计方法获得参数
     *
     * @author s
     * @param statType
     * @return
     */
    StatConfigWithBLOBs selectByType(int statType);

    /**
     * 查T分布临界值表，获得0.05或0.01的临界值
     *
     * @param t
     *            可信度，取值0.05或0.01
     * @param df自由度，
     *            统计人数减1
     * @return
     */
    Float getTvalue(@Param("t") String t, @Param("df") Integer df);

    /**
     * 根据自由度查询正态分布表查询F值
     *
     * @param t
     * @return
     */
    Float getFvalue(int rOne,int rTwo);
    /**
     * 根据T值查询正态分布表查询P值
     *
     * @param t
     * @return
     */
    Float getPvalue(@Param("t") Float t);
}