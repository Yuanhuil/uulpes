package edutec.scale.render;

import edutec.scale.model.Question;

/**
 * 标志,为html页上的控件产生名称
 * 
 * @author Administrator
 */
class TagInfo {
    String tagname;
    String recorderId;
    String btname;
    Question q;

    public static TagInfo create(Question q) {
        return new TagInfo(q);
    }

    /**
     * 构造函数，为一个问题产生html标志名称 <br>
     * 1）名称就是问题id;<br>
     * 2）问题的记录id，由问题id加上_a;<br>
     * 3) 按钮名称使用问题id加上_btn; <br>
     * 
     * @param q
     *            问题对象
     */
    public TagInfo(Question q) {
        this.q = q;
        tagname = q.getId();
        recorderId = tagname + "_a";
        btname = q.getId() + "_btn";
    }

    public String getNameOrId() {
        return tagname;
    }

    public String getRecorderId() {
        return recorderId;
    }

    public String getBtname() {
        return btname;
    }

    public String getBtnId() {
        return q.getId() + "_btnId";
    }

    public String getTextId() {
        return q.getId() + "_txtId";
    }

    public String getChkId(int pos) {
        return q.getId() + "_chkId" + pos;
    }
}
