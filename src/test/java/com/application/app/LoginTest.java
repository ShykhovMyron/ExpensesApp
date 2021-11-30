package com.application.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
            "stalker,123456",
            "phantom,654321"
    })
    public void correctLoginTest(String username, String password) throws Exception {
        this.mockMvc.perform(formLogin().user(username).password(password))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void emptyPasswordLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("stalker"))
                .andDo(print())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void emptyNameLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().password("123456"))
                .andDo(print())
                .andExpect(redirectedUrl("/login?error"));
    }
}
