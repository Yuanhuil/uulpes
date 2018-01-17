package com.njpes.www.exception.baseinfo;

public class UserPasswordNotMatchException extends UserException {

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null, "密码不匹配");
    }
}
