package com.njpes.www.exception.baseinfo;

public class UserNotExistsException extends UserException {

    public UserNotExistsException() {
        super("user.not.exists", null, "用户不存在");
    }
}
