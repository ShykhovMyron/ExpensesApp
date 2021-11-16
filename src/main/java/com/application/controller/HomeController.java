package com.application.controller;

import com.application.entity.User;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class HomeController {
    final static Logger logger = Logger.getLogger(HomeController.class);
    private static BindingResult errors;

    @Autowired
    private UserService userService;

    @GetMapping({"", "home"})
    public String getBudgetInfo(@AuthenticationPrincipal User user,
                                User expectedUser,
                                Model model) {
        logger.info("Come to main-page: " + user.toString());
        model = userService.getBudgetInfo(user, model);
        if (errors != null) {
            model.addAttribute("errors", errors.getFieldErrors());
            errors = null;
        }
        logger.info(model.toString());
        return "home";
    }

    // експенсесконтроллер, и переименоват этот на баджетконтроллер который будет вызыватьбаджетсервис
    @PostMapping("deleteExpenses")
    public String deleteExpenses(@AuthenticationPrincipal User user) {
        userService.deleteExpenses(user);
        return "redirect:/home";
    }

    @PostMapping("changeBudget")
    public String changeBudget(@AuthenticationPrincipal User user,
                               @Valid User expectedUser,
                               BindingResult result,
                               @RequestParam(required = false) Double budget) {
        if (result.hasErrors()) {
            logger.info("Error  create:" + expectedUser.toString());
            errors = result;
            return "redirect:/home";
        }
        user.setBudget(budget);
        return "redirect:/home";
    }
}