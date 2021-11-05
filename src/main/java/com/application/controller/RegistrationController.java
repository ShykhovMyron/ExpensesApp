package com.application.controller;

import com.application.entity.User;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    final static Logger logger = Logger.getLogger(RegistrationController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        logger.info("Come to registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        logger.info("Got request :" + model);
        if (userService.createUser(user)) {
            logger.info("User username:" + user.getUsername() + " password:" + user.getPassword() + " created");
            return "redirect:/login";
        } else {
            logger.info("User username:" + user.getUsername() + " password:" + user.getPassword() + " alreadydi  exist");
            model.addAttribute("message", "User exists!");
            return "registration";
        }
    }
}
