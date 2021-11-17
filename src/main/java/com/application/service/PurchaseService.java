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
    private final UserRepo userRepo;
    private final PurchasesRepo purchasesRepo;

    public PurchaseService(PurchasesRepo purchasesRepo, UserRepo userRepo) {
        this.purchasesRepo = purchasesRepo;
        this.userRepo = userRepo;
    }

    // отету хуйню логически разбить на подчасти и вынести в отдельные методы
    // PurchaseModelService создать и засунуть туда вввсе методы
    //      касающиеся создания и наполнения модели пурчейза.
    //    А лучше даже PurchaseUtils и туда засунуть все статические методы которые
    //    засовывают, проверяют и валидируют пурчейз

    // cначала публичные функции потом протектед потом приватные
    // переделать этот и другие методы чтоб они принимали юзерайди а неюзера целиком, он тут не нужен
    public void getPurchases(Integer userId, Model model, Pageable pageable, Type fitterByType) {

        Page<Purchase> purchases;
        if (fitterByType == null) {
            purchases = purchasesRepo.findAllByUser_id(userId, pageable);
        } else {
             purchases = purchasesRepo.findAllByUser_idAndType(userId, fitterByType, pageable);
        }
        PurchaseUtils.addExpensesPageInfoToModel(userId,model,pageable,fitterByType,purchases);
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
        Purchase purchaseWithDate = PurchaseUtils.getPurchaseWithDateOrNull(userId, purchase, date, validResult);
        if (purchaseWithDate == null) return;
        purchase.setUser(userRepo.getById(userId));
        purchasesRepo.save(purchase);
    }
}