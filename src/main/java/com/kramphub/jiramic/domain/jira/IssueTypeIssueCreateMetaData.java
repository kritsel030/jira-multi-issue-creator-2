package com.kramphub.jiramic.domain.jira;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

// Models a part of the response of the Jira 'GET CreateIssueMetaData' API endpoint
public class IssueTypeIssueCreateMetaData extends IssuetypeBase{

    private String self;

    private URL url;

    private String description;

    private boolean subtask;

    // Unique ID for next-gen projects.
    private UUID entityId;

    private int hierarchyLevel;

    private String expand;

    private Map<String, FieldMetaData> fields;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSubtask() {
        return subtask;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public int getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(int hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public Map<String, FieldMetaData> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldMetaData> fields) {
        this.fields = fields;
    }
}
