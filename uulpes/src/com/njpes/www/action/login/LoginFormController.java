
package com.njpes.www.action.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.njpes.www.constant.Constants;
import com.njpes.www.service.baseinfo.SyslogServiceI;

/**
 * 登陆控制
 * 
 * @author 赵忠诚
 */
@Controller
public class LoginFormController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping(value = { "/{login:login;?.*}" }) // spring3.2.2 bug see
                                                      // http://jinnianshilongnian.iteye.com/blog/1831408
    public String loginForm(HttpServletRequest request, ModelMap model) {
        // 登录失败 提取错误消息
        Exception shiroLoginFailureEx = (Exception) request
                .getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (shiroLoginFailureEx != null) {
            model.addAttribute(Constants.ERROR, shiroLoginFailureEx.getMessage());
        }
        model.addAttribute("appheadtitle", Constants.APPLICATION_APPHEADTITLE);
        model.addAttribute("appfooter", Constants.APPLICATION_APPFOOTER);
        // 如果用户直接到登录页先退出
        // 原因：isAccessAllowed实现是subject.isAuthenticated()---->即如果用户验证�?�?就允许访�?
        // 这样会导致登录一直死循环
        /**
         * 用户误关闭了浏览器。这种情况下，用户会再次登录。此时因为上次的会话还存在，
         * 所以不会去执行FormAuthenticationFilter，也就不会跳转到successUrl，
         * 而是会执行登录页面中form的post action。
         */
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            subject.logout();
            // return "redirect:login.do";
        }

        // 如果同时存在错误消息 �?普�?消息 只保留错误消�?
        if (model.containsAttribute(Constants.ERROR)) {
            model.remove(Constants.MESSAGE);
            // request.getSession().setAttribute("action", "登录系统出错");
        }
        return "homepage/Index";
    }

    /**
     * 登录退出,因为清空了session所以要重定向重新生成sessionid
     * 
     * @author 赵忠诚
     */
    @RequestMapping(value = { "/logout" })
    public String logout(HttpServletRequest request, ModelMap model) {
        // logservice.log(request, "", "退出登录");
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            subject.logout();
        }
        return "redirect:login.do";
    }

}
