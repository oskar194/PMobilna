package com.admin.budgetrook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.helpers.FileChooserHelper;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.tasks.CreateCsvFileTask;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderActivity {

    private static final String TAG = "budgetrook";

    private static final int GET_DIRCTORY_REQUEST = 43;

    private static PieChart chart;
    private Long allAmount = 0L;
    private String msg = "BudgetRookApp: ";
    private TextView notificationMessage;
    private LinearLayout notificationLayout;

    private DrawerLayout mDrawerLayout;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;

    private Button addExpenseBtn;
    private Button analyticsBtn;
    private Button myExpensesBtn;

    private FrameLayout progressBarHolder;

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
        progressBarHolder = (FrameLayout) findViewById(R.id.menu_progressBarHolder);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addExpenseBtn = (Button) findViewById(R.id.addExpense);
        analyticsBtn = (Button) findViewById(R.id.analyticsBtn);
        myExpensesBtn = (Button) findViewById(R.id.myExpensesBtn);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        ds.setSliceSpace(5);
        ds.setColors(new int[]{
                R.color.color1,
                R.color.color2,
                R.color.color3,
                R.color.color4,
                R.color.color5,
                R.color.color6
        }, getApplicationContext());
        chart.setCenterText("Expenses percentage");
        chart.setTransparentCircleAlpha(30);
        chart.setData(new PieData(ds));
        chart.invalidate();
        chart.setDescription(null);
        chart.setEntryLabelColor(R.color.labelsEntry);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // set item as selected to persist highlight
        // close drawer when item is tapped
        Log.d(TAG, "onNavigationItemSelected: menuItemId: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.nav_export: {
                try {
                    startActivityForResult(FileChooserHelper.createFile(this, "text/csv"), GET_DIRCTORY_REQUEST);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "You don't have any application for creating documents!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onNavigationItemSelected: ", e);
                }
                break;
            }
            case R.id.nav_logout : {
                PrefsHelper.getInstance().logoutUser(this);
                finish();
                break;
            }
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;

    }

    @Override
    public void finishTask() {

    }

    @Override
    public void startTask() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_DIRCTORY_REQUEST) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                File file = FileChooserHelper.handleDirectoryChoice(
                        data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
                if (file == null) {
                    Toast.makeText(this,
                            "Can't create the file in selected directory",
                            Toast.LENGTH_SHORT).show();
                } else {
                    new CreateCsvFileTask(this, file).execute();
                }
            }
        }
    }

    @Override
    public void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        addExpenseBtn.setEnabled(false);
        myExpensesBtn.setEnabled(false);
        analyticsBtn.setEnabled(false);
    }

    @Override
    public void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        addExpenseBtn.setEnabled(true);
        myExpensesBtn.setEnabled(true);
        analyticsBtn.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
