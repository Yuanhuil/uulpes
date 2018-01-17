package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.WarningInterveneTMapper;
import com.njpes.www.dao.consultcenter.WarningTeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.entity.consultcenter.WarningInterveneTWithBLOBs;
import com.njpes.www.entity.consultcenter.WarningTeacher;
import com.njpes.www.entity.scaletoollib.ExamresultTeacher;
import com.njpes.www.utils.PageParameter;

@Service("warningTeacherService")
public class WarningTeacherServiceImpl implements WarningTeacherServiceI {
    @Autowired
    private WarningTeacherMapper warningTeacherMapper;
    @Autowired
    private ExamresultTeacherMapper examresultTeacherMapper;
    @Autowired
    private WarningInterveneTMapper warningInterveneTMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningTeacherServiceI#
     * selectListByWarningTeacher(com.njpes.www.entity.consultcenter.
     * WarningTeacher, com.njpes.www.utils.PageParameter, java.util.Date,
     * java.util.Date)
     */
    @Override
    public List<WarningTeacher> selectListByEntity(WarningTeacher warningTeacher, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return warningTeacherMapper.selectEntityByPage(warningTeacher, page, beginDate, endDate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningTeacherServiceI#
     * updateIswarnsure(com.njpes.www.entity.consultcenter.WarningTeacher)
     */
    @Override
    public int updateIswarnsure(WarningTeacher warningTeacher) {
        // TODO Auto-generated method stub
        ExamresultTeacher record = new ExamresultTeacher();
        Byte iswarnsure = warningTeacher.getIswarnsure();
        warningTeacher.setIswarnsure(null);
        // List<WarningTeacher>warningTeacherList=warningTeacherMapper.selectEntityByPage(warningTeacher,
        // new PageParameter(1,10), null, null);
        examresultTeacherMapper.selectByPrimaryKey(warningTeacher.getId());
        warningTeacher.setIswarnsure(iswarnsure);
        record.setId(warningTeacher.getId());
        examresultTeacherMapper.updateByPrimaryKey(record);
        /*
         * if(iswarnsure.toString().endsWith("2")){ WarningInterveneTWithBLOBs
         * warningInterveneT= WarningTeacher2WarningInterveneT(
         * warningTeacherList.get(0)); warningInterveneT.setStatus("2");
         * warningInterveneTMapper.insert(warningInterveneT); }
         */
        return 0;
    }

    public WarningInterveneTWithBLOBs WarningTeacher2WarningInterveneT(WarningTeacher warningTeacher) {
        WarningInterveneTWithBLOBs warningInterveneT = new WarningInterveneTWithBLOBs();
        warningInterveneT.setCardid(warningTeacher.getSfzjh());
        warningInterveneT.setExamresultId(warningTeacher.getId());

        warningInterveneT.setLevel(warningTeacher.getWarningGrade().toString());
        warningInterveneT.setName(warningTeacher.getXm());
        warningInterveneT.setSex(warningTeacher.getXbm());
        warningInterveneT.setStatus(warningTeacher.getIswarnsure().toString());
        warningInterveneT.setWarningTime(new Date());
        warningInterveneT.setSchoolId(warningTeacher.getOrgId().longValue());
        return warningInterveneT;
    }

    @Override
    public WarningTeacher selectEntityById(long id) {
        WarningTeacher warningTeacher = new WarningTeacher();
        warningTeacher.setId(id);
        List<WarningTeacher> warningTeacherList = warningTeacherMapper.selectEntityByPage(warningTeacher,
                new PageParameter(1, 10), null, null);
        if (warningTeacherList.size() > 0) {
            return warningTeacherList.get(0);
        }
        return null;
    }

}
