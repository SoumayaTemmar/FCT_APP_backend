package com.soumaya.FctApp.backend.appControllers.auth;


import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.Unite.UniteRepository;
import com.soumaya.FctApp.backend.User.GradeType;
import com.soumaya.FctApp.backend.User.Role;
import com.soumaya.FctApp.backend.User.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRegisterMapper {
    private final PasswordEncoder passwordEncoder;
    private final UniteRepository uniteRepository;

    public User toUser(RegisterRequest request) {

        final Set<Role> roles = request.getRoles().stream()
                .map(String::toUpperCase)
                .map(r -> {
                    try{
                       return Role.valueOf(r);
                    }catch (IllegalArgumentException e){
                        throw new IllegalArgumentException("invalid role: " + r);
                    }
                })
                .collect(Collectors.toSet());

        final String pws = passwordEncoder.encode(request.getPassword());

       Unite unite = uniteRepository.findByDenominationFrAndDeletedFalse(request.getUniteDenominationFr())
               .orElseThrow(()-> new EntityNotFoundException(
                       "unite provided: "+request.getUniteDenominationFr()+" does not exist"
               ));

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mat(request.getMat())
                .password(pws)
                .roles(roles)
                .unite(unite)
                .grade(GradeType.valueOf(request.getGrade().toUpperCase()))
                .username(request.getUsername())
                .accountLocked(false)
                .enabled(true)
                .build();
    }
}
