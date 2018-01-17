package com.njpes.www.service.workschedule;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.workschedule.JobReportMapper;
import com.njpes.www.entity.workschedule.enums.SchoolTerm;

@Service("jobReportService")
public class JobReportServiceImpl implements JobReportServiceI {
    @Autowired
    JobReportMapper jobReportMapper;

    @Override
    public List<HashMap<String, Object>> getActivityStasByQuery(Date starttime, Date endtime, List<String> cataloglist,
            List<Long> depids) {
        return jobReportMapper.reportActivitySumByQueryHashMap(starttime, endtime, cataloglist, depids);
    }

    @Override
    public List<HashMap<String, Object>> getPlanStasByQuery(Date starttime, Date endtime, List<Long> depids) {
        return jobReportMapper.getPlanStasByQuery(starttime, endtime, depids);
    }

    @Override
    public List<HashMap<String, Object>> getUnitStas(List<Long> roleids, List<Long> depids) {
        return jobReportMapper.reportUnitHashMap(roleids, depids);
    }

    @Override
    public Integer getPlanStasCountByQuery(Date starttime, Date endtime, List<Long> depids) {
        return jobReportMapper.getPlanStasCountByQuery(starttime, endtime, depids);
    }

    @Override
    public String genTitle(Date start, Date end, String constantStr) {
        Calendar s_cal = Calendar.getInstance();
        Calendar e_cal = Calendar.getInstance();
        s_cal.clear();
        e_cal.clear();
        e_cal.setTime(end);
        s_cal.setTime(start);
        String title = "";
        String titletimeend = "";
        if (s_cal.get(Calendar.YEAR) == e_cal.get(Calendar.YEAR)) {
            title = s_cal.get(Calendar.YEAR) + "年";
        } else {
            title = s_cal.get(Calendar.YEAR) + "年";
            titletimeend = e_cal.get(Calendar.YEAR) + "年";
        }
        if (s_cal.get(Calendar.YEAR) == e_cal.get(Calendar.YEAR)
                && (s_cal.get(Calendar.MONTH) + 1) == (e_cal.get(Calendar.MONTH) + 1)) {
            title += (s_cal.get(Calendar.MONTH) + 1) + "月";
        } else {
            title += (s_cal.get(Calendar.MONTH) + 1) + "月";
            titletimeend += (e_cal.get(Calendar.MONTH) + 1) + "月";
        }
        if (s_cal.get(Calendar.YEAR) == e_cal.get(Calendar.YEAR)
                && (s_cal.get(Calendar.MONTH) + 1) == (e_cal.get(Calendar.MONTH) + 1)
                && s_cal.get(Calendar.DAY_OF_MONTH) == e_cal.get(Calendar.DAY_OF_MONTH)) {
            title += s_cal.get(Calendar.DAY_OF_MONTH) + "日";
        } else {
            title += s_cal.get(Calendar.DAY_OF_MONTH) + "日";
            titletimeend += e_cal.get(Calendar.DAY_OF_MONTH) + "日";
        }
        title = title + "至" + titletimeend + constantStr;
        return title;
    }

    @Override
    public List<HashMap<String, Object>> getActivityCountByQuery(Date starttime, Date endtime, List<String> cataloglist,
            List<Long> depids) {
        return jobReportMapper.reportActivityCountByQueryHashMap(starttime, endtime, cataloglist, depids);
    }

    @Override
    public String genTitle(String schoolyear, String term, String constantStr) {
        String r = schoolyear + "学年度";
        r += SchoolTerm.valueById(term).getInfo();
        return r + constantStr;
    }

    @Override
    public List<HashMap<String, Object>> getPlanStasByQuery(String schoolyear, String term, List<Long> depids,
            String queryOrgtype) {
        return jobReportMapper.getPlanStasByQueryByTerm(schoolyear, term, depids, queryOrgtype);
    }

    @Override
    public Integer getPlanStasCountByQuery(String schoolyear, String term, List<Long> depids, String queryOrgtype) {
        return jobReportMapper.getPlanStasCountByQueryTerm(schoolyear, term, depids, queryOrgtype);
    }

    @Override
    public List<HashMap<String, Object>> getActivityCatalogStasByQuery(Date starttime, Date endtime,
            List<String> cataloglist, List<Long> depids) {
        return jobReportMapper.reportActivityCatalogSumByQueryHashMap(starttime, endtime, cataloglist, depids);
    }

}
