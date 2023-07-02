package id.ac.ui.cs.supertictactoe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;


@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class LoginControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private User userApp;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userApp.setUsername("username");
        userApp.setEmail("email");
        userApp.setFirstName("name");
        userApp.setLastName("last");
        userApp.setPassword("password");
    }

    @Test
    void register() throws Exception {
        when(userService.createUser()).thenReturn(userApp);
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("register"))
                .andExpect(model().attribute("user", userApp))
                .andExpect(view().name("register"));
    }

    @Test
    void loginWithError() throws Exception {
        mockMvc.perform(get("/login")
                        .param("error", ""))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("login"))
                .andExpect(model().attribute("error", "Your username and password is invalid."))
                .andExpect(view().name("login"));
    }

    @Test
    void loginWithLogout() throws Exception {
        mockMvc.perform(get("/login", "")
                    .param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("login"))
                .andExpect(model().attribute("message", "You have been logged out successfully."))
                .andExpect(view().name("login"));
    }

    @Test
    void loginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("login"))
                .andExpect(view().name("login"));
    }

    @Test
    void logoutSuccess() throws Exception {
        mockMvc.perform(get("/logout")
                        .with(user(userApp)))
                .andExpect(handler().methodName("logout"))
                .andExpect(redirectedUrl("/login?logout"));
    }
}