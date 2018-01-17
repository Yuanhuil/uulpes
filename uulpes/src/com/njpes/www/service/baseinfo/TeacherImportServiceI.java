package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.organization.Organization;

public interface TeacherImportServiceI {

    /**
     * 导入当前组织结构下的Excel文件教师基础信息
     * 
     * @param string
     *            excel文件地址
     * @param org
     *            当前组织机构
     * @author 史斌
     * @throws Exception
     */
    public void importTeahinfo(String string, Organization org) throws Exception;

}
