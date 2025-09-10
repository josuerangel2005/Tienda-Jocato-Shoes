package com.productos.productos.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String SECRET_KEY = "appSpringBoot3appSpringBoot3appSpringBoot3!";
    private final Long EXPIRATION_TIME = 1000 * 60 * 60L;

    private Key getSignKey() {
        byte[] keyBytes = this.SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSignKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public <T> T extracClaim(Function<Claims, T> resolve, String jwt) {
        Claims claims = this.extractAllClaims(jwt);
        return resolve.apply(claims);
    }

    public String extractUsername(String jwt) {
        return this.extracClaim(x -> x.getSubject(), jwt);
    }

    public Date extractExpiration(String jwt) {
        return this.extracClaim(x -> x.getExpiration(), jwt);
    }

    public boolean isTokenExpired(String jwt) {
        return this.extractExpiration(jwt).before(new Date());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.EXPIRATION_TIME))
                .signWith(this.getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails){
        return !this.isTokenExpired(jwt) && userDetails.getUsername().equals(this.extractUsername(jwt));
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();

        var roles = userDetails.getAuthorities().stream().map(x -> x.toString()).toList();
        claims.put("roles",roles);

        return this.createToken(claims,userDetails.getUsername());
    }



}
