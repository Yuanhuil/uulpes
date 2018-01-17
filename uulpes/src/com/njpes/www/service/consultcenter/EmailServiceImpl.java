package com.njpes.www.service.consultcenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.EmailMapper;
import com.njpes.www.entity.consultcenter.Email;
import com.njpes.www.utils.PageParameter;

@Service("emailService")
public class EmailServiceImpl implements EmailServiceI {
    @Autowired
    private EmailMapper emailMapper;

    @Override
    public int updateEmail(Email email) {
        // TODO Auto-generated method stub
        return emailMapper.updateByPrimaryKeyWithBLOBs(email);
    }

    @Override
    public int saveEmail(Email email) {
        // TODO Auto-generated method stub
        return emailMapper.insert(email);
    }

    @Override
    public int delEmail(Email email) {
        // TODO Auto-generated method stub
        return emailMapper.deleteByPrimaryKey(email.getId());
    }

    @Override
    public Email selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return emailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Email> selectListByEntity(Email email, PageParameter page, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return emailMapper.selectEntityByPage(email, page, beginDate, endDate);
    }

    @Override
    public List<Email> selectParentEmailList(String idL) {
        // TODO Auto-generated method stub
        List<Email> emails = new ArrayList<Email>();
        if (idL != null && idL.length() > 0) {
            if (idL.endsWith(",")) {
                idL = idL.substring(0, idL.length() - 1);
            }
            String[] ids = idL.split(",");
            if (ids.length > 0) {
                emails = emailMapper.selectListByIds(ids);
            }
        }
        return emails;
    }

}
