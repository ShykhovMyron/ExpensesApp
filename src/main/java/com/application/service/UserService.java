package com.application.service;

import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import com.application.utils.UserUtils;
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

    public UserService(UserRepo userRepo, PurchasesRepo purchasesRepo) {
        this.userRepo = userRepo;
        this.purchasesRepo = purchasesRepo;
    }

    public boolean createUser(User user, BindingResult validResult, Model model) {
        user = UserUtils.getUserOrNull(user,validResult,model);
        if (user == null)
            return false;

        userRepo.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void deleteExpenses(User user) {
        purchasesRepo.deleteAll(purchasesRepo.findAllByUser_id(user.getId()));
    }
}