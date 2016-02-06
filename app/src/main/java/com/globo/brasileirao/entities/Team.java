package com.globo.brasileirao.entities;

/**
 * A football team.
 */
public class Team {

    private final String name;
    private final String iconUrl;

    public Team(String name, String iconUrl) {
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