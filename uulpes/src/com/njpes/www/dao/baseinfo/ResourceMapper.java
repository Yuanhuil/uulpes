package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.Resource;

public interface ResourceMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table resource
     *
     * @mbggenerated Sat Mar 21 22:53:46 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table resource
     *
     * @mbggenerated Sat Mar 21 22:53:46 CST 2015
     */
    int insert(Resource record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table resource
     *
     * @mbggenerated Sat Mar 21 22:53:46 CST 2015
     */
    int insertSelective(Resource record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table resource
     *
     * @mbggenerated Sat Mar 21 22:53:46 CST 2015
     */
    Resource selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table resource
     *
     * @mbggenerated Sat Mar 21 22:53:46 CST 2015
     */
    int updateByPrimaryKeySelective(Resource record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table resource
     *
     * @mbggenerated Sat Mar 21 22:53:46 CST 2015
     */
    int updateByPrimaryKey(Resource record);

    public List<Resource> findAll();

    public List<Resource> findChildrens(@Param("parentId") Long parent_id);

    public int findChildrensCnt(@Param("parentId") Long parent_id);

    /**
     * 删除本级以及孩子节点
     * 
     * @param id
     *            本级节点的主键
     * @param parentids
     *            父亲节点的路径
     * @return int 删除数据的数量
     * @author 赵忠诚
     */
    public int deleteSelfAndChild(@Param("id") Long id, @Param("parentids") String parentids);

    /**
     * 查找资源信息
     * 
     * @param ids
     *            需要查找id的数组
     * @return 返回资源列表
     * @author 赵忠诚
     */
    public List<Resource> findResourceInIds(@Param("ids") Long[] ids);

    /**
     * 查找资源信息
     * 
     * @param name
     *            需要查找name-
     * @return 返回资源列表
     * @author 赵忠诚
     */
    public List<Resource> findResourceByName(@Param("name") String name);

    /**
     * 查找根节点下的子孙节点，所以传入参数为根节点的parentids的前缀 如0/1
     * 
     * @param parentids
     * @return 返回资源列表
     * @author 赵忠诚
     */
    public List<Resource> findResourceByParentids(@Param("orQuery") String[] parentids);

    /**
     * 查找父亲节点下的所有直接孩子节点
     * 
     * @param parentid
     * @return 返回资源列表
     * @author 赵忠诚
     */
    public List<Resource> findResourceInParentId(@Param("parentid") List<Long> parentid);

    /**
     * 计算下一个插入资源的排序号
     * 
     * @param parentId
     *            父亲节点id
     * @return 下一个排序号
     * @author 赵忠诚
     */
    public int findnextSortAuto(@Param("parentId") Long parentId);

    /**
     * 查找自己以及兄弟节点信息
     * 
     * @param parentIds
     *            父亲节点的全路径
     * @param sort
     *            当前节点的顺序
     * @return 返回自己以及兄弟节点列表
     * @author 赵忠诚
     */
    public List<Resource> findSelfAndNextSiblings(@Param("parentIds") String parentIds, @Param("res_sort") int sort);

    /**
     * 更新自己一个孩子节点所属
     * 
     * @param newParentIds
     *            新父亲路径
     * @param oldParentIds
     *            旧父亲路径
     * @return int
     * @author 赵忠诚
     */
    public int updateSelftAndChild(@Param("newParentIds") String newParentIds,
            @Param("oldParentIds") String oldParentIds);
}