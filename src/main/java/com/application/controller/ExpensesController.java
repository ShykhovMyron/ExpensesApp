package com.application.controller;

import com.application.entity.Type;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.service.PurchasesService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

@Controller
@Transactional
public class ExpensesController {
    final static Logger logger = Logger.getLogger(ExpensesController.class);

    @Autowired
    private PurchasesRepo purchasesRepo;

    @Autowired
    private PurchasesService purchasesService;

    @GetMapping("/expenses")
    public String getPurchases(@AuthenticationPrincipal User user,
                              Model model,
                              @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        logger.info("come to expenses ");
        model = purchasesService.getPurchases(user, model, pageable);
        return "expenses";
    }

    @GetMapping("/editExpenses/{id}")
    public String editPurchase(@PathVariable Integer id,
                               @RequestParam(defaultValue = "0") Long amount,
                               @RequestParam Type type) {

        logger.info("get: /expenses/" + id + " amount:" + amount + " type" + type);

        purchasesService.savePurchase(id, amount, type);
        return "redirect:/expenses";
    }


    @PostMapping("/expenses/{id}")
    public String deletePurchase(@PathVariable Integer id) {
        logger.info("Delete:" + purchasesRepo.findById(id).get());

        purchasesService.deletePurchase(id);
        return "redirect:/expenses";
    }

    @PostMapping("/createExpenses")
    public String createExpenses(@AuthenticationPrincipal User user,
                                 @RequestParam Long amount,
                                 @RequestParam Type type) {
        purchasesService.createPurchase(user, amount, type);
        return "redirect:/expenses";
    }
}
