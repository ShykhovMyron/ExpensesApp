package com.application.controller;

import com.application.entity.Counter;
import com.application.entity.Purchases;
import com.application.entity.Type;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.service.PurchasesService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String expenses(@AuthenticationPrincipal User user,
                           Model model,
                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        logger.info("come to expenses");

        Page<Purchases> page = purchasesRepo.findAllByUser_id(user.getId(), pageable);

        model.addAttribute("counter", new Counter(pageable.getPageNumber() * 10));
        model.addAttribute("purchases", page);
        model.addAttribute("budget", user.getBudget());
        return "expenses";
    }

    @PostMapping("/expenses")
    public String putBudget(@AuthenticationPrincipal User user,
                            @RequestParam(required = false) Long amount,
                            @RequestParam(defaultValue = "FOOD") Type type,
                            Model model,
                            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        logger.info(amount + "  " + type.toString());
        Purchases purchases = new Purchases(amount, type, user);
        purchasesRepo.save(purchases);
        Page<Purchases> page = purchasesRepo.findAllByUser_id(user.getId(), pageable);

        model.addAttribute("counter", new Counter(pageable.getPageNumber() * 10));
        model.addAttribute("purchases", page);
        model.addAttribute("budget", user.getBudget());
        return "expenses";
    }
}