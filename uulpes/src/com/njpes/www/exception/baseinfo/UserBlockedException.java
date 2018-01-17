package com.njpes.www.exception.baseinfo;

public class UserBlockedException extends UserException {
    public UserBlockedException(String reason) {
        super("user.blocked", new Object[] { reason }, "用户被切断");
    }
}
