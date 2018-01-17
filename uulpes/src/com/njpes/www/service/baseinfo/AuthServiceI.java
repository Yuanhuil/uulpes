package com.njpes.www.service.baseinfo;

import java.util.Set;

import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.Role;

public interface AuthServiceI {

    /**
     * 根据用户信息获取 角色 1.1、用户 根据用户绝对匹配 1.2、组织机构 根据组织机构绝对匹配 此处需要注意 祖先需要自己获取 1.3、工作职务
     * 根据工作职务绝对匹配 此处需要注意 祖先需要自己获取 1.4、组织机构和工作职务 根据组织机构和工作职务绝对匹配 此处不匹配祖先 1.5、组
     * 根据组绝对匹配
     *
     * @param userId
     *            必须有
     * @param groupIds
     *            可选
     * @param organizationIds
     *            可选
     * @param jobIds
     *            可选
     * @param organizationJobIds
     *            可选
     * @return 角色集合
     * @author 赵忠诚
     */
    public Set<Long> findRoleIds(Long userId, Set<Long> groupIds, Set<Long> organizationIds, Set<Long> jobIds,
            Set<Long[]> organizationJobIds);

    /**
     * 根据用户信息获取 角色
     * 
     * @param userId
     *            必须有
     * @return 角色集合
     * @author 赵忠诚
     */
    public Set<Role> findRoles(Long userId, String logintype);

    /**
     * 插入信息
     * 
     * @author 赵忠诚
     */
    public int insert(Auth auth);

    /**
     * 更新信息
     * 
     * @author 赵忠诚
     */
    public int update(Auth auth);

    /**
     * 删除userid的授权信息
     * 
     * @author 赵忠诚
     */
    public int deleteByUserid(long userid);
}
