package com.example.spvms.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors()
            .and()
            .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                // ✅ PUBLIC
                .requestMatchers(
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()

                // 🔐 REPORTS (ADMIN ONLY)
                .requestMatchers("/reports/**").hasRole("ADMIN")


                // 🔐 VENDORS
                .requestMatchers(HttpMethod.GET, "/api/vendors/**")
                .hasAnyRole("ADMIN", "PROCUREMENT")

                .requestMatchers(HttpMethod.POST, "/api/vendors/**")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/vendors/**")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/vendors/**")
                .hasRole("ADMIN")

                // 🔐 USERS
                .requestMatchers("/api/users/**")
                .hasRole("ADMIN")

                // 🔐 PURCHASE ORDERS
                .requestMatchers("/api/purchase-orders/**")
                .permitAll()

                // 🔐 PURCHASE REQUISITIONS
                
                // Anyone authenticated can view or submit
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/purchase-requisitions/**"
                ).authenticated()

                .requestMatchers(
                HttpMethod.POST,
                "/api/purchase-requisitions/*/submit"
                ).authenticated()

                // Only approvers can approve / reject
                .requestMatchers(
                HttpMethod.POST,
                "/api/purchase-requisitions/*/approve",
                "/api/purchase-requisitions/*/reject"
                ).hasAnyRole("ADMIN", "PROCUREMENT")

                .requestMatchers("/reports/**").hasRole("ADMIN")

                // 🔐 EVERYTHING ELSE
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React URL
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
