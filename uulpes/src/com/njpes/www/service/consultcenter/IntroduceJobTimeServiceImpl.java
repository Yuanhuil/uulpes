package com.njpes.www.service.consultcenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.IntroduceJobTimeMapper;
import com.njpes.www.entity.consultcenter.IntroduceJobTime;

@Service("IntroduceJobTimeService")
public class IntroduceJobTimeServiceImpl implements IntroduceJobTimeServiceI {

    @Autowired
    private IntroduceJobTimeMapper introduceJobTimeMapper;

    @Override
    public int updateIntroduceJobTime(IntroduceJobTime IntroduceJobTime) {
        // TODO Auto-generated method stub
        return this.introduceJobTimeMapper.updateByPrimaryKey(IntroduceJobTime);
    }

    @Override
    public int saveIntroduceJobTime(IntroduceJobTime introduceJobTime) {
        // TODO Auto-generated method stub
        return this.introduceJobTimeMapper.insert(introduceJobTime);
    }

    @Override
    public int delIntroduceJobTime(IntroduceJobTime introduceJobTime) {
        // TODO Auto-generated method stub
        return this.introduceJobTimeMapper.deleteByPrimaryKey(introduceJobTime.getId());
    }

    @Override
    public List<IntroduceJobTime> getListByFid(long id) {
        // TODO Auto-generated method stub
        return this.introduceJobTimeMapper.getListByFid(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.IntroduceJobTimeServiceI#
     * getListByEntity(com.njpes.www.entity.consultcenter.IntroduceJobTime)
     */
    @Override
    public List<IntroduceJobTime> getListByEntity(IntroduceJobTime introduceJobTime) {
        // TODO Auto-generated method stub
        return this.introduceJobTimeMapper.getListByEntity(introduceJobTime);
    }

}
