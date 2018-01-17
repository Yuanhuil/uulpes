package com.njpes.www.service.baseinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.njpes.www.dao.baseinfo.DistrictMapper;
import com.njpes.www.entity.baseinfo.District;

@Service("DistrictService")
public class DistrictService {
    @Autowired
    DistrictMapper districtMapper;

    public District selectByCode(String code) {
        return districtMapper.selectByCode(code);
    }

    public List<District> getAllProvince() {
        return districtMapper.getAllProvince();
    }

    public List<District> getCities(String parentcode) {
        return districtMapper.getCities(parentcode);
    }

    public List<District> getCounties(String parentcode) {
        return districtMapper.getCounties(parentcode);
    }

    public List<District> getTowns(String parentcode) {
        return districtMapper.getTowns(parentcode);
    }

    public String getTownsStr(String parentcode) {
        List<District> districtList = districtMapper.getTowns(parentcode);
        Gson gson = new Gson();
        String districtStr = gson.toJson(districtList);
        return districtStr;
    }
}
