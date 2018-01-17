package com.njpes.www.exception.baseinfo;

public class UserDogNotFoundException extends UserException {

    public UserDogNotFoundException() {
        super("user.not.FoundDog", null, "用户需要加密狗登录");
    }
}
