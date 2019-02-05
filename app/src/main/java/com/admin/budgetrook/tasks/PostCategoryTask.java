package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.DbHelper;
import com.admin.budgetrook.apis.CategoryApi;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.wrappers.CategoryWrapper;

import retrofit2.Call;
import retrofit2.Response;

public class PostCategoryTask extends AsyncTask<CategoryEntity, Void, String> {
    private static final String TAG = "budgetrook";

    public PostCategoryTask(Context context) {
        this.listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String message;
    private Exception e;

    @Override
    protected void onPreExecute() {
        listener.startTask();
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(CategoryEntity... categoryEntities) {
        try {
            String username = PrefsHelper.getInstance().getCurrentUserLogin(listener.getContext());
            Long accountId = PrefsHelper.getInstance().getCurrentUserId(listener.getContext());
            CategoryEntity entity = categoryEntities[0];
            AppDatabase db = DbHelper.getDb(listener.getContext());

            entity.setAccountId(accountId);
            long id = db.categoryDao().insert(entity);
            entity.setUid(id);

            CategoryApi api = ApiHelper.getClient(listener.getContext()).create(CategoryApi.class);
            Call<CategoryWrapper> call = api.postCategory(new CategoryWrapper(entity, username));
            Response<CategoryWrapper> response = call.execute();
            if (response.isSuccessful()) {
                message = "Category posted successfully";
                CategoryWrapper wrapper = response.body();
                entity.setExternalId(wrapper.externalId);
                entity.setSynchronized(true);
                db.categoryDao().updateAll(entity);
            } else {
                message = "Remote unavailable";
            }

        } catch (Exception e) {
            this.e = e;
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        if (e != null) {
            Log.e(TAG, "onPostExecute: ", e);
            if (e instanceof NoConnectivityException) {
                message = "Network unavailable";
            }
        }
        listener.showMessage(message);
        listener.loaderOff();
        listener.finishTask();
    }
}
