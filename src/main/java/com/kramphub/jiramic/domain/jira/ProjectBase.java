package com.kramphub.jiramic.domain.jira;

// Used to define the project of a NewIssue which needs to be created.
public class ProjectBase {
    private String id;
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public ProjectBase setKey(String key) {
        this.key = key;
        return this;
    }

}
