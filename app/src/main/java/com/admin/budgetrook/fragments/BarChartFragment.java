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
import com.admin.budgetrook.customDataSets.ColoredMaxDataSet;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.admin.budgetrook.helpers.DateSplitHelper.PeriodSetting;
import static com.admin.budgetrook.helpers.DateSplitHelper.getKeyBySetting;

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
                ColoredMaxDataSet dataSet = new ColoredMaxDataSet(entries, "Expenses");
                dataSet.setColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.maxValue));
                BarData barData = new BarData(dataSet);
                chart.getXAxis().setValueFormatter(getFormatterBySetting(setting));
                chart.setData(barData);
                chart.notifyDataSetChanged();
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
            ColoredMaxDataSet dataSet = new ColoredMaxDataSet(entries, "Expenses");
            dataSet.setColors(R.color.colorPrimary, R.color.maxValue);
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.9f); // set custom bar width
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.getXAxis().setValueFormatter(getFormatterBySetting(setting));
            chart.getXAxis().setGranularity(0);
            chart.setData(barData);
            chart.setDescription(null);
            chart.getLegend().setEnabled(false);
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }

    private List<BarEntry> convertDataBySetting(List<CategoriesAndExpenses> rawData, PeriodSetting setting) {
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

    private IAxisValueFormatter getFormatterBySetting(PeriodSetting setting) {
        Locale locale = getResources().getConfiguration().locale;
        switch (setting) {
            case DAYS: {
                chart.getXAxis().setLabelCount(7, true);
                chart.getXAxis().setAxisMinimum(1);
                chart.getXAxis().setAxisMaximum(7);
                chart.getXAxis().setGranularity(1);
                return new DaysXAxisFormatter(locale);
            }
            case MONTHS: {
                chart.getXAxis().setLabelCount(12);
                chart.getXAxis().setAxisMinimum(0);
                chart.getXAxis().setAxisMaximum(11);
                chart.getXAxis().setGranularity(1);
                return new MonthXAxisFormatter(locale);
            }
            case YEARS: {
                chart.getXAxis().setLabelCount(5);
                chart.getXAxis().setGranularity(1);
                chart.getXAxis().resetAxisMaximum();
                chart.getXAxis().resetAxisMinimum();
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


    private List<String> getPeriodStrings() {
        List<String> result = new ArrayList<String>();
        PeriodSetting[] vals = PeriodSetting.values();
        for (PeriodSetting item : vals) {
            result.add(item.toString());
        }
        return result;
    }
}
