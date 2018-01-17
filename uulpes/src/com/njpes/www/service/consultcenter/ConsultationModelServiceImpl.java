package com.njpes.www.service.consultcenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.ConsultationModelMapper;
import com.njpes.www.entity.consultcenter.ConsultationModel;

@Service("consultationModelService")
public class ConsultationModelServiceImpl implements ConsultationModelServiceI {

    @Autowired
    private ConsultationModelMapper consultationModelMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.ConsultationModelServiceI#getAll()
     */
    @Override
    public List<ConsultationModel> selectAll() {
        // TODO Auto-generated method stub
        return consultationModelMapper.selectAll();
    }

}
