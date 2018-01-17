package com.njpes.www.entity.baseinfo;

public interface User {
    public Long getId();

    public int getFlag();

    public String getName();

    public void setName(String name);

    public String getUsername();

    public void setUsername(String username);

    public void setGender(String gender);

    public Byte getGender();

    public void setAccount(Account account);

    public Account getAccount();

    public String getRoleName();

    public boolean isIndividual();

    public boolean isAnonym();

    public String getGenderWord();

    public boolean isSetRole(int roleHolder);

    public String getBirthday();
}