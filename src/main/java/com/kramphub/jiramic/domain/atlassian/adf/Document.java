package com.kramphub.jiramic.domain.atlassian.adf;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

// Atlassian Document Format
// https://developer.atlassian.com/cloud/jira/platform/apis/document/structure/
public class Document extends BlockNode {

    private int version = 1;

    public Document() {
        super(NodeType.ROOT_DOC);
        this.version = 1;
    }

    public int getVersion() {
        return version;
    }

    @JsonIgnore
    public BlockNode getLastContentNode() {
        return (BlockNode)getContent().get(getContent().size()-1);
    }
}
