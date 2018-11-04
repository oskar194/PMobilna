package com.admin.budgetrook.helpers;

import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class DateSplitHelper {
    private static final String TAG = "BUDGETROOK";

    public enum PeriodSetting {
        DAYS,
        MONTHS,
        YEARS
    }

    public static int getKeyBySetting(ExpenseEntity expense, PeriodSetting setting) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(expense.getDate());
        switch (setting) {
            case DAYS: {
                return cal.get(Calendar.DAY_OF_WEEK);
            }
            case MONTHS: {
                return cal.get(Calendar.MONTH);
            }
            case YEARS: {
                return cal.get(Calendar.YEAR);
            }
            default: {
                return cal.get(Calendar.DAY_OF_MONTH);
            }
        }
    }

    public static List<BarEntry> convertDataBySetting(List<CategoriesAndExpenses> rawData, PeriodSetting setting) {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        HashMap<Integer, Float> data = new HashMap<Integer, Float>();
        for (CategoriesAndExpenses item : rawData) {
            List<ExpenseEntity> expenses = item.getExpenses();
            for (ExpenseEntity expense : expenses) {
                int key = getKeyBySetting(expense, setting);
                float amount = expense.getAmount().floatValue();
                if (data.containsKey(key)) {
                    amount += data.get(key);
                }
                data.put(key, amount);
            }
        }
        for (Integer key : data.keySet()) {
            entries.add(new BarEntry(key, data.get(key)));
        }
        return entries;
    }

}
