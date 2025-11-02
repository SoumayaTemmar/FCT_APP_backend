package com.soumaya.FctApp.backend.appControllers.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(
        @NotBlank(message = "firstName is mandatory")
        String firstName,

        @NotBlank(message = "lastName is mandatory")
        String lastName,

        @NotBlank(message = "mat is mandatory")
        String mat,

        @NotNull(message = "roles should not be null")
        @Size(min = 1, message = "at least one role mast be provided")
        Set<@NotBlank(message = "Role cannot be blank") String> roles,

        @NotBlank(message = "grade is mandatory")
        String grade,

        @NotBlank(message = "unite is mandatory")
        String uniteDenominationFr,

        @NotBlank(message = "username is mandatory")
        String username
) {
}
