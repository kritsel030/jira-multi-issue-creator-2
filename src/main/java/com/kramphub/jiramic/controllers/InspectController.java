package com.kramphub.jiramic.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kramphub.jiramic.domain.jira.NewIssue;
import com.kramphub.jiramic.oauth.OAuthService;
import com.kramphub.jiramic.services.JiraIssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class InspectController {

    @Autowired
    private Environment environment;

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private JiraIssueService jiraIssueService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/inspect-createissuemetadata")
    public String inspectCreateissuemetadata(Model model,
                                             @RequestParam(required = false) String jiraProjectKey,
                                             @RequestParam(required = false) String issuetypeIdOrName,
                                             @RequestParam(required = false) String fieldKeyOrName) {
        logger.info("--> InspectController.inspectCreateissuemetadata(...)");

        Object elementOfInterest = null;
        if (jiraProjectKey != null && !jiraProjectKey.equals("")) {
            model.addAttribute("jiraProjectKey", jiraProjectKey);
            Object responseObject = jiraIssueService.getCreateIssueMetaDataRaw(jiraProjectKey, true);
            elementOfInterest = responseObject;
            Map<String, Object> project = findProject(responseObject);
            if (project == null) {
                model.addAttribute("projectMessage", "Project does not exist or you do not have the 'create issue' permission for it.");
            } else {
                if (issuetypeIdOrName != null && !issuetypeIdOrName.equals("")) {
                    model.addAttribute("issuetypeIdOrName", issuetypeIdOrName);
                    Optional<Map<String, Object>> issuetype = findIssuetype(project, issuetypeIdOrName);
                    if (!issuetype.isPresent()) {
                        model.addAttribute("issuetypeMessage", "This issuetype does not exist within the project.");
                    } else {
                        elementOfInterest = issuetype.get();
                        if (fieldKeyOrName != null && !fieldKeyOrName.equals("")) {
                            model.addAttribute("fieldKeyOrName", fieldKeyOrName);
                            Optional<Map<String, Object>> field = findField(issuetype.get(), fieldKeyOrName);
                            if (!field.isPresent()) {
                                model.addAttribute("fieldMessage", "This field does not exist within the issuetype of the project.");
                            } else {
                                elementOfInterest = field.get();
                            }
                        }
                    }
                }
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String responsePrettyJson = gson.toJson(elementOfInterest);

            model.addAttribute("response", responsePrettyJson);
        }

        logger.info("<-- InspectController.inspectCreateissuemetadata(...)");
        return "inspect-createissuemetadata";
    }

    private static Map<String, Object> findProject(Object responseRoot) {
        Map<String, Object> rootElements = (Map)responseRoot;
        List<Map<String, Object>> projects = (List<Map<String, Object>>)rootElements.get("projects");

        // we're always calling the REST endpoint for a single project
        // If there is a result, we can safely assume we're interested in just the first item in te list
        if (projects != null && projects.size() > 0) {
            return projects.get(0);
        } else {
            return null;
        }
    }
    private static Optional<Map<String, Object>> findIssuetype(Map<String, Object> projectElement, String issuetypeIdOrName) {
        List<Map<String, Object>> issuetypes = (List<Map<String, Object>>)projectElement.get("issuetypes");
        Optional<Map<String, Object>> issuetype = issuetypes.stream().filter(i -> i.get("id").equals(issuetypeIdOrName) || i.get("name").equals(issuetypeIdOrName)).findFirst();
        return issuetype;
    }

    private static Optional<Map<String, Object>>findField(Map<String, Object> issuetype, String fieldKeyOrName) {
        Map<String, Map<String, Object>> fields = (Map<String, Map<String, Object>>)issuetype.get("fields");
        Optional<Map<String, Object>> field = fields.values().stream().filter(f -> f.get("name").equals(fieldKeyOrName) || f.get("key").equals(fieldKeyOrName)).findFirst();
        return field;
    }

    @GetMapping("/inspect-issue")
    public String inspectIssue(Model model,@RequestParam(required = false) String issueKey) {
        logger.info("--> InspectController.inspectIssue(...)");

        if (issueKey != null) {
            model.addAttribute("issueKey", issueKey);
            Object responseObject = jiraIssueService.getIssueRaw(issueKey);

            if (responseObject != null) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String responsePrettyJson = gson.toJson(responseObject);

                model.addAttribute("response", responsePrettyJson);
            } else {
                model.addAttribute("issueMessage", "This issue does not exist, or you do not have the permissions to see it.");
            }
        }

        logger.info("<-- InspectController.inspectIssue(...)");
        return "inspect-issue";
    }

}
