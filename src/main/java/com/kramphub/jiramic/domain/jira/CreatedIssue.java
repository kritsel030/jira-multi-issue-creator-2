package com.kramphub.jiramic.domain.jira;

import java.net.URI;

// Represents the response of the Jira API endpoint 'create issue'.
// The Jira json schema also contains 'transition' and 'watchers' properties
// which we do not define as we do not need them.
public class CreatedIssue {

    private String id;

    private URI self;

    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

