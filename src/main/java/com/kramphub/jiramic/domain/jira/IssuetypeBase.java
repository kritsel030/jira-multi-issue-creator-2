package com.kramphub.jiramic.domain.jira;

// Used to define the issuetype of a NewIssue which needs to be created.
public class IssuetypeBase {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public IssuetypeBase setName(String name) {
        this.name = name;
        return this;
    }
}
