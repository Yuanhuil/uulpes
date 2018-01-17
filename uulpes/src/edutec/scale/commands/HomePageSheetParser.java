package edutec.scale.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import edutec.scale.model.ScaleHomePage;
import heracles.excel.WorkbookUtils;

public class HomePageSheetParser extends SheetParser {

    private Map propMap;
    private ScaleHomePage homepageProps;

    public HomePageSheetParser() {
        // TODO Auto-generated constructor stub
        propMap = new HashMap();
        propMap.put("name", "3_2");
        propMap.put("abbr", "4_2");
        propMap.put("intro", "5_2");
        propMap.put("instr", "6_2");
        propMap.put("endWord", "7_2");
    }

    @Override
    public void parse(Sheet sheet, Object obj) {
        // TODO Auto-generated method stub
        String[] rc = ((String) propMap.get("name")).split("_");
        String name = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        rc = ((String) propMap.get("abbr")).split("_");
        String abbr = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        rc = ((String) propMap.get("intro")).split("_");
        String intro = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        rc = ((String) propMap.get("instr")).split("_");
        String instr = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                .getStringCellValue();
        rc = ((String) propMap.get("endWord")).split("_");
        String endWord = "";
        try {
            endWord = WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                    .getStringCellValue();
        } catch (Exception e) {

        }
        if (obj instanceof ScaleHomePage) {
            ((ScaleHomePage) obj).setName(name);
            ((ScaleHomePage) obj).setAbbr(abbr);
            ((ScaleHomePage) obj).setIntro(intro);
            ((ScaleHomePage) obj).setInstr(instr);
            ((ScaleHomePage) obj).setEndWord(endWord);
        }
    }
}
