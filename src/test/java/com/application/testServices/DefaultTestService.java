package com.application.testServices;

import com.application.model.entity.*;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class DefaultTestService {
    @Autowired
    private ExpenseTypeRepo expenseTypeRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserRepo userRepo;
    public Expense createExpense(User user, Integer amount, String type, DateFormat formatter) {
        Expense expense = new Expense();
        expense.setUser(user);
        try {
            expense.setDateAdded(formatter.parse(formatter.format(new Date())));
        } catch (ParseException ignored) {
        }
        expense.setAmount(new BigDecimal(amount + ".00"));
        expense.setType(expenseTypeRepo.findByType(type));
        expenseRepo.save(expense);
        return expense;
    }

    public User createUser(String username, String password) {
        Set<ExpenseType> defaultExpenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            defaultExpenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }
        Wallet userWallet = walletRepo.save(new Wallet(defaultExpenseTypes));
        return userRepo.save(new User(username, password, userWallet));
    }

    public Set<ExpenseType> getDefaultExpenseTypes() {
        Set<ExpenseType> expenseTypes = new HashSet<>();
        for (DefaultExpenseTypes type : DefaultExpenseTypes.values()) {
            expenseTypes.add(expenseTypeRepo.findByType(type.toString()));
        }

        return expenseTypes;
    }
}
