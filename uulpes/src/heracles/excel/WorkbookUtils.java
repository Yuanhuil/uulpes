package heracles.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import heracles.util.SimpleDateFormat;
import heracles.util.UtilMisc;

/**
 * <p>
 * <b>WorkbookUtils </b>is a helper of Microsoft Excel,it's based on POI project
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 1.6 $ $Date: 2010/07/21 09:37:33 $
 */
public class WorkbookUtils {

    private WorkbookUtils() {
    }

    public static HSSFWorkbook newWorkbook(boolean createSheet) {
        HSSFWorkbook wb = new HSSFWorkbook();
        if (createSheet) {
            wb.createSheet();
        }
        return wb;
    }

    /**
     * Open an excel file by real fileName
     * 
     * @param fileName
     * @return HSSFWorkbook
     * @throws ExcelException
     */
    public static HSSFWorkbook openWorkbook(String fileName) throws ExcelException {
        InputStream in = null;
        HSSFWorkbook wb = null;
        try {
            in = new FileInputStream(fileName);
            wb = new HSSFWorkbook(in);
        } catch (Exception e) {
            throw new ExcelException("File" + fileName + "not found" + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        return wb;
    }

    /**
     * Open an excel from InputStream
     * 
     * @param in
     * @return��HSSFWorkbook
     * @throws ExcelException
     */
    public static HSSFWorkbook openWorkbook(InputStream in) throws ExcelException {
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(in);
        } catch (Exception e) {
            throw new ExcelException(e.getMessage());
        }
        return wb;
    }

    /**
     * Save the Excel to OutputStream
     * 
     * @param wb
     *            HSSFWorkbook
     * @param out
     *            OutputStream
     * @throws ExcelException
     */
    public static void saveWorkbook(HSSFWorkbook wb, OutputStream out) throws ExcelException {
        try {
            wb.write(out);
        } catch (Exception e) {
            throw new ExcelException(e.getMessage());
        }
    }

    public static void saveWorkbook(XSSFWorkbook wb, OutputStream out) throws ExcelException {
        try {
            wb.write(out);
        } catch (Exception e) {
            throw new ExcelException(e.getMessage());
        }
    }

    public static void saveWorkbook(HSSFWorkbook wb, String fileName) throws ExcelException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(fileName);
            wb.write(output);
        } catch (Exception e) {
            throw new ExcelException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static void createMergedCell2003(HSSFSheet sheet, HSSFRow row, int indexRow, int columnindex, String content,
            int a1, int a2, int a3, int a4, HSSFCellStyle cellStyle, HSSFFont font, int flag) {
        // XSSFRow row = sheet.createRow(indexRow);

        HSSFCell cel0 = row.createCell(columnindex);
        if (flag == 0) {
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);
            cellStyle.setBorderTop((short) 1);
            cellStyle.setBottomBorderColor((short) 64);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);

            cellStyle.setAlignment((short) 1);
            cellStyle.setVerticalAlignment((short) 1);
            cellStyle.setDataFormat((short) 49);
            font.setColor(HSSFColor.WHITE.index);
        }
        cel0.setCellStyle(cellStyle);
        // cel0.setEncoding(HSSFCell.ENCODING_UTF_16);
        cel0.setCellValue(content);

        if (cellStyle != null) {
            if (font != null) {
                cellStyle.setFont(font);
            }
            cel0.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new Region(a1, (short) a2, a3, (short) a4));
    }

    public static void createMergedCell2007(XSSFSheet sheet, XSSFRow row, int indexRow, int columnindex, String content,
            int a1, int a2, int a3, int a4, XSSFCellStyle cellStyle, XSSFFont font, int flag) {
        // XSSFRow row = sheet.createRow(indexRow);

        XSSFCell cel0 = row.createCell(columnindex);
        if (flag == 0) {
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);
            cellStyle.setBorderTop((short) 1);
            cellStyle.setBottomBorderColor((short) 64);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);

            cellStyle.setAlignment((short) 1);
            cellStyle.setVerticalAlignment((short) 1);
            cellStyle.setDataFormat((short) 49);
            font.setColor(HSSFColor.WHITE.index);
        }
        cel0.setCellStyle(cellStyle);
        // cel0.setEncoding(HSSFCell.ENCODING_UTF_16);
        cel0.setCellValue(content);

        if (cellStyle != null) {
            if (font != null) {
                cellStyle.setFont(font);
            }
            cel0.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(a1, a2, a3, a4));
    }

    /**
     * Set value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @param value
     *            String
     */
    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, String value) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        cell.setCellValue(new HSSFRichTextString(value));
    }

    public static void setCellValue(XSSFSheet sheet, int rowNum, int colNum, String value) {
        XSSFRow row = getRow(rowNum, sheet);
        XSSFCell cell = getCell(row, colNum);
        cell.setCellValue(new XSSFRichTextString(value));
    }

    public static void setCellStyle(HSSFSheet sheet, int rowNum, int colNum, HSSFCellStyle style) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        cell.setCellStyle(style);
    }

    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, String value, HSSFCellStyle style) {
        setCellValue(sheet, rowNum, colNum, value);
        setCellStyle(sheet, rowNum, colNum, style);
    }

    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, int value, HSSFCellStyle style) {
        setCellValue(sheet, rowNum, colNum, value);
        setCellStyle(sheet, rowNum, colNum, style);
    }

    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, double value, HSSFCellStyle style) {
        setCellValue(sheet, rowNum, colNum, value);
        setCellStyle(sheet, rowNum, colNum, style);
    }

    /**
     * get value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @return String
     */
    public static String getStringCellValue(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return SimpleDateFormat.format(date);
            } else {
                return UtilMisc.ereaseZeros(String.valueOf(cell.getNumericCellValue()));
            }
        }
        return cell.getRichStringCellValue().toString();
    }

    public static String getStringCellValue(XSSFSheet sheet, int rowNum, int colNum) {
        XSSFRow row = getRow(rowNum, sheet);
        XSSFCell cell = getCell(row, colNum);
        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return SimpleDateFormat.format(date);
            } else {
                return UtilMisc.ereaseZeros(String.valueOf(cell.getNumericCellValue()));
            }
        }
        return cell.getRichStringCellValue().toString();
    }

    public static String get07StringCellValue(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null)
            return null;
        Cell cell = row.getCell(colNum);
        if (cell == null)
            return null;
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return SimpleDateFormat.format(date);
            } else {
                long longVal = Math.round(cell.getNumericCellValue());
                double doubleVal = cell.getNumericCellValue();
                if (Double.parseDouble(longVal + ".0") == doubleVal)
                    return String.valueOf(longVal);
                else
                    return String.valueOf(doubleVal);
                // return
                // UtilMisc.ereaseZeros(String.valueOf(cell.getNumericCellValue()));
            }
        }
        return cell.getRichStringCellValue().toString();
    }

    /**
     * set value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @param value
     *            double
     */
    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, double value) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        cell.setCellValue(value);
    }

    /**
     * get value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @return double
     */
    public static double getNumericCellValue(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        return cell.getNumericCellValue();
    }

    /**
     * set value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @param value
     *            Date
     */
    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, Date value) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        cell.setCellValue(value);
    }

    /**
     * get value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @return Date
     */
    public static Date getDateCellValue(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        return cell.getDateCellValue();
    }

    /**
     * set value of the cell
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @param value
     *            boolean
     */
    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, boolean value) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        cell.setCellValue(value);
    }

    /**
     * get value of the cell
     * 
     * @param sheet
     * @param rowNum
     * @param colNum
     * @return boolean value
     */
    public static boolean getBooleanCellValue(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = getCell(row, colNum);
        return cell.getBooleanCellValue();
    }

    /**
     * get Row, if not exists, create
     * 
     * @param rowCounter
     *            int
     * @param sheet
     *            HSSFSheet
     * @return HSSFRow
     */
    public static HSSFRow getRow(int rowCounter, HSSFSheet sheet) {
        HSSFRow row = sheet.getRow((short) rowCounter);
        if (row == null) {
            row = sheet.createRow((short) rowCounter);
        }
        return row;
    }

    public static XSSFRow getRow(int rowCounter, XSSFSheet sheet) {
        XSSFRow row = sheet.getRow((short) rowCounter);
        if (row == null) {
            row = sheet.createRow((short) rowCounter);
        }
        return row;
    }

    /**
     * get Cell, if not exists, create
     * 
     * @param row
     *            HSSFRow
     * @param column
     *            int
     * @return HSSFCell
     */
    public static HSSFCell getCell(HSSFRow row, int column) {
        HSSFCell cell = row.getCell((short) column);

        if (cell == null) {
            cell = row.createCell((short) column);
        }
        return cell;
    }

    public static XSSFCell getCell(XSSFRow row, int column) {
        XSSFCell cell = row.getCell((short) column);

        if (cell == null) {
            cell = row.createCell((short) column);
        }
        return cell;
    }

    /**
     * get cell, if not exists, create
     * 
     * @param sheet
     *            HSSFSheet
     * @param rowNum
     *            int
     * @param colNum
     *            int
     * @return HSSFCell
     */
    public static HSSFCell getCell(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = getRow(rowNum, sheet);
        HSSFCell cell = null == row ? null : getCell(row, colNum);
        return cell;
    }

    public static Cell get07Cell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null)
            return null;
        Cell cell = row.getCell(colNum);
        if (cell == null)
            return null;
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        return cell;
    }

    /**
     * copy row
     * 
     * @param sheet
     * @param from
     *            begin of the row
     * @param to
     *            destination fo the row
     * @param count
     *            count of copy
     */
    public static void copyRow(HSSFSheet sheet, int from, int to, int count) {

        for (int rownum = from; rownum < from + count; rownum++) {
            HSSFRow fromRow = sheet.getRow(rownum);
            HSSFRow toRow = getRow(to + rownum - from, sheet);
            if (null == fromRow)
                return;
            toRow.setHeight(fromRow.getHeight());
            toRow.setHeightInPoints(fromRow.getHeightInPoints());
            for (int i = fromRow.getFirstCellNum(); i <= fromRow.getLastCellNum() && i >= 0; i++) {
                HSSFCell fromCell = getCell(fromRow, i);
                HSSFCell toCell = getCell(toRow, i);
                toCell.setCellStyle(fromCell.getCellStyle());
                toCell.setCellType(fromCell.getCellType());
                switch (fromCell.getCellType()) {
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    toCell.setCellValue(fromCell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    toCell.setCellFormula(fromCell.getCellFormula());
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    toCell.setCellValue(fromCell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    toCell.setCellValue(fromCell.getRichStringCellValue());
                    break;
                default:
                }
            }
        }

        // copy merged region
        List<Region> shiftedRegions = new ArrayList<Region>();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            Region r = sheet.getMergedRegionAt(i);
            if (r.getRowFrom() >= from && r.getRowTo() < from + count) {
                Region n_r = new Region();
                n_r.setRowFrom(r.getRowFrom() + to - from);
                n_r.setRowTo(r.getRowTo() + to - from);
                n_r.setColumnFrom(r.getColumnFrom());
                n_r.setColumnTo(r.getColumnTo());
                shiftedRegions.add(n_r);
            }
        }

        // readd so it doesn't get shifted again
        Iterator<Region> iterator = shiftedRegions.iterator();
        while (iterator.hasNext()) {
            Region region = iterator.next();
            sheet.addMergedRegion(region);
        }
    }

    public static void shiftCell(HSSFSheet sheet, HSSFRow row, HSSFCell beginCell, int shift, int rowCount) {
        if (shift == 0)
            return;

        // get the from & to row
        int fromRow = row.getRowNum();
        int toRow = row.getRowNum() + rowCount - 1;
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            Region r = sheet.getMergedRegionAt(i);
            if (r.getRowFrom() == row.getRowNum()) {
                if (r.getRowTo() > toRow) {
                    toRow = r.getRowTo();
                }
                if (r.getRowFrom() < fromRow) {
                    fromRow = r.getRowFrom();
                }
            }
        }

        for (int rownum = fromRow; rownum <= toRow; rownum++) {
            HSSFRow curRow = WorkbookUtils.getRow(rownum, sheet);
            int lastCellNum = curRow.getLastCellNum();
            for (int cellpos = lastCellNum; cellpos >= beginCell.getCellNum(); cellpos--) {
                HSSFCell fromCell = WorkbookUtils.getCell(curRow, cellpos);
                HSSFCell toCell = WorkbookUtils.getCell(curRow, cellpos + shift);
                toCell.setCellType(fromCell.getCellType());
                toCell.setCellStyle(fromCell.getCellStyle());
                switch (fromCell.getCellType()) {
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    toCell.setCellValue(fromCell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    toCell.setCellFormula(fromCell.getCellFormula());
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    toCell.setCellValue(fromCell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    toCell.setCellValue(fromCell.getRichStringCellValue());
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    toCell.setCellErrorValue(fromCell.getErrorCellValue());
                    break;
                }
                fromCell.setCellValue(new HSSFRichTextString(""));
                fromCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFCellStyle style = wb.createCellStyle();
                fromCell.setCellStyle(style);
            }

            // process merged region
            for (int cellpos = lastCellNum; cellpos >= beginCell.getCellNum(); cellpos--) {
                HSSFCell fromCell = WorkbookUtils.getCell(curRow, cellpos);

                List<Region> shiftedRegions = new ArrayList<Region>();
                for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                    Region r = sheet.getMergedRegionAt(i);
                    if (r.getRowFrom() == curRow.getRowNum() && r.getColumnFrom() == fromCell.getCellNum()) {
                        r.setColumnFrom((short) (r.getColumnFrom() + shift));
                        r.setColumnTo((short) (r.getColumnTo() + shift));
                        // have to remove/add it back
                        shiftedRegions.add(r);
                        sheet.removeMergedRegion(i);
                        // we have to back up now since we removed one
                        i = i - 1;
                    }
                }
                // readd so it doesn't get shifted again
                Iterator<Region> iterator = shiftedRegions.iterator();
                while (iterator.hasNext()) {
                    Region region = iterator.next();
                    sheet.addMergedRegion(region);
                }
            }
        }
    }

    public static HSSFCellStyle createBorder(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        return cellStyle;
    }

}
