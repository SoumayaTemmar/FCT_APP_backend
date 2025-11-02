package com.soumaya.FctApp.backend.security;

import com.soumaya.FctApp.backend.Exceptions.CustomAccessDeniedHAndler;
import com.soumaya.FctApp.backend.Exceptions.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {


    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHAndler customAccessDeniedHAndler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( req ->
                        req.requestMatchers("api/v1/auth/login",
                                        "api/v1/unite/all",
                                        "api/v1/period/all/opened",
                                        "api/v1/activity/all",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                        ).permitAll()
                        .requestMatchers("api/v1/auth/register",
                                "api/v1/users/update/*",
                                "api/v1/users/delete/*",
                                "api/v1/users/all",
                                "api/v1/users/*").hasRole("ADMIN")
                        .requestMatchers("api/v1/fct/all",
                                "api/v1/fct/delete/soft/admin/*",
                                "api/v1/fct/trash/restore/admin/*",
                                "api/v1/fct/trash/admin",
                                "api/v1/fct/trash/delete/admin/*",
                                "api/v1/fct/admin/*"
                                ).hasAnyRole("ADMIN","ADMIN_FCT","RESPONSABLE_FCT")
                        .requestMatchers("api/v1/unite/*",
                                "api/v1/unite/add",
                                "api/v1/unite/delete/soft/*",
                                "api/v1/unite/trash/restore/*",
                                "api/v1/unite/trash/all",
                                "api/v1/unite/delete/*",
                                "api/v1/unite/update/*"
                        ).hasRole("ADMIN")
                        .requestMatchers("api/v1/activity/add",
                                "api/v1/activity/*",
                                "api/v1/activity/update/*",
                                "api/v1/activity/delete/soft/*",
                                "api/v1/activity/trash/restore/*",
                                "api/v1/activity/delete/*",
                                "api/v1/activity/trash/all"
                                ).hasAnyRole("ADMIN","ADMIN_FCT")

                        .requestMatchers("api/v1/period/add",
                                "api/v1/period/*",
                                "api/v1/period/all",
                                "api/v1/period/update/*",
                                "api/v1/period/close/*",
                                "api/v1/period/open/*",
                                "api/v1/period/trash/all",
                                "api/v1/period/delete/soft/*",
                                "api/v1/period/trash/restore/*",
                                "api/v1/period/delete/*"

                        ).hasAnyRole("ADMIN","ADMIN_FCT")
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(ex ->
                        ex.accessDeniedHandler(customAccessDeniedHAndler)
                          .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
