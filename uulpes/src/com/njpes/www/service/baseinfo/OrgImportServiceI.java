package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.organization.Organization;

public interface OrgImportServiceI {

    public void importOrg(String excelUrl, Organization org) throws Exception;

    public void importEdu(String excelUrl, Organization org) throws Exception;

    public Integer updateSeqId(Long maxId);

}
