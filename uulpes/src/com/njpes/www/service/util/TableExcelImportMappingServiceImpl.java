package com.njpes.www.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.util.TableExcelImportMappingMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.util.Dictionary;
import com.njpes.www.entity.util.ExcelDefaultValue;
import com.njpes.www.entity.util.ExcelFieldState;
import com.njpes.www.entity.util.TableExcelImportMapping;
import com.njpes.www.utils.ExcelUtils;

import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;

@Service("tableExcelImportMappingService")
public class TableExcelImportMappingServiceImpl implements TableExcelImportMappingServiceI {

    private Workbook wb;
    @Autowired
    private TableExcelImportMappingMapper tableExcelImportMappingMapper;

    @Autowired
    private DictionaryServiceI DictionaryService;

    public void read2003Excel(InputStream stream) {
        try {
            wb = WorkbookUtils.openWorkbook(stream);
        } catch (ExcelException e) {
            System.out.println(e.getMessage());
        }
    }

    public void read2007Excel(InputStream stream) {

        try {
            wb = new XSSFWorkbook(stream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<TableExcelImportMapping> getConfigByExcelId(Long id) {
        return tableExcelImportMappingMapper.selectByExcelid(id);
    }

    @Override
    public int getExcelModelId() {
        Sheet sheet = wb.getSheet("ID");
        Cell cell = sheet.getRow(0).getCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String s = cell.getStringCellValue();
        if (StringUtils.isBlank(s))
            return 0;
        return Integer.parseInt(s);
    }

    @Override
    public boolean write2Db(HttpServletRequest request, long excelid) {
        Sheet sheet = wb.getSheetAt(0);
        List<TableExcelImportMapping> impMapping = getConfigByExcelId(excelid);
        if (impMapping == null || impMapping.size() <= 0)
            return false;
        String insertTablename = tableExcelImportMappingMapper.selectTableName(excelid);

        List<String> fields = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (TableExcelImportMapping row : impMapping) {
            String fieldstate = row.getFieldstate();
            if (StringUtils.equals(ExcelFieldState.E.getId(), fieldstate))
                continue;
            String excel_cell = row.getExcelcell();
            String consttable = row.getConsttable();
            String consttable_name = row.getConsttabname();
            String consttable_code = row.getConsttabcode();
            String store_style = row.getStorestyle();
            String defaultval = row.getDefaultval();

            if (!StringUtils.isBlank(defaultval)) {
                Object real_default_val = null;
                try {
                    real_default_val = getDefaultValue(request, defaultval);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fields.add(row.getTabfield());
                if (real_default_val instanceof String) {
                    values.add("'" + real_default_val + "'");
                } else
                    values.add(real_default_val);
            } else {
                String[] cells = excel_cell.split(",");

                Object cell_val = null;
                for (String c : cells) {
                    Integer[] row_col = convertCellNum2RowCol(c);
                    Cell cell = sheet.getRow(row_col[0]).getCell(row_col[1]);
                    Object s = ExcelUtils.getCellValue(cell);
                    if (!StringUtils.isBlank(consttable)) {
                        if (!StringUtils.isBlank(consttable_name)) {
                            List<String> queryfields = new ArrayList<String>();
                            queryfields.add(consttable_code + " as id");
                            queryfields.add(consttable_name + " as name");
                            List<Dictionary> l = DictionaryService.selectDicWhere(consttable, queryfields,
                                    consttable_name + "='" + s.toString() + "'");
                            if (l != null && l.size() > 0) {
                                s = l.get(0).getId();
                            }
                        }
                    }
                    if (!StringUtils.isBlank(store_style)) {
                        store_style = store_style.replaceAll(c, s.toString());
                        cell_val = store_style;
                    } else {
                        cell_val = s;
                    }

                }
                if (!StringUtils.isBlank(cell_val.toString())) {
                    fields.add(row.getTabfield());
                    if (cell_val instanceof String) {
                        values.add("'" + cell_val + "'");
                    } else
                        values.add(cell_val);
                }
            }
        }
        if (StringUtils.equals(String.valueOf(excelid), "1")) {// 如果是工作计划表,添加学年学期
            fields.add("schoolyear");
            fields.add("term");
            // 判断是第几学期
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            if (month > 1 && month < 9) {
                values.add("'" + (year - 1) + "-" + year + "'");
                values.add('2');
            } else {
                values.add("'" + year + "-" + (year + 1) + "'");
                values.add('1');
            }
        }
        tableExcelImportMappingMapper.insertExcelimpData(insertTablename, fields, values);
        return false;
    }

    private Object getDefaultValue(HttpServletRequest request, String defaultval) throws ParseException {
        if (StringUtils.equals(ExcelDefaultValue.CURRENT_USER.getId(), defaultval)) {
            Account user = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
            return user.getId();
        } else if (StringUtils.equals(ExcelDefaultValue.CURRENT_DEP.getId(), defaultval)) {
            Organization orgEntity = (Organization) request.getSession().getAttribute(Constants.CURRENT_USER_ORG);
            return orgEntity.getId();
        } else if (StringUtils.equals(ExcelDefaultValue.CURRENT_DATE.getId(), defaultval)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(new Date());
        } else {
            return defaultval;
        }
    }

    private Integer[] convertCellNum2RowCol(String cellNum) {
        if (StringUtils.isBlank(cellNum))
            return null;
        Integer[] rs = new Integer[2];
        String col = cellNum.replaceAll("\\d+", "").toUpperCase();
        char[] cols = col.toCharArray();
        int col_num = 0, row_num = 0;
        for (char c : cols) {
            col_num += c - 65;
        }
        String row = cellNum.replaceAll("\\D+", "");
        row_num = Integer.parseInt(row) - 1;
        rs[0] = row_num;
        rs[1] = col_num;
        return rs;
    }
}
