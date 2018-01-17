package com.njpes.www.entity.util;

import java.io.Serializable;

public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String catid;


    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

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

}
