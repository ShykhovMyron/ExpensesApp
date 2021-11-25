package com.application.controller;

import com.application.model.entity.User;
import com.application.model.entity.Wallet;
import com.application.model.requests.ChangeBudgetRequest;
import com.application.service.UserService;
import com.application.service.WalletService;
import com.application.utils.BudgetUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    private final WalletService walletService;

    public HomePageController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping({"/", "/home"})
    public String getHomePageInfo(@AuthenticationPrincipal User user,
                                  ChangeBudgetRequest request,
                                  Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("userWallet", walletService.getWallet(user.getId()));

        return "home";
    }

}
