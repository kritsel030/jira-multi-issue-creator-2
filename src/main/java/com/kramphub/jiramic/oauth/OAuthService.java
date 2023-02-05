package com.kramphub.jiramic.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class OAuthService {

    private static final String CLIENT_AUTHORIZATION_PATH= "/oauth2/authorization";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public String getOAuthLoginUrl() {
        logger.info("--> OAuthService.getOAuthLoginUrl()");
        String url = CLIENT_AUTHORIZATION_PATH + "/" + "jira";
        logger.info("<-- OAuthService.getOAuthLoginUrl() | " + url);
        return url;
    }

    public OAuth2AuthenticationToken getAuthenticationToken () {
        logger.info("--> OAuthService.getAuthenticationToken()");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        logger.info("  securityContext.getAuthentication().class: " + securityContext.getClass().getName());
        logger.info("<-- OAuthService.getAuthenticationToken()");
        return (OAuth2AuthenticationToken) securityContext.getAuthentication();
    }

    public boolean isUserAuthenticated () {
        logger.info("--> OAuthService.isUserAuthenticated()");
        logger.info (  "SecurityContextHolder.getContext() is " + (SecurityContextHolder.getContext() != null ? "not" : "") + " null");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Apparently the authentication instance can also be of type 'AnonymousAuthenticationToken'
        // with principal = 'anonymousUser' and authenticated = true;
        // we should not treat that situation is being authenticated
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
        logger.info("<-- OAuthService.isUserAuthenticated() | " + isAuthenticated);
        return isAuthenticated;
    }

    public String getAccessToken() {
        logger.info("--> OAuthService.getAccessToken()");
        OAuth2AuthenticationToken authentication = getAuthenticationToken();
        logger.info("  authentication is " + (authentication != null ? "not" : "") + " null");
        logger.info("  authentication.getPrincipal() is " + (authentication.getPrincipal() != null ? "not" : "") + " null");
        logger.info("  authentication.getPrincipal().getName() is " + (authentication.getPrincipal().getName() != null ? "not" : "") + " null");
        logger.info("  authentication.getAuthorizedClientRegistrationId() = " + authentication.getAuthorizedClientRegistrationId());
        logger.info("  authentication.getPrincipal().getName() = " + authentication.getName());
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getPrincipal().getName()
        );

        // In some strange cases the user seems to be authenticated
        // (the user is visiting a URL which requires authentication and he is not redirected to the homepage (= login page)
        // but still when we get here, the client is null.
        // No idea how this can happen, but let's give a bit more descriptive error message
        // instead of letting the code run into a NullPointerEception.
        if (client == null) {
            throw new IllegalStateException("Oops, it looks like you got stuck in some authentication-twilight-zone. Please go to the homepage and login again.");
        } else {
            logger.info("  client.getAccessToken() is " + (client.getAccessToken() != null ? "not" : "") + " null");
            String tokenValue = client.getAccessToken().getTokenValue();
            logger.info("<-- OAuthService.getAccessToken() | " + tokenValue.substring(0, 10) + "...");
            return tokenValue;
        }
    }
}
