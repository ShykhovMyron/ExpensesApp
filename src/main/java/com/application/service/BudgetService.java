package com.application.service;

import com.application.entity.User;
import com.application.entity.Wallet;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import com.application.utils.BudgetUtils;
import com.application.utils.PurchaseUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;

@Service
public class BudgetService {
    private static UserRepo userRepo;
    private static WalletRepo walletRepo;

    public BudgetService(UserRepo userRepo, WalletRepo walletRepo) {
        BudgetService.userRepo = userRepo;
        BudgetService.walletRepo = walletRepo;
    }

    public static void changeBudget(Integer userID, Wallet userWallet) {
        walletRepo.findByUserId(userID).setBudget(userWallet.getBudget());
        changeBalance(userID);
    }

    public static void changeBalance(Integer userId) {
        Wallet userWallet = walletRepo.findByUserId(userId);
        BigDecimal costs = PurchaseUtils.getPurchasesValue(userId);
        userWallet.setBalance(userWallet.getBudget().subtract(costs));
    }

    public void getHomePageInfo(Integer userId, Model model) {

        BudgetUtils.addHomePageInfoToModel(userId, model);
    }

    public void changeBudget(Wallet userWallet, Integer userId, BindingResult validResult) {
        if (BudgetUtils.checkForValidErrors(validResult)) return;

        changeBudget(userId,userWallet);
        userRepo.save(userRepo.getById(userId));
    }
}
