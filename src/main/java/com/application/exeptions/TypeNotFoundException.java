package com.application.exeptions;

public class TypeNotFoundException extends Exception {

    public TypeNotFoundException(String message) {
        super("Type \"" + message + "\" not found");
    }
}
