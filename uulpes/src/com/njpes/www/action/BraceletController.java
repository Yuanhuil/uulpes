package com.njpes.www.action;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;

import edutec.admin.ExportUtils;
import heracles.web.config.ApplicationConfiguration;

/**
 * 接收手环报告数据接口
 */
@RequestMapping(value = "/bracelet")
@Controller
public class BraceletController {

    private String msg;

    /**
     * 接收手环报告数据
     * @param request
     * @return String
     * @Date 2017年6月1日 下午4:23:28
     */
    @RequestMapping(value = "report", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String importReport(HttpServletRequest request) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile mf = entity.getValue();
                String fileName = mf.getOriginalFilename();
                if (!StringUtils.endsWith(fileName, ".pdf")) {
                    msg = "传输的不是PDF文件，请重试!";
                    break;
                } else {
                    String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + File.separator
                            + ExportUtils.DATA_STORE_DIR;
                    // TODO 存储过程(待完成)
                    result.put("code", "200");
                    result.put("msg", "success,文件已存放至'" + saveFilePath + File.separator + fileName + "'");
                    return JSON.toJSONString(result);
                }
            }
        } else {// 文件列表为空
            msg = "文件传输列表为空或异常!";
        }
        result.put("code", "100");
        result.put("msg", msg);
        return JSON.toJSONString(result);
    }

}
