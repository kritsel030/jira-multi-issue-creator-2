package com.kramphub.jiramic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("login-failure");
        registry.addViewController("create-issues");
        registry.addViewController("create-issues-result");
        registry.addViewController("inspect-createissuemetadata");
        registry.addViewController("inspect-issue");
    }
}
