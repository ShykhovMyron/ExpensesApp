package com.application.it;

import com.application.exeptions.UserAlreadyExistException;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.UserService;
import com.application.testServices.DefaultTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserServiceIT {
    @Autowired
    private DefaultTestService defaultTestService;

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
    void createUserTest() throws UserAlreadyExistException {
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
        actual.setPassword(password);
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void createUserAlreadyExistTest() throws UserAlreadyExistException {
        //Arrange
        String username = "Thomas";
        String password = "C67cZh7e8G";
        //Act
        userService.createUser(username, password);
        //Assert
        assertThrows(UserAlreadyExistException.class,
                () -> userService.createUser(username, password));
    }
}
