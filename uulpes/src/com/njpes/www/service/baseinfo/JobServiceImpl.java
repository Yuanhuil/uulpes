package com.njpes.www.service.baseinfo;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.njpes.www.dao.baseinfo.JobMapper;
import com.njpes.www.entity.baseinfo.Job;

@Service("jobService")
public class JobServiceImpl implements JobServiceI {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public Set<Long> findAncestorIds(Iterable<Long> currentIds) {
        Set<Long> parents = Sets.newHashSet();
        for (Long currentId : currentIds) {
            parents.addAll(findAncestorIds(currentId));
        }
        return parents;
    }

    @Override
    public Set<Long> findAncestorIds(Long currentId) {
        Set<Long> ids = Sets.newHashSet();
        Job org = jobMapper.selectByPrimaryKey(currentId);
        if (org == null) {
            return ids;
        }
        for (String idStr : StringUtils.split(org.getParentIds(), "/")) {
            if (!StringUtils.isEmpty(idStr)) {
                ids.add(Long.valueOf(idStr));
            }
        }
        return ids;
    }
}
