package com.application.it;

import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.WalletService;
import com.application.testConfig.TestExpensesConfig;
import com.application.testServices.DefaultTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class WalletServiceIT {
    private User user;

    @Autowired
    private DefaultTestService defaultTestService;
    @Autowired
    private TestExpensesConfig testExpensesConfig;

    @Autowired
    private WalletService walletService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private ExpenseRepo expenseRepo;

    @BeforeEach
    public void beforeEach() {
        expenseRepo.deleteAll();
        userRepo.deleteAll();
        walletRepo.deleteAll();
        user = defaultTestService.createUser("Peter", "9BSYTW8yrv");
    }

    @Test
    public void getWalletTest() {
        //Arrange
        int budget = 100;
        BigDecimal expectedBudget = new BigDecimal(budget + ".00");
        Wallet expected = new Wallet(
                expectedBudget,
                expectedBudget,
                defaultTestService.getDefaultExpenseTypes(),
                user);
        //Act
        Wallet userWallet = walletRepo.getWalletByUserId(user.getId());
        userWallet.setBudget(expectedBudget);
        userWallet.setBalance(expectedBudget);
        walletRepo.save(userWallet);
        Wallet actual = walletService.getWallet(user.getId());
        actual.setId(null);
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void recalculateBalanceWithDefaultParamsTest() {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();

        defaultTestService.createExpense(user, 50, type, testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 120, type, testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 30, type, testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 10, type, testExpensesConfig.getFormatter());
        BigDecimal expected = new BigDecimal("-210.00");
        //Act
        walletService.recalculateBalance(user.getId());
        BigDecimal actual = walletRepo.getWalletByUserId(user.getId()).getBalance();
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void recalculateBalanceWithNewBudgetTest() {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Wallet userWallet = user.getWallet();
        userWallet.setBudget(new BigDecimal("100.00"));
        walletRepo.save(userWallet);

        defaultTestService.createExpense(user, 50, type, testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 30, type, testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 10, type, testExpensesConfig.getFormatter());
        BigDecimal expected = new BigDecimal("10.00");
        //Act
        walletService.recalculateBalance(user.getId());
        BigDecimal actual = walletRepo.getWalletByUserId(user.getId()).getBalance();
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void changeBudgetTest() {
        //Arrange
        String budget = "582";
        BigDecimal expectedBudget = new BigDecimal(budget + ".00");
        Wallet expected = new Wallet(
                expectedBudget,
                expectedBudget,
                defaultTestService.getDefaultExpenseTypes(),
                user);
        //Act
        walletService.changeBudget(user.getId(), expectedBudget);
        Wallet actual = walletRepo.getWalletByUserId(user.getId());
        actual.setId(null);
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void changeBudgetWithExpensesTest() {
        //Arrange
        Wallet wallet = user.getWallet();
        wallet.setBalance(new BigDecimal(-50));
        walletRepo.save(wallet);

        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        defaultTestService.createExpense(user, 50, type, testExpensesConfig.getFormatter());

        BigDecimal expectedBudget = new BigDecimal("210.00");
        Wallet expected = new Wallet(
                expectedBudget,
                expectedBudget.subtract(BigDecimal.valueOf(50)),
                defaultTestService.getDefaultExpenseTypes(),
                user);

        //Act
        walletService.changeBudget(user.getId(), expectedBudget);
        Wallet actual = walletRepo.getWalletByUserId(user.getId());
        actual.setId(null);
        //Assert
        assertEquals(expected, actual);
    }
}
