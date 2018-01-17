package com.njpes.www.service.consultcenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.consultcenter.CoachAttachmentMapper;
import com.njpes.www.dao.consultcenter.ConsultTypeMapper;
import com.njpes.www.dao.consultcenter.IntroduceJobTimeMapper;
import com.njpes.www.dao.consultcenter.IntroduceMapper;
import com.njpes.www.dao.util.CommonAttachmentMapper;
import com.njpes.www.entity.consultcenter.CoachAttachment;
import com.njpes.www.entity.consultcenter.Introduce;
import com.njpes.www.entity.consultcenter.IntroduceJobTime;
import com.njpes.www.entity.consultcenter.IntroducePage;
import com.njpes.www.entity.util.CommonAttachment;

@Service("introduceService")
public class IntroduceServiceImpl implements IntroduceServiceI {

    @Autowired
    private IntroduceMapper introduceMapper;
    @Autowired
    private CoachAttachmentMapper coachAttachmentMapper;
    @Autowired
    private CommonAttachmentMapper commonAttachmentMapper;
    @Autowired
    private ConsultTypeMapper consultTypeMapper;
    @Autowired
    private IntroduceJobTimeMapper introduceJobTimeMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    public List<Introduce> selectAllIntroduces() {
        return this.introduceMapper.selectAllIntroduce();
    }

    @Override
    public int updateIntroduce(Introduce introduce) {
        // TODO Auto-generated method stub
        return this.introduceMapper.updateByPrimaryKey(introduce);
    }

    @Override
    public int saveIntroduce(Introduce introduce) {
        // TODO Auto-generated method stub
        return this.introduceMapper.insert(introduce);
    }

    @Override
    public int delIntroduce(Introduce introduce) {
        // TODO Auto-generated method stub
        return this.introduceMapper.deleteByPrimaryKey(introduce.getId());
    }

    @Override
    public Introduce selectByPrimaryKey(long id) {
        return this.introduceMapper.selectByPrimaryKey(id);
    }

    @Override
    public String saveIntroducePage(IntroducePage introducePage) {
        String str = "";
        Introduce introduce = introducePage.getIntroduce();
        List<IntroduceJobTime> introduceJobTimes = introducePage.getIntroduceJobTimes();
        // List<ConsultType> consultTypes = introducePage.getConsultTypes();
        List<CoachAttachment> newAttachments = introducePage.getCoachAttachments();

        List<CoachAttachment> oldAttachments = null;

        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            Introduce oldIntroduce = introduceMapper.selectByPrimaryKey(introduce.getId());
            if (oldIntroduce != null) {
                this.introduceMapper.updateByPrimaryKeyWithBLOBs(introduce);
                for (IntroduceJobTime introduceJobTime : introduceJobTimes) {
                    introduceJobTime.setFid(introduce.getId());
                    this.introduceJobTimeMapper.updateByPrimaryKey(introduceJobTime);
                }
                // for (ConsultType consultType : consultTypes) {
                // this.consultTypeMapper.updateByPrimaryKey(consultType);
                // }
                oldAttachments = coachAttachmentMapper.getByFid(introduce.getId());
                Map<Long, CoachAttachment> oldCoachAttachmentMap = new HashMap<Long, CoachAttachment>();

                for (CoachAttachment coachAttachment : oldAttachments) {
                    oldCoachAttachmentMap.put(coachAttachment.getId(), coachAttachment);
                }
                for (CoachAttachment coachAttachment : newAttachments) {
                    if (coachAttachment.getId() != null) {
                        oldCoachAttachmentMap.remove(coachAttachment.getId());

                    } else {
                        CommonAttachment c = commonAttachmentMapper.selectByPrimaryKey(coachAttachment.getFileid());
                        if (c != null) {
                            coachAttachment.setFid(introduce.getId());
                            this.coachAttachmentMapper.insert(coachAttachment);
                        }

                    }
                    for (long l : oldCoachAttachmentMap.keySet()) {
                        this.coachAttachmentMapper.deleteByPrimaryKey(l);
                    }
                }
                str = "更新";
            } else {
                this.introduceMapper.insert(introduce);
                for (IntroduceJobTime introduceJobTime : introduceJobTimes) {
                    introduceJobTime.setFid(introduce.getId());
                    this.introduceJobTimeMapper.insert(introduceJobTime);
                }
                for (CoachAttachment coachAttachment : newAttachments) {
                    coachAttachment.setFid(introduce.getId());

                    this.coachAttachmentMapper.insert(coachAttachment);
                }

                str = "添加";
            }

            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            str += "失败";
            return str;
        }
        str += "成功";
        return str;
    }

    @Override
    public String deleteIntroducePage(Introduce introduce) {
        String str = "删除成功";
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);

        try {
            if (introduce.getId() > 0) {
                long id = introduce.getId();
                this.introduceMapper.deleteByPrimaryKey(id);
                this.introduceJobTimeMapper.deleteByFid(id);
                this.consultTypeMapper.deleteByFid(id);
                this.coachAttachmentMapper.deleteByPrimaryKey(id);
            } else {
                str = "删除失败";
            }

            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚

            txManager.rollback(status);
            str = "删除失败";
        }
        return str;
    }

}
