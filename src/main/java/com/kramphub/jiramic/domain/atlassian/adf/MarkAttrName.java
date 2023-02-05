package com.kramphub.jiramic.domain.atlassian.adf;

public enum MarkAttrName {

    HREF("href"),
    TITLE("title");

    public final String value;

    private MarkAttrName(String value) {
        this.value = value;
    }
}
