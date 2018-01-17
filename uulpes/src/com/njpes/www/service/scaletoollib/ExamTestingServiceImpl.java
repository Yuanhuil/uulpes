package com.njpes.www.service.scaletoollib;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edutec.scale.online.ExamSpace;

public class ExamTestingServiceImpl implements ExamTestingService {
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void openQuestionnaire(HttpServletRequest req, HashMap<Object, Object> page) {
        // TODO Auto-generated method stub
        try {
            ExamSpace examSpace = ExamSpace.getInstance(req);
            examSpace.createOnlineExam();
            examSpace.nextPage(page);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void nextPageAction() {
        // TODO Auto-generated method stub

    }

}
