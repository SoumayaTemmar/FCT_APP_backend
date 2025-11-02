package com.soumaya.FctApp.backend.appControllers.User;

import com.soumaya.FctApp.backend.User.User;
import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // to do later -> add an email , so when an admin opens a period an email is sent to all
    // employees having an email set

    // a user can modify his password
    @PatchMapping("/update/password")
    public ResponseEntity<StandardResponse> updatePassword(
            @RequestBody @Valid PasswordUpdateRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(userService.updateUserPassword(request, connectedUser));
    }
    //an admin can get user by specific id

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // an admin can update the user's infos
    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponse> updateUser(
            @PathVariable int id,
            @RequestBody @Valid UpdateUserRequest request
    ){
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    //an admin can delete a user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deleteUser(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> softDeleteUser(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.softDeleteUser(id));
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<StandardResponse> restoreUser(
            @PathVariable int id
    ){
        return ResponseEntity.ok(userService.restoreUser(id));
    }
    // an admin can get all the users
    @GetMapping("/all")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(
                userService.getAllUsers(page, size)
        );
    }

    // get all soft deleted users
    @GetMapping("/trash")
    public ResponseEntity<PageResponse<UserResponse>> getDeletedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(
                userService.getDeletedUsers(page, size)
        );
    }

}
