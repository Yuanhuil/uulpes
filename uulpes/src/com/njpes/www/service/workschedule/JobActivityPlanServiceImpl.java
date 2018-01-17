package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.workschedule.JobActivityPlanMapper;
import com.njpes.www.entity.workschedule.JobActivityPlanWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("jobActivityPlanService")
public class JobActivityPlanServiceImpl implements JobActivityPlanServiceI {

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private JobActivityPlanMapper jobActivityPlanMapper;

    @Autowired
    private JobAttachmentMappnigServiceI jobAttachmentMappnigService;

    @Override
    public List<JobActivityPlanWithBLOBs> findByPage(JobActivityPlanWithBLOBs params, PageParameter page) {
        return jobActivityPlanMapper.findByParamsPage(params, page);
    }

    @Override
    public int insert(JobActivityPlanWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobActivityPlanMapper.insertSelective(record);
            long maxId = record.getId();
            jobAttachmentMappnigService.insert(maxId, resourceIdentity, fileuuids);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public JobActivityPlanWithBLOBs findById(JobActivityPlanWithBLOBs record) {
        return jobActivityPlanMapper.selectByPrimaryKey(record.getId());
    }

    @Override
    public int delete(JobActivityPlanWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobActivityPlanMapper.deleteByPrimaryKey(record.getId());
            jobAttachmentMappnigService.delete(record.getId(), resourceIdentity);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int update(JobActivityPlanWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobActivityPlanMapper.updateByPrimaryKeySelective(record);
            jobAttachmentMappnigService.insert(record.getId(), resourceIdentity, fileuuids);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int justUpdateState(JobActivityPlanWithBLOBs record) {
        return jobActivityPlanMapper.updateStateByPrimaryKey(record);
    }

    @Override
    public List<String> getSchoolYearsBySchool(long orgid) {
        return jobActivityPlanMapper.getSchoolYearsBySchool(orgid);
    }

    @Override
    public int delete(Long id) {
        return jobActivityPlanMapper.deleteByPrimaryKey(id);
    }

}
