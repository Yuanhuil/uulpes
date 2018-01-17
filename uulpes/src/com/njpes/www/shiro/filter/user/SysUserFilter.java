package com.njpes.www.shiro.filter.user;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.enums.UserState;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.AuthServiceI;
import com.njpes.www.service.baseinfo.RoleServiceI;

/**
 * 验证用户过滤器 1、用户是否删除 2、用户是否锁定
 * 
 * @author 赵忠诚
 */
public class SysUserFilter extends AccessControlFilter {

    @Autowired
    private AccountServiceI accountService;

    @Autowired
    private AuthServiceI authService;

    @Autowired
    private RoleServiceI roleService;

    /**
     * 用户删除了后重定向的地址
     */
    private String userNotfoundUrl;
    /**
     * 用户锁定后重定向的地址
     */
    private String userBlockedUrl;
    /**
     * 未知错误
     */
    private String userUnknownErrorUrl;

    public String getUserNotfoundUrl() {
        return userNotfoundUrl;
    }

    public void setUserNotfoundUrl(String userNotfoundUrl) {
        this.userNotfoundUrl = userNotfoundUrl;
    }

    public String getUserBlockedUrl() {
        return userBlockedUrl;
    }

    public void setUserBlockedUrl(String userBlockedUrl) {
        this.userBlockedUrl = userBlockedUrl;
    }

    public String getUserUnknownErrorUrl() {
        return userUnknownErrorUrl;
    }

    public void setUserUnknownErrorUrl(String userUnknownErrorUrl) {
        this.userUnknownErrorUrl = userUnknownErrorUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getPrincipal() == null) {
            return true;
        }

        Account user = (Account) subject.getPrincipal();

        // 把当前用户放到request中
        request.setAttribute(Constants.CURRENT_USER, user);
        // druid监控需要
        // ((HttpServletRequest)request).getSession().setAttribute(Constants.CURRENT_USERNAME,
        // username);

        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        Account user = (Account) request.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return true;
        }

        if (user.getState().equals(UserState.deleted.getIdentify())
                || user.getState().equals(UserState.blocked.getIdentify())) {
            getSubject(request, response).logout();
            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        getSubject(request, response).logout();
        saveRequestAndRedirectToLogin(request, response);
        return true;
    }

    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        Account user = (Account) request.getAttribute(Constants.CURRENT_USER);
        String url = null;
        if (user.getState().equals(UserState.deleted.getIdentify())) {
            url = getUserNotfoundUrl();
        } else if (user.getState().equals(UserState.blocked.getIdentify())) {
            url = getUserBlockedUrl();
        } else {
            url = getUserUnknownErrorUrl();
        }

        WebUtils.issueRedirect(request, response, url);
    }

}
