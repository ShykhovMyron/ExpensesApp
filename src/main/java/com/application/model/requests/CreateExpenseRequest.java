package com.application.model.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateExpenseRequest extends ExpenseRequestModel {
    @NotNull(message = "Date must not be empty")
    @NotEmpty(message = "Type must not be empty")
    private String dateAdded;

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
