package com.admin.budgetrook.XAxisFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IndexedDatesXAxisFormatter implements IAxisValueFormatter {
    private List<Date> dates;
    private SimpleDateFormat dateFormat;

    public IndexedDatesXAxisFormatter(List<Date> dates, SimpleDateFormat dateFormat){
        this.dates = dates;
        this.dateFormat = dateFormat;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return dateFormat.format(dates.get(new Float(value).intValue() % dates.size()));
    }
}
