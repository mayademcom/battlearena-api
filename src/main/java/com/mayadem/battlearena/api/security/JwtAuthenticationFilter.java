package com.mayadem.battlearena.api.security; 

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.service.JwtService;
import com.mayadem.battlearena.api.service.WarriorService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final WarriorService warriorService;

    public JwtAuthenticationFilter(JwtService jwtService, WarriorService warriorService) {
        this.jwtService = jwtService;
        this.warriorService = warriorService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

     

        

        
        final String authHeader = request.getHeader("Authorization");

        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            Warrior warrior = this.warriorService.loadUserByUsername(username);

            
            if (jwtService.isTokenValid(jwt, warrior)) {
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        warrior,
                        null, 
                        warrior.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}