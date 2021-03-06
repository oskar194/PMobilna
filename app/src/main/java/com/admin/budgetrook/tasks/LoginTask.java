package com.admin.budgetrook.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.admin.budgetrook.apis.AccountApi;
import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginTask extends AsyncTask<AccountEntity, Void, String> {

    public LoginTask(Activity context) {
        listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String responseMessage;
    private Exception e;

    @Override
    protected void onPreExecute() {
        listener.startTask();
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(AccountEntity... accountEntities) {
        try {
            AccountApi api = ApiHelper.getClient(listener.getContext()).create(AccountApi.class);
            AccountEntity accountEntity = accountEntities[0];
            Call<ResponseBody> call = api.login(accountEntity.getLogin(), accountEntity.getPassword());
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                responseMessage = "Remote login success";
                PrefsHelper.getInstance().loginUser(listener.getContext(), accountEntity);
            } else {
                responseMessage = "Remote login failed";
            }
        } catch (Exception e) {
            this.e = e;
            responseMessage = "Remote login failed";
        }
        return responseMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        if (e != null) {
            Log.e("budgetrook", e.getMessage(), e.getCause());
            if (e instanceof NoConnectivityException) {
                responseMessage = "Network unavailable";
            }
        }
        listener.showMessage(s);
        listener.loaderOff();
        listener.finishTask();
    }
}
