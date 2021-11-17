package com.application.utils;

import com.application.entity.Purchase;
import com.application.entity.PurchaseNumberOnPage;
import com.application.entity.Type;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PurchaseUtils {
    final static Logger logger = Logger.getLogger(PurchaseUtils.class);

    public static Model modelErrors = new ExtendedModelMap();

    private static Double budgetBeforeSaving;

    private static UserRepo userRepo;
    private static PurchasesRepo purchasesRepo;

    public PurchaseUtils(PurchasesRepo purchasesRepo, UserRepo userRepo) {
        PurchaseUtils.purchasesRepo = purchasesRepo;
        PurchaseUtils.userRepo = userRepo;
    }

    public static Model addExpensesPageInfoToModel(Integer userId, Model model, Pageable pageable,
                                                   Type fitterByType, Page<Purchase> purchases) {

        model.addAttribute("purchases", purchases);

        addTypesToModel(model);
        addDateFormatToModel(model);
        addTodayDateToModel(model);
        addInputDateFormatToModel(model);
        addFormatDisplayDataOnPageToModel(model);
        warnIfLowBudget(userId, model);
        addPaginationInfoToModel(userId, pageable, model, purchases);
        checkErrorsAndAddToModel(model);

        return model;
    }

    public static Purchase getPurchaseOrNull(Integer purchaseId) {

        Optional<Purchase> purchaseOptional = purchasesRepo.findById(purchaseId);
        if (purchaseOptional.isEmpty()) {
            modelErrors.addAttribute("errors", "Nonexistent id");
            return null;
        }
        return purchaseOptional.get();
    }

    public static Purchase getOldPurchaseOrNull(Integer purchaseId, Integer userId,
                                                BindingResult validResult) {

        Optional<Purchase> purchaseOptional = purchasesRepo.findById(purchaseId);
        if (purchaseOptional.isEmpty()) {
            modelErrors.addAttribute("errors", "Nonexistent id");
            return null;
        }
        if (validResult.hasErrors()) {
            logger.info("Error edit");
            modelErrors.addAttribute("errorsValid", validResult.getFieldErrors());
            return null;
        }
        checkBudgetBeforeSaving(userRepo.getById(userId));
        return purchaseOptional.get();
    }

    public static Purchase getPurchaseWithDateOrNull(Integer userId, Purchase purchase,
                                                     String date, BindingResult validResult) {

        try {
            purchase.setDateAdded(getInputDateFormat().parse(date));
        } catch (ParseException e) {
            modelErrors.addAttribute("errors", "Incorrect date");
            return null;
        }
        if (validResult.hasErrors()) {
            logger.info("Error edit");
            modelErrors.addAttribute("errorsValid", validResult.getFieldErrors());
            return null;
        }
        checkBudgetBeforeSaving(userRepo.getById(userId));
        return purchase;
    }

    private static void checkErrorsAndAddToModel(Model model) {
        model.addAllAttributes(modelErrors.asMap());
        modelErrors = new ExtendedModelMap();
    }

    private static void addPaginationInfoToModel(Integer userId, Pageable pageable, Model model,
                                                 Page<Purchase> purchases) {
        User user = userRepo.getById(userId);

        List<Integer> pageNumbersToShow = new ArrayList<>();
        List<Integer> pageNumbersToHide = new ArrayList<>();
        Page<Purchase> page = purchases;
        int startPage;
        if (((page.getTotalPages() - 1) - pageable.getPageNumber()) < 5) {
            startPage = (page.getTotalPages() - 1) - 10;
        } else {
            startPage = Math.max((pageable.getPageNumber() - 5), 0);
        }
        for (int i = startPage; i <= startPage + 10; i++) {
            pageNumbersToShow.add(i);
        }
        if (Collections.min(pageNumbersToShow) - 1 > 0) {
            pageNumbersToShow.remove(0);
            pageNumbersToHide.add(Collections.min(pageNumbersToShow) - 1);
        }
        if (Collections.max(pageNumbersToShow) + 1 < page.getTotalPages() - 1) {
            pageNumbersToShow.remove(pageNumbersToShow.size() - 1);
            pageNumbersToHide.add(Collections.max(pageNumbersToShow) + 1);
        }
        model.addAttribute("isPrevEnabled", pageable.getPageNumber() > 0);
        model.addAttribute("isNextEnabled", pageable.getPageNumber() < page.getTotalPages() - 1);
        // Поменять имена в соответствии с новым именем переменной
        model.addAttribute("pageNumbersToHide", pageNumbersToHide);
        model.addAttribute("pageNumbersToShow", pageNumbersToShow);
        model.addAttribute("purchaseNumberOnPage", new PurchaseNumberOnPage(pageable.getPageNumber() * 10));
    }

    private static void checkBudgetBeforeSaving(User user) {
        Double tenPercentOfBudget = user.getBudget() * 0.1;
        Double balance = user.getBudget() - getPurchasesValue(user);
        if (balance >= tenPercentOfBudget) {
            budgetBeforeSaving = getPurchasesValue(user);
        }
    }

    private static Double getPurchasesValue(User user) {
        Double purchasesValue = 0.0;
        for (Purchase purchase : purchasesRepo.findAllByUser_id(user.getId())) {
            purchasesValue += purchase.getAmount();
        }
        return purchasesValue;
    }

    private static void warnIfLowBudget(Integer userId, Model model) {
        User user = userRepo.getById(userId);
        Double tenPercentOfBudget = user.getBudget() * 0.1;
        Double balance = user.getBudget() - getPurchasesValue(user);
        if (budgetBeforeSaving != null &&
                balance < tenPercentOfBudget) {
            model.addAttribute("lowBudget", true);
            budgetBeforeSaving = null;
            logger.info("lowBudget = true");
        }
    }

    private static SimpleDateFormat getInputDateFormat() {
        return new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
    }

    private static void addTypesToModel(Model model) {
        model.addAttribute("types", Type.values());
    }

    private static void addDateFormatToModel(Model model) {
        model.addAttribute("dateFormat",
                new SimpleDateFormat("E, LLLL d, yyyy", Locale.ENGLISH));
    }

    private static void addTodayDateToModel(Model model) {
        model.addAttribute("todayDate", new Date());
    }

    private static void addInputDateFormatToModel(Model model) {
        model.addAttribute("inputModalFormat", getInputDateFormat());
    }

    private static void addFormatDisplayDataOnPageToModel(Model model) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        model.addAttribute("decimalFormat", format);
    }
}
