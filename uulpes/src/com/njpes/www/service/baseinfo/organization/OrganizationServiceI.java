package com.njpes.www.service.baseinfo.organization;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.utils.PageParameter;

/**
 * 组织机构服务
 * 
 * @author 赵忠诚
 */
public interface OrganizationServiceI {
    public Set<Long> findAncestorIds(Iterable<Long> currentIds);

    public Set<Long> findAncestorIds(Long currentId);

    public List<Organization> selectAllOrganizations();

    /**
     * 获取当前节点的父亲节点
     * 
     * @author 赵忠诚
     */
    public Organization findParent(Long currentId);

    /**
     * 根据组织机构的id查询组织机构信息
     * 
     * @param id
     *            组织机构id
     * @return 返回组织机构详细信息
     * @author 赵忠诚
     */
    public Organization selectOrganizationById(Long id);

    /**
     * 根据组织机构id更新组织机构信息
     * 
     * @param id
     *            组织机构id
     * @return 更新值
     * @author 赵忠诚
     */
    public int updateOrganization(Organization org);

    /**
     * 插入组织机构
     * 
     * @author 赵忠诚
     */
    public Organization insertOrganization(Organization org);

    /**
     * 根据页面查询的查询条件查询孩子节点的信息
     * 
     * @param 组织机构查询信息,页面传到后台的变量组成的实体，目的是为了方便操作
     * @param page
     *            分页信息 {@link com.njpes.www.utils.PageParameter}
     * @return 所有查询列表
     * @author 赵忠诚
     */
    public List<Organization> selectSubOrgsInWebQuery(Organization org, PageParameter page);

    /**
     * 根据组织机构的信息查找孩子节点信息
     * 
     * @param orgid
     *            组织机构计算机识别码
     * @param page
     *            分页信息 {@link com.njpes.www.utils.PageParameter}
     * @return 所有查询列表
     * @author 赵忠诚
     */
    public List<Organization> selectSubOrgsByOrgid(Long orgid, PageParameter page);

    /**
     * 保存组织机构信息
     * 
     * @param 组织机构查询信息
     * @return 返回保存数据个数
     * @author 赵忠诚
     */
    public int saveOrg(Organization org);

    /**
     * 保存组织机构信息
     * 
     * @param 组织机构查询信息
     * @return 返回保存数据个数
     * @author 赵忠诚
     */
    public int delOrg(Organization org);

    /**
     * 查看当前用户组织机构级别以下的级别
     * 
     * @param org
     *            当前组织机构信息对象
     * @param includemyself
     *            是否包含自己
     * @param includeschoollevel
     *            是否包含学校级别
     * @return 组织机构级别list
     *         {@link com.njpes.www.entity.baseinfo.organization.OrganizationLevel}
     * @author 赵忠诚
     */
    public List<OrganizationLevel> selectSubOrgLevel(Organization org, boolean includemyself,
            boolean includeschoollevel);

    /**
     * 查看当前用户组织机构级别以下的直接级别
     * 
     * @param org
     *            当前组织机构信息对象
     * @param includemyself
     *            是否包含自己
     * @param includeschoollevel
     *            是否包含学校级别
     * @return 组织机构级别list
     *         {@link com.njpes.www.entity.baseinfo.organization.OrganizationLevel}
     * @author 赵忠诚
     */
    public List<OrganizationLevel> selectDirectSubOrgLevel(Organization org, boolean includemyself,
            boolean includeschoollevel);

    /**
     * 获取组织机构信息下的所有下属机构信息，组织机构按照教育管理部门和学校两个划分
     * 
     * @param org
     *            当前组织机构信息对象
     * @return hashMap
     * @author 赵忠诚
     */
    public HashMap<OrganizationType, List<Organization>> getAllSubOrgs(Organization org);

    /**
     * 获取组织机构信息下的直接儿子节点
     * 
     * @param org
     *            当前组织机构信息对象
     * @return hashMap
     * @author 赵忠诚
     */
    public HashMap<OrganizationType, List<Organization>> getSonSubOrgs(Organization org);

    /**
     * 获取组织机构信息下的直接儿子节点
     * 
     * @param org
     *            当前组织机构信息对象
     * @return list
     * @author 赵忠诚
     */
    // 获取区县教委以及区县教委所属学校
    public List<Organization> getQuxianAndSchoolsOrgs(Organization org);

    public List<Organization> getSonSubOrgsList(Organization org);

    /**
     * 获取组织机构信息下的直接儿子节点
     * 
     * @param org
     *            当前组织机构信息对象
     * @param orglevel
     *            组织机构级别
     * @return list
     * @author 赵忠诚
     */
    public List<Organization> getSonSubOrgsList(Organization org, int orglevel);

    /**
     * 获得直属学校
     * 
     * @param org
     * @return
     */
    public List<Organization> getSonSchoolList(Organization org);

    /**
     * 获得行政区的教委机构
     * 
     * @param org
     * @return
     * @author 赵万锋
     */
    public Organization getEduOrganizationByCountyId(String areaid);

    public List<Organization> getEduOrganizationByCityId(String areaid);

    public List<Organization> getSchoolOrgByTownId(String areaid);

    /**
     * 根据区县id获得学校
     * 
     * @author s
     * @param countyid
     * @return
     */
    public List<Organization> getSchoolOrgByCountyId(String countyid);

    /**
     * 根据市id获得学校
     * 
     * @author s
     * @param cityid
     * @return
     */
    public List<Organization> getSchoolOrgByCityId(String cityid);

    /**
     * 根据城市获得区县的列表
     * 
     * @author s
     */
    public List<Organization> getCountiesByCity(String cityid);

    /**
     * 根据区县获得乡镇的列表
     * 
     * @author s
     */
    public List<Organization> getTownsByCounty(String countyid);

    public List<Organization> getSchoolOrgByCountyIds();

    public List<Organization> getSchoolOrgByTownIds();

    public List<Organization> getAllSchool(Organization org);

    public List<Organization> getAllEcs(Organization org);

    public int updateOrgLocked(Long orgid, String locked);
}
