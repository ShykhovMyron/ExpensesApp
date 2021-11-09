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
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                           @PageableDefault(sort = {"id"},
                                   direction = Sort.Direction.DESC) Pageable pageable
    ) {
        logger.info("come to expenses");
        Page<Purchases> page = purchasesRepo.findAllByUser_id(user.getId(), pageable);

        List<Integer> pagesMustBeShowList = new ArrayList<>();
        List<Integer> pagesMustBeConvert = new ArrayList<>();
        int startPage;
        if (((page.getTotalPages() - 1) - pageable.getPageNumber()) < 5)  startPage
                = (page.getTotalPages() - 1) - 10;
       else startPage = Math.max((pageable.getPageNumber() - 5), 0);
        for (int i = startPage; i <= startPage + 10; i++) {
            pagesMustBeShowList.add(i);
        }
        if (Collections.min(pagesMustBeShowList) - 1 > 0) {
            pagesMustBeShowList.remove(0);
            pagesMustBeConvert.add(Collections.min(pagesMustBeShowList) - 1);
        }
        if (Collections.max(pagesMustBeShowList) + 1 < page.getTotalPages() - 1) {
            pagesMustBeShowList.remove(pagesMustBeShowList.size() - 1);
            pagesMustBeConvert.add(Collections.max(pagesMustBeShowList) + 1);
        }

        model.addAttribute("types",Type.values());
        model.addAttribute("pagesMustBeConvert", pagesMustBeConvert);
        model.addAttribute("pagesMustBeShow", pagesMustBeShowList);
        model.addAttribute("isPrevEnabled", pageable.getPageNumber() > 0);
        model.addAttribute("isNextEnabled",
                pageable.getPageNumber() < page.getTotalPages() - 1);
        model.addAttribute("counter", new Counter(pageable.getPageNumber() * 10));
        model.addAttribute("purchases", page);
        model.addAttribute("budget", user.getBudget());
        return "expenses";
    }

    @GetMapping("/expenses/{id}")
    public String putBudget(@PathVariable Integer id,
                            @RequestParam(required = false) Long amount,
                            @RequestParam(required = false) Type type) {
         logger.info("get: /expenses/"+id+" amount:"+amount+" type"+type);
//        Purchases purchases = new Purchases(amount, type, user);
//        purchasesRepo.save(purchases);
//        Page<Purchases> page = purchasesRepo.findAllByUser_id(user.getId(), pageable);
//
//        model.addAttribute("counter", new Counter(pageable.getPageNumber() * 10));
//        model.addAttribute("purchases", page);
//        model.addAttribute("budget", user.getBudget());
        return "redirect:/expenses?page=0";
    }

//    @GetMapping("/expenses/{id}")
//    public String category(@AuthenticationPrincipal User user,
//                           @PathVariable Long categoryId,
//                           Model model){
//        model.addAttribute("purchase",purchasesRepo.findAllByUser_id(user.getId()));
//        return "expenses-edit";
//    }
}
