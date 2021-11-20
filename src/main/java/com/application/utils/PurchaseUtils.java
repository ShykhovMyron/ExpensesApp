package com.application.utils;

import com.application.entity.Purchase;
import com.application.pager.PurchaseNumberOnPage;
import com.application.repository.PurchaseRepo;
import com.application.repository.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.application.utils.BudgetUtils.checkBudgetBeforeSaving;
import static com.application.utils.BudgetUtils.warnIfLowBudget;

@Component
public class PurchaseUtils {
    final static Logger logger = Logger.getLogger(PurchaseUtils.class);

    public static Model modelErrors = new ExtendedModelMap();
    private static PurchaseRepo purchaseRepo;
    private static  UserRepo userRepo;
    private final static Integer pagesToShow = 10;

    public PurchaseUtils(PurchaseRepo purchaseRepo, UserRepo userRepo) {
        PurchaseUtils.purchaseRepo = purchaseRepo;
        PurchaseUtils.userRepo = userRepo;
    }

    public static void addExpensesPageInfoToModel(Long userId, Model model, Pageable pageable,
                                                  Page<Purchase> purchases) {

        model.addAttribute("purchases", purchases);

        addPurchaseTypesToModel(model,userId);
        addDateFormatToModel(model);
        addTodayDateToModel(model);
        addInputDateFormatToModel(model);
        addPaginationInfoToModel(pageable, model, purchases);
        checkErrorsAndAddToModel(model);

        warnIfLowBudget(userId, model);

    }

    public static Purchase getPurchaseOrNull(Long purchaseId) {

        Optional<Purchase> purchaseOptional = purchaseRepo.findById(purchaseId);
        if (purchaseOptional.isEmpty()) {
            modelErrors.addAttribute("errors", "Nonexistent id");
            return null;
        }
        return purchaseOptional.get();
    }

    public static Purchase getOldPurchaseOrNull(Long purchaseId, Long userId,
                                                BindingResult validResult) {

        Optional<Purchase> purchaseOptional = purchaseRepo.findById(purchaseId);
        if (purchaseOptional.isEmpty()) {
            modelErrors.addAttribute("errors", "Nonexistent id");
            return null;
        }
        if (checkValidErrors(validResult)) return null;

        checkBudgetBeforeSaving(userId);
        return purchaseOptional.get();
    }

    public static Purchase getPurchaseWithDateOrNull(Long userId, Purchase purchase,
                                                     String date, BindingResult validResult) {

        try {
            purchase.setDateAdded(getInputDateFormat().parse(date));
        } catch (ParseException e) {
            modelErrors.addAttribute("errors", "Incorrect date");
            return null;
        }

        if (checkValidErrors(validResult)) return null;

        checkBudgetBeforeSaving(userId);
        return purchase;
    }

    private static void checkErrorsAndAddToModel(Model model) {
        model.addAllAttributes(modelErrors.asMap());
        modelErrors = new ExtendedModelMap();
    }

    public static boolean checkValidErrors(BindingResult validResult){
        if (validResult.hasErrors()) {
            modelErrors.addAttribute("errorsValid", validResult.getFieldErrors());
            return true;
        }
        return false;
    }

    private static void addPaginationInfoToModel(Pageable pageable, Model model,
                                                 Page<Purchase> purchases) {

        model.addAttribute("isPrevEnabled", pageable.getPageNumber() > 0);
        model.addAttribute("isNextEnabled", pageable.getPageNumber() < purchases.getTotalPages() - 1);
        model.addAttribute("pageNumbersToShow", getPageNumbersToShow(purchases, pageable));
        model.addAttribute("pageNumbersToHide", getPageNumbersToHide(purchases, pageable));
        model.addAttribute("firstPurchaseNumberOnPage", getFirstPurchaseNumberOnPage(pageable));
    }

    private static PurchaseNumberOnPage getFirstPurchaseNumberOnPage(Pageable pageable) {
        Integer purchasesOnPage = pageable.getPageSize();
        return new PurchaseNumberOnPage(pageable.getPageNumber() * purchasesOnPage);
    }

    private static List<Integer> getPageNumbersToHide(Page<Purchase> purchases, Pageable pageable) {
        List<Integer> pageNumbersToShow = getPageNumbersToShow(purchases, pageable);
        List<Integer> pageNumbersToHide = new ArrayList<>();

        if (Collections.min(pageNumbersToShow) - 1 > 0) {
            pageNumbersToShow.remove(0);
            pageNumbersToHide.add(Collections.min(pageNumbersToShow) - 1);
        }
        if (Collections.max(pageNumbersToShow) + 1 < purchases.getTotalPages() - 1) {
            pageNumbersToShow.remove(pageNumbersToShow.size() - 1);
            pageNumbersToHide.add(Collections.max(pageNumbersToShow) + 1);
        }
        return pageNumbersToHide;
    }

    private static List<Integer> getPageNumbersToShow(Page<Purchase> purchases, Pageable pageable) {
        int firstPurchaseNumberMustBeShow = getFirstPageNumberMustBeShow(purchases, pageable);

        List<Integer> pageNumbersToShow = new ArrayList<>();
        for (int i = firstPurchaseNumberMustBeShow; i <= firstPurchaseNumberMustBeShow + pagesToShow; i++) {
            pageNumbersToShow.add(i);
        }
        return pageNumbersToShow;
    }

    private static int getFirstPageNumberMustBeShow(Page<Purchase> purchases, Pageable pageable) {
        if (((purchases.getTotalPages() - 1) - pageable.getPageNumber()) < 5) {
            return (purchases.getTotalPages() - 1) - pagesToShow;
        } else {
            return Math.max((pageable.getPageNumber() - 5), 0);
        }
    }

    public static BigDecimal getPurchasesValue(Long userId) {
        BigDecimal purchasesValue = new BigDecimal(0);
        for (Purchase purchase : purchaseRepo.findAllByUserId(userId)) {
            purchasesValue = purchasesValue.add(purchase.getAmount());
        }
        return purchasesValue;
    }

    private static SimpleDateFormat getInputDateFormat() {
        return new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
    }

    private static void addPurchaseTypesToModel(Model model, Long userId) {
        model.addAttribute("types", userRepo.getById(userId).getTypes());
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

}
