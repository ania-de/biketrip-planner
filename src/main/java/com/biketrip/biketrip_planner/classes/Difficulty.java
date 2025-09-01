package com.biketrip.biketrip_planner.classes;

public enum Difficulty {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String description;
    Difficulty(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
