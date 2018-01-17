package com.njpes.www.dao.scaletoollib;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.scaletoollib.ExamTempAnswer;

public interface ExamTempAnswerMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table exam_answer_0
     *
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    int deleteByPrimaryKey(Map param);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table exam_answer_0
     *
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    int insert(ExamTempAnswer record);

    int insertBatch(Map param);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table exam_answer_0
     *
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    int insertSelective(ExamTempAnswer record);

    List<ExamTempAnswer> selectByPrimaryKey(Map param);

    List<ExamTempAnswer> selectByResultId(@Param("table") String table, @Param("id") Long id,
            @Param("scaleid") String scaleid);

    // int updateByPrimaryKeySelective(ExamTempAnswer record);

    // int updateByPrimaryKey(ExamTempAnswer record);
}