package com.njpes.www.entity.scaletoollib;

import java.util.List;

public class StatResult {

    private String title; // 整个统计结果的标题
    private StatParams params; // 统计的参数
    private List<StatResultData> data; // 统计生成的数据结果
    private List<String> resultDescription; // 每个数据对应的文字解释

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatParams getParams() {
        return params;
    }

    public void setParams(StatParams params) {
        this.params = params;
    }

    public List<StatResultData> getData() {
        return data;
    }

    public void setData(List<StatResultData> data) {
        this.data = data;
    }

    public List<String> getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(List<String> resultDescription) {
        this.resultDescription = resultDescription;
    }

}
