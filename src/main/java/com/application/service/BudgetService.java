package com.application.service;

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

    public void getHomePageInfo(Long userId, Model model) {

        BudgetUtils.addHomePageInfoToModel(userId, model);
    }

    public void changeBudget(Wallet userWallet, Long userId, BindingResult validResult) {
        if (BudgetUtils.checkForValidErrors(validResult)) return;

        changeBudget(userId, userWallet);
        walletRepo.save(userWallet);
    }

    public void createUserBudget(Long id) {
        Wallet wallet = new Wallet();
        wallet.setUser(userRepo.getById(id));
        walletRepo.save(wallet);
    }

    public static void changeBudget(Long userId, Wallet userWallet) {
        userWallet.setId(userId);

        walletRepo.getById(userId).setBudget(userWallet.getBudget());
        changeBalance(userId, userWallet);
    }

    public static void changeBalance(Long userId, Wallet userWallet) {
        BigDecimal costs = PurchaseUtils.getPurchasesValue(userId);
        userWallet.setBalance(userWallet.getBudget().subtract(costs));
    }

    public static void changeBalance(Long userId) {
        Wallet userWallet = walletRepo.getById(userId);
        BigDecimal costs = PurchaseUtils.getPurchasesValue(userId);
        userWallet.setBalance(userWallet.getBudget().subtract(costs));
    }
}
