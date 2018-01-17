package edutec.scale.commands;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleModuleTitleEnum;
import edutec.scale.model.ScaleNormObject;
import heracles.excel.WorkbookUtils;

public class ScaleNormXlsReader {
    private List<ScaleNormObject> normObjList = new ArrayList<ScaleNormObject>();
    private Map<String, Dimension> dimensionMap; // 模板里的维度信息
    private String normGradeOrAgeFlag = "grade";
    private Scale scale;
    private boolean isRead;

    public void read2003Excel(InputStream stream, Scale scale, StringBuilder errSb, String filePath) throws Exception {
        HSSFWorkbook wb;
        try {
            this.scale = scale;
            wb = WorkbookUtils.openWorkbook(stream);
            Sheet curSheet = wb.getSheet(ScaleModuleTitleEnum.NORM.getTitle());
            readNorm(curSheet);
            isRead = true;
        } catch (Exception e) {

        }
    }

    public void read2007Excel(InputStream stream, Scale scale, StringBuilder errSb, String filePath) throws Exception {
        XSSFWorkbook wb;
        try {
            this.scale = scale;
            wb = new XSSFWorkbook(stream);
            Sheet curSheet = wb.getSheet(ScaleModuleTitleEnum.NORM.getTitle());
            readNorm(curSheet);
            isRead = true;
        } catch (Exception e) {

        }
    }

    /* 读取常模 */
    public void readNorm(Sheet expSheet) {

        dimensionMap = scale.getDimensionMap();
        ScaleNormParser scaleNormParser = new ScaleNormParser();
        scaleNormParser.parse(expSheet, normObjList, dimensionMap);
        setNormGradeOrAgeFlag(scaleNormParser.getNormGradeOrAgeFlag());

    }

    /* 获取常模对象列表 */
    public List<ScaleNormObject> getScaleNormObjectList() {
        if (normObjList.size() > 0) {
            return normObjList;
        } else {
            return null;
        }
    }

    public void setNormGradeOrAgeFlag(String normGradeOrAgeFlag) {
        this.normGradeOrAgeFlag = normGradeOrAgeFlag;
    }

    public String getNormGradeOrAgeFlag() {
        return normGradeOrAgeFlag;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}
