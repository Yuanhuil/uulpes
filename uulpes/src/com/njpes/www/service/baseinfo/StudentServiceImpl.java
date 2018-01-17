package com.njpes.www.service.baseinfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.baseinfo.AccountMapper;
import com.njpes.www.dao.baseinfo.AccountOrgJobMapper;
import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.ParentMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.util.SequenceMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.enums.ThemeEnum;
import com.njpes.www.entity.baseinfo.enums.UserState;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.utils.PageParameter;

@Service("studentService")
public class StudentServiceImpl implements StudentServiceI {
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private AuthServiceI authService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ParentMapper parentMapper;
    @Autowired
    AccountOrgJobMapper aojMapper;

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private ClassSchoolMapper classSchoolMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    SequenceMapper sequenceMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insertSelective(Student record) {
        return 0;
    }

    @Override
    public Student selectByPrimaryKey(Long id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public StudentWithBLOBs selectStudentInfoById(long id) {
        StudentWithBLOBs student = studentMapper.selectByPrimaryKey(id);
        return student;
    }

    @Override
    public StudentWithBLOBs selectStudentWithBlobInfoById(long id) {
        StudentWithBLOBs student = studentMapper.selectStudentWithBlobInfoById(id);
        return student;
    }

    @Override
    public int deleteByPrimaryKey(Student student) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        student = studentMapper.selectByPrimaryKey(student.getId());
        Long accountId = student.getAccountId();
        if (accountId == null || accountId <= 0) {
            return -1;
        }

        try {
            accountService.deleteAccount(accountId);
            // accountService.deleteStudentAccount(accountId);
            int n = accountService.deleteParentAccount(accountId + 1);// 删除家长账号
            // accountService.deleteAccountOrgJob(accountId);
            studentMapper.deleteByPrimaryKey(student.getId());
            parentMapper.deleteByStudentAccountid(accountId);
            // authService.deleteByUserid(student.getAccountId());//删除分配的角色.学生在auth表里面不记录
            aojMapper.deleteByUserId(accountId);
            if (n == 1)
                aojMapper.deleteByUserId(accountId + 1);
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    @Override
    public int deleteSelected(List<Student> studentList) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            for (Student s : studentList) {
                Student student = studentMapper.selectByPrimaryKey(s.getId());
                Long accountId = student.getAccountId();
                if (accountId == null || accountId <= 0) {
                    return -1;
                }

                accountService.deleteAccount(accountId);
                // accountService.deleteStudentAccount(accountId);
                int n = accountService.deleteParentAccount(accountId + 1);// 删除家长账号
                // accountService.deleteAccountOrgJob(accountId);
                studentMapper.deleteByPrimaryKey(student.getId());
                parentMapper.deleteByStudentAccountid(accountId);
                // authService.deleteByUserid(student.getAccountId());//删除分配的角色.学生在auth表里面不记录
                aojMapper.deleteByUserId(accountId);
                if (n == 1)
                    aojMapper.deleteByUserId(accountId + 1);
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
    public int update(StudentWithBLOBs student) {
        Account user = accountService.findOne(student.getAccountId());
        user.setIdcard(student.getSfzjh());
        // user.setUsername(student.getXh());
        user.setRealname(student.getXm());
        user.setUpdateTime(new Date());
        user.setTypeFlag(AcountTypeFlag.student.getId());

        /*
         * StudentWithBLOBs stu = new StudentWithBLOBs();
         * stu.setId(student.getId()); stu.setXm(student.getXm());
         * if(student.getClassid()!=null&&!student.getClassid().equals("")){
         * stu.setBjid(Long.parseLong(student.getClassid())); }
         * stu.setCsrq(student.getCsrq()); stu.setXh(student.getXh());
         * stu.setXbm(student.getXbm());
         * stu.setAccountId(student.getAccountId()); stu.setBjxx(s);
         */
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // accountService.update(user);
            accountMapper.updateByPrimaryKeySelective(user);
            studentMapper.updateByPrimaryKeySelective(student);
            // update(student);
            // 先删除再插入操作
            /*
             * authService.deleteByUserid(student.getAccountId());
             * 
             * Auth auth = new Auth();
             * auth.setOrgId(user.getOrganizationJobs().get(0).getOrgId());
             * auth.setUserId(student.getAccountId()); auth.setRoleId((long)39);
             * auth.setAuthType("test"); authService.insert(auth);
             */
            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    @Override
    public int updateByPrimaryKeySelective(Student record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(StudentWithBLOBs record) {
        return studentMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Student record) {
        return this.studentMapper.updateByPrimaryKey(record);
    }

    @Override
    public AccountDetail selectByAccountId(int accountId) {
        return this.studentMapper.selectByAccountId(accountId);
    }

    @Override
    public void updateByAccountId(AccountDetail accountDetail) {
        this.studentMapper.updateByAccountId(accountDetail);
    }

    @Override
    public List<StudentWithBLOBs> getStudentsByPage(Student stu, long schoolorgid, PageParameter page, int flozenflag) {
        return getStudents(schoolorgid, stu, true, page, null, flozenflag);
    }

    @Override
    public List<StudentWithBLOBs> getAllStudentsInClass(Student stu, long schoolorgid, int flozenflag) {
        return getStudents(schoolorgid, stu, false, null, null, flozenflag);
    }

    @Override
    public List<StudentWithBLOBs> getAllStudentsInCounty(Student stu, String countyid, int flozenflag) {
        return studentMapper.getStudentsInCounty(stu, countyid, flozenflag);
    }

    @Override
    public List<StudentWithBLOBs> getAllStudentsInCity(Student stu, String cityid, String xzxs, int flozenflag) {
        return studentMapper.getStudentsInCity(stu, cityid, xzxs, flozenflag);
    }

    @Override
    public List<StudentWithBLOBs> getStudents(long schoolorgid, Student stu, boolean isPage, PageParameter page,
            List<Long> excludesids, int flozenflag) {
        if (flozenflag == 1)
            return studentMapper.getGraduatedStudentsByPage(stu, schoolorgid, page, excludesids, flozenflag);
        if (!isPage) {
            return studentMapper.getStudentsAll(stu, schoolorgid, excludesids, flozenflag);
        }
        return studentMapper.getStudentsByPage(stu, schoolorgid, page, excludesids, flozenflag);
    }

    public List<Student> getStudentRecordAll(Student stu, long schoolorgid, int flozenflag){
        return studentMapper.getStudentsRecordAll(stu, schoolorgid, flozenflag);
    }

    @Override
    public List<StudentWithBLOBs> getStudentsByPage(Student stu, long schoolorgid, PageParameter page,
            List<Long> excludesids, int flozenflag) {
        return getStudents(schoolorgid, stu, true, page, excludesids, flozenflag);
    }

    @Override
    public List<StudentWithBLOBs> getStudentsByIds(List<Long> ids) {
        return studentMapper.getStudentsByids(ids);
    }

    @Override
    public StudentWithBLOBs getStudentById(long id) {
        if (id <= 0)
            return null;
        StudentWithBLOBs s = studentMapper.getStudentByid(id);
        return s;
    }

    @Override
    public Student getStudentByAccountId(Long id) {
        return studentMapper.getStudentByAccountId(id);
    }

    @Override
    public Student getStudentBySfzjh(String sfzjh) {
        return studentMapper.getStudentBySfzjh(sfzjh);
    }

    @Override
    public StudentWithBLOBs getStudentWithBlobByAccountId(long id) {
        return studentMapper.getStudentByAccountId(id);
    }

    @Override
    public int insert(StudentWithBLOBs student) {
        long bjid = -1;
        String classid = student.getClassid();
        if (classid != null) {
            bjid = Integer.parseInt(classid);
            student.setBjid(bjid);
            ClassSchool cs = classSchoolMapper.selectByPrimaryKey(bjid);
            student.setBh(cs.getBh());
        }
        String birth = student.getSfzjh().substring(6, 14);
        String bjxx = "101=" + student.getXbm();
        bjxx = bjxx + " 104=" + birth;
        student.setBjxx(bjxx);
        student.setCsrq(birth);
        // Long accid = accountMapper.getMaxId() + 1;
        // sequenceMapper.updateSeqId(accid, "account");

        Account user = new Account();
        // user.setId(accid);
        user.setIdcard(student.getSfzjh());
        user.setUsername(student.getSfzjh());
        user.setRealname(student.getXm());
        user.setTheme(ThemeEnum.theme1.getId());
        user.setAdmin(0);
        user.setState(UserState.normal.getIdentify());
        user.setTypeFlag(AcountTypeFlag.student.getId());
        user.setCreateTime(new Date());
        user.setPassword(passwordService.encryptPassword(student.getXm(), Constants.DEFAULT_PASSWORD));

        // 创建家长用户，写account表，返回id，写入student的account_id
        Account parent_record = new Account();
        // parent_record.setId(accid+1); //编号
        parent_record.setUsername(user.getUsername() + "fm");
        // parent_record.setPassword("21232f297a57a5a743894a0e4a801fc3");
        // //密码，默认admin
        parent_record
                .setPassword(passwordService.encryptPassword(user.getUsername() + "fm", Constants.DEFAULT_PASSWORD));
        parent_record.setRealname(student.getXm() + "家长");
        parent_record.setCreateTime(Calendar.getInstance().getTime()); // 时间
        parent_record.setUpdateTime(Calendar.getInstance().getTime());
        parent_record.setTypeFlag(3); // 类型是家长
        parent_record.setState(1); // 正常状态
        parent_record.setAdmin(0);
        parent_record.setTheme("theme1");

        // 写parent表
        Parent parent = new Parent();
        parent.setCyxm(student.getXm() + "家长");
        parent.setGxm("51");

        AccountOrgJob accountOrgJob = new AccountOrgJob();
        accountOrgJob.setOrgId(student.getOrgid());
        List<AccountOrgJob> userOrgList = new ArrayList<AccountOrgJob>();
        userOrgList.add(accountOrgJob);
        user.setOrganizationJobs(userOrgList);

        // 写parentd user_org_job表，写学生学校映射关系
        AccountOrgJob parent_aoj = new AccountOrgJob();
        parent_aoj.setOrgId(student.getOrgid());
        List<AccountOrgJob> p_userOrgList = new ArrayList<AccountOrgJob>();
        p_userOrgList.add(parent_aoj);
        parent_record.setOrganizationJobs(p_userOrgList);

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            accountService.insert(user);
            long accountMaxId = user.getId();
            student.setAccountId(accountMaxId);
            studentMapper.insertSelective(student);

            accountService.insert(parent_record);
            parent.setAccountId(accountMaxId + 1);
            parent.setStudentAccountId(accountMaxId);
            parentMapper.insertSelective(parent);

            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return -1;
        }

        return 1;
    }

    /**
     * 根据存储的grade和该学校的学制获取真实的gradeid
     */
    public int getRealGradeId(Long orgid, int gradecodeid) {
        // School school = schoolMapper.selectSchoolInfoByOrgId(orgid);
        if (gradecodeid <= 6) {
            return gradecodeid;
        }
        if (gradecodeid >= 11) {
            return gradecodeid - 1;
        }
        School school = schoolMapper.selectSchoolInfoByOrgId(orgid);
        if (school.getXxxz() != null) {
            if (school.getXxxz().intValue() == 5) {
                return gradecodeid <= 5 ? gradecodeid : gradecodeid - 1;
            }
            return gradecodeid;
        }
        return 0;
    }

    @Override
    public int updateBjxx(long id, String bjxx) {
        StudentWithBLOBs student = new StudentWithBLOBs();
        student.setId(id);
        student.setBjxx(bjxx);
        return studentMapper.updateStudentBjxx(student);
    }

    @Override
    public int updateBj(long id, String bjid) {
        StudentWithBLOBs student = new StudentWithBLOBs();
        student.setId(id);
        student.setBjid(Long.parseLong(bjid));
        return studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public int changeClass(List<Student> studentList, int newclassid) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            for (Student s : studentList) {
                studentMapper.changeClass(s.getId(), newclassid);
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
    public boolean getStudentsXjh(long schoolorgid,String xjh){
        int r = studentMapper.getStudentsXjh(schoolorgid,xjh);
        if (r >= 1)
            return true;
        return false;
    }

	@Override
	public int getStudentCountByClassid(Long id) {
		// TODO Auto-generated method stub
		List<Student> studentsByClassId = studentMapper.getStudentsByClassId(id);
		return studentsByClassId.size();
	}
}
