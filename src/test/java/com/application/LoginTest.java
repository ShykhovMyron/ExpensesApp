package com.application;

import com.application.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("1").password("1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void emptyPasswordLoginTest() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("name", "stalker"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void emptyNameLoginTest() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("password", "123456"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
