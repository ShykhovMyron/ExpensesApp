package com.application.utils;


import com.application.entity.Wallet;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;

@Component

public class BudgetUtils {
    final static Logger logger = Logger.getLogger(com.application.utils.PurchaseUtils.class);

    public static Model modelErrors = new ExtendedModelMap();

    private static BigDecimal balanceBeforeSaving;

    private static UserRepo userRepo;
    private static WalletRepo walletRepo;

    public BudgetUtils( UserRepo userRepo, WalletRepo walletRepo) {
        BudgetUtils.userRepo = userRepo;
        BudgetUtils.walletRepo = walletRepo;
    }

    public static void addHomePageInfoToModel(Long userId, Model model) {
        model.addAttribute("username", userRepo.getById(userId).getUsername());
        model.addAttribute("userWallet", walletRepo.getById(userId));

        checkErrorsAndAddToModel(model);
    }

    public static boolean checkForValidErrors(BindingResult validResult) {
        if (validResult.hasErrors()) {
            BudgetUtils.modelErrors.addAttribute("errors", validResult.getFieldErrors());
            return true;
        }
        return false;
    }

    private static void checkErrorsAndAddToModel(Model model) {
        model.addAllAttributes(modelErrors.asMap());
        modelErrors = new ExtendedModelMap();
    }

    public static void checkBudgetBeforeSaving(Long userId) {
        Wallet userWallet = walletRepo.getById(userId);
        BigDecimal tenPercentOfBudget = userWallet.getBudget().multiply(BigDecimal.valueOf(0.1));

        if (userWallet.getBalance().compareTo(tenPercentOfBudget) >= 0) {
            balanceBeforeSaving = userWallet.getBudget();
        }
    }

    public static void warnIfLowBudget(Long userId, Model model) {
        Wallet userWallet = walletRepo.getById(userId);

        BigDecimal tenPercentOfBudget = userWallet.getBudget().multiply(BigDecimal.valueOf(0.1));
        if (balanceBeforeSaving != null &&
                userWallet.getBalance().compareTo(tenPercentOfBudget) < 0) {
            model.addAttribute("lowBudget", true);
            balanceBeforeSaving = null;
        }
    }

}
