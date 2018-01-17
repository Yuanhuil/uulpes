package com.njpes.www.action.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.service.util.TableExcelImportMappingServiceI;

@Controller
@RequestMapping(value = "/util/importExcelController")
public class ImportExcelController {

    @Autowired
    private TableExcelImportMappingServiceI tableExcelImportMappingService;

    @RequestMapping(value = { "/imp" })
    @ResponseBody
    public String imp(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest mul = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = mul.getFiles("file");
        MultipartFile impfile = files.get(0);
        if (impfile.isEmpty())
            return "error";
        try {
            tableExcelImportMappingService.read2007Excel(impfile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        long excelid = tableExcelImportMappingService.getExcelModelId();
        tableExcelImportMappingService.write2Db(request, excelid);
        return "";
    }
}
