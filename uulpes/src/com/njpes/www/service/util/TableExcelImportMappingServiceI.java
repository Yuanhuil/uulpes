package com.njpes.www.service.util;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.njpes.www.entity.util.TableExcelImportMapping;

public interface TableExcelImportMappingServiceI {

    public List<TableExcelImportMapping> getConfigByExcelId(Long id);

    /**
     * excel 模板id隐藏存储在excel的 ID sheet的A1单元格
     */
    public int getExcelModelId();

    public void read2007Excel(InputStream stream);

    public boolean write2Db(HttpServletRequest request, long excelid);
}
