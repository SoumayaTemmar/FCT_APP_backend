package com.soumaya.FctApp.backend.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StandardResponse {
    private int id;
    private String message;
}
