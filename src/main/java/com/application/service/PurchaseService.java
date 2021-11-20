package com.application.service;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import com.application.repository.PurchaseRepo;
import com.application.repository.UserRepo;
import com.application.utils.PurchaseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class PurchaseService {
    private static UserRepo userRepo;
    private static PurchaseRepo purchaseRepo;

    public PurchaseService(PurchaseRepo purchaseRepo, UserRepo userRepo) {
        PurchaseService.purchaseRepo = purchaseRepo;
        PurchaseService.userRepo = userRepo;
    }

    public void getExpensesPageInfo(Long userId, Model model, Pageable pageable) {

        Page<Purchase> userPurchases = getPurchases(userId, pageable);

        PurchaseUtils.addExpensesPageInfoToModel(userId,model,pageable,userPurchases);
    }

    public void editPurchase(Long purchaseId, Purchase newPurchaseInfo, Long userId,
                             BindingResult validResult) {

        Purchase oldPurchase = PurchaseUtils.getOldPurchaseOrNull(purchaseId, userId, validResult);
        if (oldPurchase == null) return;
        oldPurchase.setAmount(newPurchaseInfo.getAmount());
        oldPurchase.setType(newPurchaseInfo.getType());
        purchaseRepo.save(oldPurchase);

        BudgetService.changeBalance(userId);
    }

    public void deletePurchase(Long purchaseId, Long userId) {
        Purchase purchase = PurchaseUtils.getPurchaseOrNull(purchaseId);
        if (purchase == null) return;
        purchaseRepo.delete(purchase);

        BudgetService.changeBalance(userId);
    }

    public void createPurchase(Long userId, Purchase purchase, String date, BindingResult validResult) {
        Purchase purchaseWithDate = PurchaseUtils.getPurchaseWithDateOrNull(userId, purchase,
                date, validResult);
        if (purchaseWithDate == null) return;
        purchase.setUser(userRepo.getById(userId));
        purchaseRepo.save(purchase);

        BudgetService.changeBalance(userId);
    }

    private static Page<Purchase> getPurchases(Long userId, Pageable pageable){
            return purchaseRepo.findAllByUserId(userId, pageable);
    }
}