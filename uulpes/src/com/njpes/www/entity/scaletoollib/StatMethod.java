package com.njpes.www.entity.scaletoollib;

public class StatMethod {
    public final static String[] ANALYSIS_TITLES = { "频次统计", "描述统计", "单一样本T检验", "独立样本T检验", "相关样本T检验", "单因素方差分析",
            "相关分析" };
    public final static String[] ANALYSIS_CLASS = { "FrequencyStat", "DescriptionStat", "SingleSampleTStat",
            "IndependentSampleTStat", "RelatedSampleTStat", "SingleFactorVariantAnalysis", "CoRelationStat" };
    private int statMethodType;
    private int chartType;

    /**
     * 获得统计方法类型："频次统计", "描述统计", "相关分析", "单一样本T检验", "独立样本T检验", "单因素方差分析",
     * "两因素方差分析"
     *
     * @return
     */
    public int getStatMethodType() {
        return statMethodType;
    }

    public void setStatMethodType(int statMethodType) {
        this.statMethodType = statMethodType;
    }

    /**
     * 获得图表类型：没有、柱图、饼图
     * 
     * @return
     */
    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

}
