package com.soumaya.FctApp.backend.Activity;

import lombok.Getter;

@Getter
public enum ActivityType {
    PRESENTATION("Presentation"),
    PROJET("Projet"),
    TACHE_ADMINISTRATIVE("tache administrative");

    private final String description;

    ActivityType(String description){
        this.description = description;
    }
}
