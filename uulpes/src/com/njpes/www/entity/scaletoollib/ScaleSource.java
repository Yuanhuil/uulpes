package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ScaleSource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;

    private String scalesource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScalesource() {
        return scalesource;
    }

    public void setScalesource(String scalesource) {
        this.scalesource = scalesource;
    }
}
