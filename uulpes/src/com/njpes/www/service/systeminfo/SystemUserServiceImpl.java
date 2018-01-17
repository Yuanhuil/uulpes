package com.njpes.www.service.systeminfo;

import com.njpes.www.dao.systeminfo.SystemUserDao;

public class SystemUserServiceImpl implements SystemUserService {

    private SystemUserDao systemUserDao;

    public void setSystemUserDao(SystemUserDao systemUserDao) {
        this.systemUserDao = systemUserDao;
    }

    @Override
    public String selectUser(String username) {
        // TODO Auto-generated method stub
        return systemUserDao.selectUser(username);
    }

}
