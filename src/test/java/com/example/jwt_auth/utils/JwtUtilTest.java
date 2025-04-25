package com.example.jwt_auth.utils;

import com.example.jwt_auth.model.CustomUserDetails;
import com.example.jwt_auth.util.JwtUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    // este método é chamado antes de cada teste
    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.secret = "5T8hpBGUKIu1foBEpdN6+XJF/JJvX0sXq9a7vmFxHhQ=";
        jwtUtil.expirationTime = 1000 * 60 * 60; // 1 hora
        System.out.println("JWT Util initialized with secret: " + jwtUtil.secret + " and expiration time: " + jwtUtil.expirationTime);
    }

    @Test
    void generateToken_shouldGenerateValidToken() {
        CustomUserDetails userDetails = new CustomUserDetails(
                "testUser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtUtil.generateToken(userDetails.getUsername());
        System.out.println("\nGenerated token: " + token);

        // O token deve ter 3 partes (header, payload e signature)
        String[] parts = token.split("\\.");
        assertTrue(parts.length == 3, "Token should have 3 parts");

        System.out.println("\nToken parts: " + parts.length);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("testUser");
        System.out.println("\nGenerated token: " + token);

        assertTrue(jwtUtil.isTokenValid(token, new CustomUserDetails(
                "testUser",
                "password",
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER"))
        )));

        System.out.println("\nToken is valid");
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() throws InterruptedException {
        jwtUtil.expirationTime = 1000; // 1 segundo

        String token = jwtUtil.generateToken("testUser");
        System.out.println("\nGenerated token com expiração de 1 segundo: " + token);

        Thread.sleep(2000); // Espera 2 segundos para garantir que o token expirou

        assertFalse(jwtUtil.isTokenValid(token, new CustomUserDetails(
                "testUser",
                "password",
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER"))
        )));

        System.out.println("\nToken is expired");
    }

    @Test
    void getUsernameFromToken_shouldThrowExceptionForMalformedToken() {
        // Simula um token malformado
        String malformedToken = "malformed.token.string";

        System.out.println("Tentando obter nome de user a partir do token malformado: " + malformedToken);

        assertThrows(MalformedJwtException.class, () -> {
            jwtUtil.getUsernameFromToken(malformedToken);
        });

        System.out.println("\nExceção MalformedJwtException lançada - Token malformado lançado com sucesso");
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidToken() {
        String validToken = jwtUtil.generateToken("testUser");

        String invalidToken = validToken + "invalid"; // Adiciona caracteres inválidos ao token

        System.out.println("\nGenerated token: " + validToken);
        System.out.println("\nInvalid token: " + invalidToken);

        assertFalse(jwtUtil.isTokenValid((invalidToken), new CustomUserDetails(
                "testUser",
                "password",
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER"))
        )));

        System.out.println("\nToken is invalid foi corretamente invalidado");
    }

}
