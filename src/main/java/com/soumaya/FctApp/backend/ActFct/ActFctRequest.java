package com.soumaya.FctApp.backend.ActFct;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActFctRequest {
    private float percentage;
    private String actImputation;
}
