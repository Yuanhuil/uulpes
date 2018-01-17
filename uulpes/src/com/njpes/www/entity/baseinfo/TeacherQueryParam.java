package com.njpes.www.entity.baseinfo;

public class TeacherQueryParam extends TeacherWithBLOBs {

    /**
     * 
     */
    private static final long serialVersionUID = 6385707646173820889L;

    private String roleId;

    private long[] roleArray;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public long[] getRoleArray() {
        return roleArray;
    }

    public void setRoleArray(long[] roleArray) {
        this.roleArray = roleArray;
    }

}
