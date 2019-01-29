package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.wrappers.AggregatedExpense;
import com.admin.budgetrook.wrappers.ExpenseWrapper;
import com.admin.budgetrook.wrappers.ImageWrapper;

public class AggregatedExpensePostTask extends AsyncTask<AggregatedExpense, Void, String> {

    public AggregatedExpensePostTask(Context context) {
        this.listener = (LoaderActivity) listener;
    }

    private LoaderActivity listener;
    private String message;

    @Override
    protected void onPreExecute() {
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(AggregatedExpense... aggregatedExpenses) {
        AggregatedExpense aggregatedExpense = aggregatedExpenses[0];
        ImageEntity imageEntity = aggregatedExpense.getImageEntity();
        ExpenseEntity expenseEntity = aggregatedExpense.getExpenseEntity();

        String username = PrefsHelper.getInstance().getCurrentUserLogin(listener.getContext());

        ExpenseWrapper expenseWrapper = new ExpenseWrapper(expenseEntity, username, aggregatedExpense.getCategoryName());
        expenseWrapper.images.add(new ImageWrapper(imageEntity, username));


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.loaderOff();
        listener.showMessage(s);
    }
}
