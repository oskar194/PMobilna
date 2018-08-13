package com.admin.budgetrook;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
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

public class MenuActivity extends Activity {

    private static PieChart chart;
    private Long allAmount = 0L;
    private String msg = "BudgetRookApp: ";
    private TextView notificationMessage;
    private LinearLayout notificationLayout;

    @Override
    protected void onResume() {
        super.onResume();
        new FetchChartDataTask().execute();
        new SetupNotificationTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        chart = (PieChart) findViewById(R.id.chart);
        notificationMessage = (TextView)findViewById(R.id.notification_tv);
        notificationLayout = (LinearLayout)findViewById(R.id.notification_layout);
    }

    private float getCategoryPercent(List<ExpenseEntity> categoryExpenses) {
        Long categorySum = 0L;
        for (ExpenseEntity expense : categoryExpenses) {
            categorySum += expense.getAmount();
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
            List<CategoriesAndExpenses> categoriesAndExpenses =
                    AppDatabase.getInstance(getApplicationContext()).categoriesAndExpensesDao().getAll();
            Log.d("BUDGETROOK", "categoriesAndExpenses" + categoriesAndExpenses.toString());
            allAmount = AppDatabase.getInstance(getApplicationContext()).expenseDao().expensesSum();
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
            Integer expensesToReview = AppDatabase.getInstance(getApplicationContext()).expenseDao().getNumberOfExpensesToReview();
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

}
