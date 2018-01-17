package com.njpes.www.service.baseinfo;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Permission;
import com.njpes.www.entity.baseinfo.Resource;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.RoleResourcePermission;
import com.njpes.www.entity.baseinfo.UserResPerm;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;

@Service
public class UserAuthService {

    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private JobServiceI jobService;

    @Autowired
    private AuthServiceI authService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    private ResourceServiceI resourceService;

    @Autowired
    private PermissionServiceI permissionService;

    public Set<Role> findRoles(Account account) {

        if (account == null) {
            return Sets.newHashSet();
        }
        Long accountId = account.getId();
        Set<Role> roles = authService.findRoles(accountId, account.getLogintype());
        return roles;
    }

    public Set<String> findStringRoles(Account user) {
        Set<Role> roles = findRoles(user);
        return Sets.newHashSet(Collections2.transform(roles, new Function<Role, String>() {
            @Override
            public String apply(Role input) {
                return input.getRole();
            }
        }));
    }

    /**
     * 根据角色获取 权限字符串 如sys:admin
     *
     * @param user
     * @return
     */
    public Set<String> findStringPermissions(Account user) {
        List<Permission> perms = permissionService.selectAllPermissions();
        Set<String> permissions = Sets.newHashSet();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            List<RoleResourcePermission> rrps = role.getResourcePermissions();
            for (RoleResourcePermission rrp : rrps) {

                Resource resource = rrp.getResource();

                String actualResourceIdentity = resource.getActualreskey();

                // 不可用 即没查到 或者标识字符串不存在
                if (resource == null || StringUtils.isEmpty(actualResourceIdentity)
                        || Boolean.FALSE.equals(resource.getIsShow())) {
                    continue;
                }

                String permNames = rrp.getPermIdsEngNames();
                String[] permNamesArry = permNames.split(",");
                for (String permissionName : permNamesArry) {
                    permissions.add(actualResourceIdentity + ":" + permissionName);

                }
            }

        }
        List<UserResPerm> userResPermList = user.getUserResPermsList();
        if (userResPermList != null) {
            for (UserResPerm urp : userResPermList) {
                Resource resource = urp.getResource();
                String actualResourceIdentity = resource.getActualreskey();

                // 不可用 即没查到 或者标识字符串不存在
                if (resource == null || StringUtils.isEmpty(actualResourceIdentity)
                        || Boolean.FALSE.equals(resource.getIsShow())) {
                    continue;
                }
                String permIds = urp.getPermIds();
                String[] permIdsArry = permIds.split(",");
                for (String permissionId : permIdsArry) {
                    Permission permission = findPermission(Long.parseLong(permissionId), perms);

                    // 不可用
                    if (permission == null || Boolean.FALSE.equals(permission.getIsShow())) {
                        continue;
                    }
                    permissions.add(actualResourceIdentity + ":" + permission.getPermission());

                }
            }
        }
        return permissions;
    }

    private Permission findPermission(long permId, List<Permission> perms) {
        for (Permission p : perms) {
            if (p.getId() == permId)
                return p;
        }
        return null;
    }

}