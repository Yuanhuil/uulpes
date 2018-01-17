package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobAttachmentMapping;

public interface JobAttachmentMappnigServiceI {

    public int insert(JobAttachmentMapping record);

    public int delete(Long id);

    public int delete(Long fid, String resource);

    public int delete(Long fid, Long fileid, String resource);

    public int insert(Long entityid, String resource, String[] fileuuids);

    public List<JobAttachmentMapping> selectFileIdsByFidResource(Long fid, String resource);

}
