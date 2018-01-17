package com.njpes.www.service.scaletoollib;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.StatColumnMapper;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.attr.FieldValue;
import com.njpes.www.entity.baseinfo.attr.PropObject;
import com.njpes.www.entity.baseinfo.attr.SelectOption;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.scaletoollib.StatChart;
import com.njpes.www.entity.scaletoollib.StatHuman;
import com.njpes.www.entity.scaletoollib.StatObject;
import com.njpes.www.entity.scaletoollib.StatParams;
import com.njpes.www.entity.scaletoollib.StatResult;
import com.njpes.www.entity.scaletoollib.StatResultData;
import com.njpes.www.service.baseinfo.GradeCodeServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.util.DictionaryServiceI;

import heracles.util.SimpleCodec;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("FrequencyStatService")
public class FrequencyStatServiceImpl {

    @Autowired
    ExamresultStudentMapper examresultStundentMapper;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    SchoolServiceI schoolService;

    @Autowired
    GradeCodeServiceI gradeService;

    @Autowired
    StatColumnMapper colService;

    @Autowired
    DictionaryServiceI dicService;

    /**
     * 判断学生表的字段列表是否包含给定的自变量字段
     * 
     * @param cols
     * @param colname
     * @return
     */
    /*
     * private boolean containsCol(List<StatColumn> cols, String colname){
     * for(StatColumn col: cols){ if(colname.equals(col.getColName())) return
     * true; } return false; }
     */

    private boolean containsCol(List<FieldValue> cols, String colname) {
        for (FieldValue col : cols) {
            if (colname.equals(col.getId()))
                return true;
        }
        return false;
    }

    // private StatColumn getStatCol(List<StatColumn> cols, String colname){
    // for(StatColumn col: cols){
    // if(colname.equals(col.getColName()))
    // return col;
    // }
    // return null;
    // }
    private SelectOption getStatCol(List<FieldValue> cols, String depVar, String colname) {
        for (FieldValue col : cols) {
            if (col.getId().equals(depVar)) {
                List<SelectOption> opts = col.getOptionList();
                for (SelectOption opt : opts) {
                    if (colname.equals(opt.getValue()))
                        return opt;
                }
            }
        }
        return null;
    }

    public StatResult stat(StatParams params) throws Exception {
        Integer statObj = params.getStatObj(); // 统计对象是学生还是老师
        String tableName = params.getIndpVars().get(0).getTableName(); // 学生还是教师的表名
        JSONArray depVars = params.getIndpVars().get(0).getCols(); // 参数里包含的独立变量的数组，例如基础信息的统计属性，性别、年级等

        List<String> statCols = new ArrayList<String>();
        for (int i = 0; i < depVars.size(); i++) {
            statCols.add(depVars.getString(i));
        }

        Map<String, String> colTitleMap = new HashMap<String, String>();
        Map<String, Map<String, Integer>> statCount = new HashMap<String, Map<String, Integer>>();
        Map<String, Map<String, String>> dicMap = new HashMap<String, Map<String, String>>();
        // List<StatColumn> cols = colService.selectByTable(tableName);
        // 读取元数据表获取一张表对应的所有背景属性
        PropObject propObject = null;
        if (statObj == AcountTypeFlag.student.getId()) {
            propObject = PropObject.createPropObject(AcountTypeFlag.student.getId());
        } else {
            propObject = PropObject.createPropObject(AcountTypeFlag.teacher.getId());
        }
        propObject.loadMetas();
        List<FieldValue> cols = propObject.getAttrs();

        /*
         * for(StatColumn col: cols){ //如果包含在统计的列表中，就加入到map里，做统计用
         * if(statCols.contains(col.getColName())){
         * colTitleMap.put(col.getColName(), col.getColTitle());
         * 
         * if(StringUtils.isNotEmpty(col.getDicTableName()) &&
         * !dicMap.containsKey(col.getColName())){ String table =
         * col.getDicTableName(); Map dic = new HashMap(); dic =
         * dicService.selectAllDicMap(table); dicMap.put(table, dic);
         * //初始化自变量的统计值,读出所有的枚举值的中文名，设置计数为0 Iterator<String> cntIt =
         * dic.values().iterator(); Map depCntMap = new HashMap();
         * while(cntIt.hasNext()){ String item = cntIt.next();
         * depCntMap.put(item, 0); } if(depCntMap.size()>0)
         * statCount.put(col.getColTitle(),depCntMap); } } }
         */
        for (FieldValue col : cols) {
            // 如果包含在统计的列表中，就加入到map里，做统计用
            if (statCols.contains(col.getId())) {
                colTitleMap.put(col.getId(), col.getLabel());
                List<SelectOption> opts = col.getOptionList();
                // 初始化自变量的统计值,读出所有的枚举值的中文名，设置计数为0
                Map depCntMap = new HashMap();
                for (SelectOption opt : opts) {
                    depCntMap.put(opt.getTitle(), 0);
                }
                if (depCntMap.size() > 0)
                    statCount.put(col.getLabel(), depCntMap);
            }

        }
        // List<ExamresultStudentWithBackGround> rss =
        // examresultStundentMapper.getStudentResult(params.getScope());
        StatResult sr = null;
        Student stuQuery = new Student();
        List<String> schoolIds = params.getScope().getSchoolId(); // 学校id
        String gradeId = params.getScope().getGradeId(); // 年级id
        String classId = params.getScope().getClassId(); // 班级id

        if (StringUtils.isNotEmpty(gradeId) && StringUtils.isNumeric(gradeId)) {
            stuQuery.setGradecodeid(Integer.parseInt(gradeId)); // 年级id
        }

        if (StringUtils.isNotEmpty(classId) && StringUtils.isNumeric(classId)) {
            stuQuery.setBjid(Integer.parseInt(classId)); // 班级id
        }

        Map depCntMap = new HashMap();

        if (statCols.contains("999")) {
            if (statObj == StatObject.STUDENT) {
                colTitleMap.put("999", "年级");
                statCount.put("年级", depCntMap);
            }
        }
        /*
         * if(statCols.contains("999")){ Map depCntMap = new HashMap();
         * if(StringUtils.isNotEmpty(schoolId)){ List<Grade> grades =
         * schoolService.getGradeListInSchool(schoolService.getSchoolInfoByKeyid
         * (Integer.parseInt(schoolId)).getOrgid()); for(Grade grade:grades){
         * depCntMap.put(grade.getNjmc(), 0); } }else
         * if(StringUtils.isNotEmpty(gradeId)){ String title =
         * gradeService.getGradecodeTitile(Integer.parseInt(gradeId)).getTitle()
         * ; depCntMap.put(title, 0); }else{ List<GradeCode> grades =
         * gradeService.getAllGrades(); for(GradeCode grade:grades){
         * depCntMap.put(grade.getTitle(), 0); } }
         * statCount.put("年级",depCntMap); }
         */

        List<Long> excludes = new ArrayList<Long>();
        List<StatHuman> rss = new ArrayList<StatHuman>();
        List<TeacherWithBLOBs> trss = new ArrayList<TeacherWithBLOBs>();
        if (statObj == StatObject.STUDENT) {
            rss = studentMapper.getStudentsByStatscope(params.getScope());
        } else if (statObj == StatObject.TEACHER) {
            rss = teacherMapper.getTeachersByStatscope(params.getScope());
        }
        if (rss.size() <= 5) {
            throw new RuntimeException("测评结果人数不足5人，无法统计");
        }

        String[] dataTitles = new String[] { "{depVarTitle}", "频次", "百分比", "累计百分比" };
        int count = 0;
        // 将因变量和中文标题组成一个哈希表在后面组成统计结果用
        Map<String, String> varTitleMap = new HashMap<String, String>();
        for (int i = 0; i < depVars.size(); i++) {
            varTitleMap.put(depVars.getString(i), colTitleMap.get(depVars.get(i)));
        }

        // for(ExamresultStudentWithBackGround result: rss){
        for (StatHuman result : rss) {
            for (int i = 0; i < depVars.size(); i++) {
                // 如果是第一次进入因变量统计，放入一个新的map
                /*
                 * if(statCount.size()<i+1){ Map<String, Integer> newMap = new
                 * HashMap<String, Integer>();
                 * statCount.put(depVarTitles.get(i), newMap); }
                 */
                // 如果不是第一次，给当前这一条数据计数
                // 调用get方法需要把参数的第一个字母变成大写
                String depVar = depVars.getString(i);
                /*
                 * String firstCharacter = depVar.substring(0, 1).toUpperCase();
                 * depVar = firstCharacter + depVar.substring(1);
                 */
                // 根据id选出学生以及背景信息，下面做统计数量
                String key = null;
                Object depVarValue = null;
                /*
                 * if(containsCol(cols, depVar.toLowerCase())){ Method method =
                 * StudentWithBLOBs.class.getMethod("get" + depVar); depVarValue
                 * = method.invoke(result, null); }
                 */
                if ("999".equals(depVar)) {
                    key = Integer.toString(result.getGradecodeid());
                    String njmc = result.getNjmc();
                    if (statCount.get(colTitleMap.get(depVars.get(i))).containsKey(njmc)) {
                        statCount.get(colTitleMap.get(depVars.get(i))).put(njmc,
                                statCount.get(colTitleMap.get(depVars.get(i))).get(njmc) + 1);
                    } else {
                        statCount.get(colTitleMap.get(depVars.get(i))).put(njmc, 0);
                    }
                } else {
                    String bj = result.getBjxx();
                    if (StringUtils.isNotEmpty(bj)) {
                        JSONObject bjObj = new JSONObject();
                        String[] bjArray = bj.split(" ");
                        for (int j = 0; j < bjArray.length; j++) {
                            String[] kv = bjArray[j].split("=");
                            bjObj.accumulate(kv[0], kv[1]);
                        }
                        // bjObj = JSONObject.fromObject(bj);
                        if (bjObj.containsKey(depVar)) {
                            depVarValue = bjObj.get(depVar);
                        }
                    }

                    // 只处理字符型和整型，其他的抛出参数错误
                    // if(depVarValue instanceof String){
                    // key = (String)depVarValue;
                    // StatColumn sc = getStatCol(cols, depVar.toLowerCase());
                    // if(sc!=null){
                    // String dicTable = sc.getDicTableName();
                    // if(dicMap.containsKey(dicTable)){
                    // key = dicMap.get(dicTable).get(key);
                    // }
                    // }
                    // }else if(depVarValue instanceof Integer){
                    // key = Integer.toString((Integer)depVarValue);
                    // StatColumn sc = getStatCol(cols, depVar.toLowerCase());
                    // if(sc!=null){
                    // String dicTable = sc.getDicTableName();
                    // if(dicMap.containsKey(dicTable)){
                    // key = dicMap.get(dicTable).get(key);
                    // }
                    // }
                    // }else{
                    // //如果学生信息和背景信息都不包含自变量，则进入下一个自变量
                    // continue;
                    // }
                    if (depVarValue instanceof String) {
                        key = (String) depVarValue;
                        // SelectOption opt = getStatCol(cols,
                        // key.toLowerCase());
                        SelectOption opt = getStatCol(cols, depVar, key.toLowerCase());
                        if (opt != null) {
                            key = opt.getTitle();
                        }
                    } else if (depVarValue instanceof Integer) {
                        key = Integer.toString((Integer) depVarValue);
                        SelectOption opt = getStatCol(cols, depVar, key.toLowerCase());
                        if (opt != null) {
                            key = opt.getTitle();
                        }
                    } else {
                        // 如果学生信息和背景信息都不包含自变量，则进入下一个自变量
                        continue;
                    }
                    if (colTitleMap.size() > 0 && statCount.containsKey(colTitleMap.get(depVars.get(i)))
                            && statCount.get(colTitleMap.get(depVars.get(i))).containsKey(key))
                        statCount.get(colTitleMap.get(depVars.get(i))).put(key,
                                statCount.get(colTitleMap.get(depVars.get(i))).get(key) + 1);
                }
                /*
                 * else{ statCount.get(depVarTitles.get(i)).put(key, 1); }
                 */
            }
            count = count + 1;
        }

        // 组装成统一的结果前台渲染
        sr = new StatResult();
        // 某某学校某某年级某某班级学生频次统计
        sr.setTitle("");
        List<StatResultData> allData = new ArrayList<StatResultData>();
        Iterator<String> keys = statCount.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            StatResultData data = new StatResultData();
            Map<String, Integer> itemCount = statCount.get(key);
            // 需要建一张表记录所有的可统计字段的英文名和中文名，不然页面上选择因变量和自变量
            // 也没有中文名
            // 表名
            if (null == data.getTitles())
                data.setTitles(new ArrayList<String>());
            String objname = "";
            if (statObj == StatObject.STUDENT) {
                objname = "学生";
            } else if (statObj == StatObject.TEACHER) {
                objname = "教师";
            }
            data.getTitles().add(Integer.toString(count) + "名" + objname + "在\"" + key + "\"" + "上的频次统计量");
            Iterator<String> it = itemCount.keySet().iterator();
            dataTitles[0] = key;
            data.setDataTitles(Arrays.asList(dataTitles.clone()));
            Double accPercent = 0.0;
            while (it.hasNext()) {
                String item = it.next();
                // 根据dic表把key值换成中文名
                Integer frequency = itemCount.get(item); // 统计频率
                if (frequency > 0) {
                    List<String> tableData = new ArrayList<String>();
                    tableData.add(item); // 中文值
                    tableData.add(Integer.toString(frequency)); // 统计频率
                    // 查出来指标的中文值、统计频率和百分比，把stat_column表填全
                    DecimalFormat df = new DecimalFormat("######0.0");
                    tableData.add(df.format(frequency * 100.0d / count) + "%"); // 百分比
                    accPercent = accPercent + frequency * 100.0d / count;
                    tableData.add(df.format(accPercent) + "%"); // 累计百分比
                    if (data.getDatas() == null)
                        data.setDatas(new ArrayList());
                    data.getDatas().add(tableData);
                }
            }

            // 生成图表url ，保存到data中,目前是把直方图和饼图都生成了
            StatChart chart = new StatChart();
            chart.setChartType(1); // 1是直方，2是饼图
            chart.setUrl(composeChartUrl(itemCount, 1));
            if (null == data.getCharts())
                data.setCharts(new ArrayList<StatChart>());
            data.getCharts().add(chart);
            /*
             * StatChart chart2 = new StatChart(); chart2.setChartType(2);
             * //1是直方，2是饼图 chart2.setUrl(composeChartUrl(itemCount, 2));
             * data.getCharts().add(chart2);
             */
            allData.add(data);
        }
        sr.setData(allData);
        return sr;

    }

    private String composeChartUrl(Map<String, Integer> itemCount, int chartType) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder();
        url.append("data=");
        Iterator<String> items = itemCount.keySet().iterator();
        while (items.hasNext()) {
            String item = items.next();
            Integer count = itemCount.get(item);
            if (StringUtils.isNotEmpty(item) && count > 0)
                url.append(SimpleCodec.stringToUnicode(item)).append(":").append(Integer.toString(count)).append(";");
            // url.append(URLEncoder.encode(item,
            // "UTF-8")).append(":").append(Integer.toString(count)).append(";");
        }
        if (StringUtils.isNotEmpty(url.toString())) {
            if (chartType == 1)
                url.append("&cht=br&chs=800x300");
            else if (chartType == 2)
                url.append("&cht=pi&chs=800x300");
        }
        return "/assessmentcenter/report/reportchart.do?" + url.toString();
    }
}
