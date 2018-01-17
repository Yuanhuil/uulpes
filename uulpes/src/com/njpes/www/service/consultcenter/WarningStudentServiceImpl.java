package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.WarningInterveneSMapper;
import com.njpes.www.dao.consultcenter.WarningStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.entity.consultcenter.WarningInterveneSWithBLOBs;
import com.njpes.www.entity.consultcenter.WarningStudent;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.utils.PageParameter;

@Service("warningStudentService")
public class WarningStudentServiceImpl implements WarningStudentServiceI {
    @Autowired
    private WarningStudentMapper warningStudentMapper;
    @Autowired
    private ExamresultStudentMapper examresultStudentMapper;
    @Autowired
    private WarningInterveneSMapper warningInterveneSMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningStudentServiceI#
     * selectListByWarningStudent(com.njpes.www.entity.consultcenter.
     * WarningStudent, com.njpes.www.utils.PageParameter, java.util.Date,
     * java.util.Date)
     */
    @Override
    public List<WarningStudent> selectListByEntity(WarningStudent warningStudent, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return warningStudentMapper.selectEntityByPage(warningStudent, page, beginDate, endDate);
    }

    @Override
    public WarningStudent selectEntityById(Long id) {
        // TODO Auto-generated method stub
        WarningStudent warningStudent = new WarningStudent();
        warningStudent.setId(id);
        List<WarningStudent> warningStudentList = warningStudentMapper.selectEntityByPage(warningStudent,
                new PageParameter(1, 10), null, null);
        if (warningStudentList.size() > 0) {
            return warningStudentList.get(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningStudentServiceI#
     * updateIswarnsure(com.njpes.www.entity.consultcenter.WarningStudent)
     */
    @Override
    public int updateIswarnsure(WarningStudent warningStudent) {
        // TODO Auto-generated method stub
        ExamresultStudent examresultStudent = new ExamresultStudent();
        Byte iswarnsure = warningStudent.getIswarnsure();
        warningStudent.setIswarnsure(null);
        // List<WarningStudent>warningStudentList=warningStudentMapper.selectEntityByPage(warningStudent,
        // new PageParameter(1,10), null, null);
        examresultStudent = examresultStudentMapper.selectByPrimaryKey(warningStudent.getId());
        examresultStudent.setIsWarnSure(iswarnsure);
        examresultStudentMapper.updateByPrimaryKey(examresultStudent);
        /*
         * if(iswarnsure.toString().endsWith("2")){ WarningInterveneSWithBLOBs
         * warningInterveneS= WarningStudent2WarningInterveneS(
         * warningStudentList.get(0)); warningInterveneS.setStatus("2");
         * warningInterveneSMapper.insert(warningInterveneS); }
         */
        return 0;
    }

    @Override
    public WarningInterveneSWithBLOBs WarningStudent2WarningInterveneS(WarningStudent warningStudent) {
        WarningInterveneSWithBLOBs warningInterveneS = new WarningInterveneSWithBLOBs();
        warningInterveneS.setCardid(warningStudent.getSfzjh());
        warningInterveneS.setExamresultId(warningStudent.getId());
        warningInterveneS.setGrade(warningStudent.getNjmc());
        warningInterveneS.setClassName(warningStudent.getBjmch());
        warningInterveneS.setLevel(warningStudent.getWarningGrade().toString());
        warningInterveneS.setName(warningStudent.getXm());
        warningInterveneS.setSex(warningStudent.getXbm());
        warningInterveneS.setStatus(warningStudent.getIswarnsure().toString());
        warningInterveneS.setWarningTime(new Date());
        warningInterveneS.setSchoolId(warningStudent.getOrgid().longValue());
        return warningInterveneS;
    }

}
