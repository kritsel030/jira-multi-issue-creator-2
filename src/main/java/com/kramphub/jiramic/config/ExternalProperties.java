package com.kramphub.jiramic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

// https://www.amitph.com/spring-boot-configuration-properties-validation/

// This class defines properties which need to be externally provided on application start-up
// via application property command-line arguments.

@Validated
@Configuration
@ConfigurationProperties(prefix = "external")
public class ExternalProperties {

    // spring.security.oauth2.client.registration.jira.client-secret (defined in application.properties)
    // refers to this property
    @NotBlank(message="You need to provide the external.oauth2secret application property on start-up via --external.oauth2secret=<secret value>. The secret is stored in kiya: 'kiya shared copy jira-mic/oauthsecret'.")
    private String oauth2secret;

    public String getOauth2secret() {
        return oauth2secret;
    }

    public void setOauth2secret(String oauth2secret) {
        this.oauth2secret = oauth2secret;
    }
}
