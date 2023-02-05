package com.kramphub.jiramic.controllers;

import com.kramphub.jiramic.config.TeamsAndProjectsConfig;
import com.kramphub.jiramic.domain.config.TeamsAndProjectsGroupProperties;
import com.kramphub.jiramic.domain.config.TeamsAndProjectsProperties;
import com.kramphub.jiramic.domain.atlassian.adf.Document;
import com.kramphub.jiramic.domain.mvc.TeamAndProject;
import com.kramphub.jiramic.domain.jira.*;
import com.kramphub.jiramic.domain.mvc.CreateIssuesForm;
import com.kramphub.jiramic.services.JiraIssueService;
import com.kramphub.jiramic.oauth.OAuthService;
import com.kramphub.jiramic.services.JiraUserService;
import com.kramphub.jiramic.util.ADFUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static com.kramphub.jiramic.domain.jira.IssueConstants.*;

@Controller
public class CreateIssuesController {

    private static final String SESSION_TEAMPROJECTGROUPS = "teamprojectgroups";
    private static final String SESSION_FORM = "form";

    @Autowired
    private Environment environment;

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private JiraIssueService jiraIssueService;

    @Autowired
    private JiraUserService jiraUserService;

    @Autowired
    private TeamsAndProjectsConfig teamsAndProjectsConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/create-issues")
    public String prepare(Model model, HttpSession session) {
        logger.info("--> CreateIssuesController.prepare(...)");

        if (session.getAttribute(SESSION_TEAMPROJECTGROUPS) == null) {
            // set-up teams and projects based on teams-and-projects.yaml
            Map<String, List<TeamAndProject>> teamsAndProjectsGroups = new LinkedHashMap<>();
            for (TeamsAndProjectsGroupProperties groupConfig : teamsAndProjectsConfig.getTeamsAndProjects()) {
                List<TeamAndProject> teams = new ArrayList<>();
                teamsAndProjectsGroups.put(groupConfig.getGroupDescription(), teams);
                for (TeamsAndProjectsProperties teamConfig : groupConfig.getTeams()) {
                    TeamAndProject team = new TeamAndProject(teamConfig.getName(), teamConfig.getJiraProjectKey());
                    team.setSelected(teamConfig.isDefaultSelected() || groupConfig.isDefaultSelected());
                    teams.add(team);
                }
            }

            // retrieve CreateIssueMetaData for all projects
            List<String> allJiraKeys = teamsAndProjectsGroups.values().stream().flatMap(l -> l.stream().map(tp -> tp.getProjectKey())).collect(Collectors.toList());
            List<ProjectIssueCreateMetaData> teamProjectsMetaData = jiraIssueService.getCreateIssueMetaData(allJiraKeys, true);

            // enrich all TeamAndProject entries in teamsAndProjectsGroups with the CreateIssueMetaData results
            enrich(teamsAndProjectsGroups, teamProjectsMetaData);

            session.setAttribute(SESSION_TEAMPROJECTGROUPS, teamsAndProjectsGroups);
        }

        Map<String, List<TeamAndProject>> teamsAndProjectsGroups = (Map<String, List<TeamAndProject>>)session.getAttribute(SESSION_TEAMPROJECTGROUPS);

        model.addAttribute("teamsAndProjectsGroups", teamsAndProjectsGroups);
        model.addAttribute("principal", oAuthService.getAuthenticationToken().getPrincipal().getName());

        if (session.getAttribute(SESSION_FORM) == null) {
            session.setAttribute(SESSION_FORM, new CreateIssuesForm());
        }
        CreateIssuesForm form = (CreateIssuesForm)session.getAttribute(SESSION_FORM);
        model.addAttribute("form", form);

        logger.info("<-- CreateIssuesController.prepare(...)");
        return "create-issues";
    }

    @PostMapping("/create-issues")
    public String createIssues(@ModelAttribute CreateIssuesForm form, Model model, HttpSession session) {
        logger.info("--> CreateIssuesController.createIssues(...)");
        model.addAttribute("form", form);
        model.addAttribute("principal", oAuthService.getAuthenticationToken().getPrincipal().getName());
        form.setStoriesCreated(true);
        session.setAttribute(SESSION_FORM, form);

        logger.info("reporter choice: {}", form.getReporterChoice());
        logger.info("reporter email: {}", form.getReporterEmail());

        // poor man's form validation, but it works
        // this will throw errors which will be handled by the default error page
        form.validate();

        // only for test-purposes
        List<String> acceptedProjects = new ArrayList<>();
        acceptedProjects.add("ARCH");

        Map<String, List<TeamAndProject>> teamsAndProjectsGroups = (Map<String, List<TeamAndProject>>)session.getAttribute(SESSION_TEAMPROJECTGROUPS);
        reset(teamsAndProjectsGroups);
        model.addAttribute("teamsAndProjectsGroups", teamsAndProjectsGroups);

        // use the form data to correctly mark the selected teams in teamsAndProjectsGroups
        teamsAndProjectsGroups
                .values().stream().flatMap(List::stream)
                .forEach(tp -> tp.setSelected(form.getSelectedJiraProjectKeys().contains(tp.getProjectKey())));

        // prepare for issue creation
        List<TeamAndProject> selectedTeamProjects = teamsAndProjectsGroups
                .values().stream().flatMap(List::stream)
                .filter(tp -> tp.isSelected())
                // only for test-purposes
                .filter(tp -> acceptedProjects.contains(tp.getProjectKey())).collect(Collectors.toList());
        NewIssue issue = buildNewIssue(form);
        
        // create the issue in the projects which do NOT support the 'Budget type' custom field
        List<TeamAndProject> nonBudgetTypeProjects = selectedTeamProjects
                .stream().filter(tp -> !tp.isBudgetTypeFieldSupported()).collect(Collectors.toList());
        for (TeamAndProject teamProject : nonBudgetTypeProjects) {
            issue.setProjectField(PROJECT_FIELD_KEY, new ProjectBase().setKey(teamProject.getProjectKey()));
            createJiraIssue(issue, teamProject);
        }

        // create the issue in the projects which DO support the 'Budget type' custom field
        List<TeamAndProject> budgetTypeProjects = selectedTeamProjects
                .stream().filter(tp -> tp.isBudgetTypeFieldSupported()).collect(Collectors.toList());
        for (TeamAndProject teamProject : budgetTypeProjects) {
            if (form.getBudgetType() != null && form.getBudgetType().equals("Enabler")) {
                issue.setSingleSelectField(BUDGET_TYPE_FIELD_KEY, form.getBudgetType());
            }
            issue.setProjectField(PROJECT_FIELD_KEY, new ProjectBase().setKey(teamProject.getProjectKey()));
            createJiraIssue(issue, teamProject);
        }

        logger.info("<-- CreateIssuesController.createIssues(...)");
        return "redirect:/create-issues-result";
    }

    @GetMapping("/create-issues-result")
    public String redirectAfterPost(Model model, HttpSession session) {
        model.addAttribute("principal", oAuthService.getAuthenticationToken().getPrincipal().getName());
        Map<String, List<TeamAndProject>> teamsAndProjectsGroups = (Map<String, List<TeamAndProject>>)session.getAttribute(SESSION_TEAMPROJECTGROUPS);
        model.addAttribute("teamsAndProjectsGroups", teamsAndProjectsGroups);
        return "create-issues-result";
    }

    /**
     * Determines the following for each Jira project in teamsAndProjectsGroups:
     * - does the logged in user have the 'create issue' permission for the project?
     * - the name of the Jira project (only available when the user has 'create issue' permission)
     * - does the Jira project have an issuetype named 'Story'?
     * - does this 'Story' issuetype have the Budget type custom field?
     *
     @param teamsAndProjectsGroups teams and jira project group info read from configuration
     @param projectMetaDataList List of projects (and their metadata) for which the logged on user has the 'create issue' permission
     */
    private void enrich(Map<String, List<TeamAndProject>> teamsAndProjectsGroups, List<ProjectIssueCreateMetaData> projectMetaDataList) {
        for (List<TeamAndProject> teamGroup : teamsAndProjectsGroups.values()) {
            for (TeamAndProject teamProject : teamGroup) {

                // now let's see if the project for this Jira Key is present in the metadata
                Optional<ProjectIssueCreateMetaData> projectMetaData = findProjectByKey(projectMetaDataList, teamProject.getProjectKey());
                if (projectMetaData.isPresent()) {
                    teamProject.setProjectName(projectMetaData.get().getName());
                    teamProject.setCreateIssueAllowed(true);
                    teamProject.setSelectEnabled(true);
                    // does the project support the 'Story' issuetype?
                    Optional<IssueTypeIssueCreateMetaData> issueTypeMetaData = findIssueTypeByName(projectMetaData.get(), STORY_ISSUETYPE);
                    teamProject.setStoryIssueTypeSupported(issueTypeMetaData.isPresent());
                    if (issueTypeMetaData.isPresent()) {
                        // does the 'Story' issuetype support the 'Budget type' custom field?
                        Optional<Map.Entry<String, FieldMetaData>> fieldMetaData = findFieldByFieldKey(issueTypeMetaData.get(), BUDGET_TYPE_FIELD_KEY);
                        teamProject.setBudgetTypeFieldSupported(fieldMetaData.isPresent());
                    }
                }
            }
        }
    }

    // Creates a Jira issue via the Jira API
    private void createJiraIssue(NewIssue issue, TeamAndProject teamProject) {
        try {
            CreatedIssue createdIssue = jiraIssueService.createIssue(issue);
            teamProject.setCreatedIssueKey(createdIssue.getKey());
        } catch (Exception e) {
            teamProject.setCreateIssueErrorMessage(e.toString());
        }
    }

    // Constructs a NewIssue instance
    private NewIssue buildNewIssue(CreateIssuesForm form) {
        NewIssue issue = new NewIssue();
        issue.setIssuetypeField(ISSUETYPE_FIELD_KEY, new IssuetypeBase().setName(STORY_ISSUETYPE));
        issue.setTextField(SUMMARY_FIELD_KEY, form.getSummary());
        // https://confluence.atlassian.com/jirakb/how-to-set-assignee-to-unassigned-via-rest-api-in-jira-744721880.html
        issue.setUserField(ASSIGNEE_FIELD_KEY, new UserBase());
        if (form.getReporterChoice().equals(form.REPORTER_SOMEONE_ELSE)) {
            User user = jiraUserService.searchUserByEmailAddress(form.getReporterEmail());
            UserBase reporter = new UserBase();
            reporter.setId(user.getAccountId());
            logger.info("reporter accountId: {}", reporter.getId());
            issue.setUserField(REPORTER_FIELD_KEY, reporter);
        }
        Document description = ADFUtil.textAreaToDocument(form.getDescription());
        if (description != null) {
            issue.setDocumentField(DESCRIPTION_FIELD_KEY, description);
        }
        if (form.getLabel() != null && !form.getLabel().contains(" ")) {
            List<String> labels = new ArrayList<>();
            labels.add(form.getLabel());
            issue.setListField(LABELS_FIELD_KEY, labels);
        }
        return issue;
    }

    // Resets all fields related to issue creation results,
    // so the object can be re-used to create another batch of stories
    // based on the same story definition and team/project selections
    private static void reset(Map<String, List<TeamAndProject>> teamsAndProjectsGroups) {
        teamsAndProjectsGroups
                .values().stream().flatMap(List::stream)
                .forEach(tp -> {
                    tp.setCreatedIssueKey(null);
                    tp.setCreateIssueErrorMessage(null);
                });
    }

    private static Optional<ProjectIssueCreateMetaData> findProjectByKey(List<ProjectIssueCreateMetaData> projectMetaDataList, String jiraKey) {
        return projectMetaDataList.stream().filter(p -> p.getKey().equals(jiraKey)).findFirst();
    }

    private static Optional<IssueTypeIssueCreateMetaData> findIssueTypeByName(ProjectIssueCreateMetaData projectMetaData, String issueTypeName) {
        return projectMetaData.getIssuetypes().stream().filter(it -> it.getName().equals(issueTypeName)).findFirst();
    }

    private static Optional<Map.Entry<String, FieldMetaData>> findFieldByFieldKey(IssueTypeIssueCreateMetaData issueTypeMetaData, String customFieldKey) {
        return issueTypeMetaData.getFields().entrySet().stream().filter(f -> f.getValue().getKey().equals(customFieldKey)).findFirst();
    }
}

