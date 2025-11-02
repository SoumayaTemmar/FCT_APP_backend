package com.soumaya.FctApp.backend.appControllers.Unite;

import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/unite")
@RequiredArgsConstructor
public class UniteController {
    private final UniteService uniteService;

    //get all unities
    @GetMapping("/all")
    public ResponseEntity<PageResponse<UniteResponse>> getAllUnities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(uniteService.getAllUnities(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniteResponse> getUniteById(
            @PathVariable int id
    ){
        return ResponseEntity.ok(uniteService.getUniteById(id));
    }
    // create unite
    @PostMapping("/add")
    public ResponseEntity<StandardResponse> addUnite(
            @RequestBody @Valid UniteRequest request
    ){
        return ResponseEntity.ok(uniteService.addUnite(request));
    }

    //delete unite --soft
    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> deleteSoft(
            @PathVariable int id
    ){
        return ResponseEntity.ok(uniteService.softDeleteUnite(id));
    }

    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restoreUnite(
            @PathVariable int id
    ){
        return ResponseEntity.ok(uniteService.restoreUnite(id));
    }

    @GetMapping("/trash/all")
    public ResponseEntity<PageResponse<UniteResponse>> getTrash(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(uniteService.getTrash(page, size));
    }

    //delete and update
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deleteUnite(
            @PathVariable int id
    ){
        return ResponseEntity.ok(uniteService.deleteUnite(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponse> updateUnite(
            @PathVariable int id,
            @RequestBody UniteRequest request
    ){
        return ResponseEntity.ok(uniteService.updateUnite(id, request));
    }


}
