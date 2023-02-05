package com.kramphub.jiramic.domain.atlassian.adf;

public enum MarkType {
    ITALIC("em"),
    BOLD("strong"),
    UNDERLINE("underline"),
    LINK("link");

    public final String value;

    private MarkType(String value) {
        this.value = value;
    }
}
