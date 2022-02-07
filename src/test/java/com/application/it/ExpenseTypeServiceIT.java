package com.application.it;

import com.application.exeptions.TypeAlreadyExistException;
import com.application.exeptions.TypeNotFoundException;
import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.service.ExpenseTypeService;
import com.application.testServices.DefaultTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class ExpenseTypeServiceIT {
    private User user;

    @Autowired
    private DefaultTestService defaultTestService;

    @Autowired
    private ExpenseTypeService expenseTypeService;
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
    void getDefaultExpenseTypesTest() {
        // Arrange
        Set<String> expected = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            expected.add(type.toString());
        }
        //Act
        Set<String> actual = expenseTypeService.getDefaultExpenseTypes()
                .stream().map(ExpenseType::getType).collect(Collectors.toSet());
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void createExpenseTypeTest() throws TypeAlreadyExistException {
        //Arrange
        String type = "AAAA";
        //Act
        expenseTypeService.createExpenseType(user.getId(), type);
        ExpenseType actual = expenseTypeRepo.findByType(type);
        Set<String> typesName = walletRepo
                .getWalletByUserId(user.getId())
                .getTypes()
                .stream()
                .map(ExpenseType::getType)
                .collect(Collectors.toSet());
        //Assert

        assertNotNull(actual);
        assertTrue(typesName.contains(type));
    }

    @Test
    void createExpenseTypeAlreadyExistTest() {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        //Assert
        assertThrows(TypeAlreadyExistException.class,
                () -> expenseTypeService.createExpenseType(user.getId(), type));
    }

    @Test
    void hasExpenseTypeTestTrue() {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        //Act
        boolean actual = expenseTypeService.hasExpenseType(user.getId(), type);
        //Assert
        assertTrue(actual);
    }

    @Test
    void hasExpenseTypeTestFalse() {
        //Arrange
        String type = "AAAA";
        //Act
        boolean actual = expenseTypeService.hasExpenseType(user.getId(), type);
        //Assert
        assertFalse(actual);
    }

    @Test
    void deleteExpenseTypeTest() throws TypeNotFoundException {
        //Arrange
        String type = Arrays.stream(DefaultExpenseTypes.values()).findAny().get().toString();
        //Act
        expenseTypeService.deleteExpenseType(user.getId(), type);
        //Assert
        assertTrue(walletRepo.getWalletByUserId(user.getId()).getTypes()
                .stream()
                .map(ExpenseType::getType)
                .filter(s -> s.equals(type))
                .findFirst().isEmpty());
    }

    @Test
    void deleteExpenseTypeAlreadyExistTest() {
        //Arrange
        String type = "AAAA";
        //Assert
        assertThrows(TypeNotFoundException.class,
                () -> expenseTypeService.deleteExpenseType(user.getId(), type));
    }
}
