package edutec.scale.commands;

import org.apache.poi.ss.usermodel.Sheet;

import heracles.excel.WorkbookUtils;

public class ReportImageParamParser {
    private String imageParam1;
    private String imageParam2;

    public void parse(Sheet sheet) {
        if (WorkbookUtils.get07Cell(sheet, 0, 1) != null)
            imageParam1 = WorkbookUtils.get07Cell(sheet, 0, 1).getStringCellValue();
        if (WorkbookUtils.get07Cell(sheet, 1, 1) != null)
            imageParam2 = WorkbookUtils.get07Cell(sheet, 1, 1).getStringCellValue();

    }

    public String getImageParam1() {
        return imageParam1;
    }

    public String getImageParam2() {
        return imageParam2;
    }

}
