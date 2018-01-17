package com.njpes.www.exception.baseinfo;

public class UserPasswordRetryLimitExceedException extends UserException {
    public UserPasswordRetryLimitExceedException(int retryLimitCount) {
        super("user.password.retry.limit.exceed", new Object[] { retryLimitCount }, "尝试最大密码次数已过");
    }
}
