package com.njpes.www.entity.util;

import java.util.List;

/**
 * 用于处理所有的dic表，查询字典表返回的结果，可以根据id和name互查
 * 
 * @author s
 */
public class DictionaryUtil {
    private List<Dictionary> Dics;

    public List<Dictionary> getDics() {
        return Dics;
    }

    public void setDics(List<Dictionary> dics) {
        Dics = dics;
    }

    public String findNameById(String id) {
        if (!Dics.isEmpty()) {
            for (Dictionary dic : Dics) {
                if (id.equals(dic.getId())) {
                    return dic.getName();
                }
            }
        }
        return null;
    }

    public String findIdByName(String name) {
        if (!Dics.isEmpty()) {
            for (Dictionary dic : Dics) {
                if (name.equals(dic.getName())) {
                    return dic.getId();
                }
            }
        }
        return null;
    }
}
