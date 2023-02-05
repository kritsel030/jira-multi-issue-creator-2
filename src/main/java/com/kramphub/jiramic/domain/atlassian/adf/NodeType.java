package com.kramphub.jiramic.domain.atlassian.adf;

public enum NodeType {
    ROOT_DOC("doc"),
    BLOCK_PARAGRAPH("paragraph"),
    BLOCK_ORDERED_LIST("orderedList"),
    BLOCK_BULLET_LIST("bulletList"),
    BLOCK_LIST_ITEM("listItem"),
    INLINE_TEXT("text"),

    INLINE_LINK("link"),
    INLINE_HARD_BREAK("hardBreak");

    public final String value;

    private NodeType(String value) {
        this.value = value;
    }
}
