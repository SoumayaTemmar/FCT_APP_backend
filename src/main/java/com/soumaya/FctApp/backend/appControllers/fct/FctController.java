package com.soumaya.FctApp.backend.appControllers.fct;

import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fct")
@RequiredArgsConstructor
public class FctController {

    private final FctService fctService;

    // add a new fct
    @PostMapping("/add")
    public ResponseEntity<FctCreatedResponse> addFct(
            @RequestBody @Valid FctRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        fctService.addFct(request, connectedUser)
                );
    }
    // update an Fct
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateFct(
            @PathVariable int id,
            @RequestBody @Valid FctRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(
                fctService.updateFct(id, request, connectedUser)
        );

    }
    // soft delete
    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> deleteFctSoft(
            @PathVariable int id,
            Authentication connectedUSer
    ){
        return ResponseEntity.ok(fctService.softDeleteFct(id, connectedUSer));
    }
    // restore deleted fct
    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restoreFct(
            @PathVariable int id,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(fctService.restoreFct(id, connectedUser));
    }
    // delete fct permanently
    @DeleteMapping("/trash/delete/{id}")
    public ResponseEntity<StandardResponse> deleteFct(
            @PathVariable int id,
            Authentication connectedUSer
    ){
        return ResponseEntity.ok(fctService.deleteFct(id, connectedUSer));
    }
    // get deleted fct
    @GetMapping("/trash")
    public ResponseEntity<PageResponse<FctResponse>> getDeletedFct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(
                fctService.getDeletedFcts(page, size, connectedUser)
        );
    }
    // get non deleted FCTs of the connected user
    @GetMapping("/myFct")
    public ResponseEntity<PageResponse<FctResponse>> getFctUSer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(fctService.getFctOfUser(page, size, connectedUser));
    }
    // get fct by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getFctById(
            @PathVariable int id,
            Authentication connectedUser
    ){
        return  ResponseEntity.ok(fctService.getFctById(id, connectedUser));
    }

    //------->  admin only  <-------\

    // get all the FCTs
    @GetMapping("/all")
    public ResponseEntity<PageResponse<FctResponse>> getAllFCTs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(
                fctService.getAllFcts(page, size)
        );

    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getFctByIdAdmin(
            @PathVariable int id
    ){
        return  ResponseEntity.ok(fctService.getFctByIdAdmin(id));
    }

    // delete any fct
    @PatchMapping("/delete/soft/admin/{fct_id}")
    public ResponseEntity<StandardResponse> softDeleteByAdmin(
            @PathVariable int fct_id
    ){
        return ResponseEntity.ok(fctService.softDeleteByAdmin(fct_id));
    }
    // admin can restore any soft deleted fct
    @PatchMapping("/trash/restore/admin/{fct_id}")
    public ResponseEntity<StandardResponse> restoreDeletedFctByAdmin(
            @PathVariable int fct_id
    ){
        return ResponseEntity.ok( fctService.restoreFctByAdmin(fct_id));
    }
    // admin can get all deleted FCTs
    @GetMapping("/trash/admin")
    public ResponseEntity<PageResponse<FctResponse>> getDeletedFctAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(fctService.getAllSoftDeletedFctAdmin(page, size));
    }
    // admin can delete any fct permanently
    @DeleteMapping("/trash/delete/admin/{fct_id}")
    public ResponseEntity<StandardResponse> deleteFctPermanentlyAdmin(
            @PathVariable int fct_id
    ){
        return ResponseEntity.ok(fctService.deleteFctPermanentlyAdmin(fct_id));
    }

}
