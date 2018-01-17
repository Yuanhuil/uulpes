package com.njpes.www.service.systeminfo;

import com.njpes.www.utils.RSAUtils;

public class LicenseServiceImpl implements LicenseServiceI {

    // 系统初始化的时候写入私钥
    private String privateKey = "";

    @Override
    public String decrypt(String license) throws Exception {
        // TODO Auto-generated method stub
        byte[] data = license.getBytes();
        byte[] decodedData = RSAUtils.decryptByPrivateKey(data, privateKey);
        String target = new String(decodedData);
        return target;
    }

}
