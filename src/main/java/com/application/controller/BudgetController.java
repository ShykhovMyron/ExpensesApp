package com.application.controller;

import com.application.model.entity.User;
import com.application.model.requests.ChangeBudgetRequest;
import com.application.service.WalletService;
import org.apache.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

import static com.application.parser.ErrorsParser.getValidErrors;

@Controller
public class BudgetController {
    final static Logger logger = Logger.getLogger(BudgetController.class);

    private final WalletService walletService;

    public BudgetController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               ChangeBudgetRequest request,
                               Model model) {

        model.addAttribute("userBudget", walletService.getWallet(user.getId()).getBudget());
        return "modals/ChangeBudgetModalBody";
    }

    @PostMapping("/changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               @Valid ChangeBudgetRequest request,
                               BindingResult validResult,
                               @ModelAttribute("errors") ArrayList<String> errors,
                               RedirectAttributes redirectAttributes) {
        try {
            if (validResult.hasErrors()) {
                errors = getValidErrors(validResult);
            } else {
                walletService.changeBudget(user.getId(), request.getBudget());
            }
        } finally {
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/home";
        }
    }
}