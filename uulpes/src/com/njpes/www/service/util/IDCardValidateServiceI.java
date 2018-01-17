package com.njpes.www.service.util;

import java.util.Date;

/**
 * 身份证校验类
 * 
 * @author 赵忠诚
 */
public interface IDCardValidateServiceI {

    public boolean isValidatedAllIdcard(String idcard);

    /**
     * 将15位的身份证转成18位身份证
     * 
     * @param idcard
     * @return
     */
    public String convertIdcarBy15bit(String idcard);

    public String getSex(String idcard);

    public Date getBirthDate(String idcard);

}
