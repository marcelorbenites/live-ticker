package com.globo.brasileirao.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A football team.
 */
public class Team {

    private final String name;
    private final String iconUrl;

    @JsonCreator public Team(@JsonProperty("name") String name, @JsonProperty("icon") String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}