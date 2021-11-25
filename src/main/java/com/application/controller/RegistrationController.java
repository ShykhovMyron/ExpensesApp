package com.application.controller;

import com.application.model.requests.CreateUserRequest;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.InvalidParameterException;

@Controller
public class RegistrationController {
    final static Logger logger = Logger.getLogger(RegistrationController.class);
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationPageInfo(CreateUserRequest createUserRequest) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@Valid CreateUserRequest createUserRequest,
                             BindingResult validResult,
                             Model model) {
        try {
            logger.info(createUserRequest.toString());
            if (validResult.hasErrors()) {
                // TODO validate username and pass here
                throw new InvalidParameterException();
            }
            userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword());
            return "redirect:/login";
        } catch (Exception e) {
            return "registration";
        }
    }
}
