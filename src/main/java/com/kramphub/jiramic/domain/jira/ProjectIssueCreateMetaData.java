package com.kramphub.jiramic.domain.jira;

import java.net.URI;
import java.util.List;

// Models a part of the response of the Jira 'GET CreateIssueMetaData' API endpoint
public class ProjectIssueCreateMetaData {

    private String self;

    private URI url;

    private String id;

    private String key;

    private String name;

    private List<IssueTypeIssueCreateMetaData> issuetypes;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IssueTypeIssueCreateMetaData> getIssuetypes() {
        return issuetypes;
    }

    public void setIssuetypes(List<IssueTypeIssueCreateMetaData> issuetypes) {
        this.issuetypes = issuetypes;
    }
}
