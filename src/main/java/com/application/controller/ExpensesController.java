package com.application.controller;

import com.application.config.ExpensesConfig;
import com.application.exeptions.TypeNotFoundException;
import com.application.exeptions.ValidException;
import com.application.model.entity.Expense;
import com.application.model.entity.ExpenseType;
import com.application.model.entity.User;
import com.application.model.requests.CreateExpenseRequest;
import com.application.model.requests.CreateExpenseTypeRequest;
import com.application.model.requests.EditExpenseRequest;
import com.application.service.ExpenseTypeService;
import com.application.service.ExpensesService;
import com.application.service.UserService;
import com.application.service.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import static com.application.utils.ExpensesPaginationUtils.addPaginationInfoToModel;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {
    private final ExpensesService expensesService;
    private final ExpenseTypeService expenseTypeService;
    private final UserService userService;
    private final WalletService walletService;
    private final ExpensesConfig expensesConfig;

    public ExpensesController(ExpensesService expensesService, ExpenseTypeService expenseTypeService, UserService userService,
                              WalletService walletService, ExpensesConfig expensesConfig) {
        this.expensesService = expensesService;
        this.expenseTypeService = expenseTypeService;
        this.userService = userService;
        this.walletService = walletService;
        this.expensesConfig = expensesConfig;
    }

    @GetMapping("")
    public String expenses(@AuthenticationPrincipal User user,
                           Model model,
                           @PageableDefault(sort = {"dateAdded"},
                                   direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Expense> expenses = expensesService.getExpenses(user.getId(), pageable);
        Set<ExpenseType> expenseTypes = walletService.getWallet(user.getId()).getTypes();

        model.addAttribute("expenses", expenses);
        model.addAttribute("types", expenseTypes);
        addPaginationInfoToModel(pageable, model, expenses, expensesConfig.getPagesToShow());
        model.addAttribute("dateFormat", new SimpleDateFormat("E, LLLL d, yyyy", Locale.ENGLISH));
        model.addAttribute("todayDate", new Date()); // currentDate
        model.addAttribute("inputModalFormat", new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH));
        // TODO где-то добавить проверку *lowBudget* (потом)
        return "expenses";
    }

    @GetMapping("/edit/{expenseId}")
    public String editExpense(@AuthenticationPrincipal User user,
                              @Valid EditExpenseRequest expense,
                              BindingResult validResult,
                              @PathVariable Long expenseId) {
        try {
            if (validResult.hasErrors()) {
                throw new ValidException();
            }
            if (!expenseTypeService.isUserHaveThisType(user.getId(), expense.getType())) {
                throw new TypeNotFoundException();
            }

            expensesService.editExpense(expenseId, expense.getAmount(), expense.getType());
            walletService.recalculateBalance(user.getId());
        } catch (Exception e) {

        }
        return "redirect:/expenses";
    }


    @PostMapping("/delete/{expenseId}")
    public String deleteExpense(@AuthenticationPrincipal User user,
                                @PathVariable Long expenseId) {
        try {
            expensesService.deleteExpense(expenseId);
            walletService.recalculateBalance(user.getId());
        } catch (Exception e) {

        }
        return "redirect:/expenses";
    }

    @PostMapping("/create")
    public String createExpense(@AuthenticationPrincipal User user,
                                @Valid CreateExpenseRequest expense,
                                BindingResult validResult) {
        try {
            if (validResult.hasErrors()) {
                throw new ValidException();
            }
            if (!expenseTypeService.isUserHaveThisType(user.getId(), expense.getType())) {
                throw new TypeNotFoundException();
            }

            expensesService.createExpense(user.getId(), expense.getAmount(), expense.getType(), expense.getDateAdded());
            walletService.recalculateBalance(user.getId());
        } catch (Exception e) {

        }
        return "redirect:/expenses";
    }

    @PostMapping("/delete")
    public String deleteAllExpenses(@AuthenticationPrincipal User user) {
        try {
            userService.deleteExpenses(user.getId());
            walletService.recalculateBalance(user.getId());
        } catch (Exception e) {

        }
        return "redirect:/home";
    }

    @PostMapping("/create/type")
    public String createExpenseType(@AuthenticationPrincipal User user,
                                    @Valid CreateExpenseTypeRequest expenseType,
                                    BindingResult validResult) {
        try {
            if (validResult.hasErrors()) {
                throw new ValidException();
            }
            expenseTypeService.createExpenseType(user.getId(), expenseType.getType());
        } catch (Exception e) {

        }
        return "redirect:/expenses";
    }
}
