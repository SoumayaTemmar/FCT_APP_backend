package com.soumaya.FctApp.backend.appControllers.auth;


import com.soumaya.FctApp.backend.User.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Set<Role> roles;
    private String token;
}
