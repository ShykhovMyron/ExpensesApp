package com.application.service;

import com.application.entity.Purchase;
import com.application.entity.Role;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import com.application.utils.PurchaseUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PurchasesRepo purchasesRepo;

    public UserService(UserRepo userRepo, PurchasesRepo purchasesRepo) {
        this.userRepo = userRepo;
        this.purchasesRepo = purchasesRepo;
    }

    private void addDecimalFormatToModel(Model model) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        model.addAttribute("decimalFormat", format);
    }

    public boolean createUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }

        userRepo.save(user);
        return true;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    // в сервис который касается бюджетов который работает с новым репо бюджетов и тд
    public Model getBudgetInfo(User user, Model model) {
        Double purchaseValue = PurchaseUtils.getPurchasesValue(user);
        Double balance = user.getBudget() - purchaseValue;
        model.addAttribute("user", user);
        model.addAttribute("balance", balance);
        PurchaseUtils.addFormatDisplayDataOnPageToModel(model);
        return model;
    }

    public void deleteExpenses(User user) {
        purchasesRepo.deleteAll(purchasesRepo.findAllByUser_id(user.getId()));
    }
}