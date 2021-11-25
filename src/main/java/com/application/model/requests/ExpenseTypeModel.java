package com.application.model.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ExpenseTypeModel {
    @NotNull(message = "Type must not be empty")
    @NotEmpty(message = "Type must not be empty")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
