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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("SingleFactorVariantAnalysisService")
public class SingleFactorVariantAnalysisServiceImpl {

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
        return "单因素方差分析";
    }

    // 获得第一行的表头
    private List<String> getTitles() {
        // return new ArrayList<String>(){{add("N"); add("M"); add("S.D");
        // add("T"); add("SF"); add("P"); add("MD");}};
        return new ArrayList<String>() {
            {
                add("变异来源");
                add("平方和");
                add("自由度");
                add("均方");
                add("F");
                add("P");
            }
        };
    }

    // 获得事后验证的表头
    private List<String> getValidTitles() {
        // return new ArrayList<String>(){{add("N"); add("M"); add("S.D");
        // add("T"); add("SF"); add("P"); add("MD");}};
        return new ArrayList<String>() {
            {
                add("x(_)2");
                add("平方和");
                add("自由度");
                add("均方");
                add("F");
                add("P");
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
        // TODO 区域ID使用的是list存储，只有这里出错，先使用get(0)解决。
        String countyid = null;
        if (null != scope.getCountyId()) {
            countyid = scope.getCountyId().get(0); // 区县id
        }
        String townid = null;
        if (null != scope.getCountyId()) {
            townid = scope.getTownId().get(0); // // 乡镇id
        }
        String schoolid = scope.getSchoolId().get(0); // 学校id
        String gradeid = scope.getGradeId(); // 年级id
        String classid = scope.getClassId(); // 班级id
        int statojbid = params.getStatObj(); // 统计对象是学生还是老师
        if (StringUtils.isNotEmpty(classid)) {
            title = title + classService.findById(Long.parseLong(classid)).getBjmc() + "班";
        }
        if (StringUtils.isNotEmpty(gradeid)) {
            title = gradeService.getGradecodeTitile(Integer.parseInt(gradeid)) + title;
        }
        if (StringUtils.isNotEmpty(schoolid)) {
            title = schoolService.selectByPrimaryKey(Integer.parseInt(schoolid)).getXxmc() + title;
        }
        if (StringUtils.isNotEmpty(townid)) {
            title = districtService.selectByCode(townid).getName() + title;
        }
        if (StringUtils.isNotEmpty(countyid)) {
            title = districtService.selectByCode(countyid).getName() + title;
        }
        if (StringUtils.isNotEmpty(cityid) && StringUtils.isEmpty(townid) && StringUtils.isEmpty(countyid)) {
            title = districtService.selectByCode(cityid).getName() + title;
        }
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
        if (colValues != null && colValues.size() < 3)
            throw new RuntimeException("不能少于三个水平值");
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
                    attrObj.accumulate(kv[0], kv[1]);
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
    private void composalScores(List<ExamresultHuman> rds1, List<ExamresultHuman> rds2, JSONArray dimIds, int scaleId,
            TreeMap<String, List<TreeMap<String, Float>>> result) {
        // 重新组合结果，分解为按维度记录各自的成绩

        Map<String, List<Float>> dimscores1 = dimScores(rds1, dimIds);
        Map<String, List<Float>> dimscores2 = dimScores(rds2, dimIds);

        Iterator<String> dimIt = dimscores1.keySet().iterator();
        while (dimIt.hasNext()) {

            // 当前的维度id
            String dimId = dimIt.next();
            // 两个集合的维度分
            List<Float> scores1 = dimscores1.get(dimId);
            List<Float> scores2 = dimscores2.get(dimId);
            // 两个集合的维度分均值
            Float mean1 = statUtilService.computeMean(scores1);
            Float mean2 = statUtilService.computeMean(scores2);
            // 两个集合的维度分方差
            Float sd1 = statUtilService.computeSd(scores1);
            Float sd2 = statUtilService.computeSd(scores2);
            // 两个集合的测试人数
            Integer n1 = scores1.size();
            Integer n2 = scores2.size();
            // 计算sd
            Float sd = (float) Math.sqrt((n1 * sd1 * sd1 + n2 * sd2 * sd2) * ((n1 + n2) / n1 * n2) / (n1 + n2 - 2));
            Float t = (mean1 - mean2) / sd;
            List<TreeMap<String, Float>> rows = new ArrayList<TreeMap<String, Float>>();
            TreeMap<String, Float> row1 = new TreeMap<String, Float>();
            row1.put("N", Float.valueOf(n1));
            row1.put("M", mean1);
            row1.put("S.D", sd);
            row1.put("T", t);
            // row1.put("SF", cdf.format(n1-1));
            row1.put("P", 1 - (float) statUtilService.getPvalue(Math.abs(t)));
            // row1.put("MD", ndf.format(mean2-mean1));
            TreeMap<String, Float> row2 = new TreeMap<String, Float>();
            row2.put("N", Float.valueOf(n2));
            row2.put("M", mean2);
            row2.put("S.D", sd2);

            rows.add(row1);
            rows.add(row2);
            result.put(dimId, rows);
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
        DecimalFormat pdf = new DecimalFormat("######0.0");
        DecimalFormat cdf = new DecimalFormat("######0");
        StatInDependentVars indepVars = params.getIndpVars().get(0);
        String col = (String) indepVars.getCols().get(0); // 选择的列属性
        JSONArray vals = indepVars.getVals();

        // 读取元数据表获取一张表对应的所有背景属性
        PropObject propObject = null;
        String tableName = params.getIndpVars().get(0).getTableName(); // 学生还是教师的表名
        if ("student".equals(tableName)) {
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

        TreeMap<String, List<TreeMap<String, Float>>> result = new TreeMap<String, List<TreeMap<String, Float>>>();

        composalScores(sp1, sp2, dimIds, Integer.parseInt(scale.getId()), result);

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
        String explain = "备注：**表示在.01水平上显著；*表示在.05水平上显著<br/>" + "共取学生样本量" + count + "人，其中：男生" + sexStatRs.get("1")
                + "个，占总人数的" + pdf.format((float) sexStatRs.get("1") * 100 / count) + "%； 女生" + sexStatRs.get("2")
                + "个，占总人数的" + pdf.format((float) sexStatRs.get("2") * 100 / count) + "%。";
        String cyExplainTitle = "<br/>结果表明：";
        String cyExplain = ""; // 差异性结果解释
        String hlExplainTitle = "<br/>结果说明：";
        String hlExplain = ""; // 对比结果解释
        Iterator<String> dimIdIt = result.keySet().iterator();
        while (dimIdIt.hasNext()) {

            String dimId = dimIdIt.next();
            String dimName = dimMap.get(dimId).getTitle();
            List<TreeMap<String, Float>> valueList = result.get(dimId);
            for (int i = 0; i < valueList.size(); i++) { // 每个维度对应的是两个属性值的统计数据
                List<String> tableData = new ArrayList<String>(); // [维度名称,相关系数1,相关系数2,相关系数3...]
                tableData.add(dimName); // 维度名称
                TreeMap<String, Float> values = valueList.get(i);
                if (row % 2 == 0) {
                    if ("999".equals(col)) {
                        tableData.add(gradeService.getGradecodeTitile(vals.getInt(0)).getTitle());
                    } else {
                        tableData.add(valueOptionMap.get(vals.getString(0)).getTitle()); // 单行都是选定的属性的第一个值，例如选定性别，第一个都是男，第二个都是女
                    } // tableData.add(Integer.toString((((Double)Math.ceil(((double)row)/2)).intValue())));
                    Float N = values.get("N");
                    tableData.add(pdf.format(N));
                    Float M = values.get("M");
                    tableData.add(pdf.format(M));
                    Float SD = values.get("S.D");
                    tableData.add(pdf.format(SD));
                    Float T = values.get("T");
                    tableData.add(pdf.format(T));
                    Float P = values.get("P");
                    tableData.add(pdf.format(P));
                    Float tval005 = statUtilService.getTvalue("a005", N.intValue() - 1);
                    Float tval001 = statUtilService.getTvalue("a001", N.intValue() - 1);
                    if (T > tval005) {
                        cyExplain = cyExplain + "<br/>" + title + "在维度" + dimName + "和维度{dim2}上有显著差异";
                        if (T < 0) {
                            hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著低于维度{dim2}";
                        } else {
                            hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著高于维度{dim2}";
                        }
                    }
                    if (T > tval001) {
                        cyExplain = cyExplain + "<br/>" + title + "在维度" + dimName + "和维度{dim2}有非常显著差异";
                        if (T < 0) {
                            hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著低于维度{dim2}";
                        } else {
                            hlExplain = hlExplain + "<br/>" + title + "维度" + dimName + "得分显著高于维度{dim2}";
                        }
                    }
                } else {
                    if ("999".equals(col)) {
                        tableData.add(gradeService.getGradecodeTitile(vals.getInt(1)).getTitle());
                    } else {
                        tableData.add(valueOptionMap.get(vals.getString(1)).getTitle()); // 单行都是选定的属性的第一个值，例如选定性别，第一个都是男，第二个都是女
                    }
                    Float N = values.get("N");
                    tableData.add(pdf.format(N));
                    Float M = values.get("M");
                    tableData.add(pdf.format(M));
                    Float SD = values.get("S.D");
                    tableData.add(pdf.format(SD));
                    cyExplain = cyExplain.replace("{dim2}", dimName);
                    hlExplain = hlExplain.replace("{dim2}", dimName);
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
            explain = explain + cyExplainTitle + cyExplain + hlExplainTitle + hlExplain;
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
