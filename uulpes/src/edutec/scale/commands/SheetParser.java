package edutec.scale.commands;

import org.apache.poi.ss.usermodel.Sheet;

public abstract class SheetParser {

    // 将sheet中的内容解析成obj对象结构
    public abstract void parse(Sheet sheet, Object obj) throws Exception;
}
