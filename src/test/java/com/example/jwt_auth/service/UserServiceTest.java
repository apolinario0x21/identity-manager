package com.example.jwt_auth.service;

import com.example.jwt_auth.Repository.UserRepository;
import com.example.jwt_auth.dto.UserDTO;
import com.example.jwt_auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() { // inicializa os mocks - @Mock e @InjectMocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_shouldReturnPaginatedUsers(){
        List<User> users = Arrays.asList(
                new User(1L, "Luffy", "password1", "luffy@example.com"),
                new User(2L, "Ace", "password2", "ace@example.com")
        );

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> pageUsers = new PageImpl<>(users, pageRequest, users.size());

        when(userRepository.findAll(pageRequest)).thenReturn(pageUsers);

        Page<UserDTO> result = userService.getAllUsers(pageRequest);

        assertEquals(2, result.getTotalElements());
        assertEquals("Luffy", result.getContent().get(0).getUsername());

        verify(userRepository, times(1)).findAll(pageRequest);

        System.out.println("Total de usuários: " + result.getTotalElements());
        System.out.println("First user: " + result.getContent().get(0).getUsername());
    }
}
