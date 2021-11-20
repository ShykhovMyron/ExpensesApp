package com.application.service;

import com.application.entity.DefaultPurchaseTypes;
import com.application.entity.PurchaseType;
import com.application.entity.User;
import com.application.repository.PurchaseTypeRepo;
import com.application.repository.UserRepo;
import com.application.utils.PurchaseUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Set;

import static com.application.utils.PurchaseUtils.checkValidErrors;

@Service
public class PurchaseTypeService {
    private static PurchaseTypeRepo purchaseTypeRepo;
    private static UserRepo userRepo;

    public PurchaseTypeService(PurchaseTypeRepo purchaseTypeRepo, UserRepo userRepo) {
        PurchaseTypeService.purchaseTypeRepo = purchaseTypeRepo;
        PurchaseTypeService.userRepo = userRepo;
    }

    public static void addDefaultPurchaseTypesToUser(User user) {
        addDefaultPurchasesTypesToDatabase();

        Set<PurchaseType> userTypes = getDefaultPurchaseTypes();
        user.setTypes(userTypes);
    }

    public void createPurchaseType(Long userId, PurchaseType purchaseType, BindingResult validResult) {
        if (checkValidErrors(validResult)) return;

        if (userAlreadyHaveReceivedType(userId, purchaseType)) {
            PurchaseUtils.modelErrors
                    .addAttribute("errors", "User already have this type");
            return;
        }

        if (purchaseTypeRepo.findByType(purchaseType.getType()) == null) {
            purchaseTypeRepo.save(purchaseType);
        }
        User user = userRepo.getById(userId);
        user.getTypes().add(purchaseTypeRepo.findByType(purchaseType.getType()));
        userRepo.save(user);
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

    private static boolean userAlreadyHaveReceivedType(Long userId, PurchaseType purchaseType) {
        return userRepo.getById(userId).getTypes()
                .contains(purchaseTypeRepo.findByType(purchaseType.getType()));
    }
}