package com.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "expenses")
public class ExpensesConfig {
    private int pagesToShow = 20;

    public int getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(int pagesToShow) {
        this.pagesToShow = pagesToShow;
    }
}
