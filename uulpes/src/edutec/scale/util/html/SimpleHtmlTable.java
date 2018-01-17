package edutec.scale.util.html;

import org.apache.ecs.html.TBody;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

public class SimpleHtmlTable extends Table {
    public static void main(String[] args) {
        SimpleHtmlTable table = new SimpleHtmlTable();
        TR tr1 = table.newTr();
        TD td = new TD();
        td.setTagText("dddd");
        tr1.addElement(td);
        // System.out.println(table);
    }

    private static final long serialVersionUID = 8795269412986110359L;
    private TBody tbody = new TBody();
    private boolean iniStyle;

    public boolean isIniStyle() {
        return iniStyle;
    }

    public void setIniStyle(boolean iniStyle) {
        this.iniStyle = iniStyle;
    }

    public SimpleHtmlTable(boolean iniStyle) {
        this.setIniStyle(iniStyle);
        if (iniStyle)
            setupProps();
        addElement(tbody);
    }

    public SimpleHtmlTable() {
        setAlign("center");
        addElement(tbody);
    }

    public SimpleHtmlTable(String title) {
        this.setTitle(title);
        setAlign("center");
        addElement(tbody);
    }

    // width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFCB97"
    private void setupProps() {
        this.setAlign("center");
        this.setWidth("100%");
        this.setBorder(0);
        this.setCellPadding(1);
        this.setCellSpacing(1);
        this.setBgColor("#FFCB97");
        this.setClass("td_size");
    }

    public SimpleHtmlTable(String[] coltitles) {
        super();
        addTrH(coltitles);
        addElement(tbody);
    }

    public void addTrH(String[] cells) {
        TR tr = new TR();
        for (int i = 0; i < cells.length; ++i) {
            tr.addElement(new TD(cells[i]));
        }
        tbody.addElement(tr);
    }

    public void addTrV(String[] cells) {
        if (cells.length < 6) {
            for (int i = 0; i < cells.length; ++i) {
                TR tr = new TR();
                TD dd = new TD(cells[i]);
                dd.setStyle("text-align:center");
                tr.addElement(dd);
                tbody.addElement(tr);
            }
        } else {
            TR tr = new TR();
            for (int i = 0; i < cells.length; ++i) {
                if (i % 2 == 0) {
                    tbody.addElement(tr);
                    tr = new TR();
                }
                TD dd = new TD(cells[i]);
                dd.setStyle("text-align:center");
                tr.addElement(dd);
            }
            if (cells.length % 2 != 0) {
                tbody.addElement(tr);
            }
        }
    }

    public void addTrV1(String[] cells) {
        if (cells.length < 6) {
            for (int i = 0; i < cells.length; ++i) {
                TR tr = new TR();
                TD dd = new TD(cells[i]);
                dd.setStyle("text-align:left");
                tr.addElement(dd);
                tbody.addElement(tr);
            }
        } else {
            TR tr = new TR();
            for (int i = 0; i < cells.length; ++i) {
                if (i % 2 == 0) {
                    tbody.addElement(tr);
                    tr = new TR();
                }
                TD dd = new TD(cells[i]);
                dd.setStyle("text-align:left");
                tr.addElement(dd);
            }
            if (cells.length % 2 != 0) {
                tbody.addElement(tr);
            }
        }
    }

    // bgcolor="#FFFFFF"
    public TR newTr() {
        TR tr = new TR();
        if (iniStyle)
            tr.setBgColor("#FFFFFF");
        tbody.addElement(tr);
        return tr;
    }

    public TD newTD(String title, int width) {
        TD td = new TD(title);
        td.setWidth(width);
        return td;
    }

    public TD newTD(String title, String align) {
        TD td = new TD(title);
        td.setAlign(align);
        return td;
    }

    public TD newTD(String title, int width, String align) {
        TD td = new TD(title);
        td.setWidth(width);
        td.setAlign(align);
        return td;
    }

    public TD newTDWidthStyle(String title, int width, String style) {
        TD td = new TD(title);
        td.setWidth(width);
        td.setStyle(style);
        return td;
    }
}
