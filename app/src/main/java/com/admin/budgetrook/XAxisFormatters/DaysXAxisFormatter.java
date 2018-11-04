package com.admin.budgetrook.XAxisFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class DaysXAxisFormatter implements IAxisValueFormatter {
    private static final String TAG = "BUDGETROOK";
    private String[] symbols;

    public DaysXAxisFormatter(Locale locale) {
        this.symbols = DateFormatSymbols.getInstance(locale).getShortWeekdays();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return symbols[(int) value % symbols.length];
    }
}
