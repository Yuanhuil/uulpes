package com.njpes.www.service.baseinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.EcUserMapper;
import com.njpes.www.dao.systeminfo.DogInfoMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.EcUserWithBLOBs;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.BooleanStringEnum;
import com.njpes.www.entity.baseinfo.enums.ThemeEnum;
import com.njpes.www.entity.baseinfo.enums.UserState;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.systeminfo.DogInfo;
import com.njpes.www.utils.PageParameter;

@Service("ecuserService")
public class EcUserServiceImpl implements EcUserServiceI {

    @Autowired
    private EcUserMapper ecUserMapper;

    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private AuthServiceI authService;

    @Autowired
    private DogInfoMapper dogInfoMapper;

    @Autowired
    private RoleServiceI roleService;

    @Override
    public int deleteByPrimaryKey(EcUserWithBLOBs ecuser) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        ecuser = ecUserMapper.selectByPrimaryKey(ecuser.getId());
        Long accountId = ecuser.getAccountId();
        if (accountId == null || accountId <= 0) {
            return -1;
        }

        try {
            accountService.deleteAccount(accountId);
            accountService.deleteAccountOrgJob(accountId);
            authService.deleteByUserid(accountId);// 删除分配的角色
            ecUserMapper.deleteByPrimaryKey(ecuser.getId());
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    @Override
    public int deleteSelected(List<EcUserWithBLOBs> ecuserList) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            for (EcUserWithBLOBs ec : ecuserList) {
                EcUserWithBLOBs ecuser = ecUserMapper.selectByPrimaryKey(ec.getId());
                Long accountId = ecuser.getAccountId();
                if (accountId == null || accountId <= 0) {
                    return -1;
                }
                accountService.deleteAccount(accountId);
                accountService.deleteAccountOrgJob(accountId);
                authService.deleteByUserid(accountId);// 删除分配的角色
                ecUserMapper.deleteByPrimaryKey(ecuser.getId());
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int insert(EcUserWithBLOBs ecuser) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);

        DogInfo dog = new DogInfo();
        Account user = new Account();
        user.setUsername(ecuser.getSfzjh());
        user.setIdcard(ecuser.getSfzjh());
        user.setTypeFlag(AcountTypeFlag.ecuser.getId());
        user.setRealname(ecuser.getXm());
        user.setTheme(ThemeEnum.theme1.getId());
        user.setState(UserState.normal.getIdentify());
        Long roleid = ecuser.getRoleid();
        Role role = roleService.selectRole(roleid);
        if (Constants.isNeedDog(role)) {
            // 是否用加密狗选择是
            if (StringUtils.equals(ecuser.getIsdogid(), BooleanStringEnum.TRUE.getId()))
                user.setIsdoglongin(BooleanStringEnum.TRUE.getId());
        } else {
            user.setIsdoglongin(BooleanStringEnum.FALSE.getId());
        }
        if (StringUtils.equals(role.getIsadmin(), BooleanStringEnum.TRUE.getId())) {
            user.setAdmin(1);
        } else {
            user.setAdmin(0);
        }
        user.setCreateTime(new Date());
        if (StringUtils.equals(ecuser.getIsdogid(), BooleanStringEnum.TRUE.getId()) && ecuser.getDogid() != null
                && ecuser.getDogid().intValue() > 0) {
            user.setDogid(ecuser.getDogid());
            dog.setDogid(ecuser.getDogid());
            dog.setDogstatus(BooleanStringEnum.TRUE.getId());
        }

        user.setPassword(passwordService.encryptPassword(ecuser.getSfzjh(), Constants.DEFAULT_PASSWORD));
        AccountOrgJob aoj = new AccountOrgJob();
        aoj.setOrgId(ecuser.getOrgid());
        List<AccountOrgJob> l = new ArrayList<AccountOrgJob>();
        l.add(aoj);
        user.setOrganizationJobs(l);
        try {
            accountService.insert(user);
            ecuser.setAccountId(user.getId());
            ecUserMapper.insertSelective(ecuser);
            Auth auth = new Auth();
            auth.setOrgId(ecuser.getOrgid());
            auth.setUserId(user.getId());
            auth.setRoleId(ecuser.getRoleid());
            auth.setAuthType("test");
            authService.insert(auth);
            if (StringUtils.equals(ecuser.getIsdogid(), BooleanStringEnum.TRUE.getId())) {
                dogInfoMapper.insertSelective(dog);
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    @Override
    public int update(EcUserWithBLOBs ecuser) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        Long accountId = ecuser.getAccountId();
        if (accountId == null || accountId <= 0) {
            return -1;
        }
        DogInfo dog = new DogInfo();
        dog.setDogid(ecuser.getDogid());

        Account user = accountService.findOne(accountId);
        user.setIdcard(ecuser.getSfzjh());
        user.setUpdateTime(new Date());
        if (StringUtils.equals(ecuser.getIsdogid(), BooleanStringEnum.TRUE.getId()) && ecuser.getDogid() != null
                && ecuser.getDogid().intValue() > 0) {
            user.setDogid(ecuser.getDogid());
            dog.setDogid(ecuser.getDogid());
            dog.setDogstatus(BooleanStringEnum.TRUE.getId());
        }
        Long roleid = ecuser.getRoleid();
        Role role = roleService.selectRole(roleid);
        if (Constants.isNeedDog(role)) {
            if (StringUtils.equals(ecuser.getIsdogid(), BooleanStringEnum.TRUE.getId()))
                user.setIsdoglongin(BooleanStringEnum.TRUE.getId());
        } else {
            user.setIsdoglongin(BooleanStringEnum.FALSE.getId());
        }
        if (StringUtils.equals(role.getIsadmin(), BooleanStringEnum.TRUE.getId())) {
            user.setAdmin(1);
        } else {
            user.setAdmin(0);
        }
        AccountOrgJob aoj = accountService.selectAccountOrgJob(accountId, null);
        aoj.setOrgId(ecuser.getOrgid());
        List<AccountOrgJob> l = new ArrayList<AccountOrgJob>();
        l.add(aoj);
        user.setOrganizationJobs(l);
        try {
            accountService.update(user);
            ecUserMapper.updateByPrimaryKeySelective(ecuser);
            authService.deleteByUserid(ecuser.getAccountId());
            Auth auth = new Auth();
            auth.setOrgId(user.getOrganizationJobs().get(0).getOrgId());
            auth.setUserId(ecuser.getAccountId());
            auth.setRoleId(ecuser.getRoleid());
            auth.setAuthType("test");
            authService.insert(auth);
            if (StringUtils.equals(ecuser.getIsdogid(), BooleanStringEnum.TRUE.getId()) && ecuser.getDogid() != null
                    && ecuser.getDogid().intValue() > 0) {
                dogInfoMapper.insertSelective(dog);
            }
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    @Override
    public List<EcUserWithBLOBs> getEcUsersByParentOrgIdByPage(EcUserWithBLOBs ecuser, Long parentorgid,
            Organization queryOrgEntity, PageParameter page) {
        return ecUserMapper.selectEcUsersByParentOrgidByPage(ecuser, parentorgid, queryOrgEntity, page);
    }

    @Override
    public EcUserWithBLOBs selectById(Long id) {
        return ecUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public EcUserWithBLOBs getEcUserByAccountId(Long id) {
        // TODO Auto-generated method stub
        return ecUserMapper.selectByAccountId(id);
    }

}
