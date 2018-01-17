package com.njpes.www.action.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.service.util.IDCardValidateServiceI;

@Controller
@RequestMapping(value = "/util/IDCardController")
public class IDcardValidatorController {
    @Autowired
    private IDCardValidateServiceI IDCardValidateService;

    @RequestMapping(value = { "/validator" })
    @ResponseBody
    public Object validator(HttpServletRequest request, HttpServletResponse response) {
        Object[] r = new Object[3];
        String fieldId = request.getParameter("fieldId");
        String fieldValue = request.getParameter("fieldValue");
        r[0] = fieldId;
        boolean rs = IDCardValidateService.isValidatedAllIdcard(fieldValue);
        r[1] = rs;
        if (!rs)
            r[2] = "*身份证号无效！";
        return r;
    }
}
