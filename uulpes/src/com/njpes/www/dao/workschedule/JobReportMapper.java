package com.njpes.www.dao.workschedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface JobReportMapper {
    /**
     * 汇总统计下属单位心理老师和咨询老师数量
     * 
     * @param depids
     *            查询下属单位列表
     * @return 返回HashMap结果
     * @author 赵忠诚
     */
    public List<HashMap<String, Object>> reportUnitHashMap(@Param("roleids") List<Long> roleids,
            @Param("deplist") List<Long> deplist);

    /**
     * 根据页面查询获得统计结果
     * 
     * @param starttime
     * @param endtime
     * @param cataloglist
     *            分类
     * @param depids
     *            查询下属单位列表
     * @return 返回HashMap结果
     * @author 赵忠诚
     */
    public List<HashMap<String, Object>> reportActivitySumByQueryHashMap(@Param("starttime") Date starttime,
            @Param("endtime") Date endtime, @Param("querylist") List<String> querylist,
            @Param("deplist") List<Long> deplist);

    public List<HashMap<String, Object>> reportActivityCountByQueryHashMap(@Param("starttime") Date starttime,
            @Param("endtime") Date endtime, @Param("querylist") List<String> querylist,
            @Param("deplist") List<Long> deplist);

    /**
     * 根据页面查询获得统计结果
     * 
     * @param starttime
     * @param endtime
     * @param depids
     *            查询下属单位列表
     * @return 返回HashMap结果
     * @author 赵忠诚
     */
    public List<HashMap<String, Object>> getPlanStasByQuery(@Param("starttime") Date starttime,
            @Param("endtime") Date endtime, @Param("deplist") List<Long> depids);

    public List<HashMap<String, Object>> getPlanStasByQueryByTerm(@Param("schoolyear") String schoolyear,
            @Param("term") String term, @Param("deplist") List<Long> depids, @Param("querytype") String querytype);

    public Integer getPlanStasCountByQuery(@Param("starttime") Date starttime, @Param("endtime") Date endtime,
            @Param("deplist") List<Long> depids);

    public Integer getPlanStasCountByQueryTerm(@Param("schoolyear") String schoolyear, @Param("term") String term,
            @Param("deplist") List<Long> depids, @Param("querytype") String querytype);

    public List<HashMap<String, Object>> reportActivityCatalogSumByQueryHashMap(@Param("starttime") Date starttime,
            @Param("endtime") Date endtime, @Param("querylist") List<String> querylist,
            @Param("deplist") List<Long> deplist);

}
