package com.soumaya.FctApp.backend.appControllers.fct;

import com.soumaya.FctApp.backend.ActFct.ActFctRequest;
import jakarta.validation.constraints.*;

import java.util.List;

public record FctRequest(

        @NotNull(message = "you need to provide activities")
        @Size(min = 1, message = "at least one activity must be provided")
        List<ActFctRequest> actFctRequests,

        @NotBlank(message = "you have to specify the period")
        String monthYear
) {
}
