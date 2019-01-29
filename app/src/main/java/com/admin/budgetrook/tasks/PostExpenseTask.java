package com.admin.budgetrook.tasks;

import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.util.Log;

import com.admin.budgetrook.DbHelper;
import com.admin.budgetrook.apis.ExpenseApi;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.wrappers.ExpenseWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostExpenseTask extends AsyncTask<ExpenseEntity, Void, String> {
    private static final String TAG = "budgetrook";

    public PostExpenseTask(Context context, ImageEntity imageEntity) {
        listener = (LoaderActivity) context;
        this.imageEntity = imageEntity;
    }

    private LoaderActivity listener;
    private String message;
    private Exception e;
    private ImageEntity imageEntity;

    @Override
    protected void onPreExecute() {
        listener.startTask();
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(ExpenseEntity... expenseEntities) {
        try {
            String username = PrefsHelper.getInstance().getCurrentUserLogin(listener.getContext());
            ExpenseEntity expenseEntity = expenseEntities[0];
            Log.d(TAG, "doInBackground: expenseEntity "+ expenseEntity.toString());
            CategoryEntity categoryEntity = DbHelper.getDb(listener.getContext()).
                    categoryDao().getById(expenseEntity.getCategoryId(), expenseEntity.getAccountId());
            Log.d(TAG, "doInBackground: categoryEntity "+ categoryEntity.toString());
            ExpenseWrapper expenseWrapper = new ExpenseWrapper(expenseEntity, username, categoryEntity.getName());
            ExpenseApi api = ApiHelper.getClient(listener.getContext()).create(ExpenseApi.class);
            Call<ExpenseWrapper> call = api.postExpense(expenseWrapper);
            Response<ExpenseWrapper> response = call.execute();
            if (response.isSuccessful()) {
                message = "Expense posted successfully";
                ExpenseWrapper wrapper = response.body();
                expenseEntity.setSynchronized(true);
                expenseEntity.setExternalId(wrapper.externalId);
                DbHelper.getDb(listener.getContext()).expenseDao().update(expenseEntity);
            } else {
                message = "Remote unavailable";
            }
        } catch (Exception e) {
            this.e = e;
            if (e instanceof NoConnectivityException) {
                message = "Network unavailable";
            }
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        if (e != null) {
            Log.e(TAG, "onPostExecute: ", e);
            listener.loaderOff();
            listener.showMessage(s);
            listener.finishTask();
            return;
        }
        new PostImageTask(listener.getContext()).execute(imageEntity);
        listener.showMessage(s);
        listener.finishTask();

    }
}
