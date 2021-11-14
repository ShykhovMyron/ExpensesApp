package com.application.controller;

import com.application.entity.Purchases;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;

@Controller
@Transactional
public class ExpensesController {
    final static Logger logger = Logger.getLogger(ExpensesController.class);
    private static BindingResult errors;
    @Autowired
    private PurchasesRepo purchasesRepo;

    @Autowired
    private PurchasesService purchasesService;

    @GetMapping("/expenses")
    public String getPurchases(Purchases purchases,
                               @AuthenticationPrincipal User user,
                               Model model,
                               @RequestParam(required = false) Type type,
                               @PageableDefault(sort = {"dateAdded"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        logger.info("come to expenses ");
        model = purchasesService.getPurchases(user, model, pageable, type);
        if (errors != null) {
            model.addAttribute("errors", errors.getFieldErrors());
            errors = null;
        }
        return "expenses";
    }

    @GetMapping("/editExpenses/{id}")
    public String editPurchase(@Valid Purchases purchases,
                               BindingResult result,
                               @PathVariable Integer id) {

        logger.info("getToEdit: /expenses/" + id + " amount:" + purchases.getAmount()
                + " type" + purchases.getType());
        if (result.hasErrors()) {
            logger.info("Error  create:" + purchases.toString());
            errors = result;
            return "redirect:/expenses";
        }
        purchasesService.savePurchase(id, purchases);
        return "redirect:/expenses";
    }


    @PostMapping("/expenses/{id}")
    public String deletePurchase(@PathVariable Integer id) {
        logger.info("Delete:" + purchasesRepo.findById(id).get());

        purchasesService.deletePurchase(id);
        return "redirect:/expenses";
    }

    @PostMapping("/createExpenses")
    public String createExpenses(@Valid Purchases purchases,
                                 BindingResult result,
                                 @RequestParam String  date,
                                 @AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            logger.info("Error create:" + purchases.toString());
            errors = result;
            return "redirect:/expenses";
        }
        purchasesService.createPurchase(user, purchases,date);
        return "redirect:/expenses";
    }
}
