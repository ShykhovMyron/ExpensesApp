package com.application.parser;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ErrorsParser {
    public static ArrayList<String> getValidErrors(BindingResult validResult) {
        return (ArrayList<String>) validResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    public static ArrayList<String> getExceptionErrors(Exception exception) {
        return new ArrayList<>() {{
            add(exception.getMessage());
        }};
    }
}
