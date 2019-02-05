package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.admin.budgetrook.R;
import com.admin.budgetrook.XAxisFormatters.IndexedDatesXAxisFormatter;
import com.admin.budgetrook.customDataSets.ColoredMaxDataSet;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartFragment extends Fragment {

    public static final String ENTRIES = "entries";
    public static final String DATES = "dates";
    public static final String EXPENSES = "Expenses";
    private List<ExpenseEntity> chartData;
    private ChartFragmentInterface mListener;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private BarChart chart;

    private Button fromButton;
    private Button toButton;
    private Button okButton;

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
        View v = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        fromButton = v.findViewById(R.id.bar_chart_from_button);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                Date d = new Date();
                try {
                    d = dateFormat.parse(fromButton.getText().toString());
                } catch (Exception e) {

                }
                final Calendar c = Calendar.getInstance();
                c.setTime(d);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        toButton = v.findViewById(R.id.bar_chart_to_button);
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;

                Date d = new Date();
                try {
                    d = dateFormat.parse(toButton.getText().toString());
                } catch (Exception e) {

                }
                final Calendar c = Calendar.getInstance();
                c.setTime(d);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                toButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        okButton = v.findViewById(R.id.bar_chart_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mListener.getDataWithinDates(dateFormat.parse(fromButton.getText().toString()), dateFormat.parse(toButton.getText().toString()));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Add both dates", Toast.LENGTH_SHORT).show();
                }
            }
        });
        chart = v.findViewById(R.id.bar_chart_fragment_chart);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        fromButton.setText(dateFormat.format(calendar.getTime()));
        toButton.setText(dateFormat.format(new Date()));
        mListener.getDataWithinDates(calendar.getTime(), new Date());
        return v;
    }

    public void pushData(List<ExpenseEntity> expenseEntities) {
        if (expenseEntities != null && expenseEntities.isEmpty()) {
            return;
        }
        Map<String, Object> entries = convertData(expenseEntities);
        ColoredMaxDataSet dataSet = new ColoredMaxDataSet((List<BarEntry>) entries.get(ENTRIES), EXPENSES);
        chart.getXAxis().setValueFormatter(new IndexedDatesXAxisFormatter((List<Date>) entries.get(DATES), dateFormat));
        if (dataSet.getEntryCount() > 5) {
            chart.getXAxis().setLabelCount(5, true);
        } else {
            chart.getXAxis().setLabelCount(dataSet.getEntryCount(), true);
        }
        dataSet.setColors(getResources().getColor(R.color.color4), getResources().getColor(R.color.maxValue));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);
        chart.setData(barData);
        chart.setFitBars(true);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private Map<String, Object> convertData(List<ExpenseEntity> expenseEntities) {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<Date> dates = new ArrayList<Date>();
        Map<Date, BigDecimal> aggregate = new HashMap<Date, BigDecimal>();
        for (ExpenseEntity expense : expenseEntities) {
            if (aggregate.get(expense.getDate()) != null) {
                aggregate.put(expense.getDate(),
                        aggregate.get(expense.getDate()).add(expense.getAmount()));
            } else {
                aggregate.put(expense.getDate(), expense.getAmount());
            }
        }
        int index = 0;
        for (Date key : aggregate.keySet()) {
            dates.add(key);
            entries.add(new BarEntry(index, aggregate.get(key).floatValue()));
            index++;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ENTRIES, entries);
        result.put(DATES, dates);
        return result;
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
}
