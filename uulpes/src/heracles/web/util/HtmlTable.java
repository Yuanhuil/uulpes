package heracles.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.util.TreeNode;
import heracles.util.TreeVisitor;

/**
 * 实现具有阶层结构的html表的创建 1）由表头和表体组成<br>
 * 2）表头负责显示列标题，主标题/子标题/列标题/（因素标题），其中主标题包括下属子标题，每个子标题都含有列标题<br>
 * 如果只有一个列标题组，则每一个子标题都含有同样的这样的列标题组<br>
 * 如果有多个列标题组，则每一个子标题对应列标题组顺序包含的列标题组<br>
 * 表头可以只含其中之一的标题,或任意组合，或没有，只有标题<br>
 * 因素标题和所有的列标题在同一行，描述无子节点TreeNode节点<br>
 * 3）表体的每一基本行，是一个TreeNode节点<br>
 * 
 * @author 赵万锋
 */
public class HtmlTable {
    public void inden() {
        HtmlTable t = new HtmlTable();
        t.addSubheadTitles(new String[] { "男生", "女生", "" });
        t.addHeadFactorTitiles(new String[] { "因变量" });
        t.addColTitles(new String[][] { { "平均分", "标准差" }, { "平均分", "标准差" }, { "t", "p" } });

    }

    public static void main(String[] args) {
        /*
         * |A 学校 | |男 -|B |_ 年级一| | |女 - |A |_ 年级二 |B
         */

        TreeNode tn1 = new TreeNode("学校");
        TreeNode tn2 = new TreeNode("年级一");
        TreeNode tn3 = new TreeNode("年级二");

        TreeNode tn4 = new TreeNode("男");
        TreeNode tn5 = new TreeNode("女");

        TreeNode tn6 = new TreeNode("A型血");
        TreeNode tn7 = new TreeNode("B型血");
        TreeNode tn8 = new TreeNode("o型血");

        TreeNode tn9 = new TreeNode("A型血");
        TreeNode tn10 = new TreeNode("B型血");
        TreeNode tn11 = new TreeNode("o型血");

        TreeNode tn12 = new TreeNode("A型血");
        TreeNode tn13 = new TreeNode("B型血");
        TreeNode tn14 = new TreeNode("o型血");

        TreeNode tn15 = new TreeNode("男");
        TreeNode tn16 = new TreeNode("女");
        TreeNode tn17 = new TreeNode("男");
        TreeNode tn18 = new TreeNode("女");
        TreeNode tn19 = new TreeNode("男");
        TreeNode tn20 = new TreeNode("女");

        List<Object> values = new ArrayList<Object>();
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 5; ++j) {
                values.add("" + j);
            }
        }

        tn9.setValues(values);
        tn10.setValues(values);
        tn11.setValues(values);
        tn12.setValues(values);

        tn13.setValues(values);
        tn14.setValues(values);
        tn15.setValues(values);
        tn16.setValues(values);

        tn17.setValues(values);
        tn18.setValues(values);
        tn19.setValues(values);
        tn20.setValues(values);

        /*
         * values.add("1"); values.add("2"); values.add("3"); values.add("4");
         * values.add("5");
         */
        // tn.setValues(values);
        tn1.addChild(tn2);
        tn1.addChild(tn3);
        tn2.addChild(tn4);
        tn2.addChild(tn5);
        tn3.addChild(tn6);
        tn3.addChild(tn7);
        tn3.addChild(tn8);
        tn4.addChild(tn9);
        tn4.addChild(tn10);
        tn4.addChild(tn11);
        tn5.addChild(tn12);
        tn5.addChild(tn13);
        tn5.addChild(tn14);
        tn6.addChild(tn15);
        tn6.addChild(tn16);
        tn7.addChild(tn17);
        tn7.addChild(tn18);
        tn8.addChild(tn19);
        tn8.addChild(tn20);

        HtmlTable t = new HtmlTable();
        t.addNode(tn1);
        t.addNode(tn1);

        // t.setMainHeadTitle("量表一");
        t.addSubheadTitles(new String[] { "维度一", "维度一", "维度二", "维度三", "维度四", "" });

        // t.setMainHeadTitle("dfsdfsfdsfs");
        // t.addColTitles(new String[] { "N", "平均分", "标准差", "Z", "Sig" });
        /*
         * t.addColTitles(new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
         * "9", "10", "11", "2", "3", "4", "5", "16", "2", "3", "4", "5", "21",
         * "2", "3", "4", "5", "26", "27", "28", "29", "30" } );
         */

        t.addHeadFactorTitiles(new String[] { "学校", "年级", "性别", "男女" });
        t.addColTitles(new String[][] { { "N", "平均分", "标准差", "Z", "Sig" }, { "我", "平均分", "标准差", "Z", "Sig" },
                { "们", "平均分", "标准差", "Z", "Sig" }, { "都", "平均分", "标准差", "Z", "Sig" }, { "在", "平均分", "标准差", "Z", "Sig" },
                // { "这", "平均分", "标准差", "Z", "Sig" }
                { "标准差", "Z", "Sig" } });

        t.setTableStyle(
                "cellpadding=0 cellspacing=1 bgcolor=#00CCFF width=906 border=0 cellpadding=0 cellspacing=1 bgcolor=#00CCFF");
        t.setColHeadStyle("align=center bgcolor=#FFFFFF");
        t.setTdStyle("align=center bgcolor=#FFFFFF");
        t.setSubHeadTdStyle("align=center bgcolor=#A2ECFF");

        System.out.println(t);

        // org.apache.ecs.examples.HtmlTree t = new
        // org.apache.ecs.examples.HtmlTree();
        //
    }

    private StrBuilder sbTable;
    private List<TreeNode> nodes = new ArrayList<TreeNode>();

    private String tableStyle = "";
    private String subHeadTdStyle = "";
    private String colHeadStyle = "";
    private String tdStyle = "";
    private String labTdStyle = "";

    private String tableClass;
    private String tdClass;

    private Head head;
    private String caption;

    public HtmlTable() {
        sbTable = new StrBuilder();
        head = new Head(this);
    }

    public HtmlTable(String caption) {
        this.caption = caption;
        sbTable = new StrBuilder();
        head = new Head(this);
    }

    public HtmlTable(int capacity) {
        sbTable = new StrBuilder(capacity);
        head = new Head(this);
    }

    public void addNode(TreeNode tn) {
        nodes.add(tn);
    }

    public String getColHeadStyle() {
        return colHeadStyle;
    }

    public void setColHeadStyle(String colHeadStyle) {
        this.colHeadStyle = colHeadStyle;
    }

    public String getSubHeadTdStyle() {
        return subHeadTdStyle;
    }

    public void setSubHeadTdStyle(String subHeadStyle) {
        this.subHeadTdStyle = subHeadStyle;
    }

    public String getTdStyle() {
        return tdStyle;
    }

    public void setTdStyle(String tdStyle) {
        this.tdStyle = tdStyle;
    }

    public void addAll(Collection<TreeNode> tns) {
        nodes.addAll(tns);
    }

    public void clear() {
        nodes.clear();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String toString() {
        sbTable.clear();
        sbTable.append("<table ");

        if (StringUtils.isNotBlank(tableStyle))
            sbTable.append(getTableStyle());
        else if (StringUtils.isNotBlank(tableClass)) {
            sbTable.append("class='");
            sbTable.append(tableClass);
            sbTable.append("'");
        }
        sbTable.append(">\n");

        if (this.caption != null)
            sbTable.append("<caption align=top>").append(this.caption).append("</caption>");
        /* 生成标题 */
        sbTable.append(head.buildHtmlString());
        NodeVisitor visitor = new NodeVisitor();
        for (TreeNode tn : nodes) {
            tn.visit(visitor);
        }
        sbTable.append("</table>\n");
        return sbTable.toString();
    }

    class NodeVisitor implements TreeVisitor {
        public void access(TreeNode tn) {
            if (tn.isLeaf() && !tn.isFirstChild()) {
                sbTable.append("<tr>");
            }
            int nleaf = tn.countLeaf();
            if (nleaf > 1) {
                sbTable.append("<td ");
                sbTable.append(labTdStyle);
                sbTable.append(" rowspan=" + nleaf);
                sbTable.append(" >").append(tn.getLabel());
                sbTable.append("</td>\n");
            } else if (tn.getLabel() != null) {
                sbTable.append("<td ");
                sbTable.append(labTdStyle);
                sbTable.append(" rowspan=" + nleaf);
                sbTable.append(" >").append(tn.getLabel());
                sbTable.append("</td>\n");
            }
            if (tn.getValues() != null) {
                for (Object o : tn.getValues()) {
                    sbTable.append("<td ");
                    if (StringUtils.isNotBlank(tdStyle)) {
                        sbTable.append(tdStyle);
                    } else if (StringUtils.isNotBlank(tdClass)) {
                        sbTable.append("class='");
                        sbTable.append(tdClass);
                        sbTable.append("'");
                    }
                    sbTable.append(">");
                    sbTable.append(o.toString());
                    sbTable.append("</td>");
                }
            }

            if (tn.isLeaf()) {
                sbTable.append("</tr>\n");
            }
        }
    }

    public String getTableStyle() {
        return tableStyle;
    }

    public void setTableStyle(String style) {
        this.tableStyle = style;
    }

    public void addSubheadTitle(String title) {
        head.addSubheadTitle(title);
    }

    public void addSubheadTitles(String[] titles) {
        head.addSubheadTitles(titles);
    }

    public void setMainHeadTitle(String mainheadTitle) {
        this.head.setMainHeadTitle(mainheadTitle);
    }

    public void addValColTitles(String[] titles) {
        this.head.addColTitles(titles);
    }

    public void addColTitles(String[][] titles) {
        this.head.addColTitles(titles);
    }

    public void addHeadFactorTitiles(String[] titles) {
        this.head.addFactorTitiles(titles);
    }

    class Head {
        /* 表格的总标题 */
        private String mainheadTitle;

        /* 列被分几组 */
        private int numOfGroup;

        /* 第二标题 */
        private List<String> subheadTitiles = new ArrayList<String>();

        /* 每一列的标题 */
        private List<String[]> colTitiles = new ArrayList<String[]>();

        /* 节点中非值数据的列标题 */
        private List<String> factorTitiles = new ArrayList<String>();

        /* 因素（节点中非值数据）所占用的列数 */
        private int colspanFactor;

        private HtmlTable table;

        public Head(HtmlTable table) {
            this.table = table;
        }

        public void addSubheadTitle(String title) {
            subheadTitiles.add(title);
        }

        public void addSubheadTitles(String[] titles) {
            subheadTitiles.addAll(java.util.Arrays.asList(titles));
        }

        public void addColTitles(String[] titles) {
            colTitiles.add(titles);
        }

        public void addColTitles(String[][] titles) {
            colTitiles.addAll(java.util.Arrays.asList(titles));
        }

        public void addFactorTitiles(String[] titles) {
            factorTitiles.addAll(java.util.Arrays.asList(titles));
        }

        public String buildHtmlString() {
            if (colTitiles.size() == 0) {
                return StringUtils.EMPTY;
            }
            StrBuilder sbHead = new StrBuilder();
            if (table.nodes.size() == 0) {
                throw new java.lang.RuntimeException("无内容数据[table.nodes.size==0]");
            }
            // 获得一个node做分析
            TreeNode tn = table.nodes.get(0);

            // 计算出因素的列数
            colspanFactor = tn.getDepth() + 1;

            // 如果节点的子节点没有标题，列数应该减回一
            if (tn.hasChild() && !tn.childHasLabel()) {
                --colspanFactor;
            }
            // 计算出头所占用的行数
            int rowspan = getHeadRowSize();

            // 计算出值所占用的列数,在下面语句写标题时（如果标题存在）使用
            int colspanVal = tn.getaLeaf().getValues().size();

            // 如果没有二级标题，按这个值做标题数目的显示见<a>buildSubheadTitleHtmlStr</a>方法
            // 当只用
            if (colTitiles.size() == 1) {
                numOfGroup = colspanVal / colTitiles.get(0).length;
            }

            /*
             * 如果有子标题
             */
            if (subheadTitiles.size() != 0) {
                // <tr><td rowspan=${rowspan} colspan=${colspan}><td>
                sbHead.append("<tr><td rowspan=").append(rowspan);
                sbHead.append(" colspan=").append(colspanFactor);
                sbHead.append(" " + labTdStyle);
                sbHead.append(">&nbsp;</td>");

                if (isHasMainheadTitle()) {
                    sbHead.append("<td colspan=").append(colspanVal);// 合并值列数
                    sbHead.append(">");
                    sbHead.append(mainheadTitle);
                    sbHead.append("</td></tr>\n");
                    sbHead.append("<tr>"); // 为下一行的subtitles的开头
                }

                sbHead.append(buildSubheadTitleHtmlStr());// 包含</tr>终止字符
            } else if (isHasMainheadTitle()) {
                sbHead.append("<tr><td colspan=").append(colspanVal + colspanFactor);
                sbHead.append(">").append(this.mainheadTitle).append("</td></tr>");
            }

            sbHead.append(buildColTitleHtmlStr());

            return sbHead.toString();
        }

        private int getHeadRowSize() {
            int size = 0;
            if (isHasMainheadTitle()) {
                size = 3;
            } else {
                size = 2;
            }
            /* 如果有因数标题，则所span的行数要减去一 */
            if (factorTitiles.size() != 0) {
                --size;
            }
            return size;
        }

        private String buildSubheadTitleHtmlStr() {
            StrBuilder sbSubtitle = new StrBuilder();
            int n = 0;
            for (String s : subheadTitiles) {
                if (colTitiles.size() == 1) {
                    n = 0;
                }
                sbSubtitle.append("<td colspan=").append(colTitiles.get(n).length);
                sbSubtitle.append(" " + subHeadTdStyle);
                sbSubtitle.append(">");
                sbSubtitle.append(s);
                sbSubtitle.append("</td>");
                ++n;
            }
            sbSubtitle.append("</tr>\n"); // ����
            return sbSubtitle.toString();
        }

        private String buildColTitleHtmlStr() {
            StrBuilder sbColtitle = new StrBuilder();//
            sbColtitle.append("<tr>\n");
            /* 一个一级标题，对应所有的二级标题,如果没有二级标题，则根据每行所含的值的多少来定义 */
            int loop = subheadTitiles.size();
            if (loop == 0) {
                loop = numOfGroup;
            }
            if (factorTitiles.size() > 0) {
                for (String s : this.factorTitiles) {
                    sbColtitle.append("<td ");
                    sbColtitle.append(colHeadStyle);
                    sbColtitle.append(">");
                    sbColtitle.append(s);
                    sbColtitle.append("</td>");
                }
            } else if (subheadTitiles.isEmpty()) {
                /* 如果subTitiles存在，已经将factor列span，这里就不需要再span */
                sbColtitle.append("<tr><td ");
                sbColtitle.append(" colspan=").append(colspanFactor);
                sbColtitle.append(">&nbsp;</td>");
            }
            if (colTitiles.size() == 1) {
                for (int i = 0; i < loop; ++i) {
                    String[] s = colTitiles.get(0);
                    for (int j = 0; j < s.length; ++j) {
                        sbColtitle.append("<td ");
                        sbColtitle.append(colHeadStyle);
                        sbColtitle.append(">");
                        sbColtitle.append(s[j]);
                        sbColtitle.append("</td>");
                    }
                }
            } else {
                for (String[] s : colTitiles) {
                    for (int j = 0; j < s.length; ++j) {
                        sbColtitle.append("<td ");
                        sbColtitle.append(colHeadStyle);
                        sbColtitle.append(">");
                        sbColtitle.append(s[j]);
                        sbColtitle.append("</td>");
                    }
                }
            }
            sbColtitle.append("</tr>\n");
            return sbColtitle.toString();
        }

        private boolean isHasMainheadTitle() {
            return mainheadTitle != null && mainheadTitle.trim().length() > 0;
        }

        public String getMainheadTitle() {
            return mainheadTitle;
        }

        public void setMainHeadTitle(String mainheadTitle) {
            this.mainheadTitle = mainheadTitle;
        }

        public List<String[]> getColTitiles() {
            return colTitiles;
        }

        public void setColTitiles(List<String[]> colTitiles) {
            this.colTitiles = colTitiles;
        }

    }

    public String getLabTdStyle() {
        return labTdStyle;
    }

    public void setLabTdStyle(String factorTdStyle) {
        this.labTdStyle = factorTdStyle;
    }

    public String getTableClass() {
        return tableClass;
    }

    public void setTableClass(String tableClass) {
        this.tableClass = tableClass;
    }

    public String getTdClass() {
        return tdClass;
    }

    public void setTdClass(String tdClass) {
        this.tdClass = tdClass;
    }
}
