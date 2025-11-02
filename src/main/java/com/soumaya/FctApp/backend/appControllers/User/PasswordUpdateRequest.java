package com.soumaya.FctApp.backend.appControllers.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
        @NotBlank(message = "old password mast be provided")
        String oldPassword,
        @NotBlank(message = "new password is mandatory")
        @Size(min = 10, message = "password should be at least 10 characters")
        String newPassword
) {}
