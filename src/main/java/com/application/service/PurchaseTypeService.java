package com.application.service;

import com.application.entity.DefaultPurchaseTypes;
import com.application.entity.PurchaseType;
import com.application.entity.User;
import com.application.repository.PurchaseTypeRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PurchaseTypeService {
    private static PurchaseTypeRepo purchaseTypeRepo;

    public PurchaseTypeService(PurchaseTypeRepo purchaseTypeRepo) {
        PurchaseTypeService.purchaseTypeRepo = purchaseTypeRepo;
    }

    public static void addDefaultPurchaseTypesToUser(User user) {
        addDefaultPurchasesTypesToDatabase();

        Set<PurchaseType> userTypes = getDefaultPurchaseTypes();
        user.setTypes(userTypes);
    }

    private static Set<PurchaseType> getDefaultPurchaseTypes() {
        Set<PurchaseType> purchaseTypes = new HashSet<>();
        for (DefaultPurchaseTypes type : DefaultPurchaseTypes.values()) {
            purchaseTypes.add(purchaseTypeRepo.findByType(type.toString()));
        }

        return purchaseTypes;
    }

    private static void addDefaultPurchasesTypesToDatabase() {
        for (DefaultPurchaseTypes type : DefaultPurchaseTypes.values()) {
            if (purchaseTypeRepo.findByType(type.toString()) == null) {
                purchaseTypeRepo.save(new PurchaseType(type.toString()));
            }
        }
    }
}