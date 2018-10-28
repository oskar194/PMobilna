package com.admin.budgetrook;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admin.budgetrook.adapters.AnalyticsAdapter;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;

import java.util.List;

public class AnalyticsActivity extends FragmentActivity implements ChartFragmentInterface {
    private AnalyticsAdapter adapter;
    private ViewPager viewPager;

    private List<CategoriesAndExpenses> chartData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        new FetchChartDataTask().execute();
        adapter = new AnalyticsAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public List<CategoriesAndExpenses> getData() {
        Log.d("BUDGETROOK" , "getData chartData " + chartData);
        return chartData;
    }

    private class FetchChartDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            chartData = AppDatabase.getInstance(getApplicationContext())
                    .categoriesAndExpensesDao().getAll();
            Log.d("BUDGETROOK" , "chartData " + chartData);
            return null;
        }
    }
}