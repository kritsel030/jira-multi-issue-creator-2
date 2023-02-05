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

import java.util.Arrays;
import java.util.List;

@Component
public class JiraUserService extends AbstractJiraService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JiraUserService(OAuthService oAuthService, AtlassianService atlassianService) {
        super(oAuthService, atlassianService);
    }

    /*
      throws an IllegalArgumentException when the provided 'emailAddress' field produces multiple results
      (the Jira API uses this input to do 'starts with' searches on displayName and email address)

      throws an IllegalStateException when the user does not have the required privileges to call
      the Jira endpoint
     */
    public User searchUserByEmailAddress(String emailAddress)
    throws IllegalArgumentException, IllegalStateException{
        logger.info("--> JiraUserService.searchUserByEmailAddress({})", emailAddress);

        List<User> users = searchUsers(emailAddress);

        if (users.size() > 1) {
            throw new IllegalArgumentException("You've supplied an incomplete email address; the input '" + emailAddress + "' matches with the start of the email address of multiple users.");
        }
        logger.info("<-- JiraUserService.searchUserByEmailAddress(...)");
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    // https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-user-search/#api-rest-api-3-user-search-get
    /*
      Jira permissions required: Browse users and groups global permission.
      Anonymous calls or calls by users without the required permission return empty search results.

      @param query: A query string that is matched against user attributes ( displayName, and emailAddress)
      to find relevant users.
      The string can match the prefix of the attribute's value. For example, query=john matches a user
      with a displayName of John Smith and a user with an emailAddress of johnson@example.com.

      throws an IllegalStateException when the user does not have the required privileges to call
      this Jira endpoint
     */
    private List<User> searchUsers(String query)
            throws IllegalStateException {
        logger.info("--> JiraUserService.searchUsers(...)");
        RestTemplate restTemplate = new RestTemplate();
        String url = getJiraBaseUrl() + "/rest/api/3/user/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("query", query);
        url = builder.build().encode().toUri().toString();

        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders()), User[].class);
            List<User> result = Arrays.asList(response.getBody());
            logger.info("<-- JiraUserService.searchUsers(...)");
            return result;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new IllegalStateException("User does not have sufficient privileges to search for users");
            }
            throw e;
        }
    }

}
