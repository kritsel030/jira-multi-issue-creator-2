package com.kramphub.jiramic.config;

import com.kramphub.jiramic.config.YamlPropertySourceFactory;
import com.kramphub.jiramic.domain.config.TeamsAndProjectsGroupProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "config")
@PropertySource(value = "classpath:teams-and-projects.yaml", factory = YamlPropertySourceFactory.class)
public class TeamsAndProjectsConfig {

    private List<TeamsAndProjectsGroupProperties> teamsAndProjects;

    public List<TeamsAndProjectsGroupProperties> getTeamsAndProjects() {
        return teamsAndProjects;
    }

    public void setTeamsAndProjects(List<TeamsAndProjectsGroupProperties> teamsAndProjects) {
        this.teamsAndProjects = teamsAndProjects;
    }
}
