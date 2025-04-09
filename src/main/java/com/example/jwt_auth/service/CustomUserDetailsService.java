package com.example.jwt_auth.service;

import com.example.jwt_auth.Repository.UserRepository;
import com.example.jwt_auth.model.CustomUserDetails;
import com.example.jwt_auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        System.out.println("Usuário carregado: " + user.getUsername());
        System.out.println("Senha do usuário: " + user.getPassword());
        System.out.println("Roles atribuídas: " + List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

    }
}
