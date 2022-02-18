package com.application.model.requests;

import javax.validation.constraints.NotEmpty;

public abstract class ExpenseTypeModel {
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
