package com.application.utils;

import com.application.entity.User;
import com.application.repository.UserRepo;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Component
public class UserUtils {
    private static UserRepo userRepo;

    public UserUtils(UserRepo userRepo) {
        UserUtils.userRepo = userRepo;
    }

    public static User getUserOrNull(User user, BindingResult validResult, Model model) {
        checkValidErrorsAndAddToModel(validResult, model);
        checkAlreadyExistErrorAndAddToModel(user, model);

        if (model.containsAttribute("message")) return null;
        else return user;
    }

    private static void checkAlreadyExistErrorAndAddToModel(User user, Model model) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("message", "User already exist!");
        }
    }

    private static void checkValidErrorsAndAddToModel(BindingResult validResult, Model model) {
        if (validResult.hasErrors()) {
            model.addAttribute("message", "Incorrect username or password");
        }
    }
}
