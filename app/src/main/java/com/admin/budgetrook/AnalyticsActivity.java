package com.admin.budgetrook;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.admin.budgetrook.adapters.AnalyticsAdapter;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.fragments.BarChartFragment;
import com.admin.budgetrook.fragments.LineChartFragment;
import com.admin.budgetrook.fragments.RadarChartFragment;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.admin.budgetrook.interfaces.DataReceiver;
import com.admin.budgetrook.tasks.FetchChartDataTask;
import com.admin.budgetrook.tasks.GetExpensesWithinDatesTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalyticsActivity extends FragmentActivity implements
        ChartFragmentInterface, DataReceiver {

    public static final String LINE = "line";
    public static final String BAR = "bar";
    public static final String RADAR = "radar";
    private AnalyticsAdapter adapter;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        adapter = new AnalyticsAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void getDataWithinDates(Date from, Date to) {
        new GetExpensesWithinDatesTask(from, to, this, BAR).execute();
    }

    @Override
    public void getExpensesForLine() {
        new FetchChartDataTask(this, LINE).execute();
    }

    @Override
    public void getExpensesForRadar() {
        new FetchChartDataTask(this, RADAR).execute();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void receiveData(Object data, String id) {
        switch (id) {
            case LINE: {
                ((LineChartFragment) adapter.instantiateItem(viewPager, 0)).pushData((List<CategoriesAndExpenses>) data);
                break;
            }
            case BAR: {
                ((BarChartFragment) adapter.instantiateItem(viewPager, 1)).pushData((List<ExpenseEntity>) data);
                break;
            }
            case RADAR: {
                ((RadarChartFragment) adapter.instantiateItem(viewPager, 2)).setup((List<CategoriesAndExpenses>) data);
                break;
            }
        }
    }
}