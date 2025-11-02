package com.soumaya.FctApp.backend.appControllers.periode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PeriodRequest(

        @NotBlank(message = "you have to specify the month")
        String month,
        @NotNull(message = "please add the year")
        int year,
        @NotNull(message = "please specify if the period should be opened or not")
        boolean opened
) {
}
