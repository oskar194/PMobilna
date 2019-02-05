package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admin.budgetrook.R;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RadarChartFragment extends Fragment {

    private List<CategoriesAndExpenses> chartData;
    private ChartFragmentInterface mListener;

    public static final String TAG = "BUDGETROOK";

    private RadarChart chart;
    List<String> categoriesNames = new ArrayList<String>();

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
        mListener.getExpensesForRadar();
        return v;
    }

    public void setup(List<CategoriesAndExpenses> categoriesAndExpenses) {
        if (mListener != null) {
            if(categoriesAndExpenses != null && categoriesAndExpenses.isEmpty()){
                return;
            }
            chartData = categoriesAndExpenses;
            chart.getLegend().setEnabled(false);

            List<IRadarDataSet> dataSets = getAllEntries(chartData);
            Log.d(TAG, "setup: dataSets " + dataSets.toString());
            RadarData data = new RadarData(dataSets);
            data.setLabels(categoriesNames);
            Log.d(TAG, "setup: " + categoriesNames.toString());
            chart.setData(data);
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return categoriesNames.get((int) value % categoriesNames.size());
                }
            });
            xAxis.setXOffset(0f);
            xAxis.setYOffset(0f);
            xAxis.setTextSize(15f);

            YAxis yAxis = chart.getYAxis();
            yAxis.setEnabled(false);

            chart.setDrawWeb(false);
            chart.setDescription(null);
            chart.invalidate();
        }
    }

    private List<IRadarDataSet> getAllEntries(List<CategoriesAndExpenses> chartData) {
        List<IRadarDataSet> radarDataSets = new ArrayList<IRadarDataSet>();
        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        Log.d(TAG, "getAllEntries: " + chartData.toString());
        for (CategoriesAndExpenses item : chartData) {
            BigDecimal categorySum = new BigDecimal(0);
            categoriesNames.add(item.getCategory().getName());
            for (ExpenseEntity expense : item.getExpenses()) {
                categorySum = categorySum.add(expense.getAmount());
            }
            entries.add(new RadarEntry(categorySum.floatValue()));
        }
        RadarDataSet allPeriods = new RadarDataSet(entries, "All periods");
        allPeriods.setDrawFilled(true);
        radarDataSets.add(allPeriods);
        return radarDataSets;
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
