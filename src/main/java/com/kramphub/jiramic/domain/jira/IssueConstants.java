package com.kramphub.jiramic.domain.jira;

public interface IssueConstants {

    public static final String PROJECT_FIELD_KEY = "project";
    public static final String ISSUETYPE_FIELD_KEY = "issuetype";
    public static final String SUMMARY_FIELD_KEY = "summary";
    public static final String DESCRIPTION_FIELD_KEY = "description";
    public static final String ASSIGNEE_FIELD_KEY = "assignee";
    public static final String REPORTER_FIELD_KEY = "reporter";
    public static final String LABELS_FIELD_KEY = "labels";
    // found out the custom field info by inspecting the results of /rest/api/3/issue/createmeta
    public static final String BUDGET_TYPE_FIELD_KEY = "customfield_10491";

    public static final String STORY_ISSUETYPE = "Story";
}
