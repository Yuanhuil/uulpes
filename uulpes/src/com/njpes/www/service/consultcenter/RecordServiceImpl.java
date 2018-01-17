package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.njpes.www.dao.consultcenter.RecordMapper;
import com.njpes.www.entity.consultcenter.Record;
import com.njpes.www.utils.PageParameter;

@Service("recordService")
public class RecordServiceImpl implements RecordServiceI {
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    public int updateRecord(Record record) {
        // TODO Auto-generated method stub
        return recordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int saveRecord(Record record) {
        // TODO Auto-generated method stub
        return recordMapper.insert(record);
    }

    @Override
    public int delRecord(Record record) {
        // TODO Auto-generated method stub
        return recordMapper.deleteByPrimaryKey(record.getId());
    }

    @Override
    public Record selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return recordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Record> selectListByRecord(Record record, PageParameter page, Date beginDate, Date endDate) {

        return recordMapper.selectEntityByPage(record, page, beginDate, endDate);

    }

}
