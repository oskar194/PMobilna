package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.admin.budgetrook.R;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.helpers.DateSplitHelper;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.admin.budgetrook.helpers.DateSplitHelper.getKeyBySetting;

public class RadarChartFragment extends Fragment {

    private List<CategoriesAndExpenses> chartData;
    private ChartFragmentInterface mListener;

    public static final String TAG = "BUDGETROOK";

    private RadarChart chart;
    private Map<Integer, String> categoriesMap = new HashMap<Integer, String>();

    public RadarChartFragment() {
        // Required empty public constructor
    }

    public static RadarChartFragment newInstance() {
        RadarChartFragment fragment = new RadarChartFragment();
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
        View v = inflater.inflate(R.layout.fragment_radar_chart, container, false);
        chart = (RadarChart) v.findViewById(R.id.radar_chart_fragment_chart);
        setup();
        return v;
    }

    public void setup() {
        if (mListener != null) {
            chartData = mListener.getData();
            List<IRadarDataSet> dataSets = convertToEntries(chartData);
            Log.d(TAG, "setup: dataSets " + dataSets.toString());
            RadarData data = new RadarData(dataSets);
            data.setLabels(new ArrayList<String>(categoriesMap.values()));
            chart.setData(data);
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return categoriesMap.get((int) value % categoriesMap.values().size());
                }
            });
            xAxis.setXOffset(0f);
            xAxis.setYOffset(0f);
            xAxis.setTextSize(8f);
            YAxis yAxis = chart.getYAxis();
            yAxis.setLabelCount(0);

            chart.setDescription(null);
            chart.invalidate();
        }
    }

    private List<IRadarDataSet> convertToEntries(List<CategoriesAndExpenses> chartData) {
        List<IRadarDataSet> radarDataSets = new ArrayList<IRadarDataSet>();
        int counter = 0;
        List<Integer> years = new ArrayList<Integer>();
        for (CategoriesAndExpenses item : chartData) {
            categoriesMap.put(counter, item.getCategory().getName());
            for (ExpenseEntity expense : item.getExpenses()) {
                Integer expenseYear = getKeyBySetting(expense, DateSplitHelper.PeriodSetting.YEARS);
                if (!years.contains(expenseYear)) {
                    years.add(expenseYear);
                }
            }
            counter++;
        }
        for (Integer year : years) {
            List<RadarEntry> entries = getEntriesForYear(chartData, year);
            RadarDataSet singlePeriod = new RadarDataSet(entries, "Year: " + year);
            singlePeriod.setColor(generateRandomColor());
            singlePeriod.setDrawFilled(true);
            radarDataSets.add(singlePeriod);
        }
        return radarDataSets;
    }

    private int generateRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private List<RadarEntry> getEntriesForYear(List<CategoriesAndExpenses> chartData, int year) {
        int counter = 0;
        float summed = 0;
        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        for (CategoriesAndExpenses item : chartData) {
            for (ExpenseEntity expense : item.getExpenses()) {
                if (year != getKeyBySetting(expense, DateSplitHelper.PeriodSetting.YEARS)) {
                    continue;
                }
                summed += expense.getAmount().floatValue();
            }
            entries.add(new RadarEntry(summed, counter));
            counter++;
            summed = 0;
        }
        return entries;
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
