package com.soumaya.FctApp.backend.appControllers.activity;

import com.soumaya.FctApp.backend.Activity.ActivityType;
import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {

    private int id;
    private String name;
    private String imputation;
    private String description;
    private ActivityType activityType;
    private HashMap<String, String> unite;
}
