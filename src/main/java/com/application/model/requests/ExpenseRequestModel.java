package com.application.model.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ExpenseRequestModel extends ExpenseTypeModel {

    @NotNull(message = "Amount must not be empty")
    @Min(value = 0, message = "Amount must be ≧ 0")
    private BigDecimal amount;

    @Override
    public String toString() {
        return "ExpenseRequestModel{" +
                "amount=" + amount +
                '}';
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
