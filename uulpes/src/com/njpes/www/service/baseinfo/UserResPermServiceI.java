package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.UserResPerm;

public interface UserResPermServiceI {

    public List<UserResPerm> findResPermByUser(Long userId, String logintype);

    public int deleteResPermByUserId(long accountId, String orgType);

    public int updateResPermByUserId(UserResPerm userResPerm);

    public int updateResPermByUserId(List<UserResPerm> userResPerm);

    public int update(long accountid, String perm_resource, String orgtype);

    public int insertResPerm(UserResPerm userResPerm);

    public int insertResPerm(List<UserResPerm> userResPerm);
}
