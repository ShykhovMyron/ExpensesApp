package com.application.service;

import com.application.controller.ExpensesController;
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

    public void deleteExpenseType(Long userId, String type) throws TypeNotFoundException {
        if (!isUserHaveThisType(userId, type)) {
            throw new TypeNotFoundException();
        }

        Wallet userWallet = walletRepo.getWalletByUserId(userId);
//        Optional<ExpenseType> expenseTypeOptional = userWallet.getTypes()
//                .stream().filter(s -> s.getType().equals(type))
//                .findAny();
//        if (expenseTypeOptional.isEmpty()) {
//            throw new TypeNotFoundException();
//        }

        userWallet.getTypes().remove(expenseTypeRepo.findByType(type));
        logger.info(userWallet.getTypes().toString());
        walletRepo.save(userWallet);
    }
}