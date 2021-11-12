package com.application.controller;

import com.application.entity.User;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    final static Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @GetMapping({"", "home"})
    public String getBudgetInfo(@AuthenticationPrincipal User user, Model model) {
        logger.info("Come to main-page: " + user.toString());
        model = userService.getBudgetInfo(user, model);
        return "home";
    }

    @PostMapping("home")
    public String putBudget(@AuthenticationPrincipal User user,
                            @RequestParam(required = false) Long budget,
                            Model model) {
        if (budget != null) {
            user.setBudget(budget);
        }
        model.addAttribute("budget", user.getBudget());
        return "home";
    }

    @PostMapping("deleteExpenses")
    private String deleteExpenses(@AuthenticationPrincipal User user){
        userService.deleteExpenses(user);
        return "redirect:/home";
    }

}
