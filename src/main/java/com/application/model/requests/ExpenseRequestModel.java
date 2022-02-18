package com.application.model.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public abstract class ExpenseRequestModel extends ExpenseTypeModel {

    @NotNull(message = "Amount must not be empty")
    @Min(value = 0, message = "Amount must be ≧ 0")
    private BigDecimal amount;

    @NotEmpty(message = "Type must not be empty")
    private String dateAdded;


    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
