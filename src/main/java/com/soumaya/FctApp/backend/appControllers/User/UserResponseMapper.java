package com.soumaya.FctApp.backend.appControllers.User;

import com.soumaya.FctApp.backend.User.User;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;

@Service
public class UserResponseMapper {

    public UserResponse toUserResponse(User user){
        HashMap<String, String> uniteRes = new HashMap<>();
        uniteRes.put("Imputation", user.getUnite().getImputation());
        uniteRes.put("DenominationFr", user.getUnite().getDenominationFr());

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .mat(user.getMat())
                .unite(uniteRes)
                .roles(user.getRoles())
                .grade(user.getGrade())
                .build();
    }
}
