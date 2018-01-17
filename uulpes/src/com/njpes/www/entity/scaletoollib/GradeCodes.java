package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.njpes.www.dao.scaletoollib.ScaleOperatorDao;

public class GradeCodes implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Map gradeMap;
    private ScaleOperatorDao scaleOperatorDao;

    public ScaleOperatorDao getScaleOperatorDao() {
        return scaleOperatorDao;
    }

    public void setScaleOperatorDao(ScaleOperatorDao scaleOperatorDao) {
        this.scaleOperatorDao = scaleOperatorDao;
    }

    public void init() {
        gradeMap = new HashMap();
        List<Map> gradeList = this.scaleOperatorDao.getAllGradeCode();
        for (Map grade : gradeList) {
            gradeMap.put(grade.get("cnt"), grade.get("id"));
        }
    }

    public static int getGradeId(String name) {
        return (Integer) gradeMap.get(name);
    }

}
