package com.admin.budgetrook;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "budgetrook";

    private static PieChart chart;
    private Long allAmount = 0L;
    private String msg = "BudgetRookApp: ";
    private TextView notificationMessage;
    private LinearLayout notificationLayout;

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: HI");
        new FetchChartDataTask().execute();
        new SetupNotificationTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: HI");
        setContentView(R.layout.activity_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        chart = (PieChart) findViewById(R.id.chart);
        notificationMessage = (TextView) findViewById(R.id.notification_tv);
        notificationLayout = (LinearLayout) findViewById(R.id.notification_layout);
    }

    private float getCategoryPercent(List<ExpenseEntity> categoryExpenses) {
        float categorySum = 0L;
        for (ExpenseEntity expense : categoryExpenses) {
            categorySum += expense.getAmount().floatValue();
        }
        if (categorySum == 0L) {
            return 0F;
        }
        return (categorySum * 100.0f) / allAmount;
    }

    private void setupChart(List<CategoriesAndExpenses> categoriesAndExpenses) {
        List<PieEntry> entries = new ArrayList<PieEntry>();
        for (CategoriesAndExpenses element : categoriesAndExpenses) {
            float categorySum = getCategoryPercent(element.getExpenses());
            Log.d("BUDGETROOK", "categorySum" + element.getCategory().getName() + ": " + categorySum);
            if (categorySum == 0L) {
                continue;
            }
            entries.add(new PieEntry(categorySum, element.getCategory().getName()));
        }
        PieDataSet ds = new PieDataSet(entries, "Expenses");
        ds.setSliceSpace(10);
        ds.setColors(new int[]{R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark}, getApplicationContext());
        chart.setData(new PieData(ds));
        chart.invalidate();
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
    }

    public void addExpense(View view) {
        startActivity(new Intent(this, NewExpenseActivity.class));
    }

    public void goToMyExpenses(View view) {
        startActivity(new Intent(this, MyExpensesActivity.class));
    }

    public void goToAnalytics(View view) {
        startActivity(new Intent(this, AnalyticsActivity.class));
    }

    private class FetchChartDataTask extends AsyncTask<Void, Void, List<CategoriesAndExpenses>> {
        @Override
        protected List<CategoriesAndExpenses> doInBackground(Void... params) {
            Long accountId = PrefsHelper.getInstance().getCurrentUserId(getApplicationContext());
            List<CategoriesAndExpenses> categoriesAndExpenses =
                    AppDatabase.getInstance(getApplicationContext()).categoriesAndExpensesDao().getAll(accountId);
            Log.d("BUDGETROOK", "categoriesAndExpenses" + categoriesAndExpenses.toString());
            allAmount = AppDatabase.getInstance(getApplicationContext()).expenseDao().expensesSum(accountId);
            Log.d("BUDGETROOK", "allAmount: " + allAmount);
            return categoriesAndExpenses;
        }

        @Override
        protected void onPostExecute(List<CategoriesAndExpenses> categoriesAndExpenses) {
            if (categoriesAndExpenses == null || categoriesAndExpenses.isEmpty()) {
                return;
            }
            setupChart(categoriesAndExpenses);
        }
    }

    private class SetupNotificationTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Long accountId = PrefsHelper.getInstance().getCurrentUserId(getApplicationContext());
            Integer expensesToReview = AppDatabase.getInstance(getApplicationContext()).expenseDao().getNumberOfExpensesToReview(accountId);
            return expensesToReview;
        }

        @Override
        protected void onPostExecute(Integer expensesToReview) {
            buildNotificationMessage(expensesToReview);
        }
    }

    private void buildNotificationMessage(Integer expensesToReview) {
        if (expensesToReview == 0) {
            notificationLayout.setVisibility(View.INVISIBLE);
        } else {
            notificationMessage.setText("You currently have: " + expensesToReview + " expenses to review");
            notificationLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
