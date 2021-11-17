package com.application;

import com.application.entity.Type;
import com.application.repository.PurchasesRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

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
    private PurchasesRepo purchasesRepo;

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
                .andExpect(model().attributeExists("purchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"))
                .andExpect(model().attributeExists("decimalFormat"));

        Assertions.assertNotNull(purchasesRepo.findById(10));
        Assertions.assertNotNull(purchasesRepo.findById(10));
        Assertions.assertEquals(1000, purchasesRepo.findById(10).get().getAmount());
        Assertions.assertEquals(Type.FOOD, purchasesRepo.findById(10).get().getType());
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
                .andExpect(model().attributeExists("purchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"))
                .andExpect(model().attributeExists("decimalFormat"));

        Assertions.assertNotNull(purchasesRepo.findById(10));
        Assertions.assertEquals(15, purchasesRepo.findById(10).get().getAmount());
        Assertions.assertEquals(Type.RESTAURANT, purchasesRepo.findById(10).get().getType());
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
                .andExpect(model().attributeExists("purchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"))
                .andExpect(model().attributeExists("decimalFormat"));

        Assertions.assertNotNull(purchasesRepo.findById(10));
        Assertions.assertEquals(15, purchasesRepo.findById(10).get().getAmount());
        Assertions.assertEquals(Type.RESTAURANT, purchasesRepo.findById(10).get().getType());
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
                .andExpect(model().attributeExists("purchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"))
                .andExpect(model().attributeExists("decimalFormat"));

        Assertions.assertNotNull(purchasesRepo.findById(10));
        Assertions.assertEquals(15, purchasesRepo.findById(10).get().getAmount());
        Assertions.assertEquals(Type.RESTAURANT, purchasesRepo.findById(10).get().getType());
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
                .andExpect(model().attributeExists("purchaseNumberOnPage"))
                .andExpect(model().attributeExists("purchases"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("dateFormat"))
                .andExpect(model().attributeExists("todayDate"))
                .andExpect(model().attributeExists("inputModalFormat"))
                .andExpect(model().attributeExists("decimalFormat"));

        Assertions.assertNotNull(purchasesRepo.findById(10));
        Assertions.assertEquals(3000, purchasesRepo.findById(10).get().getAmount());
        Assertions.assertEquals(Type.FOOD, purchasesRepo.findById(10).get().getType());
    }
    //nonexistent id,nonexistent type,not this user


}