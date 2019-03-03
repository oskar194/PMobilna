package com.admin.budgetrook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.admin.budgetrook.adapters.AnalyticsAdapter;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.fragments.BarChartFragment;
import com.admin.budgetrook.fragments.LineChartFragment;
import com.admin.budgetrook.fragments.RadarChartFragment;
import com.admin.budgetrook.fragments.SummaryFragment;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.admin.budgetrook.interfaces.DataReceiver;
import com.admin.budgetrook.interfaces.MultiSelectListener;
import com.admin.budgetrook.tasks.FetchChartDataTask;
import com.admin.budgetrook.tasks.FetchSummaryConfigTask;
import com.admin.budgetrook.tasks.FetchSummaryDataTask;
import com.admin.budgetrook.tasks.GetExpensesWithinDatesTask;
import com.admin.budgetrook.wrappers.SummaryRequestWrapper;
import com.admin.budgetrook.wrappers.SummaryWrapper;

import java.util.Date;
import java.util.List;

public class AnalyticsActivity extends FragmentActivity implements
        ChartFragmentInterface, DataReceiver, MultiSelectListener {

    public static final String LINE = "line";
    public static final String BAR = "bar";
    public static final String RADAR = "radar";
    public static final String SUMMARY_REQUEST = "summary-request";
    public static final String SUMMARY_SETUP = "summary-setup";
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
    public void getSetupDataForSummary() {
        new FetchSummaryConfigTask(this, SUMMARY_SETUP).execute();
    }

    @Override
    public void getExpensesForSummary(SummaryRequestWrapper request) {
        new FetchSummaryDataTask(this, SUMMARY_REQUEST, request).execute();
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
            case SUMMARY_REQUEST: {
                ((SummaryFragment) adapter.instantiateItem(viewPager, 3)).setup((SummaryWrapper) data);
                break;
            }
            case SUMMARY_SETUP: {
                ((SummaryFragment) adapter.instantiateItem(viewPager, 3)).setupConfig((SummaryWrapper) data);
                break;
            }
        }
    }

    @Override
    public void onClickOk(List<String> dialog, boolean[] selectedItems) {
        ((SummaryFragment) adapter.instantiateItem(viewPager, 3)).okClickedInDialog(selectedItems);
    }

    @Override
    public void onClickCancel() {

    }
}