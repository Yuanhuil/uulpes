package com.njpes.www.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = Logger.getLogger(CustomExceptionResolver.class);

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        String errorMessage = "";
        if (ex.getCause() != null) {
            errorMessage = ex.getCause().getMessage();
        } else {
            errorMessage = ex.getMessage();
        }
        logger.error(errorMessage);

        String xRequestedWith = request.getHeader("X-Requested-With");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ex", ex);
        if (!StringUtils.isEmpty(xRequestedWith)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                if (StringUtils.isEmpty(errorMessage)) {
                    errorMessage = "服务器内部错误";
                }
                response.getWriter().write(errorMessage);
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ModelAndView("common/ajax-error", model);
        }

        return new ModelAndView("common/error", model);
    }

}
