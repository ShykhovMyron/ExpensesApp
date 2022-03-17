package com.application.service;

import com.application.exeptions.UserAlreadyExistException;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * этот сервис отвечает за юзера
 */
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final WalletRepo walletRepo;
    private final ExpenseTypeService expenseTypeService;

    public UserService(PasswordEncoder passwordEncoder, UserRepo userRepo,
                       WalletRepo walletRepo,
                       ExpenseTypeService expenseTypeService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
        this.expenseTypeService = expenseTypeService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createUser(String username, String password) throws UserAlreadyExistException {
        if (userRepo.existsByUsername(username)) {
            throw new UserAlreadyExistException(username);
        }
        Set<ExpenseType> defaultExpenseTypes = expenseTypeService.getDefaultExpenseTypes();
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        userRepo.save(new User(username, passwordEncoder.encode(password), userWallet));
    }

}