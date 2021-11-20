package com.application.service;

import com.application.entity.User;
import com.application.repository.PurchaseRepo;
import com.application.repository.PurchaseTypeRepo;
import com.application.repository.UserRepo;
import com.application.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.application.service.PurchaseTypeService.addDefaultPurchaseTypesToUser;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PurchaseRepo purchaseRepo;
    private final BudgetService budgetService;

    @Autowired
    PurchaseTypeRepo purchaseTypeRepo;

    public UserService(UserRepo userRepo, PurchaseRepo purchaseRepo, BudgetService budgetService) {
        this.userRepo = userRepo;
        this.purchaseRepo = purchaseRepo;
        this.budgetService = budgetService;
    }

    public boolean createUser(User user, BindingResult validResult, Model model) {
        user = UserUtils.getUserOrNull(user,validResult,model);
        if (user == null)
            return false;

        addDefaultPurchaseTypesToUser(user);

        userRepo.save(user);
        budgetService.createUserBudget(user.getId());
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void deleteExpenses(Long userId) {

        purchaseRepo.deleteAll(purchaseRepo.findAllByUserId(userId));
        BudgetService.changeBalance(userId);
    }
}