package com.njpes.www.entity.baseinfo;

public class DogEntity {

    private Integer dogid;
    private String vendorID = "";
    private String challenge;
    private String Factor;
    private String authCode;
    private String isdogid;

    public String getIsdogid() {
        return isdogid;
    }

    public void setIsdogid(String isdogid) {
        this.isdogid = isdogid;
    }

    public Integer getDogid() {
        return dogid;
    }

    public void setDogid(Integer dogid) {
        this.dogid = dogid;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getFactor() {
        return Factor;
    }

    public void setFactor(String factor) {
        Factor = factor;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

}
