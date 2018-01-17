package com.njpes.www.service.baseinfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Syslog;
import com.njpes.www.entity.baseinfo.SyslogFilterParam;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.utils.PageParameter;

public interface SyslogServiceI {

    public List<Syslog> queryPageSyslogEntity(SyslogFilterParam sef);

    public List<Syslog> querySyslogByPage(PageParameter page, SyslogFilterParam sef);

    public void log(Syslog log);

    public void log(HttpServletRequest request, String menuname, String content);

    public void log(Organization org, Account account, String menuname, String content);
}
