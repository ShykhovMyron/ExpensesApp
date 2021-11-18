package com.application.service;

import com.application.entity.User;
import com.application.repository.UserRepo;
import com.application.utils.BudgetUtils;
import com.application.utils.PurchaseUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class BudgetService {
    private static UserRepo userRepo;

    public BudgetService(UserRepo userRepo) {
        BudgetService.userRepo = userRepo;
    }

    public void getHomePageInfo(Integer userId, Model model) {

        User user = userRepo.getById(userId);

        Double purchaseValue = PurchaseUtils.getPurchasesValue(user);
        Double balance = user.getBudget() - purchaseValue;

        BudgetUtils.addHomePageInfoToModel(purchaseValue, balance, user, model);
    }

    public void changeBudget(User expectedUser, Integer userId, BindingResult validResult) {
        if (validResult.hasErrors()) {
            BudgetUtils.modelErrors.addAttribute("errors", validResult.getFieldErrors());
            return;
        }

        User user = userRepo.getById(userId);
        user.setBudget(expectedUser.getBudget());
        userRepo.save(user);
    }
}
