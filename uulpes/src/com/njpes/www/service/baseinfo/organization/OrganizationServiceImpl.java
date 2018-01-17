package com.njpes.www.service.baseinfo.organization;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.utils.PageParameter;

/**
 * 组织机构表实现服务
 * 
 * @author 赵忠诚
 */
@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationServiceI {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationLevelServiceI organizationLevelService;

    @Autowired
    private SchoolServiceI schoolService;

    @Autowired
    private EduCommissionServiceI eduCommissionService;

    @Override
    public Set<Long> findAncestorIds(Iterable<Long> currentIds) {
        Set<Long> parents = Sets.newHashSet();
        for (Long currentId : currentIds) {
            parents.addAll(findAncestorIds(currentId));
        }
        return parents;
    }

    @Override
    public Set<Long> findAncestorIds(Long currentId) {
        Set<Long> ids = Sets.newHashSet();
        Organization org = organizationMapper.selectByPrimaryKey(currentId);
        if (org == null) {
            return ids;
        }
        for (String idStr : StringUtils.split(org.getParentIds(), "/")) {
            if (!StringUtils.isEmpty(idStr)) {
                ids.add(Long.valueOf(idStr));
            }
        }
        return ids;
    }

    @Override
    public List<Organization> selectAllOrganizations() {
        return this.organizationMapper.selectAllOrganizations();
    }

    @Override
    public Organization selectOrganizationById(Long id) {
        Organization org = organizationMapper.selectByPrimaryKey(id);
        return org;
    }

    @Override
    public int updateOrganization(Organization org) {
        organizationMapper.updateByPrimaryKeySelective(org);
        /*
         * if(StringUtils.equals(org.getOrgType(),
         * OrganizationType.school.getId())){ School school =
         * schoolService.getSchoolInfo(org.getId());
         * school.setXxdm(org.getCode()); school.setXxmc(org.getName());
         * schoolService.update(school); }else
         * if(StringUtils.equals(org.getOrgType(),
         * OrganizationType.ec.getId())){ Educommission ec =
         * eduCommissionService.getECInfoOrgID(org.getId());
         * ec.setJwdm(org.getCode()); ec.setJwmc(org.getName());
         * eduCommissionService.updateEC(ec); }else{ return 0; }
         */
        return 1;
    }

    @Override
    public List<Organization> selectSubOrgsInWebQuery(Organization org, PageParameter page) {
        return organizationMapper.selectSubOrgsByWebQueryPage(org, page);
    }

    @Override
    public int saveOrg(Organization org) {
        return organizationMapper.insertSelective(org);
    }

    @Override
    public int delOrg(Organization org) {
        return organizationMapper.deleteByPrimaryKey(org.getId());
    }

    @Override
    public List<Organization> selectSubOrgsByOrgid(Long orgid, PageParameter page) {
        return organizationMapper.selectSubOrgsByOrgidPage(orgid, page);
    }

    @Override
    public List<OrganizationLevel> selectSubOrgLevel(Organization org, boolean includemyself,
            boolean includeschoollevel) {
        return organizationLevelService.queryOrgLevelList(org.getOrgLevel(), includemyself, includeschoollevel);
    }

    @Override
    public HashMap<OrganizationType, List<Organization>> getAllSubOrgs(Organization org) {
        HashMap<OrganizationType, List<Organization>> result = new HashMap<OrganizationType, List<Organization>>();
        List<Organization> ecList = organizationMapper.selectSubOrgsByType(org.getId(), OrganizationType.ec.value());
        List<Organization> schoolList = organizationMapper.selectSubOrgsByType(org.getId(),
                OrganizationType.school.value());
        result.put(OrganizationType.ec, ecList);
        result.put(OrganizationType.school, schoolList);
        return result;
    }

    @Override
    public HashMap<OrganizationType, List<Organization>> getSonSubOrgs(Organization org) {
        HashMap<OrganizationType, List<Organization>> result = new HashMap<OrganizationType, List<Organization>>();
        List<Organization> ecList = organizationMapper.selectSonSubOrgsByType(org.getId(), OrganizationType.ec.value());
        List<Organization> schoolList = organizationMapper.selectSonSubOrgsByType(org.getId(),
                OrganizationType.school.value());
        result.put(OrganizationType.ec, ecList);
        result.put(OrganizationType.school, schoolList);
        return result;
    }

    @Override
    public List<Organization> getQuxianAndSchoolsOrgs(Organization org) {
        HashMap<OrganizationType, List<Organization>> result = new HashMap<OrganizationType, List<Organization>>();
        List<Organization> orgList = organizationMapper.getQuxianAndSchoolsOrgs(org.getId());
        return orgList;
    }

    @Override
    public List<Organization> getSonSubOrgsList(Organization org) {
        return organizationMapper.selectSonSubOrgsByType(org.getId(), null);
    }

    @Override
    public List<Organization> getSonSchoolList(Organization org) {
        return organizationMapper.selectSonSubOrgsByType(org.getId(), OrganizationType.school.value());
    }

    @Override
    public Organization getEduOrganizationByCountyId(String areaid) {
        // if(orgLevel == 3)
        return organizationMapper.getEduOrganizationByCountyId(areaid);
        // if(orgLevel == 4)
        // return organizationMapper.getEduOrganizationByTownId(areaid);
        // return null;
    }

    @Override
    public List<Organization> getEduOrganizationByCityId(String areaid) {
        return organizationMapper.getEduOrganizationByCityId(areaid);
    }

    @Override
    public List<Organization> getSchoolOrgByTownId(String areaid) {
        return organizationMapper.getSchoolOrgByTownId(areaid);
    }

    @Override
    public Organization findParent(Long currentId) {
        return organizationMapper.findParent(currentId);
    }

    @Override
    public List<Organization> getSchoolOrgByCountyId(String countyid) {
        return organizationMapper.getSchoolOrgByCountyId(countyid);
    }

    @Override
    public List<Organization> getSchoolOrgByCityId(String cityid) {
        return organizationMapper.getCountiesByCity(cityid);
    }

    @Override
    public List<Organization> getCountiesByCity(String cityid) {
        return organizationMapper.getCountiesByCity(cityid);
    }

    @Override
    public List<Organization> getTownsByCounty(String countyid) {
        return organizationMapper.getTownsByCounty(countyid);
    }

    @Override
    public List<Organization> getSchoolOrgByCountyIds() {
        return null;
    }

    @Override
    public List<Organization> getSchoolOrgByTownIds() {
        return null;
    }

    @Override
    public Organization insertOrganization(Organization org) {
        Long parentid = org.getParentId();
        if (parentid == null || parentid == 0)
            return org;
        Organization parentOrg = organizationMapper.selectByPrimaryKey(parentid);
        String parentids = parentOrg.getParentIds();
        if (!parentids.endsWith("/")) {
            parentids = parentids + "/";
        }
        org.setParentIds(parentids + parentid + "/");
        org.setIsshow(1);
        organizationMapper.insertSelective(org);
        return org;
    }

    @Override
    public int updateOrgLocked(Long orgid, String locked) {
        return organizationMapper.updateLocked(orgid, locked);
    }

    @Override
    public List<OrganizationLevel> selectDirectSubOrgLevel(Organization org, boolean includemyself,
            boolean includeschoollevel) {
        return organizationLevelService.queryOrgDirectLevelList(org.getOrgLevel(), includemyself, includeschoollevel);
    }

    @Override
    public List<Organization> getSonSubOrgsList(Organization org, int orglevel) {
        return organizationMapper.selectSonSubOrgsByOrgLevel(org.getId(), orglevel);
    }

    @Override
    public List<Organization> getAllSchool(Organization org) {
        return organizationMapper.selectAllSchools();
    }

    @Override
    public List<Organization> getAllEcs(Organization org) {
        return organizationMapper.selectAllEcs();
    }

}
