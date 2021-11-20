package com.application.service;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import com.application.repository.PurchaseRepo;
import com.application.repository.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.application.utils.PurchaseUtils.*;

@Service
public class PurchaseService {
    private static UserRepo userRepo;
    private static PurchaseRepo purchaseRepo;

    public PurchaseService(PurchaseRepo purchaseRepo, UserRepo userRepo) {
        PurchaseService.purchaseRepo = purchaseRepo;
        PurchaseService.userRepo = userRepo;
    }

    private static Page<Purchase> getPurchases(Long userId, Pageable pageable) {
        return purchaseRepo.findAllByUserId(userId, pageable);
    }

    public void getExpensesPageInfo(Long userId, Model model, Pageable pageable) {

        Page<Purchase> userPurchases = getPurchases(userId, pageable);

        addExpensesPageInfoToModel(userId, model, pageable, userPurchases);
    }

    public void editPurchase(Long purchaseId, Purchase newPurchaseInfo, Long userId,
                             BindingResult validResult, String type) {

        Purchase oldPurchase = getOldPurchaseOrNull(purchaseId, userId, validResult);
        PurchaseType newPurchaseType = PurchaseTypeService.getPurchaseTypeOrNull(type);
        if (oldPurchase == null || newPurchaseType == null) return;
        oldPurchase.setAmount(newPurchaseInfo.getAmount());
        oldPurchase.setType(newPurchaseType);
        purchaseRepo.save(oldPurchase);

        BudgetService.changeBalance(userId);
    }

    public void deletePurchase(Long purchaseId, Long userId) {
        Purchase purchase = getPurchaseOrNull(purchaseId);
        if (purchase == null) return;
        purchaseRepo.delete(purchase);

        BudgetService.changeBalance(userId);
    }

    public void createPurchase(Long userId, Purchase purchase, String date, BindingResult validResult, String type) {
        Purchase purchaseWithDate = getPurchaseWithDateOrNull(userId, purchase,
                date, validResult);
        PurchaseType purchaseType = PurchaseTypeService.getPurchaseTypeOrNull(type);
        if (purchaseWithDate == null || purchaseType == null) return;
        purchase.setUser(userRepo.getById(userId));
        purchase.setType(purchaseType);
        purchaseRepo.save(purchase);

        BudgetService.changeBalance(userId);
    }
}