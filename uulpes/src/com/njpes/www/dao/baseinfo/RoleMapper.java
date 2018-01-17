package com.njpes.www.dao.baseinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.utils.PageParameter;

public interface RoleMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role
     *
     * @mbggenerated Sat Mar 21 22:53:17 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role
     *
     * @mbggenerated Sat Mar 21 22:53:17 CST 2015
     */
    int insert(Role record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role
     *
     * @mbggenerated Sat Mar 21 22:53:17 CST 2015
     */
    int insertSelective(Role record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role
     *
     * @mbggenerated Sat Mar 21 22:53:17 CST 2015
     */
    Role selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role
     *
     * @mbggenerated Sat Mar 21 22:53:17 CST 2015
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table role
     *
     * @mbggenerated Sat Mar 21 22:53:17 CST 2015
     */
    int updateByPrimaryKey(Role record);

    List<Role> selectBySelective(Role role);

    List<Role> selectAll();

    List<Role> selectAllByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 根据userid和登录类型查看该用户具有的角色
     * 
     * @param userid
     * @param logintype
     * @return Role
     * @author 赵忠诚
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId, @Param("logintype") String logintype);

    /**
     * @param orgLevel
     * @return Role
     * @author 赵忠诚
     */
    List<Role> selectRolesByOrgLevel(@Param("orglevel") Integer orgLevel, @Param("orgType") String orgType,
            @Param("isadmin") boolean isadmin);

    public void deleteByPrimaryKeys(List<Long> roleids);

    public int selectAllRole();

    public List<Role> selectEntityMapPage(@Param("role") Role role, @Param("page") PageParameter page);

    public List<Role> getTeacherRoles(long orgid);

    public List<Role> getEduTeacherRoles(long orgid);

    public List<Role> getTeacherRolesByOrglevel(@Param("orglevel") int orglevel);

    public List<Role> getSubordinateRoles(@Param("roleId") int roleId);

    public List<Role> selectRolesByRoleAndOrgLevelIncludeSonAdmin(@Param("orglevel") Integer orgLevel,
            @Param("rolelevel") Integer rolelevel, @Param("orgType") String orgType);

    public List<Role> selectRolesByRoleLevel(@Param("rolelevel") Integer roleLevel, @Param("orgType") String orgType);

    public List<Role> selectRolesByOrgAndLevel(@Param("orgid") int orgid, @Param("rolelevel") int rolelevel,
            @Param("page") HashMap<String, Integer> map);

    public List<Role> selectTeacherAdmin(@Param("type") int type);

    public List<Role> selectRolesByOrgLevelAndRoleLevel(@Param("orglevel") int orglevel,
            @Param("rolelevel") int rolelevel, @Param("orgtype") String orgtype);
}