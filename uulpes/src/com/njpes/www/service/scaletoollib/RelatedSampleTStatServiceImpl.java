package com.njpes.www.service.scaletoollib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.StatDependentVars;
import com.njpes.www.entity.scaletoollib.StatObject;
import com.njpes.www.entity.scaletoollib.StatParams;
import com.njpes.www.entity.scaletoollib.StatResult;
import com.njpes.www.entity.scaletoollib.StatResultData;
import com.njpes.www.entity.scaletoollib.StatScope;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.GradeCodeServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolClassServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.util.StatUtilServiceI;

import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import net.sf.json.JSONArray;

@Service("RelatedSampleTStatService")
public class RelatedSampleTStatServiceImpl {

    @Autowired
    ExamresultStudentMapper examresultStundentMapper;

    @Autowired
    ExamresultTeacherMapper examresultTeacherMapper;

    @Autowired
    StatUtilServiceI statUtilService;

    @Autowired
    SchoolClassServiceI classService;

    @Autowired
    GradeCodeServiceI gradeService;

    @Autowired
    SchoolServiceI schoolService;

    @Autowired
    DistrictService districtService;

    // 拼总表的标题
    private String composeScaleTableName() {
        return "相关样本T检验";
    }

    // 获得第一行的表头
    private List<String> getTitles() {
        return new ArrayList<String>() {
            {
                add("");
                add("N");
                add("M");
                add("SD.");
                add("t");
            }
        };
    }

    private void intersect(List<ExamresultHuman> es1, List<ExamresultHuman> es2, Map<String, Integer> sexMap) {
        List<Long> stuids = new ArrayList<Long>();
        sexMap.put("1", 0);
        sexMap.put("2", 0);
        for (ExamresultHuman es : es1) {
            Long stuid = es.getUser_id();
            if (!stuids.contains(stuid)) {
                stuids.add(stuid);
                String gender = es.getGender();
                sexMap.put(gender, sexMap.get(gender) + 1);
            }
        }
        for (ExamresultHuman es : es2) {
            Long stuid = es.getUser_id();
            if (!stuids.contains(stuid)) {
                stuids.add(stuid);
                String gender = es.getGender();
                sexMap.put(gender, sexMap.get(gender) + 1);
            }
        }
    }

    /**
     * 根据配置组合出统计的表头
     * 
     * @param params
     * @return
     */
    public String getStatTitle(StatParams params) {
        String title = ""; // 标题
        StatScope scope = params.getScope(); // 测评参数
        String cityid = scope.getCityId(); // 市id
        List<String> countyid = scope.getCountyId(); // 区县id
        List<String> townid = scope.getTownId(); // 乡镇id
        List<String> schoolid = scope.getSchoolId(); // 学校id
        String gradeid = scope.getGradeId(); // 年级id
        String classid = scope.getClassId(); // 班级id
        int statojbid = params.getStatObj(); // 统计对象是学生还是老师
        if (StringUtils.isNotEmpty(classid)) {
            title = title + classService.findById(Long.parseLong(classid)).getBjmc() + "班";
        }
        if (StringUtils.isNotEmpty(gradeid)) {
            title = gradeService.getGradecodeTitile(Integer.parseInt(gradeid)).getTitle() + title;
        }
        if (schoolid != null && schoolid.size() > 0) {
            String t = "";
            for (int i = 0; i < schoolid.size(); i++) {
                if (i >= 10) {
                    t = t.substring(0, t.length() - 1) + "";
                    t = t + "等" + schoolid.size() + "所学校";
                    break;
                }
                String sid = schoolid.get(i);
                if (StringUtils.isNotEmpty(sid))
                    t = t + schoolService.selectByPrimaryKey(Integer.parseInt(sid)).getXxmc() + ",";
            }
            if (StringUtils.isNotEmpty(t))
                title = t + title;
        }
        if (townid != null && townid.size() > 0) {
            String t = "";
            for (int i = 0; i < townid.size(); i++) {
                if (i >= 10) {
                    t = t.substring(0, t.length() - 1) + "";
                    t = t + "等" + townid.size() + "个乡镇";
                    break;
                }
                String tid = townid.get(i);
                if (StringUtils.isNotEmpty(tid))
                    t = t + districtService.selectByCode(tid).getName() + ",";
            }
            if (StringUtils.isNotEmpty(t))
                title = t + title;
        }
        if (countyid != null && countyid.size() > 0) {
            String t = "";
            for (int i = 0; i < countyid.size(); i++) {
                if (i >= 10) {
                    t = t.substring(0, t.length() - 1) + "";
                    t = t + "等" + countyid.size() + "个区县";
                    break;
                }
                String cid = countyid.get(i);
                if (StringUtils.isNotEmpty(cid))
                    t = t + districtService.selectByCode(cid).getName() + ",";
            }
            if (StringUtils.isNotEmpty(t))
                title = t + title;
        }
        if (StringUtils.isNotEmpty(cityid) && townid != null && townid.size() > 0 && countyid != null
                && countyid.size() > 0) {
            title = districtService.selectByCode(cityid).getName() + title;
        }
        if (title.endsWith(","))
            title = title.substring(0, title.length() - 2);
        return title;
    }

    /**
     * 把多个测评结果重新组合成按照{{维度1id，{用户1:[成绩1，成绩2，……]}}，{维度2id，{用户2：[成绩1，成绩2，……]}}的格式
     * 
     * @param rdss
     *            所有的测评结果
     * @param dimIds
     *            所有的维度id
     * @return
     */
    private Map<String, Map<String, Float>> dimScores(List<ExamresultHuman> rdss, JSONArray dimIds) {
        Map<String, Map<String, Float>> dimscores = new HashMap<String, Map<String, Float>>();
        for (int i = 0; i < rdss.size(); i++) {
            ExamresultHuman rds = rdss.get(i);
            // 获得维度分
            String dimScore = rds.getDim_score();
            String user_id = Long.toString(rds.getUser_id());
            // Map<String, Float> userScore = new HashMap<String, Float>();
            if (StringUtils.isNotEmpty(dimScore)) {
                String[] dimScoreArray = dimScore.split("#");
                for (int j = 0; j < dimScoreArray.length; j++) {
                    // 切分成各维度的分数
                    String[] scores = dimScoreArray[j].split(",");
                    String dimId = scores[0];
                    if (dimIds != null && dimIds.contains(dimId)) {
                        // 该维度的维度分
                        Float score = Float.parseFloat(scores[1]);
                        // 如果结果记录不包含当前的dimid，新建一个数组
                        if (!dimscores.containsKey(dimId)) {
                            dimscores.put(dimId, new HashMap<String, Float>());
                        }
                        dimscores.get(dimId).put(user_id, score);
                    }
                }
            }

        }
        return dimscores;
    }

    /**
     * 计算测评结果中每个维度对重复的测评者的个数
     * 
     * @param dimscores1
     * @param dimscores2
     * @param dims1
     * @param dims2
     * @return
     */
    private Map<String, Map<String, Integer>> userCount(Map<String, Map<String, Float>> dimscores1,
            Map<String, Map<String, Float>> dimscores2, JSONArray dims1, JSONArray dims2) {
        Map<String, Map<String, Integer>> userCount = null;
        if (dims1 == null || dims2 == null || dims1.size() <= 0 || dims2.size() <= 0)
            return null;
        for (int i = 0; i < dims1.size(); i++) {
            // 分别取得两个维度
            String dim1 = dims1.getString(i);
            String dim2 = dims2.getString(i);
            // 再取得两个维度对应的所有测评者的维度分
            Map<String, Float> score1 = dimscores1.get(dim1);
            Map<String, Float> score2 = dimscores2.get(dim2);
            if (score1 == null)
                score1 = new HashMap<String, Float>();
            if (score2 == null)
                score2 = new HashMap<String, Float>();
            // 找到相同的用户
            Iterator<String> userIt = score1.keySet().iterator();
            while (userIt.hasNext()) {
                String user_id = userIt.next();
                if (score2.containsKey(user_id)) {
                    if (userCount == null) {
                        userCount = new HashMap<String, Map<String, Integer>>();
                    }
                    if (!userCount.containsKey(dim1)) {
                        Map<String, Integer> dim2_cnt = new HashMap<String, Integer>();
                        dim2_cnt.put(dim2, 1);
                        userCount.put(dim1, dim2_cnt);
                    } else {
                        if (!userCount.get(dim1).containsKey(dim2)) {
                            userCount.get(dim1).put(dim2, 1);
                        } else {
                            Integer cnt = userCount.get(dim1).get(dim2) + 1;
                            userCount.get(dim1).put(dim2, cnt);
                        }
                    }
                }
            }
        }
        return userCount;
    }

    /**
     * 计算每一个用户在一个维度对上的成绩差
     * 
     * @param dimscores1
     * @param dimscores2
     * @return
     */
    private Map<String, Map<String, Float[]>> computeD2(Map<String, Map<String, Float>> dimscores1,
            Map<String, Map<String, Float>> dimscores2, JSONArray dims1, JSONArray dims2) {

        Map<String, Map<String, Float[]>> d2s = null; // {dim1: {dim2:
                                                      // [sigma(d*d),
                                                      // sigma(d)*sigma(d)]}}

        for (int i = 0; i < dims1.size(); i++) {
            // 分别取得两个维度
            String dim1 = dims1.getString(i);
            String dim2 = dims2.getString(i);
            // 再取得两个维度对应的所有测评者的维度分
            Map<String, Float> score1 = dimscores1.get(dim1);
            Map<String, Float> score2 = dimscores2.get(dim2);
            if (score1 == null)
                score1 = new HashMap<String, Float>();
            if (score2 == null)
                score2 = new HashMap<String, Float>();
            // 找到相同的用户
            Set<String> userSet = score1.keySet();
            if (userSet != null && userSet.size() > 0) {
                Iterator<String> userIt = userSet.iterator();

                while (userIt.hasNext()) {
                    String user_id = userIt.next();
                    if (score2.containsKey(user_id)) {
                        // 如果两个结果中都包含同一个用户，则计算d平方
                        Float d = score1.get(user_id) - score2.get(user_id);
                        Float d2 = d * d;
                        if (d2s == null)
                            d2s = new HashMap<String, Map<String, Float[]>>();
                        if (!d2s.containsKey(dim1)) {
                            Map<String, Float[]> userD2 = new HashMap<String, Float[]>();
                            Float[] dv = new Float[2];
                            dv[0] = d2;
                            dv[1] = d;
                            userD2.put(dim2, dv);
                            d2s.put(dim1, userD2); // 如果没有dim1，说明这个维度没有计算过，赋一个新值
                        } else {
                            if (!d2s.get(dim1).containsKey(dim2)) {
                                d2s.get(dim1).put(dim2, new Float[] { d2, d }); // 如果没有dim2，说明是第一个学生成绩，直接复制d平方
                            } else {
                                Float[] dv = d2s.get(dim1).get(dim2);
                                dv[0] = dv[0] + d2;
                                dv[1] = dv[1] + d;
                                d2s.get(dim1).put(dim2, dv); // 如果有dim2，说明以前计算过其他学生，把d平方累加
                            }
                        }
                    }
                }
            } else {
                Map<String, Float[]> userD2 = new HashMap<String, Float[]>();
                d2s.put(dim1, userD2);
                Float[] dv = new Float[] { 0f, 0f };
                d2s.get(dim1).put(dim2, dv);
            }
        }
        return d2s;
    }

    // 将[{stuid,dimscore}]转为{dimid, {stuid, score}}
    private void composalScores(List<ExamresultHuman> rds1, List<ExamresultHuman> rds2, JSONArray dimIds1,
            JSONArray dimIds2, Scale scaleId1, Scale scaleId2, int taskid1, int taskid2,
            List<LinkedHashMap<String, LinkedHashMap<String, Float>>> result, Map<String, Integer> sexStatRs)
            throws Exception {
        // 重新组合结果，分解为按维度记录各自的成绩
        intersect(rds1, rds2, sexStatRs);
        Map<String, Map<String, Float>> dimscores1 = dimScores(rds1, dimIds1);
        Map<String, Map<String, Float>> dimscores2 = dimScores(rds2, dimIds2);
        // 寻找两次测评结果相同的测评人集合
        Map<String, Map<String, Integer>> uCount = userCount(dimscores1, dimscores2, dimIds1, dimIds2); // {dim1:{dim2:count}}
        if (MapUtils.isEmpty(uCount))
            throw new Exception("两次测评对象没有重叠");
        Map<String, Map<String, Float[]>> dv = computeD2(dimscores1, dimscores2, dimIds1, dimIds2); // {dim1:{dim2:[sigma(d*d),
                                                                                                    // sigma(d)*sigma(d)]}}
        if (dv != null && dv.size() > 0) {
            for (int i = 0; i < dimIds1.size(); i++) {

                // 当前的维度id
                String dimId1 = dimIds1.getString(i);
                String dimId2 = dimIds2.getString(i);
                // 两个集合的测试人数
                Integer n = uCount.get(dimId1).get(dimId2);
                if (n < 3)
                    throw new Exception("测评人数不足3人");
                // 两个集合的维度分(存在问题，维度的名字可能在两个不同的量表中是重复的)
                List<Float> scores1 = new ArrayList<Float>(dimscores1.get(dimId1).values());
                List<Float> scores2 = new ArrayList<Float>(dimscores2.get(dimId2).values());
                // 两个集合的维度分均值
                Float mean1 = statUtilService.computeMean(scores1);
                Float mean2 = statUtilService.computeMean(scores2);
                // 两个集合的维度分方差
                Float sd1 = statUtilService.computeSd(scores1);
                Float sd2 = statUtilService.computeSd(scores2);
                // 计算d求和的平方和d平方的求和
                Float[] dv_pair = dv.get(dimId1).get(dimId2);
                Float sigmaD2 = dv_pair[0];
                Float sigmaD = dv_pair[1];
                // 计算t
                Float t = (float) Math.sqrt((sigmaD2 - (sigmaD * sigmaD) / n) / (n * (n - 1)));
                t = (mean1 - mean2) / t;
                if (Float.isNaN(t))
                    continue;
                LinkedHashMap<String, LinkedHashMap<String, Float>> dimMap = new LinkedHashMap<String, LinkedHashMap<String, Float>>();
                LinkedHashMap<String, Float> row1 = new LinkedHashMap<String, Float>();
                row1.put("N", Float.valueOf(n));
                row1.put("M", mean1);
                row1.put("S.D", sd1);
                row1.put("T", t);
                // row1.put("MD", mean2-mean1);
                dimMap.put(dimId1, row1);
                LinkedHashMap<String, Float> row2 = new LinkedHashMap<String, Float>();
                row2.put("N", Float.valueOf(n));
                row2.put("M", mean2);
                row2.put("S.D", sd2);
                dimMap.put(dimId2, row2);
                result.add(dimMap);
            }
        } else {
            throw new Exception("两次测评对象没有重叠");
        }
    }

    public StatResult stat(StatParams params) throws Exception {
        // TODO Auto-generated method stub

        Integer statObj = params.getStatObj(); // 统计对象是学生还是老师
        DecimalFormat pdf = new DecimalFormat("######0.0");
        DecimalFormat ndf = new DecimalFormat("######0.00");
        StatDependentVars depVars = params.getDepVars().get(0);
        StatDependentVars depVars1 = params.getDepVars().get(1);
        Scale scale = depVars.getScaleCode(); // 选择的量表
        Scale scale1 = depVars.getScaleCode(); // 选择的对比量表
        JSONArray dimIds = depVars.getDims(); // 量表的维度ids
        JSONArray dimIds1 = depVars1.getDims(); // 量表的对比维度ids
        int taskid = depVars.getTaskid(); // 任务id
        int taskid1 = depVars1.getTaskid(); // 任务id
        if ((dimIds == null || dimIds.size() == 0) && (dimIds1 == null || dimIds1.size() == 0)) {
            // 如果用户没有选择dimIds，就把量表所有的维度都装到dimIds里
            dimIds = new JSONArray();
            dimIds1 = new JSONArray();
            List<Dimension> dims = scale.getDimensions();
            for (Dimension dim : dims) {
                dimIds.add(dim.getId());
                for (Dimension dim1 : dims) {
                    if (!dim.getId().equals(dim1.getId())) {
                        dimIds1.add(dim1.getId());
                    }
                }
            }
        }

        String scaleCode = scale.getCode(); // 量表编号
        String scaleCode1 = scale1.getCode();

        String[] scaleTitles = new String[getTitles().size() + 1]; // 量表总体指标，前面有一个空，所以+1
        Map<String, Dimension> dimMap = scale.getDimensionMap(); // 量表包含的所有维度
        Map<String, Dimension> dimMap1 = scale1.getDimensionMap(); // 量表包含的所有维度

        // 每道题的答案
        List<ExamresultHuman> rss = new ArrayList<ExamresultHuman>();
        List<ExamresultHuman> rss1 = new ArrayList<ExamresultHuman>();
        if (statObj == StatObject.STUDENT) {
            rss = examresultStundentMapper.getStudentResultsByTaskId(params.getScope(), Integer.toString(taskid));
            rss1 = examresultStundentMapper.getStudentResultsByTaskId(params.getScope(), Integer.toString(taskid1));
        } else if (statObj == StatObject.TEACHER) {
            rss = examresultTeacherMapper.getTeacherResultsByTaskId(params.getScope(), Integer.toString(taskid));
            rss1 = examresultTeacherMapper.getTeacherResultsByTaskId(params.getScope(), Integer.toString(taskid));
        }
        // 统计结果包含的人数
        Integer count = rss.size();
        Integer count1 = rss1.size();

        if (count <= 0) {

            throw new Exception(scale.getTitle() + "量表测评人数为0");

        }

        if (count1 <= 0) {

            throw new Exception(scale1.getTitle() + "量表测评人数为0");

        }

        // [1:{dim1:{标题,值},dim2:{标题,值}}, 2:……]
        List<LinkedHashMap<String, LinkedHashMap<String, Float>>> result = new ArrayList<LinkedHashMap<String, LinkedHashMap<String, Float>>>();

        HashMap<String, Integer> sexStatRs = new HashMap<String, Integer>();

        composalScores(rss, rss1, dimIds, dimIds1, scale, scale1, taskid, taskid1, result, sexStatRs);

        // 组装成统一的结果前台渲染
        StatResult sr = new StatResult();
        // 某某学校某某年级某某班级学生频次统计
        sr.setTitle("");
        List<StatResultData> allData = new ArrayList<StatResultData>();
        // 首先装统计结果
        StatResultData scaledata = new StatResultData();
        // 总表名称
        scaledata.setTitles(new ArrayList<String>());
        scaledata.getTitles().add(composeScaleTableName());
        scaledata.setDatas(new ArrayList());
        // 总表表头
        scaleTitles[0] = "维度名称";
        scaleTitles[1] = "测评时间";
        List<String> keys = getTitles();
        for (int i = 0; i < keys.size(); i++) {
            scaleTitles[i + 1] = keys.get(i);
        }
        scaledata.setDataTitles(Arrays.asList(scaleTitles.clone()));
        // 总表数据
        int row = 1;
        String title = this.getStatTitle(params);
        String explain = "";
        if (statObj == StatObject.STUDENT) {
            explain = "备注：**表示在.01水平上显著；*表示在.05水平上显著<br/>" + "共取学生样本量" + count + "人，其中：男生" + sexStatRs.get("1")
                    + "个，占总人数的" + pdf.format((float) sexStatRs.get("1") * 100 / count) + "%； 女生" + sexStatRs.get("2")
                    + "个，占总人数的" + pdf.format((float) sexStatRs.get("2") * 100 / count) + "%。";
        } else {
            explain = "备注：**表示在.01水平上显著；*表示在.05水平上显著<br/>" + "共取教师样本量" + count + "人，其中：男教师" + sexStatRs.get("1")
                    + "个，占总人数的" + pdf.format((float) sexStatRs.get("1") * 100 / count) + "%； 女教师" + sexStatRs.get("2")
                    + "个，占总人数的" + pdf.format((float) sexStatRs.get("2") * 100 / count) + "%。";
        }
        String cyExplainTitle = "<br/>结果表明：";
        String cyExplain = ""; // 差异性结果解释
//        String hlExplainTitle = "<br/>结果说明：";
//        String hlExplain = ""; // 对比结果解释
        String lastDimension = null;
        String dimNameContrast = null;
        for (int i = 0; i < result.size(); i++) {
            LinkedHashMap<String, LinkedHashMap<String, Float>> dimscore = result.get(i);
            Iterator<String> dimIdIt = dimscore.keySet().iterator();
            while (dimIdIt.hasNext()) {
                List<String> tableData = new ArrayList<String>(); // [1,维度名称,相关系数1,相关系数2,相关系数3...]
                String dimId = dimIdIt.next();
                String dimName = dimMap.get(dimId).getTitle(); // 维度名称
                LinkedHashMap<String, Float> values = dimscore.get(dimId);
                if (row % 2 == 1) {
                    if(dimName.equals(dimNameContrast)){
                        break;
                    }
                    tableData.add(dimName);
                    dimNameContrast = dimName;
                    tableData.add("第一次");
                    Float N = values.get("N");
                    Float M = values.get("M");
                    Float SD = values.get("S.D");
                    Float T = values.get("T");
                    tableData.add(pdf.format(N));
                    tableData.add(pdf.format(M));
                    tableData.add(pdf.format(SD));
                    Float tval005 = statUtilService.getTvalue("a005", N.intValue() - 1);
                    Float tval001 = statUtilService.getTvalue("a001", N.intValue() - 1);
                    if (Math.abs(T) > Math.abs(tval005)) {
                        if (Math.abs(T) > Math.abs(tval001)) {
                            tableData.add(pdf.format(T) + "**");
                            if(!dimName.equals(lastDimension)){
                                cyExplain = cyExplain + "<br/>" + title + "在维度" + dimName + "上有非常显著差异";
                            }
                            lastDimension = dimName;
//                            if (T < 0) {
//                                hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著低于维度{dim2}";
//                            } else {
//                                hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著高于维度{dim2}";
//                            }
                        } else {
                            tableData.add(pdf.format(T) + "*");
                            if(!dimName.equals(lastDimension)){
                                cyExplain = cyExplain + "<br/>" + title + "在维度" + dimName + "上有显著差异";
                            }
                            lastDimension = dimName;
//                            if (T < 0) {
//                                hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著低于维度{dim2}";
//                            } else {
//                                hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著高于维度{dim2}";
//                            }
                        }
                    } else {
                        tableData.add(pdf.format(T));
//                        hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "和维度{dim2}差异不显著";
                    }
                } else {
                    tableData.add("");
                    tableData.add("第二次");
                    Float N = values.get("N");
                    tableData.add(pdf.format(N));
                    Float M = values.get("M");
                    tableData.add(pdf.format(M));
                    Float SD = values.get("S.D");
                    tableData.add(pdf.format(SD));
                    cyExplain = cyExplain.replace("{dim2}", dimName);
//                    hlExplain = hlExplain.replace("{dim2}", dimName);
                }
                // 偶行
                /*
                 * for(int j=0;j<keys.size();j++){ String k = keys.get(j); Float
                 * v = values.get(k); if(null != v)
                 * tableData.add(pdf.format(v)); }
                 */

                scaledata.getDatas().add(tableData);
                row = row + 1;
            }

        }
        if (StringUtils.isNotEmpty(cyExplain)) {
            explain = explain + cyExplainTitle + cyExplain;
        }
        if (scaledata.getExplains() == null) {
            scaledata.setExplains(new ArrayList<String>());
        }
        scaledata.getExplains().add(explain);
        allData.add(scaledata);
        sr.setData(allData);
        return sr;
    }
}
