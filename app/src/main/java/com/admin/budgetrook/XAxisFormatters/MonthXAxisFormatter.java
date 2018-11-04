package com.admin.budgetrook.XAxisFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class MonthXAxisFormatter implements IAxisValueFormatter {
    private String[] symbols;

    public MonthXAxisFormatter(Locale locale) {
        this.symbols = DateFormatSymbols.getInstance(locale).getShortMonths();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return symbols[(int) value % symbols.length];
    }
}
