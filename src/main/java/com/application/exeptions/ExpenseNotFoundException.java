package com.application.exeptions;

public class ExpenseNotFoundException extends Exception {

    public ExpenseNotFoundException() {
        super("Expense not found");
    }
}
