package com.example.spvms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthFilter jwtAuthFilter;

    // constructor injection (BEST PRACTICE)
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Swagger + Auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/api/auth/**",
                    "/auth/login", "/api/vendors/**", "/api/purchase-requisitions/**", "/api/purchase-orders/**"
                ).permitAll()

                // Vendor APIs
                .requestMatchers("/api/vendors/**")
                .hasAnyRole("ADMIN", "PROCUREMENT")

                // Purchase Orders
                .requestMatchers("/api/purchase-orders/**")
                .hasAnyRole("ADMIN", "FINANCE")

                // Purchase Requisitions
                .requestMatchers("/api/purchase-requisitions/**")
                .hasAnyRole("ADMIN", "FINANCE")

                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAuthFilter,   // ✅ OBJECT, not class
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
