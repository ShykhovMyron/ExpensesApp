package com.application.utils;


import com.application.entity.User;
import com.application.repository.PurchasesRepo;
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

    private static Double budgetBeforeSaving;

    private static UserRepo userRepo;
    private static PurchasesRepo purchasesRepo;
    private static WalletRepo walletRepo;

    public BudgetUtils(PurchasesRepo purchasesRepo, UserRepo userRepo, WalletRepo walletRepo) {
        BudgetUtils.purchasesRepo = purchasesRepo;
        BudgetUtils.userRepo = userRepo;
        BudgetUtils.walletRepo = walletRepo;
    }

    public static void addHomePageInfoToModel(Integer userId, Model model) {
        model.addAttribute("username", userRepo.getById(userId).getUsername());
        model.addAttribute("userWallet", walletRepo.findByUserId(userId));
        PurchaseUtils.addFormatDisplayDataOnPageToModel(model);

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
}
