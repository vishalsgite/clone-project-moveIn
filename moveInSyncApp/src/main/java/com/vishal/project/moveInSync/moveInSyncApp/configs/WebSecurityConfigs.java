package com.vishal.project.moveInSync.moveInSyncApp.configs;

import com.vishal.project.moveInSync.moveInSyncApp.Security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfigs {
    private final JwtAuthFilter jwtAuthFilter;
    private static final String[] PUBLIC_ROUTES = { "/auth/**",  // Existing public routes // Authentication-related endpoints
            "/v3/api-docs/**",          // OpenAPI docs
            "/swagger-ui/**",           // Swagger UI
            "/swagger-ui.html" ,        // Swagger HTML page
            "/auth/login/google",       // Google OAuth login
            "/login/oauth2/**"          // OAuth2 callback
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.sessionManagement(sessionConfig-> sessionConfig
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.disable() )
                .authorizeHttpRequests(
                        auth-> auth.requestMatchers(PUBLIC_ROUTES).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true"));
        return httpSecurity.build();
    }

}
