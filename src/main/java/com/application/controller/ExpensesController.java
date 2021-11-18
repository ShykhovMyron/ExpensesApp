package com.application.controller;

import com.application.entity.Purchase;
import com.application.entity.Type;
import com.application.entity.User;
import com.application.service.PurchaseService;
import com.application.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@Transactional
public class ExpensesController {
    private final PurchaseService purchasesService;
    private final UserService userService;

    public ExpensesController(PurchaseService purchasesService, UserService userService) {
        this.purchasesService = purchasesService;
        this.userService = userService;
    }

    @GetMapping("/expenses")
    public String getExpensesPageInfo(Purchase purchase,
                                      @AuthenticationPrincipal User user,
                                      Model model,
                                      @RequestParam(required = false) Type type,
                                      @PageableDefault(sort = {"dateAdded"},
                                              direction = Sort.Direction.DESC) Pageable pageable
    ) {

        purchasesService.getExpensesPageInfo(user.getId(), model, pageable, type);
        return "expenses";
    }

    @GetMapping("/editExpenses/{id}")
    public String editExpenses(@AuthenticationPrincipal User user,
                               @Valid Purchase purchase,
                               BindingResult validResult,
                               @PathVariable Integer id) {

        purchasesService.editPurchase(id, purchase, user.getId(), validResult);
        return "redirect:/expenses";
    }


    @PostMapping("/expenses/{id}")
    public String deleteExpenses(@PathVariable Integer id) {

        purchasesService.deletePurchase(id);
        return "redirect:/expenses";
    }

    @PostMapping("/createExpenses")
    public String createExpenses(@AuthenticationPrincipal User user,
                                 @Valid Purchase purchase,
                                 BindingResult validResult,
                                 @RequestParam String date) {

        purchasesService.createPurchase(user.getId(), purchase, date, validResult);
        return "redirect:/expenses";
    }

    @PostMapping("/deleteExpenses")
    public String deleteExpenses(@AuthenticationPrincipal User user) {
        userService.deleteExpenses(user);
        return "redirect:/home";
    }
}
