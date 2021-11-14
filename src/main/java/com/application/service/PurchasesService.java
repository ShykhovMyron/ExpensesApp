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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PurchasesService {

    final static Logger logger = Logger.getLogger(PurchasesService.class);

    private Type fitterByType;
    private Model model;

    @Autowired
    private PurchasesRepo purchasesRepo;

    public void addPaginationInfoToModel(User user, Pageable pageable) {

        List<Integer> pagesMustBeShowList = new ArrayList<>();
        List<Integer> pagesMustBeConvert = new ArrayList<>();
        Page<Purchases> page = getPurchases(user, pageable);
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
        model.addAttribute("purchases", getPurchases(user, pageable));
    }


    private void addTypesToModel() {
        model.addAttribute("types", Type.values());
    }

    private void addBudgetToModel(User user) {
        model.addAttribute("budget", user.getBudget());
    }

    private Page<Purchases> getPurchases(User user, Pageable pageable) {
        if (fitterByType == null)
            return purchasesRepo.findAllByUser_id(user.getId(), pageable);
        else
            return purchasesRepo.findAllByUser_idAndType(user.getId(), fitterByType, pageable);
    }

    private void addDateFormatToModel() {
        model.addAttribute("dateFormat", new SimpleDateFormat("E, LLLL d, yyyy", Locale.ENGLISH));
    }

    private void addInputDateFormatToModel() {
        model.addAttribute("inputModalFormat", getInputDateFormat());
    }

    private SimpleDateFormat getInputDateFormat() {
        return new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
    }

    private void addTodayDateToModel() {
        model.addAttribute("todayDate", new Date());
    }

    public Model getPurchases(User user, Model model, Pageable pageable, Type fitterByType) {
        this.model = model;
        this.fitterByType = fitterByType;
        addPaginationInfoToModel(user, pageable);
        addPurchasesToModel(user, pageable);
        addTypesToModel();
        addBudgetToModel(user);
        addDateFormatToModel();
        addTodayDateToModel();
        addInputDateFormatToModel();
        return this.model;
    }

    public void savePurchase(Integer id, Purchases purchases) {
        Purchases purchase = purchasesRepo.findById(id).get();
        purchase.setAmount(purchases.getAmount());
        purchase.setType(purchases.getType());
        logger.info("Edit:" + purchasesRepo.findById(id).get() +
                "\n\t to:" + purchase);
        purchasesRepo.save(purchase);

    }

    public void deletePurchase(Integer id) {
        purchasesRepo.delete(purchasesRepo.findById(id).get());
    }

    public void createPurchase(User user, Purchases purchases, String date) {
        purchases.setUser(user);
        try {
            purchases.setDateAdded(getInputDateFormat().parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("Create:" + purchases.toString());
        purchasesRepo.save(purchases);
    }
}
