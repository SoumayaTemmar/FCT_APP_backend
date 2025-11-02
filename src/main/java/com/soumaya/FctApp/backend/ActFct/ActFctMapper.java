package com.soumaya.FctApp.backend.ActFct;

import com.soumaya.FctApp.backend.Activity.Activity;
import com.soumaya.FctApp.backend.Activity.ActivityRepository;
import com.soumaya.FctApp.backend.appControllers.activity.ActivityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActFctMapper {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActFct toActFct(ActFctRequest request){

        Activity activity = activityRepository
                .findByImputationAndDeletedFalse(request.getActImputation())
                .orElseThrow(()-> new EntityNotFoundException(
                        "activity: "+request.getActImputation()+" does not exist"));

        return ActFct.builder()
                .percentage(request.getPercentage())
                .activity(activity).build();
    }

    public ActFctResponse toActFctResponse(ActFct actFct){
        return ActFctResponse.builder()
                .id(actFct.getId())
                .percentage(actFct.getPercentage())
                .activityResponse(activityMapper.toActivityResponse(actFct.getActivity()))
                .build();
    }
}
