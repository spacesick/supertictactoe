package id.ac.ui.cs.supertictactoe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.UserProfileDTO;
import id.ac.ui.cs.supertictactoe.repository.UserAuthorityRepository;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    @Spy
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAuthorityRepository userAuthorityRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private User userMock;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("id");
        user.setPassword("password");
    }

    @Test
    void getUserByIdCallRepository() {
        userService.getUserById("id");
        verify(userRepository).getReferenceById("id");
    }

    @Test
    void updateUserCallRepository() {
        userService.updateUser(user, new UserProfileDTO());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserUpdateFirstName() {
        var newUser = new UserProfileDTO();
        newUser.setFirstName("name");
        userService.updateUser(user, newUser);
        assertEquals("name", user.getFirstName());
    }

    @Test
    void updateUserUpdateLastName() {
        var newUser = new UserProfileDTO();
        newUser.setLastName("name");
        userService.updateUser(user, newUser);
        assertEquals("name", user.getLastName());
    }

    @Test
    void createUserIsRightType() {
        var created = userService.createUser();
        assertInstanceOf(User.class, created);
    }

    @Test
    void saveSetPasswordAsBcrypt() {
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encoded");
        userService.save(userMock);
        verify(userMock).setPassword("encoded");
    }

    @Test
    void saveSetDetails() {
        userService.save(userMock);
        verify(userMock).setEnabled(anyBoolean());
        verify(userMock).setCredentialsNonExpired(anyBoolean());
        verify(userMock).setAccountNonExpired(anyBoolean());
        verify(userMock).setAccountNonLocked(anyBoolean());
    }

    @Test
    void saveSetGrantedAuthority() {
        userService.save(userMock);
        verify(userMock).setAuthorities(any());
        verify(userAuthorityRepository).save(any());
    }

    @Test
    void saveCallRepository() {
        userService.save(user);
        verify(userRepository).save(user);
    }

    @Test
    void getUserListCallRepository() {
        userService.getUserList();
        verify(userRepository).findAll();
    }

    @Test
    void findByUsernameCallRepository() {
        userService.findByUsername("username");
        verify(userRepository).findByUsername("username");
    }

}