package com.njpes.www.service.baseinfo.organization;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.GraduatingClassMapper;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;

@Service("schoolClassService")
public class SchoolClassServiceImpl implements SchoolClassServiceI {

    @Autowired
    private ClassSchoolMapper classSchoolMapper;
    @Autowired
    private GraduatingClassMapper graduatingClassMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    public int insert(ClassSchool e) {
        return classSchoolMapper.insertSelective(e);
    }

    @Override
    public ClassSchool findById(long id) {
        return classSchoolMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(ClassSchool e) {
        return classSchoolMapper.updateByPrimaryKeySelective(e);
    }

    @Override
    public int del(ClassSchool e) {
        return classSchoolMapper.deleteByPrimaryKey(e.getId());
    }

    @Override
    public int upgrade(long orgid, int xxxz, int bybgradeid, String bxlxm) throws Exception {
        Date flozen_date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String flozenDate = sdf.format(flozen_date);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            int cn = classSchoolMapper.flozen(orgid, bybgradeid, flozenDate);
            cn = graduatingClassMapper.insertgraduate(orgid, bybgradeid);
            cn = classSchoolMapper.upgrade(orgid, xxxz, bybgradeid, bxlxm);
            txManager.commit(status); // 提交事务
            return cn;
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
            e.printStackTrace();
        }
        return -1;

    }

}
