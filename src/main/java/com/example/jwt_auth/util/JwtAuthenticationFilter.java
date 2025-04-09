package com.example.jwt_auth.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7).trim(); // Remove "Bearer " prefix
            System.out.println("Token recebido (após remover Bearer): " + jwt);

            try {
                if (!jwt.matches("^[A-Za-z0-9-_\\.]+\\.[A-Za-z0-9-_\\.]+\\.[A-Za-z0-9-_\\.]+$")) {
                    throw new IllegalArgumentException("Token JWT formatado incorretamente" + jwt);
                }

                String username = jwtUtil.getUsernameFromToken(jwt);
                System.out.println("Nome de usuário extraído do token: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    }
                }


            } catch (Exception e) {
                System.out.println("Erro ao processar o token JWT: " + e.getMessage());
            }
        } else {
            if (authHeader == null) {
                System.out.println("Header Authorization não encontrado.");
            } else {
                System.out.println("Header Authorization não começa com Bearer.");
            }
        }


        filterChain.doFilter(request, response);

    }
}

