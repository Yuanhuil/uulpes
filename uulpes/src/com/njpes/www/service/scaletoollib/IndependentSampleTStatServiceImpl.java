package com.njpes.www.service.scaletoollib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.entity.baseinfo.attr.SelectOption;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.baseinfo.organization.GradeCode;
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.StatDependentVars;
import com.njpes.www.entity.scaletoollib.StatInDependentVars;
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
import edutec.scale.util.ScaleUtils;
import heracles.util.Arith;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("IndependentSampleTStatService")
public class IndependentSampleTStatServiceImpl {

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
        return "独立样本t检验";
    }

    // 获得第一行的表头
    private List<String> getTitles() {
        // return new ArrayList<String>(){{add("N"); add("M"); add("S.D");
        // add("T"); add("SF"); add("P"); add("MD");}};
        return new ArrayList<String>() {
            {
                add("N");
                add("M");
                add("S.D");
                add("t");
            }
        };
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
            title = title + classService.findById(Long.parseLong(classid)).getBjmc();
        }
        if (StringUtils.isNotEmpty(gradeid)) {
            title = gradeService.getGradecodeTitile(Integer.parseInt(gradeid)).getTitle() + title;
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

    // 将全部的结果按照选择的列属性分为两个结果集
    private void splitExamResult(List<ExamresultHuman> whole, List<ExamresultHuman> sp1, List<ExamresultHuman> sp2,
            String colName, JSONArray colValues, Map<String, Integer> sexStatRs) {
        if (sp1 == null)
            sp1 = new ArrayList<ExamresultHuman>();
        if (sp2 == null)
            sp2 = new ArrayList<ExamresultHuman>();
        if (sexStatRs == null)
            sexStatRs = new HashMap<String, Integer>();
        sexStatRs.put("1", 0);
        sexStatRs.put("2", 0);
        if (colValues != null && colValues.size() > 2)
            throw new RuntimeException("不能多于两个水平值");
        for (ExamresultHuman ess : whole) {
            // 统计性别比例
            String gender = ess.getGender();
            sexStatRs.put(gender, sexStatRs.get(gender) + 1);
            // 获得学生的属性字段值
            JSONObject attrObj = new JSONObject();
            String bj = ess.getAttrs();
            if (StringUtils.isNotEmpty(bj)) {
                String[] bjArray = bj.split(" ");
                for (int j = 0; j < bjArray.length; j++) {
                    String[] kv = bjArray[j].split("=");
                    if (kv.length>1) {
                        attrObj.accumulate(kv[0], kv[1]);
                    }else{
                        attrObj.accumulate(kv[0], null);
                    }
                }
            }
            // attrObj = JSONObject.fromObject(jsonAttrs);
            if (attrObj.containsKey(colName)) {
                String v = attrObj.getString(colName);
                // 如果背景信息值包含在选择的两个背景信息值里，那么分别加到两个分开的结果集中
                int index = colValues.indexOf(v);
                if (index == 0) {
                    sp1.add(ess);
                } else if (index == 1) {
                    sp2.add(ess);
                }
            } else if ("999".equals(colName)) {
                // 选择是年级
                Integer nj = ess.getNj();
                Integer xd = ess.getXd();
                Integer gradecode = GradeCode.getGradecodeByXdNj(xd, nj);
                int index = colValues.indexOf(Integer.toString(gradecode));
                if (index == 0) {
                    sp1.add(ess);
                } else if (index == 1) {
                    sp2.add(ess);
                }
            }
        }
    }

    /**
     * 把多个测评结果重新组合成按照{{维度1id，[成绩1，成绩2，……]}，{维度2id，[成绩1，成绩2，……]}的格式
     * 
     * @param rdss
     *            所有的测评结果
     * @param dimIds
     *            所有的维度id
     * @return
     */
    private Map<String, List<Float>> dimScores(List<ExamresultHuman> rdss, JSONArray dimIds) {
        Map<String, List<Float>> dimscores = new HashMap<String, List<Float>>();
        for (int i = 0; i < rdss.size(); i++) {
            ExamresultHuman rds = rdss.get(i);
            // 获得维度分
            String dimScore = rds.getDim_score();
            if (StringUtils.isNotEmpty(dimScore)) {
                String[] dimScoreArray = dimScore.split("#");
                for (int j = 0; j < dimScoreArray.length; j++) {
                    // 切分成各维度的分数
                    String[] scores = dimScoreArray[j].split(",");
                    String dimId = scores[0];
                    if (dimIds.contains(dimId)) {
                        // 该维度的维度分
                        Float score = Float.parseFloat(scores[1]);
                        // 如果结果记录不包含当前的dimid，新建一个数组
                        if (!dimscores.containsKey(dimId)) {
                            List<Float> scoreList = new ArrayList<Float>();
                            dimscores.put(dimId, scoreList);
                        }
                        dimscores.get(dimId).add(score);
                    }
                }
            }

        }
        return dimscores;
    }

    // 将[{stuid,dimscore}]转为{dimid, {stuid, score}}
    private void composalScores(List<ExamresultHuman> rds1, List<ExamresultHuman> rds2, JSONArray dimIds,
            String scaleId, TreeMap<String, List<TreeMap<String, Float>>> result) {
        // 重新组合结果，分解为按维度记录各自的成绩

        Map<String, List<Float>> dimscores1 = dimScores(rds1, dimIds);
        Map<String, List<Float>> dimscores2 = dimScores(rds2, dimIds);

        Iterator<String> dimIt = dimscores1.keySet().iterator();
        while (dimIt.hasNext()) {
            // 当前的维度id
            String dimId = dimIt.next();
            // 如果是心理健康量表，不计算总分维度
            if (ScaleUtils.isMentalHealthScale(scaleId) && dimId.trim().equals("W0")) {
                continue;
            }
            // 两个集合的维度分
            List<Float> scores1 = dimscores1.get(dimId);
            List<Float> scores2 = dimscores2.get(dimId);
            List<Float> scoresSum = new ArrayList<Float>();
            scoresSum.addAll(scores1);
            scoresSum.addAll(scores2);
            // 两个集合的维度分均值
            Float mean1 = statUtilService.computeMean(scores1);
            Float mean2 = statUtilService.computeMean(scores2);
            // 整个维度分均值
            Float meanSum = statUtilService.computeMean(scoresSum);
            // 两个集合的样本方差(公式为count-1)
            Float sn1 = statUtilService.computeSn(scores1);
            Float sn2 = statUtilService.computeSn(scores2);
            // 两个集合方差
            Float sd1 = statUtilService.computeIsd(scores1);
            Float sd2 = statUtilService.computeIsd(scores2);
            // 两个集合的维度分标准差
            Float sd3 = statUtilService.computeSd(scores1);
            Float sd4 = statUtilService.computeSd(scores2);
            //两个集合求组内方差
            Float sdSum1 = statUtilService.computeSumSd(scores1);
            Float sdSum2 = statUtilService.computeSumSd(scores2);
            // 组内方差
            Float inSd = sdSum1 + sdSum2;
            // 集合维度总方差
            Float sdSum = statUtilService.computeSumSdall(scores1,scores2);
            // 组间方差
            Float bSd = sdSum - inSd;
            // 两个集合的测试人数
            Integer n1 = scores1.size();
            Integer n2 = scores2.size();
            Integer sumN = n1 + n2;
            // 自由度
            Integer dfb = 2 - 1;
            Integer dfw = sumN - 2;
            // 均方差
            Float msb = bSd / dfb;
            Float msw = inSd / dfw;
            // 方差齐性检验F值
            Float f = msb / msw;
            // 系统表里面的F值
            Float sysF = statUtilService.getFvalue(dfb,dfw);
            //t检验
            Float t;
            // 判断样本量是否大于30
            if(sumN <= 30){
                if(f<sysF){
                    // 样本的联合方差
                    Float sp = (((n1 - 1) * sn1) + ((n2 - 1) * sn2)) / ((n1 - 1) + (n2 - 1));
                    // 样本的标准误
                    Float n1c = (float)Arith.div(1, n1);
                    Float n2c = (float)Arith.div(1, n2);
                    Float se = Float.parseFloat(String.valueOf(Math.sqrt(sp * (n1c + n2c))));
                    // t检验
                    t = (float)Arith.div((mean1 - mean2),se,3);
                    List<TreeMap<String, Float>> rows = new ArrayList<TreeMap<String, Float>>();
                    TreeMap<String, Float> row1 = new TreeMap<String, Float>();
                    row1.put("N", Float.valueOf(n1));
                    row1.put("M", mean1);
                    row1.put("S.D", sd3);
                    row1.put("T", t);
                    row1.put("sum", Float.valueOf(sumN));
                    TreeMap<String, Float> row2 = new TreeMap<String, Float>();
                    row2.put("N", Float.valueOf(n2));
                    row2.put("M", mean2);
                    row2.put("S.D", sd4);
                    rows.add(row1);
                    rows.add(row2);
                    result.put(dimId, rows);
                }else{
                    t = (mean1 - mean2) / Float.parseFloat(String.valueOf(Math.sqrt((sd1 / (n1 - 1)) + (sd2 / (n2 - 1)))));
                    Float tval005n1 = statUtilService.getTvalue("a005", n1 - 1);
                    Float tval005n2 = statUtilService.getTvalue("a005", n2 - 1);
                    Float tval001n1 = statUtilService.getTvalue("a001", n1 - 1);
                    Float tval001n2 = statUtilService.getTvalue("a001", n2 - 1);
                    //计算t'@临界值
                    Float t1aTval005 = ((sd1 / (n1 -1)) * tval005n1) + ((sd2 / (n2 -1)) * tval005n2) / (sd1 / (n1 -1)) + (sd2 / (n2 -1));
                    Float t1aTval001 = ((sd1 / (n1 -1)) * tval001n1) + ((sd2 / (n2 -1)) * tval001n2) / (sd1 / (n1 -1)) + (sd2 / (n2 -1));
                    List<TreeMap<String, Float>> rows = new ArrayList<TreeMap<String, Float>>();
                    TreeMap<String, Float> row1 = new TreeMap<String, Float>();
                    row1.put("N", Float.valueOf(n1));
                    row1.put("M", mean1);
                    row1.put("S.D", sd3);
                    row1.put("T", t);
                    row1.put("t1aTval005", t1aTval005);
                    row1.put("t1aTval001", t1aTval001);
                    TreeMap<String, Float> row2 = new TreeMap<String, Float>();
                    row2.put("N", Float.valueOf(n2));
                    row2.put("M", mean2);
                    row2.put("S.D", sd4);
                    rows.add(row1);
                    rows.add(row2);
                    result.put(dimId, rows);
                }
            }else{
                Float se = (float)Math.sqrt((sn1 / n1) + (sn2 / n2));
                Float z = (mean1 - mean2) / se;
                List<TreeMap<String, Float>> rows = new ArrayList<TreeMap<String, Float>>();
                TreeMap<String, Float> row1 = new TreeMap<String, Float>();
                row1.put("N", Float.valueOf(n1));
                row1.put("M", mean1);
                row1.put("S.D", sd3);
                row1.put("T", z);
                row1.put("sum", Float.valueOf(sumN));
                TreeMap<String, Float> row2 = new TreeMap<String, Float>();
                row2.put("N", Float.valueOf(n2));
                row2.put("M", mean2);
                row2.put("S.D", sd4);
                rows.add(row1);
                rows.add(row2);
                result.put(dimId, rows);
            }
        }
    }

    private Map<String, SelectOption> toOptionMap(List<SelectOption> opts) {
        Map<String, SelectOption> map = new HashMap<String, SelectOption>();
        for (SelectOption opt : opts) {
            map.put(opt.getValue(), opt);
        }
        return map;
    }

    public StatResult stat(StatParams params) throws Exception {
        // TODO Auto-generated method stub
        Integer statObj = params.getStatObj(); // 统计对象是学生还是老师
        DecimalFormat ndf = new DecimalFormat("######0.000");
        DecimalFormat ldf = new DecimalFormat("######0.00");
        DecimalFormat pdf = new DecimalFormat("######0.0");
        DecimalFormat cdf = new DecimalFormat("######0");
        StatInDependentVars indepVars = params.getIndpVars().get(0);
        String col = (String) indepVars.getCols().get(0); // 选择的列属性
        JSONArray vals = indepVars.getVals();

        // 读取元数据表获取一张表对应的所有背景属性
        PropObject propObject = null;
        if (statObj == StatObject.STUDENT) {
            propObject = PropObject.createPropObject(AcountTypeFlag.student.getId());
        } else {
            propObject = PropObject.createPropObject(AcountTypeFlag.teacher.getId());
        }
        propObject.loadMetas();
        List<FieldValue> cols = propObject.getFieldValues(new String[] { col });
        List<SelectOption> opts = new ArrayList<SelectOption>();
        Map<String, SelectOption> valueOptionMap = new HashMap<String, SelectOption>();
        String colName = "";
        if ("999".equals(col)) {
            colName = "年级";
        } else {
            if (cols.size() > 0) {
                FieldValue cl = cols.get(0);
                colName = cl.getLabel();
                opts = cl.getOptionList();
                valueOptionMap = toOptionMap(opts);
            }
        }
        StatDependentVars depVars = params.getDepVars().get(0);
        Scale scale = depVars.getScaleCode(); // 选择的量表
        JSONArray dimIds = depVars.getDims(); // 量表的维度ids
        if (dimIds == null || dimIds.size() == 0) {
            // 如果用户没有选择dimIds，就把量表所有的维度都装到dimIds里
            dimIds = new JSONArray();
            List<Dimension> dims = scale.getDimensions();
            for (Dimension dim : dims) {
                dimIds.add(dim.getId());
            }
        }

        String scaleCode = scale.getCode(); // 量表编号

        String[] scaleTitles = new String[getTitles().size() + 2]; // 量表总体指标，前面有一个空，所以+1
        Map<String, Dimension> dimMap = scale.getDimensionMap(); // 量表包含的所有维度

        // 每道题的答案
        List<ExamresultHuman> rss = new ArrayList<ExamresultHuman>();
        if (statObj == StatObject.STUDENT) {
            rss = examresultStundentMapper.getStudentResult(params.getScope());
        } else if (statObj == StatObject.TEACHER) {
            rss = examresultTeacherMapper.getTeacherResult(params.getScope());
        }
        List<ExamresultHuman> sp1 = new ArrayList<ExamresultHuman>();
        List<ExamresultHuman> sp2 = new ArrayList<ExamresultHuman>();
        HashMap<String, Integer> sexStatRs = new HashMap<String, Integer>(); // 统计性别
        // 将结果根据选择的列属性和两个取值分为两个结果集sp1和sp2
        splitExamResult(rss, sp1, sp2, col, vals, sexStatRs);
        // 统计结果包含的人数
        Integer count = rss.size();
        Integer count1 = sp1.size();
        Integer count2 = sp2.size();

        if (count2 <= 0 || count1 <= 0) {

            throw new Exception("没有测评记录");

        }

        if (count2 < 3 || count1 < 3)
            throw new Exception("测评记录不足3条");

        TreeMap<String, List<TreeMap<String, Float>>> result = new TreeMap<String, List<TreeMap<String, Float>>>();

        composalScores(sp1, sp2, dimIds, scale.getId(), result);

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
        // 属性列
        scaleTitles[1] = colName;

        List<String> keys = getTitles();
        for (int i = 0; i < keys.size(); i++) {
            scaleTitles[i + 2] = keys.get(i);
        }
        scaledata.setDataTitles(Arrays.asList(scaleTitles.clone()));
        // 总表数据
        int row = 2;
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
        Iterator<String> dimIdIt = result.keySet().iterator();
        String lastDimension = null;
        String dimNameContrast = null;
        while (dimIdIt.hasNext()) {
            String dimId = dimIdIt.next();
            String dimName = dimMap.get(dimId).getTitle();
            List<TreeMap<String, Float>> valueList = result.get(dimId);
            for (int i = 0; i < valueList.size(); i++) { // 每个维度对应的是两个属性值的统计数据
                List<String> tableData = new ArrayList<String>(); // [维度名称,相关系数1,相关系数2,相关系数3...]
                /*tableData.add(dimName);*/ // 维度名称
                TreeMap<String, Float> values = valueList.get(i);
                if (row % 2 == 0) {
                    if(dimName.equals(dimNameContrast)){
                        break;
                    }
                    tableData.add(dimName);
                    dimNameContrast = dimName;
                    if ("999".equals(col)) {
                        tableData.add(gradeService.getGradecodeTitile(vals.getInt(0)).getTitle());
                    } else {
                        tableData.add(valueOptionMap.get(vals.getString(0)).getTitle()); // 单行都是选定的属性的第一个值，例如选定性别，第一个都是男，第二个都是女
                    } // tableData.add(Integer.toString((((Double)Math.ceil(((double)row)/2)).intValue())));
                    String N = String.valueOf(Math.round(values.get("N")));
                    tableData.add(N);
                    Float M = values.get("M");
                    tableData.add(ldf.format(M));
                    Float SD = values.get("S.D");
                    tableData.add(ndf.format(SD));
                    Float T = values.get("T");
                    Float sum = values.get("sum");
                    Float tval005 = null;
                    Float tval001 = null;
                    if(sum != null){
                        tval005 = statUtilService.getTvalue("a005", sum.intValue() - 1);
                        tval001 = statUtilService.getTvalue("a001", sum.intValue() - 1);
                    }else{
                        Float t1aTval005 = values.get("t1aTval005");
                        Float t1aTval001 = values.get("t1aTval001");
                        tval005 = t1aTval005;
                        tval001 = t1aTval001;
                    }
                    if (Math.abs(T) > Math.abs(tval005)) {
                        if (Math.abs(T) > Math.abs(tval001)) {
                            tableData.add(ndf.format(T) + "**");
                            if(!dimName.equals(lastDimension)){
                            cyExplain = cyExplain + "<br/>" + title + "在维度α=0.01" + dimName + "和维度{dim2}有非常显著差异";
                            }
                            lastDimension = dimName;
//                            if (T < 0.0) {
//                                hlExplain = hlExplain + "<br/>" + title + "在维度α=0.01" + dimName + "得分显著低于维度{dim2}";
//                            } else {
//                                hlExplain = hlExplain + "<br/>" + title + "在维度α=0.01" + dimName + "得分显著高于维度{dim2}";
//                            }
                        } else {
                            tableData.add(ndf.format(T) + "*");
                            if(!dimName.equals(lastDimension)){
                            cyExplain = cyExplain + "<br/>" + title + "在维度α=0.05" + dimName + "和维度{dim2}上有显著差异";
                            }
                            lastDimension = dimName;
//                            if (T < 0.0) {
//                                hlExplain = hlExplain + "<br/>" + title + "在维度α=0.05" + dimName + "得分显著低于维度{dim2}";
//                            } else {
//                                hlExplain = hlExplain + "<br/>" + title + "在维度α=0.05" + dimName + "得分显著高于维度{dim2}";
//                            }
                        }
                    } else {
                        tableData.add(ndf.format(T));
                        cyExplain = cyExplain + "<br/>" + title + "在维度" + dimName + "和维度{dim2}差异不显著";
//                        if (T < 0.0) {
//                            hlExplain = hlExplain + "<br/>" + title + "在维度" + dimName + "得分显著低于维度{dim2}";
//                        } else {
//                            hlExplain = hlExplain + "<br/>" + title + "在维度" + dimName + "得分显著高于维度{dim2}";
//                        }
                    }
                } else {
                    tableData.add("");
                    if ("999".equals(col)) {
                        tableData.add(gradeService.getGradecodeTitile(vals.getInt(1)).getTitle());
                    } else {
                        tableData.add(valueOptionMap.get(vals.getString(1)).getTitle()); // 单行都是选定的属性的第一个值，例如选定性别，第一个都是男，第二个都是女
                    }
                    String N = String.valueOf(Math.round(values.get("N")));
                    tableData.add(N);
                    Float M = values.get("M");
                    tableData.add(ldf.format(M));
                    Float SD = values.get("S.D");
                    tableData.add(ndf.format(SD));
                    tableData.add("");
                    cyExplain = cyExplain.replace("{dim2}", dimName);
//                    hlExplain = hlExplain.replace("{dim2}", dimName);
                }
                /*
                 * TreeMap<String, Float> values = valueList.get(i); for(int
                 * j=0;j<keys.size();j++){ String k = keys.get(j); String v =
                 * values.get(k); if(null != v) tableData.add(v); }
                 */
                scaledata.getDatas().add(tableData);
                row = row + 1;
            }
        }
        if (StringUtils.isNotEmpty(cyExplain)) {
            explain = explain + cyExplainTitle + cyExplain;
        }
//        if (StringUtils.isNotEmpty(hlExplain)) {
//            explain = explain + hlExplainTitle + hlExplain;
//        }
        if (scaledata.getExplains() == null) {
            scaledata.setExplains(new ArrayList<String>());
        }
        scaledata.getExplains().add(explain);
        allData.add(scaledata);
        sr.setData(allData);
        return sr;
    }
}
