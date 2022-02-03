package com.application.service;

import com.application.exeptions.TypeAlreadyExistException;
import com.application.exeptions.TypeNotFoundException;
import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class ExpenseTypeService {
    final static Logger logger = Logger.getLogger(ExpenseTypeService.class);

    private final ExpenseTypeRepo expenseTypeRepo;
    private final UserRepo userRepo;
    private final WalletRepo walletRepo;

    public ExpenseTypeService(ExpenseTypeRepo expenseTypeRepo, UserRepo userRepo, WalletRepo walletRepo) {
        this.expenseTypeRepo = expenseTypeRepo;
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
    }

    public Set<ExpenseType> getDefaultExpenseTypes() {
        Set<ExpenseType> expenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            expenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }

        return expenseTypes;
    }

    @Transactional
    public void createExpenseType(Long userId, String expenseType) throws TypeAlreadyExistException {
        if (hasExpenseType(userId, expenseType)) {
            throw new TypeAlreadyExistException(expenseType);
        }

        if (!expenseTypeRepo.existsByType(expenseType)) {
            expenseTypeRepo.save(new ExpenseType(expenseType));
        }
        Wallet wallet = userRepo.getById(userId).getWallet();
        wallet.getTypes().add(expenseTypeRepo.findByType(expenseType));
        walletRepo.save(wallet);
    }

    public boolean hasExpenseType(Long userId, String expenseType) {
        Set<ExpenseType> userTypes = walletRepo.getWalletByUserId(userId).getTypes();
        ExpenseType type = expenseTypeRepo.findByType(expenseType);
        return userTypes.contains(type);
    }

    public void deleteExpenseType(Long userId, String type) throws TypeNotFoundException {
        if (!hasExpenseType(userId, type)) {
            throw new TypeNotFoundException(type);
        }

        Wallet userWallet = walletRepo.getWalletByUserId(userId);

        userWallet.getTypes().remove(expenseTypeRepo.findByType(type));
        logger.info(userWallet.getTypes().toString());
        walletRepo.save(userWallet);
    }
}
