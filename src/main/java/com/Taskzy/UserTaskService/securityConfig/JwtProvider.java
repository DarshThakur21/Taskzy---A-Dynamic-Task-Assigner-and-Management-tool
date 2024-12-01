package com.Taskzy.UserTaskService.securityConfig;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {
    static SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public static String generateToken(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();

        String roles=populateAuthorities(authorities);

        String jwt= Jwts.builder().
                setIssuedAt(new Date()).
                setExpiration(new Date(new Date().getTime()+8640000)).claim("email", authentication.getName())
                .claim("authorities",roles)
                .signWith(key).compact();
        return jwt;
    }


    public static String getEmailFromJwtToken(String jwt){
        jwt=jwt.substring(7);
        Claims claims=Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        String email=String.valueOf(claims.get("email"));
        return email;
    }







    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths=new HashSet<>();

        for(GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);

    }



}
