package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.organization.Organization;

public interface SchoolClassImportServiceI {

    public void importSchoolClass(String excelUrl, Organization org) throws Exception;

}
