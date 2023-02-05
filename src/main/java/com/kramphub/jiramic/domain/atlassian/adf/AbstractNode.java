package com.kramphub.jiramic.domain.atlassian.adf;

public class AbstractNode {

    private NodeType type;

    public AbstractNode(NodeType type) {
        this.type = type;
    }

    public String getType() {
        return type.value;
    }

    public void setType(NodeType type) {
        this.type = type;
    }
}
