package com.njpes.www.entity.scaletoollib;

import java.util.List;

public class StatResultData {
    private List titles;
    private List datas;
    private List dataTitles;
    private List<StatChart> charts;
    private List<String> explains;

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public List<StatChart> getCharts() {
        return charts;
    }

    public void setCharts(List<StatChart> charts) {
        this.charts = charts;
    }

    public List getDataTitles() {
        return dataTitles;
    }

    public void setDataTitles(List dataTitles) {
        this.dataTitles = dataTitles;
    }

    public List getTitles() {
        return titles;
    }

    public void setTitles(List titles) {
        this.titles = titles;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }
}
