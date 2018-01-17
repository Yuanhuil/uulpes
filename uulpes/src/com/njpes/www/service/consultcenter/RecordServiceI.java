package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.Record;
import com.njpes.www.utils.PageParameter;

public interface RecordServiceI {

    public int updateRecord(Record recordWithBLOBs);

    public int saveRecord(Record recordWithBLOBs);

    public int delRecord(Record org);

    public Record selectByPrimaryKey(long id);

    public List<Record> selectListByRecord(Record recordWithBLOBs, PageParameter page, Date beginDate, Date endDate);

}
