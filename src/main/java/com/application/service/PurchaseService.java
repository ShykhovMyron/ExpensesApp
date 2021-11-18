package com.application.service;

import com.application.entity.Purchase;
import com.application.entity.Type;
import com.application.repository.PurchasesRepo;
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
    private static PurchasesRepo purchasesRepo;

    public PurchaseService(PurchasesRepo purchasesRepo, UserRepo userRepo) {
        PurchaseService.purchasesRepo = purchasesRepo;
        PurchaseService.userRepo = userRepo;
    }

    public void getExpensesPageInfo(Integer userId, Model model, Pageable pageable, Type fitterByType) {

        Page<Purchase> purchases = getPurchases(userId, fitterByType, pageable);

        PurchaseUtils.addExpensesPageInfoToModel(userId,model,pageable,purchases);
    }

    public void editPurchase(Integer purchaseId, Purchase newPurchaseInfo, Integer userId,
                             BindingResult validResult) {

        Purchase oldPurchase = PurchaseUtils.getOldPurchaseOrNull(purchaseId, userId, validResult);
        if (oldPurchase == null) return;
        oldPurchase.setAmount(newPurchaseInfo.getAmount());
        oldPurchase.setType(newPurchaseInfo.getType());
        purchasesRepo.save(oldPurchase);
    }

    public void deletePurchase(Integer purchaseId) {
        Purchase purchase = PurchaseUtils.getPurchaseOrNull(purchaseId);
        if (purchase == null) return;
        purchasesRepo.delete(purchase);
    }

    public void createPurchase(Integer userId, Purchase purchase, String date, BindingResult validResult) {
        Purchase purchaseWithDate = PurchaseUtils.getPurchaseWithDateOrNull(userId, purchase,
                date, validResult);
        if (purchaseWithDate == null) return;
        purchase.setUser(userRepo.getById(userId));
        purchasesRepo.save(purchase);
    }

    private static Page<Purchase> getPurchases(Integer userId, Type fitterByType, Pageable pageable){
        if (fitterByType == null) {
            return purchasesRepo.findAllByUser_id(userId, pageable);
        } else {
            return purchasesRepo.findAllByUser_idAndType(userId, fitterByType, pageable);
        }
    }
}