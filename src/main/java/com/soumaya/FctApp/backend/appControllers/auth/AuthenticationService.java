package com.soumaya.FctApp.backend.appControllers.auth;

import com.soumaya.FctApp.backend.Exceptions.DuplicateUsernameException;
import com.soumaya.FctApp.backend.User.User;
import com.soumaya.FctApp.backend.User.UserRepository;
import com.soumaya.FctApp.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserRegisterMapper userRegisterMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponse register(RegisterRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateUsernameException(
                    "a user with the same username already exist,or was not deleted permanently!"
            );
        }
        //map the request to user entity
        User user = userRegisterMapper.toUser(request);
        userRepository.save(user);

        return RegisterResponse.builder()
                .id(user.getId())
                .message("user registered successfully")
                .build();

    }

    public LoginResponse login(LoginRequest request) {

        //verify the credentials and that the user is the one he claims to be
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // build the token
        User user = (User) authentication.getPrincipal();
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("fullName", user.getFullName());

        String token = jwtService.generateToken(extraClaims, user);

        //return the generated token to the user
        return LoginResponse.builder()
                .roles(user.getRoles())
                .token(token)
                .build();

    }

}
