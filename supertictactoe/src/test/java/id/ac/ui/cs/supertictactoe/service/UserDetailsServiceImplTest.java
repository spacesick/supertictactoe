package id.ac.ui.cs.supertictactoe.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;
import id.ac.ui.cs.supertictactoe.service.UserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameWhenNotExist() {
        when(userRepository.findByUsername("username")).thenReturn(null);
        var exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("username"));
        assertEquals("Username not found!", exception.getMessage());
    }

    @Test
    void loadUserByUsernameWhenExist() {
        var user = new User();
        when(userRepository.findByUsername("username")).thenReturn(user);
        var result = userDetailsService.loadUserByUsername("username");
        assertEquals(user, result);
    }
}