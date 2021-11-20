package com.application;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql", "create-expenses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"create-expenses-after.sql", "create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class EditExpensesTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepo purchaseRepo;

    @Test
    @WithUserDetails("technology")
    public void editPurchaseTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/10")
                        .param("amount", "1000")
                        .param("type", "FOOD")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errorsValid"))
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(new BigDecimal("1000.00"),
                purchaseRepo.findById(10L).get().getAmount());
        Assertions.assertEquals("FOOD", purchaseRepo.findById(10L).get().getType());
    }

    @Test
    @WithUserDetails("technology")
    public void editPurchaseEmptyAmountTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/10")
                        .param("amount", "")
                        .param("type", "FOOD")
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(BigDecimal.valueOf(15),
                purchaseRepo.findById(10L).get().getAmount().stripTrailingZeros());
        Assertions.assertEquals("RESTAURANT", purchaseRepo.findById(10L).get().getType());
    }

    @Test
    @WithUserDetails("technology")
    public void editPurchaseNegativeAmountTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/10")
                        .param("amount", "-1000")
                        .param("type", "FOOD")
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(BigDecimal.valueOf(15),
                purchaseRepo.findById(10L).get().getAmount().stripTrailingZeros());
        Assertions.assertEquals("RESTAURANT", purchaseRepo.findById(10L).get().getType());
    }

    @Test
    @WithUserDetails("technology")
    public void editPurchaseEmptyTypeTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/10")
                        .param("amount", "1000")
                        .param("type", "")
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(BigDecimal.valueOf(15),
                purchaseRepo.findById(10L).get().getAmount().stripTrailingZeros());
        Assertions.assertEquals("RESTAURANT", purchaseRepo.findById(10L).get().getType());
    }

    @Test
    @WithUserDetails("technology")
    public void editPurchaseLowBudgetTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/10")
                        .param("amount", "3000")
                        .param("type", "FOOD")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errorsValid"))
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(model().attributeExists("lowBudget"))
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(new BigDecimal("3000.00"),
                purchaseRepo.findById(10L).get().getAmount());
        Assertions.assertEquals("FOOD", purchaseRepo.findById(10L).get().getType());
    }

    @Test
    @WithUserDetails("technology")
    public void editPurchaseNonexistentIdTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/1000")
                        .param("amount", "3000")
                        .param("type", "FOOD")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/expenses")
                        .with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errorsValid"))
                .andExpect(model().attributeExists("errors"))
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(BigDecimal.valueOf(15),
                purchaseRepo.findById(10L).get().getAmount().stripTrailingZeros());
        Assertions.assertEquals("RESTAURANT", purchaseRepo.findById(10L).get().getType());
    }

    @Test
    @WithUserDetails("technology")
    public void editPurchaseNonexistentTypeTest() throws Exception {
        this.mockMvc.perform(get("/editExpenses/10")
                        .param("amount", "3000")
                        .param("type", "asdasdasd")
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

        Assertions.assertNotNull(purchaseRepo.findById(10L));
        Assertions.assertEquals(BigDecimal.valueOf(15),
                purchaseRepo.findById(10L).get().getAmount().stripTrailingZeros());
        Assertions.assertEquals("RESTAURANT", purchaseRepo.findById(10L).get().getType());
    }


}
