package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admin.budgetrook.R;
import com.admin.budgetrook.XAxisFormatters.DateXAxisFormatter;
import com.admin.budgetrook.comparators.EntryXComparator;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineChartFragment extends Fragment {
    private List<CategoriesAndExpenses> chartData;
    private ChartFragmentInterface mListener;

    private LineChart chart;


    public LineChartFragment() {
        // Required empty public constructor
    }

    public static LineChartFragment newInstance() {
        LineChartFragment fragment = new LineChartFragment();
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
        View v = inflater.inflate(R.layout.fragment_line_chart, container, false);
        chart = (LineChart) v.findViewById(R.id.line_chart_fragment_chart);


        mListener.getExpensesForLine();
        return v;
    }

    public void pushData(List<CategoriesAndExpenses> categoriesAndExpenses) {
        if (mListener != null) {
            if(categoriesAndExpenses != null && categoriesAndExpenses.isEmpty()){
                return;
            }
            chartData = categoriesAndExpenses;
            List<Entry> entries = new ArrayList<Entry>();
            Map<Date, BigDecimal> values = new HashMap<Date, BigDecimal>();
            for (CategoriesAndExpenses item : chartData) {
                List<ExpenseEntity> expenses = item.getExpenses();
                BigDecimal aggregate = new BigDecimal(0);
                for (ExpenseEntity expense : expenses) {
                    if(values.get(expense.getDate()) != null){
                        values.put(expense.getDate(), values.get(expense.getDate()).add(expense.getAmount()));
                    } else {
                        values.put(expense.getDate(), expense.getAmount());
                    }
                }
            }
            for(Date key : values.keySet()){
                entries.add(new Entry(key.getTime(), values.get(key).floatValue()));
            }
            Collections.sort(entries, new EntryXComparator());
            LineDataSet data = new LineDataSet(entries, "Expenses");
            data.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            data.setDrawFilled(true);
            XAxis axis = chart.getXAxis();
            axis.setValueFormatter(new DateXAxisFormatter());
            axis.setLabelCount(5, true);
            chart.setData(new LineData(data));
            chart.invalidate();
            chart.setDescription(null);
            chart.getLegend().setEnabled(false);
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
}
