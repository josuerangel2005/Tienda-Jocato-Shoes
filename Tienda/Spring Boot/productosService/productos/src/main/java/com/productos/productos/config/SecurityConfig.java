package com.productos.productos.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.productos.productos.filter.Filter;
import com.productos.productos.service.JwtService;

@Configuration
public class SecurityConfig {
    @Autowired
    @Lazy
    Filter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET, "/v4/productos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v4/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v4/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v4/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/ordenes").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/orden/pdf").hasAnyRole("ADMIN","USER")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

