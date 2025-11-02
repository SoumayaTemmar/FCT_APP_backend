package com.soumaya.FctApp.backend.appControllers.fct;

import com.soumaya.FctApp.backend.ActFct.ActFctResponse;
import com.soumaya.FctApp.backend.User.User;
import com.soumaya.FctApp.backend.appControllers.activity.ActivityResponse;
import com.soumaya.FctApp.backend.appControllers.periode.PeriodResponse;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FctResponse {

    private int id;
    private String ownerUsername;
    private String mat;
    private List<ActFctResponse> actFctResponseList;
    private String periodMonthYear;
    private HashMap<String, String> unite;

}
