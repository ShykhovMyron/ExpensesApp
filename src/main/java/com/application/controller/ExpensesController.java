package com.application.controller;

import com.application.entity.Purchase;
import com.application.entity.PurchaseType;
import com.application.entity.User;
import com.application.service.PurchaseService;
import com.application.service.PurchaseTypeService;
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
    private final PurchaseService purchaseService;
    private final PurchaseTypeService purchaseTypeService;
    private final UserService userService;

    public ExpensesController(PurchaseService purchaseService, PurchaseTypeService purchaseTypeService, UserService userService) {
        this.purchaseService = purchaseService;
        this.purchaseTypeService = purchaseTypeService;
        this.userService = userService;
    }

    @GetMapping("/expenses")
    public String getExpensesPageInfo(Purchase purchase,
                                      PurchaseType purchaseType,
                                      @AuthenticationPrincipal User user,
                                      Model model,
                                      @PageableDefault(sort = {"dateAdded"},
                                              direction = Sort.Direction.DESC) Pageable pageable
    ) {
        purchaseService.getExpensesPageInfo(user.getId(), model, pageable);
        return "expenses";
    }

    @GetMapping("/editExpenses/{id}")
    public String editExpenses(@AuthenticationPrincipal User user,
                               @Valid Purchase purchase,
                               BindingResult validResult,
                               @PathVariable Long id) {

        purchaseService.editPurchase(id, purchase, user.getId(), validResult);
        return "redirect:/expenses";
    }


    @PostMapping("/expenses/{id}")
    public String deleteExpenses(@AuthenticationPrincipal User user,
                                 @PathVariable Long id) {

        purchaseService.deletePurchase(id, user.getId());
        return "redirect:/expenses";
    }

    @PostMapping("/createExpenses")
    public String createExpenses(@AuthenticationPrincipal User user,
                                 @Valid Purchase purchase,
                                 BindingResult validResult,
                                 @RequestParam String date) {

        purchaseService.createPurchase(user.getId(), purchase, date, validResult);
        return "redirect:/expenses";
    }

    @PostMapping("/deleteExpenses")
    public String deleteAllExpenses(@AuthenticationPrincipal User user) {
        userService.deleteExpenses(user.getId());
        return "redirect:/home";
    }

    @PostMapping("/createPurchaseType")
    public String createPurchaseType(@AuthenticationPrincipal User user,
                                @Valid PurchaseType purchaseType,
                                BindingResult validResult) {
        purchaseTypeService.createPurchaseType(user.getId(),purchaseType,validResult);
        return "redirect:/expenses";
    }
}
