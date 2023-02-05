package com.kramphub.jiramic.services;

import com.kramphub.jiramic.domain.atlassian.AtlassianSite;
import com.kramphub.jiramic.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class AtlassianService {

    private OAuthService oAuthService;

    private String jiraSiteId;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AtlassianService(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    public String getJiraSiteId(){
        logger.info("--> AtlassianService.getJiraSiteId()");
        // https://developer.atlassian.com/cloud/jira/platform/oauth-2-3lo-apps/#siteaccess
        // four our use-case we only need to go through the trouble of determining the site ID one time
        // and cache it once it has been determined
        if (jiraSiteId == null) {
            String url = "https://api.atlassian.com/oauth/token/accessible-resources";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthService.getAccessToken());

            ResponseEntity<AtlassianSite[]> response =
                    restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), AtlassianSite[].class);
            AtlassianSite[] sitesArray = response.getBody();
            List<AtlassianSite> sites = Arrays.asList(sitesArray);

            // there might be multiple sites returned (jira, confluence, etc.)
            // pick the first one with scope 'write:jira-work' and we will probably be OK
            jiraSiteId = sites.stream().filter(s -> s.getScopes().contains("write:jira-work")).findFirst().get().getId();
        }
        logger.info("<-- AtlassianService.getJiraSiteId() | " + jiraSiteId);
        return jiraSiteId;
    }

}
