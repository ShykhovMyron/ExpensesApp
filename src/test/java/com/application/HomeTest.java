package com.application;

import com.application.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql", "create-expenses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"create-expenses-after.sql", "create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class HomeTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Test
    @WithUserDetails("stalker")
    public void homePageUser1Test() throws Exception {
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("balance", (double) 0))
                .andExpect(model().attributeDoesNotExist("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("stalker").getWallet().getBudget(),
//                BigDecimal.valueOf(0));
    }

    @Test
    @WithUserDetails("phantom")
    public void homePageUser2Test() throws Exception {
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("balance", BigDecimal.valueOf(335)))
                .andExpect(model().attributeDoesNotExist("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("phantom").getWallet().getBudget(),
//                BigDecimal.valueOf(1000));
    }

    @Test
    @WithUserDetails("stalker")
    public void changeUser1BudgetTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("username", "ignore")
                        .param("password", "ignore")
                        .param("budget", "100")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attributeExists("user"))
//                .andExpect(model().attribute("balance", userRepo
//                        .findByUsername("stalker").getWallet().getBudget()))
                .andExpect(model().attributeDoesNotExist("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("stalker").getWallet().getBudget(),
//                BigDecimal.valueOf(100));
    }

    @Test
    @WithUserDetails("phantom")
    public void changeUser2BudgetTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("username", "ignore")
                        .param("password", "ignore")
                        .param("budget", "900")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("balance", BigDecimal.valueOf(235)))
                .andExpect(model().attributeDoesNotExist("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("phantom").getWallet().getBudget(),
//                BigDecimal.valueOf(900));

    }

    @Test
    @WithUserDetails("stalker")
    public void changeUserBudgetToEmptyTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("username", "ignore")
                        .param("password", "ignore")
                        .param("budget", (String) null)
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attributeExists("user"))
//                .andExpect(model().attribute("balance", userRepo
//                        .findByUsername("stalker").getWallet().getBudget()))
                .andExpect(model().attributeExists("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("stalker").getWallet().getBudget(),
//                BigDecimal.valueOf(0));
    }

    @Test
    @WithUserDetails("stalker")
    public void changeUserBudgetToNegativeTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("username", "ignore")
                        .param("password", "ignore")
                        .param("budget", "-100")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attributeExists("user"))
//                .andExpect(model().attribute("balance", userRepo
//                        .findByUsername("stalker").getWallet().getBudget()))
                .andExpect(model().attributeExists("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("stalker").getWallet().getBudget(),
//                BigDecimal.valueOf(0));
    }

    @Test
    @WithUserDetails("phantom")
    public void deleteExpensesTest() throws Exception {
        this.mockMvc.perform(post("/deleteExpenses")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("balance", BigDecimal.valueOf(10000)))
                .andExpect(model().attributeDoesNotExist("errors"));

//        Assertions.assertEquals(userRepo.findByUsername("phantom").getWallet().getBudget(),
//                BigDecimal.valueOf(1000));
    }
}
