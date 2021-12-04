package com.application.it;

import com.application.exeptions.UserAlreadyExistException;
import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.Expense;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.UserService;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceIT {
    private static DateFormat formatter;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseTypeRepo expenseTypeRepo;
    @Autowired
    private ExpenseRepo expenseRepo;

    @BeforeAll
    public static void beforeAll() {
        formatter = new SimpleDateFormat("yyyy-M-d");
    }

    @BeforeEach
    public void beforeEach() {
        expenseRepo.deleteAll();
        userRepo.deleteAll();
        walletRepo.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({"Steven,LH9t6rj26Y",
            "Thomas,C67cZh7e8G",
            "Hopkins,a4FE2Sgp75"})
    public void createUserTest(String username, String password) throws UserAlreadyExistException {
        // Arrange
        User expected = new User(username, password, new Wallet(getDefaultExpenseTypes()));
        //Act
        userService.createUser(username, password);
        User actual = userRepo.findByUsername(username);
        actual.setId(null);
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void createUserAlreadyExistTest() throws UserAlreadyExistException {
        //Arrange
        String username = "Thomas";
        String password = "C67cZh7e8G";
        //Act
        userService.createUser(username, password);
        //Assert
        assertThrows(UserAlreadyExistException.class,
                () -> userService.createUser(username, password));
    }

    @Test
    public void deleteExpensesTest() {
        //Arrange
        User user = createUser("Thomas", "C67cZh7e8G");
        createExpense(user, 100, "BOOKS");
        createExpense(user, 2, "BOOKS");
        createExpense(user, 400, "FLOWERS");
        createExpense(user, 10000, "PRODUCTS");
        //Act
        userService.deleteExpenses(user.getId());
        //Assert
        assertTrue(expenseRepo.findAllByUserId(user.getId()).isEmpty());
    }

    private User createUser(String username, String password) {
        Set<ExpenseType> defaultExpenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            defaultExpenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        return userRepo.save(new User(username, password, userWallet));
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

    private Set<ExpenseType> getDefaultExpenseTypes() {
        Set<ExpenseType> expenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            expenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }

        return expenseTypes;
    }
}
