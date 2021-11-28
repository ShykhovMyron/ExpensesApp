package com.application.service;

import com.application.model.entity.Expense;
import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.repository.ExpenseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExpensesServiceIT {

    @Autowired
    public ExpensesService expensesService;
    public ExpenseRepo expenseRepo;

    @BeforeEach()
    public void beforeEach() {
    }

    @Test
    void getExpenses() {
        // Arrange
//        Expense expectedExpense = new Expense();
//        Wallet wallet = Wallet()
//        User user = new User("Artem", "shit", );
//        expenseRepo.save()
        // создать юзера с воллетом и екпенсами (полноценного юзера)
        // сохранить ему пару евспенсов
        // Act
        // получить експенсы юзера в какую-то переменную
        // Assert
        // сравниить експенсы юзера с тем что ты сохранял
    }

    @Test
    void getExpense() {
    }

    @Test
    void editExpense() {
    }

    @Test
    void createExpense() {
    }

    @Test
    void deleteExpense() {
    }
}