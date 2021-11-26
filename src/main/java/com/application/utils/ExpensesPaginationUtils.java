package com.application.utils;

import com.application.model.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpensesPaginationUtils {
    public static void addPaginationInfoToModel(Pageable pageable, Model model,
                                                Page<Expense> expenses, int pagesToShow) {

        model.addAttribute("isPrevEnabled", pageable.getPageNumber() > 0);
        model.addAttribute("isNextEnabled", pageable.getPageNumber() < expenses.getTotalPages() - 1);
        model.addAttribute("pageNumbersToShow", getPageNumbersToShow(expenses, pageable, pagesToShow));
        model.addAttribute("pageNumbersToHide", getPageNumbersToHide(expenses, pageable, pagesToShow));
        model.addAttribute("firstExpenseNumberOnPage", getFirstExpenseNumberOnPage(pageable));
    }

    private static AtomicInteger getFirstExpenseNumberOnPage(Pageable pageable) {
        int expensesOnPage = pageable.getPageSize();
        return new AtomicInteger(pageable.getPageNumber() * expensesOnPage);
    }

    private static List<Integer> getPageNumbersToHide(Page<Expense> expenses, Pageable pageable, int pagesToShow) {
        List<Integer> pageNumbersToShow = getPageNumbersToShow(expenses, pageable, pagesToShow);
        List<Integer> pageNumbersToHide = new ArrayList<>();

        if (Collections.min(pageNumbersToShow) - 1 > 0) {
            pageNumbersToHide.add(Collections.min(pageNumbersToShow) - 1);
        }
        if (Collections.max(pageNumbersToShow) + 1 < expenses.getTotalPages() - 1) {
            pageNumbersToHide.add(Collections.max(pageNumbersToShow) + 1);
        }
        return pageNumbersToHide;
    }

    private static List<Integer> getPageNumbersToShow(Page<Expense> expenses, Pageable pageable, int pagesToShow) {
        int firstExpenseNumberMustBeShow = getFirstPageNumberMustBeShow(expenses, pageable, pagesToShow);

        List<Integer> pageNumbersToShow = new ArrayList<>();
        for (int i = firstExpenseNumberMustBeShow; i <= firstExpenseNumberMustBeShow + pagesToShow; i++) {
            pageNumbersToShow.add(i);
        }
        return pageNumbersToShow;
    }

    private static int getFirstPageNumberMustBeShow(Page<Expense> expenses, Pageable pageable, int pagesToShow) {
        if (((expenses.getTotalPages() - 1) - pageable.getPageNumber()) < 5) {
            return (expenses.getTotalPages() - 1) - pagesToShow;
        } else {
            return Math.max((pageable.getPageNumber() - 5), 0);
        }
    }
}
