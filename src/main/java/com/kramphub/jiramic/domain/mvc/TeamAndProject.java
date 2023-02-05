package com.kramphub.jiramic.domain.mvc;

public class TeamAndProject {

    private String teamName;

    private String projectKey;

    private boolean selected;

    private boolean selectEnabled;

    private String projectName;

    private boolean storyIssueTypeSupported;

    private boolean budgetTypeFieldSupported;

    private boolean createIssueAllowed;

    private String createdIssueKey;

    private String createIssueErrorMessage;

    public TeamAndProject(String teamName, String projectKey) {
        this.teamName = teamName;
        this.projectKey = projectKey;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectEnabled() {
        return selectEnabled;
    }

    public void setSelectEnabled(boolean selectEnabled) {
        this.selectEnabled = selectEnabled;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isStoryIssueTypeSupported() {
        return storyIssueTypeSupported;
    }

    public void setStoryIssueTypeSupported(boolean storyIssueTypeSupported) {
        this.storyIssueTypeSupported = storyIssueTypeSupported;
    }

    public boolean isBudgetTypeFieldSupported() {
        return budgetTypeFieldSupported;
    }

    public void setBudgetTypeFieldSupported(boolean budgetTypeFieldSupported) {
        this.budgetTypeFieldSupported = budgetTypeFieldSupported;
    }

    public boolean isCreateIssueAllowed() {
        return createIssueAllowed;
    }

    public void setCreateIssueAllowed(boolean createIssueAllowed) {
        this.createIssueAllowed = createIssueAllowed;
    }

    public String getCreatedIssueKey() {
        return createdIssueKey;
    }

    public void setCreatedIssueKey(String createdIssueKey) {
        this.createdIssueKey = createdIssueKey;
    }

    public String getCreateIssueErrorMessage() {
        return createIssueErrorMessage;
    }

    public void setCreateIssueErrorMessage(String createIssueErrorMessage) {
        this.createIssueErrorMessage = createIssueErrorMessage;
    }

}

