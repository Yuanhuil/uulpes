package com.njpes.www.service.baseinfo;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.njpes.www.dao.baseinfo.AuthMapper;
import com.njpes.www.dao.baseinfo.RoleMapper;
import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.Permission;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;

@Service("authService")
public class AuthServiceImpl implements AuthServiceI {

    @Autowired
    private AccountServiceI accountService;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired

    private PermissionServiceI permissionService;

    /*
     * public void addUserAuth(Long[] userIds, Auth m) {
     * 
     * if (ArrayUtils.isEmpty(userIds)) { return; }
     * 
     * for (Long userId : userIds) {
     * 
     * Account user = accountService.findOne(userId); if (user == null) {
     * continue; }
     * 
     * List<Auth> authList = authMapper.selectByUserId(userId); if (authList !=
     * null) { auth.addRoleIds(m.getRoleIds()); continue; } auth = new Auth();
     * auth.setUserId(userId); auth.setAuthType(m.getAuthType());
     * auth.setRoleIds(m.getRoleIds()); save(auth); } }
     */

    @Override
    public Set<Long> findRoleIds(Long userId, Set<Long> groupIds, Set<Long> organizationIds, Set<Long> jobIds,
            Set<Long[]> organizationJobIds) {

        List<Auth> auths = authMapper.selectByUGOJIds(userId, groupIds, organizationIds, jobIds, organizationJobIds);

        Set<Long> roleIds = Sets.newHashSet();
        for (Auth auth : auths) {
            roleIds.add(auth.getRoleId());
        }

        return roleIds;
    }

    @Override
    public Set<Role> findRoles(Long userId, String logintype) {
        List<Permission> permissionList = permissionService.selectAllPermissions();
        Hashtable<Long, String> permHashtable = new Hashtable<Long, String>();
        Hashtable<Long, String> permHashtableEng = new Hashtable<Long, String>();
        for (Permission p : permissionList) {
            permHashtable.put(p.getId(), p.getPerm_name());
            permHashtableEng.put(p.getId(), p.getPermission());
        }
        List<Role> auths = roleMapper.selectRolesByUserId(userId, logintype);
        Set<Role> roles = Sets.newHashSet();
        for (Role r : auths) {
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
            roles.add(r);
        }
        return roles;
    }

    @Override
    public int insert(Auth auth) {
        return authMapper.insertSelective(auth);
    }

    @Override
    public int update(Auth auth) {
        return authMapper.updateByPrimaryKeySelective(auth);
    }

    @Override
    public int deleteByUserid(long userid) {
        return authMapper.deleteByUserId(userid);
    }
}
