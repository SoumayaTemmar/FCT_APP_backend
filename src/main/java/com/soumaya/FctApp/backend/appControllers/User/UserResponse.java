package com.soumaya.FctApp.backend.appControllers.User;

import com.soumaya.FctApp.backend.User.GradeType;
import com.soumaya.FctApp.backend.User.Role;
import lombok.*;

import java.util.HashMap;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private HashMap<String, String> unite;
    private String mat;
    private Set<Role> roles;
    private GradeType grade;
}
