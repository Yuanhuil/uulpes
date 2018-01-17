package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.RoleResourcePermission;

public interface RoleResourcePermissionMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role_res_perm
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role_res_perm
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    int insert(RoleResourcePermission record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role_res_perm
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    int insertSelective(RoleResourcePermission record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role_res_perm
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    RoleResourcePermission selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role_res_perm
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    int updateByPrimaryKeySelective(RoleResourcePermission record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role_res_perm
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    int updateByPrimaryKey(RoleResourcePermission record);

    void updateByPrimaryKeyModify(RoleResourcePermission record);

    List<RoleResourcePermission> selectRRPAccordingRoleId(long roleId);

    public void deleteAccordingModify(RoleResourcePermission record);

    // 史斌增加删除一个角色的所有授权
    public void deleteByRole(@Param("roleid") Long roleid);

    public void deleteByRoles(@Param("roleids") List<Long> roleid);
}