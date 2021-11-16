package com.application.service;

import com.application.entity.Counter;
import com.application.entity.Purchase;
import com.application.entity.Type;
import com.application.entity.User;
import com.application.repository.PurchasesRepo;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
// PurchaseService
public class PurchasesService {

    final static Logger logger = Logger.getLogger(PurchasesService.class);
    private final PurchasesRepo purchasesRepo;
    private Type fitterByType;
    private Model model;
    private Double budgetBeforeSaving;

    public PurchasesService(PurchasesRepo purchasesRepo) {
        this.purchasesRepo = purchasesRepo;
    }

    // отету хуйню логически разбить на подчасти и вынести в отдельные методы
    // PurchaseModelService создать и засунуть туда вввсе методы
    //      касающиеся создания и наполнения модели пурчейза.
    //    А лучше даже PurchaseUtils и туда засунуть все статические методы которые
    //    засовывают, проверяют и валидируют пурчейз
    private void addPaginationInfoToModel(User user, Pageable pageable) {

        List<Integer> pageNumbersToShow = new ArrayList<>();
        List<Integer> pageNumbersToHide = new ArrayList<>();
        Page<Purchase> page = getPurchases(user, pageable);
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
        model.addAttribute("pagesMustBeConvert", pageNumbersToHide);
        model.addAttribute("pagesMustBeShow", pageNumbersToShow);
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

    private Page<Purchase> getPurchases(User user, Pageable pageable) {
        if (fitterByType == null) {
            return purchasesRepo.findAllByUser_id(user.getId(), pageable);
        } else {
            return purchasesRepo.findAllByUser_idAndType(user.getId(), fitterByType, pageable);
        }
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

    private void addDecimalFormatToModel() {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        model.addAttribute("decimalFormat", format);
    }

    private Double getPurchasesValue(User user) {
        Double purchasesValue = 0.0;
        for (Purchase purchase : purchasesRepo.findAllByUser_id(user.getId())) {
            purchasesValue += purchase.getAmount();
        }
        return purchasesValue;
    }

    private void warnIfLowBudget(User user) {
        if (budgetBeforeSaving != null &&
                (user.getBudget() - getPurchasesValue(user)) < user.getBudget() * 0.1) {
            model.addAttribute("lowBudget", true);
            budgetBeforeSaving = null;
            logger.info("lowBudget = true");
        }
    }

    // cначала публичные функции потом протектед потом приватные
    // переделать этот и другие методы чтоб они принимали юзерайди а неюзера целиком, он тут не нужен
    public Model getPurchases(User user, Model model, Pageable pageable, Type fitterByType) {
        this.model = model;
        this.fitterByType = fitterByType;
        // этот
        addPaginationInfoToModel(user, pageable);
        addPurchasesToModel(user, pageable);
        addTypesToModel();
        addBudgetToModel(user);
        addDateFormatToModel();
        addTodayDateToModel();
        addInputDateFormatToModel();
        addDecimalFormatToModel();
        warnIfLowBudget(user);

        return this.model;
    }

    public void editPurchase(Integer purchaseId, Purchase newPurchaseInfo) {
        Optional<Purchase> purchaseOptional = purchasesRepo.findById(purchaseId);
        if (purchaseOptional.isEmpty()) return;

        Purchase oldPurchase = purchaseOptional.get();

        if (oldPurchase.getUser().getBudget() - getPurchasesValue(oldPurchase.getUser())
                >= oldPurchase.getUser().getBudget()*0.1){
            budgetBeforeSaving = getPurchasesValue(oldPurchase.getUser());
        }

        oldPurchase.setAmount(newPurchaseInfo.getAmount());
        oldPurchase.setType(newPurchaseInfo.getType());
        purchasesRepo.save(oldPurchase);
    }
    public void deletePurchase(Integer id) {
        purchasesRepo.delete(purchasesRepo.findById(id).get());
    }

    public void createPurchase(User user, Purchase purchase,String  date) {
        try {
            purchase.setDateAdded(getInputDateFormat().parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (user.getBudget() - getPurchasesValue(user)
                >= user.getBudget()*0.1){
            budgetBeforeSaving = getPurchasesValue(user);
        }
        purchase.setUser(user);
        logger.info("Create:" + purchase);
        purchasesRepo.save(purchase);
    }
}