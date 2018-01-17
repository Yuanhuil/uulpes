package com.njpes.www.service.scaletoollib;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.scaletoollib.ExamresultDimStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.dao.scaletoollib.StatColumnMapper;
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.StatDependentVars;
import com.njpes.www.entity.scaletoollib.StatObject;
import com.njpes.www.entity.scaletoollib.StatParams;
import com.njpes.www.entity.scaletoollib.StatResult;
import com.njpes.www.entity.scaletoollib.StatResultData;
import com.njpes.www.service.util.DictionaryServiceI;
import com.njpes.www.service.util.StatUtilServiceI;

import edutec.scale.model.Dimension;
import edutec.scale.model.Option;
import edutec.scale.model.Scale;
import edutec.scale.model.SelectionQuestion;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("DescriptionStatService")
public class DescriptionStatServiceImpl {

    @Autowired
    ExamresultStudentMapper examresultStundentMapper;

    @Autowired
    ExamresultTeacherMapper examresultTeacherMapper;

    @Autowired
    ExamresultDimStudentMapper examDimStudentMapper;

    @Autowired
    StatColumnMapper colService;

    @Autowired
    DictionaryServiceI dicService;

    @Autowired
    StatUtilServiceI statUtilService;

    // 拼总表的标题
    private String composeScaleTableName() {
        return "描述统计";
    }

    // 拼每个维度的表的标题
    private String composeDimTableName(int n, String dimName) {
        return "维度" + Integer.toString(n) + ":" + dimName;
    }

    // 计算得分总和和最小值最大值平均值方差
    private void computeMeanMinMax(List<ExamresultHuman> rdss, Scale scale, JSONArray dimIds,
            Map<String, Float[]> scaleResult, TreeMap<String, TreeMap<Float, Float[]>> dimResult,
            TreeMap<String, TreeMap<String, TreeMap<String, Float[]>>> questionResult)
            throws SecurityException, NoSuchMethodException, NumberFormatException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        int count = rdss.size(); // 总人数
        Float[] mins = new Float[26]; // 记录所有维度最小值
        Arrays.fill(mins, Float.MAX_VALUE);
        Float[] maxs = new Float[26]; // 记录所有维度最大值
        Arrays.fill(maxs, Float.MIN_VALUE);
        Float[][] dimScores = new Float[26][count]; // 记录下所有的维度结果
        Float[] totalScores = new Float[26]; // 记录所有维度总和
        Arrays.fill(totalScores, 0f);
        int n = 0;
        for (ExamresultHuman rds : rdss) {
            String dimScoreStr = rds.getDim_score();
            String[] dimScoreArray = dimScoreStr.split("#");
            // 统计维度数据
            for (int i = 0; i < dimScoreArray.length; i++) {
                String[] dimMsdScores = dimScoreArray[i].split(",");
                String dimName = dimMsdScores[0]; // 维度名称W0
                Float dimScore = Float.parseFloat(dimMsdScores[1]); // 维度得分
                // Method method =
                // ExamresultDimStudentWithBackGround.class.getMethod("getW" +
                // Integer.toString(i));
                // Float dimScore = Float.parseFloat((String)method.invoke(rds,
                // null));
                if (dimScore != null) {
                    // 最小值
                    if (dimScore < mins[i]) {
                        mins[i] = dimScore;
                    }
                    // 最大值
                    if (dimScore > maxs[i]) {
                        maxs[i] = dimScore;
                    }
                    // 总和
                    totalScores[i] = totalScores[i] + dimScore;
                    // 记录成绩
                    dimScores[i][n] = dimScore;
                    // 统计维度的成绩
                    Integer index = i;
                    String indexStr = Integer.toString(index);
                    if (!dimResult.containsKey(dimName)) {
                        dimResult.put(dimName, new TreeMap<Float, Float[]>());
                    }
                    if (!dimResult.get(dimName).containsKey(dimScore)) {
                        dimResult.get(dimName).put(dimScore, new Float[] { 0f, 0f, 0f });
                    }
                    // 计数
                    dimResult.get(dimName).get(dimScore)[0] = dimResult.get(dimName).get(dimScore)[0] + 1f;
                    // 百分比
                    dimResult.get(dimName).get(dimScore)[1] = dimResult.get(dimName).get(dimScore)[0] / count;
                }
            }
            // 统计题目数据
            Map<String, Dimension> dimMap = scale.getDimensionMap();
            Map<String, String[]> questionMap = new TreeMap<String, String[]>();
            String questionScoreStr = rds.getQuestion_score();
            String[] questionAnswer = questionScoreStr.split("#");
            // 把题和答案组合成treemap
            for (int i = 0; i < questionAnswer.length; i++) {
                String[] questionAnswerArray = questionAnswer[i].split(",");
                questionMap.put(questionAnswerArray[0],
                        new String[] { questionAnswerArray[1], questionAnswerArray[2] }); // {Q1:[1,3.0]}
            }
            for (int i = 0; i < dimIds.size(); i++) {
                String dimId = dimIds.getString(i);
                Dimension dim = dimMap.get(dimId);
                // 处理维度还有子维度的情况
                List<Dimension> subdims = dim.getSubdimensions();
                String questionIds = dim.getQuestionIds();
                if (subdims != null && !subdims.isEmpty()) {
                    for (Dimension subdim : subdims) {
                        String subqids = subdim.getQuestionIds();
                        if (StringUtils.isNotEmpty(subqids))
                            questionIds = StringUtils.isNotEmpty(questionIds)
                                    ? StringUtils.join(
                                            statUtilService.merge(questionIds.split(","), subqids.split(",")), ",")
                                    : subqids;
                    }
                }
                if (StringUtils.isNotEmpty(questionIds)) {
                    String[] questionIdArray = questionIds.split(",");
                    for (int j = 0; j < questionIdArray.length; j++) {
                        String questionId = questionIdArray[j];
                        if (questionMap.containsKey(questionId)) {
                            if (!questionResult.containsKey(dimId)) {
                                questionResult.put(dimId,
                                        new TreeMap<String, TreeMap<String, Float[]>>(new Comparator() {
                                            public int compare(Object o1, Object o2) {
                                                // 如果有空值，直接返回0
                                                o1 = String.valueOf(o1).replace("Q", "");
                                                o2 = (String) String.valueOf(o2).replace("Q", "");
                                                if (o1 == null || o2 == null)
                                                    return 0;
                                                return ((String) o1).compareTo((String) o2);
                                            }
                                        }));
                            }
                            if (!questionResult.get(dimId).containsKey(questionId)) {
                                questionResult.get(dimId).put(questionId, new TreeMap<String, Float[]>());
                            }

                            Iterator<String> optIdIt = ((SelectionQuestion) scale.getQuestionMap().get(questionId))
                                    .getOptionMap().keySet().iterator();
                            // 初始化所有的选项频次和百分比为0
                            while (optIdIt.hasNext()) {
                                String optId = optIdIt.next();
                                if (!questionResult.get(dimId).get(questionId).containsKey(optId)) {
                                    questionResult.get(dimId).get(questionId).put(optId, new Float[] { 0f, 0f });
                                }
                                // 通过判断scale里的题目的选项value和答案的value是否一致来计数
                                if (optId.equals(
                                        (char) (Integer.parseInt(questionMap.get(questionId)[0].trim()) + 65) + "")) {
                                    // 统计频次和百分比
                                    Float frequency = questionResult.get(dimId).get(questionId).get(optId)[0] + 1; // 频次加1
                                    Float percent = frequency * 1.0f / count; // 重算百分比
                                    questionResult.get(dimId).get(questionId).get(optId)[0] = frequency;
                                    questionResult.get(dimId).get(questionId).get(optId)[1] = percent;
                                }
                            }
                        }
                    }
                }
            }
            n = n + 1;
        }
        // 重新组织结构，拼成map，{W0:[min,max,totolscore]}
        for (int i = 0; i < dimIds.size(); i++) {
            String dimId = dimIds.getString(i);
            Float min = mins[i];
            Float max = maxs[i];
            Float mean = totalScores[i] / count;
            Float totolSd = 0f;
            for (int j = 0; j < dimScores[i].length; j++) {
                if (dimScores[i][j] != null)
                    totolSd = totolSd + (dimScores[i][j] - mean) * (dimScores[i][j] - mean); // 求方差
            }
            scaleResult.put(dimId, new Float[] { min, max, mean, (float)Math.sqrt(totolSd) });
        }
    }

    public StatResult stat(StatParams params) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        // TODO Auto-generated method stub
        StatDependentVars depVars = params.getDepVars().get(0);
        Scale scale = depVars.getScaleCode(); // 选择的量表
        JSONArray dimIds = depVars.getDims(); // 量表的维度ids
        String scaleCode = scale.getCode(); // 量表编号
        Integer statObj = params.getStatObj(); // 统计学生还是教师

        String[] scaleTitles = new String[] { "维度", "人数", "最小值", "最大值", "平均值", "标准差" }; // 量表总体指标
        JSONObject bjObj = new JSONObject(); // 背景对象
        JSONArray scaleStatMap = new JSONArray(); // {["总分","45", "2.56",
                                                  // "3.22", "2.9",
                                                  // "0.21"],...}
        Map<String, Integer> statCount = new HashMap<String, Integer>(); // 计人数
        Map<String, Float> minDimMap = new HashMap<String, Float>(); // 量表维度得分最小值
        Map<String, Float> maxDimMap = new HashMap<String, Float>(); // 量表维度得分最大值
        Map<String, Float> mDemMap = new HashMap<String, Float>(); // 量表维度平均值
        Map<String, Float> sdDimMap = new HashMap<String, Float>(); // 量表维度得分标准差

        String[] dimTitles = new String[] { "得分", "频次", "百分比", "累计百分比" }; // 维度指标
        Map<Float, Integer> dimScoreCount = new HashMap<Float, Integer>(); // 维度得分频次统计
        Map<Float, Float> dimPercent = new HashMap<Float, Float>(); // 维度得分百分比统计

        Map<String, Dimension> dimMap = scale.getDimensionMap(); // 量表包含的所有维度

        String[] questionTitles = new String[] { "选项", "频次", "百分比", "累计百分比" }; // 每道题指标

        DecimalFormat pdf = new DecimalFormat("######0.0"); // 百分比
        DecimalFormat fdf = new DecimalFormat("######0.00"); // 百分比
        DecimalFormat idf = new DecimalFormat("######0"); // 整型

        // 每个维度的答案
        // List<ExamresultDimStudentWithBackGround> rdss =
        // examDimStudentMapper.getStudentResult(params.getScope());
        // 每道题的答案
        List<ExamresultHuman> rss = new ArrayList<ExamresultHuman>();
        if (statObj == StatObject.STUDENT) {
            rss = examresultStundentMapper.getStudentResult(params.getScope());
        } else if (statObj == StatObject.TEACHER) {
            rss = examresultTeacherMapper.getTeacherResult(params.getScope());
        }
        // 统计结果包含的人数
        int count = rss.size();
        // 量表统计结果
        Map<String, Float[]> scaleResult = new TreeMap<String, Float[]>();
        // 维度统计结果
        TreeMap<String, TreeMap<Float, Float[]>> dimResult = new TreeMap<String, TreeMap<Float, Float[]>>();
        // 题目统计结果 {dimId, {questionId, {optionId, float[]}}}
        TreeMap<String, TreeMap<String, TreeMap<String, Float[]>>> questionResult = new TreeMap<String, TreeMap<String, TreeMap<String, Float[]>>>();
        // 计算统计结果
        computeMeanMinMax(rss, scale, dimIds, scaleResult, dimResult, questionResult);

        // 组装成统一的结果前台渲染
        StatResult sr = new StatResult();
        // 某某学校某某年级某某班级学生频次统计
        sr.setTitle("");
        List<StatResultData> allData = new ArrayList<StatResultData>();
        // 首先装整个量表的统计结果
        StatResultData scaledata = new StatResultData();
        // 总表名称
        scaledata.setTitles(new ArrayList<String>());
        scaledata.getTitles().add(composeScaleTableName());
        scaledata.setDatas(new ArrayList());
        // 总表表头
        scaledata.setDataTitles(Arrays.asList(scaleTitles.clone()));
        // 总表数据
        Iterator<String> dimIdIt = scaleResult.keySet().iterator();
        while (dimIdIt.hasNext()) {
            List<String> scaletableData = new ArrayList<String>(); // [维度名称, 人数,
                                                                   // 最小值, 最大值,
                                                                   // 均值, 方差]
            String dimId = dimIdIt.next();
            String dimName = scale.getDimensionMap().get(dimId).getTitle();
            Float[] values = scaleResult.get(dimId);
            scaletableData.add(dimName); // 维度名称
            scaletableData.add(idf.format(count)); // 人数
            scaletableData.add(fdf.format(values[0])); // 最小值
            scaletableData.add(fdf.format(values[1])); // 最大值
            scaletableData.add(fdf.format(values[2])); // 均值
            scaletableData.add(fdf.format(values[3])); // 方差
            scaledata.getDatas().add(scaletableData);
        }
        allData.add(scaledata);

        for (int i = 0; i < dimIds.size(); i++) {
            String dimId = dimIds.getString(i);
            String dimName = scale.getDimensionMap().get(dimId).getTitle();
            StatResultData data = new StatResultData();
            if (null == data.getTitles())
                data.setTitles(new ArrayList<String>());
            data.getTitles().add(dimId + ":" + dimName);
            data.setDataTitles(Arrays.asList(dimTitles.clone())); // [得分，频次，百分比，累计百分比]
            Float accPercent = 0f;
            Iterator<Float> scoreIt = dimResult.get(dimId).keySet().iterator();
            while (scoreIt.hasNext()) {
                List<String> tableData = new ArrayList<String>();
                Float score = scoreIt.next();
                Float[] values = dimResult.get(dimId).get(score);
                tableData.add(fdf.format(score)); // 得分
                Float frequency = values[0];
                tableData.add(idf.format(frequency)); // 频次
                Float percent = values[1];
                tableData.add(pdf.format(percent * 100) + "%"); // 百分比
                accPercent = accPercent + percent;
                tableData.add(pdf.format(accPercent * 100) + "%"); // 累计百分比
                if (data.getDatas() == null)
                    data.setDatas(new ArrayList());
                data.getDatas().add(tableData);

                // 增加题目统计信息
            }
            allData.add(data);
            if (questionResult.containsKey(dimId)) {
                Iterator<String> qIdIt = questionResult.get(dimId).keySet().iterator();
                while (qIdIt.hasNext()) {
                    StatResultData qdata = new StatResultData();
                    String qId = qIdIt.next();
                    String qTitle = scale.getQuestionMap().get(qId).getTitle();
                    Map<String, Option> optionMap = ((SelectionQuestion) scale.getQuestionMap().get(qId))
                            .getOptionMap();
                    if (null == qdata.getTitles())
                        qdata.setTitles(new ArrayList<String>());
                    qdata.getTitles().add(qId + "." + qTitle); // 增加表名 Q1.想有异性朋友
                    qdata.setDataTitles(Arrays.asList(questionTitles.clone())); // 选项,
                                                                                // 频次,
                                                                                // 百分比,
                                                                                // 累计百分比
                    Iterator<String> optIdIt = questionResult.get(dimId).get(qId).keySet().iterator();
                    Float accOptPercent = 0f;
                    while (optIdIt.hasNext()) {
                        List<String> qtableData = new ArrayList<String>();
                        String optId = optIdIt.next();
                        String optTitle = ((SelectionQuestion) scale.getQuestionMap().get(qId)).getOptionMap()
                                .get(optId).getTitle();
                        qtableData.add(optTitle); // 选项
                        qtableData.add(idf.format(questionResult.get(dimId).get(qId).get(optId)[0])); // 频次
                        qtableData.add(pdf.format(questionResult.get(dimId).get(qId).get(optId)[1] * 100) + "%"); // 百分比
                        accOptPercent = accOptPercent + questionResult.get(dimId).get(qId).get(optId)[1];
                        qtableData.add(pdf.format(accOptPercent * 100) + "%"); // 累计百分比
                        if (qdata.getDatas() == null)
                            qdata.setDatas(new ArrayList());
                        qdata.getDatas().add(qtableData);
                    }
                    allData.add(qdata);
                }
            }
        }
        sr.setData(allData);
        return sr;
    }
}
