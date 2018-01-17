package com.njpes.www.entity.baseinfo.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.njpes.www.constant.Constants;
import com.njpes.www.utils.PageParameter;

public class PageMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public PageMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(PageAnnotation.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        int currentPage;
        int pageSize;
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        if (currentPageStr == null) {
            currentPage = 1;
        } else
            currentPage = Integer.parseInt(currentPageStr);
        pageSizeStr = Constants.PAGE_LIST_NUM;
        pageSize = Integer.parseInt(pageSizeStr);
        String url = request.getRequestURI();
        PageParameter page = new PageParameter(currentPage, pageSize);
        page.setUrl(url);
        return page;
    }
}
