package com.njpes.www.invoker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.SequenceConst;
import com.njpes.www.dao.baseinfo.GradeMapper;
import com.njpes.www.dao.scaletoollib.NormInfoMapper;
import com.njpes.www.dao.scaletoollib.ScalenormMapper;
import com.njpes.www.dao.util.DictionaryMapper;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.ScaleGrades;
import com.njpes.www.entity.util.DictionaryUtil;

import edutec.admin.CompressUtils;
import edutec.admin.ExportUtils;
import edutec.scale.commands.ScaleNormXlsReader;
import edutec.scale.commands.ScaleXlsReader;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.db.ScaleDto;
import edutec.scale.db.ScaleMgr;
import edutec.scale.model.PreWarn;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.model.ScoreGrade;
import edutec.scale.util.ScaleUtils;
import heracles.db.ibatis.SequenceGenerator;
import heracles.util.UtilDateTime;
import heracles.web.config.ApplicationConfiguration;

@Service("ImportScaleInvoker")
public class ImportScaleInvoker {

    // public String excelUrl;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private SequenceGenerator sequenceGenerator;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private DictionaryMapper dicMapper;

    @Autowired
    private ScalenormMapper scalenormMapper;
    @Autowired
    private NormInfoMapper normInfoMapper;

    /*
     * public String getExcelUrl() { return excelUrl; }
     * 
     * public void setExcelUrl(String excelUrl) { this.excelUrl = excelUrl; }
     */
    public String importExcel(InputStream stream, String excelUrl, long userid, Organization org, ScaleMgr scaleMgrtool)
            throws Exception {
        String tmpScaleDir = null;
        String submitScaleId = null;
        try {
            // FileInputStream stream= new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            StringBuilder errSb = new StringBuilder();
            ScaleXlsReader reader = new ScaleXlsReader();
            String fileName = excelUrl.substring(prefix + 1);
            if (!StringUtils.endsWith(fileName, ".xls") && !StringUtils.endsWith(fileName, ".xlsx")) {
                // shibin add对zip上传文件的判断，如果上传zip文件，则解压到临时目录，检查是否有excel文件，没有报错
                if (StringUtils.endsWith(fileName, ".zip")) {
                    tmpScaleDir = "picScale" + System.currentTimeMillis();
                    File tmpDir = new File(ApplicationConfiguration.getInstance().getWorkDir() + "/"
                            + ExportUtils.DATA_TMP_DIR + "/" + tmpScaleDir);
                    tmpDir.mkdir();
                    CompressUtils.upzip(stream, tmpDir.getAbsolutePath());
                    String filedir = ExportUtils.DATA_TMP_DIR + "/" + tmpScaleDir + findExcelFile(tmpDir);
                    fileName = null == filedir ? "" : filedir;
                    stream = new FileInputStream(ApplicationConfiguration.getInstance().getWorkDir() + "/" + fileName);
                }
                if (!StringUtils.endsWith(fileName, ".xls") && !StringUtils.endsWith(fileName, ".xlsx")) {
                    // return "没有提供xls文件。";
                    throw new Exception("只支持Excel的xls或xlsx格式！");
                }
            }
            HSSFWorkbook wb03 = null;
            XSSFWorkbook wb07 = null;
            if (StringUtils.endsWith(fileName, ".xls"))
                wb03 = reader.read2003Excel(stream, errSb);
            else
                wb07 = reader.read2007Excel(stream, errSb, fileName);
            if (errSb.length() > 0) {
                throw new Exception(errSb.toString());
            }
            if (!reader.isRead()) {
                // return "不能完整读取量表XLS信息。";
                throw new Exception("不能完整读取量表XLS信息。");
            }
            Scale scale = reader.buildScale2();

            // 如果量表模板里记录了量表的id，则使用记录的id，如果模板里没有id，则到数据库
            // 里查询下一个id
            String scaleId = reader.getId();
            Long nextId = StringUtils.isEmpty(scaleId) ? -1l : Long.parseLong(scaleId);
            if (StringUtils.isEmpty(scaleId)) {
                nextId = sequenceGenerator.nextID(SequenceConst.SCALE);
            }
            scale.setId(nextId + ""); // 设置scale id
            scale.setExamtime(reader.getExamineTime());
            scale.setFlag(ScaleUtils.SCALE_CUST_FLAG); // 设置scale 自定义标志位
            scale.setCreationTime(UtilDateTime.nowDate()); // 量表创建时间
            scale.setApplicablePerson(reader.getApplicablePerson());
            scale.setNormGradeOrAgeFlag(reader.getNormGradeOrAgeFlag());
            // scaleMgrtool.deleteScale(scale.getId());
            // 检查数据库中是否存在量表id，如果存在就覆盖
            int cnt = scaleMgrtool.getScale(Long.toString(nextId));
            if (cnt > 0) {
                if (cachedScaleMgr.get(Long.toString(nextId)) != null)
                    cachedScaleMgr.removeScale(cachedScaleMgr.get(Long.toString(nextId)));
                else
                    scaleMgrtool.deleteScale(scale.getId());
            }
            ScaleDto dto = new ScaleDto(); // 量表的简单描述元信息对象，将量表信息抽取出来组装一个元信息对象
            dto.setId(nextId);
            DictionaryUtil testTypeDic = new DictionaryUtil();
            testTypeDic.setDics(dicMapper.selectAllDic("dic_scale_assesstype"));
            String testType = scale.getTestType();
            if (StringUtils.isNotEmpty(testType)) {
                String testTypeId = testTypeDic.findIdByName(testType);
                if (StringUtils.isNotEmpty(testTypeId)) {
                    dto.setAssesstype(Integer.parseInt(testTypeId)); // 测评类型：自评，他评
                }
            }

            DictionaryUtil sourceDic = new DictionaryUtil();
            sourceDic.setDics(dicMapper.selectAllDic("dic_scale_source"));
            String sourceName = scale.getSource();
            if (StringUtils.isNotEmpty(sourceName)) {
                String sourceId = sourceDic.findIdByName(sourceName);
                if (StringUtils.isNotEmpty(sourceId)) {
                    dto.setSource(Integer.parseInt(sourceId)); // 量表来源
                }
            }

            DictionaryUtil typeDic = new DictionaryUtil();
            typeDic.setDics(dicMapper.selectAllDic("dic_scaletype"));
            String typeName = scale.getScaleType();
            if (StringUtils.isNotEmpty(typeName)) {
                String typeId = typeDic.findIdByName(typeName);
                if (StringUtils.isNotEmpty(typeId)) {
                    dto.setTypeId(Integer.parseInt(typeId)); // 量表类型
                }
            }

            dto.setExamtime(scale.getExamtime()); // 量表测试时间
            dto.setTitle(scale.getTitle());
            dto.setFlag(scale.getFlag());
            dto.setOrgId(scale.getOrgId());
            dto.setShortname(scale.getShortname());
            dto.setPrewarn(reader.getPreWarns() != null && !reader.getPreWarns().isEmpty()); // 如果预警级别数组不为空，则表示量表包含预警
            dto.setReportchart(scale.getReportGraph());
            String gradegroups = scale.getApplicablePerson(); // 适合的年级列表
            List<Integer> groupids = new ArrayList<Integer>();
            List<Integer> gradeids = new ArrayList<Integer>();
            if (StringUtils.isNotEmpty(gradegroups)) {
                String[] groups = gradegroups.split("、");
                for (String group : groups) {
                    Integer groupid = gradeMapper.selectGradeGroupIdByName(group);
                    if (null != groupid) {
                        groupids.add(groupid);
                    } else {
                        Integer gradeid = gradeMapper.selectGradeIdByName(group);
                        if (null != gradeid) {
                            gradeids.add(gradeid);
                        } else {
                            throw new Exception("模板适用年级信息有误");
                        }
                    }
                }
            }
            dto.setQuestionNum(scale.getQuestionNum());
            dto.setCreatedBy(scale.getCreatedBy());
            dto.setCreationTime(scale.getCreationTime());
            dto.setNormGradeOrAgeFlag(scale.getNormGradeOrAgeFlag());

            // 为常模添加scaleid,同时添加到数据库中去
            List<ScaleNormObject> normObjList = reader.getScaleNormObjectList();
            // 为维度的解释和指导建议添加scaleid,同时插入到数据库中去
            List<ScaleExplainObject> scaleExplainObjectList = reader.getScaleExplainObject();
            // 添加计分
            List<ScoreGrade> scoreGrades = reader.getScoreGrades();
            // 添加告警等级
            List<PreWarn> preWarns = reader.getPreWarns();

            scaleMgrtool.getSimpleScales();
            // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
            TransactionDefinition def = new DefaultTransactionDefinition();
            TransactionStatus status = txManager.getTransaction(def);
            try {
                scaleMgrtool.appendScale(dto); // 将量表的基础信息写入到scale表中
                // 如果是group，插入groupid
                for (int groupid : groupids) {
                    ScaleGrades scalegrades = new ScaleGrades();
                    scalegrades.setScaleid((int) nextId.longValue());
                    scalegrades.setGroupid(groupid);
                    gradeMapper.insertScaleGrades(scalegrades);
                }
                // 如果是grade，插入gradeid
                for (int gradeid : gradeids) {
                    ScaleGrades scalegrades = new ScaleGrades();
                    scalegrades.setScaleid((int) nextId.longValue());
                    scalegrades.setGradeid(gradeid);
                    gradeMapper.insertScaleGrades(scalegrades);
                }

                scaleMgrtool.updateXmlStr(scale.toXml(false), scale.getId()); // 将量表的xml字符串写入到数据库中
                // 添加得分水平
                scaleMgrtool.insertScoreGradeList(scoreGrades, nextId);
                // 添加告警等级
                scaleMgrtool.insertPreWarnList(preWarns, nextId);
                // 向数据库中添加常模
                scaleMgrtool.appendNormList(normObjList, nextId, 1, org.getId(), userid);
                int areaid = 0;
                if (org.getOrgLevel() == 3)
                    areaid = Integer.parseInt(org.getCityid());
                else if (org.getOrgLevel() == 4)
                    areaid = Integer.parseInt(org.getCountyid());

                int normid = scaleMgrtool.appendNormInfo(scale.getCode(), scale.getTitle(), 1, new Date(), userid, null,
                        areaid, org.getId(), org.getOrgLevel(), null, null);

                // 向数据库中的添加结果解释和指导建议
                scaleMgrtool.appendDimExplainAndInstr(scaleExplainObjectList, nextId);
                txManager.commit(status); // 提交事务

            } catch (Exception e) {
                // 否则回滚
                e.printStackTrace();
                txManager.rollback(status);
            }
            // 如果上传了zip带图片的文件，导入后要把临时文件夹删除
            if (null != tmpScaleDir) {
                File file = new File(tmpScaleDir);
                if (file.isDirectory() && file.exists())
                    file.delete();
            }
            // 在缓存中添加量表对象
            scale.mountDimensions();
            cachedScaleMgr.addScale(scale, true);
            submitScaleId = scale.getId();

            /*
             * String saveFileName = null; String title = scale.getTitle();
             * 
             * if(StringUtils.endsWith(fileName, ".xls")){ saveFileName=
             * title+".xls"; String saveFilePath =
             * ApplicationConfiguration.getInstance().getWorkDir() + "\\" +
             * ExportUtils.DATA_TMP_DIR;
             * fileWrite03(saveFilePath+"\\"+saveFileName,wb03); }
             * if(StringUtils.endsWith(fileName,".xlsx")){ saveFileName=
             * title+".xlsx"; String saveFilePath =
             * ApplicationConfiguration.getInstance().getWorkDir() + "\\" +
             * ExportUtils.DATA_TMP_DIR;
             * fileWrite07(saveFilePath+""+saveFileName,wb07); }
             */

        } catch (Exception e) {
            // e.printStackTrace();
            // return "上传失败，请检查量表模板";
            throw new Exception(e.getMessage());
        }
        return submitScaleId;
    }

    public String importScaleQuestionExcel(InputStream stream, String excelUrl) throws Exception {
        String tmpScaleDir = null;
        String submitScaleId = null;
        try {
            // FileInputStream stream= new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            StringBuilder errSb = new StringBuilder();
            ScaleXlsReader reader = new ScaleXlsReader();
            String fileName = excelUrl.substring(prefix + 1);
            if (!StringUtils.endsWith(fileName, ".xls") && !StringUtils.endsWith(fileName, ".xlsx")) {
                // add对zip上传文件的判断，如果上传zip文件，则解压到临时目录，检查是否有excel文件，没有报错
                if (StringUtils.endsWith(fileName, ".zip")) {
                    tmpScaleDir = "picScale" + System.currentTimeMillis();
                    File tmpDir = new File(ApplicationConfiguration.getInstance().getWorkDir() + "/"
                            + ExportUtils.DATA_TMP_DIR + "/" + tmpScaleDir);
                    tmpDir.mkdir();
                    CompressUtils.upzip(stream, tmpDir.getAbsolutePath());
                    String filedir = ExportUtils.DATA_TMP_DIR + "/" + tmpScaleDir + findExcelFile(tmpDir);
                    fileName = null == filedir ? "" : filedir;
                    stream = new FileInputStream(ApplicationConfiguration.getInstance().getWorkDir() + "/" + fileName);
                }
                if (!StringUtils.endsWith(fileName, ".xls") && !StringUtils.endsWith(fileName, ".xlsx")) {
                    // return "没有提供xls文件。";
                    throw new Exception("只支持Excel的xls或xlsx格式！");
                }
            }
            if (StringUtils.endsWith(fileName, ".xls"))
                reader.readQuestion2003Excel(stream);
            else
                reader.readQuestion2007Excel(stream);

            String scaleId = reader.getId();
            // 如果上传了zip带图片的文件，导入后要把临时文件夹删除
            if (null != tmpScaleDir) {
                File file = new File(tmpScaleDir);
                if (file.isDirectory() && file.exists())
                    file.delete();
            }
            submitScaleId = scaleId;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return submitScaleId;
    }

    public String importScaleQuestionWord(InputStream stream, String excelUrl) throws Exception {
        String tmpScaleDir = null;
        String submitScaleId = null;
        try {
            // FileInputStream stream= new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            StringBuilder errSb = new StringBuilder();
            ScaleXlsReader reader = new ScaleXlsReader();
            String fileName = excelUrl.substring(prefix + 1);
            if (!StringUtils.endsWith(fileName, ".doc") && !StringUtils.endsWith(fileName, ".docx")) {
                // shibin add对zip上传文件的判断，如果上传zip文件，则解压到临时目录，检查是否有word文件，没有报错
                if (StringUtils.endsWith(fileName, ".zip")) {
                    tmpScaleDir = "picScale" + System.currentTimeMillis();
                    File tmpDir = new File(ApplicationConfiguration.getInstance().getWorkDir() + "/"
                            + ExportUtils.DATA_TMP_DIR + "/" + tmpScaleDir);
                    tmpDir.mkdir();
                    CompressUtils.upzip(stream, tmpDir.getAbsolutePath());
                    String filedir = ExportUtils.DATA_TMP_DIR + "/" + tmpScaleDir + findExcelFile(tmpDir);
                    fileName = null == filedir ? "" : filedir;
                    stream = new FileInputStream(ApplicationConfiguration.getInstance().getWorkDir() + "/" + fileName);
                }
                if (!StringUtils.endsWith(fileName, ".doc") && !StringUtils.endsWith(fileName, ".docx")) {
                    // return "没有提供xls文件。";
                    throw new Exception("只支持office word的doc或docx格式！");
                }
            }
            if (StringUtils.endsWith(fileName, ".xls"))
                reader.readQuestion2003Excel(stream);
            else
                reader.readQuestion2007Excel(stream);

            String scaleId = reader.getId();
            // 如果上传了zip带图片的文件，导入后要把临时文件夹删除
            if (null != tmpScaleDir) {
                File file = new File(tmpScaleDir);
                if (file.isDirectory() && file.exists())
                    file.delete();
            }
            submitScaleId = scaleId;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return submitScaleId;
    }

    public String importScaleNormExcel(InputStream stream, String scaleId, long orgid, int orglevel, int areaid,
            long userid, String description, String editer, String edittime, String excelUrl, ScaleMgr scaleMgrtool)
            throws Exception {
        String tmpScaleDir = null;
        String submitScaleId = null;
        try {
            // FileInputStream stream= new FileInputStream(excelUrl);
            int prefix = excelUrl.lastIndexOf("\\");
            StringBuilder errSb = new StringBuilder();
            ScaleNormXlsReader reader = new ScaleNormXlsReader();
            String fileName = excelUrl.substring(prefix + 1);
            if (!StringUtils.endsWith(fileName, ".xls") && !StringUtils.endsWith(fileName, ".xlsx")) {
                if (!StringUtils.endsWith(fileName, ".xls") && !StringUtils.endsWith(fileName, ".xlsx")) {
                    // return "没有提供xls文件。";
                    throw new Exception("只支持Excel的xls或xlsx格式！");
                }
            }
            Scale scale = cachedScaleMgr.get(scaleId);
            if (StringUtils.endsWith(fileName, ".xls"))
                reader.read2003Excel(stream, scale, errSb, fileName);
            else
                reader.read2007Excel(stream, scale, errSb, fileName);
            if (errSb.length() > 0) {
                throw new Exception(errSb.toString());
            }
            if (!reader.isRead()) {
                // return "不能完整读取量表XLS信息。";
                throw new Exception("不能完整读取量表XLS信息。");
            }

            scale.setNormGradeOrAgeFlag(reader.getNormGradeOrAgeFlag());
            ScaleDto dto = new ScaleDto(); // 量表的简单描述元信息对象，将量表信息抽取出来组装一个元信息对象
            dto.setNormGradeOrAgeFlag(scale.getNormGradeOrAgeFlag());

            // 为常模添加scaleid,同时添加到数据库中去
            List<ScaleNormObject> normObjList = reader.getScaleNormObjectList();
            // 为维度的解释和指导建议添加scaleid,同时插入到数据库中去
            // scaleMgrtool.getSimpleScales();
            // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
            TransactionDefinition def = new DefaultTransactionDefinition();
            TransactionStatus status = txManager.getTransaction(def);
            try {
                // 向数据库中添加常模
                // scaleMgrtool.appendNormCustomList(normObjList,Integer.parseInt(scaleId),orglevel,Integer.parseInt(areaid),userid);
                scaleMgrtool.appendNormList(normObjList, Integer.parseInt(scaleId), 2, orgid, userid);
                if (ScaleUtils.isThirdAngleScaleP1(scaleId) || ScaleUtils.isThirdAngleScaleM1(scaleId)) {
                    if (ScaleUtils.isThirdAngleScaleP(scaleId)) {
                        int normid = normInfoMapper.getThreeAngleNormid(scaleId, 2, userid);
                        scalenormMapper.updateCustomScaleNormID(normid, scaleId, 2, userid);
                    } else if (ScaleUtils.isThirdAngleScaleM(scaleId)) {
                        int normid = normInfoMapper.getThreeAngleNormid(scaleId, 2, userid);
                        scalenormMapper.updateCustomScaleNormID(normid, scaleId, 2, userid);
                    }
                } else
                    scaleMgrtool.appendNormInfo(scaleId, scale.getTitle(), 2, new Date(), userid, description, areaid,
                            orgid, orglevel, editer, edittime);
                // 向数据库中的添加结果解释和指导建议
                txManager.commit(status); // 提交事务
            } catch (Exception e) {
                // 否则回滚
                e.printStackTrace();
                txManager.rollback(status);
            }
            // 如果上传了zip带图片的文件，导入后要把临时文件夹删除
            if (null != tmpScaleDir) {
                File file = new File(tmpScaleDir);
                if (file.isDirectory() && file.exists())
                    file.delete();
            }
            return "seccuss";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * shibin add遍历目录，找xlsx或xls文件，返回file
     * 
     * @param dir
     *            目录
     * @return excel文件
     */
    public String findExcelFile(File dir) {
        String fpath = "";
        File[] files;
        while ((files = dir.listFiles()).length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String findpath = findExcelFile(file);
                    if (StringUtils.isNotEmpty(findpath)) {
                        return fpath + file.separator + file.getName() + file.separator + findpath;
                    }
                } else {
                    fpath = file.getName().toLowerCase();
                    if (fpath.endsWith(".xlsx") || fpath.endsWith(".xls")) {
                        return file.getName();
                    }
                }
            }
        }
        return null;
    }

    public void fileWrite03(String targetFile, HSSFWorkbook wb) throws Exception {
        String[] sheetnames = { "计分", "分数", "常模", "得分水平划分", "预警级别", "指导建议", "结果解释", "图表参数", "结果解释-学生版", "结果解释-家长版",
                "结果解释-教师版", "指导建议-学生版", "指导建议-家长版", "指导建议-教师版" };
        for (String sheetname : sheetnames) {
            int idx = wb.getSheetIndex(sheetname);
            wb.removeSheetAt(idx);
        }
        FileOutputStream fileOut = new FileOutputStream(targetFile);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    public void fileWrite07(String targetFile, XSSFWorkbook wb) throws Exception {
        String[] sheetnames = { "计分", "分数", "常模", "得分水平划分", "预警级别", "指导建议", "结果解释", "图表参数", "结果解释-学生版", "结果解释-家长版",
                "结果解释-教师版", "指导建议-学生版", "指导建议-家长版", "指导建议-教师版" };
        for (String sheetname : sheetnames) {
            int idx = wb.getSheetIndex(sheetname);
            if (idx > -1)
                wb.removeSheetAt(idx);
        }
        FileOutputStream fileOut = new FileOutputStream(targetFile);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

}
