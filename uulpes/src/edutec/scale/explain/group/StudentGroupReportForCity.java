package edutec.scale.explain.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.util.MathUtils;

import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exam.ExamResult;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.ScaleUtils;
import heracles.jfree.ChartConstants;
import heracles.jfree.JChartParam;
import heracles.util.Arith;
import heracles.util.SimpleCodec;
import heracles.util.TreeNode;
import heracles.util.UtilMisc;
import heracles.web.util.HtmlTable;

public class StudentGroupReportForCity {
    protected Scale scale;
    protected List<String> dimTitles;
    protected Questionnaire questionnaire;
    protected Map<String, WholeStat> wholeStatMap = new HashMap<String, WholeStat>();
    protected Map<String, Map<String, WarningStat>> warningStatMaps = new HashMap<String, Map<String, WarningStat>>();
    private long currentUserId = 0;
    private int gradeorderid;
    private String cityname;
    private String njmc;
    private String[] qxArray;
    private String[] qxmcArray;
    private int totalNum = 0;// 总样本人数

    private int wn1 = 0, wnp1 = 0, wnm1 = 0, wnmp1 = 0, wnw1 = 0, wnwp1 = 0;
    private int wn2 = 0, wnp2 = 0, wnm2 = 0, wnmp2 = 0, wnw2, wnwp2 = 0;
    private int wn3 = 0, wnp3 = 0, wnm3 = 0, wnmp3 = 0, wnw3, wnwp3 = 0;

    private DimStat dimStat0;// 总分维度统计值
    private Map<String, DimStat> dimStatMap;// 各维度总分统计值

    public StudentGroupReportForCity(String cityname, String[] qxArray, String scaleid, String njmc, String starttime,
            String endtime) {
        CachedScaleMgr cachedScaleMgr = (CachedScaleMgr) SpringContextHolder.getBean("CachedScaleMgr",
                CachedScaleMgr.class);
        scale = cachedScaleMgr.get(scaleid, false);
        int dimSize = scale.getDimensionSize();
        dimStatMap = new HashMap<String, DimStat>();
        int columnCount = 8;
        if (scale.isWarningOrNot())
            columnCount = 6;
        for (Dimension dim : scale.getDimensions()) {
            Map<String, WarningStat> warningStatMap = new HashMap<String, WarningStat>();
            warningStatMaps.put(dim.getId(), warningStatMap);

            for (int i = 1; i < columnCount; i++) {
                WarningStat ws = new WarningStat();
                warningStatMap.put(i + "", ws);
            }
        }
        // 以下计算总样本数
        this.cityname = cityname;
        this.njmc = njmc;
        this.qxArray = qxArray;
        qxmcArray = new String[qxArray.length];
        // List schoollist = Arrays.asList(schoolnames);
        DistrictService districtService = (DistrictService) SpringContextHolder.getBean("DistrictService",
                DistrictService.class);
        ExamResultMapper examResultMapper = (ExamResultMapper) SpringContextHolder.getBean("examResultMapper",
                ExamResultMapper.class);
        for (int i = 0; i < qxArray.length; i++) {
            String countyid = qxArray[i];
            District district = districtService.selectByCode(countyid);
            qxmcArray[i] = district.getName();
            Map<?, ?> param = UtilMisc.toMap("countyid", countyid, "scaleid", scaleid, "njmc", njmc, "starttime",
                    starttime, "endtime", endtime);
            List<Map> stuCountMapList = examResultMapper.queryStuGroupStaticForCity(param);
            WholeStat wholeStat = new WholeStat();
            for (Map countMap : stuCountMapList) {
                String xbm = countMap.get("xbm").toString();
                int num = Integer.parseInt(countMap.get("count").toString());
                if (xbm.equals("1"))
                    wholeStat.setnMaleTotal(num);
                if (xbm.equals("2"))
                    wholeStat.setnFemaleTotal(num);
            }
            totalNum += wholeStat.getnTotal();// 总样本数
            wholeStatMap.put(district.getName(), wholeStat);
        }
    }

    public void incExmresult(ExamResult examResult) {
        if (currentUserId != examResult.getUserId()) {
            // LieItem lieItem = LieThink.getLieItem(scale.getId());
            // if (examResult.getValidVal() == 0 && (lieItem == null ||
            // lieItem.getType() != LieItem.Detected.dimension)) {
            // incNInvalid();
            // } else {
            // long classid = examResult.getUserClassId();
            // String njmc = examResult.getNjmc();
            String qxmc = examResult.getQxmc();

            // WholeStat wholeStat = wholeStatMap.get(classid+"");
            WholeStat wholeStat = wholeStatMap.get(qxmc);

            incOne(examResult, wholeStat);// 有效男、女计数
            // openQuestionnaire(examResult);
            incImpl(examResult);// 预警计数
            currentUserId = examResult.getUserId();

            // }
        }
    }

    public Map<Object, Object> complete() {
        Map<Object, Object> page = new HashMap<Object, Object>();
        String wholeStaticTable;
        wholeStaticTable = buildWholeStaticTable1();
        // wholeStaticTable = buildWholeStaticTable();
        String rootDimWarningStatTable = buildRootDimWarningStaticTable();
        String rootChartUrl = builderRootChartURL();
        List<String> descListW0 = buildDescripterWithW0();
        String maleChart = this.builderGenderChartURL(0);
        String femaleChart = this.builderGenderChartURL(1);
        String desc5 = "";
        String desc4 = "";
        if (!scale.isWarningOrNot()) {
            desc5 = buildDescripterWithoutW0(5);
            desc4 = buildDescripterWithoutW0(4);
        }
        String desc3 = buildDescripterWithoutW0(3);
        String desc2 = buildDescripterWithoutW0(2);
        String desc1 = buildDescripterWithoutW0(1);

        page.put("wholeStaticTable", wholeStaticTable);
        page.put("rootDimWarningStatTable", rootDimWarningStatTable);
        // page.put("rootChartUrl", rootChartUrl);
        page.put("descListW0", descListW0);
        // page.put("maleChart", maleChart);
        // page.put("femaleChart", femaleChart);
        boolean hasDimensionWithoutW0 = false;
        Map<String, DimStat> rootDimStatMap = new HashMap<String, DimStat>();
        for (Dimension dim : scale.getDimensions()) {
            if (dim.isRoot()) {
                if (!Dimension.SUM_SCORE_DIM.equals(dim.getId())) {
                    hasDimensionWithoutW0 = true;
                    break;
                }
            }
        }

        if (hasDimensionWithoutW0) {
            page.put("rootChartUrl", rootChartUrl);
            page.put("maleChart", maleChart);
            page.put("femaleChart", femaleChart);
            if (!scale.isWarningOrNot()) {
                page.put("desc5", desc5);
                page.put("desc4", desc4);
            }
            page.put("desc3", desc3);
            page.put("desc2", desc2);
            page.put("desc1", desc1);
        }
        // page.put("descripter", scale.getDescn());
        // page.put("title", scale.getTitle()+"量表团体测评报告");
        page.put("scale", scale);
        return page;
    }

    protected void openQuestionnaire(ExamResult examResult) {
        questionnaire = examResult.toNewQuestionnaire(null);
        questionnaire.setLoadExplain(false);
        // detect.setQuestionnaire(questionnaire);
    }

    // 预警计数
    protected void incImpl(ExamResult examResult) {
        String xb = examResult.getUserGender();
        String dimScores = examResult.getDimScore();
        String[] dimScoreArray = dimScores.split("#");
        // List<WarningStat> warningStatList = new ArrayList<WarningStat>();
        for (int i = 0; i < dimScoreArray.length; i++) {
            // WarningStat warningStat = new WarningStat();
            String score = dimScoreArray[i];
            String[] scores = score.split(",");
            String wid = scores[0];
            String gradeLevel;
            if (scale.isWarningOrNot())
                gradeLevel = scores[4];// 得分水平
            else
                gradeLevel = scores[3];
            // String warningLevel = scores[3];//预警水平
            Map<String, WarningStat> warningStatMap = warningStatMaps.get(wid);
            WarningStat ws = warningStatMap.get(gradeLevel);
            if (ws == null) {
                ws = new WarningStat();
                if (xb.equals("1") || xb.equals("男")) {
                    ws.incMaleNum();
                }
                if (xb.equals("2") || xb.equals("女")) {
                    ws.incFemaleNum();
                }
                warningStatMap.put(gradeLevel, ws);
            } else {
                if (xb.equals("1") || xb.equals("男"))
                    ws.incMaleNum();
                if (xb.equals("2") || xb.equals("女"))
                    ws.incFemaleNum();
            }
        }
    }

    /**
     * 创建样本统计表--只有一个班级
     * 
     * @return
     */
    public String buildWholeStaticTable() {
        HtmlTable table = new HtmlTable("表1" + cityname + njmc + "测评对象统计量");
        // table.setTableStyle("width='90%' border='0' cellspacing='1' " +
        // "cellpadding='0' class='table_style'");
        table.setTableStyle("width='400px'");
        table.setTdStyle("align=center");
        table.setSubHeadTdStyle("align=center");
        table.setColHeadStyle("align=center bgcolor=#A2ECFF");
        table.setLabTdStyle("align=center");
        table.setSubHeadTdStyle("align=center bgcolor=#A2ECFF");
        TreeNode tn1 = new TreeNode("男");
        TreeNode tn2 = new TreeNode("女");
        TreeNode tn3 = new TreeNode("合计");
        table.addHeadFactorTitiles(new String[] { "性别" });
        table.addSubheadTitles(new String[] { "样本", "有效数据" });
        table.addColTitles(new String[][] { { "N", "%" }, { "N", "%" } });
        table.addNode(tn1);
        table.addNode(tn2);
        table.addNode(tn3);

        WholeStat wholeStat = wholeStatMap.get(njmc);
        List<Object> values = new ArrayList<Object>();

        values.add(wholeStat.getnMaleTotal() + "");
        double temp = ((float) wholeStat.getnMaleTotal() / wholeStat.getnTotal()) * 100;
        temp = MathUtils.round(temp, 2);
        values.add(temp + "");
        values.add(wholeStat.getnValidMale() + "");
        temp = ((float) wholeStat.getnValidMale() / wholeStat.getnTotal()) * 100;
        temp = MathUtils.round(temp, 2);
        values.add(temp + "");
        tn1.setValues(values);

        values = new ArrayList<Object>();
        values.add(wholeStat.getnFemaleTotal() + "");
        temp = ((float) wholeStat.getnFemaleTotal() / wholeStat.getnTotal()) * 100;
        temp = MathUtils.round(temp, 2);
        values.add(temp + "");
        values.add(wholeStat.getnValidFemale() + "");
        temp = ((float) wholeStat.getnValidFemale() / wholeStat.getnTotal()) * 100;
        temp = MathUtils.round(temp, 2);
        values.add(temp + "");
        tn2.setValues(values);

        values = new ArrayList<Object>();
        values.add(wholeStat.getnTotal() + "");
        temp = ((float) wholeStat.getnTotal() / wholeStat.getnTotal()) * 100;
        temp = MathUtils.round(temp, 2);
        values.add(temp + "");
        values.add(wholeStat.getnValid() + "");
        temp = ((float) wholeStat.getnValid() / wholeStat.getnTotal()) * 100;
        temp = MathUtils.round(temp, 2);
        values.add(temp + "");
        tn3.setValues(values);

        System.out.println(table);

        return table.toString();
    }

    /**
     * 创建样本统计表--多个班级
     * 
     * @return
     */
    public String buildWholeStaticTable1() {
        HtmlTable table = new HtmlTable("表1" + cityname + njmc + "测评对象统计量");
        table.setTableStyle("width='400px'");
        table.setTdStyle("align=center");
        table.setSubHeadTdStyle("align=center");
        table.setColHeadStyle("align=center bgcolor=#A2ECFF");
        table.setLabTdStyle("align=center");
        table.setSubHeadTdStyle("align=center bgcolor=#A2ECFF");
        table.addHeadFactorTitiles(new String[] { "部门/班级", "性别" });
        table.addSubheadTitles(new String[] { "样本", "有效数据" });
        table.addColTitles(new String[][] { { "N", "%" }, { "N", "%" } });
        int totalMale = 0;
        double totalMaleP = 0.0;
        int totalFemale = 0;
        double totalFemaleP = 0.0;
        int totalValidMale = 0;
        double totalValidMaleP = 0.0;
        int totalValidFemale = 0;
        double totalValidFemaleP = 0.0;
        for (int i = 0; i < this.qxmcArray.length + 1; i++) {
            List<Object> values = null;
            if (i == qxmcArray.length) {
                TreeNode tn0 = new TreeNode("合计");
                TreeNode tn1 = new TreeNode("男");
                TreeNode tn2 = new TreeNode("女");
                table.addNode(tn0);
                tn0.addChild(tn1);
                totalMaleP =  Arith.div(totalMale*100, (totalMale + totalFemale), 2);
                totalFemaleP =  Arith.div(totalFemale*100, (totalMale + totalFemale), 2);
                totalValidMaleP = Arith.div(totalValidMale*100, (totalValidMale + totalValidFemale), 2);
                totalValidFemaleP =  Arith.div(totalValidFemale*100, (totalValidMale + totalValidFemale), 2);
                values = new ArrayList<Object>();
                values.add(totalMale);
                values.add(totalMaleP);
                values.add(totalValidMale);
                values.add(totalValidMaleP);
                tn1.setValues(values);
                tn0.addChild(tn2);
                values = new ArrayList<Object>();
                values.add(totalFemale);
                values.add(totalFemaleP);
                values.add(totalValidFemale);
                values.add(totalValidFemaleP);
                tn2.setValues(values);
                break;
            }
            TreeNode tn0 = new TreeNode(qxmcArray[i]);
            TreeNode tn1 = new TreeNode("男");
            TreeNode tn2 = new TreeNode("女");
            table.addNode(tn0);
            tn0.addChild(tn1);
            tn0.addChild(tn2);

            WholeStat wholeStat = wholeStatMap.get(qxmcArray[i]);
            values = new ArrayList<Object>();

            values.add(wholeStat.getnMaleTotal() + "");
            totalMale += wholeStat.getnMaleTotal();
            double temp = ((float) wholeStat.getnMaleTotal() / wholeStat.getnTotal()) * 100;
            temp = MathUtils.round(temp, 2);
            values.add(temp + "");
            values.add(wholeStat.getnValidMale() + "");
            totalValidMale += wholeStat.getnValidMale();
            temp = ((float) wholeStat.getnValidMale() / wholeStat.getnTotal()) * 100;
            temp = MathUtils.round(temp, 2);
            values.add(temp + "");
            tn1.setValues(values);

            values = new ArrayList<Object>();
            values.add(wholeStat.getnFemaleTotal() + "");
            totalFemale += wholeStat.getnFemaleTotal();
            temp = ((float) wholeStat.getnFemaleTotal() / wholeStat.getnTotal()) * 100;
            temp = MathUtils.round(temp, 2);
            values.add(temp + "");
            values.add(wholeStat.getnValidFemale() + "");
            totalValidFemale += wholeStat.getnValidFemale();
            temp = ((float) wholeStat.getnValidFemale() / wholeStat.getnTotal()) * 100;
            temp = MathUtils.round(temp, 2);
            values.add(temp + "");
            tn2.setValues(values);

        }
        return table.toString();
    }

    /**
     * 创建一级维度告警统计表
     * 
     * @return
     */
    public String buildRootDimWarningStaticTable() {
        HtmlTable table = new HtmlTable();
        if (scale.isWarningOrNot())
            table.setCaption("表2 " + cityname + njmc + "学生测评预警结果统计");
        else
            table.setCaption("表2 " + cityname + njmc + "学生测评得分结果统计");
        table.setTableStyle("width='100%'");
        table.setTdStyle("align=center");
        table.setSubHeadTdStyle("align=center");
        table.setColHeadStyle("align=center bgcolor=#A2ECFF");
        table.setLabTdStyle("align=center");
        table.setSubHeadTdStyle("align=center bgcolor=#A2ECFF");
        table.addHeadFactorTitiles(new String[] { "量表维度" });
        if (scale.isWarningOrNot()) {
            table.addSubheadTitles(new String[] { "预警级别", "一级", "二级", "三级", "总计" });
            table.addColTitles(new String[][] { { "性别" }, { "男", "女" }, { "男", "女" }, { "男", "女" }, { "男", "女" } });
        } else {
            table.addSubheadTitles(new String[] { "得分水平", "一级", "二级", "三级", "四级", "五级", "总计" });
            table.addColTitles(new String[][] { { "性别" }, { "男", "女" }, { "男", "女" }, { "男", "女" }, { "男", "女" },
                    { "男", "女" }, { "男", "女" } });
        }
        for (Dimension dim : scale.getDimensions()) {
            if (dim.isRoot()) {
                if (!Dimension.SUM_SCORE_DIM.equals(dim.getId())) {
                    DimStat dimStat = new DimStat();
                    String dimTitle = dim.getTitle();
                    String wid = dim.getId();
                    TreeNode tn0 = new TreeNode(dimTitle);
                    table.addNode(tn0);
                    TreeNode tn1 = new TreeNode();
                    TreeNode tn2 = new TreeNode();
                    tn0.addChild(tn1);
                    tn0.addChild(tn2);
                    List<Object> values = new ArrayList<Object>();
                    List<Object> values1 = new ArrayList<Object>();
                    Map<String, WarningStat> warningStatMap = warningStatMaps.get(wid);
                    int tempNMale = 0, tempNFemale = 0;
                    double tempNMaleP = 0.0, tempNFemaleP = 0.0;
                    WarningStat ws = warningStatMap.get("1");
                    if (scale.isWarningOrNot()) {
                        values.add("检出数");
                        values1.add("检出率");
                    } else {
                        values.add("数量");
                        values1.add("百分比");
                    }
                    values.add(ws.getAmount());
                    dimStat.setWnm1(ws.getMaleNum());// 一级预警检出男
                    double temp = ((float) dimStat.getWnm1() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnmp1(temp);
                    values1.add(temp);
                    tempNMale += ws.getMaleNum();
                    tempNMaleP += temp;
                    // ws = findWarningStat(warningStatList,"1","1");
                    values.add(ws.getFemaleNum());
                    dimStat.setWnw1(ws.getFemaleNum());// 一级预警检出女
                    temp = ((float) dimStat.getWnw1() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnwp1(temp);
                    tempNFemale += ws.getFemaleNum();
                    tempNFemaleP += temp;
                    values1.add(temp);
                    dimStat.setWn1(dimStat.getWnm1() + dimStat.getWnw1());
                    temp = ((float) dimStat.getWn1() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnp1(temp);

                    ws = warningStatMap.get("2");
                    values.add(ws.getMaleNum());
                    dimStat.setWnm2(ws.getMaleNum());// 二级预警检出男
                    temp = ((float) dimStat.getWnm2() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnmp1(temp);
                    tempNMale += ws.getMaleNum();
                    tempNMaleP += temp;
                    values1.add(temp);
                    // ws = findWarningStat(warningStatList,"2","1");
                    values.add(ws.getFemaleNum());
                    dimStat.setWnw2(ws.getFemaleNum());// 二级预警检出女
                    temp = ((float) dimStat.getWnw2() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnwp2(temp);
                    tempNFemale += ws.getFemaleNum();
                    tempNFemaleP += temp;
                    values1.add(temp);
                    dimStat.setWn2(dimStat.getWnm2() + dimStat.getWnw2());
                    temp = ((float) dimStat.getWn2() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnp2(temp);

                    // ws = findWarningStat(warningStatList,"3","0");
                    ws = warningStatMap.get("3");
                    values.add(ws.getMaleNum());
                    dimStat.setWnm3(ws.getMaleNum());// 一级预警检出男
                    temp = ((float) dimStat.getWnm3() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnmp3(temp);
                    tempNMale += ws.getMaleNum();
                    tempNMaleP += temp;
                    values1.add(temp);
                    values.add(ws.getFemaleNum());
                    dimStat.setWnw3(ws.getFemaleNum());// 一级预警检出女
                    temp = ((float) dimStat.getWnw3() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnwp3(temp);
                    tempNFemale += ws.getFemaleNum();
                    tempNFemaleP += temp;
                    values1.add(temp);
                    dimStat.setWn3(dimStat.getWnm3() + dimStat.getWnw3());
                    temp = ((float) dimStat.getWn3() / totalNum) * 100;
                    temp = MathUtils.round(temp, 2);
                    dimStat.setWnp3(temp);

                    if (!scale.isWarningOrNot()) {
                        ws = warningStatMap.get("4");
                        values.add(ws.getMaleNum());
                        dimStat.setWnm4(ws.getMaleNum());// 一级预警检出男
                        temp = ((float) dimStat.getWnm4() / totalNum) * 100;
                        temp = MathUtils.round(temp, 2);
                        dimStat.setWnmp4(temp);
                        tempNMale += ws.getMaleNum();
                        tempNMaleP += temp;
                        values1.add(temp);
                        values.add(ws.getFemaleNum());
                        dimStat.setWnw4(ws.getFemaleNum());// 一级预警检出女
                        temp = ((float) dimStat.getWnw4() / totalNum) * 100;
                        temp = MathUtils.round(temp, 2);
                        dimStat.setWnwp4(temp);
                        tempNFemale += ws.getFemaleNum();
                        tempNFemaleP += temp;
                        values1.add(temp);
                        dimStat.setWn4(dimStat.getWnm4() + dimStat.getWnw4());
                        temp = ((float) dimStat.getWn4() / totalNum) * 100;
                        temp = MathUtils.round(temp, 2);
                        dimStat.setWnp4(temp);

                        ws = warningStatMap.get("5");
                        values.add(ws.getMaleNum());
                        dimStat.setWnm5(ws.getMaleNum());// 一级预警检出男
                        temp = ((float) dimStat.getWnm5() / totalNum) * 100;
                        temp = MathUtils.round(temp, 2);
                        dimStat.setWnmp5(temp);
                        tempNMale += ws.getMaleNum();
                        tempNMaleP += temp;
                        values1.add(temp);
                        values.add(ws.getFemaleNum());
                        dimStat.setWnw5(ws.getFemaleNum());// 一级预警检出女
                        temp = ((float) dimStat.getWnw5() / totalNum) * 100;
                        temp = MathUtils.round(temp, 2);
                        dimStat.setWnwp5(temp);
                        tempNFemale += ws.getFemaleNum();
                        tempNFemaleP += temp;
                        values1.add(temp);
                        dimStat.setWn5(dimStat.getWnm5() + dimStat.getWnw5());
                        temp = ((float) dimStat.getWn5() / totalNum) * 100;
                        temp = MathUtils.round(temp, 2);
                        dimStat.setWnp5(temp);
                    }
                    dimStatMap.put(dimTitle, dimStat);

                    values.add(tempNMale);
                    values.add(tempNFemale);
                    tempNMaleP = MathUtils.round(tempNMaleP, 2);
                    values1.add(tempNMaleP);
                    tempNFemaleP = MathUtils.round(tempNFemaleP, 2);
                    values1.add(tempNFemaleP);

                    tn1.setValues(values);
                    tn2.setValues(values1);
                }

            }
        }

        // 总分维度
        Dimension dim = scale.getDimensionMap().get(Dimension.SUM_SCORE_DIM);
        if (dim != null) {

            DimStat dimStat = new DimStat();
            TreeNode tn0 = new TreeNode("总分");
            TreeNode tn1 = new TreeNode();
            TreeNode tn2 = new TreeNode();
            table.addNode(tn0);
            tn0.addChild(tn1);
            tn0.addChild(tn2);
            List<Object> values = new ArrayList<Object>();
            List<Object> values1 = new ArrayList<Object>();
            Map<String, WarningStat> warningStatMap = warningStatMaps.get(Dimension.SUM_SCORE_DIM);
            int tempNMale = 0, tempNFemale = 0;
            double tempNMaleP = 0.0, tempNFemaleP = 0.0;
            // WarningStat ws = findWarningStat(warningStatList,"1","0");
            WarningStat ws = warningStatMap.get("1");
            if (scale.isWarningOrNot()) {
                values.add("检出数");
                values1.add("检出率");
            } else {
                values.add("数量");
                values1.add("百分比");
            }
            dimStat.setWnm1(ws.getMaleNum());// 一级预警检出男
            double temp = ((float) dimStat.getWnm1() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnmp1(temp);
            values.add(ws.getMaleNum());
            values1.add(temp);
            tempNMale += ws.getMaleNum();
            tempNMaleP += temp;
            // ws = findWarningStat(warningStatList,"1","1");
            values.add(ws.getFemaleNum());
            dimStat.setWnw1(ws.getFemaleNum());// 一级预警检出女
            temp = ((float) dimStat.getWnw1() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnwp1(temp);
            tempNFemale += ws.getFemaleNum();
            // temp = ((float)ws.getFemaleNum()/totalNum)*100;
            // temp = MathUtils.round(temp,2);
            tempNFemaleP += temp;
            values1.add(temp);
            dimStat.setWn1(dimStat.getWnm1() + dimStat.getWnw1());
            temp = ((float) dimStat.getWn1() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnp1(temp);
            // sb.append("一级预警为为").append(wn1).append("人，占总样本的").append("XX%").append("；其中男生").append("").append("名，占总数据的").append("").append("；女生").append("").append("名，占总数据的").append("").append("%。");

            ws = warningStatMap.get("2");
            dimStat.setWnm2(ws.getMaleNum());// 一级预警检出男
            temp = ((float) dimStat.getWnm2() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnmp2(temp);
            values.add(ws.getMaleNum());
            values1.add(temp);
            tempNMale += ws.getMaleNum();
            tempNMaleP += temp;
            // ws = findWarningStat(warningStatList,"1","1");
            values.add(ws.getFemaleNum());
            dimStat.setWnw2(ws.getFemaleNum());// 一级预警检出女
            temp = ((float) dimStat.getWnw2() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnwp2(temp);
            tempNFemale += ws.getFemaleNum();
            tempNFemaleP += temp;
            values1.add(temp);
            dimStat.setWn2(dimStat.getWnm2() + dimStat.getWnw2());
            temp = ((float) dimStat.getWn2() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnp2(temp);
            // sb.append("一级预警为为"

            ws = warningStatMap.get("3");
            dimStat.setWnm3(ws.getMaleNum());// 一级预警检出男
            temp = ((float) dimStat.getWnm3() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnmp3(temp);
            values.add(ws.getMaleNum());
            values1.add(temp);
            tempNMale += ws.getMaleNum();
            tempNMaleP += temp;
            // ws = findWarningStat(warningStatList,"1","1");
            values.add(ws.getFemaleNum());
            dimStat.setWnw3(ws.getFemaleNum());// 一级预警检出女
            temp = ((float) dimStat.getWnw3() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnwp3(temp);
            tempNFemale += ws.getFemaleNum();
            tempNFemaleP += temp;
            values1.add(temp);
            dimStat.setWn3(dimStat.getWnm3() + dimStat.getWnw3());
            temp = ((float) dimStat.getWn3() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnp3(temp);

            if (!scale.isWarningOrNot()) {
                ws = warningStatMap.get("4");
                values.add(ws.getMaleNum());
                dimStat.setWnm4(ws.getMaleNum());// 一级预警检出男
                temp = ((float) dimStat.getWnm4() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnmp4(temp);
                tempNMale += ws.getMaleNum();
                tempNMaleP += temp;
                values1.add(temp);
                values.add(ws.getFemaleNum());
                dimStat.setWnw4(ws.getFemaleNum());// 一级预警检出女
                temp = ((float) dimStat.getWnw4() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnwp4(temp);
                tempNFemale += ws.getFemaleNum();
                tempNFemaleP += temp;
                values1.add(temp);
                dimStat.setWn4(dimStat.getWnm4() + dimStat.getWnw4());
                temp = ((float) dimStat.getWn4() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnp4(temp);

                ws = warningStatMap.get("5");
                values.add(ws.getMaleNum());
                dimStat.setWnm5(ws.getMaleNum());// 一级预警检出男
                temp = ((float) dimStat.getWnm5() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnmp5(temp);
                tempNMale += ws.getMaleNum();
                tempNMaleP += temp;
                values1.add(temp);
                values.add(ws.getFemaleNum());
                dimStat.setWnw5(ws.getFemaleNum());// 一级预警检出女
                temp = ((float) dimStat.getWnw5() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnwp5(temp);
                tempNFemale += ws.getFemaleNum();
                tempNFemaleP += temp;
                values1.add(temp);
                dimStat.setWn5(dimStat.getWnm5() + dimStat.getWnw5());
                temp = ((float) dimStat.getWn5() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnp5(temp);
            }

            dimStatMap.put("总分", dimStat);

            values.add(tempNMale);
            values.add(tempNFemale);
            tempNMaleP = MathUtils.round(tempNMaleP, 2);
            values1.add(tempNMaleP);
            tempNFemaleP = MathUtils.round(tempNFemaleP, 2);
            values1.add(tempNFemaleP);

            tn1.setValues(values);
            tn2.setValues(values1);
        }
        return table.toString();
    }

    /**
     * 创建二级维度告警统计表
     * 
     * @return
     */
    public String buildSubDimWarningStatTable(String id) {
        Dimension dim = scale.findDimension(id);
        List<Dimension> dimList = dim.getSubdimensions();
        if (dimList == null)
            return null;
        HtmlTable table = new HtmlTable();
        table.setTableStyle("class='statictablestyle'");

        table.addHeadFactorTitiles(new String[] { "量表维度" });
        if (!scale.isWarningOrNot()) {
            table.addSubheadTitles(new String[] { "预警级别", "一级", "二级", "三级", "四级", "五级", "总计" });
            table.addColTitles(new String[][] { { "性别" }, { "男", "女" }, { "男", "女" }, { "男", "女" }, { "男", "女" },
                    { "男", "女" }, { "男", "女" } });
        }
        for (Dimension subDim : dimList) {
            DimStat dimStat = new DimStat();
            String dimTitle = subDim.getTitle();
            String wid = subDim.getId();
            TreeNode tn0 = new TreeNode(dimTitle);
            table.addNode(tn0);
            TreeNode tn1 = new TreeNode();
            TreeNode tn2 = new TreeNode();
            tn0.addChild(tn1);
            tn0.addChild(tn2);
            List<Object> values = new ArrayList<Object>();
            List<Object> values1 = new ArrayList<Object>();
            Map<String, WarningStat> warningStatMap = warningStatMaps.get(wid);
            int tempNMale = 0, tempNFemale = 0;
            double tempNMaleP = 0.0, tempNFemaleP = 0.0;
            // WarningStat ws = findWarningStat(warningStatList,"1","0");
            WarningStat ws = warningStatMap.get("1");
            if (scale.isWarningOrNot()) {
                values.add("检出数");
                values1.add("检出率");
            } else {
                values.add("数量");
                values1.add("百分比");
            }
            values.add(ws.getMaleNum());
            dimStat.setWnm1(ws.getMaleNum());// 一级预警检出男
            double temp = ((float) dimStat.getWnm1() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnmp1(temp);
            temp = ((float) ws.getMaleNum() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            values1.add(temp);
            tempNMale += ws.getMaleNum();
            tempNMaleP += temp;
            // ws = findWarningStat(warningStatList,"1","1");
            dimStat.setWnw1(ws.getFemaleNum());// 一级预警检出女
            temp = ((float) dimStat.getWnw1() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnwp1(temp);
            values.add(ws.getFemaleNum());
            tempNFemale += ws.getFemaleNum();
            temp = ((float) ws.getFemaleNum() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            tempNFemaleP += temp;
            values1.add(temp);
            dimStat.setWn1(dimStat.getWnm1() + dimStat.getWnw1());
            temp = ((float) dimStat.getWn1() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnp1(temp);

            // ws = findWarningStat(warningStatList,"2","0");
            ws = warningStatMap.get("2");
            values.add(ws.getMaleNum());
            dimStat.setWnm2(ws.getMaleNum());// 一级预警检出男
            temp = ((float) dimStat.getWnm2() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnmp1(temp);
            temp = ((float) ws.getMaleNum() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            values1.add(temp);
            tempNMale += ws.getMaleNum();
            tempNMaleP += temp;
            // ws = findWarningStat(warningStatList,"1","1");
            dimStat.setWnw2(ws.getFemaleNum());// 一级预警检出女
            temp = ((float) dimStat.getWnw2() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnwp2(temp);
            values.add(ws.getFemaleNum());
            tempNFemale += ws.getFemaleNum();
            temp = ((float) ws.getFemaleNum() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            tempNFemaleP += temp;
            values1.add(temp);
            dimStat.setWn1(dimStat.getWnm2() + dimStat.getWnw2());
            temp = ((float) dimStat.getWn2() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnp2(temp);

            // ws = findWarningStat(warningStatList,"3","0");
            ws = warningStatMap.get("3");
            values.add(ws.getMaleNum());
            dimStat.setWnm3(ws.getMaleNum());// 一级预警检出男
            temp = ((float) dimStat.getWnm3() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnmp3(temp);
            temp = ((float) ws.getMaleNum() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            values1.add(temp);
            tempNMale += ws.getMaleNum();
            tempNMaleP += temp;
            // ws = findWarningStat(warningStatList,"1","1");
            dimStat.setWnw3(ws.getFemaleNum());// 一级预警检出女
            temp = ((float) dimStat.getWnw3() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnwp3(temp);
            values.add(ws.getFemaleNum());
            tempNFemale += ws.getFemaleNum();
            temp = ((float) ws.getFemaleNum() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            tempNFemaleP += temp;
            values1.add(temp);
            dimStat.setWn3(dimStat.getWnm3() + dimStat.getWnw3());
            temp = ((float) dimStat.getWn3() / totalNum) * 100;
            temp = MathUtils.round(temp, 2);
            dimStat.setWnp3(temp);

            if (!scale.isWarningOrNot()) {
                ws = warningStatMap.get("4");
                values.add(ws.getMaleNum());
                dimStat.setWnm4(ws.getMaleNum());// 一级预警检出男
                temp = ((float) dimStat.getWnm4() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnmp4(temp);
                tempNMale += ws.getMaleNum();
                tempNMaleP += temp;
                values1.add(temp);
                values.add(ws.getFemaleNum());
                dimStat.setWnw4(ws.getFemaleNum());// 一级预警检出女
                temp = ((float) dimStat.getWnw4() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnwp4(temp);
                tempNFemale += ws.getFemaleNum();
                tempNFemaleP += temp;
                values1.add(temp);
                dimStat.setWn4(dimStat.getWnm4() + dimStat.getWnw4());
                temp = ((float) dimStat.getWn4() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnp4(temp);

                ws = warningStatMap.get("5");
                values.add(ws.getMaleNum());
                dimStat.setWnm5(ws.getMaleNum());// 一级预警检出男
                temp = ((float) dimStat.getWnm5() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnmp5(temp);
                tempNMale += ws.getMaleNum();
                tempNMaleP += temp;
                values1.add(temp);
                values.add(ws.getFemaleNum());
                dimStat.setWnw5(ws.getFemaleNum());// 一级预警检出女
                temp = ((float) dimStat.getWnw5() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnwp5(temp);
                tempNFemale += ws.getFemaleNum();
                tempNFemaleP += temp;
                values1.add(temp);
                dimStat.setWn5(dimStat.getWnm5() + dimStat.getWnw5());
                temp = ((float) dimStat.getWn5() / totalNum) * 100;
                temp = MathUtils.round(temp, 2);
                dimStat.setWnp5(temp);
            }

            dimStatMap.put(dimTitle, dimStat);

            values.add(tempNMale);
            values.add(tempNFemale);
            tempNMaleP = MathUtils.round(tempNMaleP, 2);
            values1.add(tempNMaleP);
            values1.add(tempNFemaleP);
            tempNFemaleP = MathUtils.round(tempNFemaleP, 2);
            tn1.setValues(values);
            tn2.setValues(values1);
        }
        return table.toString();
    }

    /**
     * 创建一级维度告警统计图，不分男女
     * 
     * @return
     */
    protected String builderRootChartURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("mdata=");
        for (Dimension dim : scale.getDimensions()) {
            if (dim.isRoot()) {
                if (!Dimension.SUM_SCORE_DIM.equals(dim.getId())) {
                    int c1 = 0, c2 = 0, c3 = 0;
                    String dimTitle = dim.getTitle();
                    String wid = dim.getId();
                    Map<String, WarningStat> warningStatMap = warningStatMaps.get(wid);
                    WarningStat ws = warningStatMap.get("1");
                    c1 = ws.getAmount();
                    sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("一级")).append(":")
                            .append(c1).append(";");

                    ws = warningStatMap.get("2");
                    c2 = ws.getAmount();
                    sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("二级")).append(":")
                            .append(c2).append(";");
                    ws = warningStatMap.get("3");
                    c3 = ws.getAmount();
                    sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("三级")).append(":")
                            .append(c3).append(";");
                    if (!scale.isWarningOrNot()) {
                        int c4 = 0, c5 = 0;
                        ws = warningStatMap.get("4");
                        c4 = ws.getAmount();
                        sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("四级")).append(":")
                                .append(c4).append(";");

                        ws = warningStatMap.get("5");
                        c5 = ws.getAmount();
                        sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("五级")).append(":")
                                .append(c5).append(";");

                    }
                }

            }
        }
        sb.append("&cht=mbr&chs=600x400");
        return sb.toString();
    }

    /**
     * 创建一级维度告警统计图，分男女
     * 
     * @return
     */
    protected String builderGenderChartURL(int type) {
        StringBuilder sb = new StringBuilder();
        sb.append("mdata=");
        for (Dimension dim : scale.getDimensions()) {
            if (dim.isRoot()) {
                if (!Dimension.SUM_SCORE_DIM.equals(dim.getId())) {
                    int c1 = 0, c2 = 0, c3 = 0;
                    String dimTitle = dim.getTitle();
                    String wid = dim.getId();
                    Map<String, WarningStat> warningStatMap = warningStatMaps.get(wid);
                    WarningStat ws;
                    ws = warningStatMap.get("1");
                    if (type == 0)
                        c1 = ws.getMaleNum();
                    else
                        c1 = ws.getFemaleNum();
                    sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("一级")).append(":")
                            .append(c1).append(";");

                    ws = warningStatMap.get("2");
                    if (type == 0)
                        c1 = ws.getMaleNum();
                    else
                        c1 = ws.getFemaleNum();
                    sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("二级")).append(":")
                            .append(c1).append(";");

                    ws = warningStatMap.get("3");
                    if (type == 0)
                        c1 = ws.getMaleNum();
                    else
                        c1 = ws.getFemaleNum();
                    sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("三级")).append(":")
                            .append(c1).append(";");

                    if (!scale.isWarningOrNot()) {
                        int c4, c5;
                        ws = warningStatMap.get("4");
                        if (type == 0)
                            c4 = ws.getMaleNum();
                        else
                            c4 = ws.getFemaleNum();
                        sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("四级")).append(":")
                                .append(c4).append(";");
                        ws = warningStatMap.get("5");
                        if (type == 0)
                            c5 = ws.getMaleNum();
                        else
                            c5 = ws.getFemaleNum();
                        sb.append(SimpleCodec.enhex(dimTitle)).append(":").append(SimpleCodec.enhex("五级")).append(":")
                                .append(c5).append(";");

                    }
                }

            }
        }
        sb.append("&cht=mbr&chs=800x300");
        return sb.toString();
    }

    /**
     * 创建总分维度结果解释
     * 
     * @return
     */
    private List<String> buildDescripterWithW0() {
        List<String> descListW0 = new ArrayList<String>();
        StringBuilder sb;
        if (hasTotl()) {
            DimStat dimStat = dimStatMap.get("总分");
            if (scale.isWarningOrNot()) {
                sb = new StringBuilder();
                sb.append("三级预警为").append(dimStat.getWn3()).append("人，占总样本的").append(dimStat.getWnp3()).append("%；其中男生")
                        .append(dimStat.getWnm3()).append("名，占总数据的").append(dimStat.getWnmp3()).append("%；女生")
                        .append(dimStat.getWnw3()).append("名，占总数据的").append(dimStat.getWnwp3()).append("%。");
                descListW0.add(sb.toString());
                sb = new StringBuilder();
                sb.append("二级预警为").append(dimStat.getWn2()).append("人，占总样本的").append(dimStat.getWnp2()).append("%；其中男生")
                        .append(dimStat.getWnm2()).append("名，占总数据的").append(dimStat.getWnmp2()).append("%；女生")
                        .append(dimStat.getWnw2()).append("名，占总数据的").append(dimStat.getWnwp2()).append("%。");
                descListW0.add(sb.toString());
                sb = new StringBuilder();
                sb.append("一级预警为").append(dimStat.getWn1()).append("人，占总样本的").append(dimStat.getWnp1()).append("%；其中男生")
                        .append(dimStat.getWnm1()).append("名，占总数据的").append(dimStat.getWnmp1()).append("%；女生")
                        .append(dimStat.getWnw1()).append("名，占总数据的").append(dimStat.getWnwp1()).append("%。");
                descListW0.add(sb.toString());
            } else {
                sb = new StringBuilder();
                sb.append("得分等级为五级的有").append(dimStat.getWn5()).append("人，占总样本的").append(dimStat.getWnp5())
                        .append("%；其中男生").append(dimStat.getWnm5()).append("名，占总数据的").append(dimStat.getWnmp5())
                        .append("%；女生").append(dimStat.getWnw5()).append("名，占总数据的").append(dimStat.getWnwp5())
                        .append("%。");
                descListW0.add(sb.toString());
                sb = new StringBuilder();
                sb.append("得分等级为四级的有").append(dimStat.getWn4()).append("人，占总样本的").append(dimStat.getWnp4())
                        .append("%；其中男生").append(dimStat.getWnm4()).append("名，占总数据的").append(dimStat.getWnmp4())
                        .append("%；女生").append(dimStat.getWnw4()).append("名，占总数据的").append(dimStat.getWnwp4())
                        .append("%。");
                descListW0.add(sb.toString());
                sb = new StringBuilder();
                sb.append("得分等级为三级的有").append(dimStat.getWn3()).append("人，占总样本的").append(dimStat.getWnp3())
                        .append("%；其中男生").append(dimStat.getWnm3()).append("名，占总数据的").append(dimStat.getWnmp3())
                        .append("%；女生").append(dimStat.getWnw3()).append("名，占总数据的").append(dimStat.getWnwp3())
                        .append("%。");
                descListW0.add(sb.toString());
                sb = new StringBuilder();
                sb.append("得分等级为二级的有").append(dimStat.getWn2()).append("人，占总样本的").append(dimStat.getWnp2())
                        .append("%；其中男生").append(dimStat.getWnm2()).append("名，占总数据的").append(dimStat.getWnmp2())
                        .append("%；女生").append(dimStat.getWnw2()).append("名，占总数据的").append(dimStat.getWnwp2())
                        .append("%。");
                descListW0.add(sb.toString());
                sb = new StringBuilder();
                sb.append("得分等级为一级的有").append(dimStat.getWn1()).append("人，占总样本的").append(dimStat.getWnp1())
                        .append("%；其中男生").append(dimStat.getWnm1()).append("名，占总数据的").append(dimStat.getWnmp1())
                        .append("%；女生").append(dimStat.getWnw1()).append("名，占总数据的").append(dimStat.getWnwp1())
                        .append("%。");
                descListW0.add(sb.toString());
            }
        }

        return descListW0;
    }

    private HashMap<String, String> buildDimDescripter() {

        return null;
    }

    private String buildDescripterWithoutW0(final int level) {

        Map<String, DimStat> rootDimStatMap = new HashMap<String, DimStat>();
        for (Dimension dim : scale.getDimensions()) {
            if (dim.isRoot()) {
                if (!Dimension.SUM_SCORE_DIM.equals(dim.getId())) {
                    DimStat ds = dimStatMap.get(dim.getTitle());
                    rootDimStatMap.put(dim.getTitle(), ds);
                }
            }
        }
        // Dimension pDim = scale.findDimension(parentWid);
        // List<Dimension> subDimensions = pDim.getSubdimensions();
        // Map<String,DimStat> subDimStatMap = new HashMap<String,DimStat>();
        // for(Dimension d :subDimensions){
        // DimStat ds = dimStatMap.get(d.getTitle());
        // subDimStatMap.put(d.getTitle(), ds);
        // }

        // DimStat ds = dimStatMap.get(dim.getTitle());
        List<Map.Entry<String, DimStat>> dimStatList = new ArrayList<Map.Entry<String, DimStat>>(
                rootDimStatMap.entrySet());

        Collections.sort(dimStatList, new Comparator<Map.Entry<String, DimStat>>() {
            public int compare(Map.Entry<String, DimStat> o1, Map.Entry<String, DimStat> o2) {
                if (level == 3)
                    return o2.getValue().getWn3() - o1.getValue().getWn3();
                if (level == 2)
                    return o2.getValue().getWn2() - o1.getValue().getWn2();
                if (level == 1)
                    return o2.getValue().getWn1() - o1.getValue().getWn1();
                if (level == 4)
                    return o2.getValue().getWn4() - o1.getValue().getWn4();
                if (level == 5)
                    return o2.getValue().getWn5() - o1.getValue().getWn5();
                return 0;
            }
        });
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (scale.isWarningOrNot()) {
            if (level == 1)
                sb1.append("一级预警检出人数最多的是");
            if (level == 2)
                sb1.append("二级预警检出人数最多的是");
            if (level == 3)
                sb1.append("三级预警检出人数最多的是");
        } else {
            if (level == 1)
                sb1.append("一级得分检出人数最多的是");
            if (level == 2)
                sb1.append("二级得分检出人数最多的是");
            if (level == 3)
                sb1.append("三级得分检出人数最多的是");
            if (level == 4)
                sb1.append("四级得分检出人数最多的是");
            if (level == 5)
                sb1.append("五级得分检出人数最多的是");
        }
        for (int i = 0; i < dimStatList.size(); i++) {
            Map.Entry<String, DimStat> kv = dimStatList.get(i);
            if (level == 1) {
                if (i == 0) {
                    sb1.append(kv.getKey()).append("。然后依次是");
                    sb2.append("说明该班学生在").append(kv.getKey()).append("的问题最严重，检出数为").append(kv.getValue().getWn1());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp1()).append("%;其中男生").append(kv.getValue().getWnm1())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp1()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw1()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp1()).append("%;");
                } else if (i == dimStatList.size() - 1) {
                    sb1.append("维度").append(kv.getKey()).append("。");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn1());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp1()).append("%;其中男生").append(kv.getValue().getWnm1())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp1()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw1()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp1()).append("%。");
                } else {
                    sb1.append("维度").append(kv.getKey()).append("、");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn1());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp1()).append("%;其中男生").append(kv.getValue().getWnm1())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp1()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw1()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp1()).append("%;");
                }
            }
            if (level == 2) {
                if (i == 0) {
                    sb1.append(kv.getKey()).append("。然后依次是");
                    sb2.append("说明该班学生在").append(kv.getKey()).append("的问题最严重，检出数为").append(kv.getValue().getWn2());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp2()).append("%;其中男生").append(kv.getValue().getWnm2())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp2()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw1()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp2()).append("%;");
                } else if (i == dimStatList.size() - 1) {
                    sb1.append("维度").append(kv.getKey()).append("。");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn2());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp2()).append("%;其中男生").append(kv.getValue().getWnm2())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp2()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw2()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp2()).append("%。");
                } else {
                    sb1.append("维度").append(kv.getKey()).append("、");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn2());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp2()).append("%;其中男生").append(kv.getValue().getWnm2())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp2()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw2()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp2()).append("%;");
                }
            }
            if (level == 3) {
                if (i == 0) {
                    sb1.append(kv.getKey()).append("。然后依次是");
                    sb2.append("说明该班学生在").append(kv.getKey()).append("的问题最严重，检出数为").append(kv.getValue().getWn3());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp3()).append("%;其中男生").append(kv.getValue().getWnm3())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp3()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw3()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp3()).append("%;");
                } else if (i == dimStatList.size() - 1) {
                    sb1.append("维度").append(kv.getKey()).append("。");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn3());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp3()).append("%;其中男生").append(kv.getValue().getWnm3())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp3()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw3()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp3()).append("%。");
                } else {
                    sb1.append("维度").append(kv.getKey()).append("、");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn3());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp3()).append("%;其中男生").append(kv.getValue().getWnm3())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp3()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw3()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp3()).append("%;");
                }
            }

            if (level == 4) {
                if (i == 0) {
                    sb1.append(kv.getKey()).append("。然后依次是");
                    sb2.append("说明该班学生在").append(kv.getKey()).append("的问题最严重，检出数为").append(kv.getValue().getWn4());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp4()).append("%;其中男生").append(kv.getValue().getWnm4())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp4()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw4()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp4()).append("%;");
                } else if (i == dimStatList.size() - 1) {
                    sb1.append("维度").append(kv.getKey()).append("。");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn4());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp4()).append("%;其中男生").append(kv.getValue().getWnm4())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp4()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw4()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp4()).append("%。");
                } else {
                    sb1.append("维度").append(kv.getKey()).append("、");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn4());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp4()).append("%;其中男生").append(kv.getValue().getWnm4())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp4()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw4()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp4()).append("%;");
                }
            }

            if (level == 5) {
                if (i == 0) {
                    sb1.append(kv.getKey()).append("。然后依次是");
                    sb2.append("说明该班学生在").append(kv.getKey()).append("的问题最严重，检出数为").append(kv.getValue().getWn5());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp5()).append("%;其中男生").append(kv.getValue().getWnm5())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp5()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw5()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp5()).append("%;");
                } else if (i == dimStatList.size() - 1) {
                    sb1.append("维度").append(kv.getKey()).append("。");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn5());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp5()).append("%;其中男生").append(kv.getValue().getWnm5())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp5()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw5()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp5()).append("%。");
                } else {
                    sb1.append("维度").append(kv.getKey()).append("、");
                    sb2.append("然后依次是维度").append(kv.getKey()).append("，检出数为").append(kv.getValue().getWn5());
                    sb2.append("人，占总样本的");
                    sb2.append(kv.getValue().getWnp5()).append("%;其中男生").append(kv.getValue().getWnm5())
                            .append("名，占总数据的");
                    sb2.append(kv.getValue().getWnmp5()).append("%;");
                    sb2.append("女生").append(kv.getValue().getWnw5()).append("名，占总数据的");
                    sb2.append(kv.getValue().getWnwp5()).append("%;");
                }
            }
        }
        sb1.append(sb2);
        return sb1.toString();

    }

    protected JChartParam createBarChartParam() {
        return createChartParam(ChartConstants.CHART_BAR);
    }

    private String findMaxWarningDimTitle(int level) {
        int max = 0;
        String maxDimTitle = "";
        Iterator iter = dimStatMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            String dimTitle = key.toString();
            Object val = entry.getValue();
            DimStat dimStat = (DimStat) val;
            int wn1 = dimStat.getWn1();
            int wn2 = dimStat.getWn2();
            int wn3 = dimStat.getWn3();
            if (level == 1) {
                if (wn1 > max) {
                    max = wn1;
                    maxDimTitle = dimTitle;
                }
            }
            if (level == 2)
                if (wn2 > max) {
                    max = wn2;
                    maxDimTitle = dimTitle;
                }
            if (level == 3)
                if (wn3 > max) {
                    max = wn3;
                    maxDimTitle = dimTitle;
                }
        }
        return maxDimTitle;
    }

    private JChartParam createChartParam(String type) {
        JChartParam chartParam = new JChartParam();
        chartParam.setChartType(type);
        chartParam.setOutlineVisible(false);
        chartParam.setWidth(500);
        chartParam.setHeight(470);
        if (this.questionnaire.getDimensionSize() > 5) {
            chartParam.setLabp(90);
        }
        return chartParam;
    }

    /*
     * private WarningStat findWarningStat(List<WarningStat> warningList,String
     * level,String xb){ for(WarningStat ws :warningList){
     * if(ws.getXb()==xb&&ws.getgLevel()== level) { return ws; } } return null;
     * }
     */
    /**
     * 计总数、男、女
     * 
     * @param examResult
     */
    private void incOne(ExamResult examResult, WholeStat wholeStat) {
        String userGender = examResult.getUserGender();
        if (userGender.equals("1")) {
            wholeStat.incNValidMale();// 有效男
        } else if (userGender.equals("2")) {
            wholeStat.incNValidFemale();// 有效女
        }
    }

    public boolean hasTotl() {

        if (ScaleUtils.isThirdAngleScale(scale.getId())) {
            return false;
        }
        if (ScaleUtils.isAbilityScale(scale.getId())) {
            return false;
        }
        return scale.findDimension(Dimension.SUM_SCORE_DIM) != null;
    }

    public String getScaleTitle() {
        return scale.getTitle();
    }
}
