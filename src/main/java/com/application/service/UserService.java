package com.application.service;

import com.application.exeptions.UserAlreadyExistException;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final WalletRepo walletRepo;
    //TODO он тут будет не нужен когда спрячеш метод deleteExpenses в нужный сервис
    private final ExpenseRepo expenseRepo;
    private final ExpenseTypeService expenseTypeService;

    public UserService(UserRepo userRepo,
                       WalletRepo walletRepo,
                       ExpenseRepo expenseRepo,
                       ExpenseTypeService expenseTypeService) {
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
        this.expenseRepo = expenseRepo;
        this.expenseTypeService = expenseTypeService;
    }

    @Transactional
    public void createUser(String username, String password) throws UserAlreadyExistException {
        if (userRepo.existsByUsername(username)) {
            throw new UserAlreadyExistException(username);
        }
        createUserWithDefaultParams(username, password);
    }

    //TODO спрятать это в експенсСервис, оно ж логически туда относится
    public void deleteExpenses(Long userId) {
        expenseRepo.deleteAll(expenseRepo.findAllByUserId(userId));
    }

    //TODO верни это в метод, оно не большое и относится по сути к одному методу createUser
    private void createUserWithDefaultParams(String username, String password) {
        Set<ExpenseType> defaultExpenseTypes = expenseTypeService.getDefaultExpenseTypes();
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        userRepo.save(new User(username, password, userWallet));
    }
}