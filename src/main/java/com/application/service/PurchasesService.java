package com.application.service;

import com.application.entity.Counter;
import com.application.entity.Purchases;
import com.application.entity.Type;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PurchasesService {

    final static Logger logger = Logger.getLogger(PurchasesService.class);


    private Model model;

    @Autowired
    private PurchasesRepo purchasesRepo;

    public void addPaginationInfoToModel(User user, Pageable pageable) {

        List<Integer> pagesMustBeShowList = new ArrayList<>();
        List<Integer> pagesMustBeConvert = new ArrayList<>();
        Page<Purchases> page = purchasesRepo.findAllByUser_id(user.getId(), pageable);
        int startPage;
        if (((page.getTotalPages() - 1) - pageable.getPageNumber()) < 5) startPage
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
        model.addAttribute("isPrevEnabled", pageable.getPageNumber() > 0);
        model.addAttribute("isNextEnabled", pageable.getPageNumber() < page.getTotalPages() - 1);
        model.addAttribute("pagesMustBeConvert", pagesMustBeConvert);
        model.addAttribute("pagesMustBeShow", pagesMustBeShowList);
        model.addAttribute("counter", new Counter(pageable.getPageNumber() * 10));
    }

    public void addPurchasesToModel(User user, Pageable pageable) {
        model.addAttribute("purchases", purchasesRepo.findAllByUser_id(user.getId(), pageable));
    }


    private void addTypesToModel() {
        model.addAttribute("types", Type.values());
    }

    private void addBudgetToModel(User user) {
        model.addAttribute("budget", user.getBudget());
    }

    public Model getPurchases(User user, Model model, Pageable pageable) {
        this.model = model;
        addPaginationInfoToModel(user, pageable);
        addPurchasesToModel(user, pageable);
        addTypesToModel();
        addBudgetToModel(user);
        return this.model;
    }

    public void savePurchase(Integer id, Long amount, Type type) {
        Purchases purchase = purchasesRepo.findById(id).get();
        purchase.setAmount(amount);
        purchase.setType(type);
        purchasesRepo.save(purchase);

        logger.info("Edit:" + purchasesRepo.findById(id).get() +
                "\n\t to:" + purchase);

    }

    public void deletePurchase(Integer id) {
        purchasesRepo.delete(purchasesRepo.findById(id).get());
    }

    public void createPurchase(User user, Purchases purchases) {
        purchases.setUser(user);
        logger.info("Create:" + purchases.toString());
        purchasesRepo.save(purchases);
    }
}
