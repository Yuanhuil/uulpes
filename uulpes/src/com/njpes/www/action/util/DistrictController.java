package com.njpes.www.action.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.service.baseinfo.DistrictService;

@Controller
@RequestMapping(value = "/util/districtcontroller")
public class DistrictController extends BaseController {
    @Autowired
    private DistrictService DistrictService;

    @RequestMapping(value = { "/getTowns" }, method = RequestMethod.POST)
    @ResponseBody
    public String getTowns(String distid) {
        if (StringUtils.isEmpty(distid)) {
            return null;
        }
        List<District> list = DistrictService.getTowns(distid);
        String json = JSON.toJSONString(list);
        return json;
    }
}
