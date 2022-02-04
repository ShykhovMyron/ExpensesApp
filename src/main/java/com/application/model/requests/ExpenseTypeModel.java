package com.application.model.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public abstract class ExpenseTypeModel {
    @NotNull(message = "Type must not be empty")
    @NotEmpty(message = "Type must not be empty")
    private String type;

    @Override
    public String toString() {
        return "ExpenseTypeModel{" +
                "type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
