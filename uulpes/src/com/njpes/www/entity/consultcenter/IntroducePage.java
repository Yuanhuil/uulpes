package com.njpes.www.entity.consultcenter;

import java.util.List;

public class IntroducePage {
    private Introduce introduce;
    private List<CoachAttachment> coachAttachments;
    private List<IntroduceJobTime> introduceJobTimes;

    public Introduce getIntroduce() {
        return introduce;
    }

    public void setIntroduce(Introduce introduce) {
        this.introduce = introduce;
    }

    public List<IntroduceJobTime> getIntroduceJobTimes() {
        return introduceJobTimes;
    }

    public void setIntroduceJobTimes(List<IntroduceJobTime> introduceJobTimes) {
        this.introduceJobTimes = introduceJobTimes;
    }

    public List<CoachAttachment> getCoachAttachments() {
        return coachAttachments;
    }

    public void setCoachAttachments(List<CoachAttachment> coachAttachments) {
        this.coachAttachments = coachAttachments;
    }

}