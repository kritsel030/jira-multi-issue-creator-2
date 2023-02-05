package com.kramphub.jiramic.domain.jira;

import java.util.List;

// Represents the response of the Jira 'GET CreateIssueMetaData' API endpoint
public class IssueCreateMetaDataResponse {

   private List<ProjectIssueCreateMetaData> projects;

    public List<ProjectIssueCreateMetaData> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectIssueCreateMetaData> projects) {
        this.projects = projects;
    }
}
