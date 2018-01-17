package edutec.scale.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import edutec.scale.model.Dimension;
import heracles.excel.WorkbookUtils;

public class DimensionSheetParser extends SheetParser {

    private Map propMap;
    private Map dimensionMap;

    public DimensionSheetParser() {
        propMap = new HashMap();
        propMap.put("startCell", "2_1");
        propMap.put("firstLevelCol", "1");
        propMap.put("secondLevelCol", "2");
        propMap.put("thirdLevelCol", "3");
        propMap.put("firstLevel", "一级");
        propMap.put("secondLevel", "二级");
        propMap.put("introduction", "简介");
    }

    @Override
    public void parse(Sheet sheet, Object objs) {
        // TODO Auto-generated method stub
        if (objs instanceof Map) {
            // 读取一级维度和二级维度的开关标志位
            boolean firstLevelStart = false;
            boolean secondlevelStart = false;
            // 已读取维度的个数
            int dimensionLoaded = 0;
            // sheet的usedRow行数
            int rowCount = sheet.getPhysicalNumberOfRows();
            // 从标题行下面第一行开始循环读取内容
            String[] rc = ((String) propMap.get("startCell")).split("_");
            // 标题行号
            int startRow = Integer.parseInt(rc[0]) - 1;
            for (int i = startRow; i <= rowCount; i++) {
                // 取出当前行前两列的内容
                Cell firstCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i,
                        Integer.parseInt((String) propMap.get("firstLevelCol")) - 1);
                Cell secondeCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i,
                        Integer.parseInt((String) propMap.get("secondLevelCol")) - 1);
                Cell thirdCell = WorkbookUtils.get07Cell((XSSFSheet) sheet, i,
                        Integer.parseInt((String) propMap.get("thirdLevelCol")) - 1);
                String firstCellStr = null;
                String secondCellStr = null;
                String thirdCessStr = null;
                if (null != firstCell)
                    firstCellStr = firstCell.getStringCellValue();
                if (null != secondeCell)
                    secondCellStr = secondeCell.getStringCellValue();
                if (null != thirdCell)
                    thirdCessStr = thirdCell.getStringCellValue();
                // 如果是维度中显示的是总分，那么进行下面的操作
                if (firstCellStr != null && firstCellStr.equals("总分")) {
                    Dimension dim = new Dimension();
                    // id是“W”加序号的方式
                    String id = "W0";
                    dim.setId(id);
                    // 第一格是一级维度的名称
                    dim.setTitle(firstCellStr);
                    // 第二格是一级维度的简介
                    dim.setDescn(secondCellStr);
                    // 加入到维度map中
                    ((HashMap) objs).put(id, dim);
                    // 不放入到维度map中，因为不是map
                    /*
                     * if(null == dimensionMap) dimensionMap = new HashMap();
                     * dimensionMap.put(firstCellStr, dim);
                     */
                    continue;
                }
                // 跳过空行，判断第一列为空
                if (null == firstCellStr || "".equals(firstCellStr))
                    continue;
                // 找到一级维度所在行，第一格内容为“一级”，第二格内容为“简介”
                if (((String) propMap.get("firstLevel")).equals(firstCellStr)
                        && ((String) propMap.get("introduction")).equals(secondCellStr)) {
                    // 设置开始读一级维度标志位
                    firstLevelStart = true;
                    continue;
                }
                // 找到二级维度所在行，第一格内容为“一级”，第二格内容为“二级”
                if (((String) propMap.get("firstLevel")).equals(firstCellStr)
                        && ((String) propMap.get("secondLevel")).equals(secondCellStr)) {
                    // 设置开始读二级维度标志位，关闭一级维度标志位
                    firstLevelStart = false;
                    secondlevelStart = true;
                    continue;
                }
                // 读取一级维度
                if (firstLevelStart) {
                    dimensionLoaded++;
                    Dimension dim = new Dimension();
                    // id是“W”加序号的方式
                    String id = "W" + dimensionLoaded;
                    dim.setId(id);
                    // 第一格是一级维度的名称
                    dim.setTitle(firstCellStr.trim());
                    // 第二格是一级维度的简介
                    dim.setDescn(secondCellStr);
                    // 加入到维度map中
                    ((HashMap) objs).put(id, dim);
                    if (null == dimensionMap)
                        dimensionMap = new HashMap();
                    dimensionMap.put(firstCellStr.trim(), dim);
                }
                // 读取二级维度
                if (secondlevelStart) {
                    dimensionLoaded++;
                    Dimension dim = new Dimension();
                    // id是“W”加序号的方式
                    String id = "W" + dimensionLoaded;
                    dim.setId(id);
                    // 第二格是二级维度的名称
                    dim.setTitle(secondCellStr);
                    // 第三格是二级维度的简介
                    dim.setDescn(thirdCessStr);
                    // 第一格是一级维度的名称
                    Dimension parentDimension = (Dimension) dimensionMap.get(firstCellStr);
                    String dimStr = ((Dimension) dimensionMap.get(firstCellStr)).getDimensionIds();
                    // 加入到维度map中,同时添加父维度
                    ((HashMap) objs).put(id, dim);
                    dim.setParenDimension(parentDimension);
                    // 同时为父维度添加其子维度
                    if (parentDimension.getDimensionIds() == null) {
                        String childStr = id;
                        parentDimension.setDimensionIds(childStr);
                    } else {
                        String childStr = parentDimension.getDimensionIds();
                        childStr = childStr + "," + id;
                        parentDimension.setDimensionIds(childStr);
                    }
                }
                WorkbookUtils.get07Cell((XSSFSheet) sheet, i, Integer.parseInt(rc[1]) - 1).getStringCellValue();
            }
        }
    }

}
