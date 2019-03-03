package com.admin.budgetrook.interfaces;

import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.wrappers.SummaryRequestWrapper;

import java.util.Date;
import java.util.List;

public interface ChartFragmentInterface {
    void getDataWithinDates(Date from, Date to);

    void getExpensesForLine();

    void getExpensesForRadar();

    void getExpensesForSummary(SummaryRequestWrapper request);

    void getSetupDataForSummary();
}
