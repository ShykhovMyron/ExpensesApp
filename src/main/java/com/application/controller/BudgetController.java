package com.application.controller;

import com.application.exeptions.NegativeBudgetException;
import com.application.model.entity.User;
import com.application.model.requests.ChangeBudgetRequest;
import com.application.service.WalletService;
import org.apache.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BudgetController {
    final static Logger logger = Logger.getLogger(BudgetController.class);

    private final WalletService walletService;

    public BudgetController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               @Valid ChangeBudgetRequest request,
                               BindingResult validResult) {
//        logger.info("Post:/changeBudget - ");
        try {
            if (validResult.hasErrors()) {
                throw new NegativeBudgetException();
            }
            walletService.changeBudget(user.getId(), request.getBudget());
        } catch (Exception e) {
            logger.warn("Post:/changeBudget - " + e.getClass().getSimpleName());
        }
        return "redirect:/home";
    }
}