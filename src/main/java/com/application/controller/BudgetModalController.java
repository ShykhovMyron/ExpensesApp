package com.application.controller;

import com.application.model.entity.User;
import com.application.model.requests.ChangeBudgetRequest;
import com.application.service.WalletService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BudgetModalController {
    private final WalletService walletService;

    public BudgetModalController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               ChangeBudgetRequest request,
                               Model model) {

        model.addAttribute("userBudget", walletService.getWallet(user.getId()).getBudget());
        return "ChangeBudgetModelBody";
    }
}
