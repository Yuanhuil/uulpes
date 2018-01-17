package com.njpes.www.utils;

/*
 *此类为实现分页，记录了分页的各种参数，请求数据库连接的时候，需要构造并且page对象给服务器
 *服务器拦截器拦截statement，提取出sql语句和page参数，重新构造sql语句，实现分页目的。
 * @author zhaowanfeng
 * @version $Revision: 1000 $, $Date: 2014-12-01 20:47:56 +0800 (星期一, 01 十二月 2014) $
 */
public class PageParameter {
    private static String DEFAULT_PAGESIZE = "10";
    private int currentPage;// 当前页数
    private int pageSize;// 每页记录数
    private int startRow = 0;
    private int endRow;
    private long totalResult;// 总记录数
    private int totalPage;// 总页数
    private String pageStr;// 最终页面显示的底部翻页导航
    private String url;

    public PageParameter(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.startRow = currentPage > 0 ? (currentPage - 1) * pageSize : 0;
        this.endRow = currentPage * pageSize;

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public long getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(long total) {
        this.totalResult = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int pages) {
        this.totalPage = pages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 客户端分页控件标签生成
    public String getPageStr() {
        StringBuffer sb = new StringBuffer();
        sb.append("<div class=\"pageNav\">");
        sb.append("<input type=\"button\" id=\"previousPage\" class=\"button-normal white\" value=\"上页\">");
        sb.append("<a id=\"currtotal\">").append(currentPage).append("/").append(totalPage).append("</a>");
        sb.append("<input type=\"button\" id=\"nextPage\" class=\"button-normal white\" value=\"下页\">");
        sb.append("<input type=\"text\" class=\"input-small\" value=\"\" />");
        sb.append("<input id=\"goPage\" class=\"button-small white\" type=\"button\" value=\"确定\"/>");
        sb.append("</div>");
        /*
         * sb.append("<span>共</span>");
         * sb.append("<input type=\"text\" id=\"pageCount\" size=\"1\" value=\""
         * +totalPage+"\" disabled=\"disabled\">"); sb.append("<span>页</span>");
         * sb.append("<span>每页</span>");
         * sb.append("<input type=\"text\" id=\"pageSize\" size=\"1\" value=\""
         * +pageSize+"\"/>"); sb.append("<span>条</span>");
         * sb.append("<span>共</span>"); sb.
         * append("<input type=\"text\" id=\"totalResult\" size=\"1\" value=\""
         * +totalResult+"\" disabled=\"disabled\"/>");
         * sb.append("<span>记录;</span>");
         * sb.append("<a id=\"firstPage\" href=\"#\">首页</a>");
         * sb.append("<a id=\"previousPage\" href=\"#\">前一页</a>");
         * sb.append("<span>第</span>"); sb.
         * append("<input type=\"text\" id=\"currentPage\" size=\"1\" value=\""
         * +currentPage+"\"/>"); sb.append("<span>页</span>"); sb.
         * append("<input id=\"goPage\" class=\"hidBtn\" type=\"button\" value=\"确定\"/>"
         * ); sb.append("<a id=\"nextPage\" href=\"#\">后一页</a>");
         * sb.append("<a id=\"endPage\" href=\"#\">末页</a>");
         */
        pageStr = sb.toString();

        return pageStr;

    }

    public void setPageStr(String pageStr) {
        this.pageStr = pageStr;
    }

}
