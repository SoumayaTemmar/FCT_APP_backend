package com.soumaya.FctApp.backend.appControllers.Unite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UniteResponse {
    private int id;
    private String code;
    private String imputation;
    private String denominationFr;
}
