package com.admin.budgetrook.XAxisFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.logging.SimpleFormatter;

public class YearsXAxisFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return String.valueOf((int)value);
    }
}
