package com.application.controller;

import com.application.exeptions.NegativeBudgetException;
import com.application.model.entity.User;
import com.application.model.requests.ChangeBudgetRequest;
import com.application.service.WalletService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BudgetController {
    private final WalletService walletService;

    public BudgetController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               @Valid ChangeBudgetRequest request,
                               BindingResult validResult) {
        try {
            if (validResult.hasErrors()){ throw new NegativeBudgetException();}
            walletService.changeBudget(user.getId(), request.getBudget());
        } catch (Exception e) {

        }
        return "redirect:/home";
    }
}