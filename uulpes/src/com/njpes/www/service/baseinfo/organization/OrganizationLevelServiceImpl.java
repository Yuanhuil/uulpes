package com.njpes.www.service.baseinfo.organization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.OrganizationLevelMapper;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;

@Service("organizationLevelService")
public class OrganizationLevelServiceImpl implements OrganizationLevelServiceI {

    @Autowired
    private OrganizationLevelMapper organizationLevelMapper;

    @Override
    public List<OrganizationLevel> queryOrgLevelList() {
        return organizationLevelMapper.selectAllOrgLevel();
    }

    @Override
    public List<OrganizationLevel> queryOrgLevelList(Integer currentLevel, boolean includemyself,
            boolean includeschoollevel) {
        return organizationLevelMapper.selectOrgLevelInCurentLevel(currentLevel, includemyself, includeschoollevel);
    }

    @Override
    public List<OrganizationLevel> queryOrgDirectLevelList(Integer currentLevel, boolean includemyself,
            boolean includeschoollevel) {
        return organizationLevelMapper.selectOrgDirectLevelInCurentLevel(currentLevel, includemyself,
                includeschoollevel);
    }

}
