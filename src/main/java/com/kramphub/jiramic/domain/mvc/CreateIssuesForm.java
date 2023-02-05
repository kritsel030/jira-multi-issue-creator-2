package com.kramphub.jiramic.domain.mvc;

import java.util.List;

public class CreateIssuesForm {

    public static final String BUDGET_TYPE_ENABLER = "Enabler";
    public static final String BUDGET_TYPE_NONE = "None";

    public static final String REPORTER_YOU = "you";
    public static final String REPORTER_SOMEONE_ELSE = "someoneElse";


    private String summary;

    private String reporterChoice;

    private String reporterEmail;

    private String description;

    private String budgetType;

    private String label;

    private boolean storiesCreated;

    private List<String> selectedJiraProjectKeys;

    public CreateIssuesForm() {
        // set default values
        this.reporterChoice = REPORTER_YOU;
        this.budgetType = BUDGET_TYPE_NONE;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public void setStoriesCreated(boolean storiesCreated) {
        this.storiesCreated = storiesCreated;
    }

    public boolean isStoriesCreated() {
        return storiesCreated;
    }

    public List<String> getSelectedJiraProjectKeys() {
        return selectedJiraProjectKeys;
    }

    public void setSelectedJiraProjectKeys(List<String> selectedJiraProjectKeys) {
        this.selectedJiraProjectKeys = selectedJiraProjectKeys;
    }

    public String getReporterChoice() {
        return reporterChoice;
    }

    public void setReporterChoice(String reporterChoice) {
        this.reporterChoice = reporterChoice;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public void validate() {
        // summary
        if (summary == null || summary.length() == 0) {
            throw new IllegalStateException("summary may not be empty");
        }

        // reporter
        if (reporterChoice.equals(REPORTER_SOMEONE_ELSE) && (reporterEmail == null || reporterEmail.length() == 0)) {
            throw new IllegalStateException("if you need someone else to be the reporter, you have to enter an email address");
        }

        // description
        if (description == null || description.length() == 0) {
            throw new IllegalStateException("description may not be empty");
        }

        // label
        if (label != null && label.contains(" ")) {
            throw new IllegalStateException("label may not contain spaces ('" + label + "')");
        }
    }

}
