package com.application.controller;

import com.application.entity.User;
import com.application.entity.Wallet;
import com.application.service.BudgetService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping({"", "home"})
    public String getHomePageInfo(@AuthenticationPrincipal User user,
                                  Wallet wallet,
                                  Model model) {

        budgetService.getHomePageInfo(user.getId(), model);
        return "home";
    }

    @PostMapping("changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               @Valid Wallet wallet,
                               BindingResult validResult) {

        budgetService.changeBudget(wallet, user.getId(), validResult);
        return "redirect:/home";
    }
}