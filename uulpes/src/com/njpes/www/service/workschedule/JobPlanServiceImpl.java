package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.workschedule.JobPlanMapper;
import com.njpes.www.dao.workschedule.JobPlanShareMapper;
import com.njpes.www.entity.workschedule.JobPlan;
import com.njpes.www.entity.workschedule.JobPlanShare;
import com.njpes.www.entity.workschedule.JobPlanWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("jobPlanService")
public class JobPlanServiceImpl implements JobPlanServiceI {

    @Autowired
    JobPlanMapper jobPlanMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private JobPlanShareMapper jobPlanShareMapper;

    @Autowired
    private JobAttachmentMappnigServiceI jobAttachmentMappnigService;

    @Override
    public List<JobPlan> findByPage(JobPlanWithBLOBs params, PageParameter page) {
        return jobPlanMapper.findByParamsPage(params, page);
    }

    @Override
    public int insert(JobPlanWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobPlanMapper.insertSelective(record);
            long maxId = record.getId();
            jobAttachmentMappnigService.insert(maxId, resourceIdentity, fileuuids);
            long[] org_ids = record.getOrg_ids();
            if (org_ids != null) {
                for (long org : org_ids) {
                    JobPlanShare s = new JobPlanShare();
                    s.setPlanId(maxId);
                    s.setOrgId(org);
                    s.setSendOrgId(record.getDep());
                    jobPlanShareMapper.insert(s);
                }
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public JobPlanWithBLOBs findById(JobPlanWithBLOBs record) {
        return findById(record, 0);
    }

    @Override
    public JobPlanWithBLOBs findById(JobPlanWithBLOBs record, long sendOrgid) {
        JobPlanWithBLOBs entity = jobPlanMapper.selectBaseInfo(record.getId());
        List<JobPlanShare> list = jobPlanShareMapper.selectShareInfoBySendOrgid(record.getId(), sendOrgid);
        if (list != null) {
            entity.setJobPlanShareList(list);
            long[] l = new long[list.size()];
            int i = 0;
            for (JobPlanShare s : list) {
                l[i] = s.getOrgId();
                i++;
            }
            entity.setOrg_ids(l);
        }
        return entity;
    }

    @Override
    public int delete(JobPlanWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobPlanShareMapper.deleteByPlanId(record.getId(), 0);
            jobAttachmentMappnigService.delete(record.getId(), resourceIdentity);
            jobPlanMapper.deleteByPrimaryKey(record.getId());
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int update(JobPlanWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobPlanMapper.updateByPrimaryKeySelective(record);
            jobPlanShareMapper.deleteByPlanId(record.getId(), record.getDep());
            jobAttachmentMappnigService.insert(record.getId(), resourceIdentity, fileuuids);
            long[] org_ids = record.getOrg_ids();
            if (org_ids != null) {
                for (long org : org_ids) {
                    JobPlanShare s = new JobPlanShare();
                    s.setPlanId(record.getId());
                    s.setOrgId(org);
                    s.setSendOrgId(record.getDep());
                    jobPlanShareMapper.insert(s);
                }
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int justUpdateState(JobPlanWithBLOBs record) {
        return jobPlanMapper.updateStateByPrimaryKey(record);
    }

    @Override
    public int updateOrSaveShareInfo(long noticeId, long[] shareOrgids, long sendOrgid) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        jobPlanShareMapper.deleteByPlanId(noticeId, sendOrgid);
        try {
            for (long org : shareOrgids) {
                JobPlanShare s = new JobPlanShare();
                s.setPlanId(noticeId);
                s.setOrgId(org);
                s.setSendOrgId(sendOrgid);
                jobPlanShareMapper.insert(s);
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return shareOrgids.length;

    }

    @Override
    public int delete(long record) {
        return jobPlanMapper.deleteByPrimaryKey(record);
    }

}
