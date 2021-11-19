package com.application.service;

import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import com.application.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PurchasesRepo purchasesRepo;
    private final BudgetService budgetService;
    public UserService(UserRepo userRepo, PurchasesRepo purchasesRepo, BudgetService budgetService) {
        this.userRepo = userRepo;
        this.purchasesRepo = purchasesRepo;
        this.budgetService = budgetService;
    }

    public boolean createUser(User user, BindingResult validResult, Model model) {
        user = UserUtils.getUserOrNull(user,validResult,model);
        if (user == null)
            return false;

        userRepo.save(user);
        budgetService.createUserBudget(user.getId());
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void deleteExpenses(Long userId) {

        purchasesRepo.deleteAll(purchasesRepo.findAllByUser_id(userId));
        BudgetService.changeBalance(userId);
    }
}