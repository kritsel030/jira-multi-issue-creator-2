package com.kramphub.jiramic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientRegistrationRepository oAuthClientRegistrationRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("--> SecurityConfig.configure(...)");

        ClientRegistration jiraRegistration = oAuthClientRegistrationRepository.findByRegistrationId("jira");

        http
                // allow unauthenticated access to specific paths
                .authorizeRequests()
                    .antMatchers("/", "/login-failure", "/error")
                    .permitAll()
                // deny unauthenticated access to any other paths
                // (triggers the loginPage when a user tries to access them)
                .anyRequest()
                    .authenticated()
                    .and()
                .oauth2Login()
                    // path to redirect an authenticated user to when trying to access an URL which requires authentication
                    .loginPage("/")
                    .redirectionEndpoint()
                        // The OAuth redirect URI is the path in this application that the end-user’s user-agent
                        // is redirected back to after they have authenticated with Atlassian/Jira and have granted access
                        // to the application on Atlassian's authorize application page.
                        // The baseURI must match the pattern defined by property 'spring.security.oauth2.client.registration.jira.redirect-uri-template'
                        // And it must match the path part of the Callback URL as configured in the 'Authorization' section
                        // of this app's registration in the Atlassian developer console
                        .baseUri("/login/oauth/client/*")
                        .and()
                    // path to redirect the user to after successful authentication
                    .defaultSuccessUrl("/create-issues")
                    // path to redirect the user to after unsuccessful authentication
                    .failureUrl("/login-failure");
        logger.info("<-- SecurityConfig.configure(...)");
    }

}
