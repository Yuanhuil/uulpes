package com.njpes.www.service.util;

import java.util.List;

public interface StatUtilServiceI {

    /**
     * 计算均值
     *
     * @param data
     * @return
     */
    Float computeMean(List<Float> data);

    /**
     * 计算组内方差
     *
     * @param data
     * @return
     */
    Float computeIsd(List<Float> data);

    /**
     * 计算样本方差
     *
     * @param data
     * @return
     */
    Float computeSn(List<Float> data);

    /**
     * 计算均值和标准差
     *
     * @param data
     * @param means
     * @param sds
     */

    Float computeSd(List<Float> data);

    /**
     * 计算方差和
     *
     * @param data
     * @param means
     * @param sds
     */
    Float computeSumSd(List<Float> data);

    Float computeSumSdall(List<Float> datas,List<Float> datas1);
    /**
     * 计算相关系数
     *
     * @param datasX
     * @param datasY
     * @return
     */
    Float computeCorelation(List<Float> datasX, List<Float> datasY);

    // 获得T分布0.05或者0.01的临界值
    public Float getTvalue(String t, Integer df);

    public Float getFvalue(int rOne, int rTwo);

    // 根据T值查询P值
    public Float getPvalue(Float t);

    // 融合两个数组
    public Object[] merge(Object[] a, Object[] b);
}