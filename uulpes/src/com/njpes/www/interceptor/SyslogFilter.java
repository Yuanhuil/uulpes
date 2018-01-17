package com.njpes.www.interceptor;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Syslog;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.service.baseinfo.SyslogServiceI;

public class SyslogFilter extends OncePerRequestFilter {

    @Autowired
    private SyslogServiceI logger;

    public SyslogFilter() {
        // ClassPathXmlApplicationContext ctx = new
        // ClassPathXmlApplicationContext("../applicationContext.xml");
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        if (wac != null)
            logger = (SyslogServiceI) wac.getBean("syslogService", SyslogServiceI.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        Object content = req.getSession().getAttribute("action");
        Organization org = (Organization) req.getSession().getAttribute("user_org");
        String url = req.getRequestURL().toString();
        Account userInfo = (Account) req.getSession().getAttribute(Constants.CURRENT_USER);
        if (content != null) {
            Syslog log = null;
            if (org != null) {
                if (log == null)
                    log = new Syslog();
                log.setOrgid(org.getId().intValue());
                log.setOrglevelid(org.getOrgLevel());
            }
            if (userInfo != null) {
                if (log == null)
                    log = new Syslog();
                log.setOperator(userInfo.getRealname());
                log.setRoleid(userInfo.getRoles().iterator().next().getId().intValue());
            }
            // if(StringUtils.isNotEmpty(url)){
            if (log == null)
                log = new Syslog();
            // url = url.replace("", newChar)
            log.setOptime(new Date());
            log.setContent(content.toString());

            // }
            logger.log(log);
            req.getSession().setAttribute("action", null);
        }
        chain.doFilter(req, resp);
    }

}
