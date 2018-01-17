package com.njpes.www.service.baseinfo.organization;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.EcpsychicjobMapper;
import com.njpes.www.dao.baseinfo.EcpsychicteamMapper;
import com.njpes.www.dao.baseinfo.EducommissionMapper;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicjob;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicteam;
import com.njpes.www.entity.baseinfo.organization.Educommission;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.workschedule.JobAttachment;
import com.njpes.www.service.workschedule.JobAttachmentServiceI;
import com.njpes.www.utils.PageParameter;

/**
 * 教委接口实现服务
 * 
 * @author 赵忠诚
 */
@Service("eduCommissionService")
public class EduCommissionServiceImpl implements EduCommissionServiceI {
    @Autowired
    private EducommissionMapper educommissionMapper;

    @Autowired
    private EcpsychicteamMapper ecpsychicteamMapper;

    @Autowired
    private EcpsychicjobMapper ecpsychicjobMapper;

    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private JobAttachmentServiceI jobAttachmentService;

    @Override
    public Educommission getECInfoOrgID(Long orgid) {
        return educommissionMapper.selectByOrgId(orgid);
    }

    @Override
    public List<Ecpsychicteam> getTeamsByOrgid(Long orgid) {
        return ecpsychicteamMapper.getTeamsByOrgid(orgid);
    }

    @Override
    public List<Ecpsychicjob> getJobsByOrgid(Long orgid) {
        return ecpsychicjobMapper.getJobsByOrgid(orgid);
    }

    @Override
    public int updateEC(Educommission ec) {
        ec.getOrg().setCode(ec.getJwdm());
        ec.getOrg().setName(ec.getJwmc());
        Organization org = ec.getOrg();
        org.setId(ec.getOrgid());
        String attachment_uuid = org.getImageurl();
        JobAttachment jobAttachment = jobAttachmentService.selectByUUid(attachment_uuid);
        if (StringUtils.isNotBlank(attachment_uuid) && jobAttachment != null) {
            String imageurl = jobAttachment.getSavePath();
            org.setImageurl(imageurl);
        }
        organizationService.updateOrganization(org);
        return educommissionMapper.updateByPrimaryKeySelective(ec);
    }

    @Override
    public int insertEc(Educommission ec) {
        ec.getOrg().setCode(ec.getJwdm());
        ec.getOrg().setName(ec.getJwmc());
        Organization org = organizationService.insertOrganization(ec.getOrg());
        ec.setOrgid(org.getId());
        return educommissionMapper.insertSelective(ec);
    }

    @Override
    public int delByKeyid(Long keyid) {
        return educommissionMapper.deleteByPrimaryKey(keyid);
    }

    @Override
    public int delByOrgid(Long orgid) {
        return educommissionMapper.deleteByOrgid(orgid);
    }

    @Override
    public List<Educommission> selectSubECsInWebQueryByPage(Educommission queryEc, PageParameter page) {
        return educommissionMapper.selectSubECsInWebQueryByPage(queryEc.getOrg(), page);
    }

    @Override
    public Educommission getECInfoByKeyId(Long keyid) {
        return educommissionMapper.selectByPrimaryKey(keyid);
    }

    @Override
    public int delByEntity(Educommission ec) {
        organizationService.delOrg(ec.getOrg());
        return delByKeyid(ec.getId());
    }

}
