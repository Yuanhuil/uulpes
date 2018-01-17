package com.njpes.www.entity.baseinfo;

import java.io.Serializable;

public class AccountDetail implements Serializable {

    private int accountId;
    private String username;
    private String trueName;
    private String gender;
    private String identifyCard;
    private String organization;
    private String roleName;
    private String superior;
    // student，teacher或者parent
    private String personFlag;
    // 查询出来的表的id
    private int typeTableId;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentifyCard() {
        return identifyCard;
    }

    public void setIdentifyCard(String identifyCard) {
        this.identifyCard = identifyCard;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getPersonFlag() {
        return personFlag;
    }

    public void setPersonFlag(String personFlag) {
        this.personFlag = personFlag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTypeTableId() {
        return typeTableId;
    }

    public void setTypeTableId(int typeTableId) {
        this.typeTableId = typeTableId;
    }
}
