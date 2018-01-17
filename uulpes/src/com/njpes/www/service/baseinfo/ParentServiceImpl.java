package com.njpes.www.service.baseinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.ParentMapper;
import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.Parent;

@Service("parentService")
public class ParentServiceImpl implements ParentServiceI {

    @Autowired
    private ParentMapper parentMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(Parent record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(Parent record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Parent selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Parent record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Parent record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Parent record) {
        return this.parentMapper.updateByPrimaryKey(record);
    }

    @Override
    public AccountDetail selectByAccountId(int accountId) {
        // TODO Auto-generated method stub
        return this.parentMapper.selectByAccountId(accountId);
    }

    @Override
    public void updateByAccountId(AccountDetail accountDetail) {
        // TODO Auto-generated method stub
        this.parentMapper.updateByAccountId(accountDetail);
    }

    @Override
    public Parent getParentByStudentAccountId(long studentAccountId) {
        return parentMapper.getParentByStudentAccountId(studentAccountId);
    }

    @Override
    public Parent getParentByAccountId(Long id) {
        return parentMapper.getParentByStudentAccountId(id - 1);
    }

}
