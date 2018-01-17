package com.njpes.www.service.baseinfo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.SyslogMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Syslog;
import com.njpes.www.entity.baseinfo.SyslogFilterParam;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.utils.PageParameter;

@Service("syslogService")
public class SyslogServiceImpl implements SyslogServiceI {

    @Autowired
    private SyslogMapper syslogMapper;

    public SyslogMapper getSyslogMapper() {
        return syslogMapper;
    }

    public void setSyslogMapper(SyslogMapper syslogMapper) {
        this.syslogMapper = syslogMapper;
    }

    @Override
    public List<Syslog> queryPageSyslogEntity(SyslogFilterParam sef) {
        // TODO Auto-generated method stub
        List<Syslog> syslogList = this.syslogMapper.queryPageSyslogEntity(sef);
        return syslogList;
    }

    @Override
    public void log(Syslog log) {
        // TODO Auto-generated method stub
        syslogMapper.insert(log);
    }

    @Override
    public void log(Organization org, Account account, String menuname, String content) {
        Syslog log = new Syslog();
        if (org != null) {
            log.setOrgid(org.getId().intValue());
            log.setOrglevelid(org.getOrgLevel());
        }
        if (account != null) {
            log.setOperator(account.getRealname());
            log.setRoleid(account.getRoles().iterator().next().getId().intValue());
        }
        log.setMenuname(menuname);
        log.setOptime(new Date());
        log.setContent(content);

        syslogMapper.insert(log);

    }

    @Override
    public void log(HttpServletRequest request, String menuname, String content) {
        Organization org = (Organization) request.getSession().getAttribute("user_org");
        Account account = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        Syslog log = new Syslog();
        if (org != null) {
            log.setOrgid(org.getId().intValue());
            log.setOrglevelid(org.getOrgLevel());
        }
        if (account != null) {
            log.setOperator(account.getRealname());
            log.setRoleid(account.getRoles().iterator().next().getId().intValue());
        }
        log.setMenuname(menuname);
        log.setOptime(new Date());
        log.setContent(content);

        syslogMapper.insert(log);
    }

    @Override
    public List<Syslog> querySyslogByPage(PageParameter page, SyslogFilterParam sef) {
        List<Syslog> syslogList = this.syslogMapper.queryPageSyslogByPage(page, sef);
        return syslogList;
    }

}
