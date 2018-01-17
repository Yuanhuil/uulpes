package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.workschedule.JobActivitySummaryMapper;
import com.njpes.www.entity.workschedule.JobActivitySummaryWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("jobActivitySummaryService")
public class JobActivitySummaryServiceImpl implements JobActivitySummaryServiceI {

    @Autowired
    private JobActivitySummaryMapper jobActivitySummaryMapper;

    @Override
    public List<JobActivitySummaryWithBLOBs> findByPage(JobActivitySummaryWithBLOBs params, PageParameter page) {
        return jobActivitySummaryMapper.findByPage(params, page);
    }

    @Override
    public int insert(JobActivitySummaryWithBLOBs record) {
        return jobActivitySummaryMapper.insertSelective(record);
    }

    @Override
    public JobActivitySummaryWithBLOBs findById(JobActivitySummaryWithBLOBs record) {
        return jobActivitySummaryMapper.selectByPrimaryKey(record.getId());
    }

    @Override
    public int delete(JobActivitySummaryWithBLOBs record) {
        return jobActivitySummaryMapper.deleteByPrimaryKey(record.getId());
    }

    @Override
    public int update(JobActivitySummaryWithBLOBs record) {
        return jobActivitySummaryMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public List<String> getSchoolYearsBySchool(long depid) {
        return jobActivitySummaryMapper.getSchoolYearsBySchool(depid);
    }

    @Override
    public int delete(long record) {
        // TODO Auto-generated method stub
        return jobActivitySummaryMapper.deleteByPrimaryKey(record);
    }

}
