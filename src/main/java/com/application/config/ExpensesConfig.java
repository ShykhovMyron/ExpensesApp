package com.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "expenses")
public class ExpensesConfig {
    private int pagesToShow = 20;
    private String inputDateFormat = "yyyy-M-d";

    public int getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(int pagesToShow) {
        this.pagesToShow = pagesToShow;
    }

    public String getInputDateFormat() {
        return inputDateFormat;
    }

    public void setInputDateFormat(String inputDateFormat) {
        this.inputDateFormat = inputDateFormat;
    }
}
