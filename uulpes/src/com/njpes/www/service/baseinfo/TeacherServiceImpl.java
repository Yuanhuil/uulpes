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
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.systeminfo.DogInfoMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Auth;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.BooleanStringEnum;
import com.njpes.www.entity.baseinfo.enums.ThemeEnum;
import com.njpes.www.entity.baseinfo.enums.UserState;
import com.njpes.www.entity.systeminfo.DogInfo;
import com.njpes.www.utils.PageParameter;
import com.njpes.www.utils.ReflectUtils;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherServiceI {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private AccountServiceI accountService;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private AuthServiceI authService;

    @Autowired
    private RoleServiceI roleService;

    @Autowired
    private DogInfoMapper dogInfoMapper;

    @Override
    public int deleteByPrimaryKey(Teacher teacher) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        teacher = teacherMapper.selectByPrimaryKey(teacher.getId());
        Long accountId = teacher.getAccountId();
        if (accountId == null || accountId <= 0) {
            return -1;
        }

        try {
            accountService.deleteAccount(accountId);
            accountService.deleteAccountOrgJob(accountId);
            teacherMapper.deleteByPrimaryKey(teacher.getId());
            authService.deleteByUserid(teacher.getAccountId());// 删除分配的角色
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    @Override
    public int deleteSelected(List<Teacher> teacherList) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            for (Teacher t : teacherList) {
                Teacher teacher = teacherMapper.selectByPrimaryKey(t.getId());
                Long accountId = teacher.getAccountId();
                accountService.deleteAccount(accountId);
                accountService.deleteAccountOrgJob(accountId);
                teacherMapper.deleteByPrimaryKey(teacher.getId());
                authService.deleteByUserid(teacher.getAccountId());// 删除分配的角色
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
    public int insert(TeacherQueryParam teacher, long orgid, long[] roleids) {
        Account user = new Account();
        DogInfo dog = new DogInfo();
        user.setIdcard(teacher.getSfzjh());
        user.setUsername(teacher.getSfzjh());
        user.setRealname(teacher.getXm());
        user.setTheme(ThemeEnum.theme1.getId());
        String birth = teacher.getSfzjh().substring(6, 14);
        // birth = AgeUitl.formatDate(birth);
        String bjxx = "101=" + teacher.getXbm();
        bjxx = bjxx + " 104=" + birth;
        teacher.setBjxx(bjxx);
        teacher.setCsrq(birth);
        if (roleids != null) {
            Long roleid = roleids[0];
            Role role = roleService.selectRole(roleid);
            if (Constants.isNeedDog(role)) {
                if (StringUtils.equals(teacher.getIsdogid(), BooleanStringEnum.TRUE.getId()))
                    user.setIsdoglongin(BooleanStringEnum.TRUE.getId());
            } else {
                user.setIsdoglongin(BooleanStringEnum.FALSE.getId());
            }
            if (StringUtils.equals(role.getIsadmin(), BooleanStringEnum.TRUE.getId())) {
                user.setAdmin(1);
            } else {
                user.setAdmin(0);
            }
        } else {
            user.setAdmin(0);
        }

        user.setState(UserState.normal.getIdentify());
        user.setTypeFlag(AcountTypeFlag.teacher.getId());
        user.setCreateTime(new Date());
        if (StringUtils.equals(teacher.getIsdogid(), BooleanStringEnum.TRUE.getId()) && teacher.getDogid() != null
                && teacher.getDogid().intValue() > 0) {
            user.setDogid(teacher.getDogid());
            dog.setDogid(teacher.getDogid());
            dog.setDogstatus(BooleanStringEnum.TRUE.getId());
        }
        user.setPassword(passwordService.encryptPassword(teacher.getSfzjh(), Constants.DEFAULT_PASSWORD));

        AccountOrgJob accountOrgJob = new AccountOrgJob();

        accountOrgJob.setOrgId(orgid);

        List<AccountOrgJob> userOrgList = new ArrayList<AccountOrgJob>();
        userOrgList.add(accountOrgJob);

        user.setOrganizationJobs(userOrgList);

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            accountService.insert(user);
            long accountMaxId = user.getId();
            teacher.setAccountId(accountMaxId);
            insert(teacher);
            if (roleids != null) {
                for (long roleid : roleids) {
                    Auth auth = new Auth();
                    auth.setOrgId(orgid);
                    auth.setUserId(accountMaxId);
                    auth.setRoleId(roleid);
                    auth.setAuthType("test");
                    authService.insert(auth);
                }
            }
            if (StringUtils.equals(teacher.getIsdogid(), BooleanStringEnum.TRUE.getId())) {
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
    public Teacher selectByPrimaryKey(Long id) {
        return this.teacherMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Teacher record) {
        return this.teacherMapper.updateByPrimaryKey(record);
    }

    @Override
    public AccountDetail selectByAccountId(int accountId) {
        return this.teacherMapper.selectByAccountId(accountId);
    }

    @Override
    public void updateByAccountId(AccountDetail accountDetail) {
        this.updateByAccountId(accountDetail);
    }

    @Override
    public List<Teacher> getTeachersByPage(TeacherQueryParam teacher, long schoolorgid, PageParameter page) {
        return teacherMapper.getTeachersByPage(teacher, schoolorgid, page, null);
    }

    @Override
    public int insert(TeacherWithBLOBs teacher) {
        return teacherMapper.insertSelective(teacher);
    }

    @Override
    public TeacherQueryParam selectTeacherInfoById(long id) {
        TeacherWithBLOBs teacher = teacherMapper.selectByPrimaryKey(id);
        TeacherQueryParam reTeacher = new TeacherQueryParam();
        ReflectUtils.fatherToChild(teacher, reTeacher);
        List<Auth> authList = teacher.getTeacherAuthList();
        String roleids = "";
        for (Auth a : authList) {
            roleids += a.getRoleId() + ",";
        }
        roleids = roleids.substring(0, roleids.lastIndexOf(","));
        reTeacher.setRoleId(roleids);
        return reTeacher;
    }

    @Override
    public int update(TeacherQueryParam teacher, long[] roleids) {
        DogInfo dog = new DogInfo();
        dog.setDogid(teacher.getDogid());

        Account user = accountService.findOne(teacher.getAccountId());
        user.setIdcard(teacher.getSfzjh());
        user.setRealname(teacher.getXm());
        user.setUpdateTime(new Date());
        if (roleids != null) {
            Long roleid = roleids[0];
            Role role = roleService.selectRole(roleid);
            if (Constants.isNeedDog(role)) {
                if (StringUtils.equals(teacher.getIsdogid(), BooleanStringEnum.TRUE.getId()))
                    user.setIsdoglongin(BooleanStringEnum.TRUE.getId());
            } else {
                user.setIsdoglongin(BooleanStringEnum.FALSE.getId());
            }
            if (StringUtils.equals(role.getIsadmin(), BooleanStringEnum.TRUE.getId())) {
                user.setAdmin(1);
            } else {
                user.setAdmin(0);
            }
        } else {
            user.setAdmin(0);
        }
        user.setUpdateTime(new Date());
        if (StringUtils.equals(teacher.getIsdogid(), BooleanStringEnum.TRUE.getId()) && teacher.getDogid() != null
                && teacher.getDogid().intValue() > 0) {
            user.setDogid(teacher.getDogid());
            dog.setDogid(teacher.getDogid());
            dog.setDogstatus(BooleanStringEnum.TRUE.getId());
        }
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            accountService.update(user);
            update(teacher);
            // 先删除再插入操作
            authService.deleteByUserid(teacher.getAccountId());
            if (roleids != null) {
                for (long roleid : roleids) {
                    Auth auth = new Auth();
                    auth.setOrgId(user.getOrganizationJobs().get(0).getOrgId());
                    auth.setUserId(teacher.getAccountId());
                    auth.setRoleId(roleid);
                    auth.setAuthType("test");
                    authService.insert(auth);
                }
            }
            if (StringUtils.equals(teacher.getIsdogid(), BooleanStringEnum.TRUE.getId()) && teacher.getDogid() != null
                    && teacher.getDogid().intValue() > 0) {
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
    public int update(TeacherWithBLOBs teacher) {
        return teacherMapper.updateByPrimaryKeySelective(teacher);
    }

    @Override
    public List<Teacher> getTeacherInSchool(long orgid, int roleFlag) {
        return teacherMapper.getTeachersInSchoolByType(orgid, roleFlag);
    }

    @Override
    public List<Teacher> getPsychologyTeacherInSchool(long orgid) {
        return teacherMapper.getPsychologyTeacherInSchool(orgid);
    }

    @Override
    public int updateBjxx(long id, String bjxx) {
        TeacherWithBLOBs teacher = new TeacherWithBLOBs();
        teacher.setId(id);
        teacher.setBjxx(bjxx);
        return teacherMapper.updateTeacherBjxx(teacher);
    }

    @Override
    public List<Teacher> getTeachers(TeacherQueryParam teacher, long schoolorgid, boolean isPage, PageParameter page,
            List<Long> excludesids) {
        if (!isPage) {
            return teacherMapper.getAllTeachers(teacher, schoolorgid, excludesids);
        }
        return teacherMapper.getTeachersByPage(teacher, schoolorgid, page, excludesids);
    }

    @Override
    public List<Teacher> getTeachersByIds(List<Long> ids) {
        return teacherMapper.getTeachersByids(ids);
    }

    @Override
    public Teacher getTeacherByAccountId(Long id) {
        return teacherMapper.getTeacherByAccountId(id);
    }

    @Override
    public List<Teacher> getAdminTeachersByPage(TeacherQueryParam teacher, long schoolorgid, long parentorgid,
            PageParameter page) {
        return teacherMapper.getAdminTeachersByPage(teacher, schoolorgid, parentorgid, page);
    }
}
