package com.soumaya.FctApp.backend.appControllers.User;

import com.soumaya.FctApp.backend.Exceptions.DuplicateUsernameException;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.Unite.UniteRepository;
import com.soumaya.FctApp.backend.User.GradeType;
import com.soumaya.FctApp.backend.User.Role;
import com.soumaya.FctApp.backend.User.User;
import com.soumaya.FctApp.backend.User.UserRepository;
import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UniteRepository uniteRepository;

    @Transactional
    public StandardResponse updateUserPassword(
            PasswordUpdateRequest request,
            Authentication connectedUser
    ){
        User user = (User)connectedUser.getPrincipal();
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new IllegalArgumentException("old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        return StandardResponse.builder()
                .id(userRepository.save(user).getId())
                .message("password was successfully updated").build();
    }

    //update a hole user

    @Transactional
    public StandardResponse updateUser(int id, UpdateUserRequest request){
        GradeType grade;

        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "user with id: "+ id + " does not exist"));

        // if the username is changed we should make sure that it's not used by an existing user
        if (!user.getUsername().equals(request.username()) &&
                userRepository.existsByUsername(request.username())
        ){
            throw new DuplicateUsernameException(
                    "the username provided already exist, please chose another one"
            );
        }

        //  validate provided roles
        var roles = request.roles().stream()
                        .map(String::toUpperCase)
                        .map(r -> {
                            try{
                                return Role.valueOf(r);
                            }catch (IllegalArgumentException e){
                                throw new IllegalArgumentException("invalid role: " + r);
                            }
                        })
                        .collect(Collectors.toSet());

        // validate the provided grade
        try{
            grade = GradeType.valueOf(request.grade().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid grade: " + request.grade());
        }
        // validate the unite
        Unite unite = uniteRepository.findByDenominationFrAndDeletedFalse(request.uniteDenominationFr())
                        .orElseThrow(()-> new EntityNotFoundException(
                                "unite "+ request.uniteDenominationFr()+ " does not exist"
                        ));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setMat(request.mat());
        user.setRoles(roles);
        user.setUnite(unite);
        user.setGrade(grade);
        user.setUsername(request.username());

        userRepository.save(user);
        return StandardResponse.builder()
                .id(id)
                .message("user was successfully updated").build();
    }

    // delete a user

    @Transactional
    public StandardResponse deleteUser(int id){
        if (!userRepository.existsById(id)){
            throw  new EntityNotFoundException("user with id: "+ id + "does not exist");
        }
        userRepository.deleteById(id);
        return StandardResponse.builder()
                .id(id)
                .message("user was successfully deleted").build();
    }

    // get all users
    public PageResponse<UserResponse> getAllUsers(int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<User> users = userRepository.findAllByDeletedFalse(pageable);

        List<UserResponse> userResponses = users.stream()
                .map(userResponseMapper::toUserResponse)
                .toList();

        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalPages(),
                users.getTotalElements()
        );
    }

    @Transactional
    public StandardResponse softDeleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "user with id: " + id + " not found"
                ));

        if (user.isDeleted()){
            throw new IllegalStateException("user already deleted,please check the trash");
        }
        user.getFct().forEach(fct -> fct.setDeleted(true));
        user.setDeleted(true);

        userRepository.save(user);
        return StandardResponse.builder()
                .id(id)
                .message("user deleted").build();
    }

    @Transactional
    public StandardResponse restoreUser(int id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "user with id: " + id + " not found"
                ));

        user.getFct().forEach(fct -> fct.setDeleted(false));
        user.setDeleted(false);
        userRepository.save(user);
        return StandardResponse.builder()
                .id(id)
                .message("user restored").build();
    }

    public PageResponse<UserResponse> getDeletedUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size,Sort.by("lastModifiedDate"));
        Page<User> users = userRepository.findAllByDeletedTrue(pageable);

        List<UserResponse> userResponses = users.stream()
                .map(userResponseMapper::toUserResponse)
                .toList();

        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalPages(),
                users.getTotalElements()
        );
    }
    // get user by id
    public UserResponse getUserById(int id) {

        User user = userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("user with id: "+ id + " not found!")
        );

        return userResponseMapper.toUserResponse(user);
    }
}
