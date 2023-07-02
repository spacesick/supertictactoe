package id.ac.ui.cs.supertictactoe.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.UserProfileDTO;
import id.ac.ui.cs.supertictactoe.service.GameService;
import id.ac.ui.cs.supertictactoe.service.UserService;

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class ProfileControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private GameService gameService;

    private User mockedAppUser1;

    private User mockedAppUser2;

    private User mockedAppUser3;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        mockedAppUser1 = new User();
        mockedAppUser1.setUserId("u0");
        mockedAppUser2 = new User();
        mockedAppUser2.setUserId("u1");
    }

    @Test
    void whenGetUserProfilePageShouldReturnProfilePage() throws Exception {
        mockMvc.perform(get("/profile")
                .with(user(mockedAppUser1)))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", mockedAppUser1))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/profile")
                .with(user(mockedAppUser2)))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", mockedAppUser2))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetUserProfileEditorPageAndAuthorizedShouldReturnProfileEditorPage() throws Exception {
        mockMvc.perform(get("/profile/edit")
                .with(user(mockedAppUser1)))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", mockedAppUser1))
                .andExpect(view().name("edit_profile"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetUserProfileEditorPageAndNotAuthorizedShouldRedirect() throws Exception {
        mockMvc.perform(get("/profile/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void whenUpdateUserProfileAndAuthorizedShouldUpdateUser() throws Exception {
        mockedAppUser1.setFirstName("Jack");
        mockedAppUser2.setFirstName("Stefanus");

        mockedAppUser3 = new User();
        mockedAppUser3.setUserId(mockedAppUser1.getUserId());
        mockedAppUser3.setFirstName(mockedAppUser2.getFirstName());

        mockMvc.perform(post("/profile/edit", mockedAppUser1.getUserId())
                .with(user(mockedAppUser1)))
                .andExpect(view().name("redirect:"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void whenUpdateUserProfileAndNotAuthorizedShouldRedirect() throws Exception {
        mockedAppUser1.setFirstName("Jack");
        mockedAppUser2.setFirstName("Stefanus");

        mockedAppUser3 = new User();
        mockedAppUser3.setUserId(mockedAppUser1.getUserId());
        mockedAppUser3.setFirstName(mockedAppUser2.getFirstName());

        UserProfileDTO mockedAppUser3Profile = new UserProfileDTO(mockedAppUser3.getFirstName(), mockedAppUser3.getLastName());

        when(userService.updateUser(mockedAppUser2, mockedAppUser3Profile)).thenReturn(mockedAppUser3);

        mockMvc.perform(post("/profile/edit", mockedAppUser2.getUserId())
                .with(user(mockedAppUser1)))
                .andExpect(view().name("redirect:"))
                .andExpect(status().is3xxRedirection());

        verify(userService, times(0)).updateUser(mockedAppUser2, mockedAppUser3Profile);
    }
}
