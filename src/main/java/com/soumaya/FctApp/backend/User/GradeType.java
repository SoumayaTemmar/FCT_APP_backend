package com.soumaya.FctApp.backend.User;

import lombok.Getter;

@Getter
public enum GradeType {
    RESPONSABLE("responsable"),
    NON_RESPONSABLE("non responsable");

    private final String description;

    GradeType(String description) {
        this.description = description;
    }

}
