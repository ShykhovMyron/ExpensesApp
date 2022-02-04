package com.application.it;

import com.application.exeptions.UserAlreadyExistException;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.UserService;
import com.application.testConfig.TestExpensesConfig;
import com.application.testServices.DefaultTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceIT {
    @Autowired
    private DefaultTestService defaultTestService;
    @Autowired
    private TestExpensesConfig testExpensesConfig;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseRepo expenseRepo;

    @BeforeEach
    public void beforeEach() {
        expenseRepo.deleteAll();
        userRepo.deleteAll();
        walletRepo.deleteAll();
    }

    @Test
    public void createUserTest() throws UserAlreadyExistException {
        // Arrange
        String username = "Steven";
        String password = "LH9t6rj26Y";
        User expected = new User(
                username,
                password,
                new Wallet(defaultTestService.getDefaultExpenseTypes()));
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
        User user = defaultTestService.createUser("Thomas", "C67cZh7e8G");
        defaultTestService.createExpense(user, 100, "BOOKS", testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 2, "BOOKS", testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 400, "FLOWERS", testExpensesConfig.getFormatter());
        defaultTestService.createExpense(user, 10000, "PRODUCTS", testExpensesConfig.getFormatter());
        //Act
        userService.deleteExpenses(user.getId());
        //Assert
        assertTrue(expenseRepo.findAllByUserId(user.getId()).isEmpty());
    }
}
