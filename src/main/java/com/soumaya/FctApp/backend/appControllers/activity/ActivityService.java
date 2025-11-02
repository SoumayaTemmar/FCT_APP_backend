package com.soumaya.FctApp.backend.appControllers.activity;


import com.soumaya.FctApp.backend.Activity.Activity;
import com.soumaya.FctApp.backend.Activity.ActivityRepository;
import com.soumaya.FctApp.backend.Activity.ActivityType;
import com.soumaya.FctApp.backend.Exceptions.OperationNotPermittedException;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.Unite.UniteRepository;
import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final UniteRepository uniteRepository;

    // create an activity

    @Transactional
    public StandardResponse addActivity(ActivityRequest request){
        if (activityRepository.existsByImputation(request.imputation())){
            throw new OperationNotPermittedException(
                    "activity with imputation: "+request.imputation() +" already exist");
        }

        return StandardResponse.builder()
                .id(activityRepository.save(activityMapper.toActivity(request)).getId())
                .message("activity added successfully!")
                .build()
        ;
    }

    // get all non deleted activities
    public PageResponse<ActivityResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<Activity> activities = activityRepository.findAllByDeletedFalse(pageable);

        List<ActivityResponse> activityResponses = activities.stream()
                .map(activityMapper::toActivityResponse)
                .toList();

        return new PageResponse<>(
                activityResponses,
                activities.getNumber(),
                activities.getSize(),
                activities.getTotalPages(),
                activities.getTotalElements()
        );
    }
    // get activity by id
    public ActivityResponse getActivityById(int id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Activité avec id: " + id + " n'existe pas"
                ));

        return activityMapper.toActivityResponse(activity);
    }
    // update an existing activity
    @Transactional
    public StandardResponse updateActivity(int id, ActivityRequest request){

        ActivityType type;

        Activity activity = activityRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "activity with id: " + id + "does not exist"
                ));
        if (activity.isDeleted()){
            throw new IllegalStateException(
                    "you cannot update a deleted activity,please check the trash !");
        }
        try{
            type = ActivityType.valueOf(request.activityType().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid type: "+ request.activityType());
        }

        activity.setName(request.name());
        activity.setActivityType(type);
        activity.setDescription(request.description());

        if (!activity.getImputation().equals(request.imputation())){
            if (activityRepository.existsByImputation(request.imputation())){
                throw new OperationNotPermittedException(
                        "an activity with imputation: " + request.imputation()+" already exists");
            }
            activity.setImputation(request.imputation());
        }

        if (Objects.equals(activity.getUnite().getDenominationFr(), request.uniteDenominationFr())){
            Unite unite = uniteRepository.findByDenominationFrAndDeletedFalse(request.uniteDenominationFr())
                    .orElseThrow(()-> new EntityNotFoundException(
                            "unité" + request.uniteDenominationFr()+ " n'existe pas"
                    ));
            activity.setUnite(unite);
        }


        activityRepository.save(activity);

        return StandardResponse.builder()
                .id(id)
                .message("activity was successfully updated!")
                .build();
    }

    // soft delete an activity

    @Transactional
    public StandardResponse deleteActSoft(int id){
        Activity activity = activityRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "activity with id: " + id + "does not exist"
                ));
        if (activity.isDeleted()){
            throw new IllegalStateException(
                    "activity already deleted,please check the trash !");
        }
        activity.setDeleted(true);
        activityRepository.save(activity);

        return StandardResponse.builder()
                .id(id)
                .message("activity was successfully deleted")
                .build();
    }

    // restore activity
    @Transactional
    public StandardResponse restoreAct(int id){

        Activity activity = activityRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "activity with id: " + id + "does not exist"
                ));
        if (!activity.isDeleted()){
            throw new IllegalStateException(
                    "you cannot restore a non deleted activity!");
        }

        activity.setDeleted(false);
        activityRepository.save(activity);

        return StandardResponse.builder()
                .id(id)
                .message("activity was successfully restored")
                .build();
    }
    // delete permanently an act
    @Transactional
    public StandardResponse deleteActPermanently(int id){
        Activity activity = activityRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "activity with id: " + id + "does not exist"
                ));

        activityRepository.delete(activity);
        return StandardResponse.builder()
                .id(id)
                .message("activity was deleted permanently")
                .build();
    }
    // get activities in trash
    public PageResponse<ActivityResponse> getActInTrash(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate"));
        Page<Activity> activities = activityRepository.findAllByDeletedTrue(pageable);

        List<ActivityResponse> activityResponses = activities.stream()
                .map(activityMapper::toActivityResponse)
                .toList();

        return new PageResponse<>(
                activityResponses,
                activities.getNumber(),
                activities.getSize(),
                activities.getTotalPages(),
                activities.getTotalElements()
        );
    }

}
