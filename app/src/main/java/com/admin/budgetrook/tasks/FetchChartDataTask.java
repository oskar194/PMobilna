package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.DataReceiver;

import java.util.ArrayList;
import java.util.List;

public class FetchChartDataTask extends AsyncTask<Void, Void, List<CategoriesAndExpenses>> {
    private DataReceiver listener;
    private String id;

    public FetchChartDataTask(Context context, String id) {
        listener = (DataReceiver) context;
        this.id = id;
    }

    @Override
    protected List<CategoriesAndExpenses> doInBackground(Void... params) {
        Long accountId = PrefsHelper.getInstance().getCurrentUserId(listener.getContext());
        List<CategoriesAndExpenses> result = AppDatabase.getInstance(listener.getContext())
                .categoriesAndExpensesDao().getAll(accountId);
        for (CategoriesAndExpenses item : result) {
            List<ExpenseEntity> filtered = new ArrayList<ExpenseEntity>();
            for (ExpenseEntity expenseEntity : item.getExpenses()) {
                if (expenseEntity.isReviewed()) {
                    filtered.add(expenseEntity);
                }
            }
            item.setExpenses(filtered);
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<CategoriesAndExpenses> categoriesAndExpenses) {
        listener.receiveData(categoriesAndExpenses, id);
    }
}