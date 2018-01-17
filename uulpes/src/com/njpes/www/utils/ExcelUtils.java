package com.njpes.www.utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import com.njpes.www.entity.baseinfo.Syslog;

public class ExcelUtils {

    private final static Logger logger = Logger.getLogger(ExcelUtils.class);

    /**
     * 导出下载
     * 
     * @param response
     * @param sheetName
     * @param header
     * @param dataList
     * @throws IOException
     */
    public static void exportExcel(HttpServletResponse response, String sheetName, String[] header,
            List<Syslog> dataList) throws IOException {
        // 创建工作表对象
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = createSheet(wb, sheetName);
        HSSFRow row = null;
        HSSFCell cell = null;

        // 生成表格标题
        row = createRow(sheet, 0);
        for (int i = 0; i < header.length; i++) {
            cell = createCell(row, i, createCellStyle(wb, createFont(wb)));
            cell.setCellValue(header[i]);
        }

        // 填充表格数据
        Syslog datas = null;
        HSSFCellStyle style2 = createCellStyle2(wb);
        for (int j = 0; j < dataList.size(); j++) {
            datas = dataList.get(j);
            row = createRow(sheet, j + 1);
            int count = 0;
            createCell(row, count++, style2).setCellValue(String.valueOf(datas.getOptime()));
            createCell(row, count++, style2).setCellValue(String.valueOf(datas.getMenuname()));
            // createCell(row, count++, style2).setCellValue(
            // String.valueOf(datas.getPermission()));
            createCell(row, count++, style2).setCellValue(String.valueOf(datas.getOperator()));
            createCell(row, count++, style2).setCellValue(String.valueOf(datas.getContent()));
        }
        // 输出下载
        wb.write(writeWorkBook(response, String.valueOf(System.currentTimeMillis())));
    }

    /**
     * 将Excel文件输出
     * 
     * @param wb
     * @param fileName
     * @return
     * @throws IOException
     */
    private static ServletOutputStream writeWorkBook(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline;filename=" + fileName + ".xls");
        return response.getOutputStream();
    }

    /**
     * 创建HSSFSheet工作簿
     * 
     * @param wb
     * @param sheetName
     * @return
     */
    private static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName) {
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(12);
        sheet.setGridsPrinted(false);
        sheet.setDisplayGridlines(false);
        return sheet;
    }

    /**
     * 创建HSSFRow
     * 
     * @param sheet
     * @param rowNum
     * @param height
     * @return
     */
    private static HSSFRow createRow(HSSFSheet sheet, int rowNum) {
        HSSFRow row = sheet.createRow(rowNum);
        row.setHeight((short) 300);
        return row;
    }

    /**
     * 生成一个字体
     * 
     * @param wb
     * @return
     */
    private static HSSFFont createFont(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return font;
    }

    /**
     * 生成CellStyle
     * 
     * @param wb
     * @param font
     * @return
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook wb, HSSFFont font) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
        style.setBorderLeft(HSSFCellStyle.BORDER_DASHED);
        style.setBorderRight(HSSFCellStyle.BORDER_DASHED);
        style.setBorderTop(HSSFCellStyle.BORDER_DASHED);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        return style;
    }

    private static HSSFCellStyle createCellStyle2(HSSFWorkbook wb, HSSFFont font) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.RED.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
        style.setBorderLeft(HSSFCellStyle.BORDER_DASHED);
        style.setBorderRight(HSSFCellStyle.BORDER_DASHED);
        style.setBorderTop(HSSFCellStyle.BORDER_DASHED);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        return style;
    }

    /**
     * 生成CellStyle
     * 
     * @param wb
     * @param font
     * @return
     */
    private static HSSFCellStyle createCellStyle2(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        return style;
    }

    /**
     * 创建cell
     * 
     * @param row
     * @param cellNum
     * @param style
     * @return
     */
    private static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style) {
        HSSFCell cell = row.createCell(cellNum);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 根据单元格类型获得数据，在模板中一定要设置每个用户输入的单元格的格式，防止用户自定义输入之后 与数据库字段格式不一致，不能导入数据
     * 
     * @param hssfCell
     * @return
     * @author 赵忠诚
     */
    public static Object getCellValue(Cell hssfCell) {
        Object value = "";
        try {
            if (hssfCell != null) {
                switch (hssfCell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = hssfCell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(hssfCell)) {
                    }
                    short format = hssfCell.getCellStyle().getDataFormat();
                    SimpleDateFormat sdf = null;
                    if (format == HSSFDataFormat.getBuiltinFormat("HH:mm")) {

                    } else if (format == 20 || format == 32 || format == 176 || format == 178) {
                        // 时间
                        sdf = new SimpleDateFormat("HH:mm");
                        double cell_value = hssfCell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell_value);
                        value = sdf.format(date);
                    } else if (format == 14 || format == 31 || format == 57 || format == 58 || format == 177
                            || format == 179 || format == 22 || format == 181 || format == 178) {
                        // 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double cell_value = hssfCell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell_value);
                        value = sdf.format(date);
                    } else {
                        value = new DecimalFormat("#.####").format(hssfCell.getNumericCellValue());
                    }

                    break;
                case Cell.CELL_TYPE_FORMULA:
                    // 导入时如果为公式生成的数据则无值
                    if (!hssfCell.getStringCellValue().equals("")) {
                        value = hssfCell.getStringCellValue();
                    } else {
                        value = hssfCell.getNumericCellValue() + "";
                    }
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    value = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = (hssfCell.getBooleanCellValue() == true ? "是" : "否");
                    break;
                default:
                    value = "";
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据列Index得到列名
     * 
     * @param index
     * @return
     * @author 赵忠诚
     */
    public static String indexToColumn(int index) {
        String column = "";
        do {
            if (column.length() > 0) {
                index--;
            }
            column = ((char) (index % 26 + (int) 'A')) + column;
            index = (int) ((index - index % 26) / 26);
        } while (index > 0);

        return column;
    }

}
