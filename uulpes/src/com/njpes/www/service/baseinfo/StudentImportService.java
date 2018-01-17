package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.organization.Organization;

public interface StudentImportService {

    public String importStuinfo(String excelUrl, Organization org) throws Exception;

}
