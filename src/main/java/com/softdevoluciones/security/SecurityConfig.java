package com.softdevoluciones.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

       http
    .csrf(csrf -> csrf.disable())
    .cors(cors -> {})

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // ==========================================
                // AUTENTICACIÓN PÚBLICA
                // ==========================================
             .requestMatchers(
    "/api/auth/**",
    "/uploads/**",
    "/error"
).permitAll()
                // ==========================================
                // ADMINISTRACIÓN DE DEVOLUCIONES
                // ADMIN + OPERADOR
                // ==========================================
                .requestMatchers(
                    "/api/admin/devoluciones/**"
                ).hasAnyAuthority(
                    "ROLE_ADMIN",
                    "ROLE_OPERADOR"
                )

                // ==========================================
                // ADMINISTRACIÓN GENERAL
                // ADMIN + OPERADOR
                // ==========================================
                .requestMatchers(
                    "/api/admin/**"
                ).hasAnyAuthority(
                    "ROLE_ADMIN",
                    "ROLE_OPERADOR"
                )

                // ==========================================
                // COMPRAS
                // CLIENTE + ADMIN
                //
                // @PreAuthorize controla cada endpoint.
                // ==========================================
                .requestMatchers(
                    "/api/compras/**"
                ).hasAnyAuthority(
                    "ROLE_CLIENTE",
                    "ROLE_ADMIN"
                )

                // ==========================================
                // DEVOLUCIONES DE CLIENTES
                // CLIENTE + ADMIN
                //
                // @PreAuthorize controla cada endpoint.
                // ==========================================
                .requestMatchers(
                    "/api/devoluciones/**"
                ).hasAnyAuthority(
                    "ROLE_CLIENTE",
                    "ROLE_ADMIN"
                )

                // ==========================================
                // PRODUCTOS
                // CLIENTE + OPERADOR + ADMIN
                // ==========================================
                .requestMatchers(
                    "/api/productos/**"
                ).hasAnyAuthority(
                    "ROLE_CLIENTE",
                    "ROLE_OPERADOR",
                    "ROLE_ADMIN"
                )

                // ==========================================
                // CUALQUIER OTRO ENDPOINT
                // ==========================================
                .anyRequest()
                .authenticated()
            )

            .authenticationProvider(
                authenticationProvider()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}