package com.application.controller;

import com.application.exeptions.ExpenseNotFoundException;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.requests.CreateExpenseRequest;
import com.application.model.requests.CreateExpenseTypeRequest;
import com.application.model.requests.DeleteExpenseTypeRequest;
import com.application.model.requests.EditExpenseRequest;
import com.application.service.ExpensesService;
import com.application.service.WalletService;
import org.apache.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/expenses")
public class ExpensesModalController {
    final static Logger logger = Logger.getLogger(ExpensesModalController.class);

    private final ExpensesService expensesService;
    private final WalletService walletService;

    public ExpensesModalController(ExpensesService expensesService, WalletService walletService) {
        this.expensesService = expensesService;
        this.walletService = walletService;
    }

    @GetMapping("/edit/{expenseId}")
    public String editExpenseModel(@AuthenticationPrincipal User user,
                                   EditExpenseRequest expense,
                                   @PathVariable Long expenseId,
                                   Model model) {
        try {
            Set<ExpenseType> expenseTypes = walletService.getWallet(user.getId()).getTypes();

            model.addAttribute("expense", expensesService.getExpense(expenseId));
            logger.info(expensesService.getExpense(expenseId).getDateAdded());
            model.addAttribute("types", expenseTypes);
            model.addAttribute("inputModalFormat", new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH));

        } catch (Exception e) {

        }
        return "modals/EditExpenseModalBody";
    }

    @GetMapping("/create/expense")
    public String createExpenseModel(@AuthenticationPrincipal User user,
                                     CreateExpenseRequest expense,
                                     Model model) throws ExpenseNotFoundException {
        Set<ExpenseType> expenseTypes = walletService.getWallet(user.getId()).getTypes();

        model.addAttribute("expense", expensesService.getExpense(42L));
        model.addAttribute("types", expenseTypes);
        model.addAttribute("currentDate", new Date());
        model.addAttribute("inputModalFormat", new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH));

        return "modals/CreateExpenseModalBody";
    }

    @GetMapping("/create/type")
    public String createExpenseTypeModel(CreateExpenseTypeRequest expenseType,
                                         Model model) {
        return "modals/CreateExpenseTypeModalBody";
    }

    @GetMapping("/delete/type")
    public String deleteExpenseTypeModel(@AuthenticationPrincipal User user,
                                         DeleteExpenseTypeRequest expenseType,
                                         Model model) {
        Set<ExpenseType> expenseTypes = walletService.getWallet(user.getId()).getTypes();

        model.addAttribute("types", expenseTypes);
        return "modals/DeleteExpenseTypeModalBody";
    }
}
