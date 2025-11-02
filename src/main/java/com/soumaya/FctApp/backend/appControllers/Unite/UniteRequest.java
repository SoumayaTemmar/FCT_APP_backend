package com.soumaya.FctApp.backend.appControllers.Unite;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UniteRequest {

    @NotBlank(message = "you have to provide the unite code")
    private String code;
    @NotBlank(message = "you have to provide the imputation")
    private String imputation;
    @NotBlank(message = "please add a french denomination")
    private String denominationFr;
}
