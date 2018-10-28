package com.admin.budgetrook.XAxisFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class MonthXAxisFormatter implements IAxisValueFormatter {

    private Locale locale;

    public MonthXAxisFormatter(Locale locale){
        this.locale = locale;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return DateFormatSymbols.getInstance(locale).getMonths()[(int)value].toUpperCase().substring(0, 3);
    }
}
