package com.application.service;

import com.application.entity.Purchases;
import com.application.entity.Role;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PurchasesRepo purchasesRepo;

    public boolean createUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }

        user.setEnabled(true);
        user.setRole(Collections.singleton(Role.USER));
        userRepo.save(user);
        return true;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }


    public Model getBudgetInfo(User user, Model model) {
        Long costs = 0L;
        for (Purchases purchase : purchasesRepo.findAllByUser_id(user.getId())) {
            costs += purchase.getAmount();
        }
        Long balance = user.getBudget() - costs;
        model.addAttribute("user", user);
        model.addAttribute("balance", balance);
        return model;
    }

    public void deleteExpenses(User user) {
        purchasesRepo.deleteAll(purchasesRepo.findAllByUser_id(user.getId()));
    }
}
