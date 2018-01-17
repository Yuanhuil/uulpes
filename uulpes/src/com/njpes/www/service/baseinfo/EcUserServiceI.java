package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.EcUserWithBLOBs;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.utils.PageParameter;

public interface EcUserServiceI {

    EcUserWithBLOBs selectById(Long id);

    int deleteByPrimaryKey(EcUserWithBLOBs ecuser);

    int deleteSelected(List<EcUserWithBLOBs> ecuserList);

    /**
     * 插入前端传入参数到数据库 需要操作多张表 account teacher teacher_ext auth表 需要使用事物处理
     * 
     * @param teacher
     *            前端传入的参数
     * @param orgid
     *            组织机构编码
     * @param roleids
     *            改用户的角色列表
     * @author 赵忠诚
     */
    int insert(EcUserWithBLOBs ecuser);

    /**
     * 更新前端传入参数到数据库 需要操作多张表 account teacher teacher_ext auth表 需要使用事物处理
     * 
     * @param teacher
     *            前端传入的参数
     * @param roleids
     *            改用户的角色列表
     * @author 赵忠诚
     */
    int update(EcUserWithBLOBs ecuser);

    /**
     * 分页获取本级以及下级直管教委或者直属学校的用户信息
     * 
     * @param
     * @author 赵忠诚
     */
    public List<EcUserWithBLOBs> getEcUsersByParentOrgIdByPage(EcUserWithBLOBs ecuser, Long parentorgid,
            Organization queryOrgEntity, PageParameter page);

    public EcUserWithBLOBs getEcUserByAccountId(Long id);

}
