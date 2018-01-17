package com.njpes.www.service.consultcenter;

import java.util.List;

import com.njpes.www.entity.consultcenter.Introduce;
import com.njpes.www.entity.consultcenter.IntroducePage;

public interface IntroduceServiceI {

    public List<Introduce> selectAllIntroduces();

    public int updateIntroduce(Introduce introduce);

    public int saveIntroduce(Introduce introduce);

    public int delIntroduce(Introduce org);

    public Introduce selectByPrimaryKey(long id);

    public String saveIntroducePage(IntroducePage introducePage);

    public String deleteIntroducePage(Introduce introduce);

}
