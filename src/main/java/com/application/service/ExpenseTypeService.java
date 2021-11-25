package com.application.service;

import com.application.exeptions.TypeAlreadyExistException;
import com.application.model.entity.DefaultExpenseTypes;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ExpenseTypeService {
    private final ExpenseTypeRepo expenseTypeRepo;
    private final UserRepo userRepo;
    private final WalletRepo walletRepo;

    public ExpenseTypeService(ExpenseTypeRepo expenseTypeRepo, UserRepo userRepo, WalletRepo walletRepo) {
        this.expenseTypeRepo = expenseTypeRepo;
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
    }

    public Set<ExpenseType> getDefaultExpenseTypes() {
        addDefaultExpenseTypesToDatabase();
        Set<ExpenseType> expenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            expenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }

        return expenseTypes;
    }

    private void addDefaultExpenseTypesToDatabase() {
        //TODO добавить дефолтные типы в бд
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            if (expenseTypeRepo.findByType(type.toString()) == null) {
                expenseTypeRepo.save(new ExpenseType(type.toString()));
            }
        }
    }

    public void createExpenseType(Long userId, String expenseType) throws TypeAlreadyExistException {
        if (isUserHaveThisType(userId, expenseType)) {
            throw new TypeAlreadyExistException();
        }

        if (!expenseTypeRepo.existsByType(expenseType)) {
            expenseTypeRepo.save(new ExpenseType(expenseType));
        }
        Wallet wallet = userRepo.getById(userId).getWallet();
        wallet.getTypes().add(expenseTypeRepo.findByType(expenseType));
        walletRepo.save(wallet);
    }

    public boolean isUserHaveThisType(Long userId, String expenseType) {
        Set<ExpenseType> userTypes = walletRepo.getWalletByUserId(userId).getTypes();
        ExpenseType type = expenseTypeRepo.findByType(expenseType);
        return userTypes.contains(type);
    }
}