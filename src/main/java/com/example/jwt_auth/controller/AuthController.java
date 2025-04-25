package com.example.jwt_auth.controller;

import com.example.jwt_auth.dto.RefreshTokenDTO;
import com.example.jwt_auth.model.User;
import com.example.jwt_auth.service.UserService;
import com.example.jwt_auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Validated @RequestBody User user) {

        if (userService.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Erro: nome do usuário já está em uso.");
        }

        userService.saveUser(user);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        try {
            System.out.println("Tentando autenticar: " + user.getUsername());
            System.out.println("Senha fornecida: " + user.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            System.out.println("Usuário autenticado com sucesso: " + authentication.getName());

            String token = jwtUtil.generateToken(authentication.getName());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {

            throw new AuthenticationException("Usuário ou senha inválidos.") {
            };
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenDTO refreshToken) {

        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken.getRefreshToken());

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(refreshToken.getRefreshToken(), userDetails)) {
                String newAcessToken = jwtUtil.generateToken(username);
                return ResponseEntity.ok(newAcessToken);
            } else {
                return ResponseEntity.status(401).body("Refresh token inválido.");
            }
        } catch (Exception error) {
            return ResponseEntity.status(401).body("Erro ao atualizar o token: " + error.getMessage());
        }
    }
}
