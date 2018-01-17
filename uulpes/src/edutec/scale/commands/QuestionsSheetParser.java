
package edutec.scale.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hsqldb.lib.StringUtil;

import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import heracles.excel.WorkbookUtils;
import heracles.web.config.ApplicationConfiguration;

public class QuestionsSheetParser {

    private Map<String, String> propMap;
    private Map<String, Character> questionType;
    private Map<String, String> zipFileIndex; // 如果上传的zip文件，将所有的文件名和路径建立索引
    private String fileSep; // 默认的文件夹分隔符，通常是/或者\\
    private int qnum = 0; // 题目的数量
    private boolean hasLimittime = false;// 是否有限时
    private String[] picStartToken = new String[] { "[", "【" };
    private String[] picEndToken = new String[] { "]", "】" };
    private boolean hasPic = false;

    public QuestionsSheetParser() {
        propMap = new HashMap<String, String>();
        questionType = new HashMap<String, Character>();
        propMap.put("qnum", "3_2");
        propMap.put("limittime", "5_4");
        propMap.put("headRow", "5");
        propMap.put("qidCol", "1r");
        propMap.put("qtypeCol", "2");
        propMap.put("titleCol", "3");
        // propMap.put("prefixCol", "5");
        // propMap.put("postfixCol", "10");
        questionType.put("单", 'd');
        questionType.put("空", 't');
        questionType.put("矩{单}", 'j');
        questionType.put("矩（单）", 'j');
        questionType.put("题干", 'g');
        questionType.put("题干{多}", '模');
        questionType.put("多", 'm');
        // 男生答题
        questionType.put("男", '男');
        // 女生答题
        questionType.put("女", '女');
        // 判断题
        questionType.put("判断", 'p');
        // 应该答题
        questionType.put("是", '是');
        // 不应该答题
        questionType.put("否", '否');
        File file = new File(ApplicationConfiguration.getInstance().getWorkDir());
        fileSep = file.separator;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void parse(Sheet sheet, Object objs, String picPath) throws Exception {
        try {
            int jFlag = 0; // 矩阵题判断标志,
            int jnumFlag = 0; // 同时用来标志有多少个矩阵题，为一道题
            int tgFlag = 0; // 有多少题干行
            int pdFlag = 0; // 有多少判断题
            int jznumFlag = 0; // 矩阵题的题目有多少行，空读不计为数目
            int othernumFlag = 0; // 男生女生做等标志位
            int jzflag = 0; // 主要判断矩阵题的开始和结束 ，用excel做模板太操蛋了，没办法
            String jHeaderFlag = ""; // 矩阵题目序号
            String jHearderTitle = ""; // 矩阵题目名称

            String type = "单"; // 默认为单选题
            int prefixCol = -1; // 前缀所在列
            int postfixCol = -1; // 后缀所在列
            File tmpDir = new File(ApplicationConfiguration.getInstance().getWorkDir() + fileSep); // 图片保存的目录
            if (StringUtils.isNotEmpty(picPath) && picPath.indexOf(fileSep) > 0) {
                BuildIndexOfDir(
                        (tmpDir + fileSep + picPath).substring(0, (tmpDir + fileSep + picPath).lastIndexOf(fileSep)));
                hasPic = true;
            }
            Row row = sheet.getRow(5);
            int rownum = sheet.getLastRowNum() - 5;
            String prefi = String.valueOf(row.getLastCellNum());
            propMap.put("postfixCol", prefi);
            String[] rc = ((String) propMap.get("qnum")).split("_");
            try {
                qnum = new Integer(
                        WorkbookUtils.get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1)
                                .getStringCellValue());
                // prefixCol =
                // Integer.parseInt((String)propMap.get("prefixCol"));
                this.setQnum(qnum);
            } catch (NumberFormatException e) {
                objs = e.getMessage();
            }

            // 判断是否有限时列
            rc = ((String) propMap.get("limittime")).split("_");
            String limittimeTitle = WorkbookUtils
                    .get07Cell(sheet, Integer.parseInt(rc[0]) - 1, Integer.parseInt(rc[1]) - 1).getStringCellValue()
                    .trim();
            if (limittimeTitle.equals("限时"))
                this.hasLimittime = true;
            if (hasLimittime)
                prefixCol = 5;
            else
                prefixCol = 4;
            propMap.put("prefixCol", String.valueOf(prefixCol));
            postfixCol = Integer.parseInt((String) propMap.get("postfixCol"));

            int cycleQnum = qnum;
            // 标题行行数
            int headRow = Integer.parseInt((String) propMap.get("headRow"));
            // 选项个数
            int answerCount = postfixCol - prefixCol - 1;
            // 存储上一行的答案，如果重复就不更新这个数组了
            String[] answers = new String[answerCount];
            if (objs instanceof List) {
                String currentQn = "-1";
                for (int i = 0; i < cycleQnum; i++) {
                    // 读取题目的类型
                    String typeStr = "";
                    try {
                        typeStr = WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("qtypeCol")) - 1)
                                .getStringCellValue().trim();
                    } catch (Exception e) {
                        return;
                    }
                    Question q = null;
                    // 读当前行题号
                    Cell ccell = WorkbookUtils.get07Cell(sheet, headRow + i, 0);
                    if (null != ccell && StringUtils.isNotEmpty(ccell.getStringCellValue())) {
                        currentQn = ccell.getStringCellValue();
                    }
                    // 如果类型不为空
                    if (!StringUtil.isEmpty(typeStr)) {
                        type = typeStr;
                        // 所有的矩阵题数量
                        if (jFlag > 0)
                            jznumFlag = jznumFlag + jFlag - 1;
                        // 清0的作用是碰到新的矩阵题，那么子题目用_0开始算
                        jFlag = 0;
                    }
                    Character excelType = questionType.get(type.trim());
                    switch (excelType) {
                    case 'd':
                        jHearderTitle = "";
                        q = new SelectionQuestion("Q" + currentQn);
                        // q.setVisible(true);
                        // 设置为单选
                        q.setChoice(QuestionConsts.CHOICE_SINGLE);
                        // 增加题目
                        String qTitle = WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue();
                        if (StringUtils.isNotEmpty(qTitle)) {
                            // 如果包含【图1】，替换为【picScale***********_1.gif】
                            if (hasPic) {
                                if (qTitle.contains("[图") || qTitle.contains("【图")) {
                                    q.setHaspic("1");
                                    q.setTitle(this.replacePicRegex(qTitle, currentQn));
                                } else
                                    q.setTitle(qTitle);
                            } else {
                                q.setTitle(qTitle);
                            }
                        } else {// 如果标题是空的
                            if (hasPic) {
                                q.setHaspic("1");
                                // 处理图片题标题
                                List<String> titlePics = this.findPicOfQuestion(currentQn);
                                for (String titlePic : titlePics) {
                                    File file = new File(titlePic);
                                    if (file.exists()) {
                                        qTitle = StringUtils.isEmpty(qTitle)
                                                ? "<img style='vertical-align:middle;' src='..\\..\\" + titlePic
                                                        .replace(ApplicationConfiguration.getInstance().getWebappRoot(),
                                                                "")
                                                        + "' />"
                                                : qTitle + "<img style='vertical-align:middle;' src='..\\..\\"
                                                        + titlePic.replace(
                                                                ApplicationConfiguration.getInstance().getWebappRoot(),
                                                                "")
                                                        + "' />";
                                    }
                                }
                                q.setTitle(qTitle);
                            }
                        }
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }
                        // 处理前缀后缀
                        String prefixTitle = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 1);
                        String postfixTitle = WorkbookUtils.get07StringCellValue(sheet, headRow + i, postfixCol - 1);
                        if (StringUtils.isNotEmpty(prefixTitle)) {
                            q.setPrefix(prefixTitle);
                            q.setPostfix(postfixTitle);
                        }

                        // 增加选项
                        for (int j = prefixCol + 1; j < postfixCol; j++) {
                            String title = "";
                            // title = WorkbookUtils.get07Cell(sheet, headRow+i,
                            // j-1).getStringCellValue();
                            title = WorkbookUtils.get07StringCellValue(sheet, headRow + i, j - 1);
                            // 如果内容为空，则说明答案内容都相同，使用之前的答案
                            if (StringUtils.isNotEmpty(title)) {// 如果选项是非空的
                                // 如果内容为空，可能包含图片,如果图片路径不为空，说明有图片
                                if (hasPic) {
                                    // 处理图片题
                                    // answers[j-prefixCol-1] =
                                    // replacePicRegex(title, currentQn);
                                    if (title.contains("[图") || title.contains("【图")) {
                                        List<String> titlePics = this.findPicOfOption(currentQn, j - prefixCol);
                                        for (String titlePic : titlePics) {
                                            File file = new File(titlePic);
                                            if (file.exists()) {
                                                title = "<img style='vertical-align:middle;' src='..\\..\\" + titlePic
                                                        .replace(ApplicationConfiguration.getInstance().getWebappRoot(),
                                                                "")
                                                        + "' />";
                                            }
                                        }
                                    }
                                    answers[j - prefixCol - 1] = title;
                                } else {
                                    answers[j - prefixCol - 1] = title;
                                }
                            } else {
                                if (hasPic) {
                                    // 处理图片题标题
                                    List<String> titlePics = this.findPicOfOption(currentQn, j - prefixCol);
                                    for (String titlePic : titlePics) {
                                        File file = new File(titlePic);
                                        if (file.exists()) {
                                            title = StringUtils.isEmpty(title)
                                                    ? "<img style='vertical-align:middle;' src='..\\..\\"
                                                            + titlePic.replace(ApplicationConfiguration.getInstance()
                                                                    .getWebappRoot(), "")
                                                            + "' />"
                                                    : title + "<img style='vertical-align:middle;' src='..\\..\\"
                                                            + titlePic.replace(ApplicationConfiguration.getInstance()
                                                                    .getWebappRoot(), "")
                                                            + "' />";
                                        }
                                    }
                                    answers[j - prefixCol - 1] = title;
                                } else {
                                    // 说明是空选项，返回就行了
                                    continue;
                                }
                            }
                            if (StringUtils.isEmpty(answers[j - prefixCol - 1]))
                                continue;
                            Option o = new Option();
                            // option id, A,B,C,D,E
                            int value = j - prefixCol;
                            o.setValue(Integer.toString(value));
                            o.setId(String.valueOf((char) (value + 64)));
                            o.setTitle(answers[j - prefixCol - 1]);
                            System.out.println(o.toString());
                            ((SelectionQuestion) q).addOption(o);
                        }
                        if (currentQn.contains("_2")) {
                            cycleQnum = cycleQnum + 1;
                        }
                        break;
                    case 'm':
                        jHearderTitle = "";
                        q = new SelectionQuestion("Q" + currentQn);
                        // q.setVisible(true);
                        // 设置为多选
                        q.setChoice(QuestionConsts.CHOICE_MULTI);
                        // 增加题目
                        String qqTitle = WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue();
                        if (StringUtils.isNotEmpty(qqTitle)) {
                            // 如果包含【图1】，替换为【picScale***********_1.gif】
                            if (hasPic) {
                                q.setTitle(this.replacePicRegex(qqTitle, currentQn));
                            } else {
                                q.setTitle(qqTitle);
                            }
                        } else {
                            if (hasPic) {
                                // 处理图片题标题
                                List<String> titlePics = this.findPicOfQuestion(currentQn);
                                for (String titlePic : titlePics) {
                                    File file = new File(titlePic);
                                    if (file.exists()) {
                                        qTitle = StringUtils.isEmpty(qqTitle)
                                                ? "<img style='vertical-align:middle;' src='..\\..\\" + titlePic
                                                        .replace(ApplicationConfiguration.getInstance().getWebappRoot(),
                                                                "")
                                                        + "' />"
                                                : qqTitle + "<img style='vertical-align:middle;' src='..\\..\\"
                                                        + titlePic.replace(
                                                                ApplicationConfiguration.getInstance().getWebappRoot(),
                                                                "")
                                                        + "' />";
                                    }
                                }
                                q.setTitle(qqTitle);
                            }
                        }
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }
                        // 处理前缀后缀
                        prefixTitle = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 1);
                        postfixTitle = WorkbookUtils.get07StringCellValue(sheet, headRow + i, postfixCol - 1);
                        if (StringUtils.isNotEmpty(prefixTitle)) {
                            q.setPrefix(prefixTitle);
                            q.setPostfix(postfixTitle);
                        }
                        // 增加选项
                        for (int j = prefixCol + 1; j < postfixCol; j++) {
                            String title = "";
                            title = WorkbookUtils.get07Cell(sheet, headRow + i, j - 1).getStringCellValue();
                            // 如果内容为空，则说明答案内容都相同，使用之前的答案
                            if (StringUtils.isNotEmpty(title)) {
                                // 如果内容为空，可能包含图片,如果图片路径不为空，说明有图片
                                if (hasPic) {
                                    // 处理图片题
                                    answers[j - prefixCol - 1] = replacePicRegex(title, currentQn);
                                } else {
                                    answers[j - prefixCol - 1] = title;
                                }
                            } else {
                                if (hasPic) {
                                    // 处理图片题标题
                                    List<String> titlePics = this.findPicOfOption(currentQn, j - prefixCol);
                                    for (String titlePic : titlePics) {
                                        File file = new File(titlePic);
                                        if (file.exists()) {
                                            title = StringUtils.isEmpty(title)
                                                    ? "<img style='vertical-align:middle;' src='..\\..\\"
                                                            + titlePic.replace(ApplicationConfiguration.getInstance()
                                                                    .getWebappRoot(), "")
                                                            + "' />"
                                                    : title + "<img style='vertical-align:middle;' src='..\\..\\"
                                                            + titlePic.replace(ApplicationConfiguration.getInstance()
                                                                    .getWebappRoot(), "")
                                                            + "' />";
                                        }
                                    }
                                    answers[j - prefixCol - 1] = title;
                                }
                            }
                            if (StringUtils.isEmpty(answers[j - prefixCol - 1]))
                                continue;
                            Option o = new Option();
                            // option id, A,B,C,D,E
                            int value = j - prefixCol;
                            o.setValue(Integer.toString(value));
                            o.setId(String.valueOf((char) (value + 64)));
                            o.setTitle(answers[j - prefixCol - 1]);
                            ((SelectionQuestion) q).addOption(o);
                        }
                        if (currentQn.contains("_2")) {
                            cycleQnum = cycleQnum + 1;
                        }
                        break;
                    case 't':
                        jHearderTitle = "";
                        q = new SelectionQuestion("Q" + currentQn);
                        q.setVisible(true);
                        // 设置为单选
                        q.setChoice(QuestionConsts.CHOICE_TEXT);
                        // 增加题目
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        // 处理前缀后缀
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }

                        break;
                    case 'j':
                        // 为了防止混淆，加入标志位来表示判断，所有的矩阵题全放这下面
                        // 如果是0表示读取的是题目
                        if (jFlag == 0) {
                            jHearderTitle = WorkbookUtils
                                    .get07Cell(sheet, headRow + i,
                                            Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                    .getStringCellValue();
                            jFlag++;
                            // 增加一个矩阵题
                            jnumFlag++;
                        } else {
                            // 下面是非题目读取
                            q = new SelectionQuestion("Q" + currentQn + "." + Integer.toString(jFlag));// 矩阵题连接由"-"改成".",否则不好写公式。赵万锋
                            q.setChoice(QuestionConsts.CHOICE_SINGLE);
                            // 增加题目
                            q.setHeadtitle(jHearderTitle);
                            // q.setTitle(jHearderTitle+"("+WorkbookUtils.get07Cell(sheet,
                            // headRow+i,
                            // Integer.parseInt((String)propMap.get("titleCol"))-1).getStringCellValue()+")");
                            q.setTitle(WorkbookUtils
                                    .get07Cell(sheet, headRow + i,
                                            Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                    .getStringCellValue());
                            if (hasLimittime) {
                                String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i,
                                        prefixCol - 2);
                                q.setLimittime(limittime);
                            }
                            System.out.print(false);
                            // 增加选项
                            for (int j = prefixCol + 1; j < postfixCol; j++) {
                                String title = WorkbookUtils.get07Cell(sheet, headRow + i, j - 1).getStringCellValue();
                                // 如果内容为空，则说明答案内容都相同，使用之前的答案
                                if (StringUtils.isEmpty(title))
                                    continue;
                                // if(!"".equals(title) && null!=title)
                                else
                                    answers[j - prefixCol - 1] = title;
                                Option o = new Option();
                                // option id, A,B,C,D,E
                                int value = j - prefixCol;
                                o.setValue(Integer.toString(value));
                                o.setId(String.valueOf((char) (value + 64)));
                                o.setTitle(answers[j - prefixCol - 1]);
                                ((SelectionQuestion) q).addOption(o);
                            }
                            jFlag++;
                            cycleQnum = cycleQnum + 1;
                        }
                        break;
                    case 'g':
                        jHearderTitle = "";
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag));
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        String gTitle = WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue();
                        q.setTitle(replaceQNPicRegex(gTitle));
                        tgFlag++;
                        cycleQnum = cycleQnum + 1;
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }

                        break;
                    case '模':
                        jHearderTitle = "";
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag));
                        q.setTemplate(true);
                        q.setPlaceholder(true);
                        String tTitle = WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue();
                        q.setTitle(replaceQNPicRegex(tTitle));
                        tgFlag++;
                        cycleQnum = cycleQnum + 1;
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }

                        break;
                    case '男':
                        jHearderTitle = "";
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag) + "_M");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '女':
                        jHearderTitle = "";
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag) + "_W");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    case 'p':
                        jHearderTitle = "";
                        pdFlag++;
                        q = new SelectionQuestion("QPD" + Integer.toString(pdFlag));
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        // pdFlag++;
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '是':
                        jHearderTitle = "";
                        q = new SelectionQuestion("QPD" + Integer.toString(pdFlag) + "_Y");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '否':
                        jHearderTitle = "";
                        q = new SelectionQuestion("QPD" + Integer.toString(pdFlag) + "_N");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    default:
                        break;
                    }
                    if (q != null)
                        ((List) objs).add(q);
                    // 为了判断矩阵题临时加的
                    if ((!jHearderTitle.equals("")) && rownum > cycleQnum) {
                        cycleQnum = cycleQnum + 1;
                    }
                }
            }
            if (rownum > cycleQnum) {
                // 说明还有一部分题没有读取，这块主要针对的是道德量表
                String currentQn = "-1";
                for (int i = cycleQnum; i <= rownum; i++) {
                    // 读取题目的类型
                    String typeStr = "";
                    try {
                        typeStr = WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("qtypeCol")) - 1)
                                .getStringCellValue();
                    } catch (Exception e) {
                        return;
                    }
                    if (typeStr.equals("")) {
                        return;
                    }
                    Question q = null;
                    // 读当前行题号
                    Cell ccell = WorkbookUtils.get07Cell(sheet, headRow + i, 0);
                    if (null != ccell && StringUtils.isNotEmpty(ccell.getStringCellValue())) {
                        currentQn = ccell.getStringCellValue();
                    }
                    // 如果类型不为空
                    if (!StringUtil.isEmpty(typeStr)) {
                        type = typeStr;
                        // 所有的矩阵题数量
                        if (jFlag > 0)
                            jznumFlag = jznumFlag + jFlag - 1;
                        // 清0的作用是碰到新的矩阵题，那么子题目用_0开始算
                        jFlag = 0;
                    }
                    Character excelType = questionType.get(type);
                    switch (excelType) {
                    case 'd':
                        q = new SelectionQuestion("Q" + currentQn);
                        // q.setVisible(true);
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }
                        // 设置为单选
                        q.setChoice(QuestionConsts.CHOICE_SINGLE);
                        // 增加题目
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        // 增加选项
                        for (int j = prefixCol + 1; j < postfixCol; j++) {
                            String title = "";
                            title = WorkbookUtils.get07Cell(sheet, headRow + i, j - 1).getStringCellValue();
                            // 如果内容为空，则说明答案内容都相同，使用之前的答案
                            if (StringUtils.isEmpty(title))
                                continue;
                            // if(!"".equals(title) && null!=title)
                            else
                                answers[j - prefixCol - 1] = title;
                            Option o = new Option();
                            // option id, A,B,C,D,E
                            int value = j - prefixCol;
                            o.setValue(Integer.toString(value));
                            o.setId(String.valueOf((char) (value + 64)));
                            o.setTitle(answers[j - prefixCol - 1]);
                            ((SelectionQuestion) q).addOption(o);
                        }
                        if (currentQn.contains("_2")) {
                            cycleQnum = cycleQnum + 1;
                        }
                        break;
                    case 't':
                        q = new SelectionQuestion("Q" + currentQn);
                        q.setVisible(true);
                        // 设置为单选
                        q.setChoice(QuestionConsts.CHOICE_TEXT);
                        // 增加题目
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        if (hasLimittime) {
                            String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i, prefixCol - 2);
                            q.setLimittime(limittime);
                        }
                        break;
                    case 'j':
                        // 为了防止混淆，加入标志位来表示判断，所有的矩阵题全放这下面
                        // 如果是0表示读取的是题目
                        if (jFlag == 0) {
                            jHearderTitle = WorkbookUtils
                                    .get07Cell(sheet, headRow + i,
                                            Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                    .getStringCellValue();
                            jFlag++;
                            // 增加一个矩阵题
                            jnumFlag++;
                        } else {
                            // 下面是非题目读取
                            q = new SelectionQuestion("Q" + currentQn + "_" + Integer.toString(jFlag));
                            q.setChoice(QuestionConsts.CHOICE_SINGLE);
                            // 增加题目
                            q.setHeadtitle(jHearderTitle);
                            // q.setTitle(jHearderTitle+"("+WorkbookUtils.get07Cell(sheet,
                            // headRow+i,
                            // Integer.parseInt((String)propMap.get("titleCol"))-1).getStringCellValue()+")");
                            q.setTitle(WorkbookUtils
                                    .get07Cell(sheet, headRow + i,
                                            Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                    .getStringCellValue());

                            if (hasLimittime) {
                                String limittime = WorkbookUtils.get07StringCellValue(sheet, headRow + i,
                                        prefixCol - 2);
                                q.setLimittime(limittime);
                            }
                            System.out.print(false);
                            // 增加选项
                            for (int j = prefixCol + 1; j < postfixCol; j++) {
                                String title = WorkbookUtils.get07Cell(sheet, headRow + i, j - 1).getStringCellValue();
                                // 如果内容为空，则说明答案内容都相同，使用之前的答案
                                if (StringUtils.isEmpty(title))
                                    continue;
                                else
                                    answers[j - prefixCol - 1] = title;
                                Option o = new Option();
                                // option id, A,B,C,D,E
                                int value = j - prefixCol;
                                o.setValue(Integer.toString(value));
                                o.setId(String.valueOf((char) (value + 64)));
                                o.setTitle(answers[j - prefixCol - 1]);
                                ((SelectionQuestion) q).addOption(o);
                            }
                            jFlag++;
                            cycleQnum = cycleQnum + 1;
                        }
                        break;
                    case 'g':
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag));
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        tgFlag++;
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '模':
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag));
                        q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        tgFlag++;
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '男':
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag) + "_M");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '女':
                        q = new SelectionQuestion("QN" + Integer.toString(tgFlag) + "_W");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    case 'p':
                        q = new SelectionQuestion("QPD" + Integer.toString(pdFlag));
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        pdFlag++;
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '是':
                        q = new SelectionQuestion("QPD" + Integer.toString(pdFlag) + "_Y");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    case '否':
                        q = new SelectionQuestion("QPD" + Integer.toString(pdFlag) + "_N");
                        // q.setTemplate(true);
                        q.setPlaceholder(true);
                        q.setTitle(WorkbookUtils
                                .get07Cell(sheet, headRow + i, Integer.parseInt((String) propMap.get("titleCol")) - 1)
                                .getStringCellValue());
                        cycleQnum = cycleQnum + 1;
                        break;
                    default:
                        break;
                    }
                    if (q != null)
                        ((List) objs).add(q);
                }
            }
        } catch (Exception e) {
            throw new Exception("读取题本有误，请检查量表模板!");
        }
    }

    public int getQnum() {
        return qnum;
    }

    public void setQnum(int qnum) {
        this.qnum = qnum;
    }

    /**
     * 在固定的目录里所有图片建立索引
     * 
     * @param dir
     * @param qnum
     * @param index
     * @return
     */
    public void BuildIndexOfDir(String dir) {
        if (null == zipFileIndex)
            zipFileIndex = new HashMap<String, String>();
        File file = new File(dir);
        File[] files = file.listFiles();
        for (File f : files) {
            String fname = f.getName();
            if (StringUtils.isNotEmpty(fname)) {
                if (fname.toLowerCase().indexOf(".") > 0) {
                    // 把去掉后缀的文件名和绝对路径放到索引里
                    zipFileIndex.put(fname.substring(0, fname.lastIndexOf(".")), f.getAbsolutePath());
                } else {
                    System.out.println("文件" + fname + "不包含后缀");
                }
            }
        }
    }

    /**
     * 给定题号，在索引里根据文件名搜索存在的文件放到标题中
     * 
     * @param qnum
     * @return
     */
    public List<String> findPicOfQuestion(String qnum) {
        String filename = "Q" + qnum;
        List<String> picPaths = new ArrayList<String>();
        if (zipFileIndex.containsKey(filename)) {
            picPaths.add(zipFileIndex.get(filename));
            return picPaths;
        }
        for (int i = 1; i < zipFileIndex.size(); i++) {
            filename = "Q" + qnum + "_" + i;
            if (zipFileIndex.containsKey(filename)) {
                picPaths.add(zipFileIndex.get(filename));
            } else {
                break;
            }
        }
        return picPaths;
    }

    /**
     * 给定题号，在索引里根据文件名搜索存在的文件放到选项中
     * 
     * @param qnum
     * @return
     */
    public List<String> findPicOfOption(String qnum, int index) {
        List<String> picPaths = new ArrayList<String>();
        String filename = "A" + qnum + "_" + index;
        // 如果只有一个图片，写A1和A1_1都行
        if (zipFileIndex.containsKey(filename)) {
            picPaths.add(zipFileIndex.get(filename));
            return picPaths;
        }
        for (int i = 1; i < zipFileIndex.size(); i++) {
            filename = "A" + qnum + "_" + index + "_" + i;
            if (zipFileIndex.containsKey(filename)) {
                picPaths.add(zipFileIndex.get(filename));
            } else {
                break;
            }
        }
        return picPaths;
    }

    /**
     * 给定的题本标题和题号，根据索引里的文件列表，查找存在的图片，并把里面的代表图片位置的字符串替换掉
     * 
     * @param qTitle
     * @param cycleQnum
     * @return
     */
    public String replacePicRegex(String qTitle, String cycleQnum) {
        for (int i = 0; i < picStartToken.length; i++) {
            String startToken = picStartToken[i];
            String endToken = picEndToken[i];
            while (qTitle.contains(startToken + "图")) {
                String picStr = qTitle.substring(qTitle.indexOf(startToken), qTitle.indexOf(endToken) + 1);
                String qnum = picStr.substring(picStr.indexOf(picStr) + 2, picStr.indexOf(endToken));
                // 替换【图1】为绝对路径，方便xml存储
                if (zipFileIndex.containsKey("Q" + cycleQnum))
                    qTitle = qTitle.replace(picStr,
                            "<img style='vertical-align:middle;' src='..\\..\\" + zipFileIndex.get("Q" + cycleQnum)
                                    .replace(ApplicationConfiguration.getInstance().getWebappRoot(), "").replace("\\\\",
                                            "/")
                                    + "' />");
                else if (zipFileIndex.containsKey("Q" + cycleQnum + "_" + qnum))
                    qTitle = qTitle.replace(picStr,
                            "<img style='vertical-align:middle;' src='..\\..\\"
                                    + zipFileIndex.get("Q" + cycleQnum + "_" + qnum)
                                            .replace(ApplicationConfiguration.getInstance().getWebappRoot(), "")
                                            .replace("\\\\", "/")
                                    + "' />");
            }
        }
        return qTitle;
    }

    // 题干图片替换
    public String replaceQNPicRegex(String qTitle) {

        while (qTitle.contains("图例")) {
            String picStr = qTitle.substring(qTitle.indexOf("图例"), qTitle.indexOf("图例") + 3);
            String qnum = picStr.substring(picStr.indexOf("图例") + 2, picStr.indexOf("图例") + 3);
            // 替换【图1】为绝对路径，方便xml存储

            if (zipFileIndex.containsKey("例" + qnum))
                qTitle = qTitle.replace("例" + qnum,
                        "<img style='vertical-align:middle;' src='..\\..\\" + zipFileIndex.get("例" + qnum)
                                .replace(ApplicationConfiguration.getInstance().getWebappRoot(), "").replace("\\\\",
                                        "/")
                                + "' />");
        }

        return qTitle;
    }
}
