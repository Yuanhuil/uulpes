package com.njpes.www.entity.baseinfo.attr;

public final class SelectOption {
    private final String value;
    private final String title;

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public SelectOption(String value, String title) {
        super();
        this.value = value;
        this.title = title;
    }
}
