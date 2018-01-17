package com.njpes.www.entity.baseinfo;

import java.io.Serializable;

/**
 * @author OpenGSC
 */
public class SyslogFilterParam implements Serializable {

    private int orglevelid;
    private int orgid;
    private int roleid;
    private int menuid;
    private String menuname;
    private String operator;
    private String content;
    private int permissionid;
    private String starttime;
    private String endtime;
    private int startnum;
    private int endnum;

    public int getOrglevelid() {
        return orglevelid;
    }

    public void setOrglevelid(int orglevelid) {
        this.orglevelid = orglevelid;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getMenuid() {
        return menuid;
    }

    public void setMenuid(int menuid) {
        this.menuid = menuid;
    }

    public int getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(int permissionid) {
        this.permissionid = permissionid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getStartnum() {
        return startnum;
    }

    public void setStartnum(int startnum) {
        this.startnum = startnum;
    }

    public int getEndnum() {
        return endnum;
    }

    public void setEndnum(int endnum) {
        this.endnum = endnum;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
