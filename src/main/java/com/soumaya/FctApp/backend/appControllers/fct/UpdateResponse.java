package com.soumaya.FctApp.backend.appControllers.fct;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateResponse {
    private int id;
    private String message;
}
