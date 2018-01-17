package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.workschedule.JobCongressMapper;
import com.njpes.www.entity.workschedule.JobCongressWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("jobCongressService")
public class JobCongressServiceImpl implements JobCongressServiceI {

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private JobAttachmentMappnigServiceI jobAttachmentMappnigService;
    @Autowired
    private JobCongressMapper jobCongressMapper;

    @Override
    public List<JobCongressWithBLOBs> findByPage(JobCongressWithBLOBs params, PageParameter page) {
        return jobCongressMapper.findByPage(params, page);
    }

    @Override
    public int insert(JobCongressWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobCongressMapper.insertSelective(record);
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
    public JobCongressWithBLOBs findById(JobCongressWithBLOBs record) {
        return jobCongressMapper.selectByPrimaryKey(record.getId());
    }

    @Override
    public int delete(JobCongressWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobCongressMapper.deleteByPrimaryKey(record.getId());
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
    public int update(JobCongressWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobCongressMapper.updateByPrimaryKeySelective(record);
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
    public int delete(long record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobCongressMapper.deleteByPrimaryKey(record);
            jobAttachmentMappnigService.delete(record, resourceIdentity);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

}
