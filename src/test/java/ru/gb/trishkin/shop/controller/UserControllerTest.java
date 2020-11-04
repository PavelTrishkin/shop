package ru.gb.trishkin.shop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.gb.trishkin.shop.domain.Role;
import ru.gb.trishkin.shop.domain.User;
import ru.gb.trishkin.shop.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser = User.builder().name("Bob")
            .id(5L)
            .role(Role.CLIENT).build();

    @BeforeEach
    void setUp(){
        Mockito.when(userService.findByName(Mockito.eq(testUser.getName())))
                .thenReturn(testUser);
    }

    @Test
    void getRolesNotAuthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/Bob/roles")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is5xxServerError());
    }

    @WithMockUser("Bob")
    @Test
    void getRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/Bob/roles")
        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("CLIENT"));
    }

    @WithMockUser("testUser")
    @Test
    void getRoleWrongName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/Bob/roles")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is5xxServerError());
    }

    @WithMockUser(value = "Bob", authorities = "CLIENT")
    @Test
    void createNewUserWrongRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/new")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is5xxServerError());
    }

    //Зачем фигурные скобки для authorities, ведь работает и без них
    @WithMockUser(value = "admin", authorities = {"ADMIN"})
    @Test
    void createNewByAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/new")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}
