package com.njpes.www.datacenter.entity;

public class SchoolData {
    public String id;

    /**
     * 学校名称
     */
    public String name;

    /**
     * 学校地址
     */
    public String address;

    /**
     * 办学类型
     */
    public String xxbxlxm;

    /**
     * 办学类型名称
     */
    public String xxbxlxmName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getXxbxlxm() {
        return xxbxlxm;
    }

    public void setXxbxlxm(String xxbxlxm) {
        this.xxbxlxm = xxbxlxm;
    }

    public String getXxbxlxmName() {
        return xxbxlxmName;
    }

    public void setXxbxlxmName(String xxbxlxmName) {
        this.xxbxlxmName = xxbxlxmName;
    }

}
