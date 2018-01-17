package com.njpes.www.entity.baseinfo;

public class ParentWithBLOBs extends Parent {

    private static final long serialVersionUID = 1L;
    private byte[] zp;// 照片
    private String username;
    private String bjxx;

    public String getBjxx() {
        return bjxx;
    }

    public void setBjxx(String bjxx) {
        this.bjxx = bjxx;
    }

    public byte[] getZp() {
        return zp;
    }

    public void setZp(byte[] zp) {
        this.zp = zp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}