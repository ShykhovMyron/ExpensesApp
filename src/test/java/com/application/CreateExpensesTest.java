package com.application;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import com.application.repository.PurchaseRepo;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql", "create-expenses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"create-expenses-after.sql", "create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class CreateExpensesTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepo purchaseRepo;


    @Test
    @WithUserDetails("technology")
    public void createPurchaseTest() throws Exception {
        String date = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).format(new Date());
        this.mockMvc.perform(post("/createExpenses")
                        .param("amount", "1000")
                        .param("type", "FOOD")
                        .param("date", date)
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(model().attributeDoesNotExist("lowBudget"))
                .andExpect(model().attributeExists("isPrevEnabled"))
                .andExpect(model().attributeExists("isNextEnabled"))
                .andExpect(model().attributeExists("pageNumbersToHide"))
                .andExpect(model().attributeExists("pageNumbersToShow"))
                .andExpect(model().attributeExists("firstPurchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"));

        Set<Purchase> listPurchases = purchaseRepo.findAllByUserId(5L);

        Assertions.assertEquals(11, purchaseRepo.findAllByUserId(5L).size());


        Purchase purchase = listPurchases.stream().filter(s -> s.getAmount().equals(new BigDecimal("1000.00")) &&
                s.getType().equals("FOOD")).findAny().get();

        Assertions.assertEquals(purchase.getDateAdded()
                , new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).parse(date));
    }

    @Test
    @WithUserDetails("technology")
    public void createPurchaseEmptyAmountTest() throws Exception {
        String date = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).format(new Date());
        this.mockMvc.perform(post("/createExpenses")
                        .param("amount", "")
                        .param("type", "FOOD")
                        .param("date", date)
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorsValid"))
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(model().attributeDoesNotExist("lowBudget"))
                .andExpect(model().attributeExists("isPrevEnabled"))
                .andExpect(model().attributeExists("isNextEnabled"))
                .andExpect(model().attributeExists("pageNumbersToHide"))
                .andExpect(model().attributeExists("pageNumbersToShow"))
                .andExpect(model().attributeExists("firstPurchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"));

        Assertions.assertEquals(10, purchaseRepo.findAllByUserId(5L).size());
    }

    @Test
    @WithUserDetails("technology")
    public void createPurchaseNegativeAmountTest() throws Exception {
        String date = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).format(new Date());
        this.mockMvc.perform(post("/createExpenses")
                        .param("amount", "-1000")
                        .param("type", "FOOD")
                        .param("date", date)
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorsValid"))
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(model().attributeDoesNotExist("lowBudget"))
                .andExpect(model().attributeExists("isPrevEnabled"))
                .andExpect(model().attributeExists("isNextEnabled"))
                .andExpect(model().attributeExists("pageNumbersToHide"))
                .andExpect(model().attributeExists("pageNumbersToShow"))
                .andExpect(model().attributeExists("firstPurchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"));

        Assertions.assertEquals(10, purchaseRepo.findAllByUserId(5L).size());
    }

    @Test
    @WithUserDetails("technology")
    public void createPurchaseEmptyTypeTest() throws Exception {
        String date = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).format(new Date());
        this.mockMvc.perform(post("/createExpenses")
                        .param("amount", "1000")
                        .param("type", "")
                        .param("date", date)
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorsValid"))
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(model().attributeDoesNotExist("lowBudget"))
                .andExpect(model().attributeExists("isPrevEnabled"))
                .andExpect(model().attributeExists("isNextEnabled"))
                .andExpect(model().attributeExists("pageNumbersToHide"))
                .andExpect(model().attributeExists("pageNumbersToShow"))
                .andExpect(model().attributeExists("firstPurchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"));

        Assertions.assertEquals(10, purchaseRepo.findAllByUserId(5L).size());
    }

}
