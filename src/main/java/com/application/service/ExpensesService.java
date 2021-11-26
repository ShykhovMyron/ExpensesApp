package com.application.service;

import com.application.exeptions.ExpenseNotFoundException;
import com.application.model.entity.Expense;
import com.application.repository.ExpenseRepo;
import com.application.repository.ExpenseTypeRepo;
import com.application.repository.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * этот сервис отвечает за работу с покупками юзера
 */
@Service
public class ExpensesService {
    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;
    private final WalletService walletService;
    private final ExpenseTypeRepo expenseTypeRepo;

    public ExpensesService(UserRepo userRepo, ExpenseRepo expenseRepo, WalletService walletService, ExpenseTypeRepo expenseTypeRepo) {
        this.userRepo = userRepo;
        this.expenseRepo = expenseRepo;
        this.walletService = walletService;
        this.expenseTypeRepo = expenseTypeRepo;
    }

    @Transactional
    public Page<Expense> getExpenses(Long userId, Pageable pageable) {
        return expenseRepo.findAllByUserId(userId, pageable);
    }

    public Expense getExpense(Long expenseId) throws ExpenseNotFoundException {
        Optional<Expense> expenseOptional = expenseRepo.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException();
        }

        return expenseOptional.get();
    }

    @Transactional
    public void editExpense(Long expenseId, BigDecimal expenseAmount, String expenseType) throws ExpenseNotFoundException {
        Optional<Expense> expenseOptional = expenseRepo.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException();
        }

        Expense expense = expenseOptional.get();
        expense.setAmount(expenseAmount);
        expense.setType(expenseTypeRepo.findByType(expenseType));
        expenseRepo.save(expense);
    }

    @Transactional
    public void createExpense(Long userId, BigDecimal amount, String type, String date) throws ParseException {

        Expense newExpense = new Expense();
        newExpense.setDateAdded(new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).parse(date));
        newExpense.setType(expenseTypeRepo.findByType(type));
        newExpense.setAmount(amount);
        newExpense.setUser(userRepo.getById(userId));
        expenseRepo.save(newExpense);
    }

    @Transactional
    public void deleteExpense(Long expenseId) throws ExpenseNotFoundException {

        Optional<Expense> expenseOptional = expenseRepo.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException();
        }

        expenseRepo.delete(expenseOptional.get());
    }

}