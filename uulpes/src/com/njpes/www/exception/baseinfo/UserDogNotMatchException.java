package com.njpes.www.exception.baseinfo;

public class UserDogNotMatchException extends UserException {

    public UserDogNotMatchException() {
        super("user.not.MatchDog", null, "请插入正确的加密狗");
    }
}
