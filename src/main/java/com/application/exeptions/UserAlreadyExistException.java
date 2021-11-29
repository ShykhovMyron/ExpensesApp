package com.application.exeptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String message) {
        super("User \""+message+"\" already exist");
    }
}
