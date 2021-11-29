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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class ExpensesIT {
    private static org.springframework.data.domain.Pageable pageable;
    private final ExpensesService expensesService;
    private final UserRepo userRepo;
    private final WalletRepo walletRepo;
    private final ExpenseRepo expenseRepo;
    private final ExpenseTypeRepo expenseTypeRepo;

    public ExpensesIT(ExpensesService expensesService, UserRepo userRepo, WalletRepo walletRepo, ExpenseRepo expenseRepo, ExpenseTypeRepo expenseTypeRepo) {
        this.expensesService = expensesService;
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
        this.expenseRepo = expenseRepo;
        this.expenseTypeRepo = expenseTypeRepo;
    }

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
        Long userId = userRepo.findByUsername("Peter").getId();
        Page<Expense> expenses = expensesService.getExpenses(userId, pageable);

        Assertions.assertEquals(expenseRepo.findAllByUserId(userId, pageable), expenses);
    }

    public void createUser(String username, String password) {
        Set<ExpenseType> defaultExpenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            defaultExpenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        userRepo.save(new User(username, password, userWallet));
    }

    public void deleteUser(String username) {

    }
}
