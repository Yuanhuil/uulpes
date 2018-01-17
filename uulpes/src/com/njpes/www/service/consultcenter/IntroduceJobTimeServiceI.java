package com.njpes.www.service.consultcenter;

import java.util.List;

import com.njpes.www.entity.consultcenter.IntroduceJobTime;

public interface IntroduceJobTimeServiceI {

    public int updateIntroduceJobTime(IntroduceJobTime IntroduceJobTime);

    public int saveIntroduceJobTime(IntroduceJobTime introduceJobTime);

    public int delIntroduceJobTime(IntroduceJobTime introduceJobTime);

    public List<IntroduceJobTime> getListByFid(long id);

    /**
     * @Description:
     * @param introduceJobTime
     * @return
     */
    public List<IntroduceJobTime> getListByEntity(IntroduceJobTime introduceJobTime);

}
