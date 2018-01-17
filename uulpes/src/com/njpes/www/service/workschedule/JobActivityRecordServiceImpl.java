package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.workschedule.JobActivityRecordMapper;
import com.njpes.www.dao.workschedule.JobActivityRecordPsyactMapper;
import com.njpes.www.dao.workschedule.JobActivityRecordPsycourseMapper;
import com.njpes.www.dao.workschedule.JobActivityRecordPsyresearchMapper;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyactWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsycourseWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyresearchWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBs;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.utils.PageParameter;

@Service("jobActivityRecordService")
public class JobActivityRecordServiceImpl implements JobActivityRecordServiceI {
    @Autowired
    private JobActivityRecordMapper jobActivityRecordMapper;
    @Autowired
    private JobActivityRecordPsycourseMapper jobActivityRecordPsycourseMapper;
    @Autowired
    private JobActivityRecordPsyactMapper jobActivityRecordPsyactMapper;
    @Autowired
    private JobActivityRecordPsyresearchMapper jobActivityRecordPsyresearchMapper;

    @Autowired
    private JobAttachmentMappnigServiceI jobAttachmentMappnigService;

    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    public List<JobActivityRecordPsycourseWithBLOBs> findPsycourseByPage(JobActivityRecordPsycourseWithBLOBs params,
            PageParameter page) {
        return jobActivityRecordPsycourseMapper.findByPage(params, page);
    }

    @Override
    public List<JobActivityRecordPsyactWithBLOBs> findPsyactByPage(JobActivityRecordPsyactWithBLOBs params,
            PageParameter page) {
        return jobActivityRecordPsyactMapper.findByPage(params, page);
    }

    @Override
    public List<JobActivityRecordPsyresearchWithBLOBs> findPsyresearchByPage(
            JobActivityRecordPsyresearchWithBLOBs params, PageParameter page) {
        return jobActivityRecordPsyresearchMapper.findByPage(params, page);
    }

    @Override
    public int insertPsycourse(JobActivityRecordPsycourseWithBLOBs record) {
        String[] fileuuids = record.getFileuuids();
        jobActivityRecordPsycourseMapper.insertSelective(record);
        long maxId = record.getId();
        jobAttachmentMappnigService.insert(maxId, resourceIdentity_course, fileuuids);
        return 1;
    }

    @Override
    public int insertPsyact(JobActivityRecordPsyactWithBLOBs record) {
        String[] fileuuids = record.getFileuuids();
        jobActivityRecordPsyactMapper.insertSelective(record);
        long maxId = record.getId();
        jobAttachmentMappnigService.insert(maxId, resourceIdentity_act, fileuuids);
        return 1;
    }

    @Override
    public int insertPsyresearch(JobActivityRecordPsyresearchWithBLOBs record) {
        String[] fileuuids = record.getFileuuids();
        jobActivityRecordPsyresearchMapper.insertSelective(record);
        long maxId = record.getId();
        jobAttachmentMappnigService.insert(maxId, resourceIdentity_research, fileuuids);
        return 1;
    }

    @Override
    public JobActivityRecordPsycourseWithBLOBs findPsycourseById(JobActivityRecordPsycourseWithBLOBs record) {
        return jobActivityRecordPsycourseMapper.selectByPrimaryKey(record.getId());
    }

    @Override
    public JobActivityRecordPsyactWithBLOBs findPsyactById(JobActivityRecordPsyactWithBLOBs record) {
        return jobActivityRecordPsyactMapper.selectByPrimaryKey(record.getId());
    }

    @Override
    public JobActivityRecordPsyresearchWithBLOBs findPsyresearchById(JobActivityRecordPsyresearchWithBLOBs record) {
        return jobActivityRecordPsyresearchMapper.selectByPrimaryKey(record.getId());
    }

    @Override
    public int deletePsycourse(JobActivityRecordPsycourseWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobActivityRecordPsycourseMapper.deleteByPrimaryKey(record.getId());
            txManager.commit(status);
            jobAttachmentMappnigService.delete(record.getId(), resourceIdentity_course);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int deletePsyact(JobActivityRecordPsyactWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobActivityRecordPsyactMapper.deleteByPrimaryKey(record.getId());
            jobAttachmentMappnigService.delete(record.getId(), resourceIdentity_act);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int deletePsyresearch(JobActivityRecordPsyresearchWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jobActivityRecordPsyresearchMapper.deleteByPrimaryKey(record.getId());
            jobAttachmentMappnigService.delete(record.getId(), resourceIdentity_research);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int updatePsycourse(JobActivityRecordPsycourseWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobActivityRecordPsycourseMapper.updateByPrimaryKeySelective(record);
            jobAttachmentMappnigService.insert(record.getId(), resourceIdentity_course, fileuuids);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int updatePsyact(JobActivityRecordPsyactWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobActivityRecordPsyactMapper.updateByPrimaryKeySelective(record);
            jobAttachmentMappnigService.insert(record.getId(), resourceIdentity_act, fileuuids);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int updatePsyresearch(JobActivityRecordPsyresearchWithBLOBs record) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        String[] fileuuids = record.getFileuuids();
        try {
            jobActivityRecordPsyresearchMapper.updateByPrimaryKeySelective(record);
            jobAttachmentMappnigService.insert(record.getId(), resourceIdentity_research, fileuuids);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public List<JobActivityRecordWithBLOBs> findAllRecordInView(JobActivityRecordWithBLOBs params) {
        return jobActivityRecordMapper.selectAll(params);
    }

    @Override
    public JobActivityRecordWithBLOBs selectRecordInView(String id, String tablename) {
        return jobActivityRecordMapper.selectRecord(id, tablename);
    }

    @Override
    public List<JobActivityRecordWithBLOBs> findRecordsInViewByPage(JobActivityRecordWithBLOBs params,
            PageParameter page) {
        long orgId = params.getDep();
        Organization org = new Organization();
        org.setId(orgId);
        return jobActivityRecordMapper.selectBySubOrgidsPage(params, page, orgId);
        /*
         * HashMap<OrganizationType,List<Organization>> subOrgs =
         * organizationService.getSonSubOrgs(org); if(subOrgs != null &&
         * subOrgs.size() >0){ List<Long> suborgids = new ArrayList<Long>();
         * List<Organization> school_orgs =
         * subOrgs.get(OrganizationType.school); List<Organization> ec_orgs =
         * subOrgs.get(OrganizationType.ec); if(school_orgs !=null &&
         * school_orgs.size() >0){ for(Organization o : school_orgs){
         * suborgids.add(o.getId()); } } if(ec_orgs !=null && ec_orgs.size()
         * >0){ for(Organization o : ec_orgs){ suborgids.add(o.getId()); } }
         * 
         * 
         * }else{ return jobActivityRecordMapper.selectByPage(params, page); }
         */
    }

    @Override
    public List<JobActivityRecordWithBLOBs> findSchoolRecordsInViewByPage(JobActivityRecordWithBLOBs params,
            PageParameter page) {
        long orgId = params.getDep();
        Organization org = new Organization();
        org.setId(orgId);
        return jobActivityRecordMapper.selectByOrgidsPage(params, page, orgId);
    }

    @Override
    public int updateJobOverviewField(String tablename, long recordId, String fieldValue) {
        return jobActivityRecordMapper.updateField(tablename, "joboverview", recordId, fieldValue);
    }

    @Override
    public int updateVipEventField(String tablename, long recordId, String fieldValue) {
        return jobActivityRecordMapper.updateField(tablename, "vip_event", recordId, fieldValue);
    }

    @Override
    public int deletePsycourse(long record) {
        return jobActivityRecordPsycourseMapper.deleteByPrimaryKey(record);
    }

    @Override
    public int deletePsyact(long record) {
        return jobActivityRecordPsyactMapper.deleteByPrimaryKey(record);
    }

    @Override
    public int deletePsyresearch(long id) {
        return jobActivityRecordPsyresearchMapper.deleteByPrimaryKey(id);
    }

}
