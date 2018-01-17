package com.njpes.www.service.consultcenter;

import java.util.List;

import com.njpes.www.entity.consultcenter.CoachAttachment;

public interface CoachAttachmentServiceI {

    public int updateCoachAttachment(CoachAttachment coachAttachment);

    public int saveCoachAttachment(CoachAttachment coachAttachment);

    public int delCoachAttachment(CoachAttachment coachAttachment);

    public List<CoachAttachment> getByFid(long id);

    public CoachAttachment selectByPrimaryKey(long id);

}
