package com.njpes.www.action;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.njpes.www.entity.baseinfo.PermissionList;
import com.njpes.www.service.util.DictionaryServiceI;
import com.njpes.www.service.workschedule.JobAttachmentMappnigServiceI;

public abstract class BaseController {

    @Autowired
    public DictionaryServiceI DictionaryService;

    @Autowired
    public JobAttachmentMappnigServiceI jobAttachmentMappnigService;

    private String viewPrefix;

    protected PermissionList permissionList = null;

    public void setResourceIdentity(String resourceIdentity) {
        if (!StringUtils.isEmpty(resourceIdentity)) {
            permissionList = PermissionList.newPermissionList(resourceIdentity);
        }
    }

    protected BaseController() {
        setViewPrefix(defaultViewPrefix());
    }

    /**
     * 设置通用数据
     * 
     * @param model
     */
    protected void setCommonData(Model model) {
    }

    /**
     * 当前模块 视图的前缀 默认 1、获取当前类头上的@RequestMapping中的value作为前缀 2、如果没有就使用当前模型小写的简单类名
     */
    public void setViewPrefix(String viewPrefix) {
        if (viewPrefix.startsWith("/")) {
            viewPrefix = viewPrefix.substring(1);
        }
        this.viewPrefix = viewPrefix;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    /**
     * 获取视图名称：即prefixViewName + "/" + suffixName
     * 
     * @return
     */
    public String viewName(String suffixName) {
        if (!suffixName.startsWith("/")) {
            suffixName = "/" + suffixName;
        }
        return getViewPrefix() + suffixName;
    }

    /**
     * @param backURL
     *            null 将重定向到默认getViewPrefix()
     * @return
     */
    protected String redirectToUrl(String backURL) {
        if (StringUtils.isEmpty(backURL)) {
            backURL = getViewPrefix();
        }
        if (!backURL.startsWith("/") && !backURL.startsWith("http")) {
            backURL = "/" + backURL;
        }
        return "redirect:" + backURL;
    }

    protected String defaultViewPrefix() {
        String currentViewPrefix = "";
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(getClass(), RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            currentViewPrefix = requestMapping.value()[0];
        }

        return currentViewPrefix;
    }

}
