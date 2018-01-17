package edutec.scale.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.SelectionQuestion;
import heracles.excel.WorkbookUtils;

public class ScoreSheetParser {

    // 用来缓存读取的第一行的题的分数
    private List<String> valueList = new ArrayList<String>();

    @SuppressWarnings("unused")
    public void parser(Sheet sheet, List<Question> questionList) throws Exception {
        try {
            // 现读取第一行数据，然后缓存起来
            Row row = sheet.getRow(5);
            int prefi = row.getLastCellNum();
            for (int j = 2; j < prefi; j++) {
                String questionValue = WorkbookUtils.get07Cell((XSSFSheet) sheet, 5, j).getStringCellValue();
                if (questionValue.equals(""))
                    continue;
                valueList.add(questionValue);
            }
            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);
                ((SelectionQuestion) question).setWriteOptXml(true);
                String id = question.getId();
                String questionnum = "";
                // 如果是题干，男、女则跳过,应该中的“是否”也跳过
                if (id.contains("N") || id.contains("PD")) {
                    continue;
                }
                if (id.contains("_")) {
                    questionnum = id.split("_")[0].substring(1);
                } else if (id.contains(".")) {// 矩阵题连接由"-"改成".",否则不好写公式。赵万锋
                    questionnum = id.split("\\.")[0].substring(1);
                } else {
                    questionnum = id.substring(1);
                }
                int readRowNum = Integer.parseInt(questionnum) + 4;
                try {
                    Row readrow = sheet.getRow(readRowNum);
                    int readprefi = readrow.getLastCellNum();
                    // 为了避开有些空的选项的分数
                    for (int j = 2, m = j; j < readprefi; j++) {
                        String questionValue = WorkbookUtils.get07Cell((XSSFSheet) sheet, readRowNum, j)
                                .getStringCellValue();
                        if (questionValue.equals("")) {
                            continue;
                        }
                        Option option = ((SelectionQuestion) question).getOptions().get(m - 2);
                        option.setValue(questionValue);
                        m++;
                    }
                } catch (Exception e) {
                    // 用第一行的数据进行填充
                    for (int m = 0; m < valueList.size(); m++) {
                        String questionValue = valueList.get(m);
                        Option option = null;
                        try {
                            option = ((SelectionQuestion) question).getOptions().get(m);
                            option.setValue(questionValue);
                        } catch (Exception ex) {
                            continue;
                        }

                    }
                }
            }

        } catch (Exception e) {
            throw new Exception("读取题本有误，请检查量表模板。");
        }
    }
}
