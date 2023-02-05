package com.kramphub.jiramic.domain.jira;

import com.kramphub.jiramic.domain.atlassian.adf.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents the request body for the Jira API endpoint 'create issue'
public class NewIssue{

    private Map<String, Object> fields = new HashMap<>();

    public Map<String, Object> getFields() {
        return fields;
    }

    public NewIssue setTextField(String fieldKey, String value) {
        this.fields.put(fieldKey, value);
        return this;
    }

    public NewIssue setUserField(String fieldKey, UserBase user) {
        this.fields.put(fieldKey, user);
        return this;
    }

    public NewIssue setDocumentField(String fieldKey, Document document) {
        this.fields.put(fieldKey, document);
        return this;
    }

    public NewIssue setIssuetypeField(String fieldKey, IssuetypeBase issueType) {
        this.fields.put(fieldKey, issueType);
        return this;
    }

    public NewIssue setProjectField(String fieldKey, ProjectBase project) {
        this.fields.put(fieldKey, project);
        return this;
    }

    public NewIssue setListField(String fieldKey, List<String> list) {
        this.fields.put(fieldKey, list);
        return this;
    }
    
    public NewIssue setSingleSelectField(String fieldKey, String value) {
        Map fieldValue = new HashMap();
        fieldValue.put("value", value);
        this.fields.put(fieldKey, fieldValue);
        return this;
    }
}

