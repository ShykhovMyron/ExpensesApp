package com.application.app;

import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
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

    @Autowired
    private WalletRepo walletRepo;

    @Test
    @WithUserDetails("stalker")
    public void homePageUser1Test() throws Exception {
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attribute("username", "stalker"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeDoesNotExist("errors"));
        Assertions.assertEquals(new BigDecimal("0.00"),
                walletRepo.getById(userRepo.findByUsername("stalker").getId()).getBudget());

    }

    @Test
    @WithUserDetails("phantom")
    public void homePageUser2Test() throws Exception {
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attribute("username", "phantom"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeDoesNotExist("errors"));
        Assertions.assertEquals(new BigDecimal("1000.00"),
                walletRepo.getById(userRepo.findByUsername("phantom").getId()).getBudget());

    }

    @Test
    @WithUserDetails("stalker")
    public void changeUser1BudgetTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("budget", "100")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attribute("username", "stalker"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeDoesNotExist("errors"));

        Assertions.assertEquals(new BigDecimal("100.00"),
                walletRepo.getById(userRepo.findByUsername("stalker").getId()).getBudget());

    }

    @Test
    @WithUserDetails("phantom")
    public void changeUser2BudgetTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("budget", "900")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attribute("username", "phantom"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeDoesNotExist("errors"));

        Assertions.assertEquals(new BigDecimal("900.00"),
                walletRepo.getById(userRepo.findByUsername("phantom").getId()).getBudget());


    }

    @Test
    @WithUserDetails("stalker")
    public void changeUserBudgetToEmptyTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("budget", (String) null)
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attribute("username", "stalker"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeExists("errors"));

        Assertions.assertEquals(new BigDecimal("0.00"),
                walletRepo.getById(userRepo.findByUsername("stalker").getId()).getBudget());

    }

    @Test
    @WithUserDetails("stalker")
    public void changeUserBudgetToNegativeTest() throws Exception {
        this.mockMvc.perform(post("/changeBudget")
                        .param("budget", "-100")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/home"));
        this.mockMvc.perform(get(("/home")))
                .andExpect(authenticated())
                .andExpect(model().attribute("username", "stalker"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeExists("errors"));

        Assertions.assertEquals(new BigDecimal("0.00"),
                walletRepo.getById(userRepo.findByUsername("stalker").getId()).getBudget());

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
                .andExpect(model().attribute("username", "phantom"))
                .andExpect(model().attributeExists("userWallet"))
                .andExpect(model().attributeDoesNotExist("errors"));

        Assertions.assertEquals(new BigDecimal("1000.00"),
                walletRepo.getById(userRepo.findByUsername("phantom").getId()).getBudget());
    }
}
