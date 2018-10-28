package com.admin.budgetrook.XAxisFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateXAxisFormatter implements IAxisValueFormatter {
    private SimpleDateFormat dateFormat;

    public DateXAxisFormatter() {
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date date = new Date();
        date.setTime((long)value);
        return dateFormat.format(date);
    }
}
