package com.application.testConfig;

import com.application.config.ExpensesConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
@ConfigurationProperties(prefix = "expenses-test")
public class TestExpensesConfig extends ExpensesConfig {

    private final DateFormat formatter = new SimpleDateFormat(super.getInputDateFormat());

    public DateFormat getFormatter() {
        return formatter;
    }
}
