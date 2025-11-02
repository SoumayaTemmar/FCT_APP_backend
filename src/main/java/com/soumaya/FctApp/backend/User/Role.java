package com.soumaya.FctApp.backend.User;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "total permission"),
    ADMIN_FCT("ROLE_ADMIN_FCT", "fct administration access"),
    RESPONSABLE_FCT("ROLE_RESPONSABLE_FCT","fct administration, less access"),
    USER("ROLE_USER", "basic application access");

    private final String authority;
    private final String description;

    Role(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    public SimpleGrantedAuthority getAsAuthority(){
        return new SimpleGrantedAuthority(authority);
    }
}
