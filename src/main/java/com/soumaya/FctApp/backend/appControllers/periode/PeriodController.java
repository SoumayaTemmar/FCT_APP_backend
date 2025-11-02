package com.soumaya.FctApp.backend.appControllers.periode;

import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/period")
@RequiredArgsConstructor
public class PeriodController {
    private final PeriodService periodService;

    @PostMapping("/add")
    public ResponseEntity<StandardResponse> addPeriod(
            @RequestBody @Valid PeriodRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(periodService.addPeriod(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodResponse> getPeriodById(
            @PathVariable int id
    ){
        return ResponseEntity.ok(periodService.getPeriodById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponse<PeriodResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(periodService.getAllPeriods(page, size));
    }
    @GetMapping("/all/opened")
    public ResponseEntity<PageResponse<PeriodResponse>> getOpenedPeriods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(periodService.getOpenedPeriods(page,size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponse> updatePeriod(
            @PathVariable int id,
            @RequestBody @Valid PeriodRequest request
    ){
       return ResponseEntity.ok(periodService.updatePeriod(id, request));

    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<StandardResponse> closePeriod(
            @PathVariable int id
    ){
        return ResponseEntity.ok(periodService.closePeriod(id));
    }

    @PatchMapping("/open/{id}")
    public ResponseEntity<StandardResponse> openPeriod(
            @PathVariable int id
    ){
        return ResponseEntity.ok(periodService.openPeriod(id));
    }

    //update, delete, get trash

    @GetMapping("/trash/all")
    public ResponseEntity<PageResponse<PeriodResponse>> getAllTrash(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(periodService.getDeletedPeriods(page, size));
    }

    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> softDelete(
            @PathVariable int id
    ){
        return ResponseEntity.ok(periodService.softDelete(id));
    }

    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restorePeriod(
            @PathVariable int id
    ){
        return ResponseEntity.ok(periodService.restorePeriod(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deletePeriod(
            @PathVariable int id
    ){
        return ResponseEntity.ok(periodService.deletePeriod(id));
    }



}
