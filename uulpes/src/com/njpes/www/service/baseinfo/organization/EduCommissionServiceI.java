package com.njpes.www.service.baseinfo.organization;

import java.util.List;

import com.njpes.www.entity.baseinfo.organization.Ecpsychicjob;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicteam;
import com.njpes.www.entity.baseinfo.organization.Educommission;
import com.njpes.www.utils.PageParameter;

/**
 * 教委接口信息
 * 
 * @author 赵忠诚
 */
public interface EduCommissionServiceI {

    public Educommission getECInfoByKeyId(Long keyid);

    /**
     * 根据组织机构编码获取学校管理机构(教委或者教育厅)的信息
     * 
     * @param orgid
     *            组织机构代码 为计算机识别码，即为Organization的主键id
     * @return {@link com.njpes.www.entity.baseinfo.organization.Educommission}
     * @author 赵忠诚
     */
    public Educommission getECInfoOrgID(Long orgid);

    /**
     * 根据组织机构id获取心理工作队伍信息
     * 
     * @param orgid
     *            组织机构代码 为计算机识别码，即为Organization的主键id
     * @return {@link com.njpes.www.entity.baseinfo.organization.Ecpsychicteam}
     * @author 赵忠诚
     */
    public List<Ecpsychicteam> getTeamsByOrgid(Long orgid);

    /**
     * 根据组织机构id获取心理工作内容
     * 
     * @param orgid
     *            组织机构代码 为计算机识别码，即为Organization的主键id
     * @return {@link com.njpes.www.entity.baseinfo.organization.Ecpsychicjob}
     * @author 赵忠诚
     */
    public List<Ecpsychicjob> getJobsByOrgid(Long orgid);

    /**
     * 更新机构信息
     * 
     * @param ec
     * @author 赵忠诚
     */
    public int updateEC(Educommission ec);

    /**
     * 新增机构信息
     * 
     * @param ec
     * @author 赵忠诚
     */
    public int insertEc(Educommission ec);

    public int delByKeyid(Long keyid);

    public int delByOrgid(Long orgid);

    public int delByEntity(Educommission ec);

    public List<Educommission> selectSubECsInWebQueryByPage(Educommission queryEc, PageParameter page);
}
