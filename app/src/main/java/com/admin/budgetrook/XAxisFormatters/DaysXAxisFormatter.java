package com.admin.budgetrook.XAxisFormatters;

import android.content.Intent;
import android.support.v4.view.ViewConfigurationCompat;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class DaysXAxisFormatter implements IAxisValueFormatter {
    private Locale locale;

    public DaysXAxisFormatter(Locale locale){
        this.locale = locale;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return DateFormatSymbols.getInstance(locale).getWeekdays()[(int)value].toUpperCase().substring(0, 3);
    }
}
