package com.njpes.www.service.workschedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface JobReportServiceI {

    public static String resourceIdentity = "workschedule:jobreport";

    /**
     * 汇总统计下属单位心理老师和咨询老师数量
     * 
     * @param roleids
     *            统计角色id列表
     * @param depids
     *            查询下属单位列表
     * @return 返回HashMap结果
     * @author 赵忠诚
     */
    public List<HashMap<String, Object>> getUnitStas(List<Long> roleids, List<Long> depids);

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
    public List<HashMap<String, Object>> getActivityStasByQuery(Date starttime, Date endtime, List<String> cataloglist,
            List<Long> depids);

    public List<HashMap<String, Object>> getActivityCountByQuery(Date starttime, Date endtime, List<String> cataloglist,
            List<Long> depids);

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
    public List<HashMap<String, Object>> getPlanStasByQuery(Date starttime, Date endtime, List<Long> depids);

    public List<HashMap<String, Object>> getPlanStasByQuery(String schoolyear, String term, List<Long> depids,
            String queryOrgtype);

    public Integer getPlanStasCountByQuery(Date starttime, Date endtime, List<Long> depids);

    public Integer getPlanStasCountByQuery(String schoolyear, String term, List<Long> depids, String queryOrgtype);

    public String genTitle(Date start, Date end, String constantStr);

    public String genTitle(String schoolyear, String term, String constantStr);

    public List<HashMap<String, Object>> getActivityCatalogStasByQuery(Date starttime, Date endtime, List<String> cataloglist,
            List<Long> depids);

}
