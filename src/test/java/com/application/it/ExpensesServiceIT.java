package com.application.it;

import com.application.exeptions.ExpenseNotFoundException;
import com.application.exeptions.TypeNotFoundException;
import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.Expense;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.ExpensesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class ExpensesServiceIT {
    private static Pageable pageable;
    private static DateFormat formatter;
    private User user;

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

    @BeforeAll
    public static void beforeAll() {
        pageable = PageRequest.of(0, 10);
        formatter = new SimpleDateFormat("yyyy-M-d");
    }

    @BeforeEach
    public void beforeEach() {
        expenseRepo.deleteAll();
        userRepo.deleteAll();
        walletRepo.deleteAll();
        user = createUser("Peter", "9BSYTW8yrv");
    }

    @Test
    public void getExpensesTest() {
        // Arrange
        List<Expense> expected = List.of(
                createExpense(user, 100, "BOOKS"),
                createExpense(user, 2, "BOOKS"),
                createExpense(user, 400, "FLOWERS"),
                createExpense(user, 10000, "PRODUCTS"));
        // Act
        List<Expense> actual = expensesService.getExpenses(user.getId(), pageable)
                .stream()
                .collect(Collectors.toList());
        // Assert
        assertSame(expected.get(0), actual.get(0));
    }

    @Test
    public void getExpenseTest() throws ExpenseNotFoundException {
        // Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Expense expected = createExpense(user, 100, type);
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
        Expense expected = createExpense(user, 100, "BOOKS");
        expected.setAmount(amount);
        expected.setType(expenseTypeRepo.findByType(type));
        expected.setDateAdded(date);
        // Act
        expensesService.editExpense(
                user.getId(),
                expected.getId(),
                amount,
                type,
                formatter.format(date));
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
                        formatter.format(date)));
    }

    @Test
    public void editExpenseNonexistentTypeTest() {
        // Arrange
        Date date = new Date();
        BigDecimal amount = new BigDecimal("200.00");
        String type = "SOMETHING";
        Long expenseId = createExpense(user, 100, "BOOKS").getId();
        // Assert
        assertThrows(TypeNotFoundException.class,
                () -> expensesService.editExpense(
                        user.getId(),
                        expenseId,
                        amount,
                        type,
                        formatter.format(date)));
    }

    @Test
    public void editExpenseInvalidDateTest() {
        // Arrange
        Date date = new Date();
        BigDecimal amount = new BigDecimal("200.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Long expenseId = createExpense(user, 100, type).getId();
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
        Date date = formatter.parse(formatter.format(new Date()));
        BigDecimal amount = new BigDecimal("450.00");
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        Expense expected = new Expense(date, expenseTypeRepo.findByType(type), amount, user);
        //Act
        expensesService.createExpense(user.getId(), amount, type, formatter.format(date));
        Expense actual = expenseRepo.findAllByUserId(user.getId()).stream().findAny().get();
        actual.setId(null);
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void createExpenseNonexistentTypeTest() throws ParseException {
        // Arrange
        Date date = formatter.parse(formatter.format(new Date()));
        BigDecimal amount = new BigDecimal("200.00");
        String type = "SOMETHING";
        // Assert
        assertThrows(TypeNotFoundException.class,
                () -> expensesService.createExpense(
                        user.getId(),
                        amount,
                        type,
                        formatter.format(date)));

    }

    @Test
    public void createExpenseInvalidDateTest() throws ParseException {
        // Arrange
        Date date = formatter.parse(formatter.format(new Date()));
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
    public void deleteExpenseTest() throws ParseException, ExpenseNotFoundException {
        // Arrange
        Long expenseId = createExpense(user, 5002, "FLOWERS").getId();
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

    //TODO  должен ли проверять что их ТОЧНО нет у юзера(покупок) если получил эксепшн
}
