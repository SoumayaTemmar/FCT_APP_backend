package com.soumaya.FctApp.backend.appControllers.activity;

import jakarta.validation.constraints.NotBlank;

public record ActivityRequest(
        @NotBlank(message = "you have to provide a name")
        String name,
        @NotBlank(message = "imputation is mandatory!")
        String imputation,
        @NotBlank(message = "please add a brief description")
        String description,
        @NotBlank(message = "activity type is mandatory")
        String activityType,
        @NotBlank(message = "unite is mandatory")
        String uniteDenominationFr
) {
}
