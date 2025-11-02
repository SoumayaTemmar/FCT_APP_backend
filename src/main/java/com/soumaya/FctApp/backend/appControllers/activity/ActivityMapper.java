package com.soumaya.FctApp.backend.appControllers.activity;

import com.soumaya.FctApp.backend.Activity.Activity;
import com.soumaya.FctApp.backend.Activity.ActivityType;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.Unite.UniteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ActivityMapper {

    private final UniteRepository uniteRepository;
    public ActivityResponse toActivityResponse(Activity activity){

        HashMap<String, String> uniteH = new HashMap<>();
        uniteH.put("imputation", activity.getUnite().getImputation());
        uniteH.put("DenominationFr", activity.getUnite().getDenominationFr());

        return ActivityResponse.builder()
                .id(activity.getId())
                .name(activity.getName())
                .imputation(activity.getImputation())
                .unite(uniteH)
                .activityType(activity.getActivityType())
                .description(activity.getDescription())
                .build();
    }

    public Activity toActivity(ActivityRequest request){
        ActivityType type;
        try{
            type = ActivityType.valueOf(request.activityType().toUpperCase());
        }catch (IllegalArgumentException e){

            throw new IllegalArgumentException("invalid type: "+ request.activityType());
        }

        Unite unite = uniteRepository.findByDenominationFrAndDeletedFalse(request.uniteDenominationFr())
                .orElseThrow(()-> new EntityNotFoundException("unité" + request.uniteDenominationFr()+ " n'existe pas"));

        return Activity.builder()
                .name(request.name())
                .imputation(request.imputation())
                .activityType(type)
                .unite(unite)
                .description(request.description())
                .deleted(false)
                .build();
    }

}
