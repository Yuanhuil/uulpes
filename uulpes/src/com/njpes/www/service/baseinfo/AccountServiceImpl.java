package com.njpes.www.service.baseinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.baseinfo.AccountMapper;
import com.njpes.www.dao.baseinfo.AccountOrgJobMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.BooleanStringEnum;
import com.njpes.www.exception.baseinfo.UserDogNotFoundException;
import com.njpes.www.exception.baseinfo.UserDogNotMatchException;
import com.njpes.www.exception.baseinfo.UserNotExistsException;
import com.njpes.www.exception.baseinfo.UserPasswordNotMatchException;
import com.njpes.www.utils.Md5Utils;
import com.njpes.www.utils.UserLogUtils;

@Service("accountService")
public class AccountServiceImpl implements AccountServiceI {
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private ParentServiceI parentService;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private EcUserServiceI ecUserService;

    @Autowired
    private AccountOrgJobMapper accountOrgJobMapper;

    @Override
    public Account findByUsername(int dogid, String username, String logintype) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        Account account = accountMapper.findByUsername(username, logintype);
        if (dogid > 1 && account == null) {
            account = accountMapper.findByDogid(dogid, logintype);
        }
        if (account == null) {
            UserLogUtils.log(username, "loginError", "user is not exists!");
            throw new UserNotExistsException();
        }
        if (StringUtils.isBlank(account.getRealname())) {
            account.setRealname("未知姓名");
        }
        return account;
    }

    @Override
    public Account login(String username, String password, String logintype, int dogid, String dogresponse) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            UserLogUtils.log(username, "loginError", "username is empty");
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 肯定错误
        if (password.length() < Account.PASSWORD_MIN_LENGTH || password.length() > Account.PASSWORD_MAX_LENGTH) {
            UserLogUtils.log(username, "loginError", "password length error! password is between {} and {}",
                    Account.PASSWORD_MIN_LENGTH, Account.PASSWORD_MAX_LENGTH);

            throw new UserPasswordNotMatchException();
        }

        Account user = null;

        user = findByUsername(dogid, username, logintype);
        if (user == null) {
            UserLogUtils.log(username, "loginError", "user is not exists!");
            throw new UserNotExistsException();
        }
        // 校验改用户是否是用狗登录的
        if (StringUtils.isNotBlank(user.getIsdoglongin())
                && StringUtils.equals(user.getIsdoglongin(), BooleanStringEnum.TRUE.getId()) && dogid == 1) {
            throw new UserDogNotFoundException();
        }
        // 检查狗的id是否与数据库记录的一致
        if (StringUtils.isNotBlank(user.getIsdoglongin())
                && StringUtils.equals(user.getIsdoglongin(), BooleanStringEnum.TRUE.getId())
                && dogid != user.getDogid()) {
            throw new UserDogNotMatchException();
        }

        passwordService.validate(user, password);

        UserLogUtils.log(username, "loginSuccess", "");
        return user;
    }

    @Override
    public Account findOne(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    // 获得用户的详细信息
    @Override
    public AccountDetail findAccountDetail(HttpServletRequest request) {
        AccountDetail accountDetail = new AccountDetail();
        // 从session中取出Account
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        int typeflag = account.getTypeFlag();
        int accountId = Integer.parseInt(account.getId().toString());
        if (typeflag == 1) {
            accountDetail = this.studentService.selectByAccountId(accountId);
            if (accountDetail != null) {
                accountDetail.setPersonFlag("student");
            }

        } else if (typeflag == 2) {
            // 根据accountid来进行扫描
            accountDetail = this.teacherService.selectByAccountId(accountId);
            if (accountDetail != null)
                accountDetail.setPersonFlag("teacher");
        } else if (typeflag == 3) {
            accountDetail = this.parentService.selectByAccountId(accountId);
            if (accountDetail != null)
                accountDetail.setPersonFlag("parent");
        }
        accountDetail.setUsername(account.getUsername());
        return accountDetail;
    }

    @Override
    public void updateAccountDetail(String username, String trueName, String gender, String identifyCard,
            String organization, String superior, String personflag, int typeTableId) {
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setTrueName(trueName);
        accountDetail.setGender(gender);
        accountDetail.setIdentifyCard(identifyCard);
        accountDetail.setTypeTableId(typeTableId);
        accountDetail.setOrganization(organization);
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if (personflag.equals("teacher")) {
                this.teacherService.updateByAccountId(accountDetail);
            } else if (personflag.equals("student")) {
                this.studentService.updateByAccountId(accountDetail);
            } else if (personflag.equals("parent")) {
                this.parentService.updateByAccountId(accountDetail);
            }
            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            txManager.rollback(status);
        }
    }

    @Override
    public String updateAccountPassword(HttpServletRequest request, String sourcePassword, String nowPassword,
            int dogid) {
        String result = "";
        // 从session中取出Account
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        if (StringUtils.isNotBlank(account.getIsdoglongin())
                && StringUtils.equals(account.getIsdoglongin(), BooleanStringEnum.TRUE.getId())
                && account.getDogid() != dogid) {
            return result = "needdog";
        }
        // 下面需要对用户的密码进行加密，然后再验证用户的密码正确性
        String passStr = passwordService.encryptPassword(account.getUsername(), sourcePassword);
        Account sourceAccount = this.accountMapper.selectByPrimaryKey(account.getId());
        String sourcePassStr = sourceAccount.getPassword();
        if (!passStr.equals(sourcePassStr)) {
            return result = "notEqual";
        } else {
            Account accountt = new Account();
            accountt.setId(account.getId());
            nowPassword = passwordService.encryptPassword(account.getUsername(), nowPassword);
            accountt.setPassword(nowPassword);
            accountt.setUpdateTime(new Date());
            this.accountMapper.updateByPrimaryKeySelective(accountt);
            if (dogid > 1) {// 表示加密狗用户修改密码
                result = "success_dog";
            } else {// 非加密狗用户修改密码
                result = "success_nodog";
            }
        }
        return result;
    }

    @Override
    public List<Account> getAccountsByOrgid(Long orgid, int type_flag) {
        return accountMapper.findUsersByOrgid(orgid, type_flag);
    }

    @Override
    public int insert(Account user) {
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordService.encryptPassword(user.getUsername(), user.getIdcard()));
        }
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            accountMapper.insertSelective(user);
            long maxid = user.getId();
            List<AccountOrgJob> l = new ArrayList<AccountOrgJob>();
            for (AccountOrgJob aoj : user.getOrganizationJobs()) {
                aoj.setUserId(maxid);
                l.add(aoj);
            }
            accountMapper.insertUserOrgJobSelective(l);
            txManager.commit(status);
            user.setId(maxid);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return 0;
        }
        return 1;

    }

    @Override
    public int insertAccountOrgJob(List<AccountOrgJob> organizationJobs) {
        return accountMapper.insertUserOrgJobSelective(organizationJobs);
    }

    @Override
    public int update(Account user) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            accountMapper.updateByPrimaryKeySelective(user);
            updateAccountOrgJob(user.getOrganizationJobs());
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return 0;
        }
        return 1;
    }

    @Override
    public int updateAccountOrgJob(List<AccountOrgJob> organizationJobs) {
        return accountOrgJobMapper.updateByPrimaryKeySelective(organizationJobs.get(0));
    }

    @Override
    public Object getAccountInfo(long id, int roleFlag) {
        if (AcountTypeFlag.student.getId() == (roleFlag)) {
            return studentService.getStudentByAccountId(id);
        } else if (AcountTypeFlag.teacher.getId() == (roleFlag)) {
            return teacherService.getTeacherByAccountId(id);
        } else if (AcountTypeFlag.ecuser.getId() == (roleFlag)) {
            return ecUserService.getEcUserByAccountId(id);
        } else if (AcountTypeFlag.parent.getId() == (roleFlag)) {
            return parentService.getParentByAccountId(id);
        }
        return null;
    }

    @Override
    public Object getAccountDetailInfo(long id, int roleFlag) {
        if (AcountTypeFlag.student.getId() == (roleFlag)) {
            return studentService.getStudentWithBlobByAccountId(id);
        } else if (AcountTypeFlag.teacher.getId() == (roleFlag)) {
            // return teacherService.getTeacherWithBlobByAccountId(id);
        } else if (AcountTypeFlag.ecuser.getId() == (roleFlag)) {
            // return ecUserService.getEcUserWithBlobByAccountId(id);
        }
        return null;
    }

    @Override
    public AccountOrgJob selectAccountOrgJob(Long userId, Long orgId) {
        return accountOrgJobMapper.selectByUserOrgId(userId, orgId);
    }

    @Override
    public int deleteAccount(Long id) {
        return accountMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteStudentAccount(Long id) {
        accountMapper.deleteByPrimaryKey(id);
        return accountMapper.deleteParentByPrimaryKey(id + 1);
    }

    @Override
    public int deleteParentAccount(Long id) {
        return accountMapper.deleteParentByPrimaryKey(id);
    }

    @Override
    public int deleteAccountOrgJob(Long userid) {
        return accountOrgJobMapper.deleteByUserId(userid);
    }

    @Override
    public String updateAccountUsername(HttpServletRequest request, String username, String password) {
        String result = "";
        String str = checkPersonUserNameExist(request, username);
        if (str.equals("true")) {
            return result = "exist";
        }

        // 从session中取出Account
        HttpSession httpSession = request.getSession();
        Account account = (Account) httpSession.getAttribute("user");
        // 下面需要对用户的密码进行加密，然后再验证用户的密码正确性
        String passStr = Md5Utils.hash(password);
        Account sourceAccount = this.accountMapper.selectByPrimaryKey(account.getId());
        String sourcePassStr = sourceAccount.getPassword();
        if (!passStr.equals(sourcePassStr)) {
            return result = "notEqual";
        } else {
            Account accountt = new Account();
            accountt.setId(account.getId());
            accountt.setUsername(username);
            ;
            this.accountMapper.updateByPrimaryKeySelective(accountt);
            result = "success";
        }
        return result;
    }

    @Override
    public String checkPersonUserNameExist(HttpServletRequest request, String username) {
        Account sourceAccount = this.accountMapper.findByUsername(username, "1");
        if (sourceAccount != null) {
            return "true";
        }
        return "false";
    }

    @Override
    public boolean checkUser(String username) {
        int r = accountMapper.checkUser(username);
        if (r >= 1)
            return true;
        return false;
    }

    @Override
    public String passwdreset(long id, String newpasswd) {
        if (accountMapper.resetPasswd(id, newpasswd) > 0)
            return "success";
        else
            return "fail";
    }

}
