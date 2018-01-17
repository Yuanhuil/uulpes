package com.njpes.www.utils;

public class PageSeparatorEntity {

    private int itemCount;
    private int pageNumber;
    private int pageCount;
    private int percount = 6;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPercount() {
        return percount;
    }

    public void setPercount(int percount) {
        this.percount = percount;
    }
}
