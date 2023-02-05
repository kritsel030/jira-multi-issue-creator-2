package com.kramphub.jiramic.domain.config;

import java.util.List;

public class TeamsAndProjectsGroupProperties {

    private String groupDescription;

    private boolean defaultSelected;

    private List<TeamsAndProjectsProperties> teams;

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public boolean isDefaultSelected() {
        return defaultSelected;
    }

    public void setDefaultSelected(boolean defaultSelected) {
        this.defaultSelected = defaultSelected;
    }

    public List<TeamsAndProjectsProperties> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamsAndProjectsProperties> teams) {
        this.teams = teams;
    }
}
