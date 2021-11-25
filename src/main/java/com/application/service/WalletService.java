package com.application.service;

import com.application.model.entity.Expense;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

/**
 * отвечает за бюджет и баланс
 */
@Service
public class WalletService {
    private final WalletRepo walletRepo;
    private final ExpenseRepo expenseRepo;

    public WalletService(WalletRepo walletRepo, ExpenseRepo expenseRepo) {
        this.walletRepo = walletRepo;
        this.expenseRepo = expenseRepo;
    }

    @Transactional
    public void changeBudget(Long userId, BigDecimal newBudget) {
        Wallet wallet = walletRepo.getWalletByUserId(userId);
        BigDecimal balance = (newBudget.subtract(wallet.getBudget())).add(wallet.getBalance());
        wallet.setBudget(newBudget);
        wallet.setBalance(balance);
        walletRepo.save(wallet);
    }

    @Transactional
    public void recalculateBalance(Long userId) {
        BigDecimal expensesValue = getExpensesValue(userId);

        Wallet userWallet = walletRepo.getWalletByUserId(userId);
        userWallet.setBalance(userWallet.getBudget().subtract(expensesValue));
        walletRepo.save(userWallet);
    }

    private BigDecimal getExpensesValue(Long userId) {
        Set<Expense> expenses = expenseRepo.findAllByUserId(userId);
        BigDecimal expensesValue = new BigDecimal(0);

        for (Expense expense : expenses) {
            expensesValue = expensesValue.add(expense.getAmount());
        }

        return expensesValue;
    }

    public Wallet getWallet(Long userId) {
        return walletRepo.getWalletByUserId(userId);
    }
}
