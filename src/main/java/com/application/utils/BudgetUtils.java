package com.application.utils;


import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@Component

public class BudgetUtils {
    final static Logger logger = Logger.getLogger(com.application.utils.PurchaseUtils.class);

    public static Model modelErrors = new ExtendedModelMap();

    private static Double budgetBeforeSaving;

    private static UserRepo userRepo;
    private static PurchasesRepo purchasesRepo;

    public BudgetUtils(PurchasesRepo purchasesRepo, UserRepo userRepo) {
        BudgetUtils.purchasesRepo = purchasesRepo;
        BudgetUtils.userRepo = userRepo;
    }


    public static void addHomePageInfoToModel(Double purchaseValue, Double balance, User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("balance", balance);
        PurchaseUtils.addFormatDisplayDataOnPageToModel(model);

        checkErrorsAndAddToModel(model);
    }

    private static void checkErrorsAndAddToModel(Model model){
        model.addAllAttributes(modelErrors.asMap());
        modelErrors = new ExtendedModelMap();
    }
}
