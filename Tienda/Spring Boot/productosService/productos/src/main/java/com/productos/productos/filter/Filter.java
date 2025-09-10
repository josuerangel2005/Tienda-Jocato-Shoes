package com.productos.productos.filter;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.productos.productos.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        String jwt = null;
        String user = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {

            jwt = authorization.substring(7);
            user = jwtService.extractUsername(jwt);

        }

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authorities = jwtService.extracClaim(x -> ((List<String>) x.get("roles")), jwt).stream().map(rol -> new SimpleGrantedAuthority(rol)).collect(Collectors.toList());

            User userDetails = new User(user, "", authorities);

            if (this.jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }


        }

        filterChain.doFilter(request, response);
    }
}
