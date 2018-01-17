package com.njpes.www.service.consultcenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.CoachAttachmentMapper;
import com.njpes.www.entity.consultcenter.CoachAttachment;

@Service("coachAttachmentService")
public class CoachAttachmentServiceImpl implements CoachAttachmentServiceI {

    @Autowired
    private CoachAttachmentMapper CoachAttachmentMapper;

    @Override
    public int updateCoachAttachment(CoachAttachment coachAttachment) {
        // TODO Auto-generated method stub
        return this.CoachAttachmentMapper.updateByPrimaryKey(coachAttachment);
    }

    @Override
    public int saveCoachAttachment(CoachAttachment coachAttachment) {
        // TODO Auto-generated method stub
        return this.CoachAttachmentMapper.insert(coachAttachment);
    }

    @Override
    public int delCoachAttachment(CoachAttachment attachment) {
        // TODO Auto-generated method stub
        return this.CoachAttachmentMapper.deleteByPrimaryKey(attachment.getId());
    }

    @Override
    public List<CoachAttachment> getByFid(long fid) {
        // TODO Auto-generated method stub
        return this.CoachAttachmentMapper.getByFid(fid);
    }

    @Override
    public CoachAttachment selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return this.CoachAttachmentMapper.selectByPrimaryKey(id);
    }

}
