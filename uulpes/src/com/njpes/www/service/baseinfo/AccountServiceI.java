package com.njpes.www.service.baseinfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.AccountOrgJob;

public interface AccountServiceI {

    /**
     * 检查用户名是否存在以及合法性
     */
    public boolean checkUser(String username);

    /**
     * 根据用户名称以及登陆类型查找用户信息
     * 
     * @param username
     * @param logintype
     * @return Account
     * @author 赵忠诚
     */
    public Account findByUsername(int dogid, String username, String logintype);

    /**
     * 根据用户名和密码登陆系统
     * 
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param logintype
     * @param dogid
     *            狗的id
     * @param dogresponse
     *            狗随机生成的密钥
     * @return account 实体
     * @author 赵忠诚
     */
    public Account login(String username, String password, String logintype, int dogid, String dogresponse);

    /**
     * 根据id查找用户信息
     * 
     * @param id
     *            用户id
     * @return account 实体
     * @author 赵忠诚
     */
    public Account findOne(Long id);

    public AccountDetail findAccountDetail(HttpServletRequest request);

    public void updateAccountDetail(String username, String trueName, String gender, String identifyCard,
            String organization, String superior, String personflag, int typeTableId);

    public String updateAccountPassword(HttpServletRequest request, String sourcePassword, String nowPassword,
            int dogid);

    /**
     * 获得某个单位的所有用户信息
     * 
     * @param orgid
     *            单位机构id
     * @param type_flag
     *            获取用户的类型，如果为null或者为空则获取全部内容
     * @return account列表
     * @author 赵忠诚
     */
    public List<Account> getAccountsByOrgid(Long orgid, int type_flag);

    /**
     * 插入基本信息到account
     * 
     * @author 赵忠诚
     */
    public int insert(Account user);

    /**
     * 插入数据到user_org_job
     * 
     * @author 赵忠诚
     */
    public int insertAccountOrgJob(List<AccountOrgJob> organizationJobs);

    /**
     * 更新基本信息到account
     * 
     * @author 赵忠诚
     */
    public int update(Account user);

    /**
     * 更新数据到user_org_job
     * 
     * @author 赵忠诚
     */
    public int updateAccountOrgJob(List<AccountOrgJob> organizationJobs);

    public AccountOrgJob selectAccountOrgJob(Long userId, Long orgId);

    /**
     * 获取用户的详细信息
     * 
     * @param id
     *            用户的唯一id
     * @param roleFlag
     *            角色标示
     * @return Object 如果是老师返回的是teacher,如果是学生返回student 在使用该接口时，利用instant of 判断
     */
    public Object getAccountInfo(long id, int roleFlag);

    public Object getAccountDetailInfo(long id, int roleFlag);

    public int deleteAccount(Long id);

    public int deleteStudentAccount(Long id);

    public int deleteParentAccount(Long id);

    public int deleteAccountOrgJob(Long userid);

    public String updateAccountUsername(HttpServletRequest request, String username, String password);

    public String checkPersonUserNameExist(HttpServletRequest request, String username);

    public String passwdreset(long id, String newpasswd);

}
