package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.admin.budgetrook.R;
import com.admin.budgetrook.XAxisFormatters.DaysXAxisFormatter;
import com.admin.budgetrook.XAxisFormatters.MonthXAxisFormatter;
import com.admin.budgetrook.XAxisFormatters.YearsXAxisFormatter;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class BarChartFragment extends Fragment {
    private List<CategoriesAndExpenses> chartData;
    private ChartFragmentInterface mListener;

    private BarChart chart;
    private Spinner periodSpinner;

    private PeriodSetting setting = PeriodSetting.DAYS;

    public BarChartFragment() {
        // Required empty public constructor
    }

    public static BarChartFragment newInstance() {
        BarChartFragment fragment = new BarChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        chart = (BarChart) v.findViewById(R.id.bar_chart_fragment_chart);
        periodSpinner = (Spinner) v.findViewById(R.id.bar_fragment_period_sp);
        setup();
        setupSpinner();
        return v;
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_item, getPeriodStrings());
        periodSpinner.setAdapter(adapter);
        periodSpinner.setSelection(0);

        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setting = PeriodSetting.values()[position];
                List<BarEntry> entries = convertDataBySetting(chartData, setting);
                BarDataSet dataSet = new BarDataSet(entries, "Expenses");
                BarData barData = new BarData(dataSet);
                chart.getXAxis().setValueFormatter(getFormatterBySetting(setting));
                chart.setData(barData);
                chart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setup() {
        if (mListener != null) {
            chartData = mListener.getData();
            List<BarEntry> entries = convertDataBySetting(chartData, setting);
            BarDataSet dataSet = new BarDataSet(entries, "Expenses");
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.9f); // set custom bar width
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.getXAxis().setValueFormatter(getFormatterBySetting(setting));
            chart.getXAxis().setGranularity(0);
            chart.setData(barData);
            chart.setDescription(null);
            chart.getLegend().setEnabled(false);
            chart.invalidate();
        }
    }

    private List<BarEntry> convertDataBySetting(List<CategoriesAndExpenses> rawData, PeriodSetting setting) {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        HashMap<Integer, Long> data = new HashMap<Integer, Long>();
        for (CategoriesAndExpenses item : rawData) {
            List<ExpenseEntity> expenses = item.getExpenses();
            for (ExpenseEntity expense : expenses) {
                int key = getKeyBySetting(expense, setting);
                Long amount = expense.getAmount();
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

    private int getKeyBySetting(ExpenseEntity expense, PeriodSetting setting) {
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

    private IAxisValueFormatter getFormatterBySetting(PeriodSetting setting){
        Locale locale = getResources().getConfiguration().locale;
        switch (setting) {
            case DAYS: {
                return new DaysXAxisFormatter(locale);
            }
            case MONTHS: {
                return new MonthXAxisFormatter(locale);
            }
            case YEARS: {
                return new YearsXAxisFormatter();
            }
            default: {
                return new DaysXAxisFormatter(locale);
            }
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof ChartFragmentInterface) {
            mListener = (ChartFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChartFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private enum PeriodSetting {
        DAYS,
        MONTHS,
        YEARS
    }

    private List<String> getPeriodStrings(){
        List<String> result = new ArrayList<String>();
        PeriodSetting[] vals = PeriodSetting.values();
        for(PeriodSetting item : vals){
            result.add(item.toString());
        }
        return result;
    }
}
