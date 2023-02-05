package com.kramphub.jiramic.domain.atlassian.adf;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

// The JIRA API is very picky with null values (triggers 400 Bad Request);
// so we  instruct the JSON object mapper to only include non-null values
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineNode extends AbstractNode {

    private String text;

    private List<Mark> marks;

    public InlineNode(NodeType type, String text) {
        super(type);
        this.text = text;
    }

    public InlineNode(NodeType type) {
        super(type);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public InlineNode addMark(Mark mark) {
        if (this.marks == null) {
            this.marks = new ArrayList<>();
        }
        this.marks.add(mark);
        return this;
    }
}
