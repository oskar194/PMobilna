package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.DbHelper;
import com.admin.budgetrook.apis.ImageApi;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.wrappers.ImageWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostImageTask extends AsyncTask<ImageEntity, Void, String> {

    private static final String TAG = "budgetrook";

    public PostImageTask(Context context) {
        listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String message;
    private Exception e;
    private ImageEntity imageEntity;

    @Override
    protected void onPreExecute() {
        listener.startTask();
    }

    @Override
    protected String doInBackground(final ImageEntity... imageEntities) {
        try {
            String username = PrefsHelper.getInstance().getCurrentUserLogin(listener.getContext());
            AppDatabase db = DbHelper.getDb(listener.getContext());
            imageEntity = imageEntities[0];
            Log.d(TAG, "PostImageTask doInBackground: imageEntity " + imageEntity.toString());
            ExpenseEntity expenseEntity = db.expenseDao().getById(imageEntity.getExpenseId(), imageEntity.getAccountId());
            Log.d(TAG, "PostImageTask doInBackground: expenseEntity " + expenseEntity.toString());
            ImageWrapper imageWrapper = new ImageWrapper(imageEntity, username);
            imageWrapper.expenseId = expenseEntity.getExternalId();
            ImageApi api = ApiHelper.getClient(listener.getContext()).create(ImageApi.class);
            Call<ImageWrapper> call = api.postImage(imageWrapper);
            Response<ImageWrapper> response = call.execute();
            if (response.isSuccessful()) {
                message = "Image posted successfully";
                ImageWrapper wrapper = response.body();
                imageEntity.setExternalId(wrapper.externalId);
                db.imageDao().update(imageEntity);
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
        new PostAndProcessImageTask(listener.getContext()).execute(imageEntity);
        listener.showMessage(s);
        listener.finishTask();
    }
}
