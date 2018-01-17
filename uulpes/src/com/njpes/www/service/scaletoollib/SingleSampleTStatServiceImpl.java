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
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.Scalenorm;
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
import edutec.scale.util.ScaleUtils;
import net.sf.json.JSONArray;

@Service("SingleSampleTStatService")
public class SingleSampleTStatServiceImpl {

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
        return "单一样本t检验";
    }

    // 获得第一行的表头
    private List<String> getTitles() {
        return new ArrayList<String>() {
            {
                add("N");
                add("M");
                add("S.D");
                add("T");
                /* add("SF"); add("P"); */}
        };
        // return new ArrayList<String>(){{add("N"); add("M"); add("S.D");
        // add("T"); add("SF"); }};
    }

    // 将[{stuid,dimscore}]转为{dimid, {stuid, score}}
    private void composalScores(List<ExamresultHuman> rdss, JSONArray dimIds, Integer gradeid, String scaleId,
            List<Scalenorm> norm, TreeMap<String, TreeMap<String, String>> result, HashMap<String, Integer> sexStatRs) {
        DecimalFormat ndf = new DecimalFormat("######0.000");
        DecimalFormat pdf = new DecimalFormat("######0.00");
        DecimalFormat cdf = new DecimalFormat("######0");
        Map<String, List<Float>> dimscores = new HashMap<String, List<Float>>();
        for (int i = 0; i < rdss.size(); i++) {
            ExamresultHuman rds = rdss.get(i);
            String dimScore = rds.getDim_score();
            if (StringUtils.isNotEmpty(dimScore)) {
                String[] dimScoreArray = dimScore.split("#");
                for (int j = 0; j < dimScoreArray.length; j++) {
                    String[] scores = dimScoreArray[j].split(",");
                    String dimId = scores[0];
                    if (dimIds.contains(dimId)) {
                        Float score = Float.parseFloat(scores[1]);
                        // 如果结果记录不包含当前的dimid，心检一个数组
                        if (!dimscores.containsKey(dimId)) {
                            List<Float> scoreList = new ArrayList<Float>();
                            dimscores.put(dimId, scoreList);
                        }
                        dimscores.get(dimId).add(score);
                    }
                }
            }
            // 统计性别总数
            String gender = rds.getGender();
            if (sexStatRs.containsKey(gender)) {
                sexStatRs.put(gender, sexStatRs.get(gender) + 1);
            } else {
                sexStatRs.put(gender, 1);
            }
        }
        Iterator<String> dimIt = dimscores.keySet().iterator();
        while (dimIt.hasNext()) {
            TreeMap<String, String> row = new TreeMap<String, String>();
            String dimId = dimIt.next();
            // 如果是心理健康量表，不计算总分维度
            if (ScaleUtils.isMentalHealthScale(scaleId) && dimId.trim().equals("W0")) {
                continue;
            }
            if (gradeid == -1)
                gradeid = 0;

            Float miu0 = getNormMean(norm, gradeid, dimId);
            List<Float> scores = dimscores.get(dimId);
            Float mean = statUtilService.computeMean(scores);
            Float sd = statUtilService.computeSd(scores);
            Integer count = scores.size();

            Float t = (float) (sd / Math.sqrt(count));
            t = (mean - miu0) / t;
            Float tval005 = null;
            Float tval001 = null;
            if(count <= 30){
                tval005 = statUtilService.getTvalue("a005", count - 1);
                tval001 = statUtilService.getTvalue("a001", count - 1);
            }else{
                tval005 = (float)1.96;
                tval001 = (float)2.58;
            }
            row.put("N", cdf.format(count));
            // 判断t值和T表的值的比较，判断给m值打一个星还是两个星
            /*
             * if(t>tval001) row.put("M", ndf.format(mean) + "**"); else
             * if(t>tval005) row.put("M", ndf.format(mean) + "*"); else
             */
            row.put("M", ndf.format(mean));
            row.put("S.D", ndf.format(sd));
            if (Math.abs(t) > Math.abs(tval005)) {
                if (Math.abs(t) > tval001) {
                    row.put("T", ndf.format(t) + "**");
                } else {
                    row.put("T", ndf.format(t) + "*");
                }
            } else {
                row.put("T", ndf.format(t));
            }
            /*
             * Float p = 1-(float)statUtilService.getPvalue(Math.abs(t));
             * if(p<0.01) row.put("t", ndf.format(t) + "**"); else if(p<0.05)
             * row.put("t", ndf.format(t) + "*"); else row.put("t",
             * ndf.format(t));
             */
            // row.put("t", ndf.format(t));
            // row.put("SF", cdf.format(count-1));
            // row.put("P",
            // pdf.format((1-(float)statUtilService.getPvalue(Math.abs(t)))*100)+"%");
            result.put(dimId, row);
        }
    }

    /**
     * 在给定的常模中查询给定年级的常模均值
     * 
     * @param norm
     * @param gradeid
     * @param wid
     *            维度id
     * @return
     */
    private Float getNormMean(List<Scalenorm> norm, Integer gradeid, String wid) {
        for (int i = 0; i < norm.size(); i++) {
            Scalenorm pnorm = norm.get(i);
            if (pnorm.getGradeOrderId().toString().equals(Integer.toString(gradeid)) && pnorm.getwId().equals(wid)) {
                return pnorm.getM();
            }
        }
        return null;
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
        List<String> schoolid = (List<String>) scope.getSchoolId(); // 学校id
        String gradeid = scope.getGradeId(); // 年级id
        String classid = scope.getClassId(); // 班级id
        int statojbid = params.getStatObj(); // 统计对象是学生还是老师
        if (StringUtils.isNotEmpty(classid) && statojbid == StatObject.STUDENT) {
            title = title + classService.findById(Long.parseLong(classid)).getBjmc();
        }
        if (StringUtils.isNotEmpty(gradeid) && statojbid == StatObject.STUDENT) {
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

    public StatResult stat(StatParams params) throws Exception {
        // TODO Auto-generated method stub

        StatDependentVars depVars = params.getDepVars().get(0);
        Scale scale = depVars.getScaleCode(); // 选择的量表
        JSONArray dimIds = depVars.getDims(); // 量表的维度ids
        Integer statObj = params.getStatObj(); // 统计对象是学生还是老师

        if (dimIds == null || dimIds.size() == 0) {
            // 如果用户没有选择dimIds，就把量表所有的维度都装到dimIds里
            dimIds = new JSONArray();
            List<Dimension> dims = scale.getDimensions();
            for (Dimension dim : dims) {
                dimIds.add(dim.getId());
            }
        }
        String scaleCode = scale.getCode(); // 量表编号
        List<Scalenorm> norm = depVars.getNorm(); // 量表常模

        String[] scaleTitles = new String[getTitles().size() + 1]; // 量表总体指标，前面有一个空，所以+1
        Map<String, Dimension> dimMap = scale.getDimensionMap(); // 量表包含的所有维度

        // 如果有量表，写入限定评测范围的条件
        params.getScope().setScaleId(scaleCode);
        // 每道题的答案
        List<ExamresultHuman> rss = new ArrayList<ExamresultHuman>();
        if (statObj == StatObject.STUDENT) {
            rss = examresultStundentMapper.getStudentResult(params.getScope());
        } else if (statObj == StatObject.TEACHER) {
            rss = examresultTeacherMapper.getTeacherResult(params.getScope());
        }
        // 统计结果包含的人数
        Integer count = rss.size();

        if (count < 5) {

            throw new Exception("测评人数不足5人");

        }

        TreeMap<String, TreeMap<String, String>> result = new TreeMap<String, TreeMap<String, String>>();

        HashMap<String, Integer> sexStatRs = new HashMap<String, Integer>();

        Integer gradeid = -1;

        if (StringUtils.isNotEmpty(params.getScope().getGradeId())) {

            gradeid = Integer.parseInt(params.getScope().getGradeId());

        } else if (statObj == StatObject.TEACHER) {

            // 如果是教师,年级赋值为成人14
            gradeid = 14;

        }

        composalScores(rss, dimIds, gradeid, scale.getId(), norm, result, sexStatRs);

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
        List<String> keys = getTitles();
        for (int i = 0; i < keys.size(); i++) {
            scaleTitles[i + 1] = keys.get(i);
        }
        scaledata.setDataTitles(Arrays.asList(scaleTitles.clone()));
        // 结果解释
        String title = this.getStatTitle(params);
        if (statObj == StatObject.STUDENT) {
            title = title + "学生";
        } else {
            title = title + "教师";
        }
        DecimalFormat pdf = new DecimalFormat("######0.0");

        String explain = "备注：**表示在.01水平上显著；*表示在.05水平上显著<br/>";
        Integer man = sexStatRs.containsKey("1") ? sexStatRs.get("1") : 0;
        Integer woman = sexStatRs.containsKey("2") ? sexStatRs.get("2") : 0;
        if (statObj == StatObject.TEACHER) {
            explain = explain + "共取教师样本量" + count + "人，其中：男教师" + man + "个，占总人数的" + pdf.format((float) man * 100 / count)
                    + "%； 女教师" + woman + "个，占总人数的" + pdf.format((float) woman * 100 / count) + "%。";
        } else {
            explain = explain + "共取学生样本量" + count + "人，其中：男生" + man + "个，占总人数的" + pdf.format((float) man * 100 / count)
                    + "%； 女生" + woman + "个，占总人数的" + pdf.format((float) woman * 100 / count) + "%。";
        }

        String cyExplainTitle = "<br/>结果表明：";
        String cyExplain = ""; // 差异性结果解释
        String normExplainTitle = "<br/>结果说明：";
        String normExplain = ""; // 常模对比结果解释
        // 总表数据
        Iterator<String> dimIdIt = result.keySet().iterator();
        while (dimIdIt.hasNext()) {
            List<String> tableData = new ArrayList<String>(); // [维度名称,相关系数1,相关系数2,相关系数3...]
            String dimId = dimIdIt.next();
            String dimName = dimMap.get(dimId).getTitle();
            TreeMap<String, String> values = result.get(dimId);
            tableData.add(dimName); // 维度名称
            for (int i = 0; i < keys.size(); i++) {
                String k = keys.get(i);
                String v = values.get(k);
                tableData.add(v);
            }
            // 根据均值的两颗星判断结果解释的说法
            String arrOneTwoStar = values.get("T");
            if(arrOneTwoStar.contains("**"))
                if (arrOneTwoStar.contains("-")) {
                    cyExplain = cyExplain + "<br/>" + title + "在α=0.01水平上" + dimName + "上维度得分非常显著低于常模";
                } else {
                    cyExplain = cyExplain + "<br/>" + title + "在α=0.01水平上" + dimName + "上维度得分非常显著高于常模";
            } else if (arrOneTwoStar.contains("*")){
                if (arrOneTwoStar.contains("-")) {
                    cyExplain = cyExplain + "<br/>" + title + "在α=0.05水平上" + dimName + "上维度得分显著低于常模";
                } else {
                    cyExplain = cyExplain + "<br/>" + title + "在α=0.05水平上" + dimName + "上维度得分显著高于常模";
                }
            } else {
                cyExplain = cyExplain + "<br/>" + title + "在" + dimName + "上维度得分与常模差异不显著";
            }
            /*
             * if(Float.parseFloat(values.get("t"))<0){ normExplain =
             * normExplain + "<br/>" + title + "在" + dimName + "上低于常模"; }
             */

            scaledata.getDatas().add(tableData);
        }
        if (StringUtils.isNotEmpty(cyExplain)) {
            explain = explain + cyExplainTitle + cyExplain;
        }
        if (StringUtils.isNotEmpty(normExplain)) {
            explain = explain + normExplainTitle + normExplain;
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
