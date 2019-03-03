package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.DataReceiver;
import com.admin.budgetrook.wrappers.SummaryRequestWrapper;
import com.admin.budgetrook.wrappers.SummaryWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchSummaryDataTask extends AsyncTask<Void, Void, Object> {

    private DataReceiver listener;
    private String id;
    private SummaryRequestWrapper wrapper;

    public FetchSummaryDataTask(Context context, String id, SummaryRequestWrapper wrapper) {
        listener = (DataReceiver) context;
        this.id = id;
        this.wrapper = wrapper;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        SummaryWrapper result = new SummaryWrapper();
        PrefsHelper prefsHelper = PrefsHelper.getInstance();
        AppDatabase appDatabase = AppDatabase.getInstance(listener.getContext());

        Map<String, String> categoriesMap = new HashMap<String, String>();
        List<CategoryEntity> categories = appDatabase.categoryDao().getAll(prefsHelper.getCurrentUserId(listener.getContext()));
        for (CategoryEntity category : categories) {
            categoriesMap.put(Long.toString(category.getUid()), category.getName());
        }
        result.categoriesMap = categoriesMap;

        result.expenses = appDatabase.expenseDao().getExpensesBetweenDatesAndCategories(
                prefsHelper.getCurrentUserId(listener.getContext()),
                wrapper.from,
                wrapper.to,
                wrapper.categoryIds);

        result.sum = appDatabase.expenseDao().getExpensesBetweenDatesAndCategoriesSum(
                prefsHelper.getCurrentUserId(listener.getContext()),
                wrapper.from,
                wrapper.to,
                wrapper.categoryIds);

        return result;
    }

    @Override
    protected void onPostExecute(Object data) {
        listener.receiveData(data, id);
    }
}
