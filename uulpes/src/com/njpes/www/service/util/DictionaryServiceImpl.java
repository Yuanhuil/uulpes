package com.njpes.www.service.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.util.DictionaryMapper;
import com.njpes.www.entity.util.Dictionary;

@Service("DictionaryService")
public class DictionaryServiceImpl implements DictionaryServiceI {

    @Autowired
    private DictionaryMapper dicMapper;

    @Override
    public Dictionary selectByPrimaryKey(Map para) {
        return dicMapper.selectByPrimaryKey(para);
    }

    @Override
    public List<Dictionary> selectAllDic(String table) {
        return dicMapper.selectAllDic(table);
    }

    @Override
    public List<Dictionary> selectAllDicWhere(String table, String where) {
        return dicMapper.selectAllDicWhere(table, where);
    }

    @Override
    public List<Dictionary> selectAllType(String table) {
        return dicMapper.selectAllType(table);
    }

    @Override
    public Map selectAllDicMap(String table) {
        // TODO Auto-generated method stub
        List<Dictionary> alldic = selectAllDic(table);
        Map dicmap = new HashMap();
        for (Dictionary dic : alldic) {
            dicmap.put(dic.getId(), dic.getName());
        }
        return dicmap;
    }

    @Override
    public List<Dictionary> selectDicWhere(String tablename, List<String> fields, String where) {
        return dicMapper.selectDicWhere(tablename, fields, where);
    }

}
