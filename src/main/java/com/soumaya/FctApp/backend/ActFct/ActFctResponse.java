package com.soumaya.FctApp.backend.ActFct;

import com.soumaya.FctApp.backend.appControllers.activity.ActivityResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActFctResponse {
    private int id;
    private float percentage;
    private ActivityResponse activityResponse;
}
