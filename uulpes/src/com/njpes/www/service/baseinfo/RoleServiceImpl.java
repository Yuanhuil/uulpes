package com.njpes.www.service.baseinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.baseinfo.RoleMapper;
import com.njpes.www.dao.baseinfo.RoleResourcePermissionMapper;
import com.njpes.www.entity.baseinfo.Permission;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;
import com.njpes.www.utils.PageParameter;

import net.sf.json.JSONObject;

@Service("roleService")
public class RoleServiceImpl implements RoleServiceI {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleResourcePermissionMapper rrpMapper;
    @Autowired
    private RoleResourcePermissionMapper roleResourcePermissionMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired

    private PermissionServiceI permissionService;

    @Override
    public List<Role> selectAll() {
        return this.roleMapper.selectAll();
    }

    @Override
    public void deleteRoles(String roleIds) {
        List<Long> list = new ArrayList<Long>();
        String[] ids = roleIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            long id = Long.parseLong(ids[i]);
            list.add(id);
        }
        rrpMapper.deleteByRoles(list);
        this.roleMapper.deleteByPrimaryKeys(list);
    }

    @Override
    public void showOrNotRoleFlag(long roleId, String isShow) {
        // TODO Auto-generated method stub
        Role role = new Role();
        role.setId(roleId);
        role.setIsShow(isShow);
        this.roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public boolean addRole(Role roleentity, String perm_resource) {
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 插入角色表,同时返回相应的id
            this.roleMapper.insert(roleentity);
            // 对资源和操作进行分解
            JSONObject permResource = JSONObject.fromObject(perm_resource);
            if (perm_resource != null && perm_resource.length() > 0) {
                Iterator it = permResource.keys();
                while (it.hasNext()) {
                    String res = (String) it.next();
                    String permids = permResource.getString(res);
                    RoleResourcePermission rrp = new RoleResourcePermission();
                    rrp.setRoleId(roleentity.getId());
                    rrp.setResId(Long.parseLong(res));
                    rrp.setPermIds(permids);
                    rrpMapper.insert(rrp);
                }
            }
            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
            return false;
        }
        return true;
    }

    @Override
    public void updateRole(Role roleentity, String allPermResource) {

        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        // 删除所有该角色的授权
        try {
            // 更新角色表
            this.roleMapper.updateByPrimaryKeySelective(roleentity);
            rrpMapper.deleteByRole(roleentity.getId());
            // 对资源和操作进行分解
            JSONObject permResource = JSONObject.fromObject(allPermResource);
            if (allPermResource != null && allPermResource.length() > 0) {
                Iterator it = permResource.keys();
                while (it.hasNext()) {
                    String res = (String) it.next();
                    String permids = permResource.getString(res);
                    RoleResourcePermission rrp = new RoleResourcePermission();
                    rrp.setRoleId(roleentity.getId());
                    rrp.setResId(Long.parseLong(res));
                    rrp.setPermIds(permids);
                    rrpMapper.insert(rrp);
                }
            }
            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
        }

    }

    @Override
    public int selectAllRole() {
        return this.roleMapper.selectAllRole();
    }

    @Override
    public List<Role> selectAllConditionRole(Role role, PageParameter page) {
        List<Role> listRole = this.roleMapper.selectEntityMapPage(role, page);
        return listRole;
    }

    @Override
    public Role selectRole(long roleId) {
        List<Permission> permissionList = permissionService.selectAllPermissions();
        Hashtable<Long, String> permHashtable = new Hashtable<Long, String>();
        Hashtable<Long, String> permHashtableEng = new Hashtable<Long, String>();
        for (Permission p : permissionList) {
            permHashtable.put(p.getId(), p.getPerm_name());
            permHashtableEng.put(p.getId(), p.getPermission());
        }
        Role r = roleMapper.selectByPrimaryKey(roleId);
        if (r.getResourcePermissions() != null) {
            for (RoleResourcePermission rrp : r.getResourcePermissions()) {
                String[] rrpids = rrp.getPermIds().split(",");
                String permName = "";
                String permEngName = "";
                for (String rrpid : rrpids) {
                    permName += permHashtable.get(Long.parseLong(rrpid)) + ",";
                    permEngName += permHashtableEng.get(Long.parseLong(rrpid)) + ",";
                }
                rrp.setPermIdsNames(permName);
                rrp.setPermIdsEngNames(permEngName);
            }
        }
        return r;
    }

    @Override
    public List<RoleResourcePermission> selectRRPAccordingRoleId(Long roleId) {
        // TODO Auto-generated method stub
        return this.roleResourcePermissionMapper.selectRRPAccordingRoleId(roleId);
    }

    @Override
    public List<Role> getTeacherRoles(long orgid) {
        return roleMapper.getTeacherRoles(orgid);
    }

    @Override
    public List<Role> getTeacherRolesByOrglevel(int orglevel) {
        return roleMapper.getTeacherRolesByOrglevel(orglevel);
    }

    @Override
    public List<Role> getEduTeacherRoles(long orgid) {
        return roleMapper.getEduTeacherRoles(orgid);
    }

    @Override
    public List<Role> getSubordinateRoles(int roleId) {
        return roleMapper.getSubordinateRoles(roleId);
    }

    @Override
    public List<Role> selectBySelective(Role role) {
        return roleMapper.selectBySelective(role);
    }

    @Override
    public List<Role> selectRolesByOrgLevel(Integer orgLevel, String orgType, boolean isadmin) {
        return roleMapper.selectRolesByOrgLevel(orgLevel, orgType, isadmin);
    }

    @Override
    public List<Role> selectRolesByOrgAndLevel(int orgid, int rolelevel, HashMap<String, Integer> page) {
        // TODO Auto-generated method stub
        return roleMapper.selectRolesByOrgAndLevel(orgid, rolelevel, page);
    }

    @Override
    public List<Role> selectRolesByRoleLevel(Integer roleLevel, String orgType) {
        return roleMapper.selectRolesByRoleLevel(roleLevel, orgType);
    }

    @Override
    public List<Role> selectRolesByRoleAndOrgLevelIncludeSonAdmin(Integer orgLevel, Integer roleLevel, String orgType) {
        return roleMapper.selectRolesByRoleAndOrgLevelIncludeSonAdmin(orgLevel, roleLevel, orgType);
    }

    @Override
    public List<Role> selectTeacherAdmin(int type) {
        return roleMapper.selectTeacherAdmin(type);
    }

    @Override
    public List<Role> selectRolesByOrgLevelAndRoleLevel(int orglevel, int rolelevel, String orgtype) {
        return roleMapper.selectRolesByOrgLevelAndRoleLevel(orglevel, rolelevel, orgtype);
    }
}
