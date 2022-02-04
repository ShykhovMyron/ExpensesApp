package com.application.it;

import com.application.exeptions.ExpenseNotFoundException;
import com.application.exeptions.TypeNotFoundException;
import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.Expense;
import com.application.model.entity.User;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.ExpensesService;
import com.application.testConfig.TestExpensesConfig;
import com.application.testServices.DefaultTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.application.model.entity.DefaultExpenseTypes.BOOKS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class ExpensesServiceIT {
    private User user;

    @Autowired
    private DefaultTestService defaultTestService;
    @Autowired
    private TestExpensesConfig testExpensesConfig;

    @Autowired
    private ExpensesService expensesService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private ExpenseTypeRepo expenseTypeRepo;

    @BeforeEach
    public void beforeEach() {
        expenseRepo.deleteAll();
        userRepo.deleteAll();
        walletRepo.deleteAll();
        user = defaultTestService.createUser("Peter", "9BSYTW8yrv");
    }

    @Test
    public void getExpenseTest() throws ExpenseNotFoundException {
        // Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Expense expected = defaultTestService.createExpense(
                user,
                100,
                type,
                testExpensesConfig.getFormatter());
        // Act
        Expense actual = expensesService.getExpense(expected.getId());
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void getExpenseNonexistentIdTest() {
        // Assert
        assertThrows(ExpenseNotFoundException.class,
                () -> expensesService.getExpense(0L));
    }

    @Test
    public void editExpenseTest() throws TypeNotFoundException, ExpenseNotFoundException, ParseException {
        // Arrange
        Date date = new Date();
        BigDecimal amount = new BigDecimal("5006.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Expense expected = defaultTestService.createExpense(
                user,
                100,
                "BOOKS",
                testExpensesConfig.getFormatter());
        expected.setAmount(amount);
        expected.setType(expenseTypeRepo.findByType(type));
        expected.setDateAdded(date);
        // Act
        expensesService.editExpense(
                user.getId(),
                expected.getId(),
                amount,
                type,
                testExpensesConfig.getFormatter().format(date));
        Expense actual = expenseRepo.findById(expected.getId()).get();
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void editExpenseNonexistentExpenseIdTest() {
        // Arrange
        Date date = new Date();
        BigDecimal amount = new BigDecimal("200.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        // Assert

        assertThrows(ExpenseNotFoundException.class,
                () -> expensesService.editExpense(
                        user.getId(),
                        0L,
                        amount,
                        type,
                        testExpensesConfig.getFormatter().format(date)));
    }

    @Test
    public void editExpenseNonexistentTypeTest() {
        // Arrange
        Date date = new Date();
        BigDecimal amount = new BigDecimal("200.00");
        String type = "SOMETHING";
        Long expenseId = defaultTestService.createExpense(
                user,
                100,
                BOOKS.name(),
                testExpensesConfig.getFormatter()).getId();
        // Assert
        assertThrows(TypeNotFoundException.class,
                () -> expensesService.editExpense(
                        user.getId(),
                        expenseId,
                        amount,
                        type,
                        testExpensesConfig.getFormatter().format(date)));
    }

    @Test
    public void editExpenseInvalidDateTest() {
        // Arrange
        Date date = new Date();
        BigDecimal amount = new BigDecimal("200.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Long expenseId = defaultTestService.createExpense(
                user,
                100,
                type,
                testExpensesConfig.getFormatter()).getId();
        // Assert
        assertThrows(ParseException.class,
                () -> expensesService.editExpense(
                        user.getId(),
                        expenseId,
                        amount,
                        type,
                        new SimpleDateFormat("").format(date)));
    }


    @Test
    public void createExpenseTest() throws TypeNotFoundException, ParseException {
        // Arrange
        Date date = testExpensesConfig.getFormatter()
                .parse(testExpensesConfig.getFormatter().format(new Date()));
        BigDecimal amount = new BigDecimal("450.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Expense expected = new Expense(date, expenseTypeRepo.findByType(type), amount, user);
        //Act
        expensesService.createExpense(
                user.getId(),
                amount,
                type,
                testExpensesConfig.getFormatter().format(date));
        Expense actual = expenseRepo.findAllByUserId(user.getId()).stream().findAny().get();
        actual.setId(null);
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void createExpenseNonexistentTypeTest() throws ParseException {
        // Arrange
        Date date = testExpensesConfig.getFormatter()
                .parse(testExpensesConfig.getFormatter().format(new Date()));
        BigDecimal amount = new BigDecimal("200.00");
        String type = "SOMETHING";
        // Assert
        assertThrows(TypeNotFoundException.class,
                () -> expensesService.createExpense(
                        user.getId(),
                        amount,
                        type,
                        testExpensesConfig.getFormatter().format(date)));
    }

    @Test
    public void createExpenseInvalidDateTest() throws ParseException {
        // Arrange
        Date date = testExpensesConfig.getFormatter()
                .parse(testExpensesConfig.getFormatter().format(new Date()));
        BigDecimal amount = new BigDecimal("100.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        // Assert
        assertThrows(ParseException.class,
                () -> expensesService.createExpense(
                        user.getId(),
                        amount,
                        type,
                        new SimpleDateFormat("").format(date)));
    }

    @Test
    public void deleteExpenseTest() throws ExpenseNotFoundException {
        // Arrange
        Long expenseId = defaultTestService.createExpense(
                user,
                5002,
                "FLOWERS",
                testExpensesConfig.getFormatter()).getId();
        //Act
        expensesService.deleteExpense(expenseId);
        Optional<Expense> expense = expenseRepo.findById(expenseId);
        // Assert
        assertTrue(expense.isEmpty());

    }

    @Test
    public void deleteExpenseNonexistentIdTest() {
        // Assert
        assertThrows(ExpenseNotFoundException.class,
                () -> expensesService.deleteExpense(0L));

    }

    @Test
    public void deleteExpensesTest() {
        //Arrange
        User user = defaultTestService.createUser("Thomas", "C67cZh7e8G");
        defaultTestService.createExpense(user, 100, "BOOKS", testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 2, "BOOKS", testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 400, "FLOWERS", testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 10000, "PRODUCTS", testExpensesConfig.getFormatter());
        //Act
        expensesService.deleteExpenses(user.getId());
        //Assert
        assertTrue(expenseRepo.findAllByUserId(user.getId()).isEmpty());
    }
}
