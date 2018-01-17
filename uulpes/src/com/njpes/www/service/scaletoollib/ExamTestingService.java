package com.njpes.www.service.scaletoollib;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public interface ExamTestingService {
    public void openQuestionnaire(HttpServletRequest req, HashMap<Object, Object> page);

    public void nextPageAction();
}
