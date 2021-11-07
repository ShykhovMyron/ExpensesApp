package com.application.controller;

import com.application.entity.User;
import com.application.repository.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private UserRepo userRepo;

    final static Logger logger = Logger.getLogger(HomeController.class);

    @GetMapping()
    public String greeting(@AuthenticationPrincipal User user,Model model) {
        logger.info("Come to main-page");
        model.addAttribute("budget", user.getBudget());
        return "home";
    }


    @GetMapping("home")
    public String greetingHome(@AuthenticationPrincipal User user,Model model) {
        logger.info("Come to main-page");
        model.addAttribute("budget", user.getBudget());
        return "home";
    }

    @PostMapping("home")
    public String putBudget(@AuthenticationPrincipal User user,
                            @RequestParam(required = false) Long budget,
                            Model model) {
        if (budget != null) {
            user.setBudget(budget);
            userRepo.save(user);
        }
        model.addAttribute("budget", user.getBudget());
        return "home";
    }

}
