package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class Scaletype implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column scaletype.id
     *
     * @mbggenerated Sat Mar 21 23:14:15 CST 2015
     */
    private Byte id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column scaletype.title
     *
     * @mbggenerated Sat Mar 21 23:14:15 CST 2015
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column scaletype.id
     *
     * @return the value of scaletype.id
     * @mbggenerated Sat Mar 21 23:14:15 CST 2015
     */
    public Byte getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column scaletype.id
     *
     * @param id
     *            the value for scaletype.id
     * @mbggenerated Sat Mar 21 23:14:15 CST 2015
     */
    public void setId(Byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}