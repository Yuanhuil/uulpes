package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.Parent;

public interface ParentMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByStudentAccountid(Long id);

    int insert(Parent record);

    int insertSelective(Parent record);

    Parent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Parent record);

    int updateByPrimaryKeyWithBLOBs(Parent record);

    int updateByPrimaryKey(Parent record);

    AccountDetail selectByAccountId(int accountId);

    void updateByAccountId(AccountDetail accountDetail);

    Parent getParentByStudentAccountId(@Param("studentaccountid") long studentaccountid);

    Long insertBatch(List<Parent> students);

    // 史斌增加批量更新
    int updateBatch(List<Parent> parents);
}