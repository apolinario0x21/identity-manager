package com.example.jwt_auth.controller;

import com.example.jwt_auth.Repository.UserRepository;
import com.example.jwt_auth.model.User;
import com.example.jwt_auth.service.CustomUserDetailsService;
import com.example.jwt_auth.service.UserService;
import com.example.jwt_auth.util.JwtUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    private CustomUserDetailsService userDetailsService;

/*
    não é necessário se já estiver usando @Autowired na classe
    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
*/

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe!");
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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao autenticar: " + e.getMessage());
            return ResponseEntity.status(401).body("Usuário inexistente ou senha inválida. " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(refreshToken, userDetails)) {
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