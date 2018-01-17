package com.njpes.www.service.scaletoollib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import com.njpes.www.service.util.StatUtilServiceI;

import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import net.sf.json.JSONArray;

@Service("CoRelationStatService")
public class CoRelationStatServiceImpl {

    @Autowired
    ExamresultStudentMapper examresultStundentMapper;

    @Autowired
    ExamresultTeacherMapper examresultTeacherMapper;

    @Autowired
    StatUtilServiceI statUtilService;

    // 拼总表的标题
    private String composeScaleTableName() {
        return "相关分析";
    }

    // 计算两个结果的相关系数
    private void composalXYDatas(List<ExamresultHuman> rdssX, JSONArray dimIds, List<ExamresultHuman> rdssY,
            JSONArray dimIds1, TreeMap<String, TreeMap<String, Float>> result,
            Map<Integer, HashMap<String, Integer>> stuMap) {
        TreeMap<String, TreeMap<Long, Float>> scoresX = new TreeMap<String, TreeMap<Long, Float>>(
                new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                }); // X量表的测试结果
        TreeMap<String, TreeMap<Long, Float>> scoresY = new TreeMap<String, TreeMap<Long, Float>>(
                new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                }); // Y量表的测试结果

        composalScoresToMap(rdssX, dimIds, scoresX);
        composalScoresToMap(rdssY, dimIds1, scoresY);

        // 组成XY的studentid的并集
        List<Long> stuIds = new ArrayList<Long>();
        // 记录性别比例
        HashMap<String, Integer> sexMap = new HashMap<String, Integer>();
        sexMap.put("1", 0);
        sexMap.put("2", 0);
        for (int i = 0; i < rdssX.size(); i++) {
            long stuIdX = rdssX.get(i).getUser_id();
            String gender = rdssX.get(i).getGender();
            if (!stuIds.contains(stuIdX)) {
                stuIds.add(stuIdX);
                sexMap.put(gender, sexMap.get(gender) + 1);
            }
        }
        for (int j = 0; j < rdssY.size(); j++) {
            long stuIdY = rdssY.get(j).getUser_id();
            String gender = rdssY.get(j).getGender();
            if (!stuIds.contains(stuIdY)) {
                stuIds.add(stuIdY);
                sexMap.put(gender, sexMap.get(gender) + 1);
            }
        }
        stuMap.put(stuIds.size(), sexMap);
        // 将{dimIdX:{stuIdX:[scoreX1,scoreX2...]}},
        // {dimIdY:{stuIdY:[scoreY1,scoreY2...]}}
        // 转成{dimIdX：[stu1scoreX, stu2scoreX,...]}, {dimIdY：[stu1scoreY,
        // stu2scoreY,...]}
        Iterator<String> dimItX = scoresX.keySet().iterator();
        while (dimItX.hasNext()) {
            String dimIdX = dimItX.next();
            Iterator<String> dimItY = scoresY.keySet().iterator();
            while (dimItY.hasNext()) {
                List<Float> dimscoreX = new ArrayList<Float>();
                List<Float> dimscoreY = new ArrayList<Float>();
                String dimIdY = dimItY.next();
                // 如果只要XY都有的学生成绩就用下面这一条，如果没有的补充0就用全部的stuIds
                // Iterator<Long> stuIdItX =
                // scoresX.get(dimIdX).keySet().iterator();
                // while(stuIdItX.hasNext()){
                for (int i = 0; i < stuIds.size(); i++) {
                    // Long stuId = stuIdItX.next();
                    Long stuId = stuIds.get(i);
                    if (scoresX.get(dimIdX).containsKey(stuId)) {
                        dimscoreX.add(scoresX.get(dimIdX).get(stuId));
                    } else {
                        dimscoreX.add(0f);
                    }
                    if (scoresY.get(dimIdY).containsKey(stuId)) {
                        dimscoreY.add(scoresY.get(dimIdY).get(stuId));
                    } else {
                        dimscoreY.add(0f);
                    }
                }
                Float r = statUtilService.computeCorelation(dimscoreX, dimscoreY); // 相关系数

                if (!result.containsKey(dimIdY)) {
                    result.put(dimIdY, new TreeMap<String, Float>(new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            // 如果有空值，直接返回0
                            if (o1 == null || o2 == null)
                                return 0;
                            return o1.compareTo(o2);
                        }
                    }));
                }
                if (null != r) {
                    result.get(dimIdY).put(dimIdX, r);
                } else {
                    result.get(dimIdY).put(dimIdX, 0f);
                }
            }
        }
    }

    // 将[{stuid,dimscore}]转为{dimid, {stuid, score}}
    private void composalScoresToMap(List<ExamresultHuman> rdss, JSONArray dimIds,
            TreeMap<String, TreeMap<Long, Float>> scoresMap) {
        for (int i = 0; i < rdss.size(); i++) {
            ExamresultHuman rds = rdss.get(i);
            long stuid = rds.getUser_id();
            String dimScore = rds.getDim_score();
            String[] dimScoreArray = dimScore.split("#");
            for (int j = 0; j < dimScoreArray.length; j++) {
                String[] scores = dimScoreArray[j].split(",");
                String dimId = scores[0];
                if (dimIds.contains(dimId)) {
                    Float score = Float.parseFloat(scores[1]);
                    if (!scoresMap.containsKey(dimId)) {
                        scoresMap.put(dimId, new TreeMap<Long, Float>(new Comparator<Long>() {
                            public int compare(Long o1, Long o2) {
                                // 如果有空值，直接返回0
                                // if (o1 == null || o2 == null || o1 == o2)
                                if (o1.longValue() == o2.longValue())
                                    return 0;
                                if (o1.longValue() > o2.longValue())
                                    return 1;
                                return -1;
                            }
                        }));
                    }
                    scoresMap.get(dimId).put(stuid, score);
                }
            }
        }
    }

    public StatResult stat(StatParams params) throws Exception {
        // TODO Auto-generated method stub

        Integer statObj = params.getStatObj(); // 统计对象是学生还是老师
        DecimalFormat ndf = new DecimalFormat("######0.000");
        if (params.getDepVars().size() >= 2) {
            StatDependentVars depVars = params.getDepVars().get(0);
            Scale scale = depVars.getScaleCode(); // 选择的量表
            JSONArray dimIds = depVars.getDims(); // 量表的维度ids
            String scaleCode = scale.getCode(); // 量表编号

            StatDependentVars depVars1 = params.getDepVars().get(1); // 对比的因变量
            Scale scale1 = depVars1.getScaleCode(); // 对比的量表
            JSONArray dimIds1 = depVars1.getDims(); // 对比量表的维度ids
            String scaleCode1 = scale1.getCode(); // 对比量表编号

            String[] scaleTitles = new String[dimIds.size() + 1]; // 量表总体指标，前面有一个空，所以+1
            Map<String, Dimension> dimMap = scale.getDimensionMap(); // 量表包含的所有维度
            Map<String, Dimension> dimMap1 = scale1.getDimensionMap(); // 量表包含的所有维度

            // 每道题的答案
            List<ExamresultHuman> rssX = new ArrayList<ExamresultHuman>();
            List<ExamresultHuman> rssY = new ArrayList<ExamresultHuman>();
            if (statObj == StatObject.STUDENT) {
                // 如果有量表，写入限定评测范围的条件
                params.getScope().setScaleId(scaleCode);
                rssX = examresultStundentMapper.getStudentResult(params.getScope());
                params.getScope().setScaleId(scaleCode1);
                rssY = examresultStundentMapper.getStudentResult(params.getScope());
            } else if (statObj == StatObject.TEACHER) {
                // 如果有量表，写入限定评测范围的条件
                params.getScope().setScaleId(scaleCode);
                rssX = examresultTeacherMapper.getTeacherResult(params.getScope());
                params.getScope().setScaleId(scaleCode1);
                rssY = examresultTeacherMapper.getTeacherResult(params.getScope());
            }
            // 统计结果包含的人数
            Integer count = rssX.size();
            if (count <= 3)
                throw new Exception(scale.getTitle() + "量表测评结果人数少于3人");

            // 统计结果包含的人数
            int count1 = rssY.size();
            if (count1 <= 3)
                throw new Exception(scale1.getTitle() + "量表测评结果人数少于3人");

            TreeMap<String, TreeMap<String, Float>> result = new TreeMap<String, TreeMap<String, Float>>();
            HashMap<Integer, HashMap<String, Integer>> stuMap = new HashMap<Integer, HashMap<String, Integer>>();
            composalXYDatas(rssX, dimIds, rssY, dimIds1, result, stuMap);

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
            scaleTitles[0] = "";
            String[] keys = result.get(result.keySet().iterator().next()).keySet()
                    .toArray(new String[scaleTitles.length - 1]);
            for (int i = 0; i < keys.length; i++) {
                scaleTitles[i + 1] = dimMap.get(keys[i]).getTitle();
            }
            scaledata.setDataTitles(Arrays.asList(scaleTitles.clone()));
            HashMap<String, HashMap<String, Integer>> explainDim = new HashMap<String, HashMap<String, Integer>>();
            // 总表数据
            Integer n = 0;
            String explain = "";
            if (scaledata.getExplains() == null)
                scaledata.setExplains(new ArrayList<String>());
            if (stuMap.size() > 0) {
                n = stuMap.keySet().iterator().next();
                DecimalFormat pdf = new DecimalFormat("######0.0");
                if (statObj == StatObject.STUDENT) {
                    explain = "备注：**表示在.01水平上显著；*表示在.05水平上显著<br/>" + "共取学生样本量" + n + "人，其中：男生" + stuMap.get(n).get("1")
                            + "个，占总人数的" + pdf.format((float) stuMap.get(n).get("1") * 100 / n) + "%； 女生"
                            + stuMap.get(n).get("2") + "个，占总人数的" + pdf.format((float) stuMap.get(n).get("2") * 100 / n)
                            + "%。";
                } else {
                    explain = "备注：**表示在.01水平上显著；*表示在.05水平上显著<br/>" + "共取教师样本量" + n + "人，其中：男教师" + stuMap.get(n).get("1")
                            + "个，占总人数的" + pdf.format((float) stuMap.get(n).get("1") * 100 / n) + "%； 女教师"
                            + stuMap.get(n).get("2") + "个，占总人数的" + pdf.format((float) stuMap.get(n).get("2") * 100 / n)
                            + "%。";
                }
                // scaledata.getExplains().add(explain);
            }
            Iterator<String> dimYIdIt = result.keySet().iterator();
            while (dimYIdIt.hasNext()) {
                List<String> tableData = new ArrayList<String>(); // [维度名称,相关系数1,相关系数2,相关系数3...]
                String dimYId = dimYIdIt.next();
                String dimYName = dimMap1.get(dimYId).getTitle();
                TreeMap<String, Float> values = result.get(dimYId);
                Iterator<String> dimXIdIt = values.keySet().iterator();
                tableData.add(dimYName); // 维度名称

                while (dimXIdIt.hasNext()) {
                    String dimXId = dimXIdIt.next();
                    Float r = result.get(dimYId).get(dimXId);
                    String rStr = ndf.format(r);
                    // 显著性检验
                    if (r < 1.0) {
                        Double t = Math.sqrt((1 - r * r) / (n - 2));
                        t = (float) r / t;
                        Float t005 = statUtilService.getTvalue("a005", n - 2);
                        Float t001 = statUtilService.getTvalue("a001", n - 2);

                        if (Math.abs(t) > Math.abs(t005)) {
                            if (Math.abs(t) > Math.abs(t001)) {
                                rStr = rStr + "**";
                                if (explainDim.containsKey(dimXId)) {
                                    // if(r>0)如果是双边的，再加对r值的判断
                                    explainDim.get(dimXId).put(dimYId, 2);
                                } else {
                                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                                    map.put(dimYId, 2);
                                    explainDim.put(dimXId, map);
                                }
                            } else {
                                rStr = rStr + "*";
                                HashMap<String, Integer> map = new HashMap<String, Integer>();
                                map.put(dimYId, 1);
                                explainDim.put(dimXId, map);
                            }
                        }

                        /*
                         * if(explainDim.size()>0 &&
                         * explainDim.containsKey(dimXId) &&
                         * explainDim.get(dimXId).containsKey(dimYId)){
                         * if(explainDim.get(dimXId).get(dimYId)==1){
                         * if(StringUtils.isNotEmpty(explain)) explain = explain
                         * + "</br>"; explain = explain + "</br>" +
                         * "维度"+dimMap.get(dimXId).getTitle()+"与维度"+dimMap.get(
                         * dimYId).getTitle()+"呈显著正相关"; }else
                         * if(explainDim.get(dimXId).get(dimYId)==2){
                         * if(StringUtils.isNotEmpty(explain)) explain = explain
                         * + "</br>"; explain = explain +
                         * "维度"+dimMap.get(dimXId).getTitle()+"与维度"+dimMap.get(
                         * dimYId).getTitle()+"呈非常显著正相关"; }
                         * scaledata.getExplains().add(explain); }
                         */
                    } else if (r == 1.0) {
                        rStr = rStr + "**";
                        if (explainDim.containsKey(dimXId)) {
                            // if(r>0)如果是双边的，再加对r值的判断
                            explainDim.get(dimXId).put(dimYId, 2);
                        } else {
                            HashMap<String, Integer> map = new HashMap<String, Integer>();
                            map.put(dimYId, 2);
                            explainDim.put(dimXId, map);
                        }
                    }
                    tableData.add(rStr); // 相关系数
                }
                scaledata.getDatas().add(tableData);
            }
            // 添加结果解释
            scaledata.getExplains().add(explain);
            Set<String> dimSet = explainDim.keySet();
            Iterator<String> dims = dimSet.iterator();
            while (dims.hasNext()) {
                String dim = dims.next();
                Set<String> dim1Set = explainDim.get(dim).keySet();
                Iterator<String> dims1 = dim1Set.iterator();
                while (dims1.hasNext()) {
                    String dim1 = dims1.next();
//                    if (explainDim.get(dim).get(dim1) == 1) {
//                        scaledata.getExplains().add(
//                                "维度" + dimMap.get(dim).getTitle() + "与维度" + dimMap.get(dim1).getTitle() + "呈显著正相关");
//                    } else if (explainDim.get(dim).get(dim1) == 2) {
//                        scaledata.getExplains().add(
//                                "维度" + dimMap.get(dim).getTitle() + "与维度" + dimMap.get(dim1).getTitle() + "呈显著正相关");
//                    }
                }
            }
            allData.add(scaledata);
            sr.setData(allData);
            return sr;
        } else {
            return null;
        }
    }
}
