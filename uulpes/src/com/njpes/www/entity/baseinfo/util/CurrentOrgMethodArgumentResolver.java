package com.njpes.www.entity.baseinfo.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.AccountOrgJob;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;

public class CurrentOrgMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private OrganizationServiceI organizationService;

    public CurrentOrgMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(CurrentOrg.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Organization orgEntity = (Organization) webRequest.getAttribute(Constants.CURRENT_USER_ORG,
                NativeWebRequest.SCOPE_SESSION);
        if (orgEntity != null)
            return orgEntity;
        Account currentUser = (Account) webRequest.getAttribute(Constants.CURRENT_USER, NativeWebRequest.SCOPE_SESSION);
        if (currentUser == null)
            return null;
        List<AccountOrgJob> organizationJobs = currentUser.getOrganizationJobs();
        if (organizationJobs == null || organizationJobs.size() == 0) {
            return null;
        }
        long orgid = organizationJobs.get(0).getOrgId();
        orgEntity = organizationService.selectOrganizationById(orgid);
        return orgEntity;
    }
}
