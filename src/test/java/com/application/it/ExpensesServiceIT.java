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
import com.application.service.ExpensesService;
import liquibase.pro.packaged.D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class ExpensesServiceIT {
    private static org.springframework.data.domain.Pageable pageable;

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
    }

    @BeforeEach
    public void beforeEach() {
        //TODO удалить после добавления типов вбазу  данных по дефолту
        //(начало)
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            if (expenseTypeRepo.findByType(type.toString()) == null) {
                expenseTypeRepo.save(new ExpenseType(type.toString()));
            }
        }
        //(конец)
        createUser("Peter", "9BSYTW8yrv");
        createUser("Tyler", "Mv3Gjh7HYW");
        createUser("Robert", "skyu5CqcNg");
    }

    @AfterEach
    public void afterEach() {

    }

    @Test
    public void getExpenses() {
        // Arrange
        User user = createUser("Peter", "9BSYTW8yrv");
        createExpense(user,new BigDecimal(100),"BOOKS");
        createExpense(user,new BigDecimal(2),"BOOKS");
        createExpense(user,new BigDecimal(400),"FLOWERS");
        createExpense(user,new BigDecimal(10000),"PRODUCTS");
        // Act
        Page<Expense> expenses = expensesService.getExpenses(user.getId(), pageable);
        // Assert
        Page<Expense> expenses1 = expenseRepo.findAllByUserId(user.getId(),pageable);
        Assertions.assertEquals(expenseRepo.findAllByUserId(user.getId(), pageable), expenses);

        deleteUser(user.getId());
    }

    private void createExpense(User user, BigDecimal amount, String type) {
        Expense expense = new Expense();
        expense.setUser(user);
        expense.setDateAdded(new Date());
        expense.setAmount(amount);
        expense.setType(expenseTypeRepo.findByType(type));
        expenseRepo.save(expense);
    }

    private User createUser(String username, String password) {
        Set<ExpenseType> defaultExpenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            defaultExpenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        return userRepo.save(new User(username, password, userWallet));
    }

    private void deleteUser(Long userId) {
        expenseRepo.deleteAll();
        expenseRepo.findAllByUserId(userId);
        Wallet wallet = walletRepo.getWalletByUserId(userId);
        userRepo.delete(userRepo.getById(userId));
        walletRepo.delete(wallet);
    }
}
