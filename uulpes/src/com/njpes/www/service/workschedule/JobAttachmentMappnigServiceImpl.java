package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.workschedule.JobAttachmentMappingMapper;
import com.njpes.www.entity.workschedule.JobAttachmentMapping;

@Service("jobAttachmentMappnigService")
public class JobAttachmentMappnigServiceImpl implements JobAttachmentMappnigServiceI {

    @Autowired
    private JobAttachmentMappingMapper jobAttachmentMappingMapper;

    @Autowired
    private JobAttachmentServiceI jobAttachmentService;

    @Override
    public int insert(JobAttachmentMapping record) {
        return jobAttachmentMappingMapper.insertSelective(record);
    }

    @Override
    public int delete(Long id) {
        return jobAttachmentMappingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(Long fid, String resource) {
        List<JobAttachmentMapping> fileids = jobAttachmentMappingMapper.selectFileIdsByFidResource(fid, resource);
        for (JobAttachmentMapping l : fileids) {
            jobAttachmentService.delrecord(l.getJobfileid());
        }
        return jobAttachmentMappingMapper.deleteFileByFidResource(fid, 0L, resource);
    }

    @Override
    public int delete(Long fid, Long fileid, String resource) {
        jobAttachmentService.delrecord(fileid);
        return jobAttachmentMappingMapper.deleteFileByFidResource(fid, fileid, resource);
    }

    @Override
    public int insert(Long entityid, String resource, String[] fileuuids) {
        if (fileuuids == null || fileuuids.length == 0)
            return 0;
        for (String s : fileuuids) {
            JobAttachmentMapping e = new JobAttachmentMapping();
            e.setFid(entityid);
            e.setResource(resource);
            e.setJobfileid(jobAttachmentService.selectByUUid(s).getId());
            insert(e);
        }
        return fileuuids.length;
    }

    @Override
    public List<JobAttachmentMapping> selectFileIdsByFidResource(Long fid, String resource) {
        return jobAttachmentMappingMapper.selectFileIdsByFidResource(fid, resource);
    }
}
