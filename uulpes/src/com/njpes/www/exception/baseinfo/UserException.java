package com.njpes.www.exception.baseinfo;

import com.njpes.www.exception.BaseException;

public class UserException extends BaseException {

    public UserException(String code, Object[] args, String message) {
        super("user", code, args, message);
    }

}
