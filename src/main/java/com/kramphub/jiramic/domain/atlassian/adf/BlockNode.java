package com.kramphub.jiramic.domain.atlassian.adf;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends AbstractNode {

    private List<AbstractNode> content;

    public BlockNode(NodeType type) {
        super(type);
        content = new ArrayList<>();
    }

    public List<AbstractNode> getContent() {
        return content;
    }

    public void setContent(List<AbstractNode> content) {
        this.content = content;
    }

    public BlockNode addContentNode(AbstractNode node) {
        this.content.add(node);
        return this;
    }
}
