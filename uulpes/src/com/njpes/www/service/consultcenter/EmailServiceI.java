package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.Email;
import com.njpes.www.utils.PageParameter;

public interface EmailServiceI {

    public int updateEmail(Email email);

    public int saveEmail(Email email);

    public int delEmail(Email org);

    public Email selectByPrimaryKey(long id);

    public List<Email> selectListByEntity(Email email, PageParameter page, Date beginDate, Date endDate);

    public List<Email> selectParentEmailList(String idL);

}
