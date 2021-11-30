package com.application.service;

import com.application.config.ExpensesConfig;
import com.application.exeptions.ExpenseNotFoundException;
import com.application.exeptions.TypeNotFoundException;
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

    private final ExpenseTypeService expenseTypeService;
    private final ExpensesConfig expensesConfig;
    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;
    private final ExpenseTypeRepo expenseTypeRepo;

    public ExpensesService(ExpenseTypeService expenseTypeService,
                           ExpensesConfig expensesConfig,
                           UserRepo userRepo,
                           ExpenseRepo expenseRepo,
                           ExpenseTypeRepo expenseTypeRepo) {
        this.expenseTypeService = expenseTypeService;
        this.expensesConfig = expensesConfig;
        this.userRepo = userRepo;
        this.expenseRepo = expenseRepo;
        this.expenseTypeRepo = expenseTypeRepo;
    }

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
    public void editExpense(
            Long userId,
            Long expenseId,
            BigDecimal expenseAmount,
            String expenseType,
            String date
    ) throws ExpenseNotFoundException, ParseException, TypeNotFoundException {
        if (!expenseTypeService.userHasExpense(userId, expenseType)) {
            throw new TypeNotFoundException(expenseType);
        }

        Optional<Expense> expenseOptional = expenseRepo.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException();
        }
        Expense expense = expenseOptional.get();
        expense.setDateAdded(new SimpleDateFormat(expensesConfig.getInputDateFormat(),
                Locale.ENGLISH).parse(date));
        expense.setAmount(expenseAmount);
        expense.setType(expenseTypeRepo.findByType(expenseType));
        expenseRepo.save(expense);
    }

    public void createExpense(Long userId, BigDecimal amount, String type, String date) throws ParseException, TypeNotFoundException {
        if (!expenseTypeService.userHasExpense(userId, type)) {
            throw new TypeNotFoundException(type);
        }

        Expense newExpense = new Expense();
        newExpense.setDateAdded(new SimpleDateFormat(expensesConfig.getInputDateFormat(),
                Locale.ENGLISH).parse(date));
        newExpense.setType(expenseTypeRepo.findByType(type));
        newExpense.setAmount(amount);
        newExpense.setUser(userRepo.getById(userId));
        expenseRepo.save(newExpense);
    }

    public void deleteExpense(Long expenseId) throws ExpenseNotFoundException {

        Optional<Expense> expenseOptional = expenseRepo.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException();
        }

        expenseRepo.delete(expenseOptional.get());
    }

}