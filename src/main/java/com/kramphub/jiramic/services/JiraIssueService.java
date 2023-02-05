package com.kramphub.jiramic.services;

import com.kramphub.jiramic.domain.jira.*;
import com.kramphub.jiramic.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class JiraIssueService extends AbstractJiraService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JiraIssueService(OAuthService oAuthService, AtlassianService atlassianService) {
        super(oAuthService, atlassianService);
    }

    // https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/#api-rest-api-3-issue-createmeta-get
    // Returns details of projects, issue types within projects, and, when requested,
    // the create screen fields for each issue type for the user.
    // Use the information to populate the requests in Create issue and Create issues.
    public List<ProjectIssueCreateMetaData> getCreateIssueMetaData(List<String> projectKeys, boolean expand) {
        logger.info("--> JiraIssueService.getCreateIssueMetaData(...)");
        RestTemplate restTemplate = new RestTemplate();
        String url = getJiraBaseUrl() + "/rest/api/3/issue/createmeta";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("projectKeys", String.join(",", projectKeys));
        if (expand) {
            builder.queryParam("expand", "projects.issuetypes.fields");
        }
        url = builder.build().encode().toUri().toString();

        ResponseEntity<IssueCreateMetaDataResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders()), IssueCreateMetaDataResponse.class);
        List<ProjectIssueCreateMetaData> result = response.getBody().getProjects();
        logger.info("<-- JiraIssueService.getCreateIssueMetaData(...)");
        return result;
    }

    public Object getCreateIssueMetaDataRaw(String projectKey, boolean expand) {
        logger.info("--> JiraIssueService.getCreateIssueMetaDataRaw({}, {})", projectKey, expand);
        RestTemplate restTemplate = new RestTemplate();
        String url = getJiraBaseUrl() + "/rest/api/3/issue/createmeta";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("projectKeys", projectKey);
        if (expand) {
            builder.queryParam("expand", "projects.issuetypes.fields");
        }
        url = builder.build().encode().toUri().toString();

        ResponseEntity<Object> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders()), Object.class);
        Object result = response.getBody();
        logger.info("<-- JiraIssueService.getCreateIssueMetaDataRaw(...)");
        return result;
    }

    // https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/#api-rest-api-3-issue-post
    // https://developer.atlassian.com/server/jira/platform/jira-rest-api-examples/#creating-an-issue-examples
    public CreatedIssue createIssue(NewIssue issue) {
        logger.info("--> JiraIssueService.createIssue(...)");
        RestTemplate restTemplate = new RestTemplate();

        logger.info("  project = " + ((ProjectBase)issue.getFields().get(IssueConstants.PROJECT_FIELD_KEY)).getKey());
        // log request:
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        interceptors.add(new RestLoggingInterceptor());
//        restTemplate.setInterceptors(interceptors);

        String url = getJiraBaseUrl() + "/rest/api/3/issue";
        try {
            ResponseEntity<CreatedIssue> response =
                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(issue, getHeaders()), CreatedIssue.class);
            logger.info("<-- JiraIssueService.createIssue(...)");
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("error when creating jira issue: " + e.toString());
            throw e;
        }
    }

    // https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/#api-rest-api-3-issue-issueidorkey-get
    public Object getIssueRaw(String issueKey) {
        logger.info("--> JiraIssueService.getIssue(" + issueKey + ")");
        RestTemplate restTemplate = new RestTemplate();

        String url = getJiraBaseUrl() + "/rest/api/3/issue/" + issueKey;
        try {
            ResponseEntity<Object> response =
                    restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders()), Object.class);
            logger.info("<-- JiraIssueService.getIssue(...)");
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return null;
            } else {
                throw e;
            }
        }

    }
}
