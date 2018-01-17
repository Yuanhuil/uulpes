package com.njpes.www.action.workschedule;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.enums.OrgLevel;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.OrganizationLevel;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.workschedule.JobReportActivity;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.workschedule.JobReportServiceI;
import com.njpes.www.utils.SchoolYearTermUtil;

@Controller
@RequestMapping(value = "/workschedule/jobreport")
public class JobReportController extends BaseController {

    @Autowired
    private JobReportServiceI jobReportService;

    @Autowired
    private OrganizationServiceI organizationService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@CurrentOrg Organization org, Model model) {
        model.addAttribute("orgtype", org.getOrgType());
        return viewName("list");
    }

    @RequestMapping(value = "activity/list", method = RequestMethod.GET)
    public String activitylist(@CurrentOrg Organization org, JobReportActivity entity, Model model) {
        if (org.getOrgType().equals(OrganizationType.ec.getId())
                && org.getOrgLevel().intValue() <= OrgLevel.city.getIdentify()) {// 只有教委用户才有显示
            model.addAttribute("showchosen", true);
        }
        List<Dictionary> planCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
        entity.setPlancatalog("1");
        model.addAttribute("entity", entity);
        model.addAttribute("plancatalog", planCatalog);
        return viewName("activity/list");
    }

    @RequestMapping(value = "activity/getType")
    @ResponseBody
    public List<Dictionary> getPlanTypeByCat(String catid) {
        String where = " catid = '" + catid + "'";
        List<Dictionary> plantype = DictionaryService.selectAllDicWhere("dic_job_activity_type", where);
        return plantype;
    }

    // TODO delete
    public String oldactivityquerydata(HttpServletRequest request, HttpServletResponse response,
            @CurrentOrg Organization org, String queryOrgtype, JobReportActivity entity, Model model,
            String[] plantype) {
        if (plantype == null || plantype.length <= 0) {
            model.addAttribute("chart", false);
        } else {
            Date endtime = entity.getEndTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(endtime);
            cal.add(Calendar.DATE, 1);
            endtime = cal.getTime();
            entity.setEndTime(endtime);
            List<Organization> sonOrgs = null;
            if (StringUtils.isBlank(queryOrgtype)) {
                sonOrgs = organizationService.getSonSubOrgsList(org);
            } else {
                sonOrgs = organizationService.getSonSubOrgs(org).get(OrganizationType.valueOfStr(queryOrgtype));
            }
            List<Long> orgIds = new ArrayList<Long>();
            String inSql = "";
            for (String s : plantype) {
                inSql += "'" + s + "',";
            }
            inSql = inSql.substring(0, inSql.lastIndexOf(","));
            String where = "catid='" + entity.getPlancatalog() + "' and id in(" + inSql + ")";
            List<Dictionary> catalogType = DictionaryService.selectAllDicWhere("dic_job_activity_type", where);
            List<String> typeIds = new ArrayList<String>();
            for (Dictionary d : catalogType) {
                typeIds.add(d.getId());
            }
            if (sonOrgs == null || sonOrgs.size() == 0) {// 表示是学校了，已经是叶子节点
                orgIds.add(org.getId());
            } else {
                for (Organization o : sonOrgs) {
                    orgIds.add(o.getId());
                }
            }

            List<Dictionary> catalog = DictionaryService.selectAllDicWhere("dic_job_activity_catalog",
                    "id='" + entity.getPlancatalog() + "'");
            String name = catalog.get(0).getName() + "汇总";
            String title = jobReportService.genTitle(entity.getStartTime(), entity.getEndTime(), name);
            List<HashMap<String, Object>> resultMap = jobReportService.getActivityStasByQuery(entity.getStartTime(),
                    entity.getEndTime(), typeIds, orgIds);
            List<HashMap<String, Object>> resultConutMap = jobReportService
                    .getActivityCountByQuery(entity.getStartTime(), entity.getEndTime(), typeIds, orgIds);
            JFreeChart chart = this.createChart(resultMap, catalogType);
            if (chart != null) {
                String fileName = "";
                try {
                    fileName = ServletUtilities.saveChartAsJPEG(chart, 600, 300, null, request.getSession());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String chartURL = request.getContextPath() + "/chart?filename=" + fileName;
                model.addAttribute("chartURL", chartURL);
                model.addAttribute("resultTable", createTable(title, resultMap, resultConutMap, catalogType));
                model.addAttribute("chart", true);
            } else {
                model.addAttribute("chart", false);
            }
        }
        return viewName("activity/tablelist");
    }

    @RequestMapping(value = "activity/querydata", method = RequestMethod.POST)
    public String activityquerydata(HttpServletRequest request, HttpServletResponse response,
            @CurrentOrg Organization org, String queryOrgtype, JobReportActivity entity, Model model) {

        String activityCatalogId = entity.getPlancatalog();
        Date startTime = entity.getStartTime();
        Date endTime = entity.getEndTime();
        List<Dictionary> activityCatalog = new ArrayList<Dictionary>();
        List<Dictionary> activityType = new ArrayList<Dictionary>();
        List<String> catalogTypeIds = new ArrayList<String>();
        List<Long> orgIds = new ArrayList<Long>();
        // catalogTypeIds titleName
        String titleName = "活动汇总";
        if (StringUtils.isNotBlank(activityCatalogId)) {
            activityCatalog = DictionaryService.selectAllDicWhere("dic_job_activity_catalog",
                    "id='" + activityCatalogId + "'");
            activityType = DictionaryService.selectAllDicWhere("dic_job_activity_type",
                    "catid='" + activityCatalogId + "'");
            if (activityCatalog != null && activityCatalog.size() == 1) {
                titleName = activityCatalog.get(0).getName() + titleName;
            }
        } else {
            activityCatalog = DictionaryService.selectAllDic("dic_job_activity_catalog");
            activityType = DictionaryService.selectAllType("dic_job_activity_type");
        }
        if (activityCatalog != null && activityCatalog.size() > 0) {
            if (activityType != null && activityType.size() > 0) {
                for (Dictionary dic : activityType) {
                    catalogTypeIds.add(dic.getId());
                }
                List<Organization> sonOrgs = StringUtils.isBlank(queryOrgtype)
                        ? organizationService.getSonSubOrgsList(org)
                        : organizationService.getSonSubOrgs(org).get(OrganizationType.valueOfStr(queryOrgtype));
                if (sonOrgs != null && sonOrgs.size() > 0) {
                    for (Organization o : sonOrgs) {
                        orgIds.add(o.getId());
                    }
                } else {
                    orgIds.add(org.getId());
                }
                // query date
                List<HashMap<String, Object>> resultConutMap = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> resultMap = new ArrayList<HashMap<String, Object>>();
                if (org.getOrgType().equals("1")) { //教委用户
                    List<String> catalogs = new ArrayList<String>();
                    for (Dictionary dic : activityCatalog) {
                        catalogs.add(dic.getId());
                    }
                    resultMap = jobReportService.getActivityCatalogStasByQuery(startTime, endTime,catalogs, orgIds);
                } else {
                    resultConutMap = jobReportService.getActivityCountByQuery(startTime,endTime, catalogTypeIds, orgIds);
                    resultMap = jobReportService.getActivityStasByQuery(startTime, endTime,catalogTypeIds, orgIds);
                }
                if (resultMap != null && resultMap.size() == 1 && StringUtils.equals(org.getOrgType(), "2")) {// 学校用户不显示学校名称列
                    resultMap.get(0).put("dep", "");
                }
                String title = jobReportService.genTitle(entity.getStartTime(), entity.getEndTime(), titleName);
                if (org.getOrgType().equals("1")) {
                    model.addAttribute("activitycatalogs",activityCatalog);
                    model.addAttribute("resultTable",createActivityTable(title, resultMap,activityCatalog));
                    return viewName("activity/activityStatisticsTable");
                } else {
                    model.addAttribute("resultTable", createActivityTable(title, resultMap, resultConutMap, activityType,activityCatalog));
                }
                model.addAttribute("chart", true);
            }
        }
        return viewName("activity/tablelist");
    }

    private String createActivityTable(String title, List<HashMap<String, Object>> resultMap,
            List<HashMap<String, Object>> resultCountMap, List<Dictionary> catalogType, List<Dictionary> ActivitycatalogType) {
        StringBuffer sb = new StringBuffer();
        int value = -1;
        int dfi = -1;
        HashMap<String, Integer> countList = new HashMap<String, Integer>();
        for (Dictionary d : catalogType) {
            boolean flag = false;
            for (HashMap<String, Object> row : resultCountMap) {
                if (row.get("activitytype").toString().equals(d.getId())) {
                    countList.put(d.getId(), Integer.parseInt(row.get("cnt").toString()));
                    flag = true;
                }
            }
            if (!flag) {
                countList.put(d.getId(), 0);
            }
        }
        int count = 0;
        for(int value1 : countList.values()) {
            count = count + value1;
        }
        sb.append("<h1>").append(title).append("</h1>");
        sb.append("<table>").append("<tr class=\"titleBg\">").append("<th>活动类别</th><th>活动类型</th><th>数量</th><th>百分比</th>");
        for(Dictionary c : ActivitycatalogType){
            sb.append("<tr>").append("<td>").append(c.getName()).append("</td>");
            for(int i=0;i<catalogType.size();i++){
                List<Integer> listValue = new ArrayList<Integer>();
                List<Integer> listDfi = new ArrayList<Integer>();
                for (HashMap<String, Object> row : resultMap) {
                    int j = 0;
                    for (Dictionary d : catalogType) {
                        Object rs_value = row.get("z" + j);
                        value = 0;
                        if (rs_value != null)
                            value = Integer.parseInt(rs_value.toString());
                                listValue.add(value);
                                double bfb = 0;
                                if (countList.get(d.getId()) != 0)
                                    bfb = (double) value / count * 100;
                                    dfi = (int)Math.rint(bfb);
                                    listDfi.add(dfi);
                            j++;
                    }
                }
                    if(c.getId().equals(catalogType.get(i).getCatid())){
                        if(catalogType.get(i).getId().equals("1")||catalogType.get(i).getId().equals("4")||catalogType.get(i).getId().equals("8")){
                            sb.append("<td>").append(catalogType.get(i).getName()).append("</td>");
                            sb.append("<td>").append(listValue.get(i)).append("</td>");
                            sb.append("<td>").append(listDfi.get(i)).append("%").append("</td></tr>");
                        }else{
                            sb.append("<tr><td></td><td>").append(catalogType.get(i).getName()).append("</td>");
                            sb.append("<td>").append(listValue.get(i)).append("</td>");
                            sb.append("<td>").append(listDfi.get(i)).append("%").append("</td></tr>");
                        }
                    }
            }
        }
        sb.append("</table>");
        System.out.println(sb.toString());
        return sb.toString();
    }

    @RequestMapping(value = "plan/list", method = RequestMethod.GET)
    public String planlist(@CurrentOrg Organization org, String queryOrgtype, JobReportActivity entity, Model model) {
        if (org.getOrgType().equals(OrganizationType.ec.getId())
                && org.getOrgLevel().intValue() <= OrgLevel.city.getIdentify()) {// 只有教委用户才有显示
            model.addAttribute("showchosen", true);
        }
        List<Dictionary> schoolterm = DictionaryService.selectAllDic("dic_xq");
        List<String> schoolyears = SchoolYearTermUtil.getSchoolYearsList();
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("schoolterm", schoolterm);
        List<OrganizationLevel> suborgLevel = organizationService.selectSubOrgLevel(org, false, true);
        model.addAttribute("entity", entity);
        model.addAttribute("suborgLevel", suborgLevel);
        return viewName("plan/list");
    }

    @RequestMapping(value = "plan/querydata", method = RequestMethod.POST)
    public String planquerydata(HttpServletRequest request, HttpServletResponse response, @CurrentOrg Organization org,
            String queryOrgtype, JobReportActivity entity, Model model) {

        List<Organization> sonOrgs = null;
        if (StringUtils.isBlank(queryOrgtype)) {
            sonOrgs = organizationService.getSonSubOrgsList(org);
        } else if (queryOrgtype.equals("3")) {// 表示汇总的是本级单位
            // 暂时不执行任何代码 不需要获取孩子节点的信息
        } else if (queryOrgtype.equals("2")) {// 直属学校
            sonOrgs = organizationService.getSonSubOrgs(org).get(OrganizationType.valueOfStr(queryOrgtype));
        } else if (queryOrgtype.equals("1")) {// 区县教委
            // sonOrgs =
            // organizationService.getSonSubOrgs(org).get(OrganizationType.valueOfStr(queryOrgtype));
        }
        List<Long> orgIds = new ArrayList<Long>();
        if (sonOrgs == null || sonOrgs.size() == 0) {// 表示是学校了，已经是叶子节点
                                                     // 或者用户选择的是本机机构
            orgIds.add(org.getId());
        } else {
            for (Organization o : sonOrgs) {
                orgIds.add(o.getId());
            }
        }

        String title = jobReportService.genTitle(entity.getSchoolyear(), entity.getTerm(), "计划汇总");

        List<HashMap<String, Object>> resultMap = jobReportService.getPlanStasByQuery(entity.getSchoolyear(),
                entity.getTerm(), orgIds, queryOrgtype);
        Integer count = jobReportService.getPlanStasCountByQuery(entity.getSchoolyear(), entity.getTerm(), orgIds,
                queryOrgtype);
        JFreeChart chart = this.createPlanChart(resultMap);
        if (chart != null) {
//            String fileName = "";
//            try {
//                fileName = ServletUtilities.saveChartAsJPEG(chart, 600, 300, null, request.getSession());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String chartURL = request.getContextPath() + "/chart?filename=" + fileName;
//            model.addAttribute("chartURL", chartURL);
            model.addAttribute("resultTable", createPlanTable(title, count, resultMap));
            model.addAttribute("chart", true);
        } else {
            model.addAttribute("chart", false);
        }
        return viewName("plan/tablelist");
    }

    @RequestMapping(value = "unit/list")
    public String unitlist(HttpServletRequest request, @CurrentOrg Organization org, Model model, String queryOrgtype) {
        if (org.getOrgType().equals(OrganizationType.ec.getId())
                && org.getOrgLevel().intValue() <= OrgLevel.city.getIdentify()) {// 只有教委用户才有显示
            model.addAttribute("showchosen", true);
        }
        List<Organization> sonOrgs = null;
        if (StringUtils.isBlank(queryOrgtype)) {
            sonOrgs = organizationService.getSonSubOrgsList(org);
        } else {
            sonOrgs = organizationService.getSonSubOrgs(org).get(OrganizationType.valueOfStr(queryOrgtype));
        }
        List<Long> orgIds = new ArrayList<Long>();
        if (sonOrgs == null || sonOrgs.size() == 0) {// 表示是学校了，已经是叶子节点
            orgIds.add(org.getId());
        } else {
            for (Organization o : sonOrgs) {
                orgIds.add(o.getId());
            }
        }
        // ------------------------------------
        List<Long> roleids = new ArrayList<Long>();
        List<String> roleNames = new ArrayList<String>();
        roleids.add(Constants.ROLE_PSY_TEACHER);

        roleNames.add("心理咨询员");
        // -----------------------------------------------
        List<HashMap<String, Object>> resultMap = jobReportService.getUnitStas(roleids, orgIds);

        JFreeChart chart = this.createUintChart(resultMap, roleNames);
        if (chart != null) {
            String fileName = "";
            try {
                fileName = ServletUtilities.saveChartAsJPEG(chart, 600, 300, null, request.getSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String chartURL = request.getContextPath() + "/chart?filename=" + fileName;
            model.addAttribute("chartURL", chartURL);
            model.addAttribute("resultTable", createUnitTable(resultMap, roleNames));
            model.addAttribute("chart", true);
        } else {
            model.addAttribute("chart", false);
        }
        model.addAttribute("c", queryOrgtype);
        return viewName("unit/list");
    }

    private String createUnitTable(List<HashMap<String, Object>> resultMap, List<String> roleNames) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table>").append("<tr class=\"titleBg\">").append("<th>机构代码</th>").append("<th>机构名称</th>");
        for (String d : roleNames) {
            sb.append("<th>").append(d).append("</th>");
        }
        sb.append("</tr>");
        for (HashMap<String, Object> row : resultMap) {
            sb.append("<tr>");
            sb.append("<td>").append(row.get("code").toString()).append("</td>");
            sb.append("<td>").append(row.get("name").toString()).append("</td>");
            int i = 0;
            for (String d : roleNames) {
                Object rs_value = row.get("z" + i);
                int value = 0;
                if (rs_value != null)
                    value = Integer.parseInt(rs_value.toString());
                sb.append("<td>").append(value).append("</td>");
                i++;
            }

            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    private String createPlanTable(String title, Integer count, List<HashMap<String, Object>> resultMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>").append(title).append("</h1>");
        sb.append("<table>").append("<tr class=\"titleBg\">").append("<th>机构名称</th>").append("<th>数量</th>")
                .append("<th>百分比</th>");
        sb.append("</tr>");
        for (HashMap<String, Object> row : resultMap) {
            sb.append("<tr>");
            sb.append("<td>").append(row.get("dep").toString()).append("</td>");
            Object rs_value = row.get("s");
            int value = 0;
            if (rs_value != null)
                value = Integer.parseInt(rs_value.toString());
            double bfb = (double) value / count * 100;
            int bfi = (int)Math.rint(bfb);
            sb.append("<td>").append(value).append("</td>");
            sb.append("<td>").append(bfi).append("%</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    private String createTable(String title, List<HashMap<String, Object>> resultMap,
            List<HashMap<String, Object>> resultCountMap, List<Dictionary> catalogType) {
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>").append(title).append("</h1>");
        sb.append("<table>").append("<tr class=\"titleBg\">").append("<th></th>");
        HashMap<String, Integer> countList = new HashMap<String, Integer>();
        for (Dictionary d : catalogType) {
            sb.append("<th>").append(d.getName()).append("</th>");
            boolean flag = false;
            for (HashMap<String, Object> row : resultCountMap) {
                if (row.get("activitytype").toString().equals(d.getId())) {
                    countList.put(d.getId(), Integer.parseInt(row.get("cnt").toString()));
                    flag = true;
                }
            }
            if (!flag) {
                countList.put(d.getId(), 0);
            }
        }
        sb.append("</tr>");
        int count = 0;
        for(int value : countList.values()) {
            count = count + value;
        }
        for (HashMap<String, Object> row : resultMap) {
            sb.append("<tr>");
            sb.append("<td>").append(row.get("dep").toString()).append("</td>");
            int i = 0;
            for (Dictionary d : catalogType) {
                Object rs_value = row.get("z" + i);
                int value = 0;
                if (rs_value != null)
                    value = Integer.parseInt(rs_value.toString());
                double bfb = 0;
                if (countList.get(d.getId()) != 0)
                    bfb = (double) value / count * 100;
                    int dfi = (int)Math.rint(bfb);
                sb.append("<td>").append(value).append("(").append(dfi).append("%)").append("</td>");
                i++;
            }

            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    private JFreeChart createUintChart(List<HashMap<String, Object>> resultMap, List<String> roleNames) {
        if (resultMap != null && resultMap.size() > 0 && resultMap.get(0) != null) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (HashMap<String, Object> row : resultMap) {
                int i = 0;
                for (String d : roleNames) {
                    Object rs_value = row.get("z" + i);
                    int value = 0;
                    if (rs_value != null)
                        value = Integer.parseInt(rs_value.toString());
                    dataset.addValue(value, d, row.get("name").toString());
                    i++;
                }
            }
            JFreeChart chart = ChartFactory.createBarChart("", // 标题
                    "", // x轴
                    "教师数量", // y轴
                    dataset, // 数据
                    PlotOrientation.VERTICAL, // 定位，VERTICAL：垂直
                    true, // 是否显示图例注释(对于简单的柱状图必须是false)
                    true, // 是否生成工具//没用过
                    true);// 是否生成URL链接//没用过
            getChartByFont(chart);
            return chart;
        }
        return null;
    }

    private JFreeChart createPlanChart(List<HashMap<String, Object>> resultMap) {
        if (resultMap != null && resultMap.size() > 0 && resultMap.get(0) != null) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (HashMap<String, Object> row : resultMap) {
                Object rs_value = row.get("s");
                int value = 0;
                if (rs_value != null)
                    value = Integer.parseInt(rs_value.toString());
                dataset.addValue(value, "data", row.get("dep").toString());
            }
            JFreeChart chart = ChartFactory.createBarChart("", // 标题
                    "", // x轴
                    "数量", // y轴
                    dataset, // 数据
                    PlotOrientation.VERTICAL, // 定位，VERTICAL：垂直
                    false, // 是否显示图例注释(对于简单的柱状图必须是false)
                    true, // 是否生成工具//没用过
                    true);// 是否生成URL链接//没用过
            getChartByFont(chart);
            return chart;
        }
        return null;
    }

    private JFreeChart createChart(List<HashMap<String, Object>> resultMap, List<Dictionary> catalogType) {
        if (resultMap != null && resultMap.size() > 0 && resultMap.get(0) != null) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (HashMap<String, Object> row : resultMap) {
                int i = 0;
                for (Dictionary d : catalogType) {
                    Object rs_value = row.get("z" + i);
                    int value = 0;
                    if (rs_value != null)
                        value = Integer.parseInt(rs_value.toString());
                    dataset.addValue(value, d.getName(), row.get("dep").toString());
                    i++;
                }
            }
            JFreeChart chart = ChartFactory.createBarChart("", // 标题
                    "类别", // x轴
                    "数据", // y轴
                    dataset, // 数据
                    PlotOrientation.VERTICAL, // 定位，VERTICAL：垂直
                    true, // 是否显示图例注释(对于简单的柱状图必须是false)
                    true, // 是否生成工具//没用过
                    true);// 是否生成URL链接//没用过
            getChartByFont(chart);
            return chart;
        }
        return null;
    }

    private static void getChartByFont(JFreeChart chart) {
        chart.setBackgroundPaint(Color.white);
        TextTitle textTitle = chart.getTitle();
        Font font = new Font("宋体", Font.BOLD, 20);
        textTitle.setFont(font);
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(font);
        }
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        // 生成图片的背景色
        plot.setBackgroundPaint(Color.white);
        // 行线的颜色
        plot.setRangeGridlinePaint(Color.BLACK);
        // 刻度字体
        plot.getDomainAxis().setTickLabelFont(font);
        // X轴名称字体
        plot.getDomainAxis().setLabelFont(font);

        plot.setRangeGridlinesVisible(true);

        CategoryAxis axis = plot.getDomainAxis();
        // 设置X轴坐标上标题的文字
        axis.setLabelFont(new Font("宋体", Font.BOLD, 12));
        // 设置X轴坐标上的文字，
        axis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

        ValueAxis valueAxis = plot.getRangeAxis();
        // 设置Y轴坐标上标题的文字
        valueAxis.setLabelFont(new Font("宋体", Font.BOLD, 12));
        // 设置Y轴坐标上的文字
        valueAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD, 12));

        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    }

    private Map createActivityTable(String title, List<HashMap<String, Object>> resultMap,List<Dictionary> ActivitycatalogType) {
        StringBuffer sb = new StringBuffer();
        int value = -1;
        int dfi = -1;
        HashMap hash = new HashMap();
        int t = 0;
        for (HashMap<String, Object> row : resultMap) {
            List<Object> list1 = new ArrayList<Object>();
            Collection<Object> list=row.values();
            list1.add(row.get("dep"));
            int j = 0;
            int i = 0;
            int count = 0;
            for (Dictionary d : ActivitycatalogType) {
                Object rs_value = row.get("z" + i);
                if (rs_value != null) {
                    count += Integer.parseInt(rs_value.toString());
                }
                i++;
            }
            for (Dictionary d : ActivitycatalogType) {
                Object rs_value = row.get("z" + j);
                value = 0;
                if (rs_value != null) {
                    value = Integer.parseInt(rs_value.toString());
                }
                double bfb = 0;
                if (count != 0) {
                    bfb = (double) value / count * 100;
                }
                dfi = (int)Math.rint(bfb);
                list1.add(value+"("+dfi+"%)");
                j++;
            }
            hash.put(t, list1);
            t++;
        }
        return hash;
    }
}
