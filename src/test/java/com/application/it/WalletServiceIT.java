package com.application.it;

import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.Expense;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.WalletService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class WalletServiceIT {
    private static DateFormat formatter;
    private User user;

    @Autowired
    private WalletService walletService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private ExpenseTypeRepo expenseTypeRepo;

    @BeforeAll
    public static void beforeAll() {
        formatter = new SimpleDateFormat("yyyy-M-d");
    }

    @BeforeEach
    public void beforeEach() {
        expenseRepo.deleteAll();
        userRepo.deleteAll();
        walletRepo.deleteAll();
        user = createUser("Peter", "9BSYTW8yrv");
    }

    @ParameterizedTest
    @CsvSource({
            "100",
            "20",
            "999"})
    public void getWalletTest(int budget) {
        //Arrange
        BigDecimal expectedBudget = new BigDecimal(budget + ".00");
        Wallet expected = new Wallet(
                expectedBudget,
                expectedBudget,
                getDefaultExpenseTypes(),
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
    public void recalculateBalanceTest1() {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();

        createExpense(user,50,type);
        createExpense(user,120,type);
        createExpense(user,30,type);
        createExpense(user,10,type);
        BigDecimal expected = new BigDecimal("-210.00");
        //Act
        walletService.recalculateBalance(user.getId());
        BigDecimal actual = walletRepo.getWalletByUserId(user.getId()).getBalance();
        //Assert
        assertEquals(expected,actual);
    }

    @Test
    public void recalculateBalanceTest2() {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Wallet userWallet = user.getWallet();
        userWallet.setBudget(new BigDecimal("100.00"));
        walletRepo.save(userWallet);

        createExpense(user,50,type);
        createExpense(user,30,type);
        createExpense(user,10,type);
        BigDecimal expected = new BigDecimal("10.00");
        //Act
        walletService.recalculateBalance(user.getId());
        BigDecimal actual = walletRepo.getWalletByUserId(user.getId()).getBalance();
        //Assert
        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @CsvSource({
            "400",
            "100",
            "582"})
    public void changeBudgetTest(String budget) {
        //Arrange
        BigDecimal expectedBudget = new BigDecimal(budget + ".00");
        Wallet expected = new Wallet(
                expectedBudget,
                expectedBudget,
                getDefaultExpenseTypes(),
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
        createExpense(user, 50, type);

        BigDecimal expectedBudget = new BigDecimal("210.00");
        Wallet expected = new Wallet(
                expectedBudget,
                expectedBudget.subtract(BigDecimal.valueOf(50)),
                getDefaultExpenseTypes(),
                user);

        //Act
        walletService.changeBudget(user.getId(), expectedBudget);
        Wallet actual = walletRepo.getWalletByUserId(user.getId());
        actual.setId(null);
        //Assert
        assertEquals(expected, actual);
    }

    private Expense createExpense(User user, Integer amount, String type) {
        Expense expense = new Expense();
        expense.setUser(user);
        try {
            expense.setDateAdded(formatter.parse(formatter.format(new Date())));
        } catch (ParseException ignored) {
        }
        expense.setAmount(new BigDecimal(amount + ".00"));
        expense.setType(expenseTypeRepo.findByType(type));
        expenseRepo.save(expense);
        return expense;
    }

    private User createUser(String username, String password) {
        Set<ExpenseType> defaultExpenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            defaultExpenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        return userRepo.save(new User(username, password, userWallet));
    }

    private Set<ExpenseType> getDefaultExpenseTypes() {
        Set<ExpenseType> expenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            expenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }

        return expenseTypes;
    }
}
