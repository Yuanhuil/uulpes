package com.njpes.www.service.baseinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.baseinfo.RoleResourcePermissionMapper;
import com.njpes.www.dao.baseinfo.UserResPermMapper;
import com.njpes.www.entity.baseinfo.UserResPerm;

import net.sf.json.JSONObject;

@Service("userResPermService")
public class UserResPermServiceImpl implements UserResPermServiceI {
    @Autowired
    private UserResPermMapper userResPermMapper;
    @Autowired
    private RoleResourcePermissionMapper roleResourcePermissionMapper;
    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    public List<UserResPerm> findResPermByUser(Long userId, String logintype) {
        return userResPermMapper.selectResPermsByUser(userId, logintype);
    }

    @Override
    public int deleteResPermByUserId(long accountId, String orgType) {
        return userResPermMapper.deleteResPermsByUserId(accountId, orgType);
    }

    @Override
    public int insertResPerm(UserResPerm userResPerm) {
        return userResPermMapper.insertSelective(userResPerm);
    }

    @Override
    public int updateResPermByUserId(UserResPerm userResPerm) {
        return userResPermMapper.updateByPrimaryKeySelective(userResPerm);
    }

    @Override
    public int updateResPermByUserId(List<UserResPerm> userResPerm) {
        return 0;
    }

    @Override
    public int insertResPerm(List<UserResPerm> userResPerm) {
        return userResPermMapper.insertList(userResPerm);
    }

    @Override
    public int update(long userid, String perm_resource, String orgtype) {
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        // 删除所有该角色的授权
        try {
            // 更新角色表
            userResPermMapper.deleteResPermsByUserId(userid, orgtype);
            // 对资源和操作进行分解
            JSONObject permResource = JSONObject.fromObject(perm_resource);
            List<UserResPerm> list = new ArrayList<UserResPerm>();
            if (perm_resource != null && perm_resource.length() > 0) {
                Iterator it = permResource.keys();
                while (it.hasNext()) {
                    String res = (String) it.next();
                    String permids = permResource.getString(res);
                    UserResPerm rrp = new UserResPerm();
                    rrp.setUserId(userid);
                    rrp.setResId(Long.parseLong(res));
                    rrp.setPermIds(permids);
                    rrp.setOrgtype(orgtype);
                    rrp.setAuthType("user");
                    list.add(rrp);
                }
                userResPermMapper.insertList(list);
            }
            txManager.commit(status); // 提交事务
            return 1;
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            return 0;
        }
    }
}
