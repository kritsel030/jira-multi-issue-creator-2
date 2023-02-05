package com.kramphub.jiramic.domain.jira;

import java.net.URI;

// Cannot extend UserBase, as UserBase uses 'id' and this class uses 'accountId' for the same Jira property
public class User {

    // The URL of the user.
    private URI self;

    // The account ID of the user, which uniquely identifies the user across all Atlassian products.
    // For example, 5b10ac8d82e05b22cc7d4ef5. Required in requests.
    private String accountId;

    /* The user account type. Can take the following values:
       - atlassian: atlassian regular Atlassian user account
      - app: app system account used for Connect applications and OAuth to represent external systems
      - customer: customer Jira Service Desk account representing an external service desk
      - unknown
     */
    private String accountType;

    // The email address of the user. Depending on the user’s privacy setting, this may be returned as null.
    private String emailAddress;

    // The display name of the user. Depending on the user’s privacy setting, this may return an alternative value.
    private String diplayName;

    // Whether the user is active.
    private boolean active;

    // The time zone specified in the user's profile. Depending on the user’s privacy setting, this may be returned as null.
    private String timeZone;

    // The locale of the user. Depending on the user’s privacy setting, this may be returned as null.
    private String locale;

    // The groups that the user belongs to.
    private Object groups;

    // The application roles the user is assigned to.
    private Object applicationRoles;

    // Expand options that include additional user details in the response.
    private String expand;

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDiplayName() {
        return diplayName;
    }

    public void setDiplayName(String diplayName) {
        this.diplayName = diplayName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Object getGroups() {
        return groups;
    }

    public void setGroups(Object groups) {
        this.groups = groups;
    }

    public Object getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(Object applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }
}
