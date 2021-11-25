package com.application.exeptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException() {
        super("User already exist");
    }
}
