package com.application.controller;

import com.application.entity.Purchases;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
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

import javax.transaction.Transactional;

@Controller
@Transactional
public class ExpensesController {
    final static Logger logger = Logger.getLogger(ExpensesController.class);

    @Autowired
    private PurchasesRepo purchasesRepo;

    @GetMapping("/expenses")
    public String expenses(@AuthenticationPrincipal User user,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Purchases> page;
        page = purchasesRepo.findAllByUser_id(user.getId(), pageable);

        model.addAttribute("purchases", page);
        return "expenses";
    }
}
