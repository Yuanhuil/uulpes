package com.njpes.www.shiro.realm;

import java.util.Iterator;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.exception.baseinfo.UserBlockedException;
import com.njpes.www.exception.baseinfo.UserDogNotFoundException;
import com.njpes.www.exception.baseinfo.UserDogNotMatchException;
import com.njpes.www.exception.baseinfo.UserException;
import com.njpes.www.exception.baseinfo.UserNotExistsException;
import com.njpes.www.exception.baseinfo.UserPasswordNotMatchException;
import com.njpes.www.exception.baseinfo.UserPasswordRetryLimitExceedException;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.UserAuthService;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private AccountServiceI accountService;

    @Autowired
    private UserAuthService userAuthService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Account user = (Account) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userAuthService.findStringRoles(user));
        authorizationInfo.setStringPermissions(userAuthService.findStringPermissions(user));
        Iterator<String> roleSet = authorizationInfo.getRoles().iterator();
        while (roleSet.hasNext()) {
            System.out.println("ddd" + roleSet.next());
        }
        Iterator<String> permissionsSet = authorizationInfo.getStringPermissions().iterator();
        while (permissionsSet.hasNext()) {
            System.out.println(permissionsSet.next());
        }
        return authorizationInfo;
    }

    private static final String OR_OPERATOR = " or ";
    private static final String AND_OPERATOR = " and ";
    private static final String NOT_OPERATOR = "not ";

    /**
     * 支持or and not 关键词 不支持and or混用
     *
     * @param principals
     * @param permission
     * @return
     */
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if (permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for (String orPermission : permissions) {
                if (isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if (permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for (String orPermission : permissions) {
                if (!isPermittedWithNotOperator(principals, orPermission)) {
                    return false;
                }
            }
            return true;
        } else {
            return isPermittedWithNotOperator(principals, permission);
        }
    }

    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if (permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        } else {
            return super.isPermitted(principals, permission);
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername().trim();
        String password = "";
        String logintype = upToken.getLogintype();
        int dogid = upToken.getDogid();
        String response = upToken.getResponse();

        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }

        Account user = null;
        try {
            user = accountService.login(username, password, logintype, dogid, response);
            user.setLogintype(logintype);
        } catch (UserNotExistsException e) {
            throw new UnknownAccountException(e.getMessage(), e);
        } catch (UserPasswordNotMatchException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (UserPasswordRetryLimitExceedException e) {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        } catch (UserBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        } catch (UserDogNotFoundException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (UserDogNotMatchException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException(new UserException("user.unknown.error", null, "未知错误"));
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password.toCharArray(), getName());
        System.out.println("认证" + info);
        return info;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
