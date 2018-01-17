package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.Parent;

public interface ParentServiceI {
    int deleteByPrimaryKey(Long id);

    int insert(Parent record);

    int insertSelective(Parent record);

    Parent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Parent record);

    int updateByPrimaryKeyWithBLOBs(Parent record);

    int updateByPrimaryKey(Parent record);

    AccountDetail selectByAccountId(int accountId);

    void updateByAccountId(AccountDetail accountDetail);

    public Parent getParentByStudentAccountId(long studentAccountId);

    public Parent getParentByAccountId(Long id);
}
