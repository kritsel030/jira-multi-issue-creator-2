package com.kramphub.jiramic.domain.atlassian.adf;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// The JIRA API is very picky with null values (triggers 400 Bad Request);
// so we  instruct the JSON object mapper to only include non-null values
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mark {

    private MarkType type;

    private Map<MarkAttrName, String> attrs;

    public Map<String, String> getAttrs() {
        return attrs != null ?  attrs.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().value,
                        e -> e.getValue())) : null;
    }

    public void setAttrs(Map<MarkAttrName, String> attrs) {
        this.attrs = attrs;
    }

    public Mark addAttr(MarkAttrName key, String value) {
        if (this.attrs == null) {
            this.attrs = new HashMap<>();
        }
        this.attrs.put(key, value);
        return this;
    }

    public Mark(MarkType type) {
        this.type = type;
    }

    public String getType() {
        return type.value;
    }

    public void setType(MarkType type) {
        this.type = type;
    }
}
