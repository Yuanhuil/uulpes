package com.njpes.www.shiro.filter.authc;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.njpes.www.service.baseinfo.AccountServiceI;

/**
 * 基于几点修改： 1、onLoginFailure 时 把异常添加到request attribute中 而不是异常类名 2、登录成功时：成功页面重定向：
 * 2.1、如果前一个页面是登录页面，-->2.3 2.2、如果有SavedRequest 则返回到SavedRequest
 * 2.3、否则根据当前登录的用户决定返回到管理员首页/前台首页
 * <p/>
 * <p>
 * User: zhaozhongcheng
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    @Autowired
    AccountServiceI accountService;

    public static final String DEFAULT_LOGINTYPE_PARAM = "logintype";

    public static final String DEFAULT_DOGID_PARAM = "dogid";

    public static final String DEFAULT_RESPONSE_PARAM = "response";

    private String logintypeParam = DEFAULT_LOGINTYPE_PARAM;

    private String dogidParam = DEFAULT_DOGID_PARAM;

    private String responseParam = DEFAULT_RESPONSE_PARAM;

    public String getLogintypeParam() {
        return logintypeParam;
    }

    public void setLogintypeParam(String logintypeParam) {
        this.logintypeParam = logintypeParam;
    }

    public String getDogidParam() {
        return dogidParam;
    }

    public void setDogidParam(String dogidParam) {
        this.dogidParam = dogidParam;
    }

    public String getResponseParam() {
        return responseParam;
    }

    public void setResponseParam(String responseParam) {
        this.responseParam = responseParam;
    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String username = getUsername(request);
        String password = getPassword(request);
        String logintype = getLoginType(request);
        int dogid = Integer.parseInt(getDogId(request));
        String dogresponse = getDogResponse(request);

        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new UsernamePasswordToken(username, password, rememberMe, host, logintype, dogid, dogresponse);
    }

    protected String getLoginType(ServletRequest request) {
        return WebUtils.getCleanParam(request, getLogintypeParam());
    }

    protected String getDogId(ServletRequest request) {
        return WebUtils.getCleanParam(request, getDogidParam());
    }

    protected String getDogResponse(ServletRequest request) {
        return WebUtils.getCleanParam(request, getResponseParam());
    }

    /**
     * 默认的成功地址
     */
    private String defaultSuccessUrl;
    /**
     * 管理员默认的成功地址
     */
    private String adminDefaultSuccessUrl;

    public void setUserService(AccountServiceI userService) {
        this.accountService = userService;
    }

    public void setDefaultSuccessUrl(String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
    }

    public void setAdminDefaultSuccessUrl(String adminDefaultSuccessUrl) {
        this.adminDefaultSuccessUrl = adminDefaultSuccessUrl;
    }

    public String getDefaultSuccessUrl() {
        return defaultSuccessUrl;
    }

    public String getAdminDefaultSuccessUrl() {
        return adminDefaultSuccessUrl;
    }

    /**
     * 根据用户选择成功地址
     *
     * @return
     */
    @Override
    public String getSuccessUrl() {
        return getDefaultSuccessUrl();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response) || pathsMatch("/login.do", request)) {
            // 判断是否是提交操作，如果是提交操作则执行提交动作
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                // allow them to see the login page ;)

                return true;
            }
        } else {
            redirectToLogin(request, response);
            return false;
        }
    }

}
