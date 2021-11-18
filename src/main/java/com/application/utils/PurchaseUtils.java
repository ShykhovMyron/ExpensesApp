package com.application.utils;

import com.application.entity.*;
import com.application.repository.PurchasesRepo;
import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PurchaseUtils {
    final static Logger logger = Logger.getLogger(PurchaseUtils.class);

    public static Model modelErrors = new ExtendedModelMap();

    private static BigDecimal balanseBeforeSaving;

    private static WalletRepo walletRepo;
    private static UserRepo userRepo;
    private static PurchasesRepo purchasesRepo;

    public PurchaseUtils(PurchasesRepo purchasesRepo, UserRepo userRepo, WalletRepo walletRepo) {
        PurchaseUtils.purchasesRepo = purchasesRepo;
        PurchaseUtils.userRepo = userRepo;
        PurchaseUtils.walletRepo = walletRepo;
    }

    public static void addExpensesPageInfoToModel(Integer userId, Model model, Pageable pageable,
                                                  Page<Purchase> purchases) {

        model.addAttribute("purchases", purchases);

        addTypesToModel(model);
        addDateFormatToModel(model);
        addTodayDateToModel(model);
        addInputDateFormatToModel(model);
        addPaginationInfoToModel(pageable, model, purchases);
        checkErrorsAndAddToModel(model);
        warnIfLowBudget(userId, model);

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
            modelErrors.addAttribute("errorsValid", validResult.getFieldErrors());
            return null;
        }
        checkBudgetBeforeSaving(userId);
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
            modelErrors.addAttribute("errorsValid", validResult.getFieldErrors());
            return null;
        }
        checkBudgetBeforeSaving(userId);
        return purchase;
    }

    private static void checkErrorsAndAddToModel(Model model) {
        model.addAllAttributes(modelErrors.asMap());
        modelErrors = new ExtendedModelMap();
    }

    private static void addPaginationInfoToModel(Pageable pageable, Model model,
                                                 Page<Purchase> purchases) {

        List<Integer> pageNumbersToShow = new ArrayList<>();
        List<Integer> pageNumbersToHide = new ArrayList<>();
        int startPage;
        if (((purchases.getTotalPages() - 1) - pageable.getPageNumber()) < 5) {
            startPage = (purchases.getTotalPages() - 1) - 10;
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
        if (Collections.max(pageNumbersToShow) + 1 < purchases.getTotalPages() - 1) {
            pageNumbersToShow.remove(pageNumbersToShow.size() - 1);
            pageNumbersToHide.add(Collections.max(pageNumbersToShow) + 1);
        }
        model.addAttribute("isPrevEnabled", pageable.getPageNumber() > 0);
        model.addAttribute("isNextEnabled", pageable.getPageNumber() < purchases.getTotalPages() - 1);
        // Поменять имена в соответствии с новым именем переменной
        model.addAttribute("pageNumbersToHide", pageNumbersToHide);
        model.addAttribute("pageNumbersToShow", pageNumbersToShow);
        model.addAttribute("purchaseNumberOnPage", new PurchaseNumberOnPage(pageable.getPageNumber() * 10));
    }

    private static void checkBudgetBeforeSaving(Integer userId) {
        Wallet userWallet = walletRepo.findByUserId(userId);
        BigDecimal tenPercentOfBudget = userWallet.getBudget().multiply(BigDecimal.valueOf(0.1));

        if (userWallet.getBalance().compareTo(tenPercentOfBudget) >= 0) {
            balanseBeforeSaving = userWallet.getBudget();
        }
    }

    public static BigDecimal getPurchasesValue(Integer userId) {
        BigDecimal purchasesValue = new BigDecimal(0);
        for (Purchase purchase : purchasesRepo.findAllByUser_id(userId)) {
            purchasesValue = purchasesValue.add(purchase.getAmount());
        }
        return purchasesValue;
    }

    private static void warnIfLowBudget(Integer userId, Model model) {
        Wallet userWallet = walletRepo.findByUserId(userId);
        BigDecimal tenPercentOfBudget = userWallet.getBudget().multiply(BigDecimal.valueOf(0.1));
        if (balanseBeforeSaving != null &&
                userWallet.getBalance().compareTo(tenPercentOfBudget) < 0) {
            model.addAttribute("lowBudget", true);
            balanseBeforeSaving = null;
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

    public static void addFormatDisplayDataOnPageToModel(Model model) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        model.addAttribute("decimalFormat", format);
    }
}
