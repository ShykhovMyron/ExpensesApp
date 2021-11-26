package com.application.model.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateUserRequest {
    @NotEmpty(message = "Username must not be empty")
    @Size(min = 6, max = 30, message = "Username size must be between 6 and 30")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 6, max = 50, message = "Password size must be between 6 and 50")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
