package com.example.jwt_auth.service;

import com.example.jwt_auth.Repository.UserRepository;
import com.example.jwt_auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("Senha original: " + user.getPassword());
        System.out.println("Senha criptografada: " + hashedPassword);

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
