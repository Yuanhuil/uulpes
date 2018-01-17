package com.njpes.www.dao.baseinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;

public interface AccountMapper {

    int deleteByPrimaryKey(Long id);

    // 删除家长账号
    int deleteParentByPrimaryKey(Long id);

    int insert(Account record);

    int insertSelective(Account record);

    /**
     * sql语句不能执行事务，因此需要单独写这个方法
     * @author 赵忠诚
     */
    int insertUserOrgJobSelective(@Param("organizationJobs") List<AccountOrgJob> organizationJobs);

    Account selectByPrimaryKey(Long id);

    // 查询身份证、学籍号、用户名重复的学生
    List<Account> selectStudentByUniqueCol(Map param);

    // 查询身份证、工号、用户名重复的教师
    List<Account> selectTeacherByUniqueCol(Map param);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    /**
     * 根据用户名查找用户信息
     * @param username
     *            用户名 必须保证唯一
     * @return account结构体
     * @author 赵忠诚
     */
    public Account findByUsername(@Param("username") String username, @Param("logintype") String logintype);

    public Account findByDogid(@Param("dogid") int dogid, @Param("logintype") String logintype);

    /**
     * 根据身份证号查询用户信息
     * @param idcard
     *            有效的身份证信息
     * @return account结构体
     * @author 赵忠诚
     */
    public Account findUserByIdCard(@Param("idcard") String idcard);

    /**
     * 获得某个单位的所有用户信息
     * @param orgid
     *            单位机构id
     * @param type_flag
     *            获取用户的类型，如果为null或者为空则获取全部内容
     * @return account列表
     * @author 赵忠诚
     */
    public List<Account> findUsersByOrgid(@Param("orgid") Long orgid, @Param("flag") int type_flag);

    /**
     * 获取最大的id
     * @author 赵忠诚
     */
    public long getMaxId();

    public Map<String, String> getAllIds();

    void insertBatch(List<Account> accounts);

    void updateBatch(List<Account> accounts);

    public int checkUser(@Param("username") String username);

    public int resetPasswd(@Param("id") Long id, @Param("passwd") String passwd);

    /**
     * 查找系统中是否存在该身份证的用户
     * @Author zhangj
     * @Version 4.0
     * @param cardNo
     * @return int
     * @Date 2017年3月29日 上午11:52:47
     */
    int selectUserByUniqueCol(@Param("cardNo") String cardNo);
}