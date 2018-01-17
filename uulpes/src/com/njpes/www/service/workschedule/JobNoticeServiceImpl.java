package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.workschedule.JobNoticeMapper;
import com.njpes.www.dao.workschedule.JobNoticeShareMapper;
import com.njpes.www.entity.workschedule.JobNoticeShare;
import com.njpes.www.entity.workschedule.JobNoticeWebQueryParam;
import com.njpes.www.entity.workschedule.JobNoticeWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("jobNoticeService")
public class JobNoticeServiceImpl implements JobNoticeServiceI {
    @Autowired
    JobNoticeMapper jobNoticeMapper;

    @Autowired
    JobNoticeShareMapper jobNoticeShareMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private JobAttachmentMappnigServiceI jobAttachmentMappnigService;

    @Override
    public List<JobNoticeWithBLOBs> findNoticeByPage(JobNoticeWebQueryParam noticeParams, PageParameter page) {
        return jobNoticeMapper.findByParamsPage(noticeParams, page);
    }

    @Override
    public List<JobNoticeWithBLOBs> findAllNoticeByPage(JobNoticeWebQueryParam noticeParams, PageParameter page) {
        return jobNoticeMapper.findAllPage(noticeParams, page);
    }

    @Override
    public int insert(JobNoticeWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobNoticeMapper.insertSelective(record);
            long maxId = record.getId();
            jobAttachmentMappnigService.insert(maxId, resourceIdentity, fileuuids);
            long[] org_ids = record.getOrg_ids();
            if (org_ids != null) {
                for (long org : org_ids) {
                    JobNoticeShare s = new JobNoticeShare();
                    s.setNoticeId(maxId);
                    s.setOrgId(org);
                    s.setSendOrgId(record.getDep());
                    jobNoticeShareMapper.insert(s);
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
    public JobNoticeWithBLOBs findById(JobNoticeWithBLOBs record) {
        return findById(record, 0);
    }

    @Override
    public int delete(JobNoticeWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobNoticeShareMapper.deleteByNoticeId(record.getId(), 0);
            jobAttachmentMappnigService.delete(record.getId(), resourceIdentity);
            jobNoticeMapper.deleteByPrimaryKey(record.getId());
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;

    }

    @Override
    public int update(JobNoticeWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobNoticeMapper.updateByPrimaryKeySelective(record);
            jobAttachmentMappnigService.insert(record.getId(), resourceIdentity, fileuuids);
            jobNoticeShareMapper.deleteByNoticeId(record.getId(), record.getDep());
            long[] org_ids = record.getOrg_ids();
            if (org_ids != null) {
                for (long org : org_ids) {
                    JobNoticeShare s = new JobNoticeShare();
                    s.setNoticeId(record.getId());
                    s.setOrgId(org);
                    s.setSendOrgId(record.getDep());
                    jobNoticeShareMapper.insert(s);
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
    public int justUpdateState(JobNoticeWithBLOBs record) {
        return jobNoticeMapper.updateStateByPrimaryKey(record);
    }

    @Override
    public JobNoticeWithBLOBs findById(JobNoticeWithBLOBs record, long sendOrgid) {
        JobNoticeWithBLOBs entity = jobNoticeMapper.selectBaseInfo(record.getId());
        List<JobNoticeShare> list = jobNoticeShareMapper.selectShareInfoBySendOrgid(record.getId(), sendOrgid);
        if (list != null) {
            entity.setJobNoticeShareList(list);
            long[] l = new long[list.size()];
            int i = 0;
            for (JobNoticeShare s : list) {
                l[i] = s.getOrgId();
                i++;
            }
            entity.setOrg_ids(l);
        }
        return entity;
    }

    @Override
    public int updateOrSaveShareInfo(long noticeId, long[] shareOrgids, long sendOrgid) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        jobNoticeShareMapper.deleteByNoticeId(noticeId, sendOrgid);
        try {
            for (long org : shareOrgids) {
                JobNoticeShare s = new JobNoticeShare();
                s.setNoticeId(noticeId);
                s.setOrgId(org);
                s.setSendOrgId(sendOrgid);
                jobNoticeShareMapper.insert(s);
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
    public JobNoticeWithBLOBs findById(long id) {
        return jobNoticeMapper.selectBaseInfo(id);
    }

    @Override
    public int del(long id) {
        return jobNoticeMapper.deleteByPrimaryKey(id);
    }

}
