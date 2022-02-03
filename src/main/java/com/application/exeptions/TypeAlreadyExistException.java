package com.application.exeptions;

public class TypeAlreadyExistException extends Exception {

    public TypeAlreadyExistException(String message) {
        super("Type \"" + message + "\" already exist");
    }
}
