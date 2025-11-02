package com.soumaya.FctApp.backend.appControllers.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @NotBlank(message = "mat is mandatory")
    private String mat;

    @NotBlank(message = "unite is mandatory")
    private String uniteDenominationFr;

    @NotNull(message = "roles should not be null")
    @Size(min = 1, message = "at least one role mast be provided")
    private Set<@NotBlank(message = "Role cannot be blank") String> roles;

    @NotBlank(message = "grade is mandatory")
    private String grade;

    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "username is mandatory")
    @Size(min = 10, message = "password should be at least 10 characters")
    private String password;
}
