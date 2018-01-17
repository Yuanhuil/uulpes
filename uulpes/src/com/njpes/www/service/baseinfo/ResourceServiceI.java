package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Menu;
import com.njpes.www.entity.baseinfo.Resource;

public interface ResourceServiceI {

    /**
     * 根据user确定能够访问的目录
     * 
     * @param user
     *            用户信息
     * @return List&ltMenu&gt {@link com.njpes.www.entity.baseinfo.Menu}
     * @author 赵忠诚
     */
    public List<Menu> findMenus(Account user);

    /**
     * 获得所有的资源目录
     * 
     * @author s
     * @return List&ltMenu&gt {@link com.njpes.www.entity.baseinfo.Menu}
     */
    public List<Menu> findAllMenus();

    /**
     * 查找用户所能访问的资源
     * 
     * @param user
     *            用户信息
     * @return 资源列表 {@link com.njpes.www.entity.baseinfo.Resource}
     * @author 赵忠诚
     */
    public List<Resource> findResource(Account user);

    /**
     * 根据唯一id标示找到对应资源信息
     * 
     * @param id
     * @return Resource
     * @author 赵忠诚
     */
    public Resource findResourceById(Long id);

    /**
     * 根据名称查找资源 like 查询操作
     * 
     * @param name
     * @return Resource列表
     * @author 赵忠诚
     */
    public List<Resource> findAllByName(String name);

    /**
     * 得到真实的资源标识 即 父亲:儿子
     * 
     * @param resource
     * @return
     * @author 赵忠诚
     */
    public String findActualResourceIdentity(Resource resource);

    /**
     * 保存资源信息
     * 
     * @param res
     *            需要保存的资源信息
     * @return 返回保存信息
     * @author 赵忠诚
     */
    public int save(Resource res);

    /**
     * 更新资源信息
     * 
     * @param res
     *            需要更新的资源信息
     * @return 返回保存信息
     * @author 赵忠诚
     */
    public int update(Resource res);

    /**
     * 移动节点
     * 
     * @param source
     *            源节点
     * @param target
     *            目标节点
     * @param moveType
     *            移动类型
     * @return 返回int
     * @author 赵忠诚
     */
    public void move(Resource source, Resource target, String moveType);

    /**
     * 删除自身以及孩子节点
     * 
     * @param m
     *            resource
     * @author 赵忠诚
     */
    public int deleteSelfAndChild(Resource m);

    /**
     * 删除所有节点以及相关孩子节点
     * 
     * @param mlist
     *            要删除的资源列表
     * @author 赵忠诚
     */
    public void deleteSelfAndChild(List<Resource> mList);

    /**
     * 查找资源
     * 
     * @param ids
     *            查找资源的Long数组
     * @return 资源列表
     * @author 赵忠诚
     */
    public List<Resource> findResourceS(Long[] ids);

    /**
     * 查询子子孙孙节点
     * 
     * @param ids
     *            查找资源的Long数组
     * @return 资源列表
     * @author 赵忠诚
     */
    public List<Resource> findChildren(List<Resource> resources);

    /**
     * 查询子子孙孙节点
     * 
     * @return 资源列表
     * @author 赵忠诚
     */
    public List<Resource> findAllWithSort();

    /**
     * 查询根节点以及孩子节点
     * 
     * @param id
     *            查找根节点的id
     * @return 资源列表
     * @author 赵忠诚
     */
    public List<Resource> findRootAndChild(Long id);

    /**
     * 自动计算排序号
     * 
     * @param parentid
     *            父亲节点id
     * @author 赵忠诚
     */
    public int findNextSortAuto(Long parentid);

    /**
     * 在父亲节点增加孩子节点
     * 
     * @param parent
     *            父亲节点
     * @param child
     *            孩子节点
     * @author 赵忠诚
     */
    public void appendChild(Resource parent, Resource child);

    public List<Menu> convertToMenus(List<Resource> resources);
}
