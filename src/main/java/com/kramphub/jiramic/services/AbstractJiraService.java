package com.kramphub.jiramic.services;

import com.kramphub.jiramic.oauth.OAuthService;
import org.springframework.http.HttpHeaders;

public class AbstractJiraService {

    private AtlassianService atlassianService;

    private OAuthService oAuthService;

    public AbstractJiraService(OAuthService oAuthService, AtlassianService atlassianService) {
        this.atlassianService = atlassianService;
        this.oAuthService = oAuthService;
    }

    String getJiraBaseUrl() {
        String cloudId = atlassianService.getJiraSiteId();
        return "https://api.atlassian.com/ex/jira/" + cloudId;
    }

    HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthService.getAccessToken());
        return headers;
    }
}
