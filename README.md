# README #

### What does jira-multi-issue-creator do? 

The `jira-multi-issue-creator` is your friend when you want to automatically create 
**the same issue** in **multiple projects**.

Example use-case: you want all scrum teams to ensure that all of their GCP resources are correctly labelled.
This requires a Jira story for every scrum team.

### Under the hood
This is a Spring boot application which you need to run locally. 
It requires maven and Java to be available on your local environment.

It uses OAuth2 to logon to Atlassian Jira, so it can create user stories on your behalf.

It uses the Jira rest API to fetch project details and create stories.

It has a hard-coded list of teams (and guilds) and their main Jira project. 
These are the projects in which stories can be created.


### One-time set-up

Clone this repository to your local environment.

### How to run?

prerequisites:

* maven
* Java 1.7 (or higher)  

steps:

1. Get this application's oauth secret from kiya: `kiya shared copy jira-mic/oauthsecret`
2. Build and start the spring boot app, providing the secret as a command line parameter: `mvn spring-boot:run -Dspring-boot.run.arguments=--external.oauth2secret=<kiya secret>`
3. Once the application has started, go to https://localhost:8080, and enjoy!

### How to use?

Once the application has started and you browsed to https://localhost:8080:

1. Logon
2. Define the story you want to create (summary, description, label, etc.)
3. A list of teams and their team Jira projects appears (these teams and projects are configured in a property file). 
4. You select the Jira projects in which you want to create the story.
5. You hit 'Create stories'
6. The list of created stories is displayed.

### Atlassian OAuth app registration

As this application uses OAuth to authenticate with Atlassian, it needs to be set-up as an OAuth app in Atlassian's
developer portal environment. Currently the app is registered under Kristel's account.

If for some reason this registration no longer works, z
this is how to register another instance of this app with Atlassian yourself.

1. Go to https://developer.atlassian.com/console/myapps/
2. Hit 'Create' and choose 'OAuth 2.0 integration'
3. In the 'Distribution' section click 'Edit' and
    * Set distribution status to 'Sharing'
    * Set vendor name to 'KrampHub'
    * Set privacy policy to https://kramphub.nl (or any other functioning URL)
4. In the 'Permissions' section:
   * Next to 'User identity API' click 'Add' (no need to configure)
   * Next to 'Jira API' click 'Add' and then 'Configure'
      * Click 'Edit scopes' and add these scope 'write:jira-work', 'read:jira-work' and 'read:jira-user'
5. In the 'Authorization' section:
    * Next to 'OAuth 2.0 (3LO)' click 'Add'
       * Use 'http://localhost:8080/login/oauth/client/jira' as the callback URL
6. In the 'Settings' section:
    * Scroll all the way to the bottom
    * Copy `client-id` to /src/main/resources/application.properties
    * Copy `secret` and store it in kiya (`kiya shared put jira-mic/oauthsecret <secret>`)

### Owner
Kristel Nieuwenhuys

### This app wouldn't have been here without
* https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/
* https://developer.atlassian.com/cloud/jira/platform/oauth-2-3lo-apps/
* https://bjoernkw.com/2019/03/31/oauth-2-0-authentication-with-jira-a-spring-boot-example-application/
* https://github.com/BjoernKW/oauth2-with-jira


