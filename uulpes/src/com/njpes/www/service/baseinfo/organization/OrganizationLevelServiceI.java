package com.njpes.www.service.baseinfo.organization;

import java.util.List;

import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;

public interface OrganizationLevelServiceI {

    /**
     * 查询组织机构级别list
     * 
     * @author 赵忠诚
     */
    public List<OrganizationLevel> queryOrgLevelList();

    /**
     * 查询当前级别以下的组织机构级别list,默认包含学校层级
     * 
     * @param currentLevel
     *            当前组织机构级别
     * @param includemyself
     *            是否包含自己
     * @param includeschoollevel
     *            是否包含学校级别
     * @author 赵忠诚
     */
    public List<OrganizationLevel> queryOrgLevelList(Integer currentLevel, boolean includemyself,
            boolean includeschoollevel);

    /**
     * 查询当前级别以下的组织机构级别list,默认包含学校层级
     * 
     * @param currentLevel
     *            当前组织机构级别
     * @param includemyself
     *            是否包含自己
     * @param includeschoollevel
     *            是否包含学校级别
     * @author 赵忠诚
     */
    public List<OrganizationLevel> queryOrgDirectLevelList(Integer currentLevel, boolean includemyself,
            boolean includeschoollevel);

}
