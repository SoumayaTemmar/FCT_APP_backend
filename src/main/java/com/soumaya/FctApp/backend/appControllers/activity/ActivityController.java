package com.soumaya.FctApp.backend.appControllers.activity;

import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/add")
    public ResponseEntity<StandardResponse> addActivity(
            @RequestBody @Valid ActivityRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(activityService.addActivity(request));
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponse<ActivityResponse>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(activityService.getAll(page, size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActById(
            @PathVariable int id
    ){
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponse> updateActivity(
            @PathVariable int id,
            @RequestBody @Valid ActivityRequest request
    ){
        return ResponseEntity.ok(activityService.updateActivity(id, request));
    }

    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> softDeleteAct(
            @PathVariable int id
    ){
        return ResponseEntity.ok(activityService.deleteActSoft(id));
    }

    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restoreAct(
            @PathVariable int id
    ){
        return ResponseEntity.ok(activityService.restoreAct(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deletePermanently(
            @PathVariable int id
    ){
        return ResponseEntity.ok(activityService.deleteActPermanently(id));
    }

    @GetMapping("/trash/all")
    public ResponseEntity<PageResponse<ActivityResponse>> getAllActInTrash(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(activityService.getActInTrash(page, size));
    }

}
