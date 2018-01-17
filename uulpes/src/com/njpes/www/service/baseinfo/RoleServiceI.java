package com.njpes.www.service.baseinfo;

import java.util.HashMap;
import java.util.List;

import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;
import com.njpes.www.utils.PageParameter;

public interface RoleServiceI {

    public List<Role> selectBySelective(Role role);

    public List<Role> selectAllConditionRole(Role role, PageParameter page);

    /**
     * @author 赵忠诚
     */
    public List<Role> selectAll();

    /**
     * @param 标示是否是陶老师，根据account的logintype即可。
     * @param isadmin
     *            是否仅仅取管理员的角色
     * @author 赵忠诚
     */
    public List<Role> selectRolesByOrgLevel(Integer orgLevel, String orgType, boolean isadmin);

    public List<Role> selectRolesByRoleLevel(Integer roleLevel, String orgType);

    // 根据学校类型获得学校管理员角色集合：1：基层学校，2：直属学校，3所有学校
    public List<Role> selectTeacherAdmin(int type);

    public List<Role> selectRolesByRoleAndOrgLevelIncludeSonAdmin(Integer orgLevel, Integer roleLevel, String orgType);

    public int selectAllRole();

    public void deleteRoles(String roleIds);

    public void showOrNotRoleFlag(long roleId, String isShow);

    public boolean addRole(Role entityRole, String perm_resource);

    // public void updateRole(int orgLevel,int roleLevel,String roleName,String
    // roleAlias,String isShow,String roleDesc,String addPermResource,String
    // deletePermResource,String roleId,String allPermResource);
    // 史斌修改更新角色操作
    public void updateRole(Role entityRole, String allPermResource);

    public Role selectRole(long roleId);

    public List<RoleResourcePermission> selectRRPAccordingRoleId(Long roleId);

    public List<Role> getTeacherRoles(long orgid);

    public List<Role> getEduTeacherRoles(long orgid);

    public List<Role> getTeacherRolesByOrglevel(int orglevel);

    // 得到某个roleid的下属的role
    public List<Role> getSubordinateRoles(int roleId);

    // 史斌增加根据角色级别和组织机构获得所有的资源
    public List<Role> selectRolesByOrgAndLevel(int orgid, int rolelevel, HashMap<String, Integer> page);

    public List<Role> selectRolesByOrgLevelAndRoleLevel(int orglevel, int rolelevel, String orgtype);
}
