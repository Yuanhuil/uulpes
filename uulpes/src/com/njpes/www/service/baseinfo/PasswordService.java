package com.njpes.www.service.baseinfo;

import org.springframework.stereotype.Service;

import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.exception.baseinfo.UserPasswordNotMatchException;
import com.njpes.www.utils.Md5Utils;
import com.njpes.www.utils.UserLogUtils;

@Service("passwordService")
public class PasswordService {

    /*
     * @Autowired private CacheManager ehcacheManager;
     */

    /* private Cache loginRecordCache; */

    // @Value(value = "${user.password.maxRetryCount}")
    // private int maxRetryCount = 10;

    /*
     * public void setMaxRetryCount(int maxRetryCount) { this.maxRetryCount =
     * maxRetryCount; }
     */

    /*
     * @PostConstruct public void init() { loginRecordCache =
     * ehcacheManager.getCache("loginRecordCache"); }
     */

    public void validate(Account account, String password) {
        String username = account.getUsername();

        int retryCount = 0;

        /*
         * Element cacheElement = loginRecordCache.get(username); if
         * (cacheElement != null) { retryCount = (Integer)
         * cacheElement.getObjectValue(); if (retryCount >= maxRetryCount) {
         * UserLogUtils.log( username, "passwordError",
         * "password error, retry limit exceed! password: {},max retry count {}"
         * , password, maxRetryCount); throw new
         * UserPasswordRetryLimitExceedException(maxRetryCount); } }
         */

        if (!matches(account, password)) {
            // loginRecordCache.put(new Element(username, ++retryCount));
            UserLogUtils.log(username, "passwordError", "password error! password: {} retry count: {}", password,
                    retryCount);
            throw new UserPasswordNotMatchException();
        } else {
            // clearLoginRecordCache(username);
        }
    }

    public boolean matches(Account account, String newPassword) {
        return account.getPassword().equals(encryptPassword(account.getUsername(), newPassword));
    }

    // public void clearLoginRecordCache(String username) {
    // loginRecordCache.remove(username);
    // }

    public String encryptPassword(String username, String password) {
        return Md5Utils.hash(password);
    }

    public static void main(String[] a) {
        System.out.println(Md5Utils.hash("admin123"));
    }
}
