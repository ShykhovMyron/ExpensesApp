package com.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "expenses")
public class ExpensesConfig {
    private final int pagesToShow = 20;
    private final String inputDateFormat = "yyyy-M-d";

    public int getPagesToShow() {
        return pagesToShow;
    }

    public String getInputDateFormat() {
        return inputDateFormat;
    }
}
