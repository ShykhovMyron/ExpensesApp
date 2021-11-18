package com.application.controller;

import com.application.entity.User;
import com.application.repository.UserRepo;
import com.application.service.BudgetService;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BudgetController {
    final static Logger logger = Logger.getLogger(BudgetController.class);
    private static BindingResult errors;

    private final UserService userService;

    private final UserRepo userRepo;

    private final BudgetService budgetService;

    public BudgetController(UserService userService, UserRepo userRepo, BudgetService budgetService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.budgetService = budgetService;
    }

    @GetMapping({"", "home"})
    public String getHomePageInfo(@AuthenticationPrincipal User user,
                                User expectedUser,
                                Model model) {

        budgetService.getHomePageInfo(user.getId(),model);
        return "home";
    }

    @PostMapping("changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               @Valid User expectedUser,
                               BindingResult validResult) {

        budgetService.changeBudget(expectedUser,user.getId(),validResult);
        return "redirect:/home";
    }
}