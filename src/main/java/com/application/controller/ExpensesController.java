package com.application.controller;

import com.application.entity.Purchase;
import com.application.entity.Type;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.service.PurchaseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    final static Logger logger = Logger.getLogger(ExpensesController.class);
    private static BindingResult errors;
    @Autowired
    private PurchasesRepo purchasesRepo;

    @Autowired
    private PurchaseService purchasesService;

    @GetMapping("/expenses")
    public String getExpensesPageInfo(Purchase purchase,
                                      @AuthenticationPrincipal User user,
                                      Model model,
                                      @RequestParam(required = false) Type type,
                                      @PageableDefault(sort = {"dateAdded"},
                                              direction = Sort.Direction.DESC) Pageable pageable
    ) {

        model = purchasesService.getPurchases(user.getId(), model, pageable, type);
        return "expenses";
    }

    @GetMapping("/editExpenses/{id}")
    public String editExpenses(@AuthenticationPrincipal User user,
                               @Valid Purchase purchase,
                               BindingResult validResult,
                               @PathVariable Integer id) {

        logger.info("comeToEditExpenses: " + purchase.toString());
        purchasesService.editPurchase(id, purchase, user.getId(), validResult);
        return "redirect:/expenses";
    }


    @PostMapping("/expenses/{id}")
    public String deleteExpenses(@PathVariable Integer id) {

        purchasesService.deletePurchase(id);
        return "redirect:/expenses";
    }

    @PostMapping("/createExpenses")
    public String createExpenses(@Valid Purchase purchase,
                                 BindingResult validResult,
                                 @RequestParam String date,
                                 @AuthenticationPrincipal User user) {

        purchasesService.createPurchase(user.getId(), purchase, date,validResult);
        return "redirect:/expenses";
    }

}
