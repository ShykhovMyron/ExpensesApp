package com.application.model.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ChangeBudgetRequest {

    @NotNull(message = "Budget must not be empty")
    @Min(value = 0, message = "Budget must be ≧ 0")
    private BigDecimal budget;

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
